"""Configurable pipeline stage registry.

The transform stages a transaction passes through are data-driven: the
orchestrator reads an ordered list of stage names and runs the matching
components, instead of hard-coding ``validator -> fraud -> settlement``. This
keeps the pipeline flexible — stages can be reordered, removed, or extended by
editing ``config/pipeline.json`` (or passing ``--stages``) without touching the
orchestrator body.

A stage is any component exposing ``process_message(message) -> message`` and is
*persistence-agnostic*: it only reads and mutates the in-memory message dict and
never touches protocol files. That keeps this module compatible both with the
current file-based integrator and with the REST-owned-persistence refactor, where
the orchestrator sources/sinks messages through the runtime API but still runs
the same transform chain via :func:`run_stages`.

The reporting component is intentionally *not* a configurable stage: it is the
terminal result sink whose output other surfaces (MCP reader, REST results) rely
on, so the orchestrator always runs it after the configured stages.
"""

from __future__ import annotations

import json
from pathlib import Path
from typing import Any, Callable

from agents import fraud_detector, settlement_processor, transaction_validator

# A stage is a pure message processor: it takes a message and returns the message.
Stage = Callable[[dict[str, Any]], dict[str, Any]]

# Name -> component. Register a new agent here (and list it in config/pipeline.json
# or pass it via --stages) to add it to the pipeline.
STAGE_REGISTRY: dict[str, Stage] = {
    "transaction_validator": transaction_validator.process_message,
    "fraud_detector": fraud_detector.process_message,
    "settlement_processor": settlement_processor.process_message,
}

# Order used when no config file and no override is supplied. Matches the
# historical hard-coded chain so default runs are unchanged.
DEFAULT_STAGES: tuple[str, ...] = (
    "transaction_validator",
    "fraud_detector",
    "settlement_processor",
)

CONFIG_RELATIVE_PATH = Path("config") / "pipeline.json"


def available_stages() -> list[str]:
    """Return the registered stage names, sorted for stable display."""
    return sorted(STAGE_REGISTRY)


def validate_stages(stages: list[str]) -> list[str]:
    """Return ``stages`` unchanged, or raise ``ValueError`` naming the problem.

    Rejects an empty list and any name not present in :data:`STAGE_REGISTRY`.
    """
    if not stages:
        raise ValueError("Pipeline must define at least one stage.")
    unknown = [name for name in stages if name not in STAGE_REGISTRY]
    if unknown:
        raise ValueError(
            f"Unknown pipeline stage(s): {', '.join(unknown)}. "
            f"Available: {', '.join(available_stages())}."
        )
    return stages


def load_stages(base_dir: Path = Path(".")) -> list[str]:
    """Load the configured stage order from ``<base_dir>/config/pipeline.json``.

    Falls back to :data:`DEFAULT_STAGES` when the config file is absent. Raises
    ``ValueError`` for malformed JSON, a missing/invalid ``stages`` list, or an
    unknown stage name.
    """
    config_path = Path(base_dir) / CONFIG_RELATIVE_PATH
    if not config_path.is_file():
        return list(DEFAULT_STAGES)
    try:
        config = json.loads(config_path.read_text(encoding="utf-8"))
    except json.JSONDecodeError as exc:
        raise ValueError(f"Invalid pipeline config JSON: {config_path}") from exc
    stages = config.get("stages") if isinstance(config, dict) else None
    if not isinstance(stages, list) or not all(isinstance(name, str) for name in stages):
        raise ValueError(f"'stages' in {config_path} must be a list of stage names.")
    return validate_stages(stages)


def resolve_stages(
    override: list[str] | None = None,
    base_dir: Path = Path("."),
) -> list[str]:
    """Pick the stage list: an explicit ``override`` wins, else the config/default."""
    if override is not None:
        return validate_stages(list(override))
    return load_stages(base_dir)


def run_stages(message: dict[str, Any], stages: list[str]) -> dict[str, Any]:
    """Run ``message`` through each named stage in order and return the result."""
    for name in stages:
        message = STAGE_REGISTRY[name](message)
    return message

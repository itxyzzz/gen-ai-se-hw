from __future__ import annotations

import json
from pathlib import Path

import pytest

from agents.pipeline import (
    DEFAULT_STAGES,
    available_stages,
    load_stages,
    resolve_stages,
    run_stages,
    validate_stages,
)
from integrator import run_pipeline


def test_available_stages_lists_registered_components() -> None:
    assert set(available_stages()) == {
        "transaction_validator",
        "fraud_detector",
        "settlement_processor",
    }


def test_validate_stages_rejects_unknown_name() -> None:
    with pytest.raises(ValueError, match="Unknown pipeline stage"):
        validate_stages(["transaction_validator", "does_not_exist"])


def test_validate_stages_rejects_empty_list() -> None:
    with pytest.raises(ValueError, match="at least one stage"):
        validate_stages([])


def test_load_stages_falls_back_to_default_without_config(tmp_path: Path) -> None:
    assert load_stages(tmp_path) == list(DEFAULT_STAGES)


def test_load_stages_reads_config_file(tmp_path: Path) -> None:
    (tmp_path / "config").mkdir()
    (tmp_path / "config" / "pipeline.json").write_text(
        json.dumps({"stages": ["transaction_validator", "settlement_processor"]}),
        encoding="utf-8",
    )
    assert load_stages(tmp_path) == ["transaction_validator", "settlement_processor"]


def test_load_stages_rejects_unknown_stage_in_config(tmp_path: Path) -> None:
    (tmp_path / "config").mkdir()
    (tmp_path / "config" / "pipeline.json").write_text(
        json.dumps({"stages": ["bogus_stage"]}), encoding="utf-8"
    )
    with pytest.raises(ValueError, match="Unknown pipeline stage"):
        load_stages(tmp_path)


def test_resolve_stages_override_wins_over_config(tmp_path: Path) -> None:
    (tmp_path / "config").mkdir()
    (tmp_path / "config" / "pipeline.json").write_text(
        json.dumps({"stages": ["transaction_validator"]}), encoding="utf-8"
    )
    assert resolve_stages(["fraud_detector"], tmp_path) == ["fraud_detector"]


def test_run_stages_runs_components_in_order() -> None:
    calls: list[str] = []

    def make(name: str):
        def stage(message: dict) -> dict:
            calls.append(name)
            return message
        return stage

    import agents.pipeline as pipeline

    registry = {"a": make("a"), "b": make("b")}
    original = pipeline.STAGE_REGISTRY
    pipeline.STAGE_REGISTRY = registry
    try:
        run_stages({"data": {}}, ["b", "a", "b"])
    finally:
        pipeline.STAGE_REGISTRY = original

    assert calls == ["b", "a", "b"]


def test_pipeline_with_validator_only_skips_settlement(sample_input: Path, tmp_path: Path) -> None:
    # Dropping fraud + settlement leaves validated transactions unsettled.
    summary = run_pipeline(
        base_dir=tmp_path,
        input_path=sample_input,
        stages=["transaction_validator"],
    )
    assert summary["settled"] == 0
    assert summary["review_required"] == 0
    # The two malformed sample records are still rejected by the validator.
    assert summary["rejected"] == 2

    txn001 = json.loads((tmp_path / "shared/results/TXN001.json").read_text(encoding="utf-8"))
    assert txn001["status"] == "validated"
    assert "settlement_reference" not in txn001

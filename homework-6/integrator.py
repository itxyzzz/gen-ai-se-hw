"""Integrator for the generated Python transaction-processing pipeline."""

from __future__ import annotations

import argparse
import json
from pathlib import Path
import shutil
from typing import Any
from uuid import uuid4

from agents.common import (
    COMPONENT_INTEGRATOR,
    COMPONENT_VALIDATOR,
    GENERATED_CODE_RUN_ID,
    PIPELINE_VERSION,
    REASON_PROCESSING_ERROR,
    SOURCE_SPEC_RUN_ID,
    SOURCE_SPEC_SHA256,
    create_message,
    get_protocol_paths,
    safe_json_dump,
    safe_json_load,
    utc_now,
)
from agents import reporting_agent, transaction_validator
from agents.pipeline import (
    DEFAULT_STAGES,
    available_stages,
    resolve_stages,
    run_stages,
    validate_stages,
)


def _base_and_shared_name(shared_dir: Path) -> tuple[Path, str]:
    shared = Path(shared_dir)
    if shared.is_absolute():
        return shared.parent, shared.name
    if shared.parent == Path("."):
        return Path("."), shared.name
    return shared.parent, shared.name


def archive_existing_shared(base_dir: Path, shared_dir_name: str = "shared") -> Path | None:
    paths = get_protocol_paths(base_dir, shared_dir_name)
    shared = paths["shared"]
    if not shared.exists():
        return None
    archive_root = paths["archive"]
    archive_root.mkdir(parents=True, exist_ok=True)
    index = 1
    while True:
        destination = archive_root / f"{shared.name}-{index:03d}"
        if not destination.exists():
            shutil.copytree(shared, destination)
            return destination
        index += 1


def prepare_shared_directories(base_dir: Path, shared_dir_name: str = "shared") -> dict[str, Path]:
    archive_existing_shared(base_dir, shared_dir_name)
    paths = get_protocol_paths(base_dir, shared_dir_name)
    for key in ("input", "processing", "output", "results"):
        paths[key].mkdir(parents=True, exist_ok=True)
    return paths


def write_run_provenance(paths: dict[str, Path], provenance: dict[str, Any]) -> Path:
    safe = {
        "schema_version": 1,
        "runtime_run_id": provenance["runtime_run_id"],
        "generated_at": provenance["generated_at"],
        "stack": "python",
        "source_spec_run_id": SOURCE_SPEC_RUN_ID,
        "source_spec_path": "docs/agent-runs/20260621-220037-write-spec-python-hera-python-full-set/agent-1-spec/outputs/specification.md",
        "source_spec_sha256": SOURCE_SPEC_SHA256,
        "generated_code_run_id": GENERATED_CODE_RUN_ID,
        "pipeline_version": PIPELINE_VERSION,
        "output_inventory_path": "docs/agent-runs/20260621-222543-generate-code-python-hera-python-full-set/agent-2-code/outputs/inventory.md",
    }
    return safe_json_dump(safe, paths["run_provenance"])


def load_transactions(input_path: Path) -> list[dict[str, Any]]:
    try:
        records = json.loads(Path(input_path).read_text(encoding="utf-8"))
    except FileNotFoundError:
        raise FileNotFoundError(f"Input file not found: {input_path}") from None
    except json.JSONDecodeError as exc:
        raise ValueError(f"Input file is not valid JSON: line {exc.lineno}") from exc
    if not isinstance(records, list):
        raise ValueError("Input JSON must be a list of transaction records.")
    return records


def seed_input_messages(transactions: list[dict[str, Any]], paths: dict[str, Path]) -> list[Path]:
    message_paths: list[Path] = []
    for index, transaction in enumerate(transactions, start=1):
        transaction_id = str(transaction.get("transaction_id") or f"UNKNOWN-{index:03d}")
        metadata = transaction.get("metadata") if isinstance(transaction.get("metadata"), dict) else {}
        safe_transaction = {
            "transaction_id": transaction_id,
            "timestamp": transaction.get("timestamp", ""),
            "source_account": "PRESENT",
            "destination_account": "PRESENT",
            "amount": transaction.get("amount", ""),
            "currency": transaction.get("currency", ""),
            "transaction_type": transaction.get("transaction_type", ""),
            "destination_pattern": "review"
            if str(transaction.get("destination_account", "")).endswith("9999")
            else "standard",
            "metadata": {
                "channel": metadata.get("channel", "unknown"),
                "country": metadata.get("country", "unknown"),
            },
        }
        message = create_message(
            source_agent=COMPONENT_INTEGRATOR,
            target_agent=COMPONENT_VALIDATOR,
            data=safe_transaction,
        )
        path = paths["input"] / f"{transaction_id}.json"
        safe_json_dump(message, path)
        message_paths.append(path)
    return message_paths


def _transaction_id_from_message(message: dict[str, Any], fallback: str) -> str:
    data = message.get("data", {}) if isinstance(message, dict) else {}
    return str(data.get("transaction_id") or fallback)


def process_transaction(
    message_path: Path,
    paths: dict[str, Path],
    stages: list[str] | None = None,
) -> dict[str, Any]:
    active_stages = list(DEFAULT_STAGES) if stages is None else stages
    processing_path = paths["processing"] / Path(message_path).name
    message = safe_json_load(message_path)
    safe_json_dump(message, processing_path)
    message = run_stages(message, active_stages)
    output_path = paths["output"] / processing_path.name
    safe_json_dump(message, output_path)
    reporting_agent.process_message(message, paths["results"])
    return message


def safe_process_transaction(
    message_path: Path,
    paths: dict[str, Path],
    stages: list[str] | None = None,
) -> dict[str, Any]:
    fallback_id = Path(message_path).stem
    try:
        return process_transaction(message_path, paths, stages)
    except Exception:
        transaction_id = fallback_id
        try:
            if Path(message_path).exists():
                transaction_id = _transaction_id_from_message(safe_json_load(message_path), fallback_id)
        except Exception:
            transaction_id = fallback_id
        reporting_agent.write_error_result(transaction_id, REASON_PROCESSING_ERROR, paths["results"])
        return {"data": {"transaction_id": transaction_id, "status": "error", "reason_codes": [REASON_PROCESSING_ERROR]}}


def run_pipeline(
    base_dir: Path = Path("."),
    input_path: Path = Path("sample-transactions.json"),
    shared_dir_name: str = "shared",
    stages: list[str] | None = None,
) -> dict[str, Any]:
    runtime_run_id = str(uuid4())
    paths = prepare_shared_directories(base_dir, shared_dir_name)
    write_run_provenance(paths, {"runtime_run_id": runtime_run_id, "generated_at": utc_now()})
    active_stages = resolve_stages(stages, base_dir)
    transactions = load_transactions(input_path)
    expected_ids = [str(item.get("transaction_id") or f"UNKNOWN-{index:03d}") for index, item in enumerate(transactions, start=1)]
    message_paths = seed_input_messages(transactions, paths)
    for path in message_paths:
        safe_process_transaction(path, paths, active_stages)
    summary = reporting_agent.summarize_results(paths["results"], expected_ids, runtime_run_id)
    pipeline_status = reporting_agent.build_pipeline_status(summary)
    safe_json_dump(pipeline_status, paths["results"] / "pipeline-status.json")
    return summary


def validate_transactions_only(input_path: Path, base_dir: Path | None = None) -> list[dict[str, Any]]:
    del base_dir
    records = load_transactions(input_path)
    return [
        {
            "transaction_id": result["transaction_id"],
            "status": result["status"],
            "reason_codes": result["reason_codes"],
        }
        for result in (transaction_validator.validate_transaction(record) for record in records)
    ]


def main() -> int:
    parser = argparse.ArgumentParser(description="Run the educational transaction-processing pipeline.")
    parser.add_argument("--input", default="sample-transactions.json", help="Input JSON transaction file.")
    parser.add_argument("--shared-dir", default="shared", help="Shared protocol directory.")
    parser.add_argument(
        "--stages",
        default=None,
        help="Comma-separated stage names overriding config/pipeline.json "
        "(e.g. transaction_validator,settlement_processor).",
    )
    parser.add_argument(
        "--list-stages",
        action="store_true",
        help="Print available and configured stages, then exit.",
    )
    args = parser.parse_args()
    shared_dir = Path(args.shared_dir)
    base_dir, shared_name = _base_and_shared_name(shared_dir)

    if args.list_stages:
        print("Available stages:", ", ".join(available_stages()))
        print("Configured stages:", ", ".join(resolve_stages(None, base_dir)))
        return 0

    override = [name.strip() for name in args.stages.split(",")] if args.stages else None
    if override is not None:
        try:
            validate_stages(override)
        except ValueError as exc:
            parser.error(str(exc))
    summary = run_pipeline(
        base_dir=base_dir,
        input_path=Path(args.input),
        shared_dir_name=shared_name,
        stages=override,
    )
    print(
        "Pipeline complete: "
        f"total={summary['total_records']} settled={summary['settled']} "
        f"rejected={summary['rejected']} review_required={summary['review_required']} error={summary['error']}"
    )
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

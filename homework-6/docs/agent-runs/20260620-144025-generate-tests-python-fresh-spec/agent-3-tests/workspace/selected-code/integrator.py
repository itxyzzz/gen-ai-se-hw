"""Integrator for the educational transaction-processing pipeline."""

from __future__ import annotations

import argparse
from datetime import UTC, datetime
import hashlib
import json
from pathlib import Path
import shutil
import sys
from typing import Any
from uuid import uuid4

from agents import fraud_detector, settlement_processor, transaction_validator
from agents.common import (
    ALLOWED_FINAL_STATUSES,
    COMPONENT_FAILURE,
    MALFORMED_JSON,
    MISSING_TRANSACTION_ID,
    PIPELINE_STAGE_ERROR,
    PipelineError,
    assert_no_sensitive_fields,
    create_audit_event,
    create_message,
    read_json_file,
    sanitize_text,
    write_json_file,
)

PROTOCOL_DIRS = ("input", "processing", "output", "results")
ARCHIVE_PREFIX = "shared-"
SIMULATION_NOTICE = (
    "Educational simulation only; no real payment, banking, legal, AML, sanctions, "
    "KYC, PCI, or payment-network compliance determination is performed."
)
SOURCE_SPEC_RUN_ID = "20260619-170102-write-spec-python-fresh"
SOURCE_SPEC_PATH = "homework-6/specification.md"
SOURCE_SPEC_SHA256 = "44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B"
PIPELINE_VERSION_RUN_ID = "20260619-175211-generate-code-python-fresh-spec"
PIPELINE_INVENTORY_PATH = (
    "homework-6/docs/agent-runs/20260619-175211-generate-code-python-fresh-spec/"
    "agent-2-code/outputs/inventory.md"
)


def utc_now() -> str:
    return datetime.now(UTC).replace(microsecond=0).isoformat().replace("+00:00", "Z")


def next_archive_path(archive_root: Path) -> Path:
    archive_root = Path(archive_root)
    highest = 0
    if archive_root.exists():
        for path in archive_root.iterdir():
            if path.is_dir() and path.name.startswith(ARCHIVE_PREFIX):
                suffix = path.name.removeprefix(ARCHIVE_PREFIX)
                if suffix.isdigit():
                    highest = max(highest, int(suffix))
    while True:
        highest += 1
        candidate = archive_root / f"{ARCHIVE_PREFIX}{highest:03d}"
        if not candidate.exists():
            return candidate


def archive_existing_shared(shared_dir: Path) -> Path | None:
    shared_dir = Path(shared_dir)
    if not shared_dir.exists():
        return None
    if not shared_dir.is_dir():
        raise PipelineError("SHARED_PATH_NOT_DIRECTORY")
    archive_root = shared_dir.parent / "archive"
    archive_root.mkdir(parents=True, exist_ok=True)
    destination = next_archive_path(archive_root)
    shutil.move(str(shared_dir), str(destination))
    return destination


def prepare_shared_directories(base_dir: Path, reset: bool = True) -> dict[str, Path]:
    shared_dir = Path(base_dir)
    if reset:
        archive_existing_shared(shared_dir)
    directories = {name: shared_dir / name for name in PROTOCOL_DIRS}
    for directory in directories.values():
        directory.mkdir(parents=True, exist_ok=True)
    return directories


def fingerprint_file(path: Path) -> str:
    target = Path(path)
    if not target.exists():
        return "UNAVAILABLE"
    digest = hashlib.sha256()
    with target.open("rb") as handle:
        for block in iter(lambda: handle.read(65536), b""):
            digest.update(block)
    return digest.hexdigest().upper()


def write_run_provenance(
    shared_dir: Path,
    *,
    runtime_run_id: str,
    spec_path: Path | None = None,
    inventory_path: Path | None = None,
) -> dict[str, Any]:
    spec_path = spec_path or Path("specification.md")
    inventory_path = inventory_path or Path("docs/agent-runs") / PIPELINE_VERSION_RUN_ID / "agent-2-code" / "outputs" / "inventory.md"
    provenance = {
        "schema_version": "1.0",
        "runtime_run_id": runtime_run_id,
        "generated_at": utc_now(),
        "source_spec": {
            "run_id": SOURCE_SPEC_RUN_ID,
            "path": SOURCE_SPEC_PATH,
            "sha256": SOURCE_SPEC_SHA256,
            "observed_sha256": fingerprint_file(spec_path),
        },
        "pipeline_version": {
            "run_id": PIPELINE_VERSION_RUN_ID,
            "inventory_path": PIPELINE_INVENTORY_PATH,
            "package_sha256": fingerprint_file(inventory_path),
        },
    }
    assert_no_sensitive_fields(provenance)
    write_json_file(Path(shared_dir) / "run-provenance.json", provenance)
    return provenance


def load_transactions(input_path: Path) -> list[dict[str, Any]]:
    try:
        data = json.loads(Path(input_path).read_text(encoding="utf-8"))
    except FileNotFoundError as exc:
        raise PipelineError("INPUT_NOT_FOUND") from exc
    except json.JSONDecodeError as exc:
        raise PipelineError(MALFORMED_JSON) from exc
    if not isinstance(data, list):
        raise PipelineError("INVALID_INPUT_SHAPE")
    seen: set[str] = set()
    transactions: list[dict[str, Any]] = []
    for item in data:
        if not isinstance(item, dict):
            raise PipelineError("INVALID_INPUT_SHAPE")
        transaction_id = item.get("transaction_id")
        if not isinstance(transaction_id, str) or not transaction_id:
            raise PipelineError(MISSING_TRANSACTION_ID)
        if transaction_id in seen:
            raise PipelineError("DUPLICATE_TRANSACTION_ID")
        seen.add(transaction_id)
        transactions.append(dict(item))
    return transactions


def _stage_filename(index: int, transaction_id: str, suffix: str | None = None) -> str:
    safe_id = transaction_id or "UNKNOWN"
    if suffix:
        return f"{index:03d}-{safe_id}-{suffix}.json"
    return f"{index:03d}-{safe_id}.json"


def write_input_messages(transactions: list[dict[str, Any]], input_dir: Path) -> list[dict[str, Any]]:
    messages = []
    for index, transaction in enumerate(transactions, start=1):
        message = create_message(transaction, "integrator", "transaction_validator")
        write_json_file(Path(input_dir) / _stage_filename(index, str(transaction["transaction_id"])), message)
        messages.append(message)
    return messages


def write_stage_message(directory: Path, index: int, transaction_id: str, component_name: str, payload: dict[str, Any]) -> Path:
    path = Path(directory) / _stage_filename(index, transaction_id, component_name)
    write_json_file(path, payload)
    return path


def build_error_result(transaction: dict[str, Any], reason_code: str = PIPELINE_STAGE_ERROR) -> dict[str, Any]:
    transaction_id = str(transaction.get("transaction_id") or "UNKNOWN")
    result = {
        "schema_version": "1.0",
        "transaction_id": transaction_id,
        "status": "error",
        "reason_codes": [reason_code],
        "risk_score": 0,
        "risk_level": "not_scored",
        "amount": str(transaction.get("amount", "")),
        "currency": str(transaction.get("currency", "")),
        "component_history": [],
        "processed_at": utc_now(),
        "safe_summary": {"error": reason_code},
        "audit_events": [create_audit_event("integrator", transaction_id, "error", reason_code)],
    }
    assert_no_sensitive_fields(result)
    return result


def safe_error_message(exc: Exception) -> str:
    if isinstance(exc, PipelineError):
        return exc.reason_code
    return sanitize_text(COMPONENT_FAILURE)


def process_transaction(
    transaction: dict[str, Any],
    directories: dict[str, Path],
    *,
    index: int = 1,
    input_message: dict[str, Any] | None = None,
) -> dict[str, Any]:
    transaction_id = str(transaction.get("transaction_id") or "UNKNOWN")
    try:
        message = input_message or create_message(transaction, "integrator", "transaction_validator")
        validated = transaction_validator.process_message(message)
        write_stage_message(directories["processing"], index, transaction_id, "transaction-validator", validated)
        if validated.get("validation", {}).get("is_valid"):
            scored = fraud_detector.process_message(validated)
            write_stage_message(directories["output"], index, transaction_id, "fraud-detector", scored)
            settled = settlement_processor.process_message(scored)
        else:
            settled = settlement_processor.process_message(validated)
        write_stage_message(directories["output"], index, transaction_id, "settlement-processor", settled)
        result = settlement_processor.build_final_result(settled)
        result["processed_at"] = utc_now()
        assert result["status"] in ALLOWED_FINAL_STATUSES
        assert_no_sensitive_fields(result)
        return result
    except Exception as exc:
        return build_error_result(transaction, safe_error_message(exc))


def write_result(result: dict[str, Any], results_dir: Path) -> Path:
    assert result.get("status") in ALLOWED_FINAL_STATUSES
    path = Path(results_dir) / f"{result['transaction_id']}.json"
    write_json_file(path, result)
    return path


def summarize_results(results: list[dict[str, Any]]) -> dict[str, Any]:
    counts = {status: 0 for status in sorted(ALLOWED_FINAL_STATUSES)}
    result_files = []
    for result in sorted(results, key=lambda item: item["transaction_id"]):
        status = result.get("status") if result.get("status") in counts else "error"
        counts[status] += 1
        result_files.append(f"{result['transaction_id']}.json")
    return {
        "schema_version": "1.0",
        "runtime_run_id": "",
        "total_transactions": len(results),
        "settled": counts["settled"],
        "rejected": counts["rejected"],
        "review_required": counts["review_required"],
        "error": counts["error"],
        "result_files": result_files,
        "generated_at": utc_now(),
        "simulation_notice": SIMULATION_NOTICE,
    }


def write_summary(results: list[dict[str, Any]], results_dir: Path, runtime_run_id: str) -> dict[str, Any]:
    summary = summarize_results(results)
    summary["runtime_run_id"] = runtime_run_id
    write_json_file(Path(results_dir) / "summary.json", summary)
    status = {
        key: summary[key]
        for key in (
            "schema_version",
            "runtime_run_id",
            "total_transactions",
            "settled",
            "rejected",
            "review_required",
            "error",
            "generated_at",
        )
    }
    write_json_file(Path(results_dir) / "pipeline-status.json", status)
    return summary


def run_pipeline(
    *,
    input_path: Path = Path("sample-transactions.json"),
    shared_dir: Path = Path("shared"),
    spec_path: Path = Path("specification.md"),
    inventory_path: Path | None = None,
    validate_only: bool = False,
) -> dict[str, Any]:
    runtime_run_id = str(uuid4())
    directories = prepare_shared_directories(shared_dir, reset=True)
    write_run_provenance(shared_dir, runtime_run_id=runtime_run_id, spec_path=spec_path, inventory_path=inventory_path)
    transactions = load_transactions(input_path)
    input_messages = write_input_messages(transactions, directories["input"])

    if validate_only:
        dry_run = transaction_validator.validate_transactions_dry_run(transactions)
        results = []
        for item in dry_run["results"]:
            status = "rejected" if item["status"] == "rejected" else "settled"
            result = {
                "schema_version": "1.0",
                "transaction_id": item["transaction_id"],
                "status": status,
                "reason_codes": item["reason_codes"],
                "risk_score": 0,
                "risk_level": "not_scored",
                "amount": item["amount"],
                "currency": item["currency"],
                "component_history": [],
                "processed_at": utc_now(),
                "safe_summary": {"validation_only": True, "settlement_reference": None},
                "audit_events": [],
            }
            write_result(result, directories["results"])
            results.append(result)
    else:
        results = []
        for index, (transaction, message) in enumerate(zip(transactions, input_messages, strict=True), start=1):
            result = process_transaction(transaction, directories, index=index, input_message=message)
            write_result(result, directories["results"])
            results.append(result)
    return write_summary(results, directories["results"], runtime_run_id)


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description="Run the educational transaction-processing pipeline.")
    parser.add_argument("--input", default="sample-transactions.json", help="Path to input transactions JSON.")
    parser.add_argument("--shared-dir", default="shared", help="Path to shared protocol directory.")
    parser.add_argument("--spec-path", default="specification.md", help="Path to selected canonical specification.")
    parser.add_argument("--inventory-path", default=None, help="Path to selected pipeline inventory.")
    parser.add_argument("--validate-only", action="store_true", help="Validate records without risk scoring or settlement.")
    args = parser.parse_args(argv)
    try:
        summary = run_pipeline(
            input_path=Path(args.input),
            shared_dir=Path(args.shared_dir),
            spec_path=Path(args.spec_path),
            inventory_path=Path(args.inventory_path) if args.inventory_path else None,
            validate_only=args.validate_only,
        )
    except PipelineError as exc:
        print(f"Pipeline setup failed: {exc.reason_code}", file=sys.stderr)
        return 2
    except OSError:
        print("Pipeline setup failed: FILESYSTEM_ERROR", file=sys.stderr)
        return 2
    print(
        "Pipeline complete: "
        f"total={summary['total_transactions']} "
        f"settled={summary['settled']} "
        f"rejected={summary['rejected']} "
        f"review_required={summary['review_required']} "
        f"error={summary['error']}"
    )
    return 0


if __name__ == "__main__":
    raise SystemExit(main())


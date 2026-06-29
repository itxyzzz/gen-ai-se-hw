"""Integrator for the educational transaction-processing pipeline."""

from __future__ import annotations

import argparse
import json
from pathlib import Path
import shutil
import sys
from typing import Any
from uuid import uuid4

from agents import fraud_detector, settlement_processor, transaction_validator
from agents.common import (
    MALFORMED_JSON,
    PIPELINE_ERROR,
    PipelineError,
    audit_event,
    redact_account_id,
    utc_now,
    write_json_file,
)

PROTOCOL_DIRS = ("input", "processing", "output", "results")
ARCHIVE_PREFIX = "shared-"
SIMULATION_NOTICE = (
    "Educational simulation only; no real payment, banking, legal, AML, sanctions, "
    "KYC, PCI, or payment-network compliance determination is performed."
)


def _next_archive_dir(archive_root: Path) -> Path:
    highest_id = 0
    if archive_root.exists():
        for path in archive_root.iterdir():
            if not path.is_dir() or not path.name.startswith(ARCHIVE_PREFIX):
                continue
            suffix = path.name.removeprefix(ARCHIVE_PREFIX)
            if suffix.isdigit():
                highest_id = max(highest_id, int(suffix))
    candidate_id = highest_id + 1
    while True:
        candidate = archive_root / f"{ARCHIVE_PREFIX}{candidate_id:03d}"
        if not candidate.exists():
            return candidate
        candidate_id += 1


def archive_existing_shared(base_dir: Path) -> Path | None:
    base_dir = Path(base_dir)
    if not base_dir.exists():
        return None
    if not base_dir.is_dir():
        raise PipelineError("SHARED_PATH_NOT_DIRECTORY")

    archive_root = base_dir.parent / "archive"
    archive_root.mkdir(parents=True, exist_ok=True)
    destination = _next_archive_dir(archive_root)
    shutil.move(str(base_dir), str(destination))
    return destination


def prepare_shared_directories(base_dir: Path, reset: bool = True) -> dict[str, Path]:
    base_dir = Path(base_dir)
    if reset:
        archive_existing_shared(base_dir)
    directories = {name: base_dir / name for name in PROTOCOL_DIRS}
    for directory in directories.values():
        directory.mkdir(parents=True, exist_ok=True)
    return directories


def load_transactions(input_path: Path) -> list[dict[str, Any]]:
    try:
        raw = json.loads(Path(input_path).read_text(encoding="utf-8"))
    except FileNotFoundError as exc:
        raise PipelineError("INPUT_NOT_FOUND") from exc
    except json.JSONDecodeError as exc:
        raise PipelineError(MALFORMED_JSON) from exc
    if not isinstance(raw, list):
        raise PipelineError("INVALID_INPUT_SHAPE")
    transactions: list[dict[str, Any]] = []
    for item in raw:
        if not isinstance(item, dict):
            raise PipelineError("INVALID_INPUT_SHAPE")
        transactions.append(dict(item))
    return transactions


def _safe_transaction_id(transaction: dict[str, Any]) -> str:
    value = transaction.get("transaction_id")
    if isinstance(value, str) and value:
        return value
    return f"UNKNOWN-{uuid4()}"


def build_message_envelope(
    transaction: dict[str, Any],
    source_agent: str,
    target_agent: str,
    message_type: str = "transaction",
) -> dict[str, Any]:
    metadata = transaction.get("metadata") if isinstance(transaction.get("metadata"), dict) else {}
    data = {
        "transaction_id": transaction.get("transaction_id"),
        "timestamp": transaction.get("timestamp"),
        "amount": transaction.get("amount"),
        "currency": transaction.get("currency"),
        "transaction_type": transaction.get("transaction_type"),
        "channel": metadata.get("channel"),
        "country": metadata.get("country"),
        "source_account_redacted": redact_account_id(transaction.get("source_account")),
        "destination_account_redacted": redact_account_id(transaction.get("destination_account")),
        "status": "received",
    }
    return {
        "message_id": str(uuid4()),
        "schema_version": "1.0",
        "created_at": utc_now(),
        "source_agent": source_agent,
        "target_agent": target_agent,
        "message_type": message_type,
        "transaction_id": transaction.get("transaction_id"),
        "data": data,
        "component_history": [{"component": source_agent, "timestamp": utc_now()}],
        "audit_events": [
            audit_event(source_agent, str(transaction.get("transaction_id") or "UNKNOWN"), "received")
        ],
    }


def _write_stage(directories: dict[str, Path], folder: str, transaction_id: str, name: str, payload: dict[str, Any]) -> None:
    write_json_file(directories[folder] / f"{transaction_id}-{name}.json", payload)


def _result_from_message(message: dict[str, Any]) -> dict[str, Any]:
    data = message.get("data", {})
    validation = message.get("validation", {})
    risk = message.get("risk", {})
    settlement = message.get("settlement", {})
    status = settlement.get("status") or data.get("status") or "error"
    reason_codes = []
    if validation.get("reason_code"):
        reason_codes.append(validation["reason_code"])
    reason_codes.extend(risk.get("risk_reasons", []))
    if settlement.get("reason_code"):
        reason_codes.append(settlement["reason_code"])
    return {
        "schema_version": "1.0",
        "transaction_id": str(data.get("transaction_id") or message.get("transaction_id") or "UNKNOWN"),
        "status": status,
        "reason_code": reason_codes[0] if reason_codes else None,
        "reason_codes": reason_codes,
        "amount": data.get("amount"),
        "currency": data.get("currency"),
        "transaction_type": data.get("transaction_type"),
        "risk_score": risk.get("risk_score", 0),
        "risk_level": risk.get("risk_level", "not_scored"),
        "risk_reasons": risk.get("risk_reasons", []),
        "settlement_reference": settlement.get("settlement_reference"),
        "settlement_note": settlement.get("settlement_note"),
        "component_history": message.get("component_history", []),
        "audit": message.get("audit_events", []),
        "generated_at": utc_now(),
        "simulation_notice": SIMULATION_NOTICE,
    }


def _safe_error_result(transaction: dict[str, Any], exc: Exception) -> dict[str, Any]:
    transaction_id = _safe_transaction_id(transaction)
    reason = exc.reason_code if isinstance(exc, PipelineError) else PIPELINE_ERROR
    return {
        "schema_version": "1.0",
        "transaction_id": transaction_id,
        "status": "error",
        "reason_code": reason,
        "reason_codes": [reason],
        "amount": str(transaction.get("amount", "")),
        "currency": str(transaction.get("currency", "")),
        "transaction_type": str(transaction.get("transaction_type", "")),
        "risk_score": 0,
        "risk_level": "not_scored",
        "risk_reasons": [],
        "settlement_reference": None,
        "settlement_note": "pipeline_error",
        "component_history": [],
        "audit": [audit_event("integrator", transaction_id, "error", reason)],
        "generated_at": utc_now(),
        "simulation_notice": SIMULATION_NOTICE,
    }


def process_transaction(transaction: dict[str, Any], directories: dict[str, Path]) -> dict[str, Any]:
    transaction_id = _safe_transaction_id(transaction)
    try:
        message = build_message_envelope(transaction, "integrator", "transaction_validator")
        write_json_file(directories["input"] / f"{transaction_id}.json", message)

        validated = transaction_validator.process_message(message)
        _write_stage(directories, "processing", transaction_id, "transaction-validator", validated)
        if not validated["validation"]["is_valid"]:
            settled_rejection = settlement_processor.process_message(validated)
            _write_stage(directories, "output", transaction_id, "settlement-processor", settled_rejection)
            return _result_from_message(settled_rejection)

        scored = fraud_detector.process_message(validated)
        _write_stage(directories, "processing", transaction_id, "fraud-detector", scored)
        settled = settlement_processor.process_message(scored)
        _write_stage(directories, "output", transaction_id, "settlement-processor", settled)
        return _result_from_message(settled)
    except Exception as exc:
        return _safe_error_result(transaction, exc)


def write_result(result: dict[str, Any], results_dir: Path) -> Path:
    result_path = Path(results_dir) / f"{result['transaction_id']}.json"
    write_json_file(result_path, result)
    return result_path


def summarize_results(results: list[dict[str, Any]], results_dir: Path) -> dict[str, Any]:
    counts = {"settled": 0, "rejected": 0, "review_required": 0, "error": 0}
    safe_summaries = []
    audit_events = []
    for result in sorted(results, key=lambda item: item["transaction_id"]):
        status = result.get("status")
        if status not in counts:
            status = "error"
        counts[status] += 1
        safe_summaries.append(
            {
                "transaction_id": result["transaction_id"],
                "status": status,
                "reason_codes": result.get("reason_codes", []),
                "risk_level": result.get("risk_level"),
                "result_file": f"{result['transaction_id']}.json",
            }
        )
        audit_events.extend(result.get("audit", []))

    summary = {
        "schema_version": "1.0",
        "total_transactions": len(results),
        "settled": counts["settled"],
        "rejected": counts["rejected"],
        "review_required": counts["review_required"],
        "error": counts["error"],
        "results": safe_summaries,
        "generated_at": utc_now(),
        "simulation_notice": SIMULATION_NOTICE,
    }
    write_json_file(Path(results_dir) / "summary.json", summary)
    write_json_file(Path(results_dir) / "pipeline-status.json", {k: summary[k] for k in ("schema_version", "total_transactions", "settled", "rejected", "review_required", "error", "generated_at")})
    write_json_file(Path(results_dir) / "audit.json", {"schema_version": "1.0", "events": audit_events})
    return summary


def _run_validation_only(transactions: list[dict[str, Any]], directories: dict[str, Path]) -> list[dict[str, Any]]:
    results = []
    for transaction in transactions:
        message = build_message_envelope(transaction, "integrator", "transaction_validator")
        validated = transaction_validator.process_message(message)
        result = _result_from_message(validated)
        if result["status"] != "rejected":
            result["status"] = "settled"
            result["settlement_reference"] = None
            result["settlement_note"] = "validation_only_no_settlement"
        results.append(result)
        write_result(result, directories["results"])
    return results


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description="Run the educational transaction-processing pipeline.")
    parser.add_argument("--input", default="sample-transactions.json", help="Path to input transactions JSON.")
    parser.add_argument("--shared-dir", default="shared", help="Path to shared protocol directory.")
    parser.add_argument("--validate-only", action="store_true", help="Validate records without risk scoring or settlement.")
    args = parser.parse_args(argv)

    try:
        directories = prepare_shared_directories(Path(args.shared_dir), reset=True)
        transactions = load_transactions(Path(args.input))
        if args.validate_only:
            results = _run_validation_only(transactions, directories)
        else:
            results = []
            for transaction in transactions:
                result = process_transaction(transaction, directories)
                write_result(result, directories["results"])
                results.append(result)
        summary = summarize_results(results, directories["results"])
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

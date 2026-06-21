"""Reporting component that writes privacy-safe result artifacts."""

from __future__ import annotations

from collections import Counter
from pathlib import Path
from typing import Any

from .common import (
    COMPONENT_REPORTING,
    PIPELINE_VERSION,
    REASON_PROCESSING_ERROR,
    STATUS_ERROR,
    STATUS_REJECTED,
    STATUS_REVIEW_REQUIRED,
    STATUS_SETTLED,
    assert_no_raw_account_ids,
    append_audit_event,
    append_component_history,
    safe_json_dump,
    safe_json_load,
    utc_now,
)


def assert_privacy_safe(payload: dict[str, Any]) -> None:
    assert_no_raw_account_ids(payload)
    if "description" in payload:
        raise ValueError("Payload contains a raw transaction description field.")


def build_transaction_status_payload(message: dict[str, Any]) -> dict[str, Any]:
    data = message.get("data", {})
    status = data.get("status", STATUS_ERROR)
    payload = {
        "schema_version": 1,
        "transaction_id": data.get("transaction_id", "UNKNOWN"),
        "status": status,
        "reason_codes": list(data.get("reason_codes", [])),
        "amount": data.get("amount", ""),
        "currency": data.get("currency", ""),
        "safe_summary": "Simulated transaction outcome recorded.",
        "component_history_count": len(message.get("component_history", [])),
        "audit_event_count": len(message.get("audit_events", [])),
        "processed_at": utc_now(),
        "privacy_check": "passed",
    }
    if status == STATUS_SETTLED and data.get("settlement_reference"):
        payload["settlement_reference"] = data["settlement_reference"]
    if status == STATUS_REVIEW_REQUIRED:
        payload["safe_summary"] = "Simulated review outcome recorded."
    if status == STATUS_REJECTED:
        payload["safe_summary"] = "Simulated rejection outcome recorded."
    if status == STATUS_ERROR:
        payload["safe_summary"] = "Simulated error outcome recorded."
    assert_privacy_safe(payload)
    return payload


def write_transaction_result(message: dict[str, Any], results_dir: Path) -> Path:
    append_component_history(message, COMPONENT_REPORTING)
    append_audit_event(message, COMPONENT_REPORTING, message.get("data", {}).get("status", STATUS_ERROR))
    payload = build_transaction_status_payload(message)
    result_path = Path(results_dir) / f"{payload['transaction_id']}.json"
    safe_json_dump(payload, result_path)
    return result_path


def write_error_result(transaction_id: str, reason_code: str, results_dir: Path) -> Path:
    safe_id = transaction_id or "UNKNOWN"
    payload = {
        "schema_version": 1,
        "transaction_id": safe_id,
        "status": STATUS_ERROR,
        "reason_codes": [reason_code or REASON_PROCESSING_ERROR],
        "amount": "",
        "currency": "",
        "safe_summary": "Simulated error outcome recorded.",
        "component_history_count": 0,
        "audit_event_count": 1,
        "processed_at": utc_now(),
        "privacy_check": "passed",
    }
    assert_privacy_safe(payload)
    return safe_json_dump(payload, Path(results_dir) / f"{safe_id}.json")


def list_result_files(results_dir: Path) -> list[str]:
    return sorted(path.name for path in Path(results_dir).glob("TXN*.json"))


def summarize_results(
    results_dir: Path,
    expected_transaction_ids: list[str],
    runtime_run_id: str,
) -> dict[str, Any]:
    result_files = list_result_files(results_dir)
    records = [safe_json_load(Path(results_dir) / name) for name in result_files]
    status_counts = Counter(str(item.get("status", STATUS_ERROR)) for item in records)
    reason_counts = Counter(code for item in records for code in item.get("reason_codes", []))
    observed_ids = sorted(str(item.get("transaction_id", "")) for item in records)
    expected_ids = sorted(expected_transaction_ids)
    summary = {
        "schema_version": 1,
        "runtime_run_id": runtime_run_id,
        "generated_at": utc_now(),
        "total_records": len(records),
        "settled": status_counts.get(STATUS_SETTLED, 0),
        "rejected": status_counts.get(STATUS_REJECTED, 0),
        "review_required": status_counts.get(STATUS_REVIEW_REQUIRED, 0),
        "error": status_counts.get(STATUS_ERROR, 0),
        "status_counts": dict(sorted(status_counts.items())),
        "reason_code_counts": dict(sorted(reason_counts.items())),
        "expected_transaction_ids": expected_ids,
        "result_files": result_files,
        "privacy_check": "passed",
        "completeness_check": "passed" if observed_ids == expected_ids else "failed",
    }
    assert_privacy_safe(summary)
    safe_json_dump(summary, Path(results_dir) / "summary.json")
    return summary


def build_pipeline_status(summary: dict[str, Any]) -> dict[str, Any]:
    status = {
        "schema_version": 1,
        "pipeline_version": PIPELINE_VERSION,
        "run_status": "complete" if summary.get("completeness_check") == "passed" else "incomplete",
        "summary_path": "shared/results/summary.json",
        "total_records": summary.get("total_records", 0),
        "status_counts": summary.get("status_counts", {}),
        "reason_code_counts": summary.get("reason_code_counts", {}),
        "result_files": summary.get("result_files", []),
        "generated_at": utc_now(),
    }
    assert_privacy_safe(status)
    return status


def process_message(message: dict[str, Any], results_dir: Path) -> dict[str, Any]:
    write_transaction_result(message, results_dir)
    return message

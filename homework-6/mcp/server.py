"""Read-only FastMCP server for latest pipeline status."""

from __future__ import annotations

import json
from pathlib import Path
import re
from typing import Any

from fastmcp import FastMCP


mcp = FastMCP("pipeline-status")
PROJECT_ROOT = Path(__file__).resolve().parents[1]
RESULTS_DIR = PROJECT_ROOT / "shared" / "results"
TRANSACTION_ID_RE = re.compile(r"^[A-Za-z0-9_-]+$")
SIMULATION_NOTICE = (
    "Educational simulation only; no real payment, banking, legal, AML, sanctions, "
    "KYC, PCI, or payment-network compliance determination is performed."
)


def load_json(path: Path) -> dict[str, Any]:
    data = json.loads(Path(path).read_text(encoding="utf-8"))
    if not isinstance(data, dict):
        return {}
    return data


def _is_valid_transaction_id(transaction_id: str) -> bool:
    return isinstance(transaction_id, str) and bool(transaction_id) and bool(TRANSACTION_ID_RE.fullmatch(transaction_id))


def result_file_for(transaction_id: str, results_dir: Path = RESULTS_DIR) -> Path | None:
    if not _is_valid_transaction_id(transaction_id):
        return None
    return Path(results_dir) / f"{transaction_id}.json"


def safe_transaction_view(result: dict[str, Any]) -> dict[str, Any]:
    reason_codes = result.get("reason_codes", [])
    if not isinstance(reason_codes, list):
        reason_codes = []
    safe_summary = result.get("safe_summary", {})
    if not isinstance(safe_summary, dict):
        safe_summary = {}
    component_history = result.get("component_history", [])
    if not isinstance(component_history, list):
        component_history = []
    audit_events = result.get("audit_events", [])
    if not isinstance(audit_events, list):
        audit_events = []
    return {
        "transaction_id": result.get("transaction_id", ""),
        "status": result.get("status", "unknown"),
        "reason_codes": list(reason_codes),
        "risk_score": result.get("risk_score", 0),
        "risk_level": result.get("risk_level", "unknown"),
        "amount": result.get("amount", ""),
        "currency": result.get("currency", ""),
        "processed_at": result.get("processed_at", ""),
        "safe_summary": dict(safe_summary),
        "component_count": len(component_history),
        "audit_event_count": len(audit_events),
        "simulation_notice": SIMULATION_NOTICE,
    }


def get_transaction_status_payload(transaction_id: str, results_dir: Path = RESULTS_DIR) -> dict[str, Any]:
    result_path = result_file_for(transaction_id, results_dir)
    if result_path is None:
        return {
            "found": False,
            "transaction_id": transaction_id,
            "reason_code": "INVALID_TRANSACTION_ID",
            "simulation_notice": SIMULATION_NOTICE,
        }
    if not result_path.exists():
        return {
            "found": False,
            "transaction_id": transaction_id,
            "reason_code": "TRANSACTION_RESULT_NOT_FOUND",
            "simulation_notice": SIMULATION_NOTICE,
        }
    return {"found": True, **safe_transaction_view(load_json(result_path))}


def _summary_counts(summary: dict[str, Any], transaction_count: int) -> dict[str, Any]:
    return {
        "runtime_run_id": summary.get("runtime_run_id", ""),
        "generated_at": summary.get("generated_at", ""),
        "total_transactions": summary.get("total_transactions", transaction_count),
        "settled": summary.get("settled", 0),
        "rejected": summary.get("rejected", 0),
        "review_required": summary.get("review_required", 0),
        "error": summary.get("error", 0),
    }


def list_pipeline_results_payload(results_dir: Path = RESULTS_DIR) -> dict[str, Any]:
    results_path = Path(results_dir)
    result_files = sorted(results_path.glob("TXN*.json")) if results_path.exists() else []
    summary_path = results_path / "summary.json"
    summary = load_json(summary_path) if summary_path.exists() else {}
    transactions = [safe_transaction_view(load_json(path)) for path in result_files]
    if not summary and not transactions:
        return {
            "found": False,
            "reason_code": "PIPELINE_RESULTS_NOT_FOUND",
            "result_count": 0,
            "transactions": [],
            "simulation_notice": SIMULATION_NOTICE,
        }
    return {
        "found": True,
        "summary": _summary_counts(summary, len(transactions)),
        "result_count": len(transactions),
        "transactions": transactions,
        "simulation_notice": SIMULATION_NOTICE,
    }


def build_summary_text(results_dir: Path = RESULTS_DIR) -> str:
    payload = list_pipeline_results_payload(results_dir)
    if not payload.get("found"):
        return "\n".join(
            [
                "Pipeline results not found",
                f"Reason code: {payload.get('reason_code', 'PIPELINE_RESULTS_NOT_FOUND')}",
                f"Simulation notice: {SIMULATION_NOTICE}",
            ]
        )
    summary = payload["summary"]
    return "\n".join(
        [
            "Pipeline run summary",
            f"Runtime run ID: {summary['runtime_run_id']}",
            f"Generated at: {summary['generated_at']}",
            f"Total transactions: {summary['total_transactions']}",
            f"Settled: {summary['settled']}",
            f"Rejected: {summary['rejected']}",
            f"Review required: {summary['review_required']}",
            f"Error: {summary['error']}",
            f"Simulation notice: {SIMULATION_NOTICE}",
        ]
    )


@mcp.tool
def get_transaction_status(transaction_id: str) -> dict[str, Any]:
    """Return the latest safe status for one processed transaction."""
    return get_transaction_status_payload(transaction_id)


@mcp.tool
def list_pipeline_results() -> dict[str, Any]:
    """Return a safe summary of all latest processed transaction results."""
    return list_pipeline_results_payload()


@mcp.resource("pipeline://summary", mime_type="text/plain")
def pipeline_summary() -> str:
    """Return the latest pipeline run summary as text."""
    return build_summary_text()


if __name__ == "__main__":
    mcp.run()

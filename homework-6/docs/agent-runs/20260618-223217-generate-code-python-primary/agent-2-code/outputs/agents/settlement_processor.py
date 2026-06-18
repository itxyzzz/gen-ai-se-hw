"""Final settlement simulation component."""

from __future__ import annotations

from typing import Any

from .common import PIPELINE_ERROR, append_audit, append_history, audit_event

COMPONENT_NAME = "settlement_processor"


def settle_transaction(transaction_data: dict[str, Any], risk: dict[str, Any] | None = None) -> dict[str, Any]:
    risk = risk or {}
    transaction_id = str(transaction_data.get("transaction_id") or "UNKNOWN")
    current_status = transaction_data.get("status")

    if current_status == "rejected":
        return {
            "status": "rejected",
            "settlement_reference": None,
            "settlement_note": "validation_rejected",
        }
    if risk.get("decision") == "approved_for_settlement":
        return {
            "status": "settled",
            "settlement_reference": f"SIM-{transaction_id}",
            "settlement_note": "simulated_settlement_ready",
        }
    if risk.get("decision") == "review_required":
        return {
            "status": "review_required",
            "settlement_reference": None,
            "settlement_note": "manual_review_required",
        }
    return {
        "status": "error",
        "settlement_reference": None,
        "settlement_note": "missing_safe_decision",
        "reason_code": PIPELINE_ERROR,
    }


def process_message(message: dict[str, Any]) -> dict[str, Any]:
    result = dict(message)
    data = dict(result.get("data", {}))
    settlement = settle_transaction(data, result.get("risk"))
    data["status"] = settlement["status"]
    result["data"] = data
    result["settlement"] = settlement
    result["source_agent"] = COMPONENT_NAME
    result["target_agent"] = "integrator"
    result["message_type"] = "settlement_result"
    append_history(result, COMPONENT_NAME)
    append_audit(
        result,
        audit_event(
            COMPONENT_NAME,
            str(data.get("transaction_id") or "UNKNOWN"),
            settlement["status"],
            settlement.get("reason_code"),
        ),
    )
    return result


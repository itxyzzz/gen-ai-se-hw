"""Final settlement simulation component."""

from __future__ import annotations

from typing import Any

from .common import (
    COMPONENT_FAILURE,
    SETTLEMENT_SIMULATED,
    append_audit_event,
    append_component_history,
    clone_message,
    create_audit_event,
)

COMPONENT_NAME = "settlement_processor"


def decide_final_status(transaction_data: dict[str, Any], risk: dict[str, Any] | None = None) -> dict[str, Any]:
    risk = risk or {}
    if transaction_data.get("status") == "rejected":
        return {
            "status": "rejected",
            "settlement_reference": None,
            "settlement_note": "validation_rejected",
            "reason_code": None,
        }
    if risk.get("decision") == "approved_for_settlement":
        return {
            "status": "settled",
            "settlement_reference": f"SIM-{transaction_data.get('transaction_id', 'UNKNOWN')}",
            "settlement_note": "simulated_settlement_only",
            "reason_code": SETTLEMENT_SIMULATED,
        }
    if risk.get("decision") == "review_required":
        return {
            "status": "review_required",
            "settlement_reference": None,
            "settlement_note": "manual_review_required",
            "reason_code": None,
        }
    return {
        "status": "error",
        "settlement_reference": None,
        "settlement_note": "missing_safe_decision",
        "reason_code": COMPONENT_FAILURE,
    }


def build_final_result(message: dict[str, Any]) -> dict[str, Any]:
    data = message.get("data", {})
    validation = message.get("validation", {})
    risk = message.get("risk", {})
    settlement = message.get("settlement") or decide_final_status(data, risk)
    reason_codes: list[str] = []
    if validation.get("reason_code"):
        reason_codes.append(validation["reason_code"])
    reason_codes.extend(risk.get("risk_reasons", []))
    if settlement.get("reason_code"):
        reason_codes.append(settlement["reason_code"])
    return {
        "schema_version": "1.0",
        "transaction_id": str(data.get("transaction_id") or message.get("transaction_id") or "UNKNOWN"),
        "status": settlement["status"],
        "reason_codes": reason_codes,
        "risk_score": risk.get("risk_score", 0),
        "risk_level": risk.get("risk_level", "not_scored"),
        "amount": data.get("amount"),
        "currency": data.get("currency"),
        "component_history": message.get("component_history", []),
        "processed_at": message.get("processed_at"),
        "safe_summary": {
            "transaction_type": data.get("transaction_type"),
            "channel": data.get("channel"),
            "country": data.get("country"),
            "settlement_reference": settlement.get("settlement_reference"),
            "settlement_note": settlement.get("settlement_note"),
        },
        "audit_events": message.get("audit_events", []),
    }


def process_message(message: dict[str, Any]) -> dict[str, Any]:
    result = clone_message(message)
    data = dict(result.get("data", {}))
    settlement = decide_final_status(data, result.get("risk"))
    data["status"] = settlement["status"]
    result["data"] = data
    result["settlement"] = settlement
    result["source_agent"] = COMPONENT_NAME
    result["target_agent"] = "integrator"
    result["message_type"] = "settlement_result"
    append_component_history(result, COMPONENT_NAME)
    append_audit_event(
        result,
        create_audit_event(
            COMPONENT_NAME,
            str(data.get("transaction_id") or "UNKNOWN"),
            settlement["status"],
            settlement.get("reason_code"),
            result.get("risk", {}).get("risk_level"),
        ),
    )
    return result


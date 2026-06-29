"""Transaction Validator runtime component."""

from __future__ import annotations

from typing import Any

from .common import (
    MISSING_FIELD,
    SUPPORTED_CURRENCIES,
    UNSUPPORTED_CURRENCY,
    PipelineError,
    append_audit,
    append_history,
    audit_event,
    money_to_string,
    parse_amount,
)

COMPONENT_NAME = "transaction_validator"
REQUIRED_FIELDS = ("transaction_id", "timestamp", "amount", "currency", "transaction_type")


def validate_transaction(transaction_data: dict[str, Any]) -> dict[str, Any]:
    for field in REQUIRED_FIELDS:
        if transaction_data.get(field) in (None, ""):
            return {"is_valid": False, "status": "rejected", "reason_code": MISSING_FIELD}

    try:
        amount = parse_amount(transaction_data["amount"])
    except PipelineError as exc:
        return {"is_valid": False, "status": "rejected", "reason_code": exc.reason_code}

    currency = str(transaction_data["currency"]).strip().upper()
    if currency not in SUPPORTED_CURRENCIES:
        return {"is_valid": False, "status": "rejected", "reason_code": UNSUPPORTED_CURRENCY}

    return {
        "is_valid": True,
        "status": "validated",
        "amount": money_to_string(amount),
        "currency": currency,
        "reason_code": None,
    }


def process_message(message: dict[str, Any]) -> dict[str, Any]:
    result = dict(message)
    data = dict(result.get("data", {}))
    validation = validate_transaction(data)
    result["validation"] = validation
    result["target_agent"] = "fraud_detector" if validation["is_valid"] else "settlement_processor"
    result["source_agent"] = COMPONENT_NAME
    result["message_type"] = "validation_result"
    if validation["is_valid"]:
        data["amount"] = validation["amount"]
        data["currency"] = validation["currency"]
        data["status"] = "validated"
    else:
        data["status"] = "rejected"
    result["data"] = data
    append_history(result, COMPONENT_NAME)
    append_audit(
        result,
        audit_event(
            COMPONENT_NAME,
            str(data.get("transaction_id") or result.get("transaction_id") or "UNKNOWN"),
            data["status"],
            validation.get("reason_code"),
        ),
    )
    return result


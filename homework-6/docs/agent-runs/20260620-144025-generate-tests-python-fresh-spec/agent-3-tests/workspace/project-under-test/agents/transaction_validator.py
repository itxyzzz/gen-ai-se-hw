"""Transaction Validator runtime component."""

from __future__ import annotations

from datetime import datetime
from typing import Any

from .common import (
    INVALID_CURRENCY,
    INVALID_TIMESTAMP,
    MISSING_FIELD,
    SUPPORTED_CURRENCIES,
    UNSUPPORTED_CURRENCY,
    PipelineError,
    append_audit_event,
    append_component_history,
    clone_message,
    create_audit_event,
    parse_amount,
    serialize_amount,
    validate_message_envelope,
)

COMPONENT_NAME = "transaction_validator"
REQUIRED_FIELDS = (
    "transaction_id",
    "timestamp",
    "amount",
    "currency",
    "transaction_type",
)
RAW_REQUIRED_FIELDS = REQUIRED_FIELDS + ("source_account", "destination_account", "metadata")


def validate_required_fields(transaction_data: dict[str, Any], raw: bool = False) -> str | None:
    fields = RAW_REQUIRED_FIELDS if raw else REQUIRED_FIELDS
    for field in fields:
        if transaction_data.get(field) in (None, ""):
            return field
    if not raw:
        for field in ("source_account_present", "destination_account_present", "metadata_present"):
            if transaction_data.get(field) is False:
                return field.replace("_present", "")
    return None


def validate_currency(value: Any) -> str:
    if not isinstance(value, str):
        raise PipelineError(INVALID_CURRENCY)
    currency = value.strip().upper()
    if len(currency) != 3 or not currency.isalpha():
        raise PipelineError(INVALID_CURRENCY)
    if currency not in SUPPORTED_CURRENCIES:
        raise PipelineError(UNSUPPORTED_CURRENCY)
    return currency


def validate_timestamp(value: Any) -> str:
    if not isinstance(value, str) or not value.endswith("Z"):
        raise PipelineError(INVALID_TIMESTAMP)
    try:
        datetime.fromisoformat(value.replace("Z", "+00:00"))
    except ValueError as exc:
        raise PipelineError(INVALID_TIMESTAMP) from exc
    return value


def validate_transaction(transaction_data: dict[str, Any], *, raw: bool = False) -> dict[str, Any]:
    missing = validate_required_fields(transaction_data, raw=raw)
    if missing:
        return {
            "is_valid": False,
            "status": "rejected",
            "reason_code": MISSING_FIELD,
            "field": missing,
        }
    try:
        amount = parse_amount(transaction_data["amount"])
        currency = validate_currency(transaction_data["currency"])
        validate_timestamp(transaction_data["timestamp"])
    except PipelineError as exc:
        return {"is_valid": False, "status": "rejected", "reason_code": exc.reason_code}
    return {
        "is_valid": True,
        "status": "validated",
        "amount": serialize_amount(amount),
        "currency": currency,
        "reason_code": None,
    }


def process_message(message: dict[str, Any]) -> dict[str, Any]:
    validate_message_envelope(message)
    result = clone_message(message)
    data = dict(result.get("data", {}))
    validation = validate_transaction(data)
    result["validation"] = validation
    result["source_agent"] = COMPONENT_NAME
    result["target_agent"] = "fraud_detector" if validation["is_valid"] else "settlement_processor"
    result["message_type"] = "validation_result"
    if validation["is_valid"]:
        data["amount"] = validation["amount"]
        data["currency"] = validation["currency"]
        data["status"] = "validated"
    else:
        data["status"] = "rejected"
    result["data"] = data
    append_component_history(result, COMPONENT_NAME)
    append_audit_event(
        result,
        create_audit_event(
            COMPONENT_NAME,
            str(data.get("transaction_id") or result.get("transaction_id") or "UNKNOWN"),
            data["status"],
            validation.get("reason_code"),
        ),
    )
    return result


def validate_transactions_dry_run(transactions: list[dict[str, Any]]) -> dict[str, Any]:
    safe_results = []
    valid = 0
    rejected = 0
    for transaction in transactions:
        transaction_id = str(transaction.get("transaction_id") or "UNKNOWN")
        validation = validate_transaction(transaction, raw=True)
        if validation["is_valid"]:
            valid += 1
            status = "valid"
            reason_codes: list[str] = []
        else:
            rejected += 1
            status = "rejected"
            reason_codes = [validation["reason_code"]]
        safe_results.append(
            {
                "transaction_id": transaction_id,
                "status": status,
                "reason_codes": reason_codes,
                "amount": validation.get("amount") or str(transaction.get("amount", "")),
                "currency": validation.get("currency") or str(transaction.get("currency", "")),
            }
        )
    return {
        "schema_version": "1.0",
        "total": len(transactions),
        "valid": valid,
        "rejected": rejected,
        "results": safe_results,
    }


def validate_transactions_file(path: Any) -> dict[str, Any]:
    import json
    from pathlib import Path

    data = json.loads(Path(path).read_text(encoding="utf-8"))
    if not isinstance(data, list):
        raise PipelineError("INVALID_INPUT_SHAPE")
    return validate_transactions_dry_run(data)


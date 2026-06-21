"""Transaction Validator runtime component."""

from __future__ import annotations

from datetime import datetime
from typing import Any

from .common import (
    ALLOWED_CURRENCIES,
    COMPONENT_FRAUD,
    COMPONENT_VALIDATOR,
    REASON_INVALID_TIMESTAMP,
    REASON_MISSING_FIELD,
    REASON_UNSUPPORTED_CURRENCY,
    STATUS_REJECTED,
    STATUS_VALIDATED,
    ValidationError,
    append_audit_event,
    append_component_history,
    parse_amount,
    sanitize_transaction,
    serialize_amount,
)


REQUIRED_FIELDS = {
    "transaction_id",
    "timestamp",
    "source_account",
    "destination_account",
    "amount",
    "currency",
    "transaction_type",
    "metadata",
}


def _valid_timestamp(value: str) -> bool:
    if not isinstance(value, str) or not value:
        return False
    try:
        datetime.fromisoformat(value.replace("Z", "+00:00"))
    except ValueError:
        return False
    return True


def validate_transaction(transaction: dict[str, Any]) -> dict[str, Any]:
    reason_codes: list[str] = []
    missing = sorted(field for field in REQUIRED_FIELDS if field not in transaction)
    if missing:
        reason_codes.append(REASON_MISSING_FIELD)

    sanitized = sanitize_transaction(transaction)

    try:
        amount = parse_amount(transaction.get("amount", ""))
        sanitized["amount"] = serialize_amount(amount)
    except ValidationError as exc:
        reason_codes.append(exc.reason_code)

    currency = str(transaction.get("currency", "")).upper()
    if currency not in ALLOWED_CURRENCIES:
        reason_codes.append(REASON_UNSUPPORTED_CURRENCY)
    sanitized["currency"] = currency

    if not _valid_timestamp(str(transaction.get("timestamp", ""))):
        reason_codes.append(REASON_INVALID_TIMESTAMP)

    unique_reasons = list(dict.fromkeys(reason_codes))
    sanitized["status"] = STATUS_REJECTED if unique_reasons else STATUS_VALIDATED
    sanitized["reason_codes"] = unique_reasons
    sanitized["validation"] = "failed" if unique_reasons else "passed"
    return sanitized


def process_message(message: dict[str, Any]) -> dict[str, Any]:
    validated = validate_transaction(message.get("data", {}))
    message["data"] = validated
    message["source_agent"] = COMPONENT_VALIDATOR
    message["target_agent"] = COMPONENT_FRAUD
    append_component_history(message, COMPONENT_VALIDATOR)
    reason = validated["reason_codes"][0] if validated.get("reason_codes") else None
    append_audit_event(message, COMPONENT_VALIDATOR, validated["status"], reason)
    return message


def validate_transactions_file(input_path: str) -> dict[str, Any]:
    import json
    from pathlib import Path

    records = json.loads(Path(input_path).read_text(encoding="utf-8"))
    results = [validate_transaction(record) for record in records]
    rejected = sum(1 for item in results if item["status"] == STATUS_REJECTED)
    return {
        "total": len(results),
        "valid": len(results) - rejected,
        "rejected": rejected,
        "results": [
            {
                "transaction_id": item["transaction_id"],
                "status": item["status"],
                "reason_codes": item["reason_codes"],
            }
            for item in results
        ],
    }

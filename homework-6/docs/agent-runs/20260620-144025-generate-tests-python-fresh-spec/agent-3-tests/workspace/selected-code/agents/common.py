"""Shared helpers for the educational transaction-processing pipeline."""

from __future__ import annotations

from copy import deepcopy
from datetime import UTC, datetime
from decimal import Decimal, InvalidOperation
import json
from pathlib import Path
from typing import Any
from uuid import uuid4

SUPPORTED_CURRENCIES = {"USD", "EUR", "GBP"}
ALLOWED_FINAL_STATUSES = {"settled", "rejected", "review_required", "error"}
SENSITIVE_KEYS = {
    "source_account",
    "destination_account",
    "description",
    "metadata",
    "raw_transaction",
    "raw_payload",
}

MISSING_FIELD = "MISSING_FIELD"
INVALID_AMOUNT = "INVALID_AMOUNT"
NON_POSITIVE_AMOUNT = "NON_POSITIVE_AMOUNT"
UNSUPPORTED_CURRENCY = "UNSUPPORTED_CURRENCY"
INVALID_CURRENCY = "INVALID_CURRENCY"
INVALID_TIMESTAMP = "INVALID_TIMESTAMP"
INVALID_ENVELOPE = "INVALID_ENVELOPE"
MALFORMED_JSON = "MALFORMED_JSON"
PIPELINE_STAGE_ERROR = "PIPELINE_STAGE_ERROR"
COMPONENT_FAILURE = "COMPONENT_FAILURE"
MISSING_TRANSACTION_ID = "MISSING_TRANSACTION_ID"
SETTLEMENT_SIMULATED = "SETTLEMENT_SIMULATED"
HIGH_VALUE = "HIGH_VALUE"
VERY_HIGH_VALUE = "VERY_HIGH_VALUE"
NEAR_THRESHOLD_AMOUNT = "NEAR_THRESHOLD_AMOUNT"
WIRE_TRANSFER = "WIRE_TRANSFER"
ODD_HOUR_ACTIVITY = "ODD_HOUR_ACTIVITY"
REMOTE_CHANNEL = "REMOTE_CHANNEL"
MOBILE_CHANNEL = "MOBILE_CHANNEL"
CROSS_COUNTRY_REVIEW_SIGNAL = "CROSS_COUNTRY_REVIEW_SIGNAL"
INVALID_TIMESTAMP_FOR_SCORING = "INVALID_TIMESTAMP_FOR_SCORING"


class PipelineError(ValueError):
    """Safe exception carrying a stable public reason code."""

    def __init__(self, reason_code: str, message: str | None = None) -> None:
        super().__init__(message or reason_code)
        self.reason_code = reason_code


def utc_now() -> str:
    return datetime.now(UTC).replace(microsecond=0).isoformat().replace("+00:00", "Z")


def parse_amount(value: Any) -> Decimal:
    if not isinstance(value, str) or not value.strip():
        raise PipelineError(INVALID_AMOUNT)
    try:
        amount = Decimal(value.strip())
    except InvalidOperation as exc:
        raise PipelineError(INVALID_AMOUNT) from exc
    if not amount.is_finite():
        raise PipelineError(INVALID_AMOUNT)
    if amount <= Decimal("0"):
        raise PipelineError(NON_POSITIVE_AMOUNT)
    return amount


def validate_money_scale(amount: Decimal, places: Decimal = Decimal("0.01")) -> Decimal:
    return amount.quantize(places)


def serialize_amount(value: Decimal | str) -> str:
    amount = value if isinstance(value, Decimal) else parse_amount(value)
    return format(validate_money_scale(amount), "f")


def redact_account_id(account_id: str | None) -> str:
    if not isinstance(account_id, str) or len(account_id) < 4:
        return "REDACTED"
    tail = account_id[-4:]
    if not tail.isalnum():
        return "REDACTED"
    prefix = "ACC-" if account_id.startswith("ACC-") else ""
    return f"{prefix}****{tail}"


def create_audit_event(
    component_name: str,
    transaction_id: str,
    outcome: str,
    reason_code: str | None = None,
    risk_level: str | None = None,
    redacted_reference: str | None = None,
) -> dict[str, Any]:
    event: dict[str, Any] = {
        "schema_version": "1.0",
        "event_id": str(uuid4()),
        "timestamp": utc_now(),
        "component": component_name,
        "transaction_id": transaction_id or "UNKNOWN",
        "outcome": outcome,
    }
    if reason_code:
        event["reason_code"] = reason_code
    if risk_level:
        event["risk_level"] = risk_level
    if redacted_reference:
        event["redacted_reference"] = redacted_reference
    assert_no_sensitive_fields(event)
    return event


def _safe_data_from_transaction(transaction: dict[str, Any]) -> dict[str, Any]:
    metadata = transaction.get("metadata") if isinstance(transaction.get("metadata"), dict) else {}
    return {
        "transaction_id": transaction.get("transaction_id"),
        "timestamp": transaction.get("timestamp"),
        "amount": transaction.get("amount"),
        "currency": transaction.get("currency"),
        "transaction_type": transaction.get("transaction_type"),
        "channel": metadata.get("channel"),
        "country": metadata.get("country"),
        "source_account_redacted": redact_account_id(transaction.get("source_account")),
        "destination_account_redacted": redact_account_id(transaction.get("destination_account")),
        "source_account_present": isinstance(transaction.get("source_account"), str)
        and bool(transaction.get("source_account")),
        "destination_account_present": isinstance(transaction.get("destination_account"), str)
        and bool(transaction.get("destination_account")),
        "metadata_present": isinstance(transaction.get("metadata"), dict),
        "status": "received",
    }


def create_message(
    transaction: dict[str, Any],
    source_agent: str,
    target_agent: str,
    message_type: str = "transaction",
) -> dict[str, Any]:
    transaction_id = str(transaction.get("transaction_id") or "UNKNOWN")
    message = {
        "message_id": str(uuid4()),
        "timestamp": utc_now(),
        "schema_version": "1.0",
        "source_agent": source_agent,
        "target_agent": target_agent,
        "message_type": message_type,
        "transaction_id": transaction_id,
        "data": _safe_data_from_transaction(transaction),
        "component_history": [],
        "audit_events": [],
    }
    append_component_history(message, source_agent)
    message["audit_events"].append(create_audit_event(source_agent, transaction_id, "received"))
    validate_message_envelope(message)
    return message


def validate_message_envelope(message: dict[str, Any]) -> None:
    required = ("message_id", "timestamp", "source_agent", "target_agent", "message_type", "data")
    if not isinstance(message, dict):
        raise PipelineError(INVALID_ENVELOPE)
    for field in required:
        if field not in message:
            raise PipelineError(INVALID_ENVELOPE)
    if not isinstance(message["data"], dict):
        raise PipelineError(INVALID_ENVELOPE)


def append_component_history(message: dict[str, Any], component_name: str) -> dict[str, Any]:
    history = list(message.get("component_history", []))
    history.append({"component": component_name, "timestamp": utc_now()})
    message["component_history"] = history
    return message


def append_audit_event(message: dict[str, Any], event: dict[str, Any]) -> dict[str, Any]:
    events = list(message.get("audit_events", []))
    events.append(event)
    message["audit_events"] = events
    return message


def assert_no_sensitive_fields(payload: Any) -> None:
    def walk(value: Any, path: str = "") -> None:
        if isinstance(value, dict):
            for key, nested in value.items():
                if key in SENSITIVE_KEYS:
                    raise PipelineError(f"SENSITIVE_FIELD:{key}")
                walk(nested, f"{path}.{key}" if path else key)
        elif isinstance(value, list):
            for item in value:
                walk(item, path)

    walk(payload)


def sanitize_text(value: Any) -> str:
    text = str(value or "")
    for marker in ("ACC-", "description", "metadata"):
        text = text.replace(marker, "REDACTED")
    return text[:120]


def _json_default(value: Any) -> Any:
    if isinstance(value, Decimal):
        return serialize_amount(value)
    raise TypeError(f"Object of type {type(value).__name__} is not JSON serializable")


def write_json_file(path: Path, payload: dict[str, Any] | list[Any], *, require_safe: bool = True) -> None:
    if require_safe:
        assert_no_sensitive_fields(payload)
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", encoding="utf-8") as handle:
        json.dump(payload, handle, default=_json_default, allow_nan=False, indent=2, sort_keys=True)
        handle.write("\n")


def read_json_file(path: Path) -> Any:
    try:
        return json.loads(path.read_text(encoding="utf-8"))
    except json.JSONDecodeError as exc:
        raise PipelineError(MALFORMED_JSON) from exc


def clone_message(message: dict[str, Any]) -> dict[str, Any]:
    return deepcopy(message)


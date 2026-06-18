"""Shared utilities for the educational transaction-processing pipeline."""

from __future__ import annotations

from datetime import UTC, datetime
from decimal import Decimal, InvalidOperation
import json
from pathlib import Path
from typing import Any
from uuid import uuid4

SUPPORTED_CURRENCIES = {"USD", "EUR", "GBP"}

MISSING_FIELD = "MISSING_FIELD"
INVALID_AMOUNT = "INVALID_AMOUNT"
NON_POSITIVE_AMOUNT = "NON_POSITIVE_AMOUNT"
UNSUPPORTED_CURRENCY = "UNSUPPORTED_CURRENCY"
HIGH_VALUE = "HIGH_VALUE"
VERY_HIGH_VALUE = "VERY_HIGH_VALUE"
WIRE_TRANSFER = "WIRE_TRANSFER"
ODD_HOUR_ACTIVITY = "ODD_HOUR_ACTIVITY"
REMOTE_CHANNEL = "REMOTE_CHANNEL"
CROSS_COUNTRY_REVIEW_SIGNAL = "CROSS_COUNTRY_REVIEW_SIGNAL"
INVALID_TIMESTAMP_FOR_SCORING = "INVALID_TIMESTAMP_FOR_SCORING"
MALFORMED_JSON = "MALFORMED_JSON"
PIPELINE_ERROR = "PIPELINE_ERROR"


class PipelineError(ValueError):
    """Safe exception carrying a stable reason code."""

    def __init__(self, reason_code: str, message: str | None = None) -> None:
        super().__init__(message or reason_code)
        self.reason_code = reason_code


def utc_now() -> str:
    return datetime.now(UTC).replace(microsecond=0).isoformat().replace("+00:00", "Z")


def parse_amount(value: str) -> Decimal:
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


def money_to_string(value: Decimal | str) -> str:
    amount = value if isinstance(value, Decimal) else parse_amount(value)
    return format(amount.quantize(Decimal("0.01")), "f")


def redact_account_id(account_id: str | None) -> str:
    if not isinstance(account_id, str) or len(account_id) < 4:
        return "REDACTED"
    tail = account_id[-4:]
    if not tail.isalnum():
        return "REDACTED"
    prefix = "ACC-" if account_id.startswith("ACC-") else ""
    return f"{prefix}****{tail}"


def audit_event(
    agent_name: str,
    transaction_id: str,
    outcome: str,
    reason_code: str | None = None,
    account_reference: str | None = None,
) -> dict[str, Any]:
    event = {
        "event_id": str(uuid4()),
        "timestamp": utc_now(),
        "agent_name": agent_name,
        "transaction_id": transaction_id or "UNKNOWN",
        "outcome": outcome,
    }
    if reason_code:
        event["reason_code"] = reason_code
    if account_reference:
        event["account_reference"] = redact_account_id(account_reference)
    return event


def _json_default(value: Any) -> Any:
    if isinstance(value, Decimal):
        return money_to_string(value)
    raise TypeError(f"Object of type {type(value).__name__} is not JSON serializable")


def write_json_file(path: Path, payload: dict[str, Any] | list[Any]) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", encoding="utf-8") as handle:
        json.dump(
            payload,
            handle,
            default=_json_default,
            indent=2,
            sort_keys=True,
            allow_nan=False,
        )
        handle.write("\n")


def read_json_file(path: Path) -> Any:
    try:
        return json.loads(path.read_text(encoding="utf-8"))
    except json.JSONDecodeError as exc:
        raise PipelineError(MALFORMED_JSON) from exc


def append_history(message: dict[str, Any], component_name: str) -> dict[str, Any]:
    history = list(message.get("component_history", []))
    history.append({"component": component_name, "timestamp": utc_now()})
    message["component_history"] = history
    return message


def append_audit(message: dict[str, Any], event: dict[str, Any]) -> dict[str, Any]:
    events = list(message.get("audit_events", []))
    events.append(event)
    message["audit_events"] = events
    return message


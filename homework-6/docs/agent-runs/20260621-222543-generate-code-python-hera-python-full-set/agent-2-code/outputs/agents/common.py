"""Shared protocol, privacy, and serialization helpers."""

from __future__ import annotations

from datetime import datetime, timezone
from decimal import Decimal, InvalidOperation
import json
import math
from pathlib import Path
import re
from typing import Any
from uuid import uuid4


PIPELINE_VERSION = "hephaestus-python-hera-full-set-v1"
SOURCE_SPEC_RUN_ID = "20260621-220037-write-spec-python-hera-python-full-set"
SOURCE_SPEC_SHA256 = "6F2E8CD844884DF06172EEB1CF92A2956BC45425FE514B7A820ABAEA249D2222"
GENERATED_CODE_RUN_ID = "20260621-222543-generate-code-python-hera-python-full-set"

COMPONENT_INTEGRATOR = "integrator"
COMPONENT_VALIDATOR = "transaction_validator"
COMPONENT_FRAUD = "fraud_detector"
COMPONENT_SETTLEMENT = "settlement_processor"
COMPONENT_REPORTING = "reporting_agent"

STATUS_RECEIVED = "received"
STATUS_VALIDATED = "validated"
STATUS_REJECTED = "rejected"
STATUS_REVIEW_REQUIRED = "review_required"
STATUS_SETTLED = "settled"
STATUS_ERROR = "error"

REASON_MISSING_FIELD = "MISSING_FIELD"
REASON_INVALID_AMOUNT = "INVALID_AMOUNT"
REASON_NON_POSITIVE_AMOUNT = "NON_POSITIVE_AMOUNT"
REASON_UNSUPPORTED_CURRENCY = "UNSUPPORTED_CURRENCY"
REASON_INVALID_TIMESTAMP = "INVALID_TIMESTAMP"
REASON_REVIEW_HIGH_VALUE = "REVIEW_HIGH_VALUE"
REASON_REVIEW_UNUSUAL_TIME = "REVIEW_UNUSUAL_TIME"
REASON_REVIEW_CHANNEL_PATTERN = "REVIEW_CHANNEL_PATTERN"
REASON_REVIEW_DESTINATION_PATTERN = "REVIEW_DESTINATION_PATTERN"
REASON_SETTLED = "SETTLED"
REASON_PROCESSING_ERROR = "PROCESSING_ERROR"

ALLOWED_CURRENCIES = {"USD", "EUR", "GBP"}
RAW_ACCOUNT_PATTERN = re.compile(r"\bACC-\d{4,}\b")


class ValidationError(ValueError):
    """Raised for safe validation errors that already have reason codes."""

    def __init__(self, reason_code: str, message: str) -> None:
        super().__init__(message)
        self.reason_code = reason_code


def utc_now() -> str:
    return datetime.now(timezone.utc).replace(microsecond=0).isoformat().replace("+00:00", "Z")


def get_protocol_paths(base_dir: Path, shared_dir_name: str = "shared") -> dict[str, Path]:
    root = Path(base_dir)
    shared = root / shared_dir_name
    return {
        "base": root,
        "shared": shared,
        "input": shared / "input",
        "processing": shared / "processing",
        "output": shared / "output",
        "results": shared / "results",
        "archive": root / "archive",
        "run_provenance": shared / "run-provenance.json",
    }


def parse_amount(value: str) -> Decimal:
    if isinstance(value, float):
        raise ValidationError(REASON_INVALID_AMOUNT, "Float amounts are not accepted.")
    if not isinstance(value, str):
        raise ValidationError(REASON_INVALID_AMOUNT, "Amounts must be strings.")
    try:
        amount = Decimal(value)
    except (InvalidOperation, ValueError) as exc:
        raise ValidationError(REASON_INVALID_AMOUNT, "Amount could not be parsed.") from exc
    if not amount.is_finite():
        raise ValidationError(REASON_INVALID_AMOUNT, "Amount must be finite.")
    if amount <= Decimal("0"):
        raise ValidationError(REASON_NON_POSITIVE_AMOUNT, "Amount must be positive.")
    return amount


def serialize_amount(value: Decimal) -> str:
    if not isinstance(value, Decimal):
        raise TypeError("Money values must be Decimal instances before serialization.")
    if not value.is_finite():
        raise ValueError("Money values must be finite.")
    return format(value, "f")


def redact_account_id(value: str) -> str:
    if not value:
        return ""
    text = str(value)
    if len(text) <= 4:
        return "****"
    return f"ACC-****{text[-4:]}" if text.startswith("ACC-") else f"****{text[-4:]}"


def sanitize_transaction(raw: dict[str, Any]) -> dict[str, Any]:
    metadata = raw.get("metadata") if isinstance(raw.get("metadata"), dict) else {}
    sanitized: dict[str, Any] = {
        "transaction_id": str(raw.get("transaction_id", "")),
        "timestamp": str(raw.get("timestamp", "")),
        "amount": str(raw.get("amount", "")),
        "currency": str(raw.get("currency", "")).upper(),
        "transaction_type": str(raw.get("transaction_type", "")),
        "source_account_redacted": redact_account_id(str(raw.get("source_account", ""))),
        "destination_account_redacted": redact_account_id(str(raw.get("destination_account", ""))),
        "channel": str(metadata.get("channel", "unknown")),
        "country": str(metadata.get("country", "unknown")),
        "destination_pattern": str(raw.get("destination_pattern", "standard")),
    }
    return sanitized


def create_message(
    source_agent: str,
    target_agent: str,
    data: dict[str, Any],
    message_type: str = "transaction",
    timestamp: str | None = None,
) -> dict[str, Any]:
    return {
        "message_id": str(uuid4()),
        "timestamp": timestamp or utc_now(),
        "source_agent": source_agent,
        "target_agent": target_agent,
        "message_type": message_type,
        "data": data,
        "component_history": [],
        "audit_events": [],
    }


def append_component_history(message: dict[str, Any], component: str) -> dict[str, Any]:
    message.setdefault("component_history", []).append({"component": component, "timestamp": utc_now()})
    return message


def append_audit_event(
    message: dict[str, Any],
    component: str,
    outcome: str,
    reason_code: str | None = None,
) -> dict[str, Any]:
    event = {
        "timestamp": utc_now(),
        "component": component,
        "transaction_id": str(message.get("data", {}).get("transaction_id", "UNKNOWN")),
        "outcome": outcome,
    }
    if reason_code:
        event["reason_code"] = reason_code
    message.setdefault("audit_events", []).append(event)
    return message


def to_json_safe(value: Any) -> Any:
    if isinstance(value, Decimal):
        return serialize_amount(value)
    if isinstance(value, Path):
        return str(value)
    if isinstance(value, float):
        if not math.isfinite(value):
            raise ValueError("Non-finite float values are not JSON safe.")
        return value
    if isinstance(value, dict):
        return {str(key): to_json_safe(item) for key, item in value.items()}
    if isinstance(value, list):
        return [to_json_safe(item) for item in value]
    return value


def safe_json_dump(payload: Any, path: Path) -> Path:
    target = Path(path)
    target.parent.mkdir(parents=True, exist_ok=True)
    json_safe = to_json_safe(payload)
    with target.open("w", encoding="utf-8", newline="\n") as handle:
        json.dump(json_safe, handle, indent=2, sort_keys=True, allow_nan=False)
        handle.write("\n")
    return target


def safe_json_load(path: Path) -> Any:
    with Path(path).open("r", encoding="utf-8") as handle:
        return json.load(handle)


def assert_no_raw_account_ids(payload: Any) -> None:
    text = json.dumps(to_json_safe(payload), sort_keys=True, allow_nan=False)
    if RAW_ACCOUNT_PATTERN.search(text):
        raise ValueError("Payload contains a raw account identifier.")

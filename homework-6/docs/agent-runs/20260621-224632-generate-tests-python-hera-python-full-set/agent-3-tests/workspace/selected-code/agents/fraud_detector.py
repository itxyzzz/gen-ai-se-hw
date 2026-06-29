"""Deterministic educational risk scoring component."""

from __future__ import annotations

from datetime import datetime
from decimal import Decimal
from typing import Any

from .common import (
    COMPONENT_FRAUD,
    COMPONENT_SETTLEMENT,
    REASON_REVIEW_CHANNEL_PATTERN,
    REASON_REVIEW_DESTINATION_PATTERN,
    REASON_REVIEW_HIGH_VALUE,
    REASON_REVIEW_UNUSUAL_TIME,
    STATUS_REJECTED,
    STATUS_REVIEW_REQUIRED,
    STATUS_VALIDATED,
    append_audit_event,
    append_component_history,
    parse_amount,
)


def _hour_from_timestamp(value: str) -> int | None:
    try:
        return datetime.fromisoformat(value.replace("Z", "+00:00")).hour
    except ValueError:
        return None


def score_fraud_risk(transaction: dict[str, Any]) -> dict[str, Any]:
    if transaction.get("status") == STATUS_REJECTED:
        transaction["risk_score"] = 0
        transaction["risk_flags"] = []
        return transaction

    amount = parse_amount(str(transaction.get("amount", "0")))
    risk_score = 0
    flags: list[str] = []

    if amount >= Decimal("25000.00"):
        risk_score += 70
        flags.append(REASON_REVIEW_HIGH_VALUE)

    hour = _hour_from_timestamp(str(transaction.get("timestamp", "")))
    if hour is not None and hour < 4:
        risk_score += 45
        flags.append(REASON_REVIEW_UNUSUAL_TIME)

    if transaction.get("transaction_type") == "wire_transfer":
        risk_score += 10

    if transaction.get("channel") in {"api", "mobile"} and transaction.get("country") != "US":
        risk_score += 15
        flags.append(REASON_REVIEW_CHANNEL_PATTERN)

    if transaction.get("destination_pattern") == "review":
        risk_score += 40
        flags.append(REASON_REVIEW_DESTINATION_PATTERN)

    transaction["risk_score"] = risk_score
    transaction["risk_flags"] = list(dict.fromkeys(flags))
    reason_codes = list(dict.fromkeys(transaction.get("reason_codes", []) + flags))
    transaction["reason_codes"] = reason_codes
    transaction["status"] = STATUS_REVIEW_REQUIRED if flags else STATUS_VALIDATED
    return transaction


def process_message(message: dict[str, Any]) -> dict[str, Any]:
    scored = score_fraud_risk(message.get("data", {}))
    message["data"] = scored
    message["source_agent"] = COMPONENT_FRAUD
    message["target_agent"] = COMPONENT_SETTLEMENT
    append_component_history(message, COMPONENT_FRAUD)
    reason = scored.get("risk_flags", [None])[0] if scored.get("risk_flags") else None
    append_audit_event(message, COMPONENT_FRAUD, scored.get("status", STATUS_REJECTED), reason)
    return message

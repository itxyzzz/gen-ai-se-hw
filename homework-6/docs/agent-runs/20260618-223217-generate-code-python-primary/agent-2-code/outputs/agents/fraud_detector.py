"""Deterministic educational risk-scoring component."""

from __future__ import annotations

from datetime import datetime
from decimal import Decimal
from typing import Any

from .common import (
    CROSS_COUNTRY_REVIEW_SIGNAL,
    HIGH_VALUE,
    INVALID_TIMESTAMP_FOR_SCORING,
    ODD_HOUR_ACTIVITY,
    REMOTE_CHANNEL,
    VERY_HIGH_VALUE,
    WIRE_TRANSFER,
    append_audit,
    append_history,
    audit_event,
    parse_amount,
)

COMPONENT_NAME = "fraud_detector"
REVIEW_THRESHOLD = 35


def _parse_utc_timestamp(value: str) -> datetime | None:
    if not isinstance(value, str):
        return None
    try:
        return datetime.fromisoformat(value.replace("Z", "+00:00"))
    except ValueError:
        return None


def score_fraud_risk(transaction_data: dict[str, Any]) -> dict[str, Any]:
    amount = parse_amount(str(transaction_data.get("amount", "")))
    reasons: list[str] = []
    score = 0

    if amount >= Decimal("50000.00"):
        reasons.extend([VERY_HIGH_VALUE, HIGH_VALUE])
        score += 70
    elif amount >= Decimal("10000.00"):
        reasons.append(HIGH_VALUE)
        score += 35

    if str(transaction_data.get("transaction_type", "")).lower() == "wire_transfer":
        reasons.append(WIRE_TRANSFER)
        score += 20

    timestamp = _parse_utc_timestamp(str(transaction_data.get("timestamp", "")))
    if timestamp is None:
        reasons.append(INVALID_TIMESTAMP_FOR_SCORING)
        score += 35
    elif 0 <= timestamp.hour <= 4:
        reasons.append(ODD_HOUR_ACTIVITY)
        score += 30

    channel = str(transaction_data.get("channel", "")).lower()
    if channel in {"api", "mobile"}:
        reasons.append(REMOTE_CHANNEL)
        score += 10

    country = str(transaction_data.get("country", "")).upper()
    if country and country != "US":
        reasons.append(CROSS_COUNTRY_REVIEW_SIGNAL)
        score += 15

    status = "review_required" if score >= REVIEW_THRESHOLD else "approved_for_settlement"
    if score >= 70:
        risk_level = "very_high"
    elif score >= REVIEW_THRESHOLD:
        risk_level = "high"
    elif score:
        risk_level = "medium"
    else:
        risk_level = "low"

    return {
        "risk_score": score,
        "risk_level": risk_level,
        "risk_reasons": reasons,
        "decision": status,
    }


def process_message(message: dict[str, Any]) -> dict[str, Any]:
    result = dict(message)
    validation = result.get("validation", {})
    if not validation.get("is_valid"):
        result["target_agent"] = "settlement_processor"
        return result

    data = dict(result.get("data", {}))
    risk = score_fraud_risk(data)
    data["status"] = risk["decision"]
    result["data"] = data
    result["risk"] = risk
    result["source_agent"] = COMPONENT_NAME
    result["target_agent"] = "settlement_processor"
    result["message_type"] = "risk_result"
    append_history(result, COMPONENT_NAME)
    reason_code = risk["risk_reasons"][0] if risk["risk_reasons"] else None
    append_audit(
        result,
        audit_event(COMPONENT_NAME, str(data.get("transaction_id") or "UNKNOWN"), risk["decision"], reason_code),
    )
    return result


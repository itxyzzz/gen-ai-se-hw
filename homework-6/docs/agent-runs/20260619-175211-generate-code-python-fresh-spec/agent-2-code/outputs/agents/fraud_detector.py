"""Deterministic educational risk-scoring component."""

from __future__ import annotations

from datetime import datetime
from decimal import Decimal
from typing import Any

from .common import (
    CROSS_COUNTRY_REVIEW_SIGNAL,
    HIGH_VALUE,
    INVALID_TIMESTAMP_FOR_SCORING,
    MOBILE_CHANNEL,
    NEAR_THRESHOLD_AMOUNT,
    ODD_HOUR_ACTIVITY,
    REMOTE_CHANNEL,
    VERY_HIGH_VALUE,
    WIRE_TRANSFER,
    append_audit_event,
    append_component_history,
    clone_message,
    create_audit_event,
    parse_amount,
)

COMPONENT_NAME = "fraud_detector"


def _parse_utc_timestamp(value: Any) -> datetime | None:
    if not isinstance(value, str):
        return None
    try:
        return datetime.fromisoformat(value.replace("Z", "+00:00"))
    except ValueError:
        return None


def classify_risk(score: int, reasons: list[str]) -> str:
    if score >= 50:
        return "high"
    if score >= 25 or NEAR_THRESHOLD_AMOUNT in reasons:
        return "medium"
    return "low"


def score_fraud_risk(transaction_data: dict[str, Any]) -> dict[str, Any]:
    amount = parse_amount(transaction_data.get("amount", ""))
    score = 0
    reasons: list[str] = []

    if amount >= Decimal("50000.00"):
        score += 40
        reasons.append(VERY_HIGH_VALUE)
    if amount >= Decimal("10000.00"):
        score += 25
        reasons.append(HIGH_VALUE)
    elif amount >= Decimal("9000.00"):
        score += 15
        reasons.append(NEAR_THRESHOLD_AMOUNT)

    if str(transaction_data.get("transaction_type", "")).lower() == "wire_transfer":
        score += 20
        reasons.append(WIRE_TRANSFER)

    timestamp = _parse_utc_timestamp(transaction_data.get("timestamp"))
    if timestamp is None:
        score += 15
        reasons.append(INVALID_TIMESTAMP_FOR_SCORING)
    elif 0 <= timestamp.hour <= 4:
        score += 15
        reasons.append(ODD_HOUR_ACTIVITY)

    channel = str(transaction_data.get("channel", "")).lower()
    if channel == "api":
        score += 10
        reasons.append(REMOTE_CHANNEL)
    elif channel == "mobile":
        score += 5
        reasons.append(MOBILE_CHANNEL)

    country = str(transaction_data.get("country", "")).upper()
    if country and country != "US":
        score += 10
        reasons.append(CROSS_COUNTRY_REVIEW_SIGNAL)

    risk_level = classify_risk(score, reasons)
    decision = "review_required" if risk_level in {"medium", "high"} else "approved_for_settlement"
    return {
        "risk_score": score,
        "risk_level": risk_level,
        "risk_reasons": reasons,
        "decision": decision,
    }


def process_message(message: dict[str, Any]) -> dict[str, Any]:
    result = clone_message(message)
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
    append_component_history(result, COMPONENT_NAME)
    append_audit_event(
        result,
        create_audit_event(
            COMPONENT_NAME,
            str(data.get("transaction_id") or "UNKNOWN"),
            risk["decision"],
            risk["risk_reasons"][0] if risk["risk_reasons"] else None,
            risk["risk_level"],
        ),
    )
    return result


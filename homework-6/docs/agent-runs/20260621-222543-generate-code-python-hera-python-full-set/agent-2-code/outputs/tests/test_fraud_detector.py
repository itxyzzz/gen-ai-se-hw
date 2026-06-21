from __future__ import annotations

from agents.common import (
    REASON_REVIEW_HIGH_VALUE,
    REASON_REVIEW_DESTINATION_PATTERN,
    REASON_REVIEW_UNUSUAL_TIME,
    STATUS_REVIEW_REQUIRED,
)
from agents.fraud_detector import score_fraud_risk
from agents.transaction_validator import validate_transaction


def _validated(records: list[dict], transaction_id: str) -> dict:
    return validate_transaction(next(item for item in records if item["transaction_id"] == transaction_id))


def test_fraud_detector_flags_high_value(sample_records) -> None:
    result = score_fraud_risk(_validated(sample_records, "TXN002"))
    assert result["status"] == STATUS_REVIEW_REQUIRED
    assert REASON_REVIEW_HIGH_VALUE in result["reason_codes"]


def test_fraud_detector_flags_unusual_time(sample_records) -> None:
    result = score_fraud_risk(_validated(sample_records, "TXN004"))
    assert result["status"] == STATUS_REVIEW_REQUIRED
    assert REASON_REVIEW_UNUSUAL_TIME in result["reason_codes"]


def test_fraud_detector_flags_safe_destination_pattern(sample_records) -> None:
    record = next(item for item in sample_records if item["transaction_id"] == "TXN003").copy()
    record["destination_pattern"] = "review"
    result = score_fraud_risk(validate_transaction(record))
    assert result["status"] == STATUS_REVIEW_REQUIRED
    assert REASON_REVIEW_DESTINATION_PATTERN in result["reason_codes"]

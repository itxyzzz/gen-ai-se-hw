from __future__ import annotations

from agents.common import (
    REASON_NON_POSITIVE_AMOUNT,
    REASON_UNSUPPORTED_CURRENCY,
    STATUS_REJECTED,
    STATUS_VALIDATED,
)
from agents.transaction_validator import validate_transaction


def _by_id(records: list[dict], transaction_id: str) -> dict:
    return next(item for item in records if item["transaction_id"] == transaction_id)


def test_validator_accepts_valid_sample_record(sample_records) -> None:
    result = validate_transaction(_by_id(sample_records, "TXN001"))
    assert result["status"] == STATUS_VALIDATED
    assert result["amount"] == "1500.00"
    assert result["currency"] == "USD"
    assert "source_account" not in result
    assert "description" not in result


def test_validator_rejects_invalid_currency(sample_records) -> None:
    result = validate_transaction(_by_id(sample_records, "TXN006"))
    assert result["status"] == STATUS_REJECTED
    assert REASON_UNSUPPORTED_CURRENCY in result["reason_codes"]


def test_validator_rejects_non_positive_amount(sample_records) -> None:
    result = validate_transaction(_by_id(sample_records, "TXN007"))
    assert result["status"] == STATUS_REJECTED
    assert REASON_NON_POSITIVE_AMOUNT in result["reason_codes"]

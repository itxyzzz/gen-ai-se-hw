from __future__ import annotations

from agents.common import STATUS_REVIEW_REQUIRED, STATUS_SETTLED, STATUS_VALIDATED
from agents.settlement_processor import settle_transaction


def test_settlement_marks_valid_low_risk_transaction_settled() -> None:
    transaction = {"transaction_id": "TXN001", "status": STATUS_VALIDATED, "reason_codes": []}
    result = settle_transaction(transaction, STATUS_VALIDATED)
    assert result["status"] == STATUS_SETTLED
    assert result["settlement_reference"] == "SIM-TXN001"


def test_settlement_preserves_review_required() -> None:
    transaction = {"transaction_id": "TXN002", "status": STATUS_REVIEW_REQUIRED, "reason_codes": ["REVIEW_HIGH_VALUE"]}
    result = settle_transaction(transaction, STATUS_REVIEW_REQUIRED)
    assert result["status"] == STATUS_REVIEW_REQUIRED
    assert "settlement_reference" not in result

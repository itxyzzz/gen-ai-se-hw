from agents.fraud_detector import classify_risk, score_fraud_risk


def _transaction(**overrides):
    transaction = {
        "transaction_id": "TXN001",
        "timestamp": "2026-03-16T09:00:00Z",
        "amount": "1500.00",
        "currency": "USD",
        "transaction_type": "transfer",
        "channel": "online",
        "country": "US",
    }
    transaction.update(overrides)
    return transaction


def test_fraud_detector_flags_high_value_and_wire():
    result = score_fraud_risk(
        _transaction(transaction_id="TXN002", amount="25000.00", transaction_type="wire_transfer")
    )
    assert result["decision"] == "review_required"
    assert "HIGH_VALUE" in result["risk_reasons"]
    assert "WIRE_TRANSFER" in result["risk_reasons"]


def test_fraud_detector_flags_near_threshold_for_review():
    result = score_fraud_risk(_transaction(transaction_id="TXN003", amount="9999.99"))
    assert result["decision"] == "review_required"
    assert "NEAR_THRESHOLD_AMOUNT" in result["risk_reasons"]


def test_fraud_detector_flags_very_high_value():
    result = score_fraud_risk(
        _transaction(transaction_id="TXN005", amount="75000.00", transaction_type="wire_transfer")
    )
    assert result["decision"] == "review_required"
    assert "VERY_HIGH_VALUE" in result["risk_reasons"]
    assert "HIGH_VALUE" in result["risk_reasons"]


def test_fraud_detector_flags_odd_hour_remote_cross_country():
    result = score_fraud_risk(
        _transaction(
            transaction_id="TXN004",
            timestamp="2026-03-16T02:47:00Z",
            amount="500.00",
            channel="api",
            country="DE",
        )
    )
    assert result["decision"] == "review_required"
    assert "ODD_HOUR_ACTIVITY" in result["risk_reasons"]
    assert "REMOTE_CHANNEL" in result["risk_reasons"]
    assert "CROSS_COUNTRY_REVIEW_SIGNAL" in result["risk_reasons"]


def test_fraud_detector_approves_low_risk_transaction():
    result = score_fraud_risk(_transaction())
    assert result["decision"] == "approved_for_settlement"
    assert result["risk_level"] == "low"


def test_classify_risk_boundaries():
    assert classify_risk(0, []) == "low"
    assert classify_risk(25, []) == "medium"
    assert classify_risk(50, []) == "high"


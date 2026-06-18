from agents.transaction_validator import process_message, validate_transaction


def _transaction(**overrides):
    transaction = {
        "transaction_id": "TXN001",
        "timestamp": "2026-03-16T09:00:00Z",
        "amount": "1500.00",
        "currency": "USD",
        "transaction_type": "transfer",
    }
    transaction.update(overrides)
    return transaction


def test_validator_accepts_valid_transaction():
    result = validate_transaction(_transaction())
    assert result["is_valid"] is True
    assert result["amount"] == "1500.00"
    assert result["currency"] == "USD"


def test_validator_rejects_unsupported_currency():
    result = validate_transaction(_transaction(transaction_id="TXN006", currency="XYZ"))
    assert result["is_valid"] is False
    assert result["reason_code"] == "UNSUPPORTED_CURRENCY"


def test_validator_rejects_negative_amount():
    result = validate_transaction(_transaction(transaction_id="TXN007", amount="-100.00", currency="GBP"))
    assert result["is_valid"] is False
    assert result["reason_code"] == "NON_POSITIVE_AMOUNT"


def test_process_message_routes_rejected_record_to_settlement():
    message = {"data": _transaction(currency="XYZ"), "audit_events": [], "component_history": []}
    result = process_message(message)
    assert result["target_agent"] == "settlement_processor"
    assert result["data"]["status"] == "rejected"
    assert result["audit_events"]


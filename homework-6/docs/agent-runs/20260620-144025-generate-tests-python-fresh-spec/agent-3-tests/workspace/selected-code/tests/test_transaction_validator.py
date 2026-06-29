from agents.common import create_message
from agents.transaction_validator import process_message, validate_transaction, validate_transactions_dry_run


def _transaction(**overrides):
    transaction = {
        "transaction_id": "TXN001",
        "timestamp": "2026-03-16T09:00:00Z",
        "source_account": "ACC-" + "1001",
        "destination_account": "ACC-" + "2001",
        "amount": "1500.00",
        "currency": "USD",
        "transaction_type": "transfer",
        "metadata": {"channel": "online", "country": "US"},
    }
    transaction.update(overrides)
    return transaction


def test_validator_accepts_valid_transaction():
    result = validate_transaction(_transaction(), raw=True)
    assert result["is_valid"] is True
    assert result["amount"] == "1500.00"
    assert result["currency"] == "USD"


def test_validator_rejects_unsupported_currency():
    result = validate_transaction(_transaction(transaction_id="TXN006", currency="XYZ"), raw=True)
    assert result["is_valid"] is False
    assert result["reason_code"] == "UNSUPPORTED_CURRENCY"


def test_validator_rejects_negative_amount():
    result = validate_transaction(_transaction(transaction_id="TXN007", amount="-100.00", currency="GBP"), raw=True)
    assert result["is_valid"] is False
    assert result["reason_code"] == "NON_POSITIVE_AMOUNT"


def test_validator_rejects_missing_raw_required_field():
    transaction = _transaction()
    transaction.pop("metadata")
    result = validate_transaction(transaction, raw=True)
    assert result["is_valid"] is False
    assert result["reason_code"] == "MISSING_FIELD"


def test_process_message_routes_rejected_record_to_settlement():
    message = create_message(_transaction(currency="XYZ"), "integrator", "transaction_validator")
    result = process_message(message)
    assert result["target_agent"] == "settlement_processor"
    assert result["data"]["status"] == "rejected"
    assert result["audit_events"]


def test_validate_transactions_dry_run_reports_sample_level_counts():
    transactions = [
        _transaction(transaction_id="TXN001"),
        _transaction(transaction_id="TXN006", currency="XYZ"),
        _transaction(transaction_id="TXN007", amount="-100.00", currency="GBP"),
    ]
    report = validate_transactions_dry_run(transactions)
    assert report["total"] == 3
    assert report["valid"] == 1
    assert report["rejected"] == 2
    assert all("source_account" not in result for result in report["results"])


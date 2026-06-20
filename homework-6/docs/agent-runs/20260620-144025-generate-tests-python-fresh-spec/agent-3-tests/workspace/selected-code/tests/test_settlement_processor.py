from agents.settlement_processor import decide_final_status, process_message


def test_settlement_processor_settles_low_risk_transaction():
    result = decide_final_status({"transaction_id": "TXN001", "status": "approved_for_settlement"}, {"decision": "approved_for_settlement"})
    assert result["status"] == "settled"
    assert result["settlement_reference"] == "SIM-TXN001"
    assert result["reason_code"] == "SETTLEMENT_SIMULATED"


def test_settlement_processor_holds_review_required_without_reference():
    result = decide_final_status({"transaction_id": "TXN002", "status": "review_required"}, {"decision": "review_required"})
    assert result["status"] == "review_required"
    assert result["settlement_reference"] is None


def test_settlement_processor_preserves_rejected_records():
    result = decide_final_status({"transaction_id": "TXN006", "status": "rejected"}, {})
    assert result["status"] == "rejected"
    assert result["settlement_reference"] is None


def test_process_message_adds_safe_audit_event():
    message = {
        "data": {"transaction_id": "TXN001", "status": "approved_for_settlement"},
        "risk": {"decision": "approved_for_settlement", "risk_level": "low"},
        "audit_events": [],
        "component_history": [],
    }
    result = process_message(message)
    assert result["settlement"]["status"] == "settled"
    assert result["audit_events"]


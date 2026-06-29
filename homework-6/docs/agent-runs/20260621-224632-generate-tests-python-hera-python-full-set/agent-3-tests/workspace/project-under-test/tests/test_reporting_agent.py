from __future__ import annotations

import pytest

from agents.common import STATUS_SETTLED, create_message
from agents.reporting_agent import assert_privacy_safe, summarize_results, write_transaction_result


def test_reporting_privacy_check_rejects_account_ids() -> None:
    raw_account = "ACC-" + "1001"
    with pytest.raises(ValueError):
        assert_privacy_safe({"source": raw_account})


def test_reporting_writes_safe_result_and_summary(tmp_path) -> None:
    results_dir = tmp_path / "results"
    message = create_message(
        source_agent="settlement_processor",
        target_agent="reporting_agent",
        data={
            "transaction_id": "TXN001",
            "status": STATUS_SETTLED,
            "reason_codes": ["SETTLED"],
            "amount": "1500.00",
            "currency": "USD",
            "settlement_reference": "SIM-TXN001",
        },
    )
    write_transaction_result(message, results_dir)
    summary = summarize_results(results_dir, ["TXN001"], "run-1")
    assert summary["total_records"] == 1
    assert summary["settled"] == 1
    assert summary["completeness_check"] == "passed"

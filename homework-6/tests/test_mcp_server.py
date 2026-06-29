import json
import importlib.util
from pathlib import Path


PROJECT_ROOT = Path(__file__).resolve().parents[1]
SERVER_PATH = PROJECT_ROOT / "mcp" / "server.py"
SPEC = importlib.util.spec_from_file_location("pipeline_status_mcp_server", SERVER_PATH)
assert SPEC is not None
mcp_server = importlib.util.module_from_spec(SPEC)
assert SPEC.loader is not None
SPEC.loader.exec_module(mcp_server)

build_summary_text = mcp_server.build_summary_text
get_transaction_status_payload = mcp_server.get_transaction_status_payload
list_pipeline_results_payload = mcp_server.list_pipeline_results_payload


SIMULATION_NOTICE = (
    "Educational simulation only; no real payment, banking, legal, AML, sanctions, "
    "KYC, PCI, or payment-network compliance determination is performed."
)


def _write_json(path, payload):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(payload, indent=2, sort_keys=True), encoding="utf-8")


def _results_dir(tmp_path):
    results = tmp_path / "shared" / "results"
    _write_json(
        results / "summary.json",
        {
            "schema_version": "1.0",
            "runtime_run_id": "run-123",
            "total_transactions": 2,
            "settled": 1,
            "rejected": 1,
            "review_required": 0,
            "error": 0,
            "generated_at": "2026-06-20T14:37:33Z",
            "simulation_notice": SIMULATION_NOTICE,
        },
    )
    _write_json(
        results / "TXN001.json",
        {
            "schema_version": "1.0",
            "transaction_id": "TXN001",
            "status": "settled",
            "reason_codes": ["SETTLEMENT_SIMULATED"],
            "risk_score": 0,
            "risk_level": "low",
            "amount": "1500.00",
            "currency": "USD",
            "processed_at": "2026-06-20T14:37:33Z",
            "safe_summary": {
                "channel": "online",
                "country": "US",
                "settlement_reference": "SIM-TXN001",
            },
            "component_history": [
                {"component": "integrator", "timestamp": "2026-06-20T14:37:33Z"},
                {"component": "transaction_validator", "timestamp": "2026-06-20T14:37:33Z"},
            ],
            "audit_events": [
                {"component": "integrator", "transaction_id": "TXN001", "outcome": "received"}
            ],
        },
    )
    _write_json(
        results / "TXN006.json",
        {
            "schema_version": "1.0",
            "transaction_id": "TXN006",
            "status": "rejected",
            "reason_codes": ["UNSUPPORTED_CURRENCY"],
            "risk_score": 0,
            "risk_level": "not_scored",
            "amount": "200.00",
            "currency": "XYZ",
            "processed_at": "2026-06-20T14:37:33Z",
            "safe_summary": {"validation_only": False},
            "component_history": [],
            "audit_events": [],
        },
    )
    return results


def test_get_transaction_status_returns_safe_settled_status(tmp_path):
    payload = get_transaction_status_payload("TXN001", results_dir=_results_dir(tmp_path))

    assert payload["found"] is True
    assert payload["transaction_id"] == "TXN001"
    assert payload["status"] == "settled"
    assert payload["reason_codes"] == ["SETTLEMENT_SIMULATED"]
    assert payload["component_count"] == 2
    assert payload["audit_event_count"] == 1
    assert payload["simulation_notice"] == SIMULATION_NOTICE


def test_get_transaction_status_returns_safe_rejected_status(tmp_path):
    payload = get_transaction_status_payload("TXN006", results_dir=_results_dir(tmp_path))

    assert payload["found"] is True
    assert payload["status"] == "rejected"
    assert payload["currency"] == "XYZ"
    assert payload["reason_codes"] == ["UNSUPPORTED_CURRENCY"]


def test_get_transaction_status_reports_missing_transaction(tmp_path):
    payload = get_transaction_status_payload("UNKNOWN", results_dir=_results_dir(tmp_path))

    assert payload == {
        "found": False,
        "transaction_id": "UNKNOWN",
        "reason_code": "TRANSACTION_RESULT_NOT_FOUND",
        "simulation_notice": SIMULATION_NOTICE,
    }


def test_get_transaction_status_rejects_invalid_transaction_id(tmp_path):
    payload = get_transaction_status_payload("../summary", results_dir=_results_dir(tmp_path))

    assert payload == {
        "found": False,
        "transaction_id": "../summary",
        "reason_code": "INVALID_TRANSACTION_ID",
        "simulation_notice": SIMULATION_NOTICE,
    }


def test_list_pipeline_results_returns_summary_and_sorted_transactions(tmp_path):
    payload = list_pipeline_results_payload(results_dir=_results_dir(tmp_path))

    assert payload["found"] is True
    assert payload["summary"]["runtime_run_id"] == "run-123"
    assert payload["summary"]["total_transactions"] == 2
    assert payload["summary"]["settled"] == 1
    assert payload["summary"]["rejected"] == 1
    assert payload["summary"]["review_required"] == 0
    assert payload["summary"]["error"] == 0
    assert payload["result_count"] == 2
    assert [item["transaction_id"] for item in payload["transactions"]] == ["TXN001", "TXN006"]


def test_list_pipeline_results_reports_empty_results(tmp_path):
    payload = list_pipeline_results_payload(results_dir=tmp_path / "shared" / "results")

    assert payload == {
        "found": False,
        "reason_code": "PIPELINE_RESULTS_NOT_FOUND",
        "result_count": 0,
        "transactions": [],
        "simulation_notice": SIMULATION_NOTICE,
    }


def test_summary_text_includes_counts_and_simulation_notice(tmp_path):
    text = build_summary_text(results_dir=_results_dir(tmp_path))

    assert "Pipeline run summary" in text
    assert "Runtime run ID: run-123" in text
    assert "Generated at: 2026-06-20T14:37:33Z" in text
    assert "Total transactions: 2" in text
    assert "Settled: 1" in text
    assert "Rejected: 1" in text
    assert "Review required: 0" in text
    assert "Error: 0" in text
    assert f"Simulation notice: {SIMULATION_NOTICE}" in text


def test_summary_text_reports_missing_results(tmp_path):
    text = build_summary_text(results_dir=tmp_path / "shared" / "results")

    assert "Pipeline results not found" in text
    assert "PIPELINE_RESULTS_NOT_FOUND" in text
    assert SIMULATION_NOTICE in text


def test_mcp_payloads_do_not_expose_raw_sensitive_sample_values(tmp_path):
    results = _results_dir(tmp_path)
    combined = "\n".join(
        [
            json.dumps(get_transaction_status_payload("TXN001", results_dir=results), sort_keys=True),
            json.dumps(list_pipeline_results_payload(results_dir=results), sort_keys=True),
            build_summary_text(results_dir=results),
        ]
    )

    for forbidden in (
        "ACC-1001",
        "ACC-2001",
        "Monthly rent payment",
        "source_account",
        "destination_account",
        "description",
        "raw_transaction",
    ):
        assert forbidden not in combined

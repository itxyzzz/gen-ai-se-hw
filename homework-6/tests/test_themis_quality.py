import json
from pathlib import Path

import pytest

import integrator
from agents.common import COMPONENT_FAILURE, PipelineError
from agents.transaction_validator import validate_transactions_file
from integrator import load_transactions, main, prepare_shared_directories, process_transaction, run_pipeline


PROJECT_ROOT = Path(__file__).resolve().parents[1]
SAMPLE = PROJECT_ROOT / "sample-transactions.json"
SPEC = PROJECT_ROOT / "specification.md"
INVENTORY = PROJECT_ROOT / "inventory.md"


def _sample_transactions():
    return json.loads(SAMPLE.read_text(encoding="utf-8"))


def test_full_pipeline_result_schema_reason_codes_and_privacy(tmp_path):
    summary = run_pipeline(
        input_path=SAMPLE,
        shared_dir=tmp_path / "shared",
        spec_path=SPEC,
        inventory_path=INVENTORY,
    )
    results_dir = tmp_path / "shared" / "results"
    by_id = {path.stem: json.loads(path.read_text(encoding="utf-8")) for path in results_dir.glob("TXN*.json")}

    assert summary["total_transactions"] == 8
    assert summary["settled"] == 2
    assert summary["rejected"] == 2
    assert summary["review_required"] == 4
    assert summary["error"] == 0
    assert set(by_id) == {f"TXN00{index}" for index in range(1, 9)}

    assert by_id["TXN001"]["reason_codes"] == ["SETTLEMENT_SIMULATED"]
    assert by_id["TXN006"]["reason_codes"] == ["UNSUPPORTED_CURRENCY"]
    assert by_id["TXN007"]["reason_codes"] == ["NON_POSITIVE_AMOUNT"]
    assert {"HIGH_VALUE", "WIRE_TRANSFER"}.issubset(by_id["TXN002"]["reason_codes"])
    assert {"ODD_HOUR_ACTIVITY", "REMOTE_CHANNEL", "CROSS_COUNTRY_REVIEW_SIGNAL"}.issubset(
        by_id["TXN004"]["reason_codes"]
    )

    combined = "\n".join(path.read_text(encoding="utf-8") for path in results_dir.glob("*.json"))
    for transaction in _sample_transactions():
        assert transaction["source_account"] not in combined
        assert transaction["destination_account"] not in combined
        assert transaction["description"] not in combined
    assert "Educational simulation only" in summary["simulation_notice"]


def test_validate_only_cli_does_not_run_risk_or_settlement_stages(tmp_path):
    shared = tmp_path / "shared"
    exit_code = main(
        [
            "--validate-only",
            "--input",
            str(SAMPLE),
            "--shared-dir",
            str(shared),
            "--spec-path",
            str(SPEC),
            "--inventory-path",
            str(INVENTORY),
        ]
    )

    assert exit_code == 0
    summary = json.loads((shared / "results" / "summary.json").read_text(encoding="utf-8"))
    assert summary["total_transactions"] == 8
    assert summary["settled"] == 6
    assert summary["rejected"] == 2
    assert not list((shared / "processing").glob("*.json"))
    assert not list((shared / "output").glob("*.json"))


def test_validator_file_dry_run_reports_sample_counts_without_sensitive_fields():
    report = validate_transactions_file(SAMPLE)
    serialized = json.dumps(report, sort_keys=True)

    assert report["total"] == 8
    assert report["valid"] == 6
    assert report["rejected"] == 2
    assert "UNSUPPORTED_CURRENCY" in serialized
    assert "NON_POSITIVE_AMOUNT" in serialized
    for transaction in _sample_transactions():
        assert transaction["source_account"] not in serialized
        assert transaction["destination_account"] not in serialized
        assert transaction["description"] not in serialized


def test_invalid_input_files_fail_with_safe_reason_codes(tmp_path):
    malformed = tmp_path / "malformed.json"
    malformed.write_text("{not valid json", encoding="utf-8")
    duplicate = tmp_path / "duplicate.json"
    duplicate.write_text('[{"transaction_id":"TXN001"},{"transaction_id":"TXN001"}]', encoding="utf-8")

    with pytest.raises(PipelineError) as malformed_error:
        load_transactions(malformed)
    assert malformed_error.value.reason_code == "MALFORMED_JSON"

    with pytest.raises(PipelineError) as duplicate_error:
        load_transactions(duplicate)
    assert duplicate_error.value.reason_code == "DUPLICATE_TRANSACTION_ID"


def test_component_failure_result_is_safe_and_keeps_pipeline_contract(monkeypatch, tmp_path):
    transaction = _sample_transactions()[0]
    directories = prepare_shared_directories(tmp_path / "shared")

    def fail_with_sensitive_text(message):
        raise RuntimeError("ACC-" + "1001" + " description metadata")

    monkeypatch.setattr(integrator.fraud_detector, "process_message", fail_with_sensitive_text)

    result = process_transaction(transaction, directories, index=1)
    serialized = json.dumps(result, sort_keys=True)

    assert result["status"] == "error"
    assert result["reason_codes"] == [COMPONENT_FAILURE]
    assert "ACC-" + "1001" not in serialized
    assert "description" not in serialized
    assert "metadata" not in serialized


def test_repeated_runs_archive_results_and_run_provenance(tmp_path):
    shared = tmp_path / "shared"

    first = run_pipeline(input_path=SAMPLE, shared_dir=shared, spec_path=SPEC, inventory_path=INVENTORY)
    second = run_pipeline(input_path=SAMPLE, shared_dir=shared, spec_path=SPEC, inventory_path=INVENTORY)

    archived_summary = json.loads((tmp_path / "archive" / "shared-001" / "results" / "summary.json").read_text())
    archived_provenance = json.loads((tmp_path / "archive" / "shared-001" / "run-provenance.json").read_text())
    current_provenance = json.loads((shared / "run-provenance.json").read_text())

    assert archived_summary["runtime_run_id"] == first["runtime_run_id"]
    assert second["runtime_run_id"] != first["runtime_run_id"]
    assert archived_provenance["source_spec"]["run_id"] == "20260619-170102-write-spec-python-fresh"
    assert current_provenance["pipeline_version"]["run_id"] == "20260619-175211-generate-code-python-fresh-spec"

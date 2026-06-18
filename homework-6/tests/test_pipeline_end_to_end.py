import json
from pathlib import Path

from integrator import (
    build_message_envelope,
    load_transactions,
    main,
    prepare_shared_directories,
    process_transaction,
    summarize_results,
    write_result,
)


SAMPLE = Path(__file__).resolve().parents[1] / "sample-transactions.json"


def _run_sample(tmp_path):
    directories = prepare_shared_directories(tmp_path / "shared")
    transactions = load_transactions(SAMPLE)
    results = []
    for transaction in transactions:
        result = process_transaction(transaction, directories)
        write_result(result, directories["results"])
        results.append(result)
    summary = summarize_results(results, directories["results"])
    return directories, results, summary


def test_prepare_shared_directories_archives_existing_shared_tree(tmp_path):
    dirs = prepare_shared_directories(tmp_path / "shared")
    stale_summary = dirs["results"] / "summary.json"
    stale_summary.write_text('{"run": "previous"}', encoding="utf-8")

    fresh_dirs = prepare_shared_directories(tmp_path / "shared")

    archived_summary = tmp_path / "archive" / "shared-001" / "results" / "summary.json"
    assert archived_summary.exists()
    assert json.loads(archived_summary.read_text(encoding="utf-8")) == {"run": "previous"}
    assert not stale_summary.exists()
    assert all(path.exists() for path in fresh_dirs.values())


def test_prepare_shared_directories_uses_next_zero_padded_archive_id(tmp_path):
    dirs = prepare_shared_directories(tmp_path / "shared")
    (dirs["results"] / "summary.json").write_text('{"run": 1}', encoding="utf-8")
    prepare_shared_directories(tmp_path / "shared")
    (tmp_path / "shared" / "results" / "summary.json").write_text('{"run": 2}', encoding="utf-8")
    prepare_shared_directories(tmp_path / "shared")

    first = tmp_path / "archive" / "shared-001" / "results" / "summary.json"
    second = tmp_path / "archive" / "shared-002" / "results" / "summary.json"
    assert json.loads(first.read_text(encoding="utf-8")) == {"run": 1}
    assert json.loads(second.read_text(encoding="utf-8")) == {"run": 2}
    assert not (tmp_path / "archive" / "shared-003").exists()


def test_message_envelope_omits_sensitive_fields():
    account_id = "ACC-" + "1001"
    transaction = {
        "transaction_id": "TXN001",
        "timestamp": "2026-03-16T09:00:00Z",
        "source_account": account_id,
        "destination_account": "ACC-" + "2001",
        "amount": "1500.00",
        "currency": "USD",
        "transaction_type": "transfer",
        "description": "sensitive memo",
        "metadata": {"channel": "online", "country": "US"},
    }
    envelope = build_message_envelope(transaction, "integrator", "transaction_validator")
    text = json.dumps(envelope, sort_keys=True)
    assert "description" not in text
    assert account_id not in text
    assert "ACC-****1001" in text


def test_full_pipeline_processes_all_sample_transactions(tmp_path):
    _, results, summary = _run_sample(tmp_path)
    by_id = {result["transaction_id"]: result for result in results}
    assert summary["total_transactions"] == 8
    assert summary["settled"] >= 1
    assert by_id["TXN006"]["status"] == "rejected"
    assert "UNSUPPORTED_CURRENCY" in by_id["TXN006"]["reason_codes"]
    assert by_id["TXN007"]["status"] == "rejected"
    assert "NON_POSITIVE_AMOUNT" in by_id["TXN007"]["reason_codes"]
    assert by_id["TXN002"]["status"] == "review_required"
    assert "HIGH_VALUE" in by_id["TXN002"]["reason_codes"]
    assert by_id["TXN005"]["status"] == "review_required"
    assert "VERY_HIGH_VALUE" in by_id["TXN005"]["reason_codes"]
    assert by_id["TXN004"]["status"] == "review_required"
    assert "ODD_HOUR_ACTIVITY" in by_id["TXN004"]["reason_codes"]
    assert any(result["status"] == "settled" and result["settlement_reference"] for result in results)


def test_pipeline_outputs_do_not_leak_sensitive_fields(tmp_path):
    directories, _, _ = _run_sample(tmp_path)
    combined = "\n".join(path.read_text(encoding="utf-8") for path in directories["results"].glob("*.json"))
    for record in json.loads(SAMPLE.read_text(encoding="utf-8")):
        assert record["description"] not in combined
        assert record["source_account"] not in combined
        assert record["destination_account"] not in combined


def test_cli_full_pipeline_writes_summary(tmp_path):
    shared = tmp_path / "shared"
    exit_code = main(["--input", str(SAMPLE), "--shared-dir", str(shared)])
    assert exit_code == 0
    summary = json.loads((shared / "results" / "summary.json").read_text(encoding="utf-8"))
    assert summary["total_transactions"] == 8


def test_cli_repeated_runs_archive_previous_results(tmp_path):
    shared = tmp_path / "shared"
    assert main(["--input", str(SAMPLE), "--shared-dir", str(shared)]) == 0
    first_generated_at = json.loads((shared / "results" / "summary.json").read_text(encoding="utf-8"))["generated_at"]

    assert main(["--input", str(SAMPLE), "--shared-dir", str(shared)]) == 0

    archive_summary = tmp_path / "archive" / "shared-001" / "results" / "summary.json"
    current_summary = shared / "results" / "summary.json"
    assert archive_summary.exists()
    assert json.loads(archive_summary.read_text(encoding="utf-8"))["generated_at"] == first_generated_at
    assert json.loads(current_summary.read_text(encoding="utf-8"))["total_transactions"] == 8


def test_cli_validate_only_has_no_settlement_references(tmp_path):
    shared = tmp_path / "shared"
    exit_code = main(["--validate-only", "--input", str(SAMPLE), "--shared-dir", str(shared)])
    assert exit_code == 0
    result_files = [path for path in (shared / "results").glob("TXN*.json")]
    assert len(result_files) == 8
    for path in result_files:
        result = json.loads(path.read_text(encoding="utf-8"))
        assert result["settlement_reference"] is None

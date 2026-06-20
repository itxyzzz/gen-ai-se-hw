import json
from pathlib import Path

from integrator import (
    fingerprint_file,
    load_transactions,
    main,
    prepare_shared_directories,
    run_pipeline,
    write_run_provenance,
)

PROJECT_ROOT = Path(__file__).resolve().parents[1]
SAMPLE = PROJECT_ROOT / "sample-transactions.json"
SPEC = PROJECT_ROOT / "specification.md"


def test_prepare_shared_directories_archives_existing_shared_tree(tmp_path):
    dirs = prepare_shared_directories(tmp_path / "shared")
    (dirs["results"] / "summary.json").write_text('{"run": "previous"}', encoding="utf-8")
    fresh_dirs = prepare_shared_directories(tmp_path / "shared")
    archived_summary = tmp_path / "archive" / "shared-001" / "results" / "summary.json"
    assert archived_summary.exists()
    assert json.loads(archived_summary.read_text(encoding="utf-8")) == {"run": "previous"}
    assert all(path.exists() for path in fresh_dirs.values())


def test_prepare_shared_directories_uses_next_zero_padded_archive_id(tmp_path):
    dirs = prepare_shared_directories(tmp_path / "shared")
    (dirs["results"] / "summary.json").write_text('{"run": 1}', encoding="utf-8")
    prepare_shared_directories(tmp_path / "shared")
    (tmp_path / "shared" / "results" / "summary.json").write_text('{"run": 2}', encoding="utf-8")
    prepare_shared_directories(tmp_path / "shared")
    assert (tmp_path / "archive" / "shared-001" / "results" / "summary.json").exists()
    assert (tmp_path / "archive" / "shared-002" / "results" / "summary.json").exists()


def test_write_run_provenance_contains_non_sensitive_traceability(tmp_path):
    shared = tmp_path / "shared"
    shared.mkdir()
    provenance = write_run_provenance(shared, runtime_run_id="run-1", spec_path=SPEC)
    text = json.dumps(provenance, sort_keys=True)
    assert provenance["source_spec"]["run_id"] == "20260619-170102-write-spec-python-fresh"
    assert provenance["source_spec"]["sha256"] == "44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B"
    assert "metadata" not in text
    assert "description" not in text


def test_fingerprint_file_returns_sha256(tmp_path):
    path = tmp_path / "file.txt"
    path.write_text("abc", encoding="utf-8")
    assert fingerprint_file(path) == "BA7816BF8F01CFEA414140DE5DAE2223B00361A396177A9CB410FF61F20015AD"


def test_full_pipeline_processes_all_sample_transactions(tmp_path):
    summary = run_pipeline(input_path=SAMPLE, shared_dir=tmp_path / "shared", spec_path=SPEC)
    results_dir = tmp_path / "shared" / "results"
    by_id = {path.stem: json.loads(path.read_text(encoding="utf-8")) for path in results_dir.glob("TXN*.json")}
    assert summary["total_transactions"] == 8
    assert summary["settled"] == 2
    assert summary["rejected"] == 2
    assert summary["review_required"] == 4
    assert summary["error"] == 0
    assert by_id["TXN001"]["status"] == "settled"
    assert by_id["TXN008"]["status"] == "settled"
    assert by_id["TXN002"]["status"] == "review_required"
    assert by_id["TXN003"]["status"] == "review_required"
    assert by_id["TXN004"]["status"] == "review_required"
    assert by_id["TXN005"]["status"] == "review_required"
    assert "UNSUPPORTED_CURRENCY" in by_id["TXN006"]["reason_codes"]
    assert "NON_POSITIVE_AMOUNT" in by_id["TXN007"]["reason_codes"]


def test_pipeline_writes_deterministic_protocol_filenames(tmp_path):
    run_pipeline(input_path=SAMPLE, shared_dir=tmp_path / "shared", spec_path=SPEC)
    assert (tmp_path / "shared" / "input" / "001-TXN001.json").exists()
    assert (tmp_path / "shared" / "processing" / "001-TXN001-transaction-validator.json").exists()
    assert (tmp_path / "shared" / "output" / "001-TXN001-fraud-detector.json").exists()
    assert (tmp_path / "shared" / "output" / "001-TXN001-settlement-processor.json").exists()


def test_pipeline_outputs_do_not_leak_sensitive_fields(tmp_path):
    run_pipeline(input_path=SAMPLE, shared_dir=tmp_path / "shared", spec_path=SPEC)
    combined = "\n".join(path.read_text(encoding="utf-8") for path in (tmp_path / "shared" / "results").glob("*.json"))
    for record in json.loads(SAMPLE.read_text(encoding="utf-8")):
        assert record["description"] not in combined
        assert record["source_account"] not in combined
        assert record["destination_account"] not in combined


def test_cli_full_pipeline_writes_summary(tmp_path):
    exit_code = main(["--input", str(SAMPLE), "--shared-dir", str(tmp_path / "shared"), "--spec-path", str(SPEC)])
    assert exit_code == 0
    summary = json.loads((tmp_path / "shared" / "results" / "summary.json").read_text(encoding="utf-8"))
    assert summary["total_transactions"] == 8


def test_cli_repeated_runs_archive_previous_results(tmp_path):
    shared = tmp_path / "shared"
    assert main(["--input", str(SAMPLE), "--shared-dir", str(shared), "--spec-path", str(SPEC)]) == 0
    first_summary = json.loads((shared / "results" / "summary.json").read_text(encoding="utf-8"))
    assert main(["--input", str(SAMPLE), "--shared-dir", str(shared), "--spec-path", str(SPEC)]) == 0
    archive_summary = json.loads((tmp_path / "archive" / "shared-001" / "results" / "summary.json").read_text(encoding="utf-8"))
    assert archive_summary["runtime_run_id"] == first_summary["runtime_run_id"]
    assert json.loads((shared / "results" / "summary.json").read_text(encoding="utf-8"))["total_transactions"] == 8


def test_cli_validate_only_reports_two_rejections(tmp_path):
    shared = tmp_path / "shared"
    assert main(["--validate-only", "--input", str(SAMPLE), "--shared-dir", str(shared), "--spec-path", str(SPEC)]) == 0
    summary = json.loads((shared / "results" / "summary.json").read_text(encoding="utf-8"))
    assert summary["total_transactions"] == 8
    assert summary["rejected"] == 2


def test_load_transactions_rejects_duplicate_ids(tmp_path):
    duplicate = tmp_path / "dupe.json"
    duplicate.write_text('[{"transaction_id":"TXN001"},{"transaction_id":"TXN001"}]', encoding="utf-8")
    try:
        load_transactions(duplicate)
    except Exception as exc:
        assert getattr(exc, "reason_code", "") == "DUPLICATE_TRANSACTION_ID"

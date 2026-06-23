from __future__ import annotations

import json
from pathlib import Path

import pytest

from agents import fraud_detector, settlement_processor, transaction_validator
from agents.common import REASON_PROCESSING_ERROR, STATUS_ERROR
from pipeline_api import PipelineRuntimeApi


def _load_records(sample_input: Path) -> list[dict]:
    return json.loads(sample_input.read_text(encoding="utf-8"))


def _create_run(api: PipelineRuntimeApi, tmp_path: Path, sample_input: Path) -> dict:
    return api.create_run(
        base_dir=tmp_path,
        shared_dir_name="shared",
        input_records=_load_records(sample_input),
    )


def _process_message_to_settlement(message: dict) -> dict:
    message = transaction_validator.process_message(message)
    message = fraud_detector.process_message(message)
    return settlement_processor.process_message(message)


def test_api_initializes_run_and_writes_sanitized_inputs(sample_input: Path, tmp_path: Path) -> None:
    api = PipelineRuntimeApi()

    response = _create_run(api, tmp_path, sample_input)

    assert response["run_id"]
    assert response["message_count"] == 8
    assert response["expected_transaction_ids"] == [f"TXN{index:03d}" for index in range(1, 9)]
    seeded = tmp_path / "shared" / "input" / "TXN001.json"
    assert seeded.exists()
    seeded_text = seeded.read_text(encoding="utf-8")
    assert "ACC-1001" not in seeded_text
    assert "Monthly rent payment" not in seeded_text


def test_api_records_processing_and_output_stage_files(sample_input: Path, tmp_path: Path) -> None:
    api = PipelineRuntimeApi()
    run = _create_run(api, tmp_path, sample_input)
    run_id = run["run_id"]
    message = api.get_message(run_id, "TXN001")["message"]

    api.record_stage(run_id, "TXN001", "processing", message)
    output_message = _process_message_to_settlement(message)
    api.record_stage(run_id, "TXN001", "output", output_message)

    assert (tmp_path / "shared" / "processing" / "TXN001.json").exists()
    assert (tmp_path / "shared" / "output" / "TXN001.json").exists()


def test_api_owns_result_persistence(sample_input: Path, tmp_path: Path) -> None:
    api = PipelineRuntimeApi()
    run = _create_run(api, tmp_path, sample_input)
    run_id = run["run_id"]
    message = _process_message_to_settlement(api.get_message(run_id, "TXN001")["message"])

    response = api.record_stage(run_id, "TXN001", "result", message)

    assert response == {
        "run_id": run_id,
        "transaction_id": "TXN001",
        "stage": "result",
        "status": "recorded",
    }
    result = json.loads((tmp_path / "shared" / "results" / "TXN001.json").read_text(encoding="utf-8"))
    assert result["privacy_check"] == "passed"
    assert result["status"] == "settled"
    assert result["component_history_count"] == 4
    assert result["audit_event_count"] == 4
    assert "ACC-1001" not in json.dumps(result, sort_keys=True)


def test_api_finalizes_summary_and_pipeline_status(sample_input: Path, tmp_path: Path) -> None:
    api = PipelineRuntimeApi()
    run = _create_run(api, tmp_path, sample_input)
    run_id = run["run_id"]

    for transaction_id in run["expected_transaction_ids"]:
        message = api.get_message(run_id, transaction_id)["message"]
        api.record_stage(run_id, transaction_id, "processing", message)
        message = _process_message_to_settlement(message)
        api.record_stage(run_id, transaction_id, "output", message)
        api.record_stage(run_id, transaction_id, "result", message)

    summary = api.finalize_run(run_id)

    assert summary["total_records"] == 8
    assert summary["settled"] == 2
    assert summary["rejected"] == 2
    assert summary["review_required"] == 4
    assert summary["error"] == 0
    assert (tmp_path / "shared" / "results" / "summary.json").exists()
    assert (tmp_path / "shared" / "results" / "pipeline-status.json").exists()


def test_api_records_processing_errors(sample_input: Path, tmp_path: Path) -> None:
    api = PipelineRuntimeApi()
    run = _create_run(api, tmp_path, sample_input)
    run_id = run["run_id"]

    response = api.record_error(run_id, "TXN001", REASON_PROCESSING_ERROR)

    assert response["status"] == STATUS_ERROR
    assert response["reason_codes"] == [REASON_PROCESSING_ERROR]
    result = json.loads((tmp_path / "shared" / "results" / "TXN001.json").read_text(encoding="utf-8"))
    assert result["status"] == STATUS_ERROR
    assert result["reason_codes"] == [REASON_PROCESSING_ERROR]


def test_api_rejects_unknown_runs_transactions_and_stages(sample_input: Path, tmp_path: Path) -> None:
    api = PipelineRuntimeApi()
    run = _create_run(api, tmp_path, sample_input)
    run_id = run["run_id"]
    message = api.get_message(run_id, "TXN001")["message"]

    with pytest.raises(ValueError, match="Unknown run_id"):
        api.get_message("missing", "TXN001")
    with pytest.raises(ValueError, match="Unknown transaction_id"):
        api.get_message(run_id, "NOPE")
    with pytest.raises(ValueError, match="Unknown transaction_id"):
        api.record_error(run_id, "NOPE", REASON_PROCESSING_ERROR)
    with pytest.raises(ValueError, match="Unknown stage"):
        api.record_stage(run_id, "TXN001", "mystery", message)


def test_api_rejects_sensitive_stage_payloads_before_persistence(sample_input: Path, tmp_path: Path) -> None:
    api = PipelineRuntimeApi()
    run = _create_run(api, tmp_path, sample_input)
    run_id = run["run_id"]
    message = api.get_message(run_id, "TXN001")["message"]
    message["data"]["source_account"] = "ACC-1001"

    with pytest.raises(ValueError, match="raw account identifier"):
        api.record_stage(run_id, "TXN001", "processing", message)

    assert not (tmp_path / "shared" / "processing" / "TXN001.json").exists()

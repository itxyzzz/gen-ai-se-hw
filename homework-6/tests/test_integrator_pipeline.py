from __future__ import annotations

import json
from pathlib import Path

import pytest

import integrator
from pipeline_api import PipelineRuntimeApi
from integrator import run_pipeline, validate_transactions_only


def test_full_pipeline_processes_all_sample_records(sample_input: Path, tmp_path: Path) -> None:
    summary = run_pipeline(base_dir=tmp_path, input_path=sample_input)
    assert summary["total_records"] == 8
    assert summary["settled"] == 2
    assert summary["rejected"] == 2
    assert summary["review_required"] == 4
    assert summary["error"] == 0
    assert summary["completeness_check"] == "passed"
    assert (tmp_path / "shared/results/pipeline-status.json").exists()
    assert len(list((tmp_path / "shared/results").glob("TXN*.json"))) == 8


def test_rerun_archives_existing_shared_tree(sample_input: Path, tmp_path: Path) -> None:
    run_pipeline(base_dir=tmp_path, input_path=sample_input)
    first_summary = json.loads((tmp_path / "shared/results/summary.json").read_text(encoding="utf-8"))
    run_pipeline(base_dir=tmp_path, input_path=sample_input)
    second_summary = json.loads((tmp_path / "shared/results/summary.json").read_text(encoding="utf-8"))
    assert (tmp_path / "archive/shared-001").exists()
    assert first_summary["runtime_run_id"] != second_summary["runtime_run_id"]


def test_validate_transactions_only_does_not_create_shared(sample_input: Path, tmp_path: Path) -> None:
    results = validate_transactions_only(sample_input)
    assert len(results) == 8
    assert sum(1 for item in results if item["status"] == "rejected") == 2
    assert not (tmp_path / "shared").exists()


def test_runtime_results_do_not_expose_raw_sensitive_fields(sample_input: Path, tmp_path: Path) -> None:
    run_pipeline(base_dir=tmp_path, input_path=sample_input)
    output_text = (tmp_path / "shared").read_text(encoding="utf-8") if (tmp_path / "shared").is_file() else ""
    for path in (tmp_path / "shared").rglob("*.json"):
        if "input" in path.parts or "processing" in path.parts:
            continue
        output_text += path.read_text(encoding="utf-8")
    assert "ACC-" + "1001" not in output_text
    assert "Monthly " + "rent payment" not in output_text


def test_run_pipeline_uses_api_stage_boundary(sample_input: Path, tmp_path: Path, monkeypatch: pytest.MonkeyPatch) -> None:
    calls: list[str] = []

    class TrackingApi(PipelineRuntimeApi):
        def record_stage(self, run_id: str, transaction_id: str, stage: str, message: dict) -> dict:
            calls.append(stage)
            return super().record_stage(run_id, transaction_id, stage, message)

    monkeypatch.setattr(integrator, "PipelineRuntimeApi", TrackingApi)
    monkeypatch.setattr(
        integrator,
        "process_transaction",
        lambda *_args, **_kwargs: pytest.fail("run_pipeline must not use file-path transaction handoff"),
    )

    summary = run_pipeline(base_dir=tmp_path, input_path=sample_input)

    assert summary["total_records"] == 8
    assert calls.count("processing") == 8
    assert calls.count("output") == 8
    assert calls.count("result") == 8

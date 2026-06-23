from __future__ import annotations

import json
from pathlib import Path

import pytest

from agents.common import (
    REASON_MISSING_FIELD,
    STATUS_REJECTED,
    STATUS_SETTLED,
    STATUS_VALIDATED,
)
from agents.transaction_validator import validate_transaction
from integrator import run_pipeline
from scripts.generate_transactions import (
    ALL_CATEGORIES,
    REQUIRED_INPUT_FIELDS,
    generate,
    summarize,
)


def _balanced():
    return generate(balanced=True, seed=42)


def test_generation_is_deterministic_for_a_seed() -> None:
    first, _ = generate(balanced=True, seed=123)
    second, _ = generate(balanced=True, seed=123)
    assert first == second


def test_balanced_set_covers_every_category() -> None:
    _, manifest = _balanced()
    assert {entry["category"] for entry in manifest} == set(ALL_CATEGORIES)


def test_balanced_set_has_both_valid_and_invalid_records() -> None:
    _, manifest = _balanced()
    summary = summarize(manifest)
    assert summary["valid"] > 0
    assert summary["invalid"] > 0
    assert summary["valid"] + summary["invalid"] == summary["total"]


def test_records_match_canonical_format() -> None:
    transactions, manifest = _balanced()
    for record, entry in zip(transactions, manifest):
        # amounts must be strings (the validator rejects float amounts outright)
        assert isinstance(record["amount"], str)
        if entry["category"] != "invalid_missing_field":
            assert REQUIRED_INPUT_FIELDS <= record.keys()
            assert set(record["metadata"]) == {"channel", "country"}


def test_validator_outcomes_match_manifest() -> None:
    transactions, manifest = _balanced()
    for record, entry in zip(transactions, manifest):
        result = validate_transaction(dict(record))
        assert result["status"] == entry["expected_validation"]
        for reason in entry["expected_validation_reason_codes"]:
            assert reason in result["reason_codes"]
        if entry["category"].startswith("invalid_"):
            # each invalid category isolates exactly one validator reason code
            assert len(result["reason_codes"]) == 1


def test_pipeline_outcomes_match_manifest(tmp_path: Path) -> None:
    transactions, manifest = _balanced()
    input_path = tmp_path / "generated.json"
    input_path.write_text(json.dumps(transactions), encoding="utf-8")

    run_pipeline(base_dir=tmp_path, input_path=input_path, shared_dir_name="shared")
    results_dir = tmp_path / "shared" / "results"
    by_id = {
        json.loads(path.read_text(encoding="utf-8"))["transaction_id"]: json.loads(
            path.read_text(encoding="utf-8")
        )
        for path in results_dir.glob("TXN*.json")
    }

    for entry in manifest:
        result = by_id[entry["transaction_id"]]
        assert result["status"] == entry["expected_pipeline_status"]
        for reason in entry["expected_pipeline_reason_codes"]:
            assert reason in result["reason_codes"]


def test_missing_field_diverges_between_validator_and_pipeline(tmp_path: Path) -> None:
    """MISSING_FIELD is rejected by the validator but backfilled by the integrator."""

    transactions, manifest = generate(count=len(ALL_CATEGORIES), balanced=True, seed=7)
    missing = next(e for e in manifest if e["category"] == "invalid_missing_field")
    record = next(t for t in transactions if t["transaction_id"] == missing["transaction_id"])

    validator_result = validate_transaction(dict(record))
    assert validator_result["status"] == STATUS_REJECTED
    assert REASON_MISSING_FIELD in validator_result["reason_codes"]

    input_path = tmp_path / "generated.json"
    input_path.write_text(json.dumps(transactions), encoding="utf-8")
    run_pipeline(base_dir=tmp_path, input_path=input_path, shared_dir_name="shared")
    pipeline_result = json.loads(
        (tmp_path / "shared" / "results" / f"{missing['transaction_id']}.json").read_text(encoding="utf-8")
    )
    assert pipeline_result["status"] == STATUS_SETTLED


def test_invalid_ratio_bounds_are_enforced() -> None:
    with pytest.raises(ValueError):
        generate(count=4, invalid_ratio=1.5)


def test_unbalanced_mix_respects_invalid_ratio() -> None:
    _, manifest = generate(count=10, seed=1, invalid_ratio=0.5, review_ratio=0.0)
    summary = summarize(manifest)
    assert summary["invalid"] == 5
    assert all(entry["expected_validation"] == STATUS_VALIDATED for entry in manifest if entry["category"] == "valid_settled")

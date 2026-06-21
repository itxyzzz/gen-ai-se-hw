from __future__ import annotations

from decimal import Decimal
import json
from pathlib import Path

import pytest

from agents.common import (
    COMPONENT_FRAUD,
    COMPONENT_INTEGRATOR,
    COMPONENT_VALIDATOR,
    REASON_INVALID_AMOUNT,
    REASON_INVALID_TIMESTAMP,
    REASON_MISSING_FIELD,
    REASON_PROCESSING_ERROR,
    REASON_REVIEW_CHANNEL_PATTERN,
    REASON_SETTLED,
    REASON_UNSUPPORTED_CURRENCY,
    STATUS_ERROR,
    STATUS_REJECTED,
    STATUS_REVIEW_REQUIRED,
    STATUS_SETTLED,
    STATUS_VALIDATED,
    ValidationError,
    append_audit_event,
    assert_no_raw_account_ids,
    create_message,
    get_protocol_paths,
    parse_amount,
    redact_account_id,
    safe_json_dump,
    sanitize_transaction,
    serialize_amount,
    to_json_safe,
)
from agents import fraud_detector, reporting_agent, settlement_processor, transaction_validator
from integrator import (
    _base_and_shared_name,
    archive_existing_shared,
    load_transactions,
    prepare_shared_directories,
    run_pipeline,
    safe_process_transaction,
    seed_input_messages,
    validate_transactions_only,
    write_run_provenance,
)


def _record(records: list[dict], transaction_id: str) -> dict:
    return next(item for item in records if item["transaction_id"] == transaction_id)


def test_common_rejects_non_string_non_finite_and_non_decimal_money() -> None:
    with pytest.raises(ValidationError) as non_string:
        parse_amount(Decimal("12.00"))  # type: ignore[arg-type]
    assert non_string.value.reason_code == REASON_INVALID_AMOUNT

    with pytest.raises(ValidationError) as non_finite:
        parse_amount("NaN")
    assert non_finite.value.reason_code == REASON_INVALID_AMOUNT

    with pytest.raises(TypeError):
        serialize_amount("12.00")  # type: ignore[arg-type]

    with pytest.raises(ValueError):
        serialize_amount(Decimal("Infinity"))


def test_common_sanitizes_metadata_and_json_without_sensitive_accounts() -> None:
    raw_account = "ACC-" + "123456"
    sanitized = sanitize_transaction(
        {
            "transaction_id": "TXN900",
            "timestamp": "2026-03-16T10:00:00Z",
            "amount": "10.00",
            "currency": "usd",
            "transaction_type": "transfer",
            "source_account": raw_account,
            "destination_account": "",
            "metadata": {"channel": "mobile", "country": "US", "note": "not copied"},
        }
    )

    assert sanitized["currency"] == "USD"
    assert sanitized["source_account_redacted"] == "ACC-****3456"
    assert sanitized["destination_account_redacted"] == ""
    assert "note" not in sanitized
    assert_no_raw_account_ids(sanitized)
    assert to_json_safe({"amount": Decimal("10.50")}) == {"amount": "10.50"}
    assert redact_account_id("123") == "****"
    assert redact_account_id("USER-abcdef") == "****cdef"


def test_common_message_audit_and_protocol_paths_are_safe(tmp_path: Path) -> None:
    message = create_message(
        source_agent=COMPONENT_INTEGRATOR,
        target_agent=COMPONENT_VALIDATOR,
        data={"transaction_id": "TXN901", "status": "received"},
        timestamp="2026-03-16T10:00:00Z",
    )
    append_audit_event(message, COMPONENT_VALIDATOR, STATUS_VALIDATED, REASON_SETTLED)
    paths = get_protocol_paths(tmp_path, "custom-shared")

    assert message["audit_events"][0] == {
        "timestamp": message["audit_events"][0]["timestamp"],
        "component": COMPONENT_VALIDATOR,
        "transaction_id": "TXN901",
        "outcome": STATUS_VALIDATED,
        "reason_code": REASON_SETTLED,
    }
    assert paths["shared"] == tmp_path / "custom-shared"
    assert paths["archive"] == tmp_path / "archive"


def test_validator_reports_multiple_safe_reason_codes(sample_records: list[dict]) -> None:
    invalid = _record(sample_records, "TXN001").copy()
    invalid.pop("metadata")
    invalid["timestamp"] = "not-a-timestamp"
    invalid["amount"] = "not-money"
    invalid["currency"] = "XYZ"

    result = transaction_validator.validate_transaction(invalid)

    assert result["status"] == STATUS_REJECTED
    assert result["reason_codes"] == [
        REASON_MISSING_FIELD,
        REASON_INVALID_AMOUNT,
        REASON_UNSUPPORTED_CURRENCY,
        REASON_INVALID_TIMESTAMP,
    ]
    assert "source_account" not in result
    assert "description" not in result


def test_validator_process_message_adds_history_and_safe_audit(sample_records: list[dict]) -> None:
    message = create_message(COMPONENT_INTEGRATOR, COMPONENT_VALIDATOR, _record(sample_records, "TXN006"))
    result = transaction_validator.process_message(message)

    assert result["source_agent"] == COMPONENT_VALIDATOR
    assert result["target_agent"] == COMPONENT_FRAUD
    assert result["component_history"][0]["component"] == COMPONENT_VALIDATOR
    assert result["audit_events"][0]["reason_code"] == REASON_UNSUPPORTED_CURRENCY
    assert result["data"]["status"] == STATUS_REJECTED


def test_validation_file_helper_returns_only_safe_fields(sample_input: Path) -> None:
    report = transaction_validator.validate_transactions_file(str(sample_input))

    assert report["total"] == 8
    assert report["valid"] == 6
    assert report["rejected"] == 2
    assert {item["transaction_id"] for item in report["results"]} == {f"TXN00{i}" for i in range(1, 9)}
    assert "ACC-" not in json.dumps(report)


def test_fraud_detector_preserves_rejected_and_scores_channel_pattern(sample_records: list[dict]) -> None:
    rejected = transaction_validator.validate_transaction(_record(sample_records, "TXN006"))
    rejected_result = fraud_detector.score_fraud_risk(rejected)
    assert rejected_result["status"] == STATUS_REJECTED
    assert rejected_result["risk_score"] == 0
    assert rejected_result["risk_flags"] == []

    channel_record = _record(sample_records, "TXN004").copy()
    channel_record["timestamp"] = "2026-03-16T10:00:00Z"
    scored = fraud_detector.score_fraud_risk(transaction_validator.validate_transaction(channel_record))
    assert scored["status"] == STATUS_REVIEW_REQUIRED
    assert REASON_REVIEW_CHANNEL_PATTERN in scored["reason_codes"]


def test_fraud_detector_wire_modifier_alone_does_not_force_review(sample_records: list[dict]) -> None:
    record = _record(sample_records, "TXN001").copy()
    record["transaction_type"] = "wire_transfer"
    scored = fraud_detector.score_fraud_risk(transaction_validator.validate_transaction(record))

    assert scored["risk_score"] == 10
    assert scored["status"] == STATUS_VALIDATED
    assert scored["risk_flags"] == []


def test_settlement_preserves_rejected_and_rejects_unknown_status() -> None:
    rejected = {"transaction_id": "TXN006", "status": STATUS_REJECTED, "reason_codes": [REASON_UNSUPPORTED_CURRENCY]}
    assert settlement_processor.settle_transaction(rejected.copy(), STATUS_REJECTED)["status"] == STATUS_REJECTED

    unknown = settlement_processor.settle_transaction({"transaction_id": "TXN999"}, "unexpected")
    assert unknown["status"] == STATUS_REJECTED
    assert "settlement_reference" not in unknown


def test_settlement_process_message_emits_reporting_target_and_audit() -> None:
    message = create_message(
        COMPONENT_FRAUD,
        "settlement_processor",
        {"transaction_id": "TXN001", "status": STATUS_VALIDATED, "reason_codes": []},
    )
    result = settlement_processor.process_message(message)

    assert result["data"]["status"] == STATUS_SETTLED
    assert result["data"]["reason_codes"] == [REASON_SETTLED]
    assert result["target_agent"] == "reporting_agent"
    assert result["audit_events"][-1]["reason_code"] == REASON_SETTLED


def test_reporting_builds_status_variants_and_rejects_sensitive_payloads(tmp_path: Path) -> None:
    for status, expected_summary in [
        (STATUS_SETTLED, "Simulated transaction outcome recorded."),
        (STATUS_REVIEW_REQUIRED, "Simulated review outcome recorded."),
        (STATUS_REJECTED, "Simulated rejection outcome recorded."),
        (STATUS_ERROR, "Simulated error outcome recorded."),
    ]:
        message = create_message("component", "reporting_agent", {"transaction_id": "TXN100", "status": status})
        payload = reporting_agent.build_transaction_status_payload(message)
        assert payload["safe_summary"] == expected_summary
        assert payload["privacy_check"] == "passed"

    with pytest.raises(ValueError):
        reporting_agent.assert_privacy_safe({"description": "raw text"})
    with pytest.raises(ValueError):
        reporting_agent.assert_privacy_safe({"nested": "ACC-" + "9999"})

    error_path = reporting_agent.write_error_result("TXNERR", REASON_PROCESSING_ERROR, tmp_path)
    error_payload = json.loads(error_path.read_text(encoding="utf-8"))
    assert error_payload["status"] == STATUS_ERROR
    assert error_payload["reason_codes"] == [REASON_PROCESSING_ERROR]


def test_reporting_summary_detects_incomplete_results_and_pipeline_status(tmp_path: Path) -> None:
    message = create_message(
        "settlement_processor",
        "reporting_agent",
        {"transaction_id": "TXN001", "status": STATUS_SETTLED, "reason_codes": [REASON_SETTLED]},
    )
    reporting_agent.write_transaction_result(message, tmp_path)
    summary = reporting_agent.summarize_results(tmp_path, ["TXN001", "TXN002"], "run-2")
    status = reporting_agent.build_pipeline_status(summary)

    assert reporting_agent.list_result_files(tmp_path) == ["TXN001.json"]
    assert summary["completeness_check"] == "failed"
    assert status["run_status"] == "incomplete"
    assert status["summary_path"] == "shared/results/summary.json"


def test_integrator_rejects_missing_malformed_and_non_list_inputs(tmp_path: Path) -> None:
    missing = tmp_path / "missing.json"
    with pytest.raises(FileNotFoundError):
        load_transactions(missing)

    malformed = tmp_path / "malformed.json"
    malformed.write_text("{not-json", encoding="utf-8")
    with pytest.raises(ValueError, match="not valid JSON"):
        load_transactions(malformed)

    non_list = tmp_path / "object.json"
    non_list.write_text("{}", encoding="utf-8")
    with pytest.raises(ValueError, match="must be a list"):
        load_transactions(non_list)


def test_integrator_seeds_privacy_safe_messages_and_custom_shared(sample_records: list[dict], tmp_path: Path) -> None:
    paths = prepare_shared_directories(tmp_path, "run-shared")
    message_paths = seed_input_messages([_record(sample_records, "TXN003")], paths)
    payload = json.loads(message_paths[0].read_text(encoding="utf-8"))

    assert _base_and_shared_name(Path("nested/shared-here")) == (Path("nested"), "shared-here")
    assert payload["data"]["source_account"] == "PRESENT"
    assert payload["data"]["destination_pattern"] == "review"
    assert "ACC-" not in json.dumps(payload)
    assert (tmp_path / "run-shared/input/TXN003.json").exists()


def test_integrator_archives_existing_shared_with_incrementing_names(tmp_path: Path) -> None:
    shared = tmp_path / "shared"
    shared.mkdir()
    (shared / "marker.txt").write_text("first", encoding="utf-8")
    first_archive = archive_existing_shared(tmp_path)
    second_archive = archive_existing_shared(tmp_path)

    assert first_archive == tmp_path / "archive/shared-001"
    assert (tmp_path / "archive/shared-001/marker.txt").read_text(encoding="utf-8") == "first"
    assert second_archive == tmp_path / "archive/shared-002"


def test_integrator_writes_safe_provenance_and_handles_bad_message(tmp_path: Path) -> None:
    paths = prepare_shared_directories(tmp_path)
    provenance_path = write_run_provenance(paths, {"runtime_run_id": "run-3", "generated_at": "2026-06-21T20:00:00Z"})
    provenance = json.loads(provenance_path.read_text(encoding="utf-8"))
    assert provenance["runtime_run_id"] == "run-3"
    assert "source_spec_sha256" in provenance
    assert "ACC-" not in json.dumps(provenance)

    bad_message = paths["input"] / "BROKEN.json"
    bad_message.write_text("{bad-json", encoding="utf-8")
    result = safe_process_transaction(bad_message, paths)
    error_payload = json.loads((paths["results"] / "BROKEN.json").read_text(encoding="utf-8"))
    assert result["data"]["status"] == STATUS_ERROR
    assert error_payload["reason_codes"] == [REASON_PROCESSING_ERROR]


def test_full_pipeline_outputs_summary_status_privacy_and_archival(sample_input: Path, tmp_path: Path) -> None:
    first = run_pipeline(base_dir=tmp_path, input_path=sample_input, shared_dir_name="isolated-shared")
    second = run_pipeline(base_dir=tmp_path, input_path=sample_input, shared_dir_name="isolated-shared")

    assert first["total_records"] == 8
    assert second["status_counts"] == {"rejected": 2, "review_required": 4, "settled": 2}
    assert (tmp_path / "archive/isolated-shared-001/results/summary.json").exists()
    status = json.loads((tmp_path / "isolated-shared/results/pipeline-status.json").read_text(encoding="utf-8"))
    assert status["run_status"] == "complete"
    assert status["total_records"] == 8

    safe_text = ""
    for path in (tmp_path / "isolated-shared/results").glob("*.json"):
        safe_text += path.read_text(encoding="utf-8")
    assert "ACC-" not in safe_text
    assert "Monthly " + "rent payment" not in safe_text


def test_validation_only_returns_safe_records_without_shared_side_effect(sample_input: Path, tmp_path: Path) -> None:
    results = validate_transactions_only(sample_input, base_dir=tmp_path)
    statuses = {item["transaction_id"]: item["status"] for item in results}
    reason_groups = {item["transaction_id"]: item["reason_codes"] for item in results}

    assert statuses["TXN001"] == STATUS_VALIDATED
    assert statuses["TXN006"] == STATUS_REJECTED
    assert REASON_UNSUPPORTED_CURRENCY in reason_groups["TXN006"]
    assert REASON_INVALID_AMOUNT not in reason_groups["TXN006"]
    assert not (tmp_path / "shared").exists()
    assert "ACC-" not in json.dumps(results)


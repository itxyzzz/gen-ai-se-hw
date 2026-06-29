from decimal import Decimal
import json

import pytest

from agents.common import (
    NON_POSITIVE_AMOUNT,
    PipelineError,
    assert_no_sensitive_fields,
    create_message,
    parse_amount,
    redact_account_id,
    serialize_amount,
    write_json_file,
)


def test_parse_amount_preserves_decimal_exactness():
    assert parse_amount("25000.00") == Decimal("25000.00")
    assert serialize_amount(Decimal("12.3")) == "12.30"


def test_parse_amount_rejects_float_and_negative_values():
    with pytest.raises(PipelineError):
        parse_amount(12.3)
    with pytest.raises(PipelineError) as exc_info:
        parse_amount("-100.00")
    assert exc_info.value.reason_code == NON_POSITIVE_AMOUNT


def test_redact_account_id_masks_sample_account_without_literal_leak():
    account_id = "ACC-" + "1001"
    assert redact_account_id(account_id) == "ACC-****1001"
    assert redact_account_id(None) == "REDACTED"


def test_write_json_file_rejects_non_finite_float(tmp_path):
    with pytest.raises(ValueError):
        write_json_file(tmp_path / "bad.json", {"value": float("nan")})


def test_write_json_file_serializes_decimal_as_string(tmp_path):
    path = tmp_path / "payload.json"
    write_json_file(path, {"amount": Decimal("12.30")})
    assert json.loads(path.read_text(encoding="utf-8"))["amount"] == "12.30"


def test_create_message_omits_sensitive_raw_fields():
    transaction = {
        "transaction_id": "TXN001",
        "timestamp": "2026-03-16T09:00:00Z",
        "source_account": "ACC-" + "1001",
        "destination_account": "ACC-" + "2001",
        "amount": "1500.00",
        "currency": "USD",
        "transaction_type": "transfer",
        "description": "private memo",
        "metadata": {"channel": "online", "country": "US"},
    }
    message = create_message(transaction, "integrator", "transaction_validator")
    text = json.dumps(message, sort_keys=True)
    assert "private memo" not in text
    assert "ACC-" + "1001" not in text
    assert "ACC-****1001" in text


def test_sensitive_key_scan_blocks_final_payload_leaks():
    with pytest.raises(PipelineError):
        assert_no_sensitive_fields({"source_account": "ACC-" + "1001"})


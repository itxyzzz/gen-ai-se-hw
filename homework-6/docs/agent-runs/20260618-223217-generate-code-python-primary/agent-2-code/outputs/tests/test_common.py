from decimal import Decimal
import json

import pytest

from agents.common import NON_POSITIVE_AMOUNT, PipelineError, parse_amount, redact_account_id, write_json_file


def test_parse_amount_preserves_decimal_exactness():
    assert parse_amount("25000.00") == Decimal("25000.00")
    assert parse_amount("75000.00") == Decimal("75000.00")


def test_parse_amount_rejects_negative_values():
    with pytest.raises(PipelineError) as exc_info:
        parse_amount("-100.00")
    assert exc_info.value.reason_code == NON_POSITIVE_AMOUNT


def test_redact_account_id_masks_sample_account():
    account_id = "ACC-" + "1001"
    assert redact_account_id(account_id) == "ACC-****1001"
    assert redact_account_id(None) == "REDACTED"
    assert redact_account_id("X") == "REDACTED"


def test_write_json_file_rejects_non_finite_float(tmp_path):
    with pytest.raises(ValueError):
        write_json_file(tmp_path / "bad.json", {"value": float("nan")})


def test_write_json_file_serializes_decimal_as_string(tmp_path):
    path = tmp_path / "payload.json"
    write_json_file(path, {"amount": Decimal("12.30")})
    assert json.loads(path.read_text(encoding="utf-8"))["amount"] == "12.30"


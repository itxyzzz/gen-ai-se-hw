from __future__ import annotations

from decimal import Decimal
import json

import pytest

from agents.common import (
    REASON_INVALID_AMOUNT,
    ValidationError,
    assert_no_raw_account_ids,
    parse_amount,
    redact_account_id,
    safe_json_dump,
    safe_json_load,
    serialize_amount,
)


def test_parse_amount_rejects_float() -> None:
    with pytest.raises(ValidationError) as excinfo:
        parse_amount(1.25)  # type: ignore[arg-type]
    assert excinfo.value.reason_code == REASON_INVALID_AMOUNT


def test_decimal_amount_round_trips_as_string(tmp_path) -> None:
    amount = parse_amount("1500.00")
    assert amount == Decimal("1500.00")
    assert serialize_amount(amount) == "1500.00"
    path = tmp_path / "payload.json"
    safe_json_dump({"amount": amount}, path)
    assert safe_json_load(path)["amount"] == "1500.00"


def test_safe_json_dump_uses_strict_json(tmp_path) -> None:
    path = tmp_path / "payload.json"
    with pytest.raises(ValueError):
        safe_json_dump({"bad": float("nan")}, path)


def test_redaction_and_privacy_scan() -> None:
    raw_account = "ACC-" + "1001"
    assert redact_account_id(raw_account) == "ACC-****1001"
    with pytest.raises(ValueError):
        assert_no_raw_account_ids({"source": raw_account})
    json.dumps({"source": redact_account_id(raw_account)}, allow_nan=False)

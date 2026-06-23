"""Random transaction generator for the Homework 6 educational pipeline.

This is an Operator Layer support tool, in the same family as
``/validate-transactions`` and ``/run-pipeline``. It is **not** a runtime
transaction pipeline component and is not named after a Greek deity.

The generator emits synthetic records in the canonical input format::

    {
      "transaction_id": "TXN001",
      "timestamp": "2026-03-16T09:00:00Z",
      "source_account": "ACC-1001",
      "destination_account": "ACC-2001",
      "amount": "1500.00",
      "currency": "USD",
      "transaction_type": "transfer",
      "description": "Monthly rent payment",
      "metadata": {"channel": "online", "country": "US"}
    }

It is written against the *actual* rules in ``agents.transaction_validator``,
``agents.fraud_detector``, and ``integrator`` so it can deliberately produce
records in each outcome class. Two layers can disagree, and the manifest
records both:

* ``validator`` layer  -- what ``agents.transaction_validator.validate_transaction``
  returns (this is what ``/validate-transactions`` reports).
* ``pipeline`` layer    -- the terminal status after the full integrator run
  (validator -> fraud -> settlement), which is what ``/run-pipeline`` reports.

The most notable disagreement: ``MISSING_FIELD`` is enforced by the validator,
but ``integrator.seed_input_messages`` backfills every required field, so the
full pipeline never surfaces it. The generator encodes that on purpose.
"""

from __future__ import annotations

import argparse
import json
import random
import sys
from dataclasses import dataclass
from datetime import date, datetime
from decimal import Decimal
from pathlib import Path
from typing import Any, Callable

# Allow ``python scripts/generate_transactions.py`` from the project root to
# import the canonical rule definitions instead of duplicating them.
_PROJECT_ROOT = Path(__file__).resolve().parents[1]
if str(_PROJECT_ROOT) not in sys.path:
    sys.path.insert(0, str(_PROJECT_ROOT))

from agents.common import (  # noqa: E402  (import after sys.path setup)
    ALLOWED_CURRENCIES,
    REASON_INVALID_AMOUNT,
    REASON_INVALID_TIMESTAMP,
    REASON_MISSING_FIELD,
    REASON_NON_POSITIVE_AMOUNT,
    REASON_REVIEW_CHANNEL_PATTERN,
    REASON_REVIEW_DESTINATION_PATTERN,
    REASON_REVIEW_HIGH_VALUE,
    REASON_REVIEW_UNUSUAL_TIME,
    REASON_SETTLED,
    REASON_UNSUPPORTED_CURRENCY,
    STATUS_REJECTED,
    STATUS_REVIEW_REQUIRED,
    STATUS_SETTLED,
    STATUS_VALIDATED,
)
from agents.transaction_validator import REQUIRED_FIELDS  # noqa: E402

# The canonical input fields the validator requires. Generated records (except
# the deliberate invalid_missing_field class) always include all of these.
REQUIRED_INPUT_FIELDS = frozenset(REQUIRED_FIELDS)

# --- Rule thresholds (mirrors of agents.fraud_detector / integrator) ----------
# fraud_detector flags high value at >= 25000.00 and unusual time at hour < 4;
# integrator marks destination_pattern="review" when the destination account
# ends with "9999". These constants encode those rules so generated records
# land on the intended side of each branch.
HIGH_VALUE_THRESHOLD = Decimal("25000.00")
UNUSUAL_HOUR_MAX_EXCLUSIVE = 4
REVIEW_DESTINATION_SUFFIX = "9999"

# Currencies the validator does not accept, used for the bad-currency class.
_UNSUPPORTED_CURRENCIES = ("XYZ", "JPY", "BTC", "ZZZ", "CHF", "CAD")
# Required fields that, when dropped, yield ONLY MISSING_FIELD at the validator
# (no cascade into INVALID_AMOUNT / UNSUPPORTED_CURRENCY / INVALID_TIMESTAMP).
_DROPPABLE_FIELDS = ("source_account", "destination_account", "transaction_type", "metadata")
_BAD_AMOUNTS = ("not-a-number", "12.3.4", "1,500.00", "", "USD 100")
_NON_POSITIVE_AMOUNTS = ("0.00", "-100.00", "-2500.50", "0")
_BAD_TIMESTAMPS = ("2026-13-40T99:99:99Z", "not-a-date", "2026/03/16 09:00", "16-03-2026", "")

_ALLOWED_CURRENCIES = tuple(sorted(ALLOWED_CURRENCIES))
_SAFE_CHANNELS = ("online", "branch")
_SUSPICIOUS_CHANNELS = ("mobile", "api")
_NON_US_COUNTRIES = ("DE", "GB", "FR", "CA")
_NORMAL_TYPES = ("transfer", "payment", "deposit")
_DESCRIPTIONS = (
    "Monthly subscription",
    "Invoice settlement",
    "Vendor payment",
    "Payroll transfer",
    "Utility bill",
    "Marketplace order",
    "Service retainer",
    "Reimbursement",
)


@dataclass(frozen=True)
class Expectation:
    """Predicted outcome for a generated record, at both layers."""

    validator_status: str  # validated | rejected
    pipeline_status: str  # settled | review_required | rejected
    validator_reason_codes: tuple[str, ...] = ()
    pipeline_reason_codes: tuple[str, ...] = ()
    note: str = ""


# Outcome contract for each category.
EXPECTATIONS: dict[str, Expectation] = {
    "valid_settled": Expectation(
        STATUS_VALIDATED, STATUS_SETTLED, pipeline_reason_codes=(REASON_SETTLED,)
    ),
    "review_high_value": Expectation(
        STATUS_VALIDATED, STATUS_REVIEW_REQUIRED, pipeline_reason_codes=(REASON_REVIEW_HIGH_VALUE,)
    ),
    "review_unusual_time": Expectation(
        STATUS_VALIDATED, STATUS_REVIEW_REQUIRED, pipeline_reason_codes=(REASON_REVIEW_UNUSUAL_TIME,)
    ),
    "review_channel_pattern": Expectation(
        STATUS_VALIDATED, STATUS_REVIEW_REQUIRED, pipeline_reason_codes=(REASON_REVIEW_CHANNEL_PATTERN,)
    ),
    "review_destination_pattern": Expectation(
        STATUS_VALIDATED,
        STATUS_REVIEW_REQUIRED,
        pipeline_reason_codes=(REASON_REVIEW_DESTINATION_PATTERN,),
    ),
    "invalid_missing_field": Expectation(
        STATUS_REJECTED,
        STATUS_SETTLED,
        validator_reason_codes=(REASON_MISSING_FIELD,),
        pipeline_reason_codes=(REASON_SETTLED,),
        note=(
            "Validator rejects with MISSING_FIELD, but integrator.seed_input_messages "
            "backfills required fields, so the full pipeline settles it. Use "
            "/validate-transactions to observe MISSING_FIELD."
        ),
    ),
    "invalid_amount": Expectation(
        STATUS_REJECTED,
        STATUS_REJECTED,
        validator_reason_codes=(REASON_INVALID_AMOUNT,),
        pipeline_reason_codes=(REASON_INVALID_AMOUNT,),
    ),
    "invalid_non_positive_amount": Expectation(
        STATUS_REJECTED,
        STATUS_REJECTED,
        validator_reason_codes=(REASON_NON_POSITIVE_AMOUNT,),
        pipeline_reason_codes=(REASON_NON_POSITIVE_AMOUNT,),
    ),
    "invalid_unsupported_currency": Expectation(
        STATUS_REJECTED,
        STATUS_REJECTED,
        validator_reason_codes=(REASON_UNSUPPORTED_CURRENCY,),
        pipeline_reason_codes=(REASON_UNSUPPORTED_CURRENCY,),
    ),
    "invalid_timestamp": Expectation(
        STATUS_REJECTED,
        STATUS_REJECTED,
        validator_reason_codes=(REASON_INVALID_TIMESTAMP,),
        pipeline_reason_codes=(REASON_INVALID_TIMESTAMP,),
    ),
}

VALID_CATEGORIES = ("valid_settled",)
REVIEW_CATEGORIES = (
    "review_high_value",
    "review_unusual_time",
    "review_channel_pattern",
    "review_destination_pattern",
)
INVALID_CATEGORIES = (
    "invalid_missing_field",
    "invalid_amount",
    "invalid_non_positive_amount",
    "invalid_unsupported_currency",
    "invalid_timestamp",
)
ALL_CATEGORIES = VALID_CATEGORIES + REVIEW_CATEGORIES + INVALID_CATEGORIES


def _money(rng: random.Random, low: float, high: float) -> str:
    cents = rng.randint(int(low * 100), int(high * 100))
    return f"{Decimal(cents) / 100:.2f}"


def _timestamp(base_date: date, rng: random.Random, *, unusual: bool) -> str:
    hour = rng.randint(0, UNUSUAL_HOUR_MAX_EXCLUSIVE - 1) if unusual else rng.randint(8, 18)
    minute = rng.choice((0, 15, 30, 45))
    moment = datetime(base_date.year, base_date.month, base_date.day, hour, minute, 0)
    return moment.strftime("%Y-%m-%dT%H:%M:%SZ")


def _account(rng: random.Random, lead: int, *, review: bool = False) -> str:
    if review:
        return f"ACC-{REVIEW_DESTINATION_SUFFIX}"
    return f"ACC-{lead}{rng.randint(0, 999):03d}"


def _base_record(index: int, rng: random.Random, base_date: date) -> dict[str, Any]:
    """Build a fully valid, low-risk record; callers mutate it per category."""

    return {
        "transaction_id": f"TXN{index:03d}",
        "timestamp": _timestamp(base_date, rng, unusual=False),
        "source_account": _account(rng, 1),
        "destination_account": _account(rng, rng.choice((2, 3, 5, 6))),
        "amount": _money(rng, 50.0, 9_999.99),
        "currency": rng.choice(_ALLOWED_CURRENCIES),
        "transaction_type": rng.choice(_NORMAL_TYPES),
        "description": rng.choice(_DESCRIPTIONS),
        "metadata": {"channel": rng.choice(_SAFE_CHANNELS), "country": "US"},
    }


def _make_valid_settled(record: dict[str, Any], rng: random.Random) -> None:
    # The base record is already valid and low risk; nothing to change.
    del record, rng


def _make_review_high_value(record: dict[str, Any], rng: random.Random) -> None:
    record["amount"] = _money(rng, 25_000.0, 200_000.0)
    record["transaction_type"] = "wire_transfer"


def _make_review_unusual_time(record: dict[str, Any], rng: random.Random, base_date: date) -> None:
    record["timestamp"] = _timestamp(base_date, rng, unusual=True)


def _make_review_channel_pattern(record: dict[str, Any], rng: random.Random) -> None:
    record["metadata"] = {
        "channel": rng.choice(_SUSPICIOUS_CHANNELS),
        "country": rng.choice(_NON_US_COUNTRIES),
    }


def _make_review_destination_pattern(record: dict[str, Any], rng: random.Random) -> None:
    record["destination_account"] = _account(rng, 9, review=True)


def _make_invalid_missing_field(record: dict[str, Any], rng: random.Random) -> None:
    record.pop(rng.choice(_DROPPABLE_FIELDS), None)


def _make_invalid_amount(record: dict[str, Any], rng: random.Random) -> None:
    record["amount"] = rng.choice(_BAD_AMOUNTS)


def _make_invalid_non_positive_amount(record: dict[str, Any], rng: random.Random) -> None:
    record["amount"] = rng.choice(_NON_POSITIVE_AMOUNTS)


def _make_invalid_unsupported_currency(record: dict[str, Any], rng: random.Random) -> None:
    record["currency"] = rng.choice(_UNSUPPORTED_CURRENCIES)


def _make_invalid_timestamp(record: dict[str, Any], rng: random.Random) -> None:
    record["timestamp"] = rng.choice(_BAD_TIMESTAMPS)


def build_transaction(category: str, index: int, rng: random.Random, base_date: date) -> dict[str, Any]:
    """Return one transaction in canonical input format for ``category``."""

    if category not in EXPECTATIONS:
        raise ValueError(f"Unknown category: {category}")
    record = _base_record(index, rng, base_date)
    mutators: dict[str, Callable[[], None]] = {
        "valid_settled": lambda: _make_valid_settled(record, rng),
        "review_high_value": lambda: _make_review_high_value(record, rng),
        "review_unusual_time": lambda: _make_review_unusual_time(record, rng, base_date),
        "review_channel_pattern": lambda: _make_review_channel_pattern(record, rng),
        "review_destination_pattern": lambda: _make_review_destination_pattern(record, rng),
        "invalid_missing_field": lambda: _make_invalid_missing_field(record, rng),
        "invalid_amount": lambda: _make_invalid_amount(record, rng),
        "invalid_non_positive_amount": lambda: _make_invalid_non_positive_amount(record, rng),
        "invalid_unsupported_currency": lambda: _make_invalid_unsupported_currency(record, rng),
        "invalid_timestamp": lambda: _make_invalid_timestamp(record, rng),
    }
    mutators[category]()
    return record


def _category_plan(count: int, invalid_ratio: float, review_ratio: float) -> list[str]:
    """Decide a category for each of ``count`` records from the requested mix."""

    count = max(0, count)
    n_invalid = round(count * invalid_ratio)
    n_valid_total = count - n_invalid
    n_review = round(n_valid_total * review_ratio)
    n_settled = n_valid_total - n_review

    plan: list[str] = ["valid_settled"] * n_settled
    plan += [REVIEW_CATEGORIES[i % len(REVIEW_CATEGORIES)] for i in range(n_review)]
    plan += [INVALID_CATEGORIES[i % len(INVALID_CATEGORIES)] for i in range(n_invalid)]
    return plan


def _manifest_entry(record: dict[str, Any], category: str, index: int) -> dict[str, Any]:
    expectation = EXPECTATIONS[category]
    entry = {
        "transaction_id": record.get("transaction_id", f"TXN{index:03d}"),
        "category": category,
        "expected_validation": expectation.validator_status,
        "expected_validation_reason_codes": list(expectation.validator_reason_codes),
        "expected_pipeline_status": expectation.pipeline_status,
        "expected_pipeline_reason_codes": list(expectation.pipeline_reason_codes),
    }
    if expectation.note:
        entry["note"] = expectation.note
    return entry


def generate(
    *,
    count: int = 8,
    seed: int | None = None,
    invalid_ratio: float = 0.3,
    review_ratio: float = 0.25,
    balanced: bool = False,
    start_index: int = 1,
    base_date: date | None = None,
) -> tuple[list[dict[str, Any]], list[dict[str, Any]]]:
    """Generate transactions plus a redacted expectation manifest.

    With ``balanced=True`` every category appears at least once (cycling to
    reach ``count``), the recommended mode for exercising the validator and the
    fraud detector across all branches.
    """

    if not 0.0 <= invalid_ratio <= 1.0:
        raise ValueError("invalid_ratio must be between 0 and 1")
    if not 0.0 <= review_ratio <= 1.0:
        raise ValueError("review_ratio must be between 0 and 1")

    rng = random.Random(seed)
    base = base_date or date(2026, 3, 16)

    if balanced:
        count = max(count, len(ALL_CATEGORIES))
        categories = [ALL_CATEGORIES[i % len(ALL_CATEGORIES)] for i in range(count)]
    else:
        categories = _category_plan(count, invalid_ratio, review_ratio)
        rng.shuffle(categories)

    transactions: list[dict[str, Any]] = []
    manifest: list[dict[str, Any]] = []
    for offset, category in enumerate(categories):
        index = start_index + offset
        record = build_transaction(category, index, rng, base)
        transactions.append(record)
        manifest.append(_manifest_entry(record, category, index))
    return transactions, manifest


def _counts(values: Any) -> dict[str, int]:
    result: dict[str, int] = {}
    for value in values:
        result[value] = result.get(value, 0) + 1
    return dict(sorted(result.items()))


def summarize(manifest: list[dict[str, Any]]) -> dict[str, Any]:
    valid = sum(1 for m in manifest if m["expected_validation"] == STATUS_VALIDATED)
    return {
        "total": len(manifest),
        "valid": valid,
        "invalid": len(manifest) - valid,
        "expected_pipeline_counts": _counts(m["expected_pipeline_status"] for m in manifest),
        "category_counts": _counts(m["category"] for m in manifest),
    }


def _write_text(path: Path, text: str) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", encoding="utf-8", newline="\n") as handle:
        handle.write(text)


def parse_args(argv: list[str] | None = None) -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Generate random Homework 6 transactions (valid, review, and invalid).",
        formatter_class=argparse.RawDescriptionHelpFormatter,
    )
    parser.add_argument("--count", type=int, default=8, help="Number of transactions to generate (default 8).")
    parser.add_argument("--seed", type=int, default=None, help="Seed for reproducible output.")
    parser.add_argument(
        "--invalid-ratio",
        type=float,
        default=0.3,
        help="Fraction of records that should fail validation (default 0.3).",
    )
    parser.add_argument(
        "--review-ratio",
        type=float,
        default=0.25,
        help="Fraction of the valid records that should trip a fraud review flag (default 0.25).",
    )
    parser.add_argument(
        "--balanced",
        action="store_true",
        help="Emit at least one record of every category (best for exercising all branches).",
    )
    parser.add_argument("--start-index", type=int, default=1, help="First TXN number (default 1).")
    parser.add_argument(
        "--output",
        type=Path,
        default=None,
        help="Write the transaction JSON array to this path. Defaults to stdout.",
    )
    parser.add_argument(
        "--manifest",
        type=Path,
        default=None,
        help="Optional path for the redacted expectation manifest JSON.",
    )
    return parser.parse_args(argv)


def main(argv: list[str] | None = None) -> int:
    args = parse_args(argv)
    transactions, manifest = generate(
        count=args.count,
        seed=args.seed,
        invalid_ratio=args.invalid_ratio,
        review_ratio=args.review_ratio,
        balanced=args.balanced,
        start_index=args.start_index,
    )
    payload = json.dumps(transactions, indent=2) + "\n"
    if args.output:
        _write_text(args.output, payload)
    else:
        sys.stdout.write(payload)

    if args.manifest:
        _write_text(args.manifest, json.dumps(manifest, indent=2) + "\n")

    summary = summarize(manifest)
    destination = str(args.output) if args.output else "stdout"
    sys.stderr.write(
        "Generated {total} transactions -> {dest} "
        "(valid={valid}, invalid={invalid}); expected pipeline {pipeline}\n".format(
            total=summary["total"],
            dest=destination,
            valid=summary["valid"],
            invalid=summary["invalid"],
            pipeline=summary["expected_pipeline_counts"],
        )
    )
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

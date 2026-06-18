# Research Notes

## Query 1: Python Decimal And Strict JSON Utilities

- Access date: 2026-06-18
- Search text: `Python standard library decimal Decimal monetary arithmetic json pathlib safe file writing documentation`
- Returned Context7 library ID: `/python/cpython`
- Applied insight: Construct money values from strings with `Decimal(...)`, reject non-finite values, avoid `Decimal.from_float(...)` for transaction amounts, write JSON through `pathlib.Path`, and use `json.dump(..., allow_nan=False)` to reject non-compliant NaN/Infinity values.
- Files or tasks influenced: `agents/common.py`, `integrator.py`, `tests/test_common.py`.

## Query 2: Pytest Temporary Filesystem Isolation

- Access date: 2026-06-18
- Search text: `pytest tmp_path fixture temporary directory filesystem tests documentation`
- Returned Context7 library ID: `/pytest-dev/pytest`
- Applied insight: Use pytest's `tmp_path` fixture for per-test isolated directories and generated JSON checks, keeping test runs away from the real `shared/` runtime folders except explicit CLI smoke execution.
- Files or tasks influenced: `tests/test_pipeline_end_to_end.py`, `tests/test_integrator_cli.py`, directory-preparation tests.

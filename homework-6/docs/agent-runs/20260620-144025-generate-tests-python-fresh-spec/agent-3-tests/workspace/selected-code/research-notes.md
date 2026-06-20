# Research Notes

## Query 1: Python Decimal And Strict JSON Utilities

- Access date: 2026-06-19
- Search text: `Python standard library decimal Decimal monetary arithmetic json allow_nan pathlib safe file writing for transaction processing`
- Returned Context7 library ID: `/python/cpython`
- Applied insight: Construct monetary values from strings with `Decimal(...)`, avoid float-based construction, serialize `Decimal` values back to JSON strings, and use strict `json.dump(..., allow_nan=False, sort_keys=True, indent=2)` writes through `pathlib.Path`.
- Files or tasks influenced: `agents/common.py`, `integrator.py`, `tests/test_common.py`.

## Query 2: Pytest Temporary Filesystem Isolation

- Access date: 2026-06-19
- Search text: `pytest tmp_path fixture temporary directory filesystem isolation tests pathlib`
- Returned Context7 library ID: `/pytest-dev/pytest`
- Applied insight: Use pytest's `tmp_path` fixture as a `pathlib.Path` object for isolated protocol directories, repeated-run archive checks, generated JSON files, and provenance tests.
- Files or tasks influenced: `tests/test_integrator_pipeline.py`, `pytest.ini`.


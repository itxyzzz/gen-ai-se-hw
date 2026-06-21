# Hephaestus Context7 Research Notes

- Run ID: `20260621-222543-generate-code-python-hera-python-full-set`
- Source Athena (Spec Writer) run: `20260621-220037-write-spec-python-hera-python-full-set`
- Source specification SHA-256: `6F2E8CD844884DF06172EEB1CF92A2956BC45425FE514B7A820ABAEA249D2222`
- Access date: `2026-06-21`
- Context7 status: reachable and used during code generation.

## Query 1: Python Decimal, JSON, And Filesystem Patterns

- Search text: `Python standard library Decimal json pathlib documentation for precise Decimal money arithmetic, strict JSON serialization, and safe filesystem paths`
- Returned Context7 library ID: `/python/cpython`
- Documentation query: `For a Python file-based processing pipeline, what standard library patterns should be used for decimal.Decimal monetary arithmetic, Decimal quantization or serialization, strict json.dump allow_nan=False, and pathlib/shutil filesystem operations? Include guidance relevant to avoiding float for money and writing safe JSON files.`
- Applied insight: The generated code parses money from strings into `Decimal`, rejects float inputs for money, serializes `Decimal` as strings before JSON output, and uses `json.dump(..., allow_nan=False)` for strict JSON. Context7 also returned an atomic temp-file replacement pattern; this was evaluated, but the Windows sandbox denied `os.replace`, so the candidate uses direct strict writes and records that validation observation.
- Influenced files: `agents/common.py`, `integrator.py`, `agents/transaction_validator.py`, `agents/reporting_agent.py`, `tests/test_common.py`.

## Query 2: Pytest Isolated Filesystem Testing

- Search text: `pytest tmp_path monkeypatch isolated filesystem tests for Python pipeline and repeated-run archive validation`
- Returned Context7 library ID: `/pytest-dev/pytest`
- Documentation query: `What pytest fixtures and assertions are recommended for testing Python code that writes files, especially using tmp_path and monkeypatch to isolate filesystem state and validate repeated runs without mutating the repository root?`
- Applied insight: The generated tests use `tmp_path` for isolated pipeline runs and file assertions, avoid root `shared/` mutation, and validate repeated-run archive behavior from a temporary base directory. The verification command needed unsandboxed execution because sandboxed pytest could not access its temp directories.
- Influenced files: `tests/conftest.py`, `tests/test_integrator_pipeline.py`, `tests/test_reporting_agent.py`, `pytest.ini`.

## Implementation Notes From Research

- The product code uses only Python standard-library runtime dependencies.
- The generated pipeline is an educational simulation and makes no banking, AML, sanctions, legal, regulatory, payment-network, or production-compliance claims.
- Runtime evidence under `outputs/shared/` and `outputs/archive/shared-001/` was privacy-scanned after validation.

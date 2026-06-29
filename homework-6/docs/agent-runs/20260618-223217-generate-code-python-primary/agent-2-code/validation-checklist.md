# Validation Checklist

Run ID: `20260618-223217-generate-code-python-primary`

## Spec Traceability

- Source Athena run ID: `20260618-003908-write-spec-python-replacement`
- Source specification: `homework-6/specification.md`
- Source specification SHA-256: `B08B8D365070DAD1F6A9BFE36F2D21951802CF0A284615706F17086E961DDD06`
- Selected output inventory: `agent-2-code/outputs/inventory.md`

| Selected spec objective/task | Generated evidence |
|---|---|
| M1, Tasks 1 and 4: Python package structure and JSON protocol | `integrator.py`, `agents/__init__.py`, `agents/common.py`, `prepare_shared_directories`, `build_message_envelope`, `shared/input`, `shared/processing`, `shared/output`, `shared/results` |
| M2, Tasks 2 and 5: Decimal money, validation, supported currency rejection | `agents/common.py`, `agents/transaction_validator.py`, `tests/test_common.py`, `tests/test_transaction_validator.py` |
| M3, Task 6: deterministic educational review signals | `agents/fraud_detector.py`, `tests/test_fraud_detector.py` |
| M4, Tasks 7-10: orchestration, settlement simulation, results, summary, CLI | `agents/settlement_processor.py`, `integrator.py`, `tests/test_pipeline_end_to_end.py` |
| M5, Tasks 11-12: audit-safe output, temp-dir tests, future-readable result shapes | `research-notes.md`, `shared/results/*.json`, `tests/` |

Task 13 from the selected spec was intentionally not implemented because the Hephaestus Task 2 workflow rejects Task 4 custom MCP server/config work during a normal code-generation run.

## Commands And Results

| Check | Expected signal | Actual result |
|---|---|---|
| `python -m pytest --cov=. --cov-fail-under=75` | Focused tests pass and coverage meets temporary Task 2 target | Passed after repair: 25 tests, 92.14% total coverage |
| `python -m pytest tests/test_pipeline_end_to_end.py -v` | Integration and repeated-run archive tests pass | Passed after repair: 8 tests |
| `python integrator.py` | Pipeline completes and writes results | Passed with normal filesystem permissions: `total=8 settled=3 rejected=2 review_required=3 error=0` |
| Second `python integrator.py` or equivalent focused test | Previous `shared/` is moved to next zero-padded archive folder before fresh output is created | Passed after repair: prior output archives to the next `archive/shared-001` style folder |
| Inspect `agent-2-code/outputs/` | Selectable code, tests, research notes, and inventory present; runtime folders absent | Passed after repair |
| Count `shared/results/TXN*.json` | 8 per-transaction result files | Passed: 8 |
| Inspect `shared/results/summary.json` | Total 8, expected outcomes | Passed: `settled=3`, `rejected=2`, `review_required=3`, `error=0` |
| Privacy scan over `shared/` | No raw sample account IDs or descriptions | Passed: no matches |
| `git diff -- mcp.json .codex/config.toml` | No Task 4 config changes | Passed: no diff |

## Required Sample Outcomes

| Transaction | Expected | Actual |
|---|---|---|
| `TXN006` | rejected with `UNSUPPORTED_CURRENCY` | Passed |
| `TXN007` | rejected with `NON_POSITIVE_AMOUNT` | Passed |
| `TXN002` | review signal with `HIGH_VALUE` | Passed |
| `TXN005` | review signal with `VERY_HIGH_VALUE` | Passed |
| `TXN004` | review signal with `ODD_HOUR_ACTIVITY` | Passed |
| Low-risk transaction | settled with simulated reference | Passed: `TXN001`, `TXN003`, and `TXN008` settled |

## Known Limitations

- Validation-only mode writes final-shaped JSON records for future command reuse, but it does not call the fraud detector or settlement processor and does not produce settlement references.
- Repeated runs archive the previous shared tree before creating fresh protocol directories. If the previous runtime files are locked by the operating system, the pipeline fails clearly instead of mixing old and new output.
- Task 3 commands/hooks, Task 4 custom MCP server/config, and Task 5 documentation/screenshots remain intentionally out of scope for this run.

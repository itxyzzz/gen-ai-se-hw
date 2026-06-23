# REST Agent Communication Test Cases Snapshot

Work ID: `2026-06-23-rest-agent-communication`
Short ID: `rest-agent-communication`
Status: Approved
Schema: `schema:snapshot.test-cases`

## Purpose

Capture the expected behavior that must be protected while refactoring deterministic component communication from direct file handoff to API-owned persistence.

## API contract tests

| Test case | Setup | Action | Expected result |
|---|---|---|---|
| API initializes a run | Use `tmp_path` and load records from `sample-transactions.json`. | Create a run through the runtime API. | API returns a run ID and eight expected transaction IDs; `shared/input/` exists; input messages are sanitized. |
| API records processing stage | Initialize a run and fetch `TXN001`. | Post `TXN001` to the processing stage. | `shared/processing/TXN001.json` exists and is written by the API. |
| API records output stage | Initialize a run and process `TXN001` through validator, fraud detector, and settlement processor. | Post the settled message to the output stage. | `shared/output/TXN001.json` exists and contains the post-settlement safe message. |
| API records result payload | Initialize a run and build a final message. | Post final result through the API. | `shared/results/TXN001.json` exists, `privacy_check` is `passed`, and raw account IDs are absent. |
| API finalizes run | Record all eight transaction results through the API. | Finalize the run through the API. | Summary has `total_records=8`, `settled=2`, `rejected=2`, `review_required=4`, `error=0`; `summary.json` and `pipeline-status.json` exist. |
| API records processing error | Initialize a run. | Post an error for a transaction ID. | Result file has `status=error`, a safe reason code, no raw account ID, and the run can still finalize. |

## Integrator regression tests

| Test case | Setup | Action | Expected result |
|---|---|---|---|
| Full pipeline still processes sample records | Use `sample_input` fixture and `tmp_path`. | Call `run_pipeline(base_dir=tmp_path, input_path=sample_input)`. | Summary remains `total_records=8`, `settled=2`, `rejected=2`, `review_required=4`, `error=0`, and completeness passes. |
| Rerun archives existing shared tree | Run the pipeline twice in the same `tmp_path`. | Read both summaries. | `archive/shared-001` exists and runtime run IDs differ. |
| Validation-only remains file-free | Use `validate_transactions_only(sample_input)`. | Check returned validation rows. | Eight results are returned, two are rejected, and no `shared/` directory is created by validation-only behavior. |
| Integrator does not use file paths as component communication | Monkeypatch API stage methods or old direct file handoff helpers. | Run the pipeline. | The API stage methods are called for handoff; the pipeline fails if old path-based handoff is used. |

## Privacy tests

| Test case | Scope | Expected result |
|---|---|---|
| Runtime output privacy | `shared/processing`, `shared/output`, and `shared/results` after a run. | No raw account IDs such as `ACC-1001`; no raw descriptions such as `Monthly rent payment`. |
| Result payload privacy | Every `TXN*.json` under `shared/results`. | `privacy_check` is `passed`; only redacted or safe summary data appears. |
| Summary privacy | `summary.json` and `pipeline-status.json`. | No raw account IDs, no raw descriptions, and no PII-bearing metadata. |

## Compatibility tests

| Test case | Action | Expected result |
|---|---|---|
| MCP reader compatibility | Run pipeline, then execute existing MCP server tests. | MCP tools can still read `shared/results/` payloads without schema changes. |
| Coverage gate | Run `python scripts\check_coverage_gate.py --stack python --fail-under 80`. | Gate passes at or above 80%. |
| CLI compatibility | Run `python integrator.py`. | Command exits 0 and prints `Pipeline complete: total=8 settled=2 rejected=2 review_required=4 error=0`. |

## Approval

- Status: Approved
- Superseded by: not superseded

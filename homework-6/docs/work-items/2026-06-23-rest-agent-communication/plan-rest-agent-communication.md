# REST Agent Communication Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use `superpowers:subagent-driven-development` if the operator authorizes sub-agents, otherwise use `superpowers:executing-plans` to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Route canonical Python pipeline communication through a local REST API that owns all runtime file persistence.

**Architecture:** Keep deterministic components as pure message processors and move protocol/result persistence into a small runtime API. The integrator remains the user-facing orchestrator, but it communicates with the API using request/response calls instead of passing file paths between stages.

**Tech Stack:** Python 3.12, standard library JSON/path utilities, existing pytest suite, optional FastAPI/Starlette only if already available locally or trivial to use without adding package-management churn.

---

Work ID: `2026-06-23-rest-agent-communication`
Short ID: `rest-agent-communication`
Status: Approved
Harness release: `unknown`
Schema: `schema:plan.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:models.strategy-required`, `rule:models.context-strategy`, `rule:models.approved-strategy-authorized`, `rule:models.fresh-confirmation`, `rule:lifecycle.commit-message-format`, `rule:lifecycle.variance-policy`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, `rule:freeze.stop-before-implementation`

## Implementation summary

Create a canonical Python runtime API module that owns pipeline storage. The API can be implemented as an in-process REST-shaped service to avoid background process and port cleanup risk. It must expose clear request/response methods corresponding to REST operations, and tests should treat those methods as the communication contract. If a lightweight web framework is already available in the environment, the implementation may expose an actual ASGI app using the same service methods.

Refactor `integrator.py` so it initializes a run through the API, retrieves current messages from the API, invokes each deterministic component, posts stage results back to the API, and finalizes through the API. Refactor `reporting_agent.py` so result payload construction remains reusable, but direct result-file writes are either moved into the API or used only by the API layer. The validator, fraud detector, and settlement processor should stay file-agnostic.

Update tests first, then implementation. Tests should prove the old direct file-based communication is no longer the integrator's stage interface, while preserving all externally observable outcomes: summary counts, archive behavior, privacy, validation-only behavior, and result file compatibility for the MCP reader.

## Files and interfaces

Expected implementation files:

| File | Action | Responsibility |
|---|---|---|
| `pipeline_api.py` or `pipeline_api/__init__.py` | Create | REST-shaped runtime API service, request/response handlers, and persistence ownership. |
| `integrator.py` | Modify | Replace direct protocol file communication with API calls while preserving CLI and public functions. |
| `agents/reporting_agent.py` | Modify | Split pure payload builders from file writes so the API can own persistence. |
| `agents/common.py` | Modify only if needed | Keep shared serialization helpers for API internals; avoid component-level file communication. |
| `agents/transaction_validator.py` | Prefer no change | Remain pure validation/message processor. |
| `agents/fraud_detector.py` | Prefer no change | Remain pure risk-scoring/message processor. |
| `agents/settlement_processor.py` | Prefer no change | Remain pure settlement/message processor. |
| `tests/test_integrator_pipeline.py` | Modify | Assert REST API ownership, unchanged pipeline outcomes, and archive behavior. |
| `tests/test_reporting_agent.py` | Modify if needed | Assert pure payload building and API-owned persistence expectations. |
| `tests/test_pipeline_api.py` | Create | Focused API contract tests for run initialization, stage persistence, finalization, and error recording. |

Stable public behavior:

- `run_pipeline(base_dir=Path("."), input_path=Path("sample-transactions.json"), shared_dir_name="shared") -> dict[str, Any]`
- `validate_transactions_only(input_path: Path, base_dir: Path | None = None) -> list[dict[str, Any]]`
- `python integrator.py --input ... --shared-dir ...`

REST-shaped operations:

- `POST /runs`
- `GET /runs/{run_id}/messages/{transaction_id}`
- `POST /runs/{run_id}/messages/{transaction_id}/stages/{stage}`
- `POST /runs/{run_id}/messages/{transaction_id}/error`
- `POST /runs/{run_id}/finalize`

## Model and Sub-agent Strategy

Current orchestration: Codex in the desktop app; exact model profile and reasoning effort are not exposed in the workspace.
Fit assessment: Moderate complexity with architecture, data-flow, and privacy risk. The change is bounded to the canonical Python application, but it affects public runtime behavior and persistence ownership.
Recommended change: Use the active `enterprise-default` policy. Strong reasoning is appropriate for implementation and final review because the work changes architecture and file ownership.

Sub-agents: None for the planning draft. For implementation, one read-only final-review sub-agent is recommended if the operator authorizes sub-agent use after the freeze gate.

| Purpose | Context strategy | Input context | Output artifact | Model policy | Model class/profile | Reasoning effort | Reason | Parallel? | Blast radius if wrong |
|---|---|---|---|---|---|---|---|---|---|
| Final implementation review | curated prompt | Frozen spec and plan, final diff, validation results | Review findings in chat or handoff note | `enterprise-default` | latest strongest available | high | REST ownership and privacy regressions can be subtle | No | Medium: missed regression could affect submission behavior |

## Tasks

### Task 1: Add API contract tests

**Files:**
- Create: `tests/test_pipeline_api.py`
- Read: `docs/work-items/2026-06-23-rest-agent-communication/snapshots/api-contract.snapshot.md`
- Read: `agents/common.py`
- Read: `agents/reporting_agent.py`

- [ ] Write tests for run initialization through the API:
  - Use `tmp_path` and `sample_input`.
  - Call the API initialization operation with `base_dir=tmp_path`, `shared_dir_name="shared"`, and the sample records loaded from `sample_input`.
  - Assert `run_id` is present.
  - Assert the API returns eight expected transaction IDs.
  - Assert `tmp_path / "shared/input/TXN001.json"` exists after initialization.
  - Assert no raw `ACC-1001` or `Monthly rent payment` appears in the seeded API-managed message.

- [ ] Write tests for stage persistence through the API:
  - Initialize a run through the API.
  - Fetch `TXN001` through the API message-read operation.
  - Submit the fetched message to the `processing` stage through the API.
  - Assert `tmp_path / "shared/processing/TXN001.json"` exists.
  - Submit a post-settlement message to the `output` stage through the API.
  - Assert `tmp_path / "shared/output/TXN001.json"` exists.

- [ ] Write tests for API-owned result persistence:
  - Build or process a settled `TXN001` message.
  - Submit it to the `result` or reporting stage through the API.
  - Assert `tmp_path / "shared/results/TXN001.json"` exists.
  - Assert the result payload has `privacy_check == "passed"` and includes no raw account IDs.

- [ ] Write tests for finalization:
  - Initialize and record results for all eight sample transactions, or run the integrator against the API.
  - Finalize through the API.
  - Assert summary counts remain `total_records=8`, `settled=2`, `rejected=2`, `review_required=4`, `error=0`.
  - Assert `summary.json` and `pipeline-status.json` are written by the API.

- [ ] Write tests for API-owned error recording:
  - Initialize a run.
  - Call the error operation for a transaction ID.
  - Assert `shared/results/<transaction_id>.json` contains `status == "error"` and `PROCESSING_ERROR` or the supplied reason code.

- [ ] Run the focused API tests and confirm they fail before implementation:

```powershell
python -m pytest tests\test_pipeline_api.py -q -p no:cacheprovider
```

Expected before implementation: failures due to missing `pipeline_api` or missing API operations.

### Task 2: Implement the runtime API service

**Files:**
- Create: `pipeline_api.py` or `pipeline_api/__init__.py`
- Modify: `agents/reporting_agent.py` if direct persistence must be split from payload construction

- [ ] Create an API service object that stores per-run state:
  - `run_id`
  - `paths`
  - `expected_transaction_ids`
  - in-memory latest message per transaction.

- [ ] Move or wrap these persistence responsibilities into the API:
  - Archive existing `shared/`.
  - Create protocol directories.
  - Write `run-provenance.json`.
  - Write input, processing, output, result, summary, and pipeline-status files.

- [ ] Implement REST-shaped handler methods matching the snapshot:
  - `create_run(...)`
  - `get_message(run_id, transaction_id)`
  - `record_stage(run_id, transaction_id, stage, message)`
  - `record_error(run_id, transaction_id, reason_code)`
  - `finalize_run(run_id)`

- [ ] Use existing safe helpers inside the API:
  - `safe_json_dump`
  - `safe_json_load` only if needed by the API
  - `get_protocol_paths`
  - `create_message`
  - `utc_now`
  - `reporting_agent.build_transaction_status_payload`
  - `reporting_agent.build_pipeline_status`

- [ ] Ensure API validation rejects unknown run IDs, transaction IDs, and stages with safe `ValueError` messages.

- [ ] Run the focused API tests:

```powershell
python -m pytest tests\test_pipeline_api.py -q -p no:cacheprovider
```

Expected after implementation: all new API tests pass.

### Task 3: Refactor integrator communication to the API

**Files:**
- Modify: `integrator.py`
- Modify: `tests/test_integrator_pipeline.py`

- [ ] Replace `prepare_shared_directories(...)`, `write_run_provenance(...)`, and `seed_input_messages(...)` calls inside `run_pipeline(...)` with the API initialization operation.

- [ ] Replace message-path iteration with transaction ID iteration:
  - Fetch the current message from the API.
  - Record a processing-stage snapshot through the API.
  - Call `transaction_validator.process_message(message)`.
  - Record validator-stage state in memory or through a named stage if useful.
  - Call `fraud_detector.process_message(message)`.
  - Call `settlement_processor.process_message(message)`.
  - Record the output-stage snapshot through the API.
  - Record the final result through the API.

- [ ] Update `safe_process_transaction(...)` or replace it with a transaction-ID based helper:
  - On component exceptions, call the API error operation.
  - Return a safe error message shape for compatibility.

- [ ] Preserve `validate_transactions_only(...)` as file-input validation only. It may continue loading `sample-transactions.json` directly because it is not deterministic-agent communication and it intentionally does not create `shared/`.

- [ ] Add or update a test proving integrator stage communication no longer depends on file paths:
  - Monkeypatch the API service stage methods to count calls, or monkeypatch old file-path based helpers so the test fails if `run_pipeline(...)` uses them for stage handoff.
  - Assert the pipeline still completes with the expected counts.

- [ ] Run the focused integrator tests:

```powershell
python -m pytest tests\test_integrator_pipeline.py -q -p no:cacheprovider
```

Expected after refactor: all integrator tests pass.

### Task 4: Preserve reporting, privacy, and MCP compatibility

**Files:**
- Modify: `agents/reporting_agent.py`
- Modify: `tests/test_reporting_agent.py`
- Modify: `tests/test_mcp_server.py` only if result paths remain compatible but test setup needs API initialization

- [ ] Keep pure reporting payload builders available:
  - `build_transaction_status_payload(message)`
  - `summarize_results(results_dir, expected_transaction_ids, runtime_run_id)` may remain if called only by API, or split into pure summary building plus API file write.

- [ ] Ensure normal component-to-component communication does not call `reporting_agent.process_message(message, results_dir)` directly from deterministic components or integrator stage handoff. The API should own that call or equivalent persistence.

- [ ] Run reporting and MCP focused tests:

```powershell
python -m pytest tests\test_reporting_agent.py tests\test_mcp_server.py -q -p no:cacheprovider
```

Expected after refactor: reporting payload tests and MCP result-reader tests pass.

### Task 5: Run full validation and review scope

**Files:**
- Modify: `CHANGELOG.md` before the implementation commit, if commits are requested.
- Modify: `docs/work-items/2026-06-23-rest-agent-communication/implementation-notes/variance-log.md` only if implementation deviates from the frozen plan.

- [ ] Run the full canonical Python test suite:

```powershell
python -m pytest -p no:cacheprovider
```

Expected: all tests pass.

- [ ] Run the Python coverage gate:

```powershell
python scripts\check_coverage_gate.py --stack python --fail-under 80
```

Expected: coverage gate passes at or above 80%.

- [ ] Run the pipeline:

```powershell
python integrator.py
```

Expected: `Pipeline complete: total=8 settled=2 rejected=2 review_required=4 error=0`.

- [ ] Inspect runtime output for privacy:

```powershell
rg "ACC-1001|Monthly rent payment" shared\processing shared\output shared\results
```

Expected: no matches in processing, output, or results. If `shared/input` is included, it must still contain only sanitized account presence markers and not raw account IDs or descriptions.

- [ ] Review the diff:
  - Confirm no preserved `docs/agent-runs/` artifacts changed.
  - Confirm no Java alternate files changed.
  - Confirm no reviewer docs changed unless the operator explicitly expanded implementation scope.
  - Confirm the API owns normal runtime writes to `shared/`.

## Planned commits

| Stage | Planned subject | Changelog title or snippet | Notes |
|---|---|---|---|
| Planning approval | `rest-agent-communication spec: plan REST-owned pipeline communication` | `2026-06-23-rest-agent-communication: plan REST-owned pipeline communication` | Approval commit for this spec, plan, and planning snapshots. |
| Implementation | `rest-agent-communication refactor: route pipeline communication through REST API` | `2026-06-23-rest-agent-communication: route pipeline communication through REST API` | Implementation commit after explicit post-freeze operator authorization. |

## Validation commands

| Command | Expected result |
|---|---|
| `python -m pytest tests\test_pipeline_api.py -q -p no:cacheprovider` | New API contract tests pass after implementation. |
| `python -m pytest tests\test_integrator_pipeline.py -q -p no:cacheprovider` | Integrator still processes all sample records and archives reruns. |
| `python -m pytest tests\test_reporting_agent.py tests\test_mcp_server.py -q -p no:cacheprovider` | Reporting payloads remain privacy-safe and MCP result-reader compatibility is preserved. |
| `python -m pytest -p no:cacheprovider` | Full canonical Python test suite passes. |
| `python scripts\check_coverage_gate.py --stack python --fail-under 80` | Coverage gate passes at or above 80%. |
| `python integrator.py` | Prints `Pipeline complete: total=8 settled=2 rejected=2 review_required=4 error=0`. |

## Plan variance handling

Use `rule:lifecycle.variance-policy`. Before freeze, edit this draft directly for operator feedback. After freeze, record nontrivial implementation variance in `implementation-notes/variance-log.md`; use a plan amendment for high-impact architecture, API, data, security, privacy, compliance, scope, acceptance-criteria, or feasibility changes.

Known acceptable local technical variance:

- The API may be implemented as an in-process REST-shaped service rather than a background loopback server if that avoids flaky process and port management.
- Intermediate state may be memory-backed as long as required file evidence is written by the API.
- Endpoint method names in Python may differ from literal URL strings if tests preserve the REST operation contract and ownership boundary.

Variance requiring operator approval:

- Adding a mandatory external web framework dependency.
- Requiring reviewers to start a separate server before `python integrator.py`.
- Changing result payload schemas consumed by MCP tools or reviewer docs.
- Updating files outside the canonical Python application and tests, except harness variance/changelog artifacts required by the approved workflow.

## Planning artifact freeze gate

Use `module:freeze-gate`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, and `rule:freeze.stop-before-implementation`.

Draft review status: Completed. The operator approved the staged planning package on 2026-06-23.

Approval commit status: Approved for freeze commit with `rest-agent-communication spec: plan REST-owned pipeline communication`.

Post-freeze implementation authorization status: Not authorized yet. Implementation must wait for a fresh explicit operator instruction after the planning freeze gate.

## Completion criteria

- Acceptance criteria in `spec-rest-agent-communication.md` are met.
- Required validation commands have been run and recorded.
- Required implementation files and tests are updated within the approved scope.
- `CHANGELOG.md` has a newest-first entry for the planning approval commit and implementation commit if commits are requested.
- Commit subjects match the approved planned subjects or recorded variance, and changelog title snippets are synchronized.
- Variance log is present and current if implementation deviates from the frozen plan.
- De-facto sub-agent use is reported when applicable, including count, roles/scopes, concurrency or waves, context strategy, observed inheritance behavior, and de-facto model/model class/profile when known.

## Approval

- Status: Approved
- Superseded by: not superseded

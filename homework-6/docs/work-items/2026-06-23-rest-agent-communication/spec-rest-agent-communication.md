# REST Agent Communication Spec

Work ID: `2026-06-23-rest-agent-communication`
Short ID: `rest-agent-communication`
Status: Approved
Harness release: `unknown`
Schema: `schema:spec.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `rule:lifecycle.documentation-matrix`, `rule:lifecycle.commit-message-format`, `rule:quality.spec-handoff`

## Goal

Refactor the canonical Python transaction pipeline so deterministic runtime components communicate through a local REST API instead of reading or writing protocol files directly, while the REST API owns all runtime file persistence.

## Scope

- Canonical Python application files at the homework root:
  - `integrator.py`
  - `agents/common.py`
  - `agents/transaction_validator.py`
  - `agents/fraud_detector.py`
  - `agents/settlement_processor.py`
  - `agents/reporting_agent.py`
  - New canonical Python API module or package if needed, for example `api_server.py` or `pipeline_api/`.
- Canonical Python tests needed to prove the refactor, including focused updates under `tests/`.
- Runtime behavior for:
  - Loading `sample-transactions.json`.
  - Creating sanitized initial transaction messages.
  - Passing messages through Transaction Validator, Fraud Detector, Settlement Processor, and Reporting Agent.
  - Writing `shared/input`, `shared/processing`, `shared/output`, `shared/results`, `summary.json`, `pipeline-status.json`, and `run-provenance.json`.
  - Archiving existing `shared/` trees before a new run.
- A local in-process or loopback REST API boundary that owns file read/write. The fastest reliable implementation is acceptable as long as deterministic runtime components no longer call `safe_json_load`, `safe_json_dump`, `Path.open`, or direct result-folder writes for protocol communication.
- Existing CLI entry points must remain usable:
  - `python integrator.py`
  - `python integrator.py --input sample-transactions.json --shared-dir shared`
  - `validate_transactions_only(...)` for validation-only behavior.

## Non-scope

- Do not regenerate Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), Clio (Documentation Generator), or Hera (Orchestrator) run packages.
- Do not update preserved historical artifacts under `docs/agent-runs/`.
- Do not change Java alternate evidence.
- Do not change MCP server behavior unless canonical Python tests reveal an unavoidable compatibility break caused by the REST refactor. The custom MCP server can continue reading `shared/results/` as an external status reader.
- Do not change reviewer-facing documentation, screenshots, PR draft, command wrappers, hooks, or `CHANGELOG.md` during draft planning. Changelog is updated only at the planning freeze gate and implementation commit points.
- Do not add external network dependencies or require a long-running service to be started manually before `python integrator.py`.

## Current state

The canonical Python pipeline is a deterministic in-process sequence. `integrator.py` prepares `shared/`, loads sample records, sanitizes initial messages, writes each message directly to `shared/input`, copies messages to `shared/processing`, invokes component functions in order, writes final per-transaction messages to `shared/output`, calls `reporting_agent.process_message(...)`, and writes summaries under `shared/results`.

The runtime components communicate by mutating Python dictionaries in memory, while `integrator.py` and `reporting_agent.py` own direct file writes. The assignment's original required JSON file protocol remains visible in `shared/`, but there is no REST boundary between deterministic components and persistence. Tests assert existing result counts, archive behavior, privacy, validation-only behavior, and MCP result-reading behavior.

## Proposed behavior

Introduce a small local REST API for pipeline runtime state and persistence. The integrator starts or instantiates the API for a run, then calls API endpoints to initialize the run, load or seed transaction messages, submit component outputs, and finalize summaries. Deterministic components remain pure message processors: they accept a message payload, return an updated message payload, and never read or write runtime protocol files.

The API owns all runtime file read/write:

- It archives and prepares `shared/`.
- It writes `run-provenance.json`.
- It writes sanitized input messages to `shared/input`.
- It writes processing snapshots to `shared/processing`.
- It writes post-settlement output snapshots to `shared/output`.
- It writes per-transaction results, `summary.json`, and `pipeline-status.json` to `shared/results`.
- It may hold intermediate messages in memory during a run for speed, but the canonical file evidence must still be produced so existing support surfaces keep working.

`python integrator.py` must remain a one-command local run. The preferred implementation is an in-process FastAPI/Starlette-style app exercised through a test client or ASGI transport, if the dependency already exists or is easy to vendor-free use. If FastAPI/Starlette is not locally available, implement the API as a standard-library HTTP server or a minimal in-process REST dispatcher with request/response shapes that match the API contract snapshot. The API boundary matters more than a specific framework.

## Interfaces and data

### Runtime API

The implementation must provide a REST-shaped contract for the integrator and deterministic components. Endpoint names may be adjusted during implementation if tests and the API snapshot are updated before freeze, but the final API must expose these operations:

| Operation | Method and path | Request owner | Persistence owner | Purpose |
|---|---|---|---|---|
| Initialize run | `POST /runs` | Integrator | API | Archive prior shared tree, create protocol folders, write run provenance, and return `run_id`, expected transaction IDs, and seeded input message handles. |
| Read next message | `GET /runs/{run_id}/messages/{transaction_id}` | Integrator | API | Return the current message payload for the transaction without exposing file paths as the communication mechanism. |
| Record stage result | `POST /runs/{run_id}/messages/{transaction_id}/stages/{stage}` | Integrator after each component | API | Store the latest message in memory and write any required protocol artifact for that stage. |
| Finalize run | `POST /runs/{run_id}/finalize` | Integrator | API | Build and persist `summary.json` and `pipeline-status.json`, then return the summary payload. |
| Record processing error | `POST /runs/{run_id}/messages/{transaction_id}/error` | Integrator error handler | API | Write privacy-safe error result and keep the run completable. |

### Message and result data

- Message payloads retain the existing `create_message(...)` shape:
  - `message_id`
  - `timestamp`
  - `source_agent`
  - `target_agent`
  - `message_type`
  - `data`
  - `component_history`
  - `audit_events`
- Result payloads retain the existing reporting shape:
  - `schema_version`
  - `transaction_id`
  - `status`
  - `reason_codes`
  - `amount`
  - `currency`
  - `safe_summary`
  - `component_history_count`
  - `audit_event_count`
  - `processed_at`
  - `privacy_check`
  - `settlement_reference` only for settled transactions.
- Summary and pipeline status payloads retain current keys used by tests and MCP support.
- No raw account identifiers or raw descriptions may be stored outside permitted input evidence. Input messages must remain sanitized as today.

### File persistence

The API may still store runtime evidence in files:

```text
shared/
  input/
  processing/
  output/
  results/
  run-provenance.json
archive/
```

The API must be the only canonical Python runtime code that writes these files during normal pipeline execution.

## Risks

- REST boundary drift could accidentally change result counts, status outcomes, reason-code counts, or privacy guarantees.
- A real background HTTP server could introduce flaky port selection, process cleanup, or Windows sandbox issues. Prefer an in-process API client unless a loopback server is clearly simpler and verified.
- Existing unit tests may assert direct file-writing helpers. Update tests to assert behavior through the API boundary instead of preserving direct file communication.
- MCP status tools read result files after the run. They should remain compatible because the API still writes the same result artifacts.
- Direct file helper functions in `agents/common.py` may still exist for API internals, but deterministic components must not use them for communication.

## Acceptance criteria

- `python integrator.py` completes with `total=8 settled=2 rejected=2 review_required=4 error=0`.
- Default pytest completes successfully for canonical Python tests.
- The Python coverage gate passes at or above 80%.
- Deterministic runtime components do not directly read from or write to `shared/input`, `shared/processing`, `shared/output`, or `shared/results` during normal pipeline execution.
- The API owns all runtime file persistence for protocol and result artifacts.
- Existing result payloads, summary payloads, pipeline-status payloads, privacy checks, archive behavior, and validation-only behavior remain compatible.
- Intermediate state may be in memory, but required file evidence still appears under `shared/`.
- No raw account IDs or raw descriptions are introduced into output, processing, summary, status, or audit artifacts.

## Planned commits

| Stage | Planned subject | Changelog title or snippet | Notes |
|---|---|---|---|
| Planning approval | `rest-agent-communication spec: plan REST-owned pipeline communication` | `2026-06-23-rest-agent-communication: plan REST-owned pipeline communication` | Approval commit for this spec, plan, and planning snapshots. |
| Implementation | `rest-agent-communication refactor: route pipeline communication through REST API` | `2026-06-23-rest-agent-communication: route pipeline communication through REST API` | Refactor canonical Python application and tests after explicit post-freeze operator authorization. |

## Documentation artifact matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Freeze gate and before implementation commit | `CHANGELOG.md` | Required by harness before each commit; not edited during draft planning. |
| Test cases | Snapshot | Yes | Before implementation | `docs/work-items/2026-06-23-rest-agent-communication/snapshots/test-cases.snapshot.md` | Captures REST refactor behavior and regression expectations. |
| Testing guide delta | Living delta | No | Not applicable | Not created | Reviewer-facing test commands remain unchanged unless implementation changes them. |
| Operator manual delta | Living delta | No | Not applicable | Not created | `python integrator.py` remains the operator entry point. |
| API reference delta | Living delta | Deferred | After implementation if reviewer docs are allowed | `docs/work-items/2026-06-23-rest-agent-communication/deltas/api-reference.delta.md` | User asked to modify only canonical Python application after planning; API docs can be deferred unless operator expands scope. |
| Architecture snapshot | Snapshot | Yes | Before implementation | `docs/work-items/2026-06-23-rest-agent-communication/snapshots/api-contract.snapshot.md` | Captures the REST contract and ownership boundary. |
| Architecture summary delta | Living delta | Deferred | After implementation if reviewer docs are allowed | `docs/work-items/2026-06-23-rest-agent-communication/deltas/architecture-summary.delta.md` | Needed only if operator permits documentation updates beyond canonical Python application. |

## Approval

- Status: Approved
- Superseded by: not superseded

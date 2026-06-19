# Python Transaction Processing Pipeline Specification

Status: Candidate generated output for run `20260619-170102-write-spec-python-fresh`

Selected stack: `python`

## High-Level Objective

Build a Python educational transaction-processing pipeline that deterministically processes the eight synthetic sample transactions through stack-native runtime components, preserves audit-safe JSON evidence in the required `shared/` protocol folders, and produces final results with statuses limited to `settled`, `rejected`, `review_required`, or `error`.

## Scope Boundary

This specification describes only the Generated Transaction System Layer. Runtime transaction pipeline agents are Python application modules with bounded responsibilities, message contracts, decision logic, audit identities, and pipeline positions. They are not assistant skills and are not named after outer repository roles.

The system is an educational simulation. It must not claim real banking, AML, sanctions, KYC, payment-network, fraud-model, legal, regulatory, or settlement compliance. It does not move money, contact payment networks, screen sanctions lists, verify identities, change account balances, or create real settlement instructions.

The specification may define result shapes that later read-only status tools can consume. It does not require outer repository delivery, command wrapper, configuration, or submission mechanics as product behavior.

## Mid-Level Objectives

| ID | Objective | Observable success |
|---|---|---|
| M1 | Implement deterministic Python orchestration and file protocol setup. | `integrator.py` loads `sample-transactions.json`, archives any existing `shared/` tree to the next zero-padded `archive/shared-001` style folder, creates fresh `shared/input`, `shared/processing`, `shared/output`, and `shared/results` directories, writes non-sensitive `shared/run-provenance.json`, and accounts for all eight input transaction IDs in final outputs. |
| M2 | Validate transaction structure, money, currency, and safe rejection behavior. | `agents/transaction_validator.py` exposes `process_message(message: dict) -> dict`, parses amount strings with `decimal.Decimal` only, accepts only `USD`, `EUR`, and `GBP`, rejects `TXN006` with `UNSUPPORTED_CURRENCY`, rejects `TXN007` with `NON_POSITIVE_AMOUNT`, and emits audit events without plaintext account IDs, descriptions, or raw metadata. |
| M3 | Apply deterministic educational risk scoring without real compliance claims. | `agents/fraud_detector.py` assigns repeatable `risk_score`, `risk_level`, and reason codes using documented heuristics for high amount, wire transfer type, early-hours timestamp, API or mobile channel, country, and near-threshold amount; elevated-risk valid transactions route toward `review_required` without asserting real fraud or compliance determinations. |
| M4 | Produce safe final settlement simulation results and summary files. | `agents/settlement_processor.py` converts low-risk valid transactions to `settled`, preserves validation failures as `rejected`, routes elevated-risk transactions to `review_required`, reserves `error` for per-transaction failures, and writes safe per-transaction JSON plus `shared/results/summary.json`. |
| M5 | Specify Python verification seams for repeatable tests and future read-only inspection. | The implementation uses `pytest` with isolated temporary directories, supports validator dry-run behavior for a future `/validate-transactions` workflow, verifies repeated-run archival and provenance creation, enforces the final status vocabulary, and targets 75% coverage for this specification stage. |

## Runtime Components

| Component | File | Responsibility | Interface |
|---|---|---|---|
| Integrator | `integrator.py` | Prepare runtime folders, archive prior runs, write provenance, load samples, create input messages, call components in order, write stage evidence, write final results, and summarize the run. | `main()`, `run_pipeline(...)`, `process_transaction(...)` |
| Common utilities | `agents/common.py` | Shared envelope helpers, Decimal parsing, JSON serialization, account redaction, audit event construction, sensitive-field checks, and safe result utilities. | `create_message(...)`, `parse_amount(...)`, `create_audit_event(...)` |
| Transaction Validator | `agents/transaction_validator.py` | Validate required fields, parse positive Decimal amounts, validate supported currencies, validate timestamp shape, and produce safe rejections. | `process_message(message: dict) -> dict`, `validate_transaction(...)` |
| Fraud Detector | `agents/fraud_detector.py` | Apply deterministic educational risk scoring and route validated transactions by risk level. | `process_message(message: dict) -> dict`, `score_fraud_risk(...)` |
| Settlement Processor | `agents/settlement_processor.py` | Convert validation and risk decisions into final simulated statuses and safe result records. | `process_message(message: dict) -> dict`, `build_final_result(...)` |
| Read-only result helpers | `mcp/server.py` after result shapes exist | Read existing result files for future status tools without running or mutating the pipeline. | `get_transaction_status(...)`, `list_pipeline_results(...)`, `load_summary(...)` |

## Implementation Notes

### Python Layout

Use this product layout:

```text
integrator.py
agents/
  __init__.py
  common.py
  transaction_validator.py
  fraud_detector.py
  settlement_processor.py
tests/
  conftest.py
  test_common.py
  test_validator.py
  test_fraud_detector.py
  test_integrator_pipeline.py
mcp/
  server.py
shared/
  input/
  processing/
  output/
  results/
archive/
```

`mcp/server.py` is a later read-only product status reader after result files exist. Do not add `pipeline-status` to MCP configuration files until that server exists and can read current result files.

### Message Envelope

Runtime components pass JSON-serializable envelopes:

```json
{
  "message_id": "uuid4-string",
  "timestamp": "2026-03-16T10:00:00Z",
  "source_agent": "integrator",
  "target_agent": "transaction_validator",
  "message_type": "transaction",
  "data": {
    "transaction_id": "TXN001",
    "amount": "1500.00",
    "currency": "USD",
    "transaction_type": "transfer",
    "status": "received"
  },
  "component_history": []
}
```

Intermediate runtime evidence may retain the original transaction inside internal input files, but logs, audit events, summaries, final results, docs, and examples must omit raw `source_account`, `destination_account`, `description`, and unfiltered `metadata`.

### File-Based Protocol

Runtime files use:

```text
shared/input
shared/processing
shared/output
shared/results
```

Use deterministic filenames:

- `shared/input/001-TXN001.json`
- `shared/processing/001-TXN001-transaction-validator.json`
- `shared/output/001-TXN001-fraud-detector.json`
- `shared/results/TXN001.json`
- `shared/results/summary.json`
- `shared/results/pipeline-status.json`

Before creating a fresh `shared/` tree, move any existing `shared/` folder to the next available zero-padded archive folder such as `archive/shared-001` or `archive/shared-002`. Do not delete prior runtime evidence during a normal run.

### Runtime Provenance

Each run writes `shared/run-provenance.json` before transaction processing begins. It contains only non-sensitive traceability data:

- `schema_version`
- `runtime_run_id`
- `generated_at`
- `source_spec.run_id`
- `source_spec.path`
- `source_spec.sha256`
- `pipeline_version.run_id`
- `pipeline_version.inventory_path`
- `pipeline_version.package_sha256`

The provenance file must not include raw transactions, account identifiers, descriptions, metadata, prompts, thread content, credentials, or secrets. Because prior `shared/` folders are archived whole, provenance remains attached to each archived runtime run.

### Money And Currency

- Parse transaction amounts from JSON strings into `decimal.Decimal`.
- Never use binary floating point for amounts, thresholds, summaries, comparisons, or tests.
- Reject missing, non-string, malformed, non-finite, zero, and negative amounts.
- Serialize validated amounts back to JSON as strings.
- Validate uppercase three-letter ISO 4217-style currency shape, then enforce the local homework allowlist `USD`, `EUR`, and `GBP`.
- Reject `XYZ` with reason code `UNSUPPORTED_CURRENCY`.

### Risk Scoring

Risk scoring is deterministic homework logic, not a fraud model or compliance conclusion.

Use these stable signals:

| Signal | Rule | Score | Reason code |
|---|---|---:|---|
| Very high value | `amount >= Decimal("50000.00")` | 40 | `VERY_HIGH_VALUE` |
| High value | `amount >= Decimal("10000.00")` | 25 | `HIGH_VALUE` |
| Near threshold | `amount >= Decimal("9000.00")` | 15 | `NEAR_THRESHOLD_AMOUNT` |
| Wire transfer | `transaction_type == "wire_transfer"` | 20 | `WIRE_TRANSFER` |
| Odd hour | UTC hour from `00` through `04` | 15 | `ODD_HOUR_ACTIVITY` |
| API channel | `metadata.channel == "api"` | 10 | `REMOTE_CHANNEL` |
| Mobile channel | `metadata.channel == "mobile"` | 5 | `MOBILE_CHANNEL` |
| Non-US country | `metadata.country != "US"` | 10 | `CROSS_COUNTRY_REVIEW_SIGNAL` |

Risk levels:

- `low`: score under 25
- `medium`: score 25 through 49
- `high`: score 50 or higher

Settlement routing:

- `low` risk may settle.
- `medium` or `high` risk routes to `review_required`.
- Validation rejections bypass risk scoring.

Expected sample outcomes:

| Transaction | Expected status | Required reason signal |
|---|---|---|
| `TXN001` | `settled` | `SETTLEMENT_SIMULATED` |
| `TXN002` | `review_required` | `HIGH_VALUE`, `WIRE_TRANSFER` |
| `TXN003` | `review_required` | `NEAR_THRESHOLD_AMOUNT` |
| `TXN004` | `review_required` | `ODD_HOUR_ACTIVITY`, `REMOTE_CHANNEL`, `CROSS_COUNTRY_REVIEW_SIGNAL` |
| `TXN005` | `review_required` | `VERY_HIGH_VALUE`, `HIGH_VALUE`, `WIRE_TRANSFER` |
| `TXN006` | `rejected` | `UNSUPPORTED_CURRENCY` |
| `TXN007` | `rejected` | `NON_POSITIVE_AMOUNT` |
| `TXN008` | `settled` | `SETTLEMENT_SIMULATED` |

### Audit, Logging, And Privacy

Audit events include:

- UTC timestamp.
- Runtime component name.
- Transaction ID.
- Safe outcome.
- Stable reason codes.
- Optional risk level or redacted reference.

Centralize redaction in `agents.common.redact_account_id(account_id: str | None) -> str | None`. Documentation examples should show account identifiers only in already-redacted form such as `ACC-****1001`.

Logs, audit events, final results, summaries, docs, and tests must not expose plaintext full account IDs, raw sample descriptions, credentials, tokens, authorization headers, secrets, or unfiltered metadata dumps.

### JSON Safety

Use the Python standard `json` module. Catch `json.JSONDecodeError` for malformed JSON. Convert `Decimal` to strings before writing JSON. Write strict result files with `allow_nan=False`, deterministic indentation, and sorted keys.

### Result Shapes For Future Status Readers

`shared/results/<transaction_id>.json` includes:

- `schema_version`
- `transaction_id`
- `status`
- `reason_codes`
- `risk_score`
- `risk_level`
- `amount`
- `currency`
- `component_history`
- `processed_at`
- `safe_summary`
- `audit_events` if sanitized

`shared/results/summary.json` includes:

- `schema_version`
- `runtime_run_id`
- `total_transactions`
- `settled`
- `rejected`
- `review_required`
- `error`
- `result_files`
- `generated_at`
- `simulation_notice`

Future read-only helpers can expose `get_transaction_status(transaction_id: str)`, `list_pipeline_results()`, and `pipeline://summary` by reading these files without rerunning the pipeline.

### Testing And Coverage

Use `pytest` and `tmp_path` to isolate filesystem state. The temporary specification-stage coverage target is 75% using a non-blocking coverage report command such as:

```powershell
python -m pytest
python -m pytest --cov=.
```

Later test-generation work owns raising enforced coverage above 80% and adding a blocking coverage hook. This product spec prepares test seams but does not make hook setup a pipeline product feature.

## Context

### Beginning Context

The homework root contains `sample-transactions.json` with eight synthetic records:

| Transaction | Beginning-state facts |
|---|---|
| `TXN001` | USD online transfer for `1500.00`. |
| `TXN002` | USD branch wire transfer for `25000.00`. |
| `TXN003` | USD online transfer for `9999.99`. |
| `TXN004` | EUR API transfer at `02:47:00Z`, country `DE`. |
| `TXN005` | USD branch wire transfer for `75000.00`. |
| `TXN006` | Unsupported currency `XYZ`. |
| `TXN007` | GBP refund with negative amount `-100.00`. |
| `TXN008` | USD mobile transfer for `3200.00`. |

No generated transaction pipeline code, runtime component modules, product tests, read-only MCP status reader, or runtime `shared/` results are assumed for a fresh product generation.

### Ending Context

After the low-level tasks are complete:

- `python integrator.py` processes all eight sample transactions.
- A prior `shared/` tree is archived to the next zero-padded `archive/shared-NNN` folder before each new runtime run.
- Fresh runtime output exists under `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- `shared/run-provenance.json` records non-sensitive source spec and pipeline-version references.
- `shared/results/summary.json` accounts for all eight records exactly once.
- `TXN001` and `TXN008` are `settled`.
- `TXN002`, `TXN003`, `TXN004`, and `TXN005` are `review_required`.
- `TXN006` is `rejected` with `UNSUPPORTED_CURRENCY`.
- `TXN007` is `rejected` with `NON_POSITIVE_AMOUNT`.
- `python -m pytest` passes.
- `python -m pytest --cov=.` reports at least 75% coverage when coverage tooling is available.
- Future read-only result tools can read final result files without rerunning the pipeline.

## Low-Level Tasks

### Task 1: Create Python Package Skeleton

**Prompt:** Create the Python package and test skeleton for a deterministic educational transaction-processing pipeline. Keep runtime product files focused on `integrator.py`, `agents/common.py`, `agents/transaction_validator.py`, `agents/fraud_detector.py`, `agents/settlement_processor.py`, optional future `mcp/server.py`, and pytest tests. Do not implement repository delivery, command-wrapper, configuration, or submission mechanics.

**File to CREATE:** `integrator.py`, `agents/__init__.py`, `agents/common.py`, `agents/transaction_validator.py`, `agents/fraud_detector.py`, `agents/settlement_processor.py`, `tests/conftest.py`, `tests/test_common.py`, `tests/test_validator.py`, `tests/test_fraud_detector.py`, `tests/test_integrator_pipeline.py`

**Function to CREATE:** `main()`, `process_message(message: dict) -> dict` in each runtime component.

**Details:** Use standard-library-first Python. Runtime components are stack-native modules. Tests must import modules from the homework root without external services.

**Edge cases:** Missing package init, imports from temp working directories, accidental CWD dependency, and imports from repository control artifacts.

**Acceptance criteria:** Runtime modules import cleanly; each component exposes `process_message`; `integrator.py` has `main()`.

**Verification:** `python -m pytest`; `python -c "import integrator; from agents import transaction_validator, fraud_detector, settlement_processor"`.

### Task 2: Implement Shared Envelope, Decimal, Redaction, Audit, And JSON Helpers

**Prompt:** In `agents/common.py`, implement common helpers for message envelopes, Decimal parsing, strict JSON writing, account redaction, audit-event construction, component history, and sensitive-field checks.

**File to CREATE/UPDATE:** `agents/common.py`

**Function to CREATE:** `create_message`, `validate_message_envelope`, `parse_amount`, `serialize_amount`, `validate_money_scale`, `redact_account_id`, `create_audit_event`, `append_component_history`, `assert_no_sensitive_fields`, `write_json_file`

**Details:** Generate UUID4 message IDs and UTC timestamps. Parse money from strings only. Serialize Decimal values as strings. Use `json.dump(..., allow_nan=False, sort_keys=True, indent=2)`. Reject sensitive keys before audit or final result writes.

**Edge cases:** Float amount input, `NaN`, `Infinity`, zero/negative amounts, nested `metadata`, raw descriptions, non-serializable payloads, and malformed account IDs.

**Acceptance criteria:** Helpers are deterministic and JSON-safe; money never uses `float`; audit and final-result helpers block sensitive raw fields.

**Verification:** `python -m pytest tests/test_common.py`.

### Task 3: Implement Prior-Run Archival, Fresh Shared Tree, And Runtime Provenance

**Prompt:** In `integrator.py`, archive an existing `shared/` tree to the next `archive/shared-NNN` folder, create a fresh protocol tree, and write non-sensitive `shared/run-provenance.json`.

**File to CREATE/UPDATE:** `integrator.py`

**Function to CREATE:** `next_archive_path`, `archive_existing_shared`, `prepare_shared_directories`, `fingerprint_file`, `write_run_provenance`

**Details:** `base_dir` is injectable for tests. Archive numbering is based on existing directories. Provenance includes schema version, runtime run ID, timestamp, source spec reference, and selected pipeline-version reference. It excludes raw transactions and sensitive data.

**Edge cases:** No prior `shared/`, archive gaps, empty shared tree, failed prior run, missing spec path, missing pipeline version, and failure before processing begins.

**Acceptance criteria:** Consecutive runs preserve prior runtime evidence and create fresh current directories; provenance exists in current and archived `shared/` trees.

**Verification:** `python -m pytest tests/test_integrator_pipeline.py -k "archive or provenance"`.

### Task 4: Load Transactions And Write Initial Input Messages

**Prompt:** Implement transaction loading and initial input message writing. Load `sample-transactions.json`, validate top-level shape, preserve input order, wrap each transaction in a shared envelope, and write deterministic input files.

**File to CREATE/UPDATE:** `integrator.py`

**Function to CREATE:** `load_transactions`, `write_input_messages`

**Details:** Use filenames like `001-TXN001.json`. Duplicate or missing transaction IDs are controlled setup errors. Internal input files may contain raw transaction data, but audit and final outputs must not.

**Edge cases:** Missing file, malformed JSON, non-list top-level value, non-object entries, duplicate IDs, and missing IDs.

**Acceptance criteria:** Eight input messages are created in stable order from the sample file.

**Verification:** `python -m pytest tests/test_integrator_pipeline.py -k input_loading`.

### Task 5: Implement Transaction Validator

**Prompt:** Create `agents/transaction_validator.py` as a runtime component that validates required fields, amount strings, supported currencies, and timestamp shape. It should emit safe validated or rejected messages.

**File to CREATE/UPDATE:** `agents/transaction_validator.py`

**Function to CREATE:** `process_message`, `validate_transaction`, `validate_required_fields`, `validate_currency`, `validate_timestamp`

**Details:** Required fields are `transaction_id`, `timestamp`, `source_account`, `destination_account`, `amount`, `currency`, `transaction_type`, and `metadata`. Currency must be uppercase, ISO 4217-style, and in `USD`, `EUR`, `GBP`. `TXN006` rejects with `UNSUPPORTED_CURRENCY`; `TXN007` rejects with `NON_POSITIVE_AMOUNT`.

**Edge cases:** Missing fields, lowercase currency, `XYZ`, invalid amount, float amount, zero, negative, malformed timestamp, missing metadata, and malformed envelope.

**Acceptance criteria:** Valid records return `status: "validated"` with normalized amount and currency. Invalid records return `status: "rejected"` and stable reason codes, without raw sensitive fields in audit or summaries.

**Verification:** `python -m pytest tests/test_validator.py`.

### Task 6: Implement Validator Dry-Run Seam

**Prompt:** Implement validation-only behavior for a future `/validate-transactions` workflow. It reads transactions and reports validation outcomes without running risk scoring, settlement, or real `shared/` mutation by default.

**File to CREATE/UPDATE:** `agents/transaction_validator.py`, optionally `integrator.py`

**Function to CREATE:** `validate_transactions_dry_run`, optionally `validate_transactions_file`

**Details:** Return total, valid count, rejected count, and safe per-transaction validation results. Omit raw account IDs, descriptions, and metadata.

**Edge cases:** Malformed input, duplicate IDs, missing fields, all-invalid input, and mixed valid/invalid records.

**Acceptance criteria:** Dry-run over the sample reports eight records, two validation rejections, and six valid records before fraud/settlement.

**Verification:** `python -m pytest tests/test_validator.py -k dry_run`.

### Task 7: Implement Fraud Detector

**Prompt:** Create `agents/fraud_detector.py` with deterministic educational risk scoring. Score only validated transactions and produce `risk_score`, `risk_level`, and reason codes.

**File to CREATE/UPDATE:** `agents/fraud_detector.py`

**Function to CREATE:** `process_message`, `score_fraud_risk`, `classify_risk`

**Details:** Use the scoring table in Implementation Notes. Keep all thresholds as `Decimal` comparisons where amounts are involved. Validation rejections bypass scoring.

**Edge cases:** Boundary amounts `9999.99`, `10000.00`, and `50000.00`, early-hour parsing, missing metadata, rejected validation messages, and malformed timestamp.

**Acceptance criteria:** Risk scoring is repeatable. `TXN002`, `TXN003`, `TXN004`, and `TXN005` route to review; `TXN001` and `TXN008` remain low enough to settle.

**Verification:** `python -m pytest tests/test_fraud_detector.py`.

### Task 8: Implement Settlement Processor

**Prompt:** Create `agents/settlement_processor.py` with simulated final-outcome decisions. Preserve validation rejections, settle low-risk valid transactions, route elevated-risk transactions to review, and reserve `error` for controlled failures.

**File to CREATE/UPDATE:** `agents/settlement_processor.py`

**Function to CREATE:** `process_message`, `decide_final_status`, `build_final_result`

**Details:** Allowed final statuses are `settled`, `rejected`, `review_required`, and `error`. Settled records include `SETTLEMENT_SIMULATED`. Review records include risk reasons. Rejected records preserve validation reasons.

**Edge cases:** Missing risk fields, unknown risk level, rejected validation messages, downstream exceptions, and invalid status values.

**Acceptance criteria:** Every transaction receives exactly one allowed final status. Final payloads omit account IDs, descriptions, and raw metadata.

**Verification:** `python -m pytest tests/test_integrator_pipeline.py -k final_status`.

### Task 9: Orchestrate Full Pipeline And File Movement

**Prompt:** In `integrator.py`, orchestrate the runtime components in order and write stage evidence through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.

**File to CREATE/UPDATE:** `integrator.py`

**Function to CREATE:** `write_stage_message`, `process_transaction`, `run_pipeline`, `main`

**Details:** `run_pipeline` prepares directories, writes provenance, loads transactions, writes input messages, processes each transaction, writes final results, and writes summary. Component exceptions become safe `error` results for the affected transaction while the run continues.

**Edge cases:** Malformed input message, component exception, output collision, missing directory, empty input list, and partial prior processing state.

**Acceptance criteria:** `python integrator.py` writes eight result files plus `summary.json`; every original transaction ID is accounted for.

**Verification:** `python integrator.py`; `python -m pytest tests/test_integrator_pipeline.py -k pipeline`.

### Task 10: Write Result Files And Summary

**Prompt:** Implement result writing and summary aggregation. Result shapes must be stable, audit-safe, and readable by future status tools.

**File to CREATE/UPDATE:** `integrator.py`, `agents/common.py`

**Function to CREATE:** `write_result`, `summarize_results`, `write_summary`

**Details:** Per-transaction files are `shared/results/<transaction_id>.json`; summary is `shared/results/summary.json`; status counts must add to the total.

**Edge cases:** Duplicate transaction IDs, missing status, unrecognized status, empty results, and sensitive fields in final payloads.

**Acceptance criteria:** Summary reports `total_transactions=8`, `settled=2`, `rejected=2`, `review_required=4`, `error=0` for the sample happy path.

**Verification:** `python -m pytest tests/test_integrator_pipeline.py -k summary`.

### Task 11: Implement Per-Transaction Error Recovery

**Prompt:** Implement controlled per-transaction error handling in the integrator. If a transaction cannot be processed, write a safe `error` result when possible and continue processing later transactions.

**File to CREATE/UPDATE:** `integrator.py`, `agents/common.py`

**Function to CREATE:** `build_error_result`, `safe_error_message`

**Details:** Error records include stable reason codes such as `MALFORMED_JSON`, `COMPONENT_FAILURE`, `MISSING_TRANSACTION_ID`, and `PIPELINE_STAGE_ERROR`. They must not include stack traces or raw payloads.

**Edge cases:** Missing transaction ID, exception text containing sensitive input, write-result failure, malformed JSON before envelope parsing, and rerun after partial failure.

**Acceptance criteria:** A controlled component failure creates one safe `error` result and does not block remaining results.

**Verification:** `python -m pytest tests/test_integrator_pipeline.py -k error_recovery`.

### Task 12: Add Read-Only Result Helpers After Result Shapes Exist

**Prompt:** After result files and `summary.json` exist, create optional `mcp/server.py` read-only helpers that load existing product result files. Do not edit MCP configuration files.

**File to CREATE/UPDATE:** `mcp/server.py`, `mcp/__init__.py`

**Function to CREATE:** `load_summary`, `get_transaction_status`, `list_pipeline_results`

**Details:** Helpers return safe result payloads, handle missing files gracefully, and never run or mutate the pipeline.

**Edge cases:** No run yet, missing summary, unknown transaction ID, malformed result JSON, archived shared directories.

**Acceptance criteria:** Helpers can read summary and transaction status from generated results without exposing raw inputs.

**Verification:** `python -m pytest tests/test_integrator_pipeline.py -k mcp_readable` or `python -m pytest tests/test_mcp_readers.py`.

### Task 13: Add Pytest Coverage With Temporary Filesystem State

**Prompt:** Create pytest coverage for common helpers, validation, risk scoring, settlement, orchestration, archival, provenance, redaction, result shapes, dry-run validation, and failure recovery. Use `tmp_path` so tests do not mutate real `shared/` or `archive/`.

**File to CREATE/UPDATE:** `tests/conftest.py`, `tests/test_common.py`, `tests/test_validator.py`, `tests/test_fraud_detector.py`, `tests/test_integrator_pipeline.py`, optionally `tests/test_mcp_readers.py`

**Function to CREATE:** `sample_transactions` fixture, `isolated_workspace` fixture, and behavior-specific tests.

**Details:** Tests cover Decimal float rejection, `TXN006`, `TXN007`, deterministic risk, status vocabulary, summary counts, privacy checks, archival, provenance, dry-run behavior, and error recovery.

**Edge cases:** Tests writing real `shared/`, nondeterministic timestamps, order-sensitive assertions, and coverage passing without privacy checks.

**Acceptance criteria:** `python -m pytest` passes; `python -m pytest --cov=.` reports at least 75% coverage when available.

**Verification:** `python -m pytest`; `python -m pytest --cov=.`.

## Edge Cases And Failure Modes

| Case | Expected behavior |
|---|---|
| Unsupported currency `XYZ` | Reject with `UNSUPPORTED_CURRENCY`; do not score or settle. |
| Negative or zero amount | Reject with `NON_POSITIVE_AMOUNT`; do not score or settle. |
| Malformed amount or float amount | Reject with `INVALID_AMOUNT`; never parse through binary float. |
| Missing required field | Reject with `MISSING_FIELD` and safe field label. |
| Malformed JSON input | Return controlled setup error or safe per-transaction `error` when ID is known. |
| Component exception | Write safe `error` result for that transaction and continue. |
| Prior `shared/` exists | Archive to next zero-padded folder before fresh run. |
| Result payload includes sensitive raw field | Fail the write or test, then repair the result-shape builder. |
| Future MCP reader sees missing results | Return safe missing-result response without running pipeline. |

## Verification Mapping

| Objective | Evidence |
|---|---|
| M1 | Tasks 1, 3, 4, and 9 verify package structure, fresh shared directories, archival, provenance, input messages, and orchestration. |
| M2 | Tasks 2, 5, 6, and 13 verify Decimal parsing, currency validation, required fields, dry-run behavior, and privacy-safe rejection output. |
| M3 | Tasks 7 and 13 verify deterministic educational risk signals and review routing. |
| M4 | Tasks 8, 10, and 11 verify final statuses, simulated settlement, result summaries, and per-transaction failure recovery. |
| M5 | Tasks 3, 6, 12, and 13 verify rerun isolation, provenance, dry-run validation, read-only result shapes, pytest commands, and coverage target readiness. |

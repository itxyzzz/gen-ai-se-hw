# Python Transaction Processing Pipeline Specification

> Status: Fresh preserved Python candidate for `stack=python`.
> Scope: Generated Transaction System Layer only. This specification describes deterministic Python runtime software, tests, and result contracts for an educational transaction-processing simulation.

## High-Level Objective

Build a deterministic Python educational transaction-processing pipeline that reads `sample-transactions.json`, processes every transaction through audit-safe JSON file protocol stages, and writes complete sanitized results and summaries under `shared/results/`.

## Mid-Level Objectives

| ID | Objective | Observable success |
|---|---|---|
| M1 | Validate transaction structure, money, and currency before downstream processing. | Every input record is either accepted with `Decimal`-serialized amount and an allowed currency or rejected with a safe reason code for missing fields, invalid currency such as `XYZ`, malformed timestamp, unparseable amount, or non-positive amount. |
| M2 | Score fraud risk using deterministic educational rules without compliance claims. | High-value records, early-hour records, and selected channel/type patterns receive reproducible risk flags or reason codes, while all outputs avoid AML, sanctions, KYC, legal, regulatory, payment-network, or real fraud-detection claims. |
| M3 | Settle only transactions that pass validation and risk checks. | Each transaction receives exactly one final safe outcome. Rejected or review-required transactions are never marked settled, and final result files contain transaction ID, status, reason codes, amount string, currency, component history count, and audit event count. |
| M4 | Produce audit-safe reporting artifacts for all processed records. | `summary.json` and `pipeline-status.json` account for all 8 sample records, include status and reason-code counts, confirm result completeness, and contain no plaintext account IDs, descriptions, or full raw metadata. |
| M5 | Support repeatable Python runs with isolated tests and traceable runtime state. | `python integrator.py` archives any prior `shared/` tree to `archive/shared-001` style folders, recreates protocol directories, writes non-sensitive `shared/run-provenance.json`, and `python -m pytest` passes using `tmp_path` isolation with a non-blocking 75% coverage target for this specification stage. |

## Runtime Components

The generated product must include these stack-native Python components:

| Component | Python file | Primary callable | Responsibility |
|---|---|---|---|
| Integrator | `integrator.py` | `main() -> int`, `run_pipeline(...) -> dict` | Prepares directories, archives prior runs, loads sample input, writes provenance, orchestrates component flow, handles transaction-level errors, and verifies result completeness. |
| Transaction Validator | `agents/transaction_validator.py` | `process_message(message: dict) -> dict` | Validates required fields, parseable positive `Decimal` amount strings, bounded ISO 4217-style currency allow-list, timestamps, and dry-run validation. |
| Fraud Detector | `agents/fraud_detector.py` | `process_message(message: dict) -> dict` | Applies deterministic educational risk scoring for high value, early-hour, channel, country, and transaction-type signals. |
| Settlement Processor | `agents/settlement_processor.py` | `process_message(message: dict) -> dict` | Produces final simulated settlement outcomes only for validated low-risk transactions and preserves rejected/review-required states. |
| Reporting Agent | `agents/reporting_agent.py` | `process_message(message: dict) -> dict` | Writes safe per-transaction results, `summary.json`, `pipeline-status.json`, status counts, reason-code counts, completeness checks, and result privacy checks. |

These runtime components are Python application modules, not Claude/Codex skills and not Homework Automation Layer agents.

## Implementation Notes

### Stack And Commands

- Runtime language: Python.
- Money: use `decimal.Decimal`; never use `float` or binary floating point for amounts.
- JSON: use the standard `json` module and convert `Decimal` values to strings before serialization.
- Filesystem: use `pathlib.Path`, `os`, and `shutil` for shared protocol directories and archival.
- Tests: use `pytest`, `tmp_path`, and `monkeypatch` for isolation.
- Required runnable commands:
  - `python integrator.py`
  - `python -m pytest`
  - `python -m pytest --cov=.` as a non-blocking coverage report command when `pytest-cov` is available.
- Temporary ending-context coverage target for this specification-stage output: 75%. Later test-generation work owns increasing the blocking gate above 80%.

### Money, Currency, And Validation

- Parse monetary values from strings with `Decimal(value)`.
- Reject unparseable, non-finite, zero, and negative amounts.
- Serialize all output amounts as strings, preserving decimal precision.
- Use a bounded ISO 4217-style currency allow-list for the sample: `USD`, `EUR`, and `GBP`.
- Reject unsupported values such as `XYZ` with `UNSUPPORTED_CURRENCY`.
- Do not claim live ISO 4217 registry maintenance, payment-network validation, or legal compliance.

### JSON Message Protocol

Runtime components communicate with JSON files through this required product protocol:

```text
shared/
  input/
  processing/
  output/
  results/
```

Each message envelope must include:

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
    "status": "received"
  },
  "component_history": [],
  "audit_events": []
}
```

The word `agent` in JSON fields means product runtime component identity, not a Homework Automation Layer agent.

### Audit And Privacy

- Treat account IDs, descriptions, and transaction metadata as sensitive.
- Logs, audit events, summaries, docs, examples, and result payloads must not expose plaintext account IDs, descriptions, or full raw metadata.
- Audit events must include only:
  - ISO 8601 timestamp.
  - Runtime component name.
  - Transaction ID.
  - Safe outcome/status.
  - Reason code when applicable.
- Prefer stable reason codes over prose-heavy messages.
- If account examples are unavoidable in developer-only diagnostics, redact them as `ACC-****1001`.
- The system is an educational simulation. It does not move money, contact payment networks, detect real fraud, perform sanctions/AML/KYC checks, or make legal, banking, or regulatory decisions.

### Deterministic Reruns And Provenance

- Before a new run creates fresh protocol directories, archive any existing `shared/` tree under the next zero-padded sibling folder, such as `archive/shared-001`, `archive/shared-002`, or `archive/shared-003`.
- A fresh run must recreate:
  - `shared/input`
  - `shared/processing`
  - `shared/output`
  - `shared/results`
- Each run must write `shared/run-provenance.json` with non-sensitive traceability only:
  - schema version
  - runtime run ID
  - generated timestamp
  - stack
  - source specification run ID and SHA-256 fingerprint supplied to the runtime package
  - selected generated pipeline version or package fingerprint supplied to the runtime package
- Provenance must not include raw transactions, account IDs, descriptions, credentials, prompt text, hidden thread content, or full metadata.

### MCP-Readable Result Responsibility

The product must write stable result shapes that a later read-only status tool can consume. This specification does not require the transaction system to create MCP configuration. It requires only product result files:

- `shared/results/summary.json`
- `shared/results/pipeline-status.json`
- `shared/results/TXN*.json`

Future status tooling should be able to implement `get_transaction_status`, `list_pipeline_results`, and `pipeline://summary` by reading those files without rerunning the pipeline or accessing raw input transactions.

### Downstream Generation Notes

Later code generation should document at least two Context7 queries in its research notes for the concrete Python implementation choices it applies. That is downstream generation evidence, not runtime product behavior.

## Context

### Beginning State

- `sample-transactions.json` exists at the project root.
- It contains 8 synthetic transaction records.
- Safe structural sample coverage:
  - Currencies include `USD`, `EUR`, `GBP`, and unsupported `XYZ`.
  - Transaction types include transfers, wire transfers, and a refund.
  - Channels include online, branch, API, and mobile.
  - Countries include US, DE, and GB.
  - High-value examples: `TXN002`, `TXN005`.
  - Early-hour example: `TXN004`.
  - Unsupported currency example: `TXN006`.
  - Non-positive amount example: `TXN007`.
- No generated product code is assumed beyond this specification.

### Ending State

After implementation:

- `python integrator.py` processes all 8 sample records.
- Existing `shared/` output is archived before a fresh run starts.
- Fresh protocol directories exist under `shared/`.
- `shared/run-provenance.json` records non-sensitive runtime traceability.
- `shared/results/` contains one safe transaction result file per sample transaction.
- `shared/results/summary.json` contains total, settled, rejected, review-required, and error counts.
- `shared/results/pipeline-status.json` contains a compact status view for future read-only tooling.
- Invalid currency and non-positive amount examples are rejected with safe reason codes.
- High-value examples are review-required unless another validation failure applies.
- Ordinary valid low-risk examples can be settled as simulated outcomes.
- Tests pass with `python -m pytest` and use isolated temporary directories.
- A non-blocking coverage report can demonstrate at least 75% coverage at this stage.

## Result Contracts

### Per-Transaction Result Shape

Each `shared/results/TXN*.json` file must use this safe shape or a strict extension:

```json
{
  "schema_version": 1,
  "transaction_id": "TXN001",
  "status": "settled",
  "reason_codes": ["SETTLED"],
  "amount": "1500.00",
  "currency": "USD",
  "safe_summary": "Simulated transaction outcome recorded.",
  "component_history_count": 4,
  "audit_event_count": 4,
  "processed_at": "2026-03-16T10:00:00Z",
  "privacy_check": "passed"
}
```

Allowed final statuses: `settled`, `rejected`, `review_required`, and `error`.

### Summary Shape

`shared/results/summary.json` must include:

- `schema_version`
- `runtime_run_id`
- `generated_at`
- `total_records`
- `settled`
- `rejected`
- `review_required`
- `error`
- `status_counts`
- `reason_code_counts`
- `expected_transaction_ids`
- `result_files`
- `privacy_check`
- `completeness_check`

### Pipeline Status Shape

`shared/results/pipeline-status.json` must include:

- `schema_version`
- `pipeline_version`
- `run_status`
- `summary_path`
- `total_records`
- `status_counts`
- `reason_code_counts`
- `result_files`
- `generated_at`

## Low-Level Tasks

### Task 1: Create Python Package Skeleton And Protocol Constants

- **Task:** Establish the Python runtime module layout and shared protocol constants.
- **Prompt:** "Create the product Python package structure for a deterministic transaction-processing pipeline. Define shared directory names, status values, reason-code constants, component names, and a pipeline version constant without implementing operator-layer workflows."
- **File to CREATE:** `agents/__init__.py`
- **File to UPDATE:** `agents/common.py`
- **Function to CREATE:** `get_protocol_paths(base_dir: Path) -> dict[str, Path]`
- **Details:** Use `pathlib.Path`. Return paths for `shared`, `input`, `processing`, `output`, `results`, `archive`, and `run_provenance`. Define statuses `validated`, `rejected`, `review_required`, `settled`, and `error`. Define reason codes `MISSING_FIELD`, `INVALID_AMOUNT`, `NON_POSITIVE_AMOUNT`, `UNSUPPORTED_CURRENCY`, `REVIEW_HIGH_VALUE`, `REVIEW_UNUSUAL_TIME`, `SETTLED`, and `PROCESSING_ERROR`.
- **Edge cases:** Missing base directory, Windows path separators, repeated imports, status typos.
- **Acceptance criteria:** Runtime modules import constants from `agents.common`; no runtime component name uses Homework Automation identity labels.
- **Verification:** `python -m pytest tests/test_common.py`

### Task 2: Implement JSON Envelope, Decimal, Audit, And Redaction Helpers

- **Task:** Centralize safe JSON message handling and privacy helpers.
- **Prompt:** "Implement common helpers for message envelopes, precise money parsing, safe Decimal serialization, structured audit events, account redaction, and transaction sanitization. Never use binary floating point."
- **File to UPDATE:** `agents/common.py`
- **Function to CREATE:** `parse_amount(value: str) -> Decimal`, `serialize_amount(value: Decimal) -> str`, `create_message(...) -> dict`, `append_audit_event(message: dict, component: str, outcome: str, reason_code: str | None = None) -> dict`, `redact_account_id(value: str) -> str`, `sanitize_transaction(raw: dict) -> dict`
- **Details:** Parse amounts from strings only. Reject non-finite Decimal values. Serialize money as strings. Message envelope includes `message_id`, `timestamp`, `source_agent`, `target_agent`, `message_type`, `data`, `component_history`, and `audit_events`. Audit events include timestamp, component, transaction ID, safe outcome, and optional reason code. Sanitized data omits raw account IDs, descriptions, and full metadata.
- **Edge cases:** Decimal passed as float, malformed amount string, empty account ID, missing transaction ID, metadata containing sensitive text.
- **Acceptance criteria:** JSON output from helpers is serializable with standard `json.dumps`; audit output contains no raw account IDs or descriptions.
- **Verification:** `python -m pytest tests/test_common.py`

### Task 3: Implement Deterministic Run Reset, Archival, And Provenance

- **Task:** Prepare fresh runtime directories while preserving prior evidence.
- **Prompt:** "Implement run setup that archives an existing `shared/` tree into the next zero-padded `archive/shared-001` style folder before creating a fresh protocol tree and provenance file."
- **File to UPDATE:** `integrator.py`
- **Function to CREATE:** `archive_existing_shared(base_dir: Path) -> Path | None`, `prepare_shared_directories(base_dir: Path) -> dict[str, Path]`, `write_run_provenance(paths: dict[str, Path], provenance: dict) -> Path`
- **Details:** If `shared/` exists, move it under `archive/shared-001`, `archive/shared-002`, etc. Then create `shared/input`, `shared/processing`, `shared/output`, and `shared/results`. Write `shared/run-provenance.json` with schema version, runtime run ID, generated timestamp, source spec reference, pipeline version reference, and safe fingerprints only.
- **Edge cases:** Existing archive folders, partial `shared/` tree, read/write failure, provenance values missing, repeated reruns.
- **Acceptance criteria:** Repeated `python integrator.py` runs never overwrite prior `shared/`; provenance contains no raw transactions, account IDs, descriptions, credentials, or prompt text.
- **Verification:** `python -m pytest tests/test_integrator_setup.py`

### Task 4: Load Sample Input And Seed Protocol Messages

- **Task:** Load `sample-transactions.json` and write initial input messages.
- **Prompt:** "Implement input loading and message seeding from `sample-transactions.json` into `shared/input`, preserving all records for processing while keeping logs and written envelopes privacy-safe."
- **File to UPDATE:** `integrator.py`
- **Function to CREATE:** `load_transactions(input_path: Path) -> list[dict]`, `seed_input_messages(transactions: list[dict], paths: dict[str, Path]) -> list[Path]`
- **Details:** Read standard JSON. Expect 8 sample records. Write one envelope per transaction to `shared/input/TXN*.json` using safe message helpers. Do not print or log raw account IDs, descriptions, or full metadata.
- **Edge cases:** Missing input file, malformed JSON, duplicate transaction IDs, missing transaction ID, non-list input.
- **Acceptance criteria:** All input records become input message files; malformed file-level input produces a safe actionable error.
- **Verification:** `python -m pytest tests/test_input_loading.py`

### Task 5: Implement Transaction Validator

- **Task:** Validate transaction structure, amount, currency, and timestamp before downstream processing.
- **Prompt:** "Implement a stack-native Transaction Validator component with `process_message(message: dict) -> dict`. It must reject unsafe records before fraud scoring and serialize valid money as Decimal strings."
- **File to CREATE:** `agents/transaction_validator.py`
- **Function to CREATE:** `process_message(message: dict) -> dict`, `validate_transaction(transaction: dict) -> dict`
- **Details:** Required fields: transaction ID, timestamp, source account, destination account, amount, currency, transaction type, and metadata. Parse amount with `Decimal` from string; reject unparseable or non-positive values. Use a bounded ISO 4217-style allow-list of `USD`, `EUR`, and `GBP`; reject `XYZ`. Validate timestamp as ISO 8601-style enough for deterministic processing. Valid output status is `validated`; invalid output status is `rejected` with safe reason codes.
- **Edge cases:** `XYZ`, negative amount, zero amount, blank amount, missing metadata, missing timestamp, malformed timestamp, missing account fields.
- **Acceptance criteria:** Invalid records do not proceed as valid; outputs never expose raw account IDs/descriptions in audit events.
- **Verification:** `python -m pytest tests/test_transaction_validator.py`

### Task 6: Add Validator Dry-Run Seam

- **Task:** Support validation-only execution for future dry-run command surfaces.
- **Prompt:** "Add a validation-only seam that reads transactions and returns validation results without running fraud scoring, settlement, reporting summaries, or mutating production `shared/` directories unless an explicit temporary base directory is supplied."
- **File to UPDATE:** `integrator.py`, `agents/transaction_validator.py`
- **Function to CREATE:** `validate_transactions_only(input_path: Path, base_dir: Path | None = None) -> list[dict]`
- **Details:** Reuse Transaction Validator logic. Return one safe result per input transaction with transaction ID, status, and reason codes. This supports a future validation-only wrapper but does not implement command/plugin configuration.
- **Edge cases:** Invalid JSON file, no records, mixed valid/invalid records, temporary directory omitted, duplicate transaction IDs.
- **Acceptance criteria:** Dry-run validation can be tested with `tmp_path` and does not require the full pipeline.
- **Verification:** `python -m pytest tests/test_validator_dry_run.py`

### Task 7: Implement Fraud Detector

- **Task:** Score deterministic educational fraud risk.
- **Prompt:** "Implement a Fraud Detector component that assigns reproducible risk scores and review reason codes using transparent educational heuristics, without making compliance or real fraud-detection claims."
- **File to CREATE:** `agents/fraud_detector.py`
- **Function to CREATE:** `process_message(message: dict) -> dict`, `score_fraud_risk(transaction: dict) -> dict`
- **Details:** Only process `validated` messages. Deterministic rules: amount `>= 25000.00` adds `REVIEW_HIGH_VALUE`; timestamp hour before `04:00` adds `REVIEW_UNUSUAL_TIME`; wire transfer can add risk weight; API/mobile/branch channels can be deterministic risk modifiers without exposing raw metadata in output. High-risk messages become `review_required`; ordinary valid messages continue toward settlement.
- **Edge cases:** Already rejected message, missing timestamp after validation bug, high-value `TXN002`/`TXN005`, early-hour `TXN004`, non-USD valid currency, missing metadata channel.
- **Acceptance criteria:** `TXN002` and `TXN005` are review-required for high value; `TXN004` receives an unusual-time risk reason; rejected records stay rejected.
- **Verification:** `python -m pytest tests/test_fraud_detector.py`

### Task 8: Implement Settlement Processor

- **Task:** Settle only transactions that pass validation and risk checks.
- **Prompt:** "Implement a Settlement Processor component that marks eligible transactions as settled and preserves rejected or review-required statuses without pretending to perform real banking settlement."
- **File to CREATE:** `agents/settlement_processor.py`
- **Function to CREATE:** `process_message(message: dict) -> dict`, `settle_transaction(transaction: dict, current_status: str) -> dict`
- **Details:** Settle only `validated` records with acceptable risk. Review-required and rejected records must not become settled. Output safe final status, reason code, amount string, currency, transaction ID, component history, and audit event count.
- **Edge cases:** Rejected validation record, review-required fraud record, missing amount after validation bug, duplicate settlement attempt, unsupported status.
- **Acceptance criteria:** Every record receives one final outcome; review-required and rejected records remain non-settled.
- **Verification:** `python -m pytest tests/test_settlement_processor.py`

### Task 9: Implement Reporting Agent Result Writing And Summaries

- **Task:** Produce audit-safe per-transaction and aggregate result artifacts.
- **Prompt:** "Implement a Reporting Agent component that writes one safe `shared/results/TXN*.json` result per transaction, `summary.json`, and `pipeline-status.json`, with privacy checks and consistency checks."
- **File to CREATE:** `agents/reporting_agent.py`
- **Function to CREATE:** `process_message(message: dict) -> dict`, `write_transaction_result(message: dict, results_dir: Path) -> Path`, `summarize_results(results_dir: Path, expected_transaction_ids: list[str]) -> dict`, `build_pipeline_status(summary: dict) -> dict`, `assert_privacy_safe(payload: dict) -> None`
- **Details:** Result files include transaction ID, status, reason codes, amount string, currency, component history count, audit event count, and safe processing summary. `summary.json` includes total, settled, rejected, review-required, and error counts. `pipeline-status.json` includes run status, totals, result file names, generated timestamp, and summary location for future read-only status tools.
- **Edge cases:** Missing result file, mismatched counts, raw account ID leaked, description leaked, error status result, duplicate transaction ID.
- **Acceptance criteria:** All 8 records are accounted for; summaries are internally consistent; no plaintext account IDs/descriptions/full metadata appear in result artifacts.
- **Verification:** `python -m pytest tests/test_reporting_agent.py`

### Task 10: Implement Integrator Orchestration And File Movement

- **Task:** Orchestrate the full runtime pipeline through JSON protocol stages.
- **Prompt:** "Implement `integrator.py` so `python integrator.py` prepares directories, loads sample input, sends each message through Transaction Validator, Fraud Detector, Settlement Processor, and Reporting Agent, and verifies every input has a final result."
- **File to UPDATE:** `integrator.py`
- **Function to CREATE:** `process_transaction(message_path: Path, paths: dict[str, Path]) -> dict`, `run_pipeline(base_dir: Path = Path("."), input_path: Path = Path("sample-transactions.json")) -> dict`, `main() -> int`
- **Details:** Move/read messages through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`. Use the common `process_message(message: dict) -> dict` callable shape for runtime components. Write final summary after all transactions finish. Return exit code `0` for completed runs with handled rejections/reviews; reserve nonzero for file-level unrecoverable failures.
- **Edge cases:** Component exception, malformed message file, missing input message, duplicate output, partial processing failure, rerun after previous output exists.
- **Acceptance criteria:** Running `python integrator.py` produces `shared/results/summary.json`, `shared/results/pipeline-status.json`, and one safe result file per sample transaction.
- **Verification:** `python integrator.py`; then inspect `shared/results/summary.json` and `shared/results/pipeline-status.json`.

### Task 11: Add Per-Transaction Error Handling And Failure Recovery

- **Task:** Keep the pipeline resilient when one record fails unexpectedly.
- **Prompt:** "Add safe per-transaction exception handling so a single malformed message or component failure creates an error result for that transaction without preventing remaining records from being processed."
- **File to UPDATE:** `integrator.py`, `agents/reporting_agent.py`
- **Function to CREATE:** `safe_process_transaction(message_path: Path, paths: dict[str, Path]) -> dict`, `write_error_result(transaction_id: str, reason_code: str, results_dir: Path) -> Path`
- **Details:** Catch component exceptions at transaction boundaries. Write safe error result with `PROCESSING_ERROR`, transaction ID when known, no stack trace in JSON result, and audit event count. Preserve enough console output for developer troubleshooting without printing sensitive input payloads.
- **Edge cases:** Unknown transaction ID, JSON decode error, reporting failure, processing directory contains stale file, exception message contains sensitive text.
- **Acceptance criteria:** One bad record produces an error result and does not block other records; summaries include error counts.
- **Verification:** `python -m pytest tests/test_error_recovery.py`

### Task 12: Define MCP-Readable Product Result Shapes

- **Task:** Keep result artifacts stable for future read-only status tooling.
- **Prompt:** "Standardize result JSON shapes that future status readers can consume without needing product internals or raw transaction data."
- **File to UPDATE:** `agents/reporting_agent.py`, `agents/common.py`
- **Function to CREATE:** `build_transaction_status_payload(message: dict) -> dict`, `list_result_files(results_dir: Path) -> list[str]`
- **Details:** Per-transaction status payload includes transaction ID, status, reason codes, amount string, currency, safe summary, component history count, audit event count, and generated timestamp. `pipeline-status.json` includes `schema_version`, `pipeline_version`, `run_status`, `summary_path`, `total_records`, `status_counts`, and `result_files`.
- **Edge cases:** No result files yet, partial run, invalid JSON result file, stale result from archived run accidentally read.
- **Acceptance criteria:** Result shape can support future `get_transaction_status`, `list_pipeline_results`, and `pipeline://summary` readers without exposing raw account IDs/descriptions.
- **Verification:** `python -m pytest tests/test_mcp_readable_shapes.py`

### Task 13: Add Isolated Pytest Fixtures And Unit Coverage

- **Task:** Build isolated unit tests for common helpers and each runtime component.
- **Prompt:** "Create pytest fixtures using `tmp_path` and `monkeypatch` so tests never mutate real `shared/` or `archive/` directories. Cover Decimal handling, validation, fraud scoring, settlement, reporting privacy checks, and error helpers."
- **File to CREATE:** `tests/conftest.py`, `tests/test_common.py`, `tests/test_transaction_validator.py`, `tests/test_fraud_detector.py`, `tests/test_settlement_processor.py`, `tests/test_reporting_agent.py`
- **Function to CREATE:** `isolated_runtime(tmp_path, monkeypatch)`, `test_parse_amount_rejects_float`, `test_serialize_amount_returns_string`, `test_validator_rejects_invalid_currency`, `test_validator_rejects_non_positive_amount`, `test_fraud_detector_flags_high_value`, `test_fraud_detector_flags_unusual_time`, `test_settlement_preserves_review_required`, `test_reporting_privacy_check_rejects_account_ids`
- **Details:** Use temporary directories for filesystem tests. Build sanitized fixture transactions directly in tests or load sample records while avoiding raw sensitive output assertions. Verify no floats are used for money.
- **Edge cases:** Test pollution between runs, accidental writes to repository root, Decimal serialization failure, raw account ID leak in failed assertion output.
- **Acceptance criteria:** Unit tests pass independently and can run repeatedly.
- **Verification:** `python -m pytest`

### Task 14: Add Integration, Rerun, Privacy, And Coverage Verification

- **Task:** Verify full pipeline behavior, archival, summaries, and temporary coverage target.
- **Prompt:** "Create integration tests that run the full pipeline from an isolated temporary directory, verify all 8 records are accounted for, verify prior `shared/` is archived on rerun, and confirm result artifacts are privacy-safe and status counts are consistent."
- **File to CREATE:** `tests/test_pipeline_integration.py`, `tests/test_rerun_archival.py`, `tests/test_privacy_and_result_shapes.py`
- **Function to CREATE:** `test_full_pipeline_processes_all_sample_records`, `test_rerun_archives_existing_shared_tree`, `test_results_do_not_expose_sensitive_fields`, `test_pipeline_status_matches_summary`
- **Details:** Copy or reference `sample-transactions.json` through temporary paths. Expected sample outcomes: 8 total records; `XYZ` invalid currency rejected; non-positive amount rejected; high-value records review-required; early-hour record risk-flagged; ordinary valid records settled. Coverage report command is non-blocking at this stage with a temporary 75% target.
- **Edge cases:** Archive numbering gaps, stale result files, review counts mismatched, invalid record accidentally settled, coverage plugin unavailable.
- **Acceptance criteria:** `python -m pytest` passes; `python -m pytest --cov=.` produces a report when pytest-cov is available; implementation can document temporary 75% coverage target without making it a blocking hook.
- **Verification:** `python -m pytest`; `python -m pytest --cov=.`

## Verification Matrix

| Objective | Acceptance evidence | Test categories | Manual review evidence |
|---|---|---|---|
| M1 | Invalid currency and non-positive amount are rejected; valid amounts use string-serialized `Decimal`. | Common helper tests, validator tests, dry-run tests, malformed-input tests. | Review reason codes and result files for no raw sensitive values. |
| M2 | High-value and early-hour examples receive deterministic review reason codes. | Fraud scoring tests, integration sample-outcome tests. | Confirm wording says educational risk scoring, not real fraud/compliance detection. |
| M3 | Settled, rejected, review-required, and error outcomes are mutually exclusive per transaction. | Settlement tests, integrator tests, error-recovery tests. | Confirm review/rejected records are never marked settled. |
| M4 | `summary.json`, `pipeline-status.json`, and `TXN*.json` are complete and privacy-safe. | Reporting tests, MCP-readable shape tests, privacy scan tests. | Confirm no plaintext account IDs, descriptions, or full metadata in result artifacts. |
| M5 | Reruns archive old `shared/`; provenance is written; tests use isolated temp directories. | Setup tests, archival tests, integration tests, coverage report. | Confirm provenance contains IDs/fingerprints only and no raw sample data. |

## Edge Cases And Failure Modes

| Case | Expected behavior |
|---|---|
| Missing required transaction field | Rejected with `MISSING_FIELD`; no raw record echoed. |
| Unsupported currency `XYZ` | Rejected with `UNSUPPORTED_CURRENCY`. |
| Negative or zero amount | Rejected with `NON_POSITIVE_AMOUNT`. |
| Unparseable amount | Rejected with `INVALID_AMOUNT`. |
| High value `>= 25000.00` | Review-required with `REVIEW_HIGH_VALUE` unless validation already rejected it. |
| Timestamp before `04:00` UTC | Risk reason `REVIEW_UNUSUAL_TIME`; review-required when threshold logic says so. |
| Component exception | Per-transaction `error` result with `PROCESSING_ERROR`; remaining transactions continue. |
| Existing `shared/` tree | Archive to next `archive/shared-###` folder before fresh run. |
| Potential privacy leak in result payload | Reporting Agent fails the privacy check and writes safe error/diagnostic state without exposing sensitive input. |
| Tests run repeatedly | `tmp_path` and `monkeypatch` prevent mutation of real runtime directories. |

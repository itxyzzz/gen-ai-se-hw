# Python Transaction Processing Pipeline Specification

Status: Candidate generated output for run `20260618-003908-write-spec-python-replacement`

Selected stack: `python`

## High-Level Objective

Build a deterministic Python transaction-processing pipeline that reads synthetic transactions from `sample-transactions.json`, validates and risk-scores each transaction through stack-native JSON-message agents, settles acceptable transactions, and writes audit-safe results and summaries for educational review.

## Scope Boundary

This specification describes the Generated Transaction System Layer only. Runtime transaction pipeline agents are Python application modules with bounded responsibilities and a shared message contract. They are not Claude, Codex, or other assistant skills.

The system is an educational simulation. It must not claim real banking, AML, sanctions, KYC, payment-network, PCI, legal, regulatory, fraud-model, or production compliance. It does not move money, contact payment networks, screen sanctions lists, verify identities, issue refunds, reverse transactions, or create real settlement instructions.

The specification may define result shapes future status tools can read. It does not require MCP configuration setup, slash-command setup, hook setup, screenshot capture, PR packaging, or Homework Automation Layer run-preservation mechanics as product features.

## Mid-Level Objectives

| ID | Objective | Observable success |
|---|---|---|
| M1 | Establish the Python pipeline structure and JSON file protocol. | The product contains `integrator.py`, `agents/common.py`, `agents/transaction_validator.py`, `agents/fraud_detector.py`, and `agents/settlement_processor.py`; runtime execution prepares `shared/input`, `shared/processing`, `shared/output`, and `shared/results`; each runtime component accepts and returns a JSON-serializable message envelope through `process_message(message: dict) -> dict`. |
| M2 | Validate transaction data with precise money, currency, and required-field rules. | Validation parses amounts from strings into `decimal.Decimal`, rejects unsupported currencies outside the sample allowlist `USD`, `EUR`, and `GBP`, rejects negative or malformed amounts, avoids binary floating point, serializes money back as strings, and returns structured rejection reasons without exposing plaintext sensitive account data. |
| M3 | Produce deterministic fraud and review signals without overstating compliance. | The Fraud Detector assigns transparent educational risk signals for high-value, very-high-value, odd-hour, and other configured sample-rule conditions; `TXN002` and `TXN005` receive high-value or very-high-value review signals; `TXN004` receives an odd-hour signal; all signals are framed as simulation outputs rather than legal, AML, sanctions, payment-network, or banking compliance determinations. |
| M4 | Orchestrate validation, risk scoring, settlement, and final result writing for every input transaction. | `main`, `load_transactions`, `prepare_shared_directories`, `build_message_envelope`, `process_transaction`, `write_result`, and `summarize_results` coordinate the runtime flow; every one of the eight sample transactions is represented in `shared/results`; invalid transactions are rejected before settlement; valid transactions receive settlement outcomes plus any review signals. |
| M5 | Preserve audit-safe observability, testability, and future result-readiness. | `redact_account_id` and `audit_event` produce structured audit records with timestamp, runtime component name, transaction ID, safe outcome, and reason code while masking account identifiers; JSON output uses safe serialization such as `allow_nan=False`; tests isolate filesystem state with `pytest tmp_path`; generated result and summary shapes are stable enough for later result-reading tools; the temporary specification-stage coverage target is 75%. |

## Runtime Components

| Component | File | Responsibility | Interface |
|---|---|---|---|
| Integrator | `integrator.py` | Load samples, reset directories, build message envelopes, call runtime components in order, write stage files, write final results, and summarize the run. | `main()`, `process_transaction(transaction: dict, directories: dict[str, str]) -> dict` |
| Common utilities | `agents/common.py` | Decimal parsing, strict JSON helpers, redaction, audit-event creation, timestamps, and reason-code constants. | `parse_amount`, `redact_account_id`, `audit_event`, `write_json_file` |
| Transaction Validator | `agents/transaction_validator.py` | Validate required fields, amount strings, supported currency codes, and safe transaction shape. | `process_message(message: dict) -> dict`, `validate_transaction(transaction: dict) -> dict` |
| Fraud Detector | `agents/fraud_detector.py` | Apply deterministic educational risk signals from amount, timestamp, type, channel, and country. | `process_message(message: dict) -> dict`, `score_fraud_risk(transaction: dict, validation: dict) -> dict` |
| Settlement Processor | `agents/settlement_processor.py` | Convert validation and risk decisions into `settled`, `rejected`, `review_required`, or `error` outcomes. | `process_message(message: dict) -> dict`, `settle_transaction(message: dict) -> dict` |

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
shared/
  input/
  processing/
  output/
  results/
mcp/
  server.py
```

`mcp/server.py` is a later product status reader after result file shapes exist. Do not add `pipeline-status` to MCP configuration files until that server exists and can read current result files.

### Message Envelope

Every component receives and returns a JSON-serializable envelope:

```json
{
  "message_id": "uuid4-string",
  "schema_version": "1.0",
  "created_at": "2026-03-16T10:00:00Z",
  "source_agent": "integrator",
  "target_agent": "transaction_validator",
  "message_type": "transaction",
  "transaction_id": "TXN001",
  "data": {
    "amount": "1500.00",
    "currency": "USD",
    "status": "received"
  }
}
```

Components may add `validation`, `risk`, `settlement`, `audit_events`, `errors`, and `component_history`. Intermediate and final messages should not contain raw account IDs or raw descriptions after validation unless a component is using the original transaction internally and writes only redacted output.

### File-Based Protocol

Runtime files move through:

```text
shared/input
shared/processing
shared/output
shared/results
```

Use deterministic file names:

- `shared/input/<transaction_id>.json`
- `shared/processing/<transaction_id>-<component>.json`
- `shared/output/<transaction_id>-<component>.json`
- `shared/results/<transaction_id>.json`
- `shared/results/summary.json`
- `shared/results/audit.json`
- `shared/results/pipeline-status.json`

The integrator should clear prior generated files at the beginning of a normal run so reruns do not duplicate stale results. Tests must use temporary directories rather than relying on the real `shared/` directory.

### Money And Currency

- Parse transaction amounts from JSON strings into `decimal.Decimal`.
- Never use binary floating point for transaction amounts, risk thresholds, summaries, or tests.
- Reject missing, malformed, non-finite, zero, and negative amounts.
- Serialize validated amounts back to JSON as strings.
- Validate uppercase three-letter ISO 4217-style currency codes against an explicit allowlist containing at least `USD`, `EUR`, and `GBP`.
- Reject `XYZ` with reason code `UNSUPPORTED_CURRENCY`.

### Risk Review

Risk scoring is deterministic homework logic, not a predictive model or compliance conclusion.

Recommended stable signals:

| Signal | Rule | Reason code |
|---|---|---|
| High value | `amount >= Decimal("10000.00")` | `HIGH_VALUE` |
| Very high value | `amount >= Decimal("50000.00")` | `VERY_HIGH_VALUE` |
| Wire transfer | `transaction_type == "wire_transfer"` | `WIRE_TRANSFER` |
| Odd hour | UTC time from `00:00` through `04:59` | `ODD_HOUR_ACTIVITY` |

Very-high-value transactions may include both `VERY_HIGH_VALUE` and `HIGH_VALUE` if the scoring model documents cumulative signals. The final outcome should be deterministic and testable from the sample data.

### Audit, Logging, And Privacy

Audit events should include:

- ISO 8601 timestamp.
- Runtime component name.
- Transaction ID.
- Safe outcome.
- Stable reason code.
- Optional risk level or redacted account reference.

Centralize redaction in `agents.common.redact_account_id(account_id: str | None) -> str`. Example redaction: `ACC-1001` becomes `ACC-****1001`.

Logs, audit events, results, docs, tests, and screenshots must not expose plaintext full account IDs, raw descriptions, credentials, tokens, authorization headers, secrets, or unfiltered metadata dumps.

### JSON Safety

Use the Python standard `json` module. Catch `json.JSONDecodeError` for malformed JSON and produce safe per-transaction errors where possible. Convert `Decimal` values to strings before writing JSON. Write strict result files with `allow_nan=False`.

### Canonical Vocabulary

Use one status and reason-code contract across code, tests, result files, and status readers.

Final statuses:

- `settled`
- `rejected`
- `review_required`
- `error`

Stable reason codes:

- `MISSING_FIELD`
- `INVALID_AMOUNT`
- `NON_POSITIVE_AMOUNT`
- `UNSUPPORTED_CURRENCY`
- `HIGH_VALUE`
- `VERY_HIGH_VALUE`
- `WIRE_TRANSFER`
- `ODD_HOUR_ACTIVITY`
- `REMOTE_CHANNEL`
- `CROSS_COUNTRY_REVIEW_SIGNAL`
- `INVALID_TIMESTAMP_FOR_SCORING`
- `MALFORMED_JSON`
- `PIPELINE_ERROR`

Canonical per-transaction result files use `shared/results/<transaction_id>.json`, such as `shared/results/TXN001.json`.

### Result Shapes For Future Status Readers

`shared/results/<transaction_id>.json` should include:

- `schema_version`
- `transaction_id`
- `status`
- `reason_codes`
- `risk_score`
- `risk_level`
- `amount`
- `currency`
- `component_history`
- `audit_event_ids` or safe inline audit events

`shared/results/pipeline-status.json` should include:

- `schema_version`
- `total_transactions`
- `settled`
- `rejected`
- `review_required`
- `error`
- `generated_at`

`shared/results/summary.json` should include the count fields plus safe per-transaction summaries.

Future `mcp/server.py` can expose `get_transaction_status(transaction_id: str)`, `list_pipeline_results()`, and resource `pipeline://summary` by reading these files without rerunning the pipeline.

### Testing And Coverage

Use `pytest` and temporary directories. The temporary specification-stage coverage target is 75% using a non-blocking coverage report command such as:

```powershell
python -m pytest
python -m pytest --cov=.
```

Later test-generation work owns raising enforced coverage above 80% and adding the blocking coverage hook. This product spec should prepare test seams but should not make hook setup a pipeline product feature.

## Context

### Beginning Context

The homework root contains `sample-transactions.json` with eight synthetic transaction records:

| Transaction | Beginning-state facts |
|---|---|
| `TXN001` | USD online transfer for `1500.00`. |
| `TXN002` | USD branch wire transfer for `25000.00`. |
| `TXN003` | USD online transfer for `9999.99`. |
| `TXN004` | EUR API transfer at `02:47:00Z`, country `DE`. |
| `TXN005` | USD branch wire transfer for `75000.00`. |
| `TXN006` | USD-like transfer shape but unsupported currency `XYZ`. |
| `TXN007` | GBP refund with negative amount `-100.00`. |
| `TXN008` | USD mobile transfer for `3200.00`. |

No generated transaction pipeline code, runtime agent modules, product tests, custom MCP status reader, or runtime `shared/` results are assumed at the start.

### Ending Context

After the low-level tasks are complete:

- `python integrator.py` processes all eight sample transactions.
- `shared/results/summary.json` accounts for every sample transaction exactly once.
- `shared/results/pipeline-status.json` reports `total_transactions`, `settled`, `rejected`, `review_required`, `error`, and `generated_at`.
- `shared/results/audit.json` contains structured, audit-safe events with redacted account references only.
- `TXN006` is rejected with `UNSUPPORTED_CURRENCY`.
- `TXN007` is rejected with `NON_POSITIVE_AMOUNT`.
- `TXN002` receives a high-value review signal.
- `TXN005` receives a very-high-value review signal.
- `TXN004` receives an odd-hour review signal.
- `python -m pytest` passes.
- `python -m pytest --cov=.` reports at least 75% coverage for the temporary specification-stage target.
- Future result-reading tools can read final result files without rerunning the pipeline.

## Low-Level Tasks

### Task 1: Create Python Package Skeleton And Shared Directory Preparation

**Prompt:** Create the base Python project structure for the educational transaction-processing pipeline. Add package files for runtime pipeline agents and implement deterministic setup of the shared JSON protocol directories.

**File to CREATE/UPDATE:** `integrator.py`, `agents/__init__.py`, `agents/common.py`

**Function to CREATE:** `prepare_shared_directories(base_dir: Path, reset: bool = True) -> dict[str, Path]`

**Details:** Create `shared/input`, `shared/processing`, `shared/output`, and `shared/results` under a caller-provided base directory. When `reset=True`, remove generated JSON files from those protocol directories without touching unrelated files outside the supplied base.

**Edge cases:** Existing old JSON files, missing directories, temporary test base directories, and non-JSON files that must not be removed.

**Acceptance criteria:** `agents` imports cleanly; all four directories are created; repeated reset runs are deterministic; files outside the supplied base are untouched.

**Verification:** `python -m pytest tests/test_shared_directories.py`

### Task 2: Implement Common Decimal, Redaction, Audit, And Safe JSON Utilities

**Prompt:** Implement shared utility functions for precise money parsing, account redaction, audit event construction, and safe JSON writing. Enforce that money is never parsed through binary floating point and JSON output rejects NaN or Infinity.

**File to CREATE/UPDATE:** `agents/common.py`

**Function to CREATE:** `parse_amount(value: str) -> Decimal`, `redact_account_id(account_id: str | None) -> str`, `audit_event(agent_name: str, transaction_id: str, outcome: str, reason_code: str | None = None) -> dict`, `write_json_file(path: Path, payload: dict) -> None`

**Details:** Use `Decimal` from string input only. Reject missing, non-string, malformed, zero, non-finite, and negative monetary values with safe reason codes. Serialize money values as strings. Redact `ACC-1001` as `ACC-****1001`; short or malformed identifiers return a safe placeholder. Audit events include UTC timestamp, component name, transaction ID, outcome, and optional reason code. JSON writing uses deterministic indentation, sorted keys, and `allow_nan=False`.

**Edge cases:** `25000.00` and `75000.00` remain exact; `-100.00` rejects; nonnumeric text rejects safely; `None`, empty, short, and normal account IDs never leak raw values; NaN payloads fail to write.

**Acceptance criteria:** No `float()` for transaction amounts; all output money is string-formatted; audit records are structured and PII-safe; unsafe JSON values are rejected.

**Verification:** `python -m pytest tests/test_common.py`

### Task 3: Load And Normalize Raw Transactions

**Prompt:** Implement input loading for `sample-transactions.json` with safe error handling and deterministic raw-record normalization.

**File to CREATE/UPDATE:** `integrator.py`

**Function to CREATE:** `load_transactions(input_path: Path) -> list[dict]`

**Details:** Read JSON from a supplied path, validate that the top-level value is a list of dictionaries, return copied records, and do not log raw descriptions, account identifiers, or metadata.

**Edge cases:** Missing file, malformed JSON, top-level object instead of list, non-object list entries, and empty list.

**Acceptance criteria:** All eight sample records load; loader errors expose safe reason codes; the function works with `tmp_path` fixtures.

**Verification:** `python -m pytest tests/test_load_transactions.py`

### Task 4: Build The Shared JSON Message Envelope

**Prompt:** Implement the standard JSON message envelope used by all runtime pipeline agents. The envelope must carry sanitized transaction data, routing metadata, and traceable message identifiers without leaking sensitive descriptions or raw account identifiers.

**File to CREATE/UPDATE:** `integrator.py`, `agents/common.py`

**Function to CREATE:** `build_message_envelope(transaction: dict, source_agent: str, target_agent: str, message_type: str = "transaction") -> dict`

**Details:** Use UUID4 and UTC timestamp fields. Preserve only transaction ID, amount, currency, transaction type, timestamp, channel, and country. Convert source and destination accounts to redacted fields. Omit raw `description`, raw `source_account`, and raw `destination_account`.

**Edge cases:** Missing optional metadata, missing channel/country, missing transaction ID that validator must catch, and sample descriptions that must never appear in envelope files.

**Acceptance criteria:** Envelope has routing metadata and sanitized data only; it is JSON-serializable with `allow_nan=False`; account identifiers appear only in redacted form.

**Verification:** `python -m pytest tests/test_message_envelope.py`

### Task 5: Implement Transaction Validator Runtime Agent

**Prompt:** Create the Transaction Validator as a stack-native Python runtime pipeline agent. It must validate required fields, parse positive Decimal amounts, enforce supported currencies, and emit safe rejected or validated messages.

**File to CREATE/UPDATE:** `agents/transaction_validator.py`

**Function to CREATE:** `process_message(message: dict) -> dict`, `validate_transaction(transaction_data: dict) -> dict`

**Details:** Require `transaction_id`, `timestamp`, `amount`, `currency`, and `transaction_type`. Normalize supported currencies to uppercase and allow at least `USD`, `EUR`, and `GBP`; reject `XYZ`. Return `is_valid`, `status`, `reason_code`, normalized `amount`, and normalized `currency`. Valid records route to `fraud_detector`; invalid records become rejected final candidates and do not continue to scoring.

**Edge cases:** `TXN006` unsupported `XYZ`, `TXN007` negative `-100.00`, missing required fields, extra whitespace in amount, and lowercase currency handling.

**Acceptance criteria:** `TXN006` rejects with `UNSUPPORTED_CURRENCY`; `TXN007` rejects with `NON_POSITIVE_AMOUNT`; valid records leave with amount serialized as a string; audit events contain no raw accounts or descriptions.

**Verification:** `python -m pytest tests/test_transaction_validator.py`

### Task 6: Implement Fraud Detector Runtime Agent

**Prompt:** Create the Fraud Detector as a deterministic educational risk-scoring component. It must assign risk scores and reason codes from transparent heuristics, then route low-risk transactions toward settlement and high-risk transactions toward review.

**File to CREATE/UPDATE:** `agents/fraud_detector.py`

**Function to CREATE:** `process_message(message: dict) -> dict`, `score_fraud_risk(transaction_data: dict) -> dict`

**Details:** Use Decimal comparisons. Add `HIGH_VALUE` for `amount >= 10000.00`, `VERY_HIGH_VALUE` for `amount >= 50000.00`, `ODD_HOUR_ACTIVITY` for UTC times between `00:00:00Z` and `04:59:59Z`, `WIRE_TRANSFER` for wire transfers, `REMOTE_CHANNEL` for `api` or `mobile`, and optional `CROSS_COUNTRY_REVIEW_SIGNAL` for non-US country. Scores below the documented threshold route as `approved_for_settlement`; scores at or above it route as `review_required`.

**Edge cases:** `TXN002` high-value wire; `TXN005` very-high-value wire; `TXN004` odd-hour API/DE transfer; invalid transactions should not be scored; malformed timestamp produces a safe review reason.

**Acceptance criteria:** Scoring is deterministic; risk reasons are explicit and safe; no random, network, or model calls are used; high-value and odd-hour samples are covered by tests.

**Verification:** `python -m pytest tests/test_fraud_detector.py`

### Task 7: Implement Settlement Processor Runtime Agent

**Prompt:** Create the Settlement Processor as the final runtime pipeline agent. It must produce settled or review-required outcomes without performing real money movement.

**File to CREATE/UPDATE:** `agents/settlement_processor.py`

**Function to CREATE:** `process_message(message: dict) -> dict`, `settle_transaction(transaction_data: dict) -> dict`

**Details:** For `approved_for_settlement`, produce final status `settled` with a simulated reference such as `SIM-TXN001`. For `review_required`, produce final status `review_required` and no settlement reference. Preserve `rejected` records from validation. Missing fraud decisions fail safely as `error` or `review_required`. Use wording such as `simulated_settlement_ready`.

**Edge cases:** Low-risk `TXN001` and `TXN008` settled; review-required `TXN002`, `TXN004`, and `TXN005` have no settlement reference; rejected records remain rejected.

**Acceptance criteria:** Final outcomes are deterministic; settled records include a simulated settlement reference; review-required records include risk reasons and no settlement reference; payloads contain no raw descriptions or raw account IDs.

**Verification:** `python -m pytest tests/test_settlement_processor.py`

### Task 8: Orchestrate One Transaction Through The File Protocol

**Prompt:** Implement per-transaction orchestration in the integrator. It should write the initial envelope to `shared/input`, pass the message through validator, fraud detector, and settlement processor, write intermediate JSON protocol files, and recover safely from per-transaction failures.

**File to CREATE/UPDATE:** `integrator.py`

**Function to CREATE:** `process_transaction(transaction: dict, directories: dict[str, Path]) -> dict`

**Details:** Build a validator-targeted envelope, write `shared/input/<transaction_id>.json`, write processing-stage files, call the validator, stop rejected records after validation, call fraud detector for valid records, call settlement processor, and return the final result. Convert component exceptions into safe result records with status `error`, reason code `PIPELINE_ERROR`, and audit event while continuing later transactions.

**Edge cases:** Validator rejects `TXN006`/`TXN007`; high-risk records are review-required; malformed single records do not abort the whole run; reruns do not mix old files with new files.

**Acceptance criteria:** Each transaction produces traceable JSON protocol files; rejected transactions stop after validation; valid transactions pass through all three runtime agents; per-transaction exceptions become safe results.

**Verification:** `python -m pytest tests/test_process_transaction.py`

### Task 9: Write Final Result Files And Summary Report

**Prompt:** Implement final result writing and summary aggregation for all processed transactions. Result shapes must be stable and safe for future read-only status tools.

**File to CREATE/UPDATE:** `integrator.py`

**Function to CREATE:** `write_result(result: dict, results_dir: Path) -> Path`, `summarize_results(results: list[dict], results_dir: Path) -> dict`

**Details:** Write one result file per transaction such as `shared/results/TXN001.json` with `transaction_id`, `status`, `amount`, `currency`, `transaction_type`, `risk_score`, `risk_reasons`, `reason_code`, optional `settlement_reference`, `audit`, and `generated_at`. Write `summary.json` with total, settled, rejected, review-required, error counts, sorted result files, generated timestamp, and simulation notice.

**Edge cases:** Empty results produce zero counts; unknown final status counts as `error` or fails safely; duplicate transaction IDs are detected or overwritten deterministically with a safe warning; results exclude raw descriptions and raw accounts.

**Acceptance criteria:** All eight samples are accounted for; `TXN006`/`TXN007` reject; high-risk valid records are review-required; result shapes support future `get_transaction_status`, `list_pipeline_results`, and `pipeline://summary`.

**Verification:** `python -m pytest tests/test_results_summary.py`

### Task 10: Implement Main Entry Point And Validator Dry-Run Seam

**Prompt:** Implement the pipeline command entry point in `integrator.py`. It should run the full pipeline by default and support a validation-only mode suitable for a future `/validate-transactions` workflow.

**File to CREATE/UPDATE:** `integrator.py`

**Function to CREATE:** `main(argv: list[str] | None = None) -> int`

**Details:** Support `python integrator.py`, `python integrator.py --input sample-transactions.json --shared-dir shared`, and `python integrator.py --validate-only --input sample-transactions.json --shared-dir shared`. Full mode prepares directories, loads transactions, processes all records, writes results and summary, prints safe aggregate status, returns `0` when the run completes with business-rule rejections, and returns nonzero for unreadable input or unrecoverable setup. Validation-only mode runs only the shared validator logic and does not call risk or settlement.

**Edge cases:** Malformed input returns nonzero with safe text; validation-only identifies `TXN006` and `TXN007`; full mode continues through all eight samples; console output avoids raw descriptions and account IDs.

**Acceptance criteria:** `python integrator.py` creates `shared/results/summary.json`; validation-only mode does not produce simulated settlements; CLI works with custom `tmp_path` paths; output is audit-safe.

**Verification:** `python integrator.py --input sample-transactions.json --shared-dir shared`; `python integrator.py --validate-only --input sample-transactions.json --shared-dir shared`; `python -m pytest tests/test_integrator_cli.py`

### Task 11: Add Unit Tests For Common Utilities And Runtime Agents

**Prompt:** Create focused pytest unit tests for common utilities, validation, fraud scoring, and settlement behavior. Tests must use deterministic sample fragments and avoid writing to the real shared directories.

**File to CREATE/UPDATE:** `tests/test_common.py`, `tests/test_transaction_validator.py`, `tests/test_fraud_detector.py`, `tests/test_settlement_processor.py`

**Function to CREATE:** `test_parse_amount_rejects_negative_values`, `test_redact_account_id_masks_sample_account`, `test_validator_rejects_unsupported_currency`, `test_validator_rejects_negative_amount`, `test_fraud_detector_flags_high_value_and_odd_hour`, `test_settlement_processor_settles_low_risk_transaction`

**Details:** Use `pytest` and small dictionaries. Cover `TXN006`, `TXN007`, `TXN002`, `TXN005`, `TXN004`, and at least one low-risk settled transaction. Assert Decimal comparisons do not coerce to float and result payloads contain no raw descriptions.

**Edge cases:** Decimal exactness, audit fields, redaction assertions, and no real shared-directory writes.

**Acceptance criteria:** Unit tests cover required helper and agent functions; tests are deterministic and independent; no test writes to real `shared/`.

**Verification:** `python -m pytest tests/test_common.py tests/test_transaction_validator.py tests/test_fraud_detector.py tests/test_settlement_processor.py`

### Task 12: Add End-To-End Pipeline Tests With Temporary Filesystem State

**Prompt:** Create integration tests that run the full Python pipeline against the sample transaction file using `tmp_path`, then verify result counts, per-transaction statuses, and privacy-safe output.

**File to CREATE/UPDATE:** `tests/test_pipeline_end_to_end.py`

**Function to CREATE:** `test_full_pipeline_processes_all_sample_transactions`, `test_validate_only_reports_invalid_samples_without_settlement`, `test_pipeline_outputs_do_not_leak_sensitive_fields`

**Details:** Use a temporary input path and `tmp_path / "shared"`. Assert exactly eight transactions, `summary.json`, `TXN006` unsupported currency, `TXN007` non-positive amount rejection, `TXN002`/`TXN005` high-value review, `TXN004` odd-hour reason, at least one settled transaction, and no raw descriptions or raw `ACC-1001` style identifiers in serialized outputs.

**Edge cases:** Rerun in the same temp shared directory; deterministic sorted outputs; business rejections do not fail the process; malformed JSON fixture returns safe failure.

**Acceptance criteria:** End-to-end tests prove all samples have final outcomes; filesystem state is isolated; privacy is verified from serialized JSON text; temporary specification-stage coverage can be checked at or above 75%.

**Verification:** `python -m pytest`; `python -m pytest --cov=. --cov-fail-under=75`

### Task 13: Add Read-Only MCP Result Server After Result Shapes Exist

**Prompt:** After result files and summary shapes are implemented, create a read-only Python MCP server that exposes pipeline results from `shared/results/` without changing MCP configuration files.

**File to CREATE/UPDATE:** `mcp/server.py`, `mcp/__init__.py`

**Function to CREATE:** `get_transaction_status(transaction_id: str) -> dict`, `list_pipeline_results() -> list[dict]`, `read_pipeline_summary() -> dict`

**Details:** Read `shared/results/<transaction_id>.json`, list sorted per-transaction JSON result files while excluding `summary.json`, `audit.json`, and `pipeline-status.json`, and read `summary.json`. If FastMCP is available, expose these helpers as tools/resources. If unavailable, keep pure helper functions testable and record the dependency limitation. Do not edit `mcp.json` or `.codex/config.toml` in this product task.

**Edge cases:** Missing results directory, missing result file, malformed result JSON, raw sensitive data in result files, and accidental mutation/rerun.

**Acceptance criteria:** Helper functions read Task 9 result shapes; no MCP configuration setup is included; missing or malformed files produce safe responses; read-only behavior is covered by tests.

**Verification:** `python -m pytest tests/test_mcp_server.py`

## Verification Mapping

| Objective | Evidence |
|---|---|
| M1 | Task 1, Task 4, Task 8, and Task 12 verify package structure, envelopes, shared directories, and end-to-end file movement. |
| M2 | Task 2, Task 5, Task 10, Task 11, and Task 12 verify Decimal parsing, currency validation, required fields, and validation-only behavior. |
| M3 | Task 6, Task 7, Task 11, and Task 12 verify deterministic high-value, very-high-value, wire-transfer, and odd-hour review signals. |
| M4 | Task 3, Task 8, Task 9, Task 10, and Task 12 verify all eight inputs have final results and summaries. |
| M5 | Task 2, Task 9, Task 11, Task 12, and Task 13 verify audit-safe output, temporary-directory testing, coverage target readiness, and read-only result shapes. |

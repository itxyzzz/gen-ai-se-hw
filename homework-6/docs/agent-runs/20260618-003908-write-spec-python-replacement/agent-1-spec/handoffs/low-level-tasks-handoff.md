# Low-Level Tasks Handoff

Run ID: `20260618-003908-write-spec-python-replacement`

Stack: `python`

## 1. Assigned Scope

Produce implementation-ready, product-only task cards for the Python Generated Transaction System Layer. The decomposition targets a deterministic educational transaction-processing pipeline that reads all eight records from `sample-transactions.json`, passes JSON message envelopes through stack-native Python runtime components, writes through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`, validates money and currency with precise `decimal.Decimal` semantics, applies deterministic simulation risk scoring, and produces audit-safe results that future read-only status tools can consume.

Out of scope: Homework Automation Layer tasks, run preservation mechanics, final selection, dev-doc-harness, Superpowers, screenshot evidence, PR packaging, coverage hook setup, and MCP configuration setup.

## 2. Files/Context Inspected

- User-provided task context for run `20260618-003908-write-spec-python-replacement`.
- `agent-control/write-spec/workflow.md`
- `agent-control/write-spec/stack-profiles.md`
- `agent-control/write-spec/quality-bar.md`
- `agent-control/write-spec/run-registry.md`
- `agent-control/write-spec/transaction-system-brief.md`
- `sample-transactions.json`
- `agents.md`
- `agent-1-spec/handoffs/domain-research-handoff.md`
- `agent-1-spec/handoffs/objectives-handoff.md`

## 3. Low-Level Task Cards

### Task 1: Create Python package skeleton and shared directory preparation

**Prompt:** Create the base Python project structure for the educational transaction-processing pipeline. Add package files for runtime pipeline agents and implement deterministic setup of the shared JSON protocol directories.

**File to CREATE/UPDATE:** `integrator.py`, `agents/__init__.py`, `agents/common.py`

**Function to CREATE:** `prepare_shared_directories(base_dir: Path, reset: bool = True) -> dict[str, Path]`

**Details:** Create the `agents` package and protocol directories `shared/input`, `shared/processing`, `shared/output`, and `shared/results` under a caller-provided base directory. When `reset=True`, remove generated JSON files from those four protocol directories without touching unrelated parent files.

**Edge cases:** Existing old JSON files, missing protocol directories, temporary test base directories, and non-JSON files that must not be removed.

**Acceptance criteria:** `agents` imports cleanly; all four protocol directories are created; repeated reset runs are deterministic; files outside the supplied base directory are untouched.

**Verification:** `python -m pytest tests/test_shared_directories.py`

### Task 2: Implement common Decimal, redaction, audit, and safe JSON utilities

**Prompt:** Implement shared utility functions for precise money parsing, account redaction, audit event construction, and safe JSON writing. Enforce that money is never parsed through binary floating point and JSON output rejects NaN or Infinity.

**File to CREATE/UPDATE:** `agents/common.py`

**Function to CREATE:** `parse_amount(value: str) -> Decimal`, `redact_account_id(account_id: str | None) -> str`, `audit_event(agent_name: str, transaction_id: str, outcome: str, reason_code: str | None = None) -> dict`, `write_json_file(path: Path, payload: dict) -> None`

**Details:** Use `Decimal` from string input only. Reject missing, non-string, malformed, zero, non-finite, and negative monetary values with safe reason codes. Serialize money values as strings. Redact `ACC-1001` as `ACC-****1001`; for short or malformed identifiers, return a safe placeholder. Create audit events with UTC timestamp, component name, transaction ID, outcome, and optional reason code. Write JSON with deterministic indentation, sorted keys, and `allow_nan=False`.

**Edge cases:** `25000.00` and `75000.00` remain exact; `-100.00` rejects; nonnumeric text rejects safely; `None`, empty, short, and normal account IDs never leak raw values; NaN payloads fail to write.

**Acceptance criteria:** No `float()` for transaction amounts; all output money is string-formatted; audit records are structured and PII-safe; unsafe JSON values are rejected.

**Verification:** `python -m pytest tests/test_common.py`

### Task 3: Load and normalize raw transactions

**Prompt:** Implement input loading for `sample-transactions.json` with safe error handling and deterministic raw-record normalization.

**File to CREATE/UPDATE:** `integrator.py`

**Function to CREATE:** `load_transactions(input_path: Path) -> list[dict]`

**Details:** Read JSON from a supplied path, validate that the top-level value is a list of dictionaries, return copied records, and do not log raw descriptions, account identifiers, or metadata.

**Edge cases:** Missing file, malformed JSON, top-level object instead of list, non-object list entries, and empty list.

**Acceptance criteria:** All eight sample records load; loader errors expose safe reason codes; the function works with `tmp_path` fixtures.

**Verification:** `python -m pytest tests/test_load_transactions.py`

### Task 4: Build the shared JSON message envelope

**Prompt:** Implement the standard JSON message envelope used by all runtime pipeline agents. The envelope must carry sanitized transaction data, routing metadata, and traceable message identifiers without leaking sensitive descriptions or raw account identifiers.

**File to CREATE/UPDATE:** `integrator.py`, `agents/common.py`

**Function to CREATE:** `build_message_envelope(transaction: dict, source_agent: str, target_agent: str, message_type: str = "transaction") -> dict`

**Details:** Use UUID4 and UTC timestamp fields. Preserve transaction ID, amount, currency, transaction type, timestamp, channel, and country. Convert source and destination accounts to redacted fields. Omit raw `description`, raw `source_account`, and raw `destination_account`.

**Edge cases:** Missing optional metadata, missing channel/country, missing transaction ID that validator must catch, and sample descriptions that must never appear in envelope files.

**Acceptance criteria:** Envelope has routing metadata and sanitized data only; it is JSON-serializable with `allow_nan=False`; account identifiers appear only in redacted form.

**Verification:** `python -m pytest tests/test_message_envelope.py`

### Task 5: Implement Transaction Validator runtime agent

**Prompt:** Create the Transaction Validator as a stack-native Python runtime pipeline agent. It must validate required fields, parse positive Decimal amounts, enforce supported currencies, and emit safe rejected or validated messages.

**File to CREATE/UPDATE:** `agents/transaction_validator.py`

**Function to CREATE:** `process_message(message: dict) -> dict`, `validate_transaction(transaction_data: dict) -> dict`

**Details:** Require `transaction_id`, `timestamp`, `amount`, `currency`, and `transaction_type`. Normalize supported currencies to uppercase and allow at least `USD`, `EUR`, and `GBP`; reject `XYZ`. Return `is_valid`, `status`, `reason_code`, normalized `amount`, and normalized `currency`. Valid records route to `fraud_detector`; invalid records become rejected final candidates and do not continue to scoring.

**Edge cases:** `TXN006` unsupported `XYZ`, `TXN007` negative `-100.00`, missing required fields, extra whitespace in amount, and lowercase currency handling.

**Acceptance criteria:** `TXN006` rejects with `UNSUPPORTED_CURRENCY`; `TXN007` rejects with `NON_POSITIVE_AMOUNT`; valid records leave with amount serialized as a string; audit events contain no raw accounts or descriptions.

**Verification:** `python -m pytest tests/test_transaction_validator.py`

### Task 6: Implement Fraud Detector runtime agent

**Prompt:** Create the Fraud Detector as a deterministic educational risk-scoring component. It must assign risk scores and reason codes from transparent heuristics, then route low-risk transactions toward settlement and high-risk transactions toward review.

**File to CREATE/UPDATE:** `agents/fraud_detector.py`

**Function to CREATE:** `process_message(message: dict) -> dict`, `score_fraud_risk(transaction_data: dict) -> dict`

**Details:** Use Decimal comparisons. Add `HIGH_VALUE` for `amount >= 10000.00`, `VERY_HIGH_VALUE` for `amount >= 50000.00`, `ODD_HOUR_ACTIVITY` for UTC times between `00:00:00Z` and `04:59:59Z`, `WIRE_TRANSFER` for wire transfers, `REMOTE_CHANNEL` for `api` or `mobile`, and optional `CROSS_COUNTRY_REVIEW_SIGNAL` for non-US country. Scores below the documented threshold route as `approved_for_settlement`; scores at or above it route as `review_required`.

**Edge cases:** `TXN002` high-value wire; `TXN005` very-high-value wire; `TXN004` odd-hour API/DE transfer; invalid transactions should not be scored; malformed timestamp produces a safe review reason.

**Acceptance criteria:** Scoring is deterministic; risk reasons are explicit and safe; no random, network, or model calls are used; high-value and odd-hour samples are covered by tests.

**Verification:** `python -m pytest tests/test_fraud_detector.py`

### Task 7: Implement Settlement Processor runtime agent

**Prompt:** Create the Settlement Processor as the final runtime pipeline agent. It must produce settled or review-required outcomes without performing real money movement.

**File to CREATE/UPDATE:** `agents/settlement_processor.py`

**Function to CREATE:** `process_message(message: dict) -> dict`, `settle_transaction(transaction_data: dict) -> dict`

**Details:** For `approved_for_settlement`, produce final status `settled` with a simulated reference such as `SIM-TXN001`. For `review_required`, produce final status `review_required` and no settlement reference. Preserve `rejected` records from validation. Missing fraud decisions fail safely as `error` or `review_required`. Use wording such as `simulated_settlement_ready`.

**Edge cases:** Low-risk `TXN001` and `TXN008` settled; review-required `TXN002`, `TXN004`, and `TXN005` have no settlement reference; rejected records remain rejected.

**Acceptance criteria:** Final outcomes are deterministic; settled records include a simulated settlement reference; review-required records include risk reasons and no settlement reference; payloads contain no raw descriptions or raw account IDs.

**Verification:** `python -m pytest tests/test_settlement_processor.py`

### Task 8: Orchestrate one transaction through the file protocol

**Prompt:** Implement per-transaction orchestration in the integrator. It should write the initial envelope to `shared/input`, pass the message through validator, fraud detector, and settlement processor, write intermediate JSON protocol files, and recover safely from per-transaction failures.

**File to CREATE/UPDATE:** `integrator.py`

**Function to CREATE:** `process_transaction(transaction: dict, directories: dict[str, Path]) -> dict`

**Details:** Build a validator-targeted envelope, write `shared/input/<transaction_id>.json`, write processing-stage files, call the validator, stop rejected records after validation, call fraud detector for valid records, call settlement processor, and return the final result. Convert component exceptions into safe result records with status `error`, reason code `PIPELINE_ERROR`, and audit event while continuing later transactions.

**Edge cases:** Validator rejects `TXN006`/`TXN007`; high-risk records are review-required; malformed single records do not abort the whole run; reruns do not mix old files with new files.

**Acceptance criteria:** Each transaction produces traceable JSON protocol files; rejected transactions stop after validation; valid transactions pass through all three runtime agents; per-transaction exceptions become safe results.

**Verification:** `python -m pytest tests/test_process_transaction.py`

### Task 9: Write final result files and summary report

**Prompt:** Implement final result writing and summary aggregation for all processed transactions. Result shapes must be stable and safe for future read-only status tools.

**File to CREATE/UPDATE:** `integrator.py`

**Function to CREATE:** `write_result(result: dict, results_dir: Path) -> Path`, `summarize_results(results: list[dict], results_dir: Path) -> dict`

**Details:** Write one result file per transaction such as `shared/results/TXN001.json` with `transaction_id`, `status`, `amount`, `currency`, `transaction_type`, `risk_score`, `risk_reasons`, `reason_code`, optional `settlement_reference`, `audit`, and `generated_at`. Write `summary.json` with total, settled, rejected, review-required, error counts, sorted result files, generated timestamp, and simulation notice.

**Edge cases:** Empty results produce zero counts; unknown final status counts as `error` or fails safely; duplicate transaction IDs are detected or overwritten deterministically with a safe warning; results exclude raw descriptions and raw accounts.

**Acceptance criteria:** All eight samples are accounted for; `TXN006`/`TXN007` reject; high-risk valid records are review-required; result shapes support future `get_transaction_status`, `list_pipeline_results`, and `pipeline://summary`.

**Verification:** `python -m pytest tests/test_results_summary.py`

### Task 10: Implement main entry point and validator dry-run seam

**Prompt:** Implement the pipeline command entry point in `integrator.py`. It should run the full pipeline by default and support a validation-only mode suitable for a future `/validate-transactions` workflow.

**File to CREATE/UPDATE:** `integrator.py`

**Function to CREATE:** `main(argv: list[str] | None = None) -> int`

**Details:** Support `python integrator.py`, `python integrator.py --input sample-transactions.json --shared-dir shared`, and `python integrator.py --validate-only --input sample-transactions.json --shared-dir shared`. Full mode prepares directories, loads transactions, processes all records, writes results and summary, prints safe aggregate status, returns `0` when the run completes with business-rule rejections, and returns nonzero for unreadable input or unrecoverable setup. Validation-only mode runs only the shared validator logic and does not call risk or settlement.

**Edge cases:** Malformed input returns nonzero with safe text; validation-only identifies `TXN006` and `TXN007`; full mode continues through all eight samples; console output avoids raw descriptions and account IDs.

**Acceptance criteria:** `python integrator.py` creates `shared/results/summary.json`; validation-only mode does not produce simulated settlements; CLI works with custom `tmp_path` paths; output is audit-safe.

**Verification:** `python integrator.py --input sample-transactions.json --shared-dir shared`; `python integrator.py --validate-only --input sample-transactions.json --shared-dir shared`; `python -m pytest tests/test_integrator_cli.py`

### Task 11: Add unit tests for common utilities and runtime agents

**Prompt:** Create focused pytest unit tests for common utilities, validation, fraud scoring, and settlement behavior. Tests must use deterministic sample fragments and avoid writing to the real shared directories.

**File to CREATE/UPDATE:** `tests/test_common.py`, `tests/test_transaction_validator.py`, `tests/test_fraud_detector.py`, `tests/test_settlement_processor.py`

**Function to CREATE:** `test_parse_amount_rejects_negative_values`, `test_redact_account_id_masks_sample_account`, `test_validator_rejects_unsupported_currency`, `test_validator_rejects_negative_amount`, `test_fraud_detector_flags_high_value_and_odd_hour`, `test_settlement_processor_settles_low_risk_transaction`

**Details:** Use `pytest` and small dictionaries. Cover `TXN006`, `TXN007`, `TXN002`, `TXN005`, `TXN004`, and at least one low-risk settled transaction. Assert Decimal comparisons do not coerce to float and result payloads contain no raw descriptions.

**Edge cases:** Decimal exactness, audit fields, redaction assertions, and no real shared-directory writes.

**Acceptance criteria:** Unit tests cover required helper and agent functions; tests are deterministic and independent; no test writes to real `shared/`.

**Verification:** `python -m pytest tests/test_common.py tests/test_transaction_validator.py tests/test_fraud_detector.py tests/test_settlement_processor.py`

### Task 12: Add end-to-end pipeline tests with temporary filesystem state

**Prompt:** Create integration tests that run the full Python pipeline against the sample transaction file using `tmp_path`, then verify result counts, per-transaction statuses, and privacy-safe output.

**File to CREATE/UPDATE:** `tests/test_pipeline_end_to_end.py`

**Function to CREATE:** `test_full_pipeline_processes_all_sample_transactions`, `test_validate_only_reports_invalid_samples_without_settlement`, `test_pipeline_outputs_do_not_leak_sensitive_fields`

**Details:** Use a temporary input path and `tmp_path / "shared"`. Assert exactly eight transactions, `summary.json`, `TXN006` unsupported currency, `TXN007` non-positive amount rejection, `TXN002`/`TXN005` high-value review, `TXN004` odd-hour reason, at least one settled transaction, and no raw descriptions or raw `ACC-1001` style identifiers in serialized outputs.

**Edge cases:** Rerun in the same temp shared directory; deterministic sorted outputs; business rejections do not fail the process; malformed JSON fixture returns safe failure.

**Acceptance criteria:** End-to-end tests prove all samples have final outcomes; filesystem state is isolated; privacy is verified from serialized JSON text; Athena coverage can be checked at or above 75%.

**Verification:** `python -m pytest`; `python -m pytest --cov=. --cov-fail-under=75`

### Task 13: Add read-only MCP result server after result shapes exist

**Prompt:** After result files and summary shapes are implemented, create a read-only Python MCP server that exposes pipeline results from `shared/results/` without changing MCP configuration files.

**File to CREATE/UPDATE:** `mcp/server.py`, `mcp/__init__.py`

**Function to CREATE:** `get_transaction_status(transaction_id: str) -> dict`, `list_pipeline_results() -> list[dict]`, `read_pipeline_summary() -> dict`

**Details:** Read `shared/results/<transaction_id>.json`, list sorted per-transaction JSON result files while excluding `summary.json`, `audit.json`, and `pipeline-status.json`, and read `summary.json`. If FastMCP is available, expose these helpers as tools/resources. If unavailable, keep pure helper functions testable and record the dependency limitation. Do not edit `mcp.json` or `.codex/config.toml` in this product task.

**Edge cases:** Missing results directory, missing result file, malformed result JSON, raw sensitive data in result files, and accidental mutation/rerun.

**Acceptance criteria:** Helper functions read Task 9 result shapes; no MCP configuration setup is included; missing or malformed files produce safe responses; read-only behavior is covered by tests.

**Verification:** `python -m pytest tests/test_mcp_server.py`

## 4. Traceability Matrix

| Task | M1 | M2 | M3 | M4 | M5 |
|---|---:|---:|---:|---:|---:|
| 1. Package skeleton and shared directories | Yes | No | No | Yes | Yes |
| 2. Common utilities | Yes | Yes | No | Yes | Yes |
| 3. Load transactions | Yes | No | No | Yes | Yes |
| 4. Message envelope | Yes | No | No | Yes | Yes |
| 5. Transaction Validator | Yes | Yes | No | Yes | Yes |
| 6. Fraud Detector | Yes | No | Yes | Yes | Yes |
| 7. Settlement Processor | Yes | No | Yes | Yes | Yes |
| 8. Per-transaction orchestration | Yes | Yes | Yes | Yes | Yes |
| 9. Result files and summary | Yes | No | Yes | Yes | Yes |
| 10. Main and validation-only mode | Yes | Yes | No | Yes | Yes |
| 11. Unit tests | No | Yes | Yes | No | Yes |
| 12. End-to-end tests | Yes | Yes | Yes | Yes | Yes |
| 13. Read-only MCP result server | No | No | No | No | Yes |

## 5. Assumptions, Uncertainty, And Residual Risks

- Python is the selected stack.
- The code generator may add helper functions or tests if required function names and product boundaries remain intact.
- `review_required` is an acceptable final state distinct from `settled` and `rejected`.
- Exact risk threshold values are generated design decisions; the integration draft should document one deterministic table.
- FastMCP availability during implementation is not guaranteed; pure read-only helper functions should stay testable without it.
- Duplicate transaction ID behavior is not specified by the sample data; the spec should require deterministic safe handling.
- Result-shape changes after Task 9 require Task 13 and end-to-end tests to change together.
- Privacy checks must inspect serialized output text, where accidental raw-description leakage is easiest to miss.
- Validation-only mode should call the same `validate_transaction` function as the full pipeline.
- Coverage at 75% is Athena's ending-context target only; Themis later raises enforced coverage and adds blocking hooks outside this product decomposition.

## 6. Recommended Next Step For Integration Drafting

Integrate these cards into `agent-1-spec/outputs/specification.md` as the Low-Level Tasks section, then add a short note that result JSON shapes must stabilize before the read-only `mcp/server.py` helpers are created and that MCP configuration setup belongs to a later non-product step.

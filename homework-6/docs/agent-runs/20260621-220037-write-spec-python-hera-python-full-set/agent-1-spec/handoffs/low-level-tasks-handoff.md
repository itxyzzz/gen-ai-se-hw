# Low-Level Task Decomposition Handoff

## Assigned Scope

Low-Level Task Decomposition handoff for Athena (Spec Writer) run `20260621-220037-write-spec-python-hera-python-full-set`.

Scope is limited to implementation-ready Python task cards for the Generated Transaction System Layer. This handoff intentionally excludes dev-doc-harness, Superpowers, preserved run mechanics, final-selection mechanics, screenshots, PR packaging, MCP configuration setup, and Greek Homework Automation agents as runtime components. No files were edited by the sub-agent.

## Files/Context Inspected

- `.agents/skills/write-spec/SKILL.md`
- `agent-control/write-spec/workflow.md`
- `agent-control/write-spec/stack-profiles.md`
- `agent-control/write-spec/quality-bar.md`
- `agent-control/write-spec/run-registry.md`
- `agent-control/write-spec/transaction-system-brief.md`
- `sample-transactions.json`
- `agents.md`
- `../HOMEWORK_STANDARDS.md`
- `../README.md`
- `../AGENTS.md`
- `docs/agent-runs/20260621-220037-write-spec-python-hera-python-full-set/agent-1-spec/handoffs/domain-research-handoff.md`
- `docs/agent-runs/20260621-220037-write-spec-python-hera-python-full-set/agent-1-spec/handoffs/objectives-handoff.md`

## Sources/Commands Used

- `Get-Content -Raw` on the `write-spec` skill, workflow, stack profile, quality bar, registry, transaction brief, sample data, project guide, root standards, root README, root instructions, domain handoff, and objectives handoff.
- `Get-ChildItem -Recurse` on the active run folder to confirm existing handoff artifacts.
- `git status --short` to inspect working state; it showed untracked run folders and a warning that `.pytest_cache/` could not be opened due to permission denial.
- No new web or Context7 research was performed by this sub-agent. External source guidance was consumed through the existing domain-research handoff.

## Proposed Task Cards

Paths below are product-root relative.

### Task 1: Create Python package skeleton and protocol constants

- **Task:** Establish the Python runtime module layout and shared protocol constants.
- **Prompt:** Create the product Python package structure for a deterministic transaction-processing pipeline. Define shared directory names, status values, reason-code constants, component names, and a pipeline version constant without implementing operator-layer workflows.
- **File to CREATE/UPDATE:** `agents/__init__.py`, `agents/common.py`
- **Function to CREATE:** `get_protocol_paths(base_dir: Path) -> dict[str, Path]`
- **Details:** Use `pathlib.Path`. Return paths for `shared`, `input`, `processing`, `output`, `results`, `archive`, and `run_provenance`. Define statuses such as `validated`, `rejected`, `review_required`, `settled`, and `error`. Define reason codes such as `MISSING_FIELD`, `INVALID_AMOUNT`, `NON_POSITIVE_AMOUNT`, `UNSUPPORTED_CURRENCY`, `REVIEW_HIGH_VALUE`, `REVIEW_UNUSUAL_TIME`, `SETTLED`, and `PROCESSING_ERROR`.
- **Edge cases:** Missing base directory, Windows path separators, repeated imports, status typos.
- **Acceptance criteria:** Runtime modules can import constants from `agents.common`; no component name uses Homework Automation identity labels.
- **Verification:** `python -m pytest tests/test_common.py`

### Task 2: Implement JSON envelope, Decimal, audit, and redaction helpers

- **Task:** Centralize safe JSON message handling and privacy helpers.
- **Prompt:** Implement common helpers for message envelopes, precise money parsing, safe Decimal serialization, structured audit events, account redaction, and transaction sanitization. Never use binary floating point.
- **File to CREATE/UPDATE:** `agents/common.py`
- **Function to CREATE:** `parse_amount(value: str) -> Decimal`, `serialize_amount(value: Decimal) -> str`, `create_message(...) -> dict`, `append_audit_event(message: dict, component: str, outcome: str, reason_code: str | None = None) -> dict`, `redact_account_id(value: str) -> str`, `sanitize_transaction(raw: dict) -> dict`
- **Details:** Parse amounts from strings only. Reject non-finite Decimal values. Serialize money as strings. Message envelope includes `message_id`, `timestamp`, `source_agent`, `target_agent`, `message_type`, `data`, `component_history`, and `audit_events`. Audit events include timestamp, component, transaction ID, safe outcome, and optional reason code. Sanitized data must omit raw account IDs, descriptions, and full metadata.
- **Edge cases:** Decimal passed as float, malformed amount string, empty account ID, missing transaction ID, metadata containing sensitive text.
- **Acceptance criteria:** JSON output from helpers is serializable with standard `json.dumps`; audit output contains no raw account IDs or descriptions.
- **Verification:** `python -m pytest tests/test_common.py`

### Task 3: Implement deterministic run reset, archival, and provenance

- **Task:** Prepare fresh runtime directories while preserving prior evidence.
- **Prompt:** Implement run setup that archives an existing `shared/` tree into the next zero-padded `archive/shared-001` style folder before creating a fresh protocol tree and provenance file.
- **File to CREATE/UPDATE:** `integrator.py`
- **Function to CREATE:** `archive_existing_shared(base_dir: Path) -> Path | None`, `prepare_shared_directories(base_dir: Path) -> dict[str, Path]`, `write_run_provenance(paths: dict[str, Path], provenance: dict) -> Path`
- **Details:** If `shared/` exists, move it under `archive/shared-001`, `archive/shared-002`, etc. Then create `shared/input`, `shared/processing`, `shared/output`, and `shared/results`. Write `shared/run-provenance.json` with schema version, runtime run ID, generated timestamp, source spec reference, pipeline version reference, and safe fingerprints only.
- **Edge cases:** Existing archive folders, partial `shared/` tree, read/write failure, provenance values missing, repeated reruns.
- **Acceptance criteria:** Repeated `python integrator.py` runs never overwrite prior `shared/`; provenance contains no raw transactions, account IDs, descriptions, credentials, or prompt text.
- **Verification:** `python -m pytest tests/test_integrator_setup.py`

### Task 4: Load sample input and seed protocol messages

- **Task:** Load `sample-transactions.json` and write initial input messages.
- **Prompt:** Implement input loading and message seeding from `sample-transactions.json` into `shared/input`, preserving all records for processing while keeping logs and written envelopes privacy-safe.
- **File to CREATE/UPDATE:** `integrator.py`
- **Function to CREATE:** `load_transactions(input_path: Path) -> list[dict]`, `seed_input_messages(transactions: list[dict], paths: dict[str, Path]) -> list[Path]`
- **Details:** Read standard JSON. Expect 8 sample records. Write one envelope per transaction to `shared/input/TXN*.json` using safe message helpers. Do not print or log raw account IDs, descriptions, or full metadata.
- **Edge cases:** Missing input file, malformed JSON, duplicate transaction IDs, missing transaction ID, non-list input.
- **Acceptance criteria:** All input records become input message files; malformed file-level input produces a safe, actionable error.
- **Verification:** `python -m pytest tests/test_input_loading.py`

### Task 5: Implement Transaction Validator

- **Task:** Validate transaction structure, amount, currency, and timestamp before downstream processing.
- **Prompt:** Implement a stack-native Transaction Validator component with `process_message(message: dict) -> dict`. It must reject unsafe records before fraud scoring and serialize valid money as Decimal strings.
- **File to CREATE/UPDATE:** `agents/transaction_validator.py`
- **Function to CREATE:** `process_message(message: dict) -> dict`, `validate_transaction(transaction: dict) -> dict`
- **Details:** Required fields: transaction ID, timestamp, source account, destination account, amount, currency, transaction type, and metadata. Parse amount with `Decimal` from string; reject unparseable or non-positive values. Use a bounded ISO 4217-style allow-list of `USD`, `EUR`, and `GBP`; reject `XYZ`. Validate timestamp as ISO 8601-style enough for deterministic processing. Valid output status is `validated`; invalid output status is `rejected` with safe reason codes.
- **Edge cases:** `XYZ`, negative amount, zero amount, blank amount, missing metadata, missing timestamp, malformed timestamp, missing account fields.
- **Acceptance criteria:** Invalid records do not proceed as valid; outputs never expose raw account IDs/descriptions in audit events.
- **Verification:** `python -m pytest tests/test_transaction_validator.py`

### Task 6: Add validator dry-run seam

- **Task:** Support validation-only execution for future dry-run command surfaces.
- **Prompt:** Add a validation-only seam that reads transactions and returns validation results without running fraud scoring, settlement, reporting summaries, or mutating production `shared/` directories unless an explicit temporary base directory is supplied.
- **File to CREATE/UPDATE:** `integrator.py`, `agents/transaction_validator.py`
- **Function to CREATE:** `validate_transactions_only(input_path: Path, base_dir: Path | None = None) -> list[dict]`
- **Details:** Reuse Transaction Validator logic. Return one safe result per input transaction with transaction ID, status, and reason codes. Designed for a future `/validate-transactions` wrapper, but do not implement command/plugin configuration.
- **Edge cases:** Invalid JSON file, no records, mixed valid/invalid records, temporary directory omitted, duplicate transaction IDs.
- **Acceptance criteria:** Dry-run validation can be tested with `tmp_path` and does not require the full pipeline.
- **Verification:** `python -m pytest tests/test_validator_dry_run.py`

### Task 7: Implement Fraud Detector

- **Task:** Score deterministic educational fraud risk.
- **Prompt:** Implement a Fraud Detector component that assigns reproducible risk scores and review reason codes using transparent educational heuristics, without making compliance or real fraud-detection claims.
- **File to CREATE/UPDATE:** `agents/fraud_detector.py`
- **Function to CREATE:** `process_message(message: dict) -> dict`, `score_fraud_risk(transaction: dict) -> dict`
- **Details:** Only process `validated` messages. Suggested deterministic rules: amount `>= 25000.00` adds high-value review reason; timestamp hour before `04:00` adds unusual-time reason; wire transfer can add risk weight; API/mobile/branch channels can be deterministic risk modifiers without exposing raw metadata in output. High-risk messages become `review_required`; ordinary valid messages continue toward settlement.
- **Edge cases:** Already rejected message, missing timestamp after validation bug, high-value TXN002/TXN005, early-hour TXN004, non-USD valid currency, missing metadata channel.
- **Acceptance criteria:** TXN002 and TXN005 are review-required for high value; TXN004 receives an unusual-time risk reason; rejected records stay rejected.
- **Verification:** `python -m pytest tests/test_fraud_detector.py`

### Task 8: Implement Settlement Processor

- **Task:** Settle only transactions that pass validation and risk checks.
- **Prompt:** Implement a Settlement Processor component that marks eligible transactions as settled and preserves rejected or review-required statuses without pretending to perform real banking settlement.
- **File to CREATE/UPDATE:** `agents/settlement_processor.py`
- **Function to CREATE:** `process_message(message: dict) -> dict`, `settle_transaction(transaction: dict, current_status: str) -> dict`
- **Details:** Settle only `validated` records with acceptable risk. Review-required and rejected records must not become settled. Output safe final status, reason code, amount string, currency, transaction ID, component history, and audit event count.
- **Edge cases:** Rejected validation record, review-required fraud record, missing amount after validation bug, duplicate settlement attempt, unsupported status.
- **Acceptance criteria:** Every record receives one final outcome; review-required and rejected records remain non-settled.
- **Verification:** `python -m pytest tests/test_settlement_processor.py`

### Task 9: Implement Reporting Agent result writing and summaries

- **Task:** Produce audit-safe per-transaction and aggregate result artifacts.
- **Prompt:** Implement a Reporting Agent component that writes one safe `shared/results/TXN*.json` result per transaction, `summary.json`, and `pipeline-status.json`, with privacy checks and consistency checks.
- **File to CREATE/UPDATE:** `agents/reporting_agent.py`
- **Function to CREATE:** `process_message(message: dict) -> dict`, `write_transaction_result(message: dict, results_dir: Path) -> Path`, `summarize_results(results_dir: Path, expected_transaction_ids: list[str]) -> dict`, `build_pipeline_status(summary: dict) -> dict`, `assert_privacy_safe(payload: dict) -> None`
- **Details:** Result files include transaction ID, status, reason codes, amount string, currency, component history count, audit event count, and safe processing summary. `summary.json` includes total, settled, rejected, review-required, and error counts. `pipeline-status.json` includes run status, totals, result file names, generated timestamp, and summary location for future read-only status tools.
- **Edge cases:** Missing result file, mismatched counts, raw account ID leaked, description leaked, error status result, duplicate transaction ID.
- **Acceptance criteria:** All 8 records are accounted for; summaries are internally consistent; no plaintext account IDs/descriptions/full metadata appear in result artifacts.
- **Verification:** `python -m pytest tests/test_reporting_agent.py`

### Task 10: Implement Integrator orchestration and file movement

- **Task:** Orchestrate the full runtime pipeline through JSON protocol stages.
- **Prompt:** Implement `integrator.py` so `python integrator.py` prepares directories, loads sample input, sends each message through Transaction Validator, Fraud Detector, Settlement Processor, and Reporting Agent, and verifies every input has a final result.
- **File to CREATE/UPDATE:** `integrator.py`
- **Function to CREATE:** `process_transaction(message_path: Path, paths: dict[str, Path]) -> dict`, `run_pipeline(base_dir: Path = Path("."), input_path: Path = Path("sample-transactions.json")) -> dict`, `main() -> int`
- **Details:** Move/read messages through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`. Use the common `process_message(message: dict) -> dict` callable shape for runtime components. Write final summary after all transactions finish. Return exit code `0` for completed runs with handled rejections/reviews; reserve nonzero for file-level unrecoverable failures.
- **Edge cases:** Component exception, malformed message file, missing input message, duplicate output, partial processing failure, rerun after previous output exists.
- **Acceptance criteria:** Running `python integrator.py` produces `shared/results/summary.json`, `shared/results/pipeline-status.json`, and one safe result file per sample transaction.
- **Verification:** `python integrator.py`; then inspect `shared/results/summary.json` and `shared/results/pipeline-status.json`.

### Task 11: Add per-transaction error handling and failure recovery

- **Task:** Keep the pipeline resilient when one record fails unexpectedly.
- **Prompt:** Add safe per-transaction exception handling so a single malformed message or component failure creates an error result for that transaction without preventing remaining records from being processed.
- **File to CREATE/UPDATE:** `integrator.py`, `agents/reporting_agent.py`
- **Function to CREATE:** `safe_process_transaction(message_path: Path, paths: dict[str, Path]) -> dict`, `write_error_result(transaction_id: str, reason_code: str, results_dir: Path) -> Path`
- **Details:** Catch component exceptions at transaction boundaries. Write safe error result with `PROCESSING_ERROR`, transaction ID when known, no stack trace in JSON result, and audit event count. Preserve enough console output for developer troubleshooting without printing sensitive input payloads.
- **Edge cases:** Unknown transaction ID, JSON decode error, reporting failure, processing directory contains stale file, exception message contains sensitive text.
- **Acceptance criteria:** One bad record produces an error result and does not block other records; summaries include error counts.
- **Verification:** `python -m pytest tests/test_error_recovery.py`

### Task 12: Define MCP-readable product result shapes

- **Task:** Keep result artifacts stable for future read-only status tooling.
- **Prompt:** Standardize result JSON shapes that future status readers can consume without needing product internals or raw transaction data.
- **File to CREATE/UPDATE:** `agents/reporting_agent.py`, `agents/common.py`
- **Function to CREATE:** `build_transaction_status_payload(message: dict) -> dict`, `list_result_files(results_dir: Path) -> list[str]`
- **Details:** Per-transaction status payload includes transaction ID, status, reason codes, amount string, currency, safe summary, component history count, audit event count, and generated timestamp. `pipeline-status.json` includes `schema_version`, `pipeline_version`, `run_status`, `summary_path`, `total_records`, `status_counts`, and `result_files`.
- **Edge cases:** No result files yet, partial run, invalid JSON result file, stale result from archived run accidentally read.
- **Acceptance criteria:** Result shape can support future `get_transaction_status`, `list_pipeline_results`, and `pipeline://summary` readers without exposing raw account IDs/descriptions.
- **Verification:** `python -m pytest tests/test_mcp_readable_shapes.py`

### Task 13: Add isolated pytest fixtures and unit coverage

- **Task:** Build isolated unit tests for common helpers and each runtime component.
- **Prompt:** Create pytest fixtures using `tmp_path` and `monkeypatch` so tests never mutate real `shared/` or `archive/` directories. Cover Decimal handling, validation, fraud scoring, settlement, reporting privacy checks, and error helpers.
- **File to CREATE/UPDATE:** `tests/conftest.py`, `tests/test_common.py`, `tests/test_transaction_validator.py`, `tests/test_fraud_detector.py`, `tests/test_settlement_processor.py`, `tests/test_reporting_agent.py`
- **Function to CREATE:** `isolated_runtime(tmp_path, monkeypatch)`, `test_parse_amount_rejects_float`, `test_serialize_amount_returns_string`, `test_validator_rejects_invalid_currency`, `test_validator_rejects_non_positive_amount`, `test_fraud_detector_flags_high_value`, `test_fraud_detector_flags_unusual_time`, `test_settlement_preserves_review_required`, `test_reporting_privacy_check_rejects_account_ids`
- **Details:** Use temporary directories for filesystem tests. Build sanitized fixture transactions directly in tests or load sample records while avoiding raw sensitive output assertions. Verify no floats are used for money.
- **Edge cases:** Test pollution between runs, accidental writes to repository root, Decimal serialization failure, raw account ID leak in failed assertion output.
- **Acceptance criteria:** Unit tests pass independently and can run repeatedly.
- **Verification:** `python -m pytest`

### Task 14: Add integration, rerun, privacy, and coverage verification

- **Task:** Verify full pipeline behavior, archival, summaries, and temporary coverage target.
- **Prompt:** Create integration tests that run the full pipeline from an isolated temporary directory, verify all 8 records are accounted for, verify prior `shared/` is archived on rerun, and confirm result artifacts are privacy-safe and status counts are consistent.
- **File to CREATE/UPDATE:** `tests/test_pipeline_integration.py`, `tests/test_rerun_archival.py`, `tests/test_privacy_and_result_shapes.py`
- **Function to CREATE:** `test_full_pipeline_processes_all_sample_records`, `test_rerun_archives_existing_shared_tree`, `test_results_do_not_expose_sensitive_fields`, `test_pipeline_status_matches_summary`
- **Details:** Copy or reference `sample-transactions.json` through temporary paths. Expected sample outcomes: 8 total records; `XYZ` invalid currency rejected; non-positive amount rejected; high-value records review-required; early-hour record risk-flagged; ordinary valid records settled. Coverage report command is non-blocking at this stage with a temporary 75% target.
- **Edge cases:** Archive numbering gaps, stale result files, review counts mismatched, invalid record accidentally settled, coverage plugin unavailable.
- **Acceptance criteria:** `python -m pytest` passes; `python -m pytest --cov=.` produces a report when pytest-cov is available; implementation can document temporary 75% coverage target without making it a blocking hook.
- **Verification:** `python -m pytest`; `python -m pytest --cov=.`

## Cross-Objective Traceability

| Objective | Supporting task cards |
|---|---|
| Validate transaction structure, money, and currency before downstream processing | Tasks 2, 4, 5, 6, 13 |
| Score fraud risk using deterministic educational rules without compliance claims | Tasks 7, 13, 14 |
| Settle only transactions that pass validation and risk checks | Tasks 8, 10, 11, 14 |
| Produce audit-safe reporting artifacts for all processed records | Tasks 2, 9, 11, 12, 14 |
| Support repeatable Python runs with isolated tests and traceable runtime state | Tasks 1, 3, 10, 13, 14 |

## Assumptions And Uncertainty

- Accepted currencies for this product spec should be a bounded ISO 4217-style allow-list containing `USD`, `EUR`, and `GBP`; `XYZ` is rejected. The generated product should not claim live ISO registry maintenance.
- Fraud scoring thresholds should stay deterministic and educational. Recommended threshold: amount `>= 25000.00` triggers high-value review; timestamp hour before `04:00` triggers unusual-time review/risk reason.
- High-value transactions should be review-required, not rejected, unless they also fail validation.
- Early-hour activity should be a risk signal, not automatic rejection.
- Transaction IDs such as `TXN002` are treated as safe operational identifiers; raw account IDs, descriptions, and full metadata are not safe for logs, docs examples, summaries, or result payloads.
- The future read-only status tooling can rely on `summary.json`, `pipeline-status.json`, and per-transaction `TXN*.json` files, but this task set does not implement MCP server configuration.

## Residual Risks

- If generated code serializes `Decimal` directly through `json.dumps`, result writing will fail; the common helper tests should catch this.
- If reporting privacy checks only inspect top-level fields, nested sensitive values could leak; `assert_privacy_safe` should recursively scan payloads.
- If file movement is too literal, a component failure could strand messages in `shared/processing`; the integrator should produce an error result and leave enough safe state for debugging.
- If tests run from the repository root without `tmp_path` or `monkeypatch`, they may mutate real runtime evidence.
- If fraud wording drifts toward real compliance claims, the final specification should reject that phrasing during review.

## Recommended Next Step

Integrate these task cards into the candidate `specification.md` low-level tasks section, then run a final review against the Python stack profile and quality bar for product-boundary leakage, privacy wording, task-card executability, result-shape completeness, and test isolation.

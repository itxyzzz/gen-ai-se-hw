## Assigned Scope

Produce implementation-ready Python low-level task cards for Athena (Spec Writer) run `20260619-170102-write-spec-python-fresh`.

Scope is limited to the Generated Transaction System Layer: a Python educational transaction-processing pipeline that processes the eight synthetic records in `sample-transactions.json`, uses stack-native runtime components with `process_message(message: dict) -> dict`, preserves audit-safe JSON evidence under `shared/`, and emits final statuses limited to `settled`, `rejected`, `review_required`, or `error`.

Excluded from these task cards: harness planning, Superpowers, run selection, canonical copy workflow, slash-command setup, hook setup, screenshot capture, PR packaging, and Greek homework automation mechanics.

## Context Inspected

- `.agents/skills/write-spec/SKILL.md`
- `agent-control/write-spec/workflow.md`
- `agent-control/write-spec/stack-profiles.md`
- `agent-control/write-spec/quality-bar.md`
- `agent-control/write-spec/run-registry.md`
- `agent-control/write-spec/transaction-system-brief.md`
- `sample-transactions.json`
- `agents.md`
- `docs/agent-runs/20260619-170102-write-spec-python-fresh/agent-1-spec/handoffs/sub-agent-plan.md`
- `docs/agent-runs/20260619-170102-write-spec-python-fresh/agent-1-spec/handoffs/domain-research-handoff.md`
- `docs/agent-runs/20260619-170102-write-spec-python-fresh/agent-1-spec/handoffs/objectives-handoff.md`

## Sources Or Commands Used

Commands used by the sub-agent included `Get-Content -Raw` on the write-spec workflow package, sample transactions, run handoffs, and `git status --short`. The status command reported the new run folder as untracked and permission warnings for `.pytest_cache`; no canonical files were edited by the sub-agent.

## Task Decomposition Notes

These cards are product implementation slices for Hephaestus (Code Generator), not outer homework automation tasks.

The design assumes a deterministic Python pipeline with:

- `integrator.py`: orchestration, shared directory lifecycle, provenance, file movement, summary writing, and per-transaction failure recovery.
- `agents/common.py`: shared message envelope, Decimal helpers, redaction, audit events, and result-shape helpers.
- `agents/transaction_validator.py`: required fields, amount, currency, timestamp, and dry-run validation.
- `agents/fraud_detector.py`: deterministic educational risk scoring.
- `agents/settlement_processor.py`: simulated final outcome decisions and safe final records.
- Optional `mcp/server.py`: read-only helper layer after result shapes exist, without MCP configuration mechanics.
- `tests/`: pytest coverage with isolated temporary directories and a temporary 75% coverage target.

Suggested deterministic sample outcomes:

- `TXN001`: `settled`.
- `TXN002`: `review_required` due to high-value wire.
- `TXN003`: `review_required` due to near-threshold online amount.
- `TXN004`: `review_required` due to early-hours API and non-US country signals.
- `TXN005`: `review_required` due to very-high-value wire.
- `TXN006`: `rejected` due to `UNSUPPORTED_CURRENCY`.
- `TXN007`: `rejected` due to `NON_POSITIVE_AMOUNT`.
- `TXN008`: `settled`.

Risk thresholds should be documented as educational heuristics, not production fraud or compliance rules.

## Low-Level Task Cards

### 1. Project And Package Structure

**Task:** Create the Python project structure for the transaction-processing pipeline.

**Prompt:** Create the Python package and test skeleton for a deterministic educational transaction-processing pipeline. Keep the runtime product files focused on `integrator.py`, `agents/common.py`, `agents/transaction_validator.py`, `agents/fraud_detector.py`, `agents/settlement_processor.py`, optional future `mcp/server.py`, and pytest tests. Do not implement harness, slash commands, hook setup, screenshot capture, PR packaging, or homework automation agents.

**File to CREATE or UPDATE:** CREATE `agents/__init__.py`, `agents/common.py`, `agents/transaction_validator.py`, `agents/fraud_detector.py`, `agents/settlement_processor.py`, `integrator.py`, `tests/conftest.py`, `tests/test_common.py`, `tests/test_validator.py`, `tests/test_fraud_detector.py`, `tests/test_integrator_pipeline.py`; update or create `pytest.ini` only if needed.

**Function to CREATE:** `integrator.main()`, `agents.transaction_validator.process_message(message: dict) -> dict`, `agents.fraud_detector.process_message(message: dict) -> dict`, `agents.settlement_processor.process_message(message: dict) -> dict`.

**Details:** Use standard-library-first Python modules. Runtime components are stack-native modules, not assistant skills. Each component is callable through `process_message(message: dict) -> dict`.

**Edge cases:** Missing package init, tests importing from temporary directories, accidental dependency on current working directory, and product imports from homework automation artifacts.

**Acceptance criteria:** Runtime modules import cleanly, each component exposes `process_message`, and tests can import modules without global state mutation.

**Verification:** `python -m pytest`; `python -c "import integrator; from agents import transaction_validator, fraud_detector, settlement_processor"`.

### 2. Shared JSON Envelope And Common Helpers

**Task:** Implement shared JSON message envelope helpers and safe data structures.

**Prompt:** In `agents/common.py`, implement the shared message envelope and helpers used by all pipeline components. Envelopes include `message_id`, `timestamp`, `source_agent`, `target_agent`, `message_type`, and `data`. Helpers avoid raw account IDs, descriptions, and metadata in logs, audit events, summaries, and final results.

**File to CREATE or UPDATE:** UPDATE `agents/common.py`.

**Function to CREATE:** `create_message(source_agent: str, target_agent: str, message_type: str, data: dict) -> dict`, `append_component_history(message: dict, component: str, outcome: str, reason_codes: list[str] | None = None) -> dict`, `safe_transaction_view(transaction: dict) -> dict`, `validate_message_envelope(message: dict) -> list[str]`.

**Details:** Generate `message_id` with `uuid.uuid4()`. Generate UTC ISO 8601 timestamps ending in `Z`. Preserve `transaction_id` as the safe correlation key. Safe views may include transaction ID, amount, currency, type, timestamp, channel, and country, but omit `source_account`, `destination_account`, raw `description`, and raw `metadata`.

**Edge cases:** Missing envelope fields, malformed `data`, raw input passthrough, non-JSON-serializable values, and missing component history.

**Acceptance criteria:** Components use one envelope shape; helpers return JSON-serializable dictionaries; sensitive raw fields are absent from safe views.

**Verification:** `python -m pytest tests/test_common.py`.

### 3. Decimal Parsing And Serialization

**Task:** Implement precise money parsing and JSON-safe serialization.

**Prompt:** Implement Decimal money helpers in `agents/common.py`. Parse amounts only from JSON strings into `decimal.Decimal`, never through `float`. Validate two fractional digits for `USD`, `EUR`, and `GBP`. Serialize amounts back to JSON as strings.

**File to CREATE or UPDATE:** UPDATE `agents/common.py`.

**Function to CREATE:** `parse_amount(value: object) -> Decimal`, `serialize_amount(amount: Decimal) -> str`, `validate_money_scale(amount: Decimal, currency: str) -> bool`.

**Details:** Use `Decimal(value)` only when `value` is a string. Reject floats even if numerically valid. Reject non-finite, malformed, missing, zero, and negative values at validation call sites. Keep supported currency scale map as `{"USD": 2, "EUR": 2, "GBP": 2}`.

**Edge cases:** `"1500.0"`, `"1500.000"`, Python float input, `None`, `"NaN"`, `"Infinity"`, `"-100.00"`, `"0.00"`, and whitespace-padded strings.

**Acceptance criteria:** All money comparisons use `Decimal`; JSON result files contain amount strings; tests fail if float input is accepted.

**Verification:** `python -m pytest tests/test_common.py -k amount`.

### 4. Prior-Run Archival And Fresh Shared Tree

**Task:** Implement deterministic reset behavior for repeated runs.

**Prompt:** In `integrator.py`, implement shared directory preparation. Before a new run, if `shared/` exists, move it to `archive/shared-001`, `archive/shared-002`, and so on, using the next available zero-padded archive number. Then create fresh `shared/input`, `shared/processing`, `shared/output`, and `shared/results` directories.

**File to CREATE or UPDATE:** UPDATE `integrator.py`.

**Function to CREATE:** `archive_existing_shared(base_dir: Path) -> Path | None`, `next_archive_path(archive_dir: Path) -> Path`, `prepare_shared_directories(base_dir: Path) -> dict[str, Path]`.

**Details:** Treat `base_dir` as injectable so tests use temporary directories. `archive/` is a sibling of `shared/` under `base_dir`. Never delete prior `shared/` evidence.

**Edge cases:** No prior `shared/`, archive gaps, existing `archive/shared-001`, read-only or partially written shared directories, and rerun after a previous failure.

**Acceptance criteria:** First run creates fresh shared tree; second run archives to `archive/shared-001`; third to `archive/shared-002`; required subdirectories always exist after preparation.

**Verification:** `python -m pytest tests/test_integrator_pipeline.py -k archive`.

### 5. Runtime Provenance

**Task:** Write non-sensitive runtime provenance for each pipeline run.

**Prompt:** In `integrator.py`, write `shared/run-provenance.json` after preparing the shared tree. The file contains only non-sensitive traceability metadata: schema version, runtime run ID, generated timestamp, source spec run ID/path/fingerprint, selected pipeline version reference or unavailable-value field, and code/package fingerprint if available.

**File to CREATE or UPDATE:** UPDATE `integrator.py`.

**Function to CREATE:** `fingerprint_file(path: Path) -> str | None`, `write_run_provenance(shared_dir: Path, *, source_spec_run_id: str, source_spec_path: str | None, pipeline_version: dict | None = None) -> dict`.

**Details:** Use SHA-256 for file fingerprints when files exist. Do not include raw transactions, account identifiers, descriptions, metadata, prompts, conversation text, or credentials.

**Edge cases:** Missing canonical spec path, missing pipeline version details, repeated runs, and failure before processing begins.

**Acceptance criteria:** `shared/run-provenance.json` exists for every run, is valid JSON, contains no transaction records, and moves with `shared/` into archives on later runs.

**Verification:** `python -m pytest tests/test_integrator_pipeline.py -k provenance`; inspect generated `shared/run-provenance.json` after `python integrator.py`.

### 6. Input Loading And Initial Message Files

**Task:** Load `sample-transactions.json` and write deterministic initial input messages.

**Prompt:** In `integrator.py`, implement transaction loading and initial file creation. Load the eight sample transactions, preserve input order, wrap each transaction in the common JSON envelope, and write one message file per transaction under `shared/input`.

**File to CREATE or UPDATE:** UPDATE `integrator.py`.

**Function to CREATE:** `load_transactions(path: Path) -> list[dict]`, `write_input_messages(transactions: list[dict], input_dir: Path) -> list[Path]`.

**Details:** Use deterministic filenames such as `001-TXN001.json`. The input message may include the raw transaction as internal runtime evidence, but audit logs, final results, summaries, and reports must use safe redacted shapes.

**Edge cases:** Missing file, malformed JSON, empty list, duplicate transaction IDs, missing transaction ID, and non-object array items.

**Acceptance criteria:** All eight sample transactions load; eight input message files are created in stable order; duplicate or missing IDs raise controlled errors before processing.

**Verification:** `python -m pytest tests/test_integrator_pipeline.py -k input_loading`.

### 7. Transaction Validator Component

**Task:** Implement required-field, money, currency, and timestamp validation.

**Prompt:** Create `agents/transaction_validator.py` as a stack-native runtime component exposing `process_message(message: dict) -> dict`. Validate required fields, parse amount strings with `Decimal`, reject unsupported currencies, reject zero or negative amounts, validate ISO 8601-style UTC timestamps, and emit safe rejection data without plaintext account IDs, descriptions, or metadata.

**File to CREATE or UPDATE:** UPDATE `agents/transaction_validator.py`.

**Function to CREATE:** `process_message(message: dict) -> dict`, `validate_transaction(transaction: dict) -> tuple[bool, list[str], dict]`, `validate_required_fields(transaction: dict) -> list[str]`, `validate_currency(currency: object) -> str | None`.

**Details:** Required fields: `transaction_id`, `timestamp`, `source_account`, `destination_account`, `amount`, `currency`, `transaction_type`, and `metadata`. Supported currencies: `USD`, `EUR`, `GBP`. `TXN006` rejects with `UNSUPPORTED_CURRENCY`; `TXN007` rejects with `NON_POSITIVE_AMOUNT`.

**Edge cases:** Missing fields, lowercase currency, `XYZ`, invalid amount string, float amount, negative amount, zero amount, malformed timestamp, missing metadata, and malformed envelope.

**Acceptance criteria:** Valid transactions return `status: "validated"` with amount serialized as a string. Invalid transactions return `status: "rejected"` and stable reason codes. Sensitive fields are absent from validation summaries and audit data.

**Verification:** `python -m pytest tests/test_validator.py`.

### 8. Validator Dry-Run Seam

**Task:** Add dry-run validation for future `/validate-transactions` behavior.

**Prompt:** Implement a validator dry-run that reads transactions and reports validation results without running fraud scoring, settlement, output directory movement beyond optional temp output, or final result writing.

**File to CREATE or UPDATE:** UPDATE `agents/transaction_validator.py`; UPDATE `integrator.py` only if a thin wrapper is useful.

**Function to CREATE:** `validate_transactions_dry_run(transactions: list[dict]) -> dict`, optional `integrator.validate_transactions_file(path: Path) -> dict`.

**Details:** Return summary counts and per-transaction validation outcomes with safe reason codes. Do not mutate the real `shared/` tree by default.

**Edge cases:** Malformed input, duplicate IDs, mixed valid/invalid records, missing fields, and all-invalid input.

**Acceptance criteria:** Dry-run over `sample-transactions.json` reports eight records, two rejected validation cases (`TXN006`, `TXN007`), and six valid records before fraud/settlement.

**Verification:** `python -m pytest tests/test_validator.py -k dry_run`.

### 9. Structured Audit Events And Redaction

**Task:** Implement audit-safe structured event helpers.

**Prompt:** In `agents/common.py`, implement structured audit event creation and redaction helpers. Audit events include timestamp, runtime component name, transaction ID, safe outcome, and reason code when applicable. They never include plaintext account IDs, descriptions, or raw metadata.

**File to CREATE or UPDATE:** UPDATE `agents/common.py`; UPDATE runtime components to call audit helpers.

**Function to CREATE:** `redact_account_id(value: str | None) -> str | None`, `create_audit_event(component: str, transaction_id: str | None, outcome: str, reason_codes: list[str] | None = None, details: dict | None = None) -> dict`, `assert_no_sensitive_fields(payload: dict) -> None`.

**Details:** Prefer omitting account IDs entirely from audit. `assert_no_sensitive_fields` recursively rejects keys such as `source_account`, `destination_account`, `description`, and `metadata` before writing audit or final result data.

**Edge cases:** Nested metadata leaks, result payloads containing raw transaction dictionaries, unknown account formats, missing transaction ID, and audit details containing sensitive keys.

**Acceptance criteria:** Every runtime component adds structured audit events. Tests prove sensitive fields are absent from audit events and final results.

**Verification:** `python -m pytest tests/test_common.py -k audit`.

### 10. Fraud Detector Risk Scoring

**Task:** Implement deterministic educational risk scoring.

**Prompt:** Create `agents/fraud_detector.py` with `process_message(message: dict) -> dict`. Score only validated transactions using deterministic educational heuristics: amount thresholds, wire transfer type, early-hours timestamp, API/mobile channel, non-US country, and near-threshold amount. Return `risk_score`, `risk_level`, and reason codes. Do not claim real fraud, AML, sanctions, legal, or payment-network compliance.

**File to CREATE or UPDATE:** UPDATE `agents/fraud_detector.py`.

**Function to CREATE:** `process_message(message: dict) -> dict`, `score_fraud_risk(transaction_data: dict) -> dict`, `classify_risk(score: int) -> str`.

**Details:** Suggested scoring: `+40` for amount >= `50000.00`, `+25` for amount >= `10000.00`, `+15` for amount >= `9000.00`, `+20` for `wire_transfer`, `+15` for hour `00`-`04`, `+10` for `api`, `+5` for `mobile`, `+10` for non-US country. Suggested levels: `low` under 25, `medium` 25-49, `high` 50+.

**Edge cases:** Rejected validation messages bypass risk scoring; boundary amounts `9999.99`, `10000.00`, and `50000.00`; early-hour parsing; and missing metadata.

**Acceptance criteria:** `TXN002`, `TXN003`, `TXN004`, and `TXN005` are elevated for review; `TXN001` is low risk; `TXN008` settles under documented thresholds.

**Verification:** `python -m pytest tests/test_fraud_detector.py`.

### 11. Settlement Processor And Final Outcomes

**Task:** Implement simulated settlement decisions and final status vocabulary.

**Prompt:** Create `agents/settlement_processor.py` with `process_message(message: dict) -> dict`. Preserve validation rejections, convert low-risk validated transactions to `settled`, route elevated-risk transactions to `review_required`, and reserve `error` for processing failures. Do not perform real money movement.

**File to CREATE or UPDATE:** UPDATE `agents/settlement_processor.py`.

**Function to CREATE:** `process_message(message: dict) -> dict`, `decide_final_status(message_data: dict) -> tuple[str, list[str]]`, `build_final_result(message: dict, status: str, reason_codes: list[str]) -> dict`.

**Details:** Allowed final statuses: `settled`, `rejected`, `review_required`, `error`. Settled transactions include `SETTLEMENT_SIMULATED` or equivalent reason code.

**Edge cases:** Rejected messages from validator, missing risk fields, unknown risk level, downstream exception, and accidental final status outside the allowed vocabulary.

**Acceptance criteria:** Every input transaction receives exactly one allowed final status. Final payloads omit account IDs, descriptions, and raw metadata. Settlement language is clearly simulated.

**Verification:** `python -m pytest tests/test_integrator_pipeline.py -k final_status`.

### 12. Integrator Orchestration And File Movement

**Task:** Implement full pipeline orchestration and file movement across `shared/`.

**Prompt:** In `integrator.py`, orchestrate runtime components in order: input message, Transaction Validator, Fraud Detector, Settlement Processor, final result. Move or copy JSON evidence through `shared/input`, `shared/processing`, `shared/output`, and `shared/results` with safe deterministic filenames.

**File to CREATE or UPDATE:** UPDATE `integrator.py`.

**Function to CREATE:** `process_transaction(input_message_path: Path, dirs: dict[str, Path]) -> dict`, `run_pipeline(sample_path: Path, base_dir: Path = Path(".")) -> dict`, `write_stage_message(message: dict, directory: Path, stage: str, sequence: int) -> Path`.

**Details:** `run_pipeline` prepares directories, writes provenance, loads transactions, writes input messages, processes each transaction, and writes summary. Maintain `component_history` and audit events. One transaction failure must not prevent remaining transactions from receiving final outcomes.

**Edge cases:** Malformed input file, component exception, output file collision, partial prior processing state, missing shared subdirectory, and empty transaction list.

**Acceptance criteria:** After `python integrator.py`, `shared/results/` contains eight per-transaction result files and `summary.json`; all original transaction IDs are accounted for.

**Verification:** `python integrator.py`; `python -m pytest tests/test_integrator_pipeline.py -k pipeline`.

### 13. Result Summary And Audit-Safe Reporting

**Task:** Write per-transaction results and a summary report.

**Prompt:** In `integrator.py` and shared helpers, implement final result writing and summary aggregation. Each transaction result must be MCP-readable and audit-safe. Summary counts total, settled, rejected, review_required, and error results.

**File to CREATE or UPDATE:** UPDATE `integrator.py`; UPDATE `agents/common.py` if reusable helpers are needed.

**Function to CREATE:** `write_result(result: dict, results_dir: Path) -> Path`, `summarize_results(results: list[dict], run_id: str) -> dict`, `write_summary(summary: dict, results_dir: Path) -> Path`.

**Details:** Per-transaction result files use `shared/results/<transaction_id>.json`. Summary file uses `shared/results/summary.json`. Result fields include `transaction_id`, `status`, `reason_codes`, `risk_score`, `risk_level`, `amount`, `currency`, `component_history`, `processed_at`, and `safe_summary`.

**Edge cases:** Duplicate IDs, missing final status, unrecognized status, empty results, and sensitive fields in result payload.

**Acceptance criteria:** Summary counts add to input count; status values are only `settled`, `rejected`, `review_required`, or `error`; final files are stable enough for read-only MCP helpers.

**Verification:** `python -m pytest tests/test_integrator_pipeline.py -k summary`.

### 14. MCP-Readable Result Shapes And Optional Read-Only Server

**Task:** Define and optionally implement read-only MCP helpers after result shapes exist.

**Prompt:** After result files and `summary.json` are stable, create optional `mcp/server.py` read-only helpers that load existing product result files. Do not implement MCP configuration setup.

**File to CREATE or UPDATE:** CREATE `mcp/server.py` only after result shapes are implemented; CREATE `mcp/__init__.py` if needed.

**Function to CREATE:** `load_summary(results_dir: Path = Path("shared/results")) -> dict`, `get_transaction_status(transaction_id: str, results_dir: Path = Path("shared/results")) -> dict`, `list_pipeline_results(results_dir: Path = Path("shared/results")) -> list[dict]`.

**Details:** Helpers are read-only, return safe result payloads, and handle missing files gracefully. If FastMCP is available later, these functions can be wrapped, but the product task should not edit `mcp.json` or `.codex/config.toml`.

**Edge cases:** No run yet, missing `summary.json`, unknown ID, malformed JSON, and archived shared directories.

**Acceptance criteria:** Helpers load summary and transaction status from generated result files without running the pipeline. No raw input transactions are exposed.

**Verification:** `python -m pytest tests/test_integrator_pipeline.py -k mcp_readable` or `python -m pytest tests/test_mcp_readers.py`.

### 15. Per-Transaction Error Handling And Failure Recovery

**Task:** Ensure one bad transaction or component failure does not abort the whole batch.

**Prompt:** Implement controlled per-transaction error handling in the integrator. If a message cannot be processed because of malformed JSON, component exception, or missing stage data, write an `error` final result when the transaction ID is known, write a safe audit event, and continue.

**File to CREATE or UPDATE:** UPDATE `integrator.py`; UPDATE `agents/common.py`.

**Function to CREATE:** `build_error_result(transaction_id: str | None, component: str, reason_code: str, message: str | None = None) -> dict`, `safe_error_message(exc: Exception) -> str`.

**Details:** Do not include stack traces, raw payloads, raw descriptions, account IDs, or metadata. Use reason codes such as `MALFORMED_JSON`, `COMPONENT_FAILURE`, `MISSING_TRANSACTION_ID`, and `PIPELINE_STAGE_ERROR`.

**Edge cases:** Missing transaction ID, exception text containing sensitive input, failure while writing result, malformed JSON before envelope parsing, and repeated run after partial failure.

**Acceptance criteria:** A controlled component failure creates one safe `error` result and does not block other transaction results. Summary `error` count reflects the failure.

**Verification:** `python -m pytest tests/test_integrator_pipeline.py -k error_recovery`.

### 16. Idempotent Reruns

**Task:** Verify reruns archive old evidence and produce a fresh current run.

**Prompt:** Add implementation and tests proving that repeated `run_pipeline` or `python integrator.py` executions never mix old and new runtime evidence. Existing `shared/` must move to the next `archive/shared-NNN` folder before fresh processing begins.

**File to CREATE or UPDATE:** UPDATE `integrator.py`; UPDATE `tests/test_integrator_pipeline.py`.

**Function to CREATE:** `next_archive_path(archive_dir: Path) -> Path`.

**Details:** Archive numbering is based on existing folders, not memory state. Fresh `shared/` contains only current evidence. Archived `shared/run-provenance.json` remains intact.

**Edge cases:** Archive gaps, absent archive directory, empty shared tree, shared from failed prior run, and current run failure after archiving.

**Acceptance criteria:** Two runs produce `archive/shared-001` and a fresh `shared/`; three runs produce `archive/shared-001`, `archive/shared-002`, and a fresh `shared/`; no old result files remain in current `shared/results/`.

**Verification:** `python -m pytest tests/test_integrator_pipeline.py -k idempotent`.

### 17. Pytest Coverage And Temporary Filesystem Tests

**Task:** Build focused tests for the generated Python pipeline with temporary directories.

**Prompt:** Create pytest coverage for common helpers, validation, risk scoring, settlement decisions, integrator orchestration, archival, provenance, redaction, MCP-readable result shapes, dry-run validation, and failure recovery. Use `tmp_path` fixtures so tests do not mutate real repository `shared/` or `archive/`.

**File to CREATE or UPDATE:** UPDATE `tests/conftest.py`, `tests/test_common.py`, `tests/test_validator.py`, `tests/test_fraud_detector.py`, `tests/test_integrator_pipeline.py`; optional CREATE `tests/test_mcp_readers.py`.

**Function to CREATE:** `sample_transactions()` pytest fixture, `isolated_workspace(tmp_path)` pytest fixture, and behavior-named test functions.

**Details:** Tests assert Decimal parsing rejects floats and negative/zero amounts, `TXN006` rejects for unsupported currency, `TXN007` rejects for non-positive amount, risk scores are deterministic, results use only allowed statuses, summary total is eight, sensitive fields are absent, reruns archive prior `shared/`, provenance is non-sensitive, dry-run validation does not run settlement, and per-transaction failure recovery continues.

**Edge cases:** Tests writing real `shared/`, nondeterministic timestamps, order-sensitive assertions, and coverage passing without privacy checks.

**Acceptance criteria:** `python -m pytest` passes. `python -m pytest --cov=.` reports at least the temporary 75% target when `pytest-cov` is available.

**Verification:** `python -m pytest`; `python -m pytest --cov=.` if available.

## Assumptions

- Python is the selected stack.
- Runtime components are ordinary Python modules and not executor agents or automation skills.
- The sample data is synthetic but still treated as sensitive for logs, audit events, summaries, final results, and docs.
- `transaction_id` is safe to use as a correlation identifier.
- Supported currencies are fixed to `USD`, `EUR`, and `GBP` for deterministic homework behavior.
- The pipeline performs no foreign exchange conversion, balance mutation, external payment call, or production compliance decision.
- Amounts are serialized to JSON as strings.
- Future MCP tools will be read-only over generated product result files.

## Uncertainty And Residual Risks

- Exact risk thresholds are a design decision; the suggested thresholds are deterministic and sample-friendly.
- `TXN008` should settle unless the final risk policy deliberately treats mobile channel plus amount as elevated.
- Privacy leakage is the highest practical risk because raw sample records contain account IDs, descriptions, and metadata.
- Decimal safety can be weakened if any implementation uses `float`.
- MCP-readable result shapes should be stable now, but actual MCP configuration setup remains outside product task cards until `mcp/server.py` exists.

## Recommended Next Step

Integrate these task cards into `agent-1-spec/outputs/specification.md`, then draft the Python-specific implementation notes and validation checklist around the same files, functions, status vocabulary, privacy rules, and pytest verification commands.

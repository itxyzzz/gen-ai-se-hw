# Low-Level Tasks Handoff

Run ID: `20260617-180458-write-spec-python-primary`
Stack: `python`
Sub-agent role: Low-level task decomposition

## Scope Assigned

Produce executable low-level task cards for a Python educational transaction-processing pipeline that reads `sample-transactions.json`, coordinates validator, fraud-detection, and settlement/reporting agents through JSON files under `shared/`, preserves audit-safe outputs, and supports tests, coverage gates, MCP status access, and runnable student documentation.

Stack assumptions:

- Language: Python.
- Entry point: `integrator.py`.
- Agent modules: `agents/transaction_validator.py`, `agents/fraud_detector.py`, `agents/settlement_processor.py`.
- Common callable: `process_message(message: dict) -> dict`.
- Money type: `decimal.Decimal`, serialized to JSON as strings.
- Test tools: `pytest`, `pytest-cov`.
- Commands: `python integrator.py`, `python -m pytest`, `python -m pytest --cov=. --cov-fail-under=80`.
- MCP server: `mcp/server.py` using Python FastMCP.
- File protocol: `shared/input`, `shared/processing`, `shared/output`, `shared/results`.
- Privacy boundary: never log plaintext account IDs or raw descriptions; use redacted examples such as `ACC-****1001`.
- Simulation boundary: describe the system as an educational simulation only.

## Task Cards

### 1. Agent 1 - Specification Writer and Run Preservation Workflow

**Task**

Create and preserve the Agent 1 specification package for run `20260617-180458-write-spec-python-primary`, then select or preserve it according to the Homework 6 run rules.

**Prompt**

```text
You are Homework 6 Agent 1, the Specification Writer, for run 20260617-180458-write-spec-python-primary.

Generate a Python-specific Task 1 specification package for an educational transaction-processing pipeline. Read and apply TASKS.md, sample-transactions.json, agents.md, HOMEWORK_STANDARDS.md, root README.md, and the write-spec workflow references. Preserve all generated files inside docs/agent-runs/20260617-180458-write-spec-python-primary before touching canonical files.

The specification must define a Python pipeline with integrator.py, agents/transaction_validator.py, agents/fraud_detector.py, agents/settlement_processor.py, common process_message(message: dict) -> dict functions, Decimal/string amount handling, ISO-style currency validation, deterministic risk scoring, redacted audit events, pytest/pytest-cov gates, /run-pipeline, /validate-transactions, an 80% blocking coverage hook, a final >=90% coverage target, mcp/server.py, Context7 research notes, and final documentation/screenshot requirements.

Do not claim legal, AML, sanctions, payment-network, KYC, or banking compliance. Treat the system as an educational simulation. Do not log plaintext account IDs or raw transaction descriptions. Redact account examples as ACC-****1001.

Write run-local files first. If specification.md does not exist after a successful generation run, copy only agent-1-spec/outputs/specification.md to specification.md and record the auto-selection in docs/agent-runs/final-selection.md. If specification.md already exists, do not overwrite it without explicit operator selection.
```

**File to CREATE/UPDATE**

- Create or update the complete run folder under `docs/agent-runs/20260617-180458-write-spec-python-primary/`.
- Create or update `agent-1-spec/outputs/specification.md`, run support docs, research notes, validation checklist, final review, and handoff.
- Update `docs/agent-runs/final-selection.md` only when first-run auto-selection or explicit selection applies.
- Update canonical `specification.md` only when first-run auto-selection or explicit operator selection applies.
- Update `CHANGELOG.md` before committing selected canonical outputs and run evidence.

**Function to CREATE**

No runtime source function is created by Agent 1. The generated specification must require later functions named `main`, `load_transactions`, `prepare_shared_directories`, `process_transaction`, `validate_transaction`, `score_fraud_risk`, `settle_transaction`, `write_result`, and `summarize_results`.

**Details**

- Include the five required Task 1 sections in `agent-1-spec/outputs/specification.md`.
- Record `stack=python`.
- Require `integrator.py` as the pipeline entry point with `main()`.
- Require JSON file protocol directories: `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- Require each downstream pipeline agent to expose `process_message(message: dict) -> dict`.
- Require all amount parsing to use `decimal.Decimal`, with JSON serialization as strings.
- Require audit events with timestamp, agent name, transaction ID, safe outcome, and reason code.
- Require audit-safe redaction and no raw account identifiers or descriptions in logs, reports, tests, screenshots, or research notes.
- Require Agent 2 to document at least two Context7 queries in `research-notes.md`.
- Require Agent 3 to implement pytest, pytest-cov, `/run-pipeline`, `/validate-transactions`, and an 80% coverage-blocking hook while targeting at least 90% final coverage.
- Require Agent 4 to produce README, HOWTORUN, architecture/testing docs, screenshots, and PR support.

**Edge cases**

- `TXN006` has unsupported currency `XYZ`; require deterministic rejection.
- `TXN007` has negative amount `-100.00`; require deterministic rejection without binary floating point.
- `TXN002` and `TXN005` require deterministic high-value risk scoring.
- `TXN004` requires deterministic odd-hour risk scoring.
- US/DE/GB metadata must be handled as synthetic cross-country metadata, not jurisdiction logic.
- Existing canonical `specification.md` must not be overwritten unless selection rules allow it.

**Acceptance criteria**

- `agent-1-spec/outputs/specification.md` contains all five Task 1 sections.
- The specification is Python-specific and names Python files, functions, commands, and test tools.
- Exactly four low-level task cards exist for Agent 1, Agent 2, Agent 3, and Agent 4.
- Each card includes Task, Prompt, File to CREATE/UPDATE, Function to CREATE, Details, Edge cases, Acceptance criteria, and Verification.
- The code-generation card requires at least three cooperating pipeline agents.
- The coverage gate distinguishes the 80% hook minimum from the at least 90% final target.
- The MCP requirement names `mcp/server.py` and defers `pipeline-status` MCP configuration until after `mcp/server.py` exists.
- Research notes include cited sources and fallback limitations.
- Privacy and simulation boundaries are explicit.

**Verification**

```powershell
Test-Path docs/agent-runs/20260617-180458-write-spec-python-primary/agent-1-spec/outputs/specification.md
Test-Path docs/agent-runs/20260617-180458-write-spec-python-primary/agent-1-spec/validation-checklist.md
Select-String -Path docs/agent-runs/20260617-180458-write-spec-python-primary/agent-1-spec/outputs/specification.md -Pattern "High-Level Objective|Mid-Level Objectives|Implementation Notes|Context|Low-Level Tasks"
Select-String -Path docs/agent-runs/20260617-180458-write-spec-python-primary/agent-1-spec/outputs/specification.md -Pattern "integrator.py|process_message|decimal.Decimal|pytest-cov|mcp/server.py|Context7"
```

Manual verification: confirm any compliance terms are used only to state unsupported claims.

### 2. Agent 2 - Python Code Generator

**Task**

Implement the Python transaction-processing pipeline, including `integrator.py`, three cooperating agent modules, shared JSON file protocol, deterministic validation/risk/settlement behavior, audit-safe summaries, Context7 research notes, and MCP status server foundation.

**Prompt**

```text
You are Homework 6 Agent 2, the Python Code Generator.

Implement the Python educational transaction-processing pipeline specified by Agent 1. Use sample-transactions.json as input. Create a deterministic file-protocol pipeline that reads transactions, writes JSON messages through shared/input, shared/processing, shared/output, and shared/results, and routes each transaction through at least three cooperating agents:
- agents/transaction_validator.py
- agents/fraud_detector.py
- agents/settlement_processor.py

Each agent module must expose process_message(message: dict) -> dict. Use decimal.Decimal for all monetary parsing and calculations. Never use binary floating point for amounts. Serialize money back to JSON as strings. Validate currency against an ISO 4217-style allowlist that includes at least USD, EUR, and GBP and rejects XYZ. Redact account identifiers in all audit/log/report output using examples like ACC-****1001. Do not log raw descriptions. Treat this as an educational simulation and do not claim legal, AML, sanctions, KYC, banking, or payment-network compliance.

Implement integrator.py with main(), load_transactions(), prepare_shared_directories(), process_transaction(), write_json_file(), write_result(), and summarize_results(). The pipeline must account for every sample transaction, including invalid ones, and produce machine-readable summary/status files in shared/results.

Implement mcp/server.py after result file shapes exist. It must expose pipeline status using the generated shared/results files. Add pipeline-status MCP configuration only after mcp/server.py exists. Also document at least two Context7 research queries and applied insights in research-notes.md, including Python JSON/Decimal guidance and FastMCP or MCP server guidance when available.
```

**File to CREATE/UPDATE**

- Create/update `integrator.py`.
- Create/update `agents/__init__.py`.
- Create/update `agents/transaction_validator.py`.
- Create/update `agents/fraud_detector.py`.
- Create/update `agents/settlement_processor.py`.
- Create/update `agents/common.py` if shared helpers reduce duplication.
- Create/update `mcp/server.py`.
- Create/update `research-notes.md`.
- Create/update `mcp.json` and `.codex/config.toml` only after `mcp/server.py` exists.
- Runtime outputs: `shared/input/*.json`, `shared/processing/*.json`, `shared/output/*.json`, `shared/results/summary.json`, `shared/results/audit.json`, and `shared/results/pipeline-status.json`.

**Function to CREATE**

- `integrator.main()`
- `integrator.load_transactions(path: str = "sample-transactions.json") -> list[dict]`
- `integrator.prepare_shared_directories(base_path: str = "shared") -> dict[str, str]`
- `integrator.process_transaction(transaction: dict, directories: dict[str, str]) -> dict`
- `integrator.write_json_file(path: str, payload: dict | list) -> None`
- `integrator.write_result(result: dict, directories: dict[str, str]) -> None`
- `integrator.summarize_results(results: list[dict], directories: dict[str, str]) -> dict`
- `agents.transaction_validator.process_message(message: dict) -> dict`
- `agents.transaction_validator.validate_transaction(transaction: dict) -> dict`
- `agents.fraud_detector.process_message(message: dict) -> dict`
- `agents.fraud_detector.score_fraud_risk(transaction: dict, validation: dict) -> dict`
- `agents.settlement_processor.process_message(message: dict) -> dict`
- `agents.settlement_processor.settle_transaction(message: dict) -> dict`
- `agents.common.parse_amount(value: str) -> Decimal`
- `agents.common.redact_account_id(account_id: str | None) -> str`
- `agents.common.audit_event(agent_name: str, transaction_id: str, outcome: str, reason_code: str, details: dict | None = None) -> dict`
- `mcp.server.get_transaction_status(transaction_id: str) -> dict`
- `mcp.server.list_pipeline_results() -> dict`
- FastMCP resource handler for `pipeline://summary`.

**Details**

- `integrator.py` must be runnable with `python integrator.py`.
- Load `sample-transactions.json` from the homework root by default.
- Write one JSON file per transaction per major stage with deterministic filenames.
- Write aggregate result files under `shared/results/`.
- Convert all `Decimal` values to strings before JSON serialization.
- Validation must fail safely for missing transaction ID, malformed amount, negative amount, unsupported currency, and missing account fields.
- Risk scoring must add deterministic reason codes for high value, very high value, odd hour, and sample metadata signals.
- Settlement must mark invalid transactions as `rejected`, high-risk valid transactions as `review_required`, and valid lower-risk transactions as `settled`.
- `mcp/server.py` must read existing `shared/results/` files without re-running the pipeline.
- `research-notes.md` must record at least two Context7 queries with query text, returned library ID, access date, applied insight, and fallback note when needed.

**Edge cases**

- `TXN006` with currency `XYZ` must produce reason `unsupported_currency`.
- `TXN007` with amount `-100.00` must produce reason `negative_amount`.
- `TXN002` and `TXN005` must receive high-value risk treatment.
- `TXN004` at `02:47:00Z` must receive odd-hour risk treatment.
- Missing fields must not crash the pipeline.
- Re-running `python integrator.py` must produce deterministic current results and avoid duplicate stale output.
- JSON output must be valid and parseable by tests and MCP tools.
- Descriptions must not appear in audit files, status files, or console logs.

**Acceptance criteria**

- `python integrator.py` completes successfully.
- All sample transactions are represented in `shared/results/summary.json`.
- `shared/results/pipeline-status.json` reports total, settled, rejected, and review-required counts.
- Edge-case transactions receive deterministic reason codes.
- Every agent module exposes `process_message(message: dict) -> dict`.
- No generated audit, summary, status, or console output contains plaintext full account IDs or raw descriptions.
- `research-notes.md` contains at least two Context7 query records.
- `mcp/server.py` can import without running the pipeline.
- MCP config is updated for `pipeline-status` only after `mcp/server.py` exists.

**Verification**

```powershell
python integrator.py
python -c "import json; s=json.load(open('shared/results/summary.json', encoding='utf-8')); assert s['total_transactions'] >= 1; print(s)"
python -c "import agents.transaction_validator as v, agents.fraud_detector as f, agents.settlement_processor as s; assert callable(v.process_message); assert callable(f.process_message); assert callable(s.process_message)"
python -c "import mcp.server as server; assert isinstance(server.list_pipeline_results(), dict)"
Select-String -Path research-notes.md -Pattern "Context7"
```

Manual verification: inspect `shared/results/*.json` and confirm no raw descriptions or full account IDs appear.

### 3. Agent 3 - Tests, Commands, and Coverage Hook Author

**Task**

Create the automated test suite, command wrappers, validation helpers, and coverage hook that prove the Python pipeline behavior and block commits when coverage is below 80%, while documenting a final coverage target of at least 90%.

**Prompt**

```text
You are Homework 6 Agent 3, responsible for tests, commands, validation helpers, and coverage enforcement.

Add pytest tests and command surfaces for the Python educational transaction-processing pipeline. Validate the JSON file protocol, Decimal amount behavior, currency validation, deterministic risk scoring, redacted audit outputs, MCP status access, and all provided sample edge cases.

Create slash-style command documentation or executable command files for /run-pipeline and /validate-transactions according to this repository's conventions. /run-pipeline must run python integrator.py. /validate-transactions must validate sample-transactions.json without running the full pipeline by invoking validator dry-run behavior, for example python agents/transaction_validator.py --dry-run, and must report total count, valid count, invalid count, and rejection reasons. Add a local hook that blocks coverage below 80% using pytest-cov or coverage.py. The final documentation and validation checklist must target at least 90% coverage, even though the blocking hook threshold is 80%.

Do not log plaintext account IDs or raw descriptions in test output fixtures. Do not claim legal, AML, sanctions, KYC, banking, or payment-network compliance.
```

**File to CREATE/UPDATE**

- Create/update `tests/conftest.py`.
- Create/update `tests/test_integrator.py`.
- Create/update `tests/test_transaction_validator.py`.
- Create/update `tests/test_fraud_detector.py`.
- Create/update `tests/test_settlement_processor.py`.
- Create/update `tests/test_privacy_audit.py`.
- Create/update `tests/test_mcp_server.py`.
- Create/update `pytest.ini` or `pyproject.toml`.
- Create/update `.coveragerc` if useful.
- Create/update `.git/hooks/pre-commit` or repository-approved hook location.
- Create/update `.codex/commands/run-pipeline.md`.
- Create/update `.codex/commands/validate-transactions.md`.
- Update `CHANGELOG.md`.

**Function to CREATE**

- `tests.conftest.copy_sample_transactions(tmp_path: Path) -> Path`
- `tests.conftest.run_pipeline_in_tmpdir(tmp_path: Path) -> dict`
- `test_pipeline_accounts_for_all_sample_transactions()`
- `test_rejects_invalid_currency_xyz()`
- `test_rejects_negative_amount_without_float()`
- `test_scores_high_value_transactions_deterministically()`
- `test_scores_odd_hour_transaction_deterministically()`
- `test_rejected_and_review_required_outcomes()`
- `test_audit_outputs_redact_account_ids_and_descriptions()`
- `test_pipeline_status_reads_results_without_rerun()`

**Details**

- Use `pytest` with isolated temporary directories.
- Tests must verify the shared directories and result files are created.
- Tests must assert all sample transaction IDs are accounted for exactly once.
- Tests must assert invalid transactions are controlled rejects.
- Tests must assert `parse_amount()` returns `Decimal` and serialized amounts are strings.
- Tests must assert `TXN006`, `TXN007`, `TXN002`, `TXN004`, and `TXN005` behaviors.
- Tests must assert audit/status/summary files do not include raw account IDs or raw descriptions.
- `/run-pipeline` must document `python integrator.py`.
- `/validate-transactions` must document `python agents/transaction_validator.py --dry-run`.
- `/validate-transactions` must report total count, valid count, invalid count, and rejection reasons without running the full pipeline.
- Test and coverage verification must still document `python -m pytest` and `python -m pytest --cov=. --cov-fail-under=80`.
- Coverage hook must block below 80%; docs must state final target is at least 90%.

**Edge cases**

- Invalid currency must not enter settled state.
- Negative amount must fail validation without precision loss.
- High-value and odd-hour scores must be stable across repeated runs.
- Tests must not depend on wall-clock current time except timestamp field presence/parseability.
- Tests must not require network access or live MCP clients.

**Acceptance criteria**

- `python -m pytest` passes.
- `python -m pytest --cov=. --cov-fail-under=80` passes before commit.
- Tests cover validator, fraud detector, settlement processor, integrator, privacy/audit behavior, and MCP status access.
- `/run-pipeline` and `/validate-transactions` exist in the approved command location.
- Hook command fails below 80% and passes at or above 80%.
- No test fixture, assertion, or captured output intentionally prints plaintext account IDs or raw descriptions.

**Verification**

```powershell
python -m pytest
python -m pytest --cov=. --cov-fail-under=80
python integrator.py
Test-Path .codex/commands/run-pipeline.md
Test-Path .codex/commands/validate-transactions.md
Select-String -Path .codex/commands/run-pipeline.md -Pattern "python integrator.py"
Select-String -Path .codex/commands/validate-transactions.md -Pattern "transaction_validator.py|--dry-run|total|valid|invalid|rejection"
```

Manual verification: temporarily lower or break coverage in a disposable local change and confirm the hook command exits non-zero below 80%, then revert that temporary change.

### 4. Agent 4 - Documentation Author

**Task**

Create final student-facing documentation, architecture notes, testing evidence, screenshots, and PR-support materials that explain how to run, validate, and review the Python educational pipeline.

**Prompt**

```text
You are Homework 6 Agent 4, the Documentation Author.

Write the final documentation package for the Python educational transaction-processing pipeline. The docs must be runnable by a reviewer from a clean checkout and must explain the educational simulation boundary, privacy/audit redaction, JSON file protocol, cooperating agents, test commands, coverage evidence, MCP status access, and screenshots.

Update README.md with the required student-name section. If the actual student name is not already known from repository context, add a clear operator-facing fill-in note before final submission and flag it in the handoff. Create HOWTORUN.md, architecture documentation, testing documentation, and PR-support notes. Include screenshots showing a successful pipeline run, test/coverage run, and resulting summary/status output. Do not include plaintext account IDs, raw descriptions, or compliance claims in screenshots or docs.
```

**File to CREATE/UPDATE**

- Create/update `README.md`.
- Create/update `HOWTORUN.md`.
- Create/update `docs/architecture.md`.
- Create/update `docs/testing.md`.
- Create/update `docs/privacy-and-audit.md`.
- Create/update `docs/mcp-status.md`.
- Create/update `docs/pr-support.md`.
- Create/update `docs/screenshots/README.md`.
- Create/update screenshots such as `docs/screenshots/pipeline-run.png`, `docs/screenshots/pytest-coverage.png`, `docs/screenshots/results-summary.png`, and `docs/screenshots/mcp-status.png`.
- Update `CHANGELOG.md`.

**Function to CREATE**

No runtime source function is created by Agent 4. Documentation must reference existing callable entry points such as `integrator.main()`, pipeline agent `process_message(...)` functions, and MCP status functions.

**Details**

- `README.md` must include project title, required student-name section, educational simulation statement, quick start, pipeline overview, agent list, privacy note, testing/coverage commands, and links to HOWTORUN and detailed docs.
- If the student name is unknown, document the need for operator completion as a final-submission blocker rather than pretending it is complete.
- `HOWTORUN.md` must include setup, run, test, and coverage commands.
- `docs/architecture.md` must describe `integrator.py`, the three agents, shared directories, result files, and MCP status server.
- `docs/testing.md` must describe unit tests, integration tests, privacy/audit tests, MCP status tests, 80% hook threshold, and at least 90% final target.
- `docs/privacy-and-audit.md` must describe redaction, no raw descriptions, audit fields, reason codes, and the simulation boundary.
- `docs/mcp-status.md` must explain `mcp/server.py`, status files, and configuration sequencing.
- `docs/pr-support.md` must include deliverables, validation commands/results, known limitations, screenshot index, and reviewer checklist.
- Screenshots must not reveal plaintext full account IDs or raw transaction descriptions.

**Edge cases**

- If student name is unknown, flag it as a final-submission blocker.
- If screenshots cannot be captured, document exact commands and preserve redacted substitutes.
- If coverage is below the at least 90% final target but above the 80% hook threshold, document the gap honestly.
- If MCP status cannot be demonstrated through a live client, document import/function verification.
- Avoid claiming the simulated settlement is a real bank transfer or compliance decision.

**Acceptance criteria**

- `README.md` contains a student-name section.
- `HOWTORUN.md` gives exact setup, run, test, and coverage commands.
- `docs/architecture.md` explains the three agents and JSON file protocol.
- `docs/testing.md` records both the 80% hook threshold and at least 90% final target.
- `docs/privacy-and-audit.md` states no plaintext account IDs or raw descriptions in audit/log output.
- `docs/mcp-status.md` explains `mcp/server.py` and status-file access.
- `docs/pr-support.md` is ready to adapt into the final PR description.
- `docs/screenshots/README.md` indexes screenshots and explains any missing screenshot.
- Documentation uses educational simulation language and avoids unsupported compliance claims.

**Verification**

```powershell
Test-Path README.md
Test-Path HOWTORUN.md
Test-Path docs/architecture.md
Test-Path docs/testing.md
Test-Path docs/privacy-and-audit.md
Test-Path docs/mcp-status.md
Test-Path docs/pr-support.md
Test-Path docs/screenshots/README.md
Select-String -Path README.md -Pattern "Student Name"
Select-String -Path HOWTORUN.md -Pattern "python integrator.py|python -m pytest|cov-fail-under=80"
Select-String -Path docs/testing.md -Pattern "80%|90%"
```

Manual verification: open screenshots and confirm they show successful commands while hiding plaintext full account IDs and raw descriptions.

## Cross-Agent Dependencies

- Agent 1 must finish the preserved specification package before Agents 2, 3, and 4 use it as implementation authority.
- Agent 2 depends on Agent 1 for exact file paths, function names, privacy rules, and acceptance criteria.
- Agent 3 depends on Agent 2 for importable modules, stable function names, deterministic result file shapes, and `mcp/server.py`.
- Agent 4 depends on Agent 2 and Agent 3 for accurate commands, final file layout, test results, coverage evidence, MCP status behavior, and screenshots.
- Agent 2 must create `mcp/server.py` before any `pipeline-status` MCP configuration is added.
- Agent 3 must verify the 80% blocking coverage hook before Agent 4 documents final validation.
- Agent 4 must not claim at least 90% final coverage unless Agent 3 provides evidence.
- All agents must preserve the privacy boundary.

## Residual Risks

- The exact shape of `sample-transactions.json` may require small naming adjustments for source/destination account fields or country metadata.
- The student name may require operator input before final submission.
- Context7 availability may vary; Agent 2 must record fallback limitations if documentation lookup is unavailable.
- MCP configuration should be deferred until `mcp/server.py` exists.
- Coverage may pass the 80% hook while still missing the at least 90% final target; Agent 3 and Agent 4 must report this honestly.
- Screenshots can accidentally expose sensitive sample fields; Agent 4 must inspect every screenshot before preserving it.
- Re-running the pipeline can create stale or duplicate files unless Agent 2 makes `prepare_shared_directories()` deterministic.

## Recommended Next Step

Integrate these task cards into `agent-1-spec/outputs/specification.md`, then run final review before first-run auto-selection.

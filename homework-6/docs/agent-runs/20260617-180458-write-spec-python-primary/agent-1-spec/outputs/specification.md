# Homework 6 Transaction Pipeline Specification

Status: Candidate Agent 1 output for run `20260617-180458-write-spec-python-primary`
Selected stack: `python`

## High-Level Objective

Build a Python educational transaction-processing pipeline that reads `sample-transactions.json`, routes every transaction through cooperating validator, risk-review, and settlement agents using JSON files, and produces audit-safe results, tests, coverage evidence, MCP status access, and runnable documentation.

## Mid-Level Objectives

| ID | Objective | Observable success |
|---|---|---|
| M1 | Implement a deterministic Python pipeline orchestrated by `integrator.py` through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`. | Running `python integrator.py` creates or refreshes the shared protocol directories, processes every sample transaction once, and writes final result and summary JSON files under `shared/results/`. |
| M2 | Build at least three cooperating Python agent modules with a shared `process_message(message: dict) -> dict` contract. | `agents/transaction_validator.py`, `agents/fraud_detector.py`, and `agents/settlement_processor.py` each accept and return dictionaries with transaction ID, status, reason codes, audit metadata, and next-stage routing fields. |
| M3 | Enforce safe financial and validation behavior using `decimal.Decimal`, string-based amount parsing, ISO 4217-style currency validation, deterministic risk scoring, and privacy-preserving audit events. | Tests prove that `XYZ` and `-100.00` are rejected, `25000.00` and `75000.00` are flagged for review, `02:47:00Z` contributes an odd-hour signal, and account IDs are redacted in logs and audit examples. |
| M4 | Provide a complete verification and automation surface with `pytest`, `pytest-cov`, `/run-pipeline`, `/validate-transactions`, and a coverage hook. | Unit and integration tests pass with `python -m pytest`; coverage enforcement passes with `python -m pytest --cov=. --cov-fail-under=80`; the hook blocks below 80%; final documentation targets at least 90% coverage. |
| M5 | Prepare handoff-ready operational surfaces: Context7 research notes, FastMCP status server, and final user documentation. | Agent 2 documents at least two Context7 queries in `research-notes.md`, creates `mcp/server.py` before adding `pipeline-status` configuration, and Agent 4 produces README, HOWTORUN, docs, screenshots, and PR support with the student name. |

## Implementation Notes

### Stack And Layout

Use Python as the selected implementation stack.

Required implementation paths:

```text
integrator.py
agents/
  __init__.py
  common.py
  transaction_validator.py
  fraud_detector.py
  settlement_processor.py
tests/
mcp/server.py
shared/
  input/
  processing/
  output/
  results/
```

The pipeline entry point is `integrator.py` with `main()`. Each pipeline agent exposes `process_message(message: dict) -> dict`.

### Money And Currency

- Parse all transaction amounts from JSON strings into `decimal.Decimal`.
- Never use binary floating point for transaction amounts.
- Serialize monetary values back to JSON as strings.
- Reject missing, malformed, non-finite, zero, or negative amounts with stable reason codes.
- Validate currencies with an ISO 4217-style allowlist that includes at least `USD`, `EUR`, `GBP`, and `JPY`.
- Reject unsupported currency `XYZ` with reason code `unsupported_currency`.

### Shared JSON Protocol

Agents pass messages as JSON files through:

```text
shared/input
shared/processing
shared/output
shared/results
```

Message envelopes should include `message_id`, `timestamp`, `source_agent`, `target_agent`, `message_type`, and `data`. Intermediate files may add `validation`, `risk`, `settlement`, `audit`, and `errors` fields. Every input transaction must end with a result under `shared/results/`.

### Audit, Logging, And Privacy

Audit records should include:

- ISO 8601 timestamp.
- Agent name.
- Transaction ID.
- Safe outcome.
- Stable reason code.
- Optional redacted account reference.

Logs, audit records, result summaries, docs, tests, and screenshots must not expose plaintext full account identifiers, raw transaction descriptions, secrets, tokens, credentials, authorization headers, or raw metadata dumps. Use redacted examples such as `ACC-****1001`.

### Risk Review

Risk scoring is deterministic homework logic, not a legal, AML, sanctions, KYC, banking, payment-network, or regulatory-compliance finding. Recommended signals:

- `amount > Decimal("10000.00")`: high-value signal.
- `amount >= Decimal("50000.00")`: very-high-value signal.
- Timestamp between `00:00:00Z` and `04:59:59Z`: odd-hour signal.
- Synthetic country/channel metadata outside expected sample path: contextual signal.

### Testing, Coverage, And Commands

Use `pytest` and `pytest-cov`.

Required commands:

```powershell
python integrator.py
python -m pytest
python -m pytest --cov=. --cov-fail-under=80
```

The coverage hook must block below 80%. Final documentation should target at least 90% coverage and report honestly if the final evidence is lower.

Agent 3 must create command files for `/run-pipeline` and `/validate-transactions` in the repository-approved command location. `/run-pipeline` runs the full pipeline with `python integrator.py`. `/validate-transactions` validates `sample-transactions.json` without running the full pipeline by invoking validator dry-run behavior, for example `python agents/transaction_validator.py --dry-run`, and reports total, valid, invalid, and rejection reasons.

### Context7 And MCP

Agent 2 must use Context7 during code generation and document at least two queries in `research-notes.md`, including query text, returned library ID, access date, and applied insight.

Task 4 must add Python FastMCP server code at `mcp/server.py` after result file shapes exist. It must expose:

- Tool `get_transaction_status(transaction_id: str)`.
- Tool `list_pipeline_results()`.
- Resource `pipeline://summary`.

Add `pipeline-status` to `mcp.json` and `.codex/config.toml` only after `mcp/server.py` exists.

## Context

### Beginning State

The homework root contains `sample-transactions.json` with eight synthetic transaction records:

- `TXN001`: normal USD online transfer for `1500.00`.
- `TXN002`: high-value USD branch wire transfer for `25000.00`.
- `TXN003`: USD transfer for `9999.99`, below the strict high-value threshold.
- `TXN004`: EUR API transfer at `02:47:00Z` with country `DE`.
- `TXN005`: very high-value USD branch wire transfer for `75000.00`.
- `TXN006`: invalid sample currency `XYZ`.
- `TXN007`: invalid negative GBP amount `-100.00`.
- `TXN008`: normal USD mobile transfer for `3200.00`.

No pipeline implementation, tests, canonical research notes, command files, custom MCP server, runtime `shared/` results, or final README/HOWTORUN package is assumed at the start of implementation.

### Ending State

After Agents 2-4 complete the specification:

- `python integrator.py` processes all eight sample transactions.
- `shared/results/summary.json` accounts for every sample transaction exactly once.
- `shared/results/pipeline-status.json` reports total, settled, rejected, and review-required counts.
- `shared/results/audit.json` contains structured audit-safe events with redacted account references.
- Invalid records such as `TXN006` and `TXN007` are rejected with safe reason codes.
- High-value and odd-hour records such as `TXN002`, `TXN004`, and `TXN005` receive deterministic risk-review reason codes.
- `python -m pytest --cov=. --cov-fail-under=80` passes.
- Final documentation records a target of at least 90% coverage.
- `mcp/server.py` exposes FastMCP tools and resource backed by existing result files.
- README, HOWTORUN, screenshots, and PR support documents are ready for reviewer use.

## Low-Level Tasks

### Task: Agent 1 - Specification Writer

**Prompt**

```text
Use the repo-local write-spec workflow to create and preserve the Homework 6 Python specification package. Generate run-local specification, support docs, research notes, handoffs, validation checklist, and final review before canonical copy. If no canonical specification.md exists after validation passes, copy only the run output specification.md to the homework root and record first-run auto-selection. Do not overwrite agents.md or create pipeline code, tests, hooks, screenshots, or MCP server code in this phase.
```

**File to CREATE/UPDATE**

- Create/update `docs/agent-runs/<run-id>/agent-1-spec/outputs/specification.md`.
- Create/update `docs/agent-runs/<run-id>/agent-1-spec/outputs/docs/domain-rules.md`.
- Create/update `docs/agent-runs/<run-id>/agent-1-spec/outputs/docs/technical-conventions.md`.
- Create/update `docs/agent-runs/<run-id>/agent-1-spec/outputs/docs/development-process.md`.
- Create/update `docs/agent-runs/<run-id>/agent-1-spec/research-notes.md`.
- Create/update `docs/agent-runs/<run-id>/agent-1-spec/validation-checklist.md`.
- Create/update `docs/agent-runs/<run-id>/agent-1-spec/handoff.md`.
- Update `docs/agent-runs/final-selection.md` after successful first-run auto-selection.
- Create canonical `specification.md` only after validation and first-run auto-selection.

**Function to CREATE**

No runtime function. The generated spec must require `main`, `load_transactions`, `prepare_shared_directories`, `process_transaction`, `validate_transaction`, `score_fraud_risk`, `settle_transaction`, `write_result`, and `summarize_results`.

**Details**

Preserve all Agent 1 evidence under the run folder. Include all five Task 1 sections, Python stack specificity, sub-agent handoffs, research provenance, privacy rules, simulation boundaries, and canonical selection rules.

**Edge cases**

Handle absent `specification-TEMPLATE-hint.md`, absent canonical `specification.md`, invalid `XYZ`, negative `-100.00`, high-value samples, odd-hour sample, and pre-existing unrelated dirty files without overwriting them.

**Acceptance criteria**

The candidate spec and required run artifacts exist, validation passes, final review has no unresolved blocking findings, and first-run auto-selection copies only `specification.md` when canonical spec is absent.

**Verification**

```powershell
Test-Path docs/agent-runs/<run-id>/agent-1-spec/outputs/specification.md
Select-String -Path docs/agent-runs/<run-id>/agent-1-spec/outputs/specification.md -Pattern "High-Level Objective|Mid-Level Objectives|Implementation Notes|Context|Low-Level Tasks"
Test-Path specification.md
```

### Task: Agent 2 - Python Code Generator

**Prompt**

```text
Implement the Python educational transaction-processing pipeline from specification.md. Create integrator.py and at least three cooperating modules: agents/transaction_validator.py, agents/fraud_detector.py, and agents/settlement_processor.py. Each module must expose process_message(message: dict) -> dict. Use decimal.Decimal from strings for money, never binary floating point. Validate ISO-style currency codes, reject XYZ, reject negative amounts, score high-value and odd-hour transactions deterministically, write JSON files through shared/input, shared/processing, shared/output, and shared/results, and redact account IDs in audit/log/report output. Use Context7 during code generation and document at least two queries in research-notes.md. Create mcp/server.py only after result file shapes exist, then add pipeline-status MCP configuration.
```

**File to CREATE/UPDATE**

- Create/update `integrator.py`.
- Create/update `agents/__init__.py`.
- Create/update `agents/common.py`.
- Create/update `agents/transaction_validator.py`.
- Create/update `agents/fraud_detector.py`.
- Create/update `agents/settlement_processor.py`.
- Create/update `research-notes.md`.
- Create/update `mcp/server.py`.
- Update `mcp.json` and `.codex/config.toml` only after `mcp/server.py` exists.

**Function to CREATE**

- `integrator.main()`
- `integrator.load_transactions(path: str = "sample-transactions.json") -> list[dict]`
- `integrator.prepare_shared_directories(base_path: str = "shared") -> dict[str, str]`
- `integrator.process_transaction(transaction: dict, directories: dict[str, str]) -> dict`
- `integrator.write_json_file(path: str, payload: dict | list) -> None`
- `integrator.write_result(result: dict, directories: dict[str, str]) -> None`
- `integrator.summarize_results(results: list[dict], directories: dict[str, str]) -> dict`
- `agents.common.parse_amount(value: str) -> Decimal`
- `agents.common.redact_account_id(account_id: str | None) -> str`
- `agents.common.audit_event(agent_name: str, transaction_id: str, outcome: str, reason_code: str, details: dict | None = None) -> dict`
- `agents.transaction_validator.process_message(message: dict) -> dict`
- `agents.transaction_validator.validate_transaction(transaction: dict) -> dict`
- `agents.fraud_detector.process_message(message: dict) -> dict`
- `agents.fraud_detector.score_fraud_risk(transaction: dict, validation: dict) -> dict`
- `agents.settlement_processor.process_message(message: dict) -> dict`
- `agents.settlement_processor.settle_transaction(message: dict) -> dict`
- `mcp.server.get_transaction_status(transaction_id: str) -> dict`
- `mcp.server.list_pipeline_results() -> dict`
- FastMCP resource handler for `pipeline://summary`.

**Details**

The integrator loads samples, prepares shared directories deterministically, writes initial messages, calls the three agent modules in sequence, writes stage files, writes final results, and prints a safe summary. The validator returns controlled rejects for missing fields, malformed amounts, negative amounts, and unsupported currencies. The risk agent adds stable reason codes such as `high_value`, `very_high_value`, and `odd_hour`. The settlement agent marks outcomes as `settled`, `review_required`, or `rejected`. Result files must be MCP-readable and audit-safe.

**Edge cases**

`TXN006` rejects as `unsupported_currency`; `TXN007` rejects as `negative_amount`; `TXN002` and `TXN005` receive high-value risk reason codes; `TXN004` receives `odd_hour`; missing fields produce safe rejects; repeated runs refresh current results without stale duplicates.

**Acceptance criteria**

`python integrator.py` completes, all transactions are represented in `shared/results/summary.json`, all three modules expose `process_message`, results avoid plaintext full account IDs and raw descriptions, `research-notes.md` records two Context7 queries, and MCP status code imports without running the pipeline.

**Verification**

```powershell
python integrator.py
python -c "import json; s=json.load(open('shared/results/summary.json', encoding='utf-8')); assert s['total_transactions'] == 8"
python -c "import agents.transaction_validator as v, agents.fraud_detector as f, agents.settlement_processor as s; assert callable(v.process_message); assert callable(f.process_message); assert callable(s.process_message)"
Select-String -Path research-notes.md -Pattern "Context7"
```

### Task: Agent 3 - Tests, Commands, And Coverage Hook Author

**Prompt**

```text
Create pytest coverage for the Python educational transaction-processing pipeline. Cover validator, fraud detector, settlement processor, integrator, audit privacy, and MCP status access. Use tmp_path so tests do not depend on stale shared/ outputs. Add /run-pipeline and /validate-transactions command files. /run-pipeline must run the full pipeline with python integrator.py. /validate-transactions must validate sample-transactions.json without processing the full pipeline by invoking validator dry-run behavior, for example python agents/transaction_validator.py --dry-run, and must report total count, valid count, invalid count, and rejection reasons. Add a coverage hook that blocks below 80% using pytest-cov while the final documentation target remains at least 90%. Do not print plaintext account IDs or raw descriptions in tests or fixtures.
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
- Create/update `.codex/commands/run-pipeline.md`.
- Create/update `.codex/commands/validate-transactions.md`.
- Create/update repository-approved coverage hook.

**Function to CREATE**

- `copy_sample_transactions(tmp_path: Path) -> Path`
- `run_pipeline_in_tmpdir(tmp_path: Path) -> dict`
- `test_pipeline_accounts_for_all_sample_transactions()`
- `test_rejects_invalid_currency_xyz()`
- `test_rejects_negative_amount_without_float()`
- `test_scores_high_value_transactions_deterministically()`
- `test_scores_odd_hour_transaction_deterministically()`
- `test_rejected_and_review_required_outcomes()`
- `test_audit_outputs_redact_account_ids_and_descriptions()`
- `test_pipeline_status_reads_results_without_rerun()`

**Details**

Tests must verify shared directories, result files, every sample transaction, Decimal parsing, JSON amount strings, validation rejects, deterministic risk reason codes, privacy redaction, and MCP status reading from existing result files. `/run-pipeline` must run `python integrator.py`. `/validate-transactions` must run `python agents/transaction_validator.py --dry-run`, validate all records in `sample-transactions.json` without running the full pipeline, and report total count, valid count, invalid count, and rejection reasons. Test and coverage verification must still run `python -m pytest` and `python -m pytest --cov=. --cov-fail-under=80`.

**Edge cases**

Invalid currency does not settle; negative amount does not settle; high-value and odd-hour reason codes are stable; tests do not require network access or live MCP clients; coverage hook works on Windows PowerShell.

**Acceptance criteria**

`python -m pytest` passes, `python -m pytest --cov=. --cov-fail-under=80` passes, coverage hook blocks below 80%, `/run-pipeline` runs `python integrator.py`, `/validate-transactions` invokes validator dry-run behavior and reports validation counts/reasons, and tests avoid plaintext account IDs/raw descriptions.

**Verification**

```powershell
python -m pytest
python -m pytest --cov=. --cov-fail-under=80
Test-Path .codex/commands/run-pipeline.md
Test-Path .codex/commands/validate-transactions.md
Select-String -Path .codex/commands/run-pipeline.md -Pattern "python integrator.py"
Select-String -Path .codex/commands/validate-transactions.md -Pattern "transaction_validator.py|--dry-run|total|valid|invalid|rejection"
```

### Task: Agent 4 - Documentation Author

**Prompt**

```text
Write the final documentation package for the Python educational transaction-processing pipeline. Update README.md with the required student-name section, quick start, agent responsibilities, educational simulation boundary, privacy/audit redaction, JSON file protocol, testing/coverage commands, MCP status access, and links to detailed docs. Create HOWTORUN.md, architecture/testing/privacy/MCP docs, screenshot index, and PR-support notes. Capture or document screenshots for pipeline run, coverage run, result summary, command usage, hook behavior, and MCP status. Do not include plaintext account IDs, raw descriptions, or compliance claims in docs or screenshots.
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
- Create/update required screenshots under `docs/screenshots/`.

**Function to CREATE**

No runtime function. Documentation must reference existing entry points such as `integrator.main()`, the three agent `process_message(...)` functions, and FastMCP status tools.

**Details**

README must include the student's name or an explicit final-submission blocker for operator completion if the name is not yet known. HOWTORUN must include setup, run, test, and coverage commands. Architecture docs must explain `integrator.py`, the three agents, shared directories, result files, and MCP status. Testing docs must record the 80% hook threshold and at least 90% final target. Privacy docs must explain account redaction and no raw descriptions. PR support must list deliverables, validation commands/results, known limitations, screenshot index, and reviewer checklist.

**Edge cases**

If the student name is unknown, flag it before final submission. If screenshots cannot be captured, record exact commands and redacted substitutes. If coverage is below the at least 90% target but above the 80% hook threshold, report the gap honestly. If live MCP client proof is unavailable, document import/function verification.

**Acceptance criteria**

README has a student-name section, HOWTORUN has exact commands, docs explain architecture/testing/privacy/MCP, screenshot index exists, screenshots or documented substitutes are present, and docs use educational simulation language without unsupported compliance claims.

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
```

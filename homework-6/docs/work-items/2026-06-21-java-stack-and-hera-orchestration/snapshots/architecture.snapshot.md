# Phase 01 Architecture Snapshot

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Short ID: `java-stack-and-hera-orchestration`
Status: Draft Review
Artifact type: Immutable pre-implementation snapshot after approval

## Current Architecture Before Phase 01

Homework 6 currently has a selected Python package produced by four Homework Automation Layer agents:

- Athena (Spec Writer): selected Python specification run `20260619-170102-write-spec-python-fresh`.
- Hephaestus (Code Generator): selected Python code run `20260619-175211-generate-code-python-fresh-spec`.
- Themis (Test Generator): selected Python test run `20260620-144025-generate-tests-python-fresh-spec`.
- Clio (Documentation Generator): selected Python documentation run `20260621-011348-generate-docs-python-review-repair`.

The Generated Transaction System Layer is Python at the canonical root:

- `integrator.py`
- `agents/*.py`
- `tests/*.py`
- `pytest.ini`
- `research-notes.md`
- `shared/` as reviewer-visible last-run evidence
- `mcp/server.py` as a Python FastMCP status reader over `shared/results/`

Operator Layer support surfaces currently assume Python in practical command examples:

- `/run-pipeline` normally runs `python integrator.py`.
- `/validate-transactions` imports `agents.transaction_validator.validate_transactions_file`.
- `scripts/check_coverage_gate.py` runs pytest with pytest-cov.
- `.githooks/pre-push` and `.claude/settings.json` call the Python coverage helper.
- Themis and Clio evidence layouts name pytest, `.coverage*`, `.pytest_cache/`, and Python test paths.

## Phase 01 Target Architecture

Phase 01 makes these surfaces stack-aware without changing the canonical generated product:

```text
Operator Layer
  |
  +-- selection-set metadata
  |     +-- final-selection.md remains the human history
  |     +-- selection-sets.json becomes the machine-readable stack registry
  |
  +-- universal operation helpers
  |     +-- run-pipeline chooses Python or Java command from explicit stack or metadata
  |     +-- validate-transactions chooses Python import or Java dry-run CLI from explicit stack or metadata
  |     +-- check_coverage_gate.py dispatches to pytest or Maven/JUnit/JaCoCo
  |
  +-- stack-ready agent-control packages
        +-- Athena stack profiles and quality bar
        +-- Hephaestus generation workflow and registry
        +-- Themis test workflow and registry
        +-- Clio documentation workflow and registry
```

Phase 01 does not add Hera (Orchestrator). Hera remains a Phase 02 Automation Layer addition.

## Selection Metadata Decision

The human-readable `docs/agent-runs/final-selection.md` stays as the audit narrative and reviewer-facing selection history. Phase 01 implementation should add `docs/agent-runs/selection-sets.json` as the machine-readable package-set registry so scripts and command guidance do not need brittle markdown parsing.

The planned JSON record should support:

- Schema/version.
- Canonical set ID.
- One or more package sets.
- Stack value: `python` or `java`.
- Status such as `canonical`, `alternate`, `candidate`, `superseded`, or `failed`.
- Selected Athena, Hephaestus, Themis, and Clio run IDs where available.
- Inventory paths for selected code, tests, and docs.
- Package root or canonical root path.
- Stack command hints for pipeline run, validation-only behavior, and coverage gate.
- Explicit notes that Python remains canonical and Java is not selected until a later operator decision.

The JSON must not store raw sample transaction records, raw account IDs, descriptions, hidden prompts, credentials, or environment dumps.

## Helper Dispatch Decision

`scripts/check_coverage_gate.py` remains the public helper path for compatibility. It becomes universal:

- Default command remains valid: `python scripts/check_coverage_gate.py --fail-under 80`.
- Add explicit stack selection: `--stack auto`, `--stack python`, or `--stack java`.
- Add explicit project root: `--project-dir PATH`.
- Python mode keeps pytest, pytest-cov, and the temp coverage workspace behavior.
- Java mode requires a Maven project and runs the generated package's Maven/JUnit/JaCoCo check path. Context7 planning established JaCoCo `check` as the coverage-enforcement concept and JUnit Jupiter with Maven Surefire/Failsafe as the test execution concept.

The Git and Claude hooks may continue to call the helper with default behavior or may pass `--stack auto`. Either way, the current selected Python package must still pass.

## MCP Decision

`mcp/server.py` remains Python. It is part of the Operator Layer support surface, not the Generated Transaction System Layer's implementation language.

The stack-neutral product contract is:

- `shared/results/summary.json` contains safe aggregate fields such as total, settled, rejected, review-required, and error counts.
- `shared/results/TXN*.json` contains safe transaction-level fields such as transaction ID, status, reason codes, risk score/level, amount string, currency, processed timestamp, safe summary, component history count, and audit event count.
- The MCP server returns safe views and does not expose raw account IDs, raw descriptions, names, credentials, or unfiltered metadata.

If a future Java package cannot emit this shape, prefer repairing the Java spec/code before forking MCP.

## Java Stack Contract For Future Generation

Phase 01 control surfaces should guide future Java packages toward:

- Maven build with `pom.xml`.
- Java source under `src/main/java/...`.
- Java tests under `src/test/java/...`.
- Money with `BigDecimal`, never `double` or `float`.
- JSON with Jackson or an equivalent configured library.
- Tests with JUnit 5/JUnit Jupiter.
- Coverage with JaCoCo Maven plugin, including a check goal capable of failing below 80 percent.
- A pipeline command chosen by the generated Java spec, such as `mvn exec:java` or a packaged `java -jar` command.
- A validation-only dry-run CLI or equivalent command that reports safe counts, statuses, and reason codes without running settlement.

## Boundaries

Phase 01 may change Operator Layer control surfaces and helper scripts. It must not:

- Replace selected Python canonical outputs.
- Generate Java Athena, Hephaestus, Themis, or Clio runs.
- Add Hera control surfaces.
- Modify runtime product behavior.
- Fork MCP without a proven blocker.
- Stage or commit pre-existing `.codex/config.toml` changes unless separately authorized.

## Rollback Shape

If Phase 01 implementation breaks Python verification, revert or repair the helper/control-surface change before continuing. Because Java remains ungenerated and alternate-only in this phase, rollback should not need to delete Java run folders or replace canonical product files.

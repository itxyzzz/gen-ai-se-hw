# Generate Tests Workflow

This is the canonical workflow for Homework 6 Themis (Test Generator) entrypoints:

- Codex Markdown skill: `homework-6/.agents/skills/generate-tests/SKILL.md`
- Claude Code project skill, exposed as `/generate-tests`: `homework-6/.claude/skills/generate-tests/SKILL.md`
- Claude Code legacy command wrapper: `homework-6/.claude/commands/generate-tests.md`

The entrypoints must stay thin. Update this package first when the workflow changes.

All paths are repository-root relative unless the active project root is already `homework-6`. When running from a homework-root project, remove the leading `homework-6/` prefix from homework-local paths.

## Required Context

Read this context before writing, executing, comparing, or selecting tests:

1. `homework-6/docs/agent-runs/final-selection.md`: selected Athena (Spec Writer) and selected Hephaestus (Code Generator) records.
2. Selected Hephaestus output inventory named by the selection record.
3. `homework-6/specification.md`: current canonical selected specification.
4. `homework-6/sample-transactions.json`: canonical sample input.
5. `homework-6/agents.md`: layer glossary, privacy rules, run preservation, and Themis role.
6. `homework-6/TASKS.md`: Task 3, Task 4, and Task 5 assignment checks.
7. `homework-6/agent-control/operate-pipeline/commands-and-hooks.md`: external command and hook behavior Themis validates.
8. This package's `quality-bar.md` and `run-registry.md`.

If the selected Hephaestus version, selected output inventory, or final-selection record is missing, stop and ask the operator to select or repair the code package. Enforce no "latest" targeting: do not target "latest" from the file tree.

Repository `dev-doc-harness` and Superpowers requirements apply to Operator Layer maintenance of this package. They do not apply inside Themis (Test Generator) runs or generated transaction-system test files.

## Operating Modes

- `generate`: create or extend a candidate test package for the selected Hephaestus software version.
- `resume`: continue a bounded Themis run from preserved handoff evidence.
- `compare`: compare two or more preserved Themis runs without changing canonical tests.
- `select`: copy an accepted candidate test package to canonical targets through its inventory and record the selection.

Default to `generate` unless the operator asks for comparison, continuation, or selection.

## Run ID

Use run IDs in this format:

```text
YYYYMMDD-HHMMSS-generate-tests-<stack>-short-label
```

Examples include `YYYYMMDD-HHMMSS-generate-tests-python-primary` and `YYYYMMDD-HHMMSS-generate-tests-java-alternate`. The `<stack>` value must match the selected Hephaestus (Code Generator) package set named by the selection record or invocation.

## Required Version Traceability

Every Themis run must record:

- Selected Hephaestus run ID.
- Selected Hephaestus output inventory path.
- Selection record path, normally `homework-6/docs/agent-runs/final-selection.md`.
- Selected canonical code and test paths named by that inventory.
- SHA-256 fingerprints for selected code files or for the package inventory.
- Source Athena run ID recorded by the selected Hephaestus package.
- Source spec path and source spec fingerprint recorded by the selected Hephaestus package.
- Current canonical `homework-6/specification.md` fingerprint.
- Whether the selected code source spec differs from the current canonical spec.

The current repository may contain a selected Hephaestus package generated from an earlier Athena (Spec Writer) run than the current canonical `specification.md`. Themis must report that source spec mismatch before generating or selecting tests. Themis may still test the selected software version when the operator explicitly wants that version tested, but it must not claim fresh-spec coverage unless the selected code package actually targets the fresh spec.

## Run Folder Layout

Create the run folder before drafting tests:

```text
homework-6/docs/agent-runs/RUN_ID/
  run-metadata.md
  inputs/
    source-context.md
    selected-code-inventory.snapshot.md
  agent-3-tests/
    outputs/
      tests/
      src/test/java/
      pytest.ini
      inventory.md
    workspace/
      selected-code/
      project-under-test/
    evidence/
      coverage-summary.txt
      support-run-pipeline.txt
      support-validate-transactions.txt
      hook-pass.txt
      hook-fail.txt
    review/
    validation-checklist.md
    handoff.md
  comparison.md
```

`comparison.md` is required only for compare mode or when more than one viable run is evaluated.

`run-metadata.md` must record mode, selected stack, start time, orchestration tool, selected Hephaestus traceability, source Athena traceability, current canonical spec fingerprint, source spec mismatch status, intended output targets, and pre-existing dirty git state.

`inputs/source-context.md` must list exact source artifacts read, including selection records, inventories, specs, sample data, assignment file, agent guide, control package references, and command/hook package references.

## Run-Local File Manipulation

Themis must keep root product files untouched during ordinary generation:

1. Copy selected Hephaestus inventory-declared canonical files into `agent-3-tests/workspace/selected-code/`.
2. Write all candidate test and test-config files first under `agent-3-tests/outputs/`.
3. Build `agent-3-tests/workspace/project-under-test/` by copying `workspace/selected-code/` and overlaying files from `outputs/`.
4. Run pytest, coverage, pipeline, command, hook, and validator checks from `workspace/project-under-test/`.
5. Keep runtime output under the run folder, such as workspace-local `shared/`, `.test-tmp/`, coverage reports, and compact text evidence under `evidence/`.
6. Do not mutate root `tests/`, root `shared/`, root `.coverage`, root `.pytest_cache/`, or canonical generated product files during ordinary generation.

`agent-3-tests/outputs/` is the selectable candidate package. Its `inventory.md` must list each candidate test/config file, intended canonical target, kind, SHA-256 fingerprint, and excluded runtime/tool-output paths. `workspace/` and `evidence/` are run evidence, not canonical copy targets.

For `stack=java`, candidate tests normally live under `agent-3-tests/outputs/src/test/java/...`, Maven/JUnit validation runs from `workspace/project-under-test/`, and coverage evidence comes from the JaCoCo Maven plugin check goal. Do not create Python pytest-only wrappers for Java tests.

### Java Output Staging Guardrails

For `stack=java`, Themis must stage reusable baseline tests and new candidate tests only under:

```text
agent-3-tests/outputs/src/test/java/
```

Do not stage Java tests under `agent-3-tests/outputs/src/java/`, `agent-3-tests/outputs/test/java/`, or any duplicate nested test tree. Before rebuilding `workspace/project-under-test/`, inspect `agent-3-tests/outputs/` and block or restart the run if malformed Java test roots exist.

If malformed staging is detected before validation, Themis may correct it only when the correction is local, unambiguous, and does not require elevated cleanup. If cleanup becomes approval-dependent, destructive, or ambiguous, stop the run, write blocked metadata/checklist/handoff, and let Hera or the operator start a replacement run. Do not spend a child-agent run on cleanup loops.

Rebuild `workspace/project-under-test/` only after `outputs/` passes this staging check.

During `select`, copy only inventory-declared files from `agent-3-tests/outputs/` to canonical targets. Never copy the workspace wholesale to the root.

## Test Generation Scope

Themis may create or extend:

- Unit tests for each runtime transaction pipeline component.
- Integration tests for the full pipeline.
- Dry-run validator tests for `sample-transactions.json`.
- Tests or compact evidence for `/run-pipeline` and `/validate-transactions` behavior.
- Tests or compact evidence for the coverage gate hook success and blocking paths.
- Privacy and audit checks for redaction, reason codes, safe logs, and screenshot-safe output.
- Fixture isolation and repeated-run archival tests.
- Strict JSON, Decimal money, currency, settlement, rejection, review, and summary assertions.

Themis owns test quality, not only percentage coverage. Coverage at or above 80 percent is a floor; meaningful assertions and risk coverage decide whether a suite is acceptable.

Screenshot capture for `docs/screenshots/skill-run-pipeline.png` and `docs/screenshots/hook-trigger.png` belongs to later Clio (Documentation Generator) or an explicit evidence task, not routine Themis generation.

Themis must not implement Task 4 custom MCP server/config, final README/HOWTORUN docs, Clio-owned PR narrative, or runtime product feature changes. If a failing test reveals a selected-code defect, Themis records the defect and asks the operator to authorize a repair instead of silently changing runtime product code.

## Command And Hook Ownership

`/run-pipeline`, `/validate-transactions`, and coverage hook behavior are defined by `homework-6/agent-control/operate-pipeline/commands-and-hooks.md`.

Those files and their wrappers are one-time Operator Layer operation tools. Future Themis runs validate that they exist, invoke the shared behavior, and work against the selected package. If they are missing or stale, Themis reports a support-tool gap or requests an Operator Layer repair. Themis must not regenerate them as ordinary per-run outputs.

## Agent 4 Interpretation

Task 5 says Agent 4 is responsible for testing and documentation. In this pipeline, Clio (Documentation Generator) owns final documentation, screenshots, reviewer-facing test evidence, and the final acceptance narrative.

Clio consumes the selected Themis suite, reruns it for evidence, captures required screenshots, and documents results. If Clio finds missing final test coverage, it should request a Themis follow-up or record an explicit final test-hardening delta. Clio must not silently fork or replace the selected Themis suite.

## Executor Sub-Agent Policy

Future Themis runs may use executor sub-agents up to Homework 6's `agents.max_threads = 8` cap when independent test-audit slices can run in parallel. Good slices include component test expansion, integration/rerun scenarios, command validation, hook validation, and privacy/audit review.

Each sub-agent must receive curated context, a policy-relative model/reasoning intent, explicit file ownership, and an output artifact/report. The orchestration thread owns final integration, validation, inventory, selection, and user-facing handoff.

Use no sub-agents when the work is tightly coupled, runtime support is unavailable, or the scope is small enough that parallelism would reduce quality. Record the reason for no sub-agent use.

## Validation And Handoff

Before reporting a Themis run complete:

1. Confirm selected-code traceability and source spec mismatch reporting are present.
2. Run the candidate suite from `workspace/project-under-test/`: `python -m pytest -p no:cacheprovider` for `stack=python`, or `mvn test` for `stack=java`.
3. Confirm coverage meets or exceeds the required 80 percent gate: `python scripts/check_coverage_gate.py --stack python --fail-under 80` for Python packages, or `python scripts/check_coverage_gate.py --stack java --project-dir . --fail-under 80` from the Java project root when the helper has been copied into the validation workspace. If the local Maven environment inherits an unavailable mirror or settings profile, use the Operator Layer override documented in `agent-control/operate-pipeline/commands-and-hooks.md`, for example `--maven-settings path/to/settings.xml --maven-global-settings path/to/settings.xml`, and record the exact paths in validation evidence. For Java packages, the coverage helper's `--fail-under` value must affect the Maven JaCoCo check. If the selected `pom.xml` hardcodes the JaCoCo minimum and ignores `-Dcoverage.minimum`, Themis may add a candidate `pom.xml` test/build configuration overlay under `agent-3-tests/outputs/` that keeps the default at `0.80` and wires JaCoCo `<minimum>${coverage.minimum}</minimum>`. Inventory this overlay as Maven test/build configuration. Do not modify runtime product source under `src/main/java/...` without explicit operator repair authorization.
4. Validate meaningful assertions, unit/integration balance, dry-run behavior, command behavior, hook behavior, privacy checks, fixture isolation, and repeated-run behavior.
5. Write compact text evidence under `agent-3-tests/evidence/`.
6. Confirm `outputs/inventory.md` lists only selectable test/config files and excludes runtime/tool outputs.
7. Confirm root product files, root `shared/`, and root test files were not changed unless the operator explicitly selected the package.
8. Write `agent-3-tests/validation-checklist.md` with commands, expected signals, actual results, blockers, and limitations.
9. Write `agent-3-tests/handoff.md` with run ID, targeted Hephaestus version, files created or modified, validation status, source spec mismatch status, known risks, and exact next suggested prompt.

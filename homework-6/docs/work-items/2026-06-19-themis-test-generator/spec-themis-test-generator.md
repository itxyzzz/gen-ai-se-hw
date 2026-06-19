# Themis Test Generator Spec

Work ID: `2026-06-19-themis-test-generator`
Short ID: `themis-test-generator`
Status: Draft
Harness release: `unknown`
Schema: `schema:spec.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `rule:lifecycle.documentation-matrix`, `rule:quality.spec-handoff`

## Goal

Create the Homework 6 Themis (Test Generator) automation control surface and the separate pipeline operation surfaces so later runs can generate, execute, select, and preserve tests for a named Hephaestus (Code Generator) software version without confusing one-time outer tooling with Themis' own responsibilities.

## Scope

- Add a tool-neutral Themis (Test Generator) control package under `homework-6/agent-control/generate-tests/`.
- Add Codex and Claude Code entrypoint wrappers for Themis test-generation runs.
- Add an explicit Claude Code slash-command wrapper for invoking Themis because the assignment mentions `commands/` and the operator requested both the skill and slash-command despite current Claude project-skill precedence.
- Add a separate tool-neutral pipeline operation package under `homework-6/agent-control/operate-pipeline/` for command and hook behavior.
- Create the pipeline operation surfaces once as Operator Layer maintained outer tools: `/run-pipeline`, `/validate-transactions`, matching modern skill wrappers, and the coverage gate hook.
- Define Themis as the Homework Automation Layer owner for generated test packages and test quality evidence only. Themis must consume and validate the one-time pipeline operation surfaces, not believe it owns or regenerates them as part of every test run.
- Define test-run preservation under `homework-6/docs/agent-runs/` with selected Hephaestus code-version traceability, source Athena spec traceability, output inventories, validation evidence, and selection records.
- Define quality gates that treat coverage as necessary but not sufficient: Themis should evaluate meaningful assertions, unit/integration coverage balance, fixture isolation, privacy/audit safety, command behavior, hook behavior, and regression protection for pipeline reruns.
- Update `homework-6/agents.md` only where needed to point to Themis control surfaces and clarify the test/code-generation boundary.

## Non-scope

- Do not invoke Themis to generate or select tests in this work item.
- Do not edit canonical generated product tests, runtime code, `shared/`, `archive/`, screenshots, README, HOWTORUN, or PR-description materials in this work item.
- Do not reduce Hephaestus' ability to create useful tests during code generation. Hephaestus may continue to generate smoke, characterization, and implementation-protection tests as part of Task 2; Themis owns test generation and test-quality evidence for the selected code package.
- Do not make Themis responsible for creating or maintaining the one-time outer command and hook surfaces. This work item may create those surfaces beside the Themis package, but the Themis workflow must treat them as external support tools to validate.
- Do not implement Task 4 custom MCP server files or configuration.
- Do not move documentation ownership from Clio (Documentation Generator) to Themis except for test and quality-gate evidence that Themis needs to validate its own work.
- Do not change the frozen assignment file `homework-6/TASKS.md`.
- Do not make generated transaction-system runtime components use Greek labels such as Athena, Hephaestus, Themis, or Clio.

## Current state

Homework 6 has a selected Athena (Spec Writer) run, a selected Hephaestus (Code Generator) run, and a newer Hephaestus candidate run. `docs/agent-runs/final-selection.md` currently records selected code run `20260618-223217-generate-code-python-primary`, with an explicit traceability caveat that it was generated from Athena run `20260618-003908-write-spec-python-replacement` while canonical `specification.md` now points to fresh Athena run `20260619-170102-write-spec-python-fresh`.

Canonical product code and tests already exist at the Homework 6 root. The existing Hephaestus-generated tests report coverage above the Task 3 80 percent gate in preserved evidence. This creates a realistic boundary issue: a coverage hook may never fail against the current full suite, so Themis needs a deliberate hook-trigger validation path that demonstrates blocking behavior without degrading the real test suite or lowering the committed coverage bar.

Existing patterns for Athena and Hephaestus use:

- Tool-neutral packages under `homework-6/agent-control/`.
- Codex wrappers under concrete paths such as `homework-6/.agents/skills/generate-code/SKILL.md`.
- Claude Code project skills under concrete paths such as `homework-6/.claude/skills/generate-code/SKILL.md`.
- Preserved run evidence under `homework-6/docs/agent-runs/RUN_ID/`.
- Final-selection and version traceability through `homework-6/docs/agent-runs/final-selection.md`.

The assignment's Task 3 requires command surfaces under `.../commands/` and a coverage gate hook that blocks push or fails the action below 80 percent. The operator explicitly requested both skill and slash-command surfaces for the command ambiguity. These are outer support surfaces for the homework, not Themis-generated test artifacts.

Task 5 also says Agent 4 is responsible for "Testing & Documentation" and repeats the test-suite requirements. A coherent interpretation is:

- Hephaestus (Code Generator) may generate baseline tests as part of proving generated code works.
- The pipeline operation package supplies reusable commands and coverage hook infrastructure once.
- Themis (Test Generator) is the primary author and selector of the rigorous test suite for a named selected code package.
- Clio (Documentation Generator) is the final documentation and evidence owner. Clio validates and documents the selected Themis suite, captures screenshots, and may request a Themis repair or record a tightly scoped final test hardening only when final documentation review finds a gap. Clio must not silently fork or replace the selected Themis suite.

## Proposed behavior

Add two cleanly delineated pieces.

First, add a Themis (Test Generator) automation package that mirrors the Athena and Hephaestus durable shape while targeting tests for selected code packages:

```text
homework-6/agent-control/generate-tests/
  README.md
  workflow.md
  quality-bar.md
  run-registry.md
```

Add Themis entrypoints:

```text
homework-6/.agents/skills/generate-tests/SKILL.md
homework-6/.claude/skills/generate-tests/SKILL.md
homework-6/.claude/commands/generate-tests.md
```

Second, add pipeline operation surfaces as a one-time Operator Layer outer-tool package during implementation. These are not Themis responsibilities, even though Themis will later validate that they work with the selected test package:

```text
homework-6/agent-control/operate-pipeline/
  README.md
  commands-and-hooks.md
homework-6/.claude/commands/run-pipeline.md
homework-6/.claude/commands/validate-transactions.md
homework-6/.claude/skills/run-pipeline/SKILL.md
homework-6/.claude/skills/validate-transactions/SKILL.md
homework-6/.agents/skills/run-pipeline/SKILL.md
homework-6/.agents/skills/validate-transactions/SKILL.md
```

The Claude `commands/` files satisfy the assignment's explicit path language. The Claude and Codex project skills keep the surfaces discoverable in modern tool runtimes and point to the pipeline operation package for behavior. Themis must not treat these files as per-run outputs.

Add hook configuration and helper scripts as one-time outer tools. These are maintained by the Operator Layer and validated by Themis runs:

```text
homework-6/.claude/settings.json
homework-6/.githooks/pre-push
homework-6/scripts/check_coverage_gate.py
```

The implementation may adjust helper names if local inspection finds an existing project convention, but the committed behavior must provide a coverage gate that fails below 80 percent. The hook should run the stack's coverage command, default to `80`, and support an isolated demonstration override such as an environment variable or script argument that raises the threshold for hook-trigger evidence. That allows a Themis validation run or later Clio evidence run to demonstrate hook failure without intentionally worsening real tests.

A normal Themis run should:

1. Load the selected code-version record from `docs/agent-runs/final-selection.md`.
2. Refuse to target an unnamed "latest" code package.
3. Record selected Hephaestus run ID, selected output inventory path, selection record path, canonical code paths, file/package fingerprints, source Athena run ID, canonical spec path, and canonical spec fingerprint.
4. Detect and report source-spec mismatch between the selected code package and current canonical `specification.md`. Themis may still target the selected code package when the operator explicitly wants to test the selected software version, but it must not hide the mismatch.
5. Preserve the test-generation attempt under `docs/agent-runs/RUN_ID/agent-3-tests/`.
6. Copy the selected Hephaestus package into a run-local execution workspace before writing tests, so test authoring and execution can happen without mutating root product files.
7. Write candidate tests and test configuration under `agent-3-tests/outputs/`, then overlay those outputs into the run-local workspace for execution.
8. Run tests from the workspace with runtime output redirected into the run folder, such as workspace-local `shared/`, `.test-tmp/`, and coverage reports.
9. Validate the externally maintained `/run-pipeline`, `/validate-transactions`, and coverage hook surfaces against the selected workspace and record findings. If an outer tool is missing or stale, Themis reports the gap or requests an Operator Layer repair; it does not regenerate those surfaces as routine per-run output.
10. Select or stage the accepted test package only through an inventory-driven selection record.

## Test workspace protocol

Themis needs a deterministic file and folder protocol so it can write and execute tests while preserving run outputs:

```text
homework-6/docs/agent-runs/RUN_ID/
  run-metadata.md
  inputs/
    source-context.md
    selected-code-inventory.snapshot.md
  agent-3-tests/
    outputs/
      tests/
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
```

`workspace/selected-code/` is copied from the selected Hephaestus inventory-declared package or from the selected canonical paths named by that inventory. `workspace/project-under-test/` is built by copying `selected-code/` and overlaying candidate files from `agent-3-tests/outputs/`. Tests run only from `project-under-test/` during ordinary Themis generation. Root `tests/`, root `shared/`, root `.coverage`, and root product files remain untouched until an explicit test-package selection step.

`agent-3-tests/outputs/` is the selectable candidate test package. Its `inventory.md` lists each candidate test/config file, intended canonical target, kind, SHA-256 fingerprint, and excluded runtime/tool-output paths. `workspace/` and `evidence/` are run evidence, not canonical test-copy targets. Themis may preserve compact text evidence from coverage and from validation of external support tools, but `.coverage`, `.pytest_cache/`, `__pycache__/`, `.test-tmp/`, and archive folders are tool/runtime outputs and must not be listed as selectable tests.

During selection, Themis copies only inventory-declared files from `agent-3-tests/outputs/` to canonical targets. It must never copy the workspace wholesale to the root.

Themis' quality bar should include coverage, but should also require:

- Unit tests for each runtime component.
- At least one integration test for the full pipeline.
- Tests for rejection reasons, review reasons, settlement paths, summary counts, repeated-run archival, runtime provenance when present, strict JSON behavior, Decimal money handling, and dry-run validator behavior.
- Privacy tests or scans proving raw account IDs and raw descriptions do not leak to results, audit files, command output, or screenshots.
- Hook-trigger evidence that proves the externally maintained gate blocks when configured below the required result.
- Meaningful assertions over output fields and reason codes, not only "does not crash" smoke tests.

## Interfaces and data

Expected files to create during implementation:

- `homework-6/agent-control/generate-tests/README.md`
- `homework-6/agent-control/generate-tests/workflow.md`
- `homework-6/agent-control/generate-tests/quality-bar.md`
- `homework-6/agent-control/generate-tests/run-registry.md`
- `homework-6/agent-control/operate-pipeline/README.md`
- `homework-6/agent-control/operate-pipeline/commands-and-hooks.md`
- `homework-6/.agents/skills/generate-tests/SKILL.md`
- `homework-6/.claude/skills/generate-tests/SKILL.md`
- `homework-6/.claude/commands/generate-tests.md`
- `homework-6/.agents/skills/run-pipeline/SKILL.md`
- `homework-6/.agents/skills/validate-transactions/SKILL.md`
- `homework-6/.claude/skills/run-pipeline/SKILL.md`
- `homework-6/.claude/skills/validate-transactions/SKILL.md`
- `homework-6/.claude/commands/run-pipeline.md`
- `homework-6/.claude/commands/validate-transactions.md`
- `homework-6/.claude/settings.json`
- `homework-6/.githooks/pre-push`
- `homework-6/scripts/check_coverage_gate.py`

Expected files to update during implementation:

- `homework-6/agents.md`
- `homework-6/CHANGELOG.md`

Files to read but not modify during this control-surface implementation unless a variance is approved:

- `homework-6/TASKS.md`
- `homework-6/specification.md`
- `homework-6/docs/agent-runs/final-selection.md`
- `homework-6/agent-control/write-spec/*`
- `homework-6/agent-control/generate-code/*`
- Canonical generated product code and tests such as `integrator.py`, `agents/*.py`, `tests/*.py`, `pytest.ini`, and `research-notes.md`

Future Themis runs governed by this package may create or update:

- `homework-6/tests/*.py`
- `homework-6/pytest.ini` or stack-equivalent test configuration when needed for coverage collection.
- `homework-6/docs/agent-runs/RUN_ID/agent-3-tests/outputs/`

Those future generated-test and evidence paths are not implementation targets for this planning package.

## Risks

- Themis could silently test the wrong software version. Mitigation: require selected Hephaestus run ID, inventory path, selection record, source spec fingerprint, and code fingerprints before generation.
- The selected Hephaestus code version may not match the current canonical spec. Mitigation: require the mismatch to be reported and require operator intent before claiming fresh-spec coverage.
- The coverage hook could be untestable because real coverage is already high. Mitigation: support an isolated demonstration threshold or temporary fixture path that proves failure without degrading committed tests.
- Pipeline operation command surfaces could drift between Claude command files and project skills. Mitigation: keep commands thin and route shared behavior through the separate pipeline operation package, not through Themis.
- Themis could duplicate outer tools on every test-generation run. Mitigation: classify commands, skills, hooks, and helper scripts as Operator Layer one-time support surfaces; normal Themis runs validate them and report gaps rather than regenerate them.
- Themis could accidentally mutate root tests or runtime outputs while validating a candidate. Mitigation: require run-local selected-code and project-under-test workspaces under the run folder, with root copying only during explicit selection.
- Themis could overfit coverage percentage and miss quality. Mitigation: quality bar requires behavior assertions, fixture isolation, privacy checks, command checks, hook checks, and regression scenarios.
- Hook configuration could be tool-specific. Mitigation: implement both a portable Git pre-push hook path and the Claude settings hook surface required by the assignment where feasible.
- Generated tests or command output could leak sensitive sample data. Mitigation: require privacy scans and redacted output checks.

## Acceptance criteria

- A new `agent-control/generate-tests/` package exists with README, workflow, quality bar, and run registry.
- Codex and Claude Code `generate-tests` project skill entrypoints exist and are thin wrappers around the shared package.
- A Claude `commands/generate-tests.md` wrapper exists because the operator requested both the skill and slash-command surface.
- A separate `agent-control/operate-pipeline/` package exists and owns the command and hook behavior contract.
- One-time pipeline operation surfaces exist for `/run-pipeline` and `/validate-transactions` under Claude `commands/`, and matching project-skill wrappers exist for Claude and Codex. The Themis workflow labels them as external support tools to validate, not Themis per-run outputs.
- One-time hook configuration exists for a coverage gate that fails when coverage is below 80 percent, with helper script behavior that can demonstrate the blocking path without lowering the committed gate. The Themis workflow labels this as an external support tool to validate, not a per-run Themis artifact.
- The Themis workflow requires a named selected Hephaestus version and records selected code run ID, output inventory path, selection record, source Athena run ID, spec fingerprint, and code fingerprints.
- The Themis workflow detects selected-code/source-spec mismatch and requires it to be reported before generating or selecting tests.
- The Themis workflow defines the run-local workspace protocol for copying selected code, overlaying candidate tests, executing tests, preserving evidence, and selecting only inventory-declared output files.
- The quality bar treats 80 percent coverage as a floor and requires broader test-quality checks for assertions, fixture isolation, command behavior, hook behavior, privacy, dry-run validation, and integration/rerun behavior.
- The Themis workflow explains how Clio (Documentation Generator) should consume selected Themis results for final Task 5 testing/documentation evidence without silently replacing the selected test suite.
- `homework-6/agents.md` references Themis (Test Generator)'s control surface and clarifies the boundary with Hephaestus (Code Generator) without changing layer semantics.
- `homework-6/TASKS.md`, `homework-6/specification.md`, canonical generated code, and canonical generated tests are unchanged by this control-surface implementation unless a later approved variance explicitly changes them.
- `homework-6/CHANGELOG.md` records the Themis planning and implementation increments before commits.

## Documentation artifact matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Before each commit | `homework-6/CHANGELOG.md` | Newest-first Homework 6 entries for planning freeze and later control-surface implementation |
| Test cases | Snapshot | No | Not applicable | `homework-6/docs/work-items/2026-06-19-themis-test-generator/snapshots/test-cases.snapshot.md` | This work item creates Themis controls; generated transaction-system tests are future run outputs |
| Testing guide delta | Living delta | No | Not applicable | `homework-6/docs/work-items/2026-06-19-themis-test-generator/deltas/testing-guide.delta.md` | Long-lived testing docs belong to later Clio or documentation work |
| Operator manual delta | Living delta | No | Not applicable | `homework-6/docs/work-items/2026-06-19-themis-test-generator/deltas/operator-manual.delta.md` | The new control package and `agents.md` are updated directly |
| API reference delta | Living delta | No | Not applicable | `homework-6/docs/work-items/2026-06-19-themis-test-generator/deltas/api-reference.delta.md` | No runtime API or MCP API is changed in this control-surface work |
| Architecture snapshot | Snapshot | No | Not applicable | `homework-6/docs/work-items/2026-06-19-themis-test-generator/snapshots/architecture.snapshot.md` | Control-surface architecture is captured in this spec and plan |
| Architecture summary delta | Living delta | No | Not applicable | `homework-6/docs/work-items/2026-06-19-themis-test-generator/deltas/architecture-summary.delta.md` | No long-lived generated-system architecture document exists yet |

## Approval

- Status: Draft
- Superseded by: None

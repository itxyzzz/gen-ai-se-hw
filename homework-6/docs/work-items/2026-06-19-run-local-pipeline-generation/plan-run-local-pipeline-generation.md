# Run-Local Pipeline Generation Plan

Work ID: `2026-06-19-run-local-pipeline-generation`
Short ID: `run-local-pipeline-generation`
Status: Draft
Harness release: unknown
Schema: `schema:plan.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:models.strategy-required`, `rule:models.context-strategy`, `rule:models.approved-strategy-authorized`, `rule:models.fresh-confirmation`, `rule:lifecycle.variance-policy`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, `rule:freeze.stop-before-implementation`

## Implementation summary

This is a one-phase control-surface update. The implementation should not touch current generated runtime code or the static `transaction-system-brief.md`. It should update the quality and workflow rules that future Athena and Hephaestus runs consume.

The plan separates product intent from execution mechanics. Athena quality rules will describe what a generated specification must require: four cooperating runtime components, Reporting Agent as the default fourth component, and a generated spec that gives Hephaestus enough detail to produce run-local candidate behavior. Hephaestus workflow rules will describe how a code-generation run validates a candidate: copy the canonical sample input into the candidate package, run tests and pipeline smoke commands from inside the package, validate local archival, and keep runtime/tool artifacts out of selectable inventory.

The `.gitignore` change closes the evidence hygiene gap by ignoring Homework 6 runtime `archive/` folders while allowing `shared/` folders to be committed as last-run evidence. Root `shared/` is the primary reviewer-visible runtime evidence; run-local `shared/` may also be preserved when useful, but it remains non-selectable code output.

## Files and interfaces

Expected modifications:

- Modify `agent-control/write-spec/quality-bar.md`
  - Add at-least-four-component generated-spec requirement.
  - Add Reporting Agent as the default fourth runtime component.
  - Add a quality-gate note that run-local candidate execution expectations belong in control guidance and generated low-level task cards, while `transaction-system-brief.md` remains static.
- Modify `agent-control/generate-code/workflow.md`
  - Add candidate package fixture setup: copy `sample-transactions.json` to `agent-2-code/outputs/sample-transactions.json`.
  - Change validation commands to run from `agent-2-code/outputs/` using local `shared/` and `archive/`.
  - Reserve root-level smoke runs for explicit canonical-selection validation.
- Modify `agent-control/generate-code/quality-bar.md`
  - Require self-contained candidate validation and local repeated-run archive evidence.
  - Require output inventory to distinguish selectable code files from runtime evidence: local `shared/` is commit-capable evidence but not selectable code; local `archive/` and tool artifacts remain excluded.
  - Mention `Reporting Agent` as the preferred fourth component when the selected spec requires at least four components.
- Modify repository root `.gitignore`
  - Remove or override `shared/` ignore patterns so Homework 6 `shared/` folders can be committed as last-run evidence.
  - Add or keep ignore patterns for root and nested Homework 6 `archive/` folders and Python/pytest artifacts relevant to candidate runs.

Expected stable files:

- `agent-control/write-spec/transaction-system-brief.md` remains unchanged.
- `specification.md` remains unchanged.
- Canonical runtime code and tests remain unchanged.
- `mcp.json` and `.codex/config.toml` remain unchanged.

## Model and Sub-agent Strategy

Current orchestration: Codex Desktop agent; exact model/profile and reasoning control are not exposed in this turn.
Fit assessment: Small/medium documentation and workflow-control update with moderate downstream blast radius. The highest risk is leaking operator-run mechanics into product specs or touching the static brief.
Recommended change: Use the active orchestration thread with careful static validation. No sub-agents are needed for implementation because the file set is small, tightly related, and mostly documentation/control text.

Sub-agents: None. Rationale: the work has one coherent control-surface boundary, no independent code modules, and review risk is better handled by direct diff inspection plus targeted static scans.

## Tasks

- [ ] Confirm pre-implementation context.
  - Read `agent-control/write-spec/quality-bar.md`, `agent-control/generate-code/workflow.md`, `agent-control/generate-code/quality-bar.md`, `.gitignore`, `agents.md`, and this work item package.
  - Run `git status --short` and note pre-existing unrelated changes.

- [ ] Update Athena quality rules without editing the static brief.
  - Modify `agent-control/write-spec/quality-bar.md`.
  - Add language under `Specification Shape` or `Low-Level Task Card Standard` requiring generated specifications to target at least four cooperating runtime transaction pipeline components.
  - Add language naming Reporting Agent as the default fourth component unless an operator explicitly chooses Compliance Checker or another product component.
  - Define Reporting Agent as a stack-native runtime component responsible for audit-safe summaries, `summary.json`, `pipeline-status.json`, result completeness checks, status count consistency, and final privacy checks.
  - Add a note that candidate run-folder setup and validation mechanics belong in Hephaestus workflow/control guidance and generated low-level task cards, not in `transaction-system-brief.md`.

- [ ] Update Hephaestus run setup guidance.
  - Modify `agent-control/generate-code/workflow.md`.
  - In `Run Setup`, require `agent-2-code/outputs/sample-transactions.json` as a copied fixture from canonical `homework-6/sample-transactions.json`.
  - Require run metadata or inventory to record the canonical sample source path and SHA-256 fingerprint.
  - State that candidate validation commands run from `agent-2-code/outputs/` and use the copied local sample by default.

- [ ] Update Hephaestus generation scope and validation guidance.
  - Modify `agent-control/generate-code/workflow.md`.
  - In `Generation Scope`, update the component requirement to at least four cooperating runtime transaction pipeline components when the selected spec requires the refreshed Athena quality target.
  - Add Reporting Agent to the expected component set after Settlement Processor.
  - In `Validation And Handoff`, replace ordinary candidate smoke guidance with:
    - Run `python integrator.py --input sample-transactions.json --shared-dir shared` from `agent-2-code/outputs/`.
    - Run it a second time from the same folder.
    - Verify local `archive/shared-001` exists and current local `shared/results/summary.json` is fresh.
    - Run tests from `agent-2-code/outputs/` using a workspace-local temp root such as `python -m pytest --basetemp .test-tmp`.
    - Clean `.test-tmp`, `.coverage*`, `.pytest_cache/`, and `__pycache__/` after validation.
    - Preserve local `shared/` as current last-run evidence when the run package is committed, while keeping it out of the selectable code inventory.
  - State that root-level pipeline smoke is optional and requires explicit operator authorization or selection-stage validation.

- [ ] Update Hephaestus quality-bar checks.
  - Modify `agent-control/generate-code/quality-bar.md`.
  - Add `Reporting Agent` to the normal four-component quality target.
  - Add rejection criteria when a candidate package lacks `sample-transactions.json`.
  - Add rejection criteria when candidate validation mutates root `shared/` during ordinary generate mode.
  - Add validation criteria for local `shared/`, local `archive/shared-001`, local summary counts, and candidate output hygiene.
  - Add inventory guidance that `shared/` is runtime evidence, not selectable code, and should not be listed as a canonical copy target.

- [ ] Update root `.gitignore`.
  - Modify `../.gitignore` from the `homework-6` working directory.
  - Remove the existing `homework-6/shared/` ignore pattern so root `shared/` can be committed as reviewer-visible last-run evidence.
  - Do not add any nested `shared/` ignore patterns.
  - Keep or add archive/tool patterns:

```text
# Homework 6 generated runtime evidence and candidate tool output
homework-6/**/archive/
homework-6/**/.pytest_cache/
homework-6/**/.test-tmp/
homework-6/**/__pycache__/
homework-6/**/*.pyc
homework-6/**/.coverage*
```

  - It is acceptable to replace the existing `homework-6/archive/` line with the recursive `homework-6/**/archive/` pattern if `git check-ignore` verifies both root and nested archives are ignored.

- [ ] Run static validation.
  - Run `git diff -- agent-control/write-spec/transaction-system-brief.md` and verify no output.
  - Run `Select-String -Path agent-control\write-spec\quality-bar.md -Pattern 'at least four','Reporting Agent','transaction-system-brief'` and verify all three concepts are present.
  - Run `Select-String -Path agent-control\generate-code\workflow.md -Pattern 'sample-transactions.json','agent-2-code/outputs','--basetemp','archive/shared-001','root-level'` and verify all five concepts are present.
  - Run `Select-String -Path agent-control\generate-code\quality-bar.md -Pattern 'Reporting Agent','sample-transactions.json','archive/shared-001','root shared'` and verify the concepts are present.
  - Run `git check-ignore homework-6/archive/shared-001/example.json homework-6/docs/agent-runs/example/agent-2-code/outputs/archive/shared-001/example.json` from the repository root and verify both archive paths are ignored.
  - Run `git check-ignore homework-6/shared/results/summary.json homework-6/docs/agent-runs/example/agent-2-code/outputs/shared/results/summary.json` from the repository root and verify neither shared path is ignored; the command should exit nonzero with no path output.
  - Run `git diff -- mcp.json .codex\config.toml specification.md integrator.py agents tests` and verify no output.

- [ ] Update `CHANGELOG.md` before the implementation commit.
  - Add a newest-first entry describing the Athena/Hephaestus control-surface update, Reporting Agent default fourth-component guidance, run-local candidate validation, archive-only ignore rules, and committed `shared/` last-run evidence.

- [ ] Review the diff.
  - Confirm no generated runtime package, root `archive/`, Task 3 command, Task 4 MCP config, Task 5 documentation, or canonical selected spec/code files were modified. Root `shared/` may be committed later as last-run evidence only when a pipeline run intentionally refreshes it.
  - Confirm there are no placeholder markers in changed planning/control files using a PowerShell pattern array such as `@('TO' + 'DO', 'TB' + 'D', 'PLACE' + 'HOLDER', 'FIX' + 'ME')`.

## Validation commands

| Command | Expected result |
|---|---|
| `git diff -- agent-control/write-spec/transaction-system-brief.md` | No output |
| `Select-String -Path agent-control\write-spec\quality-bar.md -Pattern 'at least four','Reporting Agent','transaction-system-brief'` | Matches for all three concepts |
| `Select-String -Path agent-control\generate-code\workflow.md -Pattern 'sample-transactions.json','agent-2-code/outputs','--basetemp','archive/shared-001','root-level'` | Matches for all five concepts |
| `Select-String -Path agent-control\generate-code\quality-bar.md -Pattern 'Reporting Agent','sample-transactions.json','archive/shared-001','shared/ is runtime evidence'` | Matches showing four-component, run-local checks, and committed shared evidence |
| `git check-ignore homework-6/archive/shared-001/example.json homework-6/docs/agent-runs/example/agent-2-code/outputs/archive/shared-001/example.json` from repository root | Both archive paths are printed as ignored |
| `git check-ignore homework-6/shared/results/summary.json homework-6/docs/agent-runs/example/agent-2-code/outputs/shared/results/summary.json` from repository root | No paths are printed and the command exits nonzero, proving shared folders are not ignored |
| `git diff -- mcp.json .codex\config.toml specification.md integrator.py agents tests` | No output |
| `$patterns = @('TO' + 'DO', 'TB' + 'D', 'PLACE' + 'HOLDER', 'FIX' + 'ME'); Select-String -Path agent-control\write-spec\quality-bar.md,agent-control\generate-code\workflow.md,agent-control\generate-code\quality-bar.md,..\.gitignore -Pattern $patterns` | No matches |

## Plan variance handling

Use `rule:lifecycle.variance-policy`. Before freeze, edit this draft directly for operator feedback. After freeze, record nontrivial implementation variance in `implementation-notes/variance-log.md`; use a plan amendment for high-impact architecture, API, data, security, privacy, compliance, scope, acceptance-criteria, or feasibility changes.

Expected low-risk variance:

- The implementation may place the Reporting Agent requirement under a different existing heading if that keeps the quality bar clearer.
- The implementation may use equivalent PowerShell commands for validation if path quoting differs on Windows.

High-impact variance requiring operator confirmation:

- Editing `transaction-system-brief.md`.
- Regenerating or selecting `specification.md`.
- Modifying canonical runtime code or tests.
- Adding Task 3/4/5 deliverables.
- Choosing Compliance Checker instead of Reporting Agent as the default fourth component.

## Planning artifact freeze gate

Use `module:freeze-gate`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, and `rule:freeze.stop-before-implementation`.

Draft review status: ready for operator review after static draft validation.
Approval commit: not created.
Post-freeze implementation authorization: not granted.

## Completion criteria

- Acceptance criteria in `spec-run-local-pipeline-generation.md` are met.
- Required validation commands have been run and recorded.
- `CHANGELOG.md` has a newest-first entry for the implementation.
- Diff review confirms `transaction-system-brief.md`, current canonical spec/code, MCP config, commands/hooks, and docs/screenshots are untouched.
- De-facto sub-agent use is reported as none, unless the operator explicitly changes the implementation strategy.

## Approval

- Status: Draft
- Superseded by: not superseded

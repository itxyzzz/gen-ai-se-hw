# Code Run Preservation Repair Plan

Work ID: `2026-06-18-code-run-preservation-repair`
Short ID: `code-run-preservation-repair`
Status: Draft
Harness release: `unknown`
Schema: `schema:plan.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:models.strategy-required`, `rule:models.context-strategy`, `rule:models.approved-strategy-authorized`, `rule:models.fresh-confirmation`, `rule:lifecycle.variance-policy`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, `rule:freeze.stop-before-implementation`

## Implementation summary

Implement the repair in two connected tracks.

First, normalize Hephaestus run evidence and selection. The existing canonical generated product package becomes the selected contents of the first Hephaestus run by copying it into `docs/agent-runs/20260618-223217-generate-code-python-primary/agent-2-code/outputs/` and adding an inventory. Selection metadata records that this first successful code run is selected by default and that it was generated from selected Athena run `20260618-003908-write-spec-python-replacement`. Future Hephaestus workflow docs will require every code-generation run to produce a complete candidate output package inside its run folder before canonical copy and to record the source spec run ID plus a stable spec content fingerprint.

Second, update generated pipeline behavior and the upstream Athena/Hephaestus prompts. The canonical Python integrator will archive an existing `shared/` tree to zero-padded incrementing folders such as `archive/shared-001` and `archive/shared-002` before creating fresh protocol directories. Tests will cover repeated runs using temporary directories. Athena guidance will require generated specifications to describe that product-level rerun behavior; Hephaestus guidance will own run-folder evidence preservation, source-spec traceability, and selection rules.

Third, add a forward traceability rule for later Themis (Test Generator) passes. Test-generation runs may also happen in multiple attempts, so each test run and selected test package must name the selected Hephaestus software version it targets rather than implicitly testing whichever canonical files happen to be current.

No Task 3 command surfaces, Task 4 MCP server/config, or Task 5 documentation/screenshots are implemented in this work item.

## Files and interfaces

- Modify `homework-6/integrator.py` to archive existing shared output before preparing fresh directories.
- Modify or add tests under `homework-6/tests/`, likely `tests/test_pipeline_end_to_end.py`, to validate archive behavior using temporary directories.
- Create `homework-6/docs/agent-runs/20260618-223217-generate-code-python-primary/agent-2-code/outputs/` with run-local copies of selected generated source, tests, and research notes.
- Add `homework-6/docs/agent-runs/20260618-223217-generate-code-python-primary/agent-2-code/outputs/inventory.md`.
- Update `homework-6/docs/agent-runs/final-selection.md` or add a clearly linked code-selection record under `homework-6/docs/agent-runs/`.
- Add source-spec provenance to Hephaestus run metadata, output inventory, and selection records, including selected Athena run ID and a stable `specification.md` fingerprint.
- Update `homework-6/agent-control/generate-code/workflow.md`, `run-registry.md`, and `quality-bar.md`.
- Update `homework-6/agent-control/write-spec/transaction-system-brief.md` and `quality-bar.md`; update `workflow.md` only if needed for clarity.
- Update `homework-6/agents.md` only if a concise standing rule is needed to help later agents find the code-selection contract.
- Update `homework-6/CHANGELOG.md` before implementation and final commits.

Stable exclusions:

- Do not copy `shared/`, `archive/`, `.coverage`, `__pycache__/`, or `.pytest_cache/` into run-local selectable outputs.
- Do not modify `homework-6/mcp.json` or `homework-6/.codex/config.toml`.
- Do not modify `homework-6/TASKS.md`.

## Model and Sub-agent Strategy

Current orchestration: Codex desktop; exact model and reasoning controls are not exposed in the current UI.
Fit assessment: Medium complexity with moderate blast radius. The work spans docs, run evidence, generated code, and tests, but all changes are local to Homework 6 and avoid public API/MCP changes. Correctness matters because this becomes the process contract for later agents.
Recommended change: Use the active `enterprise-default` policy intent. Keep orchestration on the strongest available local profile if the UI exposes a choice before implementation. Use no write-capable sub-agents by default because the file edits are coupled through a single selection and rerun contract.

Sub-agents: None for the default implementation pass. Rationale: the safest implementation path is one orchestrator-owned patch across tightly coupled control docs, evidence inventory, and tests. A read-only reviewer sub-agent may be used after implementation if the runtime exposes sub-agents and the operator confirms that policy after the freeze gate.

## Tasks

- [ ] Confirm branch is `homework-6-submission` and review dirty worktree so only intended files are staged.
- [ ] Create `agent-2-code/outputs/` in the existing Hephaestus run folder.
- [ ] Copy selected generated source files into the run-local outputs package: `integrator.py`, `agents/__init__.py`, `agents/common.py`, `agents/transaction_validator.py`, `agents/fraud_detector.py`, and `agents/settlement_processor.py`.
- [ ] Copy selected generated tests into the run-local outputs package under `tests/`.
- [ ] Copy selected `research-notes.md` into the run-local outputs package.
- [ ] Add `outputs/inventory.md` listing each selectable source or research file, its canonical target, and excluded runtime/tool paths.
- [ ] Add provenance metadata to the current Hephaestus run and inventory: source Athena run ID `20260618-003908-write-spec-python-replacement`, canonical source `specification.md`, and the SHA-256 fingerprint of the source specification used for this code version.
- [ ] Update selection metadata to record `20260618-223217-generate-code-python-primary` as the first successful selected Hephaestus code-generation run and link it to the selected Athena source spec.
- [ ] Write the failing repeated-run archive test using temporary directories and the existing sample input.
- [ ] Run the targeted archive test and confirm it fails because current `prepare_shared_directories` clears `shared/` rather than archiving it.
- [ ] Implement archive helpers in `integrator.py`: derive archive root, choose the next available zero-padded folder name such as `shared-001` or `shared-002`, move the existing shared tree, and create fresh protocol directories.
- [ ] Keep `--shared-dir` behavior backward compatible while archiving beside the configured shared path.
- [ ] Run the targeted archive test and confirm it passes.
- [ ] Run the existing Task 2 test suite and pipeline command from `homework-6`.
- [ ] Update Hephaestus workflow and run registry so future runs produce `agent-2-code/outputs/`, an inventory, source-spec provenance, first-success auto-selection, explicit later selection, and clean canonical replacement from selected output packages.
- [ ] Update Hephaestus quality bar to reject missing run-local code outputs, missing source-spec provenance, and copied `shared/` or `archive/` runtime folders in selectable packages.
- [ ] Update Athena product brief and quality bar so generated transaction-system specs require rerunnable pipeline startup that archives prior shared output before creating fresh protocol directories.
- [ ] Add future Themis traceability guidance to the standing Homework 6 control surfaces so test-generation runs record the selected Hephaestus code version and package fingerprint they target.
- [ ] Review generated spec boundary text to ensure Athena guidance describes product behavior only and does not tell the Generated Transaction System Layer to implement Operator Layer selection mechanics.
- [ ] Update `CHANGELOG.md` with the implementation step before committing implementation files.
- [ ] Run final validation commands and record results in the user-facing completion report.
- [ ] Review `git diff` for unrelated changes, unresolved template markers, task-scope leakage, and accidental generated noise.

## Validation commands

| Command | Expected result |
|---|---|
| `git status --short --branch` | Branch is `homework-6-submission`; unrelated existing generated files are recognized and not accidentally removed |
| `python -m pytest tests/test_pipeline_end_to_end.py -v` | Integration and archive behavior tests pass |
| `python -m pytest --cov=. --cov-fail-under=75` | Existing Task 2 suite still passes at or above the temporary 75% gate |
| `python integrator.py` | Pipeline completes with all 8 sample transactions accounted for and creates a fresh `shared/` tree |
| `python integrator.py` run a second time | Previous `shared/` tree is archived to the next available zero-padded archive folder, such as `archive/shared-001`, and a new `shared/results/summary.json` is created |
| `git diff -- mcp.json .codex/config.toml TASKS.md` | No diff |
| `Get-ChildItem -Recurse docs/agent-runs/20260618-223217-generate-code-python-primary/agent-2-code/outputs` | Run-local selected code, tests, research notes, and inventory are present; runtime folders are absent |
| `Select-String -Path docs/agent-runs/final-selection.md,docs/agent-runs/20260618-223217-generate-code-python-primary/**/*.md -Pattern '20260618-003908-write-spec-python-replacement|source spec|SHA-256|software version'` | Code run metadata, inventory, or selection records tie the Hephaestus version to the selected Athena spec and describe target software-version traceability |
| `Select-String -Path agent-control/write-spec/*.md,agent-control/generate-code/*.md -Pattern 'dev-doc-harness|Superpowers|freeze gate'` | Matches, if any, remain limited to Operator Layer control-surface warnings and are not instructions for generated product code |

## Plan variance handling

Use `rule:lifecycle.variance-policy`. Before freeze, edit this draft directly for operator feedback. After freeze, record nontrivial implementation variance in `implementation-notes/variance-log.md`; use a plan amendment for high-impact architecture, API, data, security, privacy, compliance, scope, acceptance-criteria, or feasibility changes.

## Planning artifact freeze gate

Use `module:freeze-gate`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, and `rule:freeze.stop-before-implementation`.

Draft review status: Draft package created for operator review. It must be staged without unrelated generated code or runtime outputs before approval.

Approval commit: Not created yet.

Post-freeze implementation authorization: Not authorized yet. Implementation begins only after a fresh explicit operator instruction after the freeze gate.

## Completion criteria

- Acceptance criteria in `spec-code-run-preservation-repair.md` are met.
- Required validation commands have been run and recorded.
- Required documentation artifacts have been created or updated.
- `CHANGELOG.md` has a newest-first entry for the work before each commit.
- Variance log is present and current.
- De-facto sub-agent use is reported when applicable, including count, roles/scopes, concurrency or waves, context strategy, observed inheritance behavior, and de-facto model/model class/profile when known.

## Approval

- Status: Draft
- Superseded by: not applicable

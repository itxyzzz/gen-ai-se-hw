# Homework 6 Review Repair Plan

Work ID: `2026-06-21-homework-6-review-repair`
Short ID: `homework-6-review-repair`
Status: Draft
Harness release: `unknown`
Schema: `schema:plan.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:models.strategy-required`, `rule:models.context-strategy`, `rule:models.approved-strategy-authorized`, `rule:models.fresh-confirmation`, `rule:lifecycle.variance-policy`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, `rule:freeze.stop-before-implementation`

## Implementation summary

Implement the accepted review repairs as a control-surface update only. Update the portable guidance so Clio (Documentation Generator), Hephaestus (Code Generator), and Athena (Spec Writer) express the intended evidence, component-count, and command-surface decisions clearly. Do not repair the selected Clio documentation package, canonical reviewer docs, stable screenshots, or PR draft in this work item; a clean Clio regeneration and selection should happen as a separate follow-up step.

The work should keep layer terminology explicit. Athena, Hephaestus, Themis, and Clio are Homework Automation Layer agents. Transaction Validator, Fraud Detector, Settlement Processor, and optional Reporting Agent are Generated Transaction System Layer runtime components. Context7 and `pipeline-status` are MCP support surfaces. Slash commands, project skills, hooks, and screenshot capture are Operator Layer support surfaces or homework automation entrypoints, not runtime transaction components.

No runtime or documentation-package regeneration is planned. The current selected runtime package remains assignment-compliant with three cooperating components. The fourth-component language is a deliberate quality target for refreshed generation, not a retroactive defect in the selected package.

## Files and interfaces

Expected control-surface changes:

- `agent-control/generate-docs/workflow.md`
- `agent-control/generate-docs/quality-bar.md`
- `agent-control/generate-docs/run-registry.md`
- `agent-control/generate-code/workflow.md`
- `agent-control/generate-code/quality-bar.md`
- `agent-control/generate-code/run-registry.md`
- `agent-control/write-spec/README.md`, `agent-control/write-spec/workflow.md`, or `agent-control/write-spec/quality-bar.md` as needed to document the Claude project-skill decision
- `agents.md`

- `CHANGELOG.md`

Expected stable interfaces:

- Runtime code, tests, command behavior, hook behavior, MCP server behavior, `mcp.json`, and `.codex/config.toml`.
- Required assignment screenshot filenames.
- Canonical reviewer docs, stable screenshot files, `docs/pr-description-draft.md`, selected Clio run outputs, and `docs/agent-runs/final-selection.md`.

## Model and Sub-agent Strategy

Current orchestration: Codex desktop thread, exact model/profile and reasoning effort not exposed.
Fit assessment: Medium complexity, medium ambiguity, and medium blast radius. Edits are documentation/control text, but they control a later reviewer-facing evidence regeneration flow.
Recommended change: Use the repository `enterprise-default` policy. Keep orchestration ownership in the main thread. Use one bounded read-only final review sub-agent after edits if the platform supports it and no more urgent blocker appears.

| Purpose | Context strategy | Input context | Output artifact | Model policy | Model class/profile | Reasoning effort | Reason | Parallel? | Blast radius if wrong |
|---|---|---|---|---|---|---|---|---|---|
| Final control-surface terminology review | curated artifacts | Changed control files, `TASKS.md`, root standards, final validation output | Review findings or no-findings memo | enterprise-default | latest strongest available reviewer | high | Evidence-generation rules and layer terminology are easy to make subtly inconsistent | No | Medium: missed gap could keep later Clio output incomplete |

Fresh confirmation is required before adding write-capable sub-agents or more than this single read-only final reviewer.

## Tasks

- [ ] Reconfirm pre-implementation state.
  - Read this work item package, `TASKS.md`, `agents.md`, `agent-control/generate-docs/*`, and `agent-control/generate-code/*`.
  - Run `git status --short --branch` and record any pre-existing unrelated files.

- [ ] Strengthen Clio evidence requirements.
  - Update `agent-control/generate-docs/workflow.md` so MCP screenshot/evidence capture requires both Context7 query/result evidence and custom `pipeline-status` MCP tool/resource evidence.
  - Update Clio workflow so PR draft generation has a required evidence checklist for spec produced, pipeline run, tests/coverage, skill/hook, MCP usage, and README with student name.
  - Update Clio workflow so stable assignment screenshot names must be semantically distinct unless a limitation is explicitly accepted.
  - Update Clio workflow so `test-coverage.png` represents current passing coverage and `hook-trigger.png` owns deliberate blocking/failure evidence.

- [ ] Tighten Clio quality and registry checks.
  - Update `agent-control/generate-docs/quality-bar.md` with rejection criteria for custom-MCP-only MCP evidence, missing spec/README PR evidence categories, duplicate stable screenshot mappings, and coverage-failure-only `test-coverage.png`.
  - Update `agent-control/generate-docs/run-registry.md` so screenshot inventory records source path, target path, semantic category, freshness, whether it is passing or blocking evidence, and whether it satisfies Task 4/Task 5.
  - Update Clio run-registry or handoff guidance to make regenerated documentation package selection a separate step from this control repair.

- [ ] Document the deliberate fourth-runtime-component target.
  - Update `agents.md` and the relevant Athena/Hephaestus control docs to say the assignment minimum is three runtime components, the selected package currently satisfies that minimum, and refreshed generation intentionally targets at least four components with Reporting Agent as the preferred fourth component.
  - Keep this language out of generated runtime component names unless a future selected spec/code package actually includes Reporting Agent.

- [ ] Document the deliberate Claude project-skill decision for Athena.
  - Update standing control guidance to say `.claude/skills/write-spec/SKILL.md` is the active Claude Code project-skill surface for Athena (Spec Writer).
  - Explain that the assignment's `.claude/commands/write-spec.md` path is an example of a slash-invoked workflow, while this repository uses the newer project-skill surface that supersedes the legacy command wrapper.
  - Do not create a `.claude/commands/write-spec.md` file.

- [ ] Fix Hephaestus `shared/` evidence consistency.
  - Update `agent-control/generate-code/workflow.md`, `agent-control/generate-code/quality-bar.md`, and `agent-control/generate-code/run-registry.md` so all three agree: run-local `shared/` is runtime evidence and may be committed as deliberate current-run evidence, but it is excluded from selectable code inventories and canonical copy targets.
  - Keep `archive/`, coverage files, caches, bytecode, and temp folders excluded from selectable outputs.

- [ ] Remove harness terminology leakage from Hephaestus workflow.
  - Replace the phrase "frozen Hephaestus planning package" in `agent-control/generate-code/workflow.md`.
  - Cite the Homework 6 standing agent guide, `.codex/config.toml` `agents.max_threads = 8`, and the Hephaestus control package as the source of sub-agent authorization.

- [ ] Record the follow-up Clio regeneration boundary.
  - Add guidance or handoff text in the control surface that the existing selected documentation package remains historical evidence.
  - State that canonical docs, stable screenshot replacements, `docs/pr-description-draft.md`, selected Clio run outputs, and final-selection screenshot mappings are updated only by a separate Clio regeneration/selection step after this control repair.

- [ ] Run verification.
  - Run static scans listed in this plan.
  - Run the pipeline, pytest suite, and 80 percent coverage gate.
  - Verify canonical reviewer docs, stable screenshots, PR draft, selected Clio run output, and final-selection records have no diff.
  - If coverage gate fails due to the known Windows sandbox coverage-file rename issue, rerun the same coverage helper outside the sandbox with the already-approved `python scripts\check_coverage_gate.py` prefix and record both outcomes.

- [ ] Run final review.
  - Use the planned read-only final reviewer sub-agent if available.
  - Main thread resolves any findings, reviews the final diff, and owns integration judgment.

- [ ] Update `CHANGELOG.md` before the implementation commit.
  - Add a newest-first entry summarizing Clio evidence-generation hardening, deliberate fourth-component and Claude-skill documentation, `shared/` consistency, Hephaestus terminology fix, the explicit follow-up Clio regeneration boundary, and validation results.

## Validation commands

| Command | Expected result |
|---|---|
| `git status --short --branch` | Branch is `homework-6-submission`; unrelated pre-existing changes are identified and not overwritten |
| `Select-String -Path agent-control\generate-docs\workflow.md,agent-control\generate-docs\quality-bar.md,agent-control\generate-docs\run-registry.md -Pattern 'Context7','pipeline-status','spec produced','README','test-coverage.png','hook-trigger.png'` | Clio control files contain the required evidence concepts |
| `Select-String -Path agents.md,agent-control\write-spec\*.md,agent-control\generate-code\*.md -Pattern 'assignment minimum','at least four','Reporting Agent','selected package'` | Deliberate fourth-component target is documented without hiding the assignment minimum |
| `Select-String -Path agents.md,agent-control\write-spec\*.md,.claude\skills\write-spec\SKILL.md -Pattern 'project skill','commands/write-spec.md','legacy command'` | Athena Claude project-skill decision is documented |
| `Test-Path .claude\commands\write-spec.md` | Returns `False` |
| `Select-String -Path agent-control\generate-code\workflow.md,agent-control\generate-code\quality-bar.md,agent-control\generate-code\run-registry.md -Pattern 'shared/','runtime evidence','selectable code','canonical copy'` | Hephaestus `shared/` language is consistent |
| `Select-String -Path agent-control\generate-code\workflow.md -Pattern 'frozen Hephaestus planning package'` | No matches |
| `git diff -- README.md HOWTORUN.md TESTING_GUIDE.md ARCHITECTURE.md API_REFERENCE.md docs\pr-description-draft.md docs\screenshots docs\agent-runs\20260620-230201-generate-docs-python-primary docs\agent-runs\final-selection.md` | No output; existing Clio output and reviewer-facing docs are not repaired in this work item |
| `python integrator.py --shared-dir tmp\review-repair-shared` | Pipeline completes with 8 total transactions, 2 settled, 2 rejected, 4 review-required, 0 errors |
| `python -m pytest -p no:cacheprovider --basetemp=tmp\review-repair-pytest` | 50 tests pass |
| `python scripts\check_coverage_gate.py --fail-under 80 -- --basetemp=tmp\review-repair-coverage-pytest` | Coverage gate passes at or above 80 percent, or sandbox failure is rerun outside the sandbox and recorded |
| `$patterns = @('TO' + 'DO', 'TB' + 'D', 'PLACE' + 'HOLDER', 'FIX' + 'ME'); Select-String -Path agent-control\generate-docs\*.md,agent-control\generate-code\*.md,agent-control\write-spec\*.md,agents.md -Pattern $patterns` | No unresolved draft markers in changed implementation-target files |
| `git diff -- TASKS.md ..\HOMEWORK_STANDARDS.md ..\README.md mcp.json .codex\config.toml integrator.py agents tests mcp\server.py` | No output unless a narrow verified blocker was explicitly recorded |
| `git diff --check` and `git diff --cached --check` | No whitespace errors |

## Plan variance handling

Use `rule:lifecycle.variance-policy`. Before freeze, edit this draft directly for operator feedback. After freeze, record nontrivial implementation variance in `implementation-notes/variance-log.md`; use a plan amendment for high-impact architecture, API, data, security, privacy, compliance, scope, acceptance-criteria, or feasibility changes.

Expected low-risk variance:

- The implementation may choose the most local Clio control file for follow-up regeneration instructions if the boundary is clear.
- The implementation may use equivalent PowerShell validation commands when path quoting differs on Windows.

High-impact variance requiring operator confirmation:

- Regenerating or selecting a new Athena, Hephaestus, or Themis package.
- Regenerating or repairing Clio output, canonical reviewer docs, stable screenshots, PR draft, selected Clio run records, or final-selection records.
- Adding Reporting Agent to canonical runtime code.
- Creating `.claude/commands/write-spec.md`.
- Changing MCP server behavior, coverage hook behavior, or runtime JSON schemas.
- Modifying `TASKS.md`, root `HOMEWORK_STANDARDS.md`, or root `README.md`.

## Planning artifact freeze gate

Use `module:freeze-gate`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, and `rule:freeze.stop-before-implementation`.

Draft review status: this package is being prepared for operator approval.
Approval commit: not created.
Post-freeze implementation authorization: not granted.

## Completion criteria

- Acceptance criteria in `spec-homework-6-review-repair.md` are met.
- Required validation commands have been run and results recorded.
- Required documentation and control files are updated within scope.
- `CHANGELOG.md` has a newest-first entry before the implementation commit.
- Variance log is present if nontrivial implementation variance occurs.
- De-facto sub-agent use is reported, including whether the planned read-only final reviewer was used and what it inspected.

## Approval

- Status: Draft
- Superseded by: Not applicable

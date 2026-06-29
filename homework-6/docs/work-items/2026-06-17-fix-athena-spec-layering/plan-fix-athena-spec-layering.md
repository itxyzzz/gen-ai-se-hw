# Fix Athena Spec Layering Plan

Work ID: `2026-06-17-fix-athena-spec-layering`
Short ID: `fix-athena-spec-layering`
Status: Draft
Harness release: `unknown`
Schema: `schema:plan.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:models.strategy-required`, `rule:models.context-strategy`, `rule:models.approved-strategy-authorized`, `rule:models.fresh-confirmation`, `rule:lifecycle.variance-policy`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, `rule:freeze.stop-before-implementation`

## Implementation summary

This work repairs the boundary between the operator-facing assignment, the four Homework Automation Layer agents, and the generated transaction-processing system. The implementation will leave `TASKS.md` frozen, introduce a cleaned transaction-system brief as Athena's direct source input, update the standing layer glossary and identity labels, harden Athena's workflow and quality gates, and mark the failed first selected spec as rejected/superseded. A replacement Athena run is a separate follow-up in a clean Homework 6-root thread.

The most important behavioral change is that Athena (Spec Writer) no longer treats the homework automation harness as the system under specification. Its generated `specification.md` must specify the transaction-processing software that Hephaestus (Code Generator) will build. Low-level task cards must describe transaction-system implementation slices and include enough detail for code generation, tests, docs, and validation without turning the four homework automation agents into runtime product components.

The repaired Athena surface must instruct the later replacement spec to preserve useful product decisions from the failed spec only where they belong to the generated transaction system. It must reject process machinery such as run folders, canonical copy, final-selection rules, harness gates, slash-command creation, hook setup, MCP setup, screenshots, and PR support when those appear inside the product specification.

## Files and interfaces

Create:

- `homework-6/agent-control/write-spec/transaction-system-brief.md`

Modify:

- `homework-6/agents.md`
- `homework-6/agent-control/write-spec/README.md`
- `homework-6/agent-control/write-spec/workflow.md`
- `homework-6/agent-control/write-spec/quality-bar.md`
- `homework-6/agent-control/write-spec/stack-profiles.md`
- `homework-6/docs/agent-runs/final-selection.md`
- `homework-6/CHANGELOG.md`

Stable interfaces:

- The frozen assignment remains `homework-6/TASKS.md`.
- The public Athena entrypoints remain `homework-6/.agents/skills/write-spec/SKILL.md` and `homework-6/.claude/skills/write-spec/SKILL.md`.
- The selected generated system spec remains `homework-6/specification.md`.
- The run registry remains under `homework-6/docs/agent-runs/`.
- Replacement Athena generation is outside this plan and should be started after this work item in a clean thread rooted at `homework-6`.

## Model and Sub-agent Strategy

Current orchestration: Codex Desktop thread, model and reasoning labels not exposed in the shell environment.
Fit assessment: Medium complexity, high ambiguity risk, medium blast radius. The work changes prompt/control surfaces that drive later generated code, so correctness and layer clarity matter more than speed. The edits are tightly coupled across a small set of documentation and workflow files.
Recommended change: Use the strongest available reasoning profile for implementation if the UI exposes model/reasoning controls. If controls are unavailable, compensate with static validation, explicit meta-leakage checks, and careful diff review.

Sub-agents: None for this control-surface repair. The file changes are tightly coupled and mostly documentation/workflow edits, so executor sub-agent coordination overhead is higher than the expected benefit. Athena's later replacement generation workflow may still require its planned runtime sub-agent phases; that is separate from this harness implementation plan and should be recorded in the future replacement run artifacts.

## Tasks

- [ ] Confirm the worktree is still on `homework-6-submission` and inspect the existing dirty `homework-6/agent-control/write-spec/README.md` diff before editing it.

- [ ] Create `homework-6/agent-control/write-spec/transaction-system-brief.md`.
  The brief must:
  - State that it is Athena's direct product-spec input.
  - Say `TASKS.md` is frozen and operator-facing.
  - Preserve the assignment's transaction-processing requirements: `sample-transactions.json`, at least three cooperating runtime components, JSON file protocol through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`, validator/fraud/settlement or equivalent components, Decimal or BigDecimal money, ISO-style currency validation, audit-safe logging, redaction, coverage expectations, and final results.
  - Clarify that plus-column and submission mechanics are outer deliverables, not part of the product specification Athena writes.
  - Avoid becoming a full technical spec: no exhaustive architecture, no final low-level task list, no implementation prompts for every file.

- [ ] Update `homework-6/agents.md` with the layer glossary.
  Add definitions for Operator Layer, Homework Automation Layer, Generated Transaction System Layer, runtime transaction pipeline components, and executor sub-agents. Record the identity labels exactly:
  - `Athena / Spec Writer / write-spec`
  - `Hephaestus / Code Generator`
  - `Themis / Test Generator`
  - `Clio / Documentation Generator`
  Add the rule that prompts and technical instructions must use paired labels such as `Athena (Spec Writer)` until the glossary is loaded, and that Greek labels never apply to runtime transaction pipeline components or executor sub-agents.

- [ ] Update `homework-6/agent-control/write-spec/README.md`.
  Mention `transaction-system-brief.md` as the product input file in the package file list. Preserve any pre-existing user edits in that file. Do not add a second source of truth for workflow behavior.

- [ ] Update `homework-6/agent-control/write-spec/workflow.md`.
  Replace ordinary generation context item `homework-6/TASKS.md` with `homework-6/agent-control/write-spec/transaction-system-brief.md`. Keep `sample-transactions.json`, `agents.md`, stack profiles, quality bar, and Homework 3 reference context. Add a rule that `TASKS.md` is consulted by operator-layer maintainers, not required input for Athena during normal generation. Add product-only generation rules that prevent the generated spec from specifying the write-spec workflow, run preservation mechanics, final-selection/canonical copy, harness planning, command creation, hook creation, MCP setup, screenshots, README, or PR support as product requirements.

- [ ] Update `homework-6/agent-control/write-spec/quality-bar.md`.
  Replace "one entry per meta-agent" with "implementation-ready transaction-system slices." Add examples of required slice categories: project structure, JSON envelope, file movement, integrator, runtime components, validation, risk scoring, settlement/final outcomes, result summaries, redaction/audit, dry-run validation seam, tests, MCP-readable result shape, deterministic reruns, and error handling. Add a meta-leakage rejection gate for `dev-doc-harness`, freeze gates, run folders, final-selection, canonical copy, write-spec skill creation, `.claude/skills`, `.codex/commands`, hook setup, MCP config setup, screenshots, README/PR support, and Greek identity labels used as runtime product components. Add a Homework 3 depth target.

- [ ] Update `homework-6/agent-control/write-spec/stack-profiles.md`.
  Clarify that stack profiles are Athena generation controls, not generated product requirements. Keep Python and Java defaults. Remove or reframe any stack-invariant item that could cause Athena to copy run-preservation or selection mechanics into the generated `specification.md`.

- [ ] Mark the failed selected spec in `homework-6/docs/agent-runs/final-selection.md`.
  Change current status to show the selected run is failed/superseded because it targeted the homework automation harness instead of the transaction-processing system. Add a dated failure note that preserves the original selection row and states that replacement generation and selection will be handled later in a separate clean Homework 6-root thread. Do not delete the old row, old run folder, or old canonical spec in this work item.

- [ ] Update `homework-6/CHANGELOG.md` before the implementation commit.
  Add a newest-first Homework 6 step that records the failed-spec marking, Athena control-surface repair, transaction-system brief, and validation performed. Do not claim that a replacement run or selection happened in this work item.

- [ ] Review the final diff.
  Confirm no unrelated pre-existing changes were reverted or staged, `TASKS.md` is unchanged, `homework-6/specification.md` is unchanged, old run evidence remains preserved, no draft markers remain, and generated or transient files are not accidentally included.

## Validation commands

| Command | Expected result |
|---|---|
| `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw branch --show-current` | Prints `homework-6-submission`. |
| `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw status --short` | Shows only expected work-item, control-surface, final-selection, and changelog changes; pre-existing README changes are accounted for and not reverted. |
| `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw diff -- homework-6/TASKS.md` | Prints no diff. |
| `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw diff -- homework-6/specification.md` | Prints no diff. |
| `Select-String -LiteralPath C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\agent-control\write-spec\workflow.md -Pattern 'transaction-system-brief.md'` | Finds the new direct input reference. |
| `Select-String -LiteralPath C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\agents.md -Pattern 'Athena / Spec Writer / write-spec','Hephaestus / Code Generator','Themis / Test Generator','Clio / Documentation Generator','Athena (Spec Writer)'` | Finds the identity glossary and paired-label rule. |
| `Select-String -LiteralPath C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\agent-control\write-spec\quality-bar.md -Pattern 'implementation-ready transaction-system slices','meta-layer leakage','Homework 3'` | Finds the corrected low-level task standard, leakage gate, and depth target. |
| `Select-String -LiteralPath C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\docs\agent-runs\final-selection.md -Pattern 'failed','superseded','wrong target','transaction-processing system'` | Finds the explicit failed/superseded marker for the first selected spec. |
| `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw diff --check` | Exits 0 with no whitespace errors. |

## Plan variance handling

Use `rule:lifecycle.variance-policy`. Before freeze, edit this draft directly for operator feedback. After freeze, record nontrivial implementation variance in `implementation-notes/variance-log.md`; use a plan amendment for high-impact architecture, API, data, security, privacy, compliance, scope, acceptance-criteria, or feasibility changes.

The most likely variance is discovering that marking the failed selection requires a different evidence file than `docs/agent-runs/final-selection.md`. That is a local documentation variance if it preserves the same scope and evidence trail; record it in the variance log. Replacement generation remains out of scope and should not be pulled into this plan as variance.

## Planning artifact freeze gate

Use `module:freeze-gate`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, and `rule:freeze.stop-before-implementation`.

Draft review status: Draft package prepared for operator review.
Approval commit: Not created.
Post-freeze implementation authorization: Not granted.

## Completion criteria

- Acceptance criteria in `spec-fix-athena-spec-layering.md` are met.
- Required validation commands have been run and recorded.
- Required documentation artifacts have been created or updated.
- `homework-6/CHANGELOG.md` has a newest-first entry for the work before each commit.
- Variance log is present and current.
- De-facto sub-agent use is reported when applicable; this plan currently authorizes no sub-agents for the control-surface repair.

## Approval

- Status: Draft
- Superseded by: None

# Large or Phased Work Item Phase 02: Generate and Select Spec

Work ID: `2026-06-16-homework-6-spec-agent`
Short ID: `homework-6-spec-agent`
Status: Approved
Harness release: Installed global `dev-doc-harness`; no release field is published in `SKILL.md`
Schema: `schema:plan.phase`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:quality.phase-plan-fresh-thread`, `rule:models.strategy-required`, `rule:lifecycle.variance-policy`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, `rule:freeze.stop-before-implementation`

## Objective

Invoke the Homework 6 Agent 1 `write-spec` workflow, preserve a complete Python specification-generation run, validate the run against the shared quality bar, and select the run's `specification.md` as the canonical Homework 6 Task 1 specification when first-run auto-selection applies.

This phase must use the existing `write-spec` skill and shared `agent-control/write-spec/` workflow. It must not implement pipeline source code, tests, hooks, screenshots, MCP server code, or final documentation beyond the Agent 1 specification package and its run evidence.

## Input context

The implementing agent must read these files before editing or generating artifacts:

- `AGENTS.md` from the repository root, when available in the active workspace.
- `HOMEWORK_STANDARDS.md` from the repository root, when available in the active workspace.
- `README.md` from the repository root, when available in the active workspace.
- `homework-6/TASKS.md`
- `homework-6/sample-transactions.json`
- `homework-6/agents.md`
- `homework-6/.agents/skills/write-spec/SKILL.md`
- `homework-6/agent-control/write-spec/workflow.md`
- `homework-6/agent-control/write-spec/stack-profiles.md`
- `homework-6/agent-control/write-spec/quality-bar.md`
- `homework-6/agent-control/write-spec/run-registry.md`
- `homework-6/docs/agent-runs/README.md`
- `homework-6/docs/agent-runs/final-selection.md`
- `homework-6/docs/work-items/2026-06-16-homework-6-spec-agent/spec-homework-6-spec-agent.md`
- `homework-6/docs/work-items/2026-06-16-homework-6-spec-agent/plan-phase-01-agent-controls-homework-6-spec-agent.md`
- `homework-6/docs/work-items/2026-06-16-homework-6-spec-agent/plan-phase-02-generate-and-select-spec-homework-6-spec-agent.md`
- Homework 3 reference package when available from the repository root:
  - `homework-3/specification.md`
  - `homework-3/agents.md`
  - `homework-3/docs/domain-rules.md`
  - `homework-3/docs/technical-conventions.md`
  - `homework-3/docs/development-process.md`

The implementing agent must inspect current branch and worktree state before editing:

```powershell
git status --short --branch
```

Expected signal:

- Current branch is `homework-6-submission`.
- Unrelated pre-existing work remains unstaged.
- `homework-6/specification.md` is absent at the start of the first generation run unless the operator has separately created or selected one.

## Likely files and areas

Create or modify only these files in Phase 02:

| Path | Action | Responsibility |
|---|---|---|
| `homework-6/docs/agent-runs/<run-id>/run-metadata.md` | Create | Record mode, stack, operator prompt, tools, source context, missing references, validation, and selection status. |
| `homework-6/docs/agent-runs/<run-id>/inputs/source-context.md` | Create | Summarize loaded assignment, standards, sample data, standing agent guide, and local references. |
| `homework-6/docs/agent-runs/<run-id>/agent-1-spec/handoffs/sub-agent-plan.md` | Create | Record required sub-agent roles, context strategy, model policy, reasoning intent, concurrency, and integration ownership. |
| `homework-6/docs/agent-runs/<run-id>/agent-1-spec/handoffs/domain-research-handoff.md` | Create | Preserve domain research sub-agent findings, assumptions, limitations, and next steps. |
| `homework-6/docs/agent-runs/<run-id>/agent-1-spec/handoffs/objectives-handoff.md` | Create | Preserve high-level and mid-level objective recommendations. |
| `homework-6/docs/agent-runs/<run-id>/agent-1-spec/handoffs/low-level-tasks-handoff.md` | Create | Preserve detailed task-card decomposition for Agents 1-4. |
| `homework-6/docs/agent-runs/<run-id>/agent-1-spec/outputs/specification.md` | Create | Candidate stack-specific Task 1 specification. |
| `homework-6/docs/agent-runs/<run-id>/agent-1-spec/outputs/docs/domain-rules.md` | Create | Educational banking-pipeline domain assumptions, safety limits, and research provenance. |
| `homework-6/docs/agent-runs/<run-id>/agent-1-spec/outputs/docs/technical-conventions.md` | Create | Python-specific money, JSON, audit, logging, redaction, file protocol, test, coverage, and MCP conventions. |
| `homework-6/docs/agent-runs/<run-id>/agent-1-spec/outputs/docs/development-process.md` | Create | Portable process gates for later Homework 6 agents. |
| `homework-6/docs/agent-runs/<run-id>/agent-1-spec/research-notes.md` | Create | Context7, web, local-source, and fallback research notes with applied decisions. |
| `homework-6/docs/agent-runs/<run-id>/agent-1-spec/review/final-review.md` | Create | Final review findings and repair status. |
| `homework-6/docs/agent-runs/<run-id>/agent-1-spec/validation-checklist.md` | Create | Completed checks against Task 1, stack profile, quality bar, privacy rules, and review findings. |
| `homework-6/docs/agent-runs/<run-id>/agent-1-spec/handoff.md` | Create | Completion or continuation handoff for future threads. |
| `homework-6/docs/agent-runs/<run-id>/comparison.md` | Create if useful | First-run comparison may state that no prior viable runs exist. |
| `homework-6/docs/agent-runs/final-selection.md` | Update | Record first-run auto-selection when canonical `specification.md` is absent and the run passes validation. |
| `homework-6/specification.md` | Create only after successful first-run auto-selection | Canonical selected Task 1 specification copied from the run output. |
| `homework-6/CHANGELOG.md` | Update before Phase 02 commit | Newest-first entry describing the generated run and canonical selection. |

Do not create or modify these files in Phase 02:

- `homework-6/agents.md`, unless the run discovers a standing guide correction and the operator separately authorizes the control-surface update.
- `homework-6/mcp/server.py`
- `homework-6/mcp.json`, except for documenting selection in run artifacts; do not add `pipeline-status` before `mcp/server.py` exists.
- `homework-6/.codex/config.toml`, except under a separately approved control-surface update.
- Pipeline source files such as `integrator.py` or `agents/*.py`.
- Unit tests, coverage hooks, command files for `/run-pipeline` or `/validate-transactions`, screenshots, final README, or HOWTORUN docs.

## Model and Sub-agent Strategy

Current orchestration: Codex Desktop, GPT-5-based agent per system context; exact reasoning-effort control is not exposed in this thread.
Fit assessment: High importance and moderate complexity. This phase produces the canonical specification that downstream code, test, MCP, hook, and documentation agents will follow. Privacy and audit rules, stack specificity, and executable low-level task cards have high downstream blast radius.
Recommended change: If the UI exposes model and reasoning controls, use the latest strongest Codex profile with high or extra-high reasoning for orchestration, integration drafting, final review, privacy/audit review, and canonical selection. If controls are unavailable, record that limitation in `run-metadata.md` and compensate with explicit checklists, source-grounded handoffs, and fresh validation.

Use the active repository policy: `enterprise-default`.

| Purpose | Context strategy | Input context | Output artifact | Model policy | Model class/profile | Reasoning effort | Reason | Parallel? | Blast radius if wrong |
|---|---|---|---|---|---|---|---|---|---|
| Domain research | curated prompt | Assignment files, sample data, privacy/audit rules, selected stack constraints, and source requirements | `agent-1-spec/handoffs/domain-research-handoff.md` plus accepted entries in `research-notes.md` | enterprise-default | strong current Codex profile or inherited strongest profile | high | Research claims shape domain rules and must avoid compliance overreach | Yes, with stack research if used | Medium; poor assumptions can mislead downstream implementation |
| Objectives architecture | curated artifacts | Task 1 requirements, domain-research handoff, sample transaction facts, and quality bar | `agent-1-spec/handoffs/objectives-handoff.md` | enterprise-default | strong current Codex profile or inherited strongest profile | high | Objectives become acceptance criteria for all later agents | No, runs after domain research | High; weak objectives cascade into code and tests |
| Low-level task decomposition | curated artifacts | Stack profile, objectives handoff, quality bar, assignment deliverables, and required file protocol | `agent-1-spec/handoffs/low-level-tasks-handoff.md` | enterprise-default | strong current Codex profile or inherited strongest profile | high | Task cards must be executable without guessing | No, runs after objectives | High; vague task cards cause implementation drift |
| Final review | curated artifacts | Candidate outputs, validation checklist, handoffs, research notes, and selection rules | `agent-1-spec/review/final-review.md` | enterprise-default | latest strongest available profile | high or extra-high | Final check before canonical selection | No | High; missed issues become canonical homework instructions |

Maximum concurrent sub-agents: 2 during the research wave, then 1 per dependent wave. More than 3 concurrent sub-agents requires fresh operator approval.

Runtime note: if the active runtime cannot spawn sub-agents or policy blocks spawning, stop before drafting the generated spec and ask the operator whether to switch tools or proceed in documented degraded mode. Do not silently replace the required sub-agent phases with main-thread-only drafting.

## Tasks

- [ ] Step 1: Confirm the approved Phase 02 plan and inspect current state.

  Run:

  ```powershell
  git status --short --branch
  Test-Path specification.md
  Test-Path specification-TEMPLATE-hint.md
  Get-ChildItem docs\agent-runs -Directory | Select-Object -ExpandProperty Name
  ```

  Expected:

  - Branch is `homework-6-submission`.
  - `specification.md` is absent for first-run auto-selection, or the operator has explicitly selected overwrite behavior.
  - `specification-TEMPLATE-hint.md` absence is recorded in `run-metadata.md` if still missing.
  - Existing preserved runs are inventoried before creating a new run.

- [ ] Step 2: Normalize mode and stack input.

  Required decisions:

  - Mode: `generate`.
  - Stack: `python`, because the operator did not provide `stack=java`.
  - Reject `stack=auto` or unsupported values if supplied in a later prompt.
  - Run ID format: `YYYYMMDD-HHMMSS-write-spec-python-primary`.

- [ ] Step 3: Create the run folder before drafting content.

  Required layout:

  ```text
  docs/agent-runs/<run-id>/
    run-metadata.md
    inputs/
      source-context.md
    agent-1-spec/
      handoffs/
        sub-agent-plan.md
      outputs/
        docs/
      review/
      research-notes.md
      validation-checklist.md
      handoff.md
  ```

  Record in `run-metadata.md`:

  - Run ID, timestamp, mode, stack, and operator prompt.
  - Active branch and relevant worktree state.
  - Tools available, including whether sub-agent tools were available.
  - Source files read.
  - Missing `specification-TEMPLATE-hint.md` if it remains absent.
  - Model/reasoning controls exposed or unavailable.

- [ ] Step 4: Write `inputs/source-context.md`.

  Include concise summaries of:

  - Homework 6 task requirements and deliverables.
  - Sample transaction fields, valid examples, invalid currency `XYZ`, negative amount, high-value examples, odd-hour transaction, and cross-country metadata.
  - Standing `agents.md` privacy, audit, run preservation, and final-selection rules.
  - Python stack profile decisions.
  - Homework 3 references used for format only, if accessible.
  - Any inaccessible root or Homework 3 references and their impact.

- [ ] Step 5: Write `agent-1-spec/handoffs/sub-agent-plan.md`.

  Include:

  - Run ID and selected stack.
  - Planned roles from the model/sub-agent strategy table.
  - Context strategy, input artifacts, output artifacts, parallel or wave ordering, model policy, reasoning intent, and integration owner.
  - Runtime limits observed.
  - A note that the orchestration thread owns final integration and canonical copy decisions.

- [ ] Step 6: Execute the research wave.

  Use the required domain research sub-agent if spawning is available and authorized by the approved plan.

  Research should cover:

  - ISO 4217-style currency-code handling as an engineering validation rule.
  - Precise decimal money handling with Python `decimal.Decimal`.
  - Safe structured audit logging and redaction of account identifiers.
  - Educational-simulation boundary that avoids legal, AML, sanctions, payment-network, or banking-compliance claims.
  - FastMCP and Context7 expectations only as needed to specify later Agent 2 and Task 4 responsibilities.

  Preserve findings in `domain-research-handoff.md` and accepted source entries in `research-notes.md`.

  If network, Context7, or web research is unavailable, record the limitation and rely on assignment files and local Python documentation knowledge with conservative assumptions.

- [ ] Step 7: Execute the objectives wave.

  Use the objectives architect sub-agent after domain research is available.

  Preserve:

  - One-sentence high-level objective.
  - 4-5 concrete, testable mid-level objectives.
  - Boundary notes for simulation, privacy, file protocol, coverage, and MCP deliverables.
  - Residual risks or tradeoffs.

- [ ] Step 8: Execute the low-level task decomposition wave.

  Use the low-level task decomposition sub-agent after objectives and stack profile are stable.

  Preserve one task card for each meta-agent:

  - Agent 1: Specification writer and run-preservation workflow.
  - Agent 2: Python code generator for integrator and at least three cooperating pipeline agents.
  - Agent 3: Test, coverage hook, `/run-pipeline`, and `/validate-transactions` author.
  - Agent 4: Documentation author for README, HOWTORUN, architecture/testing docs, screenshots, and PR support.

  Each task card must include exact prompt, file to create or update, function to create, details, edge cases, acceptance criteria, and verification command or evidence.

- [ ] Step 9: Integrate candidate output files in the run folder.

  Create:

  - `agent-1-spec/outputs/specification.md`
  - `agent-1-spec/outputs/docs/domain-rules.md`
  - `agent-1-spec/outputs/docs/technical-conventions.md`
  - `agent-1-spec/outputs/docs/development-process.md`

  The candidate `specification.md` must include:

  - High-Level Objective.
  - Mid-Level Objectives with 4-5 testable bullets.
  - Implementation Notes covering Decimal, ISO 4217-style currency validation, audit logging, no plaintext PII logging, file protocol, coverage, and MCP responsibilities.
  - Context with beginning state from `sample-transactions.json` and ending state in `shared/results/` with summary report and at least 90% coverage target.
  - Low-Level Tasks with one detailed entry per meta-agent.

  Python specificity must appear in file paths, function names, commands, tests, hooks, and MCP notes.

- [ ] Step 10: Complete the validation checklist before review.

  `agent-1-spec/validation-checklist.md` must mark pass/fail for:

  - Task 1 five required sections.
  - Python stack specificity.
  - One low-level task card per meta-agent.
  - Agent 2 requirement for at least three cooperating pipeline agents.
  - Agent 2 Context7 usage and two-query documentation requirement.
  - Coverage hook minimum of 80% and final target of at least 90%.
  - JSON file protocol through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
  - Decimal money handling and no binary floating point for amounts.
  - ISO 4217-style currency validation.
  - Redacted audit/logging examples.
  - `pipeline-status` not added to config before `mcp/server.py` exists.
  - Required run handoffs and final review presence.
  - Canonical files untouched before selection.

- [ ] Step 11: Execute final review and repair.

  Use the final review sub-agent if spawning is available and authorized by the approved plan.

  Preserve review in `agent-1-spec/review/final-review.md`, then repair accepted blocking findings before selection.

  Required final review checks:

  - Objective clarity and assignment fit.
  - Stack specificity.
  - Privacy and audit handling.
  - Research provenance and fallback notes.
  - Low-level task executability.
  - Handoff completeness.
  - No unsupported compliance claims.

- [ ] Step 12: Write completion handoff.

  `agent-1-spec/handoff.md` must include:

  - Run ID and selected stack.
  - Completed files.
  - Validation status.
  - Review findings and repair summary.
  - Known risks and limitations.
  - Whether no continuation is required, or the exact next prompt if further work is needed.

- [ ] Step 13: Apply first-run auto-selection only when successful and canonical `specification.md` is absent.

  If all validation checks pass and `specification.md` is absent:

  - Copy `docs/agent-runs/<run-id>/agent-1-spec/outputs/specification.md` to `specification.md`.
  - Update `docs/agent-runs/final-selection.md` with date, run ID, stack, selected file, copied canonical path, rationale, operator, and post-selection edits.
  - Do not copy supporting docs to canonical paths unless the operator explicitly selected them.
  - Do not copy any run-local agent guide over `agents.md`.

  If `specification.md` already exists:

  - Preserve the run only.
  - Update `comparison.md` if useful.
  - Ask the operator whether to compare or select the new run.

- [ ] Step 14: Update `CHANGELOG.md` before committing Phase 02.

  Add a newest-first entry under the title:

  ```markdown
  ## Homework 6 - Step 9: Agent 1 Specification Run

  ### Added

  - Preserved the first Python `write-spec` generation run under `docs/agent-runs/<run-id>/`.
  - Added the selected canonical `specification.md` for Homework 6 Task 1.

  ### Changed

  - Recorded first-run auto-selection in `docs/agent-runs/final-selection.md`.

  ### Fixed

  - Not applicable.

  ### Tests

  - Validated the generated specification against the Task 1 section requirements, Python stack profile, privacy/audit rules, JSON file protocol, and `write-spec` quality bar.
  ```

  Adjust the step number or wording if newer changelog entries exist by the time this phase runs.

- [ ] Step 15: Run validation commands.

  Run from `homework-6`:

  ```powershell
  $run = Get-ChildItem docs\agent-runs -Directory | Where-Object { $_.Name -match 'write-spec-python' } | Sort-Object Name -Descending | Select-Object -First 1
  Test-Path $run.FullName
  Test-Path (Join-Path $run.FullName 'agent-1-spec\outputs\specification.md')
  Test-Path (Join-Path $run.FullName 'agent-1-spec\outputs\docs\domain-rules.md')
  Test-Path (Join-Path $run.FullName 'agent-1-spec\outputs\docs\technical-conventions.md')
  Test-Path (Join-Path $run.FullName 'agent-1-spec\outputs\docs\development-process.md')
  Test-Path (Join-Path $run.FullName 'agent-1-spec\handoffs\sub-agent-plan.md')
  Test-Path (Join-Path $run.FullName 'agent-1-spec\handoffs\domain-research-handoff.md')
  Test-Path (Join-Path $run.FullName 'agent-1-spec\handoffs\objectives-handoff.md')
  Test-Path (Join-Path $run.FullName 'agent-1-spec\handoffs\low-level-tasks-handoff.md')
  Test-Path (Join-Path $run.FullName 'agent-1-spec\review\final-review.md')
  Test-Path (Join-Path $run.FullName 'agent-1-spec\research-notes.md')
  Test-Path (Join-Path $run.FullName 'agent-1-spec\validation-checklist.md')
  Test-Path (Join-Path $run.FullName 'agent-1-spec\handoff.md')
  Test-Path specification.md
  ```

  Expected:

  - All commands print `True` for a successful first-run auto-selection.

  Run:

  ```powershell
  Select-String -Path specification.md -Pattern 'High-Level Objective|Mid-Level Objectives|Implementation Notes|Context|Low-Level Tasks'
  Select-String -Path specification.md -Pattern 'decimal.Decimal|pytest|pytest-cov|python integrator.py|mcp/server.py|shared/input|shared/processing|shared/output|shared/results'
  Select-String -Path specification.md -Pattern 'float|double|plaintext PII|legal compliance|AML compliance|sanctions compliance'
  git diff --check -- docs\agent-runs specification.md CHANGELOG.md
  ```

  Expected:

  - Required section headings are present.
  - Python-specific requirements and shared protocol paths are present.
  - The risk-word scan is reviewed manually: `float` is acceptable only where the spec explicitly says not to use binary floating point; compliance phrases are acceptable only where the spec says the educational simulation does not claim them.
  - Diff check returns no whitespace errors.

- [ ] Step 16: Review consistency before commit.

  Confirm:

  - Run artifacts are preserved under a single run ID.
  - `specification.md` matches the run output selected in `final-selection.md`, except for any post-selection edits explicitly recorded.
  - Supporting docs remain run evidence unless explicitly selected.
  - `agents.md` was not overwritten.
  - No pipeline implementation, tests, hooks, screenshots, FastMCP server, or final README/HOWTORUN docs were created.
  - Research notes distinguish cited facts, assignment assumptions, generated design decisions, and fallback limitations.
  - The final review has no unresolved blocking findings.

- [ ] Step 17: Commit Phase 02 only after the planning package has been approved and frozen, then after the operator gives a fresh implementation instruction.

  Stage only Phase 02 files:

  ```powershell
  git add docs\agent-runs\<run-id> docs\agent-runs\final-selection.md specification.md CHANGELOG.md
  git commit -m "Add homework 6 agent 1 specification"
  ```

  Expected:

  - Commit contains only selected Phase 02 artifacts and changelog.
  - No unrelated generated caches or implementation files are staged.

## Tests and validation

| Command | Expected result |
|---|---|
| `git status --short --branch` | Shows branch `homework-6-submission`; unrelated work remains unstaged unless approved. |
| `Test-Path specification.md` before generation | `False` for first-run auto-selection, unless operator has already selected a spec. |
| `Test-Path specification-TEMPLATE-hint.md` | If `False`, absence is recorded in `run-metadata.md`. |
| Required run-file `Test-Path` checks from Step 15 | All print `True` after a successful generation run. |
| `Select-String -Path specification.md -Pattern 'High-Level Objective|Mid-Level Objectives|Implementation Notes|Context|Low-Level Tasks'` | Shows all five required Task 1 sections. |
| `Select-String -Path specification.md -Pattern 'decimal.Decimal|pytest|pytest-cov|python integrator.py|mcp/server.py|shared/input|shared/processing|shared/output|shared/results'` | Shows Python stack specificity and required protocol paths. |
| `Select-String -Path specification.md -Pattern 'float|double|plaintext PII|legal compliance|AML compliance|sanctions compliance'` | Reviewed manually; any matches must be prohibitions or simulation-boundary language, not requirements to use floats or unsupported compliance claims. |
| `git diff --check -- docs\agent-runs specification.md CHANGELOG.md` | No whitespace errors. |

## Documentation tasks

- Preserve all generated Agent 1 evidence under `docs/agent-runs/<run-id>/`.
- Update `docs/agent-runs/final-selection.md` only after a run passes validation and first-run auto-selection or explicit selection applies.
- Update `CHANGELOG.md` before the Phase 02 commit.
- Do not create canonical `docs/domain-rules.md`, `docs/technical-conventions.md`, `docs/development-process.md`, or canonical `research-notes.md` unless the operator explicitly selects supporting docs.

## Variance reminder

Use `rule:lifecycle.variance-policy`. Before freeze, edit this draft directly for operator feedback. After freeze, record nontrivial implementation variance in `implementation-notes/variance-log.md`; use a plan amendment for high-impact architecture, API, data, security, privacy, compliance, scope, acceptance-criteria, or feasibility changes.

Likely allowed local technical variance:

- Adjusting the run short label from `primary` to a more descriptive label while preserving the required run ID shape.
- Adding a stack-specific technical research handoff when it reduces risk and stays inside the approved sub-agent concurrency cap.
- Recording a comparison note that no prior successful run exists.
- Treating root or Homework 3 references as unavailable when sandbox scope prevents reading them, provided the limitation is recorded.

Variance requiring approval:

- Generating with `stack=java` instead of the default Python stack for this first selected run.
- Skipping required sub-agent phases when the runtime can spawn them and the operator has not approved degraded mode.
- Copying supporting docs to canonical homework paths without explicit selection.
- Overwriting an existing canonical `specification.md` without explicit select or overwrite instruction.
- Updating `agents.md`, write-spec control surfaces, MCP config, or Codex config as part of this phase.
- Adding pipeline implementation code, tests, hooks, screenshots, or `mcp/server.py` in this phase.
- Adding `pipeline-status` to MCP config before `mcp/server.py` exists.

## Planning artifact freeze gate

Use `module:freeze-gate`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, and `rule:freeze.stop-before-implementation`.

Draft review state for this file:

- Status is Approved after operator approval on 2026-06-17.
- It was staged by itself for operator review.
- The operator approved the planning package before the freeze commit.

Approval freeze state after operator approval:

- Update `homework-6/CHANGELOG.md`.
- Re-run planning-artifact validation.
- Stage only the approved Phase 02 planning artifact and changelog.
- Commit the frozen plan-only package.
- Stop before running `write-spec generate`.
- Ask the operator to confirm model, reasoning-effort, and sub-agent policy choices, and to say whether the spec-generation run should begin in a fresh instruction.

## Handoff output

At Phase 02 implementation completion, report:

- Run ID and selected stack.
- Files created or changed.
- Whether first-run auto-selection copied `specification.md`.
- Validation commands and results.
- Research routes used and fallback limitations.
- Sub-agents used, including count, roles/scopes, concurrency or waves, context strategy, observed inheritance behavior, and de-facto model/model class/profile when known.
- Any variance from this phase plan.
- Recommended next step: draft Phase 03 handoff-hardening plan or proceed to Agent 2 planning after operator approval.

## Completion criteria

- A successful Python `write-spec generate` run is preserved under `docs/agent-runs/<run-id>/`.
- The run includes every output, handoff, review, research, validation, and completion artifact required by `agent-control/write-spec/quality-bar.md`.
- `specification.md` exists only when first-run auto-selection or explicit selection applies.
- `docs/agent-runs/final-selection.md` records the selected run and copied canonical path.
- `CHANGELOG.md` has a newest-first entry before the Phase 02 commit.
- No unsupported implementation files or canonical support docs were created.
- Validation commands have been run and recorded.
- De-facto sub-agent use is reported when applicable.

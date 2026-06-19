# Themis Test Generator Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use `superpowers:subagent-driven-development` (recommended) or `superpowers:executing-plans` to implement this plan task-by-task after the harness freeze gate and a fresh explicit operator instruction. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Create the Themis (Test Generator) automation control surface and separate one-time pipeline operation surfaces without generating or selecting tests yet.

**Architecture:** Add a tool-neutral Themis control package under `homework-6/agent-control/generate-tests/`, then add thin Codex, Claude skill, and Claude slash-command wrappers that point to it. Separately add a tool-neutral `operate-pipeline` package plus one-time Operator Layer command and hook support surfaces; Themis validates those surfaces but must not treat them as per-run Themis outputs.

**Tech Stack:** Markdown control surfaces, Codex project skills under `.agents/skills`, Claude Code project skills under `.claude/skills`, Claude command wrappers under `.claude/commands`, Python/pytest coverage helper scripts, Git pre-push hook, existing Homework 6 Python generated-product stack.

---

Work ID: `2026-06-19-themis-test-generator`
Short ID: `themis-test-generator`
Status: Draft
Harness release: `unknown`
Schema: `schema:plan.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:models.strategy-required`, `rule:models.context-strategy`, `rule:models.approved-strategy-authorized`, `rule:models.fresh-confirmation`, `rule:lifecycle.variance-policy`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, `rule:freeze.stop-before-implementation`

## Implementation summary

This implementation creates two related but distinct parts.

The first part is the third Homework Automation Layer agent control surface: Themis (Test Generator). It follows the existing Athena and Hephaestus pattern: durable rules live in `agent-control/generate-tests/`, while tool-specific wrappers stay thin and mandatory-reference driven.

Themis owns test generation, execution, candidate preservation, selection, and test-quality evidence for a named selected Hephaestus software version. Hephaestus may continue to create tests during Task 2 code generation, because those tests protect generated code while it is being built. Themis turns a selected software package into a candidate test package with unit tests, integration tests, dry-run validation checks, coverage evidence, hook/command validation evidence, and broader test-quality checks.

The second part is a one-time Operator Layer operation package: `agent-control/operate-pipeline/`, the `/run-pipeline` and `/validate-transactions` command surfaces, matching project-skill wrappers, and coverage-gate hook support. These are not Themis responsibilities. Normal Themis runs validate these surfaces and report gaps; they do not duplicate or regenerate them as routine per-run outputs.

The workflow must avoid silently targeting "latest." It must name the selected Hephaestus run ID, selected output inventory, final-selection record, source Athena run ID, spec fingerprint, and code fingerprints. Because the current selected Hephaestus code was generated from an older selected spec than the current canonical `specification.md`, Themis must detect and report that mismatch before generating or selecting tests.

The workflow must also define exactly how files move during a test run. Themis creates a run folder, copies the selected Hephaestus package into a run-local workspace, writes candidate tests under `agent-3-tests/outputs/`, overlays those tests into a run-local `project-under-test/`, and runs all validation from that workspace. Root `tests/`, root `shared/`, root `.coverage`, and root product files stay untouched until explicit selection copies only inventory-declared output files.

Task 5's "Testing & Documentation" language should be interpreted as final acceptance and documentation ownership, not as permission for Clio (Documentation Generator) to silently replace Themis. Clio consumes selected Themis results, documents them, captures final evidence, and may request a Themis repair or record a tightly scoped final test-hardening delta if final documentation review finds a gap.

## Files and interfaces

Create:

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

Modify:

- `homework-6/agents.md`
- `homework-6/CHANGELOG.md`

Do not modify during this control-surface work item:

- `homework-6/TASKS.md`
- `homework-6/specification.md`
- `homework-6/mcp.json`
- `homework-6/.codex/config.toml`
- Canonical generated product files such as `integrator.py`, `agents/*.py`, `tests/*.py`, `pytest.ini`, `research-notes.md`, `shared/`, and `archive/`

## Model and Sub-agent Strategy

Current orchestration: Codex Desktop thread, model and reasoning labels not exposed in the shell environment.
Fit assessment: Medium complexity and medium blast radius. The implementation is mostly Markdown and small helper configuration, but it will steer a later test-generation agent and repository hooks.
Recommended change: Use the strongest available reasoning profile for implementation if model/reasoning controls are exposed. If controls are unavailable, compensate with static validation, scope scans, and diff review.

Sub-agents: None for implementing this control surface. The files are tightly coupled and should be integrated by one orchestration thread. Future Themis runs may use executor sub-agents for independent test-audit slices when the selected code package is large enough to justify it.

Future Themis authorization to encode in `workflow.md`: Themis (Test Generator) may spawn executor sub-agents up to Homework 6's configured `agents.max_threads = 8` cap without additional operator approval when generating or reviewing tests for independent runtime components or quality areas. Prefer bounded read/write sub-agents for component test expansion, integration/rerun scenarios, hook/command validation, and privacy/audit test review when those areas can run in parallel. The orchestration thread owns final integration, selection, and validation.

## Tasks

- [ ] Confirm preflight state.
  Run `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw branch --show-current` and confirm it prints `homework-6-submission`. Run `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw status --short` and note any pre-existing dirty files before editing. Read `homework-6/TASKS.md`, `homework-6/agents.md`, `homework-6/docs/agent-runs/final-selection.md`, existing Athena/Hephaestus control packages, existing entrypoint wrappers, canonical tests, and current `pytest.ini`.

- [ ] Create `homework-6/agent-control/generate-tests/README.md`.
  Include package purpose, file list, Themis role, entrypoints, root-selection note matching existing packages, and a clear boundary statement: one-time pipeline operation surfaces are external Operator Layer tools described in `agent-control/operate-pipeline/` that Themis validates but does not own or regenerate. State that required shared references are mandatory and missing references stop the run.

- [ ] Create `homework-6/agent-control/operate-pipeline/README.md`.
  Include package purpose, file list, ownership boundary, entrypoint list for `/run-pipeline`, `/validate-transactions`, matching project-skill wrappers, coverage helper, Git pre-push hook, and Claude settings hook. State that this package belongs to Operator Layer pipeline operation surfaces, not to Themis per-run outputs.

- [ ] Create `homework-6/agent-control/operate-pipeline/commands-and-hooks.md`.
  Define `/run-pipeline`, `/validate-transactions`, and coverage-gate behavior in one place. Include the run-pipeline steps from `TASKS.md`, validator dry-run reporting expectations, redaction/privacy requirements, coverage default threshold `80`, demonstration override such as `--fail-under 99`, and the rule that future Themis runs validate these surfaces instead of regenerating them.

- [ ] Create `homework-6/agent-control/generate-tests/workflow.md`.
  Include required context, modes `generate`, `resume`, `compare`, `select`, default mode `generate`, run ID format `YYYYMMDD-HHMMSS-generate-tests-python-short-label`, and run folder layout under `homework-6/docs/agent-runs/RUN_ID/agent-3-tests/`. Require selected Hephaestus version traceability: selected code run ID, inventory path, selection record, source Athena run ID, source spec path/fingerprint, current canonical spec fingerprint, and code fingerprints. Require mismatch reporting when selected code source spec differs from current canonical spec. Define candidate output-first behavior, selection behavior, validation, handoff, and no "latest" targeting.

- [ ] Encode test-generation scope in `workflow.md`.
  State that Themis may create or extend unit tests, integration tests, dry-run validation tests, command validation, hook validation, privacy/audit checks, and compact text evidence for support-tool validation. State that screenshot capture for `skill-run-pipeline.png` and `hook-trigger.png` belongs to later Clio/final evidence work or an explicit evidence task, not to routine Themis test generation. State that Themis must not implement Task 4 custom MCP server/config, final README/HOWTORUN docs, Clio-owned PR narrative, or runtime product feature changes unless a failing test reveals a selected-code defect and the operator authorizes a repair.

- [ ] Encode the run-local file manipulation protocol in `workflow.md`.
  Require Themis to create `docs/agent-runs/RUN_ID/agent-3-tests/outputs/`, `workspace/selected-code/`, `workspace/project-under-test/`, and `evidence/`. Require selected Hephaestus inventory-declared files to be copied into `workspace/selected-code/`; require generated candidate tests and config to be written first under `outputs/`; require `project-under-test/` to be rebuilt by copying `selected-code/` and overlaying `outputs/`; require all pytest, coverage, command, hook, and pipeline validation to run from `project-under-test/` with runtime output contained under the run folder. State that only inventory-declared files from `outputs/` may later be copied to canonical root targets during explicit selection.

- [ ] Encode command and hook ownership in `workflow.md`.
  State that `/run-pipeline`, `/validate-transactions`, and coverage hook behavior are defined by `agent-control/operate-pipeline/commands-and-hooks.md`. State that hooks and command surfaces are external one-time Operator Layer operation tools; future Themis runs validate them and report missing/stale surfaces instead of regenerating them as routine Themis outputs.

- [ ] Encode the Agent 4 test/documentation interpretation in `workflow.md`.
  State that Clio (Documentation Generator) owns final documentation, screenshots, reviewer-facing test evidence, and the final acceptance narrative. Clio consumes selected Themis outputs, reruns the selected suite for evidence, and documents results. If Clio discovers missing final test coverage, it should request a Themis follow-up or record an explicit final test-hardening delta rather than silently forking or replacing the selected Themis suite.

- [ ] Encode future Themis sub-agent policy in `workflow.md`.
  Allow executor sub-agents up to `agents.max_threads = 8` for independent test-audit slices after normal post-freeze authorization. Require curated context, policy-relative model/reasoning intent, output artifacts, reports, and orchestration-thread final integration. Require no sub-agent use to be justified by tight coupling, runtime unavailability, or limited scope.

- [ ] Create `homework-6/agent-control/generate-tests/quality-bar.md`.
  Include checks for version traceability, test inventory, 80 percent minimum coverage, meaningful assertions, component unit tests, integration tests, dry-run validator behavior, command behavior, hook success and blocking paths, fixture isolation from root `shared/`, repeated-run archival, runtime provenance when present, Decimal money assertions, strict JSON behavior, privacy scans, workspace containment, and scope rejection checks.

- [ ] Create `homework-6/agent-control/generate-tests/run-registry.md`.
  Include Themis run ID format, required run folder layout, output inventory rules, selected-code traceability rules, workspace and evidence rules, command/hook evidence rules, comparison criteria, first-success or explicit selection behavior for tests, and selection records naming the targeted Hephaestus version.

- [ ] Create `homework-6/.agents/skills/generate-tests/SKILL.md`.
  Use frontmatter name `generate-tests` and description `Use when generating, resuming, comparing, selecting, or reviewing Homework 6 Themis (Test Generator) test-generation runs.` Require reading `../../../agent-control/generate-tests/workflow.md`, `quality-bar.md`, and `run-registry.md`. Stop if references are missing.

- [ ] Create `homework-6/.claude/skills/generate-tests/SKILL.md`.
  Use matching frontmatter plus `when_to_use` and `argument-hint` for `generate|resume|compare|select`. Keep it a thin wrapper around the shared package and include examples such as `/generate-tests`, `/generate-tests generate`, `/generate-tests resume run=...`, and `/generate-tests select run=...`.

- [ ] Create `homework-6/.claude/commands/generate-tests.md`.
  Keep it a thin legacy slash-command wrapper that says the canonical Themis workflow is in `agent-control/generate-tests/` and instructs Claude Code to read the shared files before acting. Do not duplicate the full workflow.

- [ ] Create `/run-pipeline` command and skill surfaces.
  Create `homework-6/.claude/commands/run-pipeline.md`, `homework-6/.claude/skills/run-pipeline/SKILL.md`, and `homework-6/.agents/skills/run-pipeline/SKILL.md` as Operator Layer pipeline operation surfaces, not Themis-owned per-run outputs. Keep them thin and consistent. They must require reading `agent-control/operate-pipeline/commands-and-hooks.md`, run the multi-agent banking pipeline end-to-end, check `sample-transactions.json`, clear or archive `shared/` according to pipeline behavior, run `python integrator.py` or the selected command, summarize `shared/results/summary.json`, and show rejected transaction IDs with safe reason codes only.

- [ ] Create `/validate-transactions` command and skill surfaces.
  Create `homework-6/.claude/commands/validate-transactions.md`, `homework-6/.claude/skills/validate-transactions/SKILL.md`, and `homework-6/.agents/skills/validate-transactions/SKILL.md` as Operator Layer pipeline operation surfaces, not Themis-owned per-run outputs. Keep them thin and consistent. They must require reading `agent-control/operate-pipeline/commands-and-hooks.md`, validate `sample-transactions.json` without running the full pipeline, use validator dry-run behavior when available, report total/valid/invalid counts, reason-code groups, and a redacted table.

- [ ] Create coverage gate helper and hook surfaces.
  Create `homework-6/scripts/check_coverage_gate.py` to run `python -m pytest --cov=. --cov-fail-under=80` by default from the Homework 6 root. Support an explicit threshold override for demonstration, such as `--fail-under 99`, so hook-trigger evidence can prove failure without degrading the real suite. Create `homework-6/.githooks/pre-push` to invoke the helper with the default threshold. Create or update `homework-6/.claude/settings.json` with a coverage-gate hook entry that invokes the same helper or documents the same required command in the supported Claude hook format available locally. Label these as Operator Layer pipeline operation surfaces in comments or adjacent documentation where possible, and reference `agent-control/operate-pipeline/commands-and-hooks.md`.

- [ ] Update `homework-6/agents.md`.
  Add a Themis (Test Generator) control-surface note pointing to `agent-control/generate-tests/`, Codex/Claude skill entrypoints, and Claude command wrapper. Separately document pipeline operation surfaces under `agent-control/operate-pipeline/` for `/run-pipeline`, `/validate-transactions`, and coverage hook as Operator Layer maintained outer tools that Themis validates but does not own. Clarify that Themis targets a named selected Hephaestus software version, records fingerprints, uses a run-local workspace, and owns test quality beyond raw coverage. Clarify that Clio consumes selected Themis results for final documentation/evidence and does not silently replace the selected suite. Preserve the existing layer glossary.

- [ ] Update `homework-6/CHANGELOG.md` before the implementation commit.
  Add a newest-first planning entry during the freeze commit and a later implementation entry when the Themis control surface is created. Do not claim generated tests, screenshots, or final docs exist during this control-surface step.

- [ ] Review and validate the final implementation diff.
  Confirm no canonical generated product code/tests were changed, `TASKS.md` and `specification.md` are unchanged, Themis wrappers are thin, command and hook support surfaces are labeled as Operator Layer outer tools, hooks invoke the shared coverage helper, and quality bars reject scope creep.

## Validation commands

| Command | Expected result |
|---|---|
| `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw branch --show-current` | Prints `homework-6-submission`. |
| `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw status --short` | Shows only expected Themis control-surface, planning package, `agents.md`, hook/helper, command/skill, and changelog changes; any pre-existing dirty files are accounted for. |
| `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw diff -- homework-6/TASKS.md homework-6/specification.md homework-6/mcp.json homework-6/.codex/config.toml` | Prints no diff. |
| `Test-Path C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\agent-control\generate-tests\workflow.md` | Returns `True`. |
| `Test-Path C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\agent-control\operate-pipeline\commands-and-hooks.md` | Returns `True`. |
| `Test-Path C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\.agents\skills\generate-tests\SKILL.md` | Returns `True`. |
| `Test-Path C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\.claude\skills\generate-tests\SKILL.md` | Returns `True`. |
| `Test-Path C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\.claude\commands\generate-tests.md` | Returns `True`. |
| `Test-Path C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\.claude\commands\run-pipeline.md` | Returns `True`. |
| `Test-Path C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\.claude\commands\validate-transactions.md` | Returns `True`. |
| `Test-Path C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\scripts\check_coverage_gate.py` | Returns `True`. |
| `Select-String -LiteralPath C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\agent-control\generate-tests\workflow.md -Pattern 'selected Hephaestus','inventory','fingerprint','no \"latest\"','source spec mismatch','Task 3','Task 4','Task 5'` | Finds selected-version traceability and scope-boundary language. |
| `Select-String -LiteralPath C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\agent-control\generate-tests\workflow.md -Pattern 'workspace/selected-code','project-under-test','outputs','Operator Layer','Clio'` | Finds workspace containment, outer-tool ownership, and Agent 4 interpretation language. |
| `Select-String -LiteralPath C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\agent-control\operate-pipeline\commands-and-hooks.md -Pattern 'run-pipeline','validate-transactions','coverage','80','--fail-under 99','redacted'` | Finds pipeline operation behavior separate from Themis. |
| `Select-String -LiteralPath C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\agent-control\generate-tests\quality-bar.md -Pattern '80 percent','meaningful assertions','fixture isolation','dry-run','hook-trigger','raw account','raw description','workspace'` | Finds coverage, quality, command, hook, privacy, and workspace checks. |
| `Select-String -LiteralPath C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\.claude\commands\run-pipeline.md,C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\.claude\commands\validate-transactions.md -Pattern 'sample-transactions.json','shared/results','reason'` | Finds required command behavior markers. |
| `python scripts/check_coverage_gate.py --fail-under 80` | Runs coverage from the Homework 6 root and exits 0 when the current selected suite remains above 80 percent. |
| `python scripts/check_coverage_gate.py --fail-under 99` | Exits nonzero and can be used for hook-trigger evidence without changing the committed 80 percent gate. |
| `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw diff --check` | Exits 0 with no whitespace errors. |

## Plan variance handling

Use `rule:lifecycle.variance-policy`. Before freeze, edit this draft directly for operator feedback. After freeze, record nontrivial implementation variance in `implementation-notes/variance-log.md`; use a plan amendment for high-impact architecture, API, data, security, privacy, compliance, scope, acceptance-criteria, or feasibility changes.

Likely local variance: the exact Claude hook schema in `.claude/settings.json` may need adjustment after inspecting local Claude Code support. This is a local technical variance if the committed hook still invokes the shared coverage helper and blocks below 80 percent. Removing either the command surface or skill surface is a scope change because the operator explicitly requested both.

## Planning artifact freeze gate

Use `module:freeze-gate`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, and `rule:freeze.stop-before-implementation`.

Draft review status: Awaiting operator approval.
Approval commit: Not created.
Post-freeze implementation authorization: Not granted.

## Completion criteria

- Acceptance criteria in `spec-themis-test-generator.md` are met.
- Required validation commands have been run and recorded.
- Required documentation artifacts have been created or updated.
- `homework-6/CHANGELOG.md` has a newest-first entry before each commit.
- Variance log is present and current when nontrivial variance occurs.
- De-facto sub-agent use is reported when applicable; this plan currently authorizes no sub-agents for implementing the control surface.

## Approval

- Status: Draft
- Superseded by: None

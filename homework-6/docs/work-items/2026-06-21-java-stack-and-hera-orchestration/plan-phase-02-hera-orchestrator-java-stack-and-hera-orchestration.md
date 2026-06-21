# Large or Phased Work Item Phase 02: Hera Orchestrator

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Short ID: `java-stack-and-hera-orchestration`
Status: Draft Review
Harness release: Installed global `dev-doc-harness` loaded on 2026-06-21
Schema: `schema:plan.phase`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:quality.phase-plan-fresh-thread`, `rule:models.strategy-required`, `rule:lifecycle.variance-policy`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, `rule:freeze.stop-before-implementation`

## Objective

Add Hera (Orchestrator) as a first-class Homework Automation Layer control surface that can plan, preserve, resume, compare, and explicitly select stack-specific generation sets across Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator).

This phase creates the Hera control package and entrypoint wrappers, updates standing Homework 6 guidance, and documents the approved nested-agent fallback behavior. It does not run Hera, does not generate Java outputs, does not select a Java package, and does not replace the current canonical Python submission.

## Input Context

The implementing agent must read these inputs before editing implementation-target files:

- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/spec-java-stack-and-hera-orchestration.md`, the approved anchor spec with Phase 02 as the Hera scope.
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/plan-phase-01-java-stack-readiness-java-stack-and-hera-orchestration.md`, the completed Phase 01 plan.
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/implementation-notes/variance-log.md`, especially the note that `.codex/config.toml` `agents.max_depth = 2` was included in Phase 01 by explicit operator authorization.
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/snapshots/phase-02-test-cases.snapshot.md`, the Phase 02 expected-behavior snapshot.
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/snapshots/phase-02-architecture.snapshot.md`, the Phase 02 architecture snapshot.
- `AGENTS.md`, `../HOMEWORK_STANDARDS.md`, `README.md`, `TASKS.md`, and `sample-transactions.json`.
- `docs/agent-runs/final-selection.md` and `docs/agent-runs/selection-sets.json`.
- Existing child-agent control packages:
  - `agent-control/write-spec/`
  - `agent-control/generate-code/`
  - `agent-control/generate-tests/`
  - `agent-control/generate-docs/`
  - `agent-control/operate-pipeline/`
- Existing thin wrappers under `.agents/skills/`, `.claude/skills/`, and `.claude/commands/`.
- `.codex/config.toml`, `mcp.json`, `.claude/settings.json`, `.githooks/pre-push`, `scripts/check_coverage_gate.py`, and `mcp/server.py`.
- Context7 notes from this planning pass:
  - JUnit current docs resolved to `/websites/junit_current` on 2026-06-21; Java child-run guidance should name JUnit Jupiter with Maven Surefire/Failsafe as the test execution family.
  - JaCoCo docs resolved to `/websites/jacoco_jacoco_trunk_doc` on 2026-06-21; Java child-run guidance should use the JaCoCo Maven plugin `check` goal for minimum coverage enforcement, with `haltOnFailure` blocking failed thresholds and ratios such as `0.80`.

Preserve these anchor-spec decisions:

- Hera is a Homework Automation Layer orchestrator, not a Generated Transaction System Layer runtime component.
- Hera owns sequence, run preservation, comparison, and explicit selection operations across child automation agents.
- Child agents remain responsible for their bounded generation quality bars and run artifacts.
- Python remains canonical unless an explicit later operator selection replaces it.
- Java remains a parallel alternate until explicitly selected.
- Hera must not let child agents silently target "latest"; every child invocation must name stack, source run IDs, inventories, selection records, and fingerprints.
- `.codex/config.toml` already contains `agents.max_threads = 8` and `agents.max_depth = 2`; Phase 02 must verify and document this setting, not duplicate it.
- If nested sub-agents do not work as expected, Hera must still dispatch Athena, Hephaestus, Themis, and Clio as first-level child agents and require those child agents to record degraded nested-agent unavailability while continuing when their own scope can be completed locally.

## Likely Files And Areas

Expected implementation files:

- `agents.md`
- New `agent-control/orchestrate-runs/README.md`
- New `agent-control/orchestrate-runs/workflow.md`
- New `agent-control/orchestrate-runs/quality-bar.md`
- New `agent-control/orchestrate-runs/run-registry.md`
- New `.agents/skills/orchestrate-runs/SKILL.md`
- New `.agents/skills/orchestrate-runs/agents/openai.yaml`
- New `.claude/skills/orchestrate-runs/SKILL.md`
- New `.claude/commands/orchestrate-runs.md`
- `docs/agent-runs/README.md`
- `docs/agent-runs/final-selection.md`
- `docs/agent-runs/selection-sets.json`, only if a schema note or neutral field is needed for future Hera-owned set registration. Do not create a Java set in Phase 02.
- `CHANGELOG.md` before the implementation commit.
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/deltas/operator-manual.delta.md`
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/deltas/architecture-summary.delta.md`
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/deltas/testing-guide.delta.md`, only if validation or command documentation changes.
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/implementation-notes/variance-log.md`

Do not edit canonical generated product files in this phase:

- `integrator.py`
- `agents/*.py`
- `tests/*.py`
- `specification.md`
- `research-notes.md`
- `README.md`, `HOWTORUN.md`, `ARCHITECTURE.md`, `TESTING_GUIDE.md`, `API_REFERENCE.md`
- `docs/pr-description-draft.md`
- `docs/screenshots/*`
- `mcp/server.py`, unless a static documentation link needs a comment-free path reference outside the generated product contract. The expected plan is no `mcp/server.py` edit.

## Model And Sub-Agent Strategy

Current orchestration: Codex Desktop local coding agent; exact model label and reasoning-effort selector are not exposed in this thread.
Fit assessment: Phase 02 is high-blast-radius control-surface design work. It changes orchestration authority and selection language but should be mostly documentation and wrapper scaffolding, not runtime code.
Recommended change: Use the active `enterprise-default` policy. Use the latest strongest available model class with high reasoning for Hera workflow design, selection semantics, fallback behavior, and final review. Smaller/faster profiles are acceptable only for read-only inventory scans.

Sub-agents: Optional, read-only only during Phase 02 implementation unless the operator explicitly authorizes write-capable sub-agents. The orchestration thread owns all edits and final integration.

| Purpose | Context strategy | Input context | Output artifact | Model policy | Model class/profile | Reasoning effort | Reason | Parallel? | Blast radius if wrong |
|---|---|---|---|---|---|---|---|---|---|
| Inventory child-agent entrypoints and run registries for Hera references | curated artifacts | This phase plan, `agent-control/`, `.agents/skills/`, `.claude/skills/`, `.claude/commands/`, `docs/agent-runs/` | Read-only inventory memo | `enterprise-default` | smaller/faster current profile | medium | Broad text scan is bounded and low-risk | Yes | Medium: missed wrapper or registry path can make Hera incomplete |
| Review Hera selection ownership and layer boundaries | curated artifacts | Approved spec, final-selection records, selection-set registry, `agents.md`, child-agent workflows | Read-only review memo | `enterprise-default` | latest strongest available profile | high | Hera authority affects future canonical-copy decisions | Yes | High: unclear ownership can corrupt selection records or blur runtime layers |

Planned Hera behavior during later Phase 03 may use four first-level child agents, one each for Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator). That later child-agent dispatch is not executed in Phase 02. Phase 02 only documents the approved pattern and fallback.

## Tasks

- [ ] Confirm branch is `homework-6-extension` or another explicit Homework 6 working branch, and confirm the worktree has no unrelated staged files before editing.
- [ ] Confirm the Phase 01 implementation is present:
  - `docs/agent-runs/selection-sets.json` exists and marks only `python-canonical-20260621` as canonical.
  - `.codex/config.toml` parses and contains `[agents] max_threads = 8` and `max_depth = 2`.
  - `scripts/check_coverage_gate.py --stack auto` exists as the stack-aware helper.
- [ ] Create `agent-control/orchestrate-runs/README.md` as the tool-neutral Hera package entrypoint. It must define Hera as Homework Automation Layer orchestration and list `workflow.md`, `quality-bar.md`, and `run-registry.md` as mandatory child references.
- [ ] Create `agent-control/orchestrate-runs/workflow.md` with these modes:
  - `generate-set`: create a preserved stack-specific end-to-end set through Athena, Hephaestus, Themis, and Clio.
  - `resume-set`: resume a bounded Hera run from preserved state and child-run records.
  - `compare-set`: compare preserved stack sets or individual child runs without changing canonical root files.
  - `select-set`: perform explicit inventory-driven selection updates, with Python replacement prohibited unless the operator explicitly requests it.
- [ ] In Hera workflow required context, require reading the approved selection metadata first:
  - `docs/agent-runs/selection-sets.json`
  - `docs/agent-runs/final-selection.md`
  - child-agent run inventories named by selected package sets
  - the relevant child-agent control package for each child operation
- [ ] In Hera workflow child invocation rules, require every child prompt or handoff to include:
  - parent Hera run ID
  - requested mode and selected stack
  - source or selected run IDs
  - inventory paths
  - selection record paths
  - source and current spec fingerprints when applicable
  - package-set ID when one exists
  - explicit statement that "latest" targeting is not allowed
- [ ] In Hera workflow fallback rules, document that `agents.max_depth = 2` is already configured, but runtime support may still be unavailable. If nested child agents fail, Hera records the limitation in run metadata, dispatches the four primary child agents as first-level sub-agents when the platform supports first-level dispatch, and instructs each child to continue without nested sub-agents when quality remains acceptable.
- [ ] Create `agent-control/orchestrate-runs/quality-bar.md` covering:
  - Layer separation: Hera must not appear as a runtime transaction pipeline component, Java class, Python module under generated product code, settlement/reporting component, or MCP tool.
  - Selection safety: no canonical replacement without explicit operator selection and inventory-declared copy targets.
  - Traceability: child run IDs, inventories, fingerprints, Context7 notes, validation evidence, and package-set IDs are recorded.
  - Privacy: Hera reports safe counts, reason codes, paths, run IDs, and validation status only; it must not print raw account IDs, raw descriptions, credentials, or full input payloads.
  - Degraded mode: nested-agent unavailability is recorded and does not silently reduce child-agent quality bars.
- [ ] Create `agent-control/orchestrate-runs/run-registry.md` with the Hera run ID and layout:

  ```text
  YYYYMMDD-HHMMSS-orchestrate-runs-<stack>-short-label

  docs/agent-runs/HERA_RUN_ID/
    run-metadata.md
    inputs/
      source-context.md
      selected-python-set.snapshot.md
      requested-stack-profile.snapshot.md
    agent-5-orchestrator/
      child-runs.md
      comparisons/
      selection-plan.md
      validation-checklist.md
      handoff.md
  ```

- [ ] In the Hera run registry, define `child-runs.md` as the mandatory ledger of child Athena, Hephaestus, Themis, and Clio run IDs, statuses, modes, inventories, validation commands, and blockers.
- [ ] In the Hera run registry, define `selection-plan.md` as a plan or proposal until explicit operator selection. It must not be treated as authorization to copy canonical root files.
- [ ] Add the Codex wrapper `.agents/skills/orchestrate-runs/SKILL.md` as a thin entrypoint that requires reading Hera `workflow.md`, `quality-bar.md`, and `run-registry.md` before acting.
- [ ] Add `.agents/skills/orchestrate-runs/agents/openai.yaml` with display metadata matching the existing skill metadata style.
- [ ] Add the Claude project skill `.claude/skills/orchestrate-runs/SKILL.md` with argument examples for `generate-set`, `resume-set`, `compare-set`, and `select-set`.
- [ ] Add the Claude legacy command wrapper `.claude/commands/orchestrate-runs.md` as a thin pointer to `agent-control/orchestrate-runs/`.
- [ ] Update `agents.md` to add Hera (Orchestrator) to the Homework Automation Layer roles, clarify it is not a runtime pipeline component, and preserve the existing Greek-label pairing rule.
- [ ] Update `docs/agent-runs/README.md` to list Hera's tool-neutral package and run-registry location without restating the full workflow.
- [ ] Update `docs/agent-runs/final-selection.md` only as needed to state that future cross-set comparison and selection are Hera-owned while the current Python canonical set remains unchanged.
- [ ] Leave `docs/agent-runs/selection-sets.json` unchanged unless implementation discovers a neutral schema note is required. If changed, preserve `canonical_set_id = "python-canonical-20260621"` and do not add a Java set in Phase 02.
- [ ] Update the work-item deltas:
  - `deltas/operator-manual.delta.md` with Hera invocation, run preservation, and explicit selection decision flow.
  - `deltas/architecture-summary.delta.md` with Hera as the fifth Homework Automation Layer agent.
  - `deltas/testing-guide.delta.md` only if Phase 02 changes validation guidance that later Clio documentation should absorb.
- [ ] Update `implementation-notes/variance-log.md` during implementation if any nontrivial implementation shape differs from this plan.
- [ ] Update `CHANGELOG.md` before the implementation commit.
- [ ] Review the diff for unrelated changes, accidental generated product edits, Java generation output, canonical replacement language, unresolved draft markers, and raw sample data.

## Tests And Validation

| Command | Expected result |
|---|---|
| `git branch --show-current` | Reports `homework-6-extension` unless the operator explicitly switched branches. |
| `git status --short --untracked-files=all` | Shows only scoped Phase 02 changes plus any pre-existing work explicitly accepted by the operator. |
| `python -m json.tool docs/agent-runs/selection-sets.json` | Selection-set registry remains valid JSON and keeps `python-canonical-20260621` canonical. |
| `python -c "import tomllib; tomllib.load(open('.codex/config.toml','rb'))"` | Codex config parses. |
| `python -c "import tomllib; c=tomllib.load(open('.codex/config.toml','rb')); assert c['agents']['max_threads']==8; assert c['agents']['max_depth']==2"` | Confirms Phase 02 can rely on the configured agent thread and depth values. |
| `python -m json.tool mcp.json` | MCP config remains valid JSON. |
| `python -m pytest tests\test_coverage_gate.py -q -p no:cacheprovider` | Phase 01 helper tests still pass after Hera control-surface additions. |
| `python scripts/check_coverage_gate.py --stack java --project-dir . --fail-under 80` | Fails clearly because the current root is not a Java Maven project; expected signal is a controlled missing-`pom.xml` or Java-project error, not a traceback. |
| `git diff --check` | No whitespace errors in the working diff. |
| `git diff -- specification.md README.md HOWTORUN.md ARCHITECTURE.md TESTING_GUIDE.md API_REFERENCE.md integrator.py agents tests docs/screenshots mcp/server.py` | Empty for canonical generated product/runtime/reviewer documentation targets that Phase 02 must not edit. |
| `rg -n "orchestrate-runs|Hera" agent-control .agents .claude agents.md docs/agent-runs docs/work-items/2026-06-21-java-stack-and-hera-orchestration` | Hera references are present in Operator/Homework Automation Layer control surfaces and absent from generated runtime product paths. |
| `rg -n "latest" agent-control/orchestrate-runs .agents/skills/orchestrate-runs .claude/skills/orchestrate-runs .claude/commands/orchestrate-runs.md` | Any matches reject silent "latest" targeting or refer to model policy; no Hera workflow tells child agents to target latest files. |
| `rg -n "ACC-[0-9]{4}|Monthly rent payment|Equipment purchase|Property settlement|Salary advance" agent-control/orchestrate-runs .agents/skills/orchestrate-runs .claude/skills/orchestrate-runs .claude/commands/orchestrate-runs.md agents.md docs/agent-runs/final-selection.md` | No raw sample account IDs or raw sample descriptions are introduced. Existing `sample-transactions.json` is outside this scan. |
| `Test-Path agent-control/orchestrate-runs/workflow.md; Test-Path agent-control/orchestrate-runs/quality-bar.md; Test-Path agent-control/orchestrate-runs/run-registry.md; Test-Path .agents/skills/orchestrate-runs/SKILL.md; Test-Path .claude/skills/orchestrate-runs/SKILL.md; Test-Path .claude/commands/orchestrate-runs.md` | All required Hera control-package and wrapper files exist. |

Do not run Hera, do not spawn child generation agents, do not run Java generation, and do not install or download Java dependencies in Phase 02.

## Documentation Artifact Matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Before implementation commit | `CHANGELOG.md` | Newest-first Phase 02 implementation entry |
| Test cases | Snapshot | Yes | Before implementation | `snapshots/phase-02-test-cases.snapshot.md` | Captures Hera control-surface, selection, fallback, and non-generation expectations |
| Testing guide delta | Living delta | Deferred | During implementation only if validation guidance changes | `deltas/testing-guide.delta.md` | Phase 02 is orchestration-control work; update only if command or validation behavior changes |
| Operator manual delta | Living delta | Yes | During implementation | `deltas/operator-manual.delta.md` | Required because Hera adds new operator invocation and selection flow |
| API reference delta | Living delta | No | Not applicable | Not applicable | No public runtime API or MCP tool/resource change is planned |
| Architecture snapshot | Snapshot | Yes | Before implementation | `snapshots/phase-02-architecture.snapshot.md` | Captures Hera as Automation Layer orchestrator and non-runtime boundary |
| Architecture summary delta | Living delta | Yes | During implementation | `deltas/architecture-summary.delta.md` | Required because Hera changes the Homework Automation Layer architecture |

## Documentation Tasks

- Required now: keep this phase plan and the Phase 02 snapshots current before draft review.
- During implementation: update `deltas/operator-manual.delta.md` with Hera modes, run ledger, comparison, and explicit selection flow.
- During implementation: update `deltas/architecture-summary.delta.md` with Hera as the fifth Homework Automation Layer agent and the parent/child run relationship.
- During implementation: update `deltas/testing-guide.delta.md` only if validation commands or coverage expectations change.
- During implementation: update `implementation-notes/variance-log.md` for nontrivial deviations.
- Before implementation commit: update `CHANGELOG.md` with the Phase 02 implementation entry.
- Not applicable in this phase: `deltas/api-reference.delta.md`, because Hera does not change runtime APIs or MCP public contracts.

## Variance Reminder

Use `rule:lifecycle.variance-policy`. Before freeze, edit this draft directly for operator feedback. After freeze, record nontrivial implementation variance in `implementation-notes/variance-log.md`; use a plan amendment for high-impact architecture, API, data, security, privacy, compliance, scope, acceptance-criteria, or feasibility changes.

High-impact variance examples that require an amendment before proceeding:

- Running Hera or child generation agents in Phase 02.
- Adding a Java package set to `selection-sets.json`.
- Replacing the Python canonical package.
- Implementing Hera inside generated runtime code, `mcp/server.py`, or Java/Python transaction pipeline modules.
- Allowing markdown-only "latest" targeting instead of explicit run IDs, inventories, and fingerprints.
- Dropping the nested-agent fallback recording requirement.

## Planning Artifact Freeze Gate

Draft review checkpoint:

- Status: Draft Review.
- Draft artifacts: this phase plan, `snapshots/phase-02-test-cases.snapshot.md`, and `snapshots/phase-02-architecture.snapshot.md`.
- Draft validation: run placeholder, unresolved-decision, and whitespace checks before staging for review.
- Staging rule: stage only Phase 02 draft planning artifacts and the approved spec numbering correction for operator review; do not commit before approval.

Approval freeze checkpoint after operator approval:

- Update `CHANGELOG.md` with a newest-first entry for Phase 02 planning.
- Verify approved artifacts contain no placeholders, unresolved required decisions, or missing required sections.
- Stage only the approved Phase 02 planning artifacts, the spec numbering correction if still unstaged, and `CHANGELOG.md`.
- Commit the approved planning package.
- Stop before implementation and ask for fresh implementation authorization plus model, reasoning-effort, and sub-agent policy confirmation.

## Handoff Output

At Phase 02 implementation completion, the implementing agent must report:

- Assigned scope and confirmation that Hera was added only as a control surface.
- Confirmation that Java generation, child Athena/Hephaestus/Themis/Clio runs, Java selection, and Python replacement were not started.
- Files changed and files deliberately left unchanged.
- Hera modes, run layout, child-run ledger, comparison, and selection-plan behavior.
- How `agents.max_depth = 2` was verified and how nested-agent fallback is documented.
- Whether `selection-sets.json` changed; if so, why it remained canonical-Python only.
- Commands and tests run with outcomes.
- Privacy and canonical-output safety review result.
- Variance log status.
- De-facto sub-agent use, including count, roles/scopes, concurrency or waves, context strategy, observed inheritance behavior, and de-facto model/model class/profile when known.
- Recommended next prompt for Phase 03 Java alternate generation planning or Phase 02 follow-up repair.

## Completion Criteria

- Hera is represented as a first-class Homework Automation Layer orchestrator in `agents.md`.
- `agent-control/orchestrate-runs/` exists with workflow, quality-bar, run-registry, and README files.
- Codex and Claude entrypoints exist and are thin wrappers around the shared Hera package.
- Hera modes cover `generate-set`, `resume-set`, `compare-set`, and `select-set`.
- Hera run layout preserves parent run metadata, child-run ledger, comparison notes, selection plan, validation checklist, and handoff.
- Child invocations require explicit stack, run IDs, inventories, selection records, and fingerprints; silent "latest" targeting is rejected.
- Nested-agent fallback behavior is documented against the already-configured `agents.max_depth = 2`.
- Python remains the only canonical package set after Phase 02.
- No Java generation, Java package selection, canonical root replacement, runtime product edit, or MCP contract change occurs in this phase.
- Required deltas and variance log are present or explicitly marked not applicable with reason.
- `CHANGELOG.md` has a newest-first entry before the implementation commit.
- Validation commands have been run and recorded.
- De-facto sub-agent use is reported when applicable.

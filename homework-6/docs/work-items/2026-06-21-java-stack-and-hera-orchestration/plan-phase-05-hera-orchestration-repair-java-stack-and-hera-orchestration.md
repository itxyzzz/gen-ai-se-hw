# Large or Phased Work Item Phase 05: Hera Orchestration Repair

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Short ID: `java-stack-and-hera-orchestration`
Status: Draft Review
Harness release: Installed global `dev-doc-harness` loaded on 2026-06-21
Schema: `schema:plan.phase`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:quality.phase-plan-fresh-thread`, `rule:models.strategy-required`, `rule:lifecycle.variance-policy`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, `rule:freeze.stop-before-implementation`

## Objective

Repair Hera (Orchestrator) control-surface guidance so future `generate-set` runs cannot silently perform Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator) work sequentially in the parent thread when first-level child-agent dispatch is available.

## Input context

Implementation must read:

- `spec-java-stack-and-hera-orchestration.md`
- `plan-amendment-001-java-helper-and-hera-repair-java-stack-and-hera-orchestration.md`
- `agent-control/orchestrate-runs/workflow.md`
- `agent-control/orchestrate-runs/quality-bar.md`
- `agent-control/orchestrate-runs/run-registry.md`
- `.agents/skills/orchestrate-runs/SKILL.md`
- `.claude/skills/orchestrate-runs/SKILL.md`
- `.claude/commands/orchestrate-runs.md`
- Hera review evidence at `docs/agent-runs/20260621-145059-orchestrate-runs-java-alternate/agent-5-orchestrator/orchestration-review.md`
- Official OpenAI/Codex documentation when there is any uncertainty about current thread, sub-agent, or instruction-following behavior that might explain why Hera ignored the existing child-dispatch instructions.

## Likely files and areas

- `agent-control/orchestrate-runs/workflow.md`
- `agent-control/orchestrate-runs/quality-bar.md`
- `agent-control/orchestrate-runs/run-registry.md`
- `.agents/skills/orchestrate-runs/SKILL.md` only if the thin wrapper must point to new mandatory language
- `.claude/skills/orchestrate-runs/SKILL.md` only if the thin wrapper must point to new mandatory language
- `.claude/commands/orchestrate-runs.md` only if the thin wrapper must point to new mandatory language
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/deltas/operator-manual.delta.md`
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/deltas/architecture-summary.delta.md`
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/implementation-notes/variance-log.md`
- `CHANGELOG.md`

## Model and Sub-agent Strategy

Current orchestration: Codex Desktop local coding agent; exact model label and reasoning-effort selector are not exposed in this thread.
Fit assessment: High reasoning value and medium-high blast radius. The change is mostly documentation/control-surface text, but it defines how future agents spend context and how run evidence is judged.
Recommended change: Use strongest available reasoning in the main thread. No write-capable sub-agents are needed for the control-surface edit.

Sub-agents: None for implementation. A future Hera rerun should use child agents as required by the repaired Hera guidance; that rerun is outside this repair implementation unless the operator explicitly authorizes a new clean generation run.

## Tasks

- [ ] Update Hera workflow so `generate-set` requires first-level child-agent dispatch for Athena, Hephaestus, Themis, and Clio when first-level dispatch is available.
- [ ] Change fallback behavior so Hera must stop as blocked when it cannot dispatch required first-level child agents and the operator's goal is to test orchestration behavior.
- [ ] Clarify that the parent Hera thread owns setup, sequencing, ledger updates, child prompt construction, integration of child handoffs, comparison, and selection planning, but not direct child deliverable generation.
- [ ] Add a rejection gate for evidence that says later child work ran in the main orchestration thread because outputs were tightly coupled.
- [ ] Require child-run ledger entries to record dispatch mechanism, not only child run IDs.
- [ ] Require handoff to report whether each of the four Homework Automation Layer agents ran as a child agent, degraded child-local execution, or was blocked.
- [ ] If runtime semantics are unclear, consult official OpenAI/Codex documentation before attributing why Hera ignored the existing instructions; record docs consulted or state that the repair is based only on local run evidence.
- [ ] Update deltas so future operator documentation can describe the stricter Hera dispatch contract.
- [ ] Update `CHANGELOG.md` and variance log before the implementation commit.

## Tests and validation

| Command | Expected result |
|---|---|
| `rg -n "main orchestration thread|tightly coupled|first-level child" agent-control/orchestrate-runs .agents/skills/orchestrate-runs .claude/skills/orchestrate-runs .claude/commands/orchestrate-runs.md` | No permissive language remains that allows child deliverable generation in the parent thread when first-level child dispatch is available. Required first-level child-agent fallback language is present. |
| `rg -n "dispatch mechanism|Athena.*Hephaestus.*Themis.*Clio|blocked" agent-control/orchestrate-runs` | Run registry and handoff rules require clear child dispatch accounting and blocked status when dispatch is unavailable. |
| `python -m json.tool docs\agent-runs\selection-sets.json` | Selection registry remains valid and canonical Python remains unchanged. |
| `python -c "import pathlib,tomllib; data=tomllib.loads(pathlib.Path('.codex/config.toml').read_text()); assert data['agents']['max_threads']==8; assert data['agents']['max_depth']==2"` | Agent config still records `max_threads = 8` and `max_depth = 2`. |
| Documentation consultation note in implementation handoff or variance log | If there was doubt about Codex thread or sub-agent behavior, official OpenAI/Codex documentation was consulted and cited; otherwise the handoff states the repair is based on local run evidence only. |
| `git diff --check` | No whitespace errors. |
| Protected-output diff review | No canonical generated product files, selected docs, stable screenshots, or `mcp/server.py` are changed. |

## Documentation artifact matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Before implementation commit | `CHANGELOG.md` | Record Hera control-surface repair. |
| Test cases | Snapshot | No | Not applicable | Not applicable | Static validation commands are captured in this phase plan. |
| Testing guide delta | Living delta | No | Not applicable | Not applicable | No runtime testing guide change. |
| Operator manual delta | Living delta | Yes | During implementation | `deltas/operator-manual.delta.md` | Add stricter Hera child-agent dispatch guidance. |
| API reference delta | Living delta | No | Not applicable | Not applicable | No runtime API change. |
| Architecture snapshot | Snapshot | No | Not applicable | Not applicable | Existing architecture snapshot plus amendment covers the boundary. |
| Architecture summary delta | Living delta | Yes | During implementation | `deltas/architecture-summary.delta.md` | Record Hera as parent orchestrator, not child deliverable generator. |

## Variance reminder

Use `rule:lifecycle.variance-policy`. Before freeze, edit this draft directly for operator feedback. After freeze, record nontrivial implementation variance in `implementation-notes/variance-log.md`; use a plan amendment for high-impact architecture, API, data, security, privacy, compliance, scope, acceptance-criteria, or feasibility changes.

## Planning artifact freeze gate

Use `module:freeze-gate`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, and `rule:freeze.stop-before-implementation`.

Approval status: Draft review. Implementation remains paused until this plan is approved and frozen.

## Handoff output

Report files changed, validation commands run, whether permissive parent-thread generation language was removed, and the exact clean-thread prompt recommended for a later Hera rerun.

## Completion criteria

- Hera control docs require child-agent dispatch for all four Homework Automation Layer agents when first-level dispatch is available.
- Hera docs require blocked status instead of parent-thread child deliverable generation when dispatch is unavailable and orchestration behavior is under test.
- Run ledger and handoff rules record dispatch mechanism per child agent.
- Operator and architecture deltas capture the repaired contract for future documentation regeneration.
- No canonical Python output, selected docs, stable screenshots, `selection-sets.json`, `final-selection.md`, or `mcp/server.py` are changed.

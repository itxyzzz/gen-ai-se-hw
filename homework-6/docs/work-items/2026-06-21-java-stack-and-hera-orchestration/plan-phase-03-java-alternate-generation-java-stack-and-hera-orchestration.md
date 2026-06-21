# Large or Phased Work Item Phase 03: Java Alternate Generation

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Short ID: `java-stack-and-hera-orchestration`
Status: Draft Review
Harness release: Installed global `dev-doc-harness` loaded on 2026-06-21
Schema: `schema:plan.phase`

## Objective

Authorize a clean-thread Hera (Orchestrator) run that creates a preserved Java alternate package set through Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator), without replacing the current canonical Python package.

## Clean Execution Rule

Do not copy this phase plan into Hera's execution prompt. Hera's own `orchestrate-runs` skill and `agent-control/orchestrate-runs/` package are the source of truth for run setup, child invocation rules, validation, privacy, and handoff behavior.

The clean execution thread should receive only this operator instruction:

```text
Use Hera (Orchestrator) to run generate-set for stack=java. Create a preserved Java alternate through Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator). Do not replace or modify the canonical Python outputs. Preserve all outputs under docs/agent-runs and stop with a Hera handoff for later comparison.
```

## Scope

Included:

- Run Hera in the main thread.
- Generate a new Java alternate set through the four Homework Automation Layer child agents.
- Preserve Hera and child-agent outputs under `docs/agent-runs/`.
- Leave canonical Python output selected and unchanged.
- Stop after Hera handoff evidence.

Excluded:

- No Java canonical selection.
- No canonical Python replacement.
- No root generated product copy.
- No Phase 04 comparison in the same execution step.
- No extra child-agent instructions beyond what Hera's skill already provides.

## Model And Sub-Agent Strategy

Current orchestration: Codex Desktop local coding agent; exact model label and reasoning-effort selector are not exposed in this thread.

Recommended execution: use the strongest available main-thread reasoning profile for Hera. Hera may use child agents according to its own skill and repository configuration. This phase plan does not add extra sub-agent roles, prompts, or constraints.

## Tasks

- [ ] Freeze this minimal Phase 03 planning package.
- [ ] Start a new clean thread rooted at `homework-6`.
- [ ] Send only the clean execution instruction from this plan.
- [ ] Let Hera load and follow the `orchestrate-runs` skill normally.
- [ ] After Hera completes or blocks, review the Hera handoff and verify Python canonical output was not replaced.

## Validation

| Check | Expected result |
|---|---|
| Hera run folder exists under `docs/agent-runs/` | A preserved `orchestrate-runs-java-*` run exists with handoff evidence. |
| Java child runs exist or blockers are recorded | Athena, Hephaestus, Themis, and Clio Java runs are preserved, or Hera records why a child could not complete. |
| Canonical Python selection remains intact | `docs/agent-runs/selection-sets.json` still keeps `python-canonical-20260621` canonical unless a later explicit selection authorizes a change. |
| Root canonical generated files are unchanged | No Java outputs are copied to root canonical paths during Phase 03. |

## Documentation Artifact Matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Freeze commit | `CHANGELOG.md` | Phase 03 plan-only freeze entry |
| Test cases | Snapshot | Yes | Draft review | `snapshots/phase-03-test-cases.snapshot.md` | Minimal clean-run expectations |
| Architecture snapshot | Snapshot | Yes | Draft review | `snapshots/phase-03-architecture.snapshot.md` | Minimal clean-thread orchestration boundary |
| Testing guide delta | Living delta | No | Not applicable | Not applicable | Hera run evidence is preserved under `docs/agent-runs/` |
| Operator manual delta | Living delta | No | Not applicable | Not applicable | Phase 02 already added Hera operation guidance |
| API reference delta | Living delta | No | Not applicable | Not applicable | No runtime API change |
| Architecture summary delta | Living delta | No | Not applicable | Not applicable | No standing architecture change beyond Phase 02 |

## Freeze Gate

After operator approval:

- Update `CHANGELOG.md`.
- Stage only this Phase 03 plan, the two Phase 03 snapshots, and `CHANGELOG.md`.
- Commit the plan-only freeze.
- Stop. Java generation begins only from a fresh clean-thread instruction.

## Completion Criteria

- The Phase 03 planning package is frozen.
- The next execution thread can invoke Hera with the single clean instruction above.
- Hera, not this plan, supplies detailed orchestration behavior.

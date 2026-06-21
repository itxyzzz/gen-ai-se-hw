# Variance Log

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Short ID: `java-stack-and-hera-orchestration`

## Purpose

Record nontrivial implementation variance after the Phase 01 plan is frozen. Before freeze, edit the draft phase plan and snapshots directly.

## Entries

### 2026-06-21 - Phase 05 implementation

Variance class: None for scope, architecture, public API, data, security, privacy, compliance, acceptance criteria, or feasibility.

Notes:

- Tightened the existing Hera control package rather than editing the thin Codex or Claude wrappers, because the wrappers already mandate `agent-control/orchestrate-runs/` as the canonical workflow source.
- Treated the repair as based on local run evidence only. No official OpenAI/Codex documentation was consulted because the implementation does not attribute a runtime cause for the prior Hera behavior; it only strengthens the repository contract and evidence rejection gates.
- Required blocked status when first-level child-agent dispatch is unavailable during a Hera `generate-set` orchestration test, rather than allowing parent-thread generation of child deliverables.

### 2026-06-21 - Phase 04 implementation

Variance class: Local technical.

Notes:

- Added optional Java-only Maven settings flags directly to `scripts/check_coverage_gate.py` instead of adding a separate Java wrapper. This preserves the approved universal-helper approach and keeps the default Python and Java commands unchanged when the flags are omitted.
- Settings paths resolve relative to the caller's current directory first, then `--project-dir`, so operator commands work both from the Homework 6 root and from a Java validation workspace.
- The override is documented as an environment repair for inherited Maven settings, not as a generated Java product requirement.

### 2026-06-21 - Phase 01 implementation

Variance class: None for scope, architecture, public API, data, security, privacy, compliance, acceptance criteria, or feasibility.

Notes:

- Included `.codex/config.toml` in the implementation commit because the operator explicitly authorized committing the existing `agents.max_depth = 2` change for this phase.
- Implemented `scripts/check_coverage_gate.py --stack auto` so it reads `docs/agent-runs/selection-sets.json` when present, then falls back to Java when `pom.xml` exists, otherwise Python. This is consistent with the approved helper dispatch design and avoids markdown parsing.
- Kept `mcp/server.py` unchanged because Java guidance now targets the existing stack-neutral `shared/results/` JSON contract.
- Did not implement Hera (Orchestrator), run Java generation, replace the canonical Python package, or edit canonical generated product documentation.

### 2026-06-21 - Phase 02 implementation

Variance class: None for scope, architecture, public API, data, security, privacy, compliance, acceptance criteria, or feasibility.

Notes:

- Added Hera (Orchestrator) only as a Homework Automation Layer control surface with tool-neutral workflow, quality-bar, run-registry, Codex skill, Claude project skill, and Claude legacy command wrapper.
- Left `docs/agent-runs/selection-sets.json` unchanged because no neutral schema change was required and Python remains the only canonical package set.
- Treated `.codex/config.toml` `agents.max_depth = 2` as existing Phase 01 state and verified it instead of changing the file.
- Updated the existing Phase 01 deltas with Phase 02 Hera notes instead of creating separate duplicate deltas for the same large/phased work item.
- Did not run Hera, spawn child generation agents, generate Java output, select Java, replace Python canonical output, or edit generated runtime product files.

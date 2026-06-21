# Variance Log

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Short ID: `java-stack-and-hera-orchestration`

## Purpose

Record nontrivial implementation variance after the Phase 01 plan is frozen. Before freeze, edit the draft phase plan and snapshots directly.

## Entries

### 2026-06-21 - Phase 01 implementation

Variance class: None for scope, architecture, public API, data, security, privacy, compliance, acceptance criteria, or feasibility.

Notes:

- Included `.codex/config.toml` in the implementation commit because the operator explicitly authorized committing the existing `agents.max_depth = 2` change for this phase.
- Implemented `scripts/check_coverage_gate.py --stack auto` so it reads `docs/agent-runs/selection-sets.json` when present, then falls back to Java when `pom.xml` exists, otherwise Python. This is consistent with the approved helper dispatch design and avoids markdown parsing.
- Kept `mcp/server.py` unchanged because Java guidance now targets the existing stack-neutral `shared/results/` JSON contract.
- Did not implement Hera (Orchestrator), run Java generation, replace the canonical Python package, or edit canonical generated product documentation.

# Architecture Summary Delta

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Phase: Phase 01 Java stack readiness and Phase 02 Hera orchestration

## Proposed Updates

- Add `docs/agent-runs/selection-sets.json` as the machine-readable package-set registry.
- Keep `docs/agent-runs/final-selection.md` as the human-readable selection narrative and audit record.
- Treat Python as the canonical selected package set after Phase 01.
- Treat future Java output as an alternate package set until explicitly selected.
- Keep `mcp/server.py` as the Python FastMCP status reader over stack-neutral result JSON.
- Keep operation helpers universal:
  - `/run-pipeline` chooses a stack command from explicit input or selection-set metadata.
  - `/validate-transactions` chooses Python import behavior or Java validation-only CLI behavior from the selected package set.
  - `scripts/check_coverage_gate.py` dispatches to pytest/pytest-cov or Maven/JUnit/JaCoCo.
- Require Java generation guidance to preserve Maven, `pom.xml`, `src/main/java/...`, `src/test/java/...`, `BigDecimal`, JUnit 5/JUnit Jupiter, JaCoCo, and the existing safe `shared/results/` contract.
- Add Hera (Orchestrator) as the fifth Homework Automation Layer control surface, separate from the four assignment deliverable generation agents.
- Keep Hera outside the Generated Transaction System Layer. It is not a runtime pipeline component, Java class, Python product module, MCP tool, settlement component, or reporting component.
- Add `agent-control/orchestrate-runs/` as the shared package for Hera workflow, quality-bar, and run-registry rules.
- Add Codex and Claude entrypoints as thin wrappers around the shared Hera package:
  - `.agents/skills/orchestrate-runs/SKILL.md`
  - `.claude/skills/orchestrate-runs/SKILL.md`
  - `.claude/commands/orchestrate-runs.md`
- Preserve Hera run evidence under `docs/agent-runs/<hera-run-id>/agent-5-orchestrator/`, with `child-runs.md` as the ledger and `selection-plan.md` as non-authorizing proposal state.
- Keep child agents responsible for their own quality bars while Hera owns sequence, preservation, comparison, and explicit selection workflow.
- Define Hera `generate-set` as a parent-orchestration workflow that dispatches Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator) as first-level child agents when first-level dispatch is available.
- Treat the parent Hera thread as the coordinator for setup, sequencing, child prompts, ledger updates, handoff integration, comparison, and selection planning, not as an implementation surface for child deliverables.
- Record dispatch mechanism as part of package-set provenance so future comparisons can distinguish clean child-agent orchestration, degraded child-local execution, reused prior runs, and blocked stages.
- Treat parent-thread child deliverable generation during `generate-set` as invalid orchestration evidence when first-level dispatch was available or when the goal was to test Hera orchestration behavior.

## Not Merged Into Canonical Architecture Yet

The canonical `ARCHITECTURE.md` remains selected Clio output and was not edited in Phase 01 or Phase 02. A future Clio regeneration should merge this delta into reviewer-facing documentation.

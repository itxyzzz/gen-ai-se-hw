# Operator Manual Delta

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Phase: Phase 01 Java stack readiness and Phase 02 Hera orchestration

## Proposed Updates

- Resolve package set before running `/run-pipeline`, `/validate-transactions`, or coverage checks.
- Prefer explicit `stack=python`, `stack=java`, or package-set ID when supplied by the operator.
- Otherwise read `docs/agent-runs/selection-sets.json` and use its `canonical_set_id`.
- Keep `docs/agent-runs/final-selection.md` as the human audit history, not a script parsing source.
- Use the current Python command hints for the canonical set:
  - Pipeline: `python integrator.py`
  - Validation-only: import `agents.transaction_validator.validate_transactions_file`
  - Coverage: `python scripts/check_coverage_gate.py --stack python --fail-under 80`
- For Java alternates, use the command hints or selected inventory from the Java package set, normally Maven/JUnit/JaCoCo plus a generated Java pipeline or validation-only CLI.
- Do not treat Java as canonical until a later explicit operator selection marks a Java package set canonical.
- Use Hera (Orchestrator) through `orchestrate-runs` when coordinating full package sets across Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator).
- Hera modes:
  - `generate-set`: preserve a stack-specific end-to-end set through the child agents.
  - `resume-set`: continue from named Hera run metadata and child-run records.
  - `compare-set`: compare preserved package sets or child runs without canonical root changes.
  - `select-set`: update package-set selection only after explicit operator authorization and inventory-declared copy-target review.
- Preserve Hera evidence under `docs/agent-runs/YYYYMMDD-HHMMSS-orchestrate-runs-<stack>-short-label/agent-5-orchestrator/`.
- Treat `agent-5-orchestrator/child-runs.md` as the mandatory child-run ledger and `agent-5-orchestrator/selection-plan.md` as a proposal until explicit selection.
- For `generate-set`, Hera must dispatch Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator) as first-level child agents when first-level child-agent dispatch is available.
- Hera's parent thread owns setup, sequencing, ledger updates, child prompt construction, integration of child handoffs, comparison, and selection planning. It does not directly generate child deliverables such as specifications, runtime code packages, tests, validation inventories, reviewer docs, or screenshots.
- When nested child-agent dispatch is unavailable despite `agents.max_depth = 2`, record degraded mode and use first-level child agents when available; do not waive child-agent quality bars.
- When first-level child-agent dispatch is unavailable during a Hera `generate-set` orchestration test, record the run as blocked instead of generating the four child stages sequentially in the parent thread.
- Require `agent-5-orchestrator/child-runs.md` and `agent-5-orchestrator/handoff.md` to state the dispatch mechanism for each child stage: first-level child agent, degraded child-local execution, reused prior run, or blocked.
- Reject Hera evidence that says child deliverables were generated in the main orchestration thread because outputs were tightly coupled or context was low.

## Not Merged Into Canonical Manual Yet

The canonical `HOWTORUN.md` remains selected Clio output and was not edited in Phase 01 or Phase 02. A future Clio regeneration should merge this delta into reviewer-facing documentation.

# Operator Manual Delta

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Phase: Phase 01 Java stack readiness

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

## Not Merged Into Canonical Manual Yet

The canonical `HOWTORUN.md` remains selected Clio output and was not edited in Phase 01. A future Clio regeneration should merge this delta into reviewer-facing documentation.

# Architecture Summary Delta

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Phase: Phase 01 Java stack readiness

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

## Not Merged Into Canonical Architecture Yet

The canonical `ARCHITECTURE.md` remains selected Clio output and was not edited in Phase 01. A future Clio regeneration should merge this delta into reviewer-facing documentation.

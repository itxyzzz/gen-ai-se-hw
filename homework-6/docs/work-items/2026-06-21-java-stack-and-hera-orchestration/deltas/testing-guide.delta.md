# Testing Guide Delta

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Phase: Phase 01 Java stack readiness and Phase 02 Hera orchestration

## Proposed Updates

- Document `scripts/check_coverage_gate.py` as the universal coverage helper.
- Preserve the existing default command:

  ```bash
  python scripts/check_coverage_gate.py --fail-under 80
  ```

- Add explicit stack commands:

  ```bash
  python scripts/check_coverage_gate.py --stack python --fail-under 80
  python scripts/check_coverage_gate.py --stack java --project-dir path/to/java-package --fail-under 80
  ```

- State that `--stack auto` reads `docs/agent-runs/selection-sets.json` when present, then falls back to Java when `pom.xml` exists, otherwise Python.
- State that Java coverage requires a Maven project with a JaCoCo `check` goal and uses `mvn -Dcoverage.minimum=0.80 test jacoco:report jacoco:check`.
- Add focused helper tests under `tests/test_coverage_gate.py` for argument parsing, Python command construction, registry-backed auto resolution, and Java missing-`pom.xml` failure.
- Phase 02 adds no new coverage command family. Hera validation records JSON/TOML checks, child-run ledger completeness, protected-output diffs, and privacy scans in `agent-5-orchestrator/validation-checklist.md`.
- Future Hera-driven Java alternates should still use the Phase 01 Java coverage command through the selected package set or child inventories.

## Not Merged Into Canonical Guide Yet

The canonical `TESTING_GUIDE.md` remains selected Clio output and was not edited in Phase 01 or Phase 02. A future Clio regeneration should merge this delta into reviewer-facing documentation.

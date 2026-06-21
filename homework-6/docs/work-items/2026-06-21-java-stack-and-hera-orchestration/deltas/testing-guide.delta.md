# Testing Guide Delta

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Phase: Phase 01 Java stack readiness

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

## Not Merged Into Canonical Guide Yet

The canonical `TESTING_GUIDE.md` remains selected Clio output and was not edited in Phase 01. A future Clio regeneration should merge this delta into reviewer-facing documentation.

# Run Metadata

- Run ID: `20260621-145102-generate-tests-java-alternate`
- Agent: Themis (Test Generator)
- Mode: `generate`
- Stack: `java`
- Parent Hera run ID: `20260621-145059-orchestrate-runs-java-alternate`
- Target Hephaestus run ID: `20260621-145101-generate-code-java-alternate`
- Target Hephaestus inventory: `docs/agent-runs/20260621-145101-generate-code-java-alternate/agent-2-code/outputs/inventory.md`
- Source Athena run ID: `20260621-145100-write-spec-java-alternate`
- Selection record path: not selected; Java alternate is preserved only.
- Current canonical Python set: `python-canonical-20260621`
- Source spec mismatch status: no mismatch within the Java alternate; canonical root Python spec is intentionally not the Java target.
- Canonical output policy: candidate test overlay only under run folder; no root tests changed.
- Pre-existing dirty state: tracked porcelain was clean before run-local artifacts.

## Validation Summary

- Workspace: `agent-3-tests/workspace/project-under-test`
- Test command: `mvn -s <validation-settings.xml> -gs <validation-settings.xml> test jacoco:report jacoco:check`
- Result: passed with 21 tests, 0 failures, and JaCoCo "All coverage checks have been met."
- Repository helper: `python scripts/check_coverage_gate.py --stack java --project-dir ... --fail-under 80` could not override the machine-level Maven mirror and failed before test execution; recorded as support-tool limitation, not Java package failure.

# Validation Checklist

| Check | Result | Evidence |
|---|---|---|
| Named selected-code target | Pass | Hephaestus run `20260621-145101-generate-code-java-alternate` |
| Workspace isolation | Pass | Tests ran from `agent-3-tests/workspace/project-under-test` |
| Candidate output inventory | Pass | `agent-3-tests/outputs/inventory.md` |
| JUnit suite | Pass | 21 tests, 0 failures |
| Coverage gate | Pass | Direct Maven JaCoCo command: all coverage checks met |
| Baseline blocking evidence | Pass | Baseline Hephaestus coverage failed at 0.76 before Themis overlay |
| Dry-run validation behavior | Pass | `validateOnly` exercised by Themis tests |
| Privacy checks | Pass | Tests assert no `ACC-` and no raw payment description substring in validation-only output |
| Root containment | Pass | No root tests or canonical generated product files were modified by Themis |
| Support helper validation | Blocked by environment | Python helper invoked Maven without run-local settings override and hit unavailable machine Maven mirror |

## Known Limitations

- The repository `scripts/check_coverage_gate.py --stack java` path needs an operator-layer enhancement if Java alternates must pass through a custom Maven settings override in this environment.
- Java tests are preserved only and not selected into canonical root paths.

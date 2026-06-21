# Hera Validation Checklist

| Check | Result | Evidence |
|---|---|---|
| `selection-sets.json` remained unchanged and valid | Pass | Validated after run; canonical ID remains `python-canonical-20260621` |
| `.codex/config.toml` contains agent settings | Pass | `max_threads = 8`, `max_depth = 2` |
| Child run ledger complete | Pass | `agent-5-orchestrator/child-runs.md` |
| Java Athena run preserved | Pass | `20260621-145100-write-spec-java-alternate` |
| Java Hephaestus run preserved | Pass | `20260621-145101-generate-code-java-alternate` |
| Java Themis run preserved | Pass | `20260621-145102-generate-tests-java-alternate` |
| Java Clio run preserved | Pass | `20260621-145103-generate-docs-java-alternate` |
| Direct Java validation | Pass | Maven tests, pipeline command, and JaCoCo check evidence recorded |
| Python canonical outputs protected | Pass | No canonical root product/docs/selection files intentionally edited |
| Privacy scan | Pass | New run reports/generated files contain no raw account IDs or raw sample descriptions outside copied fixture files |
| Runtime/tool output cleanup | Pass | Maven `target/`, run-local `shared/`, and run-local `archive/` removed after evidence capture |
| Repository helper Java coverage path | Blocked | Helper invokes Maven without run-local settings override and hits unavailable machine mirror |
| Hera child-agent orchestration | Process defect | Later child stages ran in the main orchestration thread instead of first-level Athena/Hephaestus/Themis/Clio child agents |

## Commands / Checks Run

- `mvn -s ..\validation-settings.xml -gs ..\validation-settings.xml test`
- `mvn -s ..\validation-settings.xml -gs ..\validation-settings.xml exec:java '-Dexec.args=--input sample-transactions.json --shared-dir shared'`
- `mvn -s <validation-settings.xml> -gs <validation-settings.xml> test jacoco:report jacoco:check`
- `python scripts\check_coverage_gate.py --stack java --project-dir ... --fail-under 80` (blocked by Maven mirror configuration)
- Privacy scan with `Select-String` over new run files excluding copied sample fixture files.

## Operator Review Addendum

Post-run review confirms the Java alternate can be preserved as generated evidence, but the Hera run does not satisfy the clean orchestration test. See `orchestration-review.md` for disposition and follow-up recommendations.

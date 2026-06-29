# Validation Checklist

| Check | Result | Evidence |
|---|---|---|
| Maven compile and tests | Pass | `mvn -s ../validation-settings.xml -gs ../validation-settings.xml test` -> 9 tests, 0 failures |
| Java pipeline command | Pass | `mvn ... exec:java '-Dexec.args=--input sample-transactions.json --shared-dir shared'` -> total 8, settled 2, rejected 2, review-required 4, error 0 |
| Repeated-run archival | Pass | Second pipeline run succeeded; archive folder created under run-local output package |
| Context7 notes | Pass | `agent-2-code/research-notes.md` includes Jackson, JUnit, JaCoCo |
| Baseline coverage gate | Gap handed to Themis | Baseline direct JaCoCo check failed at 0.76 against 0.80 |
| Privacy scan by review | Pass | Console evidence reports safe counts and reason codes; no raw descriptions intentionally emitted |
| Canonical root protection | Pass | Java package remains under `docs/agent-runs/.../agent-2-code/outputs` |
| Task 3/4/5 scope control | Pass | No root commands, hooks, MCP config/server, screenshots, or canonical docs changed |

## Maven Environment Note

The machine had a Maven mirror pointing at an unavailable host. Validation used `agent-2-code/validation-settings.xml` with both `-s` and `-gs` to force Maven Central for this preserved run.

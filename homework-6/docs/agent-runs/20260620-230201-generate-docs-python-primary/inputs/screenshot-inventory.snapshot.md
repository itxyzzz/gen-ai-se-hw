# Screenshot Inventory Snapshot

Source folder preserved unchanged: `docs/screenshots/operator-sourced/`

| Source screenshot | Size bytes | Stable target | Reason | Privacy/safety status | Capture type |
|---|---:|---|---|---|---|
| `005-generate-spec-initial-planning.png` | 179145 | `unused` | Athena planning/process evidence, not one of the five final stable evidence slots. | Safe by filename review; not copied. | Operator-sourced |
| `010-generate-spec-attempt-1-mashup1.png` | 173150 | `unused` | Superseded Athena attempt evidence. | Safe by filename review; not copied. | Operator-sourced |
| `011-generate-spec-attempt-1-mashup2.png` | 160541 | `unused` | Superseded Athena attempt evidence. | Safe by filename review; not copied. | Operator-sourced |
| `015-generate-spec-attempt-1-fail-diagnostics.png` | 191048 | `unused` | Failure diagnostics, preserved but not needed in final reviewer screenshot set. | Safe by filename review; not copied. | Operator-sourced |
| `020-spec-generator-Athena-creation.png` | 152902 | `unused` | Athena skill creation evidence, outside the final five screenshot slots. | Safe by filename review; not copied. | Operator-sourced |
| `030-generate-spec-attempt-2-direct-gen.png` | 178927 | `unused` | Historical generation evidence. | Safe by filename review; not copied. | Operator-sourced |
| `040-code-generator-Hephaestus-creation.png` | 162739 | `unused` | Hephaestus skill creation evidence, not selected for stable screenshot set. | Safe by filename review; not copied. | Operator-sourced |
| `050-generate-code-success-run.png` | 178104 | `unused` | Code-generation evidence, superseded by final docs and selection record. | Safe by filename review; not copied. | Operator-sourced |
| `060-spec-compare-and-select-mode.png` | 123038 | `unused` | Selection-process evidence, not one of the five final slots. | Safe by filename review; not copied. | Operator-sourced |
| `070-test-generator-Themis-creation.png` | 166613 | `unused` | Themis skill creation evidence, not copied to stable screenshot set. | Safe by filename review; not copied. | Operator-sourced |
| `075-test-run-cli.png` | 52415 | `unused` | Safe but stale 41-test run; fresh Clio evidence now shows 50 tests. | Visual review: safe, no raw account IDs or descriptions. | Operator-sourced |
| `080-run-pipeline.png` | 103486 | `docs/screenshots/pipeline-run.png`, `docs/screenshots/skill-run-pipeline.png` | Shows `/run-pipeline` producing safe pipeline counts and rejected reason codes. Used for both pipeline and skill evidence because no separate fresher terminal screenshot exists. | Visual review: safe, no raw account IDs or descriptions. | Operator-sourced copied |
| `090-validate-transactions.png` | 106661 | `unused` | Useful validation-only evidence, but not required as one of the stable final screenshot names. | Visual review: safe, reason-code-only output. | Operator-sourced |
| `095-coverage-fail-under-99-fail.png` | 109186 | `docs/screenshots/test-coverage.png` | Shows coverage table and deliberate fail-under-99 blocking path while total coverage remains above 80%. Fresh Clio text evidence supersedes stale test count. | Visual review: safe; no transaction payloads. | Operator-sourced copied |
| `100-pre-push-git-hook-firing.png` | 110235 | `docs/screenshots/hook-trigger.png` | Shows the Git pre-push hook invoking the 80% coverage gate and passing. Fresh Clio text evidence supersedes stale test count. | Visual review: safe; no transaction payloads. | Operator-sourced copied |
| `110-custom-mcp-server.png` | 89694 | `docs/screenshots/mcp-interaction.png` | Shows custom `pipeline-status` MCP interaction with safe summary and transaction status fields. | Visual review: safe; no account IDs or raw descriptions. | Operator-sourced copied |

## Missing Or Stale Screenshot Notes

The available operator screenshots were captured before the MCP test files brought the canonical root suite to 50 tests. Stable screenshots remain useful as visual evidence, and fresh text evidence in this Clio run records the current 50-test, 94.79% coverage state.

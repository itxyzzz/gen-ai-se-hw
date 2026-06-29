# Screenshot Inventory Snapshot

Operator-sourced screenshots remain preserved under `docs/screenshots/operator-sourced/`.

| Source screenshot | Size | Stable target | Category | Freshness | Passing/blocking | Task coverage | Privacy review | Handling |
|---|---:|---|---|---|---|---|---|---|
| `005-generate-spec-initial-planning.png` | 179145 | unused | spec/support evidence | historical support | support-only | PR spec-produced support | safe at summary level | Preserved only; selected run records cover spec-produced evidence. |
| `010-generate-spec-attempt-1-mashup1.png` | 173150 | unused | failed spec attempt | stale | support-only | support-only | safe at summary level | Preserved only. |
| `011-generate-spec-attempt-1-mashup2.png` | 160541 | unused | failed spec attempt | stale | support-only | support-only | safe at summary level | Preserved only. |
| `015-generate-spec-attempt-1-fail-diagnostics.png` | 191048 | unused | failed spec diagnostics | stale | blocking/support | support-only | safe at summary level | Preserved only. |
| `020-spec-generator-Athena-creation.png` | 152902 | unused | Athena (Spec Writer) creation | current enough | support-only | PR spec-produced support | safe at summary level | Preserved only; final selection record is the primary spec evidence. |
| `030-generate-spec-attempt-2-direct-gen.png` | 178927 | unused | spec generation | historical support | support-only | support-only | safe at summary level | Preserved only. |
| `040-code-generator-Hephaestus-creation.png` | 162739 | unused | Hephaestus (Code Generator) creation | historical support | support-only | support-only | safe at summary level | Preserved only. |
| `050-generate-code-success-run.png` | 178104 | unused | code generation | historical support | passing/support | support-only | safe at summary level | Preserved only. |
| `060-spec-compare-and-select-mode.png` | 123038 | unused | spec selection | current enough | support-only | PR spec-produced support | safe at summary level | Preserved only; final-selection record is clearer. |
| `070-test-generator-Themis-creation.png` | 166613 | unused | Themis (Test Generator) creation | historical support | support-only | support-only | safe at summary level | Preserved only. |
| `075-test-run-cli.png` | 52415 | unused | pytest passing | stale count | passing | Task 5 support | safe | Preserved only because fresh coverage screenshot was generated. |
| `080-run-pipeline.png` | 103486 | `docs/screenshots/skill-run-pipeline.png` | `/run-pipeline` skill | current enough | passing | Task 3, Task 5, PR evidence | safe; reason codes only | Copied to stable skill target. |
| `090-validate-transactions.png` | 106661 | unused | validation-only helper | current enough | passing | support-only | safe; reason codes only | Preserved only; documented in text evidence. |
| `095-coverage-fail-under-99-fail.png` | 109186 | unused | deliberate coverage failure | current enough | blocking | hook/blocking support | safe | Not used for passing `test-coverage.png`; blocking category belongs to hook evidence. |
| `100-pre-push-git-hook-firing.png` | 110235 | `docs/screenshots/hook-trigger.png` | hook trigger | current enough | blocking | Task 3, Task 5, PR evidence | safe | Copied to stable hook target. |
| `110-custom-mcp-server.png` | 89694 | unused | custom `pipeline-status` MCP | partial | passing | Task 4 support | safe | Preserved only because fresh combined Context7 plus custom MCP screenshot was generated. |

Fresh stable screenshots generated during this run:

| Fresh screenshot | Stable target | Category | Reason |
|---|---|---|---|
| `agent-4-docs/outputs/docs/screenshots/pipeline-run.png` | `docs/screenshots/pipeline-run.png` | direct pipeline run | Avoids duplicating the `/run-pipeline` skill screenshot. |
| `agent-4-docs/outputs/docs/screenshots/test-coverage.png` | `docs/screenshots/test-coverage.png` | passing 80% coverage gate | Shows current passing coverage evidence instead of the deliberate 99% failure. |
| `agent-4-docs/outputs/docs/screenshots/mcp-interaction.png` | `docs/screenshots/mcp-interaction.png` | combined MCP evidence | Covers both Context7 query records and custom `pipeline-status` behavior in one image. |

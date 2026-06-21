# Screenshot Inventory Snapshot

Source screenshots are preserved in `docs/screenshots/operator-sourced/`. This Clio run did not edit, rename, delete, compress, or overwrite that folder.

## Operator-Sourced Inventory

| Source screenshot | Size bytes | Evidence category | Candidate target | Freshness | Task coverage | Privacy review | Use decision |
|---|---:|---|---|---|---|---|---|
| `005-generate-spec-initial-planning.png` | 179145 | Spec production support | unused | current enough as historical operator evidence | PR spec-produced support | safe by filename inventory only | Not copied; candidate spec path and SHA are stronger traceability. |
| `010-generate-spec-attempt-1-mashup1.png` | 173150 | Historical spec attempt | unused | stale for fresh candidate | support-only | safe by filename inventory only | Not copied; superseded attempt. |
| `011-generate-spec-attempt-1-mashup2.png` | 160541 | Historical spec attempt | unused | stale for fresh candidate | support-only | safe by filename inventory only | Not copied; superseded attempt. |
| `015-generate-spec-attempt-1-fail-diagnostics.png` | 191048 | Historical diagnostics | unused | stale for fresh candidate | support-only | safe by filename inventory only | Not copied; not needed for reviewer docs. |
| `020-spec-generator-Athena-creation.png` | 152902 | Athena creation | unused | current enough as historical operator evidence | PR spec-produced support | safe by filename inventory only | Not copied; candidate traceability records are used instead. |
| `030-generate-spec-attempt-2-direct-gen.png` | 178927 | Spec generation | unused | stale for this fresh candidate | support-only | safe by filename inventory only | Not copied. |
| `040-code-generator-Hephaestus-creation.png` | 162739 | Hephaestus creation | unused | support evidence | support-only | safe by filename inventory only | Not copied. |
| `050-generate-code-success-run.png` | 178104 | Code generation success | unused | support evidence | support-only | safe by filename inventory only | Not copied. |
| `060-spec-compare-and-select-mode.png` | 123038 | Historical selection | unused | stale for no-select candidate | support-only | safe by filename inventory only | Not copied; this run is not a selection. |
| `070-test-generator-Themis-creation.png` | 166613 | Themis creation | unused | support evidence | support-only | safe by filename inventory only | Not copied. |
| `075-test-run-cli.png` | 52415 | Test run | unused | stale relative to fresh Clio check | Task 5 support | safe by filename inventory only | Replaced by generated candidate `test-coverage.png`. |
| `080-run-pipeline.png` | 103486 | `/run-pipeline` skill | unused | historical canonical evidence | Task 3/5/PR support | safe by filename inventory only | Replaced by generated candidate `skill-run-pipeline.png`. |
| `090-validate-transactions.png` | 106661 | Validation-only command | unused | historical canonical evidence | support-only | safe by filename inventory only | Not copied; validation evidence is text-only. |
| `095-coverage-fail-under-99-fail.png` | 109186 | Coverage blocking demo | unused | historical canonical evidence | hook support | safe by filename inventory only | Replaced by generated candidate `hook-trigger.png`. |
| `100-pre-push-git-hook-firing.png` | 110235 | Hook firing | unused | historical canonical evidence | Task 3/5/PR hook support | safe by filename inventory only | Replaced by generated candidate hook evidence image because direct shell hook was environment-blocked. |
| `110-custom-mcp-server.png` | 89694 | Custom MCP | unused | historical canonical evidence | Task 4 support | safe by filename inventory only | Replaced by generated combined candidate `mcp-interaction.png`. |

## Candidate Stable Screenshots

| Candidate screenshot | Evidence category | Source | Freshness | Passing/blocking classification | Task coverage | Privacy review |
|---|---|---|---|---|---|---|
| `agent-4-docs/outputs/docs/screenshots/pipeline-run.png` | Direct pipeline execution | Fresh Clio validation workspace text evidence | fresh | passing | Task 5 and PR pipeline evidence | safe counts/statuses only |
| `agent-4-docs/outputs/docs/screenshots/test-coverage.png` | Passing coverage gate | Fresh Clio validation workspace coverage output | fresh | passing 80% gate, 97.44% total | Task 5 and PR tests/coverage evidence | safe module/test output only |
| `agent-4-docs/outputs/docs/screenshots/skill-run-pipeline.png` | `/run-pipeline` behavior | Fresh candidate support summary based on documented fast path | fresh | passing | Task 3, Task 5, PR skill evidence | safe counts/statuses/reason codes only |
| `agent-4-docs/outputs/docs/screenshots/hook-trigger.png` | Coverage hook blocking behavior | Fresh candidate 99% threshold demonstration plus Themis hook limitation | fresh | expected blocking failure | Task 3, Task 5, PR hook evidence | safe coverage output only |
| `agent-4-docs/outputs/docs/screenshots/mcp-interaction.png` | Context7 plus custom `pipeline-status` MCP evidence | Hephaestus research notes and file-path import of root MCP server against candidate results | fresh paired evidence | passing with custom MCP helper | Task 4, Task 5, PR MCP evidence | safe counts/statuses/reason codes only |

No single source screenshot is reused for distinct stable targets.

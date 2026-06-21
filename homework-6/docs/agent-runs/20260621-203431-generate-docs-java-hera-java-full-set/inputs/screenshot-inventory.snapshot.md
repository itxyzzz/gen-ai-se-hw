# Screenshot Inventory Snapshot

Source folder: `docs/screenshots/operator-sourced/`

This folder was inventoried and left untouched. Source screenshots are preserved as operator evidence. They were not copied to canonical stable root screenshot paths.

## Operator-Sourced Screenshots

| Source screenshot | Size bytes | Observed timestamp | Category | Target in this run | Freshness | Passing/blocking | Task coverage | Safety status | Reason |
|---|---:|---|---|---|---|---|---|---|---|
| `005-generate-spec-initial-planning.png` | 179145 | 2026-06-19 16:56:54 +02:00 | Spec planning | unused | stale for Java candidate | support only | PR spec-produced support | safe by filename only | Python/general workflow, not Java candidate evidence. |
| `010-generate-spec-attempt-1-mashup1.png` | 173150 | 2026-06-18 22:48:30 +02:00 | Spec attempt | unused | stale | support only | support-only | safe by filename only | Superseded attempt evidence. |
| `011-generate-spec-attempt-1-mashup2.png` | 160541 | 2026-06-19 23:27:46 +02:00 | Spec attempt | unused | stale | support only | support-only | safe by filename only | Superseded attempt evidence. |
| `015-generate-spec-attempt-1-fail-diagnostics.png` | 191048 | 2026-06-19 16:35:13 +02:00 | Failed spec diagnostics | unused | stale | blocking historical | support-only | safe by filename only | Historical failure evidence, not Java candidate proof. |
| `020-spec-generator-Athena-creation.png` | 152902 | 2026-06-19 23:20:38 +02:00 | Athena creation | unused | stale | support only | PR spec-produced support | safe by filename only | Does not show Java candidate spec. |
| `030-generate-spec-attempt-2-direct-gen.png` | 178927 | 2026-06-18 22:49:44 +02:00 | Spec generation | unused | stale | support only | support-only | safe by filename only | Not the Java Athena candidate. |
| `040-code-generator-Hephaestus-creation.png` | 162739 | 2026-06-19 16:46:10 +02:00 | Hephaestus creation | unused | stale | support only | support-only | safe by filename only | Not Java-specific. |
| `050-generate-code-success-run.png` | 178104 | 2026-06-18 22:50:52 +02:00 | Code generation | unused | stale | support only | support-only | safe by filename only | Not the Java Hephaestus run. |
| `060-spec-compare-and-select-mode.png` | 123038 | 2026-06-19 17:48:35 +02:00 | Selection workflow | unused | stale | support only | support-only | safe by filename only | Selection is not authorized for Java. |
| `070-test-generator-Themis-creation.png` | 166613 | 2026-06-19 22:41:23 +02:00 | Themis creation | unused | stale | support only | support-only | safe by filename only | General Themis evidence. |
| `075-test-run-cli.png` | 52415 | 2026-06-20 17:09:45 +02:00 | Test run | unused | stale for Java | passing historical | Task 5 support | safe by filename only | Python-era test evidence; Java run uses fresh terminal-style image. |
| `080-run-pipeline.png` | 103486 | 2026-06-20 16:46:46 +02:00 | `/run-pipeline` | unused | stale for Java | passing historical | Task 3/5 support | safe by filename only | Python-era operation evidence; Java run uses fresh terminal-style image. |
| `090-validate-transactions.png` | 106661 | 2026-06-20 16:52:31 +02:00 | Validation command | unused | stale for Java | passing historical | support-only | safe by filename only | Python-era validation evidence. |
| `095-coverage-fail-under-99-fail.png` | 109186 | 2026-06-20 17:11:24 +02:00 | Coverage blocking | unused | stale for Java | blocking historical | hook support | safe by filename only | Java run uses fresh fail-under 99 terminal-style image. |
| `100-pre-push-git-hook-firing.png` | 110235 | 2026-06-20 17:24:47 +02:00 | Hook trigger | unused | stale for Java | blocking historical | Task 3/5 support | safe by filename only | Python canonical hook evidence; Java run uses fresh terminal-style image from Themis retry. |
| `110-custom-mcp-server.png` | 89694 | 2026-06-20 21:21:10 +02:00 | Custom MCP | unused | stale for Java | passing historical | Task 4 support | safe by filename only | Does not pair Java Context7 plus custom reader evidence. |

## Run-Local Stable Screenshots Produced

| Output screenshot | Source | Category | Freshness | Passing/blocking | Task coverage | Safety status |
|---|---|---|---|---|---|---|
| `agent-4-docs/outputs/docs/screenshots/pipeline-run.png` | Fresh terminal-style evidence generated from Themis retry counts | Java pipeline run | fresh | passing | Task 5, PR evidence | safe counts only |
| `agent-4-docs/outputs/docs/screenshots/test-coverage.png` | Fresh terminal-style evidence generated from Themis retry coverage files | Java Maven/JUnit/JaCoCo coverage | fresh | passing 80% | Task 5, PR evidence | safe counts only |
| `agent-4-docs/outputs/docs/screenshots/skill-run-pipeline.png` | Fresh terminal-style evidence generated from operation-command behavior plus Themis retry Java command | Java `/run-pipeline` support | fresh | passing | Task 3/5, PR evidence | safe counts only |
| `agent-4-docs/outputs/docs/screenshots/hook-trigger.png` | Fresh terminal-style evidence generated from Themis retry `--fail-under 99` blocker | Java coverage hook blocking | fresh | blocking demonstration | Task 3/5, PR evidence | safe counts only |
| `agent-4-docs/outputs/docs/screenshots/mcp-interaction.png` | Fresh terminal-style evidence generated from Hephaestus Context7 notes plus custom MCP reader behavior | Context7 and pipeline-status MCP | fresh | passing/covered by paired evidence | Task 4/5, PR evidence | safe counts only |

No stable root screenshots were overwritten.

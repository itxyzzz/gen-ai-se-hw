# Java Candidate Documentation Output Inventory

Run ID: `20260621-203431-generate-docs-java-hera-java-full-set`

Mode: `generate`

Stack: `java`

Package-set ID: `java-candidate-20260621-180512`

Selection status: not authorized; preserved candidate only.

## Source Traceability

| Source | Value |
|---|---|
| Parent Hera run ID | `20260621-180512-orchestrate-runs-java-full-set` |
| Athena run ID | `20260621-180826-write-spec-java-hera-java-full-set` |
| Athena spec SHA-256 | `2FC5B597915CFD59D5786271DB0CDE96B06417DEEC1433D056B76D7023234CFC` |
| Hephaestus run ID | `20260621-183025-generate-code-java-hera-java-full-set` |
| Hephaestus inventory | `docs/agent-runs/20260621-183025-generate-code-java-hera-java-full-set/agent-2-code/outputs/inventory.md` |
| Hephaestus inventory SHA-256 | `3C5B7DB95A30250DEE60A26D17BAD577FA8BFF98A293FA917A1DEA8ECCBCDEB1` |
| Hephaestus package fingerprint | `248ba67b199104cbe06b4b0869cf3143de85ff1dd88da7bf4a13bc85a58ed883` |
| Themis run ID | `20260621-201051-generate-tests-java-hera-java-full-set-retry` |
| Themis inventory | `docs/agent-runs/20260621-201051-generate-tests-java-hera-java-full-set-retry/agent-3-tests/outputs/inventory.md` |
| Themis package fingerprint | `d75aee1b9e76733d78860348fe26608fef17667a88c25e3c7b416e9150b54922` |
| Blocked prior Themis run | `20260621-191017-generate-tests-java-hera-java-full-set`, non-selectable |
| Current canonical package set | `python-canonical-20260621` |
| Current canonical spec SHA-256 | `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B` |

## Selectable Candidate Outputs

| Kind | Candidate path under `outputs/` | Intended canonical target if later selected | SHA-256 | Source screenshot | Action |
|---|---|---|---|---|---|
| README | `README.md` | `README.md` | `BD7AB42A0B2521C5BFFF9A2AC3BA62EDA4D72055F17B729B6D601CFC37E6B244` | n/a | Replaces canonical documentation only after explicit Java documentation selection. |
| Runbook | `HOWTORUN.md` | `HOWTORUN.md` | `4A5D14FD6F895F3CC7CDD8A6628134ADCC1006C60889AF00C01D7E428B1EBE2D` | n/a | Replaces canonical documentation only after explicit Java documentation selection. |
| Architecture doc | `ARCHITECTURE.md` | `ARCHITECTURE.md` | `D3E585C03DCF2D0E7BFCF30AEE35765EE5E1AF95CDC6681F371003A866F9D98B` | n/a | Replaces canonical documentation only after explicit Java documentation selection. |
| Testing doc | `TESTING_GUIDE.md` | `TESTING_GUIDE.md` | `C326050D33B2596C83D02F294DF011796E0B6DC41C757626EC7E418BDC1781CE` | n/a | Replaces canonical documentation only after explicit Java documentation selection. |
| API reference | `API_REFERENCE.md` | `API_REFERENCE.md` | `AD0B2AB42B2CA1930BB493D81D0E7E66A4EEFBEC4D1A1738B6AEFD08263913F6` | n/a | Replaces canonical documentation only after explicit Java documentation selection. |
| PR draft | `docs/pr-description-draft.md` | `docs/pr-description-draft.md` | `C06C70B2B49F8B258AA4FADD57F30452490DBD6A1E1B8A40B1A6DE279539B6C2` | n/a | Replaces canonical PR draft only after explicit Java documentation selection. |
| Screenshot | `docs/screenshots/pipeline-run.png` | `docs/screenshots/pipeline-run.png` | `1687238761EBF7A1B6527610F5257A20ADE679F5A9E15323AD4875F99E7D5388` | Fresh terminal-style Java evidence from Themis retry `support-run-pipeline-success.txt` | Replaces stable screenshot only after explicit Java documentation selection. |
| Screenshot | `docs/screenshots/test-coverage.png` | `docs/screenshots/test-coverage.png` | `FAAD5D843D95A575EA757E38DE2DBE38BBC3AB969392A8EC0AE93B93699F8D06` | Fresh terminal-style Java evidence from Themis retry `coverage-summary.txt` | Replaces stable screenshot only after explicit Java documentation selection. |
| Screenshot | `docs/screenshots/skill-run-pipeline.png` | `docs/screenshots/skill-run-pipeline.png` | `D88621560AEBFB47466002B18CA8B14A30311AAE4FC8B096253F61249F45F73A` | Fresh terminal-style Java operation evidence from Themis retry command output | Replaces stable screenshot only after explicit Java documentation selection. |
| Screenshot | `docs/screenshots/hook-trigger.png` | `docs/screenshots/hook-trigger.png` | `D1C3932A88700493773467A1BEC23B5A5EC401B37885968206E10768803D1860` | Fresh terminal-style Java fail-under 99 evidence from Themis retry `hook-fail.txt` | Replaces stable screenshot only after explicit Java documentation selection. |
| Screenshot | `docs/screenshots/mcp-interaction.png` | `docs/screenshots/mcp-interaction.png` | `A5797E8600500C545D646F057CACE603F455CBBD98EDE7964B123FD814257B1B` | Fresh terminal-style Context7 plus custom MCP evidence from Hephaestus notes and root MCP reader review | Replaces stable screenshot only after explicit Java documentation selection. |

## Explicitly Excluded From Selection

These paths are run evidence, review notes, source snapshots, runtime/tool output, or cache-like material and must not be copied wholesale during selection:

- `agent-4-docs/evidence/`
- `agent-4-docs/review/`
- `agent-4-docs/validation-checklist.md`
- `agent-4-docs/handoff.md`
- `inputs/`
- `run-metadata.md`
- `shared/`
- `archive/`
- `target/`
- `.coverage*`
- `.pytest_cache/`
- `.test-tmp/`
- `tmp/`
- `__pycache__/`
- Maven local repositories
- `.class` files
- `.jar` files
- JaCoCo execution data
- temporary Maven settings files

## Selection Notes

This inventory is a future copy map only. It does not authorize canonical copy.

Python package set `python-canonical-20260621` remains canonical until a later explicit Hera `select-set` operation updates `docs/agent-runs/final-selection.md` and `docs/agent-runs/selection-sets.json`.

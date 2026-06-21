# Clio Run Metadata

Run ID: `20260621-203431-generate-docs-java-hera-java-full-set`

Mode: `generate`

Stack: `java`

Parent Hera run ID: `20260621-180512-orchestrate-runs-java-full-set`

Package-set ID: `java-candidate-20260621-180512`

Status: preserved candidate documentation only. Selection was not authorized. Python package set `python-canonical-20260621` remains canonical.

Start time: `2026-06-21T20:34:31+02:00`

Orchestration tool: Codex Desktop child-agent dispatch from Hera (Orchestrator).

## Selected Source Traceability

| Source | Value |
|---|---|
| Final-selection context | `docs/agent-runs/final-selection.md` |
| Registry context | `docs/agent-runs/selection-sets.json` |
| Athena (Spec Writer) run | `20260621-180826-write-spec-java-hera-java-full-set` |
| Athena spec path | `docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/agent-1-spec/outputs/specification.md` |
| Athena spec SHA-256 | `2FC5B597915CFD59D5786271DB0CDE96B06417DEEC1433D056B76D7023234CFC` |
| Hephaestus (Code Generator) run | `20260621-183025-generate-code-java-hera-java-full-set` |
| Hephaestus inventory | `docs/agent-runs/20260621-183025-generate-code-java-hera-java-full-set/agent-2-code/outputs/inventory.md` |
| Hephaestus inventory SHA-256 | `3C5B7DB95A30250DEE60A26D17BAD577FA8BFF98A293FA917A1DEA8ECCBCDEB1` |
| Hephaestus package fingerprint | `248ba67b199104cbe06b4b0869cf3143de85ff1dd88da7bf4a13bc85a58ed883` |
| Themis (Test Generator) run | `20260621-201051-generate-tests-java-hera-java-full-set-retry` |
| Themis inventory | `docs/agent-runs/20260621-201051-generate-tests-java-hera-java-full-set-retry/agent-3-tests/outputs/inventory.md` |
| Themis package fingerprint | `d75aee1b9e76733d78860348fe26608fef17667a88c25e3c7b416e9150b54922` |
| Prior Themis attempt | `20260621-191017-generate-tests-java-hera-java-full-set`, blocked and non-selectable |
| Current canonical Python spec SHA-256 | `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B` |

## Author And Style Sources

Author display name: Igor Tanatarov.

Author source: prior homework README files under `homework-1` through `homework-4`, plus the selected Python Homework 6 README.

Style sources used: current Homework 6 canonical README/HOWTORUN/ARCHITECTURE/TESTING_GUIDE/API_REFERENCE, prior homework README files, and repository homework standards.

## Screenshot Source

Source folder: `docs/screenshots/operator-sourced/`

The operator-sourced screenshots are preserved and inventoried. They are mostly Python canonical or general workflow evidence, so this Java candidate run uses freshly generated run-local terminal-style PNGs for Java-specific pipeline, coverage, hook, operation-command, and MCP evidence.

## Intended Output Targets If Later Selected

Candidate outputs are under `agent-4-docs/outputs/`. Their intended canonical targets are the root documentation paths listed in `agent-4-docs/outputs/inventory.md`, but no copy is authorized in this run.

Protected root targets not modified:

- `README.md`
- `HOWTORUN.md`
- `ARCHITECTURE.md`
- `TESTING_GUIDE.md`
- `API_REFERENCE.md`
- `docs/pr-description-draft.md`
- `docs/screenshots/*.png`
- root product files
- root tests
- `mcp/server.py`
- `mcp.json`
- `.codex/config.toml`
- `docs/agent-runs/final-selection.md`
- `docs/agent-runs/selection-sets.json`
- root `shared/`

## Pre-Existing Dirty Git State

`git status --short` before this Clio run showed untracked preserved Java/Hera run folders:

```text
?? docs/agent-runs/20260621-180512-orchestrate-runs-java-full-set/
?? docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/
?? docs/agent-runs/20260621-183025-generate-code-java-hera-java-full-set/
?? docs/agent-runs/20260621-191017-generate-tests-java-hera-java-full-set/
?? docs/agent-runs/20260621-201051-generate-tests-java-hera-java-full-set-retry/
warning: could not open directory '.pytest_cache/': Permission denied
```

No root canonical copy was performed by this run.

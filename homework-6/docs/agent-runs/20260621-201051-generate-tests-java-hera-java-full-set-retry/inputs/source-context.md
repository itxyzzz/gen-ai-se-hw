# Source Context

This run used the exact Java candidate inputs supplied by Hera (Orchestrator). It did not target root code, root tests, or latest discovered files.

## Required Skill And Workflow Files Read

- `.agents/skills/generate-tests/SKILL.md`
- `agent-control/generate-tests/workflow.md`
- `agent-control/generate-tests/quality-bar.md`
- `agent-control/generate-tests/run-registry.md`
- `agent-control/operate-pipeline/commands-and-hooks.md`

## Selection And Assignment Context Read

- `docs/agent-runs/final-selection.md`
- `agents.md`
- `TASKS.md`
- `sample-transactions.json`
- Current canonical `specification.md`

## Exact Source Artifacts Read

- Java source spec: `docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/agent-1-spec/outputs/specification.md`
- Java Hephaestus inventory: `docs/agent-runs/20260621-183025-generate-code-java-hera-java-full-set/agent-2-code/outputs/inventory.md`
- Java Hephaestus package root: `docs/agent-runs/20260621-183025-generate-code-java-hera-java-full-set/agent-2-code/outputs/`
- Java `pom.xml`, runtime source, baseline tests, `sample-transactions.json`, and `research-notes.md` copied from the preserved Hephaestus package into `agent-3-tests/workspace/selected-code/`.

## Traceability Values

- Source Athena run ID: `20260621-180826-write-spec-java-hera-java-full-set`
- Source spec SHA-256: `2FC5B597915CFD59D5786271DB0CDE96B06417DEEC1433D056B76D7023234CFC`
- Target Hephaestus run ID: `20260621-183025-generate-code-java-hera-java-full-set`
- Target Hephaestus inventory SHA-256: `3C5B7DB95A30250DEE60A26D17BAD577FA8BFF98A293FA917A1DEA8ECCBCDEB1`
- Target Hephaestus package fingerprint: `248ba67b199104cbe06b4b0869cf3143de85ff1dd88da7bf4a13bc85a58ed883`
- Current canonical root spec SHA-256: `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B`

## Workspace Construction

1. Copied selected Java package files into `agent-3-tests/workspace/selected-code/`.
2. Copied baseline Java tests into `agent-3-tests/outputs/src/test/java/...`.
3. Added Themis-owned `ThemisQualityTest.java`.
4. Added a Themis-owned `pom.xml` overlay so `scripts/check_coverage_gate.py --fail-under N` controls JaCoCo through `-Dcoverage.minimum`.
5. Built `agent-3-tests/workspace/project-under-test/` from `selected-code/` plus `outputs/`.
6. Copied `scripts/check_coverage_gate.py` into `project-under-test/scripts/` for run-local validation support.

## Protected Paths Not Written

No canonical root product files, root tests, root docs, screenshots, `mcp/server.py`, `mcp.json`, `.codex/config.toml`, `final-selection.md`, `selection-sets.json`, or root `shared/` output were modified.

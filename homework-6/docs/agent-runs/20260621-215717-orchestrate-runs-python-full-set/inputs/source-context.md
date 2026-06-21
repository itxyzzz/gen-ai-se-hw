# Source Context

Hera (Orchestrator) read or attempted to read these source artifacts before child dispatch:

## Repository And Assignment Context

- `agents.md`
- `README.md`
- `TASKS.md`
- `HOMEWORK_STANDARDS.md` was attempted under the homework root and was missing.

## Selection And Current Package Context

- `docs/agent-runs/selection-sets.json`
- `docs/agent-runs/final-selection.md`
- Current canonical set ID: `python-canonical-20260621`
- Current selected Athena (Spec Writer) run: `20260619-170102-write-spec-python-fresh`
- Current selected Hephaestus (Code Generator) run: `20260619-175211-generate-code-python-fresh-spec`
- Current selected Themis (Test Generator) run: `20260620-144025-generate-tests-python-fresh-spec`
- Current selected Clio (Documentation Generator) run: `20260621-011348-generate-docs-python-review-repair`
- Selected code inventory: `docs/agent-runs/20260619-175211-generate-code-python-fresh-spec/agent-2-code/outputs/inventory.md`
- Selected test inventory: `docs/agent-runs/20260620-144025-generate-tests-python-fresh-spec/agent-3-tests/outputs/inventory.md`
- Selected documentation inventory: `docs/agent-runs/20260621-011348-generate-docs-python-review-repair/agent-4-docs/outputs/inventory.md`
- Current canonical spec SHA-256: `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B`

## Hera Control Context

- `.agents/skills/orchestrate-runs/SKILL.md`
- `agent-control/orchestrate-runs/workflow.md`
- `agent-control/orchestrate-runs/quality-bar.md`
- `agent-control/orchestrate-runs/run-registry.md`
- `docs/agent-runs/README.md`

## Child-Agent Control Context

- `.agents/skills/write-spec/SKILL.md`
- `agent-control/write-spec/workflow.md`
- `agent-control/write-spec/stack-profiles.md`
- `agent-control/write-spec/quality-bar.md`
- `agent-control/write-spec/run-registry.md`
- `agent-control/write-spec/transaction-system-brief.md`
- `.agents/skills/generate-code/SKILL.md`
- `agent-control/generate-code/workflow.md`
- `agent-control/generate-code/quality-bar.md`
- `agent-control/generate-code/run-registry.md`
- `.agents/skills/generate-tests/SKILL.md`
- `agent-control/generate-tests/workflow.md`
- `agent-control/generate-tests/quality-bar.md`
- `agent-control/generate-tests/run-registry.md`
- `.agents/skills/generate-docs/SKILL.md`
- `agent-control/generate-docs/workflow.md`
- `agent-control/generate-docs/quality-bar.md`
- `agent-control/generate-docs/run-registry.md`

## Operation And MCP Context

- `agent-control/operate-pipeline/commands-and-hooks.md`
- `.codex/config.toml`
- `mcp.json`

## Tool And Git Context

- First-level child dispatch discovered through `multi_agent_v1.spawn_agent`.
- `git status --short` returned no file entries but warned that `.pytest_cache/` could not be opened due to permission denial.

No raw sample transaction payloads were copied into this Hera source context.

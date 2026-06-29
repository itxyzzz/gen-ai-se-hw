# Source Context

Hera read these artifacts before dispatching child agents:

- Root repository instructions: `../AGENTS.md`
- Root standards: `../HOMEWORK_STANDARDS.md`
- Root README: `../README.md`
- Homework assignment: `TASKS.md`
- Homework agent guide: `agents.md`
- Package-set registry: `docs/agent-runs/selection-sets.json`
- Human selection record: `docs/agent-runs/final-selection.md`
- Codex project config: `.codex/config.toml`
- Pipeline operation guidance: `agent-control/operate-pipeline/commands-and-hooks.md`
- Hera entrypoint: `.agents/skills/orchestrate-runs/SKILL.md`
- Hera workflow: `agent-control/orchestrate-runs/workflow.md`
- Hera quality bar: `agent-control/orchestrate-runs/quality-bar.md`
- Hera run registry: `agent-control/orchestrate-runs/run-registry.md`
- Athena control package: `agent-control/write-spec/workflow.md`, `quality-bar.md`, `run-registry.md`, `stack-profiles.md`, and `transaction-system-brief.md`
- Hephaestus control package: `agent-control/generate-code/workflow.md`, `quality-bar.md`, and `run-registry.md`
- Themis control package: `agent-control/generate-tests/workflow.md`, `quality-bar.md`, and `run-registry.md`
- Clio control package: `agent-control/generate-docs/workflow.md`, `quality-bar.md`, and `run-registry.md`

Child prompts must name source artifacts, inventories, selection records, fingerprints, and output paths explicitly. Silent "latest" targeting is not allowed.


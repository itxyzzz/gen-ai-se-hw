# Source Context

## Control Package

- `.agents/skills/generate-docs/SKILL.md`
- `agent-control/generate-docs/workflow.md`
- `agent-control/generate-docs/quality-bar.md`
- `agent-control/generate-docs/run-registry.md`

## Selection And Product Sources

- `docs/agent-runs/final-selection.md`
- `docs/agent-runs/20260619-175211-generate-code-python-fresh-spec/agent-2-code/outputs/inventory.md`
- `docs/agent-runs/20260620-144025-generate-tests-python-fresh-spec/agent-3-tests/outputs/inventory.md`
- `specification.md`
- `sample-transactions.json`
- `research-notes.md`
- `agents.md`
- `TASKS.md`

## Runtime, Test, Command, Hook, And MCP Sources

- `integrator.py`
- `agents/common.py`
- `agents/transaction_validator.py`
- `agents/fraud_detector.py`
- `agents/settlement_processor.py`
- `tests/`
- `scripts/check_coverage_gate.py`
- `.githooks/pre-push`
- `.claude/settings.json`
- `.agents/skills/run-pipeline/`
- `.agents/skills/validate-transactions/`
- `.claude/commands/run-pipeline.md`
- `.claude/commands/validate-transactions.md`
- `agent-control/operate-pipeline/commands-and-hooks.md`
- `mcp.json`
- `.codex/config.toml`
- `mcp/server.py`

## Repository Standards And Style Sources

- `../AGENTS.md`
- `../HOMEWORK_STANDARDS.md`
- `../README.md`
- `../homework-1/README.md`
- `../homework-2/README.md`
- `../homework-3/README.md`
- `../homework-4/README.md`
- Supporting prior homework docs from Homeworks 1-4 when present.

## Screenshot Sources

- Full source folder: `docs/screenshots/operator-sourced/`
- Stable targets selected from source screenshots are documented in `inputs/screenshot-inventory.snapshot.md`.

## Evidence Commands

- `python integrator.py`
- `python -m pytest -p no:cacheprovider`
- `python scripts/check_coverage_gate.py --fail-under 80`
- `python scripts/check_coverage_gate.py --fail-under 99`
- Validation-only import command from `agent-control/operate-pipeline/commands-and-hooks.md`
- File-path import of `mcp/server.py` helpers for MCP-safe summary and transaction status evidence.

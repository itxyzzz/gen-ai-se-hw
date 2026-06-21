# Source Context Snapshot

This Clio run documents the explicitly named Hera candidate trio. It does not target unqualified latest files and does not select or replace canonical documentation.

## Required Control Context Read

- `.agents/skills/generate-docs/SKILL.md`
- `agent-control/generate-docs/workflow.md`
- `agent-control/generate-docs/quality-bar.md`
- `agent-control/generate-docs/run-registry.md`
- `agents.md`
- `TASKS.md`
- `agent-control/operate-pipeline/commands-and-hooks.md`

## Candidate Sources Read

- Athena candidate specification: `docs/agent-runs/20260621-220037-write-spec-python-hera-python-full-set/agent-1-spec/outputs/specification.md`
- Hephaestus candidate inventory: `docs/agent-runs/20260621-222543-generate-code-python-hera-python-full-set/agent-2-code/outputs/inventory.md`
- Hephaestus candidate package root: `docs/agent-runs/20260621-222543-generate-code-python-hera-python-full-set/agent-2-code/outputs/`
- Hephaestus research notes: `docs/agent-runs/20260621-222543-generate-code-python-hera-python-full-set/agent-2-code/outputs/research-notes.md`
- Hephaestus validation checklist: `docs/agent-runs/20260621-222543-generate-code-python-hera-python-full-set/agent-2-code/validation-checklist.md`
- Hephaestus handoff: `docs/agent-runs/20260621-222543-generate-code-python-hera-python-full-set/agent-2-code/handoff.md`
- Themis candidate inventory: `docs/agent-runs/20260621-224632-generate-tests-python-hera-python-full-set/agent-3-tests/outputs/inventory.md`
- Themis validation checklist: `docs/agent-runs/20260621-224632-generate-tests-python-hera-python-full-set/agent-3-tests/validation-checklist.md`
- Themis handoff: `docs/agent-runs/20260621-224632-generate-tests-python-hera-python-full-set/agent-3-tests/handoff.md`
- Themis evidence files: `coverage-summary.txt`, `support-run-pipeline.txt`, `support-validate-transactions.txt`, `hook-pass.txt`, and `hook-fail.txt`

## Protected Support Context Read Only

- `mcp.json`
- `.codex/config.toml`
- `mcp/server.py`
- `scripts/check_coverage_gate.py`
- `.githooks/pre-push`
- `.claude/commands/run-pipeline.md`
- `.claude/commands/validate-transactions.md`
- `README.md`
- `sample-transactions.json`
- `docs/screenshots/operator-sourced/`
- `docs/agent-runs/selection-sets.json`
- `docs/agent-runs/final-selection.md`
- `docs/agent-runs/20260621-215717-orchestrate-runs-python-full-set/agent-5-orchestrator/child-runs.md`

## Repository Style Context

- `../AGENTS.md`
- `../HOMEWORK_STANDARDS.md`
- `../README.md`
- `../homework-1/README.md`
- `../homework-1/HOWTORUN.md`
- `../homework-2/README.md`, `HOWTORUN.md`, `ARCHITECTURE.md`, `TESTING_GUIDE.md`, `API_REFERENCE.md`
- `../homework-3/README.md`, `HOWTORUN.md`
- `../homework-4/README.md`, `HOWTORUN.md`, `ARCHITECTURE.md`, `TESTING_GUIDE.md`, `API_REFERENCE.md`

## Fresh Validation Workspace

Clio created a temporary validation workspace from the candidate package at:

`C:\Users\TANATA~1\AppData\Local\Temp\clio-20260621-225923-generate-docs-python-hera-python-full-set`

The workspace copied the Hephaestus candidate package, overlaid the Themis candidate test file, and copied only Operator Layer support files required for validation: `scripts/check_coverage_gate.py` and `.githooks/pre-push`.

Fresh validation signals:

- `python integrator.py`: passed with `total=8 settled=2 rejected=2 review_required=4 error=0`.
- `python -m pytest -p no:cacheprovider`: passed with `36 passed`.
- `python scripts\check_coverage_gate.py --stack python --fail-under 80`: passed with 97.44% total coverage.
- `python scripts\check_coverage_gate.py --stack python --fail-under 99`: expected failure; tests passed and coverage was below the demonstration threshold.
- Validation-only helper: passed with `total=8 valid=6 invalid=2`.
- MCP helper import: passed against candidate `shared/results/` by file-path importing root `mcp/server.py`.

# Operate-Pipeline Control Package

This folder is the canonical, tool-neutral reference package for Homework 6 Operator Layer pipeline operation surfaces.

It defines the one-time outer tools required by Task 3: `/run-pipeline`, `/validate-transactions`, matching project-skill wrappers, the coverage helper, the Git pre-push hook, and the Claude hook setting. These surfaces support the generated transaction-processing system and later Themis (Test Generator) validation, but they are not Themis per-run outputs.

## Files

- `commands-and-hooks.md`: shared behavior for pipeline commands, validator dry-run reporting, privacy/redaction, and coverage gate hooks.

## Entrypoints

- Claude command: `homework-6/.claude/commands/run-pipeline.md`
- Claude command: `homework-6/.claude/commands/validate-transactions.md`
- Claude skill: `homework-6/.claude/skills/run-pipeline/SKILL.md`
- Claude skill: `homework-6/.claude/skills/validate-transactions/SKILL.md`
- Codex skill: `homework-6/.agents/skills/run-pipeline/SKILL.md`
- Codex skill: `homework-6/.agents/skills/validate-transactions/SKILL.md`
- Coverage helper: `homework-6/scripts/check_coverage_gate.py`
- Git hook: `homework-6/.githooks/pre-push`
- Claude hook setting: `homework-6/.claude/settings.json`

All wrappers must stay thin and route to `commands-and-hooks.md`.

## Ownership Boundary

This package belongs to the Operator Layer pipeline operation surface. It is created and maintained once. Themis (Test Generator) validates these surfaces as part of test-quality evidence and reports gaps when they are missing or stale. Themis must not regenerate them as routine per-run test artifacts.

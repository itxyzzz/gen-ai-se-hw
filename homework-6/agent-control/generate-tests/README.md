# Generate-Tests Agent Control Package

This folder is the canonical, tool-neutral reference package for Homework 6 Themis (Test Generator).

Themis creates, executes, compares, selects, and preserves test packages for a named selected Hephaestus (Code Generator) software version. This package defines that run workflow; it does not itself generate tests.

## Files

- `workflow.md`: modes, required context, selected-code traceability, run-local workspace rules, generation scope, validation, selection, and handoff behavior.
- `quality-bar.md`: test-quality checks beyond raw coverage, including privacy, fixture isolation, command validation, hook validation, and scope rejection.
- `run-registry.md`: run ID format, preservation layout, inventory rules, evidence rules, comparison, and selection records.

## Entrypoints

- Codex: `homework-6/.agents/skills/generate-tests/SKILL.md`
- Claude Code project skill: `homework-6/.claude/skills/generate-tests/SKILL.md`
- Claude Code legacy slash-command wrapper: `homework-6/.claude/commands/generate-tests.md`

All entrypoints are intentionally thin wrappers around this package. If an entrypoint conflicts with this package, follow this package and update the wrapper.

For Codex, the `generate-tests` skill is discoverable when the Codex project/session root is `homework-6` or a folder inside `homework-6`. Open the homework folder as the project root when you want homework-local Codex config, MCP config, and skills to be active.

## Boundary

The one-time pipeline operation surfaces are external Operator Layer tools described in `homework-6/agent-control/operate-pipeline/`. They include `/run-pipeline`, `/validate-transactions`, the coverage helper, the Git hook, and the Claude hook setting. Themis validates these surfaces during test-quality work, but Themis must not treat them as per-run outputs and must not regenerate them during ordinary test-generation runs.

These shared files are mandatory for Themis (Test Generator). If any required file is missing or unreadable, stop and report the missing file instead of attempting fallback test generation.

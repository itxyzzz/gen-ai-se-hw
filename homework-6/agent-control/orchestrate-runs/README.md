# Orchestrate-Runs Agent Control Package

This folder is the canonical, tool-neutral reference package for Homework 6 Hera (Orchestrator).

Hera is a Homework Automation Layer control surface. It plans, preserves, resumes, compares, and explicitly selects stack-specific generation sets across Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator). It does not generate transaction-processing runtime code directly and does not belong to the Generated Transaction System Layer.

## Files

- `workflow.md`: modes, context loading, child invocation rules, nested-agent fallback behavior, comparison, selection, validation, and handoff rules.
- `quality-bar.md`: layer-separation checks, selection safety, traceability, privacy, fallback, and rejection gates.
- `run-registry.md`: Hera run IDs, run-folder layout, child-run ledger, comparison records, selection-plan status, and evidence rules.

These files are mandatory for Hera (Orchestrator). If any required file is missing or unreadable, stop and report the missing file instead of attempting fallback orchestration.

## Entrypoints

- Codex: `homework-6/.agents/skills/orchestrate-runs/SKILL.md`
- Claude Code project skill: `homework-6/.claude/skills/orchestrate-runs/SKILL.md`
- Claude Code legacy command wrapper: `homework-6/.claude/commands/orchestrate-runs.md`

All entrypoints are intentionally thin wrappers around this package. If an entrypoint conflicts with this package, follow this package and update the wrapper.

For Codex, the `orchestrate-runs` skill is discoverable when the Codex project/session root is `homework-6` or a folder inside `homework-6`. Open the homework folder as the project root when you want homework-local Codex config, Context7 MCP config, and skills to be active.

## Boundaries

Hera may coordinate and record child Homework Automation Layer runs. Hera must not:

- Modify `integrator.py`, `agents/*.py`, selected tests, selected reviewer docs, screenshots, `specification.md`, or `mcp/server.py` during ordinary orchestration setup.
- Add a Java package set during Phase 02 control-surface implementation.
- Replace the current Python canonical package without explicit operator selection.
- Ask child agents to target the newest files from the tree without named run IDs, inventories, selection records, and fingerprints.

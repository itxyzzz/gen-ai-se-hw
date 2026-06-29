# Generate-Docs Agent Control Package

This folder is the canonical, tool-neutral reference package for Homework 6 Clio (Documentation Generator).

Clio produces final reviewer-facing documentation, screenshot evidence, and a draft pull request description for the selected Homework 6 transaction-processing system. This package defines that run workflow; it does not itself generate documentation.

## Files

- `workflow.md`: modes, required context, run layout, screenshot handling, evidence generation, canonical selection, validation, and handoff behavior.
- `quality-bar.md`: documentation completeness, author-name sourcing, screenshot evidence, privacy, Themis boundary, PR draft, and scope rejection.
- `run-registry.md`: run ID format, preservation layout, output inventory, screenshot inventory, evidence files, comparison, and selection records.

## Entrypoints

- Codex: `homework-6/.agents/skills/generate-docs/SKILL.md`
- Claude Code project skill: `homework-6/.claude/skills/generate-docs/SKILL.md`
- Claude Code legacy slash-command wrapper: `homework-6/.claude/commands/generate-docs.md`

All entrypoints are intentionally thin wrappers around this package. If an entrypoint conflicts with this package, follow this package and update the wrapper.

For Codex, the `generate-docs` skill is discoverable when the Codex project/session root is `homework-6` or a folder inside `homework-6`. Open the homework folder as the project root when you want homework-local Codex config, MCP config, and skills to be active.

## Boundary

Clio consumes selected Athena (Spec Writer), Hephaestus (Code Generator), and Themis (Test Generator) outputs. It reruns selected tests and command evidence for final documentation, but it must not silently replace the selected Themis suite or modify generated runtime code.

Clio may reuse screenshots from `docs/screenshots/operator-sourced/` only by copying selected, privacy-safe evidence to stable reviewer-facing paths. The source folder is preserved and never pruned.

These shared files are mandatory for Clio (Documentation Generator). If any required file is missing or unreadable, stop and report the missing file instead of attempting fallback documentation generation.

# Generate-Code Agent Control Package

This folder is the canonical, tool-neutral reference package for Homework 6 Hephaestus (Code Generator).

Hephaestus consumes the selected Athena (Spec Writer) `specification.md`, uses Context7 during code generation, and creates the Task 2 Generated Transaction System Layer code in a later authorized run. This package defines that run; it does not itself generate the transaction-processing pipeline.

## Files

- `workflow.md`: modes, context loading, Context7 usage, sub-agent strategy, run layout, generation scope, validation, and handoff rules.
- `quality-bar.md`: Task 2 output checks, Context7 documentation checks, privacy/audit checks, sub-agent evidence checks, and out-of-scope rejection gates.
- `run-registry.md`: preservation, comparison, and evidence rules for `docs/agent-runs/`.

## Entrypoints

- Codex: `homework-6/.agents/skills/generate-code/SKILL.md`
- Claude Code: `homework-6/.claude/skills/generate-code/SKILL.md`

Both entrypoints are intentionally thin wrappers around this package. If an entrypoint conflicts with this package, follow this package and update the wrapper.

For Codex, the `generate-code` skill is discoverable when the Codex project/session root is `homework-6` or a folder inside `homework-6`. Codex scans `.agents/skills` from the current working directory upward to the repository root; it does not recursively scan nested homework folders from a higher project root. If Codex is opened at the parent repository root, `$generate-code` will not appear in the `$` skill selector or skill list even though the skill package is valid. Open the homework folder as the project root when you want homework-local Codex config, Context7 MCP config, and skills to be active.

Official Anthropic Claude Code docs describe project skills under `.claude/skills/SKILL_NAME/SKILL.md` as directly invocable with `/SKILL_NAME`. This package intentionally uses the native project-skill surface instead of keeping a duplicate command wrapper.

These files are mandatory for Hephaestus (Code Generator). If any required file is missing or unreadable, stop and report the missing file instead of attempting fallback code generation.

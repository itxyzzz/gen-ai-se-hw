# Write-Spec Agent Control Package

This folder is the canonical, tool-neutral reference package for Homework 6 Agent 1.

## Files

- `workflow.md`: modes, context loading, run layout, sub-agent strategy, model guidance, generation, comparison, selection, and handoff rules.
- `stack-profiles.md`: supported stack enum and stack-specific defaults for Python and Java specification runs.
- `quality-bar.md`: generated specification, run evidence, comparison, and selection quality requirements.
- `run-registry.md`: preservation, comparison, and final-selection rules for `docs/agent-runs/`.

## Entrypoints

- Codex: `homework-6/.agents/skills/write-spec/SKILL.md`
- Claude Code: `homework-6/.claude/skills/write-spec/SKILL.md`

Both entrypoints are intentionally thin wrappers around this package. If an entrypoint conflicts with this package, follow this package and update the wrapper.

Official Anthropic Claude Code docs at `https://code.claude.com/docs/en/skills` describe project skills under `.claude/skills/<skill-name>/SKILL.md` as directly invocable with `/<skill-name>`. They also state that same-named skills take precedence over `.claude/commands/` command files, so this package intentionally uses the native project-skill surface instead of keeping a duplicate command wrapper.

These files are mandatory for Agent 1. If any required file is missing or unreadable, stop and report the missing file instead of attempting a fallback specification generation.

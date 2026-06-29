# Write-Spec Agent Control Package

This folder is the canonical, tool-neutral reference package for Homework 6 Athena (Spec Writer).

## Files

- `workflow.md`: modes, context loading, run layout, sub-agent strategy, model guidance, generation, comparison, selection, and handoff rules.
- `transaction-system-brief.md`: direct product input for Athena (Spec Writer); cleaned transaction-processing system assignment separate from frozen operator-facing `TASKS.md`.
- `stack-profiles.md`: supported stack enum and stack-specific defaults for Python and Java specification runs.
- `quality-bar.md`: generated specification, run evidence, comparison, and selection quality requirements.
- `run-registry.md`: preservation, comparison, and final-selection rules for `docs/agent-runs/`.

## Entrypoints

- Codex: `homework-6/.agents/skills/write-spec/SKILL.md`
- Claude Code: `homework-6/.claude/skills/write-spec/SKILL.md`

Both entrypoints are intentionally thin wrappers around this package. If an entrypoint conflicts with this package, follow this package and update the wrapper.

For Codex, the `write-spec` skill is discoverable when the Codex project/session root is `homework-6` or a folder inside `homework-6`. Codex scans `.agents/skills` from the current working directory upward to the repository root; it does not recursively scan nested homework folders from a higher project root. If Codex is opened at the parent repository root, `$write-spec` will not appear in the `$` skill selector or skill list even though the skill package is valid. This is the same root-selection peculiarity as the Homework 5 and Homework 6 MCP server configuration: open the homework folder as the project root when you want homework-local Codex config and skills to be active.

Official Anthropic Claude Code docs at `https://code.claude.com/docs/en/skills` describe project skills under `.claude/skills/<skill-name>/SKILL.md` as directly invocable with `/<skill-name>`. They also state that same-named skills take precedence over `.claude/commands/` command files, so this package intentionally uses the native project-skill surface instead of keeping a duplicate command wrapper.

`TASKS.md` names `.claude/commands/write-spec.md` as an example slash-command path. For this repository, `.claude/skills/write-spec/SKILL.md` is the deliberate newer Claude Code project skill surface for Athena (Spec Writer). Do not recreate a legacy command wrapper solely to mirror the example path; update this package and the project skill when Athena guidance changes.

These files are mandatory for Athena (Spec Writer). If any required file is missing or unreadable, stop and report the missing file instead of attempting a fallback specification generation.

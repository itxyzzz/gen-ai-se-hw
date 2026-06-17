# /write-spec

## Purpose

Generate, compare, resume, or select preserved Homework 6 Agent 1 specification runs. This slash command is a thin Claude Code wrapper around the same canonical workflow used by the Codex Markdown `write-spec` skill.

## Required Workflow

1. Read `homework-6/.agents/skills/write-spec/references/write-spec-workflow.md`.
2. Read `homework-6/.agents/skills/write-spec/references/stack-profiles.md`.
3. Read `homework-6/.agents/skills/write-spec/references/write-spec-quality-bar.md`.

These shared workflow files are mandatory. If any required reference is missing or unreadable, stop and report the missing file instead of attempting a fallback generation.

## Execution

Execute `write-spec-workflow.md` exactly. It owns modes, stack input, run layout, sub-agent requirements, model guidance, research rules, selection behavior, required outputs, and review gates.

## Examples

```text
/write-spec
/write-spec stack=python
/write-spec stack=java
/write-spec compare
/write-spec select run=20260616-173000-write-spec-python-primary
```

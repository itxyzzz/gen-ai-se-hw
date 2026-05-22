# Claude Code Adapter

## Launch Phrase

```text
Run Homework 4 bug-001 through the full agentic pipeline using the Claude Code adapter.
```

## Mapping

- Load `skills/pipeline-harness-wrapper.md` as the top-level workflow skill.
- Map each `agents/*.agent.md` file to a Claude Code agent or subagent prompt.
- Load `skills/research-quality-measurement.md` and `skills/unit-tests-FIRST.md`
  when those agents run.
- Use the same run workspace and artifact contract as Codex Chat.

## Execution Rules

- Run stages sequentially unless Claude Code subagents are explicitly used for
  read-only review after a stage completes.
- Keep file edits inside the current run's `app/` directory before promotion.
- Preserve all report names exactly so the same benchmark rubric can compare
  runs from different tools.

## Validation Checklist

- Confirm all six stages ran in order.
- Confirm required artifacts exist.
- Confirm test commands and outcomes are recorded in `command-log.md`.
- Confirm `run-metadata.json` identifies adapter `claude-code` for Claude runs.

## Limitations

This submission documents Claude Code portability. It does not include or
require a Claude Code executable wrapper.

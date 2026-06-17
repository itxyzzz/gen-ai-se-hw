# /write-spec

## Purpose

Generate, compare, resume, or select preserved Homework 6 Agent 1 specification runs. This slash command is a thin Claude Code wrapper around the same canonical workflow used by the Codex Markdown `write-spec` skill.

## Inputs

- `stack=python`: primary default stack.
- `stack=java`: optional alternate stack for comparison.
- Stack omitted: use `stack=python`.
- `stack=auto`: reject as unsupported.
- Other stack values: reject and name the supported enum.
- Optional mode words: `generate`, `resume`, `compare`, or `select`; default to `generate`.

Examples:

```text
/write-spec
/write-spec stack=python
/write-spec stack=java
/write-spec compare
/write-spec select run=20260616-173000-write-spec-python-primary
```

## Steps

1. Read `homework-6/.agents/skills/write-spec/references/write-spec-workflow.md`.
2. Read `homework-6/.agents/skills/write-spec/references/stack-profiles.md`.
3. Read `homework-6/.agents/skills/write-spec/references/write-spec-quality-bar.md`.
4. Execute the canonical workflow. Treat `write-spec-workflow.md` as the source of truth if this command differs from the Codex skill wrapper.

Fallback when the shared workflow files are unavailable:

1. Accept only `stack=python` and `stack=java`; default omitted stack to Python and reject `auto`.
2. Create a run folder under `homework-6/docs/agent-runs/` before drafting output.
3. Generate candidate outputs in the run folder, not canonical paths.
4. Require research notes or explicit fallback limitations.
5. Apply the review gate below before comparison or selection.

## Required Outputs

Follow `write-spec-workflow.md` for exact output rules. In `generate` or `resume` mode, create or update:

```text
homework-6/docs/agent-runs/<run-id>/
  run-metadata.md
  inputs/
    source-context.md
  agent-1-spec/
    outputs/
      specification.md
      agents.md
      docs/domain-rules.md
      docs/technical-conventions.md
      docs/development-process.md
    research-notes.md
    validation-checklist.md
    handoff.md
  comparison.md
```

In `select` mode, update `homework-6/docs/agent-runs/final-selection.md` and copy selected outputs to canonical paths only after recording the selected run ID, stack, files, rationale, operator, and post-selection edits.

## Review Gate

Use `write-spec-quality-bar.md` for the full review. At minimum, before reporting a run as ready:

- Confirm `specification.md` has high-level objective, 4-5 mid-level objectives, implementation notes, beginning and ending context, and one low-level task entry per meta-agent.
- Confirm every low-level task includes exact prompt, target file, target function or method, details, edge cases, acceptance criteria, and verification.
- Confirm generated files, functions, commands, tests, coverage hook, and MCP notes match the selected stack.
- Confirm precise decimal money handling, ISO 4217-style currency validation, structured audit logging, and no plaintext PII logging.
- Confirm Context7 research is documented when used, or fallback limitations are explicit.
- Confirm canonical files were not overwritten before final selection.

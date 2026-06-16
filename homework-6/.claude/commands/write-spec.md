# /write-spec

## Purpose

Generate, compare, resume, or select preserved Homework 6 Agent 1 specification runs. The command creates a stack-specific candidate specification package for the multi-agent banking transaction pipeline before downstream code, tests, hooks, MCP server, screenshots, or final docs are produced.

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

1. Prefer reading `homework-6/.agents/skills/write-spec/SKILL.md` if present.
2. Read `homework-6/.agents/skills/write-spec/references/stack-profiles.md` and normalize stack input.
3. Read `homework-6/.agents/skills/write-spec/references/write-spec-quality-bar.md`.
4. Read assignment context: `homework-6/TASKS.md`, `homework-6/sample-transactions.json`, root `HOMEWORK_STANDARDS.md`, root `README.md`, and Homework 3 reference docs when available.
5. Create a run folder before drafting output using `homework-6/docs/agent-runs/YYYYMMDD-HHMMSS-write-spec-<stack>-<short-label>/`.
6. Record source context, selected stack, operator prompt, available tools, and research limitations in run metadata.
7. Use Context7 for current technical library or framework documentation when available. Record each Context7 query, returned library ID, and applied insight.
8. Use current authoritative web sources for domain facts when needed and available. Record URLs, dates accessed, and applied decisions.
9. If research tools are unavailable, record the limitation and rely only on assignment files and local references.
10. Draft all candidate outputs inside the run folder first.
11. Validate against Homework 6 Task 1, the selected stack profile, and the quality bar.
12. Compare preserved runs when requested. Include cross-stack differences when Python and Java runs both exist.
13. Copy selected files to canonical homework paths only when the operator explicitly selects a run.

Fallback workflow when the Codex skill files are unavailable:

1. Use this command file as the workflow source.
2. Accept only `stack=python` and `stack=java`; default omitted stack to Python and reject `auto`.
3. Create the same run folder structure.
4. Generate `specification.md`, `agents.md`, domain rules, technical conventions, development process, research notes, validation checklist, and handoff in the run folder.
5. Apply the same review gate below before comparison or selection.

## Required Outputs

In `generate` or `resume` mode, create or update:

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

Before reporting a run as ready:

- Confirm `specification.md` has high-level objective, 4-5 mid-level objectives, implementation notes, beginning and ending context, and one low-level task entry per meta-agent.
- Confirm every low-level task includes exact prompt, target file, target function or method, details, edge cases, acceptance criteria, and verification.
- Confirm generated files, functions, commands, tests, coverage hook, and MCP notes match the selected stack.
- Confirm precise decimal money handling, ISO 4217-style currency validation, structured audit logging, and no plaintext PII logging.
- Confirm Context7 research is documented when used, or fallback limitations are explicit.
- Confirm canonical files were not overwritten before final selection.

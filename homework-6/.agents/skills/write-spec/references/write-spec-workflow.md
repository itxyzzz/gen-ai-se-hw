# Write Spec Workflow

This is the canonical workflow for both Homework 6 Agent 1 entrypoints:

- Codex Markdown skill: `homework-6/.agents/skills/write-spec/SKILL.md`
- Claude Code slash command: `homework-6/.claude/commands/write-spec.md`

The wrappers should stay thin. Update this file first when the workflow changes.

## Required Context

Read the assignment context before writing or selecting outputs:

1. `homework-6/TASKS.md`
2. `homework-6/sample-transactions.json`
3. `homework-6/agents.md` when present
4. Root `AGENTS.md`, `HOMEWORK_STANDARDS.md`, and `README.md` when available
5. Homework 3 reference package when available: `homework-3/specification.md`, `homework-3/agents.md`, `homework-3/docs/domain-rules.md`, `homework-3/docs/technical-conventions.md`, and `homework-3/docs/development-process.md`
6. This skill's references: `references/stack-profiles.md` and `references/write-spec-quality-bar.md`

Use local references as the quality bar. Do not copy Homework 3 domain claims as Homework 6 research.

## Operating Modes

- `generate`: create a new preserved run under `homework-6/docs/agent-runs/`.
- `resume`: continue a bounded run using its `handoff.md` and completed outputs.
- `compare`: compare two or more preserved runs without overwriting canonical files.
- `select`: record the chosen run in `docs/agent-runs/final-selection.md` and copy its selected outputs to canonical paths only after the operator requests selection.

Default to `generate` unless the operator asks for comparison, selection, or continuation.

## Stack Input

Accept only `stack=python` and `stack=java`. If the stack is omitted, use `stack=python`.

Reject `stack=auto` and any unsupported value with a short message naming the supported enum. Do not invent a new language profile during a run. Load `references/stack-profiles.md` before drafting stack-specific files, functions, commands, test tools, coverage hooks, or MCP notes.

## Workflow Steps

1. Confirm the requested mode and normalize stack input.
2. Create a unique run ID using `YYYYMMDD-HHMMSS-write-spec-<stack>-<short-label>`.
3. Create the run folder before drafting content:
   - `run-metadata.md`
   - `inputs/source-context.md`
   - `agent-1-spec/outputs/`
   - `agent-1-spec/research-notes.md`
   - `agent-1-spec/validation-checklist.md`
   - `agent-1-spec/handoff.md`
4. Inventory required outputs and record missing local references in `run-metadata.md`.
5. Research before writing. Use Context7 for current technical documentation when available. Use current authoritative web sources for banking, money, logging, audit, privacy, or ISO-currency facts when needed.
6. Record every research query, returned library ID or URL, access date, and applied decision. If research tools are unavailable, record the limitation and rely only on local assignment context.
7. Draft all generated files inside the run folder first. Do not overwrite canonical `homework-6/specification.md` or canonical docs before final selection.
8. Validate generated outputs against `references/write-spec-quality-bar.md`, the selected stack profile, and Homework 6 Task 1 required sections.
9. If the work becomes too large for one thread, stop at a coherent boundary and write `handoff.md` with completed decisions, remaining sections, next prompt, and validation state.
10. In `select` mode, copy only operator-selected outputs to canonical paths and update `docs/agent-runs/final-selection.md`.

When repository instructions require `dev-doc-harness` or Superpowers, comply with those planning and freeze gates. The generated Homework 6 agents must still be usable without those tools.

## Research Rules

- Prefer Context7 for selected framework and library documentation.
- Agent 1 may use web or other current sources for domain and technical rules when available.
- Agent 2 must later document at least two Context7 queries in canonical `research-notes.md`; prepare the spec so that requirement is explicit.
- Treat the banking pipeline as an educational simulation. Do not claim legal, AML, sanctions, payment-network, or bank regulatory compliance unless a cited source and assignment scope support the statement.
- Do not paste credentials, real account data, real customer data, tokens, secrets, or unrelated local paths into research notes.

## Run Preservation

Preserve generation attempts under `homework-6/docs/agent-runs/<run-id>/`. A run is evidence, not the canonical submission.

Use this layout:

```text
<run-id>/
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

Compare runs by completeness, research provenance, stack precision, privacy handling, task-card executability, validation coverage, and downstream handoff usefulness. Cross-stack comparison must note where Python and Java require different files, functions, commands, coverage tools, and MCP implementation choices.

## Output Requirements

For a generated Agent 1 package, produce at least:

- `agent-1-spec/outputs/specification.md`
- `agent-1-spec/outputs/agents.md`
- `agent-1-spec/outputs/docs/domain-rules.md`
- `agent-1-spec/outputs/docs/technical-conventions.md`
- `agent-1-spec/outputs/docs/development-process.md`
- `agent-1-spec/research-notes.md`
- `agent-1-spec/validation-checklist.md`
- `agent-1-spec/handoff.md`

The generated `specification.md` must include high-level objective, 4-5 mid-level objectives, implementation notes, beginning and ending context, and one low-level task entry per meta-agent. The selected stack must be visible in files, functions, commands, test tools, coverage gate design, and MCP notes.

## Self-Review Gate

Before reporting a run as ready for comparison or selection, verify:

- Task 1's five required sections are present.
- Every low-level task has exact prompt, file, function, behavior details, edge cases, acceptance criteria, and verification.
- Money uses precise decimal semantics, never binary floating point.
- Currency validation uses ISO 4217-style codes.
- Logs and audit examples avoid plaintext PII and account identifiers.
- Agent communication uses the required JSON file protocol through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- Context7 and domain research notes are present, or explicit fallback limitations are recorded.
- Canonical files are untouched unless the operator requested `select`.

## Handoff Rules

Write `handoff.md` whenever the run cannot be completed confidently in one thread. Include the run ID, selected stack, completed files, unfinished sections, validation status, known risks, and the exact next prompt. Keep the handoff bounded enough that a fresh agent can continue without re-reading this conversation.

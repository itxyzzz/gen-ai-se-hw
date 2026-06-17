# Homework 6 Agent Guide

## Context Load Order

Before changing Homework 6 files, load context in this order:

1. System, tool, sandbox, model, plugin, and MCP constraints.
2. Repository `AGENTS.md`, `HOMEWORK_STANDARDS.md`, and root `README.md`.
3. `homework-6/TASKS.md` and `homework-6/sample-transactions.json`.
4. This `homework-6/agents.md`.
5. Selected or active run artifacts under `homework-6/docs/agent-runs/`.
6. For Agent 1, the `write-spec` skill or slash command and its references.
7. Current git status, existing diffs, and relevant tests or validation output.

When instructions conflict, preserve the highest-priority user and repository rules first, then the most specific Homework 6 artifact.

## Agent Roles

| Agent | Role | Primary outputs |
|---|---|---|
| Agent 1 | Specification writer | `specification.md`, spec support docs, research notes, and preserved generation runs. |
| Agent 2 | Code generator | Integrator, at least three cooperating pipeline agents, JSON file protocol, and Context7 research notes. |
| Agent 3 | Test and hook author | Unit and integration tests, coverage gate hook, `/run-pipeline`, and `/validate-transactions`. |
| Agent 4 | Documentation author | README, HOWTORUN, architecture and testing docs, screenshots, and final PR support. |

Agent 1 is stack-flexible through the fixed enum in `.agents/skills/write-spec/references/stack-profiles.md`. The default generation stack is `python`; `java` is an optional alternate profile. `auto` is not supported. After stack selection, every generated `specification.md` must be concrete for that stack.

This `homework-6/agents.md` file is the standing project-level guide required by Task 1. It lives beside `TASKS.md` so every run and downstream agent can load the same stable context. Do not regenerate or overwrite it during individual Agent 1 runs; if a run discovers a needed guide change, record the recommendation in that run's handoff and apply it as a separate control-surface update.

## Shared Data and Run Artifacts

Later pipeline agents must use the required JSON file protocol:

```text
shared/
  input/
  processing/
  output/
  results/
```

Major generated artifacts must be preserved under `docs/agent-runs/` before canonical copy. Use run IDs in this format:

```text
YYYYMMDD-HHMMSS-<agent-or-pipeline>-<stack>-<short-label>
```

Do not overwrite canonical `specification.md`, generated docs, or final results until the operator selects a run or explicitly authorizes the overwrite.

## Research and MCP Rules

- Agent 1 may perform and document current technical or domain research before producing the spec package.
- Agent 2 must use Context7 during code generation and document at least two queries in `research-notes.md`.
- Each Context7 note should include search text, returned library ID, access date, and applied insight.
- If Context7 or web research is unavailable, record the limitation and continue from local assignment context.
- `pipeline-status` must be added to `mcp.json` and `.codex/config.toml` only after `mcp/server.py` exists.
- A homework-root Codex project may be needed for reliable discovery of homework-local MCP config.

## Privacy and Audit Rules

- Treat account identifiers, descriptions, transaction metadata, and audit details as sensitive.
- Never log plaintext PII or full account identifiers. Redact examples such as `ACC-****1001`.
- Use precise decimal types for money: `decimal.Decimal` for Python or `BigDecimal` for Java. Never use binary floating point for amounts.
- Validate currency with ISO 4217-style codes and reject unsupported sample values such as `XYZ`.
- Audit records should include timestamp, agent name, transaction ID, safe outcome, and reason code when applicable.
- Keep the banking pipeline framed as an educational simulation, not as legal, banking, AML, sanctions, or payment-network compliance.

## Quality Gates Without Harness

Every agent must be able to work without `dev-doc-harness` or Superpowers:

1. Read the required context.
2. Define or confirm the bounded task.
3. Preserve major generated outputs under a run folder when regenerating or comparing.
4. Write research notes or fallback limitations.
5. Validate required sections, commands, tests, and privacy rules before reporting completion.
6. Update `CHANGELOG.md` for committed homework increments.
7. Review the diff for unrelated changes, placeholders, canonical overwrite, and generated noise.

## Harness and Superpowers Compatibility

When repository instructions require harness-managed planning, use homework-scoped work items under:

```text
homework-6/docs/work-items/<work-id>/
```

Harness and Superpowers may assist planning, execution, verification, and review, but the Homework 6 agent instructions must remain portable. Do not make a generated agent depend on hidden harness state, a prior chat thread, or unavailable plugins.

## Final Selection Rules

- Preserve every meaningful Agent 1 generation attempt under `docs/agent-runs/`.
- Compare runs before selection when more than one viable output exists.
- After the first successful `write-spec generate` run, if `specification.md` does not exist yet, copy the run's selected `specification.md` to the canonical path automatically and record the auto-selection.
- Record the chosen run in `docs/agent-runs/final-selection.md`.
- Record date, run ID, stack, selected files, copied canonical paths, rationale, operator, and post-selection edits.
- Treat `specification.md` as the default selected package. Copy supporting docs only when explicitly selected, and never copy a run-local agent guide over this file.
- Canonical files are submission files; run folders are evidence snapshots. Keep both roles distinct.

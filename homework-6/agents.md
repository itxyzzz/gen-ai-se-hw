# Homework 6 Agent Guide

## Context Load Order

Before changing Homework 6 files, load context in this order:

1. System, tool, sandbox, model, plugin, and MCP constraints.
2. Repository `AGENTS.md`, `HOMEWORK_STANDARDS.md`, and root `README.md`.
3. For operator-layer maintenance, `homework-6/TASKS.md` and `homework-6/sample-transactions.json`.
   For Athena (Spec Writer) normal generation, use `homework-6/agent-control/write-spec/transaction-system-brief.md` instead of `TASKS.md`.
   For Hephaestus (Code Generator) normal generation, use selected `homework-6/specification.md`, `TASKS.md` Task 2 checks, Context7 MCP configuration, and `homework-6/agent-control/generate-code/`.
4. This `homework-6/agents.md`.
5. Selected or active run artifacts under `homework-6/docs/agent-runs/`.
6. For Athena (Spec Writer), the `write-spec` skill or slash command and its references.
   For Hephaestus (Code Generator), the `generate-code` skill or slash command and its references.
7. Current git status, existing diffs, and relevant tests or validation output.

When instructions conflict, preserve the highest-priority user and repository rules first, then the most specific Homework 6 artifact.

## Layer Glossary

Use this glossary before using Greek identity labels. In technical prompts and instructions, write paired labels such as `Athena (Spec Writer)` until this glossary has been loaded; never rely on `Athena` alone to carry responsibility.

| Layer | Meaning | Naming rule |
|---|---|---|
| Operator Layer | The human/Codex layer that maintains Homework 6 control surfaces, uses repository planning, updates changelogs, and repairs the homework automation setup. | May use harness terms because it is outside the generated agents. |
| Homework Automation Layer | The four deliverable automation agents that create the transaction-processing system artifacts. | Uses Greek identity plus functional role labels listed below. |
| Generated Transaction System Layer | The actual transaction-processing software, tests, docs, MCP server, commands, hooks, and runtime artifacts produced by the homework automation agents. This layer includes deterministic software components and runtime application agents. | Uses product and component names, not Greek identities. |
| Runtime transaction pipeline agents | Stack-native application components inside the generated software, such as Transaction Validator, Fraud Detector, Settlement Processor, Compliance Checker, Reporting Agent, and Integrator. They are implemented as Python modules/classes, Java classes, or equivalent stack-native components with a shared message-processing protocol. | Keep functional component names. Do not name these components Athena, Hephaestus, Themis, or Clio, and do not implement them as Claude/Codex skills. |
| Executor sub-agents | Optional worker agents used by a tool runtime to decompose a generation or implementation task. They may inspect, draft, test, or review bounded scopes. | Keep functional names such as domain-research sub-agent or final-review sub-agent. Do not confuse them with the four Homework Automation Layer agents. |

## Homework Automation Agent Roles

| Agent | Role | Primary outputs |
|---|---|---|
| Athena / Spec Writer / write-spec | Creates the detailed technical specification for the transaction-processing system. | `specification.md`, spec support docs, research notes, and preserved generation runs. |
| Hephaestus / Code Generator | Builds the generated transaction-processing software from the selected specification. | Integrator, at least three cooperating runtime pipeline components, JSON file protocol, and Context7 research notes. |
| Themis / Test Generator | Creates or extends tests and quality gates for the generated transaction-processing software. | Unit and integration tests, coverage gate hook, `/run-pipeline`, and `/validate-transactions`. |
| Clio / Documentation Generator | Produces reviewer-facing documentation and handoff evidence for the generated transaction-processing software. | README, HOWTORUN, architecture and testing docs, screenshots, and final PR support. |

Athena (Spec Writer) is stack-flexible through the fixed enum in `agent-control/write-spec/stack-profiles.md`. The default generation stack is `python`; `java` is an optional alternate profile. `auto` is not supported. After stack selection, every generated `specification.md` must be concrete for that stack.

Hephaestus (Code Generator) uses the tool-neutral control package at `agent-control/generate-code/`. Codex entrypoint: `.agents/skills/generate-code/SKILL.md`. Claude Code entrypoint: `.claude/skills/generate-code/SKILL.md`. A Hephaestus run consumes the selected `specification.md`, uses Context7 during code generation, and documents at least two Context7 query records in canonical `research-notes.md`. Hephaestus may use executor sub-agents up to the Homework 6 `agents.max_threads = 8` configuration without additional operator approval; it should use curated context, deliberate model/reasoning selection, and orchestrator-owned final integration.

This `homework-6/agents.md` file is the standing project-level guide required by Task 1. It lives beside `TASKS.md` so every run and downstream Homework Automation Layer agent can load the same stable context. Do not regenerate or overwrite it during individual Athena (Spec Writer) runs; if a run discovers a needed guide change, record the recommendation in that run's handoff and apply it as a separate control-surface update.

`TASKS.md` stays the frozen operator assignment. Athena (Spec Writer) uses `agent-control/write-spec/transaction-system-brief.md` as its direct product input so the generated specification targets the transaction-processing system, not the homework automation harness.

## Shared Data and Run Artifacts

Generated Transaction System Layer components must use the required JSON file protocol:

```text
shared/
  input/
  processing/
  output/
  results/
```

Major Homework Automation Layer run artifacts must be preserved under `docs/agent-runs/` before canonical copy. Use run IDs in this format:

```text
YYYYMMDD-HHMMSS-<agent-or-pipeline>-<stack>-<short-label>
```

Do not overwrite canonical `specification.md`, generated docs, or final results until the operator selects a run or explicitly authorizes the overwrite.

## Research and MCP Rules

- Athena (Spec Writer) may perform and document current technical or domain research before producing the spec package.
- Hephaestus (Code Generator) must use Context7 during code generation and document at least two queries in `research-notes.md`.
- Each Context7 note should include search text, returned library ID, access date, and applied insight.
- If Context7 or web research is unavailable, record the limitation and continue from local assignment context.
- `pipeline-status` must be added to `mcp.json` and `.codex/config.toml` only after `mcp/server.py` exists.
- A homework-root Codex project may be needed for reliable discovery of homework-local MCP config.

## Privacy and Audit Rules

- Treat account identifiers, descriptions, transaction metadata, and audit details as sensitive.
- Never log plaintext PII or full account identifiers. Redact examples such as `ACC-****1001`.
- Use precise decimal types for money: `decimal.Decimal` for Python or `BigDecimal` for Java. Never use binary floating point for amounts.
- Validate currency with ISO 4217-style codes and reject unsupported sample values such as `XYZ`.
- Audit records should include timestamp, runtime component name, transaction ID, safe outcome, and reason code when applicable.
- Keep the banking pipeline framed as an educational simulation, not as legal, banking, AML, sanctions, or payment-network compliance.

## Homework Automation Rules Without Harness

Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator) must be able to run without `dev-doc-harness` or Superpowers. Those tools belong to the Operator Layer when repository maintainers change the automation surfaces.

1. Read the required context.
2. Define or confirm the bounded task.
3. Preserve major generated outputs under a run folder when regenerating or comparing.
4. Write research notes or fallback limitations.
5. Validate required sections, commands, tests, and privacy rules before reporting completion.
6. Update `CHANGELOG.md` for committed homework increments.
7. Review the diff for unrelated changes, placeholders, canonical overwrite, and generated noise.

Do not introduce harness freeze gates, planning package requirements, or Superpowers-only flows into prompts for the four Homework Automation Layer agents. Athena (Spec Writer) must produce a full transaction-system specification in one `specification.md`, not a harness-style split between spec and plan.

## Harness and Superpowers Compatibility

When repository instructions require harness-managed planning, that applies to Operator Layer maintenance work. Use homework-scoped work items under:

```text
homework-6/docs/work-items/<work-id>/
```

Harness and Superpowers may assist Operator Layer planning, execution, verification, and review, but the Homework 6 automation agents and generated transaction-system specs must remain portable. Do not make a Homework Automation Layer agent or Generated Transaction System Layer artifact depend on hidden harness state, a prior chat thread, or unavailable plugins.

## Final Selection Rules

- Preserve every meaningful Athena (Spec Writer) generation attempt under `docs/agent-runs/`.
- Compare runs before selection when more than one viable output exists.
- After the first successful `write-spec generate` run, if `specification.md` does not exist yet, copy the run's selected `specification.md` to the canonical path automatically and record the auto-selection.
- Record the chosen run in `docs/agent-runs/final-selection.md`.
- Record date, run ID, stack, selected files, copied canonical paths, rationale, operator, and post-selection edits.
- Treat `specification.md` as the default selected package. Copy supporting docs only when explicitly selected, and never copy a run-local agent guide over this file.
- Canonical files are submission files; run folders are evidence snapshots. Keep both roles distinct.

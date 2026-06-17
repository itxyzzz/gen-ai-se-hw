# Write Spec Quality Bar

Use this reference when generating, reviewing, comparing, or selecting Homework 6 Agent 1 specification runs. It is intentionally stricter than the assignment minimum so downstream code, test, MCP, hook, and documentation agents receive executable instructions.

## Required Generated Files

A complete Agent 1 run produces these files inside the run folder first:

| Output | Purpose |
|---|---|
| `agent-1-spec/outputs/specification.md` | Canonical candidate Task 1 specification. |
| `agent-1-spec/outputs/docs/domain-rules.md` | Research-backed banking-pipeline domain assumptions and limits. |
| `agent-1-spec/outputs/docs/technical-conventions.md` | Money, IDs, JSON, audit, logging, redaction, and file-protocol conventions. |
| `agent-1-spec/outputs/docs/development-process.md` | Portable execution gates for later agents. |
| `agent-1-spec/research-notes.md` | Query/source log, applied decisions, and fallback notes. |
| `agent-1-spec/validation-checklist.md` | Completed self-review against this quality bar. |
| `agent-1-spec/handoff.md` | Continuity record for later threads or downstream agents. |

Canonical `homework-6/specification.md` is the normal selectable output. After the first successful generation run, copy it automatically only if no canonical spec exists yet. Later runs require explicit selection or overwrite authorization.

Supporting docs are preserved as run evidence unless the operator explicitly selects them as canonical support docs. The standing `homework-6/agents.md` guide is not a per-run output and must not be overwritten by run selection.

## Specification Shape

The generated `specification.md` must include:

1. High-Level Objective: one clear sentence describing the transaction-processing pipeline.
2. Mid-Level Objectives: 4-5 concrete, testable objectives with observable success.
3. Implementation Notes: precise decimal money handling, ISO 4217-style currency validation, audit logging, no plaintext PII logging, JSON file protocol, coverage expectations, and MCP responsibilities.
4. Context: beginning state with `sample-transactions.json`; ending state with processed results in `shared/results/`, a summary report, and test coverage target of at least 90%.
5. Low-Level Tasks: one entry per meta-agent, with enough detail for code generation, test generation, documentation generation, and command/hook creation.

The spec should also include edge cases and verification mapping when space allows. If a section is moved to a supporting doc, the spec must link to it and keep the key acceptance criteria inline.

## Low-Level Task Card Standard

Each low-level task entry must include:

| Field | Required content |
|---|---|
| `Task` | Agent name and role, such as Agent 2 Code Generation. |
| `Prompt` | Exact prompt to give Claude Code, Codex, or Copilot. |
| `File to CREATE` or `File to UPDATE` | Stack-specific path. |
| `Function to CREATE` | Stack-specific function, method, or class entry point. |
| `Details` | Inputs, outputs, transformations, file moves, audit behavior, and privacy constraints. |
| `Edge cases` | Invalid amount, invalid currency, missing field, high value, odd timestamp, cross-country data, and failed downstream state. |
| `Acceptance criteria` | Checkable outcome for the task. |
| `Verification` | Exact command, test type, or manual review evidence. |

Task cards must not say "implement appropriate code" without naming target files, functions, and behaviors. The generated spec must be stack-specific after stack selection.

## Research and Domain Rules

Research notes must distinguish:

- Cited facts: source or library documentation with URL or Context7 library ID.
- Assignment assumptions: constraints directly from `TASKS.md` and `sample-transactions.json`.
- Generated design decisions: chosen thresholds, file names, class names, prompts, validation rules, and error formats.
- Fallback limitations: tools or network access that were unavailable.

Domain docs must state that the pipeline is an educational simulation and not legal, banking, AML, sanctions, payment-network, or compliance advice. Use conservative engineering rules: precise money, structured audit events, redacted logs, synthetic data, safe errors, and explicit unsupported-claim boundaries.

## Run Folder Contract

Every run must have:

- `run-metadata.md` with run ID, timestamp, stack, mode, operator prompt, available tools, and source files read.
- `inputs/source-context.md` summarizing assignment inputs and reference docs used.
- Generated candidate outputs under `agent-1-spec/outputs/`.
- `research-notes.md` with Context7 and domain research records or fallback notes.
- `validation-checklist.md` with pass/fail review against Task 1, stack profile, and this quality bar.
- `handoff.md` even when complete; for complete runs it may say no continuation is required.

Run folders are append-only evidence once selected. Post-selection refinements to canonical files must be recorded in `final-selection.md` or a later changelog entry.

## Comparison and Selection

Compare runs using these criteria:

| Criterion | Review question |
|---|---|
| Completeness | Are all required Task 1 outputs present and internally consistent? |
| Research provenance | Are sources, Context7 library IDs, and fallback limitations recorded? |
| Technical precision | Are files, functions, commands, coverage tools, and MCP notes concrete for the selected stack? |
| Privacy handling | Are account IDs and descriptions treated as sensitive and redacted in logs/audit examples? |
| Task-card executability | Could a downstream agent implement from the low-level tasks without guessing? |
| Handoff usefulness | Can a fresh thread continue or implement from the artifacts? |

Final selection must record selected run ID, stack, selected files, copied canonical paths, rationale, operator, date, and any post-selection edits.

The default selected file is `agent-1-spec/outputs/specification.md` copied to `homework-6/specification.md`. Copy `agent-1-spec/outputs/docs/*` or `research-notes.md` only when explicitly selected. Never copy a run-local agent guide over `homework-6/agents.md`; update that standing guide separately when its global instructions need to change.

## Built-In Quality Gate

Before selecting a run, confirm:

- `specification.md` contains all five required Task 1 sections.
- The selected stack is either `python` or `java`; omitted input was normalized to `python`.
- The low-level task cards include one entry per meta-agent.
- The code-generation task requires at least three cooperating pipeline agents.
- Agent 2 Context7 usage and two-query documentation are explicit.
- Coverage targets distinguish assignment hook minimum of 80% from spec ending context target of at least 90%.
- `mcp.json` will include Context7 and `pipeline-status` only after `mcp/server.py` exists.
- No canonical file was overwritten before selection.

## Context-Limit Handoff

When a run is too large for one thread, stop at a coherent boundary and write `agent-1-spec/handoff.md` with:

- Run ID and selected stack.
- Completed files and unfinished files.
- Decisions made and decisions still open.
- Research already performed and remaining research needed.
- Validation checks already passed and checks still pending.
- Exact next prompt for a fresh agent.

Do not continue by squeezing low-quality sections into the end of a thread. A clean handoff is better than a weak canonical spec.

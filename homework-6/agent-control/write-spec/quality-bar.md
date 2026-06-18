# Write Spec Quality Bar

Use this reference when generating, reviewing, comparing, or selecting Homework 6 Athena (Spec Writer) specification runs. It is intentionally stricter than the assignment minimum so downstream code, test, MCP, hook, and documentation work receives executable instructions.

## Required Generated Files

A complete Athena (Spec Writer) run produces these files inside the run folder first:

| Output | Purpose |
|---|---|
| `agent-1-spec/outputs/specification.md` | Canonical candidate Task 1 specification. |
| `agent-1-spec/outputs/docs/domain-rules.md` | Research-backed banking-pipeline domain assumptions and limits. |
| `agent-1-spec/outputs/docs/technical-conventions.md` | Money, IDs, JSON, audit, logging, redaction, and file-protocol conventions. |
| `agent-1-spec/outputs/docs/development-process.md` | Portable execution gates for later Homework Automation Layer agents. |
| `agent-1-spec/research-notes.md` | Query/source log, applied decisions, and fallback notes. |
| `agent-1-spec/validation-checklist.md` | Completed self-review against this quality bar. |
| `agent-1-spec/handoff.md` | Continuity record for later threads or downstream Homework Automation Layer agents. |

Canonical `homework-6/specification.md` is the normal selectable output. After the first successful generation run, copy it automatically only if no canonical spec exists yet. Later runs require explicit selection or overwrite authorization.

Supporting docs are preserved as run evidence unless the operator explicitly selects them as canonical support docs. The standing `homework-6/agents.md` guide is not a per-run output and must not be overwritten by run selection.

## Specification Shape

The generated `specification.md` must include:

1. High-Level Objective: one clear sentence describing the transaction-processing pipeline.
2. Mid-Level Objectives: 4-5 concrete, testable objectives with observable success.
3. Implementation Notes: precise decimal money handling, ISO 4217-style currency validation, audit logging, no plaintext PII logging, JSON file protocol, deterministic prior-output archival for repeated runs, temporary coverage expectation, and MCP-readable result responsibilities.
4. Context: beginning state with `sample-transactions.json`; ending state with processed results in `shared/results/`, archived prior runtime output under zero-padded `archive/shared-001` style folders on repeated runs, a summary report, and a temporary test coverage target of 75%.
5. Low-Level Tasks: implementation-ready transaction-system slices, with enough detail for code generation, tests, documentation, commands, hooks, and MCP-readable result shapes to be generated from the product specification.

The spec should also include edge cases and verification mapping. Use Homework 3's selected `specification.md` as the depth target: rich product behavior, actors or components, state/data concepts, edge cases, failure modes, acceptance criteria, and implementation-ready slices. Do not copy Homework 3's dispute domain.

## Low-Level Task Card Standard

Each low-level task entry must be an implementation-ready transaction-system slice and include:

| Field | Required content |
|---|---|
| `Task` | Product implementation slice, such as project structure, JSON envelope, Transaction Validator, Fraud Detector, Settlement Processor, Integrator, result summary, or dry-run validation seam. |
| `Prompt` | Exact prompt to give Claude Code, Codex, or Copilot. |
| `File to CREATE` or `File to UPDATE` | Stack-specific path. |
| `Function to CREATE` | Stack-specific function, method, or class entry point. |
| `Details` | Inputs, outputs, transformations, file moves, audit behavior, and privacy constraints. |
| `Edge cases` | Invalid amount, invalid currency, missing field, high value, odd timestamp, cross-country data, and failed downstream state. |
| `Acceptance criteria` | Checkable outcome for the task. |
| `Verification` | Exact command, test type, or manual review evidence. |

Task cards must not say "implement appropriate code" without naming target files, functions, and behaviors. The generated spec must be stack-specific after stack selection.

The low-level task section must not be one card per Homework Automation Layer agent. Some slices may correspond to runtime transaction pipeline components, but the section must also cover non-agent software concerns such as:

- Project/package structure.
- Shared JSON envelope and file movement semantics.
- Input loading and deterministic run reset.
- Prior-run archival before creating a fresh shared protocol tree.
- Integrator orchestration.
- Decimal money parsing and serialization.
- Currency and required-field validation.
- Fraud/risk scoring.
- Settlement or final-outcome writing.
- Result summary and audit-safe reporting.
- Redaction and structured audit events.
- Validator dry-run behavior for `/validate-transactions`.
- Test seams and temporary-directory isolation.
- MCP-readable result shapes.
- Error handling, idempotent reruns, and failure recovery.

Runtime transaction pipeline agents must be specified as stack-native application components, not Claude/Codex skills. A valid generated spec should give each runtime agent a responsibility, input contract, decision logic, output contract, audit-log identity, pipeline position, and callable interface for the integrator or dry-run commands.

## Meta-Layer Leakage Rejection

Reject a generated specification when operator-process or homework-automation mechanics appear as transaction-system product requirements.

Blocking leakage includes:

- `dev-doc-harness`, Superpowers freeze gates, planning packages, variance logs, or planning commits.
- Athena (Spec Writer) run folders, run preservation, comparison workflow, final-selection records, or canonical-copy mechanics as product requirements.
- Creating or repairing the `write-spec` skill, `.claude/skills`, `.codex/commands`, slash-command wrappers, or this control package.
- Hook setup, MCP configuration setup, screenshot capture, README author evidence, PR description support, or submission packaging as low-level product slices.
- Greek identity labels used as runtime product components, implementation classes, package names, or executor sub-agent names.
- Runtime transaction pipeline agents implemented as Claude/Codex skills instead of stack-native application components.

Allowed product-level references include result files and summaries that later MCP tools can read, validation seams that later commands can invoke, and coverage expectations that later tests can enforce. The spec should describe those product contracts without instructing Athena (Spec Writer) to build the outer automation surfaces.

Allowed rerun behavior includes product-level archival of prior `shared/` runtime output under zero-padded sibling archive folders. Do not describe Hephaestus code-run inventories, selected-code records, or canonical-copy mechanics as transaction-system product requirements.

## Research and Domain Rules

Research notes must distinguish:

- Cited facts: source or library documentation with URL or Context7 library ID.
- Assignment assumptions: constraints directly from `transaction-system-brief.md` and `sample-transactions.json`.
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
| Task-card executability | Could Hephaestus (Code Generator) implement from the low-level tasks without guessing? |
| Handoff usefulness | Can a fresh thread continue or implement from the artifacts? |

Final selection must record selected run ID, stack, selected files, copied canonical paths, rationale, operator, date, and any post-selection edits.

The default selected file is `agent-1-spec/outputs/specification.md` copied to `homework-6/specification.md`. Copy `agent-1-spec/outputs/docs/*` or `research-notes.md` only when explicitly selected. Never copy a run-local agent guide over `homework-6/agents.md`; update that standing guide separately when its global instructions need to change.

## Built-In Quality Gate

Before selecting a run, confirm:

- `specification.md` contains all five required Task 1 sections.
- The selected stack is either `python` or `java`; omitted input was normalized to `python`.
- The low-level task cards are implementation-ready transaction-system slices, not one entry per Homework Automation Layer agent.
- The code-generation task requires at least three cooperating runtime transaction pipeline agents.
- The generated transaction-system spec requires repeated pipeline runs to archive an existing `shared/` tree to zero-padded sibling archive folders before creating fresh protocol directories.
- Hephaestus (Code Generator) Context7 usage and two-query documentation are explicit.
- Coverage target is the temporary Athena (Spec Writer) ending-context target of 75%; Themis (Test Generator) later owns raising coverage above 80% and adding the blocking coverage hook.
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

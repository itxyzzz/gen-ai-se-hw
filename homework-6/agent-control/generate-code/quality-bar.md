# Generate Code Quality Bar

This quality bar applies to Homework 6 Hephaestus (Code Generator) runs. It keeps Task 2 code generation grounded in the selected Athena (Spec Writer) output and prevents accidental implementation of later homework deliverables.

## Required Product Scope

A successful normal Hephaestus run must create or update Task 2 Generated Transaction System Layer files only.

Required Task 2 outcomes:

- An integrator/orchestrator such as `integrator.py`.
- At least three cooperating runtime transaction pipeline components, including Transaction Validator and Fraud Detector plus Settlement Processor, Compliance Checker, or Reporting Agent.
- JSON file communication through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- Every record from `sample-transactions.json` represented in `shared/results/` after the pipeline runs.
- Canonical `research-notes.md` with at least two Context7 query entries.

Generated runtime components use functional names. Greek Homework Automation Layer labels must not be used for runtime product modules, files, classes, or functions.

## Selected Specification Traceability

Generated code must trace back to `homework-6/specification.md`.

The run's `validation-checklist.md` must map:

- Selected spec objectives to generated files.
- Selected spec low-level tasks to generated implementation slices.
- Required sample transaction outcomes to validation evidence.
- Result-shape decisions to later MCP-readable status needs without adding Task 4 MCP config.

If Hephaestus intentionally skips or narrows a selected-spec task, record the reason in the run handoff. Skipping because the task seems "later" is only valid when the task belongs to Task 3, Task 4 configuration/server work, Task 5 documentation/evidence, or another explicitly out-of-scope deliverable.

## Context7 Documentation

Canonical `homework-6/research-notes.md` must contain at least two Context7 entries.

Each entry must include:

- Search text.
- Returned Context7 library ID.
- Access date.
- Applied insight or code pattern.
- Generated files or implementation decisions influenced.

Run-local `agent-2-code/research-notes.md` should mirror or link to the canonical notes.

If Context7 could not be used, the run is not assignment-complete. Record the limitation and stop for operator direction instead of claiming the Context7 requirement is satisfied.

## Sub-Agent Evidence

Hephaestus may use executor sub-agents up to the Homework 6 `agents.max_threads = 8` cap without additional operator approval.

The run evidence must record planned and actual sub-agent use:

- Roles or scopes handled.
- Context strategy: `curated prompt`, `curated artifacts`, `full-history fork`, or `no repo context`.
- Input context.
- Output artifact.
- Model policy and policy-relative model class/profile.
- Reasoning effort.
- Parallelism or wave order.
- Blast radius if wrong.
- Whether the orchestrator changed fan-out to preserve quality.

Every sub-agent report must include assigned scope, files inspected or changed, commands/tests run, assumptions, uncertainty or residual risk, and recommended next step.

If no sub-agents are used, the run must justify that choice through quality concerns, runtime unavailability, or tight coupling. Missing operator reapproval is not a valid reason.

The orchestration thread owns final integration and must report de-facto sub-agent use in the completion handoff.

## Python Stack Requirements

For the current selected Python spec, generated code must satisfy these checks:

- Monetary values are parsed from strings into `decimal.Decimal`; binary floating point is not used for transaction amounts, thresholds, summaries, or tests.
- Money is serialized back to JSON as strings.
- Currency validation accepts at least `USD`, `EUR`, and `GBP`, and rejects unsupported sample currency `XYZ`.
- JSON writing uses safe strict serialization such as `allow_nan=False`.
- Runtime files are written deterministically enough for reruns and tests.
- Audit events include timestamp, runtime component name, transaction ID, safe outcome, and stable reason code when applicable.
- Account identifiers are redacted. Raw descriptions and raw account IDs do not appear in logs, result files, audit files, tests, or screenshots.
- Risk scoring is deterministic educational logic, not real fraud, AML, sanctions, payment-network, banking, or legal compliance.

Expected sample outcomes from the selected spec:

- `TXN006` rejects with `UNSUPPORTED_CURRENCY`.
- `TXN007` rejects with `NON_POSITIVE_AMOUNT`.
- `TXN002` receives a high-value review signal.
- `TXN005` receives a very-high-value review signal.
- `TXN004` receives an odd-hour review signal.
- At least one low-risk transaction settles with a simulated settlement reference.

## Privacy And Safety Rejection Checks

Reject or repair generated output if it includes:

- Plaintext full account IDs such as raw `ACC-1001`.
- Raw transaction descriptions in logs, audit events, result files, or screenshots.
- Credentials, tokens, authorization headers, secrets, or unrelated environment dumps.
- Unfiltered metadata dumps that could expose PII.
- Claims of real banking, AML, sanctions, KYC, payment-network, PCI, legal, regulatory, or production compliance.
- Network calls, real payment movement, real identity screening, sanctions list lookup, or payment-network integration.

## Scope Rejection Checks

A normal Task 2 Hephaestus run must not add:

- `.claude/commands/run-pipeline.md`, `.claude/commands/validate-transactions.md`, Codex command equivalents, or other Task 3 command surfaces.
- Coverage gate hooks, push hooks, or enforced coverage gate configuration.
- `mcp/server.py` as a custom FastMCP server unless the operator explicitly changes the scope after Task 2. Pure result helper functions are allowed only when the selected spec requires MCP-readable result shapes and no MCP config is changed.
- `pipeline-status` entries in `mcp.json` or `.codex/config.toml`.
- Final `README.md`, `HOWTORUN.md`, screenshots, or PR description support.
- `dev-doc-harness`, Superpowers, freeze gates, plan artifacts, run-selection machinery, or Greek automation labels inside generated product code.

## Validation Requirements

Before reporting completion, Hephaestus must record validation evidence in `agent-2-code/validation-checklist.md`.

Minimum checks:

- Pipeline command, normally `python integrator.py`, exits successfully.
- All sample transactions are represented in `shared/results/`.
- `shared/results/summary.json` accounts for all sample records.
- Context7 notes are present in canonical `research-notes.md`.
- `mcp.json` and `.codex/config.toml` remain unchanged for Task 2.
- Available tests pass, or blockers are recorded with exact command output and next action.
- Privacy scans or focused review confirm no raw sensitive account identifiers or descriptions leak into generated outputs.

If any required check cannot run, record the blocker, reason, and exact follow-up in the run handoff.

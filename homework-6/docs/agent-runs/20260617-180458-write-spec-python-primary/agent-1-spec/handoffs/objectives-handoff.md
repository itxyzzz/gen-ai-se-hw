# Objectives Handoff

Run ID: `20260617-180458-write-spec-python-primary`
Stack: `python`
Sub-agent role: Objectives architect

## Scope Assigned

Shape the objective layer for a Python-specific `specification.md` that downstream agents can implement, test, expose through MCP, and document without guessing.

Inputs used:

- Required Task 1 sections: High-Level Objective, Mid-Level Objectives, Implementation Notes, Context, Low-Level Tasks.
- Python stack constraints: `decimal.Decimal`, `integrator.py`, `agents/`, `process_message(message: dict) -> dict`, `pytest`, `pytest-cov`, `python integrator.py`, and `mcp/server.py`.
- Required JSON file protocol directories: `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- Required downstream Agent 2, Agent 3, and Agent 4 responsibilities.
- Sample edge cases: invalid currency `XYZ`, negative amount `-100.00`, high-value amounts `25000.00` and `75000.00`, odd-hour timestamp `02:47:00Z`, and country metadata `US`, `DE`, and `GB`.
- Domain decisions: ISO-style currency allowlist, Decimal parsing from strings, structured audit events, redacted account identifiers, deterministic risk heuristics, and educational simulation boundary.

## High-Level Objective

Build a Python educational transaction-processing pipeline that reads `sample-transactions.json`, routes each transaction through cooperating validator, fraud-detection, and settlement or reporting agents using JSON files, and produces validated results, audit-safe summaries, tests, coverage evidence, MCP status access, and runnable documentation.

## Mid-Level Objectives

| ID | Objective | Observable success |
|---|---|---|
| M1 | Implement a deterministic Python pipeline orchestrated by `integrator.py` that moves transaction messages through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`. | Running `python integrator.py` creates or refreshes the shared protocol directories, processes every sample transaction once, and writes final result and summary JSON files under `shared/results/`. |
| M2 | Build at least three cooperating agent modules under `agents/` with a shared `process_message(message: dict) -> dict` contract. | Agent 2 can create `agents/transaction_validator.py`, `agents/fraud_detector.py`, and `agents/settlement_processor.py` or `agents/reporting_agent.py`; each module accepts and returns dictionaries with transaction ID, status, reason codes, audit metadata, and next-stage routing fields. |
| M3 | Enforce safe financial and validation behavior using `decimal.Decimal`, string-based amount parsing, ISO-style currency validation, deterministic risk scoring, and privacy-preserving audit events. | Tests prove that `XYZ` currency and `-100.00` are rejected, high-value transactions `25000.00` and `75000.00` are flagged or routed according to documented thresholds, odd-hour timestamp `02:47:00Z` contributes to risk scoring, and account IDs are redacted in logs and audit examples. |
| M4 | Provide a complete verification and automation surface with `pytest`, `pytest-cov`, `/run-pipeline`, `/validate-transactions`, and a coverage hook. | Agent 3 can add unit and integration tests, commands run successfully with `python -m pytest`, the hook blocks coverage below 80%, and the final documented target is at least 90% coverage. |
| M5 | Prepare handoff-ready operational surfaces: Context7 research notes, MCP status server, and final user documentation. | Agent 2 documents at least two Context7 queries in `research-notes.md`, adds `mcp/server.py` before `pipeline-status` MCP configuration, and Agent 4 can produce README, HOWTORUN, architecture/testing docs, screenshots, PR support, and a README that includes the student's name. |

## Boundary Notes

- Simulation boundary: frame the system as an educational banking-pipeline simulation only. Do not claim AML, sanctions, payment-network, banking regulatory, legal, or production compliance coverage.
- Privacy boundary: treat account identifiers, transaction descriptions, metadata, and audit records as sensitive. Logs, audit examples, reports, screenshots, and docs must avoid plaintext account IDs and use redaction such as `ACC-****1001`.
- File protocol boundary: agent communication must be file-based JSON through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- Money boundary: amounts must be parsed into `decimal.Decimal` from strings and never processed with binary floating point; invalid or malformed amounts must produce safe rejection results with reason codes.
- Currency boundary: use an ISO 4217-style allowlist sufficient for the assignment samples, while rejecting unsupported sample value `XYZ`.
- Risk boundary: use deterministic, explainable heuristics rather than opaque ML or external scoring services.
- Coverage boundary: the blocking hook threshold is 80%; the final quality target documented in the ending context should be at least 90%.
- MCP boundary: `mcp/server.py` is required for the custom `pipeline-status` MCP server. Add `pipeline-status` to `mcp.json` and `.codex/config.toml` only after `mcp/server.py` exists.
- Context7 boundary: Agent 2 must use Context7 during code generation and record at least two queries with query text, returned library ID, access date, and applied insight in `research-notes.md`.
- Documentation boundary: Agent 4 owns README, HOWTORUN, architecture/testing docs, screenshots, and PR support. README must include the student's name, but the specification should leave the actual name as a downstream fill-in requirement if it is not available during Agent 1.

## Residual Risks Or Tradeoffs

- The exact supported currency allowlist should be small and assignment-focused unless sample data or docs require more; a broad pretend-ISO list may add noise without value.
- Risk thresholds such as `25000.00` and `75000.00` need to be deterministic and documented, but they are design heuristics for the assignment.
- The choice between `settlement_processor.py` and `reporting_agent.py` should be resolved in low-level task cards. Settlement processing better fits the required pipeline flow, while reporting can still be part of the integrator summary.
- The README student-name requirement cannot be fully satisfied by Agent 1 unless the name is known; the spec should instruct Agent 4 to insert the correct student name.
- MCP configuration depends on file existence order. The spec should make this sequencing explicit so Agent 2 does not configure a missing server target.

## Recommended Next Step

Use these objectives as the top-level acceptance map for `specification.md`, then hand them to the low-level task decomposition sub-agent for executable task cards tied to objectives M1 through M5.

# Source Context

## Direct Product Input

The product is a Python transaction-processing system that reads `sample-transactions.json`, processes every synthetic transaction through cooperating stack-native runtime components, and writes final safe outcomes under `shared/results/`.

The direct product input is `agent-control/write-spec/transaction-system-brief.md`. `TASKS.md` was read only as assignment background and does not override the product-only boundary for this Athena (Spec Writer) run.

## Required Product Boundary

- Specify the Generated Transaction System Layer only.
- Runtime components are Python modules/classes or functions, not Claude/Codex skills and not Homework Automation Layer agents.
- Do not make Hera (Orchestrator), Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), or Clio (Documentation Generator) runtime product components.
- Do not include dev-doc-harness, Superpowers, planning freeze gates, canonical-copy mechanics, preserved run folders, screenshots, PR packaging, or MCP configuration setup as transaction-system low-level product tasks.
- It is allowed to specify product result files and safe result shapes that a later read-only MCP status server can consume.

## Stack Context

Selected stack: `python`.

The stack profile requires:

- `decimal.Decimal` for money; never `float`.
- Standard `json` module plus typed dictionaries or dataclasses as useful.
- `integrator.py` with `main()`.
- Runtime component modules such as `agents/transaction_validator.py`, `agents/fraud_detector.py`, `agents/settlement_processor.py`, and `agents/reporting_agent.py`.
- Common runtime component callable `process_message(message: dict) -> dict`.
- JSON file protocol through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- `pytest` with temporary-directory isolation.
- Temporary Athena ending-context coverage target of 75%; Themis later owns raising coverage above 80% and blocking hooks.
- Commands including `python integrator.py`, `python -m pytest`, and a non-blocking coverage report command.
- Python FastMCP reader later at `mcp/server.py`, reading product result files after that file exists.

## Safe Sample Data Summary

`sample-transactions.json` contains 8 synthetic transaction records.

Observed structural coverage:

- Currencies: `USD`, `EUR`, `GBP`, and unsupported sample value `XYZ`.
- Transaction types: 5 transfers, 2 wire transfers, and 1 refund.
- Channels: online, branch, API, and mobile.
- Countries: US, DE, and GB.
- Validation and risk cases by transaction ID:
  - Unsupported currency: `TXN006`.
  - Non-positive amount: `TXN007`.
  - High-value transactions: `TXN002`, `TXN005`.
  - Early-hour activity: `TXN004`.

No raw account identifiers, descriptions, or full metadata payloads are copied here.

## Current Canonical Records Used For Protected Context

- Current canonical set ID: `python-canonical-20260621`.
- Current selected Athena (Spec Writer) run: `20260619-170102-write-spec-python-fresh`.
- Current selected Hephaestus (Code Generator) run: `20260619-175211-generate-code-python-fresh-spec`.
- Current selected Themis (Test Generator) run: `20260620-144025-generate-tests-python-fresh-spec`.
- Current selected Clio (Documentation Generator) run: `20260621-011348-generate-docs-python-review-repair`.
- Current canonical spec SHA-256 supplied by parent: `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B`.

These records are comparison and traceability context only. This run does not edit `docs/agent-runs/final-selection.md`, `docs/agent-runs/selection-sets.json`, or canonical files.

## Assignment Requirements That Affect The Product Spec

- At least three cooperating runtime transaction pipeline components are required by the assignment; the current refreshed quality bar requires at least four, with Reporting Agent as the default fourth component.
- Runtime components communicate through JSON files in `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- Every sample transaction must be accounted for in final results.
- Rejected transactions include safe reason fields.
- Logs and audit outputs include ISO 8601 timestamps, runtime component name, transaction ID, safe outcome, and reason code when applicable.
- Account identifiers, descriptions, transaction metadata, and audit details are sensitive and must not be logged in plaintext.
- The system is an educational simulation, not legal, banking, AML, sanctions, payment-network, or real settlement compliance software.

## Template And Reference Context

`specification-TEMPLATE-hint.md` is missing. The run uses:

- Task 1's required sections: high-level objective, 4-5 mid-level objectives, implementation notes, context, and low-level tasks.
- Homework 3 reference package as a depth and structure example only, not as a source for Homework 6 domain claims.
- Homework 3 technical conventions for general documentation discipline around identifiers, timestamps, money, audit metadata, logging, redaction, state/idempotency, and errors.


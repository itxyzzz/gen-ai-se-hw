# Source Context

## Assignment Boundary

Athena (Spec Writer) generates a detailed product specification for the Generated Transaction System Layer only. The output must describe a Python educational banking transaction-processing pipeline that reads `sample-transactions.json`, routes every transaction through stack-native runtime components, and writes audit-safe results under `shared/results/`.

The output must not turn Homework Automation Layer responsibilities into product low-level tasks. The generated pipeline must not implement Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), Clio (Documentation Generator), dev-doc-harness, Superpowers, run preservation, final-selection mechanics, screenshot capture, PR packaging, or command/MCP configuration setup as product features.

## Product Inputs

The product starts with eight synthetic sample transactions:

| Transaction | Key behavior needed |
|---|---|
| `TXN001` | Ordinary USD online transfer for `1500.00`; expected settled path. |
| `TXN002` | High-value USD branch wire for `25000.00`; expected review signal. |
| `TXN003` | USD online transfer for `9999.99`; below high-value threshold. |
| `TXN004` | EUR API transfer at `02:47:00Z` from `DE`; expected odd-hour/contextual signal. |
| `TXN005` | Very-high-value USD branch wire for `75000.00`; expected review signal. |
| `TXN006` | Unsupported `XYZ` currency; expected validation rejection. |
| `TXN007` | Negative GBP amount `-100.00`; expected validation rejection. |
| `TXN008` | Ordinary USD mobile transfer for `3200.00`; expected settled path. |

## Required Runtime Components

At least three cooperating stack-native runtime transaction pipeline agents are required:

- Transaction Validator: required fields, `decimal.Decimal` amount parsing, positive amount checks, and ISO 4217-style currency validation.
- Fraud Detector: deterministic educational risk scoring from high value, odd hour, channel, country, transaction type, and invalid upstream state.
- Settlement Processor or equivalent final-stage component: finalizes settled, rejected, or review-required outcomes and writes safe summaries.

An `integrator.py` orchestrator must reset directories deterministically, load samples, create JSON envelopes, move/read/write files through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`, and verify every input has a final result.

## Stack Profile

The selected stack is `python`.

Required concrete defaults:

- Money: `decimal.Decimal`; never `float`.
- JSON: Python standard `json` module with typed dictionaries or dataclasses where useful.
- Entry point: `integrator.py` with `main()`.
- Common callable interface: `process_message(message: dict) -> dict`.
- Tests: `pytest` with temporary directories.
- Coverage: Athena temporary ending-context target is 75%; Themis later raises above 80% and adds the blocking coverage hook.
- Commands named in spec: `python integrator.py`, `python -m pytest`, and `python -m pytest --cov=.` or equivalent non-blocking coverage report.
- Future MCP result shape: Python FastMCP server at `mcp/server.py` reads existing JSON result files.

## Privacy And Audit Rules

- Treat account IDs, descriptions, transaction metadata, and audit details as sensitive.
- Logs, audit records, result summaries, docs, examples, and screenshots must not expose plaintext account IDs or raw descriptions.
- Use redacted examples such as `ACC-****1001`.
- Audit records should include timestamp, runtime component name, transaction ID, outcome, reason code, and redacted references only when useful.
- The product is an educational simulation. It must not claim legal, banking, AML, sanctions, KYC, payment-network, or regulatory compliance.

## Quality Bar

The generated `specification.md` must include:

1. High-Level Objective.
2. Four to five Mid-Level Objectives.
3. Implementation Notes.
4. Beginning and Ending Context.
5. Implementation-ready Low-Level Tasks.

Low-level task cards must name concrete Python files, functions, behavior, edge cases, acceptance criteria, and verification commands. They must be product implementation slices, not one card per Homework Automation Layer agent.

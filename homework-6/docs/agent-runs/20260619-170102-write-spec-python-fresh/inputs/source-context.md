# Source Context

This run creates a new Python Athena (Spec Writer) candidate for the Homework 6 transaction-processing system. The canonical selected spec already exists, so this package is preserved evidence and a comparison candidate unless later selected by the operator.

## Assignment Product Brief

The product is an educational banking transaction-processing pipeline. It starts from `sample-transactions.json`, processes each synthetic transaction through at least three cooperating runtime transaction pipeline agents, and writes final outcomes under `shared/results/`.

The generated system must include:

- A Python integrator that prepares directories, loads input, orchestrates runtime components, writes result files, and summarizes the run.
- Runtime transaction pipeline agents implemented as Python modules or classes, not Claude/Codex skills.
- JSON message file movement through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- Precise money handling with `decimal.Decimal`.
- ISO 4217-style currency validation with sample-supported currencies and rejection for `XYZ`.
- Audit-safe records with timestamp, runtime component name, transaction ID, safe outcome, and reason code when applicable.
- Redaction for account identifiers and avoidance of raw descriptions or unfiltered metadata in logs, audit, results, docs, and examples.
- Deterministic repeated-run behavior: archive an existing `shared/` tree beside the fresh run as `archive/shared-001`, `archive/shared-002`, and so on.
- Runtime provenance at `shared/run-provenance.json` containing non-sensitive source spec and selected code-package traceability fields.

## Sample Transactions

The sample file contains eight synthetic records:

| Transaction | Relevant fixture behavior |
|---|---|
| `TXN001` | Ordinary USD online transfer for `1500.00`. |
| `TXN002` | USD branch wire transfer for `25000.00`, expected high-value review signal. |
| `TXN003` | USD online transfer for `9999.99`, near but below the high-value threshold. |
| `TXN004` | EUR API transfer at `02:47:00Z` from country `DE`, expected odd-hour and remote/cross-country-style review signals. |
| `TXN005` | USD branch wire transfer for `75000.00`, expected very-high-value review signal. |
| `TXN006` | Unsupported currency `XYZ`, expected rejection. |
| `TXN007` | Negative GBP amount `-100.00`, expected rejection. |
| `TXN008` | USD mobile transfer for `3200.00`, expected valid processing with any documented mobile-channel signal. |

## Python Stack Profile

Use the fixed `python` profile:

- Money: `decimal.Decimal`; never `float` for amounts.
- JSON: standard `json` module with explicit Decimal-to-string serialization.
- Pipeline entry: `integrator.py` with `main()`.
- Agent modules: `agents/transaction_validator.py`, `agents/fraud_detector.py`, and `agents/settlement_processor.py` or another final-stage module.
- Common interface: `process_message(message: dict) -> dict`.
- Tests: `pytest` with temporary filesystem isolation.
- Coverage: temporary Athena ending-context target of 75%; later Themis (Test Generator) owns the >80% blocking coverage hook.
- MCP: future Python FastMCP server at `mcp/server.py`, but this spec should only define stable result shapes and not modify MCP configuration.

## Homework 3 Reference Usage

Homework 3 is used only as a quality and shape reference. Useful patterns include:

- Clear high-level objective and scope boundary.
- Four or five mid-level objectives with observable success.
- Concrete implementation notes before low-level tasks.
- Rich low-level task cards with prompts, file targets, behavior, edge cases, acceptance criteria, and verification.
- Explicit edge cases, verification mapping, privacy/audit guidance, and performance/operability assumptions.

Homework 3's dispute intake domain, actors, legal framing, and state machine are not copied into this Homework 6 transaction-processing product spec.

## Candidate Output Boundary

The run should produce candidate files under:

- `agent-1-spec/outputs/specification.md`
- `agent-1-spec/outputs/docs/domain-rules.md`
- `agent-1-spec/outputs/docs/technical-conventions.md`
- `agent-1-spec/outputs/docs/development-process.md`
- `agent-1-spec/research-notes.md`
- `agent-1-spec/validation-checklist.md`
- `agent-1-spec/handoff.md`

Canonical files remain untouched in this generate run because a canonical selected specification already exists.


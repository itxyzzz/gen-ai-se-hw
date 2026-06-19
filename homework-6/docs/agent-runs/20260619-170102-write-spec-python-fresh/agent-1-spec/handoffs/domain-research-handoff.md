# Domain Research Handoff

## Assigned Scope

Produce concise, evidence-backed domain research for run `20260619-170102-write-spec-python-fresh`, focused on a Python educational banking transaction-processing pipeline specification.

Scope covered:

- Validation, deterministic risk scoring, settlement simulation, and safe result summaries.
- Python `decimal.Decimal` money handling and why binary floating point is unsuitable.
- ISO 4217-style currency validation for sample currencies `USD`, `EUR`, `GBP`, and invalid `XYZ`.
- Privacy, redaction, audit, logging, and provenance constraints.
- Boundaries against unsupported real-world compliance claims.
- Result-shape implications for future read-only MCP status tools.

No canonical files were edited.

## Context Inspected

Local repository context inspected:

- `agent-control/write-spec/transaction-system-brief.md`
- `sample-transactions.json`
- `agents.md`
- `agent-control/write-spec/workflow.md`
- `agent-control/write-spec/stack-profiles.md`
- `agent-control/write-spec/quality-bar.md`
- `agent-control/write-spec/run-registry.md`
- Repository-level `AGENTS.md`
- Repository-level `HOMEWORK_STANDARDS.md`
- Repository-level `README.md`
- Existing run handoff folder for `docs/agent-runs/20260619-170102-write-spec-python-fresh/agent-1-spec/handoffs/`

Sample data confirmed eight synthetic transactions:

- `TXN001`: normal USD online transfer for `1500.00`.
- `TXN002`: USD branch wire transfer for `25000.00`.
- `TXN003`: USD online transfer for `9999.99`.
- `TXN004`: EUR API transfer from DE at `02:47:00Z`.
- `TXN005`: USD branch wire transfer for `75000.00`.
- `TXN006`: invalid `XYZ` currency.
- `TXN007`: negative GBP amount `-100.00`.
- `TXN008`: USD mobile transfer for `3200.00`.

## Sources Or Commands Used

Commands used:

- `Get-Content -Raw ...\agent-control\write-spec\transaction-system-brief.md`
- `Get-Content -Raw ...\sample-transactions.json`
- `Get-Content -Raw ...\agents.md`
- `Get-Content -Raw ...\agent-control\write-spec\workflow.md`
- `Get-Content -Raw ...\agent-control\write-spec\stack-profiles.md`
- `Get-Content -Raw ...\agent-control\write-spec\quality-bar.md`
- `Get-Content -Raw ...\agent-control\write-spec\run-registry.md`
- `Get-Content -Raw ...\AGENTS.md`
- `Get-Content -Raw ...\HOMEWORK_STANDARDS.md`
- `Get-Content -Raw ...\README.md`
- `Get-ChildItem -Name ...\docs\agent-runs\20260619-170102-write-spec-python-fresh\agent-1-spec\handoffs`
- `git status --short`; this showed the run folder as untracked and permission warnings for `.pytest_cache`, with no edits made by this task.

External sources used, accessed 2026-06-19:

- [Python `decimal` documentation](https://docs.python.org/3/library/decimal.html): supports exact decimal arithmetic, warns about binary floating-point representation issues, notes accounting suitability, and documents `Decimal` construction and `quantize`.
- Context7 `/python/cpython` query: `Python standard library decimal module Decimal exact decimal arithmetic avoid binary float for money`; returned CPython documentation excerpts for exact decimal arithmetic, monetary fixed places, and `quantize`.
- [ISO 4217 currency codes](https://www.iso.org/iso-4217-currency-codes.html): establishes internationally recognized alphabetic and numeric currency codes; alphabetic codes are three-letter codes.
- [OWASP Logging Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Logging_Cheat_Sheet.html): supports excluding or masking sensitive personal data and bank account/payment data in logs, sanitizing event data, and testing logging behavior.

## Findings

The product must remain an educational simulation. It can model validation, risk scoring, audit-safe summaries, and final status decisions, but it must not claim to perform real AML, sanctions screening, KYC, payment-network compliance, legal compliance, regulatory compliance, settlement finality, or production fraud detection.

Use `decimal.Decimal` for all amounts. Python documentation supports decimal arithmetic for exact decimal representation and accounting-style invariants, while binary floating point can produce representation and equality surprises. The spec should require parsing amounts from strings into `Decimal`, never from `float`, and serializing amounts back to JSON as strings.

For the sample domain, currency validation should be two-stage: first check ISO 4217-style shape, then enforce a project allowlist. `USD`, `EUR`, and `GBP` should be accepted as supported sample currencies. `XYZ` should be rejected even though it is three uppercase letters, because it is not in the supported assignment allowlist.

The Transaction Validator should reject records with missing required fields, malformed JSON, malformed amount strings, non-finite decimals, zero or negative amounts, unsupported currencies, invalid timestamp shape, or missing transaction IDs. `TXN006` should be rejected for `UNSUPPORTED_CURRENCY`; `TXN007` should be rejected for `NON_POSITIVE_AMOUNT`.

Risk scoring should be deterministic and explicitly educational. Good heuristic signals include high amount, very high amount, wire transfer type, API/mobile/online channel, early-hours timestamp, non-US country, and near-threshold values. The risk component should produce `risk_score`, `risk_level`, and reason codes, not assert that fraud exists.

Settlement should be a simulation. It should not move money, call external systems, perform real account balance changes, or represent payment-network finality. It should convert validated low-risk transactions into settled final results, route elevated-risk transactions to `review_required`, and preserve rejected validation outcomes.

Audit and logs must be structured and minimal. Account identifiers, raw descriptions, and raw metadata should not appear in logs, audit events, result summaries, examples, or docs. Use transaction IDs, component names, status, reason codes, safe derived risk flags, and redacted account examples such as `ACC-****1001` only when an account reference is necessary.

Each audit event should include at least: timestamp, runtime component name, transaction ID, safe outcome, and reason code where applicable. Events should be line-oriented JSON or equivalent structured records so they can be tested without scraping prose.

Repeated runs must archive any existing `shared/` tree to `archive/shared-001`, `archive/shared-002`, etc., then create a fresh `shared/input`, `shared/processing`, `shared/output`, and `shared/results` tree. `shared/run-provenance.json` should contain only non-sensitive run IDs, canonical paths, timestamps, and fingerprints.

Future read-only MCP status tools need stable result shapes, but this spec should define only product result files, not MCP configuration mechanics. Result files should be safe to expose through `get_transaction_status`, `list_pipeline_results`, and `pipeline://summary`.

## Applied Decisions For The Spec

Use these product decisions in the Python specification:

- Runtime components are Python modules/classes with a common callable interface such as `process_message(message: dict) -> dict`.
- Core components should be Transaction Validator, Fraud Detector, Settlement Processor, and an Integrator.
- Money is parsed with `Decimal(str_value)` from JSON strings only.
- Amounts for `USD`, `EUR`, and `GBP` are quantized or validated to two fractional digits for this sample scope.
- JSON outputs serialize monetary values as strings, not numbers.
- Supported currencies are `{"USD": 2, "EUR": 2, "GBP": 2}` for this homework run.
- Validation failures produce `status: "rejected"` with stable reason codes.
- Risk outcomes use `settled` or `review_required` after validation and settlement simulation, not real approval or fraud determinations.
- Settlement results are simulated final records, not real banking settlement.
- Result files should omit `source_account`, `destination_account`, raw `description`, and raw `metadata`.
- If account evidence is necessary in docs or logs, show only redacted form.
- Write one safe per-transaction result file under `shared/results/<transaction_id>.json` and one summary file under `shared/results/summary.json`, matching the existing Homework 6 result-shape convention.
- Include MCP-readable fields: `transaction_id`, `status`, `reason_codes`, `risk_score`, `risk_level`, `amount`, `currency`, `component_history`, `processed_at`, and `safe_summary`.
- Include summary counts: total, settled, rejected, review_required, error, and run_id.
- Keep `shared/run-provenance.json` separate from transaction results and free of raw transaction data.

## Assumptions

- The sample data is synthetic and intentionally includes invalid cases.
- The spec should not perform foreign exchange conversion.
- The sample currencies use two decimal places for the homework scope.
- Transaction IDs are acceptable as synthetic correlation identifiers.
- Raw account IDs, descriptions, and metadata are sensitive even when synthetic.
- Timestamps are treated as UTC ISO 8601-style strings ending in `Z`.
- Risk thresholds are assignment design choices, not industry rules.
- Future MCP tooling will be read-only over result files already produced by the pipeline.

## Uncertainty And Residual Risks

- ISO 4217 evolves over time, but this homework should use a fixed local allowlist for deterministic tests.
- Any chosen risk threshold can overfit the eight sample records; the spec should document thresholds as educational heuristics.
- Redaction rules must be tested because accidental logging of raw input records is the main privacy risk.
- Decimal handling can still be weakened if code converts to `float` during JSON parsing, comparisons, summaries, or tests.
- Result shapes need to be stable enough for future MCP tools, but the actual MCP server mechanics should remain out of this spec.
- The sources support engineering choices, not real compliance assertions.

## Recommended Next Step

Integrate these findings into `agent-1-spec/research-notes.md`, then use them to draft the domain rules, implementation notes, and low-level task cards for validation, risk scoring, settlement simulation, audit logging, run archival, provenance, and MCP-readable result shapes.


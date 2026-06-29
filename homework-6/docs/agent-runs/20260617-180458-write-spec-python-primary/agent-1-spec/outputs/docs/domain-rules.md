# Domain Rules

Run ID: `20260617-180458-write-spec-python-primary`
Stack: `python`

## Status

These rules define the domain-control baseline for the Homework 6 simulated multi-agent banking transaction pipeline. They are engineering assumptions for an educational capstone. They are not legal, banking, AML, sanctions, payment-network, KYC, or regulatory-compliance advice.

## Research Basis

The rules are informed by:

- ISO 4217 and SIX currency-code material for ISO-style currency-code design.
- Python `decimal` documentation for exact decimal money handling.
- Python logging, NIST log-management guidance, and OWASP logging guidance for structured, minimal, redacted audit records.
- Homework 6 local assignment files and sample transactions.

These sources support conservative engineering behavior: precise amounts, explicit validation, deterministic status/reason codes, redacted audit records, and scoped simulation language.

## Pipeline Scope

The pipeline processes synthetic transactions from `sample-transactions.json` through a file-based JSON agent protocol. It must:

- Validate required fields, positive money amounts, and supported currency codes.
- Score eligible transactions with deterministic risk-review heuristics.
- Settle or report final outcomes for every transaction.
- Write final per-transaction results and a summary report under `shared/results/`.
- Preserve audit events without plaintext account identifiers or raw sensitive descriptions.

The pipeline does not perform real banking operations, customer notifications, ledger posting, card-network handling, sanctions screening, AML investigation, KYC review, suspicious activity reporting, legal determinations, or payment-network compliance.

## Currency Rules

- Currency values must be strings.
- Currency values must be uppercase three-letter codes.
- Validation must check membership in a supported ISO 4217-style allowlist, not only a regular expression.
- The homework allowlist must include at least `USD`, `EUR`, `GBP`, and `JPY`.
- `XYZ` from `TXN006` must be rejected with a stable reason code such as `unsupported_currency`.
- A future production system would use maintained ISO 4217 data rather than a fixed homework allowlist.

## Money Rules

- Amounts must remain strings in JSON input and output.
- Python code must parse amounts directly into `decimal.Decimal`.
- The pipeline must never parse or compute transaction amounts using binary floating point.
- Negative, zero, malformed, missing, infinite, or NaN-like amounts must be rejected by the validator.
- `TXN007` must be rejected because `-100.00` is negative.
- Output amounts should be serialized as strings to preserve exact decimal representation.

## Risk-Review Heuristics

Risk scoring is deterministic homework logic, not a fraud, AML, sanctions, or legal conclusion.

Recommended baseline signals:

- Amount greater than `10000.00`: high-value signal.
- Timestamp hour from `00:00` through `04:59` UTC: odd-hour signal.
- Country or channel outside the default expected path for the sample: contextual signal.
- Wire transfers above the threshold: additional review signal.

Expected sample behavior:

- `TXN002` and `TXN005` should receive high-value risk-review signals.
- `TXN004` should receive an odd-hour signal.
- `TXN003` should remain below a strict `> 10000.00` high-value threshold.
- Rejected transactions should not continue to settlement.

## Audit And Privacy Rules

Audit records should include:

- ISO 8601 timestamp.
- Agent name.
- Transaction ID.
- Safe outcome such as `accepted`, `rejected`, `flagged_for_review`, or `settled`.
- Stable reason code when useful.
- Optional redacted account reference.

Audit records, logs, errors, screenshots, and examples must not include:

- Plaintext source or destination account identifiers.
- Full transaction descriptions.
- Raw metadata dumps.
- Secrets, tokens, credentials, authorization headers, or real customer information.

Use redacted examples such as `ACC-****1001` when an account-like value is needed for explanation.

## Shared Protocol Rules

Agents exchange JSON messages through:

```text
shared/input
shared/processing
shared/output
shared/results
```

Messages should include a unique `message_id`, ISO 8601 timestamp, `source_agent`, `target_agent`, `message_type`, and `data`. Each agent should move or write only valid JSON and should preserve enough status/reason information for the next agent and final report.

## Unsupported Claims

Generated docs, logs, screenshots, and code comments must not claim:

- Legal compliance.
- AML compliance.
- Sanctions compliance.
- Payment-network compliance.
- Bank regulatory compliance.
- Real fraud determination.
- Customer reimbursement, refund execution, chargeback processing, or ledger settlement.

Use "risk review," "heuristic signal," "simulation," and "educational pipeline" instead.

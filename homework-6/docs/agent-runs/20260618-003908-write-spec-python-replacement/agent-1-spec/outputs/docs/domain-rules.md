# Domain Rules

## Status

This document records domain assumptions for the Homework 6 generated transaction-processing system. The system is an educational simulation. It is not legal, banking, AML, sanctions, KYC, payment-network, PCI, or regulatory compliance software.

## Product Scope

The generated product is a local Python transaction-processing pipeline that reads synthetic records from `sample-transactions.json`, sends JSON message files through cooperating runtime components, and writes audit-safe results under `shared/results/`.

In scope:

- Required-field and amount validation.
- ISO 4217-style currency-code validation for the sample-supported currencies.
- Deterministic educational risk scoring.
- Final transaction outcomes of `settled`, `rejected`, `review_required`, or `error`.
- Structured, redacted audit events.
- MCP-readable result files for future status tools.

Out of scope:

- Real settlement or funds movement.
- Customer identity verification.
- AML, sanctions, KYC, fraud-model, PCI, legal, or bank-regulatory compliance.
- Payment-network authorization, chargeback, dispute, refund, reversal, or clearing workflows.
- Real customer data, real accounts, production logs, credentials, secrets, or third-party integrations.

## Sample-Data Rules

The eight records in `sample-transactions.json` are synthetic. The spec should treat the following behaviors as required sample coverage:

| Transaction | Domain expectation |
|---|---|
| `TXN001` | Ordinary USD transfer that can settle when validation passes. |
| `TXN002` | High-value USD wire transfer that receives a review signal. |
| `TXN003` | USD transfer below high-value threshold that can settle when validation passes. |
| `TXN004` | EUR transfer at `02:47:00Z` that receives an odd-hour review signal. |
| `TXN005` | Very-high-value USD wire transfer that receives a stronger review signal. |
| `TXN006` | Unsupported currency `XYZ`; reject with a safe reason code. |
| `TXN007` | Negative amount `-100.00`; reject with a safe reason code. |
| `TXN008` | Ordinary USD mobile transfer that can settle when validation passes. |

## Currency Assumptions

Currency validation is ISO 4217-style, meaning the product validates three-letter uppercase alphabetic currency codes against an explicit allowlist. For the homework sample, the allowlist must include at least:

- `USD`
- `EUR`
- `GBP`

The implementation may add `JPY` or a broader documented table, but it must still reject `XYZ`. A broad table should identify its source or be documented as a static homework allowlist. Currency validation does not imply that a transaction is legal, compliant, payable, or eligible for real settlement.

## Money Assumptions

Amounts arrive as JSON strings and must be parsed into Python `decimal.Decimal` at the validation boundary. The pipeline must reject malformed, missing, non-finite, zero, and negative amounts. Amounts written back to JSON must be serialized as strings.

Do not use binary floating point for money, risk thresholds, summaries, or tests.

## Educational Risk Scoring

Risk scoring is deterministic homework logic. It is not a fraud-detection model and does not make legal, regulatory, or customer-risk findings.

Acceptable signals include:

- High value at or above `10000.00`.
- Very high value at or above `50000.00`.
- Wire transfer type.
- Odd-hour activity from `00:00` through `04:59` UTC.
- Channel, country, or type combinations used as explicit sample heuristics.

Every signal must produce a stable reason code and be testable from the sample data.

## Privacy And Audit Rules

Treat account identifiers, descriptions, metadata, and audit details as sensitive even when synthetic. Logs, audit events, result summaries, examples, and docs must not expose full account identifiers or raw descriptions.

Required audit fields:

- ISO 8601 timestamp.
- Runtime component name.
- Transaction ID.
- Safe outcome.
- Stable reason code.
- Optional redacted account reference.

Use redaction examples such as `ACC-****1001`. Do not write raw source or destination account IDs, credentials, tokens, secrets, raw metadata dumps, or raw descriptions to logs, summaries, or audit output.

## Unsupported Claims Checklist

Reject or revise generated work that claims:

- Real banking compliance.
- AML, sanctions, KYC, payment-network, PCI, or legal compliance.
- Production-grade fraud detection or fraud-detection accuracy.
- Real settlement, reversal, refund, clearing, or chargeback execution.
- Regulatory audit sufficiency.
- Live verification of a complete current ISO 4217 table unless the implementation actually retrieves and documents it.

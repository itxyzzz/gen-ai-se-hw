# Domain Rules

## Status

This document supports the Java Athena (Spec Writer) candidate run `20260621-180826-write-spec-java-hera-java-full-set`. It defines conservative domain assumptions for an educational banking transaction-processing pipeline. It is not legal, banking, AML, sanctions, payment-network, fraud-prevention, or regulatory compliance advice.

## Scope

The generated transaction system processes synthetic transaction records from `sample-transactions.json` through stack-native Java runtime components. It validates required fields, applies deterministic educational fraud heuristics, assigns simulated settlement outcomes, and writes audit-safe result summaries for read-only status tooling.

The system does not:

- Move money or update balances.
- Connect to banks, card networks, payment processors, sanctions lists, AML systems, or external APIs.
- Verify real account ownership.
- Make legal, compliance, suspicious-activity, sanctions, or payment-network decisions.
- Store real customer data, credentials, hidden prompts, or production logs.

## Data Sensitivity

Treat all transaction fields as sensitive even when synthetic. Account identifiers, descriptions, metadata, audit details, and raw payloads must not appear in logs, summaries, status files, screenshots, test names, or example outputs unless redacted and necessary.

Allowed result fields are limited to safe operational data:

- Transaction ID.
- Amount string.
- Currency code.
- Final status.
- Reason codes.
- Risk category or risk score.
- Processing timestamps.
- Component history count.
- Audit event count.

Prohibited result fields include:

- Source account.
- Destination account.
- Raw description.
- Raw transaction metadata object.
- Credentials, tokens, prompts, or environment dumps.
- Full raw input payload.

## Currency And Money Rules

Money must use Java `BigDecimal` from string input. The generated system must never parse or compare transaction amounts with `double` or `float`.

Currency validation is ISO 4217-style and assignment bounded. For this sample-driven homework, accept `USD`, `EUR`, and `GBP`; reject unsupported values with a stable reason code such as `unsupported_currency`. Do not claim full ISO registry maintenance unless Hephaestus (Code Generator) later adds and cites a supported registry strategy.

Amounts must be positive. Blank, non-numeric, zero, negative, localized, or ambiguous amount values are invalid.

## Educational Risk Heuristics

Fraud Detector uses deterministic rules for homework coverage, not production fraud detection. Suggested signals:

- Amount at or above `10000.00` adds risk.
- Amount at or above `50000.00` requires review.
- UTC timestamp hour before `05:00` adds risk.
- `wire_transfer` adds risk.
- `api` or `mobile` channel adds risk.
- Non-`US` country metadata adds risk.

These signals should produce safe reason codes such as `high_value`, `very_high_value`, `unusual_time`, `wire_transfer_type`, `channel_risk`, and `country_risk`. They must not be described as AML, sanctions, KYC, legal, or regulatory findings.

## Audit Rules

Every runtime component emits structured audit events with:

- ISO 8601 timestamp.
- Runtime component name.
- Transaction ID when available.
- Safe outcome.
- Stable reason code when applicable.
- Component stage.

Audit events must not include raw account identifiers, raw descriptions, full metadata, credentials, or raw input payloads.

## Result Completeness Rules

Every input transaction must produce exactly one final result file under `shared/results/`. Reporting Agent owns count consistency, `summary.json`, `pipeline-status.json`, and a final prohibited-field privacy scan.

Repeated runs must archive the prior `shared/` tree under the next zero-padded folder such as `archive/shared-001` before creating a fresh protocol tree.

## Unsupported Claims Checklist

Generated code, tests, docs, and summaries must avoid claims that the system:

- Performs real settlement.
- Prevents fraud.
- Detects AML, sanctions, or suspicious activity.
- Satisfies banking, legal, payment-network, GDPR, PSD2, or other regulatory requirements.
- Processes real customer data or real payments.

When domain uncertainty exists, use safe phrasing: "educational simulation", "assignment-supported", "deterministic heuristic", and "requires real review before production use."

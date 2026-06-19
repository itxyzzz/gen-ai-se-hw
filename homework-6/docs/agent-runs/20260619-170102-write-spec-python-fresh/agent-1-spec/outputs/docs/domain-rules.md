# Domain Rules

Status: Candidate support document for run `20260619-170102-write-spec-python-fresh`

## Scope

The generated transaction-processing system is an educational simulation. It models validation, deterministic risk scoring, simulated settlement outcomes, structured audit events, repeated-run evidence archival, and read-only result shapes. It does not implement real banking, AML, sanctions, KYC, payment-network, legal, regulatory, settlement-finality, or fraud-model compliance.

## Research Basis

- ISO 4217 provides internationally recognized three-letter alphabetic currency codes and numeric currency codes. This specification uses an ISO 4217-style shape check plus a small homework allowlist.
- SIX is the recognized maintenance agency for ISO 4217 lists. A real production system should use a maintained authoritative list rather than the fixed homework allowlist.
- Python standard library documentation and Context7 `/python/cpython` support `decimal.Decimal` for exact decimal arithmetic and JSON `allow_nan=False` for stricter serialization.
- OWASP logging guidance supports structured event logs while masking or excluding bank account data, payment-card holder data, sensitive personal data, secrets, and unsafe raw payloads.

## Domain Assumptions

- The sample data is synthetic but still treated as sensitive for logs, audit events, final results, summaries, docs, and tests.
- Transaction IDs are safe synthetic correlation identifiers.
- Account IDs, descriptions, and metadata are sensitive and must not be copied into final results, audit events, summaries, or console output.
- The supported sample currencies are `USD`, `EUR`, and `GBP`.
- `XYZ` is unsupported for this homework even though it matches the uppercase three-letter shape.
- The system performs no foreign exchange conversion.
- Risk thresholds are educational heuristics chosen for deterministic testing.

## Simulation Boundaries

The generated product must not:

- Move money or change balances.
- Contact payment networks, banks, identity providers, sanctions lists, AML services, or fraud vendors.
- Claim that `review_required` is a legal, AML, sanctions, payment-network, or fraud determination.
- Claim that `settled` is real settlement finality.
- Store or display raw account identifiers, raw transaction descriptions, credentials, tokens, or unfiltered metadata.

## Sample Outcome Rules

| Transaction | Rule outcome |
|---|---|
| `TXN001` | Valid, low risk, simulated `settled`. |
| `TXN002` | Valid, review-required due to high-value wire signals. |
| `TXN003` | Valid, review-required due to near-threshold amount signal. |
| `TXN004` | Valid, review-required due to odd-hour, API, and non-US country signals. |
| `TXN005` | Valid, review-required due to very-high-value wire signals. |
| `TXN006` | Rejected due to unsupported currency. |
| `TXN007` | Rejected due to non-positive amount. |
| `TXN008` | Valid, low enough risk for simulated `settled`. |

## Privacy And Audit Rules

- Use structured audit events with timestamp, component name, transaction ID, safe outcome, and reason code.
- Prefer omitting account IDs from audit; when needed, redact as `ACC-****1001`.
- Do not log raw transaction descriptions.
- Do not dump raw metadata into final outputs.
- Test serialized final result text for absence of raw account IDs and sample descriptions.
- Keep `shared/run-provenance.json` limited to non-sensitive run IDs, paths, timestamps, and fingerprints.

## Future Review Checklist

- Confirm all examples remain synthetic or redacted.
- Confirm Decimal handling never falls back to `float`.
- Confirm final status vocabulary remains `settled`, `rejected`, `review_required`, and `error`.
- Confirm risk wording stays educational and does not imply real compliance or fraud conclusions.
- Confirm read-only status helpers do not rerun or mutate the pipeline.

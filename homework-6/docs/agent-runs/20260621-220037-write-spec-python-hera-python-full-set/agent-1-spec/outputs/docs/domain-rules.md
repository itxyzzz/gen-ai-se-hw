# Domain Rules

## Status

These domain rules support the preserved Python transaction-processing specification in run `20260621-220037-write-spec-python-hera-python-full-set`.

The system is an educational banking-style transaction-processing simulation. It is not legal, banking, AML, sanctions, KYC, fraud-prevention, payment-network, or settlement compliance software.

## Scope

The generated product:

- Reads synthetic sample transactions from `sample-transactions.json`.
- Processes every record through deterministic Python runtime components.
- Writes safe per-transaction results and aggregate summaries under `shared/results/`.
- Preserves repeated-run output by archiving prior `shared/` directories.
- Records minimal non-sensitive runtime provenance.

The generated product does not:

- Move money or contact payment networks.
- Verify real identities, sanctions, AML, KYC, or card-network rules.
- Make legal, regulatory, banking, or actual fraud determinations.
- Store or expose raw account IDs, descriptions, or full metadata in logs/audit/result examples.

## Currency Assumptions

- Use ISO 4217-style three-letter currency codes as a conservative engineering convention.
- Use a bounded allow-list for the sample: `USD`, `EUR`, and `GBP`.
- Reject unsupported sample value `XYZ`.
- Do not claim live ISO registry maintenance, certification, or exhaustive currency coverage.

## Money Rules

- Use Python `decimal.Decimal` for all monetary parsing and arithmetic.
- Construct `Decimal` values from strings only.
- Reject unparseable, non-finite, zero, and negative values.
- Serialize money to JSON as strings.
- Never use `float` for monetary values.

## Risk Rules

Fraud Detector risk rules are deterministic educational heuristics:

- Amounts `>= 25000.00` receive `REVIEW_HIGH_VALUE`.
- Timestamps before `04:00` UTC receive `REVIEW_UNUSUAL_TIME`.
- Transaction type, channel, and country may add deterministic risk weight, but outputs must not expose full raw metadata.
- Risk flags route to `review_required`; they do not assert real fraud.

## Settlement Rules

- Settlement Processor produces simulated outcomes only.
- Only validated, low-risk transactions may become `settled`.
- Rejected and review-required transactions must never become `settled`.
- No product output should imply money movement, banking network execution, final legal settlement, or regulatory disposition.

## Audit And Privacy Rules

Audit events include:

- timestamp
- runtime component name
- transaction ID
- safe outcome/status
- reason code when applicable

Audit events exclude:

- plaintext source/destination account IDs
- descriptions
- full metadata payloads
- raw exception traces
- credentials, prompts, hidden thread context, or unrelated local paths

## Result Completeness Rules

- Every input transaction ID must have exactly one final result file.
- `summary.json` counts must match the final result files.
- `pipeline-status.json` must be readable without raw input transactions.
- Reporting Agent owns final privacy checks before result artifacts are considered complete.


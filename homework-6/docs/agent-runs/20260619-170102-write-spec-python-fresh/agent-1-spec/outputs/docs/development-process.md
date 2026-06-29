# Development Process

Status: Candidate support document for run `20260619-170102-write-spec-python-fresh`

## Purpose

This process describes how later product-generation agents should implement from the candidate Python transaction-processing specification without depending on hidden harness state or optional workflow plugins.

## Implementation Order

1. Read the selected `specification.md`, this support package, `sample-transactions.json`, and `agents.md`.
2. Confirm the selected stack is Python.
3. Implement common helpers first: Decimal parsing, JSON writing, redaction, audit events, envelope helpers, and sensitive-field checks.
4. Implement shared directory archival, fresh runtime tree creation, and runtime provenance.
5. Implement validator behavior and dry-run validation.
6. Implement deterministic risk scoring.
7. Implement simulated settlement and final result builders.
8. Implement integrator orchestration and file movement.
9. Implement result summary and optional read-only result helpers after result shapes exist.
10. Implement pytest coverage with temporary filesystem isolation.

## Verification Gates

Before reporting implementation complete:

- Run `python -m pytest`.
- Run `python -m pytest --cov=.` when coverage tooling is available and confirm at least 75% for this specification-stage target.
- Run `python integrator.py` from the homework root.
- Confirm `shared/run-provenance.json` exists and contains no raw transaction data.
- Confirm `shared/results/summary.json` reports total eight records.
- Confirm final statuses are only `settled`, `rejected`, `review_required`, and `error`.
- Confirm `TXN006` rejects with `UNSUPPORTED_CURRENCY`.
- Confirm `TXN007` rejects with `NON_POSITIVE_AMOUNT`.
- Confirm repeated runs archive prior `shared/` under `archive/shared-NNN`.
- Search serialized results, summary, and audit output for raw account IDs and sample descriptions.

## Privacy Review

Reviewers should inspect:

- Audit event builders.
- Final result builders.
- Summary writer.
- Console output.
- Tests that serialize result files and scan for sensitive sample strings.

No result or audit output should contain full sample account IDs, raw descriptions, credentials, tokens, or unfiltered metadata.

## MCP Boundary

Read-only helpers may be implemented after result shapes exist. Do not edit MCP configuration files until `mcp/server.py` exists and is verified. Read-only helpers must not rerun or mutate the pipeline.

## Handoff Expectation

Later implementation work should record:

- Selected source spec run ID.
- Source spec path.
- Source spec SHA-256 fingerprint.
- Selected code package or inventory reference.
- Test commands and results.
- Known limitations and any deviation from expected sample outcomes.


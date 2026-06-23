---
name: generate-transactions
description: Use when generating a random Homework 6 transaction list (valid, fraud-review, and invalid records) in the canonical input format for the transaction-processing pipeline.
---

# Generate Transactions

Use this skill to generate a fresh list of synthetic transactions in the canonical input format, including both valid and invalid records. This is an Operator Layer pipeline operation surface, not a runtime transaction pipeline component and not a Themis (Test Generator) per-run output.

The generator is written against the actual validation and fraud rules (`agents.transaction_validator`, `agents.fraud_detector`, and `integrator`), so it can deliberately emit records in every outcome class: settled, review-required, and each rejection reason code.

## Required Workflow

Before acting, read `../../../agent-control/operate-pipeline/commands-and-hooks.md`. If that reference is missing or unreadable, stop and report the missing file.

## Execution

Follow the stack-resolution and `/generate-transactions` sections exactly. Prefer the documented fast path for the selected package set; for the current canonical Python set this is one bounded invocation of `scripts/generate_transactions.py`.

Do not load broad project context, run `git status`, or inspect raw sample transactions unless the fast path fails and the operator explicitly authorizes a fallback.

Required behavior:

1. Resolve the package set first. For `stack=python`, use `scripts/generate_transactions.py`.
2. Prefer `--balanced` to emit at least one record of every category; otherwise tune `--count`, `--invalid-ratio`, and `--review-ratio`. Always pass `--seed` for reproducible output.
3. Write to an explicit `--output` path. Do not overwrite the committed `sample-transactions.json` unless the operator explicitly asks; prefer a clearly named file such as `sample-transactions.generated.json`. Optionally write `--manifest` for the redacted expected-outcome record.
4. Report only safe summary fields: total, valid, invalid, category counts, and expected pipeline counts.
5. Note the validator-vs-pipeline divergence: `MISSING_FIELD` is observable only via `/validate-transactions`, because the integrator backfills required fields before the full pipeline runs.

To confirm the generated set behaves as predicted, hand the output file to `/validate-transactions` (validator layer) or `/run-pipeline` (full pipeline). Never print raw account IDs, raw descriptions, names, or full transaction payloads.

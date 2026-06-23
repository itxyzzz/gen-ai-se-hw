# Generate Transactions

Generate a random list of transactions in the canonical input format, including both valid and invalid records, for use with `/validate-transactions` and `/run-pipeline`.

This is a thin legacy slash-command wrapper for the Operator Layer pipeline operation surface. Before acting, read `agent-control/operate-pipeline/commands-and-hooks.md` and follow the `/generate-transactions` section exactly.

Prefer the documented fast path for the selected package set. For the current canonical Python set, this is one bounded invocation of `scripts/generate_transactions.py`. The generator is written against the actual rules in `agents.transaction_validator`, `agents.fraud_detector`, and `integrator`, so it can deliberately emit records in every outcome class.

Steps:

1. Resolve the package set. For `stack=python`, use `scripts/generate_transactions.py`.
2. Choose the mix: prefer `--balanced` for full branch coverage, otherwise tune `--count`, `--invalid-ratio`, and `--review-ratio`. Always pass `--seed` for reproducibility.
3. Write to an explicit `--output` path. Do not overwrite the committed `sample-transactions.json` unless the operator explicitly asks. Optionally pass `--manifest` for the redacted expected-outcome record.
4. Report only safe summary fields: total, valid, invalid, category counts, and expected pipeline counts.
5. Optionally confirm the result with `/validate-transactions` (validator layer) or `/run-pipeline` (full pipeline). `MISSING_FIELD` is observable only via `/validate-transactions`, because the integrator backfills required fields.

Do not print raw account IDs, raw descriptions, names, or full transaction payloads.

# Validate Transactions

Validate all transactions in `sample-transactions.json` without processing them through the full pipeline.

This is a thin legacy slash-command wrapper for the Operator Layer pipeline operation surface. Before acting, read `agent-control/operate-pipeline/commands-and-hooks.md` and follow the `/validate-transactions` section exactly.

Prefer the documented fast path: one bounded invocation of `agents.transaction_validator.validate_transactions_file` that emits only total, valid, invalid, reason-code groups, and a safe transaction table. Do not use the stale file-path form `python agents/transaction_validator.py --dry-run sample-transactions.json`; the current validator uses package-relative imports.

Steps:

1. Run the validator dry-run function without running the full pipeline.
2. Report total count, valid count, invalid count, and reason-code groups.
3. Show a redacted table with transaction ID, status, and safe reason code.

Do not run the full pipeline unless the operator explicitly authorizes a fallback. Do not print raw account IDs, raw descriptions, names, or full audit payloads.

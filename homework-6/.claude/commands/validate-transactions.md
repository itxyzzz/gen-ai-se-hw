# Validate Transactions

Validate all transactions in `sample-transactions.json` without processing them through the full pipeline.

This is a thin legacy slash-command wrapper for the Operator Layer pipeline operation surface. Before acting, read `agent-control/operate-pipeline/commands-and-hooks.md` and follow the `/validate-transactions` section exactly.

Steps:

1. Run the validator in dry-run mode when available, normally `python agents/transaction_validator.py --dry-run sample-transactions.json`.
2. Report total count, valid count, invalid count, and reason-code groups.
3. Show a redacted table with transaction ID, status, and safe reason code.

Do not run the full pipeline unless the operator explicitly authorizes a fallback. Do not print raw account IDs, raw descriptions, names, or full audit payloads.

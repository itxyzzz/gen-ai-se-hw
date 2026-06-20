---
name: validate-transactions
description: Use when validating Homework 6 sample transactions without running the full generated transaction-processing pipeline.
---

# Validate Transactions

Use this skill to validate `sample-transactions.json` without running the full pipeline. This is an Operator Layer pipeline operation surface, not a Themis (Test Generator) per-run output.

## Required Workflow

Before acting, read `../../../agent-control/operate-pipeline/commands-and-hooks.md`. If that reference is missing or unreadable, stop and report the missing file.

## Execution

Follow the `/validate-transactions` section exactly. Prefer its fast path: one bounded invocation of the validator dry-run function that emits total, valid, invalid, reason-code groups, and a safe transaction table.

Do not run the stale file-path form `python agents/transaction_validator.py --dry-run sample-transactions.json`; the current validator uses package-relative imports and exposes dry-run behavior through `agents.transaction_validator.validate_transactions_file`.

Do not load broad project context, run `git status`, inspect raw sample transactions, or run the full pipeline unless the fast path fails and the operator explicitly authorizes a fallback.

Required behavior:

1. Use validator dry-run behavior when available.
2. Report total, valid, and invalid counts.
3. Group invalid results by safe reason code.
4. Show a redacted table with transaction ID, status, and safe reason code.

Do not run the full pipeline unless the operator explicitly authorizes a fallback. Never print raw account IDs, raw descriptions, names, or full audit payloads.

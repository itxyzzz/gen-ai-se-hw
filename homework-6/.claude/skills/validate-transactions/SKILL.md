---
name: validate-transactions
description: Use when validating Homework 6 sample transactions without running the full generated transaction-processing pipeline.
when_to_use: Use for Homework 6 Task 3 dry-run validation of sample transactions and redacted reason-code summaries.
argument-hint: ""
---

# Validate Transactions

Use this skill to validate `sample-transactions.json` without running the full pipeline. The directory name exposes the Claude Code `/validate-transactions` project-skill surface. This is an Operator Layer pipeline operation surface, not a Themis (Test Generator) per-run output.

## Required Workflow

Before acting, read `../../../agent-control/operate-pipeline/commands-and-hooks.md`. If that reference is missing or unreadable, stop and report the missing file.

## Execution

Follow the stack-resolution and `/validate-transactions` sections exactly. Prefer the documented fast path for the selected package set; for the current canonical Python set this is one bounded invocation of the validator dry-run function that emits total, valid, invalid, reason-code groups, and a safe transaction table.

Do not run the stale file-path form `python agents/transaction_validator.py --dry-run sample-transactions.json`; the current validator uses package-relative imports and exposes dry-run behavior through `agents.transaction_validator.validate_transactions_file`.

Do not load broad project context, run `git status`, inspect raw sample transactions, or run the full pipeline unless the fast path fails and the operator explicitly authorizes a fallback.

Required behavior:

1. Use the selected package set's validation-only dry-run behavior when available.
2. Report total, valid, and invalid counts.
3. Group invalid results by safe reason code.
4. Show a redacted table with transaction ID, status, and safe reason code.

Do not run the full pipeline unless the operator explicitly authorizes a fallback. Never print raw account IDs, raw descriptions, names, or full audit payloads.

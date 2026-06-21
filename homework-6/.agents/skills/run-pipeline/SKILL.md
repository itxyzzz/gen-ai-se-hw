---
name: run-pipeline
description: Use when running the Homework 6 generated transaction-processing pipeline end to end through the shared Operator Layer pipeline operation surface.
---

# Run Pipeline

Use this skill to run the selected Homework 6 generated transaction-processing pipeline end to end. This is an Operator Layer pipeline operation surface, not a Themis (Test Generator) per-run output.

## Required Workflow

Before acting, read `../../../agent-control/operate-pipeline/commands-and-hooks.md`. If that reference is missing or unreadable, stop and report the missing file.

## Execution

Follow the stack-resolution and `/run-pipeline` sections exactly. Prefer the documented fast path for the selected package set; for the current canonical Python set this runs `python integrator.py`, reads `shared/results/summary.json`, verifies result count, and extracts rejected transaction IDs with safe reason codes.

Do not load broad project context, run `git status`, inspect raw sample transactions, or make separate schema-probing passes unless the fast path fails.

Required behavior:

1. Check that `sample-transactions.json` exists.
2. Archive or clear `shared/` according to selected pipeline behavior.
3. Run the selected pipeline command from the package-set metadata or shared command reference. The current canonical Python command is `python integrator.py`.
4. Summarize `shared/results/summary.json`.
5. Report rejected transaction IDs with safe reason codes only.
6. Confirm all sample transactions have corresponding `shared/results/TXN*.json` result files.

Never print raw account IDs, raw descriptions, names, or full audit payloads.

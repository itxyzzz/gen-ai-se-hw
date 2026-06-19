---
name: run-pipeline
description: Use when running the Homework 6 generated transaction-processing pipeline end to end through the shared Operator Layer pipeline operation surface.
when_to_use: Use for Homework 6 Task 3 or demo runs that execute the generated banking pipeline and summarize shared results.
argument-hint: ""
---

# Run Pipeline

Use this skill to run the selected Homework 6 generated transaction-processing pipeline end to end. The directory name exposes the Claude Code `/run-pipeline` project-skill surface. This is an Operator Layer pipeline operation surface, not a Themis (Test Generator) per-run output.

## Required Workflow

Before acting, read `../../../agent-control/operate-pipeline/commands-and-hooks.md`. If that reference is missing or unreadable, stop and report the missing file.

## Execution

Follow the `/run-pipeline` section exactly:

1. Check that `sample-transactions.json` exists.
2. Archive or clear `shared/` according to selected pipeline behavior.
3. Run the selected pipeline command, normally `python integrator.py`.
4. Summarize `shared/results/summary.json`.
5. Report rejected transaction IDs with safe reason codes only.

Never print raw account IDs, raw descriptions, names, or full audit payloads.

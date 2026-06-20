# Run Pipeline

Run the multi-agent banking pipeline end to end.

This is a thin legacy slash-command wrapper for the Operator Layer pipeline operation surface. Before acting, read `agent-control/operate-pipeline/commands-and-hooks.md` and follow the `/run-pipeline` section exactly.

Prefer the documented fast path: one bounded shell invocation that checks `sample-transactions.json`, runs `python integrator.py`, summarizes `shared/results/summary.json`, verifies result count, and extracts rejected transaction IDs with safe reason codes. Do not load broad project context, run `git status`, or inspect raw payloads unless the fast path fails.

Steps:

1. Check that `sample-transactions.json` exists.
2. Archive or clear `shared/` according to selected pipeline behavior.
3. Run the selected pipeline command, normally `python integrator.py`.
4. Show a summary of results from `shared/results/summary.json`.
5. Report any rejected transactions by transaction ID and safe reason code only.
6. Confirm all sample transactions have corresponding `shared/results/TXN*.json` result files.

Do not print raw account IDs, raw descriptions, names, or full audit payloads.

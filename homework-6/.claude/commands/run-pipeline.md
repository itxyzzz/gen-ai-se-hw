# Run Pipeline

Run the multi-agent banking pipeline end to end.

This is a thin legacy slash-command wrapper for the Operator Layer pipeline operation surface. Before acting, read `agent-control/operate-pipeline/commands-and-hooks.md` and follow the `/run-pipeline` section exactly.

Steps:

1. Check that `sample-transactions.json` exists.
2. Archive or clear `shared/` according to selected pipeline behavior.
3. Run the selected pipeline command, normally `python integrator.py`.
4. Show a summary of results from `shared/results/summary.json`.
5. Report any rejected transactions by transaction ID and safe reason code only.

Do not print raw account IDs, raw descriptions, names, or full audit payloads.

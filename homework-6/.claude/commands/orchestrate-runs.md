# Orchestrate Runs

Plan, preserve, resume, compare, select, or review a Homework 6 Hera (Orchestrator) package-set run.

This is a thin legacy slash-command wrapper. The canonical Hera workflow lives in `agent-control/orchestrate-runs/`.

Before acting, read:

1. `agent-control/orchestrate-runs/workflow.md`
2. `agent-control/orchestrate-runs/quality-bar.md`
3. `agent-control/orchestrate-runs/run-registry.md`

If any required reference is missing or unreadable, stop and report the missing file. Follow the shared workflow exactly; do not duplicate or invent fallback orchestration rules in this command wrapper.

Argument examples:

```text
/orchestrate-runs compare-set set=python-canonical-20260621
/orchestrate-runs generate-set stack=java
/orchestrate-runs resume-set run=20260621-140000-orchestrate-runs-java-alternate
/orchestrate-runs compare-set set=python-canonical-20260621 run=20260621-140000-orchestrate-runs-java-alternate
/orchestrate-runs select-set set=java-alternate-YYYYMMDD
```

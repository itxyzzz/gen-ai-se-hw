# Generate Tests

Create, resume, compare, select, or review a Homework 6 Themis (Test Generator) run.

This is a thin legacy slash-command wrapper. The canonical Themis workflow lives in `agent-control/generate-tests/`.

Before acting, read:

1. `agent-control/generate-tests/workflow.md`
2. `agent-control/generate-tests/quality-bar.md`
3. `agent-control/generate-tests/run-registry.md`

If any required reference is missing or unreadable, stop and report the missing file. Follow the shared workflow exactly; do not duplicate or invent fallback test-generation rules in this command wrapper.

Argument examples:

```text
/generate-tests
/generate-tests generate
/generate-tests resume run=20260620-091500-generate-tests-python-primary
/generate-tests compare
/generate-tests select run=20260620-091500-generate-tests-python-primary
```

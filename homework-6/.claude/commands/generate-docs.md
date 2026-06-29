# Generate Docs

Create, resume, compare, select, or review a Homework 6 Clio (Documentation Generator) run.

This is a thin legacy slash-command wrapper. The canonical Clio workflow lives in `agent-control/generate-docs/`.

Before acting, read:

1. `agent-control/generate-docs/workflow.md`
2. `agent-control/generate-docs/quality-bar.md`
3. `agent-control/generate-docs/run-registry.md`

If any required reference is missing or unreadable, stop and report the missing file. Follow the shared workflow exactly; do not duplicate or invent fallback documentation-generation rules in this command wrapper.

Argument examples:

```text
/generate-docs
/generate-docs generate
/generate-docs resume run=20260620-180000-generate-docs-python-primary
/generate-docs resume run=20260621-170000-generate-docs-java-alternate
/generate-docs compare
/generate-docs select run=20260620-180000-generate-docs-python-primary
```

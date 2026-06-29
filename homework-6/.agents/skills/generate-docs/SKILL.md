---
name: generate-docs
description: Use when generating, resuming, comparing, selecting, or reviewing Homework 6 Clio (Documentation Generator) documentation-generation runs.
---

# Generate Docs

Use this skill to create, resume, compare, select, or review preserved Homework 6 Clio (Documentation Generator) documentation-generation runs. It is the Codex Markdown entrypoint for the same core workflow used by the Claude Code `generate-docs` skill and command wrapper.

## Required Workflow

Before acting, read these references from the Homework 6 agent-control package:

1. `../../../agent-control/generate-docs/workflow.md`: canonical workflow for both Codex and Claude Code surfaces.
2. `../../../agent-control/generate-docs/quality-bar.md`: documentation, screenshot, evidence, privacy, PR draft, and scope quality bar.
3. `../../../agent-control/generate-docs/run-registry.md`: run preservation, inventory, evidence, screenshot mapping, comparison, and selection rules.

These shared workflow files are mandatory. If any required reference is missing or unreadable, stop and report the missing file instead of attempting fallback documentation generation.

## Execution

Follow `workflow.md` exactly. It owns modes, context loading, author/source handling, selected-run consumption, screenshot preservation, documentation scope, validation rules, selection rules, and handoff behavior.

Repository `dev-doc-harness` and Superpowers requirements apply to Operator Layer maintenance of this skill package. Clio (Documentation Generator) runs must follow the shared workflow without introducing harness planning, freeze gates, or Superpowers-only requirements into generated documentation.

---
name: generate-tests
description: Use when generating, resuming, comparing, selecting, or reviewing Homework 6 Themis (Test Generator) test-generation runs.
---

# Generate Tests

Use this skill to create, resume, compare, select, or review preserved Homework 6 Themis (Test Generator) test-generation runs. It is the Codex Markdown entrypoint for the same core workflow used by the Claude Code `generate-tests` skill and command wrapper.

## Required Workflow

Before acting, read these references from the Homework 6 agent-control package:

1. `../../../agent-control/generate-tests/workflow.md`: canonical workflow for both Codex and Claude Code surfaces.
2. `../../../agent-control/generate-tests/quality-bar.md`: version traceability, test quality, command/hook validation, privacy, scope, and validation quality bar.
3. `../../../agent-control/generate-tests/run-registry.md`: run preservation, comparison, inventory, evidence, and selection rules.

These shared workflow files are mandatory. If any required reference is missing or unreadable, stop and report the missing file instead of attempting fallback test generation.

## Execution

Follow `workflow.md` exactly. It owns modes, context loading, selected-code consumption, run-local workspace rules, generation scope, validation rules, and handoff behavior.

Repository `dev-doc-harness` and Superpowers requirements apply to Operator Layer maintenance of this skill package. Themis (Test Generator) runs must follow the shared workflow without introducing harness planning, freeze gates, or Superpowers-only requirements into generated test files.

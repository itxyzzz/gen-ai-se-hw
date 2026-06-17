---
name: write-spec
description: Use when generating, resuming, comparing, or selecting Homework 6 Athena (Spec Writer) specification runs.
---

# Write Spec

Use this skill to create, resume, compare, or select preserved Homework 6 Athena (Spec Writer) specification runs. It is the Codex Markdown entrypoint for the same core workflow used by the Claude Code `write-spec` skill.

## Required Workflow

Before acting, read these references from the Homework 6 agent-control package:

1. `../../../agent-control/write-spec/workflow.md`: canonical workflow for both Codex and Claude Code surfaces.
2. `../../../agent-control/write-spec/stack-profiles.md`: fixed stack enum, default stack, and stack-specific generation profiles.
3. `../../../agent-control/write-spec/quality-bar.md`: generated spec, run, comparison, and handoff quality bar.
4. `../../../agent-control/write-spec/run-registry.md`: run preservation, comparison, and selection rules.

These shared workflow files are mandatory. If any required reference is missing or unreadable, stop and report the missing file instead of attempting a fallback generation.

## Execution

Follow `workflow.md` exactly. It owns modes, stack input, run layout, sub-agent requirements, model guidance, research rules, selection behavior, and self-review gates.

Repository `dev-doc-harness` and Superpowers requirements apply to Operator Layer maintenance of this skill package. Athena (Spec Writer) runs must follow the shared workflow without introducing harness planning, freeze gates, or Superpowers-only requirements into generated specifications or downstream Homework Automation Layer prompts.

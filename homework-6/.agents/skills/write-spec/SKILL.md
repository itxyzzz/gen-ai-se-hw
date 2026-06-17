---
name: write-spec
description: Generate or refine the Homework 6 multi-agent banking pipeline specification package. Use when asked to create the Homework 6 specification, run Agent 1, compare specification-generation runs, select a generated spec run, or prepare the spec handoff for downstream code, test, and documentation agents.
---

# Write Spec

Use this skill to create, resume, compare, or select preserved Homework 6 Agent 1 specification runs. It is the Codex Markdown entrypoint for the same core workflow used by the Claude Code `/write-spec` slash command.

## Required Workflow

Before acting, read these references from this skill folder:

1. `references/write-spec-workflow.md`: canonical workflow for both Codex and Claude Code surfaces.
2. `references/stack-profiles.md`: fixed stack enum, default stack, and stack-specific generation profiles.
3. `references/write-spec-quality-bar.md`: generated spec, run, comparison, and handoff quality bar.

These shared workflow files are mandatory. If any required reference is missing or unreadable, stop and report the missing file instead of attempting a fallback generation.

## Execution

Follow `write-spec-workflow.md` exactly. It owns modes, stack input, run layout, sub-agent requirements, model guidance, research rules, selection behavior, and self-review gates.

When repository instructions require `dev-doc-harness` or Superpowers, comply with their planning and freeze gates. The generated Homework 6 agents must remain usable without those tools.

---
name: generate-code
description: Use when generating, resuming, comparing, or reviewing Homework 6 Hephaestus (Code Generator) code-generation runs.
---

# Generate Code

Use this skill to create, resume, compare, or review preserved Homework 6 Hephaestus (Code Generator) code-generation runs. It is the Codex Markdown entrypoint for the same core workflow used by the Claude Code `generate-code` skill.

## Required Workflow

Before acting, read these references from the Homework 6 agent-control package:

1. `../../../agent-control/generate-code/workflow.md`: canonical workflow for both Codex and Claude Code surfaces.
2. `../../../agent-control/generate-code/quality-bar.md`: generated code, Context7, sub-agent, privacy, scope, and validation quality bar.
3. `../../../agent-control/generate-code/run-registry.md`: run preservation, comparison, and evidence rules.

These shared workflow files are mandatory. If any required reference is missing or unreadable, stop and report the missing file instead of attempting fallback code generation.

## Execution

Follow `workflow.md` exactly. It owns modes, context loading, selected-spec consumption, Context7 usage, sub-agent autonomy, model/context shaping, run layout, generation scope, validation rules, and handoff behavior.

Repository `dev-doc-harness` and Superpowers requirements apply to Operator Layer maintenance of this skill package. Hephaestus (Code Generator) runs must follow the shared workflow without introducing harness planning, freeze gates, or Superpowers-only requirements into generated product files.

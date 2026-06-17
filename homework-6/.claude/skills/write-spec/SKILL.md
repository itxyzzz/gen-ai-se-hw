---
name: write-spec
description: Use when generating, resuming, comparing, or selecting Homework 6 Agent 1 specification runs.
when_to_use: Use for Homework 6 Task 1 spec generation, Agent 1 runs, stack-specific write-spec runs, comparing preserved spec runs, or selecting the canonical specification.md.
argument-hint: "[generate|resume|compare|select] [stack=python|stack=java] [run=<run-id>]"
---

# Write Spec

Use this skill to create, resume, compare, or select preserved Homework 6 Agent 1 specification runs. The directory name exposes the Claude Code `/write-spec` slash command.

## Required Workflow

Before acting, read these references from the Homework 6 agent-control package:

1. `../../../agent-control/write-spec/workflow.md`: canonical workflow for both Codex and Claude Code surfaces.
2. `../../../agent-control/write-spec/stack-profiles.md`: fixed stack enum, default stack, and stack-specific generation profiles.
3. `../../../agent-control/write-spec/quality-bar.md`: generated spec, run, comparison, and handoff quality bar.
4. `../../../agent-control/write-spec/run-registry.md`: run preservation, comparison, and selection rules.

These shared workflow files are mandatory. If any required reference is missing or unreadable, stop and report the missing file instead of attempting a fallback generation.

## Execution

Follow `workflow.md` exactly. It owns modes, stack input, run layout, sub-agent requirements, model guidance, research rules, selection behavior, required outputs, and review gates.

## Examples

```text
/write-spec
/write-spec stack=python
/write-spec stack=java
/write-spec compare
/write-spec select run=20260616-173000-write-spec-python-primary
```

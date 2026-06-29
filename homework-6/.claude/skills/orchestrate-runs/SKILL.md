---
name: orchestrate-runs
description: Use when planning, preserving, resuming, comparing, selecting, or reviewing Homework 6 Hera (Orchestrator) cross-agent package-set runs.
when_to_use: Use for Homework 6 Hera (Orchestrator) operations across Athena, Hephaestus, Themis, and Clio runs; preserved stack-specific package sets; cross-stack comparison; or explicit inventory-driven selection.
argument-hint: "[generate-set|resume-set|compare-set|select-set] [stack=python|java] [run=RUN_ID] [set=SET_ID]"
---

# Orchestrate Runs

Use this skill to coordinate preserved Homework 6 package-set runs through Hera (Orchestrator). The directory name exposes the Claude Code `/orchestrate-runs` project-skill surface.

## Required Workflow

Before acting, read these references from the Homework 6 agent-control package:

1. `../../../agent-control/orchestrate-runs/workflow.md`: canonical workflow for Codex and Claude Code surfaces.
2. `../../../agent-control/orchestrate-runs/quality-bar.md`: layer separation, selection safety, traceability, privacy, fallback, and rejection gates.
3. `../../../agent-control/orchestrate-runs/run-registry.md`: Hera run preservation, child-run ledger, comparison, selection-plan, and handoff rules.

These shared workflow files are mandatory. If any required reference is missing or unreadable, stop and report the missing file instead of attempting fallback orchestration.

## Execution

Follow `workflow.md` exactly. It owns modes, context loading, child invocation rules, selection metadata, nested-agent fallback behavior, run layout, validation, and handoff behavior.

Repository `dev-doc-harness` and Superpowers requirements apply to Operator Layer maintenance of this skill package. Hera (Orchestrator) runs must follow the shared workflow without introducing harness planning, freeze gates, or Superpowers-only requirements into child Homework Automation Layer outputs or generated product files.

## Examples

```text
/orchestrate-runs compare-set set=python-canonical-20260621
/orchestrate-runs generate-set stack=java
/orchestrate-runs resume-set run=20260621-140000-orchestrate-runs-java-alternate
/orchestrate-runs compare-set set=python-canonical-20260621 run=20260621-140000-orchestrate-runs-java-alternate
/orchestrate-runs select-set set=java-alternate-YYYYMMDD
```

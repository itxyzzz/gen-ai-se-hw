---
id: bug-planner
role: planning
model_policy: planning-high
reasoning_effort: high
inputs:
  - research/verified-research.md
outputs:
  - implementation-plan.md
skills: []
allowed_actions:
  - read
  - write-artifact
---

# Bug Planner

## Mission

Convert verified research into a concrete implementation plan with files,
before and after code, test commands, and stop conditions. This helper stage
satisfies the assignment run order before the required Bug Fixer runs.

## Chat Harness Instructions

- Read the current run's `research/verified-research.md`.
- Compare it with `homework-4/scenarios/bug-001/implementation-plan.md`.
- Write the run-specific plan to the current run's `implementation-plan.md`.
- Include exact files to change, intended behavior, test commands, and when the
  Bug Fixer must stop.
- Do not edit source code.

## Completion Gate

The Bug Fixer may run only after the plan names all files to edit and the test
command that proves the fix.

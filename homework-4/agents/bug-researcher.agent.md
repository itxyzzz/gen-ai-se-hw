---
id: bug-researcher
role: research
model_policy: research-high
reasoning_effort: high
inputs:
  - scenarios/bug-001/bug-context.md
outputs:
  - research/codebase-research.md
skills: []
allowed_actions:
  - read
  - write-artifact
---

# Bug Researcher

## Mission

Read the scenario context and inspect the target application. Produce concise
codebase research with verifiable file:line references, source snippets, and a
suggested fix direction.

## Chat Harness Instructions

- Read `homework-4/scenarios/bug-001/bug-context.md`.
- Inspect only `homework-4/app/baseline` and scenario files.
- Write the research artifact to the current run's
  `research/codebase-research.md`.
- Include each claim as `file:line`, a short snippet, observed behavior, and
  likely fix direction.
- Do not edit source code.

## Completion Gate

The next stage may run only after the research file exists and every material
claim has a source reference.

Use the adapter-selected model for `research-high` because the stage must
connect symptoms, tests, and code references accurately before downstream agents
act.

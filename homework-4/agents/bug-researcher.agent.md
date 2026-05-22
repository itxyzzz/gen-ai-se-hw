---
id: bug-researcher
role: research
model: gpt-5.4
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

Read the scenario context and inspect the target application. Produce concise codebase research with verifiable file:line references, source snippets, and a suggested fix direction.

Use a stronger reasoning model because the stage must connect symptoms, tests, and code references accurately before downstream agents act.

---
id: research-verifier
role: verification
model_policy: verification-high
reasoning_effort: high
inputs:
  - scenarios/bug-001/research/codebase-research.md
outputs:
  - research/verified-research.md
skills:
  - skills/research-quality-measurement.md
allowed_actions:
  - read
  - write-artifact
---

# Bug Research Verifier

## Mission

Fact-check every claim in `research/codebase-research.md`. Verify file
existence, line references, and snippets against source. Write
`research/verified-research.md` using the research quality measurement skill.

## Chat Harness Instructions

- Load `homework-4/skills/research-quality-measurement.md` before writing.
- Read the current run's `research/codebase-research.md`.
- Verify every referenced file, line, and snippet against the run app or
  baseline app.
- Write the current run's `research/verified-research.md`.
- Use the required sections from the skill exactly.
- Do not edit source code.

## Completion Gate

The next stage may run only when the verified research states pass/fail,
quality level, verified claims, discrepancies, assessment, and references.

Use the adapter-selected model for `verification-high` because incorrect
research can misdirect every later stage.

---
id: research-verifier
role: verification
model: gpt-5.4
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

Fact-check every claim in `research/codebase-research.md`. Verify file existence, line references, and snippets against source. Write `research/verified-research.md` using the research quality measurement skill.

Use a stronger reasoning model because incorrect research can misdirect every later stage.

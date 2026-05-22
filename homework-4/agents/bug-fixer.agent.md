---
id: bug-fixer
role: implementation
model: gpt-5.3-codex
model_policy: implementation-medium
reasoning_effort: medium
inputs:
  - implementation-plan.md
outputs:
  - fix-summary.md
skills: []
allowed_actions:
  - read
  - edit-run-workspace
  - test
  - write-artifact
---

# Bug Fixer

Read the implementation plan completely. Apply only the requested changes inside the run workspace app. Run tests after each change; if tests fail unexpectedly, document the failure and stop. Write `fix-summary.md`.

Use a coding-specialized model with medium reasoning because the edits are bounded and mechanical after verification and planning.

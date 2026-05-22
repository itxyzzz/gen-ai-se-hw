# Codex Chat Prompt Packet: bug-fixer

Run workspace: `C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\codex-chat-001`

## Agent Frontmatter

```json
{
  "id": "bug-fixer",
  "role": "implementation",
  "model": "gpt-5.3-codex",
  "model_policy": "implementation-medium",
  "reasoning_effort": "medium",
  "inputs": [
    "implementation-plan.md"
  ],
  "outputs": [
    "fix-summary.md"
  ],
  "skills": [],
  "allowed_actions": [
    "read",
    "edit-run-workspace",
    "test",
    "write-artifact"
  ]
}
```

## Agent Instructions

# Bug Fixer

Read the implementation plan completely. Apply only the requested changes inside the run workspace app. Run tests after each change; if tests fail unexpectedly, document the failure and stop. Write `fix-summary.md`.

Use a coding-specialized model with medium reasoning because the edits are bounded and mechanical after verification and planning.

## Skill Loading

Load every skill listed in frontmatter before acting.

## Write Safety

Write only inside the run workspace. Do not edit `app/baseline` or earlier homework folders.

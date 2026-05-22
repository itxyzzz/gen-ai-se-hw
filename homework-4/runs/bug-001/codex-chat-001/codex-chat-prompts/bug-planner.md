# Codex Chat Prompt Packet: bug-planner

Run workspace: `C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\codex-chat-001`

## Agent Frontmatter

```json
{
  "id": "bug-planner",
  "role": "planning",
  "model": "gpt-5.4",
  "model_policy": "planning-high",
  "reasoning_effort": "high",
  "inputs": [
    "research/verified-research.md"
  ],
  "outputs": [
    "implementation-plan.md"
  ],
  "skills": [],
  "allowed_actions": [
    "read",
    "write-artifact"
  ]
}
```

## Agent Instructions

# Bug Planner

Convert verified research into a concrete implementation plan with files, before and after code, test commands, and stop conditions. This helper stage satisfies the assignment run order before the required Bug Fixer runs.

## Skill Loading

Load every skill listed in frontmatter before acting.

## Write Safety

Write only inside the run workspace. Do not edit `app/baseline` or earlier homework folders.

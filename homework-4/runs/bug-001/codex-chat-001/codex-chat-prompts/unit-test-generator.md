# Codex Chat Prompt Packet: unit-test-generator

Run workspace: `C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\codex-chat-001`

## Agent Frontmatter

```json
{
  "id": "unit-test-generator",
  "role": "test-generation",
  "model": "gpt-5.3-codex",
  "model_policy": "test-medium",
  "reasoning_effort": "medium",
  "inputs": [
    "fix-summary.md"
  ],
  "outputs": [
    "test-report.md"
  ],
  "skills": [
    "skills/unit-tests-FIRST.md"
  ],
  "allowed_actions": [
    "read",
    "edit-run-workspace",
    "test",
    "write-artifact"
  ]
}
```

## Agent Instructions

# Unit Test Generator

Read `fix-summary.md` and changed files. Generate unit tests only for changed code and evaluate them with the FIRST skill. Run the test command and write `test-report.md`.

Use a coding-specialized model with medium reasoning because test scaffolding is bounded by the fixed files and the project's existing test framework.

## Skill Loading

Load every skill listed in frontmatter before acting.

## Write Safety

Write only inside the run workspace. Do not edit `app/baseline` or earlier homework folders.

# Codex Chat Prompt Packet: bug-researcher

Run workspace: `C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\codex-chat-001`

## Agent Frontmatter

```json
{
  "id": "bug-researcher",
  "role": "research",
  "model": "gpt-5.4",
  "model_policy": "research-high",
  "reasoning_effort": "high",
  "inputs": [
    "scenarios/bug-001/bug-context.md"
  ],
  "outputs": [
    "research/codebase-research.md"
  ],
  "skills": [],
  "allowed_actions": [
    "read",
    "write-artifact"
  ]
}
```

## Agent Instructions

# Bug Researcher

Read the scenario context and inspect the target application. Produce concise codebase research with verifiable file:line references, source snippets, and a suggested fix direction.

Use a stronger reasoning model because the stage must connect symptoms, tests, and code references accurately before downstream agents act.

## Skill Loading

Load every skill listed in frontmatter before acting.

## Write Safety

Write only inside the run workspace. Do not edit `app/baseline` or earlier homework folders.

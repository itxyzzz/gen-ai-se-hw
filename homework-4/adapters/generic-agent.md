# Generic Agentic Tool Adapter

Use this adapter only when the active tool has no dedicated adapter and can still read files, edit a run workspace, run local commands, and write Markdown and JSON artifacts.

## Mapping

- Load `skills/pipeline-harness-wrapper.md` as the workflow controller.
- Treat `agents/*.agent.md` as ordered stage prompts.
- Load skills listed in agent frontmatter before each stage.
- Keep source edits inside the current run's `app/` directory before promotion. Keep `app/baseline` immutable.
- Record missing model controls or command limitations in `run-metadata.json`.
- Capable tools are directed to inspect and implement the `## Reusable Agentic Execution Extensions` (subagents, reflection loops, and local static tools) defined in `skills/pipeline-harness-wrapper.md` if their active environment supports them.
- Populate `runtimeSubagentAudit` in `run-metadata.json` using the best runtime evidence the tool exposes. Use `adapter-recorded` when only orchestrator notes are available, or `manual-unavailable` with a reason when reliable runtime evidence cannot be exposed.

## Model Guidance

When no dedicated model selections exist, follow this reasoning-tier guidance:

- For `*-high` policies (`research-high`, `verification-high`, `planning-high`, `security-high`): Use the active tool's most capable high-reasoning model (e.g., Gemini 1.5/3.5 Pro, Claude 3.5 Sonnet, GPT-4o/5).
- For `*-medium` policies (`implementation-medium`, `test-medium`): Use the active tool's fast, high-throughput coding model (e.g., Gemini Flash, Claude Haiku, GPT-4o-mini).

## When Not To Use

Do not use this adapter if the tool cannot safely edit the current run workspace, cannot inspect required files, or cannot produce the artifact contract. In that case, ask the user which supported tool or adapter they want to use.

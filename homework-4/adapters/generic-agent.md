# Generic Agentic Tool Adapter

Use this adapter only when the active tool has no dedicated adapter and can
still read files, edit a run workspace, run local commands, and write Markdown
and JSON artifacts.

## Mapping

- Load `skills/pipeline-harness-wrapper.md` as the workflow controller.
- Treat `agents/*.agent.md` as ordered stage prompts.
- Load skills listed in agent frontmatter before each stage.
- Keep source edits inside the current run's `app/` directory before promotion.
- Record missing model controls or command limitations in `run-metadata.json`.

## When Not To Use

Do not use this adapter if the tool cannot safely edit the current run
workspace, cannot inspect required files, or cannot produce the artifact
contract. In that case, ask the user which supported tool or adapter they want
to use.

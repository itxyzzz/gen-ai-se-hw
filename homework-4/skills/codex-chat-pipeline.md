# Codex Chat Pipeline Skill

Use this skill when executing the pipeline through a chat-based coding agent instead of the live SDK adapter.

## Procedure

1. Read the prepared prompt packet for the target stage.
2. Confirm the run workspace path before editing.
3. Apply file writes only inside that run workspace.
4. Run the exact command listed in the stage prompt.
5. Write the required artifact before moving to the next stage.
6. Record manual intervention in `run-metadata.json`.

## Validation

A Codex chat run is valid only when it produces the same required artifact contract as the SDK or mock adapter:

- `run-metadata.json`
- `app/`
- `patch.diff`
- `research/verified-research.md`
- `fix-summary.md`
- `security-report.md`
- `test-report.md`
- `command-log.md`

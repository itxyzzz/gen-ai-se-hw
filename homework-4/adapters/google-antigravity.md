# Google Antigravity Adapter

## Launch Phrase

```text
Run Homework 4 bug-001 through the full agentic pipeline using the Google Antigravity adapter.
```

## Mapping

- Use `skills/pipeline-harness-wrapper.md` as the workflow controller.
- Use `agents/*.agent.md` as the ordered agent definitions.
- Use `skills/*.md` as reusable evaluation rules.
- Use the same scenario, run workspace, artifact names, and validation checklist
  as the Codex Chat adapter.

## Execution Rules

- Run the stages in order and do not skip helper stages.
- Keep edits inside the selected run workspace until promotion.
- Record model/tool limitations in `run-metadata.json`.
- Preserve the same artifact contract so results can be compared across tools.

## Validation Checklist

- Adapter name in metadata is `google-antigravity`.
- Required artifacts are complete.
- Security report is read-only.
- Unit test report includes FIRST assessment.

## Limitations

This is a portability contract for Google Antigravity-style agentic tools, not a
submitted executable integration.

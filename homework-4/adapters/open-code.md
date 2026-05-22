# Open Code Adapter

## Launch Phrase

```text
Run Homework 4 bug-001 through the full agentic pipeline using the Open Code adapter.
```

## Mapping

- Treat `skills/pipeline-harness-wrapper.md` as the root workflow skill.
- Treat each `agents/*.agent.md` file as the stage-specific instruction block.
- Load skills by path when a stage lists them in frontmatter.
- Use the current run workspace selected by the wrapper unless a new run id is
  explicitly requested.

## Execution Rules

- Execute the six stages in the harness order.
- Write Markdown and JSON artifacts directly; do not generate helper scripts.
- Record unavailable model controls honestly in `run-metadata.json`.
- Keep `app/baseline` immutable.

## Validation Checklist

- Adapter name in metadata is `open-code`.
- All required artifacts are present.
- Tests were run or a blocker is recorded.
- The benchmark rubric can be applied manually to the completed artifacts.

## Limitations

Open Code support is an instruction mapping. Reviewers do not need Open Code to
run the Codex-first submission.

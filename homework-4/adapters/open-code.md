# Open Code Adapter

This adapter configures the Homework 4 agentic pipeline for execution under **Open Code** or similar open-source model orchestrators, utilizing leading open-source model families.

## Launch Phrase

```text
Run Homework 4 bug-001 through the full agentic pipeline using the Open Code adapter.
```

## Model Selection

Concrete open-source model policies:

| Model policy | Open-Source Model | Reasoning / Effort |
| --- | --- | --- |
| `research-high` | llama-3.3-70b-instruct / Qwen-2.5-Coder-32B-Instruct | High reasoning for deep codebase analysis |
| `verification-high` | llama-3.3-70b-instruct / Qwen-2.5-Coder-32B-Instruct | High reasoning for research verification |
| `planning-high` | llama-3.3-70b-instruct / Qwen-2.5-Coder-32B-Instruct | High reasoning for implementation planning |
| `implementation-medium` | llama-3.1-8b-instruct / Qwen-2.5-Coder-7B-Instruct | Fast and efficient for code edits |
| `security-high` | llama-3.3-70b-instruct / Qwen-2.5-Coder-32B-Instruct | High reasoning for security audit |
| `test-medium` | llama-3.1-8b-instruct / Qwen-2.5-Coder-7B-Instruct | Fast and reliable for unit tests |

## Mapping

- Treat `skills/pipeline-harness-wrapper.md` as the root workflow skill.
- Treat each `agents/*.agent.md` file as the stage-specific instruction block.
- Load skills by path when a stage lists them in frontmatter.
- Use the current run workspace selected by the wrapper unless a new run id is explicitly requested.

## Execution Rules

- Execute the six stages in harness order.
- Write Markdown and JSON artifacts directly; do not generate helper scripts.
- Record unavailable model controls honestly in `run-metadata.json`.
- Keep `app/baseline` immutable.
- Capable open-source orchestrators are encouraged to utilize isolated sub-agents and automated test reflection loops (up to 3 iterations) as specified in the harness's optional agentic extensions section.

## Validation Checklist

- Adapter name in metadata is `open-code`.
- All required artifacts are present and non-empty.
- Tests were run or a blocker is recorded in `command-log.md`.
- The benchmark rubric can be applied manually to the completed artifacts.

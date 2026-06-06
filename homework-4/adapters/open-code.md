# Open Code Adapter

This adapter configures the Homework 4 pipeline for **Open Code** in mixed model environments, including Codex/OpenAI, Claude, and free/open model providers.

## Launch Phrase

```text
Run HW4 pipeline
```

In Open Code, this launch phrase is the single-command entry point for the text harness. It is a prompt command phrase rather than a shell command.

## Model Selection

Concrete model names live in adapters, not portable agent specs.
For each `model_policy`, choose the first available candidate from left to right.

| Model policy | Preferred candidates (ordered) | Reasoning / Effort |
| --- | --- | --- |
| `research-high` | `gpt-5.4` -> `claude-3-5-sonnet` -> `qwen-2.5-coder-32b-instruct` / `llama-3.3-70b-instruct` | High reasoning for deep codebase analysis |
| `verification-high` | `gpt-5.4` -> `claude-3-5-sonnet` -> `qwen-2.5-coder-32b-instruct` / `llama-3.3-70b-instruct` | High reasoning for strict research verification |
| `planning-high` | `gpt-5.4` -> `claude-3-5-sonnet` -> `qwen-2.5-coder-32b-instruct` / `llama-3.3-70b-instruct` | High reasoning for implementation planning |
| `implementation-medium` | `gpt-5.3-codex` -> `claude-3-5-haiku` -> `qwen-2.5-coder-7b-instruct` / `llama-3.1-8b-instruct` | Medium reasoning for bounded code edits |
| `security-high` | `gpt-5.4` -> `claude-3-5-sonnet` -> `qwen-2.5-coder-32b-instruct` / `llama-3.3-70b-instruct` | High reasoning for security review |
| `test-medium` | `gpt-5.3-codex` -> `claude-3-5-haiku` -> `qwen-2.5-coder-7b-instruct` / `llama-3.1-8b-instruct` | Medium reasoning for unit test generation |

Provider aliases can differ by platform. Use the closest equivalent available model and record the actual selected name.

## Model Fallback Rules

1. Resolve each stage model from that stage's `model_policy` in `agents/*.agent.md`.
2. Attempt candidates in the listed order until one is available.
3. If all listed candidates are unavailable, use the strongest available model that matches the policy tier (`high` or `medium`).
4. Record model decisions in `run-metadata.json` for each stage:
   - `modelPolicy`
   - `model`
   - `reasoning`
   - `fallback` (`true`/`false`)
   - `fallbackReason` (required when `fallback` is `true`)

## Mapping

- Treat `skills/pipeline-harness-wrapper.md` as the root workflow controller.
- Treat each `agents/*.agent.md` file as the stage-specific instruction block.
- Use the current run workspace selected by the wrapper unless a new run id is explicitly requested.
- Stage skills listed in agent frontmatter under `skills/` are local markdown requirements, not platform-level skill-tool names.

## Skill Handling in Open Code

- Superpowers process skills (for example brainstorming, planning, debugging, verification) are loaded through the `skill` tool by skill name.
- Homework stage skills (`skills/research-quality-measurement.md` and `skills/unit-tests-FIRST.md`) must be read as repository files and enforced in stage artifacts.

## Open Code Tooling Guidance

- File discovery: `glob`
- Content search: `grep`
- File reads: `read`
- File edits: `apply_patch` for focused edits
- Commands and tests: `bash`
- Optional stage isolation: `task` subagents when available

## Runtime Sub-Agent Audit

Populate `runtimeSubagentAudit` in `run-metadata.json`. Use
`collectionMode: "plugin-event"` when Open Code plugin events such as
`tool.execute.before` and `tool.execute.after` capture task-subagent execution.
When plugin capture is not configured but the adapter records task usage in the
orchestration flow or command log, use `collectionMode: "adapter-recorded"`.

Each audit event should identify the stage, agent file, task/subagent launch
mechanism, selected model, fallback details when relevant, status, and evidence
source. If Open Code cannot expose reliable runtime task evidence, use
`collectionMode: "manual-unavailable"` with the reason and the planned
policy-relative model information.

## Execution Rules

- Execute the six stages in harness order.
- Keep `app/baseline` immutable.
- Apply code changes only inside the selected run workspace `app/` directory before promotion.
- Write Markdown and JSON artifacts directly without helper scripts.
- For `bug-fixer` and `unit-test-generator`, run tests after meaningful edits when command execution is available.
- Reflection loop limit is 3 attempts; capture each failure and retry in `command-log.md`.
- If blocked, record the blocker in both `command-log.md` and `run-metadata.json`.

## Validation Checklist

- Adapter name in metadata is `open-code`.
- All required artifacts are present and non-empty.
- `run-metadata.json` includes per-stage model selection and fallback details when fallback is used.
- `run-metadata.json` contains `runtimeSubagentAudit`.
- Tests were run or a blocker is recorded in `command-log.md`.
- Security verifier remains report-only and does not edit code.
- The benchmark rubric can be applied manually to the completed artifacts.

# Codex Chat Adapter

## Launch Intent

Use this adapter when the active tool is Codex. Exact wording is handled by
`homework-4/AGENTS.md` and `skills/pipeline-harness-wrapper.md`.

## Model Selection

Concrete model names live here, not in portable agent files.

| Model policy | Codex Chat model | Reasoning |
| --- | --- | --- |
| `research-high` | `gpt-5.4` | high |
| `verification-high` | `gpt-5.4` | high |
| `planning-high` | `gpt-5.4` | high |
| `implementation-medium` | `gpt-5.3-codex` | medium |
| `security-high` | `gpt-5.4` | high |
| `test-medium` | `gpt-5.3-codex` | medium |

## Context Loading

Codex must read `skills/pipeline-harness-wrapper.md`, this adapter, the scenario
files, all agent specs, and any skills named in agent frontmatter before
modifying files.

## Execution Rules

- Treat detected Homework 4 pipeline intent as authorization to run all six
  stages in order.
- Use each `agents/*.agent.md` file as the stage prompt.
- Use `skills/codex-chat-pipeline.md` for the global run procedure.
- Apply code changes only inside the current run's `app/` directory unless
  promoting the verified fixed app to `app/current`.
- Write each required artifact before starting the next dependent stage.
- Record tool commands and manual decisions in `command-log.md`.

## Validation Checklist

- `research-verifier` uses `skills/research-quality-measurement.md`.
- `unit-test-generator` uses `skills/unit-tests-FIRST.md`.
- `security-verifier` writes only `security-report.md`.
- `run-metadata.json` uses adapter `codex-chat`, selected concrete models, and a
  folder name containing the adapter and primary model.
- Current app tests pass after promotion.

## Limitations

Codex chat is interactive rather than headless. The proof is the preserved
artifact set and command log, not an executable adapter script.

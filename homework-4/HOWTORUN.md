# How To Run Homework 4

## Prerequisites

- Node.js 24 or newer.
- PowerShell, Bash, or Command Prompt.
- No npm install step is required because the homework uses Node built-ins only.

## Baseline Verification

The baseline app is supposed to fail its behavior tests:

```powershell
npm run app:baseline:test
```

Expected result: exit code 1 with failures for line total, `SAVE10`, and catalog traversal behavior.

Use the dedicated verifier for a passing seeded-defect check:

```powershell
npm run verify:baseline
```

Expected result: exit code 0 and a message listing the reproduced seeded failures.

## Run The Pipeline

```powershell
npm run pipeline:mock -- --scenario bug-001 --run run-001
```

Expected result: a complete run folder under `runs/bug-001/run-001`.

The deterministic run records all six configured stages, loaded skills, model policies, command logs, reports, and patch output.

To attempt the live OpenAI SDK path:

```powershell
npm run pipeline:openai -- --scenario bug-001 --run openai-live-001 --model gpt-5.3-codex --reasoning high
```

If `OPENAI_API_KEY` or SDK support is unavailable, the run is marked `blocked` honestly in metadata.

## Codex Chat Workflow

```powershell
npm run pipeline:codex-chat:prepare -- --scenario bug-001 --run codex-chat-001
```

Prompt packets are written to `runs/bug-001/codex-chat-001/codex-chat-prompts`.

After externally completing the artifacts, validate them with:

```powershell
npm run pipeline:codex-chat:validate -- --scenario bug-001 --run codex-chat-001
```

## Promote And Test

```powershell
npm run promote -- --scenario bug-001 --run run-001
npm run app:current:test
npm test
```

Expected result: all current app and harness tests pass.

## Compare Runs

```powershell
npm run compare -- --scenario bug-001
```

Expected result: benchmark JSON, Markdown comparison, and rubric files are generated in `benchmark/`.

## Demo Helpers

- Windows: `demo/run.bat`
- Bash: `demo/run.sh`
- Manual command notes: `demo/sample-requests.http`

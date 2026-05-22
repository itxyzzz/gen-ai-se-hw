# API Reference

Homework 4 is a CLI and file-artifact pipeline. It does not expose an HTTP API.

## Application CLI

Run from `app/current` after promotion:

```powershell
node src/cli.js --item WIDGET:2 --item GADGET:1 --discount SAVE10
```

Arguments:

| Argument | Required | Description |
| --- | --- | --- |
| `--item SKU:quantity` | yes | Adds one catalog item to the quote. May be repeated. |
| `--discount SAVE10` | no | Applies a 10 percent discount. |
| `--catalog name` | no | Loads a catalog by safe identifier. Defaults to `default`. |

## Pipeline Commands

| Command | Purpose |
| --- | --- |
| `npm run verify:baseline` | Confirms seeded baseline issues are present. |
| `npm run pipeline:mock` | Runs the deterministic one-command pipeline. |
| `npm run pipeline:openai` | Attempts live OpenAI SDK execution or records a blocked run. |
| `npm run pipeline:codex-chat:prepare` | Writes Codex chat prompt packets. |
| `npm run pipeline:codex-chat:validate` | Validates a Codex chat run contract. |
| `npm run promote` | Copies a selected run app to `app/current`. |
| `npm run compare` | Regenerates benchmark files from run metadata. |

## Run Metadata Schema

Required fields:

| Field | Description |
| --- | --- |
| `runId` | Run folder name under the scenario. |
| `scenarioId` | Scenario such as `bug-001`. |
| `adapter` | `mock`, `openai-sdk`, or `codex-chat`. |
| `provider` | Model/runtime provider label. |
| `model` | Selected model or deterministic mock label. |
| `reasoningEffort` | Requested or selected reasoning effort. |
| `status` | `completed`, `blocked`, `prepared`, or validation state. |
| `metrics` | Bug, security, test, and verification measurements. |
| `promoted` | Whether this run was copied to `app/current`. |

## Artifact Contract

Every complete run must include:

- `run-metadata.json`
- `app/`
- `patch.diff`
- `research/verified-research.md`
- `fix-summary.md`
- `security-report.md`
- `test-report.md`
- `command-log.md`

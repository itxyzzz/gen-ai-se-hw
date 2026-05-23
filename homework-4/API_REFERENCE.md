# Prompt And Artifact Contract

Homework 4 does not expose an HTTP API. Its public interface is a prompt
contract plus file artifacts.

## Primary Prompt

```text
Run HW4 pipeline
```

## Adapter Prompts

| Adapter | Prompt |
| --- | --- |
| Codex Chat | `Run HW4 pipeline` |
| Claude Code | `Run HW4 pipeline` |
| Open Code | `Run HW4 pipeline` |
| Google Antigravity | `Run HW4 pipeline` |
| Generic agentic tool | `Run HW4 pipeline` |

Adapter selection is determined by active tool context according to `homework-4/AGENTS.md`.

## Stage Contract

| Stage | Agent file | Required output |
| --- | --- | --- |
| Bug Researcher | `agents/bug-researcher.agent.md` | `research/codebase-research.md` |
| Research Verifier | `agents/research-verifier.agent.md` | `research/verified-research.md` |
| Bug Planner | `agents/bug-planner.agent.md` | `implementation-plan.md` |
| Bug Fixer | `agents/bug-fixer.agent.md` | `fix-summary.md` |
| Security Verifier | `agents/security-verifier.agent.md` | `security-report.md` |
| Unit Test Generator | `agents/unit-test-generator.agent.md` | `test-report.md` |

## Run Metadata Fields

| Field | Description |
| --- | --- |
| `runId` | Normalized run folder name under the scenario: `run-<NNN>-<tool>-<pattern>`. |
| `scenarioId` | Scenario such as `bug-001`. |
| `adapter` | Text adapter used for the run, such as `codex-chat`. |
| `provider` | Tool/provider label. |
| `model` | Model or chat environment label. |
| `runFolderName` | Same value as `runId`; filesystem-safe folder name including tool and primary model or pattern. |
| `reasoningEffort` | Requested or selected reasoning depth. |
| `status` | `completed` or a clearly documented blocked state. |
| `pipelineCommand` | The one-phrase launch prompt. |
| `metrics` | Bug, security, test, and verification measurements. |
| `promoted` | Whether this run was copied to `app/current`. |

## Required Run Artifacts

- `run-metadata.json`
- `app/`
- `patch.diff`
- `research/verified-research.md`
- `implementation-plan.md`
- `fix-summary.md`
- `security-report.md`
- `test-report.md`
- `command-log.md`

## Normalized Benchmark Artifacts

Completed evidence snapshots under `runs/<scenario>/` are preserved. Benchmark
normalization and repaired comparison metadata live under
`benchmark/<scenario>/runs/run-<NNN>-<tool>-<pattern>/`.

## Application CLI

Run from `homework-4/app/current`:

```powershell
node src/cli.js --item WIDGET:2 --item GADGET:1 --discount SAVE10
```

Arguments:

| Argument | Required | Description |
| --- | --- | --- |
| `--item SKU:quantity` | yes | Adds one catalog item to the quote. May be repeated. |
| `--discount SAVE10` | no | Applies a 10 percent discount. |
| `--catalog name` | no | Loads a catalog by safe identifier. Defaults to `default`. |

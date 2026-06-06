# Prompt And Artifact Contract

Homework 4 does not expose an HTTP API. Its public interface is a prompt
contract plus file artifacts.

## Primary Prompt

```text
Run HW4 pipeline
```

This prompt is the full pipeline launch contract. It requires sub-agent stage
execution when the active tool can spawn sub-agents; no additional prompt
keywords or default confirmation are required for the happy path.

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
| `runtimeSubagentAudit` | Portable evidence of de-facto sub-agent use and runtime model visibility for the run. |

`runtimeSubagentAudit.collectionMode` must be one of:

| Value | Meaning |
| --- | --- |
| `native-hook` | Native hook or lifecycle event captured runtime sub-agent execution. |
| `plugin-event` | Plugin event captured runtime sub-agent execution. |
| `adapter-recorded` | Adapter or orchestrator recorded runtime choices because native events were unavailable, incomplete, or non-portable. |
| `manual-unavailable` | The tool cannot expose reliable runtime sub-agent evidence; `unavailableReason` must explain why. |

Audit events should stay compact. They record stage id, agent file, whether a
sub-agent was used, launch mechanism, model policy, requested or observed
model, reasoning effort, status, and evidence source when the active tool
exposes those details.

`runtimeSubagentAudit.operatorAuthorization` records why the run did or did not
use sub-agents:

| Field | Description |
| --- | --- |
| `required` | `true` for HW4 pipeline runs. |
| `status` | Authorization/fallback state for sub-agent spawning. |
| `source` | Short description of the launch phrase, tool-required authorization request, or fallback approval. |
| `fallbackApproved` | `true` only when the operator explicitly approved direct execution fallback. |

Allowed `operatorAuthorization.status` values:

| Value | Meaning |
| --- | --- |
| `pipeline-mandated` | `Run HW4 pipeline` launched the normal contract and sub-agents were spawned without extra confirmation. |
| `authorized-after-tool-gate` | The active tool refused to spawn without explicit operator authorization, the orchestrator asked for authorization, and the operator approved spawning. |
| `fallback-approved` | Direct execution was used only after sub-agent tooling was unavailable or still unusable after the authorization path, and the operator explicitly approved fallback. |
| `declined` | The operator declined required spawn authorization or declined direct fallback, so the run blocked if a run folder already existed. |

Not being authorized is not equivalent to unavailable tooling. If the tool can
spawn sub-agents but requires explicit authorization, the orchestrator must ask
for authorization to spawn them before any direct fallback is considered.

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

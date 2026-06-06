# How To Run Homework 4

## Prerequisites

- Node.js 24 or newer for the sample app tests.
- Codex chat for the canonical one-phrase pipeline run (or Open Code or other compatible agentic tools).
- No install step is required.

## Canonical Codex Run

Paste this phrase into Codex chat from the repository root:

```text
Run HW4 pipeline
```

Expected behavior:

- Codex reads `homework-4/skills/pipeline-harness-wrapper.md`.
- Codex loads `homework-4/adapters/codex-chat.md`.
- Codex uses sub-agents for the pipeline stages when the active Codex tooling
  exposes sub-agent spawning.
- Codex runs all six stages in order.
- Codex writes or refreshes the required artifacts under a normalized run folder
  such as `homework-4/runs/bug-001/run-007-codex-chat-gpt-5.5`.
- Codex updates the fixed app evidence in `homework-4/app/current` only after
  required reports are complete.
- The run metadata includes `runtimeSubagentAudit`, recording whether
  sub-agents actually ran and which collection mode supplied the evidence.

The phrase above is sufficient. You do not need to add extra sub-agent keywords
or confirm a default follow-up in tools that can spawn sub-agents normally. If a
tool refuses to spawn sub-agents until you explicitly authorize it, the agent
must ask you to authorize spawning and then continue with sub-agents when
approved.

## Portable Launch Phrases

Use the same short launch phrase across tools:

```text
Run HW4 pipeline
```

Adapter selection is automatic from active tool context (`homework-4/AGENTS.md`):

- Codex -> `adapters/codex-chat.md`
- Claude Code -> `adapters/claude-code.md`
- Open Code -> `adapters/open-code.md`
- Google Antigravity -> `adapters/google-antigravity.md`
- Other capable tools -> `adapters/generic-agent.md`

Each adapter preserves the same stage order and artifact contract.

Direct execution without sub-agents is not the normal pipeline. It is allowed
only when sub-agent tooling is unavailable, or still unusable after the tool's
authorization path, and you explicitly approve fallback for that run.

## Verify The Fixed App

Run from the repository root:

```powershell
node --test --test-isolation=none homework-4/app/current/tests/*.test.js
```

Expected result: all current app tests pass.

## Verify The Buggy Baseline

Run from the repository root:

```powershell
node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js
```

Expected result: the command fails because the baseline intentionally preserves:

- line total addition instead of multiplication;
- flat `SAVE10` subtraction instead of a percentage discount;
- unsafe catalog path traversal behavior.

## Run The Fixed CLI

```powershell
cd homework-4/app/current
node src/cli.js --item WIDGET:2 --item GADGET:1 --discount SAVE10
```

Expected result: the quote prints with multiplied line totals and a 10 percent
discount.

## Review Artifacts

- Normalized benchmark view: `benchmark/bug-001/`
- Immutable source snapshots: `runs/bug-001/`
- Harness skill: `skills/pipeline-harness-wrapper.md`
- Codex adapter: `adapters/codex-chat.md`
- Comparison rubric: `benchmark/scoring-rubric.md`

After a new run, inspect `run-metadata.json` and confirm
`runtimeSubagentAudit.collectionMode`, `subagentsUsed`,
`operatorAuthorization.status`, and the per-stage events. Expected sub-agent
statuses are `pipeline-mandated` or `authorized-after-tool-gate`. If direct
fallback occurred, the status must be `fallback-approved` and the metadata must
state why sub-agent tooling was unavailable or still unusable.

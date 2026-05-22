# How To Run Homework 4

## Prerequisites

- Node.js 24 or newer for the sample app tests.
- Codex chat for the canonical one-phrase pipeline run.
- No install step is required.

## Canonical Codex Run

Paste this phrase into Codex chat from the repository root:

```text
Run HW4 pipeline
```

Expected behavior:

- Codex reads `homework-4/skills/pipeline-harness-wrapper.md`.
- Codex loads `homework-4/adapters/codex-chat.md`.
- Codex runs all six stages in order.
- Codex writes or refreshes the required artifacts under
  `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-001`.
- Codex updates the fixed app evidence in `homework-4/app/current` only after
  required reports are complete.

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

- Canonical run: `runs/bug-001/codex-chat-gpt-5.4-run-001`
- Harness skill: `skills/pipeline-harness-wrapper.md`
- Codex adapter: `adapters/codex-chat.md`
- Comparison rubric: `benchmark/scoring-rubric.md`

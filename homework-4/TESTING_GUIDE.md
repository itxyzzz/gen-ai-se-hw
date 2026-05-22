# Testing Guide

## Test Strategy

```mermaid
flowchart LR
  B["Baseline expected failures"] --> R["Text pipeline artifacts"]
  R --> C["Current app tests"]
  R --> Q["Report quality review"]
```

The pipeline itself is a text instruction hierarchy, so verification focuses on
the sample app behavior and the required artifact contract.

## Command Matrix

Run commands from the repository root.

| Command | Expected Result |
| --- | --- |
| `node --test --test-isolation=none homework-4/app/current/tests/*.test.js` | Passes for the fixed app. |
| `node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js` | Fails for the seeded baseline defects. |
| `node homework-4/app/current/src/cli.js --item WIDGET:2 --item GADGET:1 --discount SAVE10` | Prints a fixed quote. |

## Artifact Review Checklist

- `skills/pipeline-harness-wrapper.md` contains the canonical launch intent.
- `adapters/codex-chat.md`, `adapters/claude-code.md`, `adapters/open-code.md`,
  `adapters/google-antigravity.md`, and `adapters/generic-agent.md` exist.
- All four required agents exist and name their inputs and outputs.
- Research verifier references `skills/research-quality-measurement.md`.
- Unit test generator references `skills/unit-tests-FIRST.md`.
- `runs/bug-001/codex-chat-gpt-5.4-run-001` contains metadata, app, patch, verified research, fix
  summary, security report, test report, and command log.
- `run-metadata.json` identifies adapter `codex-chat`.

## Current App Coverage

Current app tests cover:

- multiplication-based line totals;
- percentage-based `SAVE10`;
- catalog traversal rejection;
- generated regression coverage for combined total/discount behavior.

## Baseline Failure Evidence

The baseline command is expected to return a nonzero exit code. That result
proves the input app still contains the intentional defects that the pipeline is
meant to fix.

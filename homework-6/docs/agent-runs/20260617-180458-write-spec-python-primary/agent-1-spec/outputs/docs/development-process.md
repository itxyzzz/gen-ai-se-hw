# Development Process

Run ID: `20260617-180458-write-spec-python-primary`
Stack: `python`

## Purpose

This document gives later Homework 6 agents a portable process for implementing the selected specification without relying on hidden chat state, `dev-doc-harness`, or Superpowers. When those tools are available, use them as additional guardrails; the homework artifacts remain the source of truth.

## Agent Sequence

1. Agent 1: maintain the selected specification package and run evidence.
2. Agent 2: implement the Python pipeline and document Context7 research.
3. Agent 3: implement tests, coverage gate, `/run-pipeline`, and `/validate-transactions`.
4. Agent 4: create README, HOWTORUN, architecture/testing docs, screenshots, and PR evidence.

## Common Preflight

Before editing, each agent should:

- Confirm the branch is `homework-6-submission`.
- Read `TASKS.md`, `agents.md`, `specification.md`, and any active run/handoff artifacts.
- Inspect current git status and avoid unrelated changes.
- Identify which homework task and agent role the step serves.
- Update `CHANGELOG.md` before commit or PR closure.

## Implementation Gates

Agent 2 should implement the pipeline in small slices:

1. Shared directory setup and sample loading.
2. Transaction validator.
3. Fraud detector.
4. Settlement or reporting processor.
5. Integrator orchestration and summary report.
6. Context7 research notes.

Agent 3 should verify behavior before adding presentation evidence:

1. Unit tests for each agent.
2. Integration test for full pipeline.
3. Coverage command and blocking hook below 80%.
4. `/run-pipeline` command.
5. `/validate-transactions` command.

Agent 4 should produce documentation after runnable behavior exists:

1. README with student name and architecture diagram.
2. HOWTORUN with exact setup, pipeline, test, hook, and MCP commands.
3. Architecture/testing docs as needed.
4. Required screenshots under `docs/screenshots/`.
5. PR description evidence links.

## Verification Practices

- Run `python -m pytest` after code/test changes.
- Run `python -m pytest --cov=. --cov-fail-under=80` before committing coverage-gated work.
- Run `python integrator.py` before claiming the pipeline works.
- Confirm every input transaction has a result under `shared/results/`.
- Confirm no plaintext account identifiers appear in logs, audit files, or documentation examples.
- Confirm MCP configuration references `pipeline-status` only after `mcp/server.py` exists.

## Handoff Practices

When pausing at a boundary, write or update a handoff that states:

- Completed files.
- Validation already run.
- Known risks.
- Next exact prompt or bounded next task.

Major generated outputs should be preserved under `docs/agent-runs/` before canonical copy or final selection.

## Review Checklist

- Required Task 1-5 deliverables remain findable.
- Pipeline behavior matches `specification.md`.
- Money uses `decimal.Decimal` and never binary floating point for amounts.
- Currency validation rejects `XYZ`.
- Negative amount validation rejects `TXN007`.
- Risk-review logic is deterministic and simulation-scoped.
- Audit/log examples redact account identifiers.
- Coverage gate blocks below 80%.
- README includes the student's name.
- Screenshots are current and stored in `docs/screenshots/`.

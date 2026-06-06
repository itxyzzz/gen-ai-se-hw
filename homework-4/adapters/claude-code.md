# Claude Code Adapter

This adapter configures the Homework 4 agentic pipeline for execution under the **Claude Code** agentic tool environment, utilizing Claude model families and mandatory subagent stage execution when the tooling is available.

## Launch Phrase

```text
Run Homework 4 bug-001 through the full agentic pipeline using the Claude Code adapter.
```

## Model Selection

Concrete Anthropic Claude model policies:

| Model policy | Claude Model | Reasoning / Effort |
| --- | --- | --- |
| `research-high` | claude-3-5-sonnet | High reasoning for deep codebase analysis |
| `verification-high` | claude-3-5-sonnet | High reasoning for research verification |
| `planning-high` | claude-3-5-sonnet | High reasoning for implementation planning |
| `implementation-medium` | claude-3-5-haiku | Fast and efficient for code edits |
| `security-high` | claude-3-5-sonnet | High reasoning for security audit |
| `test-medium` | claude-3-5-haiku | Fast and reliable for unit tests |

## Mapping

- Load `skills/pipeline-harness-wrapper.md` as the top-level workflow skill.
- Map each `agents/*.agent.md` file to a Claude Code agent or subagent prompt.
- Load `skills/research-quality-measurement.md` and `skills/unit-tests-FIRST.md` when those agents run.
- Use the run workspace and artifact contract defined in `skills/pipeline-harness-wrapper.md`.

## Execution Rules

- Execute the six stages sequentially in harness order: `bug-researcher`, `research-verifier`, `bug-planner`, `bug-fixer`, `security-verifier`, `unit-test-generator`.
- If the environment supports Claude subagents or back-processes, delegate the stages to independent subagents to isolate context and prevent distraction. Do not ask for an extra default confirmation; the HW4 launch contract already requires sub-agents.
- If Claude Code refuses to spawn because it requires explicit operator authorization, stop and ask the operator to authorize sub-agent spawning. Direct execution is allowed only if sub-agent tooling is unavailable, or still unusable after the authorization path, and the operator explicitly approves fallback for that run.
- Apply code changes only inside the current run's `app/` directory before promotion. Keep `app/baseline` immutable.
- Preserve all report names exactly so the same benchmark rubric can compare runs from different tools.
- If the environment permits automated command running, execute unit tests after code changes and perform self-correction if tests fail (up to 3 attempts), aligned with the harness's optional extensions.

## Runtime Sub-Agent Audit

Populate `runtimeSubagentAudit` in `run-metadata.json` with
`collectionMode: "native-hook"` when Claude Code hooks are configured. Capture
sub-agent launches with `PostToolUse` matched to the `Agent` tool, use
`SubagentStart` and `SubagentStop` for lifecycle or transcript evidence when
available, and use a `Stop` hook as the final completeness gate when available.

Map hook evidence into compact audit events. Include stage id, agent file,
subagent type, requested or observed model, reasoning effort, status, transcript
path, token usage, and notes when exposed. If hooks are unavailable, use
`collectionMode: "adapter-recorded"` for explicit orchestrator records, or
`manual-unavailable` with a reason.

Record `operatorAuthorization.status` as `pipeline-mandated`,
`authorized-after-tool-gate`, `fallback-approved`, or `declined`. Missing
authorization must trigger an authorization request for spawning, not silent
direct fallback.

## Validation Checklist

- Confirm all six stages ran in order.
- Confirm required artifacts exist and are complete.
- Confirm test commands and outcomes are recorded in `command-log.md`.
- Confirm `run-metadata.json` identifies adapter `claude-code` and concrete Claude models.
- Confirm `run-metadata.json` contains `runtimeSubagentAudit`.
- Confirm direct execution occurred only with explicit fallback approval or
  unavailable sub-agent tooling.

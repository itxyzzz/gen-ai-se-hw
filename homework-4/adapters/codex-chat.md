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

## Execution Rules & Pipeline Runner Procedure

1. **Trigger Authorization:** Treat detected Homework 4 pipeline intent as authorization to run all six stages in order without asking for per-stage prompts.
2. **Context Setup:**
   - Read `skills/pipeline-harness-wrapper.md` and this adapter file.
   - Confirm the current run workspace selected by the wrapper.
   - Read all six agent specifications from `agents/*.agent.md` before starting stage work.
3. **Sequence Execution:**
   - Run the six stages in harness order: `bug-researcher`, `research-verifier`, `bug-planner`, `bug-fixer`, `security-verifier`, `unit-test-generator`.
   - Use each `agents/*.agent.md` file as the stage instruction block.
   - Load stage skills (`skills/research-quality-measurement.md` for `research-verifier` and `skills/unit-tests-FIRST.md` for `unit-test-generator`).
4. **Workspace & Artifact Controls:**
   - Apply code changes only inside the selected run's `app/` directory. Keep baseline app (`app/baseline`) immutable.
   - Write each required artifact before starting the next dependent stage.
   - Record commands, manual decisions, and blockers in `command-log.md` and `run-metadata.json`.
5. **Promotion:**
   - Promote the verified fixed app from the run folder to `homework-4/app/current` only after reports and tests are complete.
   - Generate `patch.diff` between the baseline app and the current run app.
   - Record promotion in `command-log.md` and mark `"promoted": true` in `run-metadata.json`.

## Runtime Sub-Agent Audit

Populate `runtimeSubagentAudit` in `run-metadata.json`. Use native Codex hooks
only when the active Codex surface exposes reliable hook coverage and sub-agent
metadata. Otherwise record stage delegation decisions at the adapter or
orchestrator boundary with `collectionMode: "adapter-recorded"`.

If no reliable runtime sub-agent evidence is exposed, use
`collectionMode: "manual-unavailable"` with a clear unavailable reason and the
planned model policy evidence. Keep the audit compact; do not copy raw
transcripts or long stage outputs into metadata.

## Validation Checklist

- `research-verifier` uses `skills/research-quality-measurement.md`.
- `unit-test-generator` uses `skills/unit-tests-FIRST.md`.
- `security-verifier` writes only `security-report.md`.
- `run-metadata.json` uses adapter `codex-chat`, selected concrete models, and a
  folder name containing the adapter and primary model.
- `run-metadata.json` contains `runtimeSubagentAudit`.
- Current app tests pass after promotion.

## Limitations

Codex chat is interactive rather than headless. The proof is the preserved
artifact set and command log, not an executable adapter script.

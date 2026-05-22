# Homework 4 Pipeline Harness Skill

## Launch Intent

The shortest memorable launch phrase is:

```text
Run HW4 pipeline
```

Exact wording is not required. When the user intent is to run, launch, execute,
continue, or validate the Homework 4 agentic pipeline, the active assistant must
load this skill, choose the adapter for its own tool, load all agent specs,
required skills, scenario inputs, and then run the complete stage chain without
asking for manual per-agent invocation. Ask the user for adapter selection only
when no dedicated adapter exists and no generic mapping can be applied safely.

## Required Context To Load

Read these files before stage execution:

1. `AGENTS.md`
2. `HOMEWORK_STANDARDS.md`
3. `homework-4/AGENTS.md`
4. `homework-4/TASKS.md`
5. `homework-4/skills/pipeline-harness-wrapper.md`
6. the automatically selected adapter from `homework-4/adapters/`
7. `homework-4/skills/codex-chat-pipeline.md` when using Codex Chat
8. `homework-4/scenarios/bug-001/bug-context.md`
9. All `homework-4/agents/*.agent.md`
10. Required stage skills referenced by the agent frontmatter

## Stage Order

Run exactly:

1. `bug-researcher`
2. `research-verifier`
3. `bug-planner`
4. `bug-fixer`
5. `security-verifier`
6. `unit-test-generator`

The four assignment-required agents are `research-verifier`, `bug-fixer`,
`security-verifier`, and `unit-test-generator`. The helper stages preserve the
assignment's stated run order.

## Workspace Rules

- Baseline input: `homework-4/app/baseline`
- Current run workspace: `homework-4/runs/bug-001/<adapter>-<primary-model>-<run-id>`
- Submitted Codex evidence run: `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-001`
- New run folder naming: `<adapter>-<primary-model>-<run-id>`, lower-case and
  filesystem safe.
- Fixed app evidence: `homework-4/app/current`
- Do not edit `app/baseline`; it intentionally remains buggy.
- Code edits happen only inside the selected run workspace, then may be copied
  into `app/current` after the required reports are complete.
- Report-only stages must not edit code.

## Required Artifacts

Every completed run must contain:

- `run-metadata.json`
- `app/`
- `patch.diff`
- `research/verified-research.md`
- `implementation-plan.md`
- `fix-summary.md`
- `security-report.md`
- `test-report.md`
- `command-log.md`

`run-metadata.json` must include `runId`, `adapter`, `model`, and
`runFolderName`. The folder name must include the adapter and primary model so
the run is recognizable at a glance.

## Stop Conditions

Stop and record the blocker in `command-log.md` and `run-metadata.json` when:

- a required input file is missing;
- a stage cannot produce its required artifact;
- the Bug Fixer changes files outside the run workspace;
- tests fail after the final Unit Test Generator stage;
- the Security Verifier finds unresolved CRITICAL, HIGH, or MEDIUM issues in
  changed code.

## Promotion Rule

Promotion is a text-pipeline action, not a script. After the run is complete and
verified, copy the fixed run app into `homework-4/app/current`, keep
`run-metadata.json` marked `"promoted": true`, and record the action in
`command-log.md`.

## Completion Checklist

- All six stages are listed in `run-metadata.json`.
- Required skills are named in the stages that used them.
- Required artifacts exist and are non-empty.
- `app/current` matches the selected fixed run.
- Current app tests pass with:
  `node --test --test-isolation=none homework-4/app/current/tests/*.test.js`
- Baseline app tests fail for the seeded defects with:
  `node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js`

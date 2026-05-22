# Codex Chat Pipeline Skill

Use this skill when the Homework 4 pipeline is launched from one chat phrase.
This is the primary runner skill for the scaled-down submission.

## Trigger

Use this skill when Homework 4 pipeline intent is detected and the selected
adapter is Codex Chat. The canonical short launch alias is defined in
`skills/pipeline-harness-wrapper.md`; do not require exact wording from the user.

## Procedure

1. Read `skills/pipeline-harness-wrapper.md` and `adapters/codex-chat.md`.
2. Confirm the current run workspace selected by the wrapper.
3. Read all six agent specs before starting stage work.
4. Run stages in harness order without asking for per-agent prompts.
5. Load stage skills when the agent frontmatter lists them.
6. Apply code edits only inside the current run's `app/` directory.
7. Write the required artifact for each stage before moving to the next stage.
8. Promote the verified fixed app to `app/current` only after reports and tests
   are complete.
9. Record commands, manual decisions, and blockers in `command-log.md` and
   `run-metadata.json`.

## Required Artifact Contract

- `run-metadata.json`
- `app/`
- `patch.diff`
- `research/verified-research.md`
- `implementation-plan.md`
- `fix-summary.md`
- `security-report.md`
- `test-report.md`
- `command-log.md`

## Quality Gates

- Research verification must use `skills/research-quality-measurement.md`.
- Unit test generation must use `skills/unit-tests-FIRST.md`.
- Security review must not edit code.
- Final current-app tests must pass, or the run must be marked blocked.

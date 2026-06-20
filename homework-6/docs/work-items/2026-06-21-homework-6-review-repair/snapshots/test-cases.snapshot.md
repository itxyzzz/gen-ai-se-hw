# Homework 6 Review Repair Test Cases Snapshot

Work ID: `2026-06-21-homework-6-review-repair`
Status: Draft

## Static Acceptance Checks

| Area | Check | Expected result |
|---|---|---|
| Clio MCP evidence | Inspect `agent-control/generate-docs/` guidance | Requires both Context7 query/result evidence and custom `pipeline-status` MCP interaction evidence |
| Clio PR evidence | Inspect Clio workflow/quality bar | Requires regenerated PR drafts to account for spec produced, pipeline run, tests/coverage, skill/hook, MCP usage, and README with student name |
| Screenshot semantics | Inspect screenshot inventory and stable target notes | Distinct assignment screenshot names map to semantically correct evidence, or explicit accepted limitations are recorded |
| Coverage screenshot | Inspect screenshot mapping and notes | `test-coverage.png` represents current passing 80 percent coverage evidence; blocking/failure evidence belongs to `hook-trigger.png` |
| Fourth component decision | Inspect `agents.md` and Athena/Hephaestus control docs | Assignment minimum, current selected three-component package, and future four-component target are clearly separated |
| Claude project skill decision | Inspect `agents.md` and Athena control docs | `.claude/skills/write-spec/SKILL.md` is documented as intentional modern project-skill surface; `.claude/commands/write-spec.md` remains absent |
| Hephaestus `shared/` consistency | Inspect workflow, quality bar, and run registry | `shared/` is runtime evidence, may be preserved deliberately, and is excluded from selectable code/canonical copy targets |
| Harness terminology leak | Inspect `agent-control/generate-code/workflow.md` | No reference to "frozen Hephaestus planning package" remains |
| Follow-up boundary | Inspect Clio control guidance | Existing selected Clio output remains historical; canonical docs, screenshots, PR draft, and final-selection mapping are updated only by a separate Clio regeneration/selection step |
| Assignment files | Inspect diff | `TASKS.md`, root `HOMEWORK_STANDARDS.md`, and root `README.md` have no diff |

## Runtime Regression Checks

| Command | Expected result |
|---|---|
| `python integrator.py --shared-dir tmp\review-repair-shared` | Pipeline exits successfully and writes summary counts: total 8, settled 2, rejected 2, review_required 4, error 0 |
| `python -m pytest -p no:cacheprovider --basetemp=tmp\review-repair-pytest` | 50 tests pass |
| `python scripts\check_coverage_gate.py --fail-under 80 -- --basetemp=tmp\review-repair-coverage-pytest` | Coverage gate passes at or above 80 percent, or sandbox-specific coverage-file failure is rerun outside the sandbox and recorded |

## Privacy And Evidence Checks

| Area | Check | Expected result |
|---|---|---|
| Control docs | Scan changed control docs for raw account identifiers and sample descriptions | No raw sample account IDs or raw transaction descriptions are introduced |
| Existing Clio output | Inspect diff for canonical docs, screenshots, PR draft, selected Clio run, and final-selection records | No output; existing documentation package is not repaired in this work item |

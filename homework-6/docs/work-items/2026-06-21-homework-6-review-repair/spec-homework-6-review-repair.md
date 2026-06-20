# Homework 6 Review Repair Spec

Work ID: `2026-06-21-homework-6-review-repair`
Short ID: `homework-6-review-repair`
Status: Draft
Harness release: `unknown`
Schema: `schema:spec.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `rule:lifecycle.documentation-matrix`, `rule:quality.spec-handoff`

## Goal

Repair the Homework 6 documentation-generation and control-surface gaps accepted after the end-to-end review, while preserving the operator decisions that the fourth runtime component target and Claude Code project skills are intentional.

## Scope

- Update Clio (Documentation Generator) control guidance so future documentation packages produce complete Task 4/Task 5 screenshot evidence and PR support.
- Define the required follow-up Clio regeneration behavior, without repairing the existing selected Clio run output, canonical reviewer docs, screenshots, or PR draft in this work item.
- Require MCP evidence to show both Context7 usage provenance and the custom `pipeline-status` MCP tool/resource interaction, either in the required stable screenshot or in an explicitly documented paired evidence set.
- Require PR draft evidence for all assignment-named categories: spec produced, pipeline run, tests/coverage, skill and hook, MCP usage, and README with student name.
- Replace stale or misleading screenshot mappings where stable reviewer-facing paths point to the same source image or to a deliberate 99 percent coverage failure image instead of current passing coverage evidence.
- Document the intentional four-runtime-component quality target as a forward generation target and assignment-strengthening choice, without implying that the already selected three-component package is below the assignment minimum.
- Document the intentional Claude Code project-skill choice for Athena (Spec Writer), including why `.claude/skills/write-spec/SKILL.md` is the active modern surface even though the assignment example names `.claude/commands/write-spec.md`.
- Fix the Hephaestus (Code Generator) `shared/` evidence inconsistency so `shared/` is consistently described as runtime evidence that may be committed when deliberately preserved, but is never selectable code.
- Fix the Hephaestus workflow sentence that refers to a "frozen Hephaestus planning package" inside Homework Automation Layer guidance.

## Non-scope

- Do not modify `TASKS.md`, root `HOMEWORK_STANDARDS.md`, or root `README.md`.
- Do not regenerate Athena (Spec Writer), Hephaestus (Code Generator), or Themis (Test Generator) runs.
- Do not regenerate Clio (Documentation Generator) output, repair the existing selected Clio run output, update canonical reviewer-facing docs, change stable screenshots, or edit `docs/pr-description-draft.md`; that belongs to a separate follow-up Clio run/selection step.
- Do not change canonical runtime code, tests, MCP server behavior, command behavior, or hook behavior unless verification uncovers a direct evidence-generation blocker.
- Do not add a `.claude/commands/write-spec.md` compatibility wrapper; the operator decision is to document the newer Claude Code project-skill surface instead.
- Do not force the selected runtime system to add Reporting Agent during this repair. The selected system remains assignment-compliant with Transaction Validator, Fraud Detector, and Settlement Processor; the fourth-component target applies to refreshed generation guidance unless a later operator request explicitly authorizes runtime regeneration.
- Do not remove preserved operator-sourced screenshots, run-local evidence, or final-selection records.

## Current state

The end-to-end review found that the generated runtime, tests, coverage gate, MCP server, and main documentation set are broadly functional. Fresh verification from the review showed:

- `python integrator.py --shared-dir tmp\review-shared` completed with `total=8`, `settled=2`, `rejected=2`, `review_required=4`, and `error=0`.
- `python -m pytest -p no:cacheprovider --basetemp=tmp\review-pytest` passed with 50 tests.
- `python scripts\check_coverage_gate.py --fail-under 80 -- --basetemp=tmp\review-coverage-pytest-escalated` passed outside the sandbox with 94.79 percent total coverage.

The review also found evidence/package gaps:

- `docs/screenshots/mcp-interaction.png` currently maps to custom `pipeline-status` MCP evidence only; it does not visually show Context7 usage even though `research-notes.md` documents Context7 queries.
- `docs/pr-description-draft.md` embeds five required screenshot files but omits explicit spec-produced and README-with-name evidence categories required by `TASKS.md`.
- `docs/screenshots/pipeline-run.png` and `docs/screenshots/skill-run-pipeline.png` share the same source image, and `docs/screenshots/test-coverage.png` maps to a deliberate 99 percent failure demonstration rather than the current passing 80 percent gate evidence.
- Athena and Hephaestus control docs contain a four-component target while the selected canonical runtime package has three components. The three-component package satisfies the assignment minimum; the four-component target is an intentional stronger target for refreshed generation and must be documented as such.
- The active Athena Claude Code surface is a project skill under `.claude/skills/write-spec/SKILL.md`, not a legacy `.claude/commands/write-spec.md` wrapper. This is an intentional newer Claude Code project-skill decision and must be documented clearly for reviewers.
- Hephaestus guidance uses inconsistent language about whether `shared/` belongs under `agent-2-code/outputs/`.
- `agent-control/generate-code/workflow.md` contains a sentence that leaks harness planning terminology by referring to sub-agent approval from a "frozen Hephaestus planning package".

## Proposed behavior

Clio should become stricter about evidence completeness and provenance. Future Clio runs must treat assignment screenshot names as reviewer-facing deliverables with semantic meaning, not just file targets. A Clio run should reject or clearly block selection when:

- `mcp-interaction.png` or paired MCP evidence lacks either Context7 query/result evidence or custom `pipeline-status` MCP interaction evidence.
- `docs/pr-description-draft.md` omits any of the assignment-required screenshot categories.
- `pipeline-run.png`, `test-coverage.png`, or `skill-run-pipeline.png` points to stale, duplicate, or semantically mismatched source evidence without an explicit accepted limitation.
- `test-coverage.png` shows only a higher-threshold demonstration failure instead of current passing 80 percent coverage evidence.

This work item should stop at Clio and adjacent control-surface repair. The existing selected Clio documentation package remains historical evidence of the prior run. A separate follow-up step should run Clio again or otherwise select a refreshed documentation package after these control rules are fixed. That later step owns canonical reviewer-facing docs, stable screenshot files, selected Clio run outputs/inventories, `docs/pr-description-draft.md`, and any updated final-selection screenshot mapping.

The standing agent and control guidance should explicitly distinguish these decisions:

- Assignment minimum: at least three cooperating runtime transaction pipeline components.
- Current selected package: three assignment-compliant runtime components.
- Refreshed generation target: at least four cooperating components, with Reporting Agent as the preferred fourth component, to strengthen future generated candidates.
- Assignment example: `.claude/commands/write-spec.md`.
- Current intentional surface: `.claude/skills/write-spec/SKILL.md`, because modern Claude Code project skills provide the slash-invoked project workflow and supersede the older command wrapper pattern for this repository.

Hephaestus guidance should consistently say that run-local `shared/` may live in a candidate output package as current runtime evidence when deliberately committed, but it must be excluded from selectable code inventories and canonical copy targets. Historical `archive/`, coverage files, pytest caches, and bytecode remain tool/runtime output and should stay excluded.

The harness term leak should be removed from Homework Automation Layer instructions. Hephaestus sub-agent authorization should instead cite the standing Homework 6 guide, `.codex/config.toml` `agents.max_threads = 8`, and the Hephaestus control package.

## Interfaces and data

Expected changed areas:

- `agent-control/generate-docs/workflow.md`
- `agent-control/generate-docs/quality-bar.md`
- `agent-control/generate-docs/run-registry.md`
- `agent-control/generate-code/workflow.md`
- `agent-control/generate-code/quality-bar.md`
- `agent-control/generate-code/run-registry.md`
- `agent-control/write-spec/README.md` or related Athena control documentation where the Claude project-skill decision is best recorded
- `agents.md`
- `CHANGELOG.md`

Interfaces expected to remain stable:

- Python runtime module APIs and JSON result schemas.
- `mcp.json`, `.codex/config.toml`, `mcp/server.py`, command wrappers, coverage hook behavior, and tests unless direct verification proves a narrow documentation-evidence blocker.
- The required stable screenshot filenames listed in `TASKS.md`.
- Current canonical reviewer docs, stable screenshots, `docs/pr-description-draft.md`, selected Clio run output, and `docs/agent-runs/final-selection.md`.

## Risks

- A follow-up Clio regeneration could be forgotten after the control-surface repair. Mitigation: record the follow-up as an explicit handoff outcome and keep current documentation-package gaps visible until a new Clio package is generated or selected.
- Accidentally repairing canonical docs or selected run output in this work item would blur ownership between Clio control repair and Clio output regeneration. Mitigation: keep canonical docs, screenshots, PR draft, final-selection records, and selected Clio run output out of scope.
- Over-documenting the fourth runtime component target could make the current selected package look non-compliant. Mitigation: explicitly separate assignment minimum, current selected package, and future generation target.
- Adding a legacy `.claude/commands/write-spec.md` wrapper would contradict the operator's accepted decision. Mitigation: document the modern skill surface rather than restoring the older wrapper.
- Evidence screenshots may leak raw account identifiers or descriptions. Mitigation: apply existing privacy-safe evidence rules before copying or embedding screenshots.

## Acceptance criteria

- Clio control guidance requires MCP evidence containing both Context7 and custom `pipeline-status` interaction evidence.
- Clio control guidance requires PR draft evidence for spec produced, pipeline run, tests/coverage, skill/hook, MCP usage, and README with student name.
- Stable screenshot guidance rejects stale duplicate mappings for distinct assignment screenshot categories unless an explicit accepted limitation is recorded.
- Stable screenshot guidance requires `test-coverage.png` to show current passing 80 percent coverage evidence; hook-blocking failure evidence belongs to `hook-trigger.png`.
- Clio run-registry and handoff guidance require any regenerated documentation package to keep canonical reviewer docs, selected run evidence, screenshot inventory, and final-selection mapping synchronized.
- The plan records that fixing the existing selected documentation package and PR draft is a separate follow-up Clio regeneration or selection step.
- The deliberate four-component target is documented as a future/refreshed generation quality target, while the current three-component selected runtime is documented as meeting the assignment minimum.
- The deliberate Claude Code project-skill decision for Athena is documented, including why no `.claude/commands/write-spec.md` wrapper is present.
- Hephaestus `shared/` guidance is consistent across workflow, quality bar, and run registry: `shared/` is runtime evidence, not selectable code, and may be preserved when deliberately committed.
- `agent-control/generate-code/workflow.md` no longer references a "frozen Hephaestus planning package" as a source of Homework Automation Layer behavior.
- Static validation confirms no canonical reviewer docs, stable screenshots, PR draft, selected Clio run output, or final-selection records changed in this work item.
- Runtime verification still passes after control-surface repair: pipeline run, pytest suite, and 80 percent coverage gate.
- Changed docs/control files contain no unresolved placeholders or internal harness leakage in Homework Automation Layer agent prompts.

## Documentation artifact matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Before freeze/implementation commits | `CHANGELOG.md` | Newest-first entries for planning approval and later repair implementation |
| Test cases | Snapshot | Yes | Before implementation | `docs/work-items/2026-06-21-homework-6-review-repair/snapshots/test-cases.snapshot.md` | Captures review-derived acceptance and validation checks |
| Testing guide delta | Living delta | No | Not applicable | Not applicable | Canonical `TESTING_GUIDE.md` is out of scope until the separate Clio regeneration step |
| Operator manual delta | Living delta | No | Not applicable | Not applicable | Control files under `agent-control/` are the operator manuals being repaired |
| API reference delta | Living delta | No | Not applicable | Not applicable | No API behavior change planned |
| Architecture snapshot | Snapshot | No | Not applicable | Not applicable | Layer and target distinctions are captured in this spec |
| Architecture summary delta | Living delta | No | Not applicable | Not applicable | Canonical `ARCHITECTURE.md` is out of scope until the separate Clio regeneration step |

## Approval

- Status: Draft
- Superseded by: Not applicable

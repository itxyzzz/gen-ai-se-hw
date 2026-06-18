# Homework 6 Changelog

## Homework 6 - Step 17: Code Run Preservation Repair Plan

### Added

- Added the approved planning package for repairing Hephaestus (Code Generator) run-local output preservation, selected-code traceability, and repeatable pipeline run archival.
- Added a test-case snapshot for zero-padded runtime archive folders such as `archive/shared-001` and for code/test generation provenance checks.

### Changed

- Planned a source trace from the selected Athena (Spec Writer) run to each Hephaestus code version using run IDs and stable content fingerprints.
- Planned future Themis (Test Generator) traceability so generated tests name the selected Hephaestus software version they target.

### Fixed

- Planned repair of the current first Hephaestus run folder so the generated code package exists under the run evidence before canonical selection.

### Tests

- Validated the draft planning artifacts for required sections, placeholder markers, staged-file scope, zero-padded archive naming, and source-spec/software-version traceability before the planning freeze commit.

## Homework 6 - Step 15: Hephaestus Control Surface

### Added

- Added the tool-neutral Hephaestus (Code Generator) control package under `agent-control/generate-code/`.
- Added Codex and Claude Code `generate-code` skill entrypoints for Task 2 code-generation runs.
- Added Hephaestus workflow, quality-bar, and run-registry guidance for Context7 research notes, run preservation, validation, and sub-agent evidence.

### Changed

- Updated `agents.md` so future Hephaestus runs discover the selected `specification.md`, `generate-code` entrypoints, Context7 requirement, and executor sub-agent autonomy up to `agents.max_threads = 8`.

### Fixed

- Not applicable.

### Tests

- Validated branch, expected changed files, untouched `TASKS.md`, untouched `specification.md`, unchanged Context7 MCP config, generate-code package paths, wrapper references, Context7/sub-agent markers, scope rejection markers, unresolved-template scans, and whitespace checks.

## Homework 6 - Step 14: Hephaestus Planning Package

### Added

- Added the approved harness planning package for Hephaestus (Code Generator) under `docs/work-items/2026-06-18-hephaestus-code-generator/`.
- Planned the `generate-code` control surface, tool-neutral workflow package, Codex and Claude Code entrypoints, run preservation, and Context7-backed `research-notes.md` requirements.

### Changed

- Authorized future Hephaestus (Code Generator) executor sub-agent use up to Homework 6's configured `agents.max_threads = 8` cap without additional operator approval.
- Scoped the future Hephaestus run to Task 2 generated transaction-system code only, with Task 3 hooks/commands, Task 4 custom MCP setup, Task 5 docs/screenshots, and PR packaging explicitly out of scope.

### Fixed

- Not applicable.

### Tests

- Validated the planning artifacts for placeholder markers, whitespace issues, staged file scope, frozen-file diffs, Context7 requirements, and sub-agent autonomy language before the planning freeze commit.

## Homework 6 - Step 13: Replacement Athena Spec Selection

### Added

- Added the replacement Athena (Spec Writer) run comparison record under `docs/agent-runs/20260618-003908-write-spec-python-replacement/comparison.md`.

### Changed

- Selected `20260618-003908-write-spec-python-replacement` as the canonical Python transaction-processing system specification.
- Copied the selected run's `agent-1-spec/outputs/specification.md` to canonical `specification.md`.
- Updated `docs/agent-runs/final-selection.md` so downstream Hephaestus (Code Generator) work uses the replacement transaction-system spec instead of the failed/superseded prior selection.

### Fixed

- Replaced the previously failed canonical spec that targeted the homework automation/control layer with a product-only Generated Transaction System Layer specification.

### Tests

- Verified canonical `specification.md` is byte-for-byte content-equivalent to the selected run output.
- Verified required Task 1 sections are present in canonical `specification.md`.
- Checked product-boundary leakage terms; remaining matches are negated non-product scope statements or privacy reminders, not transaction-system implementation tasks.
- Reviewed git status and confirmed the change set is limited to canonical spec selection, final-selection metadata, the preserved replacement run folder, and this changelog entry.

## Homework 6 - Step 12: Athena Control Surface Repair

### Added

- Added `agent-control/write-spec/transaction-system-brief.md` as Athena (Spec Writer)'s direct transaction-processing system input.
- Added layer glossary and Homework Automation Layer identity labels for Athena, Hephaestus, Themis, and Clio.

### Changed

- Updated Athena (Spec Writer) workflow, stack profile, quality bar, and entrypoint wrappers so generated specs target the transaction-processing system instead of the homework automation harness.
- Clarified that Homework Automation Layer agents do not use `dev-doc-harness` or Superpowers freeze gates.
- Clarified that generated transaction-system runtime components include runtime application agents, and set Athena (Spec Writer)'s temporary coverage target to 75% while deferring the >80% gate to Themis (Test Generator).
- Clarified that runtime transaction pipeline agents are stack-native application components, not Claude/Codex skills.

### Fixed

- Marked the first selected Athena (Spec Writer) run as failed/superseded for wrong target while preserving the run folder and canonical failed spec as evidence.
- Replaced the low-level task standard with implementation-ready transaction-system slices and added a meta-layer leakage rejection gate.

### Tests

- Validated the branch, expected dirty files, untouched `TASKS.md`, untouched `specification.md`, required control-surface markers, failed-selection markers, and whitespace with the frozen plan's static checks.

## Homework 6 - Step 11: Athena Spec Layering Plan

### Added

- Added the approved harness planning package for repairing Athena (Spec Writer) so it targets the transaction-processing system specification rather than the homework automation harness.
- Planned the transaction-system brief, layer glossary, Greek identity labels, meta-leakage gates, and failed-spec marking.

### Changed

- Scoped the plan to Athena control-surface repair only; replacement spec generation and selection are deferred to a later clean Homework 6-root thread.

### Fixed

- Planned correction of the low-level task standard so Athena produces implementation-ready transaction-system slices instead of one card per Homework Automation Layer agent.

### Tests

- Validated the planning artifacts for required sections, placeholder markers, out-of-scope replacement-run steps, staged file scope, and whitespace issues before the planning freeze commit.

## Homework 6 - Step 10: Agent 1 Specification Run

### Added

- Preserved the first Python `write-spec` generation run under `docs/agent-runs/20260617-180458-write-spec-python-primary/`.
- Added the selected canonical `specification.md` for Homework 6 Task 1.

### Changed

- Recorded first-run auto-selection in `docs/agent-runs/final-selection.md`.
- Captured domain research, objective design, low-level task decomposition, final review, repair review, validation, and completion handoff for Agent 1.

### Fixed

- Repaired the generated `/validate-transactions` requirement so it validates `sample-transactions.json` through validator dry-run behavior instead of running tests or coverage as its primary command.

### Tests

- Validated the generated specification against the Task 1 section requirements, Python stack profile, privacy/audit rules, JSON file protocol, dry-run validation command requirement, and `write-spec` quality bar.
- Ran placeholder and whitespace scans for the preserved run artifacts.

## Homework 6 - Step 9: Agent 1 Specification Run Plan

### Added

- Added the approved Phase 02 harness plan for the Python `write-spec` generation and first-run auto-selection workflow.

### Changed

- Recorded the required sub-agent waves, validation checks, selection boundary, and canonical-copy rules before running Agent 1.

### Fixed

- Not applicable.

### Tests

- Validated the Phase 02 plan for placeholder markers and whitespace issues before the planning freeze commit.

## Homework 6 - Step 8: Skill Metadata Cleanup

### Changed

- Updated the Codex `write-spec` UI metadata default prompt to explicitly invoke `$write-spec`.

### Removed

- Removed the empty `write-spec/references/` skill directory now that the shared reference package lives under `agent-control/write-spec/`.

### Tests

- Verified the empty references directory is absent, the Codex skill validates, `openai.yaml` parses with a `$write-spec` default prompt, and the working-tree whitespace check is clean.

## Homework 6 - Step 7: Claude Skill Reference Cleanup

### Added

- Documented the official Claude Code project-skill precedence rationale in the shared Agent 1 write-spec package and variance log.

### Changed

- Updated stale planning and validation references from the removed `.claude/commands/write-spec.md` wrapper to `.claude/skills/write-spec/SKILL.md`.

### Removed

- Removed the empty `.claude/commands/` directory to avoid implying that a legacy command wrapper still exists.

### Fixed

- Fixed trailing whitespace in the Homework 6 task file so diff whitespace checks are clean.

### Tests

- Validated Codex skill metadata, MCP JSON, Codex TOML, working-tree whitespace checks against `HEAD` and `main`, placeholder scan, stale active-path scans, and empty command-directory removal.

## Homework 6 - Step 6: Tool-Neutral Write-Spec Package

### Added

- Added `agent-control/write-spec/` as the canonical shared Agent 1 reference package.
- Added a Claude Code native `write-spec` project skill with Claude-oriented frontmatter and slash-command examples.

### Changed

- Updated the Codex `write-spec` skill, Agent 1 workflow references, run registry, and standing agent guide to point at the tool-neutral package.
- Moved run-registry rules into the shared package so `docs/agent-runs/README.md` can stay a thin pointer.

### Removed

- Removed the legacy `.claude/commands/write-spec.md` wrapper with no transition period.

### Tests

- Validated skill metadata, reference paths, active stale path scan, MCP JSON, Codex TOML, placeholder markers, and whitespace checks.

## Homework 6 - Step 5: Post-Review Write-Spec Tightening

### Added

- Added a variance log for the intentional removal of fallback generation behavior from the `write-spec` wrappers.
- Documented the missing original `specification-TEMPLATE-hint.md` caveat in the Agent 1 run registry workflow.

### Changed

- Thinned the Codex skill and Claude Code slash-command wrappers so the shared workflow, stack profiles, and quality bar are mandatory.
- Updated the run registry to reference shared workflow files instead of restating the Agent 1 layout and comparison criteria.
- Replaced standing exact-model labels with policy-relative Codex and Claude Code model guidance.

### Fixed

- Removed the stale Claude command `outputs/agents.md` run-output target.
- Clarified repo-root versus homework-root path resolution for `write-spec` runs.

### Tests

- Validated skill metadata, MCP JSON, Codex TOML, placeholder scan, stack-selection markers, stale run-output/model-name searches, and whitespace checks.

## Homework 6 - Step 4: Write-Spec Selection and Model Controls

### Added

- Added first-run auto-selection rules for successful Agent 1 `write-spec generate` runs when no canonical `specification.md` exists yet.
- Added explicit Codex and Claude Code model-family and reasoning prescriptions for Agent 1 orchestration, sub-agents, and final review.

### Changed

- Clarified that `specification.md` is the default selectable package and supporting docs require explicit selection before canonical copy.
- Clarified that `homework-6/agents.md` is the stable homework-level guide required by Task 1, not a per-run generated output to overwrite.

### Fixed

- Removed stale run-registry copy targets that could cause a generated run to overwrite the standing agent guide.

### Tests

- Validated skill metadata, Codex TOML, first-run selection markers, model-prescription markers, stale `outputs/agents.md` targets, placeholder scan, and whitespace checks.

## Homework 6 - Step 3: Write-Spec Sub-Agent Workflow

### Added

- Added mandatory Agent 1 sub-agent planning, domain research, objectives, low-level task, review, and planned handoff requirements to the shared `write-spec` workflow.
- Added `agents.max_threads = 8` to the Homework 6 Codex project configuration for multi-agent spec-generation runs.

### Changed

- Replaced ad-hoc research and emergency-only handoff guidance with planned sub-agent phases and explicit handoff artifacts.

### Fixed

- Prevented Agent 1 from skipping sub-agents merely because a later operator prompt does not repeat the requirement.

### Tests

- Validated skill metadata, Codex TOML, sub-agent markers, placeholder scan, stack-selection markers, and whitespace checks.

## Homework 6 - Step 2: Canonical Write-Spec Workflow

### Added

- Added `write-spec-workflow.md` as the canonical shared workflow for both the Codex Markdown skill and Claude Code slash command.

### Changed

- Reduced the Codex `write-spec` skill and Claude `/write-spec` command to thin wrappers around the shared workflow, stack profile, and quality-bar references.

### Fixed

- Removed duplicated workflow instructions from the two command surfaces to reduce drift before running Agent 1.

### Tests

- Validated skill metadata, wrapper references, placeholder scan, stack-selection markers, and whitespace checks.

## Homework 6 - Step 1: Agent 1 Control Surfaces

### Added

- Added the Homework 6 agent guide, dual `write-spec` control surfaces, stack profile reference, run registry, and Context7 project configuration.

### Changed

- Established the Task 1 workflow for generating and selecting stack-specific specification runs before downstream implementation.

### Fixed

- Not applicable.

### Tests

- Validated skill metadata, MCP JSON, Codex TOML, stack-selection markers, placeholder scan, and cross-surface command references.

## Homework 6 - Step 0: Agent 1 Planning Package

### Added

- Added the approved harness planning package for the Homework 6 Agent 1 `write-spec` control surfaces.
- Planned dual Claude Code slash-command and Codex Markdown skill support, stack-specific spec generation, run preservation, and Context7/MCP strategy.

### Changed

- Established Python as the default `write-spec` stack input and Java as the fixed alternate profile for optional comparison runs.

### Fixed

- Not applicable.

### Tests

- Validated the approved planning artifacts for placeholder markers and staged whitespace issues before the planning freeze commit.

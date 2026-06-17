# Homework 6 Changelog

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

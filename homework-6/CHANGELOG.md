# Homework 6 Changelog

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

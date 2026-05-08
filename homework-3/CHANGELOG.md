# Homework 3 Changelog

## Homework 3 - Step 5

### Added

- Added the missing Superpowers-style implementation plan for the agent-controls baseline under `docs/superpowers/plans/`.

### Changed

- Recorded the plan artifact after the agent-controls baseline had already been implemented and committed.

### Fixed

- Fixed the missing plan archival record for the Homework 3 agent-controls increment.

### Tests

- Verified the plan file exists under `docs/superpowers/plans/`.
- Verified the plan follows the required Superpowers plan header shape.
- Verified the plan includes checkbox task steps and preserves the documentation-only scope.

## Homework 3 - Step 4

### Added

- Added a feature-neutral Agent-Control Baseline to `specification.md` with eight reusable controls for future EU banking-style finance feature specs.
- Added researched, scoped banking-style control rationale to `docs/domain-rules.md`.
- Added sensitive-operator-action review expectations to `docs/operator-manual.md`.

### Changed

- Tightened `agents.md` and `.github/copilot-instructions.md` so future agents must carry the control baseline into feature-specific tasks.
- Expanded `docs/technical-conventions.md` with state-machine, idempotency, safe audit event, and redaction expectations.
- Updated `README.md` to explain why real-bank enterprise controls were trimmed to homework-realistic controls.

### Fixed

- Reduced the risk of unsupported compliance claims by labeling EU banking controls as homework assumptions and deferring feature-specific legal obligations.

### Tests

- Verified required deliverables exist.
- Verified the eight baseline controls appear in the Homework 3 package.
- Scanned for unsupported compliance language; matches were limited to explicit scope-limiting or "do not invent" wording.
- Verified the increment remains documentation-only and preserves newest-first changelog order.

## Homework 3 - Step 3

### Added

- Not applicable for this changelog maintenance increment.

### Changed

- Reordered the changelog so the latest Homework 3 changes appear at the top.

### Fixed

- Fixed changelog ordering to follow newest-first convention.

### Tests

- Verified the changelog starts with the latest step entry.

## Homework 3 - Step 2

### Added

- Added the Superpowers-style implementation plan under `docs/superpowers/plans/`.

### Changed

- Preserved the existing Homework 3 harness content while adding a plan artifact for future review and replay.

### Fixed

- Not applicable for this documentation-plan archival increment.

### Tests

- Verified the plan file exists, follows the Superpowers plan header shape, contains checkbox task steps, and keeps the same outer-harness scope.
- Verified the plan file does not contain Superpowers red-flag phrases outside the intentional marker-scan command.

## Homework 3 - Step 1

### Added

- Created the outer specification harness for Homework 3.
- Added `README.md` as the reviewer entry point and package map.
- Added `agents.md` as the AI and human agent behavior contract.
- Added `.github/copilot-instructions.md` as the editor-specific AI rules file.
- Added feature-neutral technical conventions, development process, and operator manual documents under `docs/`.
- Added deferred scaffolds for `specification.md` and `docs/domain-rules.md`.

### Changed

- Established that feature selection, domain research, final edge cases, verification targets, and performance targets are deferred to the next specification increment.

### Fixed

- Not applicable for this initial Homework 3 documentation increment.

### Tests

- Verified required deliverable files exist.
- Verified relative markdown links resolve.
- Verified unfinished marker text is absent from non-deferred documents.
- Verified the development process treats Superpowers, GitHub Spec Kit, and similar addons as optional support rather than required dependencies.

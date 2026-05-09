# Homework 3 Changelog

## Homework 3 - Step 9

### Added

- Added `docs/low-level-task-rewrite-handoff.md` as a focused handoff for starting the low-level task rewrite in a clean thread.

### Changed

- Documented the current five-objective specification shape, rewrite goals, task-format expectations, coverage targets, guardrails, and verification checklist for the next increment.
- Linked the handoff document from the README package map.

### Fixed

- Reduced the amount of prior-thread context needed before rewriting the `Low-Level Tasks` section.

### Tests

- Verified the handoff document exists and is linked from `README.md`.
- Verified `git diff --check` passes.

## Homework 3 - Step 8

### Added

- Added `Implementation Notes` subsections for domain assumptions, actors/roles/permissions, and builder guardrails in `specification.md`.

### Changed

- Moved jurisdiction/domain rationale, agent-control guardrails, and detailed role definitions out of the top of `specification.md` so the document flows from objective and scope directly into mid-level objectives.
- Consolidated seven mid-level objectives into five broader, observable objectives covering intake eligibility, evidence/follow-up, internal review, audit/privacy/compliance controls, and reliability/performance.
- Remapped verification and current low-level task traceability references from `M1-M7` to `M1-M5` without performing the later detailed low-level task rewrite.

### Fixed

- Reduced structural noise between the high-level objective and mid-level objectives.
- Kept compliance, audit, redaction, role, and performance controls present in `specification.md` while preserving `docs/domain-rules.md`, `agents.md`, and `README.md` as the owning support documents for rationale and agent workflow.

### Tests

- Verified there are no domain-rationale, agent-control, or detailed role sections between the high-level objective and mid-level objectives.
- Verified `specification.md` contains exactly five mid-level objectives.
- Verified old `M6` and `M7` references were removed from `specification.md`.
- Verified role permissions remain explicit in `Implementation Notes`.
- Verified `git diff --check` passes.

## Homework 3 - Step 7

### Added

- Added a clearer README document taxonomy that separates required deliverables, active supporting docs, and historical AI-assistance plan artifacts.
- Added `docs/superpowers/plans/README.md` to explain that archived plans are evidence, not active normative requirements.
- Added ownership and non-redundancy rules so each Homework 3 document has one clear responsibility.

### Changed

- Refactored `agents.md` to route agents to source-of-truth documents instead of repeating full domain, finance-sensitive, and control-baseline tables.
- Tightened `.github/copilot-instructions.md` into a compact editor-specific pointer back to `agents.md` and the active source documents.
- Updated `docs/development-process.md`, `docs/technical-conventions.md`, and `docs/operator-manual.md` language to reflect the selected Dispute Intake package and plan-archive status.

### Fixed

- Reduced cross-document duplication that could drift between agent rules, Copilot rules, domain rules, technical conventions, and the product specification.
- Clarified that historical plan artifacts may contain superseded scaffold language and do not override active Homework 3 docs.

### Tests

- Verified required Homework 3 deliverables are present.
- Verified relative Markdown links resolve across Homework 3 Markdown files.
- Verified active-doc stale-marker scan has no matches outside archived plan files.
- Verified detailed control-baseline tables remain in source documents while `agents.md` uses source-of-truth routing.
- Verified `git diff --check` passes.

## Homework 3 - Step 6

### Added

- Added the approved Dispute Intake implementation plan under `docs/superpowers/plans/`.
- Selected EU payment-account Dispute Intake as the Homework 3 finance feature.
- Replaced the earlier scaffold in `specification.md` with a layered dispute-intake specification covering objective, scope, stakeholders, state machine, data concepts, edge cases, verification, performance targets, and low-level tasks.
- Added feature-specific PSD2, GDPR, DORA, EBA ICT/security, EBA complaints-handling, and FIN-NET/ADR context to `docs/domain-rules.md`.
- Added dispute queue roles, review queues, sensitive operator actions, audit-safe notes, and escalation rules to `docs/operator-manual.md`.

### Changed

- Updated `README.md` to explain the selected feature, EU jurisdiction decision, scope exclusions, performance-target rationale, verification depth, and industry practices.
- Updated `agents.md` and `.github/copilot-instructions.md` so AI agents apply dispute-specific rules for posted transactions, evidence metadata, audit-safe notes, redaction, role boundaries, and state transitions.
- Preserved the existing Agent-Control Baseline while applying it directly to Dispute Intake.
- Reworded stale scaffold-marker phrasing in the earlier outer-harness plan artifact so active package scans do not look unfinished.

### Fixed

- Removed planned-later feature-selection language from the active Homework 3 specification package.
- Reduced unsupported compliance risk by documenting regulatory sources as design rationale and excluding exact legal deadlines, refund obligations, chargeback processing, regulator reporting, and ADR outcomes.

### Tests

- Verified the current branch is `homework-3-submission` before editing.
- Verified the working tree was clean before the Step 6 documentation changes.
- Verified required deliverables are present.
- Verified local Markdown links resolve in the changed reviewer-facing docs.
- Verified the unfinished-marker scan is clean for the planned marker set and accidental patch markers.
- Verified `specification.md` contains all required Homework 3 layers.
- Verified Agent-Control Baseline coverage remains present and applied to Dispute Intake.
- Verified `git diff --check` passes after whitespace cleanup.

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

- Reduced the risk of unsupported compliance claims by labeling EU banking controls as homework assumptions and reserving feature-specific legal obligations for later review.

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
- Added initial scaffolds for `specification.md` and `docs/domain-rules.md`.

### Changed

- Established that feature selection, domain research, final edge cases, verification targets, and performance targets would be completed in the next specification increment.

### Fixed

- Not applicable for this initial Homework 3 documentation increment.

### Tests

- Verified required deliverable files exist.
- Verified relative markdown links resolve.
- Verified unfinished marker text is absent from active documents.
- Verified the development process treats Superpowers, GitHub Spec Kit, and similar addons as optional support rather than required dependencies.

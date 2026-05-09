# Homework 3 Changelog

## Homework 3 - Step 16

### Added

- Added a refined M5-only low-level task rewrite for reliability, concurrency, performance, pagination, and verification traceability.
- Added new M5 task cards for read-after-write consistency and projection recovery, plus cursor pagination and mutable queue scale behavior.

### Changed

- Reworked M5 implementation prompts into structured context, task, constraints, examples, and output-format blocks.
- Expanded M5 task details for stale-version conflicts, retry and idempotency semantics, projection lag recovery, performance target measurement, cursor privacy, and synthetic verification fixtures.
- Strengthened M5 acceptance criteria and verification coverage for concurrency races, duplicate replay prevention, audit/detail reconciliation, healthy-dependency p95 targets, mutable queue pagination, and objective-to-test traceability.

### Fixed

- Reduced ambiguity in the M5 task depth by separating reliability, consistency, performance, pagination, and fixture coverage into executable task cards.

### Tests

- Reviewed the M5 task shape against the low-level task handoff and existing M1-M4 prompt-structure pattern.
- Verified the change remains documentation-only and scoped to the M5 section of `specification.md`.
- Counted six `M5.x` task cards and six M5 implementation prompts containing context, task, constraints, examples, and output-format guidance.
- Verified `git diff --check` passes.

## Homework 3 - Step 15

### Added

- Added a refined M4-only low-level task rewrite for audit, privacy, and compliance controls.
- Added a new M4.6 task card for scoped compliance controls, sensitive-data governance, restricted metadata access, unsupported-claim review gates, and synthetic-data checks.

### Changed

- Reworked M4 implementation prompts into structured context, task, constraints, examples, and output-format blocks.
- Expanded M4 task details for safe audit-event contracts, blocked-action audit evidence, redaction and minimization, safe error semantics, fail-closed dependency controls, and compliance guardrails.
- Strengthened M4 acceptance criteria and verification coverage for per-action audit assertions, wrong-owner privacy, unsafe-content omission, role-shaped projections, dependency outages, restricted-access audit, and unsupported legal/refund/retention claim checks.

### Fixed

- Reduced ambiguity in the M4 task depth by making audit persistence ordering, privacy boundaries, restricted access, and compliance-scope limits explicit in executable task cards.

### Tests

- Reviewed the M4 task shape against the low-level task handoff and existing M1-M3 prompt-structure pattern.
- Verified the change remains documentation-only and scoped to the M4 section of `specification.md`.
- Counted six `M4.x` task cards and six M4 implementation prompts containing context, task, constraints, examples, and output-format guidance.
- Verified `git diff --check` passes.

## Homework 3 - Step 14

### Added

- Added a refined M3-only low-level task rewrite for internal review workflow, including permissions, queues, assignment, transitions, notes, and sensitive approvals.

### Changed

- Reworked M3 implementation prompts into structured context, task, constraints, examples, and output-format blocks.
- Expanded M3 task details for role permission checks, field-level visibility, queue pagination, assignment ownership, transition preconditions, operator note redaction, and self-approval prevention.
- Strengthened M3 acceptance criteria and verification coverage for audit trails, stale-state behavior, restricted visibility, concurrency, performance, and fail-closed dependency handling.

### Fixed

- Reduced ambiguity in the M3 task depth by making support, ops, compliance, fraud/risk, and system-job boundaries explicit in executable task cards.

### Tests

- Reviewed the M3 task shape against the low-level task handoff and prompt-structure guidance.
- Verified the change remains documentation-only and scoped to the M3 section of `specification.md`.
- Counted six `M3.x` task cards and six M3 implementation prompts containing context, task, constraints, examples, and output-format guidance.
- Verified `git diff --check` passes.

## Homework 3 - Step 13

### Added

- Added a refined M2-only low-level task rewrite for evidence metadata, reviewer information requests, user responses, and user-safe case detail.

### Changed

- Reworked M2 implementation prompts into structured context, task, constraints, examples, and output-format blocks.
- Expanded M2 task details for metadata-only evidence handling, evidence type requiredness, redaction, safe request text, user response ownership, audit events, stale-state behavior, and read-after-write visibility.
- Strengthened M2 acceptance criteria and verification coverage for privacy, audit trails, safe errors, permission boundaries, duplicate retries, dependency failures, and performance expectations.

### Fixed

- Reduced ambiguity in the M2 task depth by making binary-file exclusions, real URL exclusions, restricted rationale hiding, and safe user-visible status behavior explicit.

### Tests

- Reviewed the M2 task shape against the low-level task handoff and prompt-structure guidance.
- Verified the change remains documentation-only and scoped to the M2 section of `specification.md`.
- Counted six `M2.x` task cards and six M2 implementation prompts containing context, task, constraints, examples, and output-format guidance.
- Verified `git diff --check` passes.

## Homework 3 - Step 12

### Added

- Added structured subfields under each M1 implementation prompt: context, task, constraints, examples, and output format.

### Changed

- Reformatted the M1 implementation prompts from single-line paragraphs into scannable nested prompt blocks while preserving the same task-card fields and M1 scope.

### Fixed

- Improved readability for future AI-agent execution prompts without changing product behavior or expanding beyond intake eligibility and submission.

### Tests

- Verified each M1 task still has one implementation prompt with all five required prompt-structure subfields.
- Verified `git diff --check` passes.

## Homework 3 - Step 11

### Added

- Added a refined M1-only low-level task rewrite for intake eligibility and submission, expanding the objective into seven implementation-ready task cards.

### Changed

- Reworked M1 implementation prompts to follow the template guidance with explicit context, task, constraints, examples, and output-format expectations.
- Split retry, duplicate-dispute, creation, and rejected-intake error/audit behavior into separate M1 leaf tasks so future implementers can verify each slice independently.
- Strengthened M1 acceptance criteria and verification coverage for privacy, audit trails, safe errors, idempotency, duplicate prevention, dependency failure, and read-after-write behavior.

### Fixed

- Reduced ambiguity in the M1 task depth by making compliance, security, data privacy, audit, error handling, and testing requirements explicit in each relevant task.

### Tests

- Reviewed the M1 task shape against the low-level task handoff and prompt-structure guidance.
- Verified the change remains documentation-only and scoped to the M1 section of `specification.md`.
- Counted seven `M1.x` task cards and seven M1 implementation prompts containing context, task, constraints, examples, and output-format guidance.
- Verified `specification.md` has no `M6` or `M7` references.
- Verified `git diff --check` passes.

## Homework 3 - Step 10

### Added

- Replaced the rough `Low-Level Tasks` table in `specification.md` with readable task-card subsections grouped by `M1` through `M5`.

### Changed

- Converted the low-level task list from document-maintenance chores into future implementation-ready slices for intake eligibility, evidence metadata, internal review, audit/privacy controls, and reliability/performance.
- Added explicit per-task fields for supports, implementation prompt, create-or-update target, core behavior, edge cases, acceptance criteria, and verification without using a wide table.
- Expanded verification guidance inside each task to cover happy paths, negative paths, role permissions, audit events, redaction, concurrency, idempotency, dependency failure, pagination, performance, and synthetic fixture coverage.

### Fixed

- Made the low-level task hierarchy human-readable while preserving traceability to the five active mid-level objectives.

### Tests

- Verified each low-level task uses an `M1.x` through `M5.x` task ID and includes the required fields.
- Verified only the five active objective IDs are used in the rewritten task cards.
- Verified the rewrite remains documentation-only and evidence remains metadata-only.
- Verified `git diff --check` passes.

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

# Dispute Intake Specification Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Turn the Homework 3 feature-neutral harness into a documentation-only specification package for EU payment-account Dispute Intake.

**Architecture:** Keep `homework-3/specification.md` as the product source of truth, with supporting domain, operator, agent, editor, and reviewer documents aligned to it. The package remains documentation-only and uses EU payment-account regulations as scoped design rationale, not as a compliance claim.

**Tech Stack:** Markdown documentation, Mermaid diagrams, Git, PowerShell verification commands, and official EU/EBA/EDPB/ESMA web sources for regulatory rationale.

---

## Scope

Implement the approved plan for a Dispute Intake specification:

- Users can file disputes against posted transactions.
- Internal ops/compliance users can review, request more information, assign statuses, and record audit-safe notes.
- The EU/EEA payment-account context is primary.
- PSD2, GDPR, DORA, EBA ICT/security, EBA complaints-handling, and FIN-NET/ADR are documented as design rationale.
- The scope excludes implementation code, real APIs, UI, real file handling, chargeback processing, provisional credits, legal deadline enforcement, regulator reporting, refunds, and arbitration.

## Files

- Modify: `homework-3/specification.md`
- Modify: `homework-3/docs/domain-rules.md`
- Modify: `homework-3/README.md`
- Modify: `homework-3/agents.md`
- Modify: `homework-3/.github/copilot-instructions.md`
- Modify: `homework-3/docs/operator-manual.md`
- Modify: `homework-3/CHANGELOG.md`
- Modify if marker scans require it: `homework-3/docs/superpowers/plans/2026-05-08-homework-3-outer-harness.md`

## Tasks

### Task 1: Confirm Workspace And Baseline

- [ ] Run `git branch --show-current`.
  - Expected: `homework-3-submission`.
- [ ] Run `git status --short`.
  - Expected before editing: no unrelated user changes.
- [ ] Read `HOMEWORK_STANDARDS.md`, `homework-3/TASKS.md`, `homework-3/specification.md`, `homework-3/docs/domain-rules.md`, and the supporting HW3 docs.
  - Expected: context confirms Homework 3 is documentation-only.

### Task 2: Replace The Deferred Specification Scaffold

- [ ] Update `homework-3/specification.md` with the selected feature title and objective.
- [ ] Add the scope boundary and EU/EEA payment-account jurisdiction decision.
- [ ] Preserve and specialize the Agent-Control Baseline for Dispute Intake.
- [ ] Add stakeholders and roles for end user, support, ops, compliance, fraud/risk, and system job.
- [ ] Add mid-level objectives M1-M7.
- [ ] Add states: `submitted`, `under_review`, `needs_information`, `accepted`, `rejected`, and `closed`.
- [ ] Add a state-transition table with actor, preconditions, rejected-transition behavior, and audit event.
- [ ] Add hypothetical data concepts: `Dispute`, `DisputeEvidenceMetadata`, `DisputeAuditEvent`, `OperatorNote`, and `OpsQueueView`.
- [ ] Add command semantics, error codes, reason categories, beginning/ending context, edge cases, verification matrix, performance targets, and low-level tasks.
- [ ] Acceptance criteria: `specification.md` contains every required Homework 3 layer and no longer reads as an unselected feature scaffold.

### Task 3: Extend Domain Rules

- [ ] Update `homework-3/docs/domain-rules.md` to state the selected EU/EEA payment-account Dispute Intake scope.
- [ ] Add regulatory rationale with official links for European Commission payment services, PSD2, EBA PSD2 complaints guidance, EDPB GDPR principles, DORA, EBA ICT/security guidance, EBA complaints-handling guidance, and FIN-NET/ADR context.
- [ ] Add feature-specific sensitive data inventory and rules for PSD2-informed payment intake, GDPR-informed data protection, DORA/ICT resilience, and complaints/ADR context.
- [ ] Preserve warnings against unsupported exact retention periods, statutory deadlines, refund obligations, chargebacks, regulator reporting, and compliance guarantees.
- [ ] Acceptance criteria: regulatory content is scoped as design rationale, not legal advice or compliance proof.

### Task 4: Align Reviewer And Agent Documentation

- [ ] Update `homework-3/README.md` to describe the selected feature, package map, rationale, industry practices, performance target rationale, and current limits.
- [ ] Update `homework-3/agents.md` with Dispute Intake rules for posted transactions, evidence metadata only, audit-safe notes, role boundaries, state transitions, idempotency, redaction, and verification.
- [ ] Update `homework-3/.github/copilot-instructions.md` with concise editor-specific dispute rules and exclusions.
- [ ] Acceptance criteria: these files no longer frame the feature as unselected and do not invite implementation code.

### Task 5: Align Operator Manual

- [ ] Update `homework-3/docs/operator-manual.md` with dispute queue roles, queue views, sensitive operator actions, audit-safe notes, escalation, audit evidence, and manual checks.
- [ ] Acceptance criteria: support, ops, compliance, fraud/risk, and system responsibilities are distinct; restricted notes and user-visible messaging are redaction-aware.

### Task 6: Update Changelog

- [ ] Add the newest changelog entry for this increment.
- [ ] If a previous committed Step 5 already exists, use the next step number instead of overwriting history.
- [ ] Include Added, Changed, Fixed, and Tests sections.
- [ ] Acceptance criteria: changelog explains feature selection, regulatory expansion, doc alignment, scope-risk reduction, and verification.

### Task 7: Verify Documentation

- [ ] Run required-file check for:
  - `homework-3/specification.md`
  - `homework-3/agents.md`
  - `homework-3/.github/copilot-instructions.md`
  - `homework-3/README.md`
  - `homework-3/docs/domain-rules.md`
  - `homework-3/docs/operator-manual.md`
  - `homework-3/CHANGELOG.md`
- [ ] Run unfinished-marker scan for the planned marker set and accidental patch markers.
  - Expected: no matches in active package content.
- [ ] Run direct heading check for required `specification.md` sections.
  - Expected: all required Homework 3 layers are present.
- [ ] Check local Markdown links in changed reviewer-facing docs.
  - Expected: local links resolve.
- [ ] Run `git diff --check`.
  - Expected: exit code 0. LF-to-CRLF warnings are acceptable on Windows.
- [ ] Run `git diff --stat`.
  - Expected: changes are limited to Homework 3 documentation and plan artifacts.

### Task 8: Stage And Commit

- [ ] Run `git status --short`.
- [ ] Confirm the configured git email uses an allowed private domain.
- [ ] Stage all intended Homework 3 documentation changes.
- [ ] Commit with a message such as `docs: specify dispute intake for homework 3`.
- [ ] Acceptance criteria: commit succeeds on `homework-3-submission` and `git status --short` is clean afterward.

## Verification Summary

The implementation is complete when:

- The Dispute Intake specification is the active Homework 3 product spec.
- The EU/EEA payment-account decision is documented explicitly.
- Domain rules extend the generic baseline only where feature-specific support is needed.
- Agent, editor, operator, README, and changelog docs all align with the selected feature.
- Verification commands pass with evidence.
- Changes are staged and committed with an allowed private git email.

# Homework 3 Agent Controls Baseline Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add a homework-sized agent-control framework for internal EU banking-style development flows while preserving the existing deferred-feature harness.

**Architecture:** Keep `specification.md` as the product source of truth, use `docs/domain-rules.md` for control rationale and scope limits, and use `agents.md` plus `.github/copilot-instructions.md` as the enforceable agent behavior layer. Supporting docs make the controls operational for future feature specs without adding application code.

**Tech Stack:** Markdown documentation only in the Homework 3 package.

---

## File Structure

- Modify `homework-3/specification.md` to add an Agent-Control Baseline section while keeping the feature deferred.
- Modify `homework-3/docs/domain-rules.md` to convert the fully deferred domain file into a scoped, researched control baseline.
- Modify `homework-3/agents.md` and `homework-3/.github/copilot-instructions.md` to make the controls enforceable for future AI and human agents.
- Modify `homework-3/docs/technical-conventions.md` to add state-machine, idempotency, safe audit event, and redaction conventions.
- Modify `homework-3/docs/operator-manual.md` to add sensitive operator action and internal escalation expectations.
- Modify `homework-3/README.md` to explain why real-bank enterprise controls were trimmed to homework-realistic controls.
- Modify `homework-3/CHANGELOG.md` to record the increment and verification.

## Task 1: Add Core Control Baseline

**Files:**
- Modify: `homework-3/specification.md`
- Modify: `homework-3/docs/domain-rules.md`

- [ ] **Step 1: Add the Agent-Control Baseline to the spec**

Add a section named `Agent-Control Baseline` after the scope boundary. Include these eight controls with a requirement and later enforcement path: synthetic data only, role boundaries, safe audit events, redaction, idempotent state changes, explicit state machines, human review for sensitive ops, and verification mapping.

- [ ] **Step 2: Preserve deferred-feature scope**

State that the increment does not select a product feature, define APIs, design UI screens, create code, or assert legal compliance for a real bank.

- [ ] **Step 3: Expand domain rules into scoped rationale**

Update `docs/domain-rules.md` with EU banking-style rationale based on GDPR-style minimization/security, DORA/EBA-style ICT resilience, and AI Act-style traceability, while labeling these as homework assumptions rather than compliance guarantees.

- [ ] **Step 4: Keep feature-specific obligations deferred**

Explicitly defer selected-feature workflows, retention periods, payment-network rules, authentication rules, reporting deadlines, and fraud escalation criteria until the final feature is selected and researched.

## Task 2: Tighten Agent And Editor Rules

**Files:**
- Modify: `homework-3/agents.md`
- Modify: `homework-3/.github/copilot-instructions.md`

- [ ] **Step 1: Add baseline enforcement guidance**

In `agents.md`, require future agents to carry the Agent-Control Baseline into feature-specific tasks and to avoid raw PII, PAN, CVV, account numbers, authentication tokens, production logs, and secrets.

- [ ] **Step 2: Expand future verification expectations**

Require future feature specs to cover happy path, negative path, edge case, permission, audit, redaction, idempotency, stale-state, and performance checks.

- [ ] **Step 3: Keep editor rules short**

In `.github/copilot-instructions.md`, point Copilot-style tools back to `specification.md`, `agents.md`, and `docs/domain-rules.md`; add concise rules for state-changing tasks and sensitive operator actions without duplicating the full domain baseline.

## Task 3: Make Controls Operational

**Files:**
- Modify: `homework-3/docs/technical-conventions.md`
- Modify: `homework-3/docs/operator-manual.md`

- [ ] **Step 1: Add idempotency and state-machine conventions**

Document that retryable state-changing commands need idempotency ownership, duplicate-response behavior, and no duplicate financial effects, notifications, or audit contradictions. Add explicit state-machine expectations for durable states, allowed transitions, stale conflicts, actor permissions, and audit events.

- [ ] **Step 2: Add safe audit event shape**

Define the expected audit event fields: `event_id`, `occurred_at`, `actor_type`, `actor_id`, `actor_role`, `action`, `target_type`, `target_id`, `correlation_id`, `reason_code`, `before_state`, `after_state`, and `sensitive_data_present`.

- [ ] **Step 3: Add redaction rules**

Require allowlisted structured fields, safe error codes, masked account/card/token/auth values, and no logging of CVV, passwords, one-time codes, session tokens, API keys, authorization headers, or full support notes.

- [ ] **Step 4: Add sensitive operator action review expectations**

In `docs/operator-manual.md`, require extra review evaluation for limit overrides, state overrides, forced reversals, destructive record changes, sensitive data reveals, failed financial retries, and fraud/compliance-sensitive decisions.

- [ ] **Step 5: Limit escalation language**

State that this homework should use internal escalation language unless the selected feature later provides researched support for external reporting obligations.

## Task 4: Update Reviewer Rationale And Changelog

**Files:**
- Modify: `homework-3/README.md`
- Modify: `homework-3/CHANGELOG.md`

- [ ] **Step 1: Update README status and package map**

Describe the package as an outer harness plus agent-control baseline. Update file responsibilities for `specification.md`, `agents.md`, `docs/domain-rules.md`, `docs/technical-conventions.md`, and `docs/operator-manual.md`.

- [ ] **Step 2: Explain control trimming**

Add rationale that the source research assumed a real EU banking application, while this homework keeps only controls that are important, documentable, and later enforceable in code or review.

- [ ] **Step 3: Update industry best practices**

Map synthetic data, minimization, redaction, auditability, sensitive-action review, idempotency, state machines, and verification mapping to their owning docs.

- [ ] **Step 4: Add newest-first changelog entry**

Add a `Homework 3 - Step 4` entry with Added, Changed, Fixed, and Tests sections describing the control framework increment and verification.

## Task 5: Verify Before Commit

**Files:**
- Inspect: `homework-3/**/*.md`

- [ ] **Step 1: Verify required deliverables and links**

Run a file-existence check for `specification.md`, `agents.md`, `.github/copilot-instructions.md`, `README.md`, `docs/domain-rules.md`, `docs/technical-conventions.md`, `docs/operator-manual.md`, and `docs/development-process.md`.

Expected output: every path exists.

- [ ] **Step 2: Scan for unsupported compliance language**

Search Homework 3 markdown for phrases such as `DORA requires this homework`, `legally required for this homework`, `must report to regulators`, `must retain for`, and `production banking system`.

Expected output: no matches.

- [ ] **Step 3: Verify eight-control coverage**

Search Homework 3 markdown for the eight baseline control names.

Expected output: all eight controls are present.

- [ ] **Step 4: Verify changelog order**

Confirm the first changelog step heading is `## Homework 3 - Step 4`.

Expected output: Step 4 appears before Step 3 and Step 2.

- [ ] **Step 5: Review diff scope**

Run `git diff --name-only` and confirm the changed files are documentation files under `homework-3`.

Expected output: only Homework 3 markdown docs changed.

## Self-Review Notes

- Spec coverage: the plan covers the approved framework-first scope without selecting a feature.
- Placeholder scan: no placeholder tokens are intentionally left for the implementer.
- Scope check: this plan is documentation-only and does not ask for application code, APIs, UI, migrations, or tests.

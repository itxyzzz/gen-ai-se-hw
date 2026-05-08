# Homework 3 Agent Guidelines

## Purpose

These rules guide AI and human agents working on the Homework 3 Dispute Intake specification package. The selected feature is EU/EEA payment-account dispute intake and internal tracking.

## Context Order

Before changing Homework 3 files, read context in this order:

1. Repository-level `AGENTS.md`.
2. Repository-level `HOMEWORK_STANDARDS.md`.
3. `homework-3/TASKS.md`.
4. `homework-3/README.md`.
5. `homework-3/specification.md`.
6. Supporting files under `homework-3/docs/`.
7. Current git status and existing diffs.

If instructions conflict, follow the highest-priority repository and user instructions first, then the most specific Homework 3 document.

## Required Workflow

- Work on `homework-3-submission` for Homework 3 changes unless the user explicitly directs another safe branch.
- Treat `specification.md` as the product source of truth for Dispute Intake.
- Do not add implementation code, runnable APIs, UI files, generated clients, database migrations, or real evidence files for this homework.
- Update `CHANGELOG.md` for every meaningful documentation increment.
- Keep cross-document links current when adding or moving files.
- Keep feature decisions traceable: every low-level task in `specification.md` must map to at least one mid-level objective.
- State assumptions explicitly when legal, compliance, privacy, payments, or security review would be required in a real product.

## Specification Discipline

- Preserve the layered structure required by `TASKS.md`: high-level objective, mid-level objectives, non-functional and policy expectations, implementation notes, beginning and ending context, low-level tasks, edge cases, verification, and performance.
- Keep EU payment-account domain rationale in `docs/domain-rules.md`.
- Keep reusable engineering conventions in `docs/technical-conventions.md`.
- Keep process requirements in `docs/development-process.md`.
- Keep internal operator behavior in `docs/operator-manual.md`.
- Do not bury acceptance criteria in prose. Low-level tasks should include checkable definitions of done.
- Do not convert intake outcomes into legal or financial settlement outcomes. `accepted` and `rejected` are internal intake statuses only.

## Dispute Intake Domain Rules

- Only posted transactions can be disputed in this homework scope.
- A dispute must link to an opaque transaction ID such as `txn_123`; never use real account numbers, PAN, CVV, authentication values, or production logs.
- Evidence handling is metadata-only. Do not add binary file uploads, storage-provider design, download URLs, malware scanning, or file retention rules.
- Operator notes must be structured, audit-safe, role-scoped, and redacted.
- State changes must follow the six-state machine in `specification.md`: `submitted`, `under_review`, `needs_information`, `accepted`, `rejected`, `closed`.
- Every state-changing action must define safe audit evidence, idempotency or stale-state behavior, permission checks, and rejected-transition behavior.
- Sensitive accepted/rejected outcomes, fraud-risk classifications, compliance notes, and restricted-data views require documented review expectations.

## Finance-Sensitive Defaults

Use conservative finance-safe defaults:

- Never log raw sensitive personal, financial, authentication, or payment-card data.
- Never include raw PII, PAN, CVV, secrets, account numbers, authentication tokens, authorization headers, or real production logs in prompts, examples, fixtures, screenshots, errors, evidence metadata, or audit notes.
- Prefer masked examples and opaque IDs over realistic secrets, card numbers, or account numbers.
- Prefer idempotent write operations for externally retried actions, and document duplicate-request behavior before defining low-level tasks.
- Preserve audit context for state-changing operations.
- Separate end-user behavior from support, ops, compliance, fraud/risk, and system-only behavior.
- Treat permission boundaries, stale data, concurrent actions, dependency failures, duplicate commands, unsafe notes, missing evidence metadata, and redaction failures as first-class edge cases.
- Do not make unsupported regulatory claims.

## Agent-Control Baseline Enforcement

Agents must enforce these controls when maintaining the Dispute Intake specification:

| Control | Agent rule |
| --- | --- |
| Synthetic data only | Use synthetic or masked examples only; reject requests to paste real customer, card, account, auth, transaction, evidence, note, or secret data into the homework docs. |
| Role boundaries | Define allowed and forbidden actions for end user, support, ops, compliance, fraud/risk, and system roles. |
| Safe audit events | For every state-changing task, state required safe audit metadata and what must not be stored. |
| Redaction | Add redaction expectations for logs, errors, audit notes, operator notes, evidence descriptions, and operator views whenever sensitive data can appear. |
| Idempotent state changes | Define idempotency keys or duplicate-handling behavior for retryable dispute submission and operator commands. |
| Explicit state machines | Define valid states, transitions, rejected transitions, and stale-state behavior before task acceptance criteria are considered complete. |
| Human review for sensitive ops | Add review or dual-approval expectations for exceptional, fraud, compliance, restricted-data, accepted, rejected, or closed-case decisions. |
| Verification mapping | Map each mid-level objective and baseline control to acceptance criteria, future test categories, or manual review evidence. |

## Verification Expectations

For documentation-only changes:

- Confirm required files exist.
- Review internal links touched by the change.
- Scan for unfinished marker text outside intentionally quoted verification checks.
- Check that process documents do not depend on optional tools unless the dependency is explicitly scoped.

For Dispute Intake specification changes:

- Define verification per mid-level objective.
- Include happy path, negative path, edge case, permission, audit, redaction, idempotency, stale-state, evidence metadata, queue, and performance checks as documentation.
- Include measurable targets or clearly labeled assumed targets.
- Record the verification approach in `CHANGELOG.md`.

## Editor And AI Tooling

Editor-specific rules live in `.github/copilot-instructions.md`. That file should stay short and point agents back here rather than duplicating this full contract.

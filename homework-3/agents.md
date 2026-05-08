# Homework 3 Agent Guidelines

## Purpose

These rules guide AI and human agents working on the Homework 3 specification package. The package describes a future finance-oriented application feature, but this increment intentionally keeps the feature and researched domain rules open.

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
- Treat `specification.md` as the product source of truth once the feature is selected.
- Do not add implementation code, runnable APIs, UI files, generated clients, or database migrations for this homework.
- Update `CHANGELOG.md` for every meaningful documentation increment.
- Keep cross-document links current when adding or moving files.
- Keep feature decisions traceable: every future low-level task in `specification.md` must map to at least one mid-level objective.
- State assumptions explicitly when a future agent must proceed before research is complete.

## Specification Discipline

- Preserve the layered structure required by `TASKS.md`: high-level objective, mid-level objectives, non-functional and policy expectations, implementation notes, beginning and ending context, low-level tasks, edge cases, verification, and performance.
- Keep domain research in `docs/domain-rules.md` and cite or explain the source of each researched rule before using it as a requirement.
- Keep reusable engineering conventions in `docs/technical-conventions.md`.
- Keep process requirements in `docs/development-process.md`.
- Keep internal operator behavior in `docs/operator-manual.md`.
- Do not bury acceptance criteria in prose. Future tasks should include checkable definitions of done.
- Before adding feature-specific tasks, confirm the feature spec carries forward the Agent-Control Baseline from `specification.md`.

## Finance-Sensitive Defaults

Until the final feature-specific domain rules are researched, use conservative finance-safe defaults:

- Never log raw sensitive personal, financial, authentication, or payment-card data.
- Never include raw PII, PAN, CVV, secrets, account numbers, authentication tokens, or real production logs in prompts, examples, fixtures, screenshots, errors, or audit notes.
- Prefer masked examples and opaque IDs over realistic secrets, card numbers, or account numbers.
- Prefer idempotent write operations for externally retried actions, and document duplicate-request behavior before defining low-level tasks.
- Preserve audit context for state-changing operations.
- Separate end-user behavior from internal operator behavior.
- Treat permission boundaries, stale data, concurrent actions, invalid amounts or limits, dependency failures, duplicate commands, and redaction failures as first-class edge cases.
- Do not make unsupported regulatory claims.

## Agent-Control Baseline Enforcement

Future agents must enforce these controls when completing the final feature specification:

| Control | Agent rule |
| --- | --- |
| Synthetic data only | Use synthetic or masked examples only; reject requests to paste real customer, card, account, auth, transaction, or secret data into the homework docs. |
| Role boundaries | Define allowed and forbidden actions for each user, support, compliance/ops, fraud/risk, and system role used by the feature. |
| Safe audit events | For every state-changing task, state the required safe audit metadata and what must not be stored. |
| Redaction | Add redaction expectations for logs, errors, audit notes, and operator views whenever sensitive data can appear. |
| Idempotent state changes | Define idempotency keys or duplicate-handling behavior for retryable commands that affect financial state. |
| Explicit state machines | Define valid states, transitions, rejected transitions, and stale-state behavior before task acceptance criteria are considered complete. |
| Human review for sensitive ops | Add review or dual-approval expectations for exceptional, destructive, override, fraud, or compliance-sensitive operator actions. |
| Verification mapping | Map each mid-level objective and baseline control to acceptance criteria, future test categories, or manual review evidence. |

## Verification Expectations

For documentation-only changes:

- Confirm required files exist.
- Review internal links touched by the change.
- Scan for unfinished marker text outside intentionally deferred documents.
- Check that process documents do not depend on optional tools unless the dependency is explicitly scoped.

For future feature-spec changes:

- Define verification per mid-level objective.
- Include happy path, negative path, edge case, permission, audit, redaction, idempotency, stale-state, and performance checks as documentation.
- Include measurable targets or clearly labeled assumed targets.
- Record the verification approach in `CHANGELOG.md`.

## Editor And AI Tooling

Editor-specific rules live in `.github/copilot-instructions.md`. That file should stay short and point agents back here rather than duplicating this full contract.

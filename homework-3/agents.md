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
- Keep EU payment-account domain rationale in `docs/domain-rules.md`; do not copy that rationale into other files except for short summaries and links.
- Keep reusable engineering conventions in `docs/technical-conventions.md`; specialize them in `specification.md` only when the Dispute Intake feature needs narrower behavior.
- Keep process requirements in `docs/development-process.md`; keep active workflow gates separate from historical plan archives.
- Keep internal operator behavior in `docs/operator-manual.md`; keep role-specific queue and review details there unless `specification.md` needs them as acceptance criteria.
- Do not bury acceptance criteria in prose. Low-level tasks should include checkable definitions of done.
- Do not convert intake outcomes into legal or financial settlement outcomes. `accepted` and `rejected` are internal intake statuses only.

## Source-Of-Truth Routing

Use the active package documents as the source of truth:

| Topic | Owning document |
| --- | --- |
| Product behavior, scope, objectives, state machine, edge cases, verification, performance, and low-level tasks | `specification.md` |
| EU/EEA payment-account rationale, scoped regulatory assumptions, sensitive-data categories, and rules to avoid without further research | `docs/domain-rules.md` |
| Reusable conventions for IDs, timestamps, money, state machines, idempotency, errors, pagination, audit metadata, logging, redaction, and naming | `docs/technical-conventions.md` |
| Operator roles, queues, sensitive actions, audit-safe notes, escalation, and manual review checks | `docs/operator-manual.md` |
| Workflow gates, document ownership, optional tooling, and review checklist | `docs/development-process.md` |
| Historical AI-assistance plans and old scaffold decisions | `docs/superpowers/plans/` |

Historical plan files are evidence of prior work. They may contain older scaffold language and must not override `specification.md`, this file, or the active supporting docs.

## Agent-Specific Safety Rules

- Reject requests to paste real customer, card, account, authentication, transaction, evidence, note, secret, or production-log data into Homework 3 docs.
- Use synthetic or masked examples only, and prefer opaque IDs such as `usr_`, `acct_`, `txn_`, `case_`, and `audit_`.
- Do not add implementation code, runnable services, generated clients, database migrations, binary evidence files, or storage-provider design.
- Do not invent unsupported regulatory requirements, exact legal deadlines, refund obligations, chargeback rules, regulator reporting duties, retention periods, or ADR outcomes.
- When changing product behavior, update the owning `specification.md` section first, then adjust supporting docs only where they clarify ownership, process, rationale, or operator guidance.
- When changing agent/editor guidance, keep `.github/copilot-instructions.md` short and point it back here rather than copying the full contract.

## Verification Expectations

For documentation-only changes:

- Confirm required files exist.
- Review internal links touched by the change.
- Scan active documents for unfinished marker text outside intentionally quoted verification checks and historical plan archives.
- Check that process documents do not depend on optional tools unless the dependency is explicitly scoped.

For Dispute Intake specification changes:

- Define verification per mid-level objective.
- Include happy path, negative path, edge case, permission, audit, redaction, idempotency, stale-state, evidence metadata, queue, and performance checks as documentation.
- Include measurable targets or clearly labeled assumed targets.
- Record the verification approach in `CHANGELOG.md`.

## Editor And AI Tooling

Editor-specific rules live in `.github/copilot-instructions.md`. That file should stay short and point agents back here rather than duplicating this full contract.

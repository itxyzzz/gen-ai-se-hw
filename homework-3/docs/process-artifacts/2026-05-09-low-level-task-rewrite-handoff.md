# Low-Level Task Rewrite Handoff

## Purpose

Use this handoff to start a clean thread focused only on rewriting the `Low-Level Tasks` section of `homework-3/specification.md`.

The previous thread tightened the core specification structure and pushed commit `a63eda8` on branch `homework-3-submission`. The next thread should not revisit feature selection, jurisdiction framing, or the five mid-level objectives unless a contradiction is discovered while rewriting the low-level tasks.

## Start Here

Read these files in order:

1. `homework-3/TASKS.md`
2. `homework-3/agents.md`
3. `homework-3/specification.md`
4. `homework-3/docs/domain-rules.md`
5. `homework-3/docs/technical-conventions.md`
6. `homework-3/docs/operator-manual.md`
7. `homework-3/README.md`

The most important source of truth is `homework-3/specification.md`. Supporting docs clarify domain rationale, conventions, operator behavior, and agent workflow. Historical plans under `docs/superpowers/plans/` are evidence only and must not override active docs.

## Current Specification Shape

`specification.md` now uses this top-level structure:

1. `High-Level Objective`
2. `Scope Boundary`
3. `Mid-Level Objectives`
4. `Dispute State Machine`
5. `Non-Functional And Policy Expectations`
6. `Implementation Notes`
7. `Context`
8. `Edge Cases And Failure Modes`
9. `Verification Plan`
10. `Expected Performance`
11. `Low-Level Tasks`

The mid-level objectives are exactly:

| ID | Objective |
| --- | --- |
| M1 | Intake Eligibility And Submission |
| M2 | Evidence And User Follow-Up |
| M3 | Internal Review Workflow |
| M4 | Audit, Privacy, And Compliance Controls |
| M5 | Reliability, Concurrency, And Performance |

The current `Low-Level Tasks` table is intentionally still rough. It mostly describes document-definition work and must be replaced with a concrete, multi-level, implementable task hierarchy.

## Rewrite Goal

Replace the current `Low-Level Tasks` section with implementation-ready specification tasks that an engineering team or AI coding agent could execute without guessing.

The rewrite must stay documentation-only. Do not add application code, API implementation, UI files, database migrations, runnable tests, binaries, or real evidence files.

The low-level tasks should be detailed enough to show real decomposition. They should describe future implementation work as hypothetical engineering slices, not merely tasks to update the homework documents.

## Required Task Design

Use a multi-level task hierarchy organized by the five mid-level objectives:

- `M1.x` tasks for intake eligibility and submission.
- `M2.x` tasks for evidence metadata and user follow-up.
- `M3.x` tasks for internal review workflow.
- `M4.x` tasks for audit, privacy, and compliance controls.
- `M5.x` tasks for reliability, concurrency, and performance.

Each leaf task should include enough fields to be executable:

- `Task ID`
- `Supports`
- `Implementation prompt`
- `Create or update`
- `Core behavior`
- `Edge cases and failure modes`
- `Acceptance criteria`
- `Verification`

If the final table becomes too wide, prefer grouped subsections by objective with compact task tables under each objective.

## Expected Coverage

The low-level task rewrite should cover at least these product slices:

- User transaction lookup and empty eligible-transaction state.
- Posted-transaction ownership and eligibility checks.
- Duplicate active dispute and idempotent retry handling.
- Dispute creation with `case_` reference and safe user summary.
- Evidence metadata schema, allowed evidence types, safe descriptions, and redacted references.
- Reviewer information request and user response flow.
- User dispute detail/status view with safe visibility.
- Support, ops, compliance, fraud/risk, and system job permissions.
- Ops queue filters, sorting, pagination, assignment, and ownership.
- State transitions for `submitted`, `under_review`, `needs_information`, `accepted`, `rejected`, and `closed`.
- Operator notes with role-scoped visibility and redaction.
- Sensitive outcome review and self-approval prevention when dual review is required.
- Audit events for create, rejected intake, evidence update, note creation, assignment, information request, user response, transition, blocked transition, stale action, and closure.
- Error semantics for validation, permission, stale version, duplicate, unsafe content, rate limit, dependency failure, and audit-write failure.
- Fail-closed behavior when audit persistence or permission checks are unavailable.
- Performance and pagination expectations for create, detail, queue, state transition, and read-after-write.
- Verification fixtures and future test categories mapped back to `M1-M5`.

## Guardrails

- Keep `Dispute Intake` as one feature with one high-level objective.
- Keep the five mid-level objectives unchanged unless there is a clear contradiction.
- Do not invent exact legal deadlines, refund obligations, chargeback rules, retention periods, regulator reporting duties, or ADR outcomes.
- Treat `accepted` and `rejected` as internal intake outcomes only.
- Treat evidence as metadata-only. No binary files, storage-provider design, malware scanning, or real file URLs.
- Use synthetic or opaque examples only: `usr_`, `acct_`, `txn_`, `case_`, `audit_`.
- Do not include raw PII, account numbers, PAN, CVV, authentication values, secrets, raw provider responses, real production logs, or realistic customer histories.
- Keep compliance, audit, redaction, edge cases, verification, and performance in the spec itself, not only in supporting docs.
- Update `CHANGELOG.md` for the rewrite.

## Suggested New-Thread Prompt

```text
We are continuing Homework 3 in C:\Work\Codex\SETU-HW\gen-ai-se-hw on branch homework-3-submission. Read homework-3/docs/process-artifacts/2026-05-09-low-level-task-rewrite-handoff.md first, then read the files it lists.

Your task is to rewrite only the Low-Level Tasks section of homework-3/specification.md so it becomes a concrete, multi-level, implementable task hierarchy mapped to M1-M5. Preserve the tightened top-level structure and the five mid-level objectives. Keep the work documentation-only and update CHANGELOG.md.
```

## Verification Checklist For The Rewrite

Before reporting completion, verify:

- `Low-Level Tasks` is no longer a generic document-update checklist.
- Every low-level task maps to at least one of `M1-M5`.
- Each objective has multiple concrete leaf tasks.
- Several tasks include acceptance criteria that can be checked off by a future implementer.
- Edge cases, audit implications, redaction, permission boundaries, stale-state behavior, idempotency, and performance checks are integrated into tasks.
- No old `M6` or `M7` references return.
- No unsupported legal, refund, retention, chargeback, regulator, or ADR claims are introduced.
- `CHANGELOG.md` records the rewrite.
- `git diff --check` passes.

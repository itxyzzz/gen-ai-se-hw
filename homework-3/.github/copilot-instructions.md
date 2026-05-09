# Copilot Instructions For Homework 3

This folder is a documentation-only specification package for EU payment-account Dispute Intake. Do not generate application code, API implementations, UI screens, database migrations, tests, binaries, or evidence files unless the homework instructions are explicitly changed.

`homework-3/agents.md` is the full agent contract. This file is only the compact Copilot/editor pointer.

Before suggesting edits, read:

1. `homework-3/TASKS.md`
2. `homework-3/agents.md`
3. `homework-3/specification.md`
4. The relevant file under `homework-3/docs/`

Follow these rules:

- Keep `specification.md` as the source of truth for Dispute Intake scope, objectives, state machine, edge cases, verification, performance targets, and low-level task acceptance criteria.
- Keep supporting material in its owning file: domain rationale in `docs/domain-rules.md`, reusable conventions in `docs/technical-conventions.md`, workflow gates in `docs/development-process.md`, and operator behavior in `docs/operator-manual.md`.
- Never invent unsupported regulatory requirements, exact retention periods, legal deadlines, refund obligations, chargeback rules, regulator reporting duties, or ADR outcomes.
- Never include raw secrets, real payment-card numbers, real account numbers, real personal data, authentication tokens, authorization headers, CVV values, real evidence files, or real production logs in examples.
- Follow the Dispute Intake state machine and agent-control baseline in `specification.md`; do not duplicate those tables here.
- Treat evidence as metadata only and keep operator notes structured, role-scoped, redacted, and audit-safe.
- Prefer traceable, checkable requirements over broad prose.
- Update `CHANGELOG.md` when making a meaningful Homework 3 documentation change.

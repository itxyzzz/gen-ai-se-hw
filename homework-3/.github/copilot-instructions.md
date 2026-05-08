# Copilot Instructions For Homework 3

This folder is a documentation-only specification package for EU payment-account Dispute Intake. Do not generate application code, API implementations, UI screens, database migrations, tests, binaries, or evidence files unless the homework instructions are explicitly changed.

Before suggesting edits, read:

1. `homework-3/TASKS.md`
2. `homework-3/agents.md`
3. `homework-3/specification.md`
4. The relevant file under `homework-3/docs/`

Follow these rules:

- Keep `specification.md` as the source of truth for Dispute Intake scope, objectives, state machine, edge cases, verification, and performance targets.
- Keep EU payment-account regulatory rationale in `docs/domain-rules.md`.
- Keep reusable conventions in `docs/technical-conventions.md`.
- Keep workflow gates in `docs/development-process.md`.
- Keep operator-facing behavior in `docs/operator-manual.md`.
- Never invent unsupported regulatory requirements, exact retention periods, legal deadlines, refund obligations, chargeback rules, regulator reporting duties, or ADR outcomes.
- Never include raw secrets, real payment-card numbers, real account numbers, real personal data, authentication tokens, authorization headers, CVV values, real evidence files, or real production logs in examples.
- For dispute state changes, require permission checks, safe audit evidence, redaction expectations, idempotency or stale-state behavior, and valid state transitions.
- Treat evidence as metadata only: type, safe description, submitter, timestamp, and redacted reference.
- For operator notes, require structured safe text, reason codes, role-scoped visibility, and redaction.
- For sensitive accepted/rejected outcomes, fraud/risk markings, compliance decisions, or restricted-data access, require documented review or dual-approval expectations.
- Prefer traceable, checkable requirements over broad prose.
- Update `CHANGELOG.md` when making a meaningful Homework 3 documentation change.

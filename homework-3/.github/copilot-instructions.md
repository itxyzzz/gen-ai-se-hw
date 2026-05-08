# Copilot Instructions For Homework 3

This folder is a documentation-only specification package. Do not generate application code, API implementations, UI screens, database migrations, or tests unless the homework instructions are explicitly changed.

Before suggesting edits, read:

1. `homework-3/TASKS.md`
2. `homework-3/agents.md`
3. `homework-3/specification.md`
4. The relevant file under `homework-3/docs/`

Follow these rules:

- Keep `specification.md` as the source of truth for future product scope.
- Carry forward the Agent-Control Baseline in `specification.md` before adding feature-specific low-level tasks.
- Keep reusable conventions in `docs/technical-conventions.md`.
- Keep workflow gates in `docs/development-process.md`.
- Keep operator-facing behavior in `docs/operator-manual.md`.
- Keep researched finance, banking, and compliance rules in `docs/domain-rules.md`.
- Never invent unsupported regulatory requirements or cite unnamed standards.
- Never include raw secrets, real payment-card numbers, real account numbers, real personal data, authentication tokens, CVV values, or real production logs in examples.
- For state-changing feature tasks, require permission checks, safe audit evidence, redaction expectations, idempotency behavior, and valid state transitions.
- For sensitive operator actions, require human review or dual-approval expectations.
- Prefer traceable, checkable requirements over broad prose.
- Update `CHANGELOG.md` when making a meaningful Homework 3 documentation change.

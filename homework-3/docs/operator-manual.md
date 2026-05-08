# Operator Manual

## Purpose

This manual defines the expected shape of internal operator and compliance procedures for the future finance feature. It is feature-neutral until the selected feature and domain rules are added to `specification.md` and `docs/domain-rules.md`.

## Operator Principles

- Operators act only within documented permissions.
- Operator actions require a business reason or workflow reason.
- State-changing operator actions must leave audit evidence.
- Operators should see only the sensitive data required for their role.
- Compliance-sensitive decisions should support review by another authorized person when the final feature requires it.

## Access Boundaries

Future feature specs should define:

- Which actions are available to end users.
- Which actions are available to support operators.
- Which actions require compliance or fraud review.
- Which actions are system-only.
- Which fields are masked, partially visible, or fully restricted.

## Review Queues

If the future feature includes review queues, define each queue with:

- Entry criteria.
- Required reviewer role.
- Required evidence.
- Allowed decisions.
- Escalation path.
- Expected service target or review window.
- Audit fields produced by the decision.

## Escalation

Escalation guidance should distinguish:

- User-service issues that support can resolve.
- Compliance-sensitive issues that require compliance review.
- Fraud-risk issues that require fraud review.
- Technical incidents that require engineering support.
- Provider or partner issues that require external coordination.

The future feature spec should define user-visible messaging for each escalation class without exposing sensitive investigation details.

## Audit Evidence

Operator workflows should capture:

- Operator identity.
- Role or permission used.
- Timestamp.
- Workflow reason.
- Before and after state when safe and useful.
- Related case, request, transaction, or account identifier.
- Notes or structured reason codes when required.

Audit records should not contain raw secrets, full payment-card data, authentication values, or unnecessary personal data.

## Incident Notes

If a future feature has incident procedures, the spec should define:

- Detection signal.
- Severity criteria.
- Initial containment action.
- Customer impact review.
- Internal notification path.
- Post-incident evidence required for audit or compliance review.

## Manual Checks For Future Specs

Before completing the final Homework 3 specification, confirm the operator-facing sections answer:

- What can an operator do?
- What is forbidden?
- What evidence is required?
- What does the user see?
- What is recorded for audit?
- When is escalation required?

# Domain Rules

## Status

This document now provides a researched, feature-neutral control baseline for Homework 3. It remains deliberately limited: the final finance feature has not been selected, and this repository is a homework specification package rather than a real regulated banking system.

Use these rules as banking-style assumptions that make the future feature realistic, documentable, and later enforceable in code. Do not describe them as legal compliance guarantees.

## Research Scope And Rationale

The baseline is informed by EU financial and privacy practice, but trimmed to fit the assignment:

- GDPR-style data minimization, purpose limitation, and security of processing are used as rationale for synthetic data, redaction, and safe logging.
- DORA-style digital operational resilience and EBA ICT/security risk guidance are used as rationale for auditability, access control, secure development, incident handling, and verification.
- EU AI Act-style traceability and human oversight concepts are used as rationale for documenting AI-assisted development controls, not as a claim that this homework is a high-risk AI system.

These references justify conservative engineering rules. They do not create fixed retention periods, regulator reporting deadlines, outsourcing approvals, or real-bank committee processes for this homework.

## Sensitive Data Inventory

Future feature specs must assume these data categories are sensitive unless the selected feature proves otherwise:

| Category | Examples | Homework rule |
| --- | --- | --- |
| Personal data | Name, email, phone, address, customer identifier | Use fake or masked values only. |
| Financial account data | IBAN-like values, account IDs, balances, limits | Use opaque sample IDs and synthetic amounts. |
| Payment-card data | PAN, expiry, CVV, tokenized card references | Never use real PAN or CVV values; prefer masked tokens such as `card_123`. |
| Authentication data | Passwords, one-time codes, session tokens, API keys | Never include in prompts, examples, logs, or audit notes. |
| Transaction data | Merchant, amount, timestamp, authorization result | Use synthetic records and avoid realistic customer histories. |
| Operational investigation data | Fraud notes, compliance notes, support comments | Keep examples minimal, structured, and redacted. |

## Homework Control Baseline

| Control | Rule | What a future implementer could enforce |
| --- | --- | --- |
| Synthetic data only | Prompts, docs, examples, fixtures, screenshots, and logs must use synthetic or masked data. | Fixture linting, sample-data review, secret and PII scans. |
| Role boundaries | Specs must define end-user, support, compliance or ops, fraud/risk if needed, and system-only permissions before low-level tasks. | Authorization tests and operator-view restrictions. |
| Safe audit events | State-changing flows must record safe audit metadata without raw secrets or unnecessary personal data. | Audit schema assertions and event-presence tests. |
| Redaction | Errors, logs, audit notes, and examples must mask account-like, card-like, token-like, and authentication values. | Redaction unit tests and structured logging filters. |
| Idempotent state changes | Retried commands must not duplicate financial effects or create misleading audit history. | Idempotency-key handling and replay tests. |
| Explicit state machines | Specs must define valid states, allowed transitions, rejected transitions, and stale-state behavior. | Transition guard tests and stale-version conflict tests. |
| Human review for sensitive ops | Exceptional operator actions, destructive changes, sensitive overrides, and fraud/compliance decisions need documented review expectations. | Approval workflow checks and reviewer-role tests. |
| Verification mapping | Every mid-level objective must map to acceptance criteria and future verification evidence. | Traceability matrix, CI checks, manual review checklist. |

## Deferred Feature-Specific Rules

The final feature specification still must research and define:

- The selected finance feature and user/operator workflows.
- Jurisdiction and market assumptions beyond the EU-only homework baseline.
- Feature-specific permission model and sensitive fields.
- Retention expectations, if any, with a stated rationale.
- Payment network or partner rules, if the feature touches cards or payments.
- Authentication, notification, or dispute-processing deadlines, if relevant.
- Fraud-risk signals and escalation criteria tied to the selected feature.

## Rules To Avoid Until Researched

Do not add unsupported claims about:

- Specific regulatory compliance obligations.
- Required retention periods.
- Payment network rules.
- Bank secrecy, consumer-credit, or dispute-processing deadlines.
- Authentication strength or customer communication requirements.
- Cross-border data transfer requirements.
- Regulator reporting deadlines or legal incident-classification outcomes.

## Future Structure

When the feature is selected, organize the final domain rules with:

1. Feature scope and assumptions.
2. Sensitive data inventory for that feature.
3. Access and permission model.
4. Audit and retention expectations.
5. Security and fraud-risk expectations.
6. User communication constraints.
7. Operator and compliance review rules.
8. References or rationale for each rule group.

# Domain Rules

## Status

This document provides the domain-control baseline for the Homework 3 Dispute Intake specification. The selected feature is **EU/EEA payment-account dispute intake and internal tracking**.

These rules are banking-style assumptions for a realistic homework package. They are not legal advice, do not assert that this repository is compliant with any regulation, and do not replace review by legal, compliance, privacy, security, or payments specialists.

## Research Scope And Rationale

The primary jurisdictional framing is EU/EEA payment services. The feature is intentionally scoped to intake and internal tracking, not full statutory dispute resolution, chargeback processing, refunds, provisional credits, arbitration, legal deadline enforcement, or regulator reporting.

The baseline is informed by:

- [European Commission payment services](https://finance.ec.europa.eu/consumer-finance-and-payments/payment-services/payment-services_en), which frames EU payment services around common rules, clear information, consumer protection, and safe payment services.
- [Directive (EU) 2015/2366, PSD2](https://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex%3A32015L2366), especially concepts around unauthorized or incorrectly executed transactions, authentication/execution evidence, and payment-service-user protection.
- [EBA Guidelines on PSD2 complaints procedures](https://eba.europa.eu/publications-and-media/press-releases/eba-publishes-final-guidelines-procedures-complaints-alleged), used as context for complaint intake channels and information quality, not as direct implementation of competent-authority procedures.
- [EDPB GDPR processing principles](https://www.edpb.europa.eu/sme-data-protection-guide/faq-frequently-asked-questions/answer/what-are-basic-processing_en), used as rationale for minimization, purpose limitation, accuracy, storage-limitation thinking, security, and accountability.
- [DORA overview](https://www.esma.europa.eu/esmas-activities/digital-finance-and-innovation/digital-operational-resilience-act-dora) and [EBA ICT/security risk guidance update](https://www.eba.europa.eu/publications-and-media/press-releases/eba-amends-its-guidelines-ict-and-security-risk-management-measures-context-dora-application), used as rationale for operational resilience, access control, auditability, dependency failure handling, and verification discipline.
- [EBA complaints-handling guidance update](https://www.eba.europa.eu/publications-and-media/press-releases/eba-updates-joint-committee-guidelines-complaints-handling) and [FIN-NET context](https://finance.ec.europa.eu/consumer-finance-and-payments/retail-financial-services/financial-dispute-resolution-network-fin-net/about-fin-net_en), used as external complaint/ADR background only.

These references justify conservative engineering rules. They do not create fixed retention periods, external-reporting deadlines, refund deadlines, outsourcing approvals, real-bank governance programs, or regulator-facing workflows for this homework.

## Feature Scope And Assumptions

- The hypothetical product is an EU/EEA payment-account service.
- Disputes apply only to posted payment-account transactions owned by the authenticated user.
- The feature records intake cases, status history, evidence metadata, safe notes, and queue metadata.
- `accepted` and `rejected` are internal intake outcomes only. They do not mean a statutory liability decision, a refund, a chargeback, or an ADR outcome.
- Evidence handling is metadata-only. Real uploads, external file storage, malware scanning, and file download controls are outside scope.
- Real implementation teams must validate retention, timing, consumer notices, refund obligations, and reporting obligations with qualified compliance and legal reviewers before launch.

## Sensitive Data Inventory

Future implementation examples, fixtures, prompts, screenshots, and logs must assume these data categories are sensitive.

| Category | Dispute Intake examples | Homework rule |
| --- | --- | --- |
| Personal data | Name, email, phone, address, customer identifier, free-text user narrative | Use fake or masked values only; prefer opaque IDs and short safe summaries. |
| Financial account data | Account IDs, balances, IBAN-like values, account ownership | Use opaque sample IDs such as `acct_123`; do not include real account numbers. |
| Payment-card data | PAN, expiry, CVV, tokenized card references | Do not use real PAN or CVV; prefer opaque references such as `card_ref_123` only when needed. |
| Authentication data | Passwords, one-time codes, session tokens, API keys, authorization headers | Never include in prompts, examples, logs, notes, or audit records. |
| Transaction data | Merchant label, amount, currency, timestamp, posting status, authorization result | Use synthetic records and avoid realistic customer histories. |
| Dispute evidence metadata | Evidence type, description, received time, redacted reference | Store metadata only; do not store binary file content or real file URLs. |
| Operational investigation data | Fraud notes, compliance notes, support notes, reason codes | Keep structured, minimal, role-restricted, and redacted. |

## PSD2-Informed Payment Rules

Use PSD2 as rationale for these dispute-intake rules:

- Intake categories should support unauthorized payment, incorrectly executed payment, duplicate payment, incorrect amount, merchant-not-recognized, goods/services issue, and other payment problem.
- The system should capture enough safe metadata for later authentication and execution review, such as transaction ID, posted timestamp, amount, currency, merchant label, user reason category, and evidence metadata.
- The feature must not state that a refund, rectification, or liability decision has occurred. Those outcomes are outside scope.
- Fraud suspicion must be handled carefully: internal fraud/risk notes are restricted and must not be exposed to the user as unsupported accusations.
- Exact PSD2 notice periods, refund timing, and communications are not specified in this homework. A real implementation must validate those obligations separately.

## GDPR-Informed Data Protection Rules

Use GDPR-style principles as rationale for these data handling rules:

- Purpose limitation: dispute data is collected for intake, review, user follow-up, audit, and controlled internal escalation only.
- Minimization: collect only the transaction reference, reason category, safe user summary, evidence metadata, workflow metadata, and audit metadata needed for intake.
- Accuracy: status, owner, timestamps, and reason codes must be versioned or otherwise protected from stale operator overwrites.
- Storage limitation: this homework does not define a retention period; the spec must state that retention requires legal/compliance validation.
- Security: role-based access, redaction, structured notes, audit trails, and safe error messages are required design controls.
- Accountability: every state-changing action must preserve who acted, why, when, and what safe state changed.

## DORA And ICT/Security Rationale

Use DORA and EBA ICT/security guidance as rationale for:

- Failing closed when audit persistence, permission checks, or critical transaction lookup dependencies are unavailable.
- Recording correlation IDs for workflows that cross user, operator, audit, and transaction systems.
- Distinguishing validation failures, permission failures, stale-state conflicts, rate limits, and dependency failures.
- Defining performance and pagination targets for user and operator workflows.
- Treating log redaction, access control, audit verification, and resilience checks as first-class verification items.

## Complaints And ADR Context

EBA complaints-handling guidance and FIN-NET/ADR context support the need for clear intake channels, case registration, status tracking, and evidence quality. In this homework:

- The internal dispute case is not a competent-authority complaint.
- The internal dispute case is not an ADR or FIN-NET procedure.
- The spec may mention escalation to compliance or external guidance as context, but it must not invent external filing deadlines, regulator notifications, or ADR outcomes.

## Homework Control Baseline

| Control | Rule | What a future implementer could enforce |
| --- | --- | --- |
| Synthetic data only | Prompts, docs, examples, fixtures, screenshots, and logs must use synthetic or masked data. | Fixture linting, sample-data review, secret and PII scans. |
| Role boundaries | Specs must define end-user, support, compliance/ops, fraud/risk, and system-only permissions before low-level tasks are complete. | Authorization tests and operator-view restrictions. |
| Safe audit events | State-changing flows must record safe audit metadata without raw secrets, full narratives, or unnecessary personal data. | Audit schema assertions and event-presence tests. |
| Redaction | Errors, logs, audit notes, operator notes, evidence descriptions, and examples must mask account-like, card-like, token-like, and authentication values. | Redaction unit tests and structured logging filters. |
| Idempotent state changes | Retried dispute commands must not create duplicate active cases, duplicate user notifications, or misleading audit history. | Idempotency-key handling and replay tests. |
| Explicit state machines | Specs must define valid states, allowed transitions, rejected transitions, and stale-state behavior. | Transition guard tests and stale-version conflict tests. |
| Human review for sensitive ops | Sensitive accepted/rejected outcomes, fraud/compliance decisions, and restricted-data access require documented review expectations. | Approval workflow checks and reviewer-role tests. |
| Verification mapping | Every mid-level objective must map to acceptance criteria and future verification evidence. | Traceability matrix, CI checks, manual review checklist. |

## Rules To Avoid Without Further Research

Do not add unsupported claims about:

- Exact PSD2 notification, refund, rectification, or response deadlines.
- Fixed retention periods for dispute, audit, or evidence metadata.
- Card-network chargeback rules or arbitration procedures.
- Provisional credits, refunds, reversals, or customer compensation execution.
- Bank secrecy, consumer-credit, sanctions, AML, or suspicious-activity reporting duties.
- Cross-border data-transfer requirements.
- Regulator reporting deadlines or legal incident-classification outcomes.
- Authentication-strength requirements beyond the generic need for secure access control.

## Future Review Checklist

Before a real implementation plan is produced, a reviewer should confirm:

- The feature still covers intake and internal tracking only.
- The jurisdictional framing remains EU/EEA payment-account focused.
- All examples are synthetic or masked.
- Operator notes and evidence metadata cannot store raw sensitive values.
- The dispute state machine and queue behavior match `specification.md`.
- Every state-changing action has a safe audit event.
- Any real retention, timing, refund, or external complaint obligations are deferred to legal/compliance review.

# Operator Manual

## Purpose

This manual defines internal operator and compliance expectations for the Homework 3 Dispute Intake specification. The selected feature is EU/EEA payment-account dispute intake and internal tracking.

This is a feature-specific supporting document. `specification.md` remains the product source of truth; this manual owns operator workflow detail that would otherwise make the core spec harder to scan.

The manual supports the specification. It does not create legal deadlines, refund obligations, chargeback procedures, regulator reporting, or external ADR workflows.

## Operator Principles

- Operators act only within documented permissions.
- Operator actions require a business reason, workflow reason, or allowlisted reason code.
- State-changing operator actions must leave safe audit evidence.
- Operators should see only the sensitive data required for their role.
- Compliance-sensitive and fraud/risk-sensitive decisions should support review by another authorized person when the case criteria require it.
- Sensitive operator actions must not be self-approved when the action changes a user-visible outcome, overrides a queue decision, suppresses a fraud/compliance signal, or reveals otherwise masked data.
- Operator notes must be structured, redacted, role-scoped, and written for later audit review.

## Dispute Queue Roles

| Role | Queue access | Allowed actions | Forbidden actions |
| --- | --- | --- | --- |
| Support operator | Masked status summaries for cases tied to support contacts. | Explain status, add support-safe note, route to ops. | Change dispute status, close case, view restricted evidence details, add fraud/compliance conclusions. |
| Ops reviewer | Standard review queue and assigned cases. | Assign owner, start review, request information, add ops note, recommend accepted/rejected outcome, close low-risk cases. | Self-approve sensitive outcomes, edit transaction records, delete audit events. |
| Compliance reviewer | Compliance queue and sensitive cases. | Review sensitive outcomes, approve restricted decisions, add compliance note, close reviewed cases. | Expose restricted rationale to end user, bypass redaction, change payment transaction state. |
| Fraud/risk reviewer | Fraud/risk queue and cases routed for suspicious patterns. | Add fraud-risk classification, request escalation, add restricted fraud/risk note. | Accuse the user in user-visible text, suppress audit records, decide refunds or legal liability. |
| System job | Queue metadata and reminder markers. | Update age buckets, reminder markers, and safe workflow metadata. | Create disputes without user action, decide accepted/rejected outcomes, delete notes. |

## Review Queues

Dispute Intake should define these queue views:

| Queue | Entry criteria | Reviewer role | Allowed decisions | Service target |
| --- | --- | --- | --- | --- |
| New submissions | `submitted` disputes without assigned owner. | Ops reviewer | Assign owner, move to `under_review`, request information. | Visible in queue within 2 seconds of creation. |
| In review | `under_review` disputes assigned to reviewer or team. | Ops reviewer | Request information, recommend accepted/rejected outcome, route to compliance or fraud/risk. | Queue page p95 <= 800 ms for 25 items. |
| Needs information | `needs_information` disputes waiting for user response. | Ops reviewer or system job | Mark response received, return to `under_review`, close if withdrawal/timeout policy is documented. | Deterministic oldest-actionable ordering. |
| Compliance review | Cases flagged by reason, amount, sensitive note, restricted data, or policy. | Compliance reviewer | Approve outcome, request more review, add compliance note, close reviewed case. | Role-restricted queue only. |
| Fraud/risk review | Cases with suspicious pattern or reviewer escalation. | Fraud/risk reviewer | Add risk classification, recommend route, return to ops/compliance. | Fraud rationale hidden from user-facing text. |

Queue filters should include status, reason category, assigned reviewer, age bucket, compliance flag, fraud/risk flag, and requires-user-response flag. Cursor pagination should default to 25 items and cap at 100 items.

## Sensitive Operator Actions

| Action | Initiating role | Review expectation | Required evidence | Audit event |
| --- | --- | --- | --- | --- |
| Accept sensitive dispute | Ops reviewer | Compliance review when case is flagged sensitive. | Reason code, evidence metadata present or reason why not required, current version. | `dispute_accepted` plus approval reference when required. |
| Reject sensitive dispute | Ops reviewer | Compliance review when case is flagged sensitive. | Allowlisted rejection reason and safe user-facing summary. | `dispute_rejected` plus approval reference when required. |
| Add fraud/risk classification | Fraud/risk reviewer | Single role review is acceptable for classification; user-visible outcome still needs normal review. | Restricted note and classification reason. | `dispute_risk_classified`. |
| Reveal restricted evidence metadata | Compliance or fraud/risk reviewer | Single authorized access with reason code. | Access reason and case assignment or escalation. | `restricted_metadata_viewed`. |
| Close case from `needs_information` | Ops reviewer or system job | Requires documented withdrawal or timeout policy; no legal deadline asserted. | Closure reason and last user-response marker. | `dispute_closed`. |

If dual approval is not required, the spec must state why the action is low enough risk for single approval. No operator may approve their own sensitive escalation when dual review is required.

## Audit-Safe Notes

Operator notes must:

- Use a `note_type`, `reason_code`, and role-scoped `visibility`.
- Avoid raw personal data, account numbers, PAN, CVV, authentication values, secrets, raw provider responses, and full user narratives.
- Use neutral language and avoid unsupported legal or fraud conclusions.
- Include enough structured context for another reviewer to understand the decision path.
- Preserve previous notes rather than editing history; corrections should be new notes that reference the earlier note ID.

Unsafe note content should be rejected or masked before persistence according to the future implementation's documented redaction policy. Rejected unsafe content must not be written to audit logs.

## Escalation

Escalation guidance should distinguish:

- User-service issues that support can explain without changing case state.
- Ops review issues that require additional evidence metadata or transaction context.
- Compliance-sensitive issues that require compliance review.
- Fraud-risk issues that require fraud/risk review and restricted user-facing messaging.
- Technical incidents such as audit store outage, transaction lookup outage, queue failure, or redaction failure.
- External process questions that are outside this homework, such as chargebacks, refunds, regulator complaints, ADR, or FIN-NET.

User-visible messaging must remain safe and must not expose internal fraud/risk signals, compliance rationale, provider responses, or another user's information.

## Audit Evidence

Operator workflows should capture:

- Operator identity.
- Role or permission used.
- Timestamp.
- Workflow reason or reason code.
- Related dispute, transaction, account, and audit identifiers using opaque IDs.
- Before and after dispute state when safe and useful.
- Correlation ID.
- Note ID, evidence metadata ID, or approval reference when relevant.
- Sensitive-data-present marker rather than raw sensitive content.

Audit records must not contain raw secrets, full payment-card data, authentication values, real account numbers, raw evidence files, raw provider responses, or unnecessary personal data.

## Manual Checks For This Specification

Before completing or reviewing a Dispute Intake increment, confirm:

- What each operator role can do.
- What each operator role is forbidden to do.
- Which state transitions are allowed.
- Which transitions require compliance or fraud/risk involvement.
- What evidence metadata is required.
- What the user sees.
- What is recorded for audit.
- How unsafe notes, stale decisions, and dependency failures behave.
- Which external legal, refund, chargeback, regulator, or ADR processes remain out of scope.

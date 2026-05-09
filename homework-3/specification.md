# EU Payment-Account Dispute Intake Specification

> **Status**: Selected feature specification for Homework 3. This document is a documentation-only product and implementation specification. It does not create application code, APIs, UI screens, database migrations, or legal compliance guarantees.

## High-Level Objective

Enable EU/EEA payment-account users to file disputes against posted transactions while giving authorized internal ops and compliance users a controlled workflow to review, request more information, assign outcomes, close cases, and preserve audit-safe evidence.

## Scope Boundary

This specification covers dispute intake and internal case tracking only. It excludes chargeback processing, provisional credits, card-network arbitration, legal deadline enforcement, regulator reporting, customer reimbursement execution, and real file upload or storage handling.

## Mid-Level Objectives

| ID | Objective | Observable success |
| --- | --- | --- |
| M1 | Intake Eligibility And Submission | Users can create one dispute for their own eligible posted transaction, while ineligible, duplicate, and retried submissions have safe deterministic outcomes. |
| M2 | Evidence And User Follow-Up | The case captures safe user summaries, evidence metadata, information requests, and user responses without storing real files or unsafe free text. |
| M3 | Internal Review Workflow | Authorized support, ops, compliance, fraud/risk, and system actors can perform only their permitted queue, assignment, note, review, and state-transition actions. |
| M4 | Audit, Privacy, And Compliance Controls | State-changing actions preserve audit evidence, redaction, minimization, safe errors, restricted visibility, and scoped EU/EEA compliance assumptions. |
| M5 | Reliability, Concurrency, And Performance | Idempotency, stale-state handling, fail-closed audit behavior, pagination, latency, and read-after-write expectations are measurable for future builders. |

## Dispute State Machine

Durable states:

- `submitted`: user created the dispute and initial metadata is stored.
- `under_review`: an authorized reviewer has started internal review.
- `needs_information`: reviewer requested more user information or evidence metadata.
- `accepted`: internal intake review accepts the dispute for the next business process outside this scope.
- `rejected`: internal intake review rejects the dispute for a documented reason.
- `closed`: final tracking state after accepted, rejected, or withdrawn/intake-complete handling.

| From | To | Actor | Preconditions | Rejected transition behavior | Audit event |
| --- | --- | --- | --- | --- | --- |
| None | `submitted` | End user | Transaction exists, is posted, belongs to user, and no active dispute exists for the same transaction and reason category. | Return safe validation or conflict error; do not create a dispute; record rejected-intake audit when a correlation ID exists. | `dispute_submitted` |
| `submitted` | `under_review` | Ops reviewer | Reviewer has queue permission and current version matches. | Return stale-state or permission error. | `dispute_review_started` |
| `submitted` | `needs_information` | Ops reviewer | Missing information is specific and user-visible text is safe. | Return validation error for vague or unsafe request text. | `dispute_information_requested` |
| `under_review` | `needs_information` | Ops reviewer or compliance reviewer | Required information is documented with reason code. | Return validation or stale-state error. | `dispute_information_requested` |
| `needs_information` | `under_review` | End user or system job | User supplies requested evidence metadata or reviewer marks response sufficient. | Keep state unchanged and record safe failed-response event when metadata is invalid. | `dispute_information_received` |
| `under_review` | `accepted` | Ops reviewer with compliance review when required | Required evidence and reason code exist; sensitive cases have required approval. | Return missing-evidence, missing-approval, permission, or stale-state error. | `dispute_accepted` |
| `under_review` | `rejected` | Ops reviewer with compliance review when required | Rejection reason is selected from allowlisted codes and user-facing summary is safe. | Return missing-reason, permission, or stale-state error. | `dispute_rejected` |
| `accepted` | `closed` | Ops reviewer or system job | Closure reason states intake complete and no required review is pending. | Return invalid-transition or missing-review error. | `dispute_closed` |
| `rejected` | `closed` | Ops reviewer or system job | Closure reason states rejection communicated or intake complete. | Return invalid-transition or missing-review error. | `dispute_closed` |
| `needs_information` | `closed` | Ops reviewer or system job | User withdrew dispute or internal timeout policy is documented; no legal deadline is asserted. | Return invalid-transition if timeout policy is absent. | `dispute_closed` |

No transition may delete a dispute, erase audit history, or mutate the linked transaction record.

## Non-Functional And Policy Expectations

| Area | Requirement |
| --- | --- |
| Security | Enforce least-privilege access for user and operator actions. All examples and future fixtures must use opaque IDs such as `usr_`, `acct_`, `txn_`, `case_`, and `audit_`. |
| Privacy | Apply GDPR-style minimization: collect only dispute reason, safe description, evidence metadata, transaction reference, and workflow metadata required for intake. |
| Auditability | State-changing actions succeed only if the dispute change and safe audit event are both recorded. Audit records contain safe state labels and redacted references, not raw sensitive data. |
| Reliability | Retried dispute submission uses an idempotency key owned by the user/session and returns the original dispute reference for an equivalent retry. |
| Concurrency | Operator state changes require a current dispute version. Stale writes return a conflict and do not overwrite a newer status or note. |
| Redaction | Operator notes and evidence descriptions are allowlisted or scanned before persistence. Unsafe values are rejected or masked according to the future implementation's documented redaction policy. |
| Performance | Targets are assumed homework targets: user dispute creation p95 <= 500 ms, dispute detail p95 <= 400 ms, ops queue p95 <= 800 ms for paginated views, audit event write completed before success. |
| Pagination | Ops queue uses cursor pagination with default 25 items and maximum 100 items, ordered by oldest actionable item first with deterministic tie-break by dispute ID. |
| Availability | Intake should fail closed when audit persistence or permission checks are unavailable. It may show a safe retry message, but must not create unaudited state changes. |

## Implementation Notes

### Domain Assumptions

The primary regulatory framing is an EU/EEA payment-account context. The feature is informed by PSD2, GDPR, DORA, EBA ICT/security guidance, and EBA complaints-handling guidance as design rationale for a realistic homework specification, not as a claim that this repository implements legal compliance. Detailed rationale and limits live in `docs/domain-rules.md`; reviewer-facing rationale lives in `README.md`.

The dispute flow treats `accepted` and `rejected` as internal intake outcomes. They do not mean statutory liability has been determined, a refund has been issued, a chargeback has been filed, or an external dispute-resolution process has completed.

### Actors, Roles, And Permissions

| Role | Purpose | Allowed actions | Forbidden actions |
| --- | --- | --- | --- |
| End user | Account holder who disputes a posted transaction. | Create a dispute for their own posted transaction, view their own dispute status, add evidence metadata, respond to information requests. | View another user's disputes, edit internal notes, assign statuses, close cases, dispute pending transactions. |
| Support operator | First-line helper for user questions. | View masked dispute summaries, explain status, add support-safe notes, route to ops. | Accept, reject, or close disputes; view restricted evidence fields; override audit records. |
| Ops reviewer | Internal reviewer responsible for queue handling. | Review queue, assign case owner, move to `under_review`, request information, add structured notes, recommend accepted or rejected outcome. | Self-approve sensitive compliance/fraud decisions when dual review is required. |
| Compliance reviewer | Reviewer for policy-sensitive or escalation cases. | Review restricted queue items, approve sensitive outcomes, add compliance notes, close reviewed cases. | Alter transaction records, delete audit events, bypass redaction rules. |
| Fraud/risk reviewer | Reviewer for suspected misuse or fraud indicators. | Add fraud-risk classification, request escalation, provide fraud-review input. | Make unsupported legal conclusions or expose fraud signals to the user. |
| System job | Background service or workflow automation. | Apply timeout markers, emit reminders, maintain queue metadata, record audit events. | Create user disputes without a user action, suppress notes, change final outcomes without a configured rule. |

### Builder Guardrails

These controls apply to future implementation tasks derived from this specification. The full homework control baseline remains in `docs/domain-rules.md`, and agent workflow enforcement remains in `agents.md`.

| Control | Dispute Intake requirement | Later code-enforcement path |
| --- | --- | --- |
| Synthetic data only | Use synthetic users, accounts, transactions, dispute IDs, notes, and evidence metadata. Do not include real customer records, production logs, personal data, account numbers, PAN, CVV, authentication data, or secrets. | Fixture review, secret scanning, PII/PAN pattern checks, safe sample-data generators. |
| Role boundaries | Define end-user, support, ops reviewer, compliance reviewer, fraud/risk reviewer, and system-only actions before low-level tasks are considered complete. | Authorization middleware, role tests, forbidden-action tests, operator-view restrictions. |
| Safe audit events | Every create, evidence update, note creation, assignment, status transition, information request, and closure must describe safe audit evidence. | Audit-event schema, required event assertions in integration tests, correlation-ID checks. |
| Redaction | Logs, errors, audit notes, operator views, examples, and evidence metadata must mask sensitive values and avoid raw request/response bodies. | Structured logging filters, error-contract tests, redaction unit tests, log review checks. |
| Idempotent state changes | Retried dispute submission and retryable operator commands must define duplicate behavior and avoid duplicate cases or contradictory audit records. | Idempotency-key validation, duplicate-command tests, replay tests. |
| Explicit state machines | The durable dispute states, allowed transitions, rejected transitions, stale-state behavior, and audit expectations are defined below. | State-transition guards, transition matrix tests, stale-state conflict tests. |
| Human review for sensitive ops | Sensitive outcomes and fraud/compliance-sensitive decisions must identify whether single review or dual review is required and why. | Approval workflow states, reviewer-role checks, self-approval rejection tests. |
| Verification mapping | Each mid-level objective maps to acceptance criteria, future test categories, manual review evidence, and performance checks. | Test matrix, CI checks, manual review checklist, performance smoke checks. |

### Hypothetical Data Concepts

| Concept | Required fields | Notes |
| --- | --- | --- |
| `Dispute` | `dispute_id`, `user_id`, `account_id`, `transaction_id`, `status`, `reason_category`, `safe_user_summary`, `created_at`, `updated_at`, `version`, `idempotency_key_hash` | `safe_user_summary` must be redacted and length-limited. |
| `DisputeEvidenceMetadata` | `evidence_id`, `dispute_id`, `submitted_by`, `evidence_type`, `safe_description`, `redacted_reference`, `received_at` | No binary content, external file URL, malware scan status, or storage provider details are in scope. |
| `DisputeAuditEvent` | `event_id`, `occurred_at`, `actor_type`, `actor_id`, `actor_role`, `action`, `target_type`, `target_id`, `correlation_id`, `reason_code`, `before_state`, `after_state`, `sensitive_data_present` | Aligns with `docs/technical-conventions.md`; snapshots are labels or redacted fields only. |
| `OperatorNote` | `note_id`, `dispute_id`, `author_id`, `author_role`, `note_type`, `safe_body`, `reason_code`, `created_at`, `visibility` | `visibility` is one of `support`, `ops`, `compliance`, or `fraud`. |
| `OpsQueueView` | `dispute_id`, `status`, `age_bucket`, `assigned_to`, `reason_category`, `last_action_at`, `requires_compliance_review`, `requires_user_response` | Excludes raw user narrative and restricted evidence details by default. |

### Command And Error Semantics

- User-created disputes require a client-provided idempotency key for retryable submissions.
- Operator transitions require a current `version` or equivalent compare-and-swap token.
- Errors use stable codes such as `transaction_not_posted`, `transaction_not_found`, `duplicate_active_dispute`, `permission_denied`, `invalid_transition`, `stale_dispute_version`, `unsafe_note_content`, and `audit_write_unavailable`.
- User-facing errors must not reveal whether another user's transaction exists.
- Operator-facing errors may include safe reason codes and correlation IDs, but not raw provider responses or sensitive narratives.

### Reason Categories

Use an allowlisted set for intake examples:

- `unauthorized_payment`
- `incorrect_amount`
- `duplicate_payment`
- `merchant_not_recognized`
- `goods_or_services_issue`
- `other_payment_problem`

These categories support intake triage only. They do not determine statutory liability or refund eligibility.

## Context

### Beginning Context

Before implementation work begins, the hypothetical product has:

- A user identity system with opaque user IDs and role claims.
- Posted transaction records with opaque transaction IDs, account ownership, amount, currency, merchant label, and posted timestamp.
- No dispute records, no dispute-specific audit events, no evidence metadata model, and no operator queue.
- Existing engineering conventions for IDs, timestamps, money, state machines, redaction, pagination, and audit metadata in `docs/technical-conventions.md`.
- Domain-control baseline and EU banking-style rationale in `docs/domain-rules.md`.

### Ending Context

After the low-level tasks are complete, the specification package should describe:

- Dispute creation for eligible posted transactions.
- Evidence metadata capture without real file handling.
- A role-controlled ops/compliance queue.
- The complete dispute state machine and transition guardrails.
- Safe audit events and redaction rules for every state-changing flow.
- Edge cases, verification expectations, and assumed performance targets.
- Supporting agent, operator, README, and domain-rule guidance aligned to EU payment-account intake.

## Edge Cases And Failure Modes

| Case | Expected user-visible behavior | Audit/compliance implication |
| --- | --- | --- |
| User has no posted transactions | Show empty state explaining there are no eligible transactions to dispute. | No dispute audit event; optional safe page-view telemetry only. |
| Transaction is pending | Reject with `transaction_not_posted`. | Record rejected-intake audit with transaction reference only if it belongs to the user. |
| Transaction does not belong to user | Return generic not-found or ineligible message. | Record permission failure with actor and correlation ID, not target details. |
| Duplicate active dispute | Return existing dispute reference for idempotent equivalent retry; return conflict for different reason/details. | Audit duplicate attempt without creating another active case. |
| Existing closed dispute | Allow new dispute only if policy permits a new reason category; otherwise return conflict. | Audit reopened/new-intake decision with reason code. |
| Evidence metadata missing | Allow initial dispute only if reason category permits no evidence yet; otherwise move to or remain in `needs_information`. | Audit missing-evidence condition and reviewer request. |
| Unsafe evidence description or note | Reject or redact according to policy and return safe validation message. | Audit redaction failure marker without storing raw unsafe value. |
| Concurrent operator decisions | First valid version wins; later stale version receives conflict. | Audit rejected stale action with actor, attempted action, and current state label. |
| Audit store unavailable | Do not commit visible state change. | Return retryable service error; emit operational alert outside dispute audit if available. |
| Fraud-ish pattern across disputes | Case may be routed to fraud/risk review using safe classification. | Do not expose fraud rationale to end user; restrict note visibility. |
| User withdraws during review | Transition to `closed` with withdrawal reason if policy allows. | Audit actor, state, and reason without deleting previous evidence metadata. |
| Compliance approval missing | Block `accepted`, `rejected`, or `closed` transition when marked sensitive. | Audit blocked transition and required reviewer role. |

## Verification Plan

| Objective | Acceptance evidence | Future test categories | Manual review evidence |
| --- | --- | --- | --- |
| M1 | User can create one dispute for own posted transaction and receives `case_` reference; ineligible and duplicate attempts resolve safely. | Unit validation, integration create flow, idempotent retry test, negative eligibility tests for pending, missing, wrong owner, reversed, and duplicate transactions. | Sample happy-path request/response in future API docs; reviewer confirms errors do not leak another user's data. |
| M2 | Evidence metadata and user responses are stored without real files or unsafe content. | Schema tests, evidence-type validation tests, safe-description tests, information-request and response-flow tests. | Evidence examples contain metadata only, no real file URLs, and safe user-facing request text. |
| M3 | Ops/compliance/fraud queues and transitions obey role boundaries and the transition matrix. | Authorization tests, queue filter tests, state-transition matrix tests, stale-version tests. | Operator manual shows allowed/forbidden actions and review expectations per role. |
| M4 | State-changing actions write audit evidence and apply redaction, minimization, and scoped compliance controls. | Audit-event integration tests, redaction unit tests, unsafe-note validation tests, log allowlist tests, permission-denial tests. | Audit field checklist completed for each transition; document scan confirms synthetic data and no raw sensitive values. |
| M5 | Reliability and performance targets are measurable. | Performance smoke tests for create/detail/queue, pagination tests, idempotency replay tests, audit-failure tests, stale-state conflict tests. | Performance assumptions and rationale are visible in this spec and README. |

## Expected Performance

These are assumed homework targets, not production service-level agreements.

| Flow | Target | Rationale |
| --- | --- | --- |
| Create dispute | p95 <= 500 ms when transaction lookup and audit store are healthy. | A user filing a dispute expects immediate confirmation, and the flow writes only a case record, metadata, and audit event. |
| View dispute detail | p95 <= 400 ms for the user's own dispute. | Detail view is a bounded read by dispute ID with role filtering. |
| Ops queue list | p95 <= 800 ms for default 25-item cursor page; maximum page size 100. | Operators need scan-friendly queues, but the query includes filters and permission shaping. |
| State transition | p95 <= 500 ms when audit store is healthy. | Transition writes one dispute update and one audit event. |
| Read-after-write | Newly created or transitioned dispute visible in detail view within 2 seconds. | Strong or near-strong consistency is appropriate for user trust and operator coordination. |
| Audit write | Must complete before success is returned. | Dispute state changes are compliance-sensitive and must not become unaudited. |

## Low-Level Tasks

| Task | Supports | Agent instruction | Artifact | Acceptance criteria |
| --- | --- | --- | --- | --- |
| 1. Define EU payment-account scope | M1, M4 | State EU/EEA payment-account assumptions and out-of-scope exclusions before workflow details. | `specification.md`, `docs/domain-rules.md`, `README.md` | Scope names PSD2/GDPR/DORA rationale and excludes chargeback, provisional credit, legal deadline enforcement, regulator reporting, and real files. |
| 2. Define role model | M3, M4 | Add end-user, support, ops, compliance, fraud/risk, and system roles with allowed and forbidden actions. | `specification.md`, `agents.md`, `docs/operator-manual.md` | Each role has at least one allowed and one forbidden action; sensitive actions name review expectations. |
| 3. Define dispute state machine | M3, M4, M5 | Add durable states, allowed transitions, actor, preconditions, rejected-transition behavior, and audit event names. | `specification.md` | Every listed state can be reached or closed through an explicit transition; forbidden/stale transitions have expected behavior. |
| 4. Define transaction eligibility | M1 | Specify that only owned posted transactions are eligible and define pending, missing, wrong-owner, reversed, and duplicate cases. | `specification.md` | Eligibility rules include safe user error behavior and audit implications. |
| 5. Define evidence metadata | M2, M4 | Specify metadata fields and explicitly exclude binary files, storage provider design, and real file URLs. | `specification.md`, `docs/domain-rules.md` | Evidence section includes type, safe description, submitter, timestamp, and redacted reference; no real file handling appears. |
| 6. Define audit events | M4 | Map every state-changing action to required safe audit metadata and blocked-action audit behavior. | `specification.md`, `docs/technical-conventions.md` if needed | Audit requirements include actor, role, target, correlation ID, reason code, before/after state, timestamp, and sensitive-data marker. |
| 7. Define redaction rules | M4 | Add rules for notes, logs, errors, examples, operator views, and evidence descriptions. | `specification.md`, `agents.md`, `.github/copilot-instructions.md` | Rules forbid raw PII, PAN, CVV, account numbers, auth values, secrets, raw provider responses, and production logs. |
| 8. Define ops queue behavior | M3, M5 | Specify queue entry criteria, filters, sorting, pagination, assignment, and review ownership. | `specification.md`, `docs/operator-manual.md` | Queue has default/max page size, deterministic ordering, role filters, and service target. |
| 9. Define edge cases | M1-M5 | Add feature-specific failure-mode table with user-visible result and audit/compliance implication. | `specification.md` | Table covers empty states, duplicate commands, stale state, permission failures, audit failure, unsafe notes, and fraud-ish patterns. |
| 10. Define verification mapping | M1-M5 | Map each mid-level objective to acceptance evidence, future test category, and manual review evidence. | `specification.md` | Each objective appears in the verification matrix and has checkable evidence. |
| 11. Update regulatory baseline | M1-M5 | Extend generic domain rules with PSD2, GDPR, DORA, EBA complaints, and ADR/FIN-NET context. | `docs/domain-rules.md` | Rules are cited or linked, scoped as rationale, and do not assert exact retention periods, statutory deadlines, or compliance guarantees. |
| 12. Update reviewer rationale | M1-M5 | Explain why Dispute Intake was chosen, why EU context is primary, and how targets were selected. | `README.md` | README status, rationale, package map, and best-practice references match the selected feature. |
| 13. Update agent/editor rules | M3, M4 | Replace generic future-feature instructions with dispute-specific constraints. | `agents.md`, `.github/copilot-instructions.md` | AI rules mention posted transactions, state machine, evidence metadata only, audit-safe notes, and redaction. |
| 14. Update changelog and verify | M1-M5 | Add newest-first Step 8 and run documentation checks. | `CHANGELOG.md` | Changelog summarizes structural cleanup, objective consolidation, docs updated, and verification performed. |

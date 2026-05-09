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

The following task cards describe future implementation slices for the Dispute Intake feature. They are intentionally written as implementation-ready work items, not as document-maintenance steps. Each task preserves the required fields while avoiding a wide table that is difficult to read.

### M1 - Intake Eligibility And Submission

#### M1.1 Transaction lookup and eligible empty state
- **Supports:** M1, M5
- **Implementation prompt:** Build the user-facing transaction lookup path that returns only dispute-eligible posted transactions for the authenticated user.
- **Create or update:** Create the eligible-transaction query, user transaction-list response shape, empty-state response, and lookup coverage fixtures.
- **Core behavior:** Return posted transactions owned by the user with opaque `txn_` and `acct_` identifiers, amount, currency, merchant label, and posted timestamp. If none exist, return a safe empty state that explains there are no eligible transactions to dispute.
- **Edge cases and failure modes:** Hide pending, reversed, missing, and wrong-owner transactions. Do not reveal whether another user's transaction exists. Treat transaction lookup outage as retryable dependency failure.
- **Acceptance criteria:** A user with eligible posted transactions sees only their own eligible items. A user with no eligible transactions receives no dispute form for a specific transaction. Empty-state behavior creates no dispute or dispute audit event.
- **Verification:** Cover happy-path lookup, empty eligible state, pending transaction exclusion, wrong-owner non-disclosure, transaction lookup dependency failure, and p95 lookup contribution to the create-dispute target.

#### M1.2 Posted-transaction ownership and eligibility guard
- **Supports:** M1, M4
- **Implementation prompt:** Implement the create-dispute eligibility guard before any dispute record is created.
- **Create or update:** Create the eligibility validator, posted-transaction ownership check, safe validation errors, and rejected-intake audit hook.
- **Core behavior:** Allow intake only when the transaction exists, belongs to the authenticated user, is posted, and is not excluded by known transaction state.
- **Edge cases and failure modes:** Return `transaction_not_posted` for owned pending transactions, generic not-found or ineligible messaging for wrong-owner transactions, and safe dependency errors when ownership cannot be verified.
- **Acceptance criteria:** Ineligible submissions do not create `Dispute`, `DisputeEvidenceMetadata`, or queue records. Owned pending transactions produce a specific safe error. Wrong-owner and missing transactions do not leak target details.
- **Verification:** Cover owned posted, owned pending, missing, wrong-owner, reversed, permission-denied, and transaction-service-unavailable cases with unit and integration eligibility tests.

#### M1.3 Reason category and safe user summary validation
- **Supports:** M1, M2, M4
- **Implementation prompt:** Validate reason categories and sanitize user-provided summaries during intake.
- **Create or update:** Create reason-category validation, safe summary length rules, redaction checks, and unsafe-content error handling.
- **Core behavior:** Accept only allowlisted reason categories and persist a redacted, length-limited `safe_user_summary` suitable for user detail, operator queue preview, and audit context.
- **Edge cases and failure modes:** Reject unknown categories, empty required summaries, oversized summaries, raw account-like values, PAN-like values, authentication values, secrets, and unsafe accusations.
- **Acceptance criteria:** Invalid categories return a stable validation code. Unsafe summaries are rejected or masked according to policy before persistence. Raw unsafe input is not copied into audit, logs, or operator notes.
- **Verification:** Cover valid categories, invalid categories, empty summary, oversized summary, PAN-like text, account-like text, token-like text, and log/audit redaction assertions.

#### M1.4 Duplicate active dispute handling
- **Supports:** M1, M5
- **Implementation prompt:** Prevent duplicate active disputes for the same user-owned transaction and reason category.
- **Create or update:** Create duplicate lookup rules, active-status definition, conflict response, and duplicate-attempt audit event.
- **Core behavior:** Treat `submitted`, `under_review`, and `needs_information` as active for duplicate prevention. Return the existing `case_` reference only for an equivalent idempotent retry; otherwise return `duplicate_active_dispute`.
- **Edge cases and failure modes:** Different reason category, changed summary, changed evidence metadata, closed prior dispute, and concurrent submissions must produce deterministic behavior.
- **Acceptance criteria:** Equivalent retries do not create a second case. Non-equivalent duplicate attempts return conflict. Closed-case behavior follows documented policy and does not reopen history silently.
- **Verification:** Cover duplicate active case, equivalent idempotent replay, changed-payload conflict, closed prior case, and concurrent double-submit tests.

#### M1.5 Idempotent dispute creation with `case_` reference
- **Supports:** M1, M4, M5
- **Implementation prompt:** Create the successful intake command that persists the dispute and returns a stable user-facing case reference.
- **Create or update:** Create the dispute creation command, idempotency-key handling, `case_` reference generation, initial queue metadata, and `dispute_submitted` audit write.
- **Core behavior:** On valid submission, create one `Dispute` in `submitted` status with `idempotency_key_hash`, version `1`, safe summary, reason category, and linked transaction identifiers.
- **Edge cases and failure modes:** Missing idempotency key, reused key with different payload, audit write failure, queue metadata failure, and retry after client timeout must not produce duplicate cases.
- **Acceptance criteria:** Successful intake returns one stable `case_` reference. Equivalent retry returns the original result. No success is returned unless the dispute and audit event are both durably recorded.
- **Verification:** Cover happy-path create, missing idempotency key, key replay, key collision with changed payload, audit-write failure, retry after timeout, and read-after-write visibility within 2 seconds.

### M2 - Evidence And User Follow-Up

#### M2.1 Evidence metadata schema
- **Supports:** M2, M4
- **Implementation prompt:** Define metadata-only evidence records linked to a dispute without implementing binary file storage.
- **Create or update:** Create the `DisputeEvidenceMetadata` model, persistence contract, safe response shape, and metadata-only fixtures.
- **Core behavior:** Persist `evidence_id`, `dispute_id`, `submitted_by`, `evidence_type`, `safe_description`, `redacted_reference`, and `received_at` with opaque IDs and UTC timestamps.
- **Edge cases and failure modes:** Reject binary payloads, real file URLs, storage-provider references, malware-scan fields, missing dispute IDs, and evidence for disputes the actor cannot access.
- **Acceptance criteria:** Evidence records contain metadata only. No task, fixture, response, audit event, or example contains a real file body or real download URL. Evidence cannot be attached across user boundaries.
- **Verification:** Cover schema validation, metadata-only enforcement, forbidden binary/file URL fields, wrong-dispute permission denial, and synthetic fixture review.

#### M2.2 Evidence type allowlist and requiredness rules
- **Supports:** M2, M3, M4
- **Implementation prompt:** Implement allowed evidence types and reason-category-specific evidence expectations.
- **Create or update:** Create evidence type allowlist, optional-versus-required evidence rules, and reviewer-facing missing-evidence indicators.
- **Core behavior:** Accept only configured evidence types and mark whether the current reason category can proceed with no evidence, needs reviewer follow-up, or blocks sensitive outcome decisions.
- **Edge cases and failure modes:** Unknown evidence type, duplicate metadata reference, missing evidence for sensitive acceptance or rejection, and evidence submitted after closure must resolve safely.
- **Acceptance criteria:** Invalid evidence type returns a stable validation error. Missing evidence can route a case to `needs_information` without inventing legal deadlines. Closed cases cannot receive new evidence metadata unless a documented policy allows correction notes.
- **Verification:** Cover valid type, invalid type, duplicate evidence reference, no-evidence permitted category, no-evidence needs-information path, sensitive outcome blocked for missing evidence, and closed-case update rejection.

#### M2.3 Safe descriptions and redacted references
- **Supports:** M2, M4
- **Implementation prompt:** Apply redaction and safe-text validation to evidence descriptions and references.
- **Create or update:** Create redaction checks for `safe_description`, redacted reference formatting, unsafe-content rejection, and audit-safe failure markers.
- **Core behavior:** Store short descriptions that help reviewers understand the evidence type without storing raw personal data, account numbers, PAN, CVV, authentication values, secrets, or raw provider responses.
- **Edge cases and failure modes:** Unsafe pasted text, realistic account values, token-like strings, excessive detail, unsupported fraud accusations, and redaction service outage must not persist raw unsafe content.
- **Acceptance criteria:** Unsafe values are rejected or masked before persistence. Rejected unsafe values are not written into audit events or logs. Evidence references remain opaque or redacted.
- **Verification:** Cover redaction unit tests for PAN-like, account-like, token-like, authentication-like, long free-text, and raw-provider-response examples plus log and audit allowlist assertions.

#### M2.4 Reviewer information request flow
- **Supports:** M2, M3, M4
- **Implementation prompt:** Allow authorized reviewers to request more information from the user with safe user-visible text.
- **Create or update:** Create information-request command, request reason codes, user-visible request text validation, status transition to `needs_information`, and `dispute_information_requested` audit event.
- **Core behavior:** From `submitted` or `under_review`, an authorized reviewer can request specific missing metadata or clarification and move the case to `needs_information`.
- **Edge cases and failure modes:** Vague request text, unsafe request text, wrong role, stale version, missing reason code, and audit write failure must block the request.
- **Acceptance criteria:** Valid requests are visible to the user without internal rationale. Invalid or unsafe requests do not change state. The audit event records actor, role, reason code, before/after state, and correlation ID.
- **Verification:** Cover happy-path request, support-role denial, unsafe request text, missing reason code, stale version, audit-write failure, and user-visible text redaction.

#### M2.5 User response to information request
- **Supports:** M2, M3, M5
- **Implementation prompt:** Let users respond to an information request by adding safe metadata or clarification and returning the case to review.
- **Create or update:** Create user response command, evidence metadata attachment path, safe response summary, status transition from `needs_information` to `under_review`, and `dispute_information_received` audit event.
- **Core behavior:** Accept a response only from the dispute owner while the case is in `needs_information`; record safe metadata and make the case actionable for reviewers again.
- **Edge cases and failure modes:** Response by wrong user, response in wrong state, unsafe response text, duplicate response retry, evidence validation failure, and concurrent closure must not corrupt state.
- **Acceptance criteria:** Valid response moves the case back to `under_review` or marks reviewer action required according to queue rules. Invalid metadata leaves the state unchanged and records only safe failure context when appropriate.
- **Verification:** Cover owner response happy path, wrong-user denial, wrong-state conflict, unsafe text rejection, idempotent duplicate response, concurrent close conflict, and queue read-after-write visibility.

#### M2.6 User dispute detail and status visibility
- **Supports:** M2, M3, M4, M5
- **Implementation prompt:** Build the user-safe dispute detail view for status, submitted metadata, requests, and responses.
- **Create or update:** Create user detail response shape, role-filtered visibility rules, status timeline projection, and safe evidence metadata projection.
- **Core behavior:** Show the dispute owner the `case_` reference, current status, reason category, safe summary, safe evidence metadata, outstanding information requests, and user-facing outcome text.
- **Edge cases and failure modes:** Hide internal notes, fraud/risk classifications, compliance rationale, restricted evidence fields, another user's case, stale reads after transition, and dependency failures.
- **Acceptance criteria:** Users can see their own safe case details and cannot see internal-only fields. Accepted and rejected labels are described as intake outcomes only. Detail view meets the p95 target and read-after-write expectation.
- **Verification:** Cover own-case detail, wrong-user denial, role-filtered field assertions, accepted/rejected safe wording, outstanding request display, read-after-write after create and response, and detail p95 smoke checks.

### M3 - Internal Review Workflow

#### M3.1 Role permission matrix enforcement
- **Supports:** M3, M4
- **Implementation prompt:** Enforce the documented role model across user, support, ops, compliance, fraud/risk, and system-job actions.
- **Create or update:** Create authorization policy checks, forbidden-action responses, role-specific test fixtures, and permission-denial audit coverage.
- **Core behavior:** Permit each actor only the actions listed for its role and deny status changes, restricted data access, and internal notes to unauthorized roles.
- **Edge cases and failure modes:** Missing role claim, multiple role claims, expired operator session, support attempting outcome changes, system job attempting user-created dispute, and fraud/risk exposing user-visible conclusions.
- **Acceptance criteria:** Every protected command checks role and case access before reading or mutating restricted fields. Permission failures return safe errors and do not change dispute state.
- **Verification:** Cover role matrix tests for end user, support, ops, compliance, fraud/risk, and system job, including at least one allowed and one forbidden action per role.

#### M3.2 Ops queue filters, sorting, and pagination
- **Supports:** M3, M5
- **Implementation prompt:** Build role-shaped operator queue views for actionable disputes.
- **Create or update:** Create queue projection, filters, deterministic sorting, cursor pagination, role restrictions, and queue performance checks.
- **Core behavior:** Support filters for status, reason category, assigned reviewer, age bucket, compliance flag, fraud/risk flag, and requires-user-response flag. Default page size is 25 and maximum is 100.
- **Edge cases and failure modes:** Invalid cursor, page size over maximum, restricted queue access, changed data between pages, no queue results, and queue dependency timeout must resolve safely.
- **Acceptance criteria:** Queue results are ordered by oldest actionable item first with deterministic tie-break by dispute ID. Restricted queues expose only authorized items and omit raw user narratives.
- **Verification:** Cover filter combinations, default and maximum page sizes, invalid cursor, deterministic ordering, no-results state, compliance/fraud queue restrictions, mutable-data pagination, and p95 <= 800 ms queue smoke checks.

#### M3.3 Assignment and review ownership
- **Supports:** M3, M4, M5
- **Implementation prompt:** Implement reviewer assignment, ownership changes, and version-aware coordination.
- **Create or update:** Create assignment command, assignment reason codes, owner field updates, queue projection refresh, and assignment audit event.
- **Core behavior:** Authorized ops or compliance reviewers can assign eligible cases to a reviewer or team when the current dispute version matches.
- **Edge cases and failure modes:** Assigning closed cases, assigning to unauthorized reviewer, stale version, self-assignment for sensitive cases, missing reason code, and audit write failure must not create misleading ownership.
- **Acceptance criteria:** Assignment changes preserve previous owner in safe audit metadata. Stale assignment attempts return conflict. Queue views reflect assignment changes within the read-after-write target.
- **Verification:** Cover assignment happy path, reassignment, unauthorized assignee, closed-case rejection, stale version conflict, missing reason code, audit-write failure, and queue projection refresh.

#### M3.4 State transition executor
- **Supports:** M3, M4, M5
- **Implementation prompt:** Implement the transition matrix for `submitted`, `under_review`, `needs_information`, `accepted`, `rejected`, and `closed`.
- **Create or update:** Create transition guard, precondition checks, status update command, transition reason codes, and transition audit events.
- **Core behavior:** Allow only documented transitions by the documented actor, with current version, required reason code, required evidence or approval, and safe user-visible outcome text.
- **Edge cases and failure modes:** Invalid transition, wrong actor, stale version, missing evidence, missing approval, unsafe rejection summary, closure without policy, and audit write failure must block the transition.
- **Acceptance criteria:** Every durable state can reach its documented next states and cannot skip required review. Accepted and rejected remain internal intake outcomes. No transition mutates the linked transaction record.
- **Verification:** Cover full transition matrix tests, invalid-transition tests, stale version tests, missing-precondition tests, audit event assertions, safe outcome wording, and linked-transaction immutability checks.

#### M3.5 Operator notes with scoped visibility
- **Supports:** M3, M4
- **Implementation prompt:** Implement structured operator notes with role-scoped visibility and redaction.
- **Create or update:** Create `OperatorNote` persistence, note type allowlist, visibility rules, correction-note behavior, redaction validation, and note-created audit event.
- **Core behavior:** Allow support, ops, compliance, and fraud/risk reviewers to add notes only within permitted visibility scopes using `note_type`, `reason_code`, and `safe_body`.
- **Edge cases and failure modes:** Unsafe note text, missing reason code, unsupported note type, editing historical note, wrong role visibility, note on closed case, and redaction failure must resolve safely.
- **Acceptance criteria:** Notes are append-only; corrections create new notes. User detail never exposes internal notes. Fraud/risk notes are hidden from support and user-facing outputs unless explicitly permitted by role policy.
- **Verification:** Cover note creation per role, unsafe content rejection, append-only correction, visibility filtering, closed-case note policy, redaction failure, and audit/log allowlist assertions.

#### M3.6 Sensitive outcome review and self-approval prevention
- **Supports:** M3, M4
- **Implementation prompt:** Enforce additional review requirements for sensitive accepted, rejected, or closed outcomes.
- **Create or update:** Create sensitive-case flag evaluation, approval record reference, dual-review guard, self-approval denial, and blocked-transition audit event.
- **Core behavior:** When a case is marked sensitive by reason, amount, restricted data, fraud/risk signal, or policy flag, outcome transitions require the configured compliance approval and cannot be approved by the same actor who requested the sensitive outcome.
- **Edge cases and failure modes:** Missing approval, approval by same operator, approval by wrong role, stale approval, removed sensitivity flag, and conflicting fraud/compliance recommendations must block or route the action safely.
- **Acceptance criteria:** Sensitive accepted/rejected outcomes include an approval reference in audit metadata. Self-approval is rejected when dual review is required. User-visible text does not expose restricted rationale.
- **Verification:** Cover sensitive accept, sensitive reject, low-risk single-review path, missing approval, wrong-role approval, self-approval denial, stale approval, restricted-rationale hiding, and audit approval-reference assertions.

### M4 - Audit, Privacy, And Compliance Controls

#### M4.1 Audit event contract for state-changing actions
- **Supports:** M4, M1, M2, M3, M5
- **Implementation prompt:** Implement the safe audit-event contract for every state-changing dispute action.
- **Create or update:** Create audit event schema, required event names, audit writer integration, and event assertion fixtures.
- **Core behavior:** Emit safe audit records for create, rejected intake, evidence update, note creation, assignment, information request, user response, transition, blocked transition, stale action, and closure.
- **Edge cases and failure modes:** Missing correlation ID, unsafe before/after snapshot, audit store unavailable, duplicate idempotent replay, and partial write attempts must not create unaudited visible changes.
- **Acceptance criteria:** Audit events include actor type, actor ID, role, action, target type and ID, correlation ID, reason code when required, safe before/after state, timestamp, and sensitive-data marker. Raw sensitive values are never stored.
- **Verification:** Cover audit schema validation and per-action event assertions for all required event names, including idempotent replay and audit failure scenarios.

#### M4.2 Blocked, stale, and rejected action audit records
- **Supports:** M4, M5
- **Implementation prompt:** Record safe audit evidence for blocked actions where a correlation ID and actor context exist.
- **Create or update:** Create blocked-action audit mapping, stale-action event shape, rejected-intake event shape, and safe target-detail rules.
- **Core behavior:** Preserve accountability for permission failures, invalid transitions, stale versions, duplicate attempts, rejected intake, unsafe content attempts, and missing approval without storing raw unsafe payloads.
- **Edge cases and failure modes:** Wrong-owner transaction attempts, unsafe text, stale operator decision, duplicate active dispute, permission denial before target lookup, and audit writer outage must avoid leaking target details.
- **Acceptance criteria:** Blocked-action audit records contain actor and attempted action with safe state labels only. Wrong-owner attempts do not record another user's transaction details. Unsafe raw content is never copied into audit.
- **Verification:** Cover rejected-intake audit, stale-action audit, blocked-transition audit, duplicate-attempt audit, wrong-owner privacy assertions, unsafe-content audit omission, and audit-writer outage behavior.

#### M4.3 Redaction and minimization across views and logs
- **Supports:** M4, M2, M3
- **Implementation prompt:** Apply minimization and redaction consistently across user views, operator views, errors, logs, audit records, notes, and evidence metadata.
- **Create or update:** Create field allowlists per view, redaction utility, structured logging rules, unsafe fixture scanner, and privacy review checklist.
- **Core behavior:** Expose only the fields each actor needs for the workflow and mask or reject account-like, card-like, token-like, authentication-like, secret-like, and raw provider-response values.
- **Edge cases and failure modes:** Free-text summaries, evidence descriptions, operator notes, error messages, queue previews, audit snapshots, and structured logs must not bypass allowlists.
- **Acceptance criteria:** User-facing views contain no internal rationale. Operator queues omit raw narratives by default. Logs and errors include correlation IDs and safe codes, not raw request or response bodies.
- **Verification:** Cover redaction unit tests, per-role projection tests, log allowlist tests, audit snapshot tests, unsafe fixture scans, and manual synthetic-data review.

#### M4.4 Safe error semantics and permission boundaries
- **Supports:** M4, M1, M3, M5
- **Implementation prompt:** Implement stable safe errors for validation, permission, stale-state, duplicate, unsafe-content, rate-limit, dependency, and audit-write failures.
- **Create or update:** Create error code catalog, user and operator error response shapes, recoverability classification, and error coverage tests.
- **Core behavior:** Return machine-readable error codes with safe summaries and correlation IDs where appropriate, while avoiding stack traces, raw provider responses, secrets, or sensitive input echoing.
- **Edge cases and failure modes:** Wrong-owner lookup, missing transaction, unsafe note, stale transition, duplicate active dispute, audit store outage, rate limit, and transaction dependency outage require distinct safe codes.
- **Acceptance criteria:** End-user errors do not reveal another user's resource. Operator errors may include safe reason codes and correlation IDs only. Each error class states whether the actor can retry, correct input, or escalate.
- **Verification:** Cover one test per error class: validation, permission, stale version, duplicate active dispute, unsafe content, rate limit, transaction dependency failure, audit-write failure, and invalid transition.

#### M4.5 Fail-closed critical dependency controls
- **Supports:** M4, M5
- **Implementation prompt:** Enforce fail-closed behavior when audit persistence, permission checks, or critical transaction lookup are unavailable.
- **Create or update:** Create dependency health checks, command preflight gates, transactional write ordering, safe retry responses, and operational alert hooks.
- **Core behavior:** Do not commit visible dispute state changes unless permission checks pass and the matching safe audit event can be persisted. Do not create disputes when transaction ownership cannot be confirmed.
- **Edge cases and failure modes:** Audit store timeout, permission service timeout, transaction lookup timeout, partial dispute write, queue projection failure, and retry after ambiguous timeout must resolve without duplicate or unaudited state.
- **Acceptance criteria:** Audit or permission dependency failure blocks mutation. Transaction lookup failure blocks intake. Queue projection failure does not claim completion unless durable dispute and audit state are consistent and recovery behavior is documented.
- **Verification:** Cover audit-store-unavailable, permission-service-unavailable, transaction-service-unavailable, redaction-service-unavailable, partial-write rollback or compensation, queue-projection failure, retry after timeout, recovery after outage, and operational alert assertions.

### M5 - Reliability, Concurrency, And Performance

#### M5.1 Stale version and concurrent decision behavior
- **Supports:** M5, M3, M4
- **Implementation prompt:** Protect operator and system-job writes with current-version checks.
- **Create or update:** Create compare-and-swap version checks, stale conflict responses, stale-action audit events, and concurrency fixtures.
- **Core behavior:** The first valid state-changing command with the current version wins; later commands using stale versions return `stale_dispute_version` and do not overwrite status, owner, notes, or evidence metadata.
- **Edge cases and failure modes:** Concurrent accept/reject, assignment during transition, user response during closure, note creation during restricted transition, and system timeout marker race must resolve deterministically.
- **Acceptance criteria:** Stale writes return conflict with safe current-state label. No stale command changes durable dispute state. Safe audit evidence records the rejected stale action when actor and correlation ID are available.
- **Verification:** Cover concurrent transition race, assignment race, user-response versus closure race, note versus status race, stale system-job marker, and stale-action audit assertions.

#### M5.2 Retry, idempotency, and rate-limit behavior
- **Supports:** M5, M1, M2, M4
- **Implementation prompt:** Define retry-safe behavior for user submissions and other retryable commands.
- **Create or update:** Create idempotency key ownership rules, replay response behavior, retry headers or guidance, rate-limit policy, and retry coverage fixtures.
- **Core behavior:** User-created disputes require a user/session-owned idempotency key. Equivalent replay returns the original result; non-equivalent replay with the same key returns a stable conflict. Excessive retries receive a safe rate-limit error.
- **Edge cases and failure modes:** Client timeout after success, duplicate network delivery, same key different payload, key used by another user, repeated unsafe submissions, and retry while dependency is degraded must not create duplicates.
- **Acceptance criteria:** Idempotency keys are scoped to the actor and command payload. Duplicate retries do not duplicate cases, evidence metadata, notifications, or audit history. Rate limits use safe errors and do not expose sensitive details.
- **Verification:** Cover equivalent replay, changed-payload replay, cross-user key reuse, timeout retry, duplicate delivery, repeated unsafe content, rate-limit response, and duplicate audit prevention.

#### M5.3 Performance and pagination target coverage
- **Supports:** M5, M1, M2, M3
- **Implementation prompt:** Add performance and pagination checks that prove the assumed homework targets are measurable.
- **Create or update:** Create performance smoke scenarios for create, detail, queue, transition, read-after-write, and cursor pagination.
- **Core behavior:** Measure create dispute p95 <= 500 ms, detail p95 <= 400 ms, queue p95 <= 800 ms for 25 items, state transition p95 <= 500 ms, and read-after-write visibility within 2 seconds under healthy dependencies.
- **Edge cases and failure modes:** Maximum page size, invalid cursor, empty page, mutable queue while paging, large assigned queue, slow audit write, and queue projection lag must be tested or explicitly flagged.
- **Acceptance criteria:** Future test reports can map each performance target to a scenario, fixture size, and pass/fail threshold. Queue pagination caps requests at 100 items and preserves deterministic ordering.
- **Verification:** Cover create, detail, queue, transition, and read-after-write performance smoke checks plus default page, max page, invalid cursor, empty page, mutable queue pagination, and projection-lag tests.

#### M5.4 Verification fixtures and objective traceability
- **Supports:** M1, M2, M3, M4, M5
- **Implementation prompt:** Build the future verification fixture set and coverage matrix that ties tests back to each objective.
- **Create or update:** Create synthetic fixture catalog, objective-to-test matrix, manual review checklist, and coverage labels for `M1` through `M5`.
- **Core behavior:** Provide synthetic users, accounts, posted and pending transactions, active and closed disputes, evidence metadata, operator roles, restricted notes, stale versions, and dependency-failure scenarios using only opaque IDs.
- **Edge cases and failure modes:** Fixtures must avoid real PII, account numbers, PAN, CVV, authentication values, secrets, raw provider responses, real production logs, realistic customer histories, and real evidence files.
- **Acceptance criteria:** Every mid-level objective maps to happy path, negative path, permission, audit, redaction, concurrency, and performance coverage where relevant. Manual review confirms fixtures are synthetic and scoped to intake only.
- **Verification:** Cover fixture linting, secret and sensitive-pattern scans, objective traceability review, audit-event coverage review, role-permission coverage review, and documentation review for unsupported legal or refund claims.

# Technical Conventions

## Purpose

This document defines feature-neutral conventions for a future finance-oriented implementation. The final `specification.md` may tighten these conventions for the selected feature, but future agents should not contradict them without documenting the reason.

## Identifiers

- Use stable opaque identifiers for public references.
- Do not expose sequential database IDs in user-facing examples.
- Use clear prefixes when a future feature needs multiple identifier types, such as `usr_`, `acct_`, `card_`, `txn_`, `case_`, or `audit_`.
- Treat identifiers as strings, not numbers, to avoid truncation and formatting problems.

## Time And Dates

- Store timestamps in UTC.
- Include timezone offsets in human-facing examples when time interpretation matters.
- Use ISO 8601 format for examples, such as `2026-05-08T14:30:00Z`.
- Record both event time and processing time when a future flow can receive delayed or replayed events.

## Money And Numeric Values

- Represent money with fixed-precision decimal semantics, never binary floating point.
- Include currency alongside every monetary amount.
- Use ISO 4217 currency codes in examples unless the future spec deliberately narrows the scope.
- Define rounding behavior at the feature-spec level before any calculation task is considered complete.
- Reject ambiguous amounts such as empty strings, localized separators without parsing rules, negative limits where not allowed, and currency mismatches.

## State And Idempotency

- Prefer idempotent writes for operations that clients, queues, or payment networks may retry.
- Use idempotency keys for externally initiated state changes when duplicate execution would be harmful.
- Define valid state transitions in the feature spec before defining low-level tasks.
- State-changing operations should produce audit evidence even when the visible state does not change because the request was idempotent.
- State-changing feature specs should define the idempotency key owner, key lifetime assumption, duplicate-response behavior, and whether the second request returns the original result or a stable conflict.
- A retried command must not create duplicate financial effects, duplicate user notifications, or contradictory audit records.

## State Machines

- Define every durable business state before low-level tasks are written.
- For each state transition, specify the actor allowed to initiate it, required preconditions, user-visible result, audit event, and rejected-transition behavior.
- Treat stale version or stale timestamp conflicts as expected edge cases, not generic failures.
- Do not infer transitions from prose alone; future specs should include a table or diagram when a feature has more than three states.

## Errors

- Error examples should be safe for users and operators to share.
- Do not expose stack traces, raw provider responses, secrets, or sensitive data in errors.
- Use stable machine-readable error codes plus human-readable summaries.
- Distinguish validation failures, permission failures, stale-state conflicts, rate limits, and dependency failures.
- Future specs should define whether an error is user-recoverable, operator-recoverable, or requires engineering intervention.

## Pagination And Listing

- Any list that can grow beyond a small fixed set must define pagination.
- Prefer cursor pagination for mutable finance records.
- Define default and maximum page sizes in the feature spec.
- Preserve deterministic ordering for audit and reconciliation views.

## Audit Metadata

State-changing future tasks should define audit metadata including:

- Actor type, such as end user, operator, system job, or external provider.
- Actor identifier or service identifier.
- Correlation ID or request ID.
- Before and after state when safe and useful.
- Reason code or workflow reason when an operator acts.
- Timestamp and source channel.
- Target resource type and opaque target identifier.
- Idempotency key or replay marker when a command can be retried.
- Review or approval identifier when a sensitive operator action requires human review.

Safe audit events should follow this shape in future implementation specs:

| Field | Requirement |
| --- | --- |
| `event_id` | Stable unique audit event identifier. |
| `occurred_at` | UTC event timestamp. |
| `actor_type` and `actor_id` | End user, operator, system job, or provider identity, using opaque identifiers. |
| `actor_role` | Role or permission used for the action. |
| `action` | Stable action name, such as `freeze_card` or `approve_limit_override`. |
| `target_type` and `target_id` | Opaque target reference. |
| `correlation_id` | Request or workflow correlation identifier. |
| `reason_code` | Required for operator actions and optional for user actions. |
| `before_state` and `after_state` | Include only safe state labels or redacted snapshots. |
| `sensitive_data_present` | Boolean or classification marker, not raw sensitive values. |

## Logging And Redaction

- Logs should support debugging without exposing sensitive financial or personal data.
- Mask account-like, card-like, token-like, and authentication values in examples.
- Prefer structured logs with correlation IDs for workflows that cross services.
- Do not include raw request or response bodies unless the future spec defines a safe redaction policy.
- Use allowlisted structured fields for logs and audit records. Do not rely on ad hoc string cleanup after logging raw data.
- Redact PAN-like values to first-six/last-four only when the feature specifically requires inspection; otherwise use opaque references.
- Never log CVV, passwords, one-time codes, session tokens, API keys, authorization headers, or full customer support notes.
- Error messages should use stable codes and safe summaries instead of echoing invalid sensitive input.

## Naming

- Use domain names consistently across files. Do not switch between synonyms such as case, ticket, dispute, and request unless the distinction is explicitly defined.
- Use active names for commands and state transitions.
- Use nouns for records and views.
- Define acronyms on first use.

# Validation Checklist

Run ID: `20260619-170102-write-spec-python-fresh`

Selected stack: `python`

## Required Output Files

- [x] `agent-1-spec/outputs/specification.md`
- [x] `agent-1-spec/outputs/docs/domain-rules.md`
- [x] `agent-1-spec/outputs/docs/technical-conventions.md`
- [x] `agent-1-spec/outputs/docs/development-process.md`
- [x] `agent-1-spec/research-notes.md`
- [x] `agent-1-spec/handoffs/sub-agent-plan.md`
- [x] `agent-1-spec/handoffs/domain-research-handoff.md`
- [x] `agent-1-spec/handoffs/objectives-handoff.md`
- [x] `agent-1-spec/handoffs/low-level-tasks-handoff.md`

## Task 1 Sections

- [x] High-Level Objective is present.
- [x] Mid-Level Objectives are present with observable success.
- [x] Implementation Notes are present and Python-specific.
- [x] Context includes beginning and ending state.
- [x] Low-Level Tasks are present with prompts, files, functions, details, edge cases, acceptance criteria, and verification.

## Stack And Product Boundary

- [x] Selected stack is `python`.
- [x] Python files, functions, commands, tests, coverage, and MCP-readable result notes are concrete.
- [x] Runtime transaction pipeline agents are Python modules, not assistant skills.
- [x] No product task requires harness planning, Superpowers, canonical-copy workflow, slash-command setup, hook setup, screenshots, or PR packaging.
- [x] Greek automation identities are not used as runtime product component names.

## Privacy, Money, And Audit

- [x] Money uses `decimal.Decimal`; binary floating point is forbidden for amounts.
- [x] Currency validation uses ISO 4217-style shape plus `USD`, `EUR`, `GBP` allowlist.
- [x] Unsupported `XYZ` is rejected.
- [x] Logs, audit examples, final results, and summaries avoid plaintext account IDs and raw descriptions.
- [x] Audit events include timestamp, component name, transaction ID, safe outcome, and reason codes.

## File Protocol, Archival, And Provenance

- [x] JSON file protocol uses `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- [x] Repeated runs archive existing `shared/` to zero-padded `archive/shared-NNN` folders.
- [x] Runtime provenance is required at `shared/run-provenance.json`.
- [x] Runtime provenance excludes raw transactions, account identifiers, descriptions, metadata, prompts, and secrets.

## Result Shapes And Verification

- [x] Final statuses are limited to `settled`, `rejected`, `review_required`, and `error`.
- [x] Per-transaction result and summary shapes are stable enough for future read-only MCP tools.
- [x] Validator dry-run behavior is specified for a future `/validate-transactions` workflow.
- [x] `pytest` with temporary directories is specified.
- [x] Temporary Athena-stage coverage target is 75%; later test-generation work owns the >80% blocking hook.

## Research Provenance

- [x] Context7 Python lookup is recorded.
- [x] ISO 4217 source is recorded.
- [x] SIX ISO 4217 maintenance-agency source is recorded.
- [x] OWASP logging source is recorded.
- [x] Shell-network limitation and missing `specification-TEMPLATE-hint.md` are recorded.

## Canonical Copy Boundary

- [x] Canonical `specification.md` already exists.
- [x] This generate run did not overwrite canonical `specification.md`.
- [x] Supporting docs remain run-local unless explicitly selected later.

## Final Review

- [x] Final review sub-agent reviewed the integrated candidate package.
- [x] Required privacy repair completed: the plaintext sample-style account identifier in the redaction example was replaced with redacted-only wording.
- [x] Non-blocking wording suggestion reviewed; `specification-stage coverage target` wording is already used in the support docs and final review residual risk.

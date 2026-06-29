# Objectives Handoff

## 1. Assigned Scope

Produce the Objectives Architect handoff for Athena (Spec Writer) run `20260618-003908-write-spec-python-replacement`, stack `python`.

This handoff defines the product-only high-level objective, mid-level objectives, sample-data coverage, verification mapping, and decomposition guidance for a Python educational transaction-processing pipeline. It intentionally excludes Homework Automation Layer work such as run preservation mechanics, hook setup, MCP configuration setup, screenshots, PR support, and Greek-named runtime components.

## 2. Files/Context Inspected

- User-provided run context for `20260618-003908-write-spec-python-replacement`.
- Required output shape for `docs/agent-runs/20260618-003908-write-spec-python-replacement/agent-1-spec/handoffs/objectives-handoff.md`.
- Homework 6 `write-spec` skill instructions.
- `agent-control/write-spec/workflow.md`.
- `agent-control/write-spec/stack-profiles.md`.
- `agent-control/write-spec/quality-bar.md`.
- `agent-control/write-spec/run-registry.md`.
- Provided summary of `sample-transactions.json`, including expected outcomes for `TXN002`, `TXN004`, `TXN005`, `TXN006`, and `TXN007`.
- Provided research findings for Decimal money handling, JSON safety, pytest filesystem isolation, ISO-style currency validation, audit-safe logging, and educational-simulation limits.

## 3. Proposed High-Level Objective

Build a deterministic Python transaction-processing pipeline that reads synthetic transactions from `sample-transactions.json`, validates and risk-scores each transaction through stack-native JSON-message agents, settles acceptable transactions, and writes audit-safe results and summaries for educational review.

## 4. Proposed Mid-Level Objectives With Observable Success

### Objective 1: Establish the Python pipeline structure and JSON file protocol

Observable success: the product contains `integrator.py`, `agents/common.py`, `agents/transaction_validator.py`, `agents/fraud_detector.py`, and `agents/settlement_processor.py`; runtime execution prepares `shared/input`, `shared/processing`, `shared/output`, and `shared/results`; each runtime component accepts and returns a JSON-serializable message envelope through `process_message(message: dict) -> dict`.

### Objective 2: Validate transaction data with precise money, currency, and required-field rules

Observable success: validation parses amounts from strings into `decimal.Decimal`, rejects unsupported currencies outside the sample allowlist `USD`, `EUR`, and `GBP`, rejects negative or malformed amounts, avoids binary floating point, serializes money back as strings, and returns structured rejection reasons without exposing plaintext sensitive account data.

### Objective 3: Produce deterministic fraud and review signals without overstating compliance

Observable success: the Fraud Detector assigns transparent educational risk signals for high-value, very-high-value, odd-hour, and other configured sample-rule conditions; `TXN002` and `TXN005` receive high-value or very-high-value review signals; `TXN004` receives an odd-hour signal; all signals are framed as simulation outputs rather than legal, AML, sanctions, payment-network, or banking compliance determinations.

### Objective 4: Orchestrate validation, risk scoring, settlement, and final result writing for every input transaction

Observable success: `main`, `load_transactions`, `prepare_shared_directories`, `build_message_envelope`, `process_transaction`, `write_result`, and `summarize_results` coordinate the runtime flow; every one of the eight sample transactions is represented in `shared/results`; invalid transactions are rejected before settlement; valid transactions receive settlement outcomes plus any review signals.

### Objective 5: Preserve audit-safe observability, testability, and future result-readiness

Observable success: `redact_account_id` and `audit_event` produce structured audit records with timestamp, runtime component name, transaction ID, safe outcome, and reason code while masking account identifiers; `json` output uses safe serialization such as `allow_nan=False`; tests isolate filesystem state with `pytest tmp_path`; generated result and summary shapes are stable enough for later result-reading tools; Athena's ending-context coverage target is 75%.

## 5. Objective-to-Sample-Data Mapping

| Objective | Sample-data mapping |
|---|---|
| Objective 1 | All eight sample transactions should be loadable into the JSON envelope and traceable through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`. |
| Objective 2 | `TXN006` must reject unsupported currency `XYZ`; `TXN007` must reject negative amount `-100.00`; all valid money values must be parsed from strings with `decimal.Decimal` and serialized as strings. |
| Objective 3 | `TXN002` must receive a high-value review signal; `TXN005` must receive a very-high-value review signal; `TXN004` must receive an odd-hour signal. |
| Objective 4 | The final result set must account for all eight sample transactions, including both rejected transactions and valid transactions with settlement or review outcomes. |
| Objective 5 | Any transaction containing account identifiers or payment metadata must appear in logs, audit events, and summaries only in redacted or non-sensitive form such as `ACC-****1001`; no sample description or account identifier should be emitted as plaintext in audit output. |

## 6. Objective-to-Verification Mapping

| Objective | Verification approach |
|---|---|
| Objective 1 | Add unit tests for `prepare_shared_directories` and `build_message_envelope`; run an integration test that verifies expected JSON files appear under the required `shared/*` directories using `pytest tmp_path`. |
| Objective 2 | Add focused tests for `parse_amount`, currency validation, `validate_transaction`, and rejection reason codes; assert `TXN006` is rejected for unsupported currency and `TXN007` is rejected for negative amount. |
| Objective 3 | Add tests for `score_fraud_risk`; assert `TXN002` has a high-value signal, `TXN005` has a very-high-value signal, and `TXN004` has an odd-hour signal. |
| Objective 4 | Add end-to-end tests for `process_transaction`, `settle_transaction`, `write_result`, and `summarize_results`; assert all eight sample transaction IDs are present in final results and invalid transactions are not settled. |
| Objective 5 | Add tests for `redact_account_id`, `audit_event`, JSON serialization with `allow_nan=False`, and summary result schema; run `python -m pytest` and a non-blocking coverage command such as `python -m pytest --cov=.` with the Athena target of at least 75% coverage. |

## 7. Assumptions, Uncertainty, Residual Risks

- Assumption: the selected stack is definitively `python`; no Java or auto-stack variant should appear in the generated product specification.
- Assumption: `sample-transactions.json` contains exactly eight synthetic transactions, and the named transaction IDs and expected behaviors from the prompt are authoritative.
- Assumption: the sample currency allowlist should be `USD`, `EUR`, and `GBP`, while validation should still describe the format as ISO 4217-style three-letter uppercase codes.
- Assumption: high-value, very-high-value, and odd-hour thresholds can be deterministic design decisions in the spec, provided the resulting behavior matches the required sample outcomes.
- Uncertainty: exact transaction field names should be confirmed from `sample-transactions.json` during integration drafting so low-level tasks name precise keys instead of generic placeholders.
- Uncertainty: exact risk threshold amounts should be chosen to classify `TXN002` as high-value and `TXN005` as very-high-value without accidentally misclassifying unrelated sample rows.
- Residual risk: if the specification overemphasizes future MCP or coverage hook setup, it may leak Homework Automation Layer responsibilities into product tasks; keep only product result shapes and the 75% Athena coverage expectation in this spec.
- Residual risk: audit examples can accidentally reveal sensitive fields if task cards ask implementers to log full messages; every logging and audit objective should explicitly require redaction and minimal fields.

## 8. Recommended Next Step for Low-Level Task Decomposition

Create implementation-ready Python task cards that map these objectives into concrete slices for project structure, shared JSON envelope, input loading, directory reset, Decimal parsing and serialization, validation, fraud scoring, settlement, integrator orchestration, audit-safe summaries, result schema stability, pytest coverage, and deterministic error handling, with each card naming exact files, functions, edge cases, acceptance criteria, and verification commands.

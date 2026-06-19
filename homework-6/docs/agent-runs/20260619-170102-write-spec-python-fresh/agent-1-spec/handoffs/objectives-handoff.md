## Assigned Scope

Shape the high-level objective and 4-5 mid-level objectives for Athena (Spec Writer) run `20260619-170102-write-spec-python-fresh`.

The scope is limited to the Generated Transaction System Layer: a Python educational transaction-processing pipeline specification. It excludes operator-layer mechanics, harness planning, run selection, canonical-copy workflow, slash-command setup, hook setup, PR packaging, screenshots, and Greek automation-agent mechanics as product requirements.

## Context Inspected

- `agent-control/write-spec/transaction-system-brief.md`
- `sample-transactions.json`
- `agents.md`
- `agent-control/write-spec/workflow.md`
- `agent-control/write-spec/stack-profiles.md`
- `agent-control/write-spec/quality-bar.md`
- `agent-control/write-spec/run-registry.md`
- `docs/agent-runs/20260619-170102-write-spec-python-fresh/agent-1-spec/handoffs/sub-agent-plan.md`
- `docs/agent-runs/20260619-170102-write-spec-python-fresh/agent-1-spec/handoffs/domain-research-handoff.md`
- Repository-level `AGENTS.md`
- Repository-level `HOMEWORK_STANDARDS.md`
- Repository-level `README.md`

Confirmed sample fixture shape: eight synthetic transactions, including ordinary USD transfers, high-value USD wire transfers, a near-threshold USD transfer, an early-hours EUR API transfer from DE, unsupported currency `XYZ`, negative GBP amount, and a USD mobile transfer.

## Sources Or Commands Used

Commands used:

- `Get-Content -Raw .agents\skills\write-spec\SKILL.md`
- `Get-Content -Raw agent-control\write-spec\workflow.md`
- `Get-Content -Raw agent-control\write-spec\stack-profiles.md`
- `Get-Content -Raw agent-control\write-spec\quality-bar.md`
- `Get-Content -Raw agent-control\write-spec\run-registry.md`
- `Get-Content -Raw agent-control\write-spec\transaction-system-brief.md`
- `Get-Content -Raw sample-transactions.json`
- `Get-Content -Raw agents.md`
- `Get-ChildItem -Force docs\agent-runs\20260619-170102-write-spec-python-fresh\agent-1-spec\handoffs | Select-Object -ExpandProperty Name`
- `Get-Content -Raw docs\agent-runs\20260619-170102-write-spec-python-fresh\agent-1-spec\handoffs\domain-research-handoff.md`
- `Get-Content -Raw docs\agent-runs\20260619-170102-write-spec-python-fresh\agent-1-spec\handoffs\sub-agent-plan.md`
- `Get-Content -Raw ..\AGENTS.md`
- `Get-Content -Raw ..\HOMEWORK_STANDARDS.md`
- `Get-Content -Raw ..\README.md`

No new external research was performed for this objectives handoff. Domain-specific source claims were inherited from `domain-research-handoff.md`, which already recorded Python `decimal`, ISO 4217, Context7, and OWASP logging research.

## Proposed High-Level Objective

Build a Python educational transaction-processing pipeline that deterministically processes the eight synthetic sample transactions through stack-native runtime components, preserves audit-safe JSON evidence in the required `shared/` protocol folders, and produces final results with statuses limited to `settled`, `rejected`, `review_required`, or `error`.

## Proposed Mid-Level Objectives

1. Implement deterministic Python orchestration and file protocol setup.

   Observable success: `integrator.py` exposes a `main()` flow that loads `sample-transactions.json`, archives any existing `shared/` tree to the next zero-padded `archive/shared-001` style folder, creates fresh `shared/input`, `shared/processing`, `shared/output`, and `shared/results` directories, writes non-sensitive `shared/run-provenance.json`, and accounts for all eight input transaction IDs in final outputs.

2. Validate transaction structure, money, currency, and safe rejection behavior.

   Observable success: `agents/transaction_validator.py` provides a common callable interface such as `process_message(message: dict) -> dict`, parses amount strings with `decimal.Decimal` only, never uses binary floating point for money, accepts only local supported currencies `USD`, `EUR`, and `GBP`, rejects `TXN006` with an unsupported-currency reason, rejects `TXN007` with a non-positive-amount reason, and emits audit events containing timestamp, component name, transaction ID, safe outcome, and reason code without plaintext account IDs, descriptions, or raw metadata.

3. Apply deterministic educational risk scoring without real compliance claims.

   Observable success: `agents/fraud_detector.py` assigns repeatable `risk_score`, `risk_level`, and reason codes using documented educational heuristics such as high amount, very high amount, wire transfer type, early-hours timestamp, API or mobile channel, country, and near-threshold amount. Valid low-risk examples can proceed toward `settled`, while elevated-risk examples such as high-value wires or unusual timing are routed toward `review_required`; the component must not assert real fraud, AML, sanctions, legal, or payment-network compliance.

4. Produce safe final settlement simulation results and summary files.

   Observable success: `agents/settlement_processor.py` or an equivalent final-stage Python component converts validated low-risk transactions to `settled`, preserves validation failures as `rejected`, routes elevated-risk transactions to `review_required`, uses `error` only for per-transaction processing failures, and writes safe per-transaction JSON files plus `shared/results/summary.json`. Result shapes include MCP-readable fields such as `transaction_id`, `status`, `reason_codes`, `risk_score`, `risk_level`, `amount`, `currency`, `component_history`, `processed_at`, and `safe_summary`, while omitting plaintext account IDs, descriptions, and raw metadata.

5. Specify Python verification seams for repeatable tests and future read-only inspection.

   Observable success: the specification drives `pytest` tests with isolated temporary directories, covers validator dry-run behavior for a future `/validate-transactions` workflow without requiring full pipeline execution, verifies repeated-run archival and provenance creation, checks that final statuses are exactly one of `settled`, `rejected`, `review_required`, or `error`, and sets the Athena-stage coverage target at 75%. Future read-only MCP tools can consume the result files, but the objectives do not require MCP configuration setup mechanics.

## Objective-To-Evidence Notes

- Objective 1 maps to evidence in `shared/run-provenance.json`, the fresh shared directory tree, archived prior `shared/` folders, and final accounting of all eight transaction IDs.
- Objective 2 maps to validation unit tests for required fields, `Decimal` parsing, supported currency allowlist, `XYZ` rejection, negative amount rejection, and audit/log redaction checks.
- Objective 3 maps to deterministic fraud/risk unit tests that assert stable scores, reason codes, and review routing for high-value, very-high-value, near-threshold, early-hours, API, mobile, and country-based sample signals.
- Objective 4 maps to per-transaction result files and `summary.json`, with tests proving status vocabulary, safe output shape, count totals, and absence of plaintext sensitive fields.
- Objective 5 maps to `python -m pytest`, a non-blocking coverage report such as `python -m pytest --cov=.`, temporary filesystem fixtures, and dry-run validator tests.

## Assumptions

- Python is the selected stack for this run.
- The eight records in `sample-transactions.json` are synthetic but still treated as sensitive for logging, audit, result summaries, and documentation examples.
- `transaction_id` is safe as the stable correlation identifier.
- The local currency allowlist is fixed to `USD`, `EUR`, and `GBP` for deterministic homework behavior.
- The pipeline performs no foreign exchange conversion, balance mutation, external payment call, or production fraud/compliance decision.
- Amounts are serialized back to JSON as strings.
- Future MCP tooling is read-only over product result shapes and is not part of this objective set.

## Uncertainty And Residual Risks

- Exact risk thresholds remain a design decision for the integrated specification; they should be simple, deterministic, and documented as educational heuristics.
- If the final spec assigns exact expected statuses per sample transaction, it should ensure those expectations follow the chosen thresholds consistently.
- Privacy leakage remains the highest practical risk because raw input records include account IDs, descriptions, and metadata that must not be copied into logs, audit events, summaries, docs, or examples.
- Decimal safety can be weakened if any later task converts amounts through `float`; low-level tasks should make this a specific rejection condition.
- Result shapes must be stable enough for future MCP reads, but the spec should continue to avoid MCP configuration mechanics.

## Recommended Next Step

Use these objectives as the objective section source for `agent-1-spec/outputs/specification.md`, then pass them to the low-level task decomposition phase so it can produce Python task cards with exact files, functions, edge cases, acceptance criteria, and verification commands.


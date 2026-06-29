# Objectives Handoff

## Assigned Scope

Objectives Architect handoff for run `20260621-220037-write-spec-python-hera-python-full-set`. Scope is limited to shaping one high-level objective and 4-5 concrete, testable mid-level objectives for a Python Generated Transaction System Layer specification. No files were edited by the sub-agent.

## Files/Context Inspected

- Operator prompt and supplied safe sample summary.
- `.agents/skills/write-spec/SKILL.md`
- `agent-control/write-spec/workflow.md`
- `agent-control/write-spec/stack-profiles.md`
- `agent-control/write-spec/quality-bar.md`
- `agent-control/write-spec/run-registry.md`

## Sources/Commands Used

- `Get-Content` on the local `write-spec` skill and required `agent-control/write-spec/` references.
- No web or Context7 research performed by this sub-agent.
- Did not inspect `sample-transactions.json` directly; used the provided safe summary to avoid copying sensitive sample fields.

## Proposed High-Level Objective

Build a deterministic Python educational transaction-processing pipeline that reads `sample-transactions.json`, processes every transaction through audit-safe JSON file protocol stages, and writes complete sanitized results and summaries under `shared/results/`.

## Proposed Mid-Level Objectives

1. **Validate transaction structure, money, and currency before downstream processing.**
   Observable success: every input record is either accepted with `Decimal`-serialized amount and allowed currency, or rejected with a safe reason code for missing fields, invalid currency such as `XYZ`, or non-positive amount.

2. **Score fraud risk using deterministic educational rules without compliance claims.**
   Observable success: high-value records, early-hour records, and relevant channel/country patterns receive reproducible risk flags or reason codes, while the output avoids AML, sanctions, KYC, legal, regulatory, or real settlement assertions.

3. **Settle only transactions that pass validation and risk checks.**
   Observable success: each transaction receives exactly one final safe outcome, rejected or flagged transactions are not marked settled, and result files contain transaction ID, status, reason codes, amount string, currency, component history count, and audit event count.

4. **Produce audit-safe reporting artifacts for all processed records.**
   Observable success: `summary.json` and `pipeline-status.json` account for all 8 sample records, include status and reason-code counts, identify no plaintext account IDs/descriptions/full metadata, and confirm result completeness.

5. **Support repeatable Python runs with isolated tests and traceable runtime state.**
   Observable success: `python integrator.py` archives any prior `shared/` tree to `archive/shared-001` style folders, recreates `shared/input`, `shared/processing`, `shared/output`, and `shared/results`, writes non-sensitive `shared/run-provenance.json`, and `python -m pytest` passes with `tmp_path` isolation plus a non-blocking 75% coverage report target.

## Objective-To-Component Mapping

| Objective | Primary Component | Supporting Component |
|---|---|---|
| Validate structure, money, and currency | Transaction Validator | Integrator |
| Score deterministic educational fraud risk | Fraud Detector | Transaction Validator |
| Settle eligible transactions | Settlement Processor | Fraud Detector |
| Produce audit-safe reports | Reporting Agent | Settlement Processor |
| Support repeatable runs and traceability | Integrator | Reporting Agent |

## Assumptions And Uncertainty

- Currency validation should use a bounded ISO 4217-style allow-list suitable for the sample, including `USD`, `EUR`, and `GBP`, while rejecting unsupported values such as `XYZ`.
- Fraud rules should be deterministic and educational, likely including high-value and early-hour indicators, but exact thresholds should be finalized in low-level task decomposition.
- Transaction IDs such as `TXN002`, `TXN004`, `TXN005`, `TXN006`, and `TXN007` are safe to reference because they are not account identifiers.
- Runtime provenance should record only non-sensitive traceability such as source spec/run identifiers, fingerprints, timestamp, stack, and pipeline version references supplied at runtime.

## Residual Risks

- If the final specification over-specifies fraud thresholds, it may look like real banking/compliance logic rather than an educational simulation.
- Reporting requirements need strong privacy wording so generated code does not accidentally echo descriptions, account IDs, or full metadata into logs/results.
- The 75% coverage target must remain non-blocking at this Athena stage so it does not conflict with later Themis ownership of stronger coverage gates.

## Recommended Next Step

Use these objectives as the stable top-level structure for the Python `specification.md`, then hand them to the Low-Level Task Decomposition sub-agent to create implementation-ready task cards for `integrator.py`, the four runtime components, JSON envelope/file movement, result shapes, audit redaction, rerun archival, provenance, pytest coverage, and MCP-readable summaries.


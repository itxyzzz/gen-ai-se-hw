# Technical Conventions

## Purpose

These conventions support the Python transaction-processing system specified in this preserved Athena (Spec Writer) run.

## File Layout

```text
integrator.py
agents/
  __init__.py
  common.py
  transaction_validator.py
  fraud_detector.py
  settlement_processor.py
  reporting_agent.py
tests/
  conftest.py
  test_common.py
  test_transaction_validator.py
  test_fraud_detector.py
  test_settlement_processor.py
  test_reporting_agent.py
  test_pipeline_integration.py
  test_rerun_archival.py
  test_privacy_and_result_shapes.py
shared/
  input/
  processing/
  output/
  results/
archive/
```

## Naming

- Runtime component names: Integrator, Transaction Validator, Fraud Detector, Settlement Processor, Reporting Agent.
- Python module names use lowercase snake case.
- Common runtime callable for components: `process_message(message: dict) -> dict`.
- Reason codes use uppercase snake case.
- Status values use lowercase snake case.

## Money And JSON

- Parse amount strings with `decimal.Decimal`.
- Reject binary floats, unparseable strings, non-finite values, zero, and negative values.
- Serialize `Decimal` values as strings before writing JSON.
- Use standard `json` with explicit conversion helpers.
- Treat transaction IDs as strings.

## Shared Protocol

- Integrator writes initial envelopes to `shared/input`.
- Runtime components process through `shared/processing` and `shared/output`.
- Final results land in `shared/results`.
- Every envelope includes `message_id`, `timestamp`, `source_agent`, `target_agent`, `message_type`, `data`, `component_history`, and `audit_events`.
- Component history should record only component names, statuses, timestamps, and reason codes.

## Rerun And Provenance

- Before creating a fresh `shared/`, move any existing `shared/` to the next zero-padded archive folder.
- Write `shared/run-provenance.json` after fresh directories are prepared.
- Provenance records schema version, runtime run ID, generated timestamp, stack, source spec reference, pipeline version reference, and fingerprints only.

## Error Semantics

- Use stable reason codes.
- File-level unrecoverable failures may return nonzero from `main()`.
- Per-transaction failures should produce safe `error` result files and allow remaining records to continue.
- Do not write stack traces, raw input payloads, account IDs, descriptions, or full metadata to result JSON.

## Test Conventions

- Use `pytest`.
- Use `tmp_path` for all filesystem state.
- Use `monkeypatch` when tests need to redirect cwd or environment.
- Unit tests cover helpers and each runtime component.
- Integration tests cover full pipeline, rerun archival, privacy checks, and result shapes.
- Coverage reporting target at this stage is 75% and non-blocking.


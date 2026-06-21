# Testing Guide

This guide documents the Themis candidate suite and fresh Clio validation evidence for the Hera-dispatched Python package set.

## Test Strategy

```mermaid
flowchart TB
    Unit["Unit tests"] --> Validator["Validator, fraud, settlement, reporting"]
    Integration["Integration tests"] --> Pipeline["Full pipeline and rerun archival"]
    Privacy["Privacy checks"] --> Results["Result JSON and command evidence"]
    Support["Support checks"] --> Coverage["Coverage helper and command behavior"]
```

The Themis candidate extends Hephaestus baseline tests with quality checks across:

- Validator field, currency, amount, audit, and validation-only behavior.
- Fraud scoring for high-value, unusual-time, channel, type, and destination-pattern cases.
- Settlement status preservation and unknown-status handling.
- Reporting result shapes, summary consistency, privacy checks, and error result behavior.
- Integrator setup, provenance, bad-message recovery, full-pipeline results, and archival.
- Operator support behavior for `/run-pipeline`, `/validate-transactions`, and coverage gate pass/fail paths.

## Commands

| Purpose | Command | Fresh Clio result |
|---|---|---|
| Run tests | `python -m pytest -p no:cacheprovider` | 36 passed |
| Coverage gate | `python scripts\check_coverage_gate.py --stack python --fail-under 80` | Passed, 97.44% total coverage |
| Blocking demonstration | `python scripts\check_coverage_gate.py --stack python --fail-under 99` | Expected failure, 97.44% below 99% |
| Pipeline support | documented `/run-pipeline` fast path | 8 results, all present |
| Validation-only support | validator dry-run helper | total 8, valid 6, invalid 2 |

## Coverage Summary

| Area | Coverage |
|---|---:|
| `agents/__init__.py` | 100% |
| `agents/common.py` | 98% |
| `agents/fraud_detector.py` | 96% |
| `agents/reporting_agent.py` | 100% |
| `agents/settlement_processor.py` | 100% |
| `agents/transaction_validator.py` | 98% |
| `integrator.py` | 88% |
| Total | 97.44% |

## Expected Sample Outcomes

| Transaction | Expected status | Safe reason signal |
|---|---|---|
| `TXN001` | `settled` | `SETTLED` |
| `TXN002` | `review_required` | `REVIEW_HIGH_VALUE` |
| `TXN003` | `review_required` | `REVIEW_DESTINATION_PATTERN` |
| `TXN004` | `review_required` | `REVIEW_UNUSUAL_TIME`, `REVIEW_CHANNEL_PATTERN` |
| `TXN005` | `review_required` | `REVIEW_HIGH_VALUE` |
| `TXN006` | `rejected` | `UNSUPPORTED_CURRENCY` |
| `TXN007` | `rejected` | `NON_POSITIVE_AMOUNT` |
| `TXN008` | `settled` | `SETTLED` |

## Fixture Isolation

Tests run from a candidate workspace and use temporary filesystem locations for runtime behavior. Clio did not run tests against root runtime code or root tests, and did not create root `.coverage` or root screenshot output.

## Manual Review Checklist

- Confirm the candidate inventory names the intended Athena, Hephaestus, and Themis runs.
- Run `python integrator.py` and confirm eight results.
- Run pytest and confirm 36 passing tests.
- Run the 80% coverage gate and confirm 97.44% or equivalent above-threshold coverage.
- Run the 99% demonstration threshold and confirm a failing coverage gate signal.
- Open `shared/results/summary.json` and verify counts without exposing raw transaction payloads.
- Use the MCP helper or server after a pipeline run and verify safe status responses.
- Confirm screenshots or capture notes cover pipeline, tests/coverage, command skill, hook, and MCP evidence.

## Limitations

Direct shell execution of `.githooks/pre-push` was blocked in the Windows sandbox. The same delegated coverage helper passed at the real 80% threshold and failed at the demonstration 99% threshold, which validates the behavior the hook relies on.

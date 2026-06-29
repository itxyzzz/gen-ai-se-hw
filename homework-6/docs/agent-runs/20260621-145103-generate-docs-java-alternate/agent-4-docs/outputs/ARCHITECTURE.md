# Java Alternate Architecture

## Layer Status

- Operator Layer: Hera (Orchestrator) created this preserved Java alternate and stopped before comparison.
- Homework Automation Layer: Athena generated the Java spec, Hephaestus generated Java code, Themis generated Java tests, and Clio generated this documentation package.
- Generated Transaction System Layer: the Java Maven project under the Hephaestus run-local `outputs/` folder.

## Data Flow

```text
input fixture
  -> shared/input stage files
  -> TransactionValidator
  -> shared/processing stage files
  -> FraudDetector
  -> shared/output stage files
  -> SettlementProcessor
  -> shared/results/TXN*.json
  -> ReportingAgent
  -> shared/results/summary.json
```

## Result Contract

The Java alternate writes stack-neutral result fields:

- `transaction_id`
- `amount`
- `currency`
- `status`
- `reason_codes`
- `settlement_reference`
- `component_history_count`
- `audit_event_count`

`summary.json` stores aggregate counts for total, settled, rejected, review-required, error, and reason-code groups.

## Privacy Design

The Java result writer omits raw account identifiers, raw descriptions, and full metadata. Validation evidence reports transaction IDs, statuses, and reason codes only.

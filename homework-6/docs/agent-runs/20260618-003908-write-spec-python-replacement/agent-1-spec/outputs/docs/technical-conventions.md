# Technical Conventions

## Python Stack

The selected implementation stack is Python.

Required paths:

```text
integrator.py
agents/
  __init__.py
  common.py
  transaction_validator.py
  fraud_detector.py
  settlement_processor.py
shared/
  input/
  processing/
  output/
  results/
tests/
mcp/server.py
```

Each runtime component exposes `process_message(message: dict) -> dict`. The integrator owns file-system orchestration and deterministic rerun reset.

## JSON Message Envelope

Every stage message should use a stable envelope:

```json
{
  "message_id": "uuid4-string",
  "schema_version": "1.0",
  "created_at": "2026-03-16T10:00:00Z",
  "source_agent": "integrator",
  "target_agent": "transaction_validator",
  "message_type": "transaction",
  "transaction_id": "TXN001",
  "data": {}
}
```

Components may add `validation`, `risk`, `settlement`, `audit_events`, `errors`, and `component_history`. Message files must avoid raw account IDs and descriptions after the validation boundary unless a component needs them internally and writes only redacted output.

## File Protocol

Use the required directories:

```text
shared/input
shared/processing
shared/output
shared/results
```

Recommended deterministic file names:

- `shared/input/<transaction_id>.json`
- `shared/processing/<transaction_id>-<component>.json`
- `shared/output/<transaction_id>-<component>.json`
- `shared/results/<transaction_id>.json`
- `shared/results/summary.json`
- `shared/results/audit.json`
- `shared/results/pipeline-status.json`

The integrator should clear or archive prior generated files at the start of a normal run so reruns do not create stale duplicates. Tests must use temporary directories rather than the real `shared/` directory.

## Money

- Parse amounts from strings with `decimal.Decimal`.
- Reject missing, malformed, `NaN`, infinite, zero, and negative values.
- Never use `float` for money or thresholds.
- Serialize validated amounts as canonical strings.
- Use explicit `Decimal("10000.00")` and `Decimal("50000.00")` thresholds for risk signals.

## JSON Handling

- Use the standard `json` module.
- Catch `json.JSONDecodeError` for malformed message or input files.
- Write result files with deterministic indentation and `allow_nan=False`.
- Convert `Decimal` values to strings before encoding.
- Do not write multiple JSON documents into one file with repeated `json.dump` calls.

## Currency

Use a small explicit allowlist for the homework sample:

```python
SUPPORTED_CURRENCIES = {"USD", "EUR", "GBP"}
```

`JPY` may be included if the implementation wants to demonstrate a broader ISO-style allowlist, but `XYZ` must reject with `UNSUPPORTED_CURRENCY`.

## Reason Codes And Status

Use stable uppercase reason codes:

- `MISSING_FIELD`
- `INVALID_AMOUNT`
- `NON_POSITIVE_AMOUNT`
- `UNSUPPORTED_CURRENCY`
- `HIGH_VALUE`
- `VERY_HIGH_VALUE`
- `WIRE_TRANSFER`
- `ODD_HOUR_ACTIVITY`
- `MALFORMED_JSON`
- `PIPELINE_ERROR`

Use these final statuses:

- `settled`
- `rejected`
- `review_required`
- `error`

## Audit And Redaction

Centralize redaction in `agents.common.redact_account_id(account_id: str | None) -> str`. Audit event creation should also be centralized in `agents.common.audit_event(...)`.

Audit events should include:

- `timestamp`
- `component`
- `transaction_id`
- `outcome`
- `reason_code`
- optional `risk_level`
- optional redacted account references

Audit events must not include raw account IDs, raw descriptions, secrets, tokens, authorization headers, or unfiltered metadata dumps.

## MCP-Readable Result Shapes

Future MCP tools should be able to read existing JSON output without rerunning the pipeline.

`shared/results/<transaction_id>.json` should include:

- `schema_version`
- `transaction_id`
- `status`
- `reason_codes`
- `risk_score`
- `risk_level`
- `amount`
- `currency`
- `component_history`
- `audit_event_ids` or inline safe audit events

`shared/results/pipeline-status.json` should include:

- `schema_version`
- `total_transactions`
- `settled`
- `rejected`
- `review_required`
- `error`
- `generated_at`

`shared/results/summary.json` should include the same count fields plus safe per-transaction summaries.

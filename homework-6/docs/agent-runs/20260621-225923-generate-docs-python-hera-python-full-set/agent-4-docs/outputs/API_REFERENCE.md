# API Reference

This candidate is a local Python file-processing pipeline. Its public interfaces are command-line commands, JSON protocol files, validation helper behavior, and MCP status tools.

## Commands

### Run Pipeline

```powershell
python integrator.py
```

Expected successful terminal signal:

```text
Pipeline complete: total=8 settled=2 rejected=2 review_required=4 error=0
```

Outputs:

- `shared/results/TXN*.json`
- `shared/results/summary.json`
- `shared/results/pipeline-status.json`

### Run Tests

```powershell
python -m pytest -p no:cacheprovider
```

Fresh Clio evidence: 36 tests passed.

### Coverage Gate

```powershell
python scripts\check_coverage_gate.py --stack python --fail-under 80
```

Fresh Clio evidence: 97.44% total coverage.

### Validation-Only Behavior

```powershell
python -c "import json; from collections import Counter; from agents.transaction_validator import validate_transactions_file; r=validate_transactions_file('sample-transactions.json'); safe={'total': r['total'], 'valid': r['valid'], 'invalid': r['rejected'], 'reason_code_groups': dict(Counter(code for item in r['results'] for code in item['reason_codes'])), 'results': [{'transaction_id': item['transaction_id'], 'status': item['status'], 'reason_codes': item['reason_codes']} for item in r['results']]}; print(json.dumps(safe, indent=2))"
```

Safe response fields:

```json
{
  "total": 8,
  "valid": 6,
  "invalid": 2,
  "reason_code_groups": {
    "UNSUPPORTED_CURRENCY": 1,
    "NON_POSITIVE_AMOUNT": 1
  },
  "results": [
    {
      "transaction_id": "TXN006",
      "status": "rejected",
      "reason_codes": ["UNSUPPORTED_CURRENCY"]
    }
  ]
}
```

The `results` array contains one safe row per transaction.

## JSON Message Envelope

Runtime components pass JSON envelopes through `shared/input`, `shared/processing`, and `shared/output`.

```json
{
  "message_id": "uuid4-string",
  "timestamp": "2026-03-16T10:00:00Z",
  "source_agent": "integrator",
  "target_agent": "transaction_validator",
  "message_type": "transaction",
  "data": {
    "transaction_id": "TXN001",
    "amount": "1500.00",
    "currency": "USD",
    "status": "received"
  },
  "component_history": [],
  "audit_events": []
}
```

The `source_agent` and `target_agent` fields refer to runtime component identities, not Homework Automation Layer agents.

## Transaction Result Shape

Each `shared/results/TXN*.json` file contains a safe result payload.

```json
{
  "schema_version": 1,
  "transaction_id": "TXN001",
  "status": "settled",
  "reason_codes": ["SETTLED"],
  "amount": "1500.00",
  "currency": "USD",
  "risk_score": 0,
  "risk_level": "low",
  "component_history": [],
  "audit_events": [],
  "processed_at": "2026-06-21T20:59:52Z",
  "privacy_check": "passed"
}
```

Allowed final statuses:

- `settled`
- `rejected`
- `review_required`
- `error`

## Summary Shape

`shared/results/summary.json` includes:

```json
{
  "schema_version": 1,
  "runtime_run_id": "uuid4-string",
  "generated_at": "2026-06-21T20:59:52Z",
  "total_records": 8,
  "settled": 2,
  "rejected": 2,
  "review_required": 4,
  "error": 0,
  "status_counts": {
    "settled": 2,
    "rejected": 2,
    "review_required": 4
  },
  "reason_code_counts": {
    "SETTLED": 2,
    "UNSUPPORTED_CURRENCY": 1
  },
  "privacy_check": "passed",
  "completeness_check": "passed"
}
```

## Pipeline Status Shape

`shared/results/pipeline-status.json` includes:

```json
{
  "schema_version": 1,
  "pipeline_version": "python-candidate",
  "run_status": "completed",
  "summary_path": "shared/results/summary.json",
  "total_records": 8,
  "status_counts": {
    "settled": 2,
    "rejected": 2,
    "review_required": 4
  },
  "reason_code_counts": {
    "SETTLED": 2,
    "UNSUPPORTED_CURRENCY": 1
  },
  "result_files": ["TXN001.json"],
  "generated_at": "2026-06-21T20:59:52Z"
}
```

## MCP Tools And Resource

The root `mcp.json` configures:

- `context7`: used by Hephaestus during code generation.
- `pipeline-status`: custom FastMCP server at `mcp/server.py`.

### `get_transaction_status(transaction_id: str)`

Returns a safe status view for one transaction result file.

Example safe response:

```json
{
  "found": true,
  "transaction_id": "TXN006",
  "status": "rejected",
  "reason_codes": ["UNSUPPORTED_CURRENCY"],
  "risk_score": 0,
  "risk_level": "unknown",
  "amount": "200.00",
  "currency": "XYZ",
  "component_count": 0,
  "audit_event_count": 0
}
```

### `list_pipeline_results()`

Returns safe aggregate counts and one safe row per transaction result.

### `pipeline://summary`

Returns a plain text run summary:

```text
Pipeline run summary
Total transactions: 8
Settled: 2
Rejected: 2
Review required: 4
Error: 0
```

## Privacy Notes

The command and MCP surfaces are intended for local educational review. They report transaction IDs, statuses, reason codes, amounts, currencies, and counts. They avoid raw account IDs, raw descriptions, credentials, tokens, and full metadata.

# API Reference

Homework 6 exposes local command interfaces, JSON file contracts, validation-only helpers, and a custom read-only MCP server.

## Pipeline Command

```powershell
python integrator.py
```

Optional arguments:

| Argument | Meaning |
|---|---|
| `--input <path>` | Input transaction JSON file. Defaults to `sample-transactions.json`. |
| `--shared-dir <path>` | Protocol output directory. Defaults to `shared`. |
| `--spec-path <path>` | Selected canonical specification path. Defaults to `specification.md`. |
| `--inventory-path <path>` | Selected pipeline inventory path for provenance. |
| `--validate-only` | Write validation-only style outputs without risk scoring or settlement. |

Success output:

```text
Pipeline complete: total=8 settled=2 rejected=2 review_required=4 error=0
```

Setup failures print a safe reason code and exit with status `2`.

## JSON File Protocol

```text
shared/
  input/
  processing/
  output/
  results/
```

Stage files use deterministic names:

- `shared/input/001-TXN001.json`
- `shared/processing/001-TXN001-transaction-validator.json`
- `shared/output/001-TXN001-fraud-detector.json`
- `shared/output/001-TXN001-settlement-processor.json`
- `shared/results/TXN001.json`
- `shared/results/summary.json`
- `shared/results/pipeline-status.json`

## Message Envelope Shape

```json
{
  "message_id": "uuid4-string",
  "timestamp": "2026-03-16T10:00:00Z",
  "schema_version": "1.0",
  "source_agent": "integrator",
  "target_agent": "transaction_validator",
  "message_type": "transaction",
  "transaction_id": "TXN001",
  "data": {
    "transaction_id": "TXN001",
    "amount": "1500.00",
    "currency": "USD",
    "transaction_type": "transfer",
    "channel": "online",
    "country": "US",
    "source_account_redacted": "ACC-****1001",
    "destination_account_redacted": "ACC-****2001",
    "status": "received"
  },
  "component_history": [],
  "audit_events": []
}
```

Review-facing examples use redacted account references only.

## Final Result Shape

`shared/results/<transaction_id>.json` contains:

| Field | Meaning |
|---|---|
| `schema_version` | Result schema version. |
| `transaction_id` | Synthetic transaction identifier. |
| `status` | One of `settled`, `rejected`, `review_required`, `error`. |
| `reason_codes` | Stable reason-code list. |
| `risk_score` | Deterministic educational risk score. |
| `risk_level` | `low`, `medium`, `high`, or `not_scored`. |
| `amount` | Money string. |
| `currency` | Currency code. |
| `component_history` | Runtime component history. |
| `processed_at` | Processing timestamp. |
| `safe_summary` | Safe transaction summary without raw account IDs or descriptions. |
| `audit_events` | Sanitized audit events. |

## Summary Shape

`shared/results/summary.json` contains:

| Field | Meaning |
|---|---|
| `schema_version` | Summary schema version. |
| `runtime_run_id` | UUID for the runtime run. |
| `total_transactions` | Total final result count. |
| `settled` | Settled count. |
| `rejected` | Rejected count. |
| `review_required` | Review-required count. |
| `error` | Error count. |
| `result_files` | Result file names. |
| `generated_at` | Summary timestamp. |
| `simulation_notice` | Educational simulation disclaimer. |

## Validation-Only Helper

Python helper:

```python
from agents.transaction_validator import validate_transactions_file

result = validate_transactions_file("sample-transactions.json")
```

Safe result fields:

```json
{
  "schema_version": "1.0",
  "total": 8,
  "valid": 6,
  "rejected": 2,
  "results": [
    {
      "transaction_id": "TXN006",
      "status": "rejected",
      "reason_codes": ["UNSUPPORTED_CURRENCY"],
      "amount": "200.00",
      "currency": "XYZ"
    }
  ]
}
```

The validation-only command should report counts, transaction IDs, statuses, and reason codes only.

## Coverage Gate Helper

```powershell
python scripts/check_coverage_gate.py --fail-under 80
```

The helper runs pytest with coverage in an ignored `tmp/coverage-gate-*` workspace and fails when total coverage is below the threshold.

## MCP Server

`mcp.json`:

```json
{
  "mcpServers": {
    "context7": {
      "command": "npx",
      "args": ["-y", "@upstash/context7-mcp@latest"]
    },
    "pipeline-status": {
      "command": "python",
      "args": ["mcp/server.py"]
    }
  }
}
```

Custom server file:

```text
mcp/server.py
```

### Tool: `get_transaction_status`

Input:

```json
{
  "transaction_id": "TXN006"
}
```

Safe output summary:

```json
{
  "found": true,
  "transaction_id": "TXN006",
  "status": "rejected",
  "reason_codes": ["UNSUPPORTED_CURRENCY"],
  "risk_score": 0,
  "risk_level": "not_scored",
  "simulation_notice": "Educational simulation only; no real payment, banking, legal, AML, sanctions, KYC, PCI, or payment-network compliance determination is performed."
}
```

### Tool: `list_pipeline_results`

Returns:

- `found`
- `summary`
- `result_count`
- `transactions`
- `simulation_notice`

The `transactions` array contains safe status views, not raw transaction records.

### Resource: `pipeline://summary`

Returns a plain-text summary:

```text
Pipeline run summary
Total transactions: 8
Settled: 2
Rejected: 2
Review required: 4
Error: 0
```

## Import Note

In one-off Python commands, `from mcp.server import ...` can resolve to an installed third-party package. Load the local server by file path when testing helper functions directly:

```powershell
python -c "import importlib.util; from pathlib import Path; p=Path('mcp/server.py').resolve(); spec=importlib.util.spec_from_file_location('pipeline_status_server', p); mod=importlib.util.module_from_spec(spec); spec.loader.exec_module(mod); print(mod.build_summary_text())"
```

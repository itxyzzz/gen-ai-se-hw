# Pipeline Status MCP Test Cases Snapshot

Work ID: `2026-06-20-pipeline-status-mcp`
Short ID: `pipeline-status-mcp`
Status: Approved
Schema: `schema:test-cases.snapshot`

## Purpose

Capture the expected behavior of the custom Homework 6 Task 4 FastMCP server before implementation begins. These cases describe the observable behavior that `tests/test_mcp_server.py` should cover with temporary result directories and safe sample payloads.

## Test Data Shape

Use temporary `shared/results/` fixtures with this minimum summary:

```json
{
  "schema_version": "1.0",
  "runtime_run_id": "run-123",
  "total_transactions": 2,
  "settled": 1,
  "rejected": 1,
  "review_required": 0,
  "error": 0,
  "generated_at": "2026-06-20T14:37:33Z",
  "simulation_notice": "Educational simulation only; no real payment, banking, legal, AML, sanctions, KYC, PCI, or payment-network compliance determination is performed."
}
```

Use one settled result:

```json
{
  "schema_version": "1.0",
  "transaction_id": "TXN001",
  "status": "settled",
  "reason_codes": ["SETTLEMENT_SIMULATED"],
  "risk_score": 0,
  "risk_level": "low",
  "amount": "1500.00",
  "currency": "USD",
  "processed_at": "2026-06-20T14:37:33Z",
  "safe_summary": {
    "channel": "online",
    "country": "US",
    "settlement_reference": "SIM-TXN001"
  },
  "component_history": [
    {"component": "integrator", "timestamp": "2026-06-20T14:37:33Z"},
    {"component": "transaction_validator", "timestamp": "2026-06-20T14:37:33Z"}
  ],
  "audit_events": [
    {"component": "integrator", "transaction_id": "TXN001", "outcome": "received"}
  ]
}
```

Use one rejected result:

```json
{
  "schema_version": "1.0",
  "transaction_id": "TXN006",
  "status": "rejected",
  "reason_codes": ["UNSUPPORTED_CURRENCY"],
  "risk_score": 0,
  "risk_level": "not_scored",
  "amount": "200.00",
  "currency": "XYZ",
  "processed_at": "2026-06-20T14:37:33Z",
  "safe_summary": {
    "validation_only": false
  },
  "component_history": [],
  "audit_events": []
}
```

## Cases

| ID | Behavior | Setup | Expected |
|---|---|---|---|
| MCP-001 | Fetch settled transaction status | Temporary results directory contains `TXN001.json` | Payload has `found=True`, `transaction_id=TXN001`, `status=settled`, `reason_codes=["SETTLEMENT_SIMULATED"]`, `component_count=2`, `audit_event_count=1`, and simulation notice |
| MCP-002 | Fetch rejected transaction status | Temporary results directory contains `TXN006.json` | Payload has `found=True`, `status=rejected`, `currency=XYZ`, and `reason_codes=["UNSUPPORTED_CURRENCY"]` |
| MCP-003 | Missing transaction is safe | Temporary results directory lacks `UNKNOWN.json` | Payload has `found=False`, `transaction_id=UNKNOWN`, `reason_code=TRANSACTION_RESULT_NOT_FOUND`, and simulation notice |
| MCP-004 | Invalid transaction ID is rejected | Call with `../summary` | Payload has `found=False`, `reason_code=INVALID_TRANSACTION_ID`, and no filesystem read outside the supplied results directory |
| MCP-005 | List pipeline results summarizes files | Temporary results directory contains `summary.json`, `TXN001.json`, and `TXN006.json` | Payload has `found=True`, `summary.total_transactions=2`, `summary.settled=1`, `summary.rejected=1`, `result_count=2`, and transactions sorted by ID |
| MCP-006 | Empty results directory is safe | Temporary results directory exists with no summary or `TXN*.json` files | Payload has `found=False`, `reason_code=PIPELINE_RESULTS_NOT_FOUND`, `result_count=0`, `transactions=[]`, and simulation notice |
| MCP-007 | Summary resource is readable text | Temporary results directory contains `summary.json` | Text contains `Pipeline run summary`, runtime run ID, generated timestamp, total count, status counts, and simulation notice |
| MCP-008 | Summary resource handles no results | Temporary results directory has no result files | Text contains `Pipeline results not found` and `PIPELINE_RESULTS_NOT_FOUND` |
| MCP-009 | Payloads are privacy-safe | Serialize outputs from status, list, and summary helpers | Serialized text does not contain raw account IDs such as `ACC-1001`, raw descriptions such as `Monthly rent payment`, or keys `source_account`, `destination_account`, `description`, or `raw_transaction` |
| MCP-010 | Server module imports without running | Import `mcp.server` in a Python one-liner | Import succeeds without blocking on `mcp.run()` because the run call is guarded by `if __name__ == "__main__"` |
| MCP-011 | Combined MCP config parses | Read `mcp.json` after implementation | JSON contains `mcpServers.context7` and `mcpServers.pipeline-status` with command `python` and args `["mcp/server.py"]` |
| MCP-012 | Codex MCP config preserves agents cap | Read `.codex/config.toml` after implementation | TOML includes `[mcp_servers.context7]`, `[mcp_servers.pipeline-status]`, and `[agents] max_threads = 8` |

## Privacy Regression Source Values

Use these values only as strings to assert absence from MCP outputs:

- `ACC-1001`
- `ACC-2001`
- `Monthly rent payment`
- `source_account`
- `destination_account`
- `description`
- `raw_transaction`

## Validation Mapping

| Validation command | Covers |
|---|---|
| `python -m pytest tests/test_mcp_server.py -q` | MCP-001 through MCP-010 |
| `python -m json.tool mcp.json` | MCP-011 |
| `python -c "import mcp.server; print('pipeline-status')"` | MCP-010 |
| `python -m pytest -p no:cacheprovider` | Regression coverage for existing selected pipeline behavior plus MCP helper tests |
| `python scripts/check_coverage_gate.py --fail-under 80` | Coverage gate remains satisfied after adding MCP tests |

## Approval

- Status: Approved by operator on 2026-06-20
- Superseded by: None

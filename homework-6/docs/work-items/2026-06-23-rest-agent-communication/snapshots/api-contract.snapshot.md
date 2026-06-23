# REST Agent Communication API Contract Snapshot

Work ID: `2026-06-23-rest-agent-communication`
Short ID: `rest-agent-communication`
Status: Approved
Schema: `schema:snapshot.api-contract`

## Purpose

Define the local REST-shaped API boundary that replaces deterministic-agent direct file communication. The API owns runtime file persistence. Deterministic components own message transformations only.

## Service lifecycle

The integrator creates a runtime API service for each pipeline run. The service may run in process or expose a loopback HTTP server. In both cases, the service contract is expressed as REST operations with JSON request and response payloads.

The implementation should prefer in-process execution unless a real HTTP server can be added without manual startup, port conflicts, or flaky cleanup.

## Endpoints

### POST /runs

Creates a pipeline run, archives any existing shared directory, prepares protocol directories, writes run provenance, sanitizes input records, writes API-owned input messages, and returns run metadata.

Request:

```json
{
  "base_dir": ".",
  "shared_dir_name": "shared",
  "input_records": [
    {
      "transaction_id": "TXN001",
      "timestamp": "2026-03-16T09:00:00Z",
      "source_account": "ACC-1001",
      "destination_account": "ACC-2001",
      "amount": "1500.00",
      "currency": "USD",
      "transaction_type": "transfer",
      "description": "Monthly rent payment",
      "metadata": {
        "channel": "online",
        "country": "US"
      }
    }
  ]
}
```

Response:

```json
{
  "run_id": "uuid4-string",
  "expected_transaction_ids": ["TXN001"],
  "message_count": 1,
  "paths": {
    "shared": "shared",
    "input": "shared/input",
    "processing": "shared/processing",
    "output": "shared/output",
    "results": "shared/results"
  }
}
```

Persistence:

- `shared/input/TXN001.json`
- `shared/run-provenance.json`
- `archive/shared-001/` when a prior shared tree exists.

Privacy:

- Raw input records may be accepted in the request because the integrator loads `sample-transactions.json`.
- Persisted input messages must use the existing sanitized message shape and must not include raw account IDs or raw descriptions.

### GET /runs/{run_id}/messages/{transaction_id}

Returns the current in-memory message for a transaction.

Response:

```json
{
  "run_id": "uuid4-string",
  "transaction_id": "TXN001",
  "message": {
    "message_id": "uuid4-string",
    "timestamp": "2026-03-16T10:00:00Z",
    "source_agent": "integrator",
    "target_agent": "transaction_validator",
    "message_type": "transaction",
    "data": {
      "transaction_id": "TXN001",
      "amount": "1500.00",
      "currency": "USD"
    },
    "component_history": [],
    "audit_events": []
  }
}
```

Persistence:

- None required for read.

### POST /runs/{run_id}/messages/{transaction_id}/stages/{stage}

Records the latest message for a transaction and writes stage evidence when the stage maps to a protocol folder.

Allowed stages:

| Stage | File effect | Notes |
|---|---|---|
| `processing` | `shared/processing/{transaction_id}.json` | Snapshot before deterministic component processing. |
| `validated` | Optional in-memory only | Use when tests need to assert API communication after validation. |
| `scored` | Optional in-memory only | Use when tests need to assert API communication after fraud scoring. |
| `output` | `shared/output/{transaction_id}.json` | Snapshot after settlement and before final result persistence. |
| `result` | `shared/results/{transaction_id}.json` | Writes privacy-safe transaction result payload. |

Request:

```json
{
  "message": {
    "message_id": "uuid4-string",
    "timestamp": "2026-03-16T10:00:00Z",
    "source_agent": "settlement_processor",
    "target_agent": "reporting_agent",
    "message_type": "transaction",
    "data": {
      "transaction_id": "TXN001",
      "status": "settled",
      "reason_codes": ["SETTLED"],
      "amount": "1500.00",
      "currency": "USD",
      "settlement_reference": "SIM-TXN001"
    },
    "component_history": [],
    "audit_events": []
  }
}
```

Response:

```json
{
  "run_id": "uuid4-string",
  "transaction_id": "TXN001",
  "stage": "result",
  "status": "recorded"
}
```

Persistence:

- Controlled only by the API.

### POST /runs/{run_id}/messages/{transaction_id}/error

Writes a privacy-safe error result for a transaction and updates in-memory state so finalization can continue.

Request:

```json
{
  "reason_code": "PROCESSING_ERROR"
}
```

Response:

```json
{
  "run_id": "uuid4-string",
  "transaction_id": "TXN001",
  "status": "error",
  "reason_codes": ["PROCESSING_ERROR"]
}
```

Persistence:

- `shared/results/{transaction_id}.json`

### POST /runs/{run_id}/finalize

Builds and writes summary and pipeline status files from API-owned result persistence.

Request:

```json
{}
```

Response:

```json
{
  "schema_version": 1,
  "runtime_run_id": "uuid4-string",
  "total_records": 8,
  "settled": 2,
  "rejected": 2,
  "review_required": 4,
  "error": 0,
  "completeness_check": "passed"
}
```

Persistence:

- `shared/results/summary.json`
- `shared/results/pipeline-status.json`

## Error behavior

- Unknown run IDs raise a safe not-found error.
- Unknown transaction IDs raise a safe not-found error.
- Unknown stages raise a safe validation error.
- File persistence errors may bubble to the integrator, which records a safe processing error when possible.
- Error messages must not include raw account IDs, raw descriptions, or full transaction payloads.

## Component ownership boundary

| Runtime unit | May read/write protocol files? | Role |
|---|---:|---|
| Runtime API | Yes | Owns all `shared/` and `archive/` runtime persistence. |
| Integrator | No direct protocol stage writes after refactor | Orchestrates API calls and component function calls. May read input file for `sample-transactions.json`. |
| Transaction Validator | No | Pure validation and message transformation. |
| Fraud Detector | No | Pure risk scoring and message transformation. |
| Settlement Processor | No | Pure settlement-state transformation. |
| Reporting Agent | No during normal communication, except when called internally by API | Builds privacy-safe payloads; API owns file writes. |
| MCP server | Read-only external support surface | May continue reading `shared/results/` after pipeline completion. |

## Approval

- Status: Approved
- Superseded by: not superseded

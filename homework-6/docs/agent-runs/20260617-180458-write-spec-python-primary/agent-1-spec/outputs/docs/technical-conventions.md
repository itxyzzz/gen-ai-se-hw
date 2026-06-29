# Technical Conventions

Run ID: `20260617-180458-write-spec-python-primary`
Stack: `python`

## Project Layout

The Python implementation should use these paths unless a later approved plan changes them:

```text
integrator.py
agents/
  __init__.py
  transaction_validator.py
  fraud_detector.py
  settlement_processor.py
tests/
  test_transaction_validator.py
  test_fraud_detector.py
  test_settlement_processor.py
  test_integrator.py
shared/
  input/
  processing/
  output/
  results/
mcp/
  server.py
```

## Python Entry Points

- `integrator.py` owns `main()`, `load_transactions(path: str) -> list[dict]`, `prepare_shared_directories(base_path: str) -> None`, `write_initial_messages(...)`, `run_pipeline(...)`, and `summarize_results(...)`.
- Each pipeline agent module exposes `process_message(message: dict) -> dict`.
- The validator should also expose `validate_transaction(transaction: dict) -> dict` for dry-run command support.
- The fraud detector should expose `score_fraud_risk(transaction: dict) -> dict`.
- The settlement processor should expose `settle_transaction(message: dict) -> dict` or equivalent finalization behavior.

## Money

- Use `decimal.Decimal` for all parsed transaction amounts.
- Construct `Decimal` from strings, not from binary floating-point values.
- Reject missing, malformed, non-finite, zero, or negative amounts.
- Preserve amount strings in JSON output; use helper functions to convert `Decimal` back to normalized strings.

## Currency

- Validate currencies with an ISO 4217-style allowlist.
- Require uppercase three-letter strings and allowlist membership.
- Include at least `USD`, `EUR`, `GBP`, and `JPY` in the homework allowlist.
- Reject unsupported codes with reason code `unsupported_currency`.

## JSON File Protocol

Every agent message should be JSON serializable and include:

```json
{
  "message_id": "uuid4-string",
  "timestamp": "2026-03-16T10:00:00Z",
  "source_agent": "transaction_validator",
  "target_agent": "fraud_detector",
  "message_type": "transaction",
  "data": {
    "transaction_id": "TXN001",
    "amount": "1500.00",
    "currency": "USD",
    "status": "validated"
  }
}
```

Agents should write invalid or rejected outcomes to `shared/results/` with safe reason codes so every input transaction is accounted for.

## Audit And Logging

Use structured dictionaries for audit events before writing them to JSON or logs. Required fields:

- `timestamp`
- `agent_name`
- `transaction_id`
- `outcome`
- `reason_code`

Optional safe fields may include `risk_score`, `status`, and redacted account references. Do not log raw transaction descriptions, raw metadata, or plaintext account identifiers.

## Testing And Coverage

- Use `pytest`.
- Use `tmp_path` fixtures so tests do not mutate the real `shared/` directories.
- Use `pytest-cov` or `coverage.py`.
- The hook gate must fail below 80% coverage.
- The final documentation target should show at least 90% coverage where practical.

Recommended verification commands:

```powershell
python -m pytest
python -m pytest --cov=. --cov-fail-under=80
python integrator.py
```

## MCP

Task 4 must add Python FastMCP server code at `mcp/server.py` after pipeline result files exist. Required tools and resource:

- `get_transaction_status(transaction_id: str)`
- `list_pipeline_results()`
- `pipeline://summary`

Add `pipeline-status` to `mcp.json` and `.codex/config.toml` only after `mcp/server.py` exists.

## Context7

Agent 2 must use Context7 during code generation and record at least two queries in canonical `research-notes.md`. Each note should include:

- Search text.
- Returned library ID.
- Access date.
- Applied insight.

Good query targets include Python `decimal`, `pytest`/`pytest-cov`, standard-library JSON/file handling, and FastMCP.

# Technical Conventions

Status: Candidate support document for run `20260619-170102-write-spec-python-fresh`

## Python Modules

- Use plain Python modules in the homework root.
- Runtime components expose `process_message(message: dict) -> dict`.
- Keep reusable helpers in `agents/common.py`.
- Keep orchestration and CLI behavior in `integrator.py`.
- Use `pytest` tests under `tests/`.

## Identifiers

- Treat `transaction_id` as the stable safe correlation identifier.
- Treat `source_account` and `destination_account` as sensitive.
- Do not use sequential internal counters as public identifiers except deterministic file prefixes such as `001-TXN001.json` for local runtime evidence.

## Time

- Write UTC timestamps in ISO 8601-style format ending in `Z`.
- Store processing timestamps separately from transaction timestamps when useful.
- Treat sample timestamps as UTC.

## Money

- Parse all amounts from strings into `decimal.Decimal`.
- Never use `float` for amount parsing, comparisons, thresholds, summary totals, or tests.
- Serialize amounts as strings.
- Validate two fractional digits for `USD`, `EUR`, and `GBP` in this homework scope.

## Currency

- Currency codes must be strings.
- First validate uppercase three-letter shape.
- Then enforce the local supported set: `USD`, `EUR`, and `GBP`.
- Reject `XYZ` with `UNSUPPORTED_CURRENCY`.

## Message Envelope

Envelope fields:

- `message_id`
- `timestamp`
- `source_agent`
- `target_agent`
- `message_type`
- `data`
- `component_history`

Each component appends a history entry with component name, outcome, timestamp, and reason codes.

## File Protocol

Runtime file movement uses:

- `shared/input`
- `shared/processing`
- `shared/output`
- `shared/results`

Repeated runs archive existing `shared/` to `archive/shared-NNN` before creating a fresh tree. The archive folder is sibling to `shared/`.

## JSON

- Use the standard `json` module.
- Use sorted keys and indentation for deterministic evidence.
- Use `allow_nan=False`.
- Convert `Decimal` values to strings before writing JSON.
- Catch `json.JSONDecodeError` and produce safe errors.

## Audit And Redaction

Audit event fields:

- `event_id`
- `timestamp`
- `component`
- `transaction_id`
- `outcome`
- `reason_codes`
- optional `risk_level`

Sensitive keys forbidden from final results and audit details:

- `source_account`
- `destination_account`
- `description`
- `metadata`
- credentials, secrets, tokens, or authorization headers

## Result Shape

Per-transaction result fields:

- `schema_version`
- `transaction_id`
- `status`
- `reason_codes`
- `risk_score`
- `risk_level`
- `amount`
- `currency`
- `component_history`
- `processed_at`
- `safe_summary`
- sanitized `audit_events`

Summary fields:

- `schema_version`
- `runtime_run_id`
- `total_transactions`
- `settled`
- `rejected`
- `review_required`
- `error`
- `result_files`
- `generated_at`
- `simulation_notice`

## Testing

- Use `tmp_path` for filesystem tests.
- Do not mutate real `shared/` or `archive/` from unit tests.
- Use `python -m pytest` as the primary command.
- Use `python -m pytest --cov=.` for the temporary 75% coverage target when `pytest-cov` is available.


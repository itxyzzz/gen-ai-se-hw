# Privacy And Schema Handoff

Assigned scope: support privacy, stack-neutral result compatibility, and runtime provenance review. No files were edited.

Files inspected: generate-code workflow, quality bar, run registry, repository README, and skill entrypoint.

Commands/tests run: none against the candidate product; read-only review only.

Accepted guidance:

- Reject raw account IDs, raw sample descriptions, raw metadata dumps, credentials, hidden prompt/thread content, or production-compliance claims.
- Keep result files stack-neutral for a Python `mcp/server.py` reader.
- Require per-transaction keys: `schema_version`, `transaction_id`, `status`, `reason_codes`, `amount`, `currency`, `processed_at`, `risk_tier`, `component_history_count`, and `audit_event_count`.
- Require `summary.json` keys: `schema_version`, `generated_at`, `total`, `settled`, `rejected`, `review_required`, `error`, `complete`, and `reason_code_counts`.
- Preserve safe `shared/run-provenance.json` fields only.

Observed follow-up:

- Privacy scan initially found raw fixture values in negative tests and guard constants. The tests and guard were repaired to construct sensitive strings without preserving full raw sample values verbatim.
- Result key inspection confirmed all `TXN*.json` files contain only safe stack-neutral keys plus optional `settlement_reference`.
- Field-name scan finds parser/privacy-guard references to input key names. These are controlled source-code references only; generated result JSON and provenance do not include raw account/description fields or values.

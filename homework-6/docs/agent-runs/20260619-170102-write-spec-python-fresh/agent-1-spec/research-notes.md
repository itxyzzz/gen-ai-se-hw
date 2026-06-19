# Research Notes

Run ID: `20260619-170102-write-spec-python-fresh`

Selected stack: `python`

Access date: 2026-06-19

## Source Log

### Context7: Python Standard Library

- Search text: `Python standard library decimal exact decimal arithmetic json allow_nan documentation`
- Returned library ID: `/python/cpython`
- Query: `For a Python transaction processing spec, what do the standard library docs say about decimal.Decimal exact decimal arithmetic and json.dump allow_nan=False behavior?`
- Applied insight: the spec requires `decimal.Decimal` for all money parsing and comparisons, with amounts constructed from strings and serialized back to JSON as strings. The spec also requires strict JSON writing with `allow_nan=False` so non-finite numeric values cannot be silently emitted.
- Source provenance: Context7 returned CPython documentation and source snippets for `decimal` and `json`.

### ISO: ISO 4217 Currency Codes

- Search text: `ISO 4217 currency codes official ISO overview`
- Source: https://www.iso.org/iso-4217-currency-codes.html
- Applied insight: the spec uses uppercase three-letter alphabetic currency codes, treats `USD`, `EUR`, and `GBP` as supported sample currencies, and requires rejection of unsupported sample value `XYZ`.
- Boundary: the product uses ISO 4217-style validation for an educational simulation. It does not claim complete production currency-list maintenance.

### SIX: ISO 4217 Maintenance Agency

- Search text: `site:six-group.com ISO 4217 currency code list`
- Source: https://www.six-group.com/en/products-services/financial-information/market-reference-data/data-standards.html
- Applied insight: the spec can cite SIX as the recognized maintenance agency for ISO 4217 lists and should recommend that a real production system depend on an authoritative maintained currency list rather than a hard-coded homework allowlist.
- Boundary: this Homework 6 run should keep the allowlist deliberately small for sample determinism.

### OWASP: Logging Cheat Sheet

- Search text: `OWASP Logging Cheat Sheet data to exclude financial account data personal data`
- Source: https://cheatsheetseries.owasp.org/cheatsheets/Logging_Cheat_Sheet.html
- Applied insight: the spec requires structured audit/log events with enough `when`, `where`, `who`, and `what` context for review, while masking or excluding bank account or payment-card holder data, secrets, sensitive personal data, raw request/response bodies, and unfiltered descriptions.
- Boundary: OWASP logging guidance supports conservative engineering choices; the product remains an educational simulation, not a compliance implementation.

## Assignment Assumptions

- The input fixture is synthetic and belongs to the homework.
- The transaction-processing system does not move real money.
- Runtime transaction pipeline agents are Python application components, not assistant skills.
- The future read-only MCP server should consume stable result files but should not be configured before `mcp/server.py` exists.

## Generated Design Decisions

- Use a homework-scoped supported currency set of `USD`, `EUR`, and `GBP`.
- Reject `XYZ` with `UNSUPPORTED_CURRENCY`.
- Reject negative or zero amounts with `NON_POSITIVE_AMOUNT`; reject malformed or non-string amounts with `INVALID_AMOUNT`.
- Use deterministic risk signals such as `HIGH_VALUE`, `VERY_HIGH_VALUE`, `WIRE_TRANSFER`, `ODD_HOUR_ACTIVITY`, `REMOTE_CHANNEL`, and `CROSS_COUNTRY_REVIEW_SIGNAL`.
- Keep risk scoring transparent and educational; do not call it a trained model, fraud determination, AML decision, sanctions screen, payment-network decision, or legal compliance control.
- Write `shared/run-provenance.json` with non-sensitive identifiers and fingerprints only.

## Fallback Limitations

- Shell-level network access is restricted, so web and Context7 lookups were performed through available Codex tools.
- The original `specification-TEMPLATE-hint.md` is absent from this checkout; this run uses Homework 3 depth and the write-spec quality bar as the template source.


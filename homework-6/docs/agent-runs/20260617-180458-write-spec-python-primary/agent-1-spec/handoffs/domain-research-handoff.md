# Domain Research Handoff

Run ID: `20260617-180458-write-spec-python-primary`
Stack: `python`
Sub-agent role: Domain research

## Scope Assigned

Research guidance for a Python-specific specification for a simulated multi-agent banking transaction pipeline.

Assigned topics:

1. ISO 4217-style currency-code validation.
2. Python `decimal.Decimal` money handling and binary floating-point risks.
3. Safe structured audit logging and account-identifier redaction.
4. Educational-simulation boundary and unsupported compliance claims.
5. FastMCP and Context7 expectations as later implementation responsibilities.

## Sources Or Fallbacks

Web access was available. Current or official sources were used where practical:

- ISO 4217 Currency Codes: `https://www.iso.org/iso-4217-currency-codes.html`
- SIX Financial Information Data Standards: `https://www.six-group.com/en/products-services/financial-information/market-reference-data/data-standards.html`
- Python `decimal` documentation: `https://docs.python.org/3/library/decimal.html`
- Python logging cookbook: `https://docs.python.org/3/howto/logging-cookbook.html`
- NIST SP 800-92: `https://csrc.nist.gov/pubs/sp/800/92/final`
- OWASP Logging Cheat Sheet: `https://cheatsheetseries.owasp.org/cheatsheets/Logging_Cheat_Sheet.html`
- FastMCP documentation: `https://gofastmcp.com/getting-started/welcome`
- Context7 FastMCP query result: `/prefecthq/fastmcp`

Local assignment context was also used: `TASKS.md`, `sample-transactions.json`, `agents.md`, and the approved Phase 02 plan. No fallback-only conclusions were needed. Exact Context7 library IDs for later Agent 2 implementation queries must be captured by Agent 2 during code generation, not invented by this run.

## Findings

ISO 4217 defines internationally recognized alphabetic and numeric currency codes. ISO describes alphabetic currency codes as three-letter codes, and SIX states it maintains the ISO 4217 code lists as the official maintenance agency. For this homework, the spec should require uppercase three-letter currency strings plus membership in a supported allowlist, not only a regular expression. `XYZ` must be rejected because it is not a supported homework currency.

Python `decimal.Decimal` is the correct money primitive for the selected Python stack. Python's official docs describe exact decimal representation, preservation of significance, configurable rounding, and accounting-style equality behavior. Amounts should be parsed from JSON strings directly into `Decimal`, never through binary floating point, and serialized back to strings in JSON results.

Audit logging should be structured and minimal. Python's logging docs support structured logging patterns; NIST frames log management around event records and effective log-management practices; OWASP logging guidance warns that logs can include sensitive data and should exclude data that should not be collected. For this pipeline, audit events should include timestamp, agent name, transaction ID, safe outcome, and reason code when useful. They should not include plaintext source or destination accounts, full descriptions, raw metadata dumps, or customer-like identifiers. Account examples should be redacted as `ACC-****1001`.

The banking rules must stay explicitly educational. The assignment asks for simulated validation, deterministic risk scoring, settlement/reporting, audit trails, and MCP access. It does not ask for legal, AML, sanctions, payment-network, KYC, or banking-regulatory compliance. Risk flags should be described as deterministic homework heuristics, not compliance judgments.

FastMCP and Context7 are downstream implementation obligations. The spec should tell Agent 2 to use Context7 during code generation and document at least two queries with search text, returned library ID, date, and applied insight. The spec should tell the MCP phase to create Python `mcp/server.py` with FastMCP tools/resources after pipeline result files exist, then add `pipeline-status` configuration only after that server exists.

## Applied Decisions For The Spec

- Use Python `decimal.Decimal` for every amount; parse from amount strings such as `"25000.00"` and reject invalid or non-positive values.
- Reject `TXN006` because currency `XYZ` is unsupported.
- Reject `TXN007` because amount `-100.00` is negative.
- Treat high-value USD transfers `25000.00` and `75000.00` as risk-review signals, not automatic criminal or compliance findings.
- Treat the `02:47:00Z` EUR/API transaction as an odd-hour risk signal.
- Use country metadata such as `US`, `DE`, and `GB` as an educational risk dimension only.
- Define audit event shape as `timestamp`, `agent_name`, `transaction_id`, `outcome`, `reason_code`, and optional safe summary fields.
- Redact account identifiers in logs and examples, such as `ACC-****1001`.
- Avoid logging transaction descriptions verbatim because descriptions can contain sensitive invoice or order references.
- Keep FastMCP and Context7 notes concrete but deferred: Agent 2 records Context7 research; later MCP implementation creates `mcp/server.py` and exposes required tools/resources.

## Assumptions And Limits

- The sample data is synthetic and educational.
- Currency validation can use a homework-scoped allowlist containing at least `USD`, `EUR`, `GBP`, and `JPY`; a production system would need maintained ISO 4217 data.
- Suggested deterministic risk heuristics: flag amounts greater than `10000.00`, add risk for odd-hour timestamps, and add risk for non-default country/channel combinations.
- Cross-country metadata is interpreted from `metadata.country` because the sample does not include separate source and destination countries.
- No legal, AML, sanctions, KYC, payment-network, or bank-compliance conclusions should appear in the generated spec or logs.
- Agent 2 should resolve and record actual Context7 library IDs during code generation.

## Residual Risks

- ISO currency data changes over time; a fixed allowlist is acceptable for homework but not durable production validation.
- Redaction reduces exposure but does not make logs non-sensitive; logs should still be treated as sensitive artifacts.
- Descriptions and metadata can contain indirect identifiers, so examples should avoid echoing them into logs or audit summaries.
- FastMCP and Context7 APIs/configuration may evolve; implementation agents should re-check current docs when writing `mcp/server.py` and `mcp.json`.
- If the spec overuses terms such as fraud or compliance, reviewers may read it as stronger than intended. Prefer "risk review," "simulation," and "heuristic reason code" language.

## Recommended Next Step

Use these findings to create objective and low-level task cards that are concrete for Python, privacy-aware, and explicit about educational simulation boundaries.

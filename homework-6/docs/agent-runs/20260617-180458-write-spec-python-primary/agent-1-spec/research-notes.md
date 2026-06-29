# Research Notes

Run ID: `20260617-180458-write-spec-python-primary`
Stack: `python`
Access date: `2026-06-17`

## Cited Facts

- Source/query: ISO 4217 currency-code handling via `https://www.iso.org/iso-4217-currency-codes.html` and SIX ISO 4217 maintenance-agency information at `https://www.six-group.com/en/products-services/financial-information/market-reference-data/data-standards.html`.
  Type: cited fact.
  Applied insight: require uppercase three-letter currency codes and membership in a supported ISO-style allowlist; reject sample `XYZ`.

- Source/query: Python money arithmetic via `https://docs.python.org/3/library/decimal.html`.
  Type: cited fact.
  Applied insight: parse JSON amount strings into `decimal.Decimal`, preserve cents/significance, and serialize amounts back as strings.

- Source/query: Python structured logging via `https://docs.python.org/3/howto/logging-cookbook.html`.
  Type: cited fact.
  Applied insight: specify structured audit/log records rather than ad hoc plaintext messages.

- Source/query: Audit/log-management framing via NIST SP 800-92 at `https://csrc.nist.gov/pubs/sp/800/92/final`.
  Type: cited fact.
  Applied insight: model audit entries as event records with timestamp, agent/source, transaction ID, outcome, and reason code.

- Source/query: Sensitive data in logs via OWASP Logging Cheat Sheet at `https://cheatsheetseries.owasp.org/cheatsheets/Logging_Cheat_Sheet.html`.
  Type: cited fact.
  Applied insight: avoid plaintext account IDs, descriptions, and raw metadata in logs; use redacted account examples such as `ACC-****1001`.

- Source/query: FastMCP later implementation docs via `https://gofastmcp.com/getting-started/welcome` and Context7 query for FastMCP.
  Type: cited fact.
  Context7 result: `/prefecthq/fastmcp`.
  Applied insight: later MCP task should implement Python `mcp/server.py` with `FastMCP`, `@mcp.tool`, `@mcp.resource`, and `mcp.run()` after pipeline result files exist.

## Local Assignment Facts

- Source/query: `TASKS.md`.
  Type: local assignment.
  Applied insight: `specification.md` must include the five Task 1 sections; Agent 2 must build at least three cooperating agents; Agent 3 must provide commands and a coverage hook; Agent 4 must create documentation and screenshots.

- Source/query: `sample-transactions.json`.
  Type: local assignment.
  Applied insight: cover all 8 sample transactions, including invalid `XYZ`, negative amount `-100.00`, high-value USD transfers, odd-hour EUR/API transaction, and US/DE/GB metadata.

- Source/query: `agents.md`.
  Type: local assignment.
  Applied insight: preserve run artifacts, avoid overwriting the standing agent guide, apply privacy/audit rules, and add `pipeline-status` config only after `mcp/server.py` exists.

## Generated Design Decisions

- Use `python` as the generated stack because the approved profile makes it the default and it aligns with `decimal.Decimal`, `pytest`, `pytest-cov`, JSON files, and Python FastMCP.
- Use a homework-scoped supported-currency allowlist including at least `USD`, `EUR`, `GBP`, and `JPY`, with room for maintained ISO data only as a future production concern.
- Treat amounts greater than `10000.00`, odd-hour timestamps, and non-default country/channel combinations as risk-review signals.
- Route all final per-transaction outcomes to `shared/results/` with reason codes and a summary report.
- Keep risk scoring deterministic and transparent; do not claim legal, AML, sanctions, KYC, payment-network, or banking-compliance coverage.

## Fallback Limitations

- `specification-TEMPLATE-hint.md` is absent; this run uses Task 1's section list, shared write-spec quality references, and Homework 3 format examples.
- Exact Agent 2 Context7 query IDs are deferred to Agent 2 code generation. This run records the requirement and the verified FastMCP result only.

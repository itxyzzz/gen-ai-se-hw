# Research Notes

Run ID: `20260618-003908-write-spec-python-replacement`

Stack: `python`

## Source Log

| Date | Source | URL or ID | Applied insight |
|---|---|---|---|
| 2026-06-18 | Python `decimal` documentation | https://docs.python.org/3/library/decimal.html | Parse monetary amount strings into `Decimal`, never `float`; preserve exact decimal arithmetic and serialize money as strings. |
| 2026-06-18 | Python `json` documentation | https://docs.python.org/3/library/json.html | Use standard `json`; handle `JSONDecodeError`; serialize `Decimal` explicitly; use `allow_nan=False` for strict result files. |
| 2026-06-18 | pytest `tmp_path` documentation | https://docs.pytest.org/en/stable/how-to/tmp_path.html | Require tests to isolate `shared/input`, `shared/processing`, `shared/output`, and `shared/results` under temporary directories. |
| 2026-06-18 | ISO 4217 overview | https://www.iso.org/iso-4217-currency-codes.html | Currency validation should use ISO 4217-style three-letter alphabetic codes; sample-supported currencies are `USD`, `EUR`, and `GBP`; reject `XYZ`. |
| 2026-06-18 | ISO 4217:2015 standard page | https://www.iso.org/standard/64758.html | ISO 4217 defines alphabetic/numeric currency code structures and minor-unit relationships for trade, commerce, and banking contexts. |
| 2026-06-18 | SIX ISO 4217 Maintenance Agency page | https://www.six-group.com/en/products-services/financial-information/market-reference-data/data-standards.html | SIX is the official maintenance source for current ISO 4217 lists; direct XML list fetch was unavailable in this run, so avoid claiming full live-list verification. |
| 2026-06-18 | OWASP Logging Cheat Sheet | https://cheatsheetseries.owasp.org/cheatsheets/Logging_Cheat_Sheet.html | Audit/log records should include useful event attributes but mask or exclude sensitive personal, bank account, and payment data. |
| 2026-06-18 | NIST SP 800-92 | https://csrc.nist.gov/pubs/sp/800/92/final | Treat log management as structured operational/security evidence, but do not claim regulatory compliance for the homework simulation. |

## Context7 Notes

Context7 was not directly callable during this Athena (Spec Writer) run. The generated specification must require Hephaestus (Code Generator) to use Context7 during implementation and record at least two queries with search text, returned library ID, access date, and applied insight.

## Assignment Assumptions

- The transaction pipeline is an educational simulation only.
- The product starts from `sample-transactions.json`, processes eight synthetic records, and writes final outcomes under `shared/results/`.
- Runtime transaction pipeline agents are Python modules/classes with a shared `process_message(message: dict) -> dict` contract, not AI assistant skills.
- Required runtime components are Transaction Validator, Fraud Detector, Settlement Processor, and Integrator.
- Required directory protocol is `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.

## Design Decisions

- Use reason-code strings in uppercase snake case for validation, risk, settlement, and pipeline errors.
- Keep sample currency support explicit: `USD`, `EUR`, and `GBP` are enough for the provided samples; unsupported `XYZ` must reject.
- Use deterministic educational risk thresholds: high value at or above `10000.00`, very high value at or above `50000.00`, and odd-hour activity from `00:00` through `04:59` UTC.
- Keep final status vocabulary small: `settled`, `rejected`, `review_required`, and `error`.
- Define MCP-readable result shapes without making MCP configuration setup a product low-level task.

## Fallback Limitations

- No direct Context7 query was available in the Athena runtime.
- The SIX currency-code page was reachable, but a live full code-list retrieval was not verified in this run.
- Risk thresholds are assignment-driven heuristics, not sourced banking standards.

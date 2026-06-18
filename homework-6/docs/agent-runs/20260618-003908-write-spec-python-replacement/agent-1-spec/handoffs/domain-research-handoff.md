# Domain Research Handoff

Run ID: `20260618-003908-write-spec-python-replacement`

Stack: `python`

Artifact target: `agent-1-spec/handoffs/domain-research-handoff.md`

## 1. Assigned Scope

Research conservative domain and technical rules for a Python educational transaction-processing pipeline specification. Focus areas: precise money handling, ISO 4217-style currency validation, audit/privacy-safe logging and result records, deterministic risk/review heuristics, unsupported compliance claims, and product-only boundaries for the Generated Transaction System Layer.

## 2. Files/Context Inspected

- `agent-control/write-spec/workflow.md`
- `agent-control/write-spec/stack-profiles.md`
- `agent-control/write-spec/quality-bar.md`
- `agent-control/write-spec/run-registry.md`
- `agent-control/write-spec/transaction-system-brief.md`
- `sample-transactions.json`
- `agents.md`
- `docs/agent-runs/20260618-003908-write-spec-python-replacement/run-metadata.md`
- `docs/agent-runs/20260618-003908-write-spec-python-replacement/inputs/source-context.md`
- `docs/agent-runs/20260618-003908-write-spec-python-replacement/agent-1-spec/handoffs/sub-agent-plan.md`
- User-provided repository and Homework 6 instructions in the prompt.

## 3. Sources Or Commands Used

Local commands:

- `Get-Content -Raw ...` for workflow, stack profile, quality bar, run registry, product brief, sample transactions, Homework 6 guide, run metadata, source context, and sub-agent plan.
- `Get-ChildItem -Force ...` and `Get-ChildItem -Recurse -File ...` to inspect the active run folder shape.

External sources accessed on 2026-06-18:

- Python `decimal` docs: [`decimal` supports exact decimal arithmetic and is preferred for accounting-style invariants](https://docs.python.org/3/library/decimal.html).
- Python `json` docs: [`json.load(s)` can use `parse_float=decimal.Decimal`; encoders need a custom `default` for unsupported objects; `allow_nan=False` enforces stricter JSON](https://docs.python.org/3/library/json.html).
- pytest docs: [`tmp_path` provides a unique temporary directory per test function](https://docs.pytest.org/en/stable/how-to/tmp_path.html).
- ISO: [ISO 4217 defines three-letter alphabetic and three-digit numeric currency codes for trade, commerce, and banking uses](https://www.iso.org/iso-4217-currency-codes.html).
- ISO standard page: [ISO 4217:2015 specifies the code structures and minor-unit relationship](https://www.iso.org/standard/64758.html).
- SIX: [SIX is the official ISO 4217 Maintenance Agency source for currency code designations and current lists](https://www.six-group.com/en/products-services/financial-information/market-reference-data/data-standards.html).
- OWASP Logging Cheat Sheet: [application logs should include useful event attributes and avoid direct logging of sensitive personal, bank account, or payment data](https://cheatsheetseries.owasp.org/cheatsheets/Logging_Cheat_Sheet.html).
- NIST SP 800-92 page: [NIST frames log management as practical guidance for developing and maintaining security log management practices](https://csrc.nist.gov/pubs/sp/800/92/final).

Fallback limitations:

- Context7 was not directly callable in this sub-agent context. Record this limitation; Hephaestus (Code Generator) must still perform and document at least two Context7 queries later.
- The web tool could open SIX's currency-code standards page, but direct XML list retrieval failed through the browser-backed fetch path. Avoid claiming a live full currency-list verification from this run.

## 4. Research Findings

### Cited Facts

- Python `decimal.Decimal` should be used for monetary amounts because the standard docs describe exact decimal representation, accounting-friendly equality behavior, preserved significant trailing zeros, configurable rounding, and construction from strings. Avoid constructing money from `float`; parse amount strings directly into `Decimal`. Source: Python `decimal` docs.
- Python `json` defaults JSON real numbers to `float`, but `parse_float=decimal.Decimal` can preserve decimal values when JSON numeric literals are used. Since the sample stores amounts as strings, the safer spec decision is to keep serialized amounts as strings and parse them at validation boundaries. `Decimal` values require explicit serialization, such as converting to strings or using a custom encoder. Source: Python `json` docs.
- `json.JSONDecodeError` is the expected malformed JSON failure type. The spec should require malformed message files to produce safe per-transaction error results rather than crash the whole run. Source: Python `json` docs.
- `json.dump(s)` should use `allow_nan=False` for result files so non-finite numeric values are rejected instead of emitted as non-standard JSON tokens. Source: Python `json` docs.
- pytest's `tmp_path` fixture provides a unique temporary directory per test function, supporting tests that isolate `shared/` filesystem state from the real checkout. Source: pytest docs.
- ISO 4217 provides three-letter alphabetic and three-digit numeric currency codes, and ISO describes it as intended for trade, commerce, and banking applications. SIX maintains the current ISO 4217 code lists as the official maintenance agency. Sources: ISO and SIX pages.
- OWASP logging guidance supports keeping useful event attributes such as time, component/location, action/object, result status, reason, and confidence, while excluding or masking sensitive personal, bank account, and payment data. Source: OWASP Logging Cheat Sheet.

### Assignment Assumptions

- The product is an educational simulation, not real banking, AML, sanctions, KYC, payment-network, legal, or regulatory compliance software.
- Required runtime components are stack-native Python application components: Transaction Validator, Fraud Detector, Settlement Processor, and Integrator. They are not Claude/Codex skills.
- Required directories are `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- The pipeline begins from eight synthetic transactions in `sample-transactions.json` and must account for all eight in final results.
- Notable sample behavior to preserve:
  - `TXN006`: reject invalid/unsupported currency `XYZ`.
  - `TXN007`: reject negative amount `-100.00`.
  - `TXN002`: mark high-value USD `25000.00` for review or elevated risk.
  - `TXN005`: mark very-high-value USD `75000.00` for review or stronger elevated risk.
  - `TXN004`: flag odd-hour `02:47:00Z` EUR API transfer as a contextual risk signal.
- Use Python `decimal.Decimal`, Python standard `json`, `integrator.py`, `agents/` modules, `process_message(message: dict) -> dict`, `pytest`, temporary directories, and a temporary Athena ending-context coverage target of 75%.
- The future FastMCP server at `mcp/server.py` should read product result files; this spec should define result shapes only, not MCP configuration setup.

### Design Decisions

- Currency validation should be explicit and deterministic for the homework sample. Minimum supported set for this run should include `USD`, `EUR`, and `GBP`; reject `XYZ` with reason code `UNSUPPORTED_CURRENCY`. If builders add a broader ISO-code table, it must come from a maintained source or a clearly documented static allowlist.
- Amount validation should parse from string to `Decimal`, reject malformed, zero, negative, `NaN`, and infinite values, and serialize validated amounts back to canonical strings.
- Risk scoring should be deterministic and educational, not predictive or compliance-based. Recommended signals:
  - high value at or above `10000.00`;
  - very high value at or above `50000.00`;
  - wire transfer type;
  - odd-hour UTC window such as `00:00` through `04:59`;
  - API/mobile/online channel context;
  - country/channel/type combinations used only as sample heuristics.
- Status vocabulary should be small and testable: `settled`, `rejected`, `review_required`, and `error`.
- Reason codes should be stable strings: examples include `MISSING_FIELD`, `INVALID_AMOUNT`, `NON_POSITIVE_AMOUNT`, `UNSUPPORTED_CURRENCY`, `HIGH_VALUE`, `VERY_HIGH_VALUE`, `ODD_HOUR_ACTIVITY`, `MALFORMED_JSON`, and `PIPELINE_ERROR`.
- Product result files should be MCP-readable JSON with safe fields only: `transaction_id`, `status`, `reason_codes`, `risk_score`, `risk_level`, `amount`, `currency`, `component_history`, `audit_events`, and `summary`. Do not include raw account IDs or raw descriptions.
- Redaction should be centralized in a helper such as `redact_account_id(value: str) -> str`, producing examples like `ACC-****1001`.

## 5. Privacy/Audit Rules To Carry Into The Spec

- Treat account identifiers, descriptions, metadata, and audit details as sensitive even though sample data is synthetic.
- Never log or document full account identifiers, raw descriptions, tokens, secrets, or unnecessary metadata. Use redacted account examples such as `ACC-****1001`.
- Audit events should include: ISO 8601 timestamp, runtime component name, transaction ID, safe outcome/status, reason code, and optional risk level/confidence. Avoid full payload dumps.
- Validation failures and pipeline errors should produce safe reason codes and brief safe messages.
- Logs and results should be structured JSON where possible; sanitize message-derived text to prevent newline/control-character log injection.
- The spec should require tests that assert redaction, no plaintext account IDs in audit/result summaries, and no raw descriptions in logs.

## 6. Unsupported Claims To Avoid

- Do not claim real banking compliance, AML compliance, sanctions screening, KYC compliance, payment-network compliance, PCI compliance, legal compliance, fraud-detection accuracy, regulatory audit sufficiency, or production readiness.
- Do not claim that ISO 4217 validation alone makes a transaction compliant or valid for real settlement.
- Do not claim live verification of the full current ISO 4217 list from this sub-agent, because the direct SIX XML list was not retrieved here.
- Do not describe Transaction Validator, Fraud Detector, Settlement Processor, or Integrator as Athena, Hephaestus, Themis, Clio, Claude/Codex skills, or executor sub-agents.
- Do not include dev-doc-harness, Superpowers, run preservation, final selection, screenshots, hooks, PR packaging, or MCP configuration setup as product low-level tasks.

## 7. Residual Risks

- The threshold choices for high-value and very-high-value review are assignment-driven educational heuristics, not sourced banking standards.
- A static currency allowlist can become stale if expanded beyond the sample currencies; if the implementation broadens scope, it needs a documented update path.
- File movement through `shared/processing` and `shared/output` can become ambiguous unless the objectives architect and task decomposer require deterministic file naming, idempotent rerun reset, and per-transaction recovery.
- Redaction can regress if audit/result helpers are not centralized and tested.
- Future MCP tooling may need additional fields, so result shapes should be stable but extensible with version fields.

## 8. Recommended Next Step For The Objectives Architect

Shape the high-level and mid-level objectives around observable product outcomes: validated decimal-safe input loading, deterministic file-based orchestration, privacy-safe component audit events, rule-based risk/review decisions for the eight sample transactions, final result/summary files under `shared/results`, isolated pytest coverage at the 75% temporary target, and MCP-readable result contracts without MCP setup mechanics.

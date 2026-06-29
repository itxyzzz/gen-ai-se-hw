# Domain Research Handoff

## Assigned Scope

Domain research handoff for a Python educational transaction-processing pipeline. Scope covered banking-pipeline domain assumptions, privacy/audit rules, ISO-currency assumptions, unsupported compliance claims, and accepted Python stack/library guidance for the Generated Transaction System Layer.

## Files/Context Inspected

- User dispatch prompt for run `20260621-220037-write-spec-python-hera-python-full-set`.
- `agent-control/write-spec/workflow.md`
- `agent-control/write-spec/stack-profiles.md`
- `agent-control/write-spec/quality-bar.md`
- `agent-control/write-spec/run-registry.md`
- `agent-control/write-spec/transaction-system-brief.md`
- `agents.md`
- `sample-transactions.json`, inspected only to confirm record mix and edge cases; raw payload should not be reproduced in generated docs or audit examples.

## Sources/Commands Used

Local commands used by the domain-research sub-agent:

- `Get-Content -Raw .agents\skills\write-spec\SKILL.md`
- `Get-Content -Raw agent-control\write-spec\workflow.md`
- `Get-Content -Raw agent-control\write-spec\stack-profiles.md`
- `Get-Content -Raw agent-control\write-spec\quality-bar.md`
- `Get-Content -Raw agent-control\write-spec\run-registry.md`
- `Get-Content -Raw agent-control\write-spec\transaction-system-brief.md`
- `Get-Content -Raw sample-transactions.json`
- `Get-Content -Raw agents.md`

External and documentation sources:

- Context7 `/python/cpython`: accepted Python standard-library profile for `decimal`, `json`, and `pathlib`.
- Context7 `/pytest-dev/pytest`: accepted pytest profile for `tmp_path` and `monkeypatch` isolation.
- Python `decimal` documentation: use exact decimal arithmetic and avoid binary float money handling.
- Python `json` documentation: non-native values such as `Decimal` must be converted before JSON output.
- Python `pathlib` documentation: `Path` methods support readable text file operations.
- pytest `tmp_path` documentation: `tmp_path` provides a unique `pathlib.Path` temporary directory for each test.
- pytest `monkeypatch` documentation: `monkeypatch.chdir()` and environment patching can redirect filesystem effects in tests.
- ISO 4217 page, `https://www.iso.org/iso-4217-currency-codes.html`, checked by the orchestration thread on 2026-06-21: ISO 4217 defines alphabetic and numeric currency codes, including three-letter alphabetic currency codes.
- SIX Financial Data Standards page, `https://www.six-group.com/en/products-services/financial-information/market-reference-data/data-standards.html`, checked by the orchestration thread on 2026-06-21: SIX is the official ISO 4217 Maintenance Agency and publishes current and historical lists. The generated product should not imply live code-list maintenance unless a later implementation explicitly integrates such a source.

## Findings To Apply

- Treat the system as a deterministic educational simulation with four stack-native runtime components: Transaction Validator, Fraud Detector, Settlement Processor, and Reporting Agent. The Integrator orchestrates setup and message flow separately.
- Transaction Validator should reject malformed or unsafe input before downstream processing:
  - Missing required fields.
  - Unparseable amount strings.
  - Non-positive amounts.
  - Unsupported currency codes.
  - Malformed timestamps or malformed JSON.
- Use `decimal.Decimal` constructed from input strings, never from floats. Reject non-finite values and serialize final amounts as strings in JSON.
- Currency handling should be "ISO 4217-style" with an explicit allow-list for this sample, at minimum `USD`, `EUR`, and `GBP`; reject `XYZ`. Do not imply the implementation maintains a complete current ISO registry unless a real code-list source is added later.
- Fraud Detector should use transparent educational heuristics only:
  - High-value transactions become review-required.
  - Early-hour activity can increase risk.
  - Channel, country, and transaction type can contribute to risk scoring.
  - Thresholds must be explicit, deterministic, and testable.
- Settlement Processor should only settle transactions that passed validation and are not review-required. Rejected and review-required records should receive safe final statuses and reason codes without pretending to perform real settlement.
- Reporting Agent should own audit-safe aggregate outputs:
  - `shared/results/summary.json`
  - `shared/results/pipeline-status.json`
  - one safe result JSON per transaction
  - completeness checks proving all 8 sample records are accounted for
  - status count consistency checks
  - privacy checks for prohibited plaintext account identifiers/descriptions in result artifacts
- Audit events should be structured and minimal:
  - ISO 8601 timestamp
  - runtime component name
  - transaction ID
  - safe outcome/status
  - reason code when applicable
  - no source/destination account IDs, descriptions, or raw metadata values unless redacted or summarized safely
- Prefer reason codes over prose-heavy sensitive messages, for example `MISSING_FIELD`, `INVALID_AMOUNT`, `NON_POSITIVE_AMOUNT`, `UNSUPPORTED_CURRENCY`, `REVIEW_HIGH_VALUE`, `REVIEW_UNUSUAL_TIME`, `SETTLED`, and `PROCESSING_ERROR`.
- The file protocol should remain explicit:
  - fresh run creates `shared/input`, `shared/processing`, `shared/output`, `shared/results`
  - existing `shared/` is archived first as `archive/shared-001`, `archive/shared-002`, and so on
  - `shared/run-provenance.json` contains only non-sensitive run IDs, timestamps, paths, schema version, and fingerprints
- Tests should use `pytest` with `tmp_path` and `monkeypatch` to isolate filesystem state from the real repository `shared/` and `archive/`.

## Unsupported Claims To Avoid

- Do not claim AML, sanctions, KYC, fraud-prevention, payment-network, legal, bank regulatory, or real settlement compliance.
- Do not claim the fraud heuristics detect actual fraud; they only produce educational risk/review decisions.
- Do not claim ISO 4217 certification or complete live ISO code-list validation.
- Do not claim audit logs meet legal evidence, banking audit, retention, or regulatory reporting standards.
- Do not expose full account IDs, descriptions, raw metadata, raw sample payloads, or sensitive audit details in examples.
- Do not name runtime product components after homework automation identities or implement them as Codex/Claude skills.

## Assumptions And Uncertainty

- Accepted currencies for the sample are assumed to be `USD`, `EUR`, and `GBP`; `XYZ` is invalid.
- High-value examples should be review-required rather than rejected, unless validation fails for another reason.
- Early-hour activity is a risk signal, not an automatic rejection.
- Exact fraud thresholds are design decisions for the spec to pin down; recommended values should be deterministic and simple enough for tests.
- Live ISO code-list updates are out of scope unless the implementation later vendors or fetches a maintained list.

## Residual Risks

- If the generated spec phrases "ISO 4217 validation" too strongly, downstream code may overclaim standards support.
- If audit examples include account IDs from the sample, they will violate the privacy rules even if the sample is synthetic.
- If `Decimal` values are passed directly to `json.dumps()` without conversion, serialization will fail.
- If tests run in the repository root without `tmp_path` or cwd redirection, they may mutate real `shared/` or `archive/` evidence.
- If Reporting Agent privacy checks are omitted, result files may accidentally preserve descriptions or account IDs.

## Recommended Next Step

Integrate these findings into the Athena candidate `specification.md`, especially the implementation notes, domain rules, technical conventions, and low-level task cards for validation, fraud scoring, settlement, reporting, JSON serialization, audit redaction, rerun archival, and pytest isolation.

## Research Notes Entries To Preserve

- Source: Context7 `/python/cpython`; applied insight: accepted Python standard-library profile for `decimal`, `json`, and `pathlib`.
- Source: Context7 `/pytest-dev/pytest`; applied insight: accepted pytest profile for `tmp_path` and `monkeypatch` isolation.
- Source: Python `decimal` documentation through Context7; applied insight: parse money with `Decimal` from strings, avoid floats, serialize amounts as strings.
- Source: Python `json` documentation through Context7; applied insight: standard JSON output needs explicit conversion for `Decimal`.
- Source: Python `pathlib` documentation through Context7; applied insight: use `Path` methods for readable file-protocol operations.
- Source: pytest `tmp_path` documentation through Context7; applied insight: isolate run directories per test with temporary `Path` objects.
- Source: pytest `monkeypatch` documentation through Context7; applied insight: safely redirect cwd/env for filesystem tests.
- Source: ISO 4217 page, `https://www.iso.org/iso-4217-currency-codes.html`, checked 2026-06-21; applied insight: use three-letter currency-code convention conservatively.
- Source: SIX Financial Data Standards page, `https://www.six-group.com/en/products-services/financial-information/market-reference-data/data-standards.html`, checked 2026-06-21; applied insight: live currency-code maintenance is external, so this pipeline should use a bounded allow-list and avoid compliance claims.

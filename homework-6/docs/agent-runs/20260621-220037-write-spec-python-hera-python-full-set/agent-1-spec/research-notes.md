# Research Notes

## Run Context

- Run ID: `20260621-220037-write-spec-python-hera-python-full-set`
- Stack: `python`
- Mode: `generate`
- Parent Hera run ID: `20260621-215717-orchestrate-runs-python-full-set`
- Research owner: Athena (Spec Writer) orchestration thread with domain-research sub-agent handoff.

## Research Method

This run used local assignment context, Context7 documentation queries for Python and pytest, and a small official-source web check for ISO 4217 currency-code context. It did not perform broad banking/legal research because the generated product is explicitly an educational simulation and must avoid real compliance claims.

## Context7 Query Records

### Query 1: Python Decimal, JSON, And Pathlib

- Search text: `Python decimal module Decimal precise monetary arithmetic json serialization datetime pathlib file operations`
- Returned library ID: `/python/cpython`
- Access date: `2026-06-21`
- Applied insight:
  - Use `decimal.Decimal` for exact monetary parsing and arithmetic.
  - Construct `Decimal` values from input strings, never from binary floats.
  - Convert `Decimal` amounts to strings before JSON serialization.
  - Use `pathlib.Path` and standard-library file operations for the shared JSON protocol.

### Query 2: Pytest Temporary Directory Isolation

- Search text: `pytest tmp_path fixture isolated temporary directories filesystem testing coverage`
- Returned library ID: `/pytest-dev/pytest`
- Access date: `2026-06-21`
- Applied insight:
  - Use `tmp_path` to give each test a unique `pathlib.Path` temporary directory.
  - Use `monkeypatch` where needed to redirect current working directory or environment settings during tests.
  - Require unit and integration tests to avoid writing to the repository's real `shared/` and `archive/` directories.

## Domain Source Records

### Local Assignment Context

- Sources:
  - `agent-control/write-spec/transaction-system-brief.md`
  - `sample-transactions.json` safe structural summary
  - `agents.md`
  - `agent-control/write-spec/quality-bar.md`
  - `agent-control/write-spec/stack-profiles.md`
- Applied insight:
  - The generated specification must describe a Python transaction-processing system, not the homework automation harness.
  - Runtime components are Transaction Validator, Fraud Detector, Settlement Processor, and Reporting Agent, with Integrator orchestrating.
  - Product messages move through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
  - Existing `shared/` runtime output is archived before repeat runs.
  - `shared/run-provenance.json` records non-sensitive run traceability.
  - The sample includes valid transactions, high-value transactions, early-hour activity, unsupported currency `XYZ`, and a non-positive amount case.

### ISO 4217 Currency-Code Context

- Source: ISO 4217 currency codes page, `https://www.iso.org/iso-4217-currency-codes.html`.
- Access date: `2026-06-21`.
- Applied insight:
  - Currency codes are represented alphabetically and numerically; common alphabetic examples use three letters such as `USD` and `EUR`.
  - The product spec should say "ISO 4217-style" and use a bounded allow-list for the educational sample rather than claim complete live standards maintenance.

### SIX Maintenance Agency Context

- Source: SIX Financial Data Standards page, `https://www.six-group.com/en/products-services/financial-information/market-reference-data/data-standards.html`.
- Access date: `2026-06-21`.
- Applied insight:
  - SIX is the ISO 4217 Maintenance Agency for currency codes and maintains code lists.
  - The generated system should not claim to maintain the live official ISO currency registry unless a later implementation explicitly integrates an authoritative code-list source.

## Assignment Assumptions

- Accepted sample currencies: `USD`, `EUR`, and `GBP`.
- Unsupported sample currency: `XYZ`.
- Money is serialized as strings in all JSON outputs.
- High-value examples should be routed to review rather than automatically rejected when validation otherwise passes.
- Early-hour activity is an educational risk signal, not a compliance finding.
- Account IDs, descriptions, and raw metadata are sensitive even when the sample is synthetic.
- Audit examples should use transaction IDs and reason codes, not plaintext account IDs or descriptions.

## Generated Design Decisions

- Four runtime components are required for this fresh spec: Transaction Validator, Fraud Detector, Settlement Processor, and Reporting Agent.
- Reporting Agent owns `summary.json`, `pipeline-status.json`, completeness checks, status counts, reason-code counts, and privacy checks on result artifacts.
- Runtime result files should be compatible with later read-only `pipeline-status` MCP tooling:
  - `shared/results/summary.json`
  - `shared/results/pipeline-status.json`
  - `shared/results/TXN*.json`
- Transaction result records should include safe fields such as `transaction_id`, `status`, `reason_codes`, `amount`, `currency`, `component_history_count`, `audit_event_count`, `processed_at`, and a redaction/privacy check marker.
- Tests should isolate all filesystem effects with `tmp_path` and should not mutate canonical runtime evidence.

## Fallback Limitations

- No Context7 query was required for FastMCP because Athena (Spec Writer) only specifies MCP-readable result shapes; later Homework Automation Layer work owns MCP implementation and configuration.
- No live bank compliance, AML, sanctions, KYC, or payment-network research was used because those claims are outside the educational simulation boundary.
- The run did not fetch a complete live ISO 4217 code list into the generated requirements; it intentionally specifies a bounded sample allow-list.

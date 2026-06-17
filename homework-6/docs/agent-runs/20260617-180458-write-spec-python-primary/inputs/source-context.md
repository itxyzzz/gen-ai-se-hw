# Source Context

Run ID: `20260617-180458-write-spec-python-primary`
Stack: `python`

## Assignment Requirements

Homework 6 is a capstone that asks for four meta-agents that create a transaction-processing system from scratch. Agent 1 must produce the full project specification before implementation. The canonical selected `specification.md` must include five required sections: high-level objective, 4-5 mid-level objectives, implementation notes, context, and low-level tasks.

The later system must use a file-based JSON communication protocol through:

```text
shared/
  input/
  processing/
  output/
  results/
```

Agent 2 must implement at least three cooperating pipeline agents: a transaction validator, a fraud detector, and at least one settlement, compliance, or reporting agent. Agent 2 must also use Context7 during code generation and document at least two queries in `research-notes.md`. Agent 3 must add tests, slash-command style workflow files, and a coverage gate that blocks below 80%. Agent 4 must produce reviewer-ready documentation and screenshots, including a README with the student's name.

## Sample Transaction Facts

`sample-transactions.json` contains eight transactions with string amounts, ISO-like uppercase currency codes, account identifiers, descriptions, and metadata:

- `TXN001`: normal USD online transfer for `1500.00`.
- `TXN002`: high-value USD branch wire transfer for `25000.00`.
- `TXN003`: USD transfer for `9999.99`, near the high-value threshold.
- `TXN004`: EUR API transfer at `02:47:00Z` with country `DE`, useful for odd-hour and cross-country signals.
- `TXN005`: very high-value USD branch wire transfer for `75000.00`.
- `TXN006`: invalid sample currency `XYZ`, which must be rejected by ISO 4217-style validation.
- `TXN007`: invalid negative GBP amount `-100.00`, which must be rejected by amount validation.
- `TXN008`: normal USD mobile transfer for `3200.00`.

Account identifiers, descriptions, transaction metadata, and audit details are treated as sensitive. Logs and audit examples should use redacted account forms such as `ACC-****1001`.

## Standing Agent Guide

`agents.md` defines the portable Homework 6 contract:

- Preserve meaningful Agent 1 runs under `docs/agent-runs/`.
- Keep `agents.md` stable; do not regenerate it during individual Agent 1 runs.
- Use `decimal.Decimal` for Python money and never binary floating point.
- Validate currency with ISO 4217-style codes and reject unsupported sample values such as `XYZ`.
- Keep the project framed as an educational simulation, not legal, AML, sanctions, payment-network, or banking-compliance work.
- Add `pipeline-status` MCP configuration only after `mcp/server.py` exists.
- After the first successful run, copy only the selected run's `specification.md` to canonical `specification.md` when it does not already exist.

## Python Stack Decisions

The selected stack is `python`, the default profile. The generated specification must name Python files, functions, commands, tests, coverage tools, and MCP implementation details. Required stack-specific defaults include:

- Money: `decimal.Decimal`.
- Pipeline entry: `integrator.py` with `main()`.
- Agent modules: `agents/transaction_validator.py`, `agents/fraud_detector.py`, and `agents/settlement_processor.py` or `agents/reporting_agent.py`.
- Common agent function: `process_message(message: dict) -> dict`.
- Tests: `pytest` with isolated temporary directories.
- Coverage: `pytest-cov` or `coverage.py`; block below 80%, aim for at least 90%.
- Commands: `python integrator.py`, `python -m pytest`, and `python -m pytest --cov=. --cov-fail-under=80`.
- MCP: Python FastMCP server at `mcp/server.py`.

## Reference Usage

Homework 3 was read for format and quality only: section depth, low-level task-card specificity, domain-scope boundary language, and technical-convention structure. Homework 3 dispute-domain claims are not copied as Homework 6 banking-pipeline research.

Root repository standards were accessible from `..\HOMEWORK_STANDARDS.md` and `..\README.md`. They require the homework branch model, per-step changelog updates, evidence screenshots in later phases, and detailed PR submission narrative.

## Missing References

- `specification-TEMPLATE-hint.md` is absent in this checkout.
- No canonical `specification.md` existed before this generation run.

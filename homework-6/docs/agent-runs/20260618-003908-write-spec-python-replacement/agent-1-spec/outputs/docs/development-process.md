# Development Process

## Purpose

This document gives portable implementation guidance for later Homework Automation Layer agents that consume the generated transaction-system specification. It does not require dev-doc-harness, Superpowers, or hidden chat state.

## Product Implementation Order

1. Read `specification.md`, `sample-transactions.json`, and the run-local support docs.
2. Create the Python package structure and shared JSON protocol helpers.
3. Implement common utilities for Decimal parsing, JSON serialization, redaction, and audit events.
4. Implement Transaction Validator.
5. Implement Fraud Detector.
6. Implement Settlement Processor.
7. Implement `integrator.py` orchestration and result summaries.
8. Add tests with temporary directories.
9. Add future MCP status server only after result shapes exist.
10. Update reviewer-facing documentation after behavior and verification are stable.

## Quality Gates

For each implementation increment:

- Keep product changes in the Generated Transaction System Layer.
- Preserve precise Decimal money semantics.
- Preserve the JSON file protocol through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- Avoid raw account IDs, raw descriptions, tokens, secrets, or unfiltered metadata in logs, audit events, summaries, and docs.
- Add or update tests for changed behavior.
- Run relevant verification commands before claiming completion.

## Verification Commands

Baseline commands expected by the specification:

```powershell
python integrator.py
python -m pytest
python -m pytest --cov=.
```

Athena (Spec Writer)'s temporary ending-context coverage target is 75%. Themis (Test Generator) later owns raising the enforced threshold above 80% and adding the blocking coverage hook.

## Test Data Rules

Tests must copy or load `sample-transactions.json` into temporary directories, not mutate the real checkout's `shared/` outputs. Use pytest fixtures such as `tmp_path` to isolate file-system state.

Required sample assertions:

- All eight sample transactions are accounted for.
- `TXN006` rejects with `UNSUPPORTED_CURRENCY`.
- `TXN007` rejects with `NON_POSITIVE_AMOUNT`.
- `TXN002` and `TXN005` produce high-value review signals.
- `TXN004` produces an odd-hour review signal.
- Audit and result files contain redacted account references only.

## Research Notes For Hephaestus

Hephaestus (Code Generator) must use Context7 during implementation and document at least two queries in canonical `research-notes.md`. Each note should include:

- Search text.
- Returned library ID.
- Access date.
- Applied insight.

Good query targets include Python `pytest`, FastMCP, coverage tooling, and standard JSON/Decimal implementation details when available in Context7.

## MCP Timing

Create `mcp/server.py` only after result file shapes exist. Add `pipeline-status` configuration to `mcp.json` and `.codex/config.toml` only after `mcp/server.py` exists and can read current result files.

## Handoff Discipline

When a later agent pauses, it should record:

- Completed files.
- Verification commands run and results.
- Known gaps.
- Whether privacy/audit checks passed.
- Exact next prompt for a fresh continuation.

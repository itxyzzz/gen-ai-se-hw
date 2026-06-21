# Requested Stack Profile Snapshot

- Requested stack: `python`
- Stack source: `agent-control/write-spec/stack-profiles.md`
- Supported enum: `python`, `java`
- Unsupported value note: `auto` is not supported.

## Python Profile Summary

- Money: `decimal.Decimal`; never binary floating point for transaction amounts.
- JSON: Python standard `json` module with strict safe serialization where applicable.
- Pipeline entry: `integrator.py` with a `main()` function.
- Runtime components: Python modules under `agents/`, including Transaction Validator, Fraud Detector, and Settlement Processor or Reporting Agent. Refreshed generation should target at least four runtime components, with Reporting Agent preferred as the fourth component.
- Common component shape: `process_message(message: dict) -> dict` or stack-equivalent helpers.
- Shared protocol: JSON files through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- Tests: `pytest` with isolated temporary directories.
- Coverage: Athena ending-context target may be 75%; Themis later owns the 80% blocking gate.
- Commands: `python integrator.py`, `python -m pytest`, and coverage commands using pytest/coverage tooling.
- MCP result compatibility: product result files should be readable by the Python FastMCP server at `mcp/server.py`.

## Stack-Invariant Requirements

- Preserve file-based JSON communication and result summaries.
- Account for every sample transaction in final results.
- Archive repeated runtime outputs before creating a fresh `shared/` tree.
- Write safe runtime provenance at `shared/run-provenance.json` when specified.
- Redact account identifiers and avoid raw descriptions in logs, audit records, docs, and evidence.
- Keep the system framed as an educational simulation, not legal, banking, AML, sanctions, KYC, PCI, or payment-network compliance.

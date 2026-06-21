# Development Process

## Purpose

This process describes how later code, test, and documentation generation should consume the preserved Python transaction-system specification. It is portable guidance for the generated product package and does not depend on dev-doc-harness, Superpowers, or hidden thread state.

## Generation Order

1. Read `agent-1-spec/outputs/specification.md`.
2. Read supporting docs:
   - `agent-1-spec/outputs/docs/domain-rules.md`
   - `agent-1-spec/outputs/docs/technical-conventions.md`
   - `agent-1-spec/research-notes.md`
3. Implement Python runtime modules and tests from the low-level task cards.
4. Run `python -m pytest`.
5. Run `python integrator.py` from a controlled workspace.
6. Review `shared/results/summary.json`, `shared/results/pipeline-status.json`, and per-transaction result files.
7. Confirm no raw account IDs, descriptions, full metadata, credentials, or hidden prompts appear in logs/results/docs examples.

## Product Implementation Gates

- Money gate: no use of `float` for amounts.
- Currency gate: `USD`, `EUR`, and `GBP` accepted; `XYZ` rejected.
- Protocol gate: messages pass through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- Rerun gate: prior `shared/` is archived under `archive/shared-###`.
- Provenance gate: `shared/run-provenance.json` contains traceability only.
- Privacy gate: result artifacts pass Reporting Agent privacy checks.
- Completeness gate: every sample transaction is represented in final results.
- Test gate: `python -m pytest` passes with isolated temporary directories.
- Coverage-report gate: non-blocking report shows at least the Athena-stage target of 75% when coverage tooling is available.

## Downstream Research Expectation

Later Python code generation should document at least two Context7 queries in its own research notes. Good query topics include:

- Python `decimal` and strict JSON serialization.
- pytest `tmp_path` or filesystem isolation.
- Any chosen helper library if the implementation adds one.

## Review Checklist

- Low-level task cards remain product implementation slices, not homework automation setup.
- Runtime components are Python modules/classes/functions, not AI skills.
- Output files are safe for future read-only status tools.
- Tests do not mutate canonical `shared/` or `archive/`.
- Documentation states educational simulation limits and avoids unsupported compliance claims.


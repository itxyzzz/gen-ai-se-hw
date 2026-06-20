# Homework 6 Final Capstone: AI-Powered Transaction Processing Pipeline

## Summary

This PR completes Homework 6 with a selected Python transaction-processing system generated through the four required Homework Automation Layer agents:

- Athena (Spec Writer) produced the selected `specification.md`.
- Hephaestus (Code Generator) produced the selected Python runtime pipeline and Context7 research notes.
- Themis (Test Generator) produced the selected test suite and coverage evidence.
- Clio (Documentation Generator) produced the final reviewer documentation, screenshot mapping, and this PR draft.

The runtime system processes the eight synthetic transactions from `sample-transactions.json` through a file-based JSON protocol and writes final safe results under `shared/results/`.

## What The Pipeline Does

- Validates required transaction fields, positive string-based `Decimal` amounts, supported currencies, and timestamps.
- Applies deterministic educational risk scoring for high-value, wire-transfer, odd-hour, channel, and country signals.
- Produces final statuses limited to `settled`, `rejected`, `review_required`, and `error`.
- Archives previous `shared/` output before each normal run.
- Provides read-only MCP status tools and `pipeline://summary`.
- Avoids raw account IDs, descriptions, and unfiltered metadata in reviewer-facing outputs.

This is an educational simulation only and does not perform real banking, legal, compliance, fraud, AML, KYC, PCI, settlement, sanctions, or payment-network determinations.

## Verification

Fresh Clio evidence from `docs/agent-runs/20260620-230201-generate-docs-python-primary/agent-4-docs/evidence/`:

| Command | Result |
|---|---|
| `python integrator.py` | `total=8 settled=2 rejected=2 review_required=4 error=0` |
| `python -m pytest -p no:cacheprovider` | 50 passed |
| `python scripts/check_coverage_gate.py --fail-under 80` | 50 passed, 94.79% total coverage |
| `python scripts/check_coverage_gate.py --fail-under 99` | 50 passed, exits nonzero as expected because 94.79% is below 99% |
| Validation-only helper | 8 total, 6 valid, 2 rejected |
| MCP status helper | `TXN006` rejected with `UNSUPPORTED_CURRENCY`; summary counts match pipeline output |

The first sandboxed coverage run hit a Windows coverage-file rename permission error. The same helper passed unsandboxed, which is recorded in the Clio evidence.

## Screenshots

Embed or link these screenshots in the GitHub PR body:

![Pipeline run](docs/screenshots/pipeline-run.png)

![Test coverage](docs/screenshots/test-coverage.png)

![Run pipeline skill](docs/screenshots/skill-run-pipeline.png)

![Hook trigger](docs/screenshots/hook-trigger.png)

![MCP interaction](docs/screenshots/mcp-interaction.png)

Original source screenshots remain preserved under `docs/screenshots/operator-sourced/`.

## Reviewer Run Instructions

```powershell
cd homework-6
python integrator.py
python -m pytest -p no:cacheprovider
python scripts/check_coverage_gate.py --fail-under 80
```

Optional blocking-path demonstration:

```powershell
python scripts/check_coverage_gate.py --fail-under 99
```

Optional MCP helper check by file path:

```powershell
python -c "import importlib.util; from pathlib import Path; p=Path('mcp/server.py').resolve(); spec=importlib.util.spec_from_file_location('pipeline_status_server', p); mod=importlib.util.module_from_spec(spec); spec.loader.exec_module(mod); print(mod.build_summary_text())"
```

## Documentation

- `README.md`
- `HOWTORUN.md`
- `ARCHITECTURE.md`
- `TESTING_GUIDE.md`
- `API_REFERENCE.md`
- `docs/pr-description-draft.md`

## Known Limitations

- The system is local and file-based by design.
- The MCP server reads current result files and does not run or mutate the pipeline.
- Some stable screenshots were captured before the current root suite reached 50 tests; fresh Clio text evidence records the current 50-test, 94.79% coverage result.
- In one-off Python imports, use a file-path import for `mcp/server.py` to avoid resolving the installed third-party `mcp` package.

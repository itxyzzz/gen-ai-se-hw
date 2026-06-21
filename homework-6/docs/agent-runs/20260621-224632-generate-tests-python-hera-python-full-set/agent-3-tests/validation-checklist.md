# Validation Checklist

## Run Identity

- Run ID: `20260621-224632-generate-tests-python-hera-python-full-set`
- Stack: `python`
- Mode: `generate`
- Parent Hera run ID: `20260621-215717-orchestrate-runs-python-full-set`
- Target Hephaestus run ID: `20260621-222543-generate-code-python-hera-python-full-set`
- Target Hephaestus inventory: `docs/agent-runs/20260621-222543-generate-code-python-hera-python-full-set/agent-2-code/outputs/inventory.md`
- Status: pass with hook-shell environment limitation.

## Traceability Gates

| Check | Status | Evidence |
|---|---:|---|
| Named Hephaestus inventory used, no "latest" targeting | Pass | `inputs/selected-code-inventory.snapshot.md` and `run-metadata.md` |
| Source Athena run and spec fingerprint recorded | Pass | `run-metadata.md` |
| Current canonical spec fingerprint recorded for comparison | Pass | `run-metadata.md` |
| Source spec mismatch reported | Pass | Candidate spec `6F2E...2222` differs from canonical `44FD...E3B`. |
| First-level Hera child status recorded | Pass | `run-metadata.md` and `handoff.md` |
| Nested sub-agent status recorded | Pass | No nested sub-agents used; no degraded nested dispatch observed. |

## Output And Workspace Gates

| Check | Status | Evidence |
|---|---:|---|
| Run folder created under `docs/agent-runs/` | Pass | `20260621-224632-generate-tests-python-hera-python-full-set/` |
| `workspace/selected-code/` contains inventory-declared Hephaestus package copy | Pass | Runtime evidence paths were excluded. |
| Candidate tests/config written only under `agent-3-tests/outputs/` | Pass | `tests/test_themis_quality.py` |
| `workspace/project-under-test/` rebuilt by overlaying outputs | Pass | Candidate test appears in run-local workspace only. |
| Output inventory lists selectable files and exclusions | Pass | `agent-3-tests/outputs/inventory.md` |

## Test Quality Gates

| Required coverage area | Status | Evidence |
|---|---:|---|
| Transaction Validator unit coverage | Pass | Baseline tests plus `test_validator_reports_multiple_safe_reason_codes`, `test_validator_process_message_adds_history_and_safe_audit`, and validation-only helper tests. |
| Fraud Detector unit coverage | Pass | Baseline tests plus rejected-message, channel-pattern, and wire-modifier tests. |
| Settlement Processor unit coverage | Pass | Baseline tests plus rejected, unknown-status, and `process_message` audit tests. |
| Reporting Agent unit coverage | Pass | Baseline tests plus status variants, error result, incomplete summary, pipeline status, and privacy failure tests. |
| Integrator/full pipeline coverage | Pass | Baseline tests plus custom shared-dir, provenance, bad-message recovery, archival, full pipeline, and validation-only tests. |
| Dry-run validation behavior | Pass | `validate_transactions_only` and `validate_transactions_file` covered. |
| Privacy and audit checks | Pass | Result/evidence privacy scan found no raw account IDs or sample descriptions in runtime result JSON. |
| Fixture isolation | Pass | Tests use `tmp_path` and run-local workspace; root `shared/` was not used. |
| Repeated-run archival/provenance | Pass | Tests assert archive creation and distinct runtime run IDs; provenance is safe. |

## Validation Commands

| Command | Status | Actual result |
|---|---:|---|
| `python -m pytest -p no:cacheprovider` | Pass | `36 passed in 5.56s`. |
| `python scripts\check_coverage_gate.py --stack python --fail-under 80` | Pass | `36 passed`; total coverage `97.44%`. |
| `python scripts\check_coverage_gate.py --stack python --fail-under 99` | Expected fail | Tests passed, helper exited `1` because `97.44%` is below `99%`. |
| Documented `/run-pipeline` fast path | Pass | `total=8 settled=2 rejected=2 review_required=4 error=0 result_count=8 all_present=true`. |
| Documented `/validate-transactions` fast path | Pass | `total=8 valid=6 invalid=2`; reason groups `UNSUPPORTED_CURRENCY=1`, `NON_POSITIVE_AMOUNT=1`. |
| Run-local `.githooks/pre-push` shell invocation via Bash | Environment blocked | Bash failed with `E_ACCESSDENIED` in this Windows sandbox. |
| Run-local `.githooks/pre-push` shell invocation via `sh` | Environment blocked | `sh` was not available in PowerShell. |

## Privacy Scan

| Scope | Status | Evidence |
|---|---:|---|
| `shared/results/*.json` raw account IDs | Pass | No `ACC-[0-9]{4,}` matches. |
| `shared/results/*.json` sample descriptions | Pass | No sample description matches. |
| Command evidence | Pass | Counts, transaction IDs, statuses, and reason codes only. |

## Scope Checks

| Check | Status |
|---|---:|
| Root runtime product code unchanged | Pass |
| Root tests unchanged | Pass |
| Root `shared/` unchanged by Themis validation | Pass |
| Root `.coverage` unchanged by Themis validation | Pass |
| Canonical docs unchanged | Pass |
| `mcp/`, `mcp.json`, final selection, and selection registry unchanged | Pass |
| No command, hook, or MCP support surfaces regenerated as candidate outputs | Pass |

## Known Limitations

- Direct Git hook shell execution could not run in this Windows sandbox because Bash access was denied and `sh` was unavailable. The hook's delegated coverage-helper behavior was validated with pass and fail paths inside the run-local workspace.
- This run validates the named Hera candidate package only. Because its source spec fingerprint differs from the current canonical spec fingerprint, it does not claim coverage of the current canonical package set.


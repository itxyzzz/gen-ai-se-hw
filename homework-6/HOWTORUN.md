# How To Run Homework 6

This runbook assumes PowerShell from the `homework-6` folder.

## 1. Confirm Prerequisites

```powershell
python --version
python -m pytest --version
```

Expected local evidence used for this documentation:

- Python 3.12.6
- pytest 8.4.2
- pytest-cov 7.1.0

## 2. Run The Pipeline

```powershell
python integrator.py
```

Expected summary:

```text
Pipeline complete: total=8 settled=2 rejected=2 review_required=4 error=0
```

The run writes:

- `shared/input/*.json`
- `shared/processing/*.json`
- `shared/output/*.json`
- `shared/results/TXN*.json`
- `shared/results/summary.json`
- `shared/results/pipeline-status.json`
- `shared/run-provenance.json`

If `shared/` already exists, the integrator preserves the previous run under `archive/shared-NNN` before creating fresh protocol folders.

## 3. Inspect Safe Result Counts

```powershell
Get-Content -Raw shared/results/summary.json | ConvertFrom-Json
```

Reviewer-safe expected counts:

| Field | Expected |
|---|---:|
| `total_transactions` | 8 |
| `settled` | 2 |
| `review_required` | 4 |
| `rejected` | 2 |
| `error` | 0 |

Reviewer-facing evidence should stay limited to transaction IDs, statuses, counts, and reason codes.

## 4. Run Validation-Only Mode

Use the validator dry-run helper when you want structural validation without risk scoring or settlement:

```powershell
python -c "import json; from collections import Counter; from agents.transaction_validator import validate_transactions_file; r=validate_transactions_file('sample-transactions.json'); safe={'total': r['total'], 'valid': r['valid'], 'invalid': r['rejected'], 'reason_code_groups': dict(Counter(code for item in r['results'] for code in item['reason_codes'])), 'results': [{'transaction_id': item['transaction_id'], 'status': item['status'], 'reason_codes': item['reason_codes']} for item in r['results']]}; print(json.dumps(safe, indent=2))"
```

Expected summary:

- Total: 8
- Valid: 6
- Invalid: 2
- Invalid reason codes: `UNSUPPORTED_CURRENCY`, `NON_POSITIVE_AMOUNT`

## 5. Run Tests

```powershell
python -m pytest -p no:cacheprovider
```

Current Clio evidence:

```text
50 passed in 6.70s
```

## 6. Run The Coverage Gate

```powershell
python scripts/check_coverage_gate.py --fail-under 80
```

Expected passing signal:

```text
Required test coverage of 80% reached. Total coverage: 94.79%
```

On this Windows Codex sandbox, the first sandboxed coverage run can fail during coverage-file rename inside `tmp/coverage-gate-*`. The same command passed when rerun unsandboxed, which is recorded in the Clio evidence.

## 7. Demonstrate The Blocking Path

```powershell
python scripts/check_coverage_gate.py --fail-under 99
```

Expected result:

- Tests still pass.
- The command exits nonzero because total coverage is below 99%.
- This demonstrates the gate blocking behavior without weakening the real 80% threshold.

## 8. Use The Operation Skills Or Commands

Codex skills:

- `.agents/skills/run-pipeline/SKILL.md`
- `.agents/skills/validate-transactions/SKILL.md`

Claude Code command wrappers:

- `.claude/commands/run-pipeline.md`
- `.claude/commands/validate-transactions.md`

The shared behavior is documented in:

- `agent-control/operate-pipeline/commands-and-hooks.md`

Both operation paths should summarize safe counts and reason codes only.

## 9. Check The Git Hook

The pre-push hook lives at:

```text
.githooks/pre-push
```

It runs:

```powershell
python scripts/check_coverage_gate.py --fail-under 80
```

If your clone does not already use the homework hook path, configure it from the repository root:

```powershell
git config core.hooksPath homework-6/.githooks
```

## 10. Use The MCP Status Server

`mcp.json` configures both MCP servers:

```json
{
  "mcpServers": {
    "context7": {
      "command": "npx",
      "args": ["-y", "@upstash/context7-mcp@latest"]
    },
    "pipeline-status": {
      "command": "python",
      "args": ["mcp/server.py"]
    }
  }
}
```

The custom server exposes:

- Tool `get_transaction_status(transaction_id: str)`
- Tool `list_pipeline_results()`
- Resource `pipeline://summary`

If importing helpers from a Python one-liner, load `mcp/server.py` by file path to avoid resolving the installed third-party `mcp` package.

## 11. Review Screenshots

Stable reviewer-facing screenshots:

- `docs/screenshots/pipeline-run.png`
- `docs/screenshots/test-coverage.png`
- `docs/screenshots/skill-run-pipeline.png`
- `docs/screenshots/hook-trigger.png`
- `docs/screenshots/mcp-interaction.png`

Original operator-sourced screenshots remain preserved under:

- `docs/screenshots/operator-sourced/`

## 12. Cleanup

Runtime evidence can be regenerated at any time:

```powershell
python integrator.py
```

Normal pipeline runs archive prior `shared/` output rather than deleting it. Do not remove `docs/agent-runs/`, `docs/screenshots/operator-sourced/`, or selected inventories; they are submission evidence.

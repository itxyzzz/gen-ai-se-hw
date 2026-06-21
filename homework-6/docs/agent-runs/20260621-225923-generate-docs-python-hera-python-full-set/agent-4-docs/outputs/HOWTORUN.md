# How To Run The Fresh Python Candidate

This guide describes the preserved candidate package generated for Hera package-set review. It does not replace the current canonical Homework 6 package unless a later selection step copies the inventory-declared outputs.

## 1. Locate Or Materialize The Candidate Package

The source candidate code lives under:

```text
docs/agent-runs/20260621-222543-generate-code-python-hera-python-full-set/agent-2-code/outputs/
```

The candidate test expansion lives under:

```text
docs/agent-runs/20260621-224632-generate-tests-python-hera-python-full-set/agent-3-tests/outputs/
```

For review, use a temporary workspace that copies the Hephaestus output package and overlays Themis outputs. Clio validated this shape in:

```text
C:\Users\TANATA~1\AppData\Local\Temp\clio-20260621-225923-generate-docs-python-hera-python-full-set
```

## 2. Run The Pipeline

From a candidate workspace:

```powershell
python integrator.py
```

Expected signal:

```text
Pipeline complete: total=8 settled=2 rejected=2 review_required=4 error=0
```

The run creates:

```text
shared/input/
shared/processing/
shared/output/
shared/results/
```

Result files are safe reviewer evidence. They include transaction IDs, statuses, counts, risk fields, reason codes, and sanitized summaries.

## 3. Run The Validation-Only Check

```powershell
python -c "import json; from collections import Counter; from agents.transaction_validator import validate_transactions_file; r=validate_transactions_file('sample-transactions.json'); safe={'total': r['total'], 'valid': r['valid'], 'invalid': r['rejected'], 'reason_code_groups': dict(Counter(code for item in r['results'] for code in item['reason_codes'])), 'results': [{'transaction_id': item['transaction_id'], 'status': item['status'], 'reason_codes': item['reason_codes']} for item in r['results']]}; print(json.dumps(safe, indent=2))"
```

Expected summary:

- total: 8
- valid: 6
- invalid: 2
- reason-code groups: `UNSUPPORTED_CURRENCY=1`, `NON_POSITIVE_AMOUNT=1`

## 4. Run Tests

```powershell
python -m pytest -p no:cacheprovider
```

Fresh Clio result:

```text
36 passed
```

## 5. Run The Coverage Gate

```powershell
python scripts\check_coverage_gate.py --stack python --fail-under 80
```

Fresh Clio result:

```text
Required test coverage of 80% reached. Total coverage: 97.44%
36 passed
```

To demonstrate the blocking path without changing the real threshold:

```powershell
python scripts\check_coverage_gate.py --stack python --fail-under 99
```

Expected signal:

```text
FAIL Required test coverage of 99% not reached. Total coverage: 97.44%
```

## 6. Review The Hook

The pre-push hook delegates to:

```powershell
python scripts\check_coverage_gate.py --stack auto --fail-under 80
```

In this Windows sandbox, direct shell execution of the run-local hook was blocked because Bash returned access denied and `sh` was unavailable. The same coverage helper was validated with both the real 80% pass path and the deliberate 99% failure path.

## 7. Review MCP Status Behavior

The MCP configuration includes both servers:

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

For this candidate, Clio validated the existing `mcp/server.py` helpers by file-path importing the module and pointing it at candidate `shared/results/`.

Observed safe signals:

- `list_pipeline_results`: total 8, settled 2, rejected 2, review-required 4, error 0.
- `get_transaction_status("TXN006")`: rejected with `UNSUPPORTED_CURRENCY`.
- `pipeline://summary`: returned safe aggregate status text.

## 8. Review Screenshots

Candidate screenshots are under:

```text
docs/screenshots/
```

Required images:

- `pipeline-run.png`
- `test-coverage.png`
- `skill-run-pipeline.png`
- `hook-trigger.png`
- `mcp-interaction.png`

The preserved operator-sourced screenshot folder remains untouched at root `docs/screenshots/operator-sourced/`.

## 9. Troubleshooting

| Symptom | Likely cause | Action |
|---|---|---|
| `sample-transactions.json` missing | Candidate workspace was not fully materialized | Copy the fixture from the Hephaestus candidate package. |
| Coverage helper missing | Operator support file was not copied into the candidate workspace | Copy `scripts/check_coverage_gate.py` from root support surfaces. |
| MCP subprocess reads canonical root results | `mcp.json` starts `mcp/server.py` from the root project | For candidate validation, import `mcp/server.py` by file path and pass the candidate results directory to helper functions. |
| Direct hook shell execution fails on Windows | Bash or `sh` unavailable or blocked | Use the coverage helper pass/fail commands as hook behavior evidence. |

## 10. Cleanup

Temporary validation workspaces can be removed after evidence is preserved. Do not delete preserved run folders under `docs/agent-runs/` or source screenshots under `docs/screenshots/operator-sourced/`.

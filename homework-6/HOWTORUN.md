# How To Run The Canonical Python Package

This guide describes the selected canonical Python package generated through Hera package-set run `20260621-215717-orchestrate-runs-python-full-set` and selected through Hera `select-set` run `20260622-000718-orchestrate-runs-python-select-latest`.

## 1. Confirm The Selected Package

The canonical root files are selected from these preserved runs:

```text
Athena:     docs/agent-runs/20260621-220037-write-spec-python-hera-python-full-set/
Hephaestus: docs/agent-runs/20260621-222543-generate-code-python-hera-python-full-set/
Themis:     docs/agent-runs/20260621-224632-generate-tests-python-hera-python-full-set/
Clio:       docs/agent-runs/20260621-225923-generate-docs-python-hera-python-full-set/
```

`docs/agent-runs/selection-sets.json` records `python-canonical-20260622-hera-full-set` as the canonical set. Earlier Python runs and the Java alternate remain preserved under `docs/agent-runs/`.

## 2. Run The Pipeline

From the homework root:

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

## 7. Review Stack-Aware Support

The root package is Python, but the support layer records both stack families:

```powershell
python scripts\check_coverage_gate.py --stack python --fail-under 80
python scripts\check_coverage_gate.py --stack java --project-dir <java-package> --fail-under 80
```

The preserved Java package set is available at:

```text
docs/agent-runs/20260621-180512-orchestrate-runs-java-full-set/
```

It is evidence for the alternate stack and uses Maven, JUnit, JaCoCo, Jackson, and `BigDecimal`. It is not copied into the canonical root package.

## 8. Review MCP Status Behavior

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

For this selected package, `mcp/server.py` reads root `shared/results/` through the configured `pipeline-status` server. Clio also validated helper compatibility against the run-local candidate results before selection.

Observed safe signals:

- `list_pipeline_results`: total 8, settled 2, rejected 2, review-required 4, error 0.
- `get_transaction_status("TXN006")`: rejected with `UNSUPPORTED_CURRENCY`.
- `pipeline://summary`: returned safe aggregate status text.

## 9. Review Screenshots

Selected stable screenshots are under:

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

That source folder contains the full manual evidence set, including Java orchestration screenshots (`120` through `132`) and the Python orchestration handoff screenshot (`140`). The stable reviewer screenshots above are the selected subset used by the documentation and PR draft.

## 10. Troubleshooting

| Symptom | Likely cause | Action |
|---|---|---|
| `sample-transactions.json` missing | The homework root is incomplete | Restore the canonical fixture from git or the selected Hephaestus run. |
| Coverage helper missing | Operator support files are incomplete | Restore `scripts/check_coverage_gate.py` from the root support surfaces. |
| MCP subprocess has no results | The pipeline has not been run yet | Run `python integrator.py`, then inspect `shared/results/`. |
| Direct hook shell execution fails on Windows | Bash or `sh` unavailable or blocked | Use the coverage helper pass/fail commands as hook behavior evidence. |

## 11. Cleanup

Temporary validation workspaces can be removed after evidence is preserved. Do not delete preserved run folders under `docs/agent-runs/` or source screenshots under `docs/screenshots/operator-sourced/`.

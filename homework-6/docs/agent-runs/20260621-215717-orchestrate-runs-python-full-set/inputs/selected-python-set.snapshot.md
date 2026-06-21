# Selected Python Set Snapshot

- Snapshot purpose: protect the current canonical Python package while creating a fresh preserved Python candidate set.
- Canonical package-set ID: `python-canonical-20260621`
- Stack: `python`
- Registry path: `docs/agent-runs/selection-sets.json`
- Human selection record: `docs/agent-runs/final-selection.md`

## Selected Runs

| Role | Selected run |
|---|---|
| Athena (Spec Writer) | `20260619-170102-write-spec-python-fresh` |
| Hephaestus (Code Generator) | `20260619-175211-generate-code-python-fresh-spec` |
| Themis (Test Generator) | `20260620-144025-generate-tests-python-fresh-spec` |
| Clio (Documentation Generator) | `20260621-011348-generate-docs-python-review-repair` |

## Selected Inventories

| Package | Inventory |
|---|---|
| Code | `docs/agent-runs/20260619-175211-generate-code-python-fresh-spec/agent-2-code/outputs/inventory.md` |
| Tests | `docs/agent-runs/20260620-144025-generate-tests-python-fresh-spec/agent-3-tests/outputs/inventory.md` |
| Documentation | `docs/agent-runs/20260621-011348-generate-docs-python-review-repair/agent-4-docs/outputs/inventory.md` |

## Command Hints

- Pipeline: `python integrator.py`
- Tests: `python -m pytest -p no:cacheprovider`
- Coverage gate: `python scripts/check_coverage_gate.py --stack python --fail-under 80`
- Result contract: `shared/results/summary.json`, `shared/results/TXN*.json`, and `mcp/server.py`

## Protection Note

This Hera `generate-set` run is not authorized to replace this canonical set. Any replacement requires a later explicit `select-set` instruction naming the candidate set or child runs and using inventory-declared copy targets.

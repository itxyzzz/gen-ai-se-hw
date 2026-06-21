# Selected Python Set Snapshot

- Snapshot purpose: protect the current canonical Python package set while generating a preserved Java candidate set.
- Canonical package-set ID: `python-canonical-20260621`
- Registry path: `docs/agent-runs/selection-sets.json`
- Human selection path: `docs/agent-runs/final-selection.md`
- Stack: `python`
- Status: canonical

Selected runs:

- Athena (Spec Writer): `20260619-170102-write-spec-python-fresh`
- Hephaestus (Code Generator): `20260619-175211-generate-code-python-fresh-spec`
- Themis (Test Generator): `20260620-144025-generate-tests-python-fresh-spec`
- Clio (Documentation Generator): `20260621-011348-generate-docs-python-review-repair`

Selected inventories:

- Code: `docs/agent-runs/20260619-175211-generate-code-python-fresh-spec/agent-2-code/outputs/inventory.md`
- Tests: `docs/agent-runs/20260620-144025-generate-tests-python-fresh-spec/agent-3-tests/outputs/inventory.md`
- Documentation: `docs/agent-runs/20260621-011348-generate-docs-python-review-repair/agent-4-docs/outputs/inventory.md`

Protection rule: this Hera `generate-set` run does not authorize changing the canonical Python set, canonical root outputs, or `canonical_set_id`.


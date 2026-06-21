# Hera Selection Plan

Status: executed with explicit operator authorization.

## Selected Package Set

- Package-set ID: `python-canonical-20260622-hera-full-set`
- Source Hera generate-set run: `20260621-215717-orchestrate-runs-python-full-set`
- Athena: `20260621-220037-write-spec-python-hera-python-full-set`
- Hephaestus: `20260621-222543-generate-code-python-hera-python-full-set`
- Themis: `20260621-224632-generate-tests-python-hera-python-full-set`
- Clio: `20260621-225923-generate-docs-python-hera-python-full-set`

## Canonical Copy Actions

- Copied Athena `agent-1-spec/outputs/specification.md` to `specification.md`.
- Copied Hephaestus inventory-declared Python runtime files, baseline tests, pytest config, and `research-notes.md`.
- Copied Themis inventory-declared `tests/test_themis_quality.py`.
- Copied Clio inventory-declared reviewer docs, PR draft, and stable screenshots.
- Updated selected docs after copy to use canonical root paths and include multi-stack evidence, historical preservation, Java alternate evidence, full operator-sourced screenshot evidence, and the operator-provided PR workflow/challenge narrative.
- Updated `docs/agent-runs/final-selection.md` and `docs/agent-runs/selection-sets.json`.

## Exclusions

- Did not copy Java source, Java tests, Java docs, or Java screenshots to root canonical paths.
- Did not copy run-local `shared/`, `archive/`, evidence folders, review folders, caches, `.coverage*`, `.pytest_cache/`, or `__pycache__/`.
- Did not change `mcp/server.py`, `mcp.json`, `.codex/config.toml`, support commands, or hooks.

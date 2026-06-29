# Hera Select-Set Run Metadata

- Run ID: `20260622-000718-orchestrate-runs-python-select-latest`
- Mode: `select-set`
- Stack: `python`
- Start time: 2026-06-22 00:07 Europe/Budapest
- Orchestration tool: Codex Desktop, Hera (Orchestrator) skill
- Operator instruction: commit newly added operator-sourced screenshots, then select the latest Python package as canonical; adjust documentation paths; document stack-aware Python/Java support, historical evidence preservation, Java alternate evidence, full screenshot evidence, and PR workflow/challenge notes.
- Selection authorized: yes, explicit operator request to select the latest Python run as canonical.
- Previous canonical package-set ID: `python-canonical-20260621`
- New canonical package-set ID: `python-canonical-20260622-hera-full-set`
- Source Hera generate-set run: `20260621-215717-orchestrate-runs-python-full-set`
- Source package-set proposal: `python-candidate-20260621-hera-full-set`

## Selected Child Runs

| Role | Run ID | Inventory or output |
|---|---|---|
| Athena (Spec Writer) | `20260621-220037-write-spec-python-hera-python-full-set` | `agent-1-spec/outputs/specification.md` |
| Hephaestus (Code Generator) | `20260621-222543-generate-code-python-hera-python-full-set` | `agent-2-code/outputs/inventory.md` |
| Themis (Test Generator) | `20260621-224632-generate-tests-python-hera-python-full-set` | `agent-3-tests/outputs/inventory.md` |
| Clio (Documentation Generator) | `20260621-225923-generate-docs-python-hera-python-full-set` | `agent-4-docs/outputs/inventory.md` |

## Canonical Output Policy

This run is authorized to replace the canonical Python package using inventory-declared targets. It also registers Java package set `java-candidate-20260621-180512` as preserved alternate evidence without changing the canonical stack.

## Pre-Existing Dirty State

Before selection, the worktree contained an existing modified `CHANGELOG.md` entry from the prior Hera comparison run and untracked latest Python run folders. The operator-sourced screenshots were committed first in commit `c32277a` so screenshot evidence was preserved before selection.

## Agent Config

`.codex/config.toml` records `agents.max_threads = 8` and `agents.max_depth = 2`.

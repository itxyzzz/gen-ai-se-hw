# Source Context

## Required Context Read

- Repository standards: `../HOMEWORK_STANDARDS.md`
- Repository README: `../README.md`
- Homework assignment: `TASKS.md`
- Homework agent guide: `agents.md`
- Canonical selected specification: `specification.md`
- Canonical sample input: `sample-transactions.json`
- Selection record: `docs/agent-runs/final-selection.md`
- Themis workflow: `agent-control/generate-tests/workflow.md`
- Themis quality bar: `agent-control/generate-tests/quality-bar.md`
- Themis run registry: `agent-control/generate-tests/run-registry.md`
- Pipeline support behavior: `agent-control/operate-pipeline/commands-and-hooks.md`
- Target Hephaestus metadata: `docs/agent-runs/20260619-175211-generate-code-python-fresh-spec/run-metadata.md`
- Target Hephaestus inventory: `docs/agent-runs/20260619-175211-generate-code-python-fresh-spec/agent-2-code/outputs/inventory.md`
- Target Hephaestus validation checklist: `docs/agent-runs/20260619-175211-generate-code-python-fresh-spec/agent-2-code/validation-checklist.md`
- Target Hephaestus handoff: `docs/agent-runs/20260619-175211-generate-code-python-fresh-spec/agent-2-code/handoff.md`
- Target Hephaestus runtime source files under `agent-2-code/outputs/`
- Target Hephaestus baseline tests under `agent-2-code/outputs/tests/`
- Coverage support helper: `scripts/check_coverage_gate.py`
- Changelog: `CHANGELOG.md`

## Target Selection

The current canonical Hephaestus run in `final-selection.md` was `20260618-223217-generate-code-python-primary`, which was generated from the prior Athena (Spec Writer) source run.

The latest preserved non-canonical Hephaestus run found in `docs/agent-runs/` was `20260619-175211-generate-code-python-fresh-spec`. It targets the current canonical Athena run `20260619-170102-write-spec-python-fresh` and was selected as the named Themis target because the operator explicitly requested "latest generated code version (not the current canonical)."

## Source Spec Fingerprints

- Target Hephaestus source spec SHA-256: `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B`
- Current canonical `specification.md` SHA-256: `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B`
- Mismatch: none.

## Support Scope

Themis validated `/run-pipeline`, `/validate-transactions`, and the coverage gate through compact evidence. It did not regenerate those Operator Layer support surfaces and did not implement Task 4 MCP server/config or Clio (Documentation Generator) docs/screenshots.

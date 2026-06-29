# Hera Selection Plan

Status: proposal only. This file is not authorization to copy canonical files.

This `generate-set` run is authorized to create and preserve a fresh Python candidate package set through child Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator) runs.

Selection is not authorized in the current operator instruction. If the fresh Python set is later selected, the operator must give a separate explicit `select-set` instruction naming this Hera run or the final candidate package-set ID, and Hera must use child inventories to update canonical targets, `docs/agent-runs/final-selection.md`, and `docs/agent-runs/selection-sets.json`.

## Candidate Package Set

- Proposed candidate package-set ID: `python-candidate-20260621-hera-full-set`
- Athena run: `20260621-220037-write-spec-python-hera-python-full-set`
- Athena selected output path if later selected: `docs/agent-runs/20260621-220037-write-spec-python-hera-python-full-set/agent-1-spec/outputs/specification.md`
- Athena spec SHA-256: `6F2E8CD844884DF06172EEB1CF92A2956BC45425FE514B7A820ABAEA249D2222`
- Hephaestus run: `20260621-222543-generate-code-python-hera-python-full-set`
- Hephaestus inventory: `docs/agent-runs/20260621-222543-generate-code-python-hera-python-full-set/agent-2-code/outputs/inventory.md`
- Hephaestus inventory SHA-256: `CB19F97C1ED372905A764F5735EC6E3D0BDA6F1BEF132B723E258770B99C30DF`
- Themis run: `20260621-224632-generate-tests-python-hera-python-full-set`
- Themis inventory: `docs/agent-runs/20260621-224632-generate-tests-python-hera-python-full-set/agent-3-tests/outputs/inventory.md`
- Themis inventory SHA-256: `FD3AFE4F20B5DBB697E536535A1706994BD6D1383CBF8C03B20056A5FC7761C4`
- Clio run: `20260621-225923-generate-docs-python-hera-python-full-set`
- Clio inventory: `docs/agent-runs/20260621-225923-generate-docs-python-hera-python-full-set/agent-4-docs/outputs/inventory.md`
- Clio inventory SHA-256: `3FB246233BB02BCE9A66F8FE88AFC2C2119A851F0EC82D27A8A0603673E56B89`

## Required Pre-Selection Checks

- All child runs completed as first-level child agents or recorded a blocker.
- Each child run has run metadata, source context, validation checklist, inventory or output package, and handoff.
- Hephaestus Context7 evidence includes at least two query records.
- Themis coverage gate is at least 80% and command/hook evidence is preserved.
- Clio documentation and screenshot evidence are complete and privacy-safe.
- No root canonical product, test, documentation, screenshot, MCP, selection-registry, or final-selection files changed during preservation.
- Selection inventories declare every canonical target and runtime/tool exclusions.

## Known Selection Risks To Review

- The fresh candidate spec differs from the current canonical spec by design. Selection would replace the current canonical Python set, so it requires explicit operator confirmation.
- Hephaestus recorded Windows sandbox limitations: sandboxed pytest temp-directory access failed, while unsandboxed pytest passed.
- Themis and Clio recorded that direct run-local hook shell execution was blocked by the Windows sandbox, while coverage-helper pass and fail paths were validated.
- Clio recorded that candidate MCP compatibility was validated by file-path importing root `mcp/server.py` against candidate result files because the normal `mcp.json` subprocess reads root `shared/results/`.
- Clio candidate screenshots are generated terminal-style evidence PNGs, not literal terminal-window captures.

## Explicit Selection Prompt

To select this set later, use a separate instruction such as:

```text
Invoke Hera select-set for package set python-candidate-20260621-hera-full-set from run 20260621-215717-orchestrate-runs-python-full-set, replacing the current canonical Python set using inventory-declared targets.
```

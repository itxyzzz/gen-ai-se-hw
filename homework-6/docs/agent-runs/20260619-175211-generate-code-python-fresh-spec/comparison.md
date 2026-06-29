# Hephaestus Comparison

This generate-mode run produced a fresh candidate for the latest canonical Athena (Spec Writer) specification. It was compared against the existing selected Hephaestus run only to clarify selection status; canonical product files were not replaced.

## Candidates

| Run | Source spec | Status |
|---|---|---|
| `20260618-223217-generate-code-python-primary` | `20260618-003908-write-spec-python-replacement` | Current selected Task 2 software version |
| `20260619-175211-generate-code-python-fresh-spec` | `20260619-170102-write-spec-python-fresh` | Fresh validated candidate, not selected |

## Findings

- The fresh candidate matches the latest canonical spec SHA-256 `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B`; the prior selected code package traces to the older spec SHA-256 `B08B8D365070DAD1F6A9BFE36F2D21951802CF0A284615706F17086E961DDD06`.
- The fresh candidate writes `shared/run-provenance.json` with source spec and pipeline inventory references.
- The fresh candidate produces exact expected sample counts: settled 2, rejected 2, review-required 4, error 0.
- The fresh candidate keeps Task 2 scope: no command wrappers, hooks, custom MCP server, MCP config changes, README, HOWTORUN, screenshots, or PR packaging.
- The fresh candidate contains a complete selectable `agent-2-code/outputs/` package and inventory.

## Recommendation

Select `20260619-175211-generate-code-python-fresh-spec` when the operator is ready to replace the older selected Task 2 package. Selection should remove the prior inventory-declared canonical targets, copy this run's inventory-declared files, update `docs/agent-runs/final-selection.md`, and update canonical `research-notes.md`.


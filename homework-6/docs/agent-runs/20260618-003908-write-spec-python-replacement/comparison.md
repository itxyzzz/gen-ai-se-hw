# Run Comparison

Selected run: `20260618-003908-write-spec-python-replacement`

Comparison date: 2026-06-18

## Compared Runs

| Run ID | Stack | Status | Selection outcome |
|---|---|---|---|
| `20260618-003908-write-spec-python-replacement` | `python` | Passing replacement run | Selected as canonical `specification.md`. |
| `20260617-180458-write-spec-python-primary` | `python` | Failed / superseded | Preserved as evidence only; not selected for downstream use. |

## Selection Criteria

| Criterion | Replacement run evidence | Prior run outcome |
|---|---|---|
| Completeness | Contains the required Task 1 sections and full run package, including validation checklist and handoff. | Initially selected, then marked failed/superseded. |
| Stack specificity | Names Python files, functions, commands, `pytest`, coverage tooling, `decimal.Decimal`, and `mcp/server.py` result-reader expectations. | Python-specific, but aimed low-level tasks at the wrong layer. |
| Research provenance | Includes domain/source notes, local assignment context, and recorded Context7 limitation for Athena (Spec Writer). | Preserved, but not sufficient to overcome wrong-target specification. |
| Privacy and audit | Requires redacted account IDs, no raw descriptions in logs/results, safe audit events, and educational-simulation boundaries. | Preserved as failed evidence. |
| Product boundary | Specifies only the Generated Transaction System Layer and excludes run preservation, final selection, harness mechanics, screenshots, PR packaging, hook setup, and MCP configuration setup from product tasks. | Failed because the selected spec described homework automation and control-surface mechanics instead of only the transaction-processing system. |
| Task-card executability | Low-level tasks are transaction-system slices with concrete files, functions, edge cases, acceptance criteria, and verification. | Failed low-level task target. |
| Handoff usefulness | Complete enough for Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator) to consume. | Not usable downstream until superseded. |

## Decision

Select `20260618-003908-write-spec-python-replacement` and copy only:

- `agent-1-spec/outputs/specification.md` to `specification.md`

Support docs remain preserved under the run folder because the operator selected the run as the canonical spec and did not explicitly select canonical support docs.

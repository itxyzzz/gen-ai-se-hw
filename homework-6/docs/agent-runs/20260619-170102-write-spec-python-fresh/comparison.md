# Run Comparison

Selected run: `20260619-170102-write-spec-python-fresh`

Comparison date: 2026-06-19

## Compared Runs

| Run ID | Stack | Status | Selection outcome |
|---|---|---|---|
| `20260619-170102-write-spec-python-fresh` | `python` | Passing fresh run | Selected as canonical `specification.md`. |
| `20260618-003908-write-spec-python-replacement` | `python` | Passing replacement run, previously selected | Superseded because it predates the runtime provenance and repeated-run archival contract now required by the Homework 6 control surface. |
| `20260617-180458-write-spec-python-primary` | `python` | Failed / superseded | Preserved as evidence only; not selected for downstream use. |

## Selection Criteria

| Criterion | Fresh run evidence | Replacement run outcome | Primary run outcome |
|---|---|---|---|
| Completeness | Contains the required Task 1 sections, support docs, research notes, validation checklist, sub-agent handoffs, final review, and completion handoff. | Complete and previously selected. | Complete run package, but wrong product target. |
| Stack specificity | Names Python files, functions, `decimal.Decimal`, standard `json`, `pytest`, temporary-directory testing, coverage commands, and optional read-only `mcp/server.py` helpers. | Python-specific and implementation-ready. | Python-specific in places, but low-level tasks target the homework automation layer. |
| Research provenance | Records Context7 CPython lookup, ISO 4217, SIX, OWASP, assignment assumptions, and fallback limitations. | Records research and Context7 limitation. | Records research, but provenance does not overcome the wrong-target low-level task structure. |
| Privacy and audit | Requires redacted account identifiers, excludes raw descriptions and metadata from logs/results, and passed an independent plaintext sample scan. | Strong privacy and audit handling. | Preserved as failed evidence. |
| Product boundary | Specifies only the Generated Transaction System Layer; no harness, Superpowers, canonical-copy, screenshot, PR packaging, hook setup, or MCP configuration setup appears as product work. | Product-only boundary is sound. | Failed because low-level tasks describe Homework Automation Layer responsibilities. |
| Runtime archival and provenance | Requires existing `shared/` archival to zero-padded `archive/shared-NNN` folders and `shared/run-provenance.json` with non-sensitive source/version references. | Does not include the newer runtime provenance or zero-padded archival contract in the selected spec output. | Does not include the newer runtime provenance or archival contract and is already failed/superseded. |
| Task-card executability | Low-level tasks are transaction-system slices with exact prompts, target files, functions, behavior details, edge cases, acceptance criteria, and verification. | Executable, but less current after later provenance requirements. | Not executable for the desired product layer. |
| Handoff usefulness | Handoff and validation records are sufficient for a fresh downstream Hephaestus (Code Generator) run. | Useful for the already-generated earlier code version. | Not usable downstream until superseded. |

## Decision

Select `20260619-170102-write-spec-python-fresh` and copy only:

- `agent-1-spec/outputs/specification.md` to `specification.md`

Support docs remain preserved under the run folder because the operator selected the default specification package only.

## Post-Selection Note

The currently selected Hephaestus (Code Generator) software package was generated from the prior Athena run `20260618-003908-write-spec-python-replacement`. After this selection, downstream code generation or code repair should explicitly reconcile the generated software with the fresh canonical specification, especially the `shared/run-provenance.json` product contract.

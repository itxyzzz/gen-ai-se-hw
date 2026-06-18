# Code Run Preservation Repair Spec

Work ID: `2026-06-18-code-run-preservation-repair`
Short ID: `code-run-preservation-repair`
Status: Draft
Harness release: `unknown`
Schema: `schema:spec.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `rule:lifecycle.documentation-matrix`, `rule:quality.spec-handoff`

## Goal

Repair Homework 6 Task 2 control and evidence handling so Hephaestus (Code Generator) preserves full candidate code outputs under each run folder, supports clean canonical selection, and generates transaction pipelines whose runtime outputs remain clearly separated across repeated executions.

## Scope

- Restructure the current first Hephaestus run evidence so `docs/agent-runs/20260618-223217-generate-code-python-primary/` contains a complete run-local output inventory and candidate code package.
- Keep the first successful Hephaestus candidate selected by default, while making the selected canonical product files a copy from the run-local output package rather than the only copy.
- Update Hephaestus control-surface guidance under `agent-control/generate-code/`, plus thin entrypoint references if necessary, so future code-generation runs write all generated code and research evidence inside the run folder first.
- Update Athena (Spec Writer) control-surface guidance under `agent-control/write-spec/` so future generated specifications require deterministic pipeline reruns, `shared/` archiving, evidence preservation, and Hephaestus selection semantics without leaking Operator Layer harness process into the Generated Transaction System Layer.
- Preserve or add a clear selected-code record under `docs/agent-runs/` that distinguishes selected specification runs from selected code-generation runs.
- Update the existing generated Python pipeline in canonical paths only as needed to match the selected run-local candidate and to add repeated-run archival behavior.
- Add or update focused tests that prove repeated `python integrator.py` executions archive the prior `shared/` tree to zero-padded incrementing folders such as `archive/shared-001`, `archive/shared-002`, and `archive/shared-003`, then create a fresh `shared/` tree for the current run.
- Add traceability documentation showing which selected Athena (Spec Writer) specification run produced each Hephaestus (Code Generator) code version, and require later test-generation passes to declare which selected software version they target.

## Non-scope

- Do not implement Task 3 slash commands, hooks, or coverage gate behavior.
- Do not implement Task 4 custom MCP server files or add `pipeline-status` to MCP configuration.
- Do not implement Task 5 README, HOWTORUN, screenshots, final PR packaging, or author documentation.
- Do not change `TASKS.md`.
- Do not rewrite selected Athena run artifacts except for standing control-surface guidance that improves future Athena outputs.
- Do not copy runtime pipeline outputs such as `shared/` or `archive/` into canonical code selection packages.

## Current state

Task 1 is complete. The selected Athena (Spec Writer) replacement run `20260618-003908-write-spec-python-replacement` produced a transaction-system-focused `specification.md` and is recorded in `docs/agent-runs/final-selection.md`.

Task 2 has an initial Hephaestus run at `docs/agent-runs/20260618-223217-generate-code-python-primary/`. Its evidence folder contains metadata, source context, handoff, research notes, validation checklist, and final integration review, but it does not contain the generated code package. The generated product files currently live only at canonical Homework 6 paths such as `integrator.py`, `agents/*.py`, `tests/*.py`, and `research-notes.md`. The runtime output from the first test run lives under canonical `shared/`.

The current `integrator.py` clears JSON files inside `shared/input`, `shared/processing`, `shared/output`, and `shared/results` on each run. This makes repeated executions overwrite visible evidence instead of preserving distinct prior run outputs.

The current Hephaestus workflow says generated product files may be written directly to canonical Homework 6 product paths during authorized code-generation runs. This differs from the Athena pattern, where full run-local outputs are preserved and canonical paths are updated only through selection or first-run auto-selection.

## Proposed behavior

Future Hephaestus runs use a run-local first pattern:

1. Create an `agent-2-code/outputs/` package inside each concrete Hephaestus run folder before generating code.
2. Write the complete candidate product package under `agent-2-code/outputs/`, including code files, tests, canonical research notes, and an output inventory.
3. Exclude runtime execution folders from selectable code output packages. `shared/` and `archive/` remain runtime evidence, not generated source artifacts.
4. Record the selected Athena source for each Hephaestus candidate, including the source spec run ID, canonical spec path, and a stable content fingerprint such as a SHA-256 hash of `specification.md`.
5. For the first successful code-generation run, auto-select the candidate when no selected code package exists yet. Copy selected code and research notes from the run-local package to canonical paths and record the selection.
6. For later selections, remove the previous selected canonical product files that belong to the Hephaestus code package, then copy the new selected code and research notes from the chosen run-local package to canonical paths.
7. Preserve every meaningful code-generation run folder as evidence even when it is not selected.

The current first Hephaestus run should be repaired into that future shape by copying the existing canonical generated code and tests into `docs/agent-runs/20260618-223217-generate-code-python-primary/agent-2-code/outputs/`, adding a manifest or inventory, and recording that this run is selected as the first successful code-generation output. Canonical generated code remains present, but its provenance becomes the selected run-local output package.

Every generated transaction pipeline should preserve repeated runtime output visibly:

1. At pipeline startup, if the configured shared directory exists, move it under an archive root before creating the fresh protocol structure.
2. The default archive root is `archive/` beside the configured shared directory unless the implementation exposes an equivalent explicit option.
3. Archive folders use zero-padded incrementing IDs: `archive/shared-001`, `archive/shared-002`, `archive/shared-003`, and so on. The integrator picks the next unused positive integer and pads it to three digits.
4. After archival, the pipeline creates a fresh `shared/input`, `shared/processing`, `shared/output`, and `shared/results` structure for the current run.
5. Tests use temporary directories and must not mutate the real canonical `shared/` or `archive/` folders.

Later Themis (Test Generator) runs may also happen in multiple passes. Each preserved test-generation run and selected test package must declare the target software version it tests by recording the selected Hephaestus code run ID, code package inventory path, canonical code-selection record, and stable fingerprints for the files or package under test. A test-generation run must not silently target "latest" without naming the software version that existed when the tests were written.

## Interfaces and data

Affected repository control surfaces:

- `agent-control/generate-code/workflow.md`
- `agent-control/generate-code/run-registry.md`
- `agent-control/generate-code/quality-bar.md`
- `agent-control/write-spec/transaction-system-brief.md`
- `agent-control/write-spec/quality-bar.md`
- `agent-control/write-spec/workflow.md` only if needed to clarify how Athena must express product rerun behavior without copying Operator Layer selection mechanics into generated product specs.
- `agents.md` only if the standing Homework 6 guide needs a concise shared rule for Hephaestus code-run selection and generated pipeline rerun archival.

Affected generated product files:

- `integrator.py`
- `agents/*.py` only if pipeline archive behavior needs shared helper support.
- `tests/*.py` for focused repeated-run and selection-safe behavior tests.
- `research-notes.md` as selected code-generation research evidence.

Affected evidence files:

- `docs/agent-runs/20260618-223217-generate-code-python-primary/agent-2-code/outputs/**`
- A selection record under `docs/agent-runs/`, either by extending `final-selection.md` with a separate Code Generation Selection section or by adding an explicit code-selection file.
- An `agent-2-code/outputs/inventory.md` file or equivalent manifest in each concrete Hephaestus run folder, listing selectable source and research files.
- A provenance field in Hephaestus run metadata, output inventory, and selected-code records that ties the code version to the selected Athena run `20260618-003908-write-spec-python-replacement` and the canonical `specification.md` content fingerprint used for generation.
- A forward traceability rule for future Themis test-generation records: each test run must identify the selected Hephaestus code version it targets before copying tests to canonical paths.

Runtime data shape:

```text
archive/
  shared-001/
    input/
    processing/
    output/
    results/
  shared-002/
    input/
    processing/
    output/
    results/
shared/
  input/
  processing/
  output/
  results/
```

Canonical selection copies must include generated code and research notes only. They must exclude `shared/`, `archive/`, `.coverage`, `__pycache__/`, `.pytest_cache/`, and other runtime or tool caches.

## Risks

- Moving a real `shared/` folder can fail on Windows if files are locked. The implementation should fail clearly or use a documented fallback only when it preserves separation and does not silently mix old and new outputs.
- A clean canonical replacement procedure can accidentally remove hand-maintained files if the selected package ownership boundary is too broad. The file inventory must declare only Hephaestus-owned product paths.
- Athena (Spec Writer) guidance must avoid leaking Operator Layer run-selection mechanics into the generated transaction-system specification. Product specs should describe rerunnable pipeline behavior and result shape contracts, while Hephaestus workflow docs describe code-run preservation and canonical selection.
- Traceability records can become misleading if they rely only on mutable canonical paths. They must include immutable run IDs and stable content fingerprints, not only `specification.md` or `integrator.py` path names.
- Tests that inspect `archive/` must use temporary paths to avoid polluting reviewer-visible runtime evidence.
- Existing untracked generated code and runtime output are part of the current operator state. The implementation must not discard them while repairing evidence structure.

## Acceptance criteria

- The first Hephaestus run folder contains `agent-2-code/outputs/` with copies of all selected generated code files, tests, and `research-notes.md`.
- The run-local outputs include an inventory that marks each file as selectable source, selectable research, or excluded runtime/tool output.
- Code-generation selection is recorded separately from or clearly within `docs/agent-runs/final-selection.md`, including run ID, selected files, canonical targets, rationale, and excluded runtime folders.
- Code-generation selection records include the selected Athena source spec run ID, canonical spec path, and content fingerprint used by the Hephaestus run.
- Future test-generation guidance requires every Themis run and selected test package to name the selected Hephaestus code version it targets before tests are copied to canonical paths.
- The selected canonical Task 2 code package can be cleanly replaced by a later selected run using the recorded inventory; previous selected generated files are removed before copying the new selection.
- Running the generated pipeline multiple times preserves prior `shared/` output under zero-padded incrementing folders such as `archive/shared-001` and `archive/shared-002`, and creates a fresh `shared/` tree for the newest run.
- Tests cover at least the archive ID increment behavior and confirm the current run's `shared/results/summary.json` is separate from archived results.
- Athena (Spec Writer) guidance requires generated specs to include deterministic rerun and prior-output archival behavior as a product requirement.
- Hephaestus (Code Generator) guidance requires full run-local candidate outputs, output inventory, first-success auto-selection, explicit later selection, and exclusion of `shared/` and `archive/` from selected code copies.
- Validation confirms Task 3, Task 4, and Task 5 deliverables remain out of scope.

## Documentation artifact matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Before each commit | `homework-6/CHANGELOG.md` | Newest-first Homework 6 step entry for the planning package and later implementation |
| Test cases | Snapshot | Yes | Before implementation | `docs/work-items/2026-06-18-code-run-preservation-repair/snapshots/test-cases.snapshot.md` | Captures repeated-run archival and selection inventory expectations |
| Testing guide delta | Living delta | No | Not applicable | Not applicable | Task 5 testing docs are out of scope for this control-surface repair |
| Operator manual delta | Living delta | No | Not applicable | Not applicable | Existing agent-control docs are the operator surfaces being updated directly |
| API reference delta | Living delta | No | Not applicable | Not applicable | No public API or MCP API change is planned |
| Architecture snapshot | Snapshot | No | Not applicable | Not applicable | Existing spec and plan capture the control-flow decision sufficiently |
| Architecture summary delta | Living delta | No | Not applicable | Not applicable | No long-lived architecture summary exists for Task 2 yet |

## Approval

- Status: Draft
- Superseded by: not applicable

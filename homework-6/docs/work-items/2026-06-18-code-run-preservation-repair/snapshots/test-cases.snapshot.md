# Test Cases Snapshot: Code Run Preservation Repair

Work ID: `2026-06-18-code-run-preservation-repair`
Status: Draft

## Pipeline Rerun Archival

### Case 1: First run with no existing shared directory

- Setup: Use a temporary directory as the pipeline working root. Ensure `shared/` and `archive/` do not exist.
- Action: Run the integrator against the sample transactions with `--shared-dir C:/tmp/hw6-case/shared`, substituting an isolated test temporary directory for `C:/tmp/hw6-case`.
- Expected:
  - `C:/tmp/hw6-case/shared/input`, `C:/tmp/hw6-case/shared/processing`, `C:/tmp/hw6-case/shared/output`, and `C:/tmp/hw6-case/shared/results` exist in the isolated test directory.
  - `C:/tmp/hw6-case/archive` either does not exist or contains no `shared-*` child folders in the isolated test directory.
  - `C:/tmp/hw6-case/shared/results/summary.json` accounts for all sample transactions in the isolated test directory.

### Case 2: Second run archives the first shared directory

- Setup: Reuse the temporary directory from Case 1 with existing `shared/` content.
- Action: Run the integrator a second time with the same isolated shared path.
- Expected:
  - The previous shared tree is moved to `archive/shared-001` under the isolated test directory.
  - A new `shared` tree exists with fresh protocol subdirectories under the isolated test directory.
  - `archive/shared-001/results/summary.json` remains readable under the isolated test directory.
  - `shared/results/summary.json` is from the current run and remains separate from the archived summary.

### Case 3: Zero-padded incrementing archive IDs

- Setup: Use an isolated test directory containing existing `archive/shared-001` and a current `shared`.
- Action: Run the integrator again with the same isolated shared path.
- Expected:
  - The previous current shared tree is moved to `archive/shared-002` under the isolated test directory.
  - Existing `archive/shared-001` is not overwritten.
  - A fresh `shared` tree is created for the new run.

### Case 4: Non-default shared directory archives beside the configured path

- Setup: Use `--shared-dir C:/tmp/hw6-case/runs/current-shared`, substituting an isolated test temporary directory for `C:/tmp/hw6-case`.
- Action: Run twice with the same non-default shared path.
- Expected:
  - The second run archives the prior shared tree under `runs/archive/shared-001` or another documented archive root colocated with the configured shared path.
  - The current run uses `runs/current-shared` for fresh protocol directories.

## Run-Local Code Output Inventory

### Case 5: Current Hephaestus run contains selected output package

- Setup: Inspect `docs/agent-runs/20260618-223217-generate-code-python-primary/agent-2-code/outputs/`.
- Expected:
  - The folder contains copies of selected generated code files: `integrator.py`, `agents/*.py`, and `tests/*.py`.
  - The folder contains selected `research-notes.md`.
  - The folder contains an inventory or manifest that maps each run-local file to its canonical target.
  - The inventory identifies the selected Athena source spec run `20260618-003908-write-spec-python-replacement` and the source `specification.md` fingerprint used to generate this code package.
  - The inventory excludes `shared/`, `archive/`, `.coverage`, `__pycache__/`, and `.pytest_cache/`.

### Case 6: Selection record separates code generation from specification selection

- Setup: Inspect `docs/agent-runs/final-selection.md` or the selected code-generation record.
- Expected:
  - The selected Athena specification run remains recorded.
  - The selected Hephaestus code-generation run is recorded with selected file list, canonical targets, source Athena run ID, source spec fingerprint, rationale, and excluded runtime folders.
  - The record states that the first successful Hephaestus code output is selected by default.

### Case 7: Future selection can replace canonical code cleanly

- Setup: Use the output inventory from the selected Hephaestus run.
- Action: Review the documented selection procedure.
- Expected:
  - The procedure removes only previously selected Hephaestus-owned canonical code and research files.
  - The procedure copies only selected code and research files from the new run-local output package.
  - Runtime folders and caches are not copied.

### Case 8: Future test-generation passes target a named software version

- Setup: Inspect the updated standing control-surface guidance for future Themis runs.
- Expected:
  - A future Themis test-generation run must record the selected Hephaestus code run ID it targets.
  - A future selected test package must reference the selected code inventory or code-selection record and a stable package fingerprint.
  - The guidance rejects silently targeting mutable "latest" canonical code without naming the software version under test.

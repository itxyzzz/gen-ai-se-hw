# Generate Tests Run Registry

This file defines preservation and selection rules for Themis (Test Generator) runs.

## Run ID Format

Use:

```text
YYYYMMDD-HHMMSS-generate-tests-<stack>-short-label
```

Examples:

```text
20260620-091500-generate-tests-python-primary
20260620-103000-generate-tests-python-privacy-expansion
20260621-151500-generate-tests-java-alternate
```

Use `python` or `java` for `<stack>`, matching the selected Hephaestus package set being tested.

## Required Layout

Each meaningful Themis run lives under:

```text
homework-6/docs/agent-runs/RUN_ID/
```

Required files and folders:

- `run-metadata.md`
- `inputs/source-context.md`
- `inputs/selected-code-inventory.snapshot.md`
- `agent-3-tests/outputs/`
- `agent-3-tests/outputs/inventory.md`
- `agent-3-tests/workspace/selected-code/`
- `agent-3-tests/workspace/project-under-test/`
- `agent-3-tests/evidence/`
- `agent-3-tests/validation-checklist.md`
- `agent-3-tests/handoff.md`

## Output Inventory

`agent-3-tests/outputs/inventory.md` is the only source for selection copy targets. It must list:

- Candidate relative path under `outputs/`.
- Intended canonical target path.
- Kind, such as unit test, integration test, pytest config, fixture, or helper.
- SHA-256 fingerprint.
- Source selected Hephaestus run ID.
- Whether the file replaces, extends, or creates a canonical target.

The inventory must explicitly exclude runtime and tool outputs such as `workspace/`, `evidence/`, `shared/`, `archive/`, `.coverage*`, `.pytest_cache/`, `.test-tmp/`, and `__pycache__/`.

For `stack=java`, inventory kinds should name Java test classes, Maven test resources, or build-test configuration. Java test paths normally use `src/test/java/...` and validation evidence should cite Maven/JUnit and JaCoCo commands.

## Selected-Code Traceability

Each run must preserve:

- Selected Hephaestus run ID.
- Selected output inventory path.
- Final-selection record path.
- Source Athena run ID.
- Source spec path and fingerprint recorded by Hephaestus.
- Current canonical `specification.md` fingerprint.
- Source spec mismatch status.
- Selected code file or package fingerprints.

Do not compare against or select from "latest" files discovered in the root tree.

## Workspace And Evidence

`workspace/selected-code/` is a copy of the selected Hephaestus package. `workspace/project-under-test/` is rebuilt by overlaying `outputs/` onto `selected-code/`.

Tests, coverage, command validation, hook validation, and pipeline validation run from `workspace/project-under-test/`. Runtime output must stay under the run folder.

`evidence/` stores compact text evidence only. Do not store bulky caches or opaque coverage databases as selectable outputs.

Command and hook evidence should include:

- `support-run-pipeline.txt`
- `support-validate-transactions.txt`
- `hook-pass.txt`
- `hook-fail.txt`

## Comparison

Compare candidate Themis runs by:

- Targeted selected Hephaestus version.
- Source spec mismatch handling.
- Coverage percentage and missing-line profile.
- Unit and integration coverage balance.
- Assertion quality.
- Privacy/audit coverage.
- Fixture isolation and rerun behavior.
- Command and hook validation evidence.
- Simplicity and maintainability of tests.

## Selection

The first successful Themis package may be selected by default only when no selected test package exists and the operator has not asked for comparison. Later selections require explicit operator selection.

Selection records must name:

- Selected Themis run ID.
- Targeted selected Hephaestus run ID.
- Selected output inventory path.
- Files copied to canonical targets.
- Files removed or replaced from a prior selected test package.
- Validation commands and results.
- Operator and rationale.
- Excluded runtime/tool paths.

During selection, copy only inventory-declared files from `agent-3-tests/outputs/`. Never copy `workspace/` wholesale.

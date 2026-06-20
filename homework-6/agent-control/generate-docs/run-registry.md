# Generate Docs Run Registry

This file defines preservation and selection rules for Clio (Documentation Generator) runs.

## Run ID Format

Use:

```text
YYYYMMDD-HHMMSS-generate-docs-python-short-label
```

Examples:

```text
20260620-180000-generate-docs-python-primary
20260620-193000-generate-docs-python-screenshot-repair
```

## Required Layout

Each meaningful Clio run lives under:

```text
homework-6/docs/agent-runs/RUN_ID/
```

Required files and folders:

- `run-metadata.md`
- `inputs/source-context.md`
- `inputs/selected-code-inventory.snapshot.md`
- `inputs/selected-test-inventory.snapshot.md`
- `inputs/screenshot-inventory.snapshot.md`
- `inputs/prior-homework-style.snapshot.md`
- `agent-4-docs/outputs/`
- `agent-4-docs/outputs/inventory.md`
- `agent-4-docs/outputs/docs/screenshots/`
- `agent-4-docs/evidence/`
- `agent-4-docs/review/`
- `agent-4-docs/validation-checklist.md`
- `agent-4-docs/handoff.md`

## Output Inventory

`agent-4-docs/outputs/inventory.md` is the only source for selection copy targets. It must list:

- Candidate relative path under `outputs/`.
- Intended canonical target path.
- Kind, such as README, runbook, architecture doc, testing doc, API reference, PR draft, screenshot, or support doc.
- SHA-256 fingerprint.
- Source selected Athena, Hephaestus, and Themis run IDs.
- Screenshot source path when applicable.
- Whether the file replaces, extends, or creates a canonical target.

The inventory must explicitly exclude runtime and tool outputs such as `evidence/`, `review/`, `shared/`, `archive/`, `.coverage*`, `.pytest_cache/`, `.test-tmp/`, `tmp/`, and `__pycache__/`.

## Source Traceability

Each run must preserve:

- Selected Athena run ID.
- Selected Hephaestus run ID and output inventory path.
- Selected Themis run ID and output inventory path.
- Final-selection record path.
- Source and current spec fingerprints.
- Selected code and test package fingerprints.
- MCP config/server status.
- Command and hook support-surface status.
- Prior homework author and style source paths.
- Screenshot source inventory and mapping.

Do not compare against or select from "latest" files discovered in the root tree.

## Screenshot Inventory

`inputs/screenshot-inventory.snapshot.md` must list every source screenshot under `docs/screenshots/operator-sourced/` and record:

- Source path.
- File size and observed timestamp when available.
- Evidence category inferred from filename or visual inspection.
- Stable target path if used.
- `unused` when not used.
- Reason for use or non-use.
- Freshness assessment: fresh, current enough with rationale, stale, or blocked.
- Passing/blocking classification when relevant.
- Task coverage: Task 4 MCP evidence, Task 5 screenshot evidence, PR evidence, or support-only evidence.
- Privacy/safety review status.
- Whether the stable target was copied from source or freshly captured.

Clio may use only a subset of operator-sourced screenshots. It must preserve the full source folder.

Stable screenshot targets have assignment-specific semantics. `test-coverage.png` is passing coverage evidence; `hook-trigger.png` is hook blocking or failure evidence; `mcp-interaction.png` must include both Context7 and custom `pipeline-status` MCP evidence in one screenshot or point to explicit paired evidence and an accepted limitation.

## Evidence

`agent-4-docs/evidence/` stores compact text evidence and screenshot-capture notes. Good evidence files include:

- `pipeline-run.txt`
- `test-coverage.txt`
- `skill-run-pipeline.txt`
- `validate-transactions.txt`
- `hook-trigger.txt`
- `mcp-interaction.txt`
- `screenshot-capture-notes.md`

Do not store bulky caches or opaque coverage databases as selectable outputs.

## Comparison

Compare candidate Clio runs by:

- Targeted selected Athena, Hephaestus, and Themis versions.
- Documentation completeness against Homework 6 and repository standards.
- Accuracy against current code, tests, MCP server, commands, hooks, and research notes.
- Screenshot coverage and privacy safety.
- Evidence freshness and reproducibility.
- PR draft usefulness.
- Simplicity, maintainability, and reviewer readability.

## Selection

The first successful Clio package may be selected by default only when no final canonical documentation package exists and the operator has not asked for comparison. Later selections require explicit operator selection.

Selection records must name:

- Selected Clio run ID.
- Selected Athena, Hephaestus, and Themis output run IDs and inventory versions.
- Selected output inventory path.
- Files copied to canonical targets.
- Screenshot source-to-target mapping.
- Validation commands and results.
- Operator and rationale.
- Excluded runtime/tool paths.

During selection, copy only inventory-declared files from `agent-4-docs/outputs/`. Never copy evidence, review notes, source screenshots, caches, or runtime output wholesale.

Regeneration after a control-surface repair is a separate Clio run and selection step. Do not edit an existing selected Clio run output to make it look as though it was generated under newer rules. A refreshed documentation package must preserve a new run folder or an explicit selection record that names the regenerated outputs and keeps canonical docs, screenshot targets, PR draft, selected run evidence, and final-selection mapping synchronized.

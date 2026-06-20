# Generate Docs Workflow

This is the canonical workflow for Homework 6 Clio (Documentation Generator) entrypoints:

- Codex Markdown skill: `homework-6/.agents/skills/generate-docs/SKILL.md`
- Claude Code project skill, exposed as `/generate-docs`: `homework-6/.claude/skills/generate-docs/SKILL.md`
- Claude Code legacy command wrapper: `homework-6/.claude/commands/generate-docs.md`

The entrypoints must stay thin. Update this package first when the workflow changes.

All paths are repository-root relative unless the active project root is already `homework-6`. When running from a homework-root project, remove the leading `homework-6/` prefix from homework-local paths while keeping root-level references such as `AGENTS.md`, `HOMEWORK_STANDARDS.md`, `README.md`, and prior homework folders relative to the repository root when available.

## Required Context

Read this context before writing, executing, comparing, or selecting documentation:

1. `homework-6/docs/agent-runs/final-selection.md`: selected Athena (Spec Writer), Hephaestus (Code Generator), and Themis (Test Generator) records.
2. Selected Hephaestus output inventory named by the selection record.
3. Selected Themis output inventory named by the selection record.
4. `homework-6/specification.md`: current canonical specification used as comparison context. Clio documents the explicitly selected Athena, Hephaestus, and Themis output runs named by the selection record or invocation; those selected outputs may or may not match the current canonical root files.
5. `homework-6/sample-transactions.json`: canonical sample input, read only when needed for counts or safe summaries.
6. `homework-6/agents.md`: layer glossary, privacy rules, run preservation, Clio role, and Themis boundary.
7. `homework-6/TASKS.md`: Task 5 documentation and screenshot checks plus submission PR description requirements.
8. Root `AGENTS.md`, `HOMEWORK_STANDARDS.md`, and root `README.md` when available.
9. `homework-6/agent-control/operate-pipeline/commands-and-hooks.md`: command and hook behavior to document and validate.
10. `homework-6/mcp.json`, `homework-6/.codex/config.toml`, and `homework-6/mcp/server.py`: MCP configuration and custom `pipeline-status` behavior.
11. `homework-6/research-notes.md`: Context7 provenance for code generation.
12. Prior homework documentation for author and style examples:
    - `homework-1/README.md`
    - `homework-2/README.md`
    - `homework-3/README.md`
    - `homework-4/README.md`
    - Supporting `HOWTORUN.md`, `ARCHITECTURE.md`, `TESTING_GUIDE.md`, and `API_REFERENCE.md` files from Homeworks 1 through 4 when present.
13. `homework-6/docs/screenshots/operator-sourced/`: source screenshot inventory.
14. This package's `quality-bar.md` and `run-registry.md`.

If the selected Hephaestus package, selected Themis package, or final-selection record is missing, stop and ask the operator to select or repair the package. Do not target "latest" from the file tree, and do not assume the canonical root files are the documentation target unless the selection record explicitly names them as the selected outputs.

Repository `dev-doc-harness` and Superpowers requirements apply to Operator Layer maintenance of this package. They do not apply inside Clio (Documentation Generator) runs or generated reviewer-facing documentation.

## Operating Modes

- `generate`: create a candidate documentation package for the selected Homework 6 system.
- `resume`: continue a bounded Clio run from preserved handoff evidence.
- `compare`: compare two or more preserved Clio runs without changing canonical docs.
- `select`: copy an accepted candidate documentation package to canonical targets through its inventory and record the selection.

Default to `generate` unless the operator asks for comparison, continuation, or selection.

## Run ID

Use run IDs in this format:

```text
YYYYMMDD-HHMMSS-generate-docs-python-short-label
```

## Required Traceability

Every Clio run must record:

- Selected Athena run ID.
- Selected Hephaestus run ID and output inventory path.
- Selected Themis run ID and output inventory path.
- Final-selection record path.
- Current canonical `specification.md` fingerprint.
- Selected code package fingerprint or file fingerprints.
- Selected test package fingerprint or file fingerprints.
- MCP status server/config status.
- Source screenshot inventory path and file list.
- Prior homework author source and detected author display name.
- Prior homework documentation files used as style examples.
- Pre-existing dirty git state.

The current prior homework README files identify the author as `Igor Tanatarov`. If prior homework author sources are unavailable or contradictory, stop and ask the operator for the exact display name before producing `README.md`.

## Run Folder Layout

Create the run folder before drafting docs:

```text
homework-6/docs/agent-runs/RUN_ID/
  run-metadata.md
  inputs/
    source-context.md
    selected-code-inventory.snapshot.md
    selected-test-inventory.snapshot.md
    screenshot-inventory.snapshot.md
    prior-homework-style.snapshot.md
  agent-4-docs/
    outputs/
      README.md
      HOWTORUN.md
      ARCHITECTURE.md
      TESTING_GUIDE.md
      API_REFERENCE.md
      docs/
        pr-description-draft.md
        screenshots/
      inventory.md
    evidence/
      pipeline-run.txt
      test-coverage.txt
      skill-run-pipeline.txt
      validate-transactions.txt
      hook-trigger.txt
      mcp-interaction.txt
      screenshot-capture-notes.md
    review/
    validation-checklist.md
    handoff.md
  comparison.md
```

`comparison.md` is required only for compare mode or when more than one viable run is evaluated.

`run-metadata.md` must record mode, selected stack, start time, orchestration tool, selected run traceability, author source, screenshot source, intended output targets, and pre-existing dirty git state.

`inputs/source-context.md` must list exact source artifacts read, including selection records, inventories, specs, sample data, assignment file, agent guide, MCP config/server, command/hook guidance, prior homework documentation sources, screenshot source folder, and this control package.

## Documentation Generation Scope

Clio may create or update:

- `README.md`
- `HOWTORUN.md`
- `ARCHITECTURE.md`
- `TESTING_GUIDE.md`
- `API_REFERENCE.md`
- `docs/pr-description-draft.md`
- Required screenshots under `docs/screenshots/`
- Clio run metadata, inventories, validation evidence, review notes, and handoff files.

The final documentation must describe these layers clearly:

- Operator Layer: control packages, skills/commands, selection records, MCP configuration, operation commands, and coverage hooks.
- Homework Automation Layer: Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator).
- Generated Transaction System Layer: deterministic runtime pipeline, JSON file protocol, runtime components, MCP status server, tests, commands, coverage gate, and runtime evidence.

Do not include a narrative about operator challenges or feedback. The documentation and PR draft should describe the system as built, AI workflow, verification, evidence, known limitations, and how to review it.

Keep internal Clio instructions out of reviewer-facing outputs. Privacy and evidence guidance may appear in docs only as reviewer-facing expectations or observable properties, not as agent directives such as "do not print raw input transactions in review evidence," "Clio must," "the agent should," or similar workflow-control language. Put those imperatives in run metadata, evidence notes, validation checklists, handoff files, or this control package instead.

## Themis Boundary

Clio consumes the selected Themis suite and final-selection record. Clio must rerun selected tests and coverage for evidence when local tooling permits, but it must not silently modify, fork, or replace the selected Themis suite.

If Clio finds a missing final test requirement:

1. Record the gap in `agent-4-docs/validation-checklist.md` and `agent-4-docs/handoff.md`.
2. Request a Themis follow-up or explicit operator authorization for a final test-hardening delta.
3. Do not present the documentation package as fully complete until the gap is addressed or explicitly documented as a limitation.

## Evidence Commands

Prefer fresh local evidence where safe and available:

```powershell
python integrator.py
python -m pytest -p no:cacheprovider
python scripts/check_coverage_gate.py --fail-under 80
python scripts/check_coverage_gate.py --fail-under 99
```

Use `agent-control/operate-pipeline/commands-and-hooks.md` for the exact `/run-pipeline` and `/validate-transactions` behavior. Evidence must summarize safe counts, statuses, and reason codes only. Do not print raw account IDs, raw descriptions, names, or full audit payloads.

If coverage or hook commands need unsandboxed execution because of Windows coverage-file rename restrictions, request approval through the active tool policy and record the reason in evidence.

## Screenshot Handling

Source screenshots live under:

```text
homework-6/docs/screenshots/operator-sourced/
```

Clio must preserve every source screenshot in that folder. Do not rename, overwrite, prune, compress, or edit source screenshots.

Clio must produce or select stable reviewer-facing screenshots:

- `docs/screenshots/pipeline-run.png`
- `docs/screenshots/test-coverage.png`
- `docs/screenshots/skill-run-pipeline.png`
- `docs/screenshots/hook-trigger.png`
- `docs/screenshots/mcp-interaction.png`

Clio may copy an operator-sourced screenshot to one of those stable targets when it satisfies the evidence requirement and appears safe for review. Clio does not have to use every source screenshot. The screenshot inventory must record:

- Source screenshot path.
- Intended stable target, or `unused`.
- Reason for use or non-use.
- Privacy/safety review status.
- Whether a fresh automated screenshot was captured instead.

If a required screenshot cannot be captured automatically and no safe source screenshot exists, write the missing screenshot name, blocker, and exact manual capture steps in `agent-4-docs/evidence/screenshot-capture-notes.md`.

## Output Inventory And Selection

Write all candidate docs first under `agent-4-docs/outputs/`. `agent-4-docs/outputs/inventory.md` is the only source for canonical copy targets. It must list each output path, canonical target, kind, SHA-256 fingerprint, source run IDs, screenshot source where applicable, and whether the file creates or replaces a canonical target.

The first successful Clio documentation package may be selected by default when canonical final documentation does not yet exist. Later selections require explicit operator selection.

During selection, copy only inventory-declared files from `agent-4-docs/outputs/`. Never copy `evidence/`, `review/`, or input snapshots wholesale into canonical documentation targets.

Record selected Clio run ID, selected output inventory, copied canonical paths, validation commands and results, screenshot mapping, rationale, operator, and excluded runtime/tool paths in `docs/agent-runs/final-selection.md` or a linked Clio selection record.

## Validation And Handoff

Before reporting a Clio run complete:

1. Confirm selected-run traceability is present.
2. Confirm prior homework author source and style-source records are present.
3. Confirm required docs exist and contain no unresolved draft markers.
4. Confirm `README.md` includes `Igor Tanatarov` or the operator-provided author name.
5. Confirm required Homework 6 docs include diagrams where repository standards require them.
6. Confirm `docs/pr-description-draft.md` is standalone and includes screenshot links or embedding instructions. Screenshot links in the draft file must be correct relative to `docs/pr-description-draft.md`, for example `screenshots/pipeline-run.png` rather than `docs/screenshots/pipeline-run.png`.
7. Confirm required screenshots exist or missing-capture notes contain exact operator steps.
8. Confirm evidence and screenshots do not expose raw account IDs, raw descriptions, credentials, tokens, or unfiltered metadata.
9. Confirm reviewer-facing docs do not contain internal Clio/workflow-control instructions; rephrase privacy and evidence notes as reviewer-facing expectations or product behavior.
10. Rerun available pipeline, test, coverage, command, hook, and MCP checks or record blockers.
11. Confirm Clio did not modify selected tests or runtime product code unless explicitly authorized.
12. Confirm `agent-4-docs/outputs/inventory.md` excludes evidence, review notes, caches, runtime output, and tool-output folders.
13. Update `homework-6/CHANGELOG.md` before any commit.
14. Review the diff for unrelated changes, generated noise, unresolved draft markers, and scope creep.

Write `agent-4-docs/validation-checklist.md` with commands, expected signals, actual results, blockers, and limitations.

Write `agent-4-docs/handoff.md` with run ID, selected source versions, files created or modified, screenshot status, validation status, known risks, and exact next suggested prompt.

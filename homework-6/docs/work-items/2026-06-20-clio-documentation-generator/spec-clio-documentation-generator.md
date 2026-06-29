# Clio Documentation Generator Spec

Work ID: `2026-06-20-clio-documentation-generator`
Short ID: `clio-documentation-generator`
Status: Draft
Harness release: `unknown`
Schema: `schema:spec.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `rule:lifecycle.documentation-matrix`, `rule:quality.spec-handoff`

## Goal

Create the fourth Homework Automation Layer agent, Clio (Documentation Generator), and define the first-run documentation workflow that produces reviewer-facing Homework 6 documentation, screenshot evidence, and a draft pull request description from the selected Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Task 4 MCP outputs.

## Scope

- Add a tool-neutral Clio control package under `agent-control/generate-docs/`.
- Add Codex and Claude Code entrypoints for Clio, expected as `generate-docs` project skills and a thin Claude legacy command wrapper.
- Update the standing Homework 6 agent guide only where needed to route future Clio runs to the shared workflow.
- Define a preserved Clio run layout under `docs/agent-runs/` with candidate documentation outputs, screenshot inventory, validation evidence, and handoff.
- Define how Clio consumes selected Themis results: rerun and document the selected suite, but do not silently fork or replace it.
- Define how Clio uses `docs/screenshots/operator-sourced/`: preserve every source screenshot in place, copy only necessary evidence screenshots to stable reviewer-facing paths, and list unused source screenshots in the Clio run inventory.
- Define final documentation targets: `README.md`, `HOWTORUN.md`, `ARCHITECTURE.md`, `TESTING_GUIDE.md`, `API_REFERENCE.md`, screenshot evidence under `docs/screenshots/`, and a draft pull request description under `docs/pr-description-draft.md`.
- Define prior homework documentation as style and author-name source context, especially Homeworks 1 through 4 README/HOWTORUN/API/architecture/testing documents when present.

## Non-scope

- Do not change the selected runtime transaction-processing implementation unless a later explicit repair task authorizes it.
- Do not generate or replace tests as Clio. Test suite changes remain Themis (Test Generator) work unless a separate operator-approved final test-hardening delta is created.
- Do not modify `TASKS.md`, selected Athena/Hephaestus/Themis run artifacts, or frozen prior work-item snapshots.
- Do not include an operator challenges or feedback narrative in the generated documentation or PR draft. Clio should describe the system as built, evidence, verification, AI workflow, and known limitations.
- Do not make Homework Automation Layer agents depend on `dev-doc-harness`, Superpowers, hidden chat state, or unavailable plugins.

## Current state

Tasks 1 through 4 are complete. `docs/agent-runs/final-selection.md` names the selected Athena (Spec Writer) run `20260619-170102-write-spec-python-fresh`, selected Hephaestus (Code Generator) run `20260619-175211-generate-code-python-fresh-spec`, and selected Themis (Test Generator) run `20260620-144025-generate-tests-python-fresh-spec`.

The selected Themis suite is canonical and already covers the selected code package. Task 4 added the custom FastMCP `pipeline-status` server, MCP tests, and combined MCP configuration. The current root tree does not yet contain the final reviewer-facing documentation set expected by Homework 6 standards. Operator-sourced screenshots have been copied into `docs/screenshots/operator-sourced/` and must remain preserved at that source path.

Prior homework README files consistently identify the student author as `Igor Tanatarov`. Their documentation packages also provide useful local style examples for author lines, overview structure, quick-start sections, documentation maps, architecture diagrams, testing guides, and API references.

## Proposed behavior

Clio (Documentation Generator) becomes the fourth portable Homework Automation Layer agent. A Clio run reads selected run records, selected code/test inventories, canonical implementation files, MCP configuration, operation-command guidance, sample transactions, and the operator-sourced screenshot inventory. It produces a run-local documentation candidate first, validates the candidate, and then copies inventory-declared documentation and screenshot outputs to canonical targets only when the workflow authorizes selection.

The first successful Clio run may be selected by default when final canonical documentation files do not yet exist. Later Clio selections require explicit operator selection. Canonical documentation must describe all relevant layers:

- Operator Layer control surfaces that maintain generation workflows, selection records, commands, hooks, and MCP support.
- Homework Automation Layer agents: Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator).
- Generated Transaction System Layer: deterministic runtime pipeline, JSON file protocol, runtime components, MCP status server, tests, commands, and coverage gate.

Clio must include the student author name in `README.md`. The preferred source is the prior homework documentation, which currently identifies the author as `Igor Tanatarov`. If that source becomes unavailable or inconsistent, Clio must stop and ask the operator for the exact display name rather than guessing.

Clio must rerun or capture fresh evidence for the selected system when local tooling permits:

- Pipeline run evidence for `python integrator.py`.
- Test and coverage evidence for `python -m pytest -p no:cacheprovider` and `python scripts/check_coverage_gate.py --fail-under 80`.
- Hook blocking evidence, preferably `python scripts/check_coverage_gate.py --fail-under 99` or an actual Git hook capture when available.
- `/run-pipeline` and `/validate-transactions` command evidence through the shared Operator Layer surfaces.
- MCP interaction evidence that shows both Context7 usage provenance and the custom `pipeline-status` tool/resource behavior.

When automated screenshot capture is not possible, Clio must record the missing screenshot, why it could not be captured, and exact operator steps to capture it. Clio may reuse operator-sourced screenshots by copying them to stable assignment paths when they satisfy the evidence requirement and are safe for review.

## Interfaces and data

Expected implementation files and interfaces:

- `agent-control/generate-docs/README.md`
- `agent-control/generate-docs/workflow.md`
- `agent-control/generate-docs/quality-bar.md`
- `agent-control/generate-docs/run-registry.md`
- `.agents/skills/generate-docs/SKILL.md`
- `.agents/skills/generate-docs/agents/openai.yaml`
- `.claude/skills/generate-docs/SKILL.md`
- `.claude/commands/generate-docs.md`
- `agents.md` routing updates for Clio, only if needed.

Prior homework context sources for Clio:

- `../homework-1/README.md`
- `../homework-2/README.md`
- `../homework-3/README.md`
- `../homework-4/README.md`
- Supporting `HOWTORUN.md`, `ARCHITECTURE.md`, `TESTING_GUIDE.md`, and `API_REFERENCE.md` files from Homeworks 1 through 4 when present and readable.

Expected Clio run output layout:

```text
docs/agent-runs/RUN_ID/
  run-metadata.md
  inputs/
    source-context.md
    selected-code-inventory.snapshot.md
    selected-test-inventory.snapshot.md
    screenshot-inventory.snapshot.md
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
      hook-trigger.txt
      mcp-interaction.txt
      screenshot-capture-notes.md
    validation-checklist.md
    handoff.md
```

Expected canonical documentation and evidence targets after selection:

- `README.md`
- `HOWTORUN.md`
- `ARCHITECTURE.md`
- `TESTING_GUIDE.md`
- `API_REFERENCE.md`
- `docs/pr-description-draft.md`
- `docs/screenshots/pipeline-run.png`
- `docs/screenshots/test-coverage.png`
- `docs/screenshots/skill-run-pipeline.png`
- `docs/screenshots/hook-trigger.png`
- `docs/screenshots/mcp-interaction.png`
- Additional reviewer-facing screenshots only when useful, such as spec production or README evidence.

The source folder `docs/screenshots/operator-sourced/` remains preserved and is never overwritten or pruned by Clio.

## Risks

- Screenshots may contain raw account IDs, descriptions, or other sensitive fields. Clio must inspect evidence safety and reject or document unsafe screenshots rather than copying them into reviewer-facing targets.
- Local screenshot capture may be unavailable from the current sandbox. Clio must provide exact manual capture instructions for any missing required images.
- The Task 5 assignment wording says Agent 4 owns tests and documentation, while the local Themis agreement assigns selected-test creation to Themis and final test evidence to Clio. Clio must document and respect that division.
- Documentation can become a stale second source of truth if it restates implementation details without checking current code, inventories, and validation evidence.
- Including the operator challenges narrative would violate current scope and should be avoided.

## Acceptance criteria

- Clio control package exists and routes all entrypoints to the shared workflow without duplicated fallback logic.
- Codex and Claude Code `generate-docs` entrypoints exist and fail clearly when required workflow references are missing.
- Clio workflow requires selected Athena, Hephaestus, Themis, MCP, command/hook, screenshot, prior-homework style, and author-name context before generating final docs.
- Clio run registry preserves candidate documentation and screenshot outputs under a run folder before canonical copy.
- Clio quality bar requires README author name, required Homework 6 docs, assignment screenshots, draft PR description, privacy-safe evidence, and no operator challenges narrative.
- The plan preserves the Themis boundary: Clio reruns selected tests and documents evidence; it does not silently modify the selected suite.
- `docs/screenshots/operator-sourced/` remains preserved, and Clio copies only necessary evidence screenshots to stable reviewer-facing paths.
- The first Clio implementation validates static routing markers, absence of unresolved draft markers, workflow boundary markers, and staged-file scope before commit.

## Documentation artifact matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Before each commit | `CHANGELOG.md` | Newest-first Homework 6 entry |
| Test cases | Snapshot | Yes | Before implementation | `docs/work-items/2026-06-20-clio-documentation-generator/snapshots/test-cases.snapshot.md` | Static and workflow validation expectations |
| Testing guide delta | Living delta | No | Not applicable | Not applicable | Final `TESTING_GUIDE.md` is Clio output, not a harness delta |
| Operator manual delta | Living delta | No | Not applicable | Not applicable | Final run instructions belong in `HOWTORUN.md` and Clio workflow docs |
| API reference delta | Living delta | No | Not applicable | Not applicable | Final API/MCP details belong in `API_REFERENCE.md` |
| Architecture snapshot | Snapshot | No | Not applicable | Not applicable | Architecture decisions are contained in this spec and generated `ARCHITECTURE.md` output |
| Architecture summary delta | Living delta | No | Not applicable | Not applicable | Final architecture documentation is a canonical Clio output |

## Approval

- Status: Draft
- Superseded by: Not applicable

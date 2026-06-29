# Clio Documentation Generator Test Cases Snapshot

Work ID: `2026-06-20-clio-documentation-generator`
Short ID: `clio-documentation-generator`
Status: Draft
Harness release: `unknown`
Schema: `schema:snapshot.test-cases`

## Static Routing Cases

| Case | Check | Expected result |
|---|---|---|
| Shared workflow references | Inspect Codex, Claude skill, and Claude command wrappers | Every entrypoint points to `agent-control/generate-docs/workflow.md`, `quality-bar.md`, and `run-registry.md` |
| Missing reference behavior | Read wrapper text | Wrappers require stopping and reporting missing shared references instead of using fallback generation |
| Layer portability | Scan Clio workflow text | Clio runs do not require `dev-doc-harness`, Superpowers, hidden chat state, or prior thread memory |
| Themis boundary | Scan Clio workflow and quality bar | Clio reruns and documents selected Themis evidence, but does not silently fork or replace tests |

## Documentation Scope Cases

| Case | Check | Expected result |
|---|---|---|
| Required docs | Inspect Clio quality bar | Requires `README.md`, `HOWTORUN.md`, `ARCHITECTURE.md`, `TESTING_GUIDE.md`, `API_REFERENCE.md`, and `docs/pr-description-draft.md` |
| Author name | Inspect Clio quality bar and workflow | Requires a student display name for README and stops to ask if absent |
| Prior homework author source | Inspect Clio workflow | Uses Homeworks 1-4 README files as the preferred source for `Igor Tanatarov` |
| Prior homework style source | Inspect Clio workflow | Uses Homeworks 1-4 documentation as examples for README structure, quick start, documentation maps, architecture docs, testing docs, and API reference style |
| Layer coverage | Inspect Clio quality bar | Requires documentation of Operator Layer, Homework Automation Layer, and Generated Transaction System Layer |
| PR draft | Inspect Clio workflow | Requires a standalone draft PR description with summary, verification, AI workflow, and screenshot links, without operator challenges narrative |

## Screenshot And Evidence Cases

| Case | Check | Expected result |
|---|---|---|
| Source preservation | Inspect Clio workflow | `docs/screenshots/operator-sourced/` is preserved and never overwritten or pruned |
| Required screenshot mapping | Inspect Clio quality bar | Requires stable reviewer-facing `pipeline-run.png`, `test-coverage.png`, `skill-run-pipeline.png`, `hook-trigger.png`, and `mcp-interaction.png` outputs or explicit missing-capture instructions |
| Screenshot inventory | Inspect run registry | Requires source screenshot inventory, selected screenshot targets, unused source screenshots, and safety notes |
| Privacy safety | Inspect Clio quality bar | Requires safe evidence checks for raw account IDs, raw descriptions, credentials, tokens, and unfiltered metadata |

## Validation Cases

| Case | Command or review | Expected result |
|---|---|---|
| Draft-marker scan | Search Clio files for common unresolved draft markers | No unresolved draft markers |
| Frozen file guard | `git diff -- TASKS.md specification.md docs/agent-runs/final-selection.md agent-control/generate-tests/workflow.md` | No accidental changes |
| Whitespace | `git diff --check` and `git diff --cached --check` | No whitespace errors |
| Staged scope | `git status --short` | Only intended screenshot staging plus Clio planning or implementation files for the current checkpoint |

## Approval

- Status: Draft
- Superseded by: Not applicable

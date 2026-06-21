# Hera Validation Checklist

## Run Identity

- Run ID: `20260621-233836-orchestrate-runs-multi-package-comparison`
- Mode: `compare-set`
- Compared packages: canonical selected Python, latest Python candidate, latest Java candidate
- Selection authorized: no

## Required Checks

| Check | Result |
|---|---|
| Hera skill and mandatory workflow references read | Pass |
| `selection-sets.json` read and canonical ID preserved | Pass |
| `final-selection.md` read | Pass |
| Canonical Clio evidence read | Pass |
| Latest Python Clio evidence read | Pass |
| Latest Java Clio evidence read | Pass |
| Parent Hera child ledgers read for latest Python and Java packages | Pass |
| `.codex/config.toml` read and agent depth values observed | Pass, `max_threads = 8`, `max_depth = 2` |
| `selection-sets.json` remains valid JSON | Pass |
| `.codex/config.toml` parsed for expected agent settings | Pass |
| Child ledger completeness reviewed | Pass by preserved evidence reuse |
| No child deliverables generated in this compare-set parent thread | Pass |
| Comparison file written under `agent-5-orchestrator/comparisons/` | Pass |
| Selection plan clearly marked non-authorizing | Pass |
| Privacy-safe evidence only | Pass |
| Canonical output unchanged | Pass |

## Commands Run

| Command | Result |
|---|---|
| `Get-Content -Raw .agents\skills\orchestrate-runs\SKILL.md` | Pass |
| `Get-Content -Raw agent-control\orchestrate-runs\workflow.md` | Pass |
| `Get-Content -Raw agent-control\orchestrate-runs\quality-bar.md` | Pass |
| `Get-Content -Raw agent-control\orchestrate-runs\run-registry.md` | Pass |
| `Get-Content -Raw docs\agent-runs\selection-sets.json` | Pass |
| `Get-Content -Raw docs\agent-runs\final-selection.md` | Pass |
| `Get-ChildItem docs\agent-runs -Directory | Sort-Object Name` | Pass |
| `Get-Content` of compared Clio metadata, inventories, validation checklists, handoffs, README, and TESTING_GUIDE files | Pass |
| `Get-Content` of latest Python and Java Hera child ledgers and handoffs | Pass |
| `git status --short --branch` | Pass with `.pytest_cache/` permission warning |

## Skipped Commands

| Command | Reason |
|---|---|
| Fresh Python pytest or coverage | Operator authorized documentation-only comparison; preserved Clio evidence was sufficient |
| Fresh Java Maven/JUnit/JaCoCo | Operator authorized documentation-only comparison; preserved Themis/Clio Java evidence was sufficient |
| Child-agent dispatch | Compare mode reuses named preserved package records |

## Limitations

- The latest Python package set was already untracked in the worktree before this comparison.
- The comparison is evidence-based and did not independently rerun runtime pipelines or test suites.
- Java evidence depends on preserved Maven offline/empty-settings validation notes from Themis retry and Clio.


# Agent Run Registry

This folder preserves Homework 6 agent and pipeline runs so repeated generation, troubleshooting, comparison, and final selection do not overwrite useful evidence.

## Sources of Truth

To avoid drift, this registry guide does not restate the full agent workflows or registry rules. Use these shared packages as the authoritative instructions:

| Agent | Package | Registry reference |
|---|---|---|
| Athena (Spec Writer) | `../../agent-control/write-spec/README.md` | `../../agent-control/write-spec/run-registry.md` |
| Hephaestus (Code Generator) | `../../agent-control/generate-code/README.md` | `../../agent-control/generate-code/run-registry.md` |
| Themis (Test Generator) | `../../agent-control/generate-tests/README.md` | `../../agent-control/generate-tests/run-registry.md` |
| Clio (Documentation Generator) | `../../agent-control/generate-docs/README.md` | `../../agent-control/generate-docs/run-registry.md` |
| Hera (Orchestrator) | `../../agent-control/orchestrate-runs/README.md` | `../../agent-control/orchestrate-runs/run-registry.md` |

Selection records use two standing sources:

- Human-readable audit history: `final-selection.md`
- Machine-readable package-set registry: `selection-sets.json`

If this README conflicts with a shared package, update this README and follow the package.

Hera (Orchestrator) run folders use `YYYYMMDD-HHMMSS-orchestrate-runs-<stack>-short-label` and preserve parent orchestration evidence under `agent-5-orchestrator/`. The required child-run ledger is `agent-5-orchestrator/child-runs.md`, and `agent-5-orchestrator/selection-plan.md` remains a proposal until explicit operator selection.

The original Homework 6 repository references `specification-TEMPLATE-hint.md`, but that file is not present in this checkout. Agent 1 runs must record that absence in `run-metadata.md` and use the Task 1 section list plus Homework 3 references as the local template source unless the template file is later added.

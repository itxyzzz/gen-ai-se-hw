# Phase 02 Test Cases Snapshot

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Short ID: `java-stack-and-hera-orchestration`
Status: Draft Review
Artifact type: Immutable pre-implementation snapshot after approval

## Purpose

This snapshot captures the expected behaviors Phase 02 must protect while adding Hera (Orchestrator) as a Homework Automation Layer control surface. It is not a generated test suite and does not authorize running Hera or Java generation.

## Hera Control-Surface Cases

| Case | Setup | Expected result |
|---|---|---|
| Hera package exists | Inspect `agent-control/orchestrate-runs/`. | README, workflow, quality-bar, and run-registry files exist and are mandatory shared references. |
| Codex entrypoint is thin | Inspect `.agents/skills/orchestrate-runs/SKILL.md`. | The wrapper requires reading Hera workflow, quality bar, and run registry before acting; it does not duplicate the full workflow. |
| Claude project skill is thin | Inspect `.claude/skills/orchestrate-runs/SKILL.md`. | The wrapper exposes `/orchestrate-runs` style usage and points to the shared package. |
| Claude legacy command is thin | Inspect `.claude/commands/orchestrate-runs.md`. | The command wrapper points to the shared package and rejects fallback orchestration rules. |
| Skill metadata exists | Inspect `.agents/skills/orchestrate-runs/agents/openai.yaml`. | Metadata uses the existing Codex skill metadata shape and a default prompt that invokes `$orchestrate-runs`. |

## Hera Mode Cases

| Case | Setup | Expected result |
|---|---|---|
| Generate-set mode is defined | Inspect Hera workflow. | `generate-set` creates a preserved stack-specific set through child agents, but Phase 02 does not execute it. |
| Resume-set mode is defined | Inspect Hera workflow. | `resume-set` resumes from preserved Hera run metadata and child-run records. |
| Compare-set mode is defined | Inspect Hera workflow. | `compare-set` compares preserved package sets without changing canonical root files. |
| Select-set mode is guarded | Inspect Hera workflow and quality bar. | `select-set` requires explicit operator selection and inventory-declared copy targets; it does not auto-replace Python with Java. |
| Latest targeting is rejected | Search Hera package for `latest`. | Any match rejects silent latest targeting or refers to model policy; child run selection requires explicit IDs and fingerprints. |

## Run Preservation Cases

| Case | Setup | Expected result |
|---|---|---|
| Hera run ID is stack-aware | Inspect run registry. | Format is `YYYYMMDD-HHMMSS-orchestrate-runs-<stack>-short-label`. |
| Hera run layout is complete | Inspect run registry. | Layout includes `run-metadata.md`, `inputs/source-context.md`, `agent-5-orchestrator/child-runs.md`, comparisons, selection plan, validation checklist, and handoff. |
| Child-run ledger is mandatory | Inspect run registry and workflow. | `child-runs.md` records child agent, mode, stack, run ID, inventory, status, validation, and blocker fields. |
| Selection plan is not authorization | Inspect run registry and workflow. | `selection-plan.md` is a proposal until explicit operator selection; it cannot itself authorize canonical copying. |
| Parent-child traceability is explicit | Inspect workflow child invocation rules. | Every child prompt or handoff includes parent Hera run ID, stack, selected/source run IDs, inventories, selection records, and fingerprints. |

## Config And Fallback Cases

| Case | Setup | Expected result |
|---|---|---|
| Existing agent config is verified | Run TOML parse and assertion commands. | `.codex/config.toml` has `max_threads = 8` and `max_depth = 2`. |
| Phase 02 does not re-add config | Inspect diff. | `.codex/config.toml` changes only if a repair is needed; normal Phase 02 implementation treats the Phase 01 setting as existing. |
| Nested-agent fallback is documented | Inspect Hera workflow and quality bar. | If nested agents fail, Hera records the limitation and uses first-level child dispatch with child-local degraded mode records. |
| Child quality bars remain intact | Inspect workflow. | Fallback does not waive child-agent requirements for Context7 notes, run inventories, validation, privacy, or handoff records. |

## Selection Safety Cases

| Case | Setup | Expected result |
|---|---|---|
| Python remains canonical | Validate `docs/agent-runs/selection-sets.json`. | `canonical_set_id` remains `python-canonical-20260621`. |
| No Java set is added early | Inspect `selection-sets.json`. | Phase 02 does not add a Java candidate or alternate set. |
| Final selection remains human-readable | Inspect `docs/agent-runs/final-selection.md`. | The current Python selection history remains intact; any Hera note clarifies future ownership only. |
| Canonical product files are untouched | Inspect diff. | Runtime code, selected tests, selected reviewer docs, screenshots, canonical spec, and MCP server are unchanged. |
| Selection operations are inventory-driven | Inspect Hera quality bar and workflow. | Canonical copy operations require selected inventories and explicit operator authorization. |

## Privacy And Layer Cases

| Case | Setup | Expected result |
|---|---|---|
| Hera is not a runtime component | Search generated product paths. | Hera does not appear in `integrator.py`, `agents/*.py`, generated Java paths, or MCP tool/resource definitions. |
| Standing guide preserves layer boundary | Inspect `agents.md`. | Hera is listed in the Homework Automation Layer and explicitly excluded from runtime transaction pipeline agents. |
| Privacy-safe reporting is required | Inspect Hera quality bar. | Hera reports safe counts, reason codes, run IDs, paths, and validation status only. |
| No raw sample data is introduced | Search edited control files. | No raw sample account IDs or sample descriptions are added outside `sample-transactions.json`. |

## Deferred Test Cases

The following cases belong to later phases:

- Running Hera in `generate-set` mode.
- Spawning child Athena, Hephaestus, Themis, or Clio generation runs.
- Generating the Java alternate package set.
- Running Maven/JUnit/JaCoCo against a generated Java package.
- Comparing Python canonical and Java alternate evidence.
- Selecting or replacing any canonical root package files.

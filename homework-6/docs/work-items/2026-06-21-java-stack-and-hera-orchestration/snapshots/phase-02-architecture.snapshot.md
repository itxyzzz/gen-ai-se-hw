# Phase 02 Architecture Snapshot

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Short ID: `java-stack-and-hera-orchestration`
Status: Draft Review
Artifact type: Immutable pre-implementation snapshot after approval

## Current Architecture Before Phase 02

After Phase 01, Homework 6 has:

- A selected canonical Python package set recorded in `docs/agent-runs/selection-sets.json`.
- Stack-aware helper and control-surface guidance for future Java alternates.
- `scripts/check_coverage_gate.py` supporting `--stack auto`, `--stack python`, and `--stack java`.
- `.codex/config.toml` with `[agents] max_threads = 8` and `max_depth = 2`.
- Existing child-agent packages for Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator).

Hera (Orchestrator) is still only described in the approved anchor spec. It has no control package, skill entrypoint, command wrapper, run layout, or quality bar yet.

## Phase 02 Target Architecture

Phase 02 adds Hera as a parent orchestration control surface:

```text
Operator Layer
  |
  +-- Hera (Orchestrator)
  |     +-- agent-control/orchestrate-runs/
  |     +-- .agents/skills/orchestrate-runs/
  |     +-- .claude/skills/orchestrate-runs/
  |     +-- .claude/commands/orchestrate-runs.md
  |
  +-- Child Homework Automation Layer agents
        +-- Athena (Spec Writer)
        +-- Hephaestus (Code Generator)
        +-- Themis (Test Generator)
        +-- Clio (Documentation Generator)
```

Hera coordinates child-agent runs through preserved metadata and explicit handoffs. It does not generate transaction-processing runtime code directly and does not become part of the Generated Transaction System Layer.

## Hera Run Model

Hera run folders live beside other preserved agent runs:

```text
docs/agent-runs/YYYYMMDD-HHMMSS-orchestrate-runs-<stack>-short-label/
  run-metadata.md
  inputs/
    source-context.md
    selected-python-set.snapshot.md
    requested-stack-profile.snapshot.md
  agent-5-orchestrator/
    child-runs.md
    comparisons/
    selection-plan.md
    validation-checklist.md
    handoff.md
```

`child-runs.md` is the orchestration ledger. It records child agent, requested mode, stack, run ID, inventory path, validation status, blocker status, and next action.

`selection-plan.md` is a proposal or checklist until the operator explicitly authorizes a selection. It is not a replacement for `docs/agent-runs/final-selection.md` or `docs/agent-runs/selection-sets.json`.

## Selection Ownership

Hera owns future cross-set comparison and selection workflow, but it must preserve these boundaries:

- `docs/agent-runs/final-selection.md` remains the human-readable audit history.
- `docs/agent-runs/selection-sets.json` remains the machine-readable package-set registry.
- Child-agent inventories remain the source for canonical copy targets.
- Python remains canonical until an explicit operator selection changes `canonical_set_id`.
- A Java package set can be proposed or preserved in a later phase, but Phase 02 does not create it.

## Nested-Agent Fallback

The approved config target is already present:

```toml
[agents]
max_threads = 8
max_depth = 2
```

Phase 02 records how Hera should behave if runtime nested-agent support is unavailable:

1. Hera records the observed limitation in `run-metadata.md` and `agent-5-orchestrator/handoff.md`.
2. Hera still dispatches Athena, Hephaestus, Themis, and Clio as first-level child agents when first-level dispatch is available.
3. Each child agent records that nested sub-agents were unavailable and continues locally only when its scope can still meet its quality bar.
4. Hera does not mark the set complete until child validations, inventories, and handoffs exist or blockers are explicitly recorded.

## Java Child-Run Expectations

Phase 02 does not run Java generation, but Hera's workflow must know how to pass Phase 01's Java expectations to child agents:

- Athena (Spec Writer) Java specs must name Maven, `pom.xml`, Java source/test paths, `BigDecimal`, Jackson or equivalent JSON handling, JUnit Jupiter, JaCoCo, and stack-neutral result JSON.
- Hephaestus (Code Generator) Java runs must use Context7 and preserve at least two query notes. Good Java topics include `BigDecimal`, Jackson, Maven layout, JUnit Jupiter, and JaCoCo.
- Themis (Test Generator) Java runs validate from run-local workspaces with Maven/JUnit and JaCoCo evidence.
- Clio (Documentation Generator) Java docs describe Java as an alternate set unless selection metadata explicitly marks it canonical.

Context7 planning notes on 2026-06-21:

- `/websites/junit_current` supports JUnit Jupiter with Maven Surefire/Failsafe for test execution guidance.
- `/websites/jacoco_jacoco_trunk_doc` supports JaCoCo Maven `check` goal guidance for build-blocking coverage thresholds such as `0.80`.

## Boundaries

Phase 02 may change Operator Layer and Homework Automation Layer control surfaces. It must not:

- Run Hera.
- Run child generation agents.
- Generate Java outputs.
- Add a Java set to `selection-sets.json`.
- Replace selected Python canonical outputs.
- Modify generated runtime transaction-system code.
- Modify selected tests, selected reviewer docs, screenshots, or `mcp/server.py`.
- Change the runtime JSON or MCP public contract.

## Rollback Shape

If Phase 02 implementation causes confusion or breaks helper validation, rollback should remove or repair the Hera control-surface additions while preserving:

- Phase 01 stack-aware helper changes.
- The current canonical Python package set.
- The approved anchor spec and Phase 01 records.
- Existing generated product files and documentation.

Because Phase 02 does not create Java package sets or canonical replacements, rollback should not require deleting Java run folders or restoring root product files.

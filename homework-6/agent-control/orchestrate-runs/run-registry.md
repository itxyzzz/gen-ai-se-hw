# Orchestrate Runs Run Registry

This file defines preservation, comparison, selection-plan, and evidence rules for Homework 6 Hera (Orchestrator) runs.

## Run IDs

Use this format for normal stack-specific Hera runs:

```text
YYYYMMDD-HHMMSS-orchestrate-runs-<stack>-short-label
```

Examples:

```text
20260621-140000-orchestrate-runs-java-alternate
20260621-153000-orchestrate-runs-python-comparison
```

Use `python` or `java` for `<stack>` when the run targets one stack. Use a short label that explains the purpose, such as `alternate`, `comparison`, `selection-review`, `resume`, or `repair`.

## Required Layout

Preserve each meaningful Hera run under:

```text
homework-6/docs/agent-runs/HERA_RUN_ID/
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

Additional comparison files may be added under `agent-5-orchestrator/comparisons/`, for example:

```text
agent-5-orchestrator/comparisons/python-vs-java-summary.md
agent-5-orchestrator/comparisons/child-run-readiness.md
```

## Run Metadata

`run-metadata.md` must record:

- Hera run ID.
- Mode: `generate-set`, `resume-set`, `compare-set`, or `select-set`.
- Selected or requested stack.
- Start time and orchestration tool.
- Operator instruction and whether selection was authorized.
- Current canonical package-set ID.
- Source package-set ID when applicable.
- Child-agent plan and nested-agent support status.
- First-level child-agent dispatch availability and expected dispatch mechanism.
- Agent config values when available, including `max_threads` and `max_depth`.
- Pre-existing dirty git state relevant to the run.
- Privacy and canonical-output safety notes.

## Inputs

`inputs/source-context.md` must list every source artifact Hera read, including:

- `agents.md`.
- `docs/agent-runs/selection-sets.json`.
- `docs/agent-runs/final-selection.md`.
- Named child inventories.
- Relevant child-agent control packages.
- Operation helper guidance when runnable evidence is involved.
- Assignment and standards files when relevant.

`inputs/selected-python-set.snapshot.md` preserves the current canonical Python set when the run compares against it or protects it from replacement.

`inputs/requested-stack-profile.snapshot.md` records stack-profile guidance used for the requested package set. It should contain stack decisions and command families, not raw sample transaction payloads.

## Child-Run Ledger

`agent-5-orchestrator/child-runs.md` is mandatory. It is the ledger for child Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator) work.

For each child entry, record:

- Child agent name and role.
- Requested child mode.
- Selected or requested stack.
- Parent Hera run ID.
- Intended dispatch mechanism, such as first-level child agent, reused prior run, blocked before dispatch, or operator-authorized non-Hera manual recovery.
- Observed dispatch mechanism, including whether the child ran as a first-level child agent, ran with degraded child-local execution because nested child sub-agents were unavailable, or was blocked.
- Child run ID.
- Child run folder path.
- Source run IDs.
- Package-set ID when one exists.
- Inventory path.
- Selection record path.
- Source and current spec fingerprints when applicable.
- Validation commands and status.
- Context7 notes status when required.
- Blockers.
- Next action.

If a child run is skipped, record the explicit reason. Acceptable reasons include out-of-scope mode, missing operator authorization, prior selected package reuse, or a blocker that stops the sequence.

For `generate-set`, missing or non-child observed dispatch for Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), or Clio (Documentation Generator) is a blocker unless the entry is explicit prior selected package reuse outside a new generation stage. Do not mark the set preserved or complete when later child deliverables were generated in the parent Hera thread.

## Comparisons

Comparison files under `agent-5-orchestrator/comparisons/` must compare evidence without mutating canonical files.

A comparison must state:

- Compared package sets or child runs.
- Selected stacks.
- Source inventories and selection records.
- Validation evidence reviewed.
- Assignment completeness.
- Privacy and result-contract safety.
- Known blockers or limitations.
- Recommendation and rationale.
- Whether canonical output remained unchanged.

## Selection Plan

`agent-5-orchestrator/selection-plan.md` is a proposal or checklist until explicit operator selection.

It may describe:

- Candidate package set or child run recommended for selection.
- Required inventory-declared copy targets.
- Files to remove from prior selected inventories.
- `selection-sets.json` updates.
- `final-selection.md` updates.
- Validation and privacy checks that must pass first.
- Rollback notes.

It must also state that it is not authorization to copy canonical files. Selection requires a separate explicit operator instruction naming the target package set or run.

## Validation Checklist

`agent-5-orchestrator/validation-checklist.md` must capture:

- JSON validation for `selection-sets.json`.
- TOML validation for `.codex/config.toml`.
- Agent-depth assertion results when available.
- Child ledger completeness.
- Dispatch-mechanism completeness for Athena, Hephaestus, Themis, and Clio.
- Evidence review confirming no child deliverables were generated in the main orchestration thread during `generate-set`.
- Comparison or selection-plan status.
- Privacy scan results.
- Protected canonical-output diff review.
- Any skipped command and its reason.

## Handoff

`agent-5-orchestrator/handoff.md` must include:

- Assigned Hera scope.
- Mode and selected or requested stack.
- Child run statuses.
- Per-child dispatch status for Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator): first-level child agent, degraded child-local execution, reused prior run, or blocked.
- Files inspected or changed.
- Commands and tests run.
- Assumptions.
- Nested-agent support or degraded-mode observations.
- Uncertainty or residual risk.
- Recommended next prompt.

When sub-agents are used, include total count, roles, whether they ran concurrently or in waves, context strategy, model policy intent, observed model details when available, and inherited-context behavior when known.

## Canonical Output Rule

Hera run folders are evidence snapshots. Canonical generated product files live at the Homework 6 root only for selected packages.

During `compare-set` and ordinary `generate-set` preservation, Hera must not edit:

- `specification.md`.
- `integrator.py`.
- `agents/*.py`.
- `tests/*.py`.
- `research-notes.md`.
- `README.md`, `HOWTORUN.md`, `ARCHITECTURE.md`, `TESTING_GUIDE.md`, or `API_REFERENCE.md`.
- `docs/pr-description-draft.md`.
- Stable screenshots under `docs/screenshots/`.
- `mcp/server.py`.

Canonical files are changed only through explicit inventory-driven selection or a separately approved Operator Layer repair.

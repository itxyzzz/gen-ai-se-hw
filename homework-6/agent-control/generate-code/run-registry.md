# Generate Code Run Registry

This file defines preservation, comparison, and evidence rules for Homework 6 Hephaestus (Code Generator) runs.

## Run IDs

Use this format for normal Python code-generation runs:

```text
YYYYMMDD-HHMMSS-generate-code-python-short-label
```

Examples:

```text
20260618-140000-generate-code-python-primary
20260618-153000-generate-code-python-repair
```

Use a short label that explains the purpose, such as `primary`, `repair`, `context7-retry`, or `privacy-fix`.

## Required Layout

Preserve each meaningful Hephaestus run under:

```text
homework-6/docs/agent-runs/RUN_ID/
  run-metadata.md
  inputs/
    source-context.md
  agent-2-code/
    outputs/
      inventory.md
    handoffs/
      sub-agent-plan.md
    review/
    research-notes.md
    validation-checklist.md
    handoff.md
  comparison.md
```

Additional handoff files are encouraged when sub-agents are used, for example:

```text
agent-2-code/handoffs/task-01-shared-directories-handoff.md
agent-2-code/handoffs/task-05-validator-handoff.md
agent-2-code/review/final-integration-review.md
```

## Evidence Rules

Run folders are evidence snapshots. Canonical generated product files live at the Homework 6 root, such as `integrator.py`, `agents/*.py`, `tests/*.py`, `research-notes.md`, and runtime `shared/` outputs.

A run folder must preserve:

- Source context read by the run.
- Source Athena (Spec Writer) run ID, canonical source spec path, and SHA-256 fingerprint of the selected `specification.md`.
- Context7 query notes and limitations.
- Complete candidate generated output package under `agent-2-code/outputs/`.
- Output inventory listing selectable files, canonical targets, fingerprints, and excluded runtime/tool paths.
- Planned and de-facto sub-agent strategy.
- Sub-agent handoffs or reason sub-agents were not used.
- Validation commands and actual results.
- Known risks and exact next prompt when the run pauses.

Do not copy a run-local agent guide over `homework-6/agents.md`. Update standing guidance only through explicit Operator Layer maintenance.

Do not overwrite canonical `specification.md`; Hephaestus consumes it and does not select specs.

Do not place runtime or tool-output folders under selectable code packages. Exclude `shared/`, `archive/`, `.coverage`, `__pycache__/`, and `.pytest_cache/` from `agent-2-code/outputs/`.

## Sub-Agent Evidence

When sub-agents are used, preserve the plan and reports before final integration.

Each sub-agent handoff must include:

- Assigned scope.
- Context strategy and inputs.
- Model policy and policy-relative model/reasoning intent when known.
- Files inspected or changed.
- Commands and tests run.
- Assumptions.
- Uncertainty or residual risk.
- Recommended next step.

Record whether sub-agents ran concurrently or in waves. The configured Homework 6 cap is `agents.max_threads = 8`; runs with more implementation slices should use waves.

The orchestration thread must preserve a final integration summary that explains accepted changes, rejected recommendations, conflict resolution, validation evidence, and de-facto sub-agent use.

## Comparison Criteria

When comparing two or more Hephaestus runs, assess:

- Coverage of selected `specification.md` objectives and low-level tasks.
- Context7 provenance and whether the applied insights are visible in generated code.
- Pipeline correctness against all eight sample transactions.
- Privacy and audit safety.
- Decimal money handling and JSON strictness.
- Scope control against Task 3, Task 4, and Task 5 deliverables.
- Test and validation evidence.
- Sub-agent coordination quality and final integration coherence.
- Diff cleanliness and absence of generated noise.

`comparison.md` must state the recommended run, rationale, rejected alternatives, unresolved risks, and whether canonical product files were updated.

## Canonical Output Rule

Hephaestus writes generated product files to `agent-2-code/outputs/` first. Preserved run artifacts are evidence snapshots, and `agent-2-code/outputs/` is the candidate package used for selection.

The first successful code-generation run is auto-selected only when no selected code package exists. Auto-selection copies files from `agent-2-code/outputs/` to their inventory-declared canonical targets and records the selection.

Later selections require explicit operator selection. Before copying a replacement package, remove the canonical targets declared by the prior selected inventory so stale selected files do not survive accidentally. Do not remove paths outside the inventory without explicit operator approval.

Selection records must include the selected Hephaestus run ID, selected Athena source run ID, source spec SHA-256, inventory path, selected files, canonical targets, rationale, operator, and excluded runtime/tool paths.

Canonical `research-notes.md` must include the Context7 entries required by Task 2. Run-local notes may mirror, expand, or link to that canonical file.

Keep Task 2 implementation separate from later Task 3 hooks/commands, Task 4 custom MCP server/config, and Task 5 docs/screenshots unless the operator explicitly starts that later work.

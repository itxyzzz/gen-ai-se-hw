# Fix Athena Spec Layering Spec

Work ID: `2026-06-17-fix-athena-spec-layering`
Short ID: `fix-athena-spec-layering`
Status: Draft
Harness release: `unknown`
Schema: `schema:spec.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `rule:lifecycle.documentation-matrix`, `rule:quality.spec-handoff`

## Goal

Repair Homework 6 Agent 1 so Athena (Spec Writer) is configured to generate a detailed technical specification for the transaction-processing system, not a self-referential specification for the homework automation harness, and preserve the failed first selected spec as rejected evidence for a later clean generation run.

## Scope

- Create a direct transaction-system brief for Athena (Spec Writer) that is separate from operator-facing `TASKS.md`.
- Add a durable layer glossary distinguishing the Operator Layer, Homework Automation Layer, Generated Transaction System Layer, runtime transaction pipeline components, and executor sub-agents.
- Add the Homework Automation Layer identity labels:
  - Athena / Spec Writer / write-spec
  - Hephaestus / Code Generator
  - Themis / Test Generator
  - Clio / Documentation Generator
- Add the rule that technical prompts and instructions must use `Athena (Spec Writer)` until the glossary is loaded, and must not rely on `Athena` alone to carry responsibility.
- Update the `write-spec` workflow and quality bar so Athena reads the transaction-system brief as its assignment input and does not require `TASKS.md` during ordinary generation.
- Update Athena's generation quality gates to reject meta-layer leakage such as `dev-doc-harness`, freeze gates, agent-run preservation, canonical-copy mechanics, slash-command creation, hook setup, MCP configuration setup, README/PR support, and other operator deliverables when they appear as product-spec requirements.
- Fix the low-level task standard so generated tasks are implementation-ready slices of the transaction-processing software, not one card per Homework Automation Layer agent.
- Mark the currently selected `20260617-180458-write-spec-python-primary` spec/run as failed or superseded for wrong target while preserving the original run folder and canonical evidence.
- Document that the replacement Athena generation and selection must happen later in a clean Homework 6-root thread.

## Non-scope

- Do not edit `homework-6/TASKS.md`; it remains frozen and operator-facing.
- Do not rewrite or hide the existing failed run folder under `homework-6/docs/agent-runs/20260617-180458-write-spec-python-primary/`.
- Do not redesign all four Homework Automation Layer agents in this work item beyond shared vocabulary and instructions needed for Athena's corrected output.
- Do not implement the transaction-processing system, tests, hooks, MCP server, screenshots, or final documentation package in this work item.
- Do not run Athena (Spec Writer), create a replacement run folder, replace `homework-6/specification.md`, or select a replacement spec in this work item.
- Do not make runtime transaction pipeline components or executor sub-agents use Greek identity labels.
- Do not introduce `dev-doc-harness` or Superpowers requirements into generated homework automation agents or generated transaction-system specs.

## Current state

`homework-6/TASKS.md` is a mixed operator assignment: it defines the four homework automation agents, the generated transaction-processing system, and one-time plus-column control-surface requirements such as skills, Context7, hooks, MCP configuration, README name requirements, screenshots, and PR description evidence.

The selected `homework-6/specification.md` is associated with run `20260617-180458-write-spec-python-primary`. It has useful transaction-processing content, but it is still structurally wrong because the low-level task section specifies the homework automation process itself. It includes an Agent 1 task for creating and preserving a spec run, canonical copy behavior, final-selection behavior, run folders, and other meta-process requirements. That makes Agent 2's input contaminated: it asks a code-generation agent to build or reason about the machinery that produced the spec, instead of only the transaction-processing software.

The current `agent-control/write-spec/workflow.md` tells Athena to read `TASKS.md` directly. The current `quality-bar.md` says low-level tasks should include one entry per meta-agent. Both instructions reinforce the layer confusion. The current `agents.md` has useful rules, but its "Quality Gates Without Harness" and role table still use generic agent naming and do not clearly isolate the Greek identity labels or prevent harness leakage into generated agent flows.

Homework 3's selected specification is the detail-depth reference. It specifies product scope, actors, state machines, data concepts, commands, edge cases, failure modes, verification mapping, performance assumptions, and many implementation-ready low-level slices. Athena's future replacement spec should borrow that depth and traceability style, but it must specify a transaction-processing pipeline rather than an EU dispute-intake product.

## Proposed behavior

`TASKS.md` remains the frozen assignment and operator source. A new brief under `homework-6/agent-control/write-spec/` becomes Athena's direct input for the transaction-processing product. The brief should copy the transaction-processing system description from `TASKS.md` as closely as possible while removing or clarifying layer ambiguities. It should describe what the generated system must do, what sample data exists, what file protocol it must use, which runtime components are required, which constraints are non-negotiable, and which homework plus-column requirements belong outside the product spec.

`homework-6/agents.md` becomes the standing layer glossary. It should define:

- Operator Layer: the human/Codex layer that uses harness planning to create and maintain the homework automation surfaces.
- Homework Automation Layer: the four deliverable automation agents with Greek identities and functional roles.
- Generated Transaction System Layer: the actual code, tests, docs, MCP server, commands, and runtime artifacts produced by the homework automation agents.
- Runtime transaction pipeline components: functional components such as Transaction Validator, Fraud Detector, Settlement Processor, Compliance Checker, Reporting Agent, and Integrator.
- Executor sub-agents: optional worker agents used by a tool runtime to decompose work; they should use functional names and should not be confused with the four homework automation agents.

Athena (Spec Writer) should generate `specification.md` for the Generated Transaction System Layer only. The generated spec may mention that Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator) will consume the spec, but it must not include task cards instructing those automation agents to create the spec workflow, run registry, skill wrappers, hooks, MCP configuration, screenshots, README, PR support, or other plus-column/operator deliverables as part of the transaction-system specification.

The low-level task section should use product implementation slices. Some slices may correspond to runtime transaction pipeline components, but the list must also cover non-agent software concerns such as:

- Project/package structure.
- Shared JSON envelope and file movement semantics.
- Input loading and deterministic run reset.
- Integrator orchestration.
- Decimal money parsing and serialization.
- Currency and required-field validation.
- Fraud/risk scoring.
- Settlement/final-outcome writing.
- Result summary and audit-safe reporting.
- Redaction and structured audit events.
- Validator dry-run behavior for `/validate-transactions`.
- Test seams and temporary-directory isolation.
- MCP-readable result shapes.
- Error handling, idempotent reruns, and failure recovery.

The failed first selected spec should be explicitly marked as failed or superseded in selection evidence before any later replacement is selected. The preserved failed run should remain readable for audit and comparison. The later Athena run is intentionally outside this work item and should be triggered in a separate clean Homework 6-root thread after the repaired control surface is frozen and implemented.

## Interfaces and data

Expected files to create:

- `homework-6/agent-control/write-spec/transaction-system-brief.md`

Expected files to update:

- `homework-6/agents.md`
- `homework-6/agent-control/write-spec/README.md`
- `homework-6/agent-control/write-spec/workflow.md`
- `homework-6/agent-control/write-spec/quality-bar.md`
- `homework-6/agent-control/write-spec/stack-profiles.md`
- `homework-6/docs/agent-runs/final-selection.md`
- `homework-6/CHANGELOG.md`

No public runtime API, persistence schema, or transaction-system code should change during the control-surface repair. Any later generated transaction-system code remains governed by the replacement `specification.md`.

## Risks

- The new brief could over-specify the transaction system and take over Athena's job. Mitigation: keep the brief close to `TASKS.md`, limit it to source facts and cleaned constraints, and leave architecture decisions, detailed task decomposition, and stack-specific implementation design to Athena.
- The repaired quality bar could become too strict and reject valid references to required downstream deliverables. Mitigation: distinguish allowed product constraints from banned operator-process instructions. Product specs may specify result shapes that enable MCP later; they should not instruct Agent 1 to create MCP config or run-folder machinery.
- The Greek identity labels could obscure responsibility if used alone. Mitigation: require paired labels such as `Athena (Spec Writer)` in prompts and technical instructions until the glossary is loaded.
- A later replacement run could overwrite evidence or hide the failed run if the failure marker is vague. Mitigation: mark the failed selection in this work item, preserve the old run folder, and update final-selection history instead of deleting records.
- Existing dirty work in `homework-6/agent-control/write-spec/README.md` could be user-owned. Mitigation: inspect the diff before editing and work with any existing changes without reverting them.

## Acceptance criteria

- `TASKS.md` is unchanged by this work item.
- A new `transaction-system-brief.md` exists and is direct Athena input for the transaction-processing system.
- `agents.md` defines the layer model and Greek identity labels exactly for the Homework Automation Layer, and states that runtime pipeline components and executor sub-agents keep functional names.
- `agents.md` and Athena prompts require `Athena (Spec Writer)` rather than bare `Athena` in technical instructions until the glossary is loaded.
- `workflow.md` no longer requires Athena to read `TASKS.md` for normal generation; it requires the transaction-system brief, sample data, standing agent guide, stack profile, quality bar, and Homework 3 reference.
- `quality-bar.md` rejects meta-layer leakage and requires the generated spec to target the transaction-processing system only.
- `quality-bar.md` defines low-level task cards as transaction-system implementation slices, including non-agent software concerns, rather than one card per homework automation agent.
- `docs/agent-runs/final-selection.md` marks run `20260617-180458-write-spec-python-primary` or its canonical output as failed/superseded for wrong target and records that replacement generation is a separate follow-up.
- `homework-6/specification.md` is not replaced by this work item.
- Homework 6 changelog records the control-surface repair and failed-spec marking before commit.

## Documentation artifact matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Before each commit | `homework-6/CHANGELOG.md` | Newest-first Homework 6 entry for the planning freeze and later implementation increment |
| Test cases | Snapshot | No | Not applicable | `homework-6/docs/work-items/2026-06-17-fix-athena-spec-layering/snapshots/test-cases.snapshot.md` | This is a documentation/control-surface repair; validation is static review and follow-up generation instructions |
| Testing guide delta | Living delta | No | Not applicable | `homework-6/docs/work-items/2026-06-17-fix-athena-spec-layering/deltas/testing-guide.delta.md` | No long-lived test guide exists yet for the generated transaction system |
| Operator manual delta | Living delta | No | Not applicable | `homework-6/docs/work-items/2026-06-17-fix-athena-spec-layering/deltas/operator-manual.delta.md` | Standing instructions are updated directly in `agents.md` and write-spec control docs |
| API reference delta | Living delta | No | Not applicable | `homework-6/docs/work-items/2026-06-17-fix-athena-spec-layering/deltas/api-reference.delta.md` | No runtime API is changed in this work item |
| Architecture snapshot | Snapshot | No | Not applicable | `homework-6/docs/work-items/2026-06-17-fix-athena-spec-layering/snapshots/architecture.snapshot.md` | The layer model is captured in this spec and then in `agents.md` |
| Architecture summary delta | Living delta | No | Not applicable | `homework-6/docs/work-items/2026-06-17-fix-athena-spec-layering/deltas/architecture-summary.delta.md` | No repository-level architecture summary exists for this homework stage |

## Approval

- Status: Draft
- Superseded by: None

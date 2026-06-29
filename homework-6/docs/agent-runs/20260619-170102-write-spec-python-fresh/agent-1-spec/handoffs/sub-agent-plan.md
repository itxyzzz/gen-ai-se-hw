# Sub-Agent Plan

Run ID: `20260619-170102-write-spec-python-fresh`

Selected stack: `python`

Mode: `generate`

Integration owner: the orchestration thread.

## Runtime Capability

Codex multi-agent tooling is available in this session. The run will use executor sub-agents because the `write-spec` workflow requires them for normal generation.

Maximum concurrent sub-agents for this run: 3 active agents at a time, staying below the Homework 6 project limit of 8.

## Model And Reasoning Intent

Requested model policy: use the strongest available Codex profile exposed by the current UI for orchestration, integration drafting, privacy/audit review, and final review, with high or extra-high reasoning where the UI exposes it.

Observed runtime limitation: the orchestration thread can request sub-agent reasoning effort, but exact UI model labels are not guaranteed in run metadata. Sub-agents will inherit the parent model unless a bounded role clearly benefits from an explicit high-reasoning request.

Compensation: each sub-agent receives curated context, a narrow scope, required output path, and explicit uncertainty/residual-risk fields. The orchestrator owns integration and validation.

## Planned Roles

| Role | Scope | Context strategy | Output artifact | Schedule |
|---|---|---|---|---|
| Domain research sub-agent | Research banking-pipeline simulation constraints, ISO 4217-style currency assumptions, precise decimal money handling, privacy/audit constraints, and unsupported compliance claims. | Curated prompt with transaction-system brief, sample fixture summary, Python stack profile, and research/source expectations. | `agent-1-spec/handoffs/domain-research-handoff.md` plus research-note entries. | Research wave. |
| Objectives architect sub-agent | Shape high-level objective and 4-5 mid-level objectives for transaction-system fit, testability, stack specificity, and product-only scope. | Curated prompt with source context, domain-research summary when available, quality bar, and Homework 3 shape reference. | `agent-1-spec/handoffs/objectives-handoff.md`. | Objectives wave after research. |
| Low-level task decomposition sub-agent | Produce implementation-ready Python task cards with exact files, functions, behaviors, edge cases, acceptance criteria, and verification. | Curated prompt with Python stack profile, objectives handoff, quality bar, brief, and sample transaction summary. | `agent-1-spec/handoffs/low-level-tasks-handoff.md`. | Task wave after objectives. |
| Final review sub-agent | Review completed candidate package for required sections, stack specificity, privacy, research provenance, task-card executability, meta-layer leakage, and handoff quality. | Curated prompt with all candidate outputs, research notes, validation checklist, and quality bar. | `agent-1-spec/review/final-review.md`. | Review wave after integration draft. |

Optional specialized reviewers may be added only if integration uncovers risk that the four required roles do not cover.

## Integration Plan

1. Create run metadata, source context, and this sub-agent plan.
2. Run the research wave and preserve the domain handoff.
3. Use the research handoff to drive objectives architecture.
4. Use objectives and quality-bar constraints to drive low-level task decomposition.
5. Integrate handoffs into candidate `specification.md`, supporting docs, research notes, validation checklist, and completion handoff.
6. Run final review, repair accepted findings, and update validation notes.
7. Preserve the run only. Do not copy to canonical `specification.md` because a selected spec already exists.

## Required Handoff Fields

Every sub-agent handoff must include assigned scope, files or context inspected, sources or commands used, assumptions, uncertainty, residual risks, and recommended next step.


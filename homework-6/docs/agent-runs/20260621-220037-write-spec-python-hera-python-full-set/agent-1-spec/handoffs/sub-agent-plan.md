# Sub-Agent Plan

## Run Identity

- Run ID: `20260621-220037-write-spec-python-hera-python-full-set`
- Stack: `python`
- Mode: `generate`
- Parent Hera run ID: `20260621-215717-orchestrate-runs-python-full-set`
- Execution role: Athena (Spec Writer), first-level Hera child agent

## Nested-Agent Availability

Nested sub-agent tooling is available in this Codex runtime through `multi_agent_v1`. This run will use required nested phases. No degraded main-thread-only substitution is planned.

## Model Policy And Reasoning Intent

The run follows the write-spec workflow's policy-relative model guidance:

- Orchestration, integration drafting, privacy/audit decisions, and final review: latest strongest Codex profile exposed by the UI, high or extra-high reasoning.
- Domain research, objectives architecture, and low-level task decomposition: strong current Codex profile, high reasoning.
- If exact model labels are hidden by the runtime, record that limitation and compensate with explicit handoffs, source lists, validation checklists, and final review.

Observed current runtime exposes sub-agent spawning and optional model overrides. This run inherits the parent model for sub-agents rather than hard-coding a dated model ID.

## Concurrency Plan

- Maximum concurrent nested sub-agents planned: 2 during independent context work when safe.
- Required phase order from workflow:
  1. Domain research sub-agent.
  2. Objectives architect sub-agent after domain research handoff exists.
  3. Low-level task decomposition sub-agent after objectives and stack profile are stable.
  4. Final review sub-agent after candidate outputs and validation notes exist.
- Integration owner: this Athena orchestration thread. Sub-agents produce handoffs; the orchestration thread owns final candidate files, repairs, validation, and completion report.

## Planned Roles

| Role | Scope | Context strategy | Output artifact | Scheduling |
|---|---|---|---|---|
| Domain research sub-agent | Banking-pipeline domain rules, privacy/audit constraints, ISO-currency assumptions, unsupported compliance claims, Python stack documentation notes. | Curated source-context summary, transaction-system brief, safe sample summary, stack profile, quality bar, and Context7 query results. | `agent-1-spec/handoffs/domain-research-handoff.md` plus accepted entries in `agent-1-spec/research-notes.md`. | First wave. |
| Objectives architect sub-agent | Shape one high-level objective and 4-5 concrete mid-level objectives for Python transaction-system fit and testability. | Curated brief, safe sample summary, domain-research handoff, stack profile, and product-only boundary. | `agent-1-spec/handoffs/objectives-handoff.md`. | Second wave. |
| Low-level task decomposition sub-agent | Produce implementation-ready Python task-card slices with exact files, functions, edge cases, acceptance criteria, and verification. | Stack profile, quality bar, objectives handoff, domain handoff, safe sample summary, and product-only boundary. | `agent-1-spec/handoffs/low-level-tasks-handoff.md`. | Third wave. |
| Final review sub-agent | Review completed candidate package for required sections, stack specificity, privacy, research provenance, task executability, meta-layer leakage, and handoff quality. | Candidate specification, support docs, research notes, validation checklist draft, and all handoffs. | `agent-1-spec/review/final-review.md`. | Review wave after integration draft. |

## Inputs To Avoid

Sub-agents must not copy raw sample account identifiers, descriptions, full metadata payloads, credentials, hidden prompts, final-selection file contents beyond supplied IDs, or canonical-copy mechanics into product requirements.

## Acceptance For Nested Handoffs

Each handoff must include:

- Assigned scope.
- Files or context inspected.
- Sources or commands used.
- Assumptions and uncertainty.
- Residual risks.
- Recommended next step.


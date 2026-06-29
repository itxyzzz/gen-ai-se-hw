# Sub-Agent Plan

Run ID: `20260618-003908-write-spec-python-replacement`

Selected stack: `python`

Integration owner: orchestration thread.

Maximum planned concurrency: three active sub-agents at a time. The run uses waves because later objectives and low-level task decomposition depend on the domain research handoff.

## Model And Reasoning Prescription

- Orchestration, integration drafting, final review, privacy/audit review, and architecture-sensitive decisions: strongest exposed Codex profile with high or extra-high reasoning.
- Domain research, objectives architecture, and low-level task decomposition: strong current Codex profile with high reasoning.
- Runtime limitation: exact model label and reasoning setting are not directly visible in the shell context. Spawned agents inherit the parent model unless the runtime applies its own defaults.

## Planned Roles

| Role | Scope | Context Strategy | Output Artifact | Scheduling |
|---|---|---|---|---|
| Domain research sub-agent | Research conservative banking-pipeline rules, ISO 4217-style currency validation, precise decimal money, audit/privacy constraints, and unsupported compliance claims. | Curated prompt with transaction brief, sample summary, stack profile, privacy rules, and quality bar. Use current web/official documentation where useful and record fallback limitations. | `agent-1-spec/handoffs/domain-research-handoff.md` plus entries for `agent-1-spec/research-notes.md`. | Wave 1 |
| Objectives architect sub-agent | Shape high-level objective and 4-5 testable mid-level objectives for a Python transaction pipeline. | Curated prompt with source context, transaction brief, sample behavior, domain research handoff, and product-only boundary. | `agent-1-spec/handoffs/objectives-handoff.md`. | Wave 2 |
| Low-level task decomposition sub-agent | Produce implementation-ready product task cards with exact prompts, files, functions, details, edge cases, acceptance criteria, and verification. | Curated prompt with selected stack profile, objectives handoff, quality bar, transaction brief, sample behavior, and privacy/audit rules. | `agent-1-spec/handoffs/low-level-tasks-handoff.md`. | Wave 3 |
| Final review sub-agent | Review the completed candidate package for objective clarity, stack specificity, privacy/audit, research provenance, task-card executability, product-only boundary, and handoff quality. | Curated prompt with all candidate outputs, research notes, validation checklist draft, and source context. | `agent-1-spec/review/final-review.md`. | Wave 4 |

## Assumptions And Guardrails

- Canonical `specification.md` is not overwritten during this generate run because it already exists, even though it is marked failed/superseded.
- Supporting docs remain run evidence unless the operator explicitly selects them later.
- The standing `agents.md` is not regenerated or overwritten.
- The generated product spec may prepare result shapes for future MCP status tools but must not instruct product builders to configure MCP servers before `mcp/server.py` exists.

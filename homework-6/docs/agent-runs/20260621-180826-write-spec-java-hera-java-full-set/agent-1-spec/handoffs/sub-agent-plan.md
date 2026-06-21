# Sub-Agent Plan

## Run Identity

- Run ID: `20260621-180826-write-spec-java-hera-java-full-set`
- Stack: `java`
- Mode: `generate`
- Parent Hera run ID: `20260621-180512-orchestrate-runs-java-full-set`
- Package-set ID under construction: `java-candidate-20260621-180512`
- Selection authorized: no

## Runtime Availability

Nested executor sub-agents are available in this Codex runtime through the multi-agent tool surface. This run will use the required four roles and preserve their handoffs.

## Model And Reasoning Intent

The requested policy is the strongest currently exposed Codex-family profile practical for architecture-sensitive integration and review, with high reasoning for all required executor roles. Exact UI model labels are not surfaced in the preserved artifacts; spawned agents inherit the parent model unless an explicit stronger override is needed. The orchestration thread remains integration owner.

## Planned Roles And Outputs

| Role | Scope | Context strategy | Output artifact | Wave |
|---|---|---|---|---|
| Domain research sub-agent | Research educational banking pipeline rules, audit/privacy constraints, ISO 4217-style currency assumptions, Java money/JSON/library notes, and unsupported compliance claims. | Curated local sources: transaction-system brief, sample-data observations, stack profile, privacy rules, quality bar, and explicit web/Context7 fallback expectation. | `agent-1-spec/handoffs/domain-research-handoff.md` and research-note entries. | Research |
| Objectives architect sub-agent | Shape one high-level objective and 4-5 measurable mid-level objectives for the Java transaction system. | Curated local sources plus domain research handoff when available. | `agent-1-spec/handoffs/objectives-handoff.md`. | Objectives |
| Low-level task decomposition sub-agent | Produce Java-specific implementation slices with exact files, classes, methods, edge cases, acceptance criteria, and verification. | Curated artifacts: Java stack profile, quality bar, transaction-system brief, objectives handoff, sample-data observations. | `agent-1-spec/handoffs/low-level-tasks-handoff.md`. | Low-level tasks |
| Final review sub-agent | Review completed candidate package for stack specificity, product-only boundary, privacy, research provenance, task-card executability, and handoff quality. | Candidate outputs, research notes, validation checklist, and all handoffs. | `agent-1-spec/review/final-review.md`. | Review |

## Concurrency

Maximum planned concurrent sub-agents: three. The domain research role runs first. Objectives and low-level decomposition run in sequence because the low-level tasks depend on objective structure. Final review runs after integration draft.

## Integration Ownership

The orchestration thread owns run setup, integrating executor handoffs, writing candidate outputs, applying accepted review findings, validation, SHA-256 fingerprinting, and final response to Hera.

## Boundary Controls

- Preserve outputs under this run folder first.
- Do not copy to canonical `specification.md` or selection records.
- Do not write Homework Automation Layer mechanics into the product low-level tasks.
- Do not use Greek identity labels for runtime Java classes or package names.
- Do not include raw account IDs, descriptions, hidden prompts, credentials, or real compliance claims.

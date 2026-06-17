# Write Spec Workflow

This is the canonical workflow for both Homework 6 Agent 1 entrypoints:

- Codex Markdown skill: `homework-6/.agents/skills/write-spec/SKILL.md`
- Claude Code skill slash command: `homework-6/.claude/skills/write-spec/SKILL.md`

The entrypoints must stay thin. Update this package first when the workflow changes, and do not keep independent fallback generation logic in either wrapper.

All paths in this file are repository-root relative unless the active project root is already `homework-6`. When running from a homework-root project, remove the leading `homework-6/` prefix from homework-local paths while keeping root-level references such as `AGENTS.md`, `HOMEWORK_STANDARDS.md`, `README.md`, and Homework 3 paths relative to the repository root when available.

## Required Context

Read the assignment context before writing or selecting outputs:

1. `homework-6/TASKS.md`
2. `homework-6/sample-transactions.json`
3. `homework-6/agents.md` when present
4. Root `AGENTS.md`, `HOMEWORK_STANDARDS.md`, and `README.md` when available
5. Homework 3 reference package when available: `homework-3/specification.md`, `homework-3/agents.md`, `homework-3/docs/domain-rules.md`, `homework-3/docs/technical-conventions.md`, and `homework-3/docs/development-process.md`
6. This package's references: `agent-control/write-spec/stack-profiles.md` and `agent-control/write-spec/quality-bar.md`

The original Homework 6 repository does not provide the referenced `specification-TEMPLATE-hint.md` in this checkout. Record that absence in `run-metadata.md` and use the Task 1 section list plus the Homework 3 reference package as the local template source. If `specification-TEMPLATE-hint.md` appears later, read it before drafting and let it override Homework 3 formatting where it is more specific.

Use local references as the quality bar. Do not copy Homework 3 domain claims as Homework 6 research.

## Operating Modes

- `generate`: create a new preserved run under `homework-6/docs/agent-runs/`.
- `resume`: continue a bounded run using its `handoff.md` and completed outputs.
- `compare`: compare two or more preserved runs without overwriting canonical files.
- `select`: record the chosen run in `docs/agent-runs/final-selection.md` and copy its selected output package to canonical paths.

Default to `generate` unless the operator asks for comparison, selection, or continuation.

After a successful `generate` run, if `homework-6/specification.md` does not exist yet, automatically select that run's `agent-1-spec/outputs/specification.md`, copy it to `homework-6/specification.md`, update `docs/agent-runs/final-selection.md`, and report that first-run canonical selection happened. If `homework-6/specification.md` already exists, preserve the run only; do not overwrite the canonical spec without explicit `select` or overwrite instruction.

A successful generation run means all required outputs and handoffs for the selected stack exist, `validation-checklist.md` passes the Task 1 and quality-bar checks, and the final review has no unresolved blocking findings.

The default selectable package is `specification.md` only. Supporting run artifacts such as `docs/domain-rules.md`, `docs/technical-conventions.md`, `docs/development-process.md`, `research-notes.md`, reviews, and handoffs remain evidence and source material under the run folder unless the operator explicitly selects them as canonical support docs. Do not copy a run-local `agents.md` over `homework-6/agents.md`.

`homework-6/agents.md` is the standing homework-level agent guide. It lives beside `TASKS.md` and applies to all Agent 1 and later pipeline runs. It should be created and maintained as a stable control surface for the assignment, not regenerated per run. If a spec run discovers a needed permanent change to that guide, record the recommendation in the run handoff or validation checklist and handle it as a separate control-surface update.

## Stack Input

Accept only `stack=python` and `stack=java`. If the stack is omitted, use `stack=python`.

Reject `stack=auto` and any unsupported value with a short message naming the supported enum. Do not invent a new language profile during a run. Load `agent-control/write-spec/stack-profiles.md` before drafting stack-specific files, functions, commands, test tools, coverage hooks, or MCP notes.

## Workflow Steps

1. Confirm the requested mode and normalize stack input.
2. Create a unique run ID using `YYYYMMDD-HHMMSS-write-spec-<stack>-<short-label>`.
3. Create the run folder before drafting content:
   - `run-metadata.md`
   - `inputs/source-context.md`
   - `agent-1-spec/outputs/`
   - `agent-1-spec/handoffs/`
   - `agent-1-spec/review/`
   - `agent-1-spec/research-notes.md`
   - `agent-1-spec/validation-checklist.md`
   - `agent-1-spec/handoff.md`
4. Inventory required outputs and record missing local references in `run-metadata.md`.
5. Write a run-local sub-agent plan in `agent-1-spec/handoffs/sub-agent-plan.md` before research or drafting.
6. Run the planned sub-agent phases below and preserve each handoff artifact.
7. Integrate the sub-agent outputs into candidate files inside the run folder first. Do not overwrite canonical `homework-6/specification.md` or canonical docs before explicit selection, except for first-run auto-selection when no canonical spec exists.
8. Validate generated outputs against `agent-control/write-spec/quality-bar.md`, the selected stack profile, Homework 6 Task 1 required sections, and all sub-agent review findings.
9. Use emergency handoff only when the planned phases cannot finish cleanly. The emergency handoff supplements, not replaces, the planned phase handoffs.
10. In `select` mode, copy only the selected output package to canonical paths and update `docs/agent-runs/final-selection.md`. The normal package is `specification.md`; support docs require explicit selection.

When repository instructions require `dev-doc-harness` or Superpowers, comply with those planning and freeze gates. The generated Homework 6 agents must still be usable without those tools.

## Required Sub-Agent Strategy

Agent 1 must use sub-agents when the runtime can spawn them. Lack of an explicit operator reminder is not a reason to skip them; this workflow is the operator requirement and must be inherited by agents that run the pipeline.

Before drafting, write `agent-1-spec/handoffs/sub-agent-plan.md` with:

- Run ID and selected stack.
- Planned sub-agent roles, scopes, context strategy, inputs, output artifacts, and whether roles run in parallel or in waves.
- Model policy and reasoning intent using policy-relative wording.
- Requested model family or profile and reasoning effort for Codex and Claude Code runtimes, using the prescription below.
- Maximum concurrent sub-agents for the run and any runtime limits observed.
- Integration owner, which remains the orchestration thread.

## Model Prescription

If the runtime exposes model selection, prescribe the strongest appropriate exposed Codex or Claude Code profile in `sub-agent-plan.md`. Avoid hard-coded dated model IDs in standing instructions because they age quickly. If a run UI exposes exact model labels, record the actual label used in that run's metadata or sub-agent plan.

For Codex:

- Orchestration, integration drafting, final review, privacy/audit review, and architecture-sensitive decisions: use the latest strongest Codex profile exposed by the UI, with high or extra-high reasoning. Prefer the highest practical reasoning for the first full Task 1 generation, final selection, and difficult repairs after review.
- Domain research, objectives architecture, and low-level task decomposition sub-agents: use a strong current Codex profile with high reasoning. Medium reasoning is allowed only for bounded source inventory, summarization, or comparison notes that do not make final quality decisions.
- Do not use a smaller, older, or low-reasoning Codex profile for final review, stack-specific implementation planning, privacy/audit decisions, or canonical selection unless the operator explicitly approves the downgrade and the limitation is recorded.

For Claude Code:

- Orchestration, integration drafting, final review, privacy/audit review, and architecture-sensitive decisions: use the strongest reasoning-capable Claude Code model family exposed by the runtime, with the highest practical thinking/reasoning setting.
- Domain research, objectives architecture, low-level task decomposition, and stack review sub-agents: use a current strong Claude Code model family by default; escalate to the strongest exposed family for ambiguous, high-blast-radius, or failed review-repair work.
- Use a smaller or faster Claude Code family only for narrow extraction, inventory, formatting, or non-authoritative comparison support. A smaller/faster run must not be the final authority for generated spec quality.

If the runtime cannot set model or reasoning effort, record the requested prescription, the observed limitation, and the compensation used. Do not omit the prescription merely because the current tool may ignore it.

Required sub-agents for normal `generate` and `resume` runs:

| Role | Purpose | Context strategy | Required output |
|---|---|---|---|
| Domain research sub-agent | Research banking-pipeline domain rules, audit/privacy constraints, ISO-currency assumptions, and unsupported compliance claims. | Curated prompt plus assignment files and source requirements. | `agent-1-spec/handoffs/domain-research-handoff.md` and entries in `agent-1-spec/research-notes.md`. |
| Objectives architect sub-agent | Shape the high-level objective and 4-5 mid-level objectives for clarity, testability, and assignment fit. | Curated prompt plus Task 1 requirements, sample transactions, and domain-research handoff. | `agent-1-spec/handoffs/objectives-handoff.md`. |
| Low-level task decomposition sub-agent | Produce detailed low-level task cards with exact prompts, files, functions, edge cases, acceptance criteria, and verification. | Curated artifacts containing selected stack profile, objectives handoff, quality bar, and assignment requirements. | `agent-1-spec/handoffs/low-level-tasks-handoff.md`. |
| Final review sub-agent | Review the completed candidate package for objective clarity, stack specificity, privacy, research provenance, task-card executability, and handoff quality. | Curated artifacts containing all candidate outputs and validation notes. | `agent-1-spec/review/final-review.md`. |

`compare` and `select` modes may use a narrower sub-agent set, but any mode that drafts or materially revises a candidate spec package must use the four roles above unless the operator explicitly approves a reduced run after being told what quality gate is being dropped.

Additional specialized review sub-agents are allowed for stack-specific technical review, privacy/audit review, run comparison, or documentation review when they reduce risk. Prefer waves of up to three active sub-agents unless the operator has explicitly approved higher concurrency. The project config sets `max_threads = 8` so the runtime can support multiple planned roles across the run.

If the environment cannot spawn sub-agents at all, stop before drafting and ask the operator whether to switch tools, open a project configuration that supports sub-agents, or proceed with a documented degraded mode. Do not silently replace the required sub-agent phases with main-thread work.

## Planned Phases and Handoffs

Use these phases for `generate` and `resume` runs:

1. **Run setup:** create the run folder, normalize stack, write `run-metadata.md`, `inputs/source-context.md`, and `agent-1-spec/handoffs/sub-agent-plan.md`.
2. **Research wave:** spawn the domain research sub-agent and any stack/technical research sub-agent. Merge accepted claims into `research-notes.md`; keep raw findings in handoff files.
3. **Objectives wave:** spawn the objectives architect sub-agent after domain research is available. Preserve `objectives-handoff.md` before drafting the top of `specification.md`.
4. **Low-level task wave:** spawn the low-level task decomposition sub-agent after objectives and stack profile are stable. Preserve `low-level-tasks-handoff.md` before drafting or revising the task-card section.
5. **Integration draft:** the orchestration thread integrates the handoffs into candidate `specification.md`, domain rules, technical conventions, development process, validation checklist, and run handoff. Do not regenerate the standing `homework-6/agents.md` as part of a run.
6. **Review wave:** spawn the final review sub-agent. Add optional privacy/audit, stack, or documentation reviewers if risk remains. Preserve each review under `agent-1-spec/review/`.
7. **Repair and revalidate:** the orchestration thread fixes accepted review findings, updates validation notes, and records unresolved risks.
8. **Comparison or selection:** compare runs when requested, copy the first successful generated spec automatically only when no canonical `specification.md` exists, or copy selected package files after operator selection.

Each planned handoff must include assigned scope, files or context inspected, sources or commands used, assumptions, uncertainty, residual risks, and recommended next step. Emergency `agent-1-spec/handoff.md` is still required when the run must pause unexpectedly, but it must reference the planned phase handoffs already produced.

## Research Rules

- Prefer Context7 for selected framework and library documentation.
- Agent 1 domain research is required and must be assigned to the domain research sub-agent. Use web, Context7 where relevant, or other current sources when available.
- Agent 2 must later document at least two Context7 queries in canonical `research-notes.md`; prepare the spec so that requirement is explicit.
- Treat the banking pipeline as an educational simulation. Do not claim legal, AML, sanctions, payment-network, or bank regulatory compliance unless a cited source and assignment scope support the statement.
- Do not paste credentials, real account data, real customer data, tokens, secrets, or unrelated local paths into research notes.

## Run Preservation

Preserve generation attempts under `homework-6/docs/agent-runs/<run-id>/`. A run is evidence, not the canonical submission.

Use this layout:

```text
<run-id>/
  run-metadata.md
  inputs/
    source-context.md
  agent-1-spec/
    handoffs/
      sub-agent-plan.md
      domain-research-handoff.md
      objectives-handoff.md
      low-level-tasks-handoff.md
    outputs/
      specification.md
      docs/domain-rules.md
      docs/technical-conventions.md
      docs/development-process.md
    review/
      final-review.md
    research-notes.md
    validation-checklist.md
    handoff.md
  comparison.md
```

Compare runs by completeness, research provenance, stack precision, privacy handling, task-card executability, validation coverage, and downstream handoff usefulness. Cross-stack comparison must note where Python and Java require different files, functions, commands, coverage tools, and MCP implementation choices.

## Output Requirements

For a generated Agent 1 package, produce at least:

- `agent-1-spec/outputs/specification.md`
- `agent-1-spec/outputs/docs/domain-rules.md`
- `agent-1-spec/outputs/docs/technical-conventions.md`
- `agent-1-spec/outputs/docs/development-process.md`
- `agent-1-spec/handoffs/sub-agent-plan.md`
- `agent-1-spec/handoffs/domain-research-handoff.md`
- `agent-1-spec/handoffs/objectives-handoff.md`
- `agent-1-spec/handoffs/low-level-tasks-handoff.md`
- `agent-1-spec/review/final-review.md`
- `agent-1-spec/research-notes.md`
- `agent-1-spec/validation-checklist.md`
- `agent-1-spec/handoff.md`

The generated `specification.md` must include high-level objective, 4-5 mid-level objectives, implementation notes, beginning and ending context, and one low-level task entry per meta-agent. The selected stack must be visible in files, functions, commands, test tools, coverage gate design, and MCP notes.

## Self-Review Gate

Before reporting a run as ready for comparison or selection, verify:

- Task 1's five required sections are present.
- Every low-level task has exact prompt, file, function, behavior details, edge cases, acceptance criteria, and verification.
- Money uses precise decimal semantics, never binary floating point.
- Currency validation uses ISO 4217-style codes.
- Logs and audit examples avoid plaintext PII and account identifiers.
- Agent communication uses the required JSON file protocol through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- Context7 and domain research notes are present, or explicit fallback limitations are recorded.
- Required sub-agent handoffs and final review are present, or the run stopped for operator instruction because sub-agents were unavailable.
- Canonical files are untouched unless first-run auto-selection copied `specification.md` because no canonical spec existed, or the operator requested `select`.

## Handoff Rules

Planned phase handoffs are mandatory and are listed in `Planned Phases and Handoffs`. Write or update emergency `handoff.md` whenever the run cannot be completed confidently in one thread or must pause outside the planned phase boundary.

Emergency `handoff.md` must include the run ID, selected stack, completed phase handoffs, completed candidate files, unfinished sections, validation status, known risks, and the exact next prompt. Keep the handoff bounded enough that a fresh agent can continue without re-reading this conversation.

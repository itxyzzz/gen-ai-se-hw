# Hephaestus Code Generator Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use `superpowers:subagent-driven-development` (recommended) or `superpowers:executing-plans` to implement this plan task-by-task after the harness freeze gate and a fresh explicit operator instruction. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Create the Hephaestus (Code Generator) automation control surface for Homework 6 Task 2 without generating the pipeline code yet.

**Architecture:** Add a tool-neutral control package under `homework-6/agent-control/generate-code/`, then add thin Codex and Claude wrappers that point to it. The package will consume the selected Athena specification, enforce Context7 research documentation, preserve Hephaestus run evidence, and constrain future generated outputs to Task 2 product code.

**Tech Stack:** Markdown control surfaces, Codex project skills under `.agents/skills`, Claude Code project skills under `.claude/skills`, existing Homework 6 Context7 MCP configuration, Python as the selected generated-product stack.

---

Work ID: `2026-06-18-hephaestus-code-generator`
Short ID: `hephaestus-code-generator`
Status: Approved
Harness release: `unknown`
Schema: `schema:plan.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:models.strategy-required`, `rule:models.context-strategy`, `rule:models.approved-strategy-authorized`, `rule:models.fresh-confirmation`, `rule:lifecycle.variance-policy`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, `rule:freeze.stop-before-implementation`

## Implementation summary

This implementation creates the second Homework Automation Layer agent control surface: Hephaestus (Code Generator). It follows the same control-surface pattern as Athena (Spec Writer): the real rules live in a tool-neutral `agent-control/` package, while Codex and Claude Code entrypoints stay thin and mandatory-reference driven.

The new Hephaestus package will not run code generation. It will define how a later Homework 6-root run reads the selected `specification.md`, uses Context7 MCP while building the Python transaction-processing pipeline, writes canonical `research-notes.md` with at least two Context7 query records, preserves run evidence under `docs/agent-runs/`, and validates that Task 2 generated code obeys privacy, Decimal, JSON protocol, and result-shape requirements.

Hephaestus should be autonomous in its use of executor sub-agents during the later code-generation run. The workflow must authorize sub-agents up to Homework 6's configured `agents.max_threads = 8` cap without asking for another operator approval, prefer one bounded sub-agent per selected-spec task or independently testable implementation slice when that improves quality, and require curated context plus deliberate policy-relative model/reasoning selection. The orchestrator remains final integration owner and may choose fewer sub-agents when fan-out would degrade consistency or quality.

The implementation must be careful about layer boundaries. Hephaestus is a Homework Automation Layer agent. It may generate runtime transaction pipeline components named Transaction Validator, Fraud Detector, Settlement Processor, and Integrator, but it must not name runtime product modules after Greek automation agents or make generated code depend on `dev-doc-harness`, Superpowers, or prior chat state.

## Files and interfaces

Create:

- `homework-6/agent-control/generate-code/README.md`
- `homework-6/agent-control/generate-code/workflow.md`
- `homework-6/agent-control/generate-code/quality-bar.md`
- `homework-6/agent-control/generate-code/run-registry.md`
- `homework-6/.agents/skills/generate-code/SKILL.md`
- `homework-6/.claude/skills/generate-code/SKILL.md`

Modify:

- `homework-6/agents.md`
- `homework-6/CHANGELOG.md`

Do not modify during this work item:

- `homework-6/TASKS.md`
- `homework-6/specification.md`
- `homework-6/mcp.json`
- `homework-6/.codex/config.toml`
- Generated Transaction System Layer code paths such as `integrator.py`, `agents/*.py`, `tests/*.py`, `research-notes.md`, and `shared/`

## Model and Sub-agent Strategy

Current orchestration: Codex Desktop thread, model and reasoning labels not exposed in the shell environment.
Fit assessment: Medium complexity and medium blast radius. The implementation is documentation/control-surface work, but mistakes will steer a later code-generation agent and could cause scope creep across Tasks 2-5.
Recommended change: Use the strongest available reasoning profile for implementation if model/reasoning controls are exposed. If controls are unavailable, compensate with static validation, scope scans, and diff review.

Sub-agents: None for implementing this control surface. The files are tightly coupled, mostly Markdown, and should be integrated by one orchestration thread.

Future Hephaestus authorization to encode in `workflow.md`: The Hephaestus (Code Generator) run is authorized to spawn executor sub-agents up to Homework 6's configured `agents.max_threads = 8` cap without additional operator approval. Prefer one bounded executor sub-agent per selected `specification.md` low-level task or independently testable implementation slice, run in waves when there are more slices than available threads, and use fewer sub-agents when parallelism would reduce quality. Each sub-agent must receive curated context, a deliberate policy-relative model class/profile and reasoning effort, a clear output artifact, and a required report. Borrow the context-shaping and model-selection vocabulary from `dev-doc-harness` `subagent-model-policy.md` by copying the needed guidance into the Hephaestus package; do not require the later Homework Automation Layer run to load harness policy.

## Tasks

- [ ] Confirm preflight state.
  Run `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw branch --show-current` and confirm it prints `homework-6-submission`. Run `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw status --short` and note any pre-existing dirty files before editing. Read `homework-6/specification.md`, `homework-6/agents.md`, `homework-6/mcp.json`, `homework-6/.codex/config.toml`, and the existing Athena wrappers/package files before writing Hephaestus files.

- [ ] Create `homework-6/agent-control/generate-code/README.md`.
  Include:
  - Package purpose: canonical tool-neutral reference package for Homework 6 Hephaestus (Code Generator).
  - File list for `workflow.md`, `quality-bar.md`, and `run-registry.md`.
  - Entrypoints: `homework-6/.agents/skills/generate-code/SKILL.md` and `homework-6/.claude/skills/generate-code/SKILL.md`.
  - Root-selection note matching Athena: open the homework folder as project root when using homework-local Codex skills and MCP configuration.
  - Mandatory-file rule: stop if required package files are unreadable rather than inventing fallback generation logic.

- [ ] Create `homework-6/agent-control/generate-code/workflow.md`.
  Include:
  - A heading naming the workflow as canonical for both Hephaestus entrypoints.
  - Required context list: selected `homework-6/specification.md`, `homework-6/sample-transactions.json`, `homework-6/agents.md`, `homework-6/TASKS.md` for assignment checks, root `AGENTS.md`/`HOMEWORK_STANDARDS.md`/`README.md` when available, `homework-6/mcp.json`, `homework-6/.codex/config.toml`, and this package's quality bar/run registry.
  - Modes: `generate`, `resume`, and `compare`, defaulting to `generate`.
  - Run ID format: `YYYYMMDD-HHMMSS-generate-code-python-short-label`.
  - Run folder layout under `homework-6/docs/agent-runs/RUN_ID/`, with `run-metadata.md`, `inputs/source-context.md`, `agent-2-code/handoffs/`, `agent-2-code/review/`, `agent-2-code/research-notes.md`, `agent-2-code/validation-checklist.md`, and `agent-2-code/handoff.md`.
  - Context7 rule: use Context7 during code generation for at least two concrete implementation questions; record search text, returned library ID, access date, and applied insight in canonical `homework-6/research-notes.md` and mirror the notes in the run folder.
  - Sub-agent authorization rule: Hephaestus may spawn executor sub-agents up to `agents.max_threads = 8` from `homework-6/.codex/config.toml` without additional operator approval. Prefer one bounded sub-agent per selected-spec low-level task or implementation slice when quality benefits; run waves when needed.
  - Sub-agent context/model rule: require curated prompt or curated artifact context, policy-relative model class/profile, reasoning effort, output artifact, whether the work can run in parallel, and blast-radius note for each planned sub-agent. The orchestrator owns final integration and may reduce fan-out when parallelism would degrade quality.
  - Sub-agent reporting rule: every sub-agent report includes assigned scope, files inspected or changed, commands/tests run, assumptions, uncertainty or residual risk, and recommended next step.
  - Generation scope: Task 2 code only, including integrator/orchestrator, runtime transaction pipeline components, JSON file protocol, result writing, and code-level tests when needed to verify the generated pipeline.
  - Non-scope: Task 3 slash commands/hooks, Task 4 custom MCP server/config changes, Task 5 README/HOWTORUN/screenshots/PR package.
  - Layer rule: generated runtime components use functional names, not Hephaestus/Athena/Themis/Clio.
  - Degraded Context7 handling: if MCP is unavailable, stop and ask the operator whether to switch to a Context7-enabled project/thread or proceed with documented limitation; do not silently mark the assignment requirement satisfied.

- [ ] Create `homework-6/agent-control/generate-code/quality-bar.md`.
  Include checks for:
  - Selected spec consumption and traceability from low-level tasks to generated files.
  - Task 2 deliverables: `integrator.py` or equivalent, at least three runtime components, shared JSON directories, all sample transactions landing in `shared/results/`, and `research-notes.md`.
  - Context7 documentation: at least two entries with search, library ID, access date, and applied insight.
  - Sub-agent strategy documentation: the run metadata or handoff records planned and actual sub-agent use, model/reasoning intent, context strategy, output artifacts, and integration ownership. Absence of sub-agents must be justified by quality concerns, runtime unavailability, or tight coupling, not by missing operator reapproval.
  - Python stack expectations from the selected spec: Decimal money, ISO-style currency allowlist, JSON `allow_nan=False`, redaction, audit-safe logs/results, deterministic risk scoring, and safe result summaries.
  - Privacy rejection checks for raw account IDs, raw descriptions, credentials, tokens, and unfiltered metadata dumps.
  - Scope rejection checks for premature Task 3 commands/hooks, Task 4 `mcp/server.py` or `pipeline-status` config additions, Task 5 docs/screenshots, and harness/Superpowers leakage into generated product files.
  - Validation requirements: run pipeline command, inspect `shared/results/summary.json`, run available tests, and record blockers when a command cannot run.

- [ ] Create `homework-6/agent-control/generate-code/run-registry.md`.
  Include:
  - Hephaestus run ID format and required run folder layout.
  - Evidence preservation rules for generated code attempts and Context7 notes.
  - Evidence preservation rules for sub-agent plans, handoffs, reviews, and de-facto sub-agent use, including whether work ran concurrently or in waves.
  - Comparison criteria for multiple code-generation attempts: spec coverage, Context7 provenance, privacy/audit safety, pipeline correctness, validation evidence, and scope control.
  - Canonical-output rule: generated code writes to Homework 6 product paths only during an authorized Hephaestus run; preserved run artifacts remain evidence and are not the canonical submission by themselves.

- [ ] Create `homework-6/.agents/skills/generate-code/SKILL.md`.
  Use frontmatter:

  ```markdown
  ---
  name: generate-code
  description: Use when generating, resuming, comparing, or reviewing Homework 6 Hephaestus (Code Generator) code-generation runs.
  ---
  ```

  Body requirements:
  - Name the skill `Generate Code`.
  - State that it creates or resumes preserved Homework 6 Hephaestus (Code Generator) runs.
  - Require reading `../../../agent-control/generate-code/workflow.md`, `quality-bar.md`, and `run-registry.md` before acting.
  - Stop if required references are missing.
  - State that repository harness and Superpowers rules apply only to Operator Layer maintenance, not inside Hephaestus-generated product files.

- [ ] Create `homework-6/.claude/skills/generate-code/SKILL.md`.
  Use frontmatter:

  ```markdown
  ---
  name: generate-code
  description: Use when generating, resuming, comparing, or reviewing Homework 6 Hephaestus (Code Generator) code-generation runs.
  when_to_use: Use for Homework 6 Task 2 code generation, Hephaestus (Code Generator) runs, Context7-backed pipeline generation, comparing preserved code runs, or reviewing generated Task 2 outputs.
  argument-hint: "[generate|resume|compare] [run=RUN_ID]"
  ---
  ```

  Body requirements:
  - Keep it a thin wrapper around the shared package.
  - Include examples: `/generate-code`, `/generate-code generate`, `/generate-code resume run=20260618-120000-generate-code-python-primary`, `/generate-code compare`.
  - Match the Codex wrapper's required-reference and no-fallback behavior.

- [ ] Update `homework-6/agents.md`.
  Add a short Hephaestus (Code Generator) control-surface note that points to `agent-control/generate-code/` and the two entrypoints. Keep the existing layer glossary intact. Clarify that Hephaestus consumes the selected `specification.md`, uses Context7 during code generation, documents at least two query records in canonical `research-notes.md` during a later run, and may use executor sub-agents up to the Homework 6 config cap without additional operator approval.

- [ ] Update `homework-6/CHANGELOG.md` before the implementation commit.
  Add a newest-first `Homework 6 - Step 14: Hephaestus Planning Package` entry for this planning package during the freeze commit. During later implementation, update the top entry or add the next step to describe the created Hephaestus control surface and validation. Do not claim generated pipeline code exists.

- [ ] Review and validate the final implementation diff.
  Confirm no generated product code was added, `TASKS.md` and `specification.md` are unchanged, Context7 config is unchanged, wrappers are thin, package files are mandatory, and the quality bar rejects out-of-scope Task 3-5 deliverables.

## Validation commands

| Command | Expected result |
|---|---|
| `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw branch --show-current` | Prints `homework-6-submission`. |
| `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw status --short` | Shows only expected Hephaestus control-surface, planning package, `agents.md`, and changelog changes; any pre-existing dirty files are accounted for. |
| `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw diff -- homework-6/TASKS.md` | Prints no diff. |
| `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw diff -- homework-6/specification.md` | Prints no diff. |
| `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw diff -- homework-6/mcp.json homework-6/.codex/config.toml` | Prints no diff for this control-surface work. |
| `Test-Path C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\agent-control\generate-code\workflow.md` | Returns `True`. |
| `Test-Path C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\.agents\skills\generate-code\SKILL.md` | Returns `True`. |
| `Test-Path C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\.claude\skills\generate-code\SKILL.md` | Returns `True`. |
| `Select-String -LiteralPath C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\agent-control\generate-code\workflow.md -Pattern 'Context7','research-notes.md','specification.md','Task 2','Task 3','Task 4','Task 5'` | Finds required Context7, selected spec, and scope-boundary language. |
| `Select-String -LiteralPath C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\agent-control\generate-code\workflow.md -Pattern 'max_threads = 8','without additional operator approval','curated context','policy-relative','final integration'` | Finds sub-agent autonomy, context-shaping, model-selection, and integration-ownership language. |
| `Select-String -LiteralPath C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\agent-control\generate-code\quality-bar.md -Pattern 'Decimal','allow_nan=False','raw account','pipeline-status','mcp/server.py','screenshots'` | Finds money, JSON safety, privacy, and out-of-scope rejection checks. |
| `Select-String -LiteralPath C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\.agents\skills\generate-code\SKILL.md,C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6\.claude\skills\generate-code\SKILL.md -Pattern 'agent-control/generate-code/workflow.md','stop'` | Finds mandatory shared-package references and no-fallback behavior. |
| `git -C C:\Work\Codex\SETU-HW\gen-ai-se-hw diff --check` | Exits 0 with no whitespace errors. |

## Plan variance handling

Use `rule:lifecycle.variance-policy`. Before freeze, edit this draft directly for operator feedback. After freeze, record nontrivial implementation variance in `implementation-notes/variance-log.md`; use a plan amendment for high-impact architecture, API, data, security, privacy, compliance, scope, acceptance-criteria, or feasibility changes.

Likely local variance: the exact entrypoint name might need to be changed from `generate-code` to `code-generator` if the operator prefers the Greek role naming over action naming. That is a scope-preserving control-surface naming variance if all references are updated consistently before freeze. A later decision to generate pipeline code in the same work item is a scope change and requires operator approval or a new work item.

## Planning artifact freeze gate

Use `module:freeze-gate`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, and `rule:freeze.stop-before-implementation`.

Draft review status: Approved by operator on 2026-06-18.
Approval commit: This planning freeze commit.
Post-freeze implementation authorization: Not granted.

## Completion criteria

- Acceptance criteria in `spec-hephaestus-code-generator.md` are met.
- Required validation commands have been run and recorded.
- Required documentation artifacts have been created or updated.
- `homework-6/CHANGELOG.md` has a newest-first entry before each commit.
- Variance log is present and current.
- De-facto sub-agent use is reported when applicable; this plan currently authorizes no sub-agents for implementing the control surface.

## Approval

- Status: Approved
- Superseded by: None

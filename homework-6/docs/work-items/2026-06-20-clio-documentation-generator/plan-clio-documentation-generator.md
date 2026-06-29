# Clio Documentation Generator Plan

Work ID: `2026-06-20-clio-documentation-generator`
Short ID: `clio-documentation-generator`
Status: Draft
Harness release: `unknown`
Schema: `schema:plan.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:models.strategy-required`, `rule:models.context-strategy`, `rule:models.approved-strategy-authorized`, `rule:models.fresh-confirmation`, `rule:lifecycle.variance-policy`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, `rule:freeze.stop-before-implementation`

## Implementation summary

Implement Clio (Documentation Generator) as a portable Homework Automation Layer control surface, matching the established Athena, Hephaestus, and Themis pattern. The shared package under `agent-control/generate-docs/` will own the workflow, quality bar, and run registry. Codex and Claude Code entrypoints will be thin wrappers that read the shared package and stop if required references are unavailable.

The first implementation step creates only Clio's control surface and standing guide updates. A later Clio run, started after the control surface is available, will generate a preserved `agent-4-docs` candidate package, rerun selected test and pipeline evidence, map operator-sourced screenshots to assignment screenshot targets, generate any additional feasible screenshots, and produce the final README, HOWTORUN, architecture, testing, API/MCP docs, and draft PR description.

Clio must keep the selected Themis suite authoritative. If Clio discovers a final test gap, it records the gap and either requests Themis follow-up or writes an explicit final test-hardening delta when the operator authorizes that scope. It must not silently edit tests while producing documentation.

The Clio workflow will use Homeworks 1 through 4 as local style and author-name context. The prior README files consistently identify the author as `Igor Tanatarov`; Clio should use that display name unless the prior docs become unavailable or contradictory.

## Files and interfaces

Expected implementation changes:

- Add `agent-control/generate-docs/README.md`.
- Add `agent-control/generate-docs/workflow.md`.
- Add `agent-control/generate-docs/quality-bar.md`.
- Add `agent-control/generate-docs/run-registry.md`.
- Add `.agents/skills/generate-docs/SKILL.md`.
- Add `.agents/skills/generate-docs/agents/openai.yaml`.
- Add `.claude/skills/generate-docs/SKILL.md`.
- Add `.claude/commands/generate-docs.md`.
- Update `agents.md` with Clio discovery context and selected Themis consumption rules only where the current guide is incomplete.
- Update `CHANGELOG.md` before the implementation commit.
- Reference prior homework documentation paths in the Clio workflow as style examples and author-name context.

Expected later Clio run canonical outputs after successful selection:

- `README.md`
- `HOWTORUN.md`
- `ARCHITECTURE.md`
- `TESTING_GUIDE.md`
- `API_REFERENCE.md`
- `docs/pr-description-draft.md`
- Stable reviewer-facing screenshots under `docs/screenshots/`

Interfaces to keep stable:

- Existing Athena, Hephaestus, Themis, run-pipeline, validate-transactions, coverage hook, MCP, and selected runtime code surfaces.
- Existing `docs/screenshots/operator-sourced/` source screenshots.

## Model and Sub-agent Strategy

Current orchestration: Codex desktop thread, exact model profile and reasoning effort not exposed.
Fit assessment: Moderate complexity and medium blast radius. The control package is documentation/process work, but a later Clio run must coordinate selected artifacts, screenshots, privacy checks, and final reviewer-facing docs.
Recommended change: Use the repository `enterprise-default` policy. Prefer strongest available reasoning for final documentation synthesis and final review because stale or privacy-unsafe evidence can affect submission quality.

Sub-agents: None for this control-surface implementation. The file set is small and tightly coupled, and the orchestration thread can preserve consistency better than parallel workers.

For later Clio documentation generation runs, the workflow should allow bounded read-only reviewer sub-agents when available:

| Purpose | Context strategy | Input context | Output artifact | Model policy | Model class/profile | Reasoning effort | Reason | Parallel? | Blast radius if wrong |
|---|---|---|---|---|---|---|---|---|---|
| Screenshot and privacy evidence review | curated artifacts | Screenshot inventory, generated docs, safe-output rules, sample-data privacy rules | Review memo or checklist section | enterprise-default | latest strongest available for review | high | Screenshot leaks are subtle and reviewer-visible | Yes | Medium: unsafe evidence may be copied |
| Documentation completeness review | curated artifacts | Candidate docs, TASKS.md, HOMEWORK_STANDARDS.md, final-selection.md | Review findings | enterprise-default | latest strongest available for review | high | Final docs must satisfy assignment and repo standards | Yes | Medium: missing required docs or PR details |

## Tasks

- [ ] Create `agent-control/generate-docs/README.md` describing Clio as the canonical documentation-generation package.
- [ ] Create `agent-control/generate-docs/workflow.md` with required context, modes, run ID, run layout, screenshot handling, evidence generation, canonical selection, and Themis boundary rules.
- [ ] Create `agent-control/generate-docs/quality-bar.md` covering required docs, author name from prior homework documentation, screenshot evidence, draft PR description, layer coverage, privacy, validation, and scope rejection.
- [ ] Create `agent-control/generate-docs/run-registry.md` covering run preservation, output inventory, screenshot inventory, evidence files, comparison, and selection.
- [ ] Add the Codex `generate-docs` skill wrapper and metadata.
- [ ] Add the Claude Code `generate-docs` project skill wrapper.
- [ ] Add the thin Claude legacy `/generate-docs` command wrapper.
- [ ] Update `agents.md` only for routing details missing from the current Clio section.
- [ ] Update `CHANGELOG.md` with a newest-first implementation entry before committing.
- [ ] Validate reference paths, required markers, absence of unresolved draft markers, staged scope, and whitespace.
- [ ] Review the diff for frozen-artifact changes, generated noise, screenshot source preservation, and scope creep.

## Validation commands

| Command | Expected result |
|---|---|
| `git status --short` | Shows staged operator-sourced screenshots plus only intended Clio planning or implementation files for the current step |
| `Get-ChildItem -Recurse -File 'agent-control/generate-docs','.agents/skills/generate-docs','.claude/skills/generate-docs','.claude/commands'` | Clio control package and wrappers exist after implementation |
| `Get-ChildItem -Recurse -File 'agent-control/generate-docs','.agents/skills/generate-docs','.claude/skills/generate-docs','.claude/commands' \| Select-String -Pattern 'agent-control/generate-docs/workflow.md','agent-control/generate-docs/quality-bar.md','agent-control/generate-docs/run-registry.md'` | Entrypoints and wrappers reference the shared workflow files |
| `Get-ChildItem '..\homework-1','..\homework-2','..\homework-3','..\homework-4' -File -Include README.md,HOWTORUN.md,ARCHITECTURE.md,TESTING_GUIDE.md,API_REFERENCE.md` | Prior homework documentation style sources are discoverable when available |
| `Get-Content -Raw '..\homework-1\README.md','..\homework-2\README.md','..\homework-3\README.md','..\homework-4\README.md' \| Select-String -Pattern 'Igor Tanatarov'` | Prior homework README files identify the author display name for Clio |
| `Get-ChildItem -Recurse -File 'agent-control/generate-docs','.agents/skills/generate-docs','.claude/skills/generate-docs','.claude/commands','agents.md' \| Select-String -Pattern 'dev-doc-harness','Superpowers','freeze gate'` | No Clio runtime workflow requirement depends on hidden harness or Superpowers state, except explicit Operator Layer maintenance caveats |
| Run the repository's unresolved draft-marker scan over `agent-control/generate-docs`, `.agents/skills/generate-docs`, `.claude/skills/generate-docs`, `.claude/commands`, and `agents.md` | No unresolved draft markers in implementation files |
| `git diff --check` and `git diff --cached --check` | No whitespace errors |
| `git diff -- TASKS.md specification.md docs/agent-runs/final-selection.md agent-control/generate-tests/workflow.md` | No accidental changes to frozen assignment, selected spec, selection record, or Themis boundary workflow |

## Plan variance handling

Use `rule:lifecycle.variance-policy`. Before freeze, edit this draft directly for operator feedback. After freeze, record nontrivial implementation variance in `implementation-notes/variance-log.md`; use a plan amendment for high-impact architecture, API, data, security, privacy, compliance, scope, acceptance-criteria, or feasibility changes.

## Planning artifact freeze gate

Use `module:freeze-gate`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, and `rule:freeze.stop-before-implementation`.

Draft review status: planning artifacts are being prepared for operator approval. Approval commit has not been created. Implementation is not authorized until a fresh operator instruction is given after the freeze gate.

## Completion criteria

- Acceptance criteria in `spec-clio-documentation-generator.md` are met.
- Required validation commands have been run and recorded.
- Required Clio control files and wrappers have been created.
- `CHANGELOG.md` has a newest-first entry before the implementation commit.
- Variance log is present if nontrivial variance occurs.
- De-facto sub-agent use is reported. Expected for this implementation: none.

## Approval

- Status: Draft
- Superseded by: Not applicable

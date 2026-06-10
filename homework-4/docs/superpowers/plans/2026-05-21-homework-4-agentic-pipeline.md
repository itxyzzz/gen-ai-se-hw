# Homework 4 Agentic Pipeline Implementation Plan

> Superseded on 2026-05-22 by
> `2026-05-22-pure-agentic-pipeline-scale-down.md`. This historical plan
> describes the earlier JavaScript harness approach and is retained only as
> handoff context.

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development when implementing the later coding phases in a session that permits subagents, or superpowers:executing-plans for inline execution. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a portable Homework 4 agentic pipeline that preserves a buggy baseline app, produces fixed app outputs through adapter/model-specific runs, and compares previous runs as a mini-benchmark.

**Architecture:** A Node.js harness reads universal agent specs, skills, and pipeline configuration, then delegates model execution to adapters. The OpenAI SDK adapter is the primary single-command runner; the Codex chat adapter produces and validates the same artifact shape through guided chat execution.

**Tech Stack:** Node.js 24, npm, ES modules, built-in `node:test`, Markdown agent specs, YAML or JSON configuration, OpenAI SDK for the primary live adapter.

---

## Phase 0: Durable Planning Artifacts

**Files:**
- Create: `homework-4/docs/superpowers/specs/2026-05-21-agentic-pipeline-design.md`
- Create: `homework-4/docs/superpowers/plans/2026-05-21-homework-4-agentic-pipeline.md`
- Create: `homework-4/CHANGELOG.md`

- [x] **Step 1: Confirm branch and state**

Run:

```powershell
git status --short --branch
```

Expected: branch is `homework-4-submission`; no unrelated dirty files block the planning step.

- [x] **Step 2: Read required context**

Read:

```text
AGENTS.md
HOMEWORK_STANDARDS.md
homework-4/TASKS.md
```

Expected: the implementation follows repo workflow rules, Homework 4 deliverables, and the user-approved Phase 0 scope.

- [x] **Step 3: Create planning documents and changelog**

Create the design spec, this implementation plan, and the Step 0 changelog entry. Do not create runtime app, agent, adapter, harness, benchmark, test, screenshot, or package files in Phase 0.

- [ ] **Step 4: Verify planning files**

Run after the files are written:

```powershell
git status --short
$markers = @('TO' + 'DO', 'TB' + 'D', 'implement ' + 'later')
Get-ChildItem -Recurse -File homework-4 | Select-String -Pattern $markers
```

Expected: only Homework 4 planning files are new, and the placeholder scan returns no matches.

## Phase 1: Sample Buggy App And Scenario Artifacts

**Files:**
- Create: `homework-4/package.json`
- Create: `homework-4/app/baseline/src/cli.js`
- Create: `homework-4/app/baseline/src/catalogRepository.js`
- Create: `homework-4/app/baseline/src/quoteCalculator.js`
- Create: `homework-4/app/baseline/data/catalogs/default.json`
- Create: `homework-4/app/baseline/tests/quoteCalculator.test.js`
- Create: `homework-4/app/baseline/tests/security.test.js`
- Create: `homework-4/scenarios/bug-001/bug-context.md`
- Create: `homework-4/scenarios/bug-001/research/codebase-research.md`
- Create: `homework-4/scenarios/bug-001/implementation-plan.md`
- Modify: `homework-4/CHANGELOG.md`

- [ ] **Step 1: Create the Node package shell**

Define scripts for baseline verification, current app tests, harness tests, pipeline execution, promotion, and comparison. The initial Phase 1 scripts should include:

```json
{
  "type": "module",
  "scripts": {
    "verify:baseline": "node pipeline/verify-baseline.js",
    "app:baseline:test": "node --test app/baseline/tests/*.test.js",
    "app:current:test": "node --test app/current/tests/*.test.js",
    "test": "node --test pipeline/tests/*.test.js app/current/tests/*.test.js",
    "pipeline": "node pipeline/run.js",
    "pipeline:openai": "node pipeline/run.js --adapter openai-sdk",
    "pipeline:codex-chat:prepare": "node pipeline/run.js --adapter codex-chat --prepare",
    "pipeline:codex-chat:validate": "node pipeline/run.js --adapter codex-chat --validate",
    "promote": "node pipeline/promote.js",
    "compare": "node pipeline/compare.js"
  }
}
```

- [ ] **Step 2: Implement the intentionally buggy baseline app**

Implement a quote calculator CLI with these seeded defects:

- `quantity + unitPrice` is used where `quantity * unitPrice` is expected.
- `SAVE10` subtracts a flat `10` instead of applying a 10 percent discount.
- Catalog names are joined to the catalog directory without rejecting traversal input.

- [ ] **Step 3: Add tests that describe correct behavior**

Use built-in `node:test` and `assert/strict`. Tests should fail against the buggy baseline for the seeded issues.

- [ ] **Step 4: Add bug scenario artifacts**

Create `bug-context.md`, `research/codebase-research.md`, and `implementation-plan.md` with real file references to the baseline app. These artifacts feed the verifier and fixer stages.

- [ ] **Step 5: Verify baseline bug reproduction**

Run:

```powershell
npm run app:baseline:test
```

Expected: tests fail because seeded issues exist.

Run:

```powershell
npm run verify:baseline
```

Expected: exits 0 only when the expected seeded failures are reproduced.

- [ ] **Step 6: Update changelog**

Record the sample app, scenario artifacts, and verification result in `homework-4/CHANGELOG.md`.

## Phase 2: Universal Agent Specs And Skills

**Files:**
- Create: `homework-4/agents/bug-researcher.agent.md`
- Create: `homework-4/agents/research-verifier.agent.md`
- Create: `homework-4/agents/bug-planner.agent.md`
- Create: `homework-4/agents/bug-fixer.agent.md`
- Create: `homework-4/agents/security-verifier.agent.md`
- Create: `homework-4/agents/unit-test-generator.agent.md`
- Create: `homework-4/skills/research-quality-measurement.md`
- Create: `homework-4/skills/unit-tests-FIRST.md`
- Create: `homework-4/skills/codex-chat-pipeline.md`
- Create: `homework-4/pipeline.config.yaml`
- Modify: `homework-4/CHANGELOG.md`

- [ ] **Step 1: Create the required skills**

`research-quality-measurement.md` must define measurable quality labels and required sections for `verified-research.md`. `unit-tests-FIRST.md` must define Fast, Independent, Repeatable, Self-validating, and Timely criteria for generated tests.

- [ ] **Step 2: Create required agent specs**

Each required agent file must include frontmatter with `id`, `role`, `model_policy`, `reasoning_effort`, `inputs`, `outputs`, `skills`, and `allowed_actions`.

- [ ] **Step 3: Create helper stage specs**

Add Bug Researcher and Bug Planner helper specs so the implementation can satisfy the task's stated run order while documenting that four required agents are still the main deliverables.

- [ ] **Step 4: Create pipeline manifest**

`pipeline.config.yaml` must define stage order, model policies, scenario defaults, output paths, and benchmark score weights.

- [ ] **Step 5: Validate agent and skill documents manually**

Read all files and confirm there are no missing required sections, no unresolved placeholder language, and no mismatch between manifest stage ids and agent spec ids.

- [ ] **Step 6: Update changelog**

Record agent specs, skills, manifest, and validation notes.

## Phase 3: Universal Harness

**Files:**
- Create: `homework-4/pipeline/run.js`
- Create: `homework-4/pipeline/promote.js`
- Create: `homework-4/pipeline/compare.js`
- Create: `homework-4/pipeline/verify-baseline.js`
- Create: `homework-4/pipeline/lib/config.js`
- Create: `homework-4/pipeline/lib/agentSpec.js`
- Create: `homework-4/pipeline/lib/workspace.js`
- Create: `homework-4/pipeline/lib/artifacts.js`
- Create: `homework-4/pipeline/lib/diff.js`
- Create: `homework-4/pipeline/tests/*.test.js`
- Modify: `homework-4/CHANGELOG.md`

- [ ] **Step 1: Build config and agent spec loading**

Read the manifest, parse Markdown frontmatter, and validate that all stage ids, inputs, outputs, and skills exist.

- [ ] **Step 2: Build isolated workspace creation**

Copy `app/baseline` to `runs/<scenario>/<run-id>/app`. Reject writes outside the run directory.

- [ ] **Step 3: Build artifact writing**

Write normalized reports, metadata, raw responses, command logs, and generated patches for each run.

- [ ] **Step 4: Build promotion**

Copy a selected verified run app to `app/current` and mark the run as promoted in metadata.

- [ ] **Step 5: Build comparison aggregation**

Read valid previous runs and generate benchmark JSON and Markdown summaries.

- [ ] **Step 6: Add harness unit tests**

Test config loading, agent parsing, workspace isolation, metadata validation, promotion, and comparison aggregation.

- [ ] **Step 7: Run verification**

Run:

```powershell
npm test
```

Expected: harness tests pass once `app/current` exists; before promotion, run the harness-specific test command defined in `package.json`.

- [ ] **Step 8: Update changelog**

Record harness modules and verification results.

## Phase 4: Adapter Layer

**Files:**
- Create: `homework-4/adapters/openai-sdk.js`
- Create: `homework-4/adapters/codex-chat.js`
- Create: `homework-4/adapters/mock.js`
- Create: `homework-4/adapters/README.md`
- Modify: `homework-4/pipeline/run.js`
- Modify: `homework-4/CHANGELOG.md`

- [ ] **Step 1: Implement the mock adapter**

Use a deterministic mock adapter for harness tests and local fallback when model credentials are unavailable.

- [ ] **Step 2: Implement the OpenAI SDK adapter**

Load agent specs and skills, call the configured OpenAI model, request structured output, apply file writes inside the run workspace, execute tests, and write all required reports.

- [ ] **Step 3: Implement the Codex chat adapter**

Generate prompt packets and a chat invocation guide. Validate externally produced Codex chat artifacts against the same run contract.

- [ ] **Step 4: Document optional Claude Code support**

Describe how a future Claude Code adapter would map the universal specs to `.claude/agents` or headless commands without making it required.

- [ ] **Step 5: Run adapter checks**

Run mock adapter tests and, when credentials are available, run:

```powershell
npm run pipeline:openai -- --scenario bug-001 --model gpt-5.3-codex --reasoning high
```

Expected: a complete run folder is produced with reports, metadata, fixed app files, tests, and `patch.diff`.

- [ ] **Step 6: Update changelog**

Record adapter implementation and whether live SDK execution was verified or blocked by missing credentials.

## Phase 5: Run Isolation, Promotion, And Benchmarking

**Files:**
- Create or update: `homework-4/runs/bug-001/<run-id>/...`
- Create: `homework-4/benchmark/scoring-rubric.md`
- Create or update: `homework-4/benchmark/bug-001-results.json`
- Create or update: `homework-4/benchmark/bug-001-comparison.md`
- Create or update: `homework-4/app/current/...`
- Modify: `homework-4/CHANGELOG.md`

- [ ] **Step 1: Execute at least one primary run**

Run the OpenAI SDK adapter if credentials are available. If not, run the mock adapter and clearly mark live SDK execution as blocked in metadata and documentation.

- [ ] **Step 2: Validate run artifacts**

Confirm the run contains `run-metadata.json`, app copy, patch, verified research, fix summary, security report, test report, command log, and raw responses where applicable.

- [ ] **Step 3: Promote the selected run**

Run:

```powershell
npm run promote -- --scenario bug-001 --run run-001
```

Expected: `app/current` contains the selected fixed app and passes its tests.

- [ ] **Step 4: Generate comparison**

Run:

```powershell
npm run compare -- --scenario bug-001
```

Expected: benchmark JSON and Markdown files summarize all valid previous runs.

- [ ] **Step 5: Update changelog**

Record the promoted run id, adapter, model, verification result, and benchmark output files.

## Phase 6: Documentation And Evidence

**Files:**
- Create: `homework-4/README.md`
- Create: `homework-4/HOWTORUN.md`
- Create: `homework-4/API_REFERENCE.md`
- Create: `homework-4/ARCHITECTURE.md`
- Create: `homework-4/TESTING_GUIDE.md`
- Create or update: `homework-4/docs/screenshots/...`
- Modify: `homework-4/CHANGELOG.md`

- [ ] **Step 1: Write README**

Include author/student info, overview, Mermaid pipeline diagram, model/adapters table, baseline/current explanation, quick start, benchmark summary, and links to detailed docs.

- [ ] **Step 2: Write HOWTORUN**

Include install, baseline verification, primary pipeline run, Codex chat prepare/validate flow, promotion, comparison, and final tests.

- [ ] **Step 3: Write API_REFERENCE**

Document CLI commands, pipeline commands, adapter contract, run metadata schema, and artifact formats.

- [ ] **Step 4: Write ARCHITECTURE**

Document harness, adapters, artifact flow, run isolation, model policy, and write safety rules with Mermaid diagrams.

- [ ] **Step 5: Write TESTING_GUIDE**

Document baseline expected-failure verification, current app tests, harness tests, adapter validation, benchmark checks, and manual QA.

- [ ] **Step 6: Capture screenshots**

Capture evidence for pipeline run, Codex chat workflow or prompt preparation, tests passing, security report, and benchmark comparison.

- [ ] **Step 7: Update changelog**

Record documentation and evidence changes.

## Phase 7: Final Verification

**Files:**
- Modify as needed: `homework-4/CHANGELOG.md`
- Create or update: final PR description draft if useful

- [ ] **Step 1: Run baseline verification**

Run:

```powershell
npm run verify:baseline
```

Expected: exits 0 by confirming the seeded baseline issues exist.

- [ ] **Step 2: Run primary pipeline**

Run:

```powershell
npm run pipeline:openai -- --scenario bug-001 --model gpt-5.3-codex --reasoning high
```

Expected: complete primary run artifacts are produced. If credentials are unavailable, document the blocker and provide mock adapter verification evidence.

- [ ] **Step 3: Promote and test current app**

Run:

```powershell
npm run promote -- --scenario bug-001 --run run-001
npm test
```

Expected: final tests pass for harness and promoted app.

- [ ] **Step 4: Generate comparison**

Run:

```powershell
npm run compare -- --scenario bug-001
```

Expected: benchmark files are regenerated from run data.

- [ ] **Step 5: Review diff**

Run:

```powershell
git status --short
git diff --stat
```

Expected: only Homework 4 files changed unless a root-level change was intentionally made and documented.

- [ ] **Step 6: Final changelog review**

Confirm every implementation increment has a matching `homework-4/CHANGELOG.md` entry.

## Sub-Agent Strategy For Later Phases

Use no subagents for Phase 0 because it is docs-only and tightly coupled.

For implementation phases, use subagents only when the current platform permits spawning and the orchestrator has explicit confirmation for any nondefault model or reasoning settings.

- Worker A: sample app and bug scenario artifacts.
- Worker B: universal harness and run isolation.
- Worker C: OpenAI SDK and Codex chat adapters.
- Worker D: benchmark and documentation.
- Reviewer agents: spec compliance after each phase, code quality after harness and adapters, final review before completion.

The orchestrator owns decomposition, integration, final validation, changelog consistency, and the user-facing summary.

## Starter Message For A New Implementation Thread

```text
We are on branch homework-4-submission in C:\Work\Codex\SETU-HW\gen-ai-se-hw.

Continue Homework 4 after Phase 0. Read these files first:
1. AGENTS.md
2. HOMEWORK_STANDARDS.md
3. homework-4/TASKS.md
4. homework-4/docs/superpowers/specs/2026-05-21-agentic-pipeline-design.md
5. homework-4/docs/superpowers/plans/2026-05-21-homework-4-agentic-pipeline.md
6. homework-4/CHANGELOG.md

Do not modify previous homework folders. Implement the next approved phase only, update homework-4/CHANGELOG.md in the same step, and run the verification commands listed in the plan for that phase.
```

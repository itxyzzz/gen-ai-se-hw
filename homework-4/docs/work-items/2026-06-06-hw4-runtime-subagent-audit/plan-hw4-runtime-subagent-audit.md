# HW4 Runtime Sub-Agent Audit Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add lightweight enforcement that every future Homework 4 pipeline run records de-facto sub-agent use and runtime model evidence in `run-metadata.json`.

**Architecture:** `run-metadata.json` becomes the portable audit surface. Native hooks and plugin events are adapter-specific ingestion mechanisms, while Codex and generic tools fall back to adapter-recorded evidence or an explicit unavailable reason.

**Tech Stack:** Markdown workflow harness, adapter instructions, JSON run metadata contract, PowerShell-based documentation verification.

Work ID: `2026-06-06-hw4-runtime-subagent-audit`
Short ID: `hw4-runtime-subagent-audit`
Status: Approved

---

## Implementation summary

This is a documentation and artifact-contract change for the text-first Homework 4 pipeline. The implementation should not add a JavaScript harness or mutate historical run evidence. Instead, it should update the harness, adapters, and homework docs so future runs are forced to record actual sub-agent usage in a common `runtimeSubagentAudit` object.

The key design choice is portability by schema, not portability by hook implementation. Claude Code, Google Antigravity, Open Code, Codex, and generic tools expose different runtime event surfaces. Each adapter should therefore name the strongest mechanism it can use and map that evidence into the same compact metadata shape.

## Files and interfaces

Expected implementation files:

- Modify: `homework-4/skills/pipeline-harness-wrapper.md`
  - Add `runtimeSubagentAudit` to required metadata.
  - Add compact field requirements and stop/block guidance for missing audit data.
- Modify: `homework-4/adapters/claude-code.md`
  - Document `PostToolUse:Agent`, `SubagentStart`, `SubagentStop`, and `Stop` usage.
- Modify: `homework-4/adapters/google-antigravity.md`
  - Document `PostToolUse:invoke_subagent` and `Stop` usage.
- Modify: `homework-4/adapters/open-code.md`
  - Document plugin event capture through `tool.execute.before` / `tool.execute.after` or equivalent task evidence.
- Modify: `homework-4/adapters/codex-chat.md`
  - Document adapter-recorded evidence and native hook use only when the Codex surface exposes reliable runtime coverage.
- Modify: `homework-4/adapters/generic-agent.md`
  - Require best-effort audit population or a `manual-unavailable` reason.
- Modify: `homework-4/API_REFERENCE.md`
  - Add metadata field definitions and allowed collection modes.
- Modify: `homework-4/ARCHITECTURE.md`
  - Add the portable enforcement-layer explanation.
- Modify: `homework-4/HOWTORUN.md`
  - Add a short operator check after running the pipeline.
- Modify: `homework-4/TESTING_GUIDE.md`
  - Add artifact review checks for `runtimeSubagentAudit`.
- Modify: `homework-4/README.md`
  - Add a brief portability note without expanding the reflection section heavily.
- Modify: `homework-4/CHANGELOG.md`
  - Add a newest-first implementation entry before the implementation commit.

Interface change:

- Future `run-metadata.json` files must include `runtimeSubagentAudit`.
- Historical run metadata is not backfilled.

## Model and Sub-agent Strategy

Current orchestration: Codex desktop, GPT-5 class model, reasoning effort not explicitly selected by operator.
Fit assessment: The implementation is mostly documentation and contract editing. Risk is moderate because inconsistent wording could overpromise portability or accidentally weaken the HW4 evidence contract; blast radius is limited to Homework 4 instructions.
Recommended change: None for implementation. Use the current orchestration model. Use high attention for final consistency review.

Sub-agents: None. The edits are tightly coupled across a small set of Markdown files, and consistency is more important than parallel throughput.

## Tasks

### Task 1: Update harness metadata contract

**Files:**
- Modify: `homework-4/skills/pipeline-harness-wrapper.md`

- [ ] **Step 1: Add `runtimeSubagentAudit` to required run metadata**

  In the `Required Artifacts` section, update the `run-metadata.json` requirement so it includes the existing fields plus `runtimeSubagentAudit`.

- [ ] **Step 2: Add a compact runtime audit subsection**

  Add a subsection after the required metadata paragraph with these decisions:

  - `runtimeSubagentAudit.schemaVersion` is `1.0.0`.
  - `collectionMode` is one of `native-hook`, `plugin-event`, `adapter-recorded`, or `manual-unavailable`.
  - `subagentsUsed` is a boolean.
  - `events` contains one compact entry per observed or intentionally not-delegated stage.
  - Missing runtime details must be represented by `null`, omission, or a short `notes` explanation.
  - Raw hook payloads, long transcripts, and long report text should not be copied into metadata unless needed to explain a blocker.

- [ ] **Step 3: Add enforcement wording**

  In `Stop Conditions` or the completion checklist, state that a future run is blocked or incomplete if `runtimeSubagentAudit` is missing, unless `collectionMode` is `manual-unavailable` with a clear unavailable reason.

### Task 2: Update adapter-specific collection instructions

**Files:**
- Modify: `homework-4/adapters/claude-code.md`
- Modify: `homework-4/adapters/google-antigravity.md`
- Modify: `homework-4/adapters/open-code.md`
- Modify: `homework-4/adapters/codex-chat.md`
- Modify: `homework-4/adapters/generic-agent.md`

- [ ] **Step 1: Update Claude Code adapter**

  Add a concise runtime audit section:

  - Capture sub-agent launches with `PostToolUse` matched to `Agent`.
  - Use `SubagentStart` / `SubagentStop` for lifecycle and transcript evidence when available.
  - Use `Stop` as the final gate when available.
  - Map captured evidence to `runtimeSubagentAudit`.

- [ ] **Step 2: Update Google Antigravity adapter**

  Add a concise runtime audit section:

  - Capture sub-agent invocations with `PostToolUse` matched to `invoke_subagent`.
  - Use `Stop` as the final gate when available.
  - Map `define_subagent` / `invoke_subagent` evidence into `runtimeSubagentAudit`.

- [ ] **Step 3: Update Open Code adapter**

  Add a concise runtime audit section:

  - Capture task-subagent evidence through Open Code plugin events such as `tool.execute.before` / `tool.execute.after` or equivalent task evidence.
  - Use `plugin-event` when plugin capture is available.
  - Use `adapter-recorded` when the run can record task use but plugin events are not configured.

- [ ] **Step 4: Update Codex Chat adapter**

  Add a concise runtime audit section:

  - Use native hooks only when the active Codex surface exposes reliable hook coverage and sub-agent metadata.
  - Otherwise record decisions at the adapter/orchestrator boundary with `collectionMode: "adapter-recorded"`.
  - If no reliable runtime evidence is exposed, use `manual-unavailable` with a clear reason and planned model policy evidence.

- [ ] **Step 5: Update Generic adapter**

  Require the adapter to produce `runtimeSubagentAudit` using best available evidence, or to set `manual-unavailable` with a reason.

### Task 3: Update homework-scoped documentation

**Files:**
- Modify: `homework-4/API_REFERENCE.md`
- Modify: `homework-4/ARCHITECTURE.md`
- Modify: `homework-4/HOWTORUN.md`
- Modify: `homework-4/TESTING_GUIDE.md`
- Modify: `homework-4/README.md`

- [ ] **Step 1: Update `API_REFERENCE.md`**

  Add `runtimeSubagentAudit` to the run metadata table and list allowed `collectionMode` values with short definitions.

- [ ] **Step 2: Update `ARCHITECTURE.md`**

  Add a brief section explaining that hooks/plugins are not portable across tools; the portable enforcement layer is the shared `run-metadata.json` audit contract.

- [ ] **Step 3: Update `HOWTORUN.md`**

  Add a short post-run review step telling operators to inspect `runtimeSubagentAudit` and confirm whether sub-agents were used, which collection mode was used, and why details are unavailable when they are unavailable.

- [ ] **Step 4: Update `TESTING_GUIDE.md`**

  Add compact artifact review bullets:

  - `run-metadata.json` includes `runtimeSubagentAudit`.
  - `collectionMode` is one of the allowed values.
  - Each stage has either observed runtime evidence or a clear unavailable/not-used note.
  - Existing source and benchmark snapshots are not rewritten for this change.

- [ ] **Step 5: Update `README.md`**

  Add a short portability note near the model policy or workflow reflection discussion. Keep it to a few sentences.

### Task 4: Changelog and validation

**Files:**
- Modify: `homework-4/CHANGELOG.md`

- [ ] **Step 1: Add Homework 4 changelog entry**

  Add a newest-first entry under the title:

  ```markdown
  ## Homework 4 - Step 19: Runtime Sub-Agent Audit Contract

  ### Added
  - Added a portable runtime sub-agent audit contract for future pipeline runs.

  ### Changed
  - Updated harness, adapter, and homework documentation so de-facto sub-agent use is recorded in `run-metadata.json`.

  ### Tests
  - Verified documentation coverage for `runtimeSubagentAudit` across the harness, adapters, and homework docs.
  ```

- [ ] **Step 2: Run documentation coverage verification**

  Run:

  ```powershell
  Get-ChildItem -Path homework-4 -Recurse -File -Include *.md | Select-String -Pattern 'runtimeSubagentAudit'
  ```

  Expected: matches in the harness, all five adapters, `API_REFERENCE.md`, `ARCHITECTURE.md`, `HOWTORUN.md`, `TESTING_GUIDE.md`, and `README.md`.

- [ ] **Step 3: Verify preserved evidence was not edited**

  Run:

  ```powershell
  git status --short homework-4/runs homework-4/benchmark
  ```

  Expected: no output.

- [ ] **Step 4: Review scoped diff**

  Run:

  ```powershell
  git diff -- homework-4/skills/pipeline-harness-wrapper.md homework-4/adapters homework-4/API_REFERENCE.md homework-4/ARCHITECTURE.md homework-4/HOWTORUN.md homework-4/TESTING_GUIDE.md homework-4/README.md homework-4/CHANGELOG.md
  ```

  Expected: only the runtime sub-agent audit contract, adapter collection guidance, compact doc updates, and changelog entry are present.

## Validation commands

| Command | Expected result |
|---|---|
| `Get-ChildItem -Path homework-4 -Recurse -File -Include *.md | Select-String -Pattern 'runtimeSubagentAudit'` | Finds the audit term in the harness, five adapters, and relevant homework docs |
| `git status --short homework-4/runs homework-4/benchmark` | No output; preserved evidence remains unchanged |
| `git diff -- homework-4/skills/pipeline-harness-wrapper.md homework-4/adapters homework-4/API_REFERENCE.md homework-4/ARCHITECTURE.md homework-4/HOWTORUN.md homework-4/TESTING_GUIDE.md homework-4/README.md homework-4/CHANGELOG.md` | Diff is limited to planned HW4 audit documentation changes |

## Plan variance handling

Before approval, operator feedback edits this draft directly and does not require an amendment. After the approval commit or explicit handoff snapshot, approved plans are immutable snapshots. Record nontrivial implementation variance in `implementation-notes/variance-log.md`. Create a plan amendment named `plan-amendment-NNN-short-title-hw4-runtime-subagent-audit.md` and request operator approval before proceeding when post-freeze variance affects architecture, APIs, data, security, privacy, compliance, scope, acceptance criteria, or plan feasibility.

## Planning artifact freeze gate

When this plan is ready for operator review, follow `C:/Users/tanatarov/.agents/skills/dev-doc-harness/references/planning-freeze-gates.md`: stage the draft without committing, request approval or feedback, revise directly on feedback, and commit only after explicit approval.

After the approval commit, confirm model, reasoning-effort, and sub-agent policy choices and ask whether implementation should begin now.

## Completion criteria

- Acceptance criteria in `spec-hw4-runtime-subagent-audit.md` are met.
- Required validation commands have been run and recorded.
- Required Homework 4 documentation has been updated.
- `homework-4/CHANGELOG.md` has a newest-first Step 19 entry before commit.
- Variance log is present and current.
- De-facto sub-agent use for this implementation is reported as `Sub-agents: None` unless implementation uses unplanned sub-agents with operator approval.

## Approval

- Status: Approved
- Superseded by: None

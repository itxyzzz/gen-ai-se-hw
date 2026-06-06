# HW4 Sub-Agent Authorization Gate Plan

Work ID: `2026-06-06-hw4-subagent-authorization-gate`
Short ID: `hw4-subagent-authorization-gate`
Status: Draft

## Implementation summary

Implement a documentation and artifact-contract change that prevents silent
fallback from sub-agent execution to parent-session execution in Homework 4
pipeline runs. The implementation should make sub-agent stage execution the
default required runtime behavior, while acknowledging that some tools need
explicit operator authorization before they can spawn sub-agents.

The core implementation pattern is an authorization gate before `bug-researcher`:
first determine whether sub-agent spawning is available, then determine whether
the operator has authorized it. Missing authorization should ask for
confirmation. Missing capability should ask whether direct fallback is approved
or whether the run should be blocked. Direct execution is allowed only after an
explicit operator fallback approval, and the decision must be recorded in
`runtimeSubagentAudit.operatorAuthorization`.

This remains a text-first HW4 pipeline. Do not add scripts, package
dependencies, or executable harness code. Do not mutate historical run or
benchmark artifacts.

## Files and interfaces

Expected implementation files:

- Modify: `homework-4/skills/pipeline-harness-wrapper.md`
  - Add the sub-agent capability and authorization gate before stage execution.
  - Add operator confirmation wording.
  - Add direct-fallback approval rules.
  - Add `runtimeSubagentAudit.operatorAuthorization` requirements.
- Modify: `homework-4/adapters/codex-chat.md`
  - Explain Codex-specific spawning authorization constraints.
  - Require the confirmation gate when the prompt did not explicitly authorize
    sub-agents.
  - Forbid silent `manual-unavailable` fallback when the operator has not
    approved fallback.
- Modify: `homework-4/adapters/claude-code.md`
  - Apply the same authorization/fallback decision model to Claude Code.
- Modify: `homework-4/adapters/google-antigravity.md`
  - Apply the same authorization/fallback decision model to Google Antigravity.
- Modify: `homework-4/adapters/open-code.md`
  - Apply the same authorization/fallback decision model to Open Code.
- Modify: `homework-4/adapters/generic-agent.md`
  - Require best-effort capability detection and explicit fallback approval.
- Modify: `homework-4/API_REFERENCE.md`
  - Document `runtimeSubagentAudit.operatorAuthorization`.
- Modify: `homework-4/ARCHITECTURE.md`
  - Explain capability vs. authorization vs. fallback approval.
- Modify: `homework-4/HOWTORUN.md`
  - Add the preferred sub-agent launch phrase and the expected confirmation
    prompt for the shorter launch phrase.
- Modify: `homework-4/TESTING_GUIDE.md`
  - Add artifact-review checks for authorization and fallback status.
- Modify: `homework-4/README.md`
  - Add a compact note that the pipeline is designed to use sub-agents and asks
    before fallback.
- Modify: `homework-4/CHANGELOG.md`
  - Add a newest-first Homework 4 step entry at freeze/implementation time.

Interface changes:

- Future `run-metadata.json` files add
  `runtimeSubagentAudit.operatorAuthorization`.
- Allowed `operatorAuthorization.status` values are `confirmed`,
  `confirmed-after-gate`, `fallback-approved`, `declined`, and `not-required`.
- Historical run metadata is not backfilled.

## Model and Sub-agent Strategy

Current orchestration: Codex desktop, GPT-5 class model, reasoning effort not
explicitly selected by operator.
Fit assessment: The implementation is a bounded Markdown contract update with
moderate risk. The riskiest part is consistency across harness, adapters, and
documentation; there is no app code or data migration risk. Latency and cost are
secondary to preserving the intended HW4 pipeline semantics.
Recommended change: None for the implementation thread. Use careful main-thread
editing plus fresh documentation verification.

Sub-agents: None for implementing this planning item. The actual HW4 pipeline
runtime should use one sub-agent per stage, but the documentation update itself
is tightly coupled across a small set of Markdown files and benefits more from
single-threaded consistency than parallel editing. If the operator explicitly
requests a review sub-agent after implementation, use one read-only reviewer
with curated artifacts and no write scope.

## Tasks

### Task 1: Update the harness authorization gate

**Files:**
- `homework-4/skills/pipeline-harness-wrapper.md`

- [ ] Add a required runtime delegation authorization gate before stage
  execution.
- [ ] Define the three pre-stage states: available and authorized, available
  but not yet authorized, unavailable.
- [ ] Add the exact operator confirmation wording for the
  available-but-not-authorized case.
- [ ] State that direct execution is allowed only after explicit fallback
  approval.
- [ ] State when blocked run metadata is required.
- [ ] Add `operatorAuthorization` to the `runtimeSubagentAudit` field list.

### Task 2: Update adapter-specific behavior

**Files:**
- `homework-4/adapters/codex-chat.md`
- `homework-4/adapters/claude-code.md`
- `homework-4/adapters/google-antigravity.md`
- `homework-4/adapters/open-code.md`
- `homework-4/adapters/generic-agent.md`

- [ ] Update Codex Chat adapter wording so Codex asks for explicit confirmation
  when sub-agent tools exist but the launch prompt did not authorize spawning.
- [ ] Update Claude Code adapter wording to require the same gate before stage
  execution.
- [ ] Update Google Antigravity adapter wording to require the same gate before
  stage execution.
- [ ] Update Open Code adapter wording to require the same gate before stage
  execution.
- [ ] Update Generic adapter wording to require capability detection and
  explicit fallback approval.
- [ ] Ensure all adapters describe how to populate
  `operatorAuthorization.status` and `fallbackApproved`.

### Task 3: Update operator-facing documentation

**Files:**
- `homework-4/HOWTORUN.md`
- `homework-4/README.md`

- [ ] Add the preferred launch phrase:

  ```text
  Run HW4 pipeline using sub-agents. Spawn one sub-agent per stage. If sub-agent
  use is not already authorized or not available, ask me before falling back.
  ```

- [ ] Explain that `Run HW4 pipeline` remains recognized as pipeline intent but
  may prompt for sub-agent authorization before stage execution.
- [ ] Explain what it means to approve direct fallback.
- [ ] Keep the README note compact and avoid duplicating the full harness rules.

### Task 4: Update artifact-contract documentation

**Files:**
- `homework-4/API_REFERENCE.md`
- `homework-4/ARCHITECTURE.md`
- `homework-4/TESTING_GUIDE.md`

- [ ] Document `runtimeSubagentAudit.operatorAuthorization` and allowed status
  values in `API_REFERENCE.md`.
- [ ] Explain in `ARCHITECTURE.md` why capability, authorization, and fallback
  approval are separate decisions.
- [ ] Add `TESTING_GUIDE.md` review checks:
  - `subagentsExpected` is true for HW4 pipeline runs;
  - `subagentsUsed` is true unless fallback was explicitly approved;
  - `operatorAuthorization.status` matches the prompt/confirmation evidence;
  - blocked runs explain declined or unavailable sub-agent execution;
  - preserved historical evidence is not backfilled.

### Task 5: Changelog and validation

**Files:**
- `homework-4/CHANGELOG.md`

- [ ] Add a newest-first changelog entry for the implementation step before
  commit or PR closure.
- [ ] Run documentation coverage checks for the new authorization contract.
- [ ] Verify preserved run and benchmark evidence folders were not modified by
  the implementation.
- [ ] Review the scoped diff for contradictions, placeholders, accidental
  historical backfills, or silent-fallback language.

## Validation commands

| Command | Expected result |
|---|---|
| `Get-ChildItem -Path homework-4 -Recurse -File -Include *.md | Select-String -Pattern 'operatorAuthorization'` | Finds the metadata field in the harness and artifact-contract documentation |
| `Get-ChildItem -Path homework-4 -Recurse -File -Include *.md | Select-String -Pattern 'fallback-approved|confirmed-after-gate|sub-agent authorization'` | Finds authorization/fallback language in harness, adapters, and operator docs |
| `git status --short homework-4/runs homework-4/benchmark` | No implementation edits to preserved historical evidence, except unrelated pre-existing run work if intentionally left in the worktree |
| `git diff -- homework-4/skills/pipeline-harness-wrapper.md homework-4/adapters homework-4/API_REFERENCE.md homework-4/ARCHITECTURE.md homework-4/HOWTORUN.md homework-4/TESTING_GUIDE.md homework-4/README.md homework-4/CHANGELOG.md` | Diff is limited to the sub-agent authorization gate, fallback approval audit contract, docs, and changelog |

## Plan variance handling

Before approval, operator feedback edits this draft directly and does not
require an amendment. After the approval commit or explicit handoff snapshot,
approved plans are immutable snapshots. Record nontrivial implementation
variance in `implementation-notes/variance-log.md`. Create a plan amendment
named `plan-amendment-NNN-short-title-hw4-subagent-authorization-gate.md` and
request operator approval before proceeding when post-freeze variance affects
architecture, APIs, data, security, privacy, compliance, scope, acceptance
criteria, or plan feasibility.

## Planning artifact freeze gate

When this plan is ready for operator review, follow
`C:/Users/tanatarov/.agents/skills/dev-doc-harness/references/planning-freeze-gates.md`:
stage the draft without committing, request approval or feedback, revise
directly on feedback, and commit only after explicit approval.

After the approval commit, confirm model, reasoning-effort, and sub-agent
policy choices and ask whether implementation should begin now.

## Completion criteria

- Acceptance criteria in `spec-hw4-subagent-authorization-gate.md` are met.
- Required validation commands have been run and recorded.
- Required Homework 4 documentation has been updated.
- `homework-4/CHANGELOG.md` has a newest-first entry before commit.
- Variance log is present and current.
- De-facto sub-agent use for implementation is reported. Expected for this
  implementation plan: `Sub-agents: None`, unless the operator later authorizes
  a read-only review sub-agent.

## Approval

- Status: Draft
- Superseded by: None

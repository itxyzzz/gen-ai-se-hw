# Root README Portfolio Router Plan

Work ID: `2026-06-24-root-readme-portfolio-router`
Short ID: `root-readme-portfolio-router`
Status: Approved
Harness release: `0.4.0`
Schema: `schema:plan.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:models.strategy-required`, `rule:models.context-strategy`, `rule:models.approved-strategy-authorized`, `rule:models.fresh-confirmation`, `rule:lifecycle.commit-message-format`, `rule:lifecycle.variance-policy`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, `rule:freeze.stop-before-implementation`

## Implementation summary

Rewrite the root README as a concise portfolio and evidence router. The implementation should preserve the current assignment-template README by moving it under a clear trailing section, then place the new portfolio narrative above it.

The new narrative should be source-backed and claim-safe. Use each homework README for local package facts and the actual GitHub PR descriptions for personal workflow notes, challenges, verification, and evidence framing. Use the review excerpt to choose emphasis: strong signal for AI-assisted systems/product analysis, workflow automation, documentation, testing, and prototype engineering; careful caveats for production finance, ML, compliance, and cloud/SRE claims.

Keep the README dense. It should help a reviewer decide what to inspect next, not replace the detailed homework docs.

## Files and interfaces

Expected implementation changes:

- `README.md`: rewrite top content, preserve existing assignment-template text at the end.
- `CHANGELOG.md`: update only before the planning approval commit and before the implementation commit as required by harness/repository rules.

Planning package files:

- `docs/work-items/2026-06-24-root-readme-portfolio-router/spec-root-readme-portfolio-router.md`
- `docs/work-items/2026-06-24-root-readme-portfolio-router/plan-root-readme-portfolio-router.md`
- `docs/work-items/2026-06-24-root-readme-portfolio-router/implementation-notes/variance-log.md`

Interfaces expected to remain stable:

- Homework folder contents.
- Source code, tests, scripts, sample data, screenshots, MCP configuration, PR bodies, and branch topology.
- Runtime behavior for all homework projects.

## Model and Sub-agent Strategy

Current orchestration: Codex desktop, exact model/profile and reasoning effort not exposed in the local UI.
Fit assessment: Small/medium documentation rewrite with moderate ambiguity and moderate reputational risk because it affects external portfolio presentation and claim safety. No runtime or production blast radius.
Recommended change: None. Use the current orchestration thread with careful source checking.

Sub-agents: None. The work is a single-file implementation with one coherent editorial voice; sub-agent coordination would add overhead and increase risk of inconsistent claims.

## Tasks

- [ ] Confirm worktree state and avoid unrelated untracked Homework 6 files.
- [ ] Re-read the frozen spec, plan, and variance log after approval.
- [ ] Re-open `README.md` and the six homework `README.md` files immediately before editing.
- [ ] Re-open or re-query the actual GitHub PR bodies for PR #1, #5, #6, #8, #9, and #10 if any source detail is uncertain.
- [ ] Draft the new top-level README sections:
  - Portfolio identity and claim boundary.
  - What to review first.
  - Six-homework progression.
  - Skills demonstrated.
  - Evidence map with homework README and PR links.
  - AI-assistance and authorship note.
  - Safe/unsafe claim boundaries.
- [ ] Move the current assignment-template README text under a clear trailing `Original Course Assignment README` label without dropping content.
- [ ] Remove unnecessary duplication and keep the new text concise enough to act as a router.
- [ ] Update root `CHANGELOG.md` with the implementation entry before committing implementation.
- [ ] Run validation commands and inspect the diff for scope, link, and claim-safety issues.
- [ ] Update `implementation-notes/variance-log.md` only if implementation deviates from the approved plan in a nontrivial way.

## Planned commits

| Stage | Planned subject | Changelog title or snippet | Notes |
|---|---|---|---|
| Planning approval | `root-readme-portfolio-router spec: plan portfolio router README rewrite` | `2026-06-24-root-readme-portfolio-router: plan portfolio router README rewrite` | Approval commit for spec, plan, variance log, and required changelog entry. |
| Implementation | `root-readme-portfolio-router docs: rewrite root README as portfolio router` | `2026-06-24-root-readme-portfolio-router: rewrite root README as portfolio router` | Implementation commit for root README rewrite and required changelog entry. |

## Validation commands

| Command | Expected result |
|---|---|
| `git status --short` | Shows only planned README, root CHANGELOG, and work-item package changes plus pre-existing unrelated untracked Homework 6 files. |
| `git diff --check -- README.md CHANGELOG.md docs/work-items/2026-06-24-root-readme-portfolio-router` | Exits 0 with no whitespace errors. |
| `Select-String -Path README.md -Pattern 'Original Course Assignment README'` | Finds the preservation label. |
| `Select-String -Path README.md -Pattern 'homework-1/README.md','homework-2/README.md','homework-3/README.md','homework-4/README.md','homework-5/README.md','homework-6/README.md','/pull/1','/pull/5','/pull/6','/pull/8','/pull/9','/pull/10'` | Finds all required homework README references and actual PR links. |
| `Select-String -Path README.md -Pattern 'production banking','AML','KYC','PCI','ML model'` | Used for manual review: any hits must be caveated as non-claims rather than presented as experience. |

## Plan variance handling

Use `rule:lifecycle.variance-policy`. Before freeze, edit this draft directly for operator feedback. After freeze, record nontrivial implementation variance in `implementation-notes/variance-log.md`; use a plan amendment for high-impact architecture, API, data, security, privacy, compliance, scope, acceptance-criteria, or feasibility changes.

Expected low-risk editorial adjustments that do not require a variance entry:

- Reordering README sections while preserving the approved content goals.
- Tightening language for concision.
- Adding or removing a single supporting link when it improves reviewer routing without changing scope.

Variance requiring a log entry:

- Choosing not to include one of the six homework narratives.
- Omitting one of the required PR links.
- Adding a new category of claim, role positioning, or evidence source not covered in the spec.
- Editing any file outside `README.md`, `CHANGELOG.md`, or this work-item package.

## Planning artifact freeze gate

Draft review status: Draft package reviewed and approved by operator on 2026-06-24.
Approval commit status: Approved for plan-only freeze commit.
Post-freeze implementation authorization: Not authorized yet; implementation must wait for a fresh operator instruction after the approval freeze gate.

## Completion criteria

- Acceptance criteria in `spec-root-readme-portfolio-router.md` are met.
- Required validation commands have been run and recorded in the final response or commit notes.
- Required documentation artifacts have been created or updated.
- `CHANGELOG.md` has a newest-first entry for the work before each commit.
- Commit subjects match the approved planned subjects or recorded variance, and changelog title snippets are synchronized.
- Variance log is present and current.
- De-facto sub-agent use is reported; expected value is zero sub-agents.

## Approval

- Status: Approved
- Superseded by: none

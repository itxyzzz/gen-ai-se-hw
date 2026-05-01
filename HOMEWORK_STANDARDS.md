# Homework Implementation Standards (HW2+)

This document defines the default delivery, documentation, and workflow standards for **Homework 2 and later**.
Its goal is to keep every homework submission consistent, easy to review, and easy to run.

---

## 1) Scope and Precedence

- Applies to each `homework-N/` folder unless a specific homework task overrides it.
- If there is a conflict:
  1. Homework task requirements win.
  2. Repository `AGENTS.md` policy is next.
  3. This standards file is baseline guidance.

---

## 2) Branch and PR Model (Clarified)

There are **two valid implementation modes**; choose based on where implementation is performed.

### A) Local implementation mode
Use this when changes are developed locally in the repo.

- Work only on the homework-specific branch (for example `homework-x-submission`).
- Implement in small incremental steps.
- Each step must:
  1. start with a brief plan,
  2. implement and verify,
  3. update `CHANGELOG.md`,
  4. end with a local commit.
- No intermediate GitHub PR is required for each step.
- In local mode, do not require PRs into `homework-x-submission`; commit-level gates are sufficient.
- Final submission PR is created manually by student in GitHub from homework branch to `main`.

### B) Web/agent implementation mode
Use this when implementation happens through web/remote agent sessions (including Codex Web UI).

- Start each web/agent session from the intended homework-specific submission branch (for example `homework-x-submission`).
- Do not introduce extra `-web` branches just to satisfy Codex workflow.
- Codex may auto-create a temporary working branch (for example `codex/<id>`) for the session.
- Implement in small incremental steps.
- Each step must:
  1. start with a brief plan,
  2. implement and verify,
  3. update `CHANGELOG.md`,
  4. end with a commit,
  5. open/update a step PR.
- When the session starts on the intended homework branch, step PRs should target that same branch by default.
- Final submission remains a single PR from the homework branch to `main`.

### C) Final homework submission PR (single PR)
Regardless of mode above:
- Exactly one final homework PR is prepared from the homework branch.
- Base: `main`
- Compare: homework-specific branch
- That PR must contain the full narrative/evidence required by the course task.


### D) Branch simplification for Codex Web UI
Default branch model for HW2+:
- Canonical branch: `homework-x-submission`
- Temporary agent branch: auto-created `codex/<id>` (managed by Codex UI)

Rules:
- Do not create additional long-lived `-web` branches solely for agent sessions.
- Step PRs created by Codex are review/integration PRs and should target `homework-x-submission` when that branch is selected at session start.
- Final submission PR to `main` always comes from `homework-x-submission`.


---

## 2.1) Quality Gates by Mode (Enforcement Point)

Use the same gate checklist in both modes; only the enforcement point changes.

### Local mode (enforced before each commit)
A step is not complete until all are true:
- Plan captured (brief note)
- Implementation verified (commands/results recorded as needed)
- `CHANGELOG.md` updated for that step
- If behavior changed, related docs updated in same step (`API_REFERENCE.md`, `HOWTORUN.md`, and/or `README.md`)
- Commit message is scoped and descriptive

### Web/agent mode (enforced in each step PR)
A step PR is not review-ready until all are true:
- Everything required in local mode
- PR description includes what changed, how it was verified, and doc/changelog updates
- If behavior changed, evidence links/screenshots are included or referenced

## 3) Recommended Homework Folder Structure

Use this baseline structure for each homework:

```text
homework-N/
├── README.md
├── HOWTORUN.md
├── API_REFERENCE.md
├── ARCHITECTURE.md
├── TESTING_GUIDE.md
├── CHANGELOG.md
├── TASKS.md                      # copy/reference task scope for convenience
├── src/
├── tests/
├── demo/
│   ├── run.sh
│   ├── run.bat
│   ├── sample-requests.http
│   └── (optional lifecycle helpers: start/stop/restart scripts)
└── docs/
    ├── screenshots/
    ├── superpowers/              # optional: plans/specs/prompts
    └── <project>.postman_collection.json
```

Notes:
- Keep runnable demo and sample requests under `demo/`.
- Keep reviewer evidence and extra design artifacts under `docs/`.
- Keep fixtures close to tests unless assignment asks for dedicated sample-data placement.

---

## 4) Required Deliverables Per Homework

Unless explicitly waived by assignment:

1. `README.md`
2. `HOWTORUN.md`
3. `API_REFERENCE.md`
4. `ARCHITECTURE.md`
5. `TESTING_GUIDE.md`
6. `CHANGELOG.md`
7. `docs/screenshots/` evidence
8. `demo/` runnable helpers and sample requests
9. Postman collection (or equivalent API collection) in `docs/`

---

## 5) Documentation Intent and Minimum Content

### `README.md` (developer entry point)
Intent: fast orientation for someone joining the project.
Must include:
- Problem overview and implemented scope
- Setup summary and quick start
- Feature map and project structure
- Link map to other docs
- At least one Mermaid diagram

### `HOWTORUN.md` (operator/reviewer runbook)
Intent: exact reproducible execution path.
Must include:
- Prerequisites and environment assumptions
- Exact start/stop/restart commands
- Test commands and expected outcomes
- Common troubleshooting notes
- Minimal smoke-check requests

### `API_REFERENCE.md` (API consumer contract)
Intent: authoritative request/response contract.
Must include:
- Endpoint catalog
- Request/response examples
- Validation and error formats
- cURL examples per endpoint
- Versioning/compatibility notes if behavior changed

### `ARCHITECTURE.md` (technical design rationale)
Intent: explain why system is built this way.
Must include:
- High-level component diagram (Mermaid)
- Data flow or sequence diagram (Mermaid)
- Design decisions and trade-offs
- Security/performance considerations
- Known non-goals/limitations

### `TESTING_GUIDE.md` (QA execution and strategy)
Intent: make test coverage and quality strategy auditable.
Must include:
- Test strategy and scope
- Test pyramid diagram (Mermaid)
- Test run matrix/commands
- Manual checklist
- Performance/coverage summary table

### `CHANGELOG.md` (incremental delivery log)
Intent: clear user-impact history for each incremental step.

Required structure:
- `## <current-homework-iteration>` (for example: `## Homework 2 - Step 3`)
  - `### Added`
  - `### Changed`
  - `### Fixed`
  - `### Tests`

Authoring rules:
- Update on **every** incremental commit/step.
- Describe behavior/contract impact, not internal trivia.
- Call out endpoint and validation rule changes explicitly.
- Log documentation/demo asset updates when behavior changed.
- Summarize test additions/changes in `### Tests`.

---

## 6) Documentation Timing in the Commit/PR Cycle

Documentation should be written continuously, not postponed.

### Step lifecycle (applies to both modes)
1. **Plan**
   - Add/adjust architecture notes when design intent changes.
2. **Implement**
   - Update API reference and runbook alongside code changes.
3. **Verify**
   - Record testing commands/outcomes in testing guide if new patterns are introduced.
4. **Record**
   - Update changelog for that exact step.
5. **Finish step**
   - Local mode: commit.
   - Web mode: commit + PR/update PR to remote homework branch.

### Finalization lifecycle (before final PR to `main`)
- Tighten README narrative and cross-links.
- Ensure diagrams are accurate and readable.
- Ensure HOWTORUN and API_REFERENCE match current behavior exactly.
- Ensure screenshot evidence is complete and current.

---

## 7) Diagram and Evidence Requirements

- Include at least **3 Mermaid diagrams** across docs:
  - one in `README.md`
  - one in `ARCHITECTURE.md`
  - one in `TESTING_GUIDE.md`
- Store evidence screenshots in `docs/screenshots/`.
- Keep diagrams focused; avoid oversized unreadable blocks.

---

## 8) Demo and Manual QA Package

Each homework should provide a minimal manual QA pack:
- `demo/run.sh` and `demo/run.bat`
- optional lifecycle scripts (`start/stop/restart`) when useful
- `demo/sample-requests.http` or equivalent script
- sample fixture data for positive/negative checks (as relevant)
- Postman collection with `baseUrl` variable and short expected-result notes

---

## 9) AI-Assistance Documentation Standard

For every homework:
- State which AI tools/models were used and for what.
- Record what was manually validated by the student.
- Keep prompt/design artifacts (optional) under `docs/superpowers/` when helpful.
- Preserve screenshots that demonstrate meaningful AI-assisted workflow.

---

## 10) Review Checklist (Per Increment and Before Final PR)

Per incremental step:
- [ ] Plan captured (brief)
- [ ] Implementation completed
- [ ] Validation performed
- [ ] `CHANGELOG.md` updated
- [ ] Step closed (commit only in local mode; commit + step PR/update PR in web mode)

Before final PR to `main`:
- [ ] All required docs are present and non-placeholder
- [ ] Mermaid minimum (>=3) is met
- [ ] HOWTORUN commands work as written
- [ ] API reference matches implementation
- [ ] Testing guide includes execution commands and checklist
- [ ] Demo assets and collection are runnable/importable
- [ ] Screenshots are complete and current
- [ ] Changelog reflects the full implementation history

---

## 11) AGENTS.md Integration Guidance

`AGENTS.md` should stay concise and only enforce non-negotiable workflow rules.
This file remains the detailed operational reference for structure, documentation content, timing, and quality gates.

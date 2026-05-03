# Homework Implementation Standards (HW2+)

This document defines the default delivery, documentation, and workflow standards for Homework 2 and later. It is the detailed operational reference; `AGENTS.md` stays the concise non-negotiable rule layer.

---

## 1) Scope and Precedence

- Applies to each `homework-N/` folder unless a specific homework task explicitly overrides a content requirement.
- Repository workflow guardrails in `AGENTS.md` are mandatory for all agents.
- Homework task files define assignment deliverables and can add or refine homework-specific requirements.
- This file provides the reusable baseline for planning, implementation, documentation, evidence, and review.

---

## 2) Context-Model-Prompt Practice

Use each homework to practice setting the right agent context, choosing the right capability level, and writing efficient prompts.

### Context
- Durable context belongs in repo and homework docs, not repeated in every prompt.
- Start from `AGENTS.md`, this file, the relevant `TASKS.md`, existing homework docs, and current code/test state.
- Move repeated process agreements up to the root control layer when they apply across homeworks or future projects.

### Model and tools
- Record which AI tools, models, plugins, and quality controls were used when that information is available.
- In local mode, prefer stronger/deeper reasoning for planning, architecture, debugging, cross-file changes, and final review.
- Use lighter handling only for narrow mechanical edits or simple documentation cleanup.
- If model choice, reasoning effort, or plugins are unavailable, disclose that limitation and use the web-mode compensation checklist below.

### Prompt
Good task prompts should state:
- Goal: what outcome is wanted.
- Scope: what should and should not change.
- Context: which task files, standards, plans, or docs apply.
- Constraints: stack, style, verification, documentation, and timing requirements.
- Done criteria: what evidence proves the step is complete.

---

## 3) Work Modes and Branch Model

Use one canonical homework branch: `homework-N-submission`.

- Never implement homework changes on `main`.
- Do not create long-lived `-web` branches solely for Codex or other agent tools.
- Temporary agent branches such as `codex/<id>` are acceptable when created by the tool.
- The final homework submission PR is always from `homework-N-submission` to `main`.

### Local implementation mode
Use when changes are developed locally in this repository.

- Work directly on `homework-N-submission`.
- Implement in small, reviewable steps.
- Enforce quality gates before each local commit.
- Intermediate GitHub PRs into the homework branch are not required.

### Web/agent implementation mode
Use when implementation happens through Codex Web or another remote agent workflow.

- Start each session from the intended `homework-N-submission` branch.
- Step PRs created by the agent should target `homework-N-submission`.
- Each step PR must include what changed, how it was verified, and what docs/changelog entries were updated.
- The final submission remains a single PR from `homework-N-submission` to `main`.

---

## 4) Increment Quality Gates

Every local commit or web/agent step PR must satisfy the same gate checklist:

- Plan captured before implementation.
- Relevant existing files, docs, and tests inspected.
- Implementation completed within the stated scope.
- Verification run and result recorded where useful.
- Applicable docs updated with behavior changes.
- Applicable `CHANGELOG.md` updated for the step.
- Diff reviewed for unrelated changes, placeholders, contradictions, and accidental generated noise.

Local mode enforces these gates before commit. Web/agent mode enforces them before the step PR is review-ready.

---

## 5) Web Mode Compensation

Codex Web and similar environments may lack Superpowers plugins, model selection, and reasoning-effort control. When those controls are unavailable, the agent must manually follow equivalent discipline:

- State a brief plan before editing.
- Use test-first or focused-verification-first workflow for behavior changes when practical.
- For docs-only changes, define the review checks before editing.
- Debug from observed errors, logs, diffs, and test output.
- Run fresh verification before claiming completion.
- Update docs and changelog in the same step as the behavior or policy change.
- In the PR description or AI-assistance notes, disclose missing quality levers and the practices used to compensate.

---

## 6) Homework Folder Structure and Deliverables

Use this baseline structure unless the assignment requires a different layout:

```text
homework-N/
├── README.md
├── HOWTORUN.md
├── API_REFERENCE.md
├── ARCHITECTURE.md
├── TESTING_GUIDE.md
├── CHANGELOG.md
├── TASKS.md
├── src/
├── tests/
├── demo/
│   ├── run.sh
│   ├── run.bat
│   ├── sample-requests.http
│   └── optional start/stop/restart helpers
└── docs/
    ├── screenshots/
    ├── superpowers/
    └── optional API collection or extra design evidence
```

Required per homework unless explicitly waived:
- Source code and tests.
- `README.md`, `HOWTORUN.md`, `API_REFERENCE.md`, `ARCHITECTURE.md`, `TESTING_GUIDE.md`, and `CHANGELOG.md`.
- Evidence screenshots under `docs/screenshots/`.
- Demo helpers and sample requests under `demo/`.
- Sample data, fixtures, coverage reports, benchmarks, or API collections when required by the task.

Create `homework-N/CHANGELOG.md` early if it does not already exist.

---

## 7) Documentation Standards

Write documentation continuously, not at the end.

- `README.md`: developer entry point with overview, scope, quick start, feature map, project structure, links to detailed docs, and at least one Mermaid diagram.
- `HOWTORUN.md`: reviewer runbook with prerequisites, exact run/stop/restart commands, test commands, expected outcomes, troubleshooting, and smoke checks.
- `API_REFERENCE.md`: API contract with endpoint catalog, schemas, examples, validation/error formats, and cURL examples.
- `ARCHITECTURE.md`: technical rationale with component and data-flow diagrams, design decisions, trade-offs, security/performance notes, and known limitations.
- `TESTING_GUIDE.md`: QA guide with strategy, test pyramid diagram, command matrix, manual checklist, coverage summary, and performance notes when relevant.

Keep diagrams focused and readable. Across each homework, include at least three Mermaid diagrams: one in `README.md`, one in `ARCHITECTURE.md`, and one in `TESTING_GUIDE.md`.

---

## 8) Changelog Standards

Each homework has its own `CHANGELOG.md`; repository-level policy changes use the root `CHANGELOG.md`.

Homework changelog entries should use this shape:

```markdown
## Homework N - Step X

### Added
### Changed
### Fixed
### Tests
```

Authoring rules:
- Update on every incremental step before commit or PR closure.
- Describe behavior, contract, documentation, and verification impact.
- Call out endpoint, validation, data model, workflow, and run-command changes explicitly.
- Avoid internal trivia that does not help a reviewer understand the submission.

---

## 9) Evidence, Demo, and AI-Assistance Records

Each homework should provide a minimal manual QA package:
- `demo/run.sh` and `demo/run.bat`.
- `demo/start.ps1`, `demo/stop.ps1`, and `demo/restart.ps1` when a managed local server lifecycle is useful.
- `demo/sample-requests.http` or equivalent request script.
- Positive and negative sample data where imports, validation, or workflows depend on data files.
- Postman collection or equivalent API collection when useful for review.

For AI-assistance documentation:
- State tools, models, plugins, and workflow modes used.
- Record what was manually validated by the student.
- Preserve meaningful prompt/design artifacts under `docs/superpowers/` or an equivalent docs subfolder.
- Preserve screenshots showing meaningful AI-assisted workflow, app behavior, test/coverage evidence, and manual checks.
- For Codex Web work, include the missing quality levers and the manual compensation practices used.

### Managed PowerShell lifecycle scripts

For Spring Boot or similar long-running homework APIs, prefer the reusable pattern from `homework-1/demo/AppLifecycle.ps1` instead of ad hoc `Start-Process mvn spring-boot:run` wrappers.

Required behavior for managed lifecycle scripts:
- Start a stable Java process directly, either with `java -cp "target/classes;target/dependency/*" <MainClass>` after `compile dependency:copy-dependencies`, or with `java -jar target/<artifact>.jar` after packaging.
- Do not record a transient Maven launcher PID as the managed app PID.
- Record the app PID, process start time, port, and state file path under `target/managed-app.json`.
- On start, fail clearly if the expected port already has a responding service that is not the recorded managed process.
- Wait for a documented health/smoke endpoint to return success before reporting that the app started.
- On stop, verify the PID and process start time before killing it, then mark the state inactive or otherwise handle sandbox/file-lock cleanup robustly.
- Include tests or a manual verification note for start, smoke request, stop, and no-listener-left-on-port behavior.

---

## 10) Review Checklist

Per increment:
- [ ] Plan captured.
- [ ] Existing context inspected.
- [ ] Implementation completed within scope.
- [ ] Verification run.
- [ ] Relevant docs updated.
- [ ] Applicable changelog updated.
- [ ] Diff reviewed.
- [ ] Local mode: commit ready.
- [ ] Web/agent mode: step PR description ready.

Before final PR to `main`:
- [ ] All required docs are present and non-placeholder.
- [ ] Mermaid minimum is met.
- [ ] Run commands work as written.
- [ ] API reference matches implementation.
- [ ] Testing guide includes commands, coverage, and checklist.
- [ ] Demo assets and sample data are runnable/importable.
- [ ] Screenshots and evidence are current.
- [ ] Changelog reflects the full implementation history.
- [ ] Final PR body stands on its own with summary, AI workflow, verification, challenges, and evidence links.

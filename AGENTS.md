# Repository Instructions

## Context Hierarchy

Build task context in this order before making changes:

1. System, tool, plugin, sandbox, and model constraints.
2. Repository instructions in this file and `HOMEWORK_STANDARDS.md`.
3. The relevant homework task file, usually `homework-N/TASKS.md`.
4. The user's immediate prompt, including scope and stop conditions.
5. Discovered working state from files, tests, logs, diffs, and branch status.

If instructions conflict, preserve higher-priority system/user constraints, then apply the most specific repo or homework requirement. Homework task requirements define deliverables; this file defines non-negotiable workflow guardrails.

## Git Author Email

All commits in this repository must use a private email address for author and committer metadata.

Allowed email domains:
- `@gmail.com`
- `@users.noreply.github.com`

Do not use company, contractor, school, client, or other organizational email domains in Git commit metadata.

## Mandatory Task Preflight

Before implementing any homework or repository-policy change:

1. Confirm the current branch and do not implement homework work on `main`.
2. Read this file and `HOMEWORK_STANDARDS.md`.
3. Read the relevant `homework-N/TASKS.md` and existing homework docs/changelog when working inside a homework folder.
4. Inspect current files and tests before editing.
5. Identify whether the work is in local mode or web/agent mode and apply the matching gates.

## Local Tooling Notes

- In the Codex desktop Windows sandbox, the bundled `rg.exe` may fail with `Access is denied`. If `rg` fails to start, do not spend time retrying it or escalating just for search. Immediately fall back to native PowerShell discovery:
  - File search: `Get-ChildItem -Recurse -File`, with `-Include` or `-Filter` when useful.
  - Text search: `Get-ChildItem -Recurse -File | Select-String -Pattern ...`.
- Keep this fallback quiet in routine work unless the tool failure affects verification, timing, or the user explicitly asks about tooling.
- For opening local Swagger UI through Codex Desktop/browser-use, use the probe-first workflow in `documentation/agent-workflows/local-swagger-browser.md` instead of starting the app blindly.

## Repository-Level Change Logging

For repository-level changes outside `homework-*` folders, update the root `CHANGELOG.md` before commit/PR closure.

## Homework Standards Enforcement (HW2+)

For work inside `homework-*` folders, enforce these non-negotiables:

1. Use homework-specific branch naming by default: `homework-x-submission`.
2. Never implement homework changes on `main`.
3. Every incremental step must update that homework's `CHANGELOG.md` before commit/PR closure.
4. Enforce quality gates by mode:
   - Local mode: enforce gates at commit time.
   - Web/agent mode: enforce gates in each Codex-generated step PR.
5. In web/agent mode, treat `homework-x-submission` as the canonical branch and do **not** create extra `-web` branches solely for Codex.
6. In web/agent mode, start work from the intended homework branch (for example `homework-x-submission`).
7. Codex step PRs should target that same homework branch; do not manually retarget bases unless required by tooling failure.
8. Keep final-delivery flow consistent with `HOMEWORK_STANDARDS.md`: homework branch integrates to `main` only at final submission.

`HOMEWORK_STANDARDS.md` is the detailed source of truth for structure, documentation minima, timing, quality gates, diagrams/evidence, and review checklist.

## Codex Web Fallback Gates

Codex Web may lack local quality levers such as Superpowers plugins, model selection, and reasoning-effort control. When those controls are unavailable, agents must follow the Web Mode Compensation checklist in `HOMEWORK_STANDARDS.md` and disclose the missing controls in PRs or AI-assistance notes.

## Local Model and Reasoning Guidance

When local tooling allows model or reasoning-effort choice:

- Use stronger/deeper reasoning for planning, architecture, debugging, cross-file implementation, security-sensitive changes, and final review.
- Use lighter handling only for narrow mechanical edits, formatting, or simple documentation cleanup.
- If model/reasoning controls are unavailable, follow the Codex Web fallback gate above.

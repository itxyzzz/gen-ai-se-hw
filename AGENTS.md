# Repository Instructions

## Git Author Email

All commits in this repository must use a private email address for author and committer metadata.

Allowed email domains:
- `@gmail.com`
- `@users.noreply.github.com`

Do not use company, contractor, school, client, or other organizational email domains in Git commit metadata.

## Homework Standards Enforcement (HW2+)

For work inside `homework-*` folders, enforce these non-negotiables:

1. Use homework-specific branch naming by default: `homework-x-submission`.
2. Never implement homework changes on `main`.
3. Every incremental step must update that homework's `CHANGELOG.md` before commit/PR closure.
4. Enforce quality gates by mode:
   - Local mode: enforce gates at commit time.
   - Web/agent mode: enforce gates in each Codex-generated step PR.
5. In web/agent mode, treat `homework-x-submission` as the canonical branch and do **not** create extra `-web` branches solely for Codex.
6. Codex Web UI may auto-create a temporary `codex/<id>` branch and default PR base `main`; the user must manually retarget that PR base to `homework-x-submission` before merge.
7. Final submission PR to `main` is created manually by the student from `homework-x-submission` after step work is complete.

`HOMEWORK_STANDARDS.md` is the detailed source of truth for structure, documentation minima, timing, quality gates, diagrams/evidence, and review checklist.

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
   - Web/agent mode: enforce gates in step PRs.
5. Follow the local vs web/agent workflow model defined in `HOMEWORK_STANDARDS.md`.
6. Final submission PR to `main` is created manually by the student.

`HOMEWORK_STANDARDS.md` is the detailed source of truth for structure, documentation minima, timing, quality gates, diagrams/evidence, and review checklist.

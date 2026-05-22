# Homework 3 Review And Verification Guide

Homework 3 is a documentation-only specification package. There is no application server, API, UI, database, test runner, or build command to run.

## How To Review

1. Start with [README.md](README.md) for the package summary, rationale, file map, and best-practice references.
2. Read [TASKS.md](TASKS.md) to compare the assignment requirements with the submitted package.
3. Read [specification.md](specification.md) as the primary graded artifact.
4. Read [agents.md](agents.md) and [.github/copilot-instructions.md](.github/copilot-instructions.md) for AI and editor guidance.
5. Review active supporting docs under [docs/](docs/), especially:
   - [docs/domain-rules.md](docs/domain-rules.md)
   - [docs/technical-conventions.md](docs/technical-conventions.md)
   - [docs/development-process.md](docs/development-process.md)
   - [docs/operator-manual.md](docs/operator-manual.md)

## Verification Checklist

- `specification.md` contains the required layered structure: high-level objective, mid-level objectives, non-functional and policy expectations, implementation notes, beginning and ending context, low-level tasks, edge cases, verification, and performance.
- `specification.md` keeps the chosen feature documentation-only and does not add application code, API implementation, UI implementation, database migrations, or real evidence files.
- Low-level tasks map back to the mid-level objectives `M1` through `M5` and include checkable acceptance criteria and verification notes.
- Edge cases, audit behavior, redaction, permission boundaries, stale-state handling, idempotency, and performance expectations appear in the spec itself.
- `agents.md` defines domain-sensitive AI behavior, verification expectations, security/compliance constraints, and edge-case handling.
- `.github/copilot-instructions.md` provides editor-specific AI rules and points back to the full agent contract.
- Supporting docs explain domain rationale, technical conventions, development process, and operator expectations without overriding `specification.md`.
- Screenshots for PR evidence are stored under [docs/screenshots/](docs/screenshots/).

## Optional Local Checks

Run these from the repository root:

```powershell
git status --short
git diff --check
$markers = 'TO' + 'DO|TB' + 'D|FIX' + 'ME'
rg -n $markers homework-3
```

`rg` may report intentionally quoted historical text in archived process artifacts. Treat active package files as the source of truth.

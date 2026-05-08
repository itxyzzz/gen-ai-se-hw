# Homework 3 Outer Harness Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Create the general Homework 3 specification package structure before selecting the finance feature.

**Architecture:** This is a documentation-only harness. `specification.md` remains the future product source of truth, while `agents.md`, editor rules, and focused documents under `docs/` define reusable agent behavior, technical conventions, workflow gates, and operator guidance.

**Tech Stack:** Markdown documentation, Mermaid diagram in README, git-based homework branch workflow.

---

## File Structure

- Create `homework-3/README.md` as the reviewer entry point and package rationale.
- Create `homework-3/specification.md` as a deferred scaffold with Homework 3 required layered headings.
- Create `homework-3/agents.md` as the AI and human agent behavior contract.
- Create `homework-3/.github/copilot-instructions.md` as the required editor/AI rules file.
- Create `homework-3/docs/domain-rules.md` as the intentionally deferred researched domain-rules scaffold.
- Create `homework-3/docs/technical-conventions.md` for feature-neutral finance engineering conventions.
- Create `homework-3/docs/development-process.md` for addon-independent workflow gates.
- Create `homework-3/docs/operator-manual.md` for feature-neutral internal operator guidance.
- Create or update `homework-3/CHANGELOG.md` with this harness increment.

## Task 1: Preflight And Current State

**Files:**
- Read: `AGENTS.md`
- Read: `HOMEWORK_STANDARDS.md`
- Read: `homework-3/TASKS.md`
- Inspect: `homework-3/`

- [ ] **Step 1: Confirm branch**

Run:

```powershell
git branch --show-current
```

Expected: `homework-3-submission`. If the branch is `main`, stop before editing.

- [ ] **Step 2: Confirm working tree**

Run:

```powershell
git status --short
```

Expected: either clean output or only known user-approved changes.

- [ ] **Step 3: Read required context**

Run:

```powershell
Get-Content -Path AGENTS.md
Get-Content -Path HOMEWORK_STANDARDS.md
Get-Content -Path homework-3\TASKS.md
Get-ChildItem -Path homework-3 -Recurse -Force
```

Expected: repository and Homework 3 instructions are visible before editing.

## Task 2: Create Harness Directories

**Files:**
- Create: `homework-3/docs/`
- Create: `homework-3/.github/`

- [ ] **Step 1: Create directories**

Run:

```powershell
New-Item -ItemType Directory -Force -Path homework-3\docs, homework-3\.github
```

Expected: both directories exist.

## Task 3: Add Core Harness Documents

**Files:**
- Create: `homework-3/README.md`
- Create: `homework-3/specification.md`
- Create: `homework-3/agents.md`
- Create: `homework-3/.github/copilot-instructions.md`
- Create: `homework-3/docs/domain-rules.md`
- Create: `homework-3/docs/technical-conventions.md`
- Create: `homework-3/docs/development-process.md`
- Create: `homework-3/docs/operator-manual.md`

- [ ] **Step 1: Write `README.md`**

Include student/task summary, package map, rationale, industry best practices, current limits, and links to every harness document.

- [ ] **Step 2: Write `specification.md`**

Include the Homework 3 required layered headings:

```markdown
## High-Level Objective
## Scope Boundary
## Mid-Level Objectives
## Non-Functional And Policy Expectations
## Implementation Notes
## Context
## Edge Cases And Failure Modes
## Verification Plan
## Expected Performance
## Low-Level Tasks
```

Each section must clearly state that feature-specific content is deferred until feature selection.

- [ ] **Step 3: Write `agents.md`**

Define context order, required workflow, specification discipline, finance-sensitive defaults, verification expectations, and editor/AI tooling boundaries.

- [ ] **Step 4: Write `.github/copilot-instructions.md`**

Point Copilot-style agents to `homework-3/TASKS.md`, `agents.md`, `specification.md`, and relevant `docs/` files. Require no code generation and no unsupported regulatory claims.

- [ ] **Step 5: Write `docs/domain-rules.md`**

Mark this document as intentionally deferred until finance feature selection and research. List the research inputs required before it becomes authoritative.

- [ ] **Step 6: Write `docs/technical-conventions.md`**

Document feature-neutral conventions for identifiers, timestamps, money, idempotency, errors, pagination, audit metadata, logging/redaction, and naming.

- [ ] **Step 7: Write `docs/development-process.md`**

Document the addon-independent process, SPEC markdown usage, documentation-first gate, verification-first gate, TDD-style specification practice, change review checklist, and optional tooling policy.

- [ ] **Step 8: Write `docs/operator-manual.md`**

Document feature-neutral operator principles, access boundaries, review queues, escalation, audit evidence, incident notes, and manual checks for the future feature spec.

## Task 4: Add Changelog Entry

**Files:**
- Create or update: `homework-3/CHANGELOG.md`

- [ ] **Step 1: Add Step 1 entry**

Create a `Homework 3 - Step 1` entry with `Added`, `Changed`, `Fixed`, and `Tests` sections.

- [ ] **Step 2: Record verification**

Under `Tests`, record the actual checks run: required files, markdown links, unfinished marker text, required spec headings, and optional-addon wording.

## Task 5: Verify Harness

**Files:**
- Check: all new Markdown files under `homework-3/`

- [ ] **Step 1: Verify required files exist**

Run:

```powershell
Get-Item homework-3\README.md, homework-3\specification.md, homework-3\agents.md, homework-3\.github\copilot-instructions.md, homework-3\docs\domain-rules.md, homework-3\docs\technical-conventions.md, homework-3\docs\development-process.md, homework-3\docs\operator-manual.md, homework-3\CHANGELOG.md
```

Expected: all listed files are returned.

- [ ] **Step 2: Verify non-deferred files have no unfinished markers**

Run:

```powershell
$markerPattern = ('TO' + 'DO') + '|' + ('TB' + 'D') + '|place' + 'holder'
Select-String -Path homework-3\README.md, homework-3\agents.md, homework-3\.github\copilot-instructions.md, homework-3\docs\technical-conventions.md, homework-3\docs\development-process.md, homework-3\docs\operator-manual.md, homework-3\CHANGELOG.md -Pattern $markerPattern
```

Expected: no output.

- [ ] **Step 3: Verify markdown links resolve**

Run:

```powershell
$files = Get-ChildItem -Path homework-3 -Recurse -File -Filter *.md
$missing = @()
foreach ($file in $files) {
  foreach ($line in Get-Content -Path $file.FullName) {
    foreach ($match in [regex]::Matches($line, '\[[^\]]+\]\(([^)]+)\)')) {
      $target = $match.Groups[1].Value
      if ($target -match '^(https?://|mailto:)') { continue }
      $path = $target.Split('#')[0]
      if ([string]::IsNullOrWhiteSpace($path)) { continue }
      $resolved = Join-Path -Path $file.DirectoryName -ChildPath $path
      if (-not (Test-Path -Path $resolved)) { $missing += "$($file.FullName): $target" }
    }
  }
}
if ($missing.Count -gt 0) { $missing; exit 1 } else { 'All markdown links resolve.' }
```

Expected: `All markdown links resolve.`

- [ ] **Step 4: Verify required scaffold headings**

Run:

```powershell
Select-String -Path homework-3\specification.md -Pattern '^## High-Level Objective|^## Mid-Level Objectives|^## Non-Functional And Policy Expectations|^## Implementation Notes|^## Context|^## Edge Cases And Failure Modes|^## Verification Plan|^## Expected Performance|^## Low-Level Tasks'
```

Expected: all required headings are returned.

- [ ] **Step 5: Verify optional tooling wording**

Run:

```powershell
Select-String -Path homework-3\docs\development-process.md -Pattern 'without relying on Superpowers, GitHub Spec Kit|do not replace these gates|optional tooling'
```

Expected: output confirms optional tools are support only, not required dependencies.

- [ ] **Step 6: Review git status**

Run:

```powershell
git status --short
```

Expected: only the intended Homework 3 documentation files are changed or untracked.

## Self-Review Notes

- The plan intentionally allows deferred content only in `specification.md` and `docs/domain-rules.md`.
- The plan does not choose the finance feature.
- The plan does not create application code, API contracts, UI designs, databases, or implementation tests.
- The plan keeps the written process executable without Superpowers, GitHub Spec Kit, or other addons.

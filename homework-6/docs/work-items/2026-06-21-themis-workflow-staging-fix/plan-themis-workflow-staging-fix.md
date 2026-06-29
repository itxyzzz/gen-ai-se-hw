# Themis Workflow Staging Fix Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Update Themis (Test Generator) workflow documentation so Java test-generation runs stage outputs deterministically, block malformed output trees before validation, and verify Maven coverage-helper threshold behavior before claiming hook evidence.

**Architecture:** This is a control-surface documentation repair. The shared Themis package remains the source of truth; Codex and Claude entrypoints stay thin wrappers. The change adds workflow gates, quality-bar checks, and inventory rules without editing generated product code or preserved run evidence.

**Tech Stack:** Markdown control docs, PowerShell static validation, Homework 6 Themis run artifact conventions.

---

## Files

- Modify: `agent-control/generate-tests/workflow.md`
- Modify: `agent-control/generate-tests/quality-bar.md`
- Modify: `agent-control/generate-tests/run-registry.md`
- Modify: `CHANGELOG.md` during freeze and implementation closure
- Do not modify: preserved `docs/agent-runs/...` run folders
- Do not modify: canonical product source, root tests, docs/screenshots, MCP files, `docs/agent-runs/final-selection.md`, `docs/agent-runs/selection-sets.json`, or root `shared/`

## Task 1: Add Java Staging Gates To Workflow

**Files:**
- Modify: `agent-control/generate-tests/workflow.md`

- [ ] **Step 1: Add a Java output staging subsection under `Run-Local File Manipulation`.**

Insert guidance with these exact requirements:

```markdown
### Java Output Staging Guardrails

For `stack=java`, Themis must stage reusable baseline tests and new candidate tests only under:

```text
agent-3-tests/outputs/src/test/java/
```

Do not stage Java tests under `agent-3-tests/outputs/src/java/`, `agent-3-tests/outputs/test/java/`, or any duplicate nested test tree. Before rebuilding `workspace/project-under-test/`, inspect `agent-3-tests/outputs/` and block or restart the run if malformed Java test roots exist.

If malformed staging is detected before validation, Themis may correct it only when the correction is local, unambiguous, and does not require elevated cleanup. If cleanup becomes approval-dependent, destructive, or ambiguous, stop the run, write blocked metadata/checklist/handoff, and let Hera or the operator start a replacement run. Do not spend a child-agent run on cleanup loops.

Rebuild `workspace/project-under-test/` only after `outputs/` passes this staging check.
```

- [ ] **Step 2: Add Java Maven coverage-helper compatibility guidance near validation.**

Add wording that says:

```markdown
For Java packages, the coverage helper's `--fail-under` value must affect the Maven JaCoCo check. If the selected `pom.xml` hardcodes the JaCoCo minimum and ignores `-Dcoverage.minimum`, Themis may add a candidate `pom.xml` test/build configuration overlay under `agent-3-tests/outputs/` that keeps the default at `0.80` and wires JaCoCo `<minimum>${coverage.minimum}</minimum>`. Inventory this overlay as Maven test/build configuration. Do not modify runtime product source under `src/main/java/...` without explicit operator repair authorization.
```

- [ ] **Step 3: Run focused workflow scans.**

Run:

```powershell
Select-String -Path agent-control\generate-tests\workflow.md -Pattern 'Java Output Staging Guardrails','outputs/src/test/java','outputs/src/java','coverage.minimum','project-under-test'
```

Expected:

- All patterns are found.

## Task 2: Add Quality-Bar Checks

**Files:**
- Modify: `agent-control/generate-tests/quality-bar.md`

- [ ] **Step 1: Add required gate bullets.**

Add bullets to `## Required Gates`:

```markdown
- Java candidate output staging is clean before validation: Java tests live under `agent-3-tests/outputs/src/test/java/...`, and malformed roots such as `agent-3-tests/outputs/src/java/` are absent or the run is blocked as non-selectable.
- Java coverage-helper evidence proves the requested threshold is wired to JaCoCo. When the selected Maven build needs a test/build configuration overlay such as `pom.xml` with `${coverage.minimum}`, that overlay is staged under `outputs/`, inventoried, and limited to build/test configuration.
```

- [ ] **Step 2: Add scope rejection bullet.**

Add to `## Scope Rejection`:

```markdown
- Continue validation from a malformed Java output tree or silently ignore duplicate Java test roots in a selectable inventory.
```

- [ ] **Step 3: Run focused quality-bar scans.**

Run:

```powershell
Select-String -Path agent-control\generate-tests\quality-bar.md -Pattern 'outputs/src/test/java','outputs/src/java','coverage.minimum','malformed Java output tree'
```

Expected:

- All patterns are found.

## Task 3: Add Run-Registry Inventory Rules

**Files:**
- Modify: `agent-control/generate-tests/run-registry.md`

- [ ] **Step 1: Add Java inventory guidance under `Output Inventory`.**

Add wording:

```markdown
For `stack=java`, a selectable inventory must not include malformed duplicate trees such as `agent-3-tests/outputs/src/java/`. If such a path exists, the run inventory must mark the run blocked and explicitly exclude the path rather than presenting the package as selectable. A clean Java Themis inventory lists only Maven test/build configuration and files under `src/test/java/...` unless a stack-specific test resource path is intentionally required.
```

- [ ] **Step 2: Add workspace/evidence guidance under `Workspace And Evidence`.**

Add wording:

```markdown
Themis must rebuild `workspace/project-under-test/` only after candidate outputs pass the stack-specific staging check. Evidence for a blocked staging failure belongs in `validation-checklist.md` and `handoff.md`; do not create passing command, hook, or coverage evidence from a malformed output tree.
```

- [ ] **Step 3: Run focused run-registry scans.**

Run:

```powershell
Select-String -Path agent-control\generate-tests\run-registry.md -Pattern 'outputs/src/java','blocked','project-under-test','malformed output tree'
```

Expected:

- All patterns are found.

## Task 4: Changelog And Static Validation

**Files:**
- Modify: `CHANGELOG.md`

- [ ] **Step 1: Add newest-first changelog entry.**

Add a new top entry:

```markdown
## Homework 6 - Step 52: Themis Workflow Staging Repair

### Added

- Added Themis Java output-staging guardrails so candidate tests must live under `agent-3-tests/outputs/src/test/java/` and malformed duplicate roots block selection.
- Added Java Maven coverage-helper guidance for candidate `pom.xml` test/build overlays that wire `coverage.minimum` into JaCoCo checks.

### Changed

- Updated Themis run-registry and validation guidance so `workspace/project-under-test/` is rebuilt only after candidate outputs pass staging checks.

### Fixed

- Prevented future Themis child agents from spending approval-dependent cleanup loops on malformed run-local output paths such as `agent-3-tests/outputs/src/java/`.

### Tests

- Ran focused static scans for Themis staging, malformed-output, coverage-threshold, inventory, and unresolved draft markers.
```

- [ ] **Step 2: Run draft-marker and static validation scans.**

Run:

```powershell
Select-String -Path agent-control\generate-tests\workflow.md,agent-control\generate-tests\quality-bar.md,agent-control\generate-tests\run-registry.md -Pattern 'Java Output Staging Guardrails','outputs/src/test/java','outputs/src/java','coverage.minimum'
$draftTerms = @('T'+'BD','TO'+'DO','PLACE'+'HOLDER')
Select-String -Path docs\work-items\2026-06-21-themis-workflow-staging-fix\*.md,agent-control\generate-tests\workflow.md,agent-control\generate-tests\quality-bar.md,agent-control\generate-tests\run-registry.md -Pattern $draftTerms
```

Expected:

- First scan finds the new guardrails.
- Second scan returns no matches.

- [ ] **Step 3: Review diff scope.**

Run:

```powershell
git diff -- agent-control/generate-tests/workflow.md agent-control/generate-tests/quality-bar.md agent-control/generate-tests/run-registry.md CHANGELOG.md
```

Expected:

- Diff is limited to Themis control-surface workflow/quality/registry guidance and changelog.
- No preserved run folders, canonical product files, root tests, docs/screenshots, MCP files, or selection records are modified.

## Acceptance Criteria

- `workflow.md` contains explicit Java output staging guardrails.
- `quality-bar.md` treats malformed Java output trees as a blocker.
- `run-registry.md` prevents malformed duplicate Java trees from appearing in selectable inventories.
- Java coverage-helper guidance requires real `--fail-under` threshold wiring.
- Changelog is newest-first and names the Themis workflow staging repair.
- Static validation commands produce the expected signals.

## Implementation Handoff

After this planning package is approved and frozen, the implementing agent should edit only the three Themis control docs and `CHANGELOG.md`, then run the validation commands in this plan. If additional behavior is discovered, record it as variance rather than broadening into Hera or coverage-helper implementation.

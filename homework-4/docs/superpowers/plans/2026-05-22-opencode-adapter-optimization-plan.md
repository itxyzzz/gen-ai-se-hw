# Open Code Adapter Optimization Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Update Homework 4 Open Code adapter and related docs so pipeline launch is short and consistent, model selection supports Codex/Claude/free-open options, and fallback/skill semantics are explicit and reproducible.

**Architecture:** Keep the universal harness and portable agent specs unchanged. Apply focused documentation contract updates in `adapters/open-code.md`, then align public runbook docs (`HOWTORUN.md`, `API_REFERENCE.md`) and changelog metadata. Preserve all run artifact and stage-order guarantees defined by the harness.

**Tech Stack:** Markdown documentation, text-harness adapter contract, Node test command references (no runtime code changes).

---

### Task 1: Finalize Design Spec Artifact

**Files:**
- Create: `homework-4/docs/superpowers/specs/2026-05-22-opencode-adapter-optimization-design.md`
- Review: `homework-4/TASKS.md`
- Review: `homework-4/skills/pipeline-harness-wrapper.md`
- Review: `homework-4/adapters/open-code.md`

- [ ] **Step 1: Write spec markdown file**
  - Include sections: Goal, Context/Problem, Design Decisions, File Changes, Acceptance Criteria, Risks.
  - Ensure explicit command-phrase semantics for OpenCode.

- [ ] **Step 2: Validate spec completeness**
  - Confirm it covers launch phrase simplification, model matrix expansion, fallback metadata, and skill-semantics split.
  - Expected: no TODO/TBD placeholders.

- [ ] **Step 3: Commit spec file**
  ```bash
  git add homework-4/docs/superpowers/specs/2026-05-22-opencode-adapter-optimization-design.md
  git commit -m "docs(hw4): add Open Code adapter optimization design spec"
  ```

### Task 2: Retarget Open Code Adapter for Mixed Model Availability

**Files:**
- Modify: `homework-4/adapters/open-code.md`

- [ ] **Step 1: Replace launch phrase with canonical short trigger**
  - Set launch phrase to:
    ```text
    Run HW4 pipeline
    ```

- [ ] **Step 2: Replace model table with multi-family ordered candidates**
  - Include policy rows for:
    - `research-high`
    - `verification-high`
    - `planning-high`
    - `implementation-medium`
    - `security-high`
    - `test-medium`
  - Each row must include Codex/OpenAI, Claude, and free/open alternatives in priority order.

- [ ] **Step 3: Add deterministic fallback rules**
  - Define first-available resolution, tier-preserving fallback, and required fallback reason capture.

- [ ] **Step 4: Add explicit skill handling split**
  - Distinguish:
    - Superpowers skills loaded via `skill` tool.
    - Homework local skill docs read from `homework-4/skills/*.md`.

- [ ] **Step 5: Add OpenCode tooling guidance + reflection loop policy**
  - Document preferred tools and max 3 retry loop for fix/test stages with blocker logging.

- [ ] **Step 6: Commit adapter update**
  ```bash
  git add homework-4/adapters/open-code.md
  git commit -m "docs(hw4): optimize Open Code adapter for mixed model environments"
  ```

### Task 3: Align Public Prompt Contract Docs

**Files:**
- Modify: `homework-4/HOWTORUN.md`
- Modify: `homework-4/API_REFERENCE.md`

- [ ] **Step 1: Normalize Open Code launch wording**
  - Use `Run HW4 pipeline` as the launch phrase in both docs.
  - Keep adapter selection behavior described as automatic by active tool context.

- [ ] **Step 2: Preserve clarity for multi-adapter usage**
  - Ensure docs still explain that adapters are separate mappings, but launch phrase can remain canonical.

- [ ] **Step 3: Commit docs alignment**
  ```bash
  git add homework-4/HOWTORUN.md homework-4/API_REFERENCE.md
  git commit -m "docs(hw4): normalize Open Code launch phrase in runbook and API contract"
  ```

### Task 4: Update Homework 4 Changelog

**Files:**
- Modify: `homework-4/CHANGELOG.md`

- [ ] **Step 1: Add newest-first changelog step**
  - Add a new top entry directly under title.
  - Include Added/Changed/Fixed/Tests sections.
  - Mention model matrix expansion, fallback metadata rules, skill-semantics clarification, and short launch phrase normalization.

- [ ] **Step 2: Commit changelog update**
  ```bash
  git add homework-4/CHANGELOG.md
  git commit -m "docs(hw4): record Open Code adapter optimization step"
  ```

### Task 5: Verification and Consistency Checks

**Files:**
- Review: `homework-4/adapters/open-code.md`
- Review: `homework-4/HOWTORUN.md`
- Review: `homework-4/API_REFERENCE.md`
- Review: `homework-4/CHANGELOG.md`

- [ ] **Step 1: Validate prompt consistency**
  Run:
  ```bash
  rg "Run HW4 pipeline|Open Code adapter|single-command|launch phrase" homework-4/*.md homework-4/adapters/*.md homework-4/skills/*.md
  ```
  Expected: Open Code launch contract is consistent and short phrase is present where intended.

- [ ] **Step 2: Validate adapter structure**
  - Confirm adapter includes:
    - model matrix
    - fallback rules
    - skill split
    - tooling guidance
    - validation checklist

- [ ] **Step 3: Optional regression sanity (docs-only change)**
  Run:
  ```bash
  node --test --test-isolation=none homework-4/app/current/tests/*.test.js
  node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js
  ```
  Expected:
  - current app tests pass
  - baseline tests fail as seeded defects evidence

- [ ] **Step 4: Final diff review**
  Run:
  ```bash
  git status
  git diff -- homework-4/adapters/open-code.md homework-4/HOWTORUN.md homework-4/API_REFERENCE.md homework-4/CHANGELOG.md homework-4/docs/superpowers/specs/2026-05-22-opencode-adapter-optimization-design.md homework-4/docs/superpowers/plans/2026-05-22-opencode-adapter-optimization-plan.md
  ```
  Expected: only intended docs/adapter/spec/plan files changed.

- [ ] **Step 5: Final commit (if single-commit workflow preferred)**
  ```bash
  git add homework-4/adapters/open-code.md homework-4/HOWTORUN.md homework-4/API_REFERENCE.md homework-4/CHANGELOG.md homework-4/docs/superpowers/specs/2026-05-22-opencode-adapter-optimization-design.md homework-4/docs/superpowers/plans/2026-05-22-opencode-adapter-optimization-plan.md
  git commit -m "docs(hw4): optimize Open Code adapter contract and launch semantics"
  ```

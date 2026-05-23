# Bug 001 Normalized Benchmark Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a snapshot-safe normalized benchmark layer for all Homework 4 `bug-001` runs.

**Architecture:** Preserve original run folders and historical plan/spec files as immutable snapshots. Store normalized ids, repaired metadata, copied reports, synthesized patch evidence, and comparative scoring under `homework-4/benchmark/bug-001/`.

**Tech Stack:** Markdown docs, JSON benchmark metadata, PowerShell/file-copy workflow, git diff validation.

---

### Task 1: Create Durable Design Documents

**Files:**
- Create: `homework-4/docs/superpowers/specs/2026-05-23-bug-001-normalized-benchmark-design.md`
- Create: `homework-4/docs/superpowers/plans/2026-05-23-bug-001-normalized-benchmark-plan.md`

- [ ] **Step 1: Record snapshot boundaries**

Document that `homework-4/runs/bug-001/**` and existing `docs/superpowers/{specs,plans}/**` files are immutable snapshots.

- [ ] **Step 2: Record normalized mapping**

Use the commit-sequence mapping from the paired design spec and keep the six normalized ids unchanged.

- [ ] **Step 3: Record model and sub-agent strategy**

State that GPT-5.5 owns orchestration and integration while read-only sub-agents analyze run pairs in parallel.

### Task 2: Build Benchmark-Owned Normalized Evidence

**Files:**
- Create: `homework-4/benchmark/bug-001/runs/<normalized-run-id>/source-map.json`
- Create: `homework-4/benchmark/bug-001/runs/<normalized-run-id>/run-metadata.normalized.json`
- Create: `homework-4/benchmark/bug-001/runs/<normalized-run-id>/artifact-index.md`
- Copy: source reports from `homework-4/runs/bug-001/<source-run>/`

- [ ] **Step 1: Create per-run benchmark directories**

Create one directory for each normalized id under `homework-4/benchmark/bug-001/runs/`.

- [ ] **Step 2: Copy review artifacts**

Copy source reports and patch evidence into the benchmark directory. Do not modify source snapshot files.

- [ ] **Step 3: Add normalized metadata**

For each run, write `run-metadata.normalized.json` with normalized id, source folder, score breakdown, metrics, notes, and uncertainty.

- [ ] **Step 4: Repair Nemotron patch evidence in benchmark only**

Create `run-005-open-code-nemotron-3-super-free-1779485697/patch.diff` from a benchmark-owned no-index diff because the source snapshot only stores split patch files.

### Task 3: Update Active Instructions And Docs

**Files:**
- Modify: `homework-4/AGENTS.md`
- Modify: `homework-4/skills/pipeline-harness-wrapper.md`
- Modify: `homework-4/adapters/*.md` as needed
- Modify: active HW4 overview and benchmark docs

- [ ] **Step 1: Install strict future naming rule**

Require `run-<NNN>-<tool>-<pattern>` and define the next-number algorithm.

- [ ] **Step 2: Redirect current benchmark references**

Point active docs to `homework-4/benchmark/bug-001/` for normalized comparison, while identifying `homework-4/runs/bug-001/` as immutable source snapshots.

- [ ] **Step 3: Add newest changelog entry**

Add a newest-first `CHANGELOG.md` entry describing the normalized benchmark layer and snapshot policy.

### Task 4: Fill Comparative Benchmark Outputs

**Files:**
- Modify: `homework-4/benchmark/bug-001-results.json`
- Modify: `homework-4/benchmark/bug-001-comparison.md`

- [ ] **Step 1: Merge read-only sub-agent analyses**

Use sub-agent findings for artifact completeness, metrics, scores, qualitative notes, and uncertainty.

- [ ] **Step 2: Write aggregate JSON**

Include exactly six normalized run entries with source folder references and comparable metrics.

- [ ] **Step 3: Write comparison Markdown**

Include score table, normalized mapping, artifact caveats, and comparative findings.

### Task 5: Verify Snapshot Safety

**Files:**
- Inspect only: git diff and benchmark outputs

- [ ] **Step 1: Verify source snapshots untouched**

Run `git diff -- homework-4/runs/bug-001` and expect no diff.

- [ ] **Step 2: Verify historical specs/plans untouched**

Run `git diff -- homework-4/docs/superpowers/specs homework-4/docs/superpowers/plans` and expect only the two new files.

- [ ] **Step 3: Verify normalized benchmark completeness**

Confirm six normalized run directories and six JSON entries.

- [ ] **Step 4: Verify instruction consistency**

Search active guidance for old future-run naming rules and ensure only `run-<NNN>-<tool>-<pattern>` remains.

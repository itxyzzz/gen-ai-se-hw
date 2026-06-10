# Bug 001 Quote Calculator Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Fix the quote math and catalog-name validation in the current run workspace without touching the seeded baseline.

**Architecture:** Keep the existing single-module calculator and repository layout. Make the smallest source-only edits in the run app: correct the arithmetic in `quoteCalculator.js`, add catalog-name validation plus resolved-path containment in `catalogRepository.js`, then verify the existing test suite and write the run summary.

**Tech Stack:** Node.js ES modules, built-in `node:test`, `assert/strict`, `fs/promises`, `path`.

---

### Task 1: Fix line totals

**Files:**
- Modify: `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/app/src/quoteCalculator.js:1-5`
- Validate: `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/app/tests/quoteCalculator.test.js:5-12`

**Before:** `calculateLineTotal()` returns `item.quantity + item.unitPrice`, so `3 x 25` incorrectly produces `28`.

**After:** `calculateLineTotal()` returns `item.quantity * item.unitPrice`, so the same input produces `75` and flows into the subtotal.

- [ ] **Step 1: Confirm the current failure**
Run: `node --test --test-isolation=none tests/quoteCalculator.test.js`
Expected: the line-total assertions fail against the buggy addition logic.

- [ ] **Step 2: Patch the calculator**
Replace:
```js
return item.quantity + item.unitPrice;
```
with:
```js
return item.quantity * item.unitPrice;
```

- [ ] **Step 3: Re-run the targeted test**
Run: `node --test --test-isolation=none tests/quoteCalculator.test.js`
Expected: line-total and subtotal assertions pass.

### Task 2: Fix SAVE10 percentage discount

**Files:**
- Modify: `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/app/src/quoteCalculator.js:7-17`
- Validate: `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/app/tests/quoteCalculator.test.js:14-17`

**Before:** `applyDiscount()` treats `SAVE10` as a flat `$10` subtraction, so `75` becomes `65`.

**After:** `applyDiscount()` applies a 10% discount and rounds with the existing currency helper, so `75` becomes `67.5` and `100` still becomes `90`.

- [ ] **Step 1: Confirm the current failure**
Run: `node --test --test-isolation=none tests/quoteCalculator.test.js`
Expected: the `SAVE10` assertion for `75` fails with the flat-subtraction behavior.

- [ ] **Step 2: Patch the discount branch**
Replace:
```js
return subtotal - 10;
```
with:
```js
return roundCurrency(subtotal * 0.9);
```

- [ ] **Step 3: Re-run the targeted test**
Run: `node --test --test-isolation=none tests/quoteCalculator.test.js`
Expected: both `SAVE10` assertions pass.

### Task 3: Reject unsafe catalog names

**Files:**
- Modify: `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/app/src/catalogRepository.js:5-12`
- Validate: `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/app/tests/security.test.js:5-9`

**Before:** `loadCatalog()` joins the provided name directly, so `../catalogs/default` can escape the catalogs directory.

**After:** `loadCatalog()` only accepts catalog names that match `^[a-zA-Z0-9_-]+$`, resolves the final file path under the known catalog directory, and rejects anything that escapes that directory with `Invalid catalog name`.

- [ ] **Step 1: Confirm the current failure**
Run: `node --test --test-isolation=none tests/security.test.js`
Expected: the traversal test fails or reports the missing validation.

- [ ] **Step 2: Add name validation and containment checks**
Add an allowlist check before file access, then resolve the candidate file and reject it unless it stays inside the catalog directory.

```js
if (!/^[a-zA-Z0-9_-]+$/.test(catalogName)) {
  throw new Error("Invalid catalog name");
}

const catalogPath = path.resolve(catalogDirectory, `${catalogName}.json`);
const catalogRoot = `${catalogDirectory}${path.sep}`;
if (!catalogPath.startsWith(catalogRoot)) {
  throw new Error("Invalid catalog name");
}
```

Use the existing `readFile` and `JSON.parse` flow only after the path check passes.

- [ ] **Step 3: Re-run the security test**
Run: `node --test --test-isolation=none tests/security.test.js`
Expected: the traversal case rejects with `/Invalid catalog name/`.

### Task 4: Write the run summary

**Files:**
- Create: `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/fix-summary.md`

**Before:** No run-specific completion summary exists for this run.

**After:** The summary records the three fixes, the final test status, and the manual verification evidence required by the scenario.

- [ ] **Step 1: Draft the summary**
Include `Changes Made`, `Overall Status`, `Manual Verification`, and `References`.

- [ ] **Step 2: Verify the full run**
Run: `node --test --test-isolation=none tests/*.test.js`
Expected: all tests pass.

- [ ] **Step 3: Save the summary**
Record the exact files changed and the commands run in the run workspace.

**Stop conditions**
- Do not touch `app/baseline`.
- Stop when `node --test --test-isolation=none tests/*.test.js` passes in the run app.
- Stop when the security test rejects path traversal with `Invalid catalog name`.
- Stop when `fix-summary.md` is written with the final verification results.

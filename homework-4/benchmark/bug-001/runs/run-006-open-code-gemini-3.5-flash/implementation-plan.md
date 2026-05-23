# Bug 001 Quote Calculator Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Fix the arithmetic bug in line total calculations, the percentage discount bug in SAVE10 discount code, and secure catalog loading against path traversal vulnerabilities.

**Architecture:** We will replace the addition operator with multiplication in line total calculation, apply a proper 10% discount multiplier wrapped in a currency rounding utility for the SAVE10 code, and implement validation regex and directory containment check in catalog loading to prevent path traversal (CWE-22).

**Tech Stack:** Node.js (native `node --test` runner)

---

## Inputs

- **Verified Research Source**: `homework-4/runs/bug-001/open-code-gemini-3.5-flash-run-005/research/verified-research.md`
- **Run Workspace App Directory**: `homework-4/runs/bug-001/open-code-gemini-3.5-flash-run-005/app`
- **Baseline App**: `homework-4/app/baseline`

---

## Task Decomposition

### Task 1: Correct Line Total Calculation

**Files:**
- Modify: `homework-4/runs/bug-001/open-code-gemini-3.5-flash-run-005/app/src/quoteCalculator.js`
- Test: `homework-4/runs/bug-001/open-code-gemini-3.5-flash-run-005/app/tests/quoteCalculator.test.js`

- [ ] **Step 1: Inspect the failing test or verify current state**
  Verify that the test currently fails if run without the fix (though the buggy code is active).
  Run (from run workspace app directory): `node --test --test-isolation=none tests/quoteCalculator.test.js`
  Expected: FAIL (or verify the error relates to incorrect line total/discount math).

- [ ] **Step 2: Modify `calculateLineTotal` in `src/quoteCalculator.js`**
  Open `homework-4/runs/bug-001/open-code-gemini-3.5-flash-run-005/app/src/quoteCalculator.js`.
  Locate `calculateLineTotal` at lines 1-5:
  ```javascript
  export function calculateLineTotal(item) {
    assertPositiveQuantity(item.quantity);
    assertNonNegativePrice(item.unitPrice);
    return item.quantity + item.unitPrice;
  }
  ```
  Replace it with the correct multiplication:
  ```javascript
  export function calculateLineTotal(item) {
    assertPositiveQuantity(item.quantity);
    assertNonNegativePrice(item.unitPrice);
    return item.quantity * item.unitPrice;
  }
  ```

- [ ] **Step 3: Run the test to verify line total calculation passes**
  Run (from run workspace app directory): `node --test --test-isolation=none tests/quoteCalculator.test.js`
  Expected: Test "calculates line totals by multiplying quantity and unit price" passes.

- [ ] **Step 4: Commit the change**
  If git tracking is required by the fixer, commit the modification.
  ```bash
  git add app/src/quoteCalculator.js
  git commit -m "fix(quoteCalculator): correct line total calculation from addition to multiplication"
  ```

---

### Task 2: Correct SAVE10 Discount Calculation

**Files:**
- Modify: `homework-4/runs/bug-001/open-code-gemini-3.5-flash-run-005/app/src/quoteCalculator.js`
- Test: `homework-4/runs/bug-001/open-code-gemini-3.5-flash-run-005/app/tests/quoteCalculator.test.js`

- [ ] **Step 1: Inspect `applyDiscount` in `src/quoteCalculator.js`**
  Open `homework-4/runs/bug-001/open-code-gemini-3.5-flash-run-005/app/src/quoteCalculator.js`.
  Locate `applyDiscount` at lines 7-17:
  ```javascript
  export function applyDiscount(subtotal, discountCode) {
    if (!discountCode) {
      return subtotal;
    }

    if (discountCode === "SAVE10") {
      return subtotal - 10;
    }

    throw new Error(`Unknown discount code: ${discountCode}`);
  }
  ```

- [ ] **Step 2: Modify `applyDiscount` to apply a 10% discount**
  Replace lines 12-14 with a 10% calculation wrapped in `roundCurrency`:
  ```javascript
  export function applyDiscount(subtotal, discountCode) {
    if (!discountCode) {
      return subtotal;
    }

    if (discountCode === "SAVE10") {
      return roundCurrency(subtotal * 0.9);
    }

    throw new Error(`Unknown discount code: ${discountCode}`);
  }
  ```

- [ ] **Step 3: Run tests to verify discount calculation passes**
  Run (from run workspace app directory): `node --test --test-isolation=none tests/quoteCalculator.test.js`
  Expected: Both "calculates line totals by multiplying quantity and unit price" and "applies SAVE10 as ten percent discount" tests pass.

- [ ] **Step 4: Commit the change**
  ```bash
  git add app/src/quoteCalculator.js
  git commit -m "fix(quoteCalculator): correct SAVE10 discount calculation to 10% off"
  ```

---

### Task 3: Prevent Path Traversal in Catalog Loading

**Files:**
- Modify: `homework-4/runs/bug-001/open-code-gemini-3.5-flash-run-005/app/src/catalogRepository.js`
- Test: `homework-4/runs/bug-001/open-code-gemini-3.5-flash-run-005/app/tests/security.test.js`

- [ ] **Step 1: Inspect `loadCatalog` in `src/catalogRepository.js`**
  Open `homework-4/runs/bug-001/open-code-gemini-3.5-flash-run-005/app/src/catalogRepository.js`.
  Locate `loadCatalog` at lines 8-12:
  ```javascript
  export async function loadCatalog(catalogName = "default") {
    const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
    const rawCatalog = await readFile(catalogPath, "utf8");
    return JSON.parse(rawCatalog);
  }
  ```

- [ ] **Step 2: Modify `loadCatalog` to validate names and restrict directory escape**
  Implement catalog name alphanumeric check and a path containment check.
  ```javascript
  export async function loadCatalog(catalogName = "default") {
    // Validate catalog name to prevent path traversal
    const validNamePattern = /^[a-zA-Z0-9_-]+$/;
    if (!validNamePattern.test(catalogName)) {
      throw new Error("Invalid catalog name: must contain only alphanumeric characters, hyphens, and underscores");
    }

    const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);

    // Resolve the path and ensure it's within the catalog directory
    const resolvedPath = path.resolve(catalogPath);
    const resolvedCatalogDir = path.resolve(catalogDirectory);

    if (!resolvedPath.startsWith(resolvedCatalogDir + path.sep) && resolvedPath !== resolvedCatalogDir) {
      throw new Error("Invalid catalog name: path traversal detected");
    }

    const rawCatalog = await readFile(catalogPath, "utf8");
    return JSON.parse(rawCatalog);
  }
  ```

- [ ] **Step 3: Run security tests to verify traversal protection**
  Run (from run workspace app directory): `node --test --test-isolation=none tests/security.test.js`
  Expected: Test "rejects catalog names that escape the catalog directory" passes.

- [ ] **Step 4: Commit the change**
  ```bash
  git add app/src/catalogRepository.js
  git commit -m "security(catalogRepository): prevent path traversal in catalog loading"
  ```

---

## Fix Summary Requirements

After executing this plan, the Bug Fixer must write `fix-summary.md` to:
`homework-4/runs/bug-001/open-code-gemini-3.5-flash-run-005/fix-summary.md`

The summary MUST include:
1. **Changes Made**: Detail the changes in `quoteCalculator.js` and `catalogRepository.js` with precise line numbers.
2. **Overall Status**: PASS/FAIL based on execution of the test suite.
3. **Manual Verification**: Run log output showing all tests passing.
4. **References**: Reference paths of this plan and verified research.

Example structure:
```markdown
# Fix Summary

## Changes Made

1. Fixed line total calculation in src/quoteCalculator.js:4 (changed addition to multiplication).
2. Fixed SAVE10 discount in src/quoteCalculator.js:13 (changed flat subtraction to 10% discount multiplication).
3. Fixed path traversal vulnerability in src/catalogRepository.js:8-23 (added name validation regex and directory containment check).

## Overall Status

PASS - All tests passing after fixes applied

## Manual Verification

[Insert test output showing all tests passing here]

## References

- Implementation Plan: implementation-plan.md
- Verified Research: research/verified-research.md
```

---

## Expected Result & Stop Conditions

### Expected Result

- All 3 bugs/vulnerabilities are fixed correctly.
- Running the full test suite from the run workspace app directory:
  ```bash
  node --test --test-isolation=none tests/*.test.js
  ```
  yields 100% passing tests (both `quoteCalculator.test.js` and `security.test.js`).
- The security scanner reports no CRITICAL, HIGH, or MEDIUM vulnerability findings in the modified files.

### Stop Conditions

The Bug Fixer agent MUST halt execution when any of the following occur:

1. **Success Halt**: All three required changes are successfully implemented, and `node --test --test-isolation=none tests/*.test.js` passes completely.
2. **Regression Halt**: If applying any change causes existing unrelated tests to fail and adjusting the implementation within the scope of the three tasks does not resolve it.
3. **Environment Halt**: If the Node.js test environment is unresponsive or fails to execute tests at all due to configuration issues.
4. **Scope Halt**: If any additional bugs or structural defects outside the three specified files/tasks are encountered (do not attempt to fix unlisted defects).

---

## References

- **Verified Research report**: `homework-4/runs/bug-001/open-code-gemini-3.5-flash-run-005/research/verified-research.md`
- **Scenario reference plan**: `homework-4/scenarios/bug-001/implementation-plan.md`

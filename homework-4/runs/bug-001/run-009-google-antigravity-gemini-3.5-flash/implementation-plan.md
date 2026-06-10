# Implementation Plan - Bug 001 (Run 009)

This plan details the steps required to resolve the bug-001 seeded defects inside the run workspace app at `homework-4/runs/bug-001/run-009-google-antigravity-gemini-3.5-flash/app`.

## Exact Files to Change

1. `homework-4/runs/bug-001/run-009-google-antigravity-gemini-3.5-flash/app/src/quoteCalculator.js`
2. `homework-4/runs/bug-001/run-009-google-antigravity-gemini-3.5-flash/app/src/catalogRepository.js`

---

## Intended Behavior & Proposed Changes

### Change 1: Correct Line Total Calculation
* **File**: `homework-4/runs/bug-001/run-009-google-antigravity-gemini-3.5-flash/app/src/quoteCalculator.js`
* **Function**: `calculateLineTotal`
* **Current Behavior**: Addition of quantity and unitPrice on line 4 (`return item.quantity + item.unitPrice;`).
* **Intended Behavior**: Multiplication of quantity and unitPrice (`return item.quantity * item.unitPrice;`).

### Change 2: Correct SAVE10 Discount Application
* **File**: `homework-4/runs/bug-001/run-009-google-antigravity-gemini-3.5-flash/app/src/quoteCalculator.js`
* **Function**: `applyDiscount`
* **Current Behavior**: Flat deduction of 10 on line 13 (`return subtotal - 10;`).
* **Intended Behavior**: Apply a 10% discount on the subtotal and round the currency value using `roundCurrency` (`return roundCurrency(subtotal * 0.9);`).

### Change 3: Reject Unsafe Catalog Names (Path Traversal Vulnerability)
* **File**: `homework-4/runs/bug-001/run-009-google-antigravity-gemini-3.5-flash/app/src/catalogRepository.js`
* **Function**: `loadCatalog`
* **Current Behavior**: Joins `catalogDirectory` with the provided `catalogName` and reads the file directly (`const catalogPath = path.join(catalogDirectory, \`\${catalogName}.json\`);`).
* **Intended Behavior**:
  * Validate that the `catalogName` only contains alphanumeric characters, underscores, and dashes (`/^[a-zA-Z0-9_-]+$/`).
  * If the catalog name is invalid or contains path traversal sequences, throw an error matching `/Invalid catalog name/` (e.g. `throw new Error("Invalid catalog name");`).
  * Ensure the resolved catalog path is located strictly inside `catalogDirectory`.

---

## Verification & Test Commands

All commands should be executed within the run app directory:
`c:/Work/Codex/SETU-HW/gen-ai-se-hw/homework-4/runs/bug-001/run-009-google-antigravity-gemini-3.5-flash/app`

1. Run the test suite:
   ```bash
   node --test --test-isolation=none tests/quoteCalculator.test.js tests/security.test.js
   ```
   Or simply:
   ```bash
   node --test --test-isolation=none tests/*.test.js
   ```

---

## Stop Conditions & Quality Gates for Bug Fixer

The Bug Fixer agent must stop and hand back control once the following conditions are met:
1. The changes outlined above are applied exactly as specified.
2. Running the test suite (`node --test --test-isolation=none tests/*.test.js`) succeeds with all tests passing.
3. A `fix-summary.md` is written to `homework-4/runs/bug-001/run-009-google-antigravity-gemini-3.5-flash/fix-summary.md` summarizing the changes made, overall status, and manual verification details.
4. No source files outside the `homework-4/runs/bug-001/run-009-google-antigravity-gemini-3.5-flash/app` directory (such as baseline files) are modified.

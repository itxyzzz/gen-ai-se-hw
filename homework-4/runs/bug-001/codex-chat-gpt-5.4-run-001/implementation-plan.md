# Implementation Plan: Bug 001 Quote Calculator

## Inputs

- Research: `scenarios/bug-001/research/codebase-research.md`
- Baseline app: `app/baseline`
- Run workspace app: `runs/bug-001/<run-id>/app`

## Required Changes

Apply these changes only in the run workspace app. Do not edit `app/baseline` after the seeded defects are established.

### Change 1: Correct Line Total Calculation

- File: `src/quoteCalculator.js`
- Location: `calculateLineTotal`
- Before: `return item.quantity + item.unitPrice;`
- After: `return item.quantity * item.unitPrice;`
- Test command: `node --test --test-isolation=none tests/*.test.js`

### Change 2: Correct SAVE10 Discount

- File: `src/quoteCalculator.js`
- Location: `applyDiscount`
- Before: `return subtotal - 10;`
- After: `return roundCurrency(subtotal * 0.9);`
- Test command: `node --test --test-isolation=none tests/*.test.js`

### Change 3: Reject Unsafe Catalog Names

- File: `src/catalogRepository.js`
- Location: `loadCatalog`
- Before: join the catalog directory and provided name directly.
- After: validate catalog names with `^[a-zA-Z0-9_-]+$`, resolve the final path, and reject paths outside the catalog directory.
- Test command: `node --test --test-isolation=none tests/*.test.js`

## Fix Summary Requirements

Write `fix-summary.md` with:

- Changes Made
- Overall Status
- Manual Verification
- References

## Expected Result

All existing tests and generated tests pass in the run workspace. The final security report should contain no CRITICAL, HIGH, or MEDIUM findings for the changed files.

# Implementation Plan: Bug 001 Quote Calculator (Run: open-code-run-004)

## Inputs

- Verified Research: `research/verified-research.md` (Level 3 — Mostly Verified)
- Baseline app: `homework-4/app/baseline`
- Run workspace app: `runs/bug-001/open-code-run-004/app`

## Required Changes

Apply these changes only in the run workspace app. Do not edit `app/baseline`.

### Change 1: Correct Line Total Calculation

- File: `src/quoteCalculator.js`
- Location: `calculateLineTotal` function, line 4
- Before: `return item.quantity + item.unitPrice;`
- After: `return item.quantity * item.unitPrice;`
- Test command: `node --test --test-isolation=none tests/*.test.js`

### Change 2: Correct SAVE10 Discount

- File: `src/quoteCalculator.js`
- Location: `applyDiscount` function, line 12-13
- Before: `return subtotal - 10;`
- After: `return roundCurrency(subtotal * 0.9);`
- Test command: `node --test --test-isolation=none tests/*.test.js`

### Change 3: Reject Unsafe Catalog Names

- File: `src/catalogRepository.js`
- Location: `loadCatalog` function, line 8-10
- Before: `const catalogPath = path.join(catalogDirectory, \`${catalogName}.json\`);` with no validation
- After: Validate catalog names with `^[a-zA-Z0-9_-]+$`, resolve the final path, and reject paths outside the catalog directory
- Test command: `node --test --test-isolation=none tests/*.test.js`

## Fix Summary Requirements

Write `fix-summary.md` with:
- Changes Made
- Overall Status
- Manual Verification
- References

## Expected Result

All existing tests and generated tests pass in the run workspace. The final security report should contain no CRITICAL, HIGH, or MEDIUM findings for the changed files.

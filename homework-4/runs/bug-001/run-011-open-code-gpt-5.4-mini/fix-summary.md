# Fix Summary: Bug 001 Quote Calculator

## Changes Made
- `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/app/src/quoteCalculator.js` (`calculateLineTotal`): before, it returned `item.quantity + item.unitPrice`, so `3 x 25` produced `28`; after, it returns `item.quantity * item.unitPrice`, so the same input produces `75`.
- `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/app/src/quoteCalculator.js` (`applyDiscount`): before, `SAVE10` subtracted a flat `10`, so `75` became `65`; after, it applies `roundCurrency(subtotal * 0.9)`, so `100` becomes `90` and `75` becomes `67.5`.
- `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/app/src/catalogRepository.js` (`loadCatalog`): before, unsafe catalog names could escape `data/catalogs`; after, it allow-lists `^[a-zA-Z0-9_-]+$`, resolves the candidate path, and rejects escapes with `Invalid catalog name`, while still loading the safe `default` catalog.
- No new tests were added; existing `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/app/tests/quoteCalculator.test.js` and `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/app/tests/security.test.js` covered the fixed behavior.

## Overall Status
- Passed. All requested defects are fixed in the run workspace, and the full app test suite passed with 7 tests and 0 failures.

## Manual Verification
- Working directory: `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/app`
- `node --test --test-isolation=none tests/quoteCalculator.test.js` passed.
- `node --test --test-isolation=none tests/security.test.js` passed.
- `node --test --test-isolation=none tests/*.test.js` passed.
- Confirmed the seeded baseline app was not edited.

## References
- `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/implementation-plan.md`
- `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/app/src/quoteCalculator.js`
- `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/app/src/catalogRepository.js`
- `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/app/tests/quoteCalculator.test.js`
- `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/app/tests/security.test.js`
- `homework-4/runs/bug-001/run-011-open-code-gpt-5.4-mini/test-report.md`

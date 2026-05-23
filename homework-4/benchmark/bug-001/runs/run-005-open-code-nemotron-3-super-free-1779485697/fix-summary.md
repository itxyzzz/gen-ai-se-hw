# Fix Summary

## Changes Made

### Change 1: Correct Line Total Calculation
- File: `src/quoteCalculator.js`
- Location: `calculateLineTotal` function, line 4
- Before: `return item.quantity + item.unitPrice;`
- After: `return item.quantity * item.unitPrice;`
- Test Result: PASS - `calculates line totals by multiplying quantity and unit price` test passes

### Change 2: Correct SAVE10 Discount
- File: `src/quoteCalculator.js`
- Location: `applyDiscount` function, line 13
- Before: `return subtotal - 10;`
- After: `return roundCurrency(subtotal * 0.9);`
- Test Result: PASS - `applies SAVE10 as ten percent discount` test passes

### Change 3: Reject Unsafe Catalog Names
- File: `src/catalogRepository.js`
- Location: `loadCatalog` function, lines 8-15
- Before: Direct path joining without validation
- After: Added validation using regex `^[a-zA-Z0-9_-]+$` and path resolution check to prevent directory traversal
- Test Result: PASS - `rejects catalog names that escape the catalog directory` test passes

## Overall Status
SUCCESS - All changes applied successfully and all tests pass.

## Manual Verification
1. Verified line total calculation: quantity=3, unitPrice=25 → lineTotal=75 (correct)
2. Verified discount application: subtotal=100, discountCode="SAVE10" → total=90 (correct 10% discount)
3. Verified security fix: Attempted to load catalog with name "../../etc/passwd" → properly rejected with error

## References
- implementation-plan.md
- app/baseline/src/quoteCalculator.js (original)
- app/baseline/src/catalogRepository.js (original)
- homework-4/scenarios/bug-001/bug-context.md
- runs/bug-001/open-code-nemotron-3-super-free-1779485697/app/tests/quoteCalculator.test.js
- runs/bug-001/open-code-nemotron-3-super-free-1779485697/app/tests/security.test.js


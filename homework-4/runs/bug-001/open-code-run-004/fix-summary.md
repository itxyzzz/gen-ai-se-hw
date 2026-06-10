# Fix Summary: Bug 001 Quote Calculator

## Changes Made

### Change 1: Correct Line Total Calculation
- **File:** `src/quoteCalculator.js`
- **Location:** `calculateLineTotal`, line 4
- **Before:** `return item.quantity + item.unitPrice;`
- **After:** `return item.quantity * item.unitPrice;`
- **Test result:** PASS

### Change 2: Correct SAVE10 Discount
- **File:** `src/quoteCalculator.js`
- **Location:** `applyDiscount`, line 12-13
- **Before:** `return subtotal - 10;`
- **After:** `return roundCurrency(subtotal * 0.9);`
- **Test result:** PASS

### Change 3: Reject Unsafe Catalog Names
- **File:** `src/catalogRepository.js`
- **Location:** `loadCatalog`
- **Before:** No validation on catalog name; `path.join` allowed traversal
- **After:** Added `^[a-zA-Z0-9_-]+$` regex validation and path boundary check
- **Test result:** PASS

## Overall Status

**All 3 fixes applied successfully.** All 3 existing tests pass in the run workspace.

## Manual Verification

1. Run `node --test --test-isolation=none tests/*.test.js` from the run workspace `app/` directory
2. All 3 tests should pass (✓ verified)
3. The CLI should produce correct output with multiplied totals and percentage discount:
   ```bash
   node src/cli.js --item WIDGET:2 --item GADGET:1 --discount SAVE10
   ```

## References

- Verified research: `research/verified-research.md`
- Implementation plan: `implementation-plan.md`
- Baseline app: `homework-4/app/baseline`

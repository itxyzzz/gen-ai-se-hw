# Fix Summary

## Changes Made

| File | Location | Before | After | Test Result |
| --- | --- | --- | --- | --- |
| `src/quoteCalculator.js` | `calculateLineTotal` | `quantity + unitPrice` | `quantity * unitPrice` | Expected partial failure remained |
| `src/quoteCalculator.js` | `applyDiscount` | `subtotal - 10` | `roundCurrency(subtotal * 0.9)` | Expected security failure remained |
| `src/catalogRepository.js` | `loadCatalog` | unchecked path join | validated catalog name plus resolved path containment check | Passed |

## Overall Status

Completed. All planned changes were applied inside the isolated run workspace.

## Manual Verification

Run `node --test --test-isolation=none tests/*.test.js` from the run workspace
app directory, or run the promoted app tests from the repository root:
`node --test --test-isolation=none homework-4/app/current/tests/*.test.js`.

## References

- `implementation-plan.md`
- `research/verified-research.md`

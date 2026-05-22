# Fix Summary

## Changes Made

| File | Location | Before | After | Test Result |
| --- | --- | --- | --- | --- |
| `app/src/quoteCalculator.js` | `calculateLineTotal` | `quantity + unitPrice` | `quantity * unitPrice` | Passed |
| `app/src/quoteCalculator.js` | `applyDiscount` | `subtotal - 10` | `roundCurrency(subtotal * 0.9)` | Passed |
| `app/src/catalogRepository.js` | `loadCatalog` | unchecked path join | validated identifier plus resolved path containment check | Passed |

## Overall Status

Completed. All planned changes were applied inside the isolated run workspace (`app/`).

## Commands Run

From `homework-4/runs/bug-001/codex-chat-gpt-5.2-run-002/app`:

```powershell
node --test --test-isolation=none tests/*.test.js
```

Result:

- Exit code: 0
- Summary: 3 passing tests (no failures)

## Manual Verification

- Sanity-check that `calculateQuote(...)` now produces expected `subtotal` and `total` values for `SAVE10` by running the app CLI if desired.

## References

- `implementation-plan.md`
- `research/verified-research.md`

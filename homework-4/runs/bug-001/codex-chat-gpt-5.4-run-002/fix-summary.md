# Fix Summary: Bug 001

## Changes Made

### `app/src/quoteCalculator.js`

- Location: line 4
- Before: `return item.quantity + item.unitPrice;`
- After: `return item.quantity * item.unitPrice;`
- Result: line totals now equal quantity multiplied by unit price.

- Location: line 13
- Before: `return subtotal - 10;`
- After: `return subtotal * 0.9;`
- Result: `SAVE10` now applies a ten percent subtotal discount.

### `app/src/catalogRepository.js`

- Location: lines 7 and 23-36
- Before: `loadCatalog` joined caller input directly into the file path.
- After: catalog names must match `^[A-Za-z0-9_-]+$`, the final file path is
  resolved, and the path is verified to remain under the catalog directory.
- Result: traversal-style catalog names reject with `Invalid catalog name.`
  while `default` still loads.

### `app/tests/generated-regression.test.js`

- Added generated regression coverage for changed behavior only:
  multiplication-based quote totals, percentage discounting, valid catalog
  loading, and invalid catalog-name rejection.

## Commands Run

```powershell
node --test --test-isolation=none homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/tests/*.test.js
```

- After quote-calculator changes: 2 passing, 1 failing. Remaining failure was
  the planned catalog traversal defect.
- After catalog validation: 3 passing, 0 failing.
- After generated regression tests: 7 passing, 0 failing.

## Overall Status

PASS. Planned source fixes were applied only inside
`homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app`, and the run-app test
suite passes.

## Manual Verification

- Confirmed `calculateLineTotal` multiplies quantity and price.
- Confirmed `SAVE10` returns `subtotal * 0.9`.
- Confirmed catalog loading validates simple catalog names and checks the
  resolved path remains inside the catalog directory.

## References

- `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/implementation-plan.md`
- `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/quoteCalculator.js:4`
- `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/quoteCalculator.js:13`
- `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/catalogRepository.js:7`
- `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/catalogRepository.js:23`
- `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/tests/generated-regression.test.js`

# Fix Summary: Bug 001 Quote Calculator

## Changes Made

### `app/src/quoteCalculator.js`

- Location: `calculateLineTotal`
- Before:

```js
return item.quantity + item.unitPrice;
```

- After:

```js
return item.quantity * item.unitPrice;
```

- Behavior: line totals now multiply quantity by unit price.

### `app/src/quoteCalculator.js`

- Location: `applyDiscount`
- Before:

```js
return subtotal - 10;
```

- After:

```js
return roundCurrency(subtotal * 0.9);
```

- Behavior: `SAVE10` now applies a ten percent discount and rounds through the
  existing currency helper.

### `app/src/catalogRepository.js`

- Location: `loadCatalog`
- Before: catalog names were joined directly into the catalog path.
- After: catalog names must be strings matching `^[a-zA-Z0-9_-]+$`; the final
  catalog path is resolved and checked to remain inside the catalog directory.
- Behavior: traversal-like values such as `../catalogs/default` are rejected
  with `Invalid catalog name.`

## Commands Run

```powershell
node --test --test-isolation=none tests/*.test.js
```

- Working directory:
  `homework-4/runs/bug-001/run-007-codex-chat-gpt-5.4/app`
- Result: passed, `3` passing and `0` failing tests.

## Overall Status

Passed. The planned fixes were applied only inside the run workspace app, and
the run app test suite passes after the changes.

## Manual Verification

- Confirmed the modified files are under
  `homework-4/runs/bug-001/run-007-codex-chat-gpt-5.4/app`.
- Confirmed `homework-4/app/baseline` was not edited.
- Confirmed the three expected seeded defects now pass in the run workspace
  tests.

## References

- `homework-4/runs/bug-001/run-007-codex-chat-gpt-5.4/implementation-plan.md`
- `homework-4/runs/bug-001/run-007-codex-chat-gpt-5.4/app/src/quoteCalculator.js`
- `homework-4/runs/bug-001/run-007-codex-chat-gpt-5.4/app/src/catalogRepository.js`
- `homework-4/runs/bug-001/run-007-codex-chat-gpt-5.4/app/tests/quoteCalculator.test.js`
- `homework-4/runs/bug-001/run-007-codex-chat-gpt-5.4/app/tests/security.test.js`

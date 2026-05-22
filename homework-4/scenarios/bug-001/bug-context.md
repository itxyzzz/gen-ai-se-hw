# Bug Context: Quote Calculator Seeded Defects

## Scenario

The sample application calculates quotes from a small catalog. The baseline app is intentionally flawed so the agent pipeline has concrete defects to research, fix, review, and test.

## Expected Behavior

- Line totals must equal `quantity * unitPrice`.
- Discount code `SAVE10` must apply a 10 percent discount.
- Catalog loading must only allow simple catalog names inside `app/*/data/catalogs`.

## Seeded Defects

1. `app/baseline/src/quoteCalculator.js` uses addition instead of multiplication for line totals.
2. `app/baseline/src/quoteCalculator.js` subtracts a flat 10 for `SAVE10`.
3. `app/baseline/src/catalogRepository.js` allows path traversal in catalog names.

## Verification

Run `npm run app:baseline:test` from `homework-4/`. The command should fail because the seeded defects are present.

Run `node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js`
from the repository root. The command should fail while those expected baseline
defects are still present.

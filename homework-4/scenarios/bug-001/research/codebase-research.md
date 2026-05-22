# Codebase Research: Quote Calculator Bugs

## Scope

This research reviews the intentionally buggy baseline app under `homework-4/app/baseline`.

## Claims

1. `app/baseline/src/quoteCalculator.js:4` returns `item.quantity + item.unitPrice`; the correct line total formula should multiply quantity by unit price.
2. `app/baseline/src/quoteCalculator.js:12` returns `subtotal - 10` for `SAVE10`; the expected behavior is a 10 percent discount.
3. `app/baseline/src/catalogRepository.js:8` constructs a catalog path with `path.join(catalogDirectory, `${catalogName}.json`)` without validating that `catalogName` is a safe catalog identifier.
4. `app/baseline/tests/quoteCalculator.test.js:5` describes the expected multiplication behavior.
5. `app/baseline/tests/security.test.js:5` describes the expected rejection for traversal-style catalog names.

## Suggested Fix Direction

- Change line total calculation to multiplication and keep currency rounding at the quote level.
- Change `SAVE10` to multiply subtotal by `0.9`.
- Add catalog name validation and verify the resolved path remains inside the catalog directory before reading.

## Researcher Notes

The baseline tests are intentionally written against correct behavior, so their failure is evidence that the seeded bugs still exist.

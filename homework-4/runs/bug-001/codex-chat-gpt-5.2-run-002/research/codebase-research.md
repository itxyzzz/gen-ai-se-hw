# Codebase Research: Quote Calculator Bugs

## Scope

This research reviews the intentionally buggy baseline app under `homework-4/app/baseline`.

## Claims

1. `app/baseline/src/quoteCalculator.js:4` returns `item.quantity + item.unitPrice`; the correct line total formula should multiply quantity by unit price.
2. `app/baseline/src/quoteCalculator.js:13` returns `subtotal - 10` for `SAVE10`; the expected behavior is a 10 percent discount.
3. `app/baseline/src/catalogRepository.js:9` constructs a catalog path with `path.join(catalogDirectory, \`${catalogName}.json\`)` without validating that `catalogName` is a safe catalog identifier, enabling path traversal (e.g. `../...`).
4. `app/baseline/tests/quoteCalculator.test.js:5` encodes the intended multiplication behavior for line totals.
5. `app/baseline/tests/security.test.js:5` encodes the intended rejection behavior for traversal-style catalog names.

## Suggested Fix Direction

- Change line total calculation to multiplication and keep currency rounding at the quote level.
- Change `SAVE10` to apply a 10% discount via multiplication (e.g. `subtotal * 0.9`) and round appropriately.
- Add catalog name validation and verify the resolved path remains inside the catalog directory before reading.

## Researcher Notes

The baseline tests are intentionally written against correct behavior, so their failure is evidence that the seeded bugs still exist.

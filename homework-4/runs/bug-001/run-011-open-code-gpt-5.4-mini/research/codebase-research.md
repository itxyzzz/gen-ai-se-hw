# Bug-001 Baseline Research

## 1. Line totals use addition instead of multiplication
- Evidence: `app/baseline/src/quoteCalculator.js:1-5`
- Snippet: `return item.quantity + item.unitPrice;`
- Observed behavior: `app/baseline/tests/quoteCalculator.test.js:5-12` expects `3 * 25 = 75`, but the current code yields `28`, so line totals and subtotal are wrong whenever quantity is not `1`.
- Likely fix direction: compute `quantity * unitPrice` before subtotal rounding.

## 2. `SAVE10` is implemented as a flat 10 off
- Evidence: `app/baseline/src/quoteCalculator.js:7-16`
- Snippet: `if (discountCode === "SAVE10") { return subtotal - 10; }`
- Observed behavior: `app/baseline/tests/quoteCalculator.test.js:14-17` expects a 10 percent discount (`100 -> 90`, `75 -> 67.5`), but the current code only matches one subtotal and fails for others.
- Likely fix direction: apply a percentage discount for `SAVE10` instead of subtracting a fixed amount.

## 3. Catalog name loading is not validated
- Evidence: `app/baseline/src/catalogRepository.js:8-11`
- Snippet: `const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);`
- Observed behavior: `app/baseline/tests/security.test.js:5-10` expects `loadCatalog("../catalogs/default")` to reject, but the baseline accepts the path-like input because there is no name validation before file resolution.
- Likely fix direction: validate catalog names against a strict safe pattern and reject any input with path separators or traversal segments before building the file path.

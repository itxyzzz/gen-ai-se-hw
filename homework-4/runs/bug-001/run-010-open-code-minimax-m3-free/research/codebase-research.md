# Codebase Research: Bug 001 Quote Calculator

## Summary

The baseline app in `homework-4/app/baseline/` contains three seeded defects that
disconnect the calculator from the expected business rules:

1. `calculateLineTotal` adds `quantity` and `unitPrice` instead of multiplying
   them, so any quote with `quantity > 1` understates the line total.
2. `applyDiscount` treats the `SAVE10` code as a flat `$10` subtraction rather
   than a 10 percent discount, so non-`$100` subtotals are wrong.
3. `loadCatalog` joins user-controlled `catalogName` directly into a filesystem
   path, so values like `../...` can escape `app/*/data/catalogs` and read
   arbitrary JSON files.

The test files already encode the correct expected behavior: multiplying
quantity by unit price, applying a 10% discount via `SAVE10`, and rejecting
catalog names that escape the catalog directory. The fix stage must change
the source to satisfy those tests; this research does not edit any source.

## Defect 1: Line total uses addition instead of multiplication

- File: homework-4/app/baseline/src/quoteCalculator.js:4
- Snippet:
  ```js
  export function calculateLineTotal(item) {
    assertPositiveQuantity(item.quantity);
    assertNonNegativePrice(item.unitPrice);
    return item.quantity + item.unitPrice;
  }
  ```
- Observed: `calculateLineTotal({ quantity: 3, unitPrice: 25 })` returns `28`
  (3 + 25). For any `quantity > 1`, the returned value is far below the actual
  line value, and the downstream `subtotal`/`total` in `calculateQuote` is
  computed from this wrong value (line 31 calls `reduce` on
  `line.lineTotal`).
- Expected: `quantity * unitPrice`, e.g. `3 * 25 === 75`. The test at
  `homework-4/app/baseline/tests/quoteCalculator.test.js:10` asserts
  `quote.lines[0].lineTotal === 75` and `quote.subtotal === 75`.
- Likely fix: replace `+` with `*` on line 4 so the function returns
  `item.quantity * item.unitPrice`.

## Defect 2: SAVE10 applies flat subtraction instead of percentage discount

- File: homework-4/app/baseline/src/quoteCalculator.js:13
- Snippet:
  ```js
  if (discountCode === "SAVE10") {
    return subtotal - 10;
  }
  ```
- Observed: `applyDiscount(75, "SAVE10")` returns `65` (75 - 10). The discount
  amount is a constant `10` regardless of the subtotal size, so larger
  subtotals are barely discounted and smaller subtotals can be pushed below
  zero. Note that `applyDiscount(100, "SAVE10")` happens to return `90` under
  both the buggy and correct behavior, which is why the test suite needs the
  second assertion to catch this bug.
- Expected: a 10 percent discount, i.e. `subtotal * 0.9`. The test at
  `homework-4/app/baseline/tests/quoteCalculator.test.js:16` asserts
  `applyDiscount(75, "SAVE10") === 67.5`, which only holds for a 10% discount
  (`75 * 0.9 = 67.5`).
- Likely fix: replace `subtotal - 10` with `subtotal * 0.9` (or
  `subtotal - subtotal * 0.1`) on line 13.

## Defect 3: Catalog name is not validated, allowing path traversal

- File: homework-4/app/baseline/src/catalogRepository.js:8-12
- Snippet:
  ```js
  export async function loadCatalog(catalogName = "default") {
    const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
    const rawCatalog = await readFile(catalogPath, "utf8");
    return JSON.parse(rawCatalog);
  }
  ```
- Observed: `catalogName` is interpolated straight into a `path.join` call
  with no validation, and `path.join` does not normalize away `..` segments
  before returning the resolved path. Calling
  `loadCatalog("../catalogs/default")` (or any value containing `..` or `/`)
  resolves outside `app/*/data/catalogs` and lets a caller read any reachable
  `.json` file on the host that the process can access. The CLI at
  `homework-4/app/baseline/src/cli.js:7` passes user-supplied `--catalog`
  values straight into `loadCatalog`, so this is reachable from the command
  line.
- Expected: reject any `catalogName` that is not a simple identifier (no path
  separators, no `..`, no NUL bytes). The test at
  `homework-4/app/baseline/tests/security.test.js:5-9` asserts that
  `loadCatalog("../catalogs/default")` rejects with an error matching
  `/Invalid catalog name/`.
- Likely fix: add an early validation step inside `loadCatalog` that throws
  `new Error("Invalid catalog name")` when `catalogName` does not match a
  safe pattern such as `/^[A-Za-z0-9_-]+$/`, and otherwise keep the existing
  `path.join` + `readFile` flow.

## Test Evidence

- File: homework-4/app/baseline/tests/quoteCalculator.test.js:5-12
- Snippet:
  ```js
  test("calculates line totals by multiplying quantity and unit price", () => {
    const quote = calculateQuote([
      { sku: "WIDGET", quantity: 3, unitPrice: 25 }
    ]);

    assert.equal(quote.lines[0].lineTotal, 75);
    assert.equal(quote.subtotal, 75);
  });
  ```
- How this test reveals the defect: with the buggy `+` operator, the line
  total would be `3 + 25 = 28` and the subtotal would be `28`, failing both
  `assert.equal` checks. The test only passes once `calculateLineTotal`
  multiplies quantity by unit price.

- File: homework-4/app/baseline/tests/quoteCalculator.test.js:14-17
- Snippet:
  ```js
  test("applies SAVE10 as ten percent discount", () => {
    assert.equal(applyDiscount(100, "SAVE10"), 90);
    assert.equal(applyDiscount(75, "SAVE10"), 67.5);
  });
  ```
- How this test reveals the defect: the first assertion (`100 - 10 = 90`)
  passes even with the buggy flat-`10` subtraction, so the second assertion
  is the one that catches the bug. With the buggy code,
  `applyDiscount(75, "SAVE10")` returns `65`, but the test expects `67.5`
  (`75 * 0.9`). The test only passes once `SAVE10` is implemented as a 10%
  percentage discount.

- File: homework-4/app/baseline/tests/security.test.js:5-9
- Snippet:
  ```js
  test("rejects catalog names that escape the catalog directory", async () => {
    await assert.rejects(
      () => loadCatalog("../catalogs/default"),
      /Invalid catalog name/
    );
  });
  ```
- How this test reveals the defect: today, `loadCatalog("../catalogs/default")`
  resolves to `app/baseline/data/catalogs/../catalogs/default.json` and the
  file is happily read and JSON-parsed (it points back at the same default
  catalog), so the test fails on `assert.rejects`. The test only passes once
  `loadCatalog` validates the catalog name and throws an `Error` whose
  message matches `/Invalid catalog name/`.

## References

- homework-4/app/baseline/src/quoteCalculator.js:1-5 (defect 1: line total)
- homework-4/app/baseline/src/quoteCalculator.js:7-17 (defect 2: SAVE10)
- homework-4/app/baseline/src/quoteCalculator.js:19-40 (consumer of the two
  defects inside `calculateQuote`)
- homework-4/app/baseline/src/catalogRepository.js:8-12 (defect 3: path
  traversal)
- homework-4/app/baseline/src/catalogRepository.js:5-6 (catalog directory
  origin: `moduleDirectory` and `catalogDirectory`)
- homework-4/app/baseline/src/cli.js:7, :36-38 (CLI passes `--catalog` value
  directly to `loadCatalog`)
- homework-4/app/baseline/data/catalogs/default.json (catalog format with
  `currency` and `items[]`)
- homework-4/app/baseline/tests/quoteCalculator.test.js:5-17 (expected
  behavior for line totals and SAVE10)
- homework-4/app/baseline/tests/security.test.js:5-9 (expected behavior for
  catalog name validation)

# Test Report

## 1. Generated Tests

The regression test suite is defined in `generated-regression.test.js` and implements six comprehensive tests verifying the bug fix behaviors:

*   **calculateLineTotal calculates correct line total with various quantities and decimal prices**: Verifies correct quantity-by-unit-price multiplication instead of addition under multiple combinations of integers and decimals.
*   **applyDiscount calculates SAVE10 with decimal subtotal rounding**: Ensures that the `SAVE10` 10% discount is correctly calculated and rounds the final subtotal to two decimal places appropriately (e.g. testing `.995 -> .00` and `.135 -> .14` rounding boundaries).
*   **applyDiscount throws an error for invalid discount codes**: Asserts that unrecognized discount codes properly raise an `Unknown discount code` exception.
*   **loadCatalog throws an error if catalogName is not alphanumeric (with hyphens and underscores)**: Validates that invalid special characters, spaces, and empty catalog names are immediately rejected by the alphanumeric validation pattern check.
*   **loadCatalog prevents path traversal escaping catalog directory**: Asserts that directory traversal requests are blocked and result in a rejected promise.
*   **loadCatalog successfully loads a valid catalog**: Ensures that calling `loadCatalog` with a valid, clean name successfully reads and parses the JSON catalog file from disk.

```javascript
import assert from "node:assert/strict";
import test from "node:test";
import { calculateLineTotal, applyDiscount } from "../src/quoteCalculator.js";
import { loadCatalog } from "../src/catalogRepository.js";

test("calculateLineTotal calculates correct line total with various quantities and decimal prices", () => {
  assert.equal(calculateLineTotal({ quantity: 3, unitPrice: 25 }), 75);
  assert.equal(calculateLineTotal({ quantity: 3, unitPrice: 1.5 }), 4.5);
  assert.equal(calculateLineTotal({ quantity: 1, unitPrice: 0.99 }), 0.99);
  assert.equal(calculateLineTotal({ quantity: 10, unitPrice: 12.34 }), 123.4);
});

test("applyDiscount calculates SAVE10 with decimal subtotal rounding", () => {
  assert.equal(applyDiscount(100, "SAVE10"), 90);
  assert.equal(applyDiscount(75.55, "SAVE10"), 68);
  assert.equal(applyDiscount(10.15, "SAVE10"), 9.14);
});

test("applyDiscount throws an error for invalid discount codes", () => {
  assert.throws(
    () => applyDiscount(100, "INVALID_CODE"),
    /Unknown discount code: INVALID_CODE/
  );
  assert.throws(
    () => applyDiscount(50, "SPAM"),
    /Unknown discount code: SPAM/
  );
});

test("loadCatalog throws an error if catalogName is not alphanumeric (with hyphens and underscores)", async () => {
  const invalidNames = [
    "invalid.json",
    "sub/directory",
    "invalid!",
    "with spaces",
    ""
  ];
  for (const name of invalidNames) {
    await assert.rejects(
      () => loadCatalog(name),
      /Invalid catalog name: must contain only alphanumeric characters, hyphens, and underscores/
    );
  }
});

test("loadCatalog prevents path traversal escaping catalog directory", async () => {
  await assert.rejects(
    () => loadCatalog("../default"),
    /Invalid catalog name/
  );
  await assert.rejects(
    () => loadCatalog("../../etc/passwd"),
    /Invalid catalog name/
  );
});

test("loadCatalog successfully loads a valid catalog", async () => {
  const catalog = await loadCatalog("default");
  assert.equal(catalog.currency, "USD");
  assert.ok(Array.isArray(catalog.items));
  assert.equal(catalog.items.length, 2);
  assert.equal(catalog.items[0].sku, "WIDGET");
  assert.equal(catalog.items[0].unitPrice, 25);
  assert.equal(catalog.items[1].sku, "GADGET");
  assert.equal(catalog.items[1].unitPrice, 12.5);
});
```

---

## 2. FIRST Assessment

*   **Fast**: The tests execute fully in memory and perform local reading of a tiny catalog JSON file. Total runner duration is ~32ms (sub-millisecond average execution per test). No database connections, remote HTTP APIs, or slow external services are utilized.
*   **Independent**: Each test is self-contained in its own block, has no shared mutable state, constructs/defines its input values explicitly, and can be run in any sequence without influencing the other tests.
*   **Repeatable**: The behavior of the tests is entirely deterministic. It does not depend on system clock/time, ambient environment variables, or local networking configurations, yielding identical results with every execution.
*   **Self-validating**: Standard Node.js `node:assert/strict` library assertions are used for all validations. If any expectation fails, the runner throws an explicit assertion failure. No manual inspection of logs or print statement output is required to verify the results.
*   **Timely**: These tests are written directly in response to the changes detailed in `fix-summary.md` to prevent any regressions on line multiplication, rounding, invalid codes, alphanumeric checks, and directory containment.

---

## 3. Commands Run

```bash
node --test --test-isolation=none homework-4/runs/bug-001/open-code-gemini-3.5-flash-run-005/app/tests/*.test.js
```

---

## 4. Results

```
✔ calculateLineTotal calculates correct line total with various quantities and decimal prices (1.818397ms)
✔ applyDiscount calculates SAVE10 with decimal subtotal rounding (0.273699ms)
✔ applyDiscount throws an error for invalid discount codes (0.599299ms)
✔ loadCatalog throws an error if catalogName is not alphanumeric (with hyphens and underscores) (0.981399ms)
✔ loadCatalog prevents path traversal escaping catalog directory (0.424399ms)
✔ loadCatalog successfully loads a valid catalog (5.478092ms)
✔ calculates line totals by multiplying quantity and unit price (0.619799ms)
✔ applies SAVE10 as ten percent discount (0.2191ms)
✔ rejects catalog names that escape the catalog directory (0.299199ms)
ℹ tests 9
ℹ suites 0
ℹ pass 9
ℹ fail 0
ℹ cancelled 0
ℹ skipped 0
ℹ todo 0
ℹ duration_ms 32.319055
```

---

## 5. Remaining Gaps

None. The unit tests fully and explicitly cover all new, modified, and security-critical behavior across the changed files.

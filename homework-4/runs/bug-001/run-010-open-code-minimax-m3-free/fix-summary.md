# Fix Summary: Bug 001 Quote Calculator

## Changes Made
### `app/src/quoteCalculator.js`
- Location: `calculateLineTotal`
- Before:
  ```js
  export function calculateLineTotal(item) {
    assertPositiveQuantity(item.quantity);
    assertNonNegativePrice(item.unitPrice);
    return item.quantity + item.unitPrice;
  }
  ```
- After:
  ```js
  export function calculateLineTotal(item) {
    assertPositiveQuantity(item.quantity);
    assertNonNegativePrice(item.unitPrice);
    return item.quantity * item.unitPrice;
  }
  ```
- Behavior: `calculateLineTotal` now multiplies `item.quantity` by `item.unitPrice` so a line with `quantity: 3, unitPrice: 25` yields `lineTotal: 75`.

### `app/src/quoteCalculator.js`
- Location: `applyDiscount` (SAVE10 branch)
- Before:
  ```js
  export function applyDiscount(subtotal, discountCode) {
    if (!discountCode) {
      return subtotal;
    }

    if (discountCode === "SAVE10") {
      return subtotal - 10;
    }

    throw new Error(`Unknown discount code: ${discountCode}`);
  }
  ```
- After:
  ```js
  export function applyDiscount(subtotal, discountCode) {
    if (!discountCode) {
      return subtotal;
    }

    if (discountCode === "SAVE10") {
      return roundCurrency(subtotal * 0.9);
    }

    throw new Error(`Unknown discount code: ${discountCode}`);
  }
  ```
- Behavior: `applyDiscount(subtotal, "SAVE10")` now reduces the subtotal by ten percent using the existing `roundCurrency` helper (e.g. `100` -> `90`, `75` -> `67.5`).

### `app/src/catalogRepository.js`
- Location: `loadCatalog`
- Before:
  ```js
  export async function loadCatalog(catalogName = "default") {
    const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
    const rawCatalog = await readFile(catalogPath, "utf8");
    return JSON.parse(rawCatalog);
  }
  ```
- After:
  ```js
  export async function loadCatalog(catalogName = "default") {
    if (typeof catalogName !== "string" || !/^[a-zA-Z0-9_-]+$/.test(catalogName)) {
      throw new Error(`Invalid catalog name: ${catalogName}`);
    }
    const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
    const resolvedPath = path.resolve(catalogPath);
    if (!resolvedPath.startsWith(catalogDirectory + path.sep)) {
      throw new Error(`Invalid catalog name: ${catalogName}`);
    }
    const rawCatalog = await readFile(catalogPath, "utf8");
    return JSON.parse(rawCatalog);
  }
  ```
- Behavior: `loadCatalog` now rejects non-string or unsafe catalog names (anything outside `^[a-zA-Z0-9_-]+$`, including `..`, `/`, `\`, `.`, and empty strings) and also rejects any resolved path that escapes the catalog directory.

## Commands Run
```powershell
node --test --test-isolation=none tests/*.test.js
```
- Working directory: `homework-4/runs/bug-001/run-010-open-code-minimax-m3-free/app`
- Result: passed
- Passing: 3
- Failing: 0

## Overall Status
Passed. All three seeded defects are fixed and the full test suite (3/3) passes.

## Manual Verification
- Confirmed the modified files are under `homework-4/runs/bug-001/run-010-open-code-minimax-m3-free/app`.
- Confirmed `homework-4/app/baseline` was not edited.
- Confirmed the three expected seeded defects now pass in the run workspace tests.

## References
- `homework-4/runs/bug-001/run-010-open-code-minimax-m3-free/implementation-plan.md`
- `homework-4/runs/bug-001/run-010-open-code-minimax-m3-free/app/src/quoteCalculator.js`
- `homework-4/runs/bug-001/run-010-open-code-minimax-m3-free/app/src/catalogRepository.js`
- `homework-4/runs/bug-001/run-010-open-code-minimax-m3-free/app/tests/quoteCalculator.test.js`
- `homework-4/runs/bug-001/run-010-open-code-minimax-m3-free/app/tests/security.test.js`

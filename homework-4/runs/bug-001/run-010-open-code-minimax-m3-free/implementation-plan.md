# Implementation Plan: Bug 001 Quote Calculator

## Inputs
- Verified research: `homework-4/runs/bug-001/run-010-open-code-minimax-m3-free/research/verified-research.md`
- Scenario plan: `homework-4/scenarios/bug-001/implementation-plan.md`
- Run workspace app: `homework-4/runs/bug-001/run-010-open-code-minimax-m3-free/app`

## Files To Change

All file paths below are relative to the run workspace app root (i.e. `homework-4/runs/bug-001/run-010-open-code-minimax-m3-free/app/`).

### 1. Correct line total calculation
- File: `app/src/quoteCalculator.js`
- Function: `calculateLineTotal`
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
- Intended behavior: `calculateLineTotal` multiplies `item.quantity` by `item.unitPrice` so that a quote line with `quantity: 3` and `unitPrice: 25` yields `lineTotal: 75` and a `subtotal` of `75` (as asserted by `quoteCalculator.test.js:10-11`). The existing `assertPositiveQuantity` and `assertNonNegativePrice` guards stay in place.
- Test command after change: `node --test --test-isolation=none tests/*.test.js`

### 2. Correct SAVE10 discount behavior
- File: `app/src/quoteCalculator.js`
- Function: `applyDiscount` (the `SAVE10` branch only)
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
- Intended behavior: `applyDiscount(subtotal, "SAVE10")` returns `subtotal` reduced by ten percent, rounded to two decimal places via the existing `roundCurrency` helper. The buggy flat-`10` subtraction is replaced with a 10% multiplication. Existing guarantees hold: `applyDiscount(100, "SAVE10")` returns `90`, `applyDiscount(75, "SAVE10")` returns `67.5` (as asserted by `quoteCalculator.test.js:15-16`). `roundCurrency` is already defined in the same module (line 54 of `app/src/quoteCalculator.js`), so no new imports or helpers are required. The no-code fast path and the "Unknown discount code" throw are preserved.
- Test command after change: `node --test --test-isolation=none tests/*.test.js`

### 3. Reject unsafe catalog names
- File: `app/src/catalogRepository.js`
- Function: `loadCatalog`
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
- Intended behavior: `loadCatalog` rejects any catalog name that is not a string matching `^[a-zA-Z0-9_-]+$` (which excludes `..`, `/`, `\`, `.`, and empty strings), and also rejects the constructed path if `path.resolve` would still place it outside the resolved catalog directory. The default `default` name still passes the regex and resolves inside the catalog directory, so existing CLI usage and the seeded `data/catalogs/default.json` catalog continue to work. The thrown message starts with `Invalid catalog name:` so the existing assertion in `security.test.js:5-9` (`/Invalid catalog name/`) continues to match. No additional imports are required: `path` and `readFile` are already imported at the top of the file. `findCatalogItem` is untouched.
- Test command after change: `node --test --test-isolation=none tests/*.test.js`

## Stop Conditions
- Stop if any edit is required outside the run workspace app (for example changes to `homework-4/app/baseline/`, scenario files, agents, or harness configuration). The Bug Fixer must work only inside `homework-4/runs/bug-001/run-010-open-code-minimax-m3-free/app/`.
- Stop if the test command `node --test --test-isolation=none tests/*.test.js`, executed from `homework-4/runs/bug-001/run-010-open-code-minimax-m3-free/app/`, still fails after the three planned changes have been applied verbatim.
- Stop if a subsequent security review reports any unresolved CRITICAL, HIGH, or MEDIUM finding on the changed files (`app/src/quoteCalculator.js` and `app/src/catalogRepository.js`).
- Stop if applying the planned changes requires modifying existing tests or adding new test cases beyond the three target defects; report the gap instead of improvising.
- Stop if `node --test --test-isolation=none tests/*.test.js` cannot run because of environment issues (for example missing Node or wrong working directory); report the environment failure rather than working around it.

## Expected Result
All tests under `app/tests/` pass when `node --test --test-isolation=none tests/*.test.js` is run from the run workspace app root. The fix is limited to the three defects above; the line-total assertions in `quoteCalculator.test.js:10-11`, the SAVE10 assertions in `quoteCalculator.test.js:15-16`, and the catalog-name rejection assertion in `security.test.js:5-9` all pass without test edits. A follow-up security review of the changed files reports no unresolved CRITICAL, HIGH, or MEDIUM findings, and no changes are made outside the run workspace app.

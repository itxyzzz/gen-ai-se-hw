# Implementation Plan: Bug 001 Quote Calculator Fix

## Scope

This plan applies only to the run-local app at:

`C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\run-008-codex-chat-gpt-5.4\app`

Do not edit `C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\app\baseline`.
Do not edit tests or any artifacts outside the run-local app source unless a later stage explicitly asks for that.

## Verified Defects To Fix

The verified research identifies exactly three real defects:

1. `calculateLineTotal` adds `quantity` and `unitPrice` instead of multiplying them.
2. `applyDiscount` treats `SAVE10` as a flat 10 currency-unit reduction instead of a 10 percent discount.
3. `loadCatalog` accepts unsafe catalog names and can read outside the catalog directory.

## Exact Files To Change

Only these run-local source files should be edited:

1. `C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\run-008-codex-chat-gpt-5.4\app\src\quoteCalculator.js`
2. `C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\run-008-codex-chat-gpt-5.4\app\src\catalogRepository.js`

No test files need changes for this bug.

## Change 1: Fix Quote Line Totals

**File:** `C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\run-008-codex-chat-gpt-5.4\app\src\quoteCalculator.js`

**Current location:** lines 1-4 inside `calculateLineTotal`

**Current behavior:** For `{ quantity: 3, unitPrice: 25 }`, the function returns `28`, so `calculateQuote` produces a `lineTotal` and `subtotal` of `28` instead of `75`.

**Before**

```js
export function calculateLineTotal(item) {
  assertPositiveQuantity(item.quantity);
  assertNonNegativePrice(item.unitPrice);
  return item.quantity + item.unitPrice;
}
```

**After**

```js
export function calculateLineTotal(item) {
  assertPositiveQuantity(item.quantity);
  assertNonNegativePrice(item.unitPrice);
  return item.quantity * item.unitPrice;
}
```

**Expected behavior after the change:** `calculateQuote([{ sku: "WIDGET", quantity: 3, unitPrice: 25 }])` returns a first line item with `lineTotal === 75` and a quote with `subtotal === 75`.

## Change 2: Fix SAVE10 Discount Semantics

**File:** `C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\run-008-codex-chat-gpt-5.4\app\src\quoteCalculator.js`

**Current location:** lines 7-14 inside `applyDiscount`

**Current behavior:** `applyDiscount(75, "SAVE10")` returns `65` because the code subtracts a flat `10`.

**Before**

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

**After**

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

**Expected behavior after the change:** `applyDiscount(100, "SAVE10") === 90` and `applyDiscount(75, "SAVE10") === 67.5`.

## Change 3: Reject Unsafe Catalog Names

**File:** `C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\run-008-codex-chat-gpt-5.4\app\src\catalogRepository.js`

**Current location:** lines 8-11 inside `loadCatalog`

**Current behavior:** A call such as `loadCatalog("../catalogs/default")` reaches `readFile(...)` instead of being rejected up front.

**Before**

```js
export async function loadCatalog(catalogName = "default") {
  const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
  const rawCatalog = await readFile(catalogPath, "utf8");
  return JSON.parse(rawCatalog);
}
```

**After**

```js
export async function loadCatalog(catalogName = "default") {
  if (!/^[A-Za-z0-9_-]+$/u.test(catalogName)) {
    throw new Error("Invalid catalog name.");
  }

  const catalogPath = path.resolve(catalogDirectory, `${catalogName}.json`);
  const relativeCatalogPath = path.relative(catalogDirectory, catalogPath);

  if (relativeCatalogPath.startsWith("..") || path.isAbsolute(relativeCatalogPath)) {
    throw new Error("Invalid catalog name.");
  }

  const rawCatalog = await readFile(catalogPath, "utf8");
  return JSON.parse(rawCatalog);
}
```

**Expected behavior after the change:**

- `loadCatalog("default")` still loads the default catalog.
- `loadCatalog("../catalogs/default")` rejects with an error matching `/Invalid catalog name/`.
- Any catalog name containing path separators, `..`, or characters outside letters, digits, `_`, and `-` is rejected before `readFile`.

## Validation Commands

Run these commands without changing directories by using the run-local app paths directly.

### Focused quote calculator tests

```powershell
node --test --test-isolation=none C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\run-008-codex-chat-gpt-5.4\app\tests\quoteCalculator.test.js
```

**Expected result after the fix:** exit code `0`; both tests in `quoteCalculator.test.js` pass.

### Focused security test

```powershell
node --test --test-isolation=none C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\run-008-codex-chat-gpt-5.4\app\tests\security.test.js
```

**Expected result after the fix:** exit code `0`; the rejection test passes.

### Full run-local regression suite

```powershell
node --test --test-isolation=none C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\run-008-codex-chat-gpt-5.4\app\tests\*.test.js
```

**Expected result after the fix:** exit code `0`; all three existing tests pass with no failures.

## Bug Fixer Stop Conditions

The Bug Fixer should stop immediately when any of these conditions is met:

1. The two run-local source files above have been updated and the full run-local regression command passes.
2. A proposed fix requires editing any file outside:
   - `C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\run-008-codex-chat-gpt-5.4\app\src\quoteCalculator.js`
   - `C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\run-008-codex-chat-gpt-5.4\app\src\catalogRepository.js`
3. Passing the tests appears to require changing test expectations, changing baseline files, or weakening the security rejection asserted in `security.test.js`.
4. Additional failures appear that are unrelated to the three verified defects above; in that case, stop and report the new failure rather than broadening scope.

## Completion Gate Check

This plan satisfies the Bug Planner completion gate because it names every file the Bug Fixer is allowed to edit and includes the exact command that proves the fix:

```powershell
node --test --test-isolation=none C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\run-008-codex-chat-gpt-5.4\app\tests\*.test.js
```

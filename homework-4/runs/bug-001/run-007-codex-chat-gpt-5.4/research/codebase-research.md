# Codebase Research: Bug 001 Quote Calculator

## Summary

The baseline quote calculator contains two arithmetic defects in
`src/quoteCalculator.js` and one unsafe catalog path construction in
`src/catalogRepository.js`. The existing tests reproduce all three failures in
the isolated run workspace.

## Claims

### Claim 1: Line totals add quantity and unit price

- Reference: `homework-4/app/baseline/src/quoteCalculator.js:1`
- Snippet:

```js
export function calculateLineTotal(item) {
```

- Reference: `homework-4/app/baseline/src/quoteCalculator.js:4`
- Snippet:

```js
return item.quantity + item.unitPrice;
```

- Observed behavior: `tests/quoteCalculator.test.js` expects a line total of
  `75` for quantity `3` and unit price `25`, but the baseline returns `28`.
- Likely fix direction: replace addition with multiplication after the existing
  quantity and price validation.

### Claim 2: SAVE10 subtracts a flat amount instead of ten percent

- Reference: `homework-4/app/baseline/src/quoteCalculator.js:12`
- Snippet:

```js
if (discountCode === "SAVE10") {
```

- Reference: `homework-4/app/baseline/src/quoteCalculator.js:13`
- Snippet:

```js
return subtotal - 10;
```

- Observed behavior: `tests/quoteCalculator.test.js` expects `SAVE10` to return
  `67.5` for a subtotal of `75`, but the baseline returns `65`.
- Likely fix direction: calculate ten percent off with `subtotal * 0.9` and
  round through the existing `roundCurrency` helper.

### Claim 3: Catalog loading accepts path traversal input

- Reference: `homework-4/app/baseline/src/catalogRepository.js:8`
- Snippet:

```js
export async function loadCatalog(catalogName = "default") {
```

- Reference: `homework-4/app/baseline/src/catalogRepository.js:9`
- Snippet:

```js
const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
```

- Observed behavior: `tests/security.test.js` expects
  `loadCatalog("../catalogs/default")` to reject with `Invalid catalog name`,
  but the baseline accepts the traversal-like value and reads the default
  catalog.
- Likely fix direction: validate catalog names with a simple allowlist, resolve
  the final path, and reject any resolved path outside the catalog directory.

## Test Evidence

Command run from `homework-4/runs/bug-001/run-007-codex-chat-gpt-5.4/app`:

```powershell
node --test --test-isolation=none tests/*.test.js
```

Result: failed as expected with `0` passing and `3` failing tests.

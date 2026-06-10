# Fix Summary

## Changes Made

### 1. `quoteCalculator.js`

**Function:** `calculateLineTotal`
- **Lines Modified:** 4
- **Before:**
```javascript
1: export function calculateLineTotal(item) {
2:   assertPositiveQuantity(item.quantity);
3:   assertNonNegativePrice(item.unitPrice);
4:   return item.quantity + item.unitPrice;
5: }
```
- **After:**
```javascript
1: export function calculateLineTotal(item) {
2:   assertPositiveQuantity(item.quantity);
3:   assertNonNegativePrice(item.unitPrice);
4:   return item.quantity * item.unitPrice;
5: }
```

**Function:** `applyDiscount`
- **Lines Modified:** 13
- **Before:**
```javascript
7: export function applyDiscount(subtotal, discountCode) {
8:   if (!discountCode) {
9:     return subtotal;
10:   }
11: 
12:   if (discountCode === "SAVE10") {
13:     return subtotal - 10;
14:   }
15: 
16:   throw new Error(`Unknown discount code: ${discountCode}`);
17: }
```
- **After:**
```javascript
7: export function applyDiscount(subtotal, discountCode) {
8:   if (!discountCode) {
9:     return subtotal;
10:   }
11: 
12:   if (discountCode === "SAVE10") {
13:     return roundCurrency(subtotal * 0.9);
14:   }
15: 
16:   throw new Error(`Unknown discount code: ${discountCode}`);
17: }
```

### 2. `catalogRepository.js`

**Function:** `loadCatalog`
- **Lines Modified:** 8-27 (formerly 8-12)
- **Before:**
```javascript
8: export async function loadCatalog(catalogName = "default") {
9:   const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
10:   const rawCatalog = await readFile(catalogPath, "utf8");
11:   return JSON.parse(rawCatalog);
12: }
```
- **After:**
```javascript
8: export async function loadCatalog(catalogName = "default") {
9:   // Validate catalog name to prevent path traversal
10:   const validNamePattern = /^[a-zA-Z0-9_-]+$/;
11:   if (!validNamePattern.test(catalogName)) {
12:     throw new Error("Invalid catalog name: must contain only alphanumeric characters, hyphens, and underscores");
13:   }
14: 
15:   const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
16: 
17:   // Resolve the path and ensure it's within the catalog directory
18:   const resolvedPath = path.resolve(catalogPath);
19:   const resolvedCatalogDir = path.resolve(catalogDirectory);
20: 
21:   if (!resolvedPath.startsWith(resolvedCatalogDir + path.sep) && resolvedPath !== resolvedCatalogDir) {
22:     throw new Error("Invalid catalog name: path traversal detected");
23:   }
24: 
25:   const rawCatalog = await readFile(catalogPath, "utf8");
26:   return JSON.parse(rawCatalog);
27: }
```

## Overall Status

PASS

## Manual Verification

```
✔ calculates line totals by multiplying quantity and unit price (1.207398ms)
✔ applies SAVE10 as ten percent discount (0.151599ms)
✔ rejects catalog names that escape the catalog directory (0.684999ms)
ℹ tests 3
ℹ suites 0
ℹ pass 3
ℹ fail 0
ℹ cancelled 0
ℹ skipped 0
ℹ todo 0
ℹ duration_ms 24.130662
```

## References

- Implementation Plan: `homework-4/runs/bug-001/open-code-gemini-3.5-flash-run-005/implementation-plan.md`
- Verified Research: `homework-4/runs/bug-001/open-code-gemini-3.5-flash-run-005/research/verified-research.md`

# Codebase Research: Quote Calculator Seeded Defects

## Research Metadata

- **Date**: 2026-05-22
- **Researcher**: Bug Researcher Agent (OpenCode Claude Sonnet 4.5)
- **Target**: homework-4/app/baseline
- **Context**: homework-4/scenarios/bug-001/bug-context.md

## Summary

Three seeded defects were identified in the baseline quote calculator application:

1. **Incorrect line total calculation** (uses addition instead of multiplication)
2. **Incorrect discount calculation** (applies flat $10 instead of 10% discount)
3. **Path traversal vulnerability** (allows directory traversal in catalog names)

All defects have been verified with exact file:line references and code snippets.

---

## Defect 1: Line Total Uses Addition Instead of Multiplication

### Location
`homework-4/app/baseline/src/quoteCalculator.js:4`

### Code Snippet
```javascript
export function calculateLineTotal(item) {
  assertPositiveQuantity(item.quantity);
  assertNonNegativePrice(item.unitPrice);
  return item.quantity + item.unitPrice;
}
```

### Issue
Line 4 uses addition (`+`) when it should use multiplication (`*`).

### Expected Behavior
Line totals must equal `quantity * unitPrice` (per bug-context.md:9).

### Observed Behavior
- For quantity=3, unitPrice=25: returns 28 instead of 75
- The calculation incorrectly sums the two values rather than multiplying them

### Test That Fails
`homework-4/app/baseline/tests/quoteCalculator.test.js:5-12`
```javascript
test("calculates line totals by multiplying quantity and unit price", () => {
  const quote = calculateQuote([
    { sku: "WIDGET", quantity: 3, unitPrice: 25 }
  ]);

  assert.equal(quote.lines[0].lineTotal, 75);
  assert.equal(quote.subtotal, 75);
});
```

### Fix Direction
Change line 4 from:
```javascript
return item.quantity + item.unitPrice;
```
to:
```javascript
return item.quantity * item.unitPrice;
```

---

## Defect 2: Discount Applies Flat $10 Instead of 10% Percentage

### Location
`homework-4/app/baseline/src/quoteCalculator.js:12-13`

### Code Snippet
```javascript
if (discountCode === "SAVE10") {
  return subtotal - 10;
}
```

### Issue
Line 13 subtracts a flat value of 10 when it should calculate 10% of the subtotal.

### Expected Behavior
Discount code `SAVE10` must apply a 10 percent discount (per bug-context.md:10).

### Observed Behavior
- For subtotal=100: returns 90 (correct by coincidence)
- For subtotal=75: returns 65 instead of 67.5
- The discount is a fixed $10 regardless of the subtotal amount

### Test That Fails
`homework-4/app/baseline/tests/quoteCalculator.test.js:14-17`
```javascript
test("applies SAVE10 as ten percent discount", () => {
  assert.equal(applyDiscount(100, "SAVE10"), 90);
  assert.equal(applyDiscount(75, "SAVE10"), 67.5);
});
```
The second assertion (subtotal=75) will fail because 75 - 10 = 65, not 67.5.

### Fix Direction
Change line 13 from:
```javascript
return subtotal - 10;
```
to:
```javascript
return subtotal * 0.9;
```
or equivalently:
```javascript
return subtotal - (subtotal * 0.1);
```

---

## Defect 3: Path Traversal Vulnerability in Catalog Loading

### Location
`homework-4/app/baseline/src/catalogRepository.js:8-11`

### Code Snippet
```javascript
export async function loadCatalog(catalogName = "default") {
  const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
  const rawCatalog = await readFile(catalogPath, "utf8");
  return JSON.parse(rawCatalog);
}
```

### Issue
The function does not validate `catalogName` before using it in `path.join()`. This allows path traversal attacks where malicious input like `"../catalogs/default"` or `"../../etc/passwd"` can escape the intended catalog directory.

### Expected Behavior
Catalog loading must only allow simple catalog names inside `app/*/data/catalogs` (per bug-context.md:11).

### Observed Behavior
- `loadCatalog("../catalogs/default")` will attempt to read from a parent directory
- No validation prevents directory traversal sequences (`..`, `/`, etc.)
- The function trusts user input directly in filesystem operations

### Test That Fails
`homework-4/app/baseline/tests/security.test.js:5-10`
```javascript
test("rejects catalog names that escape the catalog directory", async () => {
  await assert.rejects(
    () => loadCatalog("../catalogs/default"),
    /Invalid catalog name/
  );
});
```

### Security Impact
This is a **CWE-22: Improper Limitation of a Pathname to a Restricted Directory** vulnerability. An attacker could:
- Read arbitrary files on the system
- Access sensitive configuration or data files
- Potentially escalate to other attacks depending on file permissions

### Fix Direction
Add validation before line 9 to:
1. Check that `catalogName` contains only safe characters (alphanumeric, dash, underscore)
2. Reject any input containing path traversal sequences (`..`, `/`, `\`)
3. Throw an error with message matching `/Invalid catalog name/`

Example fix:
```javascript
export async function loadCatalog(catalogName = "default") {
  // Validate catalog name to prevent path traversal
  if (!/^[a-zA-Z0-9_-]+$/.test(catalogName)) {
    throw new Error(`Invalid catalog name: ${catalogName}`);
  }
  
  const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
  const rawCatalog = await readFile(catalogPath, "utf8");
  return JSON.parse(rawCatalog);
}
```

---

## Verification Commands

From repository root:
```bash
node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js
```

From homework-4 directory:
```bash
npm run app:baseline:test
```

Both commands should fail while the defects are present, confirming the issues documented above.

---

## Research Complete

All 3 seeded defects from `homework-4/scenarios/bug-001/bug-context.md` have been identified, documented with exact file:line references, code snippets, and fix directions.

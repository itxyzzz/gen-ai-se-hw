# Codebase Research

## Defect 1: Line Total Calculation
**File**: app/baseline/src/quoteCalculator.js:4
**Snippet**: `return item.quantity + item.unitPrice;`
**Observed Behavior**: The function uses addition (`+`) instead of multiplication (`*`) to calculate line totals.
**Expected Behavior**: Line total should be `quantity * unitPrice`.
**Fix Direction**: Change the operator from `+` to `*`.

## Defect 2: Discount Application
**File**: app/baseline/src/quoteCalculator.js:13
**Snippet**: `return subtotal - 10;`
**Observed Behavior**: The function subtracts a flat 10 from the subtotal for discount code "SAVE10".
**Expected Behavior**: Discount code "SAVE10" should apply a 10 percent discount (i.e., multiply by 0.9).
**Fix Direction**: Change the calculation to apply a 10% discount: `return subtotal * 0.9;`.

## Defect 3: Path Traversal Vulnerability
**File**: app/baseline/src/catalogRepository.js:10
**Snippet**: `const catalogPath = path.join(catalogDirectory, \`${catalogName}.json\`);`
**Observed Behavior**: The function does not validate the `catalogName` input, allowing path traversal (e.g., `../../etc/passwd`).
**Expected Behavior**: Catalog loading should only allow simple catalog names inside `app/*/data/catalogs`.
**Fix Direction**: Validate `catalogName` to ensure it contains no path separators or is limited to a safe set of characters.


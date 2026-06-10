# Codebase Research Report: Bug 001

## Discovered Defects

### 1. Line Total Addition Defect
- **File & Line:** `homework-4/app/baseline/src/quoteCalculator.js:4`
- **Snippet:**
  ```javascript
  return item.quantity + item.unitPrice;
  ```
- **Observed Behavior:** The `calculateLineTotal` function adds the quantity and unit price together instead of multiplying them, which incorrectly calculates line totals.
- **Likely Fix Direction:** Change the operator from `+` to `*` to perform multiplication.
  ```javascript
  return item.quantity * item.unitPrice;
  ```

### 2. Discount Code Flat Subtraction Defect
- **File & Line:** `homework-4/app/baseline/src/quoteCalculator.js:13`
- **Snippet:**
  ```javascript
  return subtotal - 10;
  ```
- **Observed Behavior:** The `applyDiscount` function subtracts a flat amount of 10 from the subtotal when the `SAVE10` discount code is used, rather than applying a 10 percent discount.
- **Likely Fix Direction:** Apply a 10 percent reduction to the subtotal.
  ```javascript
  return subtotal * 0.9;
  ```

### 3. Catalog Path Traversal Defect
- **File & Line:** `homework-4/app/baseline/src/catalogRepository.js:9`
- **Snippet:**
  ```javascript
  const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
  ```
- **Observed Behavior:** The `loadCatalog` function directly joins the `catalogName` argument to the `catalogDirectory` path using `path.join` without validation. This allows catalog names that include relative path segments (e.g., `../`) to traverse outside of the designated directory.
- **Likely Fix Direction:** Validate that the input `catalogName` contains only simple/safe characters (e.g., alphanumeric, underscores, hyphens) to prevent directory traversal, throwing an error if it fails validation.
  ```javascript
  if (typeof catalogName !== "string" || !/^[a-zA-Z0-9_-]+$/.test(catalogName)) {
    throw new Error("Invalid catalog name");
  }
  ```

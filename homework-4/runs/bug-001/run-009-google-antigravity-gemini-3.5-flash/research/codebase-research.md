# Codebase Research Report

This report outlines the seeded defects found in the baseline quote calculator application under `homework-4/app/baseline/`.

## Seeded Defects and Analysis

### 1. Incorrect Line Total Calculation

- **Source Reference**: [quoteCalculator.js:L4](file:///c:/Work/Codex/SETU-HW/gen-ai-se-hw/homework-4/app/baseline/src/quoteCalculator.js#L4)
- **Code Snippet**:
  ```javascript
  return item.quantity + item.unitPrice;
  ```
- **Observed Behavior**: The function `calculateLineTotal` calculates the line total by adding `item.quantity` and `item.unitPrice` together, which yields incorrect mathematical results for any line item where quantity is not 1 and price is not 0.
- **Likely Fix Direction**: Modify the function to return the product of the quantity and unit price:
  ```javascript
  return item.quantity * item.unitPrice;
  ```

---

### 2. Incorrect Discount Application for SAVE10

- **Source Reference**: [quoteCalculator.js:L13](file:///c:/Work/Codex/SETU-HW/gen-ai-se-hw/homework-4/app/baseline/src/quoteCalculator.js#L13)
- **Code Snippet**:
  ```javascript
  return subtotal - 10;
  ```
- **Observed Behavior**: The function `applyDiscount` subtracts a flat value of `10` from the subtotal when the `SAVE10` discount code is used, instead of calculating and subtracting a 10% discount.
- **Likely Fix Direction**: Update the discount calculation to apply a 10% discount:
  ```javascript
  return subtotal * 0.9;
  ```

---

### 3. Path Traversal Vulnerability in Catalog Loading

- **Source Reference**: [catalogRepository.js:L9](file:///c:/Work/Codex/SETU-HW/gen-ai-se-hw/homework-4/app/baseline/src/catalogRepository.js#L9)
- **Code Snippet**:
  ```javascript
  const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
  ```
- **Observed Behavior**: The `loadCatalog` function directly joins the input parameter `catalogName` with the `catalogDirectory` path. If `catalogName` contains path traversal sequences such as `..`, it can allow reading files outside of the expected catalog directory.
- **Likely Fix Direction**: Validate and sanitize `catalogName` before constructing the path. Ensure it only contains safe characters (e.g., alphanumeric, hyphens, and underscores) and matches its basename:
  ```javascript
  if (typeof catalogName !== "string" || path.basename(catalogName) !== catalogName) {
    throw new Error("Invalid catalog name.");
  }
  ```
  Additionally, or alternatively, resolve the absolute path and verify it begins with the absolute path of `catalogDirectory`.

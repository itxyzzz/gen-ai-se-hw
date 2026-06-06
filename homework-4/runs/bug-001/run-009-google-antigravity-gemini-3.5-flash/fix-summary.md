# Fix Summary - Bug 001 (Run 009)

## Changes Made

### 1. Correct Line Total Calculation
- **File**: `homework-4/runs/bug-001/run-009-google-antigravity-gemini-3.5-flash/app/src/quoteCalculator.js`
- **Correction**: Changed `calculateLineTotal(item)` implementation to multiply `item.quantity` and `item.unitPrice` instead of adding them.

### 2. Correct SAVE10 Discount Application
- **File**: `homework-4/runs/bug-001/run-009-google-antigravity-gemini-3.5-flash/app/src/quoteCalculator.js`
- **Correction**: Changed `applyDiscount(subtotal, discountCode)` for "SAVE10" to multiply subtotal by `0.9` (applying a 10% discount) and wrapped it in `roundCurrency()` to ensure proper rounding.

### 3. Reject Unsafe Catalog Names (Path Traversal Protection)
- **File**: `homework-4/runs/bug-001/run-009-google-antigravity-gemini-3.5-flash/app/src/catalogRepository.js`
- **Correction**: Validated `catalogName` using regex `/^[a-zA-Z0-9_-]+$/`. Added path resolution checks via `path.resolve` and `path.relative` to ensure resolved paths are strictly inside `catalogDirectory`, throwing an Error with "Invalid catalog name" message if validation fails.

---

## Overall Status
- All 3 tests executed via the test runner succeeded without any failures.

---

## Manual Verification
Executed the test suite:
```bash
node --test --test-isolation=none homework-4/runs/bug-001/run-009-google-antigravity-gemini-3.5-flash/app/tests/*.test.js
```
Output:
```
✔ calculates line totals by multiplying quantity and unit price (1.5766ms)
✔ applies SAVE10 as ten percent discount (0.2355ms)
✔ rejects catalog names that escape the catalog directory (1.6244ms)
ℹ tests 3
ℹ suites 0
ℹ pass 3
ℹ fail 0
ℹ cancelled 0
ℹ skipped 0
ℹ todo 0
ℹ duration_ms 43.7168
```

---

## References
- Implementation Plan: [implementation-plan.md](file:///c:/Work/Codex/SETU-HW/gen-ai-se-hw/homework-4/runs/bug-001/run-009-google-antigravity-gemini-3.5-flash/implementation-plan.md)

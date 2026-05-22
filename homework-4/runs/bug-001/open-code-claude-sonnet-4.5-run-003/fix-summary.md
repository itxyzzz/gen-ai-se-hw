# Fix Summary

## Changes Made

### Fix 1: Corrected Line Total Calculation
**File**: `src/quoteCalculator.js`  
**Location**: Line 4  
**Type**: Arithmetic Logic Error

**Before**:
```javascript
return item.quantity + item.unitPrice;
```

**After**:
```javascript
return item.quantity * item.unitPrice;
```

**Rationale**: Line totals must equal quantity multiplied by unit price, not added. The bug caused incorrect calculations (e.g., 3 items at $25 each calculated as 28 instead of 75).

**Test Result**: ✔ PASS - Test "calculates line totals by multiplying quantity and unit price" now passes

---

### Fix 2: Corrected SAVE10 Discount Calculation
**File**: `src/quoteCalculator.js`  
**Location**: Line 13  
**Type**: Business Logic Error

**Before**:
```javascript
return subtotal - 10;
```

**After**:
```javascript
return roundCurrency(subtotal * 0.9);
```

**Rationale**: SAVE10 must apply a 10% discount, not a flat $10 deduction. The bug caused incorrect discounts for all amounts except $100 (e.g., $75 - $10 = $65 instead of $75 * 0.9 = $67.50).

**Test Result**: ✔ PASS - Test "applies SAVE10 as ten percent discount" now passes

---

### Fix 3: Prevented Path Traversal in Catalog Loading
**File**: `src/catalogRepository.js`  
**Location**: Lines 8-12 (entire function)  
**Type**: Security Vulnerability (CWE-22)

**Before**:
```javascript
export async function loadCatalog(catalogName = "default") {
  const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
  const rawCatalog = await readFile(catalogPath, "utf8");
  return JSON.parse(rawCatalog);
}
```

**After**:
```javascript
export async function loadCatalog(catalogName = "default") {
  // Validate catalog name to prevent path traversal
  const validNamePattern = /^[a-zA-Z0-9_-]+$/;
  if (!validNamePattern.test(catalogName)) {
    throw new Error("Invalid catalog name: must contain only alphanumeric characters, hyphens, and underscores");
  }

  const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
  
  // Resolve the path and ensure it's within the catalog directory
  const resolvedPath = path.resolve(catalogPath);
  const resolvedCatalogDir = path.resolve(catalogDirectory);
  
  if (!resolvedPath.startsWith(resolvedCatalogDir + path.sep) && resolvedPath !== resolvedCatalogDir) {
    throw new Error("Invalid catalog name: path traversal detected");
  }

  const rawCatalog = await readFile(catalogPath, "utf8");
  return JSON.parse(rawCatalog);
}
```

**Rationale**: The original implementation was vulnerable to path traversal attacks (CWE-22), allowing attackers to use sequences like `../../../etc/passwd` to read arbitrary files. The fix adds:
1. Regex validation to reject catalog names with path traversal characters
2. Path resolution to verify the final path stays within the catalog directory

**Test Result**: ✔ PASS - Test "rejects catalog names that escape the catalog directory" now passes

---

## Overall Status

**PASS** - All tests passing after fixes applied

### Test Results Summary

**After Fix 1 (Line Total Calculation)**:
- ✔ calculates line totals by multiplying quantity and unit price
- ✖ applies SAVE10 as ten percent discount (expected - not yet fixed)
- ✖ rejects catalog names that escape the catalog directory (expected - not yet fixed)
- **Result**: 1/3 tests passing

**After Fix 2 (Discount Calculation)**:
- ✔ calculates line totals by multiplying quantity and unit price
- ✔ applies SAVE10 as ten percent discount
- ✖ rejects catalog names that escape the catalog directory (expected - not yet fixed)
- **Result**: 2/3 tests passing

**After Fix 3 (Path Traversal Security)**:
- ✔ calculates line totals by multiplying quantity and unit price
- ✔ applies SAVE10 as ten percent discount
- ✔ rejects catalog names that escape the catalog directory
- **Result**: 3/3 tests passing ✓

---

## Manual Verification

### Final Test Execution

Command executed from `app/` directory:
```bash
node --test --test-isolation=none tests/*.test.js
```

### Final Test Output

```
✔ calculates line totals by multiplying quantity and unit price (1.068194ms)
✔ applies SAVE10 as ten percent discount (0.151999ms)
✔ rejects catalog names that escape the catalog directory (0.744096ms)
ℹ tests 3
ℹ suites 0
ℹ pass 3
ℹ fail 0
ℹ cancelled 0
ℹ skipped 0
ℹ todo 0
ℹ duration_ms 24.090871
```

**Result**: All tests pass with exit code 0

---

## Security Impact

### Vulnerability Eliminated
- **CWE-22**: Improper Limitation of a Pathname to a Restricted Directory (Path Traversal)
- **Severity**: CRITICAL
- **Status**: FIXED

### Attack Vectors Blocked
The fix prevents all of the following path traversal attempts:
- `loadCatalog("../catalogs/default")` → Error: "Invalid catalog name"
- `loadCatalog("../../package.json")` → Error: "Invalid catalog name"
- `loadCatalog("../../../etc/passwd")` → Error: "Invalid catalog name"
- `loadCatalog("./../../secret")` → Error: "Invalid catalog name"

Legitimate catalog names continue to work:
- `loadCatalog("default")` → Success
- `loadCatalog("valid-name")` → Success
- `loadCatalog("catalog_2024-05")` → Success

---

## References

- **Implementation Plan**: `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/implementation-plan.md`
- **Verified Research**: `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/research/verified-research.md`
- **Bug Context**: `homework-4/scenarios/bug-001/bug-context.md`

---

## Execution Details

- **Execution Date**: 2026-05-22
- **Agent**: Bug Fixer Agent (OpenCode Claude Sonnet 4.5)
- **Total Fixes Applied**: 3
- **Files Modified**: 2
  - `src/quoteCalculator.js` (2 fixes)
  - `src/catalogRepository.js` (1 fix)
- **Test Runs**: 3 (one after each fix)
- **Final Status**: SUCCESS - All tests passing, all defects fixed

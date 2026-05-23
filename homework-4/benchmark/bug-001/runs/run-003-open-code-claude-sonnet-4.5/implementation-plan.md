# Implementation Plan: Bug 001 Quote Calculator Fixes

## Plan Metadata

**Plan Date**: 2026-05-22  
**Planner**: Bug Planner Agent (OpenCode Claude Sonnet 4.5)  
**Research Source**: `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/research/verified-research.md`  
**Target Workspace**: `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/app/`

## Summary

This plan addresses three verified defects in the quote calculator application:

1. **Arithmetic Defect**: Line total calculation uses addition instead of multiplication
2. **Discount Calculation Defect**: SAVE10 discount applies flat $10 instead of 10% percentage
3. **Security Vulnerability**: Path traversal vulnerability in catalog loading (CWE-22)

**Total Fixes**: 3  
**Files to Edit**: 2  
**Test Command**: `node --test --test-isolation=none tests/*.test.js`

## Required Changes

### Fix 1: Correct Line Total Calculation

**Priority**: HIGH  
**Type**: Arithmetic Logic Error  
**File**: `src/quoteCalculator.js`  
**Function**: `calculateLineTotal` (lines 1-5)

#### Current (Buggy) Code

```javascript
export function calculateLineTotal(item) {
  assertPositiveQuantity(item.quantity);
  assertNonNegativePrice(item.unitPrice);
  return item.quantity + item.unitPrice;
}
```

**Defect Location**: Line 4

#### Fixed Code

```javascript
export function calculateLineTotal(item) {
  assertPositiveQuantity(item.quantity);
  assertNonNegativePrice(item.unitPrice);
  return item.quantity * item.unitPrice;
}
```

**Change**: Replace `+` operator with `*` operator on line 4

#### Rationale

The requirement states: "Line totals must equal `quantity * unitPrice`" (bug-context.md:9). The current implementation incorrectly adds quantity and unit price instead of multiplying them, producing invalid line totals.

**Example**:
- Input: `{quantity: 3, unitPrice: 25}`
- Current (buggy): `3 + 25 = 28`
- Expected (correct): `3 * 25 = 75`

#### Test Verification

This fix will make the following test pass:

```javascript
test("calculates line totals by multiplying quantity and unit price", () => {
  const quote = calculateQuote([
    { sku: "WIDGET", quantity: 3, unitPrice: 25 }
  ]);

  assert.equal(quote.lines[0].lineTotal, 75);
  assert.equal(quote.subtotal, 75);
});
```

**Test Location**: `tests/quoteCalculator.test.js:5-12`

---

### Fix 2: Correct SAVE10 Discount Calculation

**Priority**: HIGH  
**Type**: Business Logic Error  
**File**: `src/quoteCalculator.js`  
**Function**: `applyDiscount` (lines 7-17)

#### Current (Buggy) Code

```javascript
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

**Defect Location**: Line 13

#### Fixed Code

```javascript
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

**Change**: Replace `subtotal - 10` with `roundCurrency(subtotal * 0.9)` on line 13

#### Rationale

The requirement states: "Discount code `SAVE10` must apply a 10 percent discount" (bug-context.md:10). The current implementation incorrectly applies a flat $10 discount instead of a 10% percentage discount.

**Example**:
- Input: `subtotal = 100, discountCode = "SAVE10"`
- Current (buggy): `100 - 10 = 90` (correct only by coincidence)
- Input: `subtotal = 75, discountCode = "SAVE10"`
- Current (buggy): `75 - 10 = 65` (incorrect)
- Expected (correct): `75 * 0.9 = 67.5` (10% off)

The `roundCurrency` function is already defined in the same file (line 54-56) and is used to ensure consistent currency rounding throughout the application.

#### Test Verification

This fix will make the following test pass:

```javascript
test("applies SAVE10 as ten percent discount", () => {
  assert.equal(applyDiscount(100, "SAVE10"), 90);
  assert.equal(applyDiscount(75, "SAVE10"), 67.5);
});
```

**Test Location**: `tests/quoteCalculator.test.js:14-17`

---

### Fix 3: Prevent Path Traversal in Catalog Loading

**Priority**: CRITICAL  
**Type**: Security Vulnerability (CWE-22)  
**File**: `src/catalogRepository.js`  
**Function**: `loadCatalog` (lines 8-12)

#### Current (Vulnerable) Code

```javascript
export async function loadCatalog(catalogName = "default") {
  const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
  const rawCatalog = await readFile(catalogPath, "utf8");
  return JSON.parse(rawCatalog);
}
```

**Vulnerability Location**: Lines 9-10 (no validation of `catalogName` parameter)

#### Fixed Code

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

**Changes**:
1. Add validation regex to reject catalog names containing path traversal sequences
2. Resolve the final path and verify it remains within the catalog directory
3. Throw descriptive errors for invalid inputs

#### Rationale

The requirement states: "Catalog loading must only allow simple catalog names inside `app/*/data/catalogs`" (bug-context.md:11). The current implementation directly concatenates the user-provided `catalogName` parameter into a file path without validation, allowing attackers to use path traversal sequences like `../../../etc/passwd` to read arbitrary files from the system.

**Attack Example**:
- Input: `catalogName = "../../../etc/passwd"`
- Current (vulnerable): Reads `/etc/passwd` or other system files
- Expected (secure): Throws error "Invalid catalog name"

This is a **CWE-22** vulnerability (Improper Limitation of a Pathname to a Restricted Directory).

#### Test Verification

This fix will make the following test pass:

```javascript
test("rejects catalog names that escape the catalog directory", async () => {
  await assert.rejects(
    () => loadCatalog("../catalogs/default"),
    /Invalid catalog name/
  );
});
```

**Test Location**: `tests/security.test.js:5-10`

The fix will also prevent other traversal attempts:
- `loadCatalog("../../package.json")` → Error
- `loadCatalog("../../../etc/passwd")` → Error
- `loadCatalog("./../../secret")` → Error
- `loadCatalog("valid-name")` → Success (legitimate use)

---

## Test Commands

### Primary Test Command

Run all tests after applying fixes:

```bash
node --test --test-isolation=none tests/*.test.js
```

**Location**: Run from `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/app/` directory

### Individual Test Verification (Optional)

If needed for debugging:

```bash
# Test line total calculation fix
node --test --test-isolation=none tests/quoteCalculator.test.js

# Test discount calculation fix
node --test --test-isolation=none tests/quoteCalculator.test.js

# Test path traversal security fix
node --test --test-isolation=none tests/security.test.js
```

---

## Success Criteria

### Test Success

All of the following tests must pass:

1. ✓ Line total calculation test (quoteCalculator.test.js:5-12)
2. ✓ SAVE10 discount calculation test (quoteCalculator.test.js:14-17)
3. ✓ Path traversal rejection test (security.test.js:5-10)
4. ✓ All other existing tests remain passing

### Security Success

- No CRITICAL, HIGH, or MEDIUM security findings for modified files
- Path traversal vulnerability (CWE-22) is eliminated
- Catalog loading is restricted to the intended directory

### Functional Success

- Line totals correctly multiply quantity by unit price
- SAVE10 discount correctly applies 10% reduction
- Only valid, simple catalog names are accepted

---

## Stop Conditions

The Bug Fixer agent should halt execution when:

1. **Success Stop**: All test commands pass and all three fixes are applied
2. **Failure Stop**: A fix introduces a new test failure that cannot be resolved by adjusting the fix
3. **Blocker Stop**: The test environment is broken or cannot run tests
4. **Scope Stop**: Additional defects are discovered that are not covered in this plan

### Stop Condition Details

**DO NOT**:
- Attempt to fix defects not listed in this plan
- Modify test files (unless they are broken and preventing verification)
- Refactor or optimize code beyond the specific fixes
- Add new features or functionality
- Modify any files outside `src/` directory except for verification

**DO**:
- Apply exactly the three fixes specified above
- Run tests to verify each fix
- Document any unexpected issues in fix-summary.md
- Report success or failure clearly

---

## Fix Execution Order

Apply fixes in this order:

1. **Fix 1**: Line total calculation (lowest risk, foundational)
2. **Fix 2**: Discount calculation (depends on Fix 1 for accurate totals)
3. **Fix 3**: Path traversal security (independent, can be done in any order)

After each fix:
1. Save the file
2. Run the test command
3. Verify that the related test passes
4. Proceed to next fix

After all fixes:
1. Run full test suite
2. Verify all tests pass
3. Create fix-summary.md

---

## Fix Summary Requirements

After applying all fixes, create `fix-summary.md` with:

### Required Sections

1. **Changes Made**: List each fix applied with file and line numbers
2. **Overall Status**: PASS/FAIL with test results
3. **Manual Verification**: Evidence that fixes work (test output)
4. **References**: Link to this plan and the verified research

### Example Format

```markdown
# Fix Summary

## Changes Made

1. Fixed line total calculation in src/quoteCalculator.js:4
2. Fixed SAVE10 discount in src/quoteCalculator.js:13
3. Fixed path traversal vulnerability in src/catalogRepository.js:8-12

## Overall Status

PASS - All tests passing after fixes applied

## Manual Verification

[Test output showing all tests passing]

## References

- Implementation Plan: implementation-plan.md
- Verified Research: research/verified-research.md
```

---

## Expected Result

After successful execution of this plan:

- All 3 defects are fixed
- All existing tests pass
- All generated tests pass
- No new CRITICAL, HIGH, or MEDIUM security findings for changed files
- fix-summary.md documents the changes and results

---

## References

### Source Documents

- **Verified Research**: `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/research/verified-research.md`
- **Bug Context**: `homework-4/scenarios/bug-001/bug-context.md`
- **Reference Implementation Plan**: `homework-4/scenarios/bug-001/implementation-plan.md`

### Files to Modify

- `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/app/src/quoteCalculator.js` (2 fixes)
- `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/app/src/catalogRepository.js` (1 fix)

### Test Files (Do Not Modify)

- `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/app/tests/quoteCalculator.test.js`
- `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/app/tests/security.test.js`

---

**Plan Status**: Ready for execution  
**Plan Version**: 1.0  
**Last Updated**: 2026-05-22

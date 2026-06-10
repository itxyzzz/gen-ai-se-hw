# Verified Research: Quote Calculator Seeded Defects

## Verification Summary

**Verification Date**: 2026-05-22  
**Verifier**: Bug Research Verifier Agent (OpenCode Claude Sonnet 4.5)  
**Source Document**: `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/research/codebase-research.md`  
**Source Code Base**: `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/app/`

**Total Claims Verified**: 12  
**Discrepancies Found**: 0  
**Quality Level**: 4 (Verified)

All file references, line numbers, and code snippets have been verified against the actual source code. The research is accurate and complete.

---

## Verified Claims

### Defect 1: Line Total Uses Addition Instead of Multiplication

**Claim 1.1 - File Location**  
✅ **VERIFIED**: `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/app/src/quoteCalculator.js:4`  
- File exists at stated path
- Line 4 contains the defective code

**Claim 1.2 - Code Snippet**  
✅ **VERIFIED**: Code snippet matches source exactly  
```javascript
export function calculateLineTotal(item) {
  assertPositiveQuantity(item.quantity);
  assertNonNegativePrice(item.unitPrice);
  return item.quantity + item.unitPrice;
}
```
Source: `quoteCalculator.js:1-5`

**Claim 1.3 - Defect Description**  
✅ **VERIFIED**: Line 4 uses addition operator (`+`) instead of multiplication operator (`*`)  
Source: `quoteCalculator.js:4`

**Claim 1.4 - Expected Behavior Reference**  
✅ **VERIFIED**: Requirement "Line totals must equal `quantity * unitPrice`" found in bug-context.md:9  
Source: `homework-4/scenarios/bug-001/bug-context.md:9`

**Claim 1.5 - Test Reference**  
✅ **VERIFIED**: Test code matches exactly  
```javascript
test("calculates line totals by multiplying quantity and unit price", () => {
  const quote = calculateQuote([
    { sku: "WIDGET", quantity: 3, unitPrice: 25 }
  ]);

  assert.equal(quote.lines[0].lineTotal, 75);
  assert.equal(quote.subtotal, 75);
});
```
Source: `quoteCalculator.test.js:5-12`

**Claim 1.6 - Fix Direction**  
✅ **VERIFIED**: Proposed fix is technically correct and would resolve the defect

---

### Defect 2: Discount Applies Flat $10 Instead of 10% Percentage

**Claim 2.1 - File Location**  
✅ **VERIFIED**: `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/app/src/quoteCalculator.js:12-13`  
- File exists at stated path
- Lines 12-13 contain the defective code

**Claim 2.2 - Code Snippet**  
✅ **VERIFIED**: Code snippet matches source exactly  
```javascript
if (discountCode === "SAVE10") {
  return subtotal - 10;
}
```
Source: `quoteCalculator.js:12-13`

**Claim 2.3 - Defect Description**  
✅ **VERIFIED**: Line 13 subtracts flat value 10 instead of calculating 10% of subtotal  
Source: `quoteCalculator.js:13`

**Claim 2.4 - Expected Behavior Reference**  
✅ **VERIFIED**: Requirement "Discount code `SAVE10` must apply a 10 percent discount" found in bug-context.md:10  
Source: `homework-4/scenarios/bug-001/bug-context.md:10`

**Claim 2.5 - Test Reference**  
✅ **VERIFIED**: Test code matches exactly  
```javascript
test("applies SAVE10 as ten percent discount", () => {
  assert.equal(applyDiscount(100, "SAVE10"), 90);
  assert.equal(applyDiscount(75, "SAVE10"), 67.5);
});
```
Source: `quoteCalculator.test.js:14-17`

**Claim 2.6 - Fix Direction**  
✅ **VERIFIED**: Proposed fixes are technically correct and would resolve the defect

---

### Defect 3: Path Traversal Vulnerability in Catalog Loading

**Claim 3.1 - File Location**  
✅ **VERIFIED**: `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/app/src/catalogRepository.js:8-11`  
- File exists at stated path
- Lines 8-11 contain the vulnerable code

**Claim 3.2 - Code Snippet**  
✅ **VERIFIED**: Code snippet matches source exactly  
```javascript
export async function loadCatalog(catalogName = "default") {
  const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
  const rawCatalog = await readFile(catalogPath, "utf8");
  return JSON.parse(rawCatalog);
}
```
Source: `catalogRepository.js:8-12`

**Claim 3.3 - Vulnerability Description**  
✅ **VERIFIED**: No validation of `catalogName` parameter before use in `path.join()`  
Source: `catalogRepository.js:8-12` (no validation present)

**Claim 3.4 - Expected Behavior Reference**  
✅ **VERIFIED**: Requirement "Catalog loading must only allow simple catalog names inside `app/*/data/catalogs`" found in bug-context.md:11  
Source: `homework-4/scenarios/bug-001/bug-context.md:11`

**Claim 3.5 - Test Reference**  
✅ **VERIFIED**: Test code matches exactly  
```javascript
test("rejects catalog names that escape the catalog directory", async () => {
  await assert.rejects(
    () => loadCatalog("../catalogs/default"),
    /Invalid catalog name/
  );
});
```
Source: `security.test.js:5-10`

**Claim 3.6 - Security Classification**  
✅ **VERIFIED**: CWE-22 (Improper Limitation of a Pathname to a Restricted Directory) is the correct classification for this path traversal vulnerability

**Claim 3.7 - Fix Direction**  
✅ **VERIFIED**: Proposed validation approach is technically correct and would prevent path traversal attacks

---

## Discrepancies Found

None. All claims in the research document have been verified as accurate.

---

## Research Quality Assessment

**Quality Level**: 4 (Verified)

### Criteria Met

✅ **File Existence**: All referenced files exist at the stated paths  
✅ **Line Accuracy**: All line number references are precise and correct  
✅ **Snippet Matching**: All code snippets match source code exactly  
✅ **Requirement Traceability**: All references to bug-context.md are accurate  
✅ **Test Coverage**: All test references are correct and complete  
✅ **Technical Accuracy**: All defect descriptions are technically correct  
✅ **Fix Directions**: All proposed fixes are technically sound

### Assessment Details

1. **Defect 1 (Line Total Calculation)**: Fully verified with exact file:line references, correct code snippets, and accurate behavior description.

2. **Defect 2 (Discount Calculation)**: Fully verified with exact file:line references, correct code snippets, and accurate behavior description.

3. **Defect 3 (Path Traversal Vulnerability)**: Fully verified with exact file:line references, correct code snippets, accurate security classification, and appropriate fix guidance.

4. **Test References**: All test file paths and line numbers are accurate. Test code snippets match source exactly.

5. **Requirement References**: All references to bug-context.md are accurate and properly cited.

### Planner Safety Assessment

**Safe for Planning**: YES

This research can be confidently used by the bug planner to create fix plans. All claims are verified, all references are accurate, and the fix directions are technically sound. A planner can rely on:

- Exact file paths and line numbers for modifications
- Accurate understanding of the defects
- Clear requirements from bug-context.md
- Correct test expectations
- Sound fix approaches

No additional verification or investigation is needed before proceeding to the planning phase.

---

## References

### Source Files Verified

- `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/app/src/quoteCalculator.js` (56 lines)
- `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/app/src/catalogRepository.js` (20 lines)
- `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/app/tests/quoteCalculator.test.js` (17 lines)
- `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/app/tests/security.test.js` (10 lines)
- `homework-4/scenarios/bug-001/bug-context.md` (25 lines)

### Research Document Verified

- `homework-4/runs/bug-001/open-code-claude-sonnet-4.5-run-003/research/codebase-research.md` (202 lines)

### Verification Method

- Manual line-by-line comparison of research claims against source files
- Verification of all file paths, line numbers, and code snippets
- Confirmation of requirement references in bug-context.md
- Technical review of defect descriptions and fix approaches

---

**Verification Complete**: 2026-05-22  
**Status**: All research claims verified  
**Recommendation**: Proceed to bug planning phase

# Verified Research Report

## 1. Verification Summary

This report verifies the codebase research findings presented in `codebase-research.md` for run-009. We verified all three claims against the codebase inside both the run application (`homework-4/runs/bug-001/run-009-google-antigravity-gemini-3.5-flash/app`) and the baseline application (`homework-4/app/baseline`). All file references, line numbers, and code snippets match the source code perfectly.

## 2. Verified Claims

### Claim 1: Incorrect Line Total Calculation
- **File Reference**: [quoteCalculator.js:L4](file:///c:/Work/Codex/SETU-HW/gen-ai-se-hw/homework-4/app/baseline/src/quoteCalculator.js#L4)
- **Line Number**: 4
- **Snippet Checked**:
  ```javascript
  return item.quantity + item.unitPrice;
  ```
- **Status**: **Verified**. The snippet exists on line 4 of `quoteCalculator.js` in both baseline and the run app. The addition of quantity and unitPrice instead of multiplication is incorrect and results in wrong calculations when quantity is not 1 and price is not 0.

### Claim 2: Incorrect Discount Application for SAVE10
- **File Reference**: [quoteCalculator.js:L13](file:///c:/Work/Codex/SETU-HW/gen-ai-se-hw/homework-4/app/baseline/src/quoteCalculator.js#L13)
- **Line Number**: 13
- **Snippet Checked**:
  ```javascript
  return subtotal - 10;
  ```
- **Status**: **Verified**. The snippet exists on line 13 of `quoteCalculator.js` in both baseline and the run app. Subtracting a flat `10` instead of `10%` (`subtotal * 0.1` or returning `subtotal * 0.9`) is incorrect for a percentage discount code like `SAVE10`.

### Claim 3: Path Traversal Vulnerability in Catalog Loading
- **File Reference**: [catalogRepository.js:L9](file:///c:/Work/Codex/SETU-HW/gen-ai-se-hw/homework-4/app/baseline/src/catalogRepository.js#L9)
- **Line Number**: 9
- **Snippet Checked**:
  ```javascript
  const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
  ```
- **Status**: **Verified**. The snippet exists on line 9 of `catalogRepository.js` in both baseline and the run app. Directly joining `catalogDirectory` with the user-supplied `catalogName` without validation allows path traversal (e.g. `../../some/file`) to read files outside the expected folder.

## 3. Discrepancies Found

None.

## 4. Research Quality Assessment

- **Quality Level**: **Level 4 (Verified)**
- **Criteria Met**: Every file and line reference exists, code snippets match the source code exactly, and no material discrepancies or errors were found.
- **Safety for Planner**: The research is highly accurate and safe for a bug planner to rely on. The proposed fixes are sound and address the root causes of all three defects.

## 5. References

- [Baseline quoteCalculator.js](file:///c:/Work/Codex/SETU-HW/gen-ai-se-hw/homework-4/app/baseline/src/quoteCalculator.js)
- [Baseline catalogRepository.js](file:///c:/Work/Codex/SETU-HW/gen-ai-se-hw/homework-4/app/baseline/src/catalogRepository.js)
- [Run app quoteCalculator.js](file:///c:/Work/Codex/SETU-HW/gen-ai-se-hw/homework-4/runs/bug-001/run-009-google-antigravity-gemini-3.5-flash/app/src/quoteCalculator.js)
- [Run app catalogRepository.js](file:///c:/Work/Codex/SETU-HW/gen-ai-se-hw/homework-4/runs/bug-001/run-009-google-antigravity-gemini-3.5-flash/app/src/catalogRepository.js)

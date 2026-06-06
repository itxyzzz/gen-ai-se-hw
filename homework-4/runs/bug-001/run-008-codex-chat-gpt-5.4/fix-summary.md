# Changes Made

- Updated `app/src/quoteCalculator.js`:
  - `calculateLineTotal` now multiplies `quantity * unitPrice` instead of adding them.
  - `applyDiscount` now applies a `SAVE10` 10% discount via `roundCurrency(subtotal * 0.9)`.
- Updated `app/src/catalogRepository.js`:
  - Added catalog-name validation with `/^[A-Za-z0-9_-]+$/u`.
  - Added path resolution and `path.relative` containment checks so unsafe names are rejected before `readFile`.
  - Both unsafe names and names outside the catalog directory now throw `Error("Invalid catalog name.")`.

# Overall Status

- All requested defects are fixed and scoped edits completed.
- Validation completed via the required full test suite command with zero failures.

# Manual Verification

- Reviewed both edited files to confirm only the two allowed source files changed.
- Confirmed fixed behavior via tests:
  - `calculateLineTotal` now yields multiplication-based totals.
  - `SAVE10` discount now yields 10% reduction.
  - Unsafe catalog names are rejected before read.

# References

- Implemented changes in:
  - `C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\run-008-codex-chat-gpt-5.4\app\src\quoteCalculator.js`
  - `C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\run-008-codex-chat-gpt-5.4\app\src\catalogRepository.js`
- Test command executed:
  - `node --test --test-isolation=none C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\runs\bug-001\run-008-codex-chat-gpt-5.4\app\tests\*.test.js`

# Implementation Plan: Bug 001 Quote Calculator (Run: codex-chat-gpt-5.2-run-002)

## Inputs

- Verified research: `research/verified-research.md`
- Baseline app: `homework-4/app/baseline`
- Run workspace app: `homework-4/runs/bug-001/codex-chat-gpt-5.2-run-002/app`

## Test Command

Run from the run workspace app directory (`homework-4/runs/bug-001/codex-chat-gpt-5.2-run-002/app`):

```powershell
node --test --test-isolation=none tests/*.test.js
```

Stop immediately if:

- edits are needed outside `app/`, or
- test failures appear unrelated to the planned changes (record the failure in `fix-summary.md` and stop).

## Required Changes (Run Workspace Only)

### Change 1: Correct Line Total Calculation

- File: `app/src/quoteCalculator.js`
- Location: `calculateLineTotal`
- Before: `return item.quantity + item.unitPrice;`
- After: `return item.quantity * item.unitPrice;`

### Change 2: Correct SAVE10 Discount

- File: `app/src/quoteCalculator.js`
- Location: `applyDiscount`
- Before: `return subtotal - 10;`
- After: `return roundCurrency(subtotal * 0.9);`

### Change 3: Reject Unsafe Catalog Names

- File: `app/src/catalogRepository.js`
- Location: `loadCatalog`
- Before: join the catalog directory and provided name directly.
- After:
  - Validate catalog names with `^[a-zA-Z0-9_-]+$`.
  - Resolve the final path from the catalog directory.
  - Reject any resolved path that escapes the catalog directory.

## Fix Summary Requirements

Write `fix-summary.md` with:

- Changes Made
- Overall Status
- Commands Run (including exit codes)
- Manual Verification
- References

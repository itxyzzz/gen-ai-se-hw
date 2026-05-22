# Codebase Research: Bug 001

## Scope

- Scenario: `homework-4/scenarios/bug-001/bug-context.md`
- Inspected app: `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app`
- Baseline evidence command:
  `node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js`

## Findings

### 1. Line totals add quantity and unit price

- Reference:
  `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/quoteCalculator.js:4`
- Source snippet:
  `return item.quantity + item.unitPrice;`
- Observed behavior:
  `quantity=3` and `unitPrice=25` produce `28` instead of `75`.
- Test evidence:
  `homework-4/app/baseline/tests/quoteCalculator.test.js:10` expects line
  total `75`.
- Likely fix direction:
  Multiply quantity by unit price and continue using the existing currency
  rounding at the quote subtotal and total boundaries.

### 2. SAVE10 applies a flat amount instead of ten percent

- Reference:
  `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/quoteCalculator.js:12`
  and `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/quoteCalculator.js:13`
- Source snippet:
  `if (discountCode === "SAVE10") { return subtotal - 10; }`
- Observed behavior:
  `applyDiscount(75, "SAVE10")` returns `65` instead of `67.5`.
- Test evidence:
  `homework-4/app/baseline/tests/quoteCalculator.test.js:15` and
  `homework-4/app/baseline/tests/quoteCalculator.test.js:16` expect ten percent
  off.
- Likely fix direction:
  Return `subtotal * 0.9` for `SAVE10`, leaving unknown-code validation intact.

### 3. Catalog loading permits path traversal

- Reference:
  `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/catalogRepository.js:9`
  and `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/catalogRepository.js:10`
- Source snippet:
  `const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);`
- Observed behavior:
  Caller-controlled catalog names can contain path separators such as
  `../catalogs/default`, and the code reads the resulting path without
  validation.
- Test evidence:
  `homework-4/app/baseline/tests/security.test.js:6` expects traversal-style
  names to reject with `Invalid catalog name`.
- Likely fix direction:
  Accept only simple catalog names, resolve the final path, and confirm it
  stays inside `catalogDirectory` before reading.

## Suggested Next Step

Bug Planner should produce a bounded plan that edits only
`app/src/quoteCalculator.js`, `app/src/catalogRepository.js`, and app tests for
the corrected behavior.

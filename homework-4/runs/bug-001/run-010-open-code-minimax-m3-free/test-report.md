# Test Report: Bug 001 Quote Calculator

## Generated Tests
- Added `sums line totals across multiple items into the subtotal` in `app/tests/quoteCalculator.test.js` to cover the multi-line aggregation path of `calculateQuote`, verifying that per-line `lineTotal` values are computed from `quantity * unitPrice` and summed into `subtotal` and `total`.
- Added `rounds SAVE10 discount to two decimal places` in `app/tests/quoteCalculator.test.js` to cover the `applyDiscount(19.99, "SAVE10") === 17.99` rounding case introduced by the `roundCurrency(subtotal * 0.9)` change.
- Added `rejects catalog names that fail the allow-list` in `app/tests/security.test.js` to cover additional unsafe shapes (`""`, `"default.json"`, `"foo bar"`, `"/etc/passwd"`) that the new `^[a-zA-Z0-9_-]+$` allow-list must reject.
- Added `loads a valid catalog by a safe allow-list name` in `app/tests/security.test.js` to confirm the hardened `loadCatalog` still loads a known-good catalog (`"default"`) and returns the expected JSON shape.

## FIRST Assessment
- Fast: All seven tests complete in under 50 ms total using pure in-process `node:test` runs with no network or external services.
- Independent: Each test sets up its own inputs and either calls a pure function or reads a static catalog file; no shared state is mutated across tests.
- Repeatable: Tests rely only on the in-repo `app/data/catalogs/default.json` fixture and deterministic arithmetic, so they produce the same result on every machine without hidden state or timing.
- Self-validating: Every test uses `assert.equal`, `assert.ok`, or `assert.rejects` with explicit expected values and regex matchers; pass/fail is decided automatically with no manual inspection.
- Timely: The tests were added together with the fix, directly target the three changed behaviors documented in `fix-summary.md`, and do not modify the seed tests.

## Commands Run
```powershell
node --test --test-isolation=none tests/*.test.js
```
- Working directory: `homework-4/runs/bug-001/run-010-open-code-minimax-m3-free/app`

## Results
- Result: passed
- Tests: 7 total
- Passing: 7
- Failing: 0

## Remaining Gaps
The new tests cover the three fixed behaviors (multi-line subtotal aggregation, SAVE10 percentage discount with rounding, and the catalog allow-list plus a happy-path load) but do not exhaustively probe surrounding behavior. Specifically, there is no coverage for: rejection of unknown discount codes via `applyDiscount`, validation errors raised by `assertPositiveQuantity` / `assertNonNegativePrice` for malformed line items, the `options.currency` override path in `calculateQuote`, `findCatalogItem` for unknown SKUs, and a directory-escape attempt via a name that satisfies the allow-list character class but resolves outside `catalogDirectory`. These remain candidates for future test expansion but are outside the scope of the changed code in this fix.

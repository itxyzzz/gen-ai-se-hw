# Codebase Research

## Verification Summary

- Read the stage brief in `agents/bug-researcher.agent.md` and the scenario in `scenarios/bug-001/bug-context.md`.
- Inspected only scenario files plus baseline source/tests under `app/baseline`.
- Ran `node --test --test-isolation=none C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-4\app\baseline\tests\*.test.js`; all three baseline tests failed, which matches the seeded-defect scenario.

## Verified Claims

### 1. Line totals are calculated with addition instead of multiplication

- File: `app/baseline/src/quoteCalculator.js:4`
- Snippet: `return item.quantity + item.unitPrice;`
- Observed behavior: `app/baseline/tests/quoteCalculator.test.js:5` through `app/baseline/tests/quoteCalculator.test.js:12` expect a quote line with `quantity: 3` and `unitPrice: 25` to produce `lineTotal = 75` and `subtotal = 75`. The current implementation returns `28`, and `app/baseline/src/quoteCalculator.js:31` then rolls that incorrect line total into the subtotal.
- Likely fix direction: change the line-total calculation to multiply `quantity * unitPrice` while keeping the existing quantity/price validation and downstream rounding flow intact.

### 2. `SAVE10` is implemented as a flat 10-unit subtraction instead of a 10 percent discount

- File: `app/baseline/src/quoteCalculator.js:12`
- Snippet: `if (discountCode === "SAVE10") {`
- File: `app/baseline/src/quoteCalculator.js:13`
- Snippet: `return subtotal - 10;`
- Observed behavior: `app/baseline/tests/quoteCalculator.test.js:14` through `app/baseline/tests/quoteCalculator.test.js:17` expect `applyDiscount(100, "SAVE10")` to return `90` and `applyDiscount(75, "SAVE10")` to return `67.5`. The current helper returns `65` for the second case because it subtracts a flat `10`, and `app/baseline/src/quoteCalculator.js:32` uses that helper to compute the final quote total.
- Likely fix direction: implement `SAVE10` as a percentage-based discount, such as subtracting `subtotal * 0.1` or multiplying by `0.9`, then keep the existing quote-level currency rounding at the caller.

### 3. Catalog loading accepts traversal-style catalog names instead of rejecting them

- File: `app/baseline/src/catalogRepository.js:8`
- Snippet: `export async function loadCatalog(catalogName = "default") {`
- File: `app/baseline/src/catalogRepository.js:9`
- Snippet: `const catalogPath = path.join(catalogDirectory, \`${catalogName}.json\`);`
- Observed behavior: `app/baseline/tests/security.test.js:5` through `app/baseline/tests/security.test.js:9` expect `loadCatalog("../catalogs/default")` to reject with `/Invalid catalog name/`. The current implementation passes the raw catalog name into `path.join(...)` and proceeds to `readFile(...)` at `app/baseline/src/catalogRepository.js:10`, so the test fails with a missing rejection instead of blocking traversal-style input.
- Likely fix direction: validate `catalogName` before path construction so only simple catalog identifiers are allowed, rejecting values that contain path separators, traversal segments, or other non-name characters. As defense in depth, resolve the final path and verify it remains under `catalogDirectory`.

## Discrepancies

- None observed. The scenario description in `scenarios/bug-001/bug-context.md` matches the current baseline failures and source defects.

## Quality Assessment

- The completion gate is satisfied by this artifact as written: the file exists, and each material claim is anchored to baseline source/test file references with a concrete fix direction.

## References

- `agents/bug-researcher.agent.md`
- `scenarios/bug-001/bug-context.md`
- `app/baseline/src/quoteCalculator.js`
- `app/baseline/src/catalogRepository.js`
- `app/baseline/tests/quoteCalculator.test.js`
- `app/baseline/tests/security.test.js`

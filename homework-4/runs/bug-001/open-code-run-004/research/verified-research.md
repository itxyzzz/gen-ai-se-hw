# Verified Research: Quote Calculator Bugs

## Verification Summary

**Pass/Fail:** Pass (with minor line drift)
**Research Quality:** Mostly Verified (Level 3)

## Verified Claims

1. **`src/quoteCalculator.js:4`** — Returns `item.quantity + item.unitPrice` instead of multiplication.
   - Verified: source at line 4 reads `return item.quantity + item.unitPrice;`
   - Expected: `return item.quantity * item.unitPrice;`
   - Status: Correct

2. **`src/quoteCalculator.js:12`** — Returns `subtotal - 10` for `SAVE10`.
   - Verified: source at lines 12-13 reads `return subtotal - 10;`
   - Expected: 10 percent discount (e.g., `return roundCurrency(subtotal * 0.9);`)
   - Status: Correct

3. **`src/catalogRepository.js:8`** — Constructs catalog path without validation.
   - Slight line drift: the claim cites line 8 (`export async function loadCatalog...`), but `path.join` is at line 9.
   - Verified: source at line 9 reads `const catalogPath = path.join(catalogDirectory, \`${catalogName}.json\`);` with no validation.
   - Status: Correct (line drift noted, material claim holds)

4. **`tests/quoteCalculator.test.js:5`** — Test description expects multiplication.
   - Verified: line 5 reads `test("calculates line totals by multiplying quantity and unit price", ...)`
   - Status: Correct

5. **`tests/security.test.js:5`** — Test description expects traversal rejection.
   - Verified: line 5 reads `test("rejects catalog names that escape the catalog directory", ...)`
   - Status: Correct

## Discrepancies Found

| Claim Reference | Issue | Severity |
|---|---|---|
| `catalogRepository.js:8` | Line should be 9 (`path.join` is on line 9, not the function declaration on line 8) | Minor (line drift) |

## Research Quality Assessment

**Level:** 3 — Mostly Verified

**Reasoning:** All material claims are correct. Every referenced file exists. Snippets match source behavior. One minor line drift was found (claim 3 cites line 8 instead of line 9), but the core observation is accurate. The suggested fix directions align with the actual defects. A bug planner can safely rely on this research.

## References

- `src/quoteCalculator.js:4` — addition instead of multiplication
- `src/quoteCalculator.js:12-13` — flat subtraction instead of percentage discount
- `src/catalogRepository.js:9` — unsafe path construction
- `tests/quoteCalculator.test.js:5` — expected multiplication behavior
- `tests/security.test.js:5` — expected traversal rejection
- `scenarios/bug-001/bug-context.md` — seeded defect documentation

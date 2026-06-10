# Verified Research: Bug 001 Quote Calculator

## Verification Summary

- Status: pass.
- Research Quality: Level 4, Verified.
- Scope checked: baseline source, baseline tests, scenario context, and the
  isolated run workspace before fixes.
- Planner readiness: the research is safe for the Bug Planner to use.

## Verified Claims

- `homework-4/app/baseline/src/quoteCalculator.js:1` defines
  `calculateLineTotal(item)`, and line `4` returns
  `item.quantity + item.unitPrice`; this matches the research claim that line
  totals are added instead of multiplied.
- `homework-4/app/baseline/tests/quoteCalculator.test.js:5` through line `12`
  assert that quantity `3` and unit price `25` should produce a line total and
  subtotal of `75`.
- `homework-4/app/baseline/src/quoteCalculator.js:12` checks for `SAVE10`, and
  line `13` returns `subtotal - 10`; this matches the research claim that the
  code applies a flat discount.
- `homework-4/app/baseline/tests/quoteCalculator.test.js:14` through line `17`
  assert that `SAVE10` should produce `90` for subtotal `100` and `67.5` for
  subtotal `75`.
- `homework-4/app/baseline/src/catalogRepository.js:8` defines
  `loadCatalog(catalogName = "default")`, and line `9` joins the provided name
  directly into a catalog path; this matches the research claim that traversal
  input is not rejected before path construction.
- `homework-4/app/baseline/tests/security.test.js:5` through line `9` assert
  that `loadCatalog("../catalogs/default")` must reject with
  `Invalid catalog name`.
- The pre-fix command
  `node --test --test-isolation=none tests/*.test.js` failed in the run
  workspace with three failing tests: line-total calculation, percentage
  discount behavior, and unsafe catalog-name rejection.

## Discrepancies Found

No discrepancies found. All referenced files existed, all cited snippets
matched the baseline source, and the observed test failures matched the stated
defects.

## Research Quality Assessment

Level 4, Verified. The research cites exact file and line references, the
snippets match source, and the test evidence confirms each described defect.
There is enough concrete evidence for the Bug Planner to produce a bounded
implementation plan.

## References

- `homework-4/scenarios/bug-001/bug-context.md`
- `homework-4/app/baseline/src/quoteCalculator.js:1`
- `homework-4/app/baseline/src/quoteCalculator.js:4`
- `homework-4/app/baseline/src/quoteCalculator.js:12`
- `homework-4/app/baseline/src/quoteCalculator.js:13`
- `homework-4/app/baseline/src/catalogRepository.js:8`
- `homework-4/app/baseline/src/catalogRepository.js:9`
- `homework-4/app/baseline/tests/quoteCalculator.test.js:5`
- `homework-4/app/baseline/tests/quoteCalculator.test.js:14`
- `homework-4/app/baseline/tests/security.test.js:5`

# Verified Research: Bug 001

## Verification Summary

- Result: PASS
- Research Quality: Level 4 - Verified
- Skill used: `homework-4/skills/research-quality-measurement.md`
- Planner readiness: Safe for Bug Planner to use.

## Verified Claims

1. `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/quoteCalculator.js:4`
   contains `return item.quantity + item.unitPrice;`. This matches the research
   claim and explains the failing line-total assertion.
2. `homework-4/app/baseline/tests/quoteCalculator.test.js:10` expects the line
   total to be `75` for quantity `3` and unit price `25`. Baseline test output
   showed the actual value was `28`.
3. `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/quoteCalculator.js:12`
   and `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/quoteCalculator.js:13`
   contain the `SAVE10` branch and `return subtotal - 10;`. This matches the
   flat-discount defect.
4. `homework-4/app/baseline/tests/quoteCalculator.test.js:15` and
   `homework-4/app/baseline/tests/quoteCalculator.test.js:16` expect `SAVE10`
   to produce `90` from `100` and `67.5` from `75`. Baseline test output showed
   the second assertion failed with actual `65`.
5. `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/catalogRepository.js:9`
   joins caller-controlled `catalogName` into the catalog path, and
   `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/catalogRepository.js:10`
   reads that path without validation. This matches the path traversal claim.
6. `homework-4/app/baseline/tests/security.test.js:6` through
   `homework-4/app/baseline/tests/security.test.js:8` expect
   `loadCatalog("../catalogs/default")` to reject with `Invalid catalog name`.
   Baseline test output showed the rejection was missing.

## Discrepancies Found

No discrepancies found. File paths, line references, snippets, and observed
test failures match the inspected source and test evidence.

## Research Quality Assessment

Level 4 - Verified. Every referenced file exists, the cited line numbers match
the source snippets, and each defect is independently supported by a failing
baseline assertion. The research is specific enough for a planner to produce a
controlled edit plan.

## References

- `homework-4/skills/research-quality-measurement.md`
- `homework-4/scenarios/bug-001/bug-context.md`
- `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/quoteCalculator.js:4`
- `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/quoteCalculator.js:12`
- `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/quoteCalculator.js:13`
- `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/catalogRepository.js:9`
- `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/catalogRepository.js:10`
- `homework-4/app/baseline/tests/quoteCalculator.test.js:10`
- `homework-4/app/baseline/tests/quoteCalculator.test.js:15`
- `homework-4/app/baseline/tests/quoteCalculator.test.js:16`
- `homework-4/app/baseline/tests/security.test.js:6`

# Test Report: Bug 001 Quote Calculator

## Generated Tests

- Added `sums multiplied line totals across quote items` in
  `app/tests/quoteCalculator.test.js` to cover multiplied line totals across
  more than one item.
- Added `rounds SAVE10 percentage discounts to currency precision` in
  `app/tests/quoteCalculator.test.js` to cover percentage discount rounding.
- Added `rejects absolute catalog paths` in `app/tests/security.test.js` to
  cover another unsafe catalog-name shape.
- Added `loads allowlisted catalog names` in `app/tests/security.test.js` to
  confirm valid catalog names still work after validation.

## FIRST Assessment

- Fast: the full run workspace suite completes in under one second of reported
  Node test duration.
- Independent: each test constructs its own inputs and does not depend on test
  order.
- Repeatable: tests use local fixtures under `app/data/catalogs` and no network
  or external services.
- Self-validating: all tests use `node:assert/strict` assertions.
- Timely: tests were generated immediately after the Bug Fixer and Security
  Verifier stages for the changed behavior only.

## Commands Run

```powershell
node --test --test-isolation=none tests/*.test.js
```

- Working directory:
  `homework-4/runs/bug-001/run-007-codex-chat-gpt-5.4/app`

## Results

- Result: passed.
- Tests: `7`
- Passing: `7`
- Failing: `0`

## Remaining Gaps

No remaining unit-test gaps for the seeded defects. Broader integration or CLI
coverage is outside this run's changed-code scope.

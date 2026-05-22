# Test Report: Bug 001

## Generated Tests

Generated
`homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/tests/generated-regression.test.js`
with four tests:

1. quote totals use multiplication before discount;
2. `SAVE10` scales with the subtotal;
3. valid `default` catalog loading still works;
4. traversal and non-simple catalog names reject.

## FIRST Assessment

- Fast: Uses Node's built-in test runner and local fixture data only.
- Independent: Each test creates or loads its own data and does not rely on
  test order.
- Repeatable: No network, clock, random, or external service dependency.
- Self-validating: Assertions check exact totals, item data, and expected
  rejection messages.
- Timely: Tests cover the same behaviors changed by the Bug Fixer stage.

## Commands Run

```powershell
node --test --test-isolation=none homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/tests/*.test.js
```

## Results

PASS: 7 tests passed, 0 failed.

## Remaining Gaps

No required regression gaps remain for the changed behavior. Broader catalog
schema validation remains outside the scope of this bug fix.

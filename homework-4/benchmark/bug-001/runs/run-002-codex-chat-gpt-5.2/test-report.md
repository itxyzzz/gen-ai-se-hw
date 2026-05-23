# Test Report

## Generated Tests

- Added `app/tests/generated-regression.test.js` covering:
  - corrected quote totals (`quantity * unitPrice`) combined with `SAVE10` percentage discount
  - catalog name traversal rejection (`../...`)

## FIRST Assessment

- Fast: uses built-in `node:test` with local files only.
- Independent: each test constructs its own inputs and does not depend on ordering.
- Repeatable: no network, clock, or shared external state.
- Self-validating: assertions check exact totals and explicit rejection behavior.
- Timely: tests target behavior changed in this run (`fix-summary.md`).

## Commands Run

From `homework-4/runs/bug-001/codex-chat-gpt-5.2-run-002/app`:

```powershell
node --test --test-isolation=none tests/*.test.js
```

## Results

- Exit code: 0
- Summary: 5 passing tests

## Remaining Gaps

- No remaining gaps for the seeded defects in `scenarios/bug-001/bug-context.md`.

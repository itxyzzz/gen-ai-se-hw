# Implementation Plan: Bug 001

## Inputs Read

- `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/research/verified-research.md`
- `homework-4/scenarios/bug-001/implementation-plan.md`
- `homework-4/scenarios/bug-001/bug-context.md`

## Files To Change

1. `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/quoteCalculator.js`
2. `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/src/catalogRepository.js`
3. `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/tests/generated-regression.test.js`

## Planned Source Changes

### quoteCalculator.js

- Before:
  `return item.quantity + item.unitPrice;`
- After:
  `return item.quantity * item.unitPrice;`
- Expected behavior:
  Each line total equals `quantity * unitPrice`.

- Before:
  `return subtotal - 10;`
- After:
  `return subtotal * 0.9;`
- Expected behavior:
  `SAVE10` applies ten percent off to the subtotal.

### catalogRepository.js

- Before:
  `path.join(catalogDirectory, `${catalogName}.json`)` accepts path separators
  and reads the resulting file.
- After:
  Validate catalog names with a simple-name allowlist, resolve the final path,
  verify it remains inside `catalogDirectory`, then read it.
- Expected behavior:
  `loadCatalog("../catalogs/default")` rejects with `Invalid catalog name`, and
  `loadCatalog("default")` still works.

### generated-regression.test.js

- Add focused regression tests for changed behavior only:
  line-total multiplication, percentage discount, valid catalog loading, and
  traversal rejection.
- Keep tests fast, independent, repeatable, self-validating, and timely.

## Test Commands

Run after source fixes:

```powershell
node --test --test-isolation=none homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app/tests/*.test.js
```

Run after promotion:

```powershell
node --test --test-isolation=none homework-4/app/current/tests/*.test.js
node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js
```

The baseline command is expected to fail with the seeded defects.

## Stop Conditions

- Stop if any edit would affect files outside
  `homework-4/runs/bug-001/codex-chat-gpt-5.4-run-002/app`.
- Stop if run-app tests fail after the fixes and generated regressions.
- Stop if security review finds unresolved CRITICAL, HIGH, or MEDIUM issues.

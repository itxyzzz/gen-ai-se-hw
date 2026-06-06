# Implementation Plan: Bug 001 Quote Calculator

## Inputs

- Verified research:
  `homework-4/runs/bug-001/run-007-codex-chat-gpt-5.4/research/verified-research.md`
- Scenario plan: `homework-4/scenarios/bug-001/implementation-plan.md`
- Run workspace app:
  `homework-4/runs/bug-001/run-007-codex-chat-gpt-5.4/app`

## Files To Change

### 1. Correct line total calculation

- File: `app/src/quoteCalculator.js`
- Function: `calculateLineTotal`
- Before:

```js
return item.quantity + item.unitPrice;
```

- After:

```js
return item.quantity * item.unitPrice;
```

- Intended behavior: line totals equal `quantity * unitPrice`.
- Test command after change:

```powershell
node --test --test-isolation=none tests/*.test.js
```

### 2. Correct SAVE10 discount behavior

- File: `app/src/quoteCalculator.js`
- Function: `applyDiscount`
- Before:

```js
return subtotal - 10;
```

- After:

```js
return roundCurrency(subtotal * 0.9);
```

- Intended behavior: `SAVE10` applies a ten percent discount and preserves
  currency rounding.
- Test command after change:

```powershell
node --test --test-isolation=none tests/*.test.js
```

### 3. Reject unsafe catalog names

- File: `app/src/catalogRepository.js`
- Function: `loadCatalog`
- Before:

```js
const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
```

- After:

```js
if (!/^[a-zA-Z0-9_-]+$/.test(catalogName)) {
  throw new Error("Invalid catalog name.");
}

const catalogPath = path.resolve(catalogDirectory, `${catalogName}.json`);
const relativePath = path.relative(catalogDirectory, catalogPath);
if (relativePath.startsWith("..") || path.isAbsolute(relativePath)) {
  throw new Error("Invalid catalog name.");
}
```

- Intended behavior: only simple catalog identifiers are allowed, and resolved
  paths must remain inside the catalog directory.
- Test command after change:

```powershell
node --test --test-isolation=none tests/*.test.js
```

## Stop Conditions

- Stop if edits are required outside
  `homework-4/runs/bug-001/run-007-codex-chat-gpt-5.4/app`.
- Stop if the test command still fails after the three planned changes.
- Stop if security review finds unresolved CRITICAL, HIGH, or MEDIUM issues.

## Expected Result

All run workspace tests pass, security review reports no unresolved CRITICAL,
HIGH, or MEDIUM findings, and generated or updated tests remain scoped to the
changed behavior.

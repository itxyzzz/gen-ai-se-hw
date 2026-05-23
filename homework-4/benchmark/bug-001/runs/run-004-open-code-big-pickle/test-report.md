# Test Report: Bug 001 Quote Calculator (Run: open-code-run-004)

## Generated Tests

New file: `tests/fix.test.js` — 12 tests added for changed code:

| Test | Coverage |
|---|---|
| `calculateLineTotal multiplies quantity by unit price` | Line total fix |
| `calculateLineTotal rejects zero quantity` | Input validation (quantity boundary) |
| `calculateLineTotal rejects negative quantity` | Input validation (negative) |
| `calculateLineTotal rejects non-integer quantity` | Input validation (type) |
| `calculateLineTotal rejects negative price` | Input validation (price boundary) |
| `applyDiscount returns subtotal when no discount code` | Discount code missing |
| `applyDiscount throws for unknown discount code` | Discount code rejection |
| `calculateQuote with multiple items` | Multi-item calculation |
| `calculateQuote with discount and multiple items` | Discount + multi-item |
| `calculateQuote rejects empty items` | Empty items validation |
| `loadCatalog rejects invalid catalog names` | Path traversal prevention |
| `loadCatalog loads default catalog` | Happy path catalog load |

## FIRST Assessment

| Criterion | Assessment |
|---|---|
| **Fast** | All 15 tests complete in ~43ms. No external services. PASS |
| **Independent** | Each test creates its own inputs. No shared state. PASS |
| **Repeatable** | All tests produce deterministic output (no randomness, no dates). PASS |
| **Self-Validating** | Every test has explicit `assert` or `assert.rejects` calls. PASS |
| **Timely** | Tests written for all three changed behaviors (line total, discount, catalog validation). PASS |

## Commands Run

```bash
node --test --test-isolation=none tests/*.test.js
```

## Results

| Metric | Value |
|---|---|
| Total tests | 15 |
| Passed | 15 |
| Failed | 0 |
| Duration | ~43ms |

## Remaining Gaps

- CLI integration tests are not included (would require spawning a subprocess)
- No security regression tests beyond the path traversal check
- No stress/performance tests (not applicable for this scope)

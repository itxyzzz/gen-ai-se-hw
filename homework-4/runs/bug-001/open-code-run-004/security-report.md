# Security Report: Bug 001 Quote Calculator (Run: open-code-run-004)

## Scope

Review of changed files in `runs/bug-001/open-code-run-004/app/src/`:

- `src/quoteCalculator.js` (lines 4, 12-13 changed)
- `src/catalogRepository.js` (lines 9-14 added)

## Findings

### Finding 1: Path Traversal (FIXED in changed code)

| Field | Value |
|---|---|
| **Severity** | HIGH |
| **File** | `src/catalogRepository.js` |
| **Baseline issue** | `loadCatalog` used `path.join(catalogDirectory, \`${catalogName}.json\`)` without validating the catalog name, allowing directory traversal via `../` sequences. |
| **Fix applied** | Added regex validation `^[a-zA-Z0-9_-]+$` and `path.resolve` + `startsWith` boundary check. |
| **Status** | RESOLVED — The validation prevents `..`, `/`, `\` and other traversal characters. |
| **Remediation** | Keep the regex and path boundary check. No further action needed. |

### Finding 2: Missing Input Validation on CLI Arguments

| Field | Value |
|---|---|
| **Severity** | INFO |
| **File** | `src/cli.js` (unchanged — noted for awareness) |
| **Issue** | The `--catalog` flag passes user-provided values directly to `loadCatalog`. The fixed `loadCatalog` now validates catalog names, so this is no longer exploitable. |
| **Status** | MITIGATED by the `catalogRepository.js` fix. |

### Finding 3: Discount Code Injection (Not Applicable)

| Field | Value |
|---|---|
| **Severity** | NONE |
| **File** | `src/quoteCalculator.js` |
| **Assessment** | Discount codes are compared with a strict equality check (`=== "SAVE10"`). Unknown codes throw an error. No injection vector exists. |

### Finding 4: Quantity/Price Validation

| Field | Value |
|---|---|
| **Severity** | NONE |
| **File** | `src/quoteCalculator.js` |
| **Assessment** | `assertPositiveQuantity` validates integer > 0. `assertNonNegativePrice` validates non-negative number. Validation is adequate for the CLI use case. |

### Finding 5: Hardcoded Secrets

| Field | Value |
|---|---|
| **Severity** | NONE |
| **Assessment** | No hardcoded credentials, API keys, tokens, or secrets found in changed files. |

### Finding 6: Unsafe Dependencies

| Field | Value |
|---|---|
| **Severity** | NONE |
| **Assessment** | The app has no external dependencies (only Node.js built-ins: `fs/promises`, `path`, `url`). No `npm audit` scope. |

## Gate Status

| Check | Status |
|---|---|
| CRITICAL findings | 0 (none) |
| HIGH findings | 0 unresolved (1 fixed) |
| MEDIUM findings | 0 (none) |
| LOW findings | 0 (none) |
| INFO findings | 1 (documented above, no action needed) |

**Gate: PASS** — No unresolved CRITICAL, HIGH, or MEDIUM findings in changed code.

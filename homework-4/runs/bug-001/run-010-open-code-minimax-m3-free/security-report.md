# Security Report: Bug 001 Quote Calculator

## Scope
- Reviewed files:
  - `homework-4/runs/bug-001/run-010-open-code-minimax-m3-free/app/src/quoteCalculator.js`
  - `homework-4/runs/bug-001/run-010-open-code-minimax-m3-free/app/src/catalogRepository.js`
- Inputs read:
  - `homework-4/runs/bug-001/run-010-open-code-minimax-m3-free/fix-summary.md`
  - `homework-4/agents/security-verifier.agent.md`
- Method: static review of changed code only. No execution, no dependency audit beyond what is observable in the source files.

## Findings

### Catalog-name validation correctly blocks path traversal
- Severity: INFO
- File: `app/src/catalogRepository.js:9-16`
- Impact: The fix introduces a two-layer defense against path traversal in `loadCatalog`:
  1. A strict allow-list regex `^[a-zA-Z0-9_-]+$` rejects any input containing `..`, `/`, `\`, `.`, NUL, whitespace, or the empty string. The regex is properly anchored at both ends and the `-` inside the character class is positioned at the end, so it is treated as a literal rather than a range delimiter.
  2. A resolved-path containment check (`resolvedPath.startsWith(catalogDirectory + path.sep)`) provides defense in depth even if the regex were ever loosened. Concatenating `path.sep` prevents a prefix-match bypass against a sibling directory whose name starts with the catalog directory name.
  Together these reject the originally seeded attack input `../../../../etc/passwd` and any other traversal payload before reaching the filesystem.
- Remediation: None required. This is the most security-sensitive change in the diff and it is implemented correctly. Consider documenting the intentional dual-layer design in a code comment if the project's style permits comments; the repository convention here forbids non-essential comments, so this is optional.

### User-supplied values reflected in error messages
- Severity: LOW
- File: `app/src/catalogRepository.js:10,15` and `app/src/quoteCalculator.js:16`
- Impact: `loadCatalog` and `applyDiscount` echo the user-supplied `catalogName` and `discountCode` back inside thrown error messages via template literals. For the current CLI/test harness usage this is benign. If these errors are ever surfaced into a server log, a structured logging pipeline that auto-indexes message text, or returned verbatim to an HTTP client, the reflected values could enable log forging (newline injection), log-search pollution, or minor information disclosure of attacker-controlled input.
- Remediation: Optional. If the calculator is later embedded in a network service, sanitise or truncate the reflected value (e.g. show only a fixed-length, alphanumeric-only excerpt) or attach the offending value to a structured `cause`/`details` field rather than the human-readable message.

### No injection risk in `JSON.parse` path
- Severity: INFO
- File: `app/src/catalogRepository.js:17-18`
- Impact: `JSON.parse` is called on file contents loaded only after the catalog name has passed the allow-list regex and the resolved-path containment check. The reviver argument is not used, so prototype pollution via `__proto__` keys inside catalog JSON is not introduced by `JSON.parse` itself (Node's parser ignores `__proto__` for own-property assignment during parse). The downstream consumer in `findCatalogItem` performs a strict-equality SKU lookup and does not iterate via `for...in`, which avoids reading polluted prototype chains. No action needed for the current diff.
- Remediation: None required.

### Strict equality used throughout
- Severity: INFO
- File: `app/src/quoteCalculator.js:12,22`, `app/src/catalogRepository.js:22`
- Impact: Discount-code comparison (`discountCode === "SAVE10"`) and SKU lookup (`candidate.sku === sku`) both use strict equality. There are no `==`, `!=`, or other coercion-prone comparisons in the changed code.
- Remediation: None required.

### Numeric inputs validated before arithmetic
- Severity: INFO
- File: `app/src/quoteCalculator.js:42-52`
- Impact: `calculateLineTotal` rejects non-integer or non-positive quantities and non-numeric, `NaN`, or negative prices before multiplying. This prevents `NaN` propagation, integer-vs-float surprises, and trivial denial-of-quote inputs from poisoning a downstream subtotal. `applyDiscount` rejects unknown discount codes with a thrown error rather than silently no-oping, which is appropriate.
- Remediation: None required. (Out of scope: `calculateQuote` does not validate `options.currency` shape, but the fix did not introduce that surface and it is not exploitable in the current CLI context.)

## Risk Checklist
- Injection: Pass. Path traversal in `loadCatalog` is blocked by allow-list regex and resolved-path containment. No SQL, shell, or template-string injection sinks present.
- Hardcoded secrets: Pass. No credentials, tokens, keys, or connection strings in either file.
- Insecure comparisons: Pass. Strict equality (`===`) used in all changed comparisons.
- Missing validation: Pass. Quantity, unit price, catalog name, and items-array shape are validated before use.
- Unsafe dependencies: Pass. Only Node.js built-ins (`node:fs/promises`, `node:path`, `node:url`) are imported. No third-party packages introduced by the diff.
- XSS/CSRF: N/A. This is a Node-side calculator/catalog-loader module with no HTML rendering, no DOM sink, and no HTTP entry point in the changed surface.
- Data handling: Pass with one LOW note. Catalog files are read read-only from a fixed directory after validation; user input is reflected in error messages, which is low-risk for the current CLI usage (see LOW finding above).

## Final Gate Status
Passed.

No CRITICAL, HIGH, or MEDIUM findings. The most security-sensitive change in this run is the new `loadCatalog` validation, and it is implemented correctly with both a strict allow-list regex and a resolved-path containment guard. The remaining observations are a single LOW item (user input reflected in error messages, advisory only) and four INFO items. The run can be promoted from a security perspective.

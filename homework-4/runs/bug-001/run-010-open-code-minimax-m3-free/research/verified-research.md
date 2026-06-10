# Verified Research: Bug 001 Quote Calculator

## Verification Summary
- Status: pass
- Research Quality: Level 4, Verified
- Scope checked: defect 1 (line total addition), defect 2 (SAVE10 flat subtraction), defect 3 (catalog path traversal), all test evidence snippets, the `calculateQuote` consumer, the CLI argument handling, the `catalogDirectory` origin, and the default catalog format.
- Planner readiness: the Bug Planner can use this research safely. Every file:line reference resolves to the source shown, all code snippets match the source verbatim, and the described observed/expected behavior is consistent with both the source and the test files.

## Verified Claims
- Defect 1 location `homework-4/app/baseline/src/quoteCalculator.js:4`: file exists, line 4 reads `return item.quantity + item.unitPrice;`, snippet matches: yes, behavior consistent: yes (3 + 25 = 28, not 75). Note: surrounding function shown in the snippet matches lines 1-5 verbatim.
- Defect 1 test reference `homework-4/app/baseline/tests/quoteCalculator.test.js:10`: file exists, line 10 reads `assert.equal(quote.lines[0].lineTotal, 75);` and line 11 reads `assert.equal(quote.subtotal, 75);`, snippet matches: yes, behavior consistent: yes.
- Defect 2 location `homework-4/app/baseline/src/quoteCalculator.js:13`: file exists, line 13 reads `return subtotal - 10;`, snippet matches: yes, behavior consistent: yes (75 - 10 = 65, not 67.5). The guard on line 12 (`if (discountCode === "SAVE10") {`) and the closing brace on line 14 match the snippet shown.
- Defect 2 test reference `homework-4/app/baseline/tests/quoteCalculator.test.js:16`: file exists, line 16 reads `assert.equal(applyDiscount(75, "SAVE10"), 67.5);`, snippet matches: yes, behavior consistent: yes. The companion assertion on line 15 (`assert.equal(applyDiscount(100, "SAVE10"), 90);`) also matches the research's note that `100` is the only subtotal where the buggy and correct implementations coincide.
- Defect 3 location `homework-4/app/baseline/src/catalogRepository.js:8-12`: file exists, lines 8-12 form the entire `loadCatalog` function with `path.join(catalogDirectory, `${catalogName}.json`)` on line 9, `readFile` on line 10, and `JSON.parse` on line 11, snippet matches: yes, behavior consistent: yes (no validation, `path.join` does not strip `..`).
- Defect 3 test reference `homework-4/app/baseline/tests/security.test.js:5-9`: file exists, lines 5-9 contain the test body that calls `loadCatalog("../catalogs/default")` and expects rejection with `/Invalid catalog name/`, snippet matches: yes, behavior consistent: yes.
- Reference `homework-4/app/baseline/src/quoteCalculator.js:31` for the `calculateQuote` consumer: file exists, line 31 reads `const subtotal = roundCurrency(lines.reduce((sum, line) => sum + line.lineTotal, 0));`, snippet matches: yes, behavior consistent: yes (the buggy line totals feed into the buggy subtotal).
- Reference `homework-4/app/baseline/src/quoteCalculator.js:7-17` for the `applyDiscount` function range: file exists, lines 7-17 cover the full function from the signature through the throw for unknown codes, snippet matches: yes.
- Reference `homework-4/app/baseline/src/quoteCalculator.js:19-40` for `calculateQuote`: file exists, lines 19-40 cover the full function including the final `return` block, snippet matches: yes.
- Reference `homework-4/app/baseline/src/catalogRepository.js:5-6` for the catalog directory origin: file exists, line 5 reads `const moduleDirectory = path.dirname(fileURLToPath(import.meta.url));` and line 6 reads `const catalogDirectory = path.resolve(moduleDirectory, "../data/catalogs");`, snippet matches: yes, behavior consistent: yes.
- Reference `homework-4/app/baseline/src/cli.js:7` for the CLI call into `loadCatalog`: file exists, line 7 reads `const catalog = await loadCatalog(args.catalog);`, snippet matches: yes, behavior consistent: yes (user-supplied `--catalog` reaches `loadCatalog` unvalidated).
- Reference `homework-4/app/baseline/src/cli.js:36-38` for the `--catalog` argument handling: file exists, lines 36-38 cover the `if (current === "--catalog")` branch, snippet matches: yes, behavior consistent: yes (`requireValue` does no validation beyond rejecting flags).
- Reference `homework-4/app/baseline/data/catalogs/default.json` for the catalog format: file exists, top-level keys are `currency` ("USD") and `items[]` (with `sku`, `name`, `unitPrice`), snippet matches: yes, behavior consistent: yes.
- Run workspace mirror check: `diff -r homework-4/app/baseline/ homework-4/runs/bug-001/run-010-open-code-minimax-m3-free/app/` produced no output, confirming the run app is byte-identical to the baseline used as the verification target.

## Discrepancies Found
- No discrepancies found.

## Research Quality Assessment
Level 4, Verified. Every file:line reference resolves to source that contains the cited snippet verbatim, every described observed/expected behavior follows directly from the cited source, and the run workspace mirror matches the baseline so the verification is not stale. No line drift, no missing files, no snippet mismatches, and no behavior contradictions were observed. The research gives the Bug Planner a complete and accurate defect map (line total addition at `quoteCalculator.js:4`, flat-`10` discount at `quoteCalculator.js:13`, unvalidated catalog name in `catalogRepository.js:8-12`) with matching test evidence at `quoteCalculator.test.js:5-17` and `security.test.js:5-9`, plus the relevant consumer and CLI wiring. The lowest level that matches the observed evidence is therefore 4.

## References
- `homework-4/app/baseline/src/quoteCalculator.js:1-5` (defect 1: `calculateLineTotal` with `+` operator)
- `homework-4/app/baseline/src/quoteCalculator.js:4` (the buggy return statement)
- `homework-4/app/baseline/src/quoteCalculator.js:7-17` (defect 2: `applyDiscount` with flat-`10` subtraction)
- `homework-4/app/baseline/src/quoteCalculator.js:12-14` (the `SAVE10` branch)
- `homework-4/app/baseline/src/quoteCalculator.js:13` (the buggy return statement)
- `homework-4/app/baseline/src/quoteCalculator.js:19-40` (`calculateQuote` consumer)
- `homework-4/app/baseline/src/quoteCalculator.js:31` (subtotal reduction over `line.lineTotal`)
- `homework-4/app/baseline/src/catalogRepository.js:5-6` (`moduleDirectory` and `catalogDirectory`)
- `homework-4/app/baseline/src/catalogRepository.js:8-12` (defect 3: `loadCatalog` with unvalidated `path.join`)
- `homework-4/app/baseline/src/cli.js:7` (`loadCatalog(args.catalog)`)
- `homework-4/app/baseline/src/cli.js:36-38` (`--catalog` argument branch)
- `homework-4/app/baseline/data/catalogs/default.json:1-15` (catalog format with `currency` and `items[]`)
- `homework-4/app/baseline/tests/quoteCalculator.test.js:5-12` (line-total test)
- `homework-4/app/baseline/tests/quoteCalculator.test.js:10-11` (assertions on `lineTotal` and `subtotal`)
- `homework-4/app/baseline/tests/quoteCalculator.test.js:14-17` (SAVE10 test)
- `homework-4/app/baseline/tests/quoteCalculator.test.js:15-16` (assertions on `applyDiscount`)
- `homework-4/app/baseline/tests/security.test.js:5-9` (catalog-name validation test)
- Mirror check: `homework-4/runs/bug-001/run-010-open-code-minimax-m3-free/app/` equals `homework-4/app/baseline/` (byte-identical via `diff -r`).

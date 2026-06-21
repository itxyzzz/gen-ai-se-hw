# Themis Validation Checklist

Run ID: `20260621-201051-generate-tests-java-hera-java-full-set-retry`

## Traceability

- PASS: targeted explicit Hephaestus run `20260621-183025-generate-code-java-hera-java-full-set`.
- PASS: preserved Hephaestus inventory snapshot copied to `inputs/selected-code-inventory.snapshot.md`.
- PASS: source Athena run and source spec hash recorded.
- PASS: current canonical spec mismatch recorded. Java candidate source spec is `2FC5...4CFC`; canonical Python spec is `44FD...E3B`.
- PASS: blocked prior Themis run `20260621-191017-generate-tests-java-hera-java-full-set` was not used as selected evidence.

## Candidate Package Scope

- PASS: selected Hephaestus package copied under `agent-3-tests/workspace/selected-code/`.
- PASS: candidate outputs are under `agent-3-tests/outputs/`.
- PASS: Java tests are under `agent-3-tests/outputs/src/test/java/...`.
- PASS: no `outputs/src/java/` path was created.
- PASS: runtime product Java source under `src/main/java/...` was not modified.
- PASS: Themis added one focused test class and a Maven test/build configuration overlay for coverage threshold override support.

## Maven, JUnit, And JaCoCo

- PASS: `mvn -o -s ..\maven-empty-settings.xml -gs ..\maven-empty-settings.xml test jacoco:report jacoco:check`
- Result: 13 tests, 0 failures, JaCoCo check met.
- Instruction coverage: 87.86% from `target/site/jacoco/jacoco.csv`.
- Evidence: `agent-3-tests/evidence/maven-test-jacoco.txt` and `agent-3-tests/evidence/coverage-summary.txt`.

## Coverage Helper And Hook Behavior

- PASS: `python scripts\check_coverage_gate.py --stack java --project-dir . --maven-settings ..\maven-empty-settings.xml --maven-global-settings ..\maven-empty-settings.xml --fail-under 80`
- PASS: deliberate blocking evidence with `--fail-under 99` failed as expected.
- Blocking signal: JaCoCo reported instruction covered ratio `0.87`, expected minimum `0.99`, and Maven ended with `BUILD FAILURE`.
- Evidence: `agent-3-tests/evidence/hook-pass.txt` and `agent-3-tests/evidence/hook-fail.txt`.

## Pipeline Command Evidence

- PASS: valid Java pipeline command is `mvn -o -s ..\maven-empty-settings.xml -gs ..\maven-empty-settings.xml exec:java`.
- PASS: command produced `total=8`, `settled=2`, `rejected=2`, `review_required=4`, `error=0`, `complete=true`, and 8 `TXN*.json` result files.
- PASS: rerun archive evidence exists at `agent-3-tests/workspace/project-under-test/archive/shared-001/results/summary.json`.
- Note: an initial PowerShell attempt using an unquoted `-Dexec.mainClass=...` failed as Maven parsed `.mainClass=...` as a lifecycle phase. The working command uses the `exec.mainClass` already declared in `pom.xml`.
- Evidence: `agent-3-tests/evidence/support-run-pipeline-success.txt`.

## Validation-Only Command Evidence

- PASS: `mvn -o -s ..\maven-empty-settings.xml -gs ..\maven-empty-settings.xml exec:java '-Dexec.mainClass=edu.setu.transactionpipeline.cli.ValidateTransactionsCommand' '-Dexec.args=sample-transactions.json'`
- Safe counts: `total=8`, `valid=6`, `invalid=2`, invalid IDs `TXN006,TXN007`, reason counts `UNSUPPORTED_CURRENCY=1`, `NON_POSITIVE_AMOUNT=1`.
- PASS: validation-only rerun left the existing `shared/results/summary.json` hash unchanged.
- Evidence: `agent-3-tests/evidence/support-validate-transactions.txt` and `agent-3-tests/evidence/support-validate-transactions-hash.txt`.

## Privacy And Audit

- PASS: privacy scan found no raw account IDs, sample descriptions, obvious credentials, tokens, or authorization strings in selectable outputs and compact evidence.
- PASS: tests assert safe results and validation-only reports do not contain raw account identifiers or sample descriptions.
- Evidence: `agent-3-tests/evidence/privacy-scan.txt`.

## Root Containment

- PASS: protected root product, test, docs, screenshot, MCP, config, selection, and root shared paths showed no tracked modifications.
- PASS: new files are confined to `docs/agent-runs/20260621-201051-generate-tests-java-hera-java-full-set-retry/`.
- Evidence: `agent-3-tests/evidence/root-containment.txt`.

## Limitations

- This is preserved candidate evidence only. No canonical Java package set is selected.
- The Java candidate remains source-spec-mismatched against the protected Python canonical root spec by design.
- The run-local Maven settings override file is support evidence only and is not selectable.

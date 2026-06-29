# Themis Handoff

THEMIS_RUN_ID: `20260621-201051-generate-tests-java-hera-java-full-set-retry`

Run folder: `docs/agent-runs/20260621-201051-generate-tests-java-hera-java-full-set-retry/`

Inventory path: `docs/agent-runs/20260621-201051-generate-tests-java-hera-java-full-set-retry/agent-3-tests/outputs/inventory.md`

Selected test package fingerprint: `d75aee1b9e76733d78860348fe26608fef17667a88c25e3c7b416e9150b54922`

## Targeted Source Package

- Parent Hera run ID: `20260621-180512-orchestrate-runs-java-full-set`
- Package-set ID under construction: `java-candidate-20260621-180512`
- Source Athena run ID: `20260621-180826-write-spec-java-hera-java-full-set`
- Source spec path: `docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/agent-1-spec/outputs/specification.md`
- Source spec SHA-256: `2FC5B597915CFD59D5786271DB0CDE96B06417DEEC1433D056B76D7023234CFC`
- Hephaestus run ID: `20260621-183025-generate-code-java-hera-java-full-set`
- Hephaestus inventory path: `docs/agent-runs/20260621-183025-generate-code-java-hera-java-full-set/agent-2-code/outputs/inventory.md`
- Hephaestus inventory SHA-256: `3C5B7DB95A30250DEE60A26D17BAD577FA8BFF98A293FA917A1DEA8ECCBCDEB1`
- Hephaestus package fingerprint: `248ba67b199104cbe06b4b0869cf3143de85ff1dd88da7bf4a13bc85a58ed883`

## What Changed In The Candidate Outputs

- Reused and preserved the Java Hephaestus baseline JUnit tests under `agent-3-tests/outputs/src/test/java/...`.
- Added `ThemisQualityTest.java` for validation-only isolation, safe runtime evidence, archive evidence, and coverage-gate configuration checks.
- Replaced `BuildContractTest.java` in the candidate output so it verifies default 80% coverage plus override-aware JaCoCo wiring.
- Added a candidate `pom.xml` overlay that keeps `<coverage.minimum>0.80</coverage.minimum>` and uses `${coverage.minimum}` in JaCoCo checks. This allows the portable coverage helper's `--fail-under` value to block correctly.

No runtime product code under `src/main/java/...` was modified.

## Validation Status

Overall status: PASS as preserved Themis candidate test package.

- Maven/JUnit/JaCoCo: PASS, 13 tests, 0 failures.
- Coverage: PASS, 87.86% instruction coverage.
- Coverage helper 80%: PASS.
- Coverage helper 99%: PASS as deliberate blocking evidence because it failed with expected minimum `0.99` above actual `0.87`.
- Java pipeline command: PASS with `total=8`, `settled=2`, `rejected=2`, `review_required=4`, `error=0`, `complete=true`.
- Java validation-only command: PASS with `total=8`, `valid=6`, `invalid=2`.
- Command/hook support evidence: PASS after Themis Maven overlay connected `coverage.minimum` to the helper threshold.
- Privacy scan: PASS.
- Root containment: PASS.

## Evidence Files For Clio

- `agent-3-tests/evidence/maven-test-jacoco.txt`
- `agent-3-tests/evidence/coverage-summary.txt`
- `agent-3-tests/evidence/hook-pass.txt`
- `agent-3-tests/evidence/hook-fail.txt`
- `agent-3-tests/evidence/support-run-pipeline-success.txt`
- `agent-3-tests/evidence/support-validate-transactions.txt`
- `agent-3-tests/evidence/support-validate-transactions-hash.txt`
- `agent-3-tests/evidence/privacy-scan.txt`
- `agent-3-tests/evidence/root-containment.txt`

## Source Spec Mismatch

This run intentionally targets the preserved Java candidate spec and Hephaestus package. The canonical root package remains Python:

- Java source spec SHA-256: `2FC5B597915CFD59D5786271DB0CDE96B06417DEEC1433D056B76D7023234CFC`
- Current canonical root spec SHA-256: `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B`

Do not describe this Java test package as canonical unless Hera later selects the Java package set.

## Sub-Agent State

No executor sub-agents were used. This was a bounded retry with tightly coupled file ownership and validation.

## Blockers

No blocker remains for preserving this Themis candidate. The prior blocked run `20260621-191017-generate-tests-java-hera-java-full-set` remains non-selectable and was not used as selected evidence.

## Exact Next Data Clio Needs

Clio (Documentation Generator), if asked to document this Java candidate package, should consume:

- Themis run ID: `20260621-201051-generate-tests-java-hera-java-full-set-retry`
- Themis inventory: `docs/agent-runs/20260621-201051-generate-tests-java-hera-java-full-set-retry/agent-3-tests/outputs/inventory.md`
- Selected test package fingerprint: `d75aee1b9e76733d78860348fe26608fef17667a88c25e3c7b416e9150b54922`
- Target Hephaestus run ID/inventory/fingerprint from this handoff.
- Evidence files listed above.
- The source-spec mismatch note above.

Clio must continue to treat this Java package as candidate-only until Hera performs an explicit inventory-driven package-set selection.

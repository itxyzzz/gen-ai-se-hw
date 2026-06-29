# Themis Java Candidate Output Inventory

- Run ID: `20260621-145102-generate-tests-java-alternate`
- Parent Hera run ID: `20260621-145059-orchestrate-runs-java-alternate`
- Target Hephaestus run ID: `20260621-145101-generate-code-java-alternate`
- Target Hephaestus inventory: `docs/agent-runs/20260621-145101-generate-code-java-alternate/agent-2-code/outputs/inventory.md`
- Source Athena run ID: `20260621-145100-write-spec-java-alternate`
- Stack: `java`
- Selection status: preserved alternate, not canonical

## Selectable Files

| Candidate path under `outputs/` | Intended canonical target if later selected | Kind | SHA-256 | Action |
|---|---|---|---|---|
| `src/test/java/edu/setu/banking/pipeline/ThemisQualityTest.java` | `src/test/java/edu/setu/banking/pipeline/ThemisQualityTest.java` | Java JUnit quality test | `AB6EED5D6DE131A49BFF567B009EFE3CDF4BE8FE54B2C24643DBAC8383C70908` | creates |

## Excluded Runtime And Tool Outputs

- `workspace/`
- `evidence/`
- `target/`
- `shared/`
- `archive/`
- `.coverage*`
- `.pytest_cache/`
- `.test-tmp/`
- `tmp/`
- `__pycache__/`

## Validation Notes

- Candidate workspace validation passed with 21 tests and 0 failures.
- Direct Maven/JUnit/JaCoCo coverage gate passed.
- The repository helper could not use the run-local Maven settings override and is recorded as a support-tool limitation for Java alternates.

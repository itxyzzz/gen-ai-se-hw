# Homework 6 Java Candidate PR Draft

## Summary

This PR would preserve a Java/Maven alternate package for Homework 6 while keeping the current Python package set canonical. The Java candidate implements the same educational transaction-processing pipeline shape with Maven, Jackson, `BigDecimal`, JUnit 5, and JaCoCo.

Important status note: this Java package is not canonical unless a later Hera (Orchestrator) selection explicitly updates `docs/agent-runs/final-selection.md` and `docs/agent-runs/selection-sets.json`.

## Candidate Package

| Role | Run |
|---|---|
| Parent Hera (Orchestrator) | `20260621-180512-orchestrate-runs-java-full-set` |
| Athena (Spec Writer) | `20260621-180826-write-spec-java-hera-java-full-set` |
| Hephaestus (Code Generator) | `20260621-183025-generate-code-java-hera-java-full-set` |
| Themis (Test Generator) | `20260621-201051-generate-tests-java-hera-java-full-set-retry` |
| Clio (Documentation Generator) | `20260621-203431-generate-docs-java-hera-java-full-set` |

The prior Java Themis run `20260621-191017-generate-tests-java-hera-java-full-set` is blocked and non-selectable.

## AI Workflow

- Athena (Spec Writer) produced the Java Maven specification.
- Hephaestus (Code Generator) generated the Java runtime package and recorded Context7 research for Jackson, JUnit, and JaCoCo.
- Themis (Test Generator) produced the usable retry test package and evidence.
- Clio (Documentation Generator) produced this candidate documentation package and Java evidence screenshots.

## Verification

Validated from the preserved Themis retry workspace:

```powershell
mvn -o -s ..\maven-empty-settings.xml -gs ..\maven-empty-settings.xml test jacoco:report jacoco:check
python scripts\check_coverage_gate.py --stack java --project-dir . --maven-settings ..\maven-empty-settings.xml --maven-global-settings ..\maven-empty-settings.xml --fail-under 80
python scripts\check_coverage_gate.py --stack java --project-dir . --maven-settings ..\maven-empty-settings.xml --maven-global-settings ..\maven-empty-settings.xml --fail-under 99
mvn -o -s ..\maven-empty-settings.xml -gs ..\maven-empty-settings.xml exec:java
mvn -o -s ..\maven-empty-settings.xml -gs ..\maven-empty-settings.xml exec:java '-Dexec.mainClass=edu.setu.transactionpipeline.cli.ValidateTransactionsCommand' '-Dexec.args=sample-transactions.json'
```

Results:

- Maven/JUnit/JaCoCo: 13 tests, 0 failures.
- Coverage: 87.86% JaCoCo instruction coverage.
- Coverage helper at 80%: pass.
- Coverage helper at 99%: expected block.
- Full pipeline: `total=8`, `settled=2`, `rejected=2`, `review_required=4`, `error=0`, `complete=true`.
- Validation-only: `total=8`, `valid=6`, `invalid=2`.
- Privacy scan: pass.
- Root containment: pass.

## Screenshots And Evidence

Run-local screenshots:

- Spec produced: Athena Java spec at `../20260621-180826-write-spec-java-hera-java-full-set/agent-1-spec/outputs/specification.md`
- Pipeline run: `screenshots/pipeline-run.png`
- Tests and coverage: `screenshots/test-coverage.png`
- Skill or command in action: `screenshots/skill-run-pipeline.png`
- Hook blocking behavior: `screenshots/hook-trigger.png`
- MCP usage: `screenshots/mcp-interaction.png`
- README with student name: `../README.md`

Evidence files:

- `../evidence/pipeline-run.txt`
- `../evidence/test-coverage.txt`
- `../evidence/skill-run-pipeline.txt`
- `../evidence/validate-transactions.txt`
- `../evidence/hook-trigger.txt`
- `../evidence/mcp-interaction.txt`
- `../evidence/screenshot-capture-notes.md`

## Reviewer Run Instructions

```powershell
cd docs\agent-runs\20260621-201051-generate-tests-java-hera-java-full-set-retry\agent-3-tests\workspace\project-under-test
mvn -o -s ..\maven-empty-settings.xml -gs ..\maven-empty-settings.xml exec:java
mvn -o -s ..\maven-empty-settings.xml -gs ..\maven-empty-settings.xml test jacoco:report jacoco:check
```

If local Maven settings are normal and dependencies resolve, the same checks can run without the `-o -s ... -gs ...` flags.

## Known Limitations

- Java is preserved as a candidate only. Python remains canonical.
- The Java candidate source spec hash intentionally differs from the canonical Python root spec hash.
- Maven evidence used an empty settings/offline cached-artifact workaround because the local configured mirror was unavailable.
- Root `mcp/server.py` remains Python and reads root `shared/results` in normal MCP mode; candidate Java results can be inspected by staging Java outputs there or by direct helper invocation with a candidate result directory.
- The screenshots in this run are terminal-style evidence images derived from preserved Java evidence files.

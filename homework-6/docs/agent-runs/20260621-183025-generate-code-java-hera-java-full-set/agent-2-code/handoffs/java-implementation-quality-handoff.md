# Java Implementation Quality Handoff

Assigned scope: support the Java candidate run by identifying implementation files, Maven details, and high-risk integration points. No files were edited.

Files inspected: generate-code skill, workflow, quality bar, run registry, and source Java specification.

Commands/tests run: none; read-only review only.

Accepted guidance:

- Include `pom.xml`, Java 17, Jackson, JUnit Jupiter, Surefire, JaCoCo, and Exec Maven Plugin.
- Generate functional runtime classes: `TransactionValidator`, `FraudDetector`, `SettlementProcessor`, `ReportingAgent`, and `Integrator`.
- Use `BigDecimal`; avoid binary floating point for money.
- Validate expected sample outcomes, especially `TXN006`, `TXN007`, `TXN002`, `TXN005`, `TXN004`, and at least one settled low-risk transaction.
- Ensure repeated runs archive existing `shared/` to `archive/shared-001`.
- Keep later Task 3-5 surfaces out of the product.

Residual risks noted by sub-agent and addressed:

- Coverage target conflict between source spec's temporary 75% note and current prompt's 80% requirement. The generated `pom.xml` uses an 80% JaCoCo `COVEREDRATIO` rule.
- Exec plugin version and dependency access could be environment-sensitive. The generated package uses a cached stable plugin version for local validation.

Recommended next step from sub-agent: implement, validate with Maven, run the pipeline twice, and preserve inventory. Completed by orchestration thread.

# Research Notes

Run ID: `20260621-180826-write-spec-java-hera-java-full-set`

## Research Classification

These notes distinguish local assignment sources, executor sub-agent findings, Context7 library documentation, generated design decisions, and fallback limitations. The generated transaction system is an educational simulation and does not claim legal, banking, AML, sanctions, payment-network, or regulatory compliance.

## Local Assignment Sources

| Source | Type | Applied insight |
|---|---|---|
| `agent-control/write-spec/transaction-system-brief.md` | Local assignment source | Defines the transaction pipeline, runtime components, JSON file protocol, shared/results ending state, runtime provenance, privacy constraints, dry-run validator seam, and MCP-readable result shape. |
| `sample-transactions.json` | Local assignment source | Confirms eight synthetic transactions with valid cases, high-value cases, unusual timing/channel/country metadata, unsupported currency, and negative amount edge cases. Raw account IDs and descriptions are treated as sensitive and are not repeated in generated examples. |
| `agents.md` | Local assignment source | Establishes layer boundaries, protected Python canonical package set, no silent "latest" targeting, privacy rules, and functional runtime component naming. |
| `agent-control/write-spec/stack-profiles.md` | Local assignment source | Requires Java Maven, `pom.xml`, `src/main/java`, `src/test/java`, `BigDecimal`, Jackson or equivalent JSON, JUnit Jupiter, JaCoCo report/check goals, and Python MCP-readable result files. |
| `agent-control/write-spec/quality-bar.md` | Local assignment source | Requires at least four runtime components, Reporting Agent as preferred fourth component, run provenance, repeated-run archival, 75% temporary coverage target, implementation-ready low-level task cards, and no meta-layer leakage. |
| Homework 3 reference package | Local style/depth source | Used only as a depth and formatting example for a detailed product specification. Homework 3 dispute-domain claims are not reused as Homework 6 domain research. |

## Context7 Library Documentation

| Query | Library ID | Access date | Applied insight |
|---|---|---:|---|
| Java Maven JSON serialization ObjectMapper setup for a transaction processing pipeline using BigDecimal and JSON files | `/fasterxml/jackson-databind` | 2026-06-21 | Jackson Databind `ObjectMapper` is the central data-binding entry point for reading JSON from files/strings/streams into Java objects and writing Java objects to files/strings/bytes. The Java spec should require a reusable JSON mapper helper for POJO result/message serialization. |
| JUnit Jupiter Maven Surefire unit tests Java project current documentation | `/websites/junit_current` | 2026-06-21 | JUnit Jupiter tests should use the `junit-jupiter` dependency and Maven Surefire for unit tests; Failsafe can be configured for integration-test phases. The Java spec should name `mvn test` and concrete `src/test/java` test classes. |
| JaCoCo Maven plugin report check goal coverage threshold documentation | `/websites/jacoco_jacoco_trunk_doc` | 2026-06-21 | JaCoCo Maven `check` verifies configured coverage rules and can halt the build when thresholds fail; `report` generates coverage reports. The Java spec should require `mvn test jacoco:report jacoco:check` with an initial 75% threshold. |

## Domain Research Sub-Agent Findings

The domain research executor produced `agent-1-spec/handoffs/domain-research-handoff.md`.

Accepted findings:

- Treat runtime "agents" as stack-native Java components with bounded responsibility, message contracts, audit identity, and pipeline position.
- Use four components: Transaction Validator, Fraud Detector, Settlement Processor, and Reporting Agent.
- Use `BigDecimal` for money and emit amounts as strings.
- Use ISO 4217-style currency validation for the assignment-supported currencies and reject unsupported sample values with a safe reason code.
- Use deterministic, educational fraud heuristics only; do not claim production fraud detection or compliance.
- Reporting Agent should verify result completeness, summary consistency, `pipeline-status.json`, and final privacy safety.
- Per-transaction results should omit raw account identifiers, descriptions, and unnecessary metadata.

Executor limitation:

- The domain sub-agent did not have Context7 and did not inspect parent-directory Homework 3 references. The orchestration thread separately read the Homework 3 reference package and queried Context7 as recorded above.

## Generated Design Decisions

- Java package root: `edu.setu.transactionpipeline`.
- Maven artifact ID: `transaction-pipeline-java`.
- Main entry point: `src/main/java/edu/setu/transactionpipeline/Integrator.java`.
- Product model package: `edu.setu.transactionpipeline.model`.
- Runtime component package: `edu.setu.transactionpipeline.agent`.
- Support package: `edu.setu.transactionpipeline.support`.
- Runtime component method: `PipelineMessage processMessage(PipelineMessage message)`.
- Currency allowlist for the homework run: `USD`, `EUR`, and `GBP`; `XYZ` is rejected as `unsupported_currency`.
- Fraud thresholds: amount `>= 10000.00` adds risk; amount `>= 50000.00` requires review; UTC hour before `05:00` adds risk; `wire_transfer`, `api`, `mobile`, or non-`US` metadata add educational risk points.
- Final statuses: `settled`, `rejected`, `review_required`, and `error`.
- Pipeline run command: `mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.Integrator`.
- Validation-only seam: a Java CLI option such as `--validate-only sample-transactions.json` should run Transaction Validator without the full pipeline.
- Product result shape: `shared/results/summary.json`, `shared/results/pipeline-status.json`, and `shared/results/TXN*.json` with safe fields only.

## Fallback Limitations

- No live ISO standards registry was queried. The spec intentionally says "ISO 4217-style" and defines an assignment-supported allowlist instead of claiming full ISO registry coverage.
- Maven Exec Plugin documentation was not separately queried. The spec can require the plugin and command because the Java stack profile requires a concrete Java pipeline run command, but Hephaestus (Code Generator) should verify exact plugin syntax during code generation.
- This Athena run does not execute Java code or tests. Validation is document and artifact validation only.

## Downstream Research Requirements For Hephaestus

Hephaestus (Code Generator) must use Context7 during code generation and preserve at least two documented query records, including search text, returned library ID, access date, and applied insight. Recommended queries:

- Maven/Jackson configuration for Java file-based JSON with `ObjectMapper`.
- JUnit Jupiter, Maven Surefire/Failsafe, and JaCoCo Maven plugin coverage check configuration.

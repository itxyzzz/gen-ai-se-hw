# Research Notes

Run ID: `20260621-183025-generate-code-java-hera-java-full-set`

## Context7 Query 1: Jackson JSON Binding

- Access date: 2026-06-21
- Search text: `Java Jackson ObjectMapper setup for strict JSON file reading and writing with BigDecimal amount fields and Java time in a Maven transaction processing pipeline`
- Returned library ID: `/fasterxml/jackson-databind`
- Applied insight: Context7 documented central `ObjectMapper` configuration with serialization/deserialization features and pretty writers. The generated `JsonCodec` centralizes Jackson setup, registers Java Time support, disables timestamp date serialization, enables pretty output, preserves BigDecimal parsing, and writes files through a single helper.
- Files influenced: `src/main/java/edu/setu/transactionpipeline/io/JsonCodec.java`, model DTOs, result writers, tests.

## Context7 Query 2: JUnit Jupiter And Surefire

- Access date: 2026-06-21
- Search text: `JUnit Jupiter Maven Surefire configuration for Java 17 tests in src/test/java with @TempDir filesystem isolation`
- Returned library ID: `/websites/junit_current`
- Applied insight: Context7 documented `junit-jupiter` with Maven Surefire/Failsafe and `@TempDir` support. The generated test suite uses JUnit Jupiter, Maven Surefire, and `@TempDir` to isolate filesystem integration tests from root `shared/`.
- Files influenced: `pom.xml`, `src/test/java/edu/setu/transactionpipeline/**`.

## Context7 Query 3: JaCoCo Maven Check

- Access date: 2026-06-21
- Search text: `JaCoCo Maven plugin report check goal coverage threshold 80 percent covered ratio rule in pom.xml`
- Returned library ID: `/websites/jacoco_jacoco_trunk_doc`
- Applied insight: Context7 documented the `jacoco:check` goal, `BUNDLE` rules, `INSTRUCTION` counter, `COVEREDRATIO` value, and `0.80` minimum threshold. The generated POM configures `prepare-agent`, `report`, and `check`, with plugin-level rules so direct `jacoco:check` works.
- Files influenced: `pom.xml`, `BuildContractTest.java`.

## Local Validation Note

The configured Maven mirror was unavailable by DNS in this runtime, so final Maven validation used a temporary empty Maven settings file and offline mode against locally cached dependencies. To make validation deterministic in this environment, dependency/plugin patch versions were aligned to cached artifacts: Jackson `2.17.1` and `exec-maven-plugin` `3.5.0`.

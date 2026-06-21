# Hephaestus Context7 Research Notes

## Query 1: Jackson Databind JSON DTOs

- Access date: 2026-06-21
- Search text: Java transaction pipeline JSON DTO serialization and deserialization with Jackson ObjectMapper safe BigDecimal handling
- Returned Context7 library ID: `/fasterxml/jackson-databind`
- Applied insight: use `ObjectMapper` as the central JSON entrypoint for reading DTOs from files and writing pretty JSON to files. BigDecimal-aware deserialization belongs in Jackson Databind configuration.
- Files influenced: `JsonSupport.java`, `TransactionRecord.java`, `Integrator.java`, `pom.xml`

## Query 2: JUnit Jupiter with Maven

- Access date: 2026-06-21
- Search text: JUnit Jupiter tests with Maven Surefire for Java unit and integration tests
- Returned Context7 library ID: `/websites/junit_current`
- Applied insight: use `junit-jupiter` test dependency and Maven Surefire `3.5.3` for JUnit Jupiter test execution.
- Files influenced: `pom.xml`, `src/test/java/...`

## Query 3: JaCoCo Maven Coverage Gate

- Access date: 2026-06-21
- Search text: JaCoCo Maven plugin report and check goal with covered ratio minimum threshold
- Returned Context7 library ID: `/websites/jacoco_jacoco_trunk_doc`
- Applied insight: configure `jacoco:check` with a BUNDLE-level `INSTRUCTION` `COVEREDRATIO` minimum of `0.80`, and place rules at plugin level so direct `jacoco:check` invocation sees them.
- Files influenced: `pom.xml`

## Validation Note

The first direct `jacoco:check` attempt exposed that execution-scoped rules were not visible to direct CLI invocation. The rule was moved to plugin-level configuration and later Themis coverage validation passed.

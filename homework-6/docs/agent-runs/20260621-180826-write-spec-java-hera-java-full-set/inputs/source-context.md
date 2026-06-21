# Source Context

## Request Summary

Hera (Orchestrator) dispatched Athena (Spec Writer) to create a preserved Java specification generation run for package set `java-candidate-20260621-180512`. The run must produce Java-specific Task 1 specification artifacts under this child run folder only. Selection is not authorized.

## Product Context

The Generated Transaction System Layer is an educational banking transaction-processing pipeline. It begins with `sample-transactions.json`, creates a fresh `shared/` protocol tree, processes every transaction through stack-native runtime components, archives any previous `shared/` tree as `archive/shared-001`, `archive/shared-002`, and so on, and writes final result files in `shared/results/`.

Required runtime components for this Java spec:

- Integrator, implemented as a Java application entry point.
- Transaction Validator.
- Fraud Detector.
- Settlement Processor.
- Reporting Agent as the default fourth runtime component.

Runtime components are Java classes, not Claude/Codex skills or Homework Automation Layer agents.

## Java Stack Requirements

The generated candidate spec must be concrete for Java:

- Maven project with `pom.xml`.
- Source code under `src/main/java/...`.
- Tests under `src/test/java/...`.
- Money handled with `BigDecimal`; no `double` or `float` for amounts.
- JSON handled with Jackson or equivalent.
- Unit and integration tests through JUnit 5/JUnit Jupiter, Maven Surefire, and Failsafe where needed.
- Coverage through JaCoCo `report` and `check` goals.
- Commands such as `mvn test`, `mvn test jacoco:report jacoco:check`, and `mvn exec:java`.
- Stack-neutral results readable by the Python `mcp/server.py` reader: `shared/results/summary.json` and `shared/results/TXN*.json`.

## Sample Data Observations

The sample input contains eight synthetic transactions:

- Normal transfer amounts in USD, EUR, and GBP.
- High-value transfers.
- Early-hours API activity.
- Invalid currency value `XYZ`.
- Negative amount value.
- Sensitive account identifiers and descriptions that must be redacted or omitted in logs and examples.

## Protected Canonical Context

The current selected package set is `python-canonical-20260621`. This Java run is a candidate alternate only. It must not copy to canonical paths, update selection records, or target "latest" child artifacts.

## Local Template And Style Context

Homework 3 provides depth and style examples for a detailed specification with high-level objective, mid-level objectives, implementation notes, beginning/ending context, edge cases, verification, and low-level task cards. Homework 3 domain claims are not reused as Homework 6 domain research.

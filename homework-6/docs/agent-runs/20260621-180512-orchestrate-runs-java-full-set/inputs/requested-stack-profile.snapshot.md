# Requested Stack Profile Snapshot

- Requested stack: `java`
- Source profile: `agent-control/write-spec/stack-profiles.md`

Java generation must use:

- Maven as the build tool.
- `pom.xml` at the Java candidate package root.
- Source files under `src/main/java/...`.
- Tests under `src/test/java/...`.
- Java `BigDecimal` for money; never `double` or `float`.
- Jackson or equivalent JSON handling.
- Runtime components with functional names such as `TransactionValidator`, `FraudDetector`, `SettlementProcessor`, `ReportingAgent`, and `Integrator`.
- Common component method shape such as `PipelineMessage processMessage(PipelineMessage message)`.
- JSON file protocol through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- JUnit 5/JUnit Jupiter through Maven Surefire or Failsafe.
- JaCoCo Maven plugin with `report` and `check` goals, able to fail below an 80 percent covered-ratio threshold.
- Java pipeline run command declared by the candidate package, such as `mvn exec:java` or a packaged `java -jar` command.
- Stack-neutral result files compatible with the Python FastMCP status reader: `shared/results/summary.json` and `shared/results/TXN*.json`.

Unsupported values such as `auto`, `node`, `go`, and `csharp` are out of scope for this run.


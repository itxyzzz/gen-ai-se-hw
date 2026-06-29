# Requested Stack Profile Snapshot

Requested stack: `java`

Profile decisions from `agent-control/write-spec/stack-profiles.md`:

- Build tool: Maven
- Money: `BigDecimal`
- JSON: Jackson or equivalent; generated package uses Jackson Databind
- Pipeline entry: `src/main/java/.../Integrator.java`
- Runtime components: `TransactionValidator`, `FraudDetector`, `SettlementProcessor`, and `ReportingAgent`
- Tests: JUnit 5/JUnit Jupiter through Maven Surefire
- Coverage: JaCoCo Maven plugin with an 80 percent check goal
- Commands: `mvn test`, `mvn test jacoco:report jacoco:check`, and `mvn exec:java`
- MCP: existing Python `mcp/server.py` should read Java-produced stack-neutral result JSON; no Java MCP server is required for this alternate.

This snapshot contains stack-control guidance only and omits raw sample transaction payloads.

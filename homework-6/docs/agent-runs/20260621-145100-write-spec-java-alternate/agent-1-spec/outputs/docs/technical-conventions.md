# Technical Conventions

- Java package root: `edu.setu.banking.pipeline`.
- Build: Maven with Java 17.
- Money: `BigDecimal`; no `double` or `float` for amounts.
- JSON: Jackson Databind `ObjectMapper` for DTO and map serialization.
- Runtime components: `TransactionValidator`, `FraudDetector`, `SettlementProcessor`, `ReportingAgent`, and `Integrator`.
- Protocol directories: `shared/input`, `shared/processing`, `shared/output`, `shared/results`.
- Final safe outputs: `shared/results/summary.json` and one `shared/results/TXN*.json` file per transaction.
- Tests: JUnit Jupiter through Maven Surefire.
- Coverage: JaCoCo `check` goal with an 80 percent covered-ratio gate after Themis overlay.
- MCP compatibility: the Python `pipeline-status` server can read Java-produced JSON result files.

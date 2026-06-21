# Technical Conventions

## Java Project Layout

Use a Maven Java project:

```text
pom.xml
src/main/java/edu/setu/transactionpipeline/
src/test/java/edu/setu/transactionpipeline/
```

Suggested packages:

- `edu.setu.transactionpipeline` for `Integrator`.
- `edu.setu.transactionpipeline.agent` for runtime components.
- `edu.setu.transactionpipeline.model` for records and JSON models.
- `edu.setu.transactionpipeline.support` for filesystem, JSON, audit, redaction, and result helpers.

## Runtime Components

Runtime transaction pipeline agents are Java classes:

- `TransactionValidator`
- `FraudDetector`
- `SettlementProcessor`
- `ReportingAgent`

Each main runtime component should expose a method compatible with:

```java
PipelineMessage processMessage(PipelineMessage message)
```

Reporting Agent may also expose a run-level method such as:

```java
SummaryReport summarizeRun(List<ProcessingResult> results, RunContext context)
```

Do not implement these components as Claude/Codex skills or name them after Homework Automation Layer agents.

## JSON Protocol

Use Jackson Databind or equivalent JSON handling. Prefer a reusable helper wrapping a shared `ObjectMapper` for file reads and writes.

Runtime protocol directories:

```text
shared/
  input/
  processing/
  output/
  results/
```

The Integrator writes `shared/run-provenance.json` at the root of the fresh shared tree. Result files live under `shared/results/`.

## Message Envelope

Messages should include:

- `message_id`
- `timestamp`
- `source_agent`
- `target_agent`
- `message_type`
- `transaction_id`
- `data`
- `component_history`
- `audit_events`
- `reason_codes`

Amounts in JSON must be strings. Currency values must be uppercase strings.

## Result Files

Per-transaction files use stable names:

```text
shared/results/TXN001.json
```

The payload must be stack-neutral for a Python status reader:

- `transaction_id`
- `status`: `settled`, `rejected`, `review_required`, or `error`
- `amount`
- `currency`
- `reason_codes`
- `risk_tier`
- `processed_at`
- `component_history_count`
- `audit_event_count`

`summary.json` and `pipeline-status.json` should include aggregate counts only, plus run ID, generated timestamp, and result directory metadata.

## Money

Use `BigDecimal` for parsing, validation, threshold comparison, and internal amount representation. Do not use `double`, `float`, or binary floating point for money.

Serialize amounts as plain decimal strings. Avoid scientific notation in result files.

## Time

Use ISO 8601 instants for processing timestamps. For early-hours risk, use the UTC hour from the parsed transaction timestamp unless a later implementation explicitly documents another deterministic rule.

## Reason Codes

Use stable lowercase snake_case reason codes. Suggested codes:

- `missing_required_field`
- `invalid_amount`
- `unsupported_currency`
- `invalid_timestamp`
- `malformed_json`
- `high_amount`
- `very_high_amount`
- `early_hours`
- `wire_transfer_risk`
- `channel_risk`
- `country_risk`
- `settlement_failed`
- `reporting_privacy_violation`
- `processing_error`

## Testing And Coverage

Use JUnit 5/JUnit Jupiter under `src/test/java`. Unit tests should cover component logic without touching the real root `shared/` tree. Integration tests should use temporary directories.

Use Maven Surefire for unit tests and Failsafe when integration-test naming or Maven phases are used. Use JaCoCo Maven plugin `report` and `check` goals with an initial 75% threshold for this Athena candidate. Themis (Test Generator) later owns raising the blocking gate above 80%.

Verification commands:

```bash
mvn test
mvn test jacoco:report jacoco:check
mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.Integrator
```

## Privacy Scanning

Reporting Agent and tests should reject result payloads that include prohibited keys such as `source_account`, `destination_account`, `description`, `metadata`, `raw_payload`, `prompt`, `token`, `secret`, or `credential`.

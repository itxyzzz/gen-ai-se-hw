# Homework 6 Java Alternate Banking Pipeline

Created by Igor Tanatarov.

This preserved Java alternate implements the Homework 6 transaction-processing system as a Maven project under `docs/agent-runs/20260621-145101-generate-code-java-alternate/agent-2-code/outputs`. It is not the canonical submission package; the Python set remains canonical until a later Hera (Orchestrator) `select-set`.

The Java pipeline reads synthetic transaction records, validates required fields and currency, scores deterministic educational review signals, simulates settlement for low-risk records, and writes safe JSON results for later inspection.

## Architecture

```text
sample-transactions.json
        |
        v
Integrator.java
        |
        v
TransactionValidator -> FraudDetector -> SettlementProcessor -> ReportingAgent
        |
        v
shared/results/TXN*.json + shared/results/summary.json
```

## Runtime Components

- `TransactionValidator`: required fields, BigDecimal amount parsing, supported currency, and safe rejection reason codes.
- `FraudDetector`: deterministic review signals for high value, odd hour, country signal, and destination pattern.
- `SettlementProcessor`: simulated settlement for low-risk transactions and safe hold/reject behavior otherwise.
- `ReportingAgent`: aggregate safe counts and reason-code groups.
- `Integrator`: CLI entry point, shared-directory preparation, rerun archival, pipeline orchestration, dry-run validation, and result writing.

## Tech Stack

| Area | Java alternate |
|---|---|
| Language | Java 17 |
| Build | Maven |
| JSON | Jackson Databind |
| Money | `BigDecimal` |
| Tests | JUnit Jupiter via Maven Surefire |
| Coverage | JaCoCo Maven plugin |
| MCP compatibility | Existing Python `pipeline-status` reader consumes Java JSON result files |

## Evidence

- Code run: `20260621-145101-generate-code-java-alternate`
- Test run: `20260621-145102-generate-tests-java-alternate`
- Documentation run: `20260621-145103-generate-docs-java-alternate`
- Passing direct coverage evidence: `agent-3-tests/evidence/test-coverage.txt`

## Status

Preserved alternate for later comparison. No canonical Python outputs were replaced.

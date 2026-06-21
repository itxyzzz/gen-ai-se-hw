# Java Transaction Processing Pipeline Specification

> Candidate Athena (Spec Writer) run: `20260621-180826-write-spec-java-hera-java-full-set`  
> Stack: Java Maven  
> Package-set context: `java-candidate-20260621-180512`  
> Status: preserved candidate output only; not canonical and not selected.

## High-Level Objective

Build an educational Java Maven transaction-processing pipeline that reads `sample-transactions.json`, processes every transaction through stack-native runtime components, writes audit-safe JSON results under `shared/results/`, and preserves deterministic run evidence without exposing sensitive account data.

## Mid-Level Objectives

| ID | Objective | Observable success |
|---|---|---|
| `M1` | Integrator, run setup, and JSON protocol | `Integrator` prepares `shared/input`, `shared/processing`, `shared/output`, `shared/results`, writes `shared/run-provenance.json`, archives any previous `shared/` tree to `archive/shared-001` style folders, loads every sample record, and verifies one terminal result per input. |
| `M2` | Transaction validation | `TransactionValidator` validates required fields, ISO 8601 timestamps, positive `BigDecimal` amounts, and assignment-supported ISO 4217-style currencies, then emits safe validated messages or rejected terminal results with stable reason codes. |
| `M3` | Educational fraud/risk scoring | `FraudDetector` applies deterministic, testable heuristics for high amount, very high amount, early-hours activity, channel, country, and transfer type, routing risky transactions to `review_required` without claiming real fraud, AML, sanctions, or regulatory compliance. |
| `M4` | Simulated settlement outcome | `SettlementProcessor` converts validated and risk-scored messages into exactly one final status: `settled`, `rejected`, `review_required`, or `error`, without moving money, updating balances, or calling external systems. |
| `M5` | Reporting, privacy, and MCP-readable results | `ReportingAgent` writes stack-neutral `shared/results/TXN*.json`, `summary.json`, and `pipeline-status.json`, checks result counts, and fails safely if prohibited sensitive fields would be emitted. |

## Implementation Notes

### Java Stack Contract

The generated product must be a Maven project with this layout:

```text
pom.xml
src/main/java/edu/setu/transactionpipeline/
src/test/java/edu/setu/transactionpipeline/
```

Required Java technologies:

- Maven build and test lifecycle.
- Jackson Databind or equivalent JSON handling, with a reusable JSON codec.
- `BigDecimal` for all money parsing, comparison, thresholds, and serialization decisions.
- JUnit 5/JUnit Jupiter tests through Maven Surefire, with Failsafe when integration-test phases or `*IT` naming are used.
- JaCoCo Maven plugin with `report` and `check` goals.
- Pipeline run command:

```bash
mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.Integrator
```

Validation-only command:

```bash
mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.cli.ValidateTransactionsCommand -Dexec.args="sample-transactions.json"
```

Core verification commands:

```bash
mvn test
mvn test jacoco:report jacoco:check
```

The temporary Athena (Spec Writer) coverage target is 75%. Themis (Test Generator) later owns raising the blocking coverage gate above 80%.

### Runtime Components

The runtime transaction pipeline components are Java classes, not Claude/Codex skills:

- `edu.setu.transactionpipeline.Integrator`
- `edu.setu.transactionpipeline.agent.TransactionValidator`
- `edu.setu.transactionpipeline.agent.FraudDetector`
- `edu.setu.transactionpipeline.agent.SettlementProcessor`
- `edu.setu.transactionpipeline.agent.ReportingAgent`

Main runtime components should use this shared interface shape:

```java
PipelineMessage processMessage(PipelineMessage message)
```

Reporting Agent may also expose run-level summary methods invoked by the Integrator.

### Money, Currency, And Risk Rules

Money is parsed from JSON strings into `BigDecimal`. The implementation must not use `double` or `float` for monetary amounts.

Currency validation is ISO 4217-style and assignment bounded. Accept `USD`, `EUR`, and `GBP`; reject unsupported values with reason code `unsupported_currency`. Do not claim complete ISO registry maintenance.

Use deterministic educational risk constants:

- `HIGH_VALUE_THRESHOLD = BigDecimal("10000.00")`
- `REVIEW_VALUE_THRESHOLD = BigDecimal("50000.00")`
- Early-hours risk: UTC hour before `05:00`

Additional educational risk signals may include wire transfer type, API or mobile channel, and non-`US` country metadata. These are homework heuristics only.

### JSON File Protocol

Runtime communication uses JSON files through:

```text
shared/
  input/
  processing/
  output/
  results/
```

Before a new run creates `shared/`, an existing `shared/` tree must be moved to the next zero-padded archive folder:

```text
archive/shared-001
archive/shared-002
archive/shared-003
```

The Integrator writes `shared/run-provenance.json` for every fresh run. The file contains only schema version, runtime run ID, generated timestamp, source spec run ID/path/fingerprint, selected pipeline version/inventory/fingerprint values supplied to the runtime, and safe path references. It must not include raw transactions, account identifiers, descriptions, credentials, hidden prompts, or thread content.

### Result Shape For Python Status Reader

The Java pipeline must produce generic JSON readable by a future Python `mcp/server.py` reader.

Per-transaction result files:

```text
shared/results/TXN*.json
```

Required safe keys:

- `schema_version`
- `transaction_id`
- `status`
- `reason_codes`
- `amount`
- `currency`
- `processed_at`
- `risk_tier`
- `component_history_count`
- `audit_event_count`

Required `summary.json` keys:

- `schema_version`
- `generated_at`
- `total`
- `settled`
- `rejected`
- `review_required`
- `error`
- `complete`
- `reason_code_counts`

Required `pipeline-status.json` keys:

- `schema_version`
- `generated_at`
- `ready`
- `summary_path`
- `results_path`
- `total`
- `settled`
- `rejected`
- `review_required`
- `error`
- `complete`

Amounts are strings. Counts are integers. Status values are limited to `settled`, `rejected`, `review_required`, and `error`. Results must not include raw account identifiers, raw descriptions, raw metadata objects, prompt text, secrets, or full input payloads.

### Privacy And Audit

Audit events include:

- ISO 8601 timestamp.
- Component name.
- Transaction ID when available.
- Safe outcome.
- Reason code when applicable.

Logs, audit events, validation reports, summaries, and test fixtures must not echo account identifiers, transaction descriptions, credentials, or full input payloads. Use reason codes and counts instead of sensitive prose.

## Context

### Beginning Context

The repository contains `sample-transactions.json` with eight synthetic records. The sample includes ordinary valid cases, high-value records, unusual timing/channel/country metadata, an unsupported currency, and a negative amount. Account identifiers and descriptions are sensitive and must not be repeated in generated examples or logs.

The Java candidate starts without a Java Maven product package. The current selected package set is Python (`python-canonical-20260621`) and remains protected unless a later explicit selection changes it.

### Ending Context

After implementation from this specification:

- `pom.xml` configures Jackson, JUnit Jupiter, Surefire/Failsafe as needed, JaCoCo, and Maven Exec Plugin.
- Java source exists under `src/main/java/edu/setu/transactionpipeline/`.
- Java tests exist under `src/test/java/edu/setu/transactionpipeline/`.
- `mvn test` passes.
- `mvn test jacoco:report jacoco:check` enforces at least 75% coverage.
- `mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.Integrator` processes all sample records.
- Repeated runs archive previous `shared/` output.
- `shared/run-provenance.json` exists for the current run and archived runs retain their provenance.
- `shared/results/summary.json`, `shared/results/pipeline-status.json`, and one safe `TXN*.json` file per input record exist.
- Validator dry-run can inspect `sample-transactions.json` without running the full pipeline.

## Low-Level Tasks

### Task 1: Maven Project Structure And Build Contract

**Prompt:** Create the Maven Java project skeleton for the educational transaction-processing pipeline. Configure Jackson, JUnit Jupiter, Maven Surefire, Maven Failsafe if integration tests use the `*IT` naming pattern, JaCoCo `report` and `check`, and Maven Exec Plugin for `edu.setu.transactionpipeline.Integrator`.

**Files to create or update:** `pom.xml`, `src/main/java/edu/setu/transactionpipeline/Integrator.java`, `src/main/java/edu/setu/transactionpipeline/model/PipelineMessage.java`, `src/test/java/edu/setu/transactionpipeline/BuildContractTest.java`

**Functions/classes to create:** `Integrator`, `public static void main(String[] args)`, `public int run(PipelineOptions options)`, `PipelineMessage`

**Details:** Set Java 17 or newer. Add Jackson Databind and Java Time module. Add `org.junit.jupiter:junit-jupiter`. Configure Surefire, optional Failsafe, JaCoCo `prepare-agent`, `report`, and `check` with a 75% threshold. Add `exec-maven-plugin` for the pipeline command. Keep the product local and file-based.

**Edge cases:** Java version mismatch, missing dependencies, coverage below threshold, tests writing to root `shared/`, missing Exec Plugin configuration.

**Acceptance criteria:** `pom.xml` names required dependencies/plugins; JUnit Jupiter tests run; JaCoCo check can fail below threshold; pipeline command targets `Integrator`.

**Verification:** `mvn test`; `mvn test jacoco:report jacoco:check`; pipeline command after Integrator tasks are complete.

### Task 2: Shared Models, Statuses, Reason Codes, And Audit Events

**Prompt:** Implement core models for message envelopes, transaction records, final results, processing statuses, reason codes, and audit events.

**Files to create or update:** `PipelineMessage.java`, `TransactionRecord.java`, `TransactionResult.java`, `ProcessingStatus.java`, `ReasonCode.java`, `AuditEvent.java`, `src/test/java/edu/setu/transactionpipeline/model/ModelSerializationTest.java`

**Functions/classes to create:** `ProcessingStatus { SETTLED, REJECTED, REVIEW_REQUIRED, ERROR }`, `String jsonValue()`, `AuditEvent`, `PipelineMessage withAuditEvent(AuditEvent event)`, `PipelineMessage withReasonCode(ReasonCode reasonCode)`

**Details:** Serialize statuses as `settled`, `rejected`, `review_required`, and `error`. Represent amounts as strings at JSON boundaries. Keep account and description fields out of result and audit serialization.

**Edge cases:** Unknown status strings, blank reason codes, messages missing transaction IDs, accidental Java type metadata in JSON.

**Acceptance criteria:** Models expose the exact final vocabulary; serialization emits safe keys only; audit events remain structured.

**Verification:** `mvn test -Dtest=ModelSerializationTest`; inspect serialized fixtures for safe fields.

### Task 3: Jackson Codec And Shared Directory Protocol

**Prompt:** Implement a Jackson JSON codec and shared-directory manager for protocol folders and deterministic archival.

**Files to create or update:** `io/JsonCodec.java`, `io/SharedDirectoryManager.java`, `io/ProtocolPaths.java`, `JsonCodecTest.java`, `SharedDirectoryManagerTest.java`

**Functions/classes to create:** `JsonCodec.read(Path, Class<T>)`, `JsonCodec.write(Path, Object)`, `JsonCodec.readTree(Path)`, `SharedDirectoryManager.prepareFreshSharedTree(Path, Path)`, `archiveExistingSharedTree`, `nextArchivePath`

**Details:** Archive existing `shared/` before fresh directory creation. Use `archive/shared-001` and next highest plus one. Configure `ObjectMapper` for Java time and stable JSON. Preserve amount strings.

**Edge cases:** Existing nested `shared/`, archive gaps, missing archive directory, unwritable paths, malformed JSON, numeric amount tokens.

**Acceptance criteria:** Repeated runs preserve prior runtime output; fresh protocol directories always exist; codec round-trips safe DTOs.

**Verification:** `mvn test -Dtest=JsonCodecTest,SharedDirectoryManagerTest`.

### Task 4: Runtime Provenance Writer

**Prompt:** Implement safe `shared/run-provenance.json` writing for current and archived runs.

**Files to create or update:** `model/RunProvenance.java`, `io/RunProvenanceWriter.java`, `RunProvenanceWriterTest.java`

**Functions/classes to create:** `RunProvenance`, `RunProvenance fromOptions(PipelineOptions options, Clock clock)`, `Path write(Path sharedRoot, RunProvenance provenance)`, `assertSafe(RunProvenance provenance)`

**Details:** Include `schema_version`, `runtime_run_id`, `generated_at`, `source_spec_run_id`, `source_spec_path`, `source_spec_fingerprint`, `pipeline_version_id`, `pipeline_inventory_path`, and `pipeline_package_fingerprint` when supplied. Use explicit safe placeholders for missing optional values.

**Edge cases:** Missing provenance options, invalid fingerprint shape, unwritable shared root, deterministic clock injection.

**Acceptance criteria:** Every successful pipeline run writes provenance; archived shared folders retain prior provenance; no sensitive content appears.

**Verification:** `mvn test -Dtest=RunProvenanceWriterTest`; inspect safe keys only.

### Task 5: Input Loader And Initial Message Writer

**Prompt:** Load `sample-transactions.json`, convert records into initial pipeline messages, and write them to `shared/input`.

**Files to create or update:** `io/InputTransactionLoader.java`, `io/InputMessageWriter.java`, `model/TransactionRecord.java`, `InputTransactionLoaderTest.java`, `InputMessageWriterTest.java`

**Functions/classes to create:** `List<TransactionRecord> load(Path samplePath)`, `List<PipelineMessage> toInitialMessages(List<TransactionRecord> records)`, `Path writeInitialMessage(ProtocolPaths paths, PipelineMessage message, int sequenceNumber)`

**Details:** Load the JSON array while preserving amount text. Initial messages target `transaction_validator`. Use deterministic filenames such as `001-<transaction-id>-transaction-validator.json`. Business validation belongs to Transaction Validator.

**Edge cases:** Missing sample file, empty array, malformed JSON, duplicate transaction ID, unknown extra fields, blank transaction ID.

**Acceptance criteria:** Every input record becomes one initial message or a safe startup error; messages are under `shared/input`; no audit-ready output repeats sensitive fields.

**Verification:** `mvn test -Dtest=InputTransactionLoaderTest,InputMessageWriterTest`.

### Task 6: Transaction Validator Component

**Prompt:** Implement `TransactionValidator` as the first runtime component with required-field, timestamp, amount, and currency validation.

**Files to create or update:** `agent/TransactionValidator.java`, `model/ValidationResult.java`, `TransactionValidatorTest.java`

**Functions/classes to create:** `PipelineMessage processMessage(PipelineMessage message)`, `ValidationResult validate(TransactionRecord record)`, `BigDecimal parsePositiveAmount(String amountText)`, `boolean isSupportedCurrency(String currencyCode)`, `Instant parseTimestamp(String timestampText)`

**Details:** Validate transaction ID, timestamp, source/destination account presence, amount, currency, transaction type, and metadata object presence. Presence checks must not expose account values. Valid records target `fraud_detector`; invalid records become rejected terminal messages.

**Edge cases:** Unsupported currency, negative amount, zero amount, non-numeric amount, missing required field, blank transaction ID, malformed timestamp, extra metadata.

**Acceptance criteria:** Valid records proceed; invalid records receive `rejected`; reason codes include `invalid_amount`, `unsupported_currency`, `missing_required_field`, `invalid_timestamp`, and `malformed_json`; errors do not echo sensitive input.

**Verification:** `mvn test -Dtest=TransactionValidatorTest`.

### Task 7: Validator Dry-Run CLI

**Prompt:** Implement validation-only CLI support for future operator tooling without running the full pipeline.

**Files to create or update:** `cli/ValidateTransactionsCommand.java`, `model/ValidationReport.java`, `ValidateTransactionsCommandTest.java`

**Functions/classes to create:** `public static void main(String[] args)`, `int run(String[] args)`, `ValidationReport validateOnly(Path samplePath)`, `void writeReport(ValidationReport report, Optional<Path> outputPath)`

**Details:** The command loads records and runs only Transaction Validator logic. Report fields: `schema_version`, `generated_at`, `total`, `valid`, `invalid`, `reason_code_counts`, and invalid transaction IDs only. It must not write `shared/results/TXN*.json` or `summary.json` as its primary behavior.

**Edge cases:** Unsupported currency, negative amount, missing field, malformed JSON file, empty sample file, unwritable optional report path.

**Acceptance criteria:** Dry run is independent; output has counts, transaction IDs, and reason codes only; raw account identifiers and descriptions are omitted.

**Verification:** `mvn test -Dtest=ValidateTransactionsCommandTest`; run the validation-only command.

### Task 8: Fraud Detector Component

**Prompt:** Implement deterministic educational risk scoring for validated messages.

**Files to create or update:** `agent/FraudDetector.java`, `model/FraudAssessment.java`, `model/RiskTier.java`, `FraudDetectorTest.java`

**Functions/classes to create:** `PipelineMessage processMessage(PipelineMessage message)`, `FraudAssessment assess(TransactionRecord record)`, `int calculateRiskScore(TransactionRecord record)`, `RiskTier classifyRisk(int score)`

**Details:** Use `BigDecimal` for amount thresholds. Add reason codes such as `high_value`, `very_high_value`, `unusual_time`, `wire_transfer_type`, `channel_risk`, and `country_risk`. Low-risk records proceed to settlement; medium/high/very-high educational risk records carry `review_required` routing. Already rejected messages pass through safely.

**Edge cases:** Amount exactly at thresholds, timestamp exactly at early-hours boundary, missing optional metadata, multiple risk signals, rejected message entering Fraud Detector.

**Acceptance criteria:** Scoring is deterministic; no production fraud/compliance claims; output includes safe tier, score, and reason codes only.

**Verification:** `mvn test -Dtest=FraudDetectorTest`.

### Task 9: Settlement Processor Component

**Prompt:** Implement simulated final outcome mapping without money movement or external systems.

**Files to create or update:** `agent/SettlementProcessor.java`, `model/SettlementDecision.java`, `SettlementProcessorTest.java`

**Functions/classes to create:** `PipelineMessage processMessage(PipelineMessage message)`, `SettlementDecision decide(PipelineMessage message)`, `ProcessingStatus mapToFinalStatus(PipelineMessage message)`, `boolean shouldRequireReview(FraudAssessment assessment)`

**Details:** Validation failures become `rejected`; low-risk valid records become `settled`; medium/high/very-high risk records become `review_required`; unexpected state becomes `error` with `processing_error`.

**Edge cases:** Rejected validator message, missing fraud assessment, unknown risk tier, duplicate settlement attempt, result serialization failure.

**Acceptance criteria:** Every settlement input receives one terminal status; status values are limited to `settled`, `rejected`, `review_required`, and `error`; output is privacy-safe.

**Verification:** `mvn test -Dtest=SettlementProcessorTest`.

### Task 10: Reporting Agent, Result Files, And Summary

**Prompt:** Implement `ReportingAgent` as the fourth runtime component that writes result files and aggregate summaries.

**Files to create or update:** `agent/ReportingAgent.java`, `model/PipelineSummary.java`, `model/PipelineStatusReport.java`, `io/ResultWriter.java`, `privacy/PrivacyGuard.java`, `ReportingAgentTest.java`, `PrivacyGuardTest.java`

**Functions/classes to create:** `TransactionResult toTransactionResult(PipelineMessage terminalMessage)`, `PipelineSummary summarize(List<TransactionResult> results, int expectedInputCount)`, `PipelineStatusReport buildStatusReport(PipelineSummary summary)`, `void writeResults(Path resultsDir, List<TransactionResult> results, PipelineSummary summary)`, `void verifyCompleteness(...)`, `void verifyPrivacySafe(JsonNode node)`

**Details:** Write one `TXN*.json` file per input. Write `summary.json` and `pipeline-status.json`. Count statuses, detect missing/extra/duplicate results, and reject prohibited keys before writing.

**Edge cases:** Missing terminal result, duplicate result, unknown final status, count mismatch, empty input, prohibited sensitive field, unwritable results directory.

**Acceptance criteria:** Python `mcp/server.py` can consume generic JSON; summary counts use exact status vocabulary; reporting fails safely on inconsistency or privacy leakage.

**Verification:** `mvn test -Dtest=ReportingAgentTest,PrivacyGuardTest`.

### Task 11: Integrator Orchestration And File Movement

**Prompt:** Implement `Integrator` to prepare the runtime tree, archive prior output, write provenance, load sample transactions, move messages through protocol directories, invoke all components, and finalize the run.

**Files to create or update:** `Integrator.java`, `cli/PipelineOptions.java`, `io/ProtocolFileMover.java`, `IntegratorTest.java`, `IntegrationPipelineTest.java`

**Functions/classes to create:** `int run(PipelineOptions options)`, `List<PipelineMessage> loadAndPrepareMessages(Path samplePath)`, `PipelineMessage processTransaction(PipelineMessage initialMessage)`, `void writeProtocolState(...)`, `PipelineSummary finalizeRun(...)`

**Details:** Normal mode prepares `shared/`, archives previous output, writes provenance, creates directories, writes initial messages, records processing/output state, catches per-transaction component exceptions as safe `error` results, and allows unrelated transactions to continue.

**Edge cases:** Existing `shared/`, one invalid transaction among valid inputs, component exception, malformed input file, unwritable results directory, duplicate transaction ID, empty sample array.

**Acceptance criteria:** Full pipeline accounts for every input; repeated runs archive prior output; required protocol directories and result files exist after success.

**Verification:** `mvn test -Dtest=IntegratorTest,IntegrationPipelineTest`; run the full pipeline command; inspect `summary.json`, `pipeline-status.json`, and representative `TXN*.json`.

### Task 12: Result Schema Lock For Python MCP Reader

**Prompt:** Lock the stack-neutral JSON schemas expected by future read-only status tooling.

**Files to create or update:** `io/ResultSchema.java`, `model/TransactionResult.java`, `model/PipelineSummary.java`, `ResultSchemaTest.java`

**Functions/classes to create:** `void validateTransactionResult(JsonNode node)`, `void validateSummary(JsonNode node)`, `void validatePipelineStatus(JsonNode node)`, `boolean containsSensitiveField(JsonNode node)`

**Details:** Per-transaction files are named `TXN*.json` and contain generic JSON objects, not Java class metadata. Amount is always a string. Counts are integers. Status is one of the four final values.

**Edge cases:** Unknown status, missing amount string, numeric amount token, count mismatch, Java-specific metadata, sensitive field name or value.

**Acceptance criteria:** Generic Python JSON code can parse the result files; schema tests reject unsafe or Java-specific output.

**Verification:** `mvn test -Dtest=ResultSchemaTest`; compare fixtures with documented keys.

### Task 13: JUnit Unit And Integration Tests With Filesystem Isolation

**Prompt:** Build a JUnit Jupiter test suite for runtime components, protocol utilities, dry-run validation, full-pipeline behavior, archive behavior, provenance, schemas, and privacy controls.

**Files to create or update:** `TestFixtures.java`, component tests, `IntegratorTest.java`, `IntegrationPipelineTest.java`, `PrivacySafetyTest.java`

**Functions/classes to create:** `TestFixtures.validLowRiskTransaction()`, `invalidCurrencyTransaction()`, `negativeAmountTransaction()`, `highValueTransaction()`, `tempPipelineOptions(Path tempDir)`, JUnit tests using `@TempDir`

**Details:** Tests must never write into repository root `shared/` or `archive/`. Cover valid settlement, invalid currency rejection, negative amount rejection, high-value review, early-hours review, malformed JSON, repeated-run archival, provenance, dry-run, result schema, and privacy guard failures.

**Edge cases:** Unsupported currency, negative amount, very high amount, early-hours timestamp, malformed JSON, empty input, duplicate transaction ID, archive sequence.

**Acceptance criteria:** `mvn test` passes; tests cover all runtime components and Integrator; privacy tests fail on sensitive leakage.

**Verification:** `mvn test`; confirm generated files live under JUnit temporary directories.

### Task 14: Final Java Verification Commands And Evidence Contract

**Prompt:** Define and enforce the final Java verification command contract without adding homework automation mechanics to the product.

**Files to create or update:** `pom.xml`, optional `CoverageGateContractTest.java`

**Functions/classes to create:** JaCoCo `report` and `check` executions; optional command-contract assertions.

**Details:** Required commands are `mvn test`, `mvn test jacoco:report jacoco:check`, full pipeline command, and validation-only command. Manual inspection targets are `shared/run-provenance.json`, `shared/results/summary.json`, `shared/results/pipeline-status.json`, and `shared/results/TXN*.json`. Do not add Git hook setup, MCP configuration setup, screenshot capture, PR packaging, or selection mechanics to product tasks.

**Edge cases:** Coverage below threshold, DTO-only tests, missing Exec Plugin configuration, unsafe startup errors, validator dry run triggering full pipeline output.

**Acceptance criteria:** Build, test, coverage, full-run, and dry-run commands are clear; JaCoCo check enforces threshold; result inspection confirms stack-neutral JSON.

**Verification:** Run all four commands listed above.

## Edge Cases And Failure Modes

| Case | Expected behavior |
|---|---|
| Existing `shared/` tree | Archive to next zero-padded `archive/shared-###` folder before new run. |
| Missing or malformed sample input | Fail with safe startup error and no sensitive echo. |
| Unsupported currency | Reject transaction with `unsupported_currency`. |
| Negative, zero, blank, or non-numeric amount | Reject transaction with `invalid_amount`. |
| High or very high amount | Route to review as dictated by deterministic risk thresholds. |
| Early-hours timestamp | Add educational risk reason code using UTC hour rule. |
| Component exception for one transaction | Emit safe `error` result for that transaction and continue others when possible. |
| Duplicate transaction ID | Detect deterministically and avoid overwriting result files. |
| Missing result file | Reporting Agent marks completeness false or fails safely. |
| Sensitive field in result payload | Privacy Guard rejects write or reports safe `reporting_privacy_violation`. |

## Verification Plan

| Objective | Evidence | Test categories |
|---|---|---|
| `M1` | Full pipeline creates protocol tree, archive behavior, provenance, and one result per input. | Integrator tests, directory manager tests, repeated-run integration test. |
| `M2` | Invalid amount/currency/timestamp/required-field records reject with safe codes. | Transaction Validator unit tests, dry-run CLI tests. |
| `M3` | Risk thresholds and early-hours rules are deterministic and privacy-safe. | Fraud Detector threshold and reason-code tests. |
| `M4` | Terminal status mapping is exact and side-effect free. | Settlement Processor tests and integration tests. |
| `M5` | Summary/status/result JSON is complete, count-consistent, stack-neutral, and privacy-safe. | Reporting Agent tests, result schema tests, Privacy Guard tests. |

Manual review should confirm that no generated product requirement implements Homework Automation Layer agents, selection workflows, harness planning, screenshots, or PR packaging as runtime product behavior.

## Downstream Handoff To Hephaestus

Hephaestus (Code Generator) needs these exact inputs:

- Athena run ID: `20260621-180826-write-spec-java-hera-java-full-set`
- Specification path: `docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/agent-1-spec/outputs/specification.md`
- Specification SHA-256 fingerprint from this run's final handoff.
- Requested stack: `java`
- Parent Hera run ID: `20260621-180512-orchestrate-runs-java-full-set`
- Package-set ID under construction: `java-candidate-20260621-180512`
- Selection record path: `docs/agent-runs/final-selection.md`
- Protected canonical set: `python-canonical-20260621`

Hephaestus must not target "latest" artifacts silently. It must use the explicit Athena run ID, specification path, fingerprint, package-set ID, and Hera context supplied above. It must use Context7 during code generation and document at least two query records.

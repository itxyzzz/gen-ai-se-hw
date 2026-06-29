# Low-Level Tasks Handoff

Run ID: `20260621-145100-write-spec-java-alternate`
Role: Athena (Spec Writer) low-level task decomposition executor sub-agent
Stack: `java`

## Assigned Scope

Produce implementation-ready Java low-level task cards for the Generated Transaction System Layer only. This handoff is run-local evidence for the Java alternate Athena (Spec Writer) package and does not authorize editing canonical root outputs.

## Context Inspected

- Operator prompt for this bounded low-level task handoff.
- `agent-control/write-spec/transaction-system-brief.md`.
- `agent-control/write-spec/stack-profiles.md`.
- `agent-control/write-spec/workflow.md`.
- `agent-control/write-spec/quality-bar.md`.
- `agent-control/write-spec/run-registry.md`.
- `agents.md`.
- `README.md`.
- `sample-transactions.json`, inspected for structural facts only.
- `docs/agent-runs/20260621-145100-write-spec-java-alternate/agent-1-spec/handoffs/domain-research-handoff.md`.

## Assumptions

- Use Maven, Java 17 or newer, Jackson, JUnit Jupiter, and JaCoCo.
- Use package `edu.setu.transactionpipeline` unless the integration author chooses another single package and updates every path consistently.
- Use `BigDecimal` for all money parsing, comparisons, totals, and serialized amount handling. Do not use `double` or `float`.
- Treat account identifiers, descriptions, and metadata as sensitive. Logs, audit events, result files, summaries, tests, and docs must not expose raw account identifiers or raw descriptions.
- Keep the pipeline an educational simulation. Do not claim real banking, AML, sanctions, payment-network, legal, or regulatory compliance.

## Task Cards

### Task 1: Maven Project Structure And Build Contract

Prompt: Create the Java Maven project skeleton for the transaction-processing pipeline. Use Maven, Jackson, JUnit Jupiter, and JaCoCo. Keep all product code under `src/main/java` and all tests under `src/test/java`.

Files to create or update:

- `pom.xml`
- `src/main/java/edu/setu/transactionpipeline/Integrator.java`
- `src/main/java/edu/setu/transactionpipeline/model/PipelineMessage.java`
- `src/test/java/edu/setu/transactionpipeline/BuildContractTest.java`

Classes and methods:

- `edu.setu.transactionpipeline.Integrator`
- `public static void main(String[] args)`
- `edu.setu.transactionpipeline.model.PipelineMessage`

Details:

- Configure Maven compiler source and target for Java 17 or newer.
- Add Jackson databind and Java time module dependencies.
- Add JUnit Jupiter dependencies and Maven Surefire.
- Add JaCoCo Maven plugin with `prepare-agent`, `report`, and `check` goals.
- Configure JaCoCo check for Athena's temporary 75% coverage target.
- Add optional `exec-maven-plugin` support for `mvn exec:java`.
- Keep dependency choices minimal and local-first; no database, external service, queue, or network dependency.

Edge cases:

- A missing `pom.xml` should make the project impossible to run; this task must establish all required commands.
- JaCoCo should fail clearly when coverage falls below the configured threshold.
- Java version mismatch should be visible through Maven compiler configuration rather than hidden in source code.

Acceptance criteria:

- `mvn test` runs JUnit Jupiter tests.
- `mvn test jacoco:report jacoco:check` produces a coverage report and enforces the 75% threshold.
- `mvn exec:java` can be wired to `Integrator.main` without requiring canonical Python files.

Verification:

- Run `mvn test`.
- Run `mvn test jacoco:report jacoco:check`.
- Review `pom.xml` for Jackson, JUnit Jupiter, Surefire, JaCoCo, and Java compiler configuration.

### Task 2: Shared JSON Envelope, DTOs, And File Protocol Utilities

Prompt: Implement stack-neutral JSON envelope classes and shared-directory file protocol utilities for `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.

Files to create or update:

- `src/main/java/edu/setu/transactionpipeline/model/PipelineMessage.java`
- `src/main/java/edu/setu/transactionpipeline/model/TransactionRecord.java`
- `src/main/java/edu/setu/transactionpipeline/model/AuditEvent.java`
- `src/main/java/edu/setu/transactionpipeline/model/ProcessingStatus.java`
- `src/main/java/edu/setu/transactionpipeline/io/JsonCodec.java`
- `src/main/java/edu/setu/transactionpipeline/io/SharedDirectoryManager.java`
- `src/test/java/edu/setu/transactionpipeline/io/JsonCodecTest.java`
- `src/test/java/edu/setu/transactionpipeline/io/SharedDirectoryManagerTest.java`

Classes and methods:

- `PipelineMessage processMessage(PipelineMessage message)` as the shared component contract shape.
- `JsonCodec.read(Path path, Class<T> type)`
- `JsonCodec.write(Path path, Object value)`
- `SharedDirectoryManager.prepareFreshSharedTree(Path sharedRoot, Path archiveRoot)`
- `SharedDirectoryManager.archiveExistingSharedTree(Path sharedRoot, Path archiveRoot)`
- `SharedDirectoryManager.writeInputMessage(PipelineMessage message)`

Details:

- Model a JSON envelope with `messageId`, `timestamp`, `sourceAgent`, `targetAgent`, `messageType`, `data`, `auditEvents`, and `reasonCodes`.
- Serialize monetary amounts as strings and deserialize to `BigDecimal` only in Java DTOs or component logic.
- Configure Jackson for ISO-8601 timestamps and strict enough behavior to surface malformed JSON.
- Implement deterministic directory creation for `input`, `processing`, `output`, and `results`.
- Archive an existing `shared/` tree to the next zero-padded `archive/shared-###` folder before creating a fresh tree.
- Ensure messages and audit events store safe identifiers only: transaction ID, component name, outcome, reason code, timestamp, and counts where useful.

Edge cases:

- Existing `shared/` folder with files should be archived before new directories are created.
- Existing `archive/shared-001` and `archive/shared-002` should lead to the next available folder.
- Malformed JSON should produce a per-transaction error result or a safe startup failure, not an unredacted stack trace in output files.
- Amount fields must remain exact strings in JSON and `BigDecimal` in Java logic.

Acceptance criteria:

- A fresh run creates exactly the required shared protocol directories.
- A repeated run preserves the prior `shared/` tree under a zero-padded archive folder.
- JSON messages round-trip without binary floating point conversion.
- No audit or result fixture includes raw account identifiers or raw descriptions.

Verification:

- Run `mvn test -Dtest=JsonCodecTest,SharedDirectoryManagerTest`.
- Inspect generated test JSON fixtures for amount strings and redacted or omitted sensitive fields.

### Task 3: Input Loader And Runtime Provenance

Prompt: Implement input loading from `sample-transactions.json` and write a safe `shared/run-provenance.json` file for each run.

Files to create or update:

- `src/main/java/edu/setu/transactionpipeline/io/InputTransactionLoader.java`
- `src/main/java/edu/setu/transactionpipeline/model/RunProvenance.java`
- `src/main/java/edu/setu/transactionpipeline/io/RunProvenanceWriter.java`
- `src/test/java/edu/setu/transactionpipeline/io/InputTransactionLoaderTest.java`
- `src/test/java/edu/setu/transactionpipeline/io/RunProvenanceWriterTest.java`

Classes and methods:

- `InputTransactionLoader.load(Path samplePath)`
- `InputTransactionLoader.toInitialMessages(List<TransactionRecord> records)`
- `RunProvenanceWriter.write(Path sharedRoot, RunProvenance provenance)`
- `RunProvenance.fromEnvironmentOrDefaults(Clock clock)`

Details:

- Load the sample array into typed records while preserving monetary amount text until validation.
- Convert each raw transaction into an initial `PipelineMessage` targeted to `TransactionValidator`.
- Omit raw account identifiers and raw descriptions from the message fields consumed downstream unless a component explicitly needs the structural presence check. If retained internally, redact before logs, audit, results, and summaries.
- Write `shared/run-provenance.json` with schema version, runtime run ID, generated timestamp, source spec run ID, canonical spec path, spec fingerprint, selected code run ID, selected inventory path or package fingerprint, and code package fingerprint when supplied.
- Do not include raw transactions, account identifiers, descriptions, credentials, prompts, or local secret paths in provenance.

Edge cases:

- Missing sample file should produce a safe startup error.
- Empty sample array should produce a summary with zero processed records and no per-transaction results.
- Unknown extra fields should not break the loader unless they replace required fields.
- Missing provenance environment values should be represented as explicit placeholders such as `not-supplied`, not guessed.

Acceptance criteria:

- The loader accounts for every sample record without exposing sensitive fields in audit-ready output objects.
- `shared/run-provenance.json` exists for each fresh run and contains only traceability metadata.
- Tests prove provenance excludes raw transactions and sensitive field values.

Verification:

- Run `mvn test -Dtest=InputTransactionLoaderTest,RunProvenanceWriterTest`.
- Manually review `run-provenance.json` fixture keys for safe traceability-only content.

### Task 4: TransactionValidator Component

Prompt: Implement the `TransactionValidator` runtime component with deterministic validation rules, `BigDecimal` amount parsing, ISO-style currency checks, safe audit events, and dry-run compatibility.

Files to create or update:

- `src/main/java/edu/setu/transactionpipeline/component/TransactionValidator.java`
- `src/main/java/edu/setu/transactionpipeline/model/ValidationResult.java`
- `src/test/java/edu/setu/transactionpipeline/component/TransactionValidatorTest.java`

Classes and methods:

- `public final class TransactionValidator`
- `public PipelineMessage processMessage(PipelineMessage message)`
- `ValidationResult validate(TransactionRecord record)`
- `BigDecimal parsePositiveAmount(String amountText)`
- `boolean isSupportedCurrency(String currencyCode)`

Details:

- Validate required fields: transaction ID, timestamp, source account presence, destination account presence, amount, currency, transaction type, and metadata object presence.
- Parse amount text to `BigDecimal`; reject missing, malformed, zero, or negative values.
- Validate supported ISO 4217-style currencies for the sample scope, including at least USD, EUR, and GBP; reject unsupported values such as the synthetic invalid sample currency.
- Validate timestamp parseability as an ISO instant or offset date-time.
- Produce `validated` messages for valid records and `rejected` result-ready messages for invalid records.
- Add audit events with component name `transaction_validator`, transaction ID, safe outcome, reason code, and timestamp.
- Do not include raw account identifiers or raw descriptions in validation errors.

Edge cases:

- Invalid currency.
- Negative amount.
- Missing required field.
- Malformed amount text.
- Malformed timestamp.
- Valid non-USD sample currency.

Acceptance criteria:

- Valid records proceed to `FraudDetector`.
- Invalid records bypass risk scoring and settlement with safe rejection reason codes.
- All money parsing uses `BigDecimal`.
- Validation output can be reused by the dry-run validation seam without executing the full pipeline.

Verification:

- Run `mvn test -Dtest=TransactionValidatorTest`.
- Review test assertions for invalid currency, negative amount, missing fields, timestamp parsing, safe audit events, and no sensitive field leakage.

### Task 5: FraudDetector Component

Prompt: Implement deterministic educational risk scoring in `FraudDetector` using validated messages only. Keep the scoring transparent, testable, and free of real compliance claims.

Files to create or update:

- `src/main/java/edu/setu/transactionpipeline/component/FraudDetector.java`
- `src/main/java/edu/setu/transactionpipeline/model/FraudAssessment.java`
- `src/test/java/edu/setu/transactionpipeline/component/FraudDetectorTest.java`

Classes and methods:

- `public final class FraudDetector`
- `public PipelineMessage processMessage(PipelineMessage message)`
- `FraudAssessment assess(TransactionRecord record)`
- `int calculateRiskScore(TransactionRecord record)`
- `String classifyRisk(int score)`

Details:

- Score risk from simple educational indicators: high monetary amount, wire-transfer type, unusual early-hours timestamp, non-default channel, non-default country, and destination pattern supplied as a safe category rather than a raw identifier.
- Define explicit thresholds for `low`, `medium`, and `high` risk.
- Preserve amount as a string in JSON and use `BigDecimal` for comparisons.
- Add reason codes such as `HIGH_VALUE`, `UNUSUAL_TIME`, `WIRE_TRANSFER`, `CHANNEL_RISK`, and `COUNTRY_RISK`.
- Add audit events with component name `fraud_detector`, transaction ID, risk tier, reason-code count, and timestamp.
- Do not claim to detect real fraud, perform sanctions checks, or meet AML obligations.

Edge cases:

- Valid high-value transfer should become high or medium risk based on explicit threshold rules.
- Valid early-hours transaction should receive an unusual-time reason code.
- Missing metadata after validation should degrade safely to a reason code or safe error.
- Unknown channel or country should be categorized without copying raw metadata to logs.

Acceptance criteria:

- Validated messages receive deterministic risk scores and risk tiers.
- Rejected validation messages are not rescored.
- Risk output contains reason codes and safe audit events only.
- Tests cover low, medium, and high risk tiers.

Verification:

- Run `mvn test -Dtest=FraudDetectorTest`.
- Review score thresholds and reason-code assertions for deterministic behavior.

### Task 6: SettlementProcessor Component

Prompt: Implement `SettlementProcessor` to convert validation and risk outcomes into final educational transaction statuses.

Files to create or update:

- `src/main/java/edu/setu/transactionpipeline/component/SettlementProcessor.java`
- `src/main/java/edu/setu/transactionpipeline/model/SettlementDecision.java`
- `src/test/java/edu/setu/transactionpipeline/component/SettlementProcessorTest.java`

Classes and methods:

- `public final class SettlementProcessor`
- `public PipelineMessage processMessage(PipelineMessage message)`
- `SettlementDecision decide(PipelineMessage message)`
- `boolean shouldSettle(FraudAssessment assessment)`
- `boolean shouldRequireReview(FraudAssessment assessment)`

Details:

- Convert validation rejections to final status `rejected` with the validator reason code preserved.
- Convert low-risk valid transactions to final status `settled`.
- Convert medium-risk or high-risk valid transactions to final status `review_required`, unless the specification chooses high risk as `rejected`; make the rule explicit and testable.
- Add audit events with component name `settlement_processor`, transaction ID, final status, reason code, and timestamp.
- Write no real payment, bank, or network side effects. This is a simulated final decision only.
- Preserve safe amount string, currency, final status, reason codes, component history count, and audit event count for result writing.

Edge cases:

- Validation rejection with invalid amount.
- Validation rejection with unsupported currency.
- High-risk valid transaction.
- Missing fraud assessment on a valid message should produce safe `error` or `review_required` based on explicit policy.
- Duplicate settlement attempt should be idempotent for the same transaction message.

Acceptance criteria:

- Every message reaching settlement produces one terminal status.
- Settlement decisions are deterministic and side-effect-free.
- Results do not expose raw account identifiers or raw descriptions.
- Tests cover settled, rejected, review-required, and safe error outcomes.

Verification:

- Run `mvn test -Dtest=SettlementProcessorTest`.
- Review final status mapping and audit event assertions.

### Task 7: ReportingAgent Component And Summary Generation

Prompt: Implement `ReportingAgent` as the fourth runtime component. It must write audit-safe aggregate results, validate result completeness, and produce MCP-readable summaries.

Files to create or update:

- `src/main/java/edu/setu/transactionpipeline/component/ReportingAgent.java`
- `src/main/java/edu/setu/transactionpipeline/model/TransactionResult.java`
- `src/main/java/edu/setu/transactionpipeline/model/PipelineSummary.java`
- `src/test/java/edu/setu/transactionpipeline/component/ReportingAgentTest.java`

Classes and methods:

- `public final class ReportingAgent`
- `public TransactionResult toTransactionResult(PipelineMessage message)`
- `public PipelineSummary summarize(List<TransactionResult> results, int expectedInputCount)`
- `public void writeResults(Path resultsDir, List<TransactionResult> results, PipelineSummary summary)`
- `public void verifyCompleteness(List<TransactionResult> results, int expectedInputCount)`

Details:

- Write one `shared/results/TXN*.json` file per transaction using only safe result fields.
- Write `shared/results/summary.json` with total, settled, rejected, review_required, error, generated timestamp, completeness flag, and reason-code counts.
- Optionally write `shared/results/pipeline-status.json` with the same safe aggregate status for read-only status tooling.
- Verify that the number of terminal results equals the input count.
- Verify status counts add up to total.
- Run a final privacy check that rejects or strips raw account identifiers and raw descriptions before writing result files.
- Add audit events with component name `reporting_agent`, transaction ID for per-transaction files, safe outcome, and timestamp.

Edge cases:

- Missing final result for an input transaction.
- Duplicate transaction result.
- Inconsistent status counts.
- A result object that still contains raw sensitive fields.
- Empty input set.

Acceptance criteria:

- `summary.json` and per-transaction `TXN*.json` files are stack-neutral and readable by a Python MCP status reader.
- Reporting fails safely or marks completeness false when a transaction is missing.
- No result file contains raw account identifiers or raw descriptions.
- Tests cover count consistency, duplicate detection, missing result detection, and privacy checks.

Verification:

- Run `mvn test -Dtest=ReportingAgentTest`.
- Inspect test result fixtures for `summary.json`, `pipeline-status.json`, and `TXN*.json` shape.

### Task 8: Integrator Orchestration And File Movement

Prompt: Implement `Integrator` to prepare the run, load sample transactions, write initial messages, call each runtime component in order, move JSON files through the shared protocol directories, and ensure every input has a terminal result.

Files to create or update:

- `src/main/java/edu/setu/transactionpipeline/Integrator.java`
- `src/main/java/edu/setu/transactionpipeline/cli/PipelineOptions.java`
- `src/test/java/edu/setu/transactionpipeline/IntegratorTest.java`
- `src/test/java/edu/setu/transactionpipeline/IntegrationPipelineTest.java`

Classes and methods:

- `public static void main(String[] args)`
- `int run(PipelineOptions options)`
- `List<PipelineMessage> loadAndPrepareMessages(Path samplePath)`
- `PipelineMessage processTransaction(PipelineMessage initialMessage)`
- `void writeTerminalResults(List<PipelineMessage> terminalMessages)`

Details:

- Parse command options for sample path, shared root, archive root, dry-run validation flag, and provenance values.
- Prepare a fresh `shared/` tree and archive previous runtime output before processing.
- Write initial messages under `shared/input`.
- Move or copy current component state through `shared/processing` while a component works and `shared/output` for component outputs.
- Process components in order: `TransactionValidator`, `FraudDetector`, `SettlementProcessor`, `ReportingAgent`.
- Recover per transaction: one malformed or failing transaction should produce a safe error result while the rest continue.
- Return exit code `0` when the pipeline completes with all transactions accounted for, and nonzero only for startup-level failures such as unreadable input or unwritable directories.

Edge cases:

- Existing `shared/` tree from a prior run.
- One invalid transaction among otherwise valid inputs.
- Malformed JSON input file.
- Component exception for a single transaction.
- Unwritable results directory.
- Empty sample file.

Acceptance criteria:

- Running the integrator processes every sample transaction into `shared/results/`.
- Repeated runs archive prior output before creating new output.
- File movements leave a clear trace through the required shared directories.
- Per-transaction failures are isolated and reflected in safe result files.

Verification:

- Run `mvn test -Dtest=IntegratorTest,IntegrationPipelineTest`.
- Run `mvn exec:java`.
- Inspect `shared/results/summary.json` and a redacted sample `TXN*.json` result after a manual run.

### Task 9: Dry-Run Validation Seam

Prompt: Implement a validation-only entry point that supports a future `/validate-transactions` workflow without running fraud scoring, settlement, or reporting as the primary behavior.

Files to create or update:

- `src/main/java/edu/setu/transactionpipeline/cli/ValidateTransactionsCommand.java`
- `src/main/java/edu/setu/transactionpipeline/component/TransactionValidator.java`
- `src/main/java/edu/setu/transactionpipeline/model/ValidationReport.java`
- `src/test/java/edu/setu/transactionpipeline/cli/ValidateTransactionsCommandTest.java`

Classes and methods:

- `public final class ValidateTransactionsCommand`
- `public static void main(String[] args)`
- `ValidationReport validateOnly(Path samplePath)`
- `int run(String[] args)`

Details:

- Load the sample file and apply only `TransactionValidator` logic.
- Produce a safe validation report with total records, valid count, invalid count, reason-code counts, and transaction IDs for invalid records.
- Do not write settlement results as the primary output.
- Do not invoke `FraudDetector`, `SettlementProcessor`, or `ReportingAgent`.
- Support output to stdout and optionally a safe JSON report path.
- Avoid raw account identifiers, raw descriptions, and raw metadata values in validation output.

Edge cases:

- Invalid currency.
- Negative amount.
- Missing required field.
- Malformed JSON file.
- Empty sample file.
- Output path unwritable.

Acceptance criteria:

- Validation-only command reports sample validation failures without running the full pipeline.
- The command exits nonzero for malformed input or validation failures only if the specification chooses that behavior explicitly; otherwise it returns zero with invalid counts. The chosen policy must be documented and tested.
- Output contains reason codes and counts, not sensitive raw fields.

Verification:

- Run `mvn test -Dtest=ValidateTransactionsCommandTest`.
- Run `mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.cli.ValidateTransactionsCommand`.
- Review stdout or JSON report for safe fields only.

### Task 10: Stack-Neutral Result Shapes For Status Readers

Prompt: Define and test the exact JSON shapes written under `shared/results/` so future read-only status tooling can consume Java-produced results.

Files to create or update:

- `src/main/java/edu/setu/transactionpipeline/model/TransactionResult.java`
- `src/main/java/edu/setu/transactionpipeline/model/PipelineSummary.java`
- `src/main/java/edu/setu/transactionpipeline/io/ResultSchema.java`
- `src/test/java/edu/setu/transactionpipeline/io/ResultSchemaTest.java`

Classes and methods:

- `TransactionResult.fromTerminalMessage(PipelineMessage message)`
- `PipelineSummary.fromResults(List<TransactionResult> results, int expectedInputCount, Instant generatedAt)`
- `ResultSchema.validateTransactionResult(JsonNode node)`
- `ResultSchema.validateSummary(JsonNode node)`

Details:

- Per-transaction `TXN*.json` files must include: `transaction_id`, `status`, `reason_codes`, `amount`, `currency`, `processed_at`, `component_history_count`, `audit_event_count`, and optional safe `risk_tier`.
- `summary.json` must include: `schema_version`, `generated_at`, `total`, `settled`, `rejected`, `review_required`, `error`, `complete`, and `reason_code_counts`.
- Serialize amount as a string and counts as integers.
- Use status values consistently: `settled`, `rejected`, `review_required`, and `error`.
- Exclude source account, destination account, raw description, and raw metadata values from result JSON.
- Keep shape independent of Java class names so Python status readers can parse it.

Edge cases:

- Unknown status should fail schema validation.
- Missing amount string should fail per-transaction schema validation.
- Summary count mismatch should fail summary validation.
- Result object containing sensitive field names should fail privacy validation.

Acceptance criteria:

- Java tests define locked result-shape fixtures for `summary.json` and `TXN*.json`.
- Result schemas can be parsed by generic JSON tools without Java-specific type metadata.
- Tests reject sensitive-field leakage.

Verification:

- Run `mvn test -Dtest=ResultSchemaTest`.
- Manually inspect JSON fixtures for stack-neutral keys and no raw sensitive fields.

### Task 11: Unit And Integration Test Suite With Filesystem Isolation

Prompt: Build a JUnit Jupiter test suite that validates component behavior, full pipeline behavior, filesystem isolation, privacy controls, and result shape compatibility.

Files to create or update:

- `src/test/java/edu/setu/transactionpipeline/component/TransactionValidatorTest.java`
- `src/test/java/edu/setu/transactionpipeline/component/FraudDetectorTest.java`
- `src/test/java/edu/setu/transactionpipeline/component/SettlementProcessorTest.java`
- `src/test/java/edu/setu/transactionpipeline/component/ReportingAgentTest.java`
- `src/test/java/edu/setu/transactionpipeline/IntegrationPipelineTest.java`
- `src/test/java/edu/setu/transactionpipeline/PrivacySafetyTest.java`
- `src/test/java/edu/setu/transactionpipeline/TestFixtures.java`

Classes and methods:

- `TestFixtures.validTransaction()`
- `TestFixtures.invalidCurrencyTransaction()`
- `TestFixtures.negativeAmountTransaction()`
- `TestFixtures.tempPipelineOptions(Path tempDir)`
- JUnit test methods for each component and end-to-end pipeline flow.

Details:

- Use JUnit Jupiter `@TempDir` for all filesystem tests.
- Keep tests independent of root `shared/`, root `archive/`, and canonical runtime output.
- Cover validation failures, valid low-risk flow, medium/high-risk review flow, repeated-run archival, run provenance writing, dry-run validation, result schemas, and privacy checks.
- Assert `BigDecimal` comparisons rather than floating-point comparisons.
- Include explicit negative assertions that output JSON and logs do not contain raw account identifiers or raw descriptions.
- Use minimal fixture records with synthetic safe labels where possible; do not paste raw descriptions into tests.

Edge cases:

- Invalid currency.
- Negative amount.
- High amount.
- Unusual timestamp.
- Non-default channel or country category.
- Malformed JSON.
- Repeated run archival.
- Empty input.

Acceptance criteria:

- `mvn test` passes in a clean checkout.
- Tests do not write to canonical runtime directories.
- Coverage-relevant tests exercise each runtime component and the integrator.
- Privacy tests fail if sensitive fields leak into result or audit JSON.

Verification:

- Run `mvn test`.
- Confirm test output paths are under JUnit temporary directories.
- Review privacy test fixture strings for absence of raw account identifiers and raw descriptions.

### Task 12: Coverage, Commands, And Final Verification Gate

Prompt: Add the Maven verification commands and acceptance evidence expected for the Java Generated Transaction System Layer.

Files to create or update:

- `pom.xml`
- `README.md` or later generated Java run documentation, if selected by downstream documentation generation.
- `src/test/java/edu/setu/transactionpipeline/CoverageGateContractTest.java`

Classes and methods:

- JaCoCo Maven plugin `report` and `check` executions.
- `CoverageGateContractTest` only if useful for asserting command-level assumptions without duplicating plugin behavior.

Details:

- Use `mvn test` as the baseline test command.
- Use `mvn test jacoco:report jacoco:check` as the coverage command for Athena's temporary 75% target.
- Use `mvn exec:java` or an explicitly documented packaged `java -jar target/...jar` command for pipeline execution.
- Document that Themis (Test Generator) later owns raising coverage above 80% and adding any blocking hook behavior.
- Do not add hook setup, MCP configuration setup, screenshot capture, PR packaging, or homework automation mechanics as product implementation tasks.

Edge cases:

- Coverage below threshold should fail the JaCoCo check.
- Tests that only cover DTOs should not satisfy behavioral confidence; component and integration tests are required.
- Command documentation must not assume a global Maven cache state beyond normal dependency resolution.

Acceptance criteria:

- The Java package has clear commands for build, test, coverage, and pipeline execution.
- JaCoCo `check` enforces the configured threshold.
- The test suite covers runtime components, shared file protocol, dry-run validation, result shapes, and privacy constraints.

Verification:

- Run `mvn test`.
- Run `mvn test jacoco:report jacoco:check`.
- Run the chosen pipeline execution command and inspect safe result shapes under `shared/results/`.

## Cross-Task Verification Matrix

| Requirement | Covered by tasks | Verification |
|---|---|---|
| Maven Java project structure | 1, 12 | `mvn test`; `pom.xml` review |
| JSON envelope and shared dirs | 2, 8 | `JsonCodecTest`; `SharedDirectoryManagerTest`; integration test |
| Integrator | 3, 8 | `IntegratorTest`; `IntegrationPipelineTest`; `mvn exec:java` |
| TransactionValidator | 4, 9, 11 | `TransactionValidatorTest`; validation-only command test |
| FraudDetector | 5, 11 | `FraudDetectorTest` |
| SettlementProcessor | 6, 11 | `SettlementProcessorTest` |
| ReportingAgent | 7, 10, 11 | `ReportingAgentTest`; `ResultSchemaTest` |
| Dry-run validation seam | 4, 9 | `ValidateTransactionsCommandTest` |
| Stack-neutral result shapes | 7, 10 | `ResultSchemaTest`; manual fixture review |
| Tests and coverage | 11, 12 | `mvn test`; `mvn test jacoco:report jacoco:check` |

## Residual Risks

- Exact fraud thresholds remain a design choice for the integration author. Keep them simple, documented, and test-backed.
- Maven dependency versions should be refreshed by Hephaestus (Code Generator) with Context7 or official documentation during code generation.
- If the Java package name changes, every file path and class reference in the final specification must be updated consistently.
- Future MCP tooling should remain read-only and should consume the stack-neutral result shapes rather than Java internals.

## Recommended Next Step

Athena (Spec Writer) should integrate these task cards into the Java alternate `specification.md` low-level task section, preserving the product-only boundary and stack-specific Maven/Jackson/JUnit/JaCoCo details.

# Low-Level Tasks Handoff

Run ID: `20260621-180826-write-spec-java-hera-java-full-set`
Role: Low-Level Task Decomposition executor sub-agent
Stack: `java`
Package root: `edu.setu.transactionpipeline`

## Assigned Scope

Produce Java-specific, implementation-ready low-level task cards for a Maven transaction-processing pipeline. This handoff is run-local evidence for the preserved specification run and does not authorize canonical file edits, selection records, or product implementation outside the generated transaction system.

The task cards below are product-only. They specify the Generated Transaction System Layer: Java source, tests, Maven build, runtime components, file protocol, validator dry-run behavior, runtime provenance, and MCP-readable result shapes. They do not define runtime components named after homework automation agents and do not describe canonical selection mechanics.

## Files And Context Inspected

- `docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/run-metadata.md`
- `docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/inputs/source-context.md`
- `docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/agent-1-spec/research-notes.md`
- `docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/agent-1-spec/handoffs/domain-research-handoff.md`
- `docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/agent-1-spec/handoffs/objectives-handoff.md`
- `agent-control/write-spec/stack-profiles.md`
- `agent-control/write-spec/quality-bar.md`
- `agent-control/write-spec/transaction-system-brief.md`
- `sample-transactions.json`, inspected for structural edge cases only; raw account identifiers and descriptions are intentionally not repeated here.

## Assumptions And Fixed Decisions

- Build tool: Maven.
- Java package root: `edu.setu.transactionpipeline`.
- Source root: `src/main/java`.
- Test root: `src/test/java`.
- Runtime components: Integrator, Transaction Validator, Fraud Detector, Settlement Processor, and Reporting Agent.
- Runtime component interface: `PipelineMessage processMessage(PipelineMessage message)`.
- Money type: `BigDecimal`; `double` and `float` are forbidden for monetary amount parsing, comparison, totals, and serialization.
- JSON: Jackson `ObjectMapper`, including Java time support.
- Tests: JUnit Jupiter through Maven Surefire; Failsafe may be used for integration-test phase naming.
- Coverage: JaCoCo Maven plugin with `report` and `check`; temporary specification target is 75%.
- Pipeline command: `mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.Integrator`.
- Validator dry-run command: `mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.cli.ValidateTransactionsCommand -Dexec.args="sample-transactions.json"`.
- Final status vocabulary: `settled`, `rejected`, `review_required`, and `error`.
- Assignment-supported currencies for this run: `USD`, `EUR`, and `GBP`; unsupported sample values are rejected with `unsupported_currency`.
- Educational risk thresholds: amount `>= 10000.00` adds high-value risk, amount `>= 50000.00` requires review, UTC hour before `05:00` adds unusual-time risk, and channel/country/type metadata may add documented educational risk codes.
- Result files must be stack-neutral for a Python `mcp/server.py` reader: `shared/results/summary.json` and one safe `shared/results/TXN*.json` file per input transaction.
- Sensitive fields include account identifiers, descriptions, raw metadata values, credentials, prompt/thread content, and full source transaction payloads. Logs, audit events, validation reports, summaries, status files, and test fixtures must omit or redact them.

## Task Cards

### Task 1: Maven Project Structure And Build Contract

**Prompt:** Create the Maven Java project skeleton for the educational transaction-processing pipeline. Configure Jackson, JUnit Jupiter, Maven Surefire, Maven Failsafe if integration tests use the `*IT` naming pattern, JaCoCo `report` and `check`, and the Maven Exec Plugin command for `edu.setu.transactionpipeline.Integrator`.

**Files to create or update:**

- `pom.xml`
- `src/main/java/edu/setu/transactionpipeline/Integrator.java`
- `src/main/java/edu/setu/transactionpipeline/model/PipelineMessage.java`
- `src/test/java/edu/setu/transactionpipeline/BuildContractTest.java`

**Classes and methods to create:**

- `edu.setu.transactionpipeline.Integrator`
- `public static void main(String[] args)`
- `public int run(PipelineOptions options)`
- `edu.setu.transactionpipeline.model.PipelineMessage`

**Details:**

- Set Maven compiler source and target to Java 17 or newer.
- Add Jackson Databind and Jackson Java Time module dependencies.
- Add `org.junit.jupiter:junit-jupiter` and configure Surefire for JUnit Jupiter.
- Configure JaCoCo with `prepare-agent`, `report`, and `check` executions; the initial `check` threshold is 75% line or instruction coverage.
- Add `exec-maven-plugin` so `mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.Integrator` runs the pipeline.
- Keep the product local and file-based: no database, queue, external service, payment network, or credential dependency.

**Edge cases:**

- Java version mismatch must fail clearly through Maven compiler settings.
- JaCoCo below threshold must fail the coverage check.
- Tests must run without writing to the repository root `shared/` directory.

**Acceptance criteria:**

- `pom.xml` names Jackson, JUnit Jupiter, Surefire, JaCoCo, and Exec Plugin.
- `mvn test` discovers JUnit Jupiter tests.
- `mvn test jacoco:report jacoco:check` generates a report and enforces 75%.
- The chosen pipeline command points to `edu.setu.transactionpipeline.Integrator`.

**Verification:**

- Run `mvn test`.
- Run `mvn test jacoco:report jacoco:check`.
- Run `mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.Integrator` after the integrator tasks are complete.

### Task 2: Shared Models, Statuses, Reason Codes, And Audit Events

**Prompt:** Implement the core model classes used by all runtime components, including the shared message envelope, transaction record, status enum, reason-code enum, processing result, and audit event schema.

**Files to create or update:**

- `src/main/java/edu/setu/transactionpipeline/model/PipelineMessage.java`
- `src/main/java/edu/setu/transactionpipeline/model/TransactionRecord.java`
- `src/main/java/edu/setu/transactionpipeline/model/TransactionResult.java`
- `src/main/java/edu/setu/transactionpipeline/model/ProcessingStatus.java`
- `src/main/java/edu/setu/transactionpipeline/model/ReasonCode.java`
- `src/main/java/edu/setu/transactionpipeline/model/AuditEvent.java`
- `src/test/java/edu/setu/transactionpipeline/model/ModelSerializationTest.java`

**Classes and methods to create:**

- `public enum ProcessingStatus { SETTLED, REJECTED, REVIEW_REQUIRED, ERROR }`
- `public String ProcessingStatus.jsonValue()`
- `public record AuditEvent(Instant timestamp, String component, String transactionId, String outcome, String reasonCode)`
- `public PipelineMessage withAuditEvent(AuditEvent event)`
- `public PipelineMessage withReasonCode(ReasonCode reasonCode)`

**Details:**

- JSON status values must serialize exactly as `settled`, `rejected`, `review_required`, and `error`.
- `TransactionRecord` may retain raw input only inside private runtime memory needed for validation, but model serialization used for messages/results must omit sensitive account and description fields.
- `PipelineMessage` includes `message_id`, `timestamp`, `source_agent`, `target_agent`, `message_type`, safe `data`, `reason_codes`, `component_history`, and `audit_events`.
- Audit events include timestamp, runtime component name, transaction ID when available, safe outcome, and reason code.
- Monetary amounts are represented as strings at JSON boundaries and parsed into `BigDecimal` only by validation/scoring logic.

**Edge cases:**

- Unknown status strings must not silently map to settled.
- Blank reason codes must be rejected or normalized to a safe `processing_error`.
- Messages without transaction IDs must still support safe malformed-record audit events without exposing source payloads.

**Acceptance criteria:**

- The model layer exposes the exact final status vocabulary.
- JSON serialization never emits raw account identifiers, descriptions, or raw metadata values.
- Audit events are structured and safe.

**Verification:**

- Run `mvn test -Dtest=ModelSerializationTest`.
- Inspect serialized model fixtures for status spellings and absence of sensitive field names.

### Task 3: Jackson Codec And Shared Directory Protocol

**Prompt:** Implement a Jackson JSON codec and shared-directory manager for `shared/input`, `shared/processing`, `shared/output`, and `shared/results`, including deterministic archive behavior for repeated runs.

**Files to create or update:**

- `src/main/java/edu/setu/transactionpipeline/io/JsonCodec.java`
- `src/main/java/edu/setu/transactionpipeline/io/SharedDirectoryManager.java`
- `src/main/java/edu/setu/transactionpipeline/io/ProtocolPaths.java`
- `src/test/java/edu/setu/transactionpipeline/io/JsonCodecTest.java`
- `src/test/java/edu/setu/transactionpipeline/io/SharedDirectoryManagerTest.java`

**Classes and methods to create:**

- `public final class JsonCodec`
- `public <T> T read(Path path, Class<T> type)`
- `public void write(Path path, Object value)`
- `public JsonNode readTree(Path path)`
- `public final class SharedDirectoryManager`
- `public ProtocolPaths prepareFreshSharedTree(Path sharedRoot, Path archiveRoot)`
- `public Optional<Path> archiveExistingSharedTree(Path sharedRoot, Path archiveRoot)`
- `public Path nextArchivePath(Path archiveRoot)`

**Details:**

- `prepareFreshSharedTree` archives an existing `shared/` tree before creating a new one.
- Archive folder names are zero-padded and monotonic: the first archived tree is `archive/shared-001`, followed by `archive/shared-002`, and so on.
- Create `shared/input`, `shared/processing`, `shared/output`, and `shared/results` for every fresh run.
- Configure `ObjectMapper` for Java time values and stable pretty-printed JSON where useful.
- Reject or surface malformed JSON as safe component errors; do not write unredacted stack traces into output files.

**Edge cases:**

- Existing `shared/` with nested files.
- Existing archive gaps, such as `shared-001` and `shared-003`; choose the next highest plus one.
- Missing archive directory.
- Unwritable shared or archive paths.
- JSON containing numeric amount tokens should be rejected or normalized only through explicit validation, not silently converted through binary floating point.

**Acceptance criteria:**

- Repeated runs preserve prior runtime output in `archive/shared-001` style folders.
- Fresh protocol directories always exist before processing begins.
- JSON codec round-trips safe DTOs and preserves amount strings.

**Verification:**

- Run `mvn test -Dtest=JsonCodecTest,SharedDirectoryManagerTest`.
- Review temporary test directories to confirm archive naming and protocol directory creation.

### Task 4: Runtime Provenance Writer

**Prompt:** Implement safe runtime provenance at `shared/run-provenance.json` so each current or archived run can be traced without storing raw transaction data or hidden context.

**Files to create or update:**

- `src/main/java/edu/setu/transactionpipeline/model/RunProvenance.java`
- `src/main/java/edu/setu/transactionpipeline/io/RunProvenanceWriter.java`
- `src/test/java/edu/setu/transactionpipeline/io/RunProvenanceWriterTest.java`

**Classes and methods to create:**

- `public record RunProvenance(...)`
- `public static RunProvenance fromOptions(PipelineOptions options, Clock clock)`
- `public Path write(Path sharedRoot, RunProvenance provenance)`
- `public void assertSafe(RunProvenance provenance)`

**Details:**

- `shared/run-provenance.json` includes `schema_version`, `runtime_run_id`, `generated_at`, `source_spec_run_id`, `source_spec_path`, `source_spec_fingerprint`, `pipeline_version_id`, `pipeline_inventory_path`, and `pipeline_package_fingerprint` when supplied.
- Missing optional provenance values are written as explicit safe values such as `not-supplied`; do not guess or inspect selection records.
- The provenance writer runs after a fresh shared tree is prepared and before transaction messages are processed.
- Provenance never includes raw transactions, account identifiers, descriptions, credentials, prompt text, thread text, or full local secret paths.

**Edge cases:**

- Missing provenance command options.
- Invalid fingerprint format.
- Unwritable `shared/` root.
- Clock injection for deterministic tests.

**Acceptance criteria:**

- Every pipeline run writes `shared/run-provenance.json`.
- Archived `shared-*` folders retain the provenance from the archived run.
- Provenance contains traceability metadata only.

**Verification:**

- Run `mvn test -Dtest=RunProvenanceWriterTest`.
- Inspect test provenance JSON for safe keys only.

### Task 5: Input Loader And Initial Message Writer

**Prompt:** Implement input loading from `sample-transactions.json`, safe conversion into initial pipeline messages, and writing those messages into `shared/input`.

**Files to create or update:**

- `src/main/java/edu/setu/transactionpipeline/io/InputTransactionLoader.java`
- `src/main/java/edu/setu/transactionpipeline/io/InputMessageWriter.java`
- `src/main/java/edu/setu/transactionpipeline/model/TransactionRecord.java`
- `src/test/java/edu/setu/transactionpipeline/io/InputTransactionLoaderTest.java`
- `src/test/java/edu/setu/transactionpipeline/io/InputMessageWriterTest.java`

**Classes and methods to create:**

- `public List<TransactionRecord> load(Path samplePath)`
- `public List<PipelineMessage> toInitialMessages(List<TransactionRecord> records)`
- `public Path writeInitialMessage(ProtocolPaths paths, PipelineMessage message, int sequenceNumber)`
- `public List<Path> writeInitialMessages(ProtocolPaths paths, List<PipelineMessage> messages)`

**Details:**

- Load the sample JSON array into typed records while preserving amount text.
- Initial messages target `transaction_validator` and use deterministic filenames such as `001-TXN001-transaction-validator.json`.
- The loader validates only JSON structure enough to produce records; business validation belongs to Transaction Validator.
- Initial message payloads may include fields needed for validation but all logs and audit-ready output must redact or omit account and description values.
- A malformed file produces a startup-level safe error and nonzero exit from the integrator, unless the implementation can isolate individual malformed records safely.

**Edge cases:**

- Missing sample file.
- Empty sample array.
- Malformed JSON.
- Duplicate transaction ID.
- Unknown extra fields.
- Blank transaction ID.

**Acceptance criteria:**

- Every input record becomes one initial message or one safe startup error.
- Initial messages are written under `shared/input`.
- No generated audit or test expected-output fixture repeats raw sensitive fields.

**Verification:**

- Run `mvn test -Dtest=InputTransactionLoaderTest,InputMessageWriterTest`.
- Review fixture JSON for safe amount strings and redacted or omitted sensitive fields.

### Task 6: Transaction Validator Component

**Prompt:** Implement `TransactionValidator` as the first runtime component. It must validate required fields, parse positive monetary amounts with `BigDecimal`, enforce assignment-supported ISO 4217-style currency codes, validate timestamps, produce safe rejection reason codes, and support validator dry-run reuse.

**Files to create or update:**

- `src/main/java/edu/setu/transactionpipeline/agent/TransactionValidator.java`
- `src/main/java/edu/setu/transactionpipeline/model/ValidationResult.java`
- `src/test/java/edu/setu/transactionpipeline/agent/TransactionValidatorTest.java`

**Classes and methods to create:**

- `public final class TransactionValidator`
- `public PipelineMessage processMessage(PipelineMessage message)`
- `public ValidationResult validate(TransactionRecord record)`
- `public BigDecimal parsePositiveAmount(String amountText)`
- `public boolean isSupportedCurrency(String currencyCode)`
- `public Instant parseTimestamp(String timestampText)`

**Details:**

- Required fields: transaction ID, timestamp, source account presence, destination account presence, amount, currency, transaction type, and metadata object presence.
- Source/destination account presence is validated without exposing the values in logs, audit events, results, or summaries.
- Parse amount from string with `BigDecimal`; reject blank, malformed, zero, and negative amounts.
- Accept `USD`, `EUR`, and `GBP`; reject unsupported sample values with `unsupported_currency`.
- Parse timestamps as ISO-8601 instants or offset date-times.
- Valid records produce messages targeted to `fraud_detector`.
- Invalid records produce terminal-safe messages with status `rejected`.
- Audit component name is `transaction_validator`.

**Edge cases:**

- Unsupported currency.
- Negative amount.
- Zero amount.
- Non-numeric amount.
- Missing required field.
- Blank transaction ID.
- Malformed timestamp.
- Extra metadata.

**Acceptance criteria:**

- Valid records proceed to Fraud Detector.
- Invalid records bypass risk scoring and settlement as rejected terminal messages.
- Reason codes include `invalid_amount`, `unsupported_currency`, `missing_required_field`, `invalid_timestamp`, and `malformed_json` where applicable.
- No validation error echoes raw account identifiers or descriptions.

**Verification:**

- Run `mvn test -Dtest=TransactionValidatorTest`.
- Review assertions for `BigDecimal`, currency allowlist, timestamp parsing, safe audit events, and final status `rejected`.

### Task 7: Validator Dry-Run CLI

**Prompt:** Implement a validation-only command for future operator tooling. It must load the sample file, run only Transaction Validator logic, and emit a safe report without running fraud scoring, settlement, reporting, or the full shared-results pipeline.

**Files to create or update:**

- `src/main/java/edu/setu/transactionpipeline/cli/ValidateTransactionsCommand.java`
- `src/main/java/edu/setu/transactionpipeline/model/ValidationReport.java`
- `src/test/java/edu/setu/transactionpipeline/cli/ValidateTransactionsCommandTest.java`

**Classes and methods to create:**

- `public final class ValidateTransactionsCommand`
- `public static void main(String[] args)`
- `public int run(String[] args)`
- `public ValidationReport validateOnly(Path samplePath)`
- `public void writeReport(ValidationReport report, Optional<Path> outputPath)`

**Details:**

- Main command: `mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.cli.ValidateTransactionsCommand -Dexec.args="sample-transactions.json"`.
- Report fields: `schema_version`, `generated_at`, `total`, `valid`, `invalid`, `reason_code_counts`, and invalid transaction IDs only.
- The command may return nonzero for malformed input. For business validation failures, choose and document one behavior: either return zero with invalid counts or return nonzero with a safe validation-failed code.
- Do not create `shared/results/TXN*.json` or `summary.json` as the primary behavior.
- Do not invoke Fraud Detector, Settlement Processor, or Reporting Agent.

**Edge cases:**

- Unsupported currency.
- Negative amount.
- Missing required field.
- Malformed JSON file.
- Empty sample file.
- Unwritable optional report path.

**Acceptance criteria:**

- Dry-run validation can be invoked independently of the full pipeline.
- Output contains counts, transaction IDs, and reason codes only.
- Output omits raw account identifiers, descriptions, and raw metadata values.

**Verification:**

- Run `mvn test -Dtest=ValidateTransactionsCommandTest`.
- Run `mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.cli.ValidateTransactionsCommand -Dexec.args="sample-transactions.json"`.

### Task 8: Fraud Detector Component

**Prompt:** Implement `FraudDetector` as a deterministic educational risk-scoring component for validated messages only. Keep risk rules transparent, threshold-based, and free of real banking or compliance claims.

**Files to create or update:**

- `src/main/java/edu/setu/transactionpipeline/agent/FraudDetector.java`
- `src/main/java/edu/setu/transactionpipeline/model/FraudAssessment.java`
- `src/test/java/edu/setu/transactionpipeline/agent/FraudDetectorTest.java`

**Classes and methods to create:**

- `public final class FraudDetector`
- `public PipelineMessage processMessage(PipelineMessage message)`
- `public FraudAssessment assess(TransactionRecord record)`
- `public int calculateRiskScore(TransactionRecord record)`
- `public RiskTier classifyRisk(int score)`

**Details:**

- Use `BigDecimal` comparisons for amount thresholds.
- Recommended constants: `HIGH_VALUE_THRESHOLD = new BigDecimal("10000.00")`, `REVIEW_VALUE_THRESHOLD = new BigDecimal("50000.00")`, and early-hours cutoff `05:00` UTC.
- Add reason codes such as `high_value`, `very_high_value`, `unusual_time`, `wire_transfer_type`, `channel_risk`, and `country_risk`.
- Valid low-risk records continue toward settlement.
- Medium or high educational risk records continue with a `review_required` route marker.
- Already rejected or error messages pass through without rescoring.
- Audit component name is `fraud_detector`.

**Edge cases:**

- Amount exactly equal to each threshold.
- UTC timestamp exactly at the early-hours boundary.
- Missing optional metadata after validation.
- Multiple simultaneous risk signals.
- Unsupported status entering Fraud Detector.

**Acceptance criteria:**

- Risk scoring is deterministic and documented.
- Fraud Detector never claims real fraud detection, sanctions screening, AML, KYC, payment-network, or legal compliance.
- Risk output contains safe tier, score, and reason codes only.
- Rejected validation messages remain rejected.

**Verification:**

- Run `mvn test -Dtest=FraudDetectorTest`.
- Review threshold-boundary tests and reason-code assertions.

### Task 9: Settlement Processor Component

**Prompt:** Implement `SettlementProcessor` as the final per-transaction decision component. It converts validation and risk outcomes into `settled`, `rejected`, `review_required`, or `error` without moving money or contacting any external system.

**Files to create or update:**

- `src/main/java/edu/setu/transactionpipeline/agent/SettlementProcessor.java`
- `src/main/java/edu/setu/transactionpipeline/model/SettlementDecision.java`
- `src/test/java/edu/setu/transactionpipeline/agent/SettlementProcessorTest.java`

**Classes and methods to create:**

- `public final class SettlementProcessor`
- `public PipelineMessage processMessage(PipelineMessage message)`
- `public SettlementDecision decide(PipelineMessage message)`
- `public ProcessingStatus mapToFinalStatus(PipelineMessage message)`
- `public boolean shouldRequireReview(FraudAssessment assessment)`

**Details:**

- Validation failures become final status `rejected`.
- Low-risk valid transactions become final status `settled`.
- Medium-risk, high-risk, or very-high-value valid transactions become final status `review_required`.
- Unexpected component state becomes final status `error` with `processing_error`.
- Settlement Processor writes no balances, no payment-network calls, no account verification, and no real settlement side effects.
- Audit component name is `settlement_processor`.

**Edge cases:**

- Rejected validator message.
- Review-required fraud assessment.
- Missing fraud assessment on a valid message.
- Unknown risk tier.
- Duplicate settlement attempt for the same transaction.
- Result serialization failure.

**Acceptance criteria:**

- Every message reaching settlement receives exactly one terminal status.
- Final status values are limited to `settled`, `rejected`, `review_required`, and `error`.
- Final messages include safe amount string, currency, reason codes, component history count, and audit event count.
- No settlement output exposes raw account identifiers or descriptions.

**Verification:**

- Run `mvn test -Dtest=SettlementProcessorTest`.
- Review tests for settled, rejected, review-required, and error mapping.

### Task 10: Reporting Agent, Result Files, And Summary

**Prompt:** Implement `ReportingAgent` as the fourth runtime component. It must create stack-neutral per-transaction result files, `summary.json`, and `pipeline-status.json`, verify completeness and status-count consistency, and fail safely on privacy violations.

**Files to create or update:**

- `src/main/java/edu/setu/transactionpipeline/agent/ReportingAgent.java`
- `src/main/java/edu/setu/transactionpipeline/model/PipelineSummary.java`
- `src/main/java/edu/setu/transactionpipeline/model/PipelineStatusReport.java`
- `src/main/java/edu/setu/transactionpipeline/io/ResultWriter.java`
- `src/main/java/edu/setu/transactionpipeline/privacy/PrivacyGuard.java`
- `src/test/java/edu/setu/transactionpipeline/agent/ReportingAgentTest.java`
- `src/test/java/edu/setu/transactionpipeline/privacy/PrivacyGuardTest.java`

**Classes and methods to create:**

- `public final class ReportingAgent`
- `public TransactionResult toTransactionResult(PipelineMessage terminalMessage)`
- `public PipelineSummary summarize(List<TransactionResult> results, int expectedInputCount)`
- `public PipelineStatusReport buildStatusReport(PipelineSummary summary)`
- `public void writeResults(Path resultsDir, List<TransactionResult> results, PipelineSummary summary)`
- `public void verifyCompleteness(List<TransactionResult> results, int expectedInputCount)`
- `public void verifyPrivacySafe(JsonNode node)`

**Details:**

- Write one `shared/results/TXN*.json` file per input transaction.
- Each per-transaction result includes `schema_version`, `transaction_id`, `status`, `reason_codes`, `amount`, `currency`, `processed_at`, `risk_tier`, `component_history_count`, and `audit_event_count`.
- Write `shared/results/summary.json` with `schema_version`, `generated_at`, `total`, `settled`, `rejected`, `review_required`, `error`, `complete`, and `reason_code_counts`.
- Write `shared/results/pipeline-status.json` with safe aggregate fields for read-only status tooling.
- Ensure counts add up and expected input count equals terminal result count.
- Privacy Guard rejects prohibited keys and account-shaped or description-shaped values before files are written.
- Audit component name is `reporting_agent`.

**Edge cases:**

- Missing terminal result.
- Duplicate transaction result.
- Unknown final status.
- Count mismatch.
- Empty input.
- Result object containing prohibited sensitive fields.
- Unwritable results directory.

**Acceptance criteria:**

- The Python `mcp/server.py` reader can consume `summary.json` and `TXN*.json` without Java-specific type metadata.
- `summary.json` uses the final status vocabulary exactly: `settled`, `rejected`, `review_required`, `error`.
- Reporting fails safely or marks completeness false when results are missing or inconsistent.
- Result files contain no raw account identifiers, descriptions, or raw metadata values.

**Verification:**

- Run `mvn test -Dtest=ReportingAgentTest,PrivacyGuardTest`.
- Inspect result fixtures for stack-neutral keys and privacy-safe content.

### Task 11: Integrator Orchestration And File Movement

**Prompt:** Implement `Integrator` to prepare the runtime tree, archive prior output, write provenance, load sample transactions, move messages through protocol directories, invoke Transaction Validator, Fraud Detector, Settlement Processor, and Reporting Agent, and ensure every input has a final result.

**Files to create or update:**

- `src/main/java/edu/setu/transactionpipeline/Integrator.java`
- `src/main/java/edu/setu/transactionpipeline/cli/PipelineOptions.java`
- `src/main/java/edu/setu/transactionpipeline/io/ProtocolFileMover.java`
- `src/test/java/edu/setu/transactionpipeline/IntegratorTest.java`
- `src/test/java/edu/setu/transactionpipeline/IntegrationPipelineTest.java`

**Classes and methods to create:**

- `public static void main(String[] args)`
- `public int run(PipelineOptions options)`
- `public List<PipelineMessage> loadAndPrepareMessages(Path samplePath)`
- `public PipelineMessage processTransaction(PipelineMessage initialMessage)`
- `public void writeProtocolState(ProtocolPaths paths, PipelineMessage message, String componentName, String phase)`
- `public PipelineSummary finalizeRun(List<PipelineMessage> terminalMessages, int expectedInputCount)`

**Details:**

- Parse options for sample path, shared root, archive root, dry-run flag, and provenance values.
- For normal pipeline mode, prepare `shared/`, archive any previous `shared/` tree, write `shared/run-provenance.json`, and create protocol directories.
- Write initial messages into `shared/input`.
- During component work, write or move current state into `shared/processing`.
- After each component output, write safe messages into `shared/output`.
- Reporting Agent writes terminal results into `shared/results`.
- Per-transaction component exceptions become safe `error` terminal messages while unrelated transactions continue.
- Startup-level failures, such as unreadable input or unwritable root directory, return nonzero and do not emit unsafe details.

**Edge cases:**

- Existing `shared/` tree requires archive to `archive/shared-001` or next available zero-padded folder.
- One invalid transaction among otherwise valid input.
- Component exception for one transaction.
- Malformed input file.
- Unwritable results directory.
- Duplicate transaction ID.
- Empty sample array.

**Acceptance criteria:**

- Full pipeline accounts for every input transaction.
- Repeated runs visibly archive prior `shared/` output before creating fresh output.
- The required directory protocol is visible: `shared/input`, `shared/processing`, `shared/output`, `shared/results`.
- `shared/run-provenance.json`, `shared/results/summary.json`, `shared/results/pipeline-status.json`, and `shared/results/TXN*.json` are produced for successful runs.

**Verification:**

- Run `mvn test -Dtest=IntegratorTest,IntegrationPipelineTest`.
- Run `mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.Integrator`.
- Inspect `shared/results/summary.json`, `shared/results/pipeline-status.json`, and a representative `TXN*.json` for safe result shape.

### Task 12: Result Schema Lock For Python MCP Reader

**Prompt:** Lock the stack-neutral JSON result schemas expected by a future Python `mcp/server.py` reader for `get_transaction_status`, `list_pipeline_results`, and `pipeline://summary`.

**Files to create or update:**

- `src/main/java/edu/setu/transactionpipeline/io/ResultSchema.java`
- `src/main/java/edu/setu/transactionpipeline/model/TransactionResult.java`
- `src/main/java/edu/setu/transactionpipeline/model/PipelineSummary.java`
- `src/test/java/edu/setu/transactionpipeline/io/ResultSchemaTest.java`

**Classes and methods to create:**

- `public final class ResultSchema`
- `public void validateTransactionResult(JsonNode node)`
- `public void validateSummary(JsonNode node)`
- `public void validatePipelineStatus(JsonNode node)`
- `public boolean containsSensitiveField(JsonNode node)`

**Details:**

- Per-transaction files are named `TXN*.json` and contain only generic JSON objects, not Java class metadata.
- Required per-transaction keys: `schema_version`, `transaction_id`, `status`, `reason_codes`, `amount`, `currency`, `processed_at`, `risk_tier`, `component_history_count`, and `audit_event_count`.
- Required summary keys: `schema_version`, `generated_at`, `total`, `settled`, `rejected`, `review_required`, `error`, `complete`, and `reason_code_counts`.
- Required pipeline-status keys: `schema_version`, `generated_at`, `ready`, `summary_path`, `results_path`, `total`, `settled`, `rejected`, `review_required`, `error`, and `complete`.
- Amount is always a string.
- Counts are integers.
- Status is one of `settled`, `rejected`, `review_required`, or `error`.
- Prohibited keys include source account, destination account, description, raw metadata, credentials, prompt text, and raw transaction payload fields.

**Edge cases:**

- Unknown status.
- Missing amount string.
- Numeric amount token.
- Summary count mismatch.
- Extra Java-specific type metadata.
- Sensitive field name or sensitive-looking value.

**Acceptance criteria:**

- Result schemas can be parsed by generic Python JSON code.
- Schema tests reject unknown statuses, count mismatches, numeric money output, Java-specific metadata, and sensitive leakage.
- The result shape supports read-only status tooling without rerunning the Java pipeline.

**Verification:**

- Run `mvn test -Dtest=ResultSchemaTest`.
- Manually compare JSON fixtures with the documented Python-reader keys.

### Task 13: JUnit Unit And Integration Tests With Filesystem Isolation

**Prompt:** Build the JUnit Jupiter test suite for runtime components, protocol utilities, dry-run validation, full-pipeline behavior, archive behavior, provenance, result schemas, and privacy controls.

**Files to create or update:**

- `src/test/java/edu/setu/transactionpipeline/TestFixtures.java`
- `src/test/java/edu/setu/transactionpipeline/agent/TransactionValidatorTest.java`
- `src/test/java/edu/setu/transactionpipeline/agent/FraudDetectorTest.java`
- `src/test/java/edu/setu/transactionpipeline/agent/SettlementProcessorTest.java`
- `src/test/java/edu/setu/transactionpipeline/agent/ReportingAgentTest.java`
- `src/test/java/edu/setu/transactionpipeline/IntegratorTest.java`
- `src/test/java/edu/setu/transactionpipeline/IntegrationPipelineTest.java`
- `src/test/java/edu/setu/transactionpipeline/PrivacySafetyTest.java`

**Classes and methods to create:**

- `public final class TestFixtures`
- `public static TransactionRecord validLowRiskTransaction()`
- `public static TransactionRecord invalidCurrencyTransaction()`
- `public static TransactionRecord negativeAmountTransaction()`
- `public static TransactionRecord highValueTransaction()`
- `public static PipelineOptions tempPipelineOptions(Path tempDir)`
- JUnit methods using `@TempDir` for every filesystem test.

**Details:**

- Use `@TempDir` so tests never write into repository root `shared/`, `archive/`, or canonical runtime output.
- Cover valid low-risk settlement, invalid currency rejection, negative amount rejection, high-value review, early-hours review, malformed JSON, repeated-run archival, provenance writing, dry-run validation, result schema, and privacy guard failures.
- Assert `BigDecimal` behavior for amounts; do not assert through `double` or `float`.
- Include negative assertions that result JSON, audit JSON, validation reports, summaries, and logs do not contain raw account identifiers or raw descriptions.
- Use safe synthetic fixture values rather than copying raw sample descriptions into test names or expected strings.

**Edge cases:**

- Unsupported currency.
- Negative amount.
- Very high amount.
- Early-hours timestamp.
- Missing optional metadata.
- Malformed JSON.
- Empty input.
- Duplicate transaction ID.
- Repeated run archive sequence.

**Acceptance criteria:**

- `mvn test` passes in a clean checkout.
- Tests cover all four runtime components plus the Integrator.
- Tests isolate filesystem state.
- Privacy tests fail if sensitive fields leak into result or audit JSON.

**Verification:**

- Run `mvn test`.
- Confirm any generated test files live under JUnit temporary directories.
- Review fixture strings for sensitive-field safety.

### Task 14: Final Java Verification Commands And Evidence Contract

**Prompt:** Define the final verification commands and evidence expectations for the Java transaction-processing package without adding homework automation mechanics as product behavior.

**Files to create or update:**

- `pom.xml`
- `src/test/java/edu/setu/transactionpipeline/CoverageGateContractTest.java`, only if a lightweight command-contract test is useful.
- Later generated product documentation may mention these commands, but this task does not require editing canonical documentation.

**Classes and methods to create:**

- JaCoCo Maven plugin `report` and `check` executions in `pom.xml`.
- Optional `CoverageGateContractTest` that verifies test assumptions without duplicating plugin internals.

**Details:**

- Baseline tests: `mvn test`.
- Coverage gate: `mvn test jacoco:report jacoco:check`.
- Pipeline run: `mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.Integrator`.
- Validator dry run: `mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.cli.ValidateTransactionsCommand -Dexec.args="sample-transactions.json"`.
- Manual post-run inspection targets: `shared/run-provenance.json`, `shared/results/summary.json`, `shared/results/pipeline-status.json`, and `shared/results/TXN*.json`.
- Coverage target in this specification phase is 75%; later test-hardening work may raise it.
- Do not add Git hook setup, MCP configuration setup, screenshot capture, PR packaging, or run-selection mechanics to the Java product tasks.

**Edge cases:**

- Coverage below threshold.
- Tests that exercise only DTOs but not runtime components.
- Missing Exec Plugin configuration.
- Pipeline command fails before writing safe startup error.
- Validator dry run accidentally triggers full pipeline output.

**Acceptance criteria:**

- Java package has clear build, test, coverage, full-run, and dry-run commands.
- JaCoCo `check` enforces the configured threshold.
- Manual result inspection confirms the Python-reader-compatible JSON shape.
- Product tasks remain local, deterministic, educational, and privacy-safe.

**Verification:**

- Run `mvn test`.
- Run `mvn test jacoco:report jacoco:check`.
- Run `mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.Integrator`.
- Run `mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.cli.ValidateTransactionsCommand -Dexec.args="sample-transactions.json"`.

## Cross-Task Verification Matrix

| Requirement | Covered by tasks | Verification |
|---|---|---|
| Maven `pom.xml`, Java source/test layout | 1, 14 | `mvn test`; `pom.xml` review |
| Jackson JSON handling | 2, 3, 12 | `JsonCodecTest`; `ModelSerializationTest`; `ResultSchemaTest` |
| `BigDecimal` money handling | 2, 6, 8, 13 | Validator and fraud tests; source review for no `double`/`float` money |
| Shared protocol directories | 3, 5, 11 | `SharedDirectoryManagerTest`; `IntegrationPipelineTest` |
| `archive/shared-001` repeated-run behavior | 3, 11, 13 | Archive tests; full integration repeated-run test |
| `shared/run-provenance.json` | 4, 11 | `RunProvenanceWriterTest`; full pipeline run inspection |
| Transaction Validator | 6, 7, 13 | `TransactionValidatorTest`; dry-run CLI test |
| Fraud Detector | 8, 13 | `FraudDetectorTest` |
| Settlement Processor | 9, 13 | `SettlementProcessorTest` |
| Reporting Agent | 10, 12, 13 | `ReportingAgentTest`; `ResultSchemaTest`; privacy tests |
| Final statuses | 2, 9, 10, 12 | Model serialization, settlement, reporting, schema tests |
| `summary.json` and `TXN*.json` for Python reader | 10, 12 | `ResultSchemaTest`; manual JSON review |
| Maven Exec pipeline command | 1, 11, 14 | `mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.Integrator` |
| JaCoCo report/check | 1, 14 | `mvn test jacoco:report jacoco:check` |
| Privacy-safe output | 2, 4, 6, 7, 10, 12, 13 | Privacy tests; fixture review |

## Residual Risks

- Maven dependency versions should be refreshed during code generation using current library documentation.
- The exact risk-score weights can be tuned by the implementation author, but thresholds and expected routing must remain deterministic and test-backed.
- The Python status reader is future read-only tooling; the Java product must lock generic JSON shapes rather than depend on Python internals.
- If the package root changes, every file path and class reference in the final specification must be updated consistently.

## Recommended Next Step

Integrate these task cards into the Java candidate `specification.md` low-level task section, preserving the package root `edu.setu.transactionpipeline`, the final status vocabulary, Maven/Jackson/JUnit/JaCoCo details, validator dry-run seam, archive/provenance behavior, Reporting Agent responsibilities, and stack-neutral result shapes.

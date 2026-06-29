# Java Transaction Processing Pipeline Specification

## 1. High-Level Objective

Build a Java Maven transaction-processing pipeline that reads synthetic transaction records, validates them, scores review risk, simulates safe settlement, and writes audit-safe JSON results for every transaction.

## 2. Mid-Level Objectives

1. Validate every input transaction using Java `BigDecimal` for money, ISO 4217-style currency rules, required-field checks, and deterministic reason codes.
2. Route valid transactions through `FraudDetector`, `SettlementProcessor`, and `ReportingAgent` components using the shared JSON file protocol.
3. Write one safe `shared/results/TXN*.json` file per transaction plus `shared/results/summary.json` with total, settled, rejected, review-required, and error counts.
4. Archive any existing `shared/` tree to the next zero-padded `archive/shared-###` folder before each full run.
5. Provide JUnit Jupiter and JaCoCo-ready test seams so Themis (Test Generator) can verify at least 80 percent coverage without touching canonical Python outputs.

## 3. Implementation Notes

- Use Maven with `pom.xml`, Java 17 source/target, Jackson Databind, JUnit Jupiter, Maven Surefire, and JaCoCo.
- Use `BigDecimal` for all money parsing, thresholds, comparisons, summaries, and tests. Do not use `double` or `float` for transaction amounts.
- Serialize money back to JSON as the original safe amount string or a plain decimal string.
- Accept at least `USD`, `EUR`, `GBP`, and `JPY`; reject unsupported currency values with `UNSUPPORTED_CURRENCY`.
- Reject non-positive amounts with `NON_POSITIVE_AMOUNT`; reject malformed money with `INVALID_AMOUNT`.
- Treat account identifiers, descriptions, and metadata as sensitive. Do not write raw account IDs, raw descriptions, or full metadata dumps to result files, logs, screenshots, or evidence.
- Audit events should carry timestamp, runtime component name, transaction ID, safe outcome, and reason code. Reviewer-facing summaries should prefer counts and reason codes.
- Frame the pipeline as an educational simulation only. Do not claim real banking, AML, sanctions, KYC, payment-network, PCI, legal, or regulatory compliance.
- The Java result JSON must remain compatible with the existing Python `mcp/server.py` status reader by keeping safe fields under `shared/results/summary.json` and `shared/results/TXN*.json`.

## 4. Context

Beginning state:

- `sample-transactions.json` contains eight synthetic transaction records.
- The Java alternate is generated under preserved run folders, not canonical root Java paths.
- Python remains the canonical package set until a later explicit `select-set`.

Ending state:

- `agent-2-code/outputs/` contains a runnable Maven Java project.
- Running `mvn exec:java '-Dexec.args=--input sample-transactions.json --shared-dir shared'` from the Java package processes all eight transactions.
- `shared/results/` contains one safe transaction result per input transaction plus `summary.json`.
- Repeated runs archive prior Java run-local `shared/` output under `archive/shared-###`.
- Themis adds or overlays JUnit tests so `mvn test jacoco:report jacoco:check` meets the 80 percent Java coverage gate.

## 5. Low-Level Tasks

### Task: Maven Project Structure

Prompt: "Create a Java 17 Maven project for the Homework 6 transaction-processing pipeline. Use Jackson Databind, JUnit Jupiter, Maven Surefire, exec-maven-plugin, and JaCoCo with an 80 percent covered-ratio check."

File to CREATE: `pom.xml`

Function to CREATE: Maven lifecycle and plugin configuration.

Details: Declare Java 17 compiler settings, Jackson dependency, JUnit Jupiter test dependency, `exec:java` main class, Surefire test execution, and JaCoCo report/check goals.

Edge cases: direct `jacoco:check` invocation must see rules; build must not require custom local repository settings in the submitted package.

Acceptance criteria: `mvn test` compiles and runs JUnit Jupiter tests; `mvn test jacoco:report jacoco:check` can fail or pass according to the configured threshold.

Verification: `mvn test` and `mvn test jacoco:report jacoco:check`.

### Task: Shared JSON Protocol

Prompt: "Implement Java helpers for strict JSON reading/writing and shared directory preparation for the transaction file protocol."

File to CREATE: `src/main/java/edu/setu/banking/pipeline/util/JsonSupport.java`, `DirectorySupport.java`

Function to CREATE: `JsonSupport.read`, `JsonSupport.write`, `DirectorySupport.prepareShared`, `DirectorySupport.deleteTree`

Details: Use Jackson `ObjectMapper`, strict safe JSON writes, `BigDecimal`-aware parsing, and protocol directories `shared/input`, `shared/processing`, `shared/output`, and `shared/results`. Move existing shared output to the next `archive/shared-###` folder before a new run.

Edge cases: missing directories, repeated runs, nested cleanup, invalid JSON.

Acceptance criteria: fresh protocol directories are created and repeated runs preserve prior output.

Verification: JUnit tests with temporary directories and a two-run integration test.

### Task: Transaction Validator

Prompt: "Implement a Java TransactionValidator component that rejects malformed or unsupported transactions with safe reason codes."

File to CREATE: `src/main/java/edu/setu/banking/pipeline/agent/TransactionValidator.java`

Function to CREATE: `ProcessingResult process(TransactionRecord record)`

Details: Validate required fields, parse amount using `BigDecimal`, reject non-positive amount, reject unsupported currency, and write safe audit events.

Edge cases: null record, missing transaction ID, missing timestamp, malformed amount, unsupported currency, negative amount.

Acceptance criteria: unsupported sample currency rejects with `UNSUPPORTED_CURRENCY`; negative sample amount rejects with `NON_POSITIVE_AMOUNT`.

Verification: `TransactionValidatorTest` and Themis quality tests.

### Task: Fraud Detector

Prompt: "Implement a deterministic educational FraudDetector component for review signals without claiming real fraud or compliance screening."

File to CREATE: `src/main/java/edu/setu/banking/pipeline/agent/FraudDetector.java`

Function to CREATE: `ProcessingResult process(TransactionRecord record, ProcessingResult result)`

Details: Add review reason codes for high value, very high value, odd-hour timestamp, cross-border metadata, and destination-pattern signals. Skip rejected records safely.

Edge cases: already rejected transaction, malformed timestamp, missing metadata, non-US country.

Acceptance criteria: high-value and odd-hour sample transactions are marked `review_required`.

Verification: `FraudDetectorTest`.

### Task: Settlement Processor

Prompt: "Implement a Java SettlementProcessor that simulates settlement only for low-risk validated transactions."

File to CREATE: `src/main/java/edu/setu/banking/pipeline/agent/SettlementProcessor.java`

Function to CREATE: `ProcessingResult process(ProcessingResult result)`

Details: Settle low-risk transactions with a synthetic settlement reference, hold review-required transactions, leave rejected transactions unsettled, and mark unknown states as `error`.

Edge cases: rejected input, review-required input, unexpected status.

Acceptance criteria: at least one low-risk transaction receives a simulated reference and review-required transactions are not settled.

Verification: `SettlementAndReportingTest`.

### Task: Reporting Agent

Prompt: "Implement a Java ReportingAgent that writes audit-safe aggregate counts and reason-code groups."

File to CREATE: `src/main/java/edu/setu/banking/pipeline/agent/ReportingAgent.java`

Function to CREATE: `Summary summarize(List<ProcessingResult> results)`

Details: Count settled, rejected, review-required, and error statuses; aggregate reason codes only.

Edge cases: unknown status, multiple reason codes, empty result list.

Acceptance criteria: `summary.json` accounts for all input records and exposes safe aggregate fields only.

Verification: JUnit summary tests and pipeline evidence.

### Task: Integrator

Prompt: "Implement a Java Integrator that orchestrates the validator, fraud detector, settlement processor, and reporting agent through the shared JSON file protocol."

File to CREATE: `src/main/java/edu/setu/banking/pipeline/Integrator.java`

Function to CREATE: `main(String[] args)`, `run(Path input, Path sharedDir)`, `validateOnly(Path input)`

Details: Parse `--input`, `--shared-dir`, and `--dry-run`; prepare protocol directories; load records; write stage files; write safe final results and summary; emit safe console counts.

Edge cases: missing input file, unsupported CLI argument, repeated run, result completeness mismatch.

Acceptance criteria: Java command processes all eight sample transactions and writes safe result files.

Verification: `mvn exec:java '-Dexec.args=--input sample-transactions.json --shared-dir shared'`.

### Task: Themis Test And Coverage Seam

Prompt: "Create Java JUnit Jupiter tests that cover unit components, integration behavior, dry-run validation, privacy, fixture isolation, repeated-run archival, and JaCoCo coverage gate behavior."

File to CREATE: `src/test/java/edu/setu/banking/pipeline/*.java`

Function to CREATE: JUnit Jupiter test methods for each runtime component and integration path.

Details: Use `@TempDir` for filesystem isolation, avoid root `shared/`, assert safe fields and reason codes, and verify no raw account IDs or raw descriptions appear in safe command responses.

Edge cases: null transaction, malformed amount, unknown status, missing input path, unsupported CLI argument.

Acceptance criteria: Themis candidate suite passes with 80 percent or higher JaCoCo instruction coverage.

Verification: `mvn test jacoco:report jacoco:check`.

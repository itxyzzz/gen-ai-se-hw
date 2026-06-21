# Objectives Handoff

Run ID: `20260621-180826-write-spec-java-hera-java-full-set`
Role: Objectives Architect executor sub-agent for Athena (Spec Writer)
Selected stack: `java`

## Assigned Scope

Propose one high-level objective and 4-5 testable mid-level objectives for a Java Maven transaction-processing pipeline. The objectives must cover the Integrator, Transaction Validator, Fraud Detector, Settlement Processor, and Reporting Agent as stack-native runtime product components.

This handoff is product-only. It does not authorize canonical file edits, final selection, Homework Automation Layer mechanics, or runtime implementation of Athena, Hephaestus, Themis, Clio, Hera, harness workflows, screenshots, PR support, or selection workflows.

## Files And Context Inspected

- `agent-1-spec/handoffs/domain-research-handoff.md`
- `run-metadata.md`
- `inputs/source-context.md`
- `agent-control/write-spec/transaction-system-brief.md`
- `agent-control/write-spec/stack-profiles.md`
- `agent-control/write-spec/quality-bar.md`
- `agent-control/write-spec/workflow.md`
- `agent-control/write-spec/run-registry.md`

## Sources And Commands Used

- Local source reads with `Get-Content -Raw` for the required run and write-spec control files.
- Local path existence check with `Test-Path` for the target handoff directory.
- No web or Context7 research was performed in this objectives pass. Use the run-local domain research handoff's limitations for source classification.

## Assumptions

- Java package root may be finalized by the low-level task decomposer, but objectives assume a Maven layout with `pom.xml`, `src/main/java/...`, and `src/test/java/...`.
- JSON handling should use Jackson or an equivalent library, with the low-level task cards choosing exact dependencies and versions.
- Money is parsed, compared, and serialized using `BigDecimal`; JSON output stores amounts as strings.
- Currency validation is ISO 4217-style and assignment-bounded unless later cited research supports a fuller registry implementation.
- Deterministic educational fraud thresholds should be specified as constants in the generated spec and verified by tests.
- Status vocabulary should be finite and implementation-ready: `accepted`, `rejected`, `review_required`, and `error`.

## High-Level Objective

| ID | Objective | Observable Success | Java/Maven Mapping | Edge/Failure Coverage | Privacy/Audit Coverage |
|---|---|---|---|---|---|
| `HLO-001` | Build an educational Java Maven transaction-processing pipeline that processes every sample transaction through an Integrator, Transaction Validator, Fraud Detector, Settlement Processor, and Reporting Agent using the required JSON file protocol, precise money semantics, deterministic outcomes, and audit-safe result artifacts. | A fresh pipeline run produces a complete `shared/` tree with protocol directories, `shared/run-provenance.json`, one safe per-transaction result for every input transaction, `shared/results/summary.json`, and `shared/results/pipeline-status.json`; `mvn test jacoco:report jacoco:check` can verify the temporary 75% coverage target. | Maven project; `pom.xml`; `Integrator.java` with `main(String[] args)` or equivalent command entry; runtime component classes implementing `PipelineMessage processMessage(PipelineMessage message)`; Jackson JSON mapping; JUnit 5/JUnit Jupiter; Maven Surefire/Failsafe as needed; JaCoCo report/check goals. | Existing `shared/` tree archival, malformed input file, invalid transaction fields, unsupported currency, non-positive amount, high-risk routing, downstream component exception, duplicate or missing result, repeated-run idempotence. | Audit events contain timestamp, component name, transaction ID, safe status/outcome, and reason code. Logs, result files, summaries, and provenance omit raw account identifiers, raw descriptions, credentials, and unnecessary metadata. |

## Mid-Level Objectives

### `MLO-001` - Integrator, Run Setup, And JSON Protocol

**Objective:** Implement the Integrator as the Java application entry point that prepares a fresh protocol tree, archives any prior `shared/` runtime evidence, loads the sample input, writes safe message envelopes, invokes runtime components in order, and verifies that every input transaction has a final result.

**Observable Success:** Running the selected Maven command, preferably `mvn exec:java` or an equivalent packaged command chosen in the spec, creates `shared/input`, `shared/processing`, `shared/output`, `shared/results`, and `shared/run-provenance.json`; repeated runs move the previous `shared/` tree into the next zero-padded archive folder before creating a new one; result count equals input count.

**Java/Maven Mapping:**

- `src/main/java/.../Integrator.java` owns `main(String[] args)`, directory preparation, input loading, component orchestration, and completeness checks.
- Shared model classes should include `PipelineMessage`, `TransactionRecord`, `ProcessingResult`, `AuditEvent`, and `RunProvenance`.
- Jackson `ObjectMapper` configuration should serialize decimal amounts as strings and reject or surface malformed JSON safely.
- Tests should use JUnit 5 temporary directories rather than the real root `shared/` tree.

**Edge/Failure Coverage:**

- Existing `shared/` folder must archive deterministically.
- Missing sample input should fail with safe `error` status and a non-sensitive message.
- Malformed JSON files should not stop processing of unrelated records.
- Component exceptions should become safe per-transaction error results.
- Duplicate transaction identifiers or output filename collisions must be handled deterministically.

**Privacy/Audit Coverage:**

- `shared/run-provenance.json` contains only schema version, runtime run ID, timestamp, source spec reference, package/version reference, paths, and fingerprints supplied to the runtime.
- Integrator logs and audit entries must not include account fields, raw descriptions, full source payloads, or hidden prompt/thread content.

### `MLO-002` - Transaction Validator

**Objective:** Implement Transaction Validator as the first runtime component that turns raw transaction records into validated pipeline messages or safe rejected results based on required fields, parseable positive `BigDecimal` amounts, ISO 4217-style currency checks, transaction ID presence, and parseable timestamps.

**Observable Success:** Valid sample-shaped transactions receive validated messages for Fraud Detector; invalid amount, unsupported currency, missing required field, blank identifier, unparsable timestamp, and malformed record cases become rejected or error results with stable reason codes.

**Java/Maven Mapping:**

- `src/main/java/.../components/TransactionValidator.java` should expose `PipelineMessage processMessage(PipelineMessage message)`.
- Money parsing uses `BigDecimal` from string input and rejects `double` or `float` amount paths.
- Currency validation uses an explicit assignment-supported allowlist or a documented Java validation helper chosen by the final spec.
- Unit tests should target validator methods and JSON boundary behavior through JUnit 5.

**Edge/Failure Coverage:**

- Negative, zero, blank, or non-numeric amounts.
- Unsupported currency such as the sample invalid code.
- Missing transaction ID, timestamp, amount, currency, type, or account fields.
- Unexpected extra metadata should be ignored or retained only for internal scoring, not emitted unsafely.
- Timestamps must use a deterministic parse rule, preferably ISO 8601 instant parsing.

**Privacy/Audit Coverage:**

- Validation failures emit reason codes such as `invalid_amount`, `unsupported_currency`, `missing_required_field`, `invalid_timestamp`, and `malformed_json` without echoing sensitive input values.
- Audit events record component identity, transaction ID when available, outcome, and reason code only.

### `MLO-003` - Fraud Detector

**Objective:** Implement Fraud Detector as a deterministic educational risk-scoring component that evaluates only validated messages and assigns a risk score/category using documented heuristics for high amount, very high amount, early-hours timestamp, channel, country metadata, and transfer type.

**Observable Success:** Valid low-risk transactions continue toward settlement as acceptable; high-risk or very-high-risk transactions receive a stable `review_required` route; tests prove threshold boundaries and early-hours behavior without claiming real fraud, AML, sanctions, or regulatory compliance.

**Java/Maven Mapping:**

- `src/main/java/.../components/FraudDetector.java` should expose `PipelineMessage processMessage(PipelineMessage message)`.
- Thresholds should live as named constants or configuration values, using `BigDecimal` comparisons for amount-based rules.
- Risk outputs should be represented in safe fields such as risk score, risk category, and reason codes.
- Unit tests should cover threshold edges and deterministic scoring.

**Edge/Failure Coverage:**

- Amount exactly at each threshold.
- Early-hours timestamp boundary, with the spec choosing UTC hour unless another deterministic rule is explicitly selected.
- Missing optional metadata on otherwise valid transactions.
- Multiple simultaneous risk signals.
- Fraud Detector receiving an already rejected or malformed message should pass it through safely without reclassifying it as accepted.

**Privacy/Audit Coverage:**

- Risk reason codes must avoid raw account data and descriptions.
- Metadata used internally for scoring should not be copied into final result payloads unless explicitly classified safe by the spec.
- Audit entries should state risk category and safe reason codes, not source or destination details.

### `MLO-004` - Settlement Processor

**Objective:** Implement Settlement Processor as the final decision component for individual transactions, converting validation and risk outcomes into safe final statuses without performing real money movement, balance updates, payment execution, account verification, or network integration.

**Observable Success:** Every transaction entering Settlement Processor receives exactly one final per-transaction result file with status `accepted`, `rejected`, `review_required`, or `error`; accepted and review-required results include safe processing summaries, and rejected/error results include stable reason codes.

**Java/Maven Mapping:**

- `src/main/java/.../components/SettlementProcessor.java` should expose `PipelineMessage processMessage(PipelineMessage message)`.
- Result models should serialize to stack-neutral JSON consumed later by the Python status reader.
- Integration tests should run the validator-to-fraud-to-settlement path in a temporary filesystem.

**Edge/Failure Coverage:**

- Rejected validator messages must remain rejected.
- Review-required fraud outcomes must not be accepted.
- Failed result-file write should produce a recoverable component-level error path where possible.
- Duplicate final result attempts must be deterministic and detectable.
- Unknown status or unknown risk category should fail safe as `error` or `review_required`, as finalized by the spec.

**Privacy/Audit Coverage:**

- Final per-transaction result files may include transaction ID, amount string, currency, status, reason codes, risk category, timestamps, component history count, and audit event count.
- Final result files must omit raw account identifiers, descriptions, and unnecessary metadata.
- Audit events distinguish educational simulated settlement decisions from real financial settlement.

### `MLO-005` - Reporting Agent, Summary, And MCP-Readable Result Shape

**Objective:** Implement Reporting Agent as the fourth runtime component that verifies result completeness and count consistency, performs a final privacy check over result artifacts, and writes audit-safe aggregate files for status tooling.

**Observable Success:** `shared/results/summary.json` and `shared/results/pipeline-status.json` contain safe aggregate counts for total, accepted, rejected, review-required, and error outcomes; the counts match the per-transaction result files; privacy checks fail safely if prohibited fields appear in result payloads.

**Java/Maven Mapping:**

- `src/main/java/.../components/ReportingAgent.java` should expose `PipelineMessage processMessage(PipelineMessage message)` or a reporting method invoked by `Integrator` after per-transaction processing.
- Summary models should be Jackson-serializable and stack-neutral for the future Python `mcp/server.py` reader.
- Tests should verify aggregate count consistency, missing result detection, and prohibited-field detection.
- JaCoCo coverage should include the reporting path because it is part of the product quality gate.

**Edge/Failure Coverage:**

- Missing per-transaction result file.
- Extra per-transaction result file not tied to current input.
- Status count mismatch.
- Empty input file or all transactions rejected.
- Result payload containing a prohibited sensitive field should trigger a safe reporting failure.

**Privacy/Audit Coverage:**

- Summary and status files contain aggregate counts and safe identifiers only.
- Reporting Agent must not include account fields, descriptions, raw metadata, or raw transaction payload excerpts.
- Reporting audit entries should record aggregate consistency status, privacy-check outcome, and safe reason codes.

## Objective Coverage Matrix

| Concern | Covered By |
|---|---|
| Java Maven structure and commands | `HLO-001`, `MLO-001` |
| `pom.xml`, source/test paths, JUnit 5, JaCoCo | `HLO-001`, `MLO-001`, `MLO-005` |
| Integrator and protocol directories | `MLO-001` |
| Prior-run archival and provenance | `MLO-001` |
| `BigDecimal` money handling | `MLO-002`, `MLO-003` |
| ISO 4217-style currency validation | `MLO-002` |
| Transaction Validator component | `MLO-002` |
| Fraud Detector component | `MLO-003` |
| Settlement Processor component | `MLO-004` |
| Reporting Agent component | `MLO-005` |
| Stack-neutral `summary.json` and per-transaction result shape | `MLO-004`, `MLO-005` |
| Edge cases and failure recovery | All mid-level objectives |
| Privacy-safe audit and logging | All mid-level objectives |
| Product-only boundary | `HLO-001` and all objective notes |

## Residual Risks And Open Decisions

- The low-level task decomposer must choose exact Java package names, Maven plugin configuration, and the concrete pipeline run command.
- Fraud thresholds and risk weights are design decisions. The domain handoff suggested plausible values, but the final specification should define exact constants before implementation.
- Currency allowlist scope must be finalized. A finite assignment-supported set is safest without fresh external research.
- The timestamp rule for early-hours risk must be finalized. UTC hour is recommended for determinism.
- The shape of `pipeline-status.json` should be aligned with the eventual Python status reader's expected fields while remaining stack-neutral.
- Privacy checks need a precise prohibited-field list so Reporting Agent tests can enforce them without relying on fragile string matching.

## Recommended Next Step For Low-Level Task Decomposition

Create implementation-ready Java task cards from these objectives in this order:

1. Maven project structure, dependencies, JaCoCo, JUnit 5, and command definitions.
2. Shared models, Jackson JSON configuration, audit helper, redaction policy, and reason-code enum.
3. Integrator directory setup, archival, provenance, message envelope writing, and per-transaction orchestration.
4. Transaction Validator with `BigDecimal`, required-field, timestamp, and currency validation.
5. Fraud Detector with finalized deterministic thresholds and risk scoring.
6. Settlement Processor with safe final-result writing and failure recovery.
7. Reporting Agent with summary/status files, completeness checks, and final privacy scan.
8. Unit and integration test seams using temporary directories, plus coverage verification through `mvn test jacoco:report jacoco:check`.

Each task card should name the exact file to create or update, Java class or method to implement, behavior details, edge cases, acceptance criteria, and verification command.

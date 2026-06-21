# Domain Research Handoff

Run ID: `20260621-145100-write-spec-java-alternate`
Role: Athena (Spec Writer) domain research executor sub-agent
Stack: `java`

## Assigned Scope

Produce a concise domain research handoff for a Java transaction-processing specification. The handoff is limited to educational banking-pipeline domain constraints, privacy/audit boundaries, sample-data implications, and Java stack choices that Athena (Spec Writer) should carry into the candidate specification. This file is run-local evidence only and does not authorize editing canonical root outputs.

## Sources And Constraints Inspected

- Operator prompt for this Hera generate-set executor task.
- Repository Homework 6 agent guide and README.
- `agent-control/write-spec/transaction-system-brief.md`.
- `agent-control/write-spec/stack-profiles.md`.
- `agent-control/write-spec/workflow.md`.
- `agent-control/write-spec/quality-bar.md`.
- `agent-control/write-spec/run-registry.md`.
- `sample-transactions.json`, inspected only for safe structural facts.

Observed constraints from the prompt and local references:

- The product is an educational banking transaction-processing simulation, not real banking, legal, AML, sanctions, payment-network, or regulatory compliance software.
- The sample input contains eight synthetic transactions with normal transfers, high-value activity, unusual timing, unsupported currency data, and invalid negative amount data.
- Account identifiers, descriptions, and transaction metadata are sensitive. Specification examples, logs, audit events, summaries, and handoffs should redact or omit raw account identifiers and descriptions.
- Java profile requires Maven, `BigDecimal`, Jackson or equivalent JSON handling, JUnit Jupiter, and JaCoCo.
- Runtime communication must use JSON files under `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- Result files must remain stack-neutral enough for a Python `pipeline-status` MCP reader to inspect `shared/results/summary.json` and per-transaction `TXN*.json` files.

## Decisions

- Specify money parsing and arithmetic with `java.math.BigDecimal`; reject `double` and `float` for amounts, comparisons, totals, and tests.
- Specify Maven project structure with `pom.xml`, Java sources under `src/main/java/...`, and tests under `src/test/java/...`.
- Use Jackson for JSON serialization/deserialization, with amount values serialized as strings to preserve decimal precision and MCP-reader compatibility.
- Require at least four stack-native runtime components: `TransactionValidator`, `FraudDetector`, `SettlementProcessor`, and `ReportingAgent`, plus an `Integrator` entry point.
- Give each runtime component a common `PipelineMessage processMessage(PipelineMessage message)` style contract and an audit-safe component identity.
- Keep validation deterministic: required fields, parseable positive amount, supported ISO 4217-style currency codes, timestamp parseability, and safe failure records for malformed input.
- Keep fraud scoring educational and rule-based, using safe indicators such as high value, unusual timing, channel/country metadata categories, and transfer type. Avoid claims that the score detects real fraud or satisfies compliance duties.
- Require structured audit events with timestamp, component name, transaction ID, safe outcome, and reason code. Omit raw account IDs and descriptions from audit output.
- Require deterministic reruns: archive existing `shared/` to zero-padded `archive/shared-###` before creating a fresh protocol tree and `shared/run-provenance.json`.
- Require JaCoCo reporting and check goals with Athena's temporary 75% target; Themis (Test Generator) later owns increasing coverage above 80% and enforcing the blocking hook.

## Uncertainty

- No external web or Context7 research was performed in this bounded handoff. Treat Java library details as profile-driven local constraints until Hephaestus (Code Generator) records required Context7 queries during code generation.
- The exact Java package name is not selected here. Athena (Spec Writer) should choose one consistent package, such as `edu.setu.transactionpipeline`, and use it throughout file paths and task cards.
- Fraud thresholds are not final. Athena should make thresholds explicit, simple, and testable without implying real-world fraud accuracy.

## Residual Risks

- A Java spec can drift into boilerplate unless task cards name concrete classes, methods, Maven plugins, commands, and result JSON shapes.
- Raw sample fields could leak into examples if downstream agents copy input records directly. Keep examples synthetic, redacted, or field-shape-only.
- MCP compatibility can break if Java result files use Java-specific nested objects or numeric money values instead of simple strings, counts, statuses, reason codes, timestamps, and safe history/audit counts.
- Compliance language needs continued review; "validation", "risk scoring", "review required", and "educational simulation" are safer than regulatory or legal claims.

## Recommended Next Step

Athena (Spec Writer) should pass this handoff to the objectives architect sub-agent, then draft Java-specific objectives and low-level task cards that name Maven, `pom.xml`, `Integrator.java`, runtime component classes, `BigDecimal`, Jackson DTOs, JUnit Jupiter tests, JaCoCo commands, deterministic shared-directory archival, safe result schemas, and privacy-preserving audit behavior.

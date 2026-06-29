# Domain Research Handoff

Run ID: `20260621-180826-write-spec-java-hera-java-full-set`
Role: Domain Research executor sub-agent for Athena (Spec Writer)
Selected stack: `java`

## Scope

This handoff summarizes domain rules and technical constraints for an educational banking transaction-processing pipeline specification. The focus is the Generated Transaction System Layer only: transaction validation, fraud/risk review heuristics, settlement or final outcome handling, audit-safe reporting, ISO 4217-style currency assumptions, privacy boundaries, unsupported compliance claims, and Java stack implications.

This handoff does not authorize canonical file edits, final selection, or changes to Homework Automation Layer control surfaces. It is input for the objectives architect and later Athena (Spec Writer) integration work.

## Files/Context Inspected

- `agent-control/write-spec/transaction-system-brief.md` - local assignment/source. Primary product brief for Athena (Spec Writer).
- `sample-transactions.json` - local assignment/source. Sample data shape and edge-case inventory. Raw account identifiers and descriptions were treated as sensitive and are not repeated here.
- `agents.md` - local assignment/source. Layer glossary, privacy/audit rules, run preservation rules, and automation/product boundary guidance.
- `agent-control/write-spec/stack-profiles.md` - local assignment/source. Java stack profile and stack-invariant requirements.
- `agent-control/write-spec/quality-bar.md` - local assignment/source. Required spec depth, required components, task-card expectations, research classification rules, and quality gate.
- Homework 3 docs - not inspected in this delegated pass. I did not find a Homework 3 reference package inside the allowed Homework 6 root during targeted local discovery, and I did not inspect parent directories. The quality bar's description of Homework 3 depth was used instead: rich product behavior, state/data concepts, edge cases, failure modes, acceptance criteria, and implementation-ready slices.

No external web sources were used. Context7 was not exposed as a callable tool in this executor runtime, and the operator instruction constrained this pass to relevant local sources. Treat any ISO 4217 or Java-library statements below as local-assignment assumptions or high-level Java-stack implications, not as fresh external documentation research.

## Findings With Cited Source Type

1. The product is an educational banking transaction-processing pipeline that begins with `sample-transactions.json`, processes every raw transaction record, and writes final outcomes under `shared/results/`. Source type: local assignment/source, `transaction-system-brief.md`.

2. Runtime "agents" are stack-native application components, not Claude/Codex skills. The specification should frame them as Java classes with bounded responsibility, message contracts, audit identities, and pipeline positions. Source type: local assignment/source, `transaction-system-brief.md`, `agents.md`, `quality-bar.md`.

3. The generated specification must require at least four cooperating runtime transaction pipeline components for the current quality bar. The normal component set is Transaction Validator, Fraud Detector, Settlement Processor, and Reporting Agent. Source type: local assignment/source, `quality-bar.md`; supported by `agents.md` guidance that refreshed generation targets at least four components.

4. Required file protocol is JSON through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`, with deterministic archival of any previous `shared/` tree under zero-padded `archive/shared-001` style folders before a fresh run. Source type: local assignment/source, `transaction-system-brief.md`, `agents.md`, `quality-bar.md`.

5. Runtime provenance is a product-level requirement. A fresh run should write `shared/run-provenance.json` containing non-sensitive traceability such as schema version, runtime run ID, timestamp, source Athena (Spec Writer) run ID or canonical spec path, spec fingerprint, selected Hephaestus (Code Generator) run ID or package fingerprint, and selected output inventory reference. It must not include raw transactions, account identifiers, descriptions, credentials, or hidden prompt/thread content. Source type: local assignment/source, `transaction-system-brief.md`, `quality-bar.md`.

6. Sample input contains ordinary transfers, high-value transfers, early-hours activity, an unsupported currency code, a negative amount, multiple channels, multiple countries, and multiple transaction types. The spec should map these to validation and risk-review rules without repeating raw account IDs or descriptions. Source type: local assignment/source, `sample-transactions.json`, summarized through `transaction-system-brief.md`.

7. Money must use precise decimal semantics. For Java, specify `BigDecimal` for parsing, comparisons, and serialization of monetary amounts. The spec should forbid `double` and `float` for amounts. Source type: local assignment/source, `transaction-system-brief.md`, `agents.md`, `stack-profiles.md`.

8. Currency validation must use ISO 4217-style assumptions and reject the unsupported sample value. Because this pass did not use external standards documentation, the safest spec decision is to define a finite assignment-supported currency set for sample processing, such as valid sample currencies only, and call it "ISO 4217-style" rather than claiming full registry coverage. Source type: local assignment/source, `transaction-system-brief.md`, `sample-transactions.json`, `agents.md`, `stack-profiles.md`.

9. Audit output must include ISO 8601 timestamps, runtime component name, transaction ID, safe outcome, and reason code when applicable. Account identifiers, descriptions, and unnecessary metadata must be redacted or omitted. Source type: local assignment/source, `transaction-system-brief.md`, `agents.md`, `stack-profiles.md`.

10. Fraud detection should use clear educational heuristics rather than real financial-crime or banking compliance models. Locally supported signals include high value, unusual timing, channel, country, and transfer type. Source type: local assignment/source, `transaction-system-brief.md`, `sample-transactions.json`.

11. Java profile expectations are Maven, Jackson or equivalent JSON handling, `src/main/java/.../Integrator.java` with `main(String[] args)`, runtime agent classes with `PipelineMessage processMessage(PipelineMessage message)`, JUnit 5/JUnit Jupiter with Maven Surefire, optional Failsafe for integration-test phases, and JaCoCo Maven plugin with report/check goals. Source type: local assignment/source, `stack-profiles.md`.

12. Java result files must remain stack-neutral so the future Python `pipeline-status` MCP reader can consume them. Required result shapes include `shared/results/summary.json` for safe aggregate counts and per-transaction `shared/results/TXN*.json` files with safe transaction status, reason codes, amount string, currency, timestamps, component history count, and audit event count. Source type: local assignment/source, `stack-profiles.md`.

13. The spec must not leak Homework Automation Layer mechanics into product requirements. It may specify product-level runtime provenance, MCP-readable result shapes, validation seams, and coverage expectations, but it must not require the generated transaction system to implement Athena, Hephaestus, Themis, Clio, Hera, harness planning, selection, screenshots, PR packaging, or control-surface repair. Source type: local assignment/source, `transaction-system-brief.md`, `agents.md`, `quality-bar.md`.

## Recommended Spec Decisions

1. Define the Java package root explicitly, for example `edu.setu.banking.pipeline`, with model classes for `TransactionRecord`, `PipelineMessage`, `ProcessingResult`, `AuditEvent`, `Summary`, and `RunProvenance`.

2. Require `BigDecimal` parsing from string values only. Store and emit amounts as strings in JSON to preserve decimal formatting and avoid binary floating-point drift.

3. Use a finite assignment-supported currency policy: accept the valid sample currencies and reject the unsupported sample code with reason code `unsupported_currency`. Phrase this as ISO 4217-style validation suitable for the assignment, not as full official ISO registry maintenance.

4. Reject missing required fields, blank identifiers, unparsable timestamps, non-positive amounts, unsupported currencies, malformed JSON, and files that cannot be mapped to a transaction record. Use safe reason codes such as `missing_required_field`, `invalid_amount`, `unsupported_currency`, `malformed_json`, and `processing_error`.

5. Make Transaction Validator the only component that can turn malformed or invalid base input into a rejected final result before fraud scoring. It should not log raw account identifiers or descriptions.

6. Make Fraud Detector deterministic and educational. Suggested risk signals:
   - High amount threshold produces review or high-risk status.
   - Very high amount threshold requires review before settlement.
   - Early-hours timestamp adds risk points.
   - API or mobile channel adds configurable risk points only as an educational heuristic.
   - Cross-country or non-default country metadata adds risk points only as an educational heuristic.
   - Wire-transfer type adds risk points.

7. Keep the thresholds configurable constants in code and documented in the spec. Example design decision for the architect to confirm: `amount >= 10000.00` adds risk, `amount >= 50000.00` requires review, and early-hours means local hour before 05:00 based on the transaction timestamp. These are assignment heuristics, not real fraud rules.

8. Make Settlement Processor write final accepted, rejected, or review-required outcomes. It should not claim to move money, connect to a payment network, update balances, or perform legal settlement.

9. Make Reporting Agent the fourth required runtime component. It should verify every input transaction has exactly one result, create `summary.json`, create `pipeline-status.json`, check status counts, and perform a final privacy scan of result payloads before handoff to read-only status tooling.

10. Keep result files audit-safe. Per-transaction result files may include transaction ID, amount string, currency, status, reason codes, risk score/category, component history count, audit event count, and timestamps. They should omit raw account identifiers, raw descriptions, and unnecessary metadata values.

11. Require an `AuditService` or equivalent helper so all components use the same audit-event schema and redaction behavior. Do not let each component invent its own log format.

12. Require deterministic file naming that sorts cleanly and avoids collisions, such as `<sequence>-<transaction_id>-<component>.json` for intermediate files and `<transaction_id>.json` for final result files, while keeping result payloads safe.

13. Specify isolated test behavior: JUnit tests must use temporary directories and must not write into the canonical root `shared/` tree. Integration tests should copy or synthesize safe sample records with redacted account examples.

14. Specify Maven commands at a high level: `mvn test`, `mvn test jacoco:report jacoco:check`, and one concrete pipeline run command chosen by the final spec, such as `mvn exec:java` if the Maven Exec plugin is selected.

15. Set the Athena (Spec Writer) ending-context coverage target to 75%. Themis (Test Generator) later owns raising coverage above 80% and adding a blocking coverage hook.

## Privacy/Audit Guardrails

- Treat account identifiers, descriptions, and transaction metadata as sensitive even though the sample is synthetic.
- Do not include full account identifiers in logs, audit events, summaries, docs, examples, validation errors, or result files. Use redaction such as `ACC-****1234` only when an account-shaped example is necessary.
- Do not repeat raw transaction descriptions in generated examples, test names, assertion messages, screenshots, summaries, or handoff text.
- Prefer reason codes over prose that echoes sensitive input.
- Keep audit events structured and minimal: timestamp, component name, transaction ID, safe status/outcome, reason code, and optional non-sensitive counts.
- Store amount as a decimal string and currency as a code; do not store full source/destination account values in final output.
- Runtime provenance must include only run IDs, paths, timestamps, schema versions, and fingerprints. It must not include raw transaction data, prompt content, credentials, or account identifiers.
- Reporting Agent should perform a final privacy check before writing summary/status artifacts and fail safely if unsafe fields are about to be emitted.
- Test fixtures should use synthetic or redacted account-shaped values and should avoid copying raw descriptions into expected-output files.

## Unsupported Claims To Avoid

- Do not claim the system provides real banking operations, payment execution, settlement finality, balance updates, account verification, or payment-network integration.
- Do not claim AML, sanctions, KYC, fraud prevention, legal, regulatory, or compliance certification.
- Do not claim ISO 4217 registry completeness unless a later executor performs and cites external standards/library research. For this run, use "ISO 4217-style" and an explicit assignment-supported currency set.
- Do not describe educational fraud heuristics as production models, machine learning, risk-grade accuracy, suspicious activity reporting, or legally meaningful compliance decisions.
- Do not implement or describe Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), Clio (Documentation Generator), Hera (Orchestrator), dev-doc-harness, Superpowers, screenshots, PR evidence, or selection workflows as runtime product components.
- Do not require a Java MCP server. The Java output should produce JSON result shapes that the required Python FastMCP server can read later.
- Do not require external services, databases, message queues, bank APIs, credentials, or network access for the generated transaction system.

## Uncertainty/Residual Risks

- External research was not performed. ISO 4217 details and Java library behavior were not freshly verified against external documentation in this executor pass.
- Context7 was not available as a callable tool here. Later Hephaestus (Code Generator) still must use Context7 and record at least two query records during code generation.
- The exact Java build command for running the pipeline remains a spec decision. Maven Exec is likely simplest, but the final spec should choose one concrete path and include the required `pom.xml` plugin expectations.
- Currency scope should be explicitly chosen. A finite assignment-supported set is safest, but the objectives architect may choose a larger embedded allowlist if they can support it without external network dependency.
- Fraud thresholds are design choices, not assignment facts. The objectives architect should confirm threshold values so downstream task cards are implementation-ready.
- Time-zone interpretation for "early-hours" risk needs a deterministic rule. The simplest choice is to derive the UTC hour from the ISO 8601 timestamp unless the final spec explicitly introduces local-country conversion.
- The sample data includes metadata that can support risk heuristics, but metadata is also sensitive. The spec should distinguish internal scoring use from safe output fields.
- Homework 3 style/depth examples were not directly inspected in this pass, so final integration should verify the candidate spec reaches the required level of detail against any available Homework 3 reference package.

## Next Step For Objectives Architect

Use these findings to write Java-specific high-level and mid-level objectives that are product-only, privacy-safe, and testable. In particular, decide the exact currency allowlist, fraud threshold constants, early-hours timestamp rule, final status vocabulary, audit-event schema, and Reporting Agent responsibilities before the low-level task architect creates implementation-ready Java task cards.

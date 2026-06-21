## Assigned Scope

Shape the high-level objective and 4-5 mid-level objectives for Athena (Spec Writer) run `20260621-145100-write-spec-java-alternate`.

The scope is limited to the Generated Transaction System Layer: a Java Maven educational transaction-processing pipeline specification. It excludes canonical root output edits, operator-layer mechanics, harness planning, run selection, canonical-copy workflow, slash-command setup, hook setup, PR packaging, screenshot capture, and Greek automation-agent mechanics as product requirements.

## Context Inspected

- `AGENTS.md`
- `README.md`
- `.agents/skills/write-spec/SKILL.md`
- `agent-control/write-spec/workflow.md`
- `agent-control/write-spec/stack-profiles.md`
- `agent-control/write-spec/quality-bar.md`
- `agent-control/write-spec/run-registry.md`
- `agent-control/write-spec/transaction-system-brief.md`
- `sample-transactions.json`
- Existing Python objectives handoff at `docs/agent-runs/20260619-170102-write-spec-python-fresh/agent-1-spec/handoffs/objectives-handoff.md`

Confirmed sample fixture shape without copying sensitive fields: eight synthetic transactions with ordinary transfers, high-value wire transfers, a near-threshold transfer, early-hours API activity, unsupported currency `XYZ`, a negative amount, and mobile-channel activity.

## Sources Or Commands Used

Commands used:

- `Get-Content -Raw AGENTS.md`
- `Get-Content -Raw README.md`
- `Get-Content -Raw .agents\skills\write-spec\SKILL.md`
- `Get-Content -Raw agent-control\write-spec\workflow.md`
- `Get-Content -Raw agent-control\write-spec\stack-profiles.md`
- `Get-Content -Raw agent-control\write-spec\quality-bar.md`
- `Get-Content -Raw agent-control\write-spec\run-registry.md`
- `Get-Content -Raw agent-control\write-spec\transaction-system-brief.md`
- `Get-Content -Raw sample-transactions.json`
- `Get-Content -Raw docs\agent-runs\20260619-170102-write-spec-python-fresh\agent-1-spec\handoffs\objectives-handoff.md`

No new external research was performed for this objectives handoff. The objectives are based on local Homework 6 controls, the Java stack profile, and the synthetic sample fixture.

## Proposed High-Level Objective

Build a Java Maven educational transaction-processing pipeline that deterministically processes every synthetic sample transaction through stack-native runtime components, uses `BigDecimal` money semantics and ISO 4217-style currency validation, preserves audit-safe JSON evidence in the required `shared/` protocol folders, and writes `shared/results` outputs compatible with the existing `pipeline-status` reader.

## Proposed Mid-Level Objectives

1. Implement Java Maven orchestration and deterministic file protocol setup.

   Observable success: `pom.xml` defines a Maven project with Jackson or equivalent JSON handling, JUnit 5/JUnit Jupiter, and JaCoCo support; `src/main/java/.../Integrator.java` exposes `main(String[] args)`; the integrator loads `sample-transactions.json`, archives any existing `shared/` tree to the next zero-padded `archive/shared-001` style folder, creates fresh `shared/input`, `shared/processing`, `shared/output`, and `shared/results` directories, writes non-sensitive `shared/run-provenance.json`, and accounts for every input transaction in final outputs.

2. Validate transaction structure, `BigDecimal` money, currency, and safe rejection behavior.

   Observable success: `TransactionValidator` implements a common method such as `PipelineMessage processMessage(PipelineMessage message)`, parses amount strings with `BigDecimal` only, never uses `double` or `float` for monetary values, accepts only the configured ISO 4217-style currency allowlist for the sample set, rejects unsupported currency and non-positive amount records with stable reason codes, and emits audit records containing timestamp, component name, transaction ID, safe outcome, and reason code without plaintext account identifiers, descriptions, or raw metadata.

3. Apply deterministic educational fraud-risk scoring without real compliance claims.

   Observable success: `FraudDetector` assigns repeatable `riskScore`, `riskLevel`, and reason codes using documented educational heuristics such as high amount, very high amount, wire transfer type, early-hours timestamp, channel, country, and near-threshold amount. Low-risk validated records can proceed toward `settled`, elevated-risk records are routed toward `review_required`, and the component never claims real fraud, AML, sanctions, legal, banking, or payment-network compliance.

4. Produce safe settlement outcomes and per-transaction result files.

   Observable success: `SettlementProcessor` converts validated low-risk transactions to `settled`, preserves validation failures as `rejected`, routes elevated-risk transactions to `review_required`, reserves `error` for per-transaction processing failures, and writes safe `shared/results/TXN*.json` files. Each transaction result includes MCP-readable fields such as transaction ID, status, reason codes, amount string, currency, processed timestamp, component history count, and audit event count while omitting plaintext account identifiers, descriptions, and raw metadata.

5. Generate audit-safe reporting summaries compatible with future read-only status tooling.

   Observable success: `ReportingAgent` verifies result completeness, checks status-count consistency, performs a final privacy review over result payloads, and writes `shared/results/summary.json` plus any optional sanitized status file needed by the product spec. The summary includes total, settled, rejected, review-required, and error counts, and its shape remains stack-neutral so the Python `pipeline-status` MCP reader can inspect Java-produced result files without requiring a Java MCP server.

## Objective-To-Evidence Notes

- Objective 1 maps to Maven project files, fresh `shared/` protocol directories, deterministic prior-run archival, `shared/run-provenance.json`, and final accounting for all sample transaction IDs.
- Objective 2 maps to validator unit tests for required fields, `BigDecimal` parsing, supported currency behavior, unsupported currency rejection, non-positive amount rejection, and audit/log redaction checks.
- Objective 3 maps to repeatable fraud-risk unit tests that assert stable scores, risk levels, reason codes, and review routing for high-value, very-high-value, near-threshold, early-hours, channel, and country-based sample signals.
- Objective 4 maps to settlement tests and result-file assertions for allowed status vocabulary, safe per-transaction JSON shape, string amount serialization, reason-code preservation, and absence of plaintext sensitive fields.
- Objective 5 maps to reporting tests for `summary.json`, count consistency, per-transaction result discovery, privacy review, and compatibility with read-only `pipeline-status` expectations.

## Assumptions

- Java is the selected stack for this alternate run, and Maven is the build tool.
- The generated Java package uses Jackson or equivalent JSON handling and Java standard filesystem APIs.
- Monetary values are parsed and stored with `BigDecimal` and serialized to JSON as strings.
- The local supported-currency behavior is deterministic for the sample fixture and rejects unsupported sample values such as `XYZ`.
- `transaction_id` values are acceptable as stable correlation identifiers, while account identifiers, descriptions, and raw metadata remain sensitive.
- The pipeline performs no foreign exchange conversion, balance mutation, external payment call, production fraud decision, or real compliance decision.
- The existing `pipeline-status` reader consumes stack-neutral JSON files from `shared/results`; the Java spec should not require a Java MCP server.

## Uncertainty And Residual Risks

- Exact Java package names and class names remain integration decisions for the final specification, but they should be concrete and consistent across task cards.
- Exact risk thresholds remain a design decision for the integrated specification; they should be simple, deterministic, and documented as educational heuristics.
- Privacy leakage remains the highest practical risk because the raw input contains sensitive account identifiers, descriptions, and metadata that must not be copied into logs, audit events, summaries, docs, or examples.
- Decimal safety can be weakened if later implementation slices convert amounts through `double`, `float`, or imprecise JSON number handling; low-level tasks should make this a specific rejection condition.
- Result shapes must stay stable enough for future MCP reads while avoiding MCP configuration mechanics in the product specification.
- Maven and Java add build-system overhead compared with the selected Python package, so later task decomposition must be precise about `pom.xml`, source paths, test paths, and JaCoCo commands.

## Recommended Next Step

Use these objectives as the objective section source for `agent-1-spec/outputs/specification.md`, then pass them to the low-level task decomposition phase so it can produce Java Maven task cards with exact files, classes, methods, edge cases, acceptance criteria, and verification commands.

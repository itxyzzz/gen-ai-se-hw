# Transaction System Brief For Athena (Spec Writer)

This brief is Athena (Spec Writer)'s direct product-spec input for Homework 6. It describes the transaction-processing system that the generated `specification.md` must specify.

`homework-6/TASKS.md` is frozen and operator-facing. It explains the full homework assignment, including one-time setup and submission deliverables. Athena (Spec Writer) should not need to read `TASKS.md` during normal generation; use this brief, `sample-transactions.json`, `agents.md`, the stack profile, the quality bar, and the Homework 3 reference package instead.

## System To Specify

The product is a working AI-assisted banking transaction-processing pipeline with cooperating runtime application agents. It starts from `sample-transactions.json`, processes every raw transaction record, and writes final outcomes under `shared/results/`.

The generated system must include at least three cooperating runtime transaction pipeline agents. In this brief, "agent" means a stack-native application component with a bounded responsibility, message contract, decision logic, audit identity, and pipeline position:

- Transaction Validator: checks required fields, parseable positive monetary amounts, and ISO 4217-style currency codes.
- Fraud Detector: scores transaction risk using clear educational heuristics such as high value, unusual timing, channel, country, or transfer type.
- At least one final-stage component such as Settlement Processor, Compliance Checker, or Reporting Agent.

These runtime transaction pipeline agents are not Claude/Codex skills. They are stack-native application components: Python modules/classes, Java classes, or equivalent implementations with a shared message-processing interface. Their behavior may be deterministic; the "agent" label comes from their responsibility boundary, message contract, decision ownership, output contract, audit-log identity, and position in the pipeline.

The generated system must include an integrator or orchestrator that sets up directories, loads `sample-transactions.json`, passes messages through the runtime components in order, and verifies that every input transaction has a final result.

## Input Data

Beginning state:

- `sample-transactions.json` contains raw transaction records.
- Records include transaction identifiers, timestamps, source and destination accounts, string monetary amounts, currencies, transaction types, descriptions, and metadata such as channel and country.
- The sample includes ordinary transfers, high-value transfers, early-hours activity, invalid currency (`XYZ`), and invalid negative amount data.

Ending state:

- Every sample transaction is accounted for in `shared/results/`.
- Repeated pipeline executions remain visibly separated: before a new run creates protocol directories, an existing `shared/` tree is archived beside it under the next zero-padded folder such as `archive/shared-001`, `archive/shared-002`, or `archive/shared-003`.
- Rejected transactions include a safe reason field.
- Accepted or review-required transactions include a clear status and safe processing summary.
- The pipeline emits a summary report with total, accepted, rejected, review-required, and error counts.
- The generated specification should use a temporary 75% coverage target in its ending context. This intentionally lowers the earlier internal standard to keep the homework to spec for this stage. Themis (Test Generator) later owns increasing coverage above 80% and adding the blocking coverage hook.

## File-Based Communication Protocol

Runtime components pass JSON message files through these directories:

```text
shared/
  input/
  processing/
  output/
  results/
```

The integrator drops initial messages into `shared/input`. Each runtime component moves or reads a message into `shared/processing` while working, writes the next safe JSON message into `shared/output`, and final outcomes land in `shared/results`.

The standard message envelope should follow this shape or a stack-equivalent extension:

```json
{
  "message_id": "uuid4-string",
  "timestamp": "2026-03-16T10:00:00Z",
  "source_agent": "transaction_validator",
  "target_agent": "fraud_detector",
  "message_type": "transaction",
  "data": {
    "transaction_id": "TXN001",
    "amount": "1500.00",
    "currency": "USD",
    "status": "validated"
  }
}
```

The generated spec should make file movement, idempotent reruns, deterministic prior-output archival, malformed JSON handling, and per-transaction failure recovery clear enough for code generation. The product requirement is that prior runtime evidence is preserved and the current run receives a fresh `shared/input`, `shared/processing`, `shared/output`, and `shared/results` tree.

## Technical Constraints

- Monetary values must use precise decimal types such as Python `decimal.Decimal` or Java `BigDecimal`; never binary floating point.
- Currency validation should use ISO 4217-style codes and reject unsupported sample values such as `XYZ`.
- Logging and audit output must include ISO 8601 timestamps, runtime component name, transaction ID, safe outcome, and reason code when applicable.
- Account identifiers, names, descriptions, and transaction metadata are sensitive for this homework. Logs, audit events, result summaries, docs, and examples must not expose plaintext account identifiers or unnecessary PII; use redaction such as `ACC-****1001`.
- The system is an educational simulation. Do not claim real banking, AML, sanctions, payment-network, or legal compliance.
- Tests must isolate filesystem state from the real `shared/` directories, for example with temporary directories or stack-equivalent fixtures.
- The `/validate-transactions` workflow must be supportable as a validator dry-run over `sample-transactions.json`, without running the full pipeline as its primary behavior.
- Future MCP tools must be able to read result shapes for `get_transaction_status`, `list_pipeline_results`, and `pipeline://summary`, but Athena (Spec Writer) should specify product result shapes, not create MCP configuration mechanics.

## Outer Homework Deliverables

The homework also requires slash-command or skill surfaces, Context7 notes, hooks, MCP configuration, screenshots, README author information, and PR evidence. Those are outer deliverables handled by the Homework Automation Layer and operator setup.

Do not turn these outer mechanics into transaction-system product requirements in `specification.md`:

- Creating or repairing the `write-spec` skill, Claude skill, Codex command, or harness workflow.
- `dev-doc-harness`, Superpowers freeze gates, planning package creation, or planning commits.
- Preserved Athena (Spec Writer) run folders, final-selection records, canonical-copy mechanics, or comparison workflow.
- Hook setup, MCP configuration setup, screenshot capture, README/PR support, or submission packaging as low-level product slices.

The generated spec may mention that Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator) will consume the spec later. It must not ask the generated transaction-processing software to implement those homework automation agents.

## Detail Boundary

This brief is not the final technical specification. Athena (Spec Writer) owns the detailed architecture, stack-specific files and functions, exact low-level task decomposition, validation mapping, and handoff-ready acceptance criteria.

Keep the generated spec detailed like Homework 3, but focused only on the Generated Transaction System Layer.

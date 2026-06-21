# Run Metadata

## Identity

- Run ID: `20260621-180826-write-spec-java-hera-java-full-set`
- Agent: Athena (Spec Writer)
- Parent Hera run ID: `20260621-180512-orchestrate-runs-java-full-set`
- Requested mode: `generate`
- Requested stack: `java`
- Package-set ID under construction: `java-candidate-20260621-180512`
- Selection authorized: no
- Expected output path: `docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/agent-1-spec/outputs/specification.md`
- Selection record path: `docs/agent-runs/final-selection.md`
- Current canonical package context: `python-canonical-20260621` is protected and not replaced by this run.

## Canonical Protection

This run is preservation-only. It must not overwrite `specification.md`, `docs/agent-runs/final-selection.md`, `docs/agent-runs/selection-sets.json`, root product files, tests, docs, screenshots, `mcp/server.py`, `shared/`, or canonical runtime output.

Silent "latest" targeting is not allowed. Downstream agents must consume explicit run IDs, inventory paths, fingerprints, selected package-set IDs, and selection records supplied by Hera (Orchestrator) or the operator.

## Local References Read

- `.agents/skills/write-spec/SKILL.md`
- `agent-control/write-spec/workflow.md`
- `agent-control/write-spec/stack-profiles.md`
- `agent-control/write-spec/quality-bar.md`
- `agent-control/write-spec/run-registry.md`
- `agent-control/write-spec/transaction-system-brief.md`
- `sample-transactions.json`
- `agents.md`
- Repository `../AGENTS.md`
- Repository `../HOMEWORK_STANDARDS.md`
- Repository `../README.md`
- `docs/agent-runs/final-selection.md`
- `docs/agent-runs/selection-sets.json`
- `../homework-3/specification.md`
- `../homework-3/agents.md`
- `../homework-3/docs/domain-rules.md`
- `../homework-3/docs/technical-conventions.md`
- `../homework-3/docs/development-process.md`

## Missing Or Optional References

- `specification-TEMPLATE-hint.md` was not present in this checkout. Per `run-registry.md`, this run uses the Task 1 section list, Homework 3 depth/format references, and the Homework 6 write-spec quality bar as the local template source.

## Tool And Runtime Notes

- Current branch observed: `homework-6-extension`.
- Existing canonical `specification.md` observed with SHA-256 `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B`; first-run auto-selection does not apply.
- `git status --short --branch` reported an unrelated untracked parent Hera run folder: `docs/agent-runs/20260621-180512-orchestrate-runs-java-full-set/`.
- Nested executor sub-agents are available through the current Codex runtime and are used for this run.
- Model/reasoning controls are available only through the current runtime's exposed sub-agent options. The run requested strong/current Codex-family reasoning by role and recorded the limitation that exact UI model labels are not surfaced in these artifacts.

## Privacy And Boundary Reminders

- Do not store raw account identifiers, descriptions, hidden prompts, credentials, or real customer data in generated examples, logs, or run metadata.
- The transaction pipeline is an educational simulation only, not legal, banking, AML, sanctions, payment-network, or regulatory compliance advice.
- Runtime components must use functional names such as Transaction Validator, Fraud Detector, Settlement Processor, Reporting Agent, and Integrator. Do not name Generated Transaction System Layer components Athena, Hephaestus, Themis, Clio, or Hera.

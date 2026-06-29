# Run Metadata

- Run ID: `20260621-145100-write-spec-java-alternate`
- Agent: Athena (Spec Writer)
- Mode: `generate`
- Stack: `java`
- Parent Hera run ID: `20260621-145059-orchestrate-runs-java-alternate`
- Start time: 2026-06-21 14:51 Europe/Budapest
- Orchestration tool: Codex Desktop main thread with executor sub-agents.
- Operator instruction: create a preserved Java alternate through Athena, Hephaestus, Themis, and Clio without replacing or modifying canonical Python outputs.
- Canonical output policy: preserve run-local outputs only; no canonical copy or selection authorized.
- Stack profile: Maven, `pom.xml`, `src/main/java`, `src/test/java`, BigDecimal, Jackson, JUnit Jupiter, JaCoCo.
- Nested-agent behavior: first-level executor sub-agents were available for Athena research, objectives, and low-level task handoffs.
- Pre-existing dirty state: tracked porcelain check was clean before run-local artifacts were added.
- Missing references: `specification-TEMPLATE-hint.md` is not present in this checkout; Task 1 section list and control-package quality bar were used as local template sources.
- Privacy note: run metadata stores paths, run IDs, and safe counts only; it does not include raw account identifiers or raw transaction descriptions.

## Source Inputs

- `agent-control/write-spec/workflow.md`
- `agent-control/write-spec/stack-profiles.md`
- `agent-control/write-spec/quality-bar.md`
- `agent-control/write-spec/run-registry.md`
- `agent-control/write-spec/transaction-system-brief.md`
- `agents.md`
- `TASKS.md`
- `sample-transactions.json` counted and shape-inspected only; raw payload not copied into reports.
- Root `AGENTS.md`, `HOMEWORK_STANDARDS.md`, and root `README.md`.

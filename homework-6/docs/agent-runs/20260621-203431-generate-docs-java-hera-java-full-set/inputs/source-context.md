# Source Context

This Clio (Documentation Generator) run documents only the Java candidate package-set under construction by Hera.

## Required Control And Assignment Context Read

- `.agents/skills/generate-docs/SKILL.md`
- `agent-control/generate-docs/workflow.md`
- `agent-control/generate-docs/quality-bar.md`
- `agent-control/generate-docs/run-registry.md`
- `agents.md`
- `TASKS.md`
- `docs/agent-runs/final-selection.md`
- `docs/agent-runs/selection-sets.json`
- `agent-control/operate-pipeline/commands-and-hooks.md`
- `mcp.json`
- `.codex/config.toml`
- `mcp/server.py`
- `sample-transactions.json`
- repository `../AGENTS.md`
- repository `../HOMEWORK_STANDARDS.md`
- repository `../README.md`

## Java Candidate Sources Read

- Athena output: `docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/agent-1-spec/outputs/specification.md`
- Hephaestus inventory: `docs/agent-runs/20260621-183025-generate-code-java-hera-java-full-set/agent-2-code/outputs/inventory.md`
- Hephaestus handoff: `docs/agent-runs/20260621-183025-generate-code-java-hera-java-full-set/agent-2-code/handoff.md`
- Hephaestus Context7 notes: `docs/agent-runs/20260621-183025-generate-code-java-hera-java-full-set/agent-2-code/outputs/research-notes.md`
- Java source files inspected for command and API accuracy:
  - `src/main/java/edu/setu/transactionpipeline/Integrator.java`
  - `src/main/java/edu/setu/transactionpipeline/cli/PipelineOptions.java`
  - `src/main/java/edu/setu/transactionpipeline/cli/ValidateTransactionsCommand.java`
  - `src/main/java/edu/setu/transactionpipeline/model/TransactionResult.java`
  - `src/main/java/edu/setu/transactionpipeline/model/PipelineSummary.java`
  - `src/main/java/edu/setu/transactionpipeline/model/PipelineStatusReport.java`
- Themis retry inventory: `docs/agent-runs/20260621-201051-generate-tests-java-hera-java-full-set-retry/agent-3-tests/outputs/inventory.md`
- Themis retry validation checklist: `docs/agent-runs/20260621-201051-generate-tests-java-hera-java-full-set-retry/agent-3-tests/validation-checklist.md`
- Themis retry handoff: `docs/agent-runs/20260621-201051-generate-tests-java-hera-java-full-set-retry/agent-3-tests/handoff.md`
- Themis retry evidence files:
  - `agent-3-tests/evidence/maven-test-jacoco.txt`
  - `agent-3-tests/evidence/coverage-summary.txt`
  - `agent-3-tests/evidence/hook-pass.txt`
  - `agent-3-tests/evidence/hook-fail.txt`
  - `agent-3-tests/evidence/support-run-pipeline-success.txt`
  - `agent-3-tests/evidence/support-validate-transactions.txt`
  - `agent-3-tests/evidence/support-validate-transactions-hash.txt`
  - `agent-3-tests/evidence/privacy-scan.txt`
  - `agent-3-tests/evidence/root-containment.txt`
- Blocked Themis attempt:
  - `docs/agent-runs/20260621-191017-generate-tests-java-hera-java-full-set/agent-3-tests/handoff.md`
  - `docs/agent-runs/20260621-191017-generate-tests-java-hera-java-full-set/agent-3-tests/validation-checklist.md`
- Hera parent ledger:
  - `docs/agent-runs/20260621-180512-orchestrate-runs-java-full-set/agent-5-orchestrator/child-runs.md`
  - `docs/agent-runs/20260621-180512-orchestrate-runs-java-full-set/agent-5-orchestrator/selection-plan.md`

## Prior Homework Style Sources

- `../homework-1/README.md`
- `../homework-2/README.md`
- `../homework-3/README.md`
- `../homework-4/README.md`
- Additional HOWTORUN/ARCHITECTURE/TESTING_GUIDE/API_REFERENCE files discovered under Homeworks 1 through 4.

## Source Conclusions

- Python remains canonical through `python-canonical-20260621`.
- This Java package is a preserved alternate candidate and must not be described as selected.
- The usable Java Themis retry evidence passed with 13 tests and 87.86% JaCoCo instruction coverage.
- The Java candidate source spec intentionally differs from the canonical Python root spec.
- The current custom Python `pipeline-status` MCP reader can read Java result files for core safe fields when Java results are staged under its `shared/results` location, or when its helper functions are invoked with a candidate results directory.

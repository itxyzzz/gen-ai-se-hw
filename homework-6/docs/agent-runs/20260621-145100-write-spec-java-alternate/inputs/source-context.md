# Source Context

This Athena (Spec Writer) Java alternate consumed the Homework 6 control context required by Hera (Orchestrator):

- Homework guide: `agents.md`
- Assignment: `TASKS.md`
- Stack profile: `agent-control/write-spec/stack-profiles.md`
- Athena workflow and quality bar: `agent-control/write-spec/workflow.md`, `quality-bar.md`, `run-registry.md`
- Product brief: `agent-control/write-spec/transaction-system-brief.md`
- Current package-set registry: `docs/agent-runs/selection-sets.json`
- Current final selection audit: `docs/agent-runs/final-selection.md`
- Parent repository rules: `../AGENTS.md`, `../HOMEWORK_STANDARDS.md`, `../README.md`

The sample fixture was used for transaction count and expected safe outcomes. This context snapshot intentionally avoids raw account identifiers, raw descriptions, and full payload copies.

## Requested Stack Decisions

- Stack value: `java`
- Build tool: Maven
- Runtime source path: `src/main/java/edu/setu/banking/pipeline/...`
- Test path: `src/test/java/edu/setu/banking/pipeline/...`
- Money type: `java.math.BigDecimal`
- JSON library: Jackson Databind
- Test tool: JUnit Jupiter through Maven Surefire
- Coverage gate: JaCoCo `check` with an 80 percent covered-ratio threshold for Themis validation
- Result contract: `shared/results/summary.json` and `shared/results/TXN*.json` remain stack-neutral for the existing Python `pipeline-status` MCP reader.

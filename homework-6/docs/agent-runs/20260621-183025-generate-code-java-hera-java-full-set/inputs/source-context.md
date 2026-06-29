# Source Context

## Required Context Loaded

| Source | Status | Notes |
|---|---|---|
| `.agents/skills/generate-code/SKILL.md` | Read | Skill entrypoint for Hephaestus (Code Generator). |
| `agent-control/generate-code/workflow.md` | Read | Canonical workflow, run layout, Context7, validation, and handoff rules. |
| `agent-control/generate-code/quality-bar.md` | Read | Java package, privacy, validation, and scope requirements. |
| `agent-control/generate-code/run-registry.md` | Read | Preservation, inventory, and selection rules. |
| `agents.md` | Read | Layer glossary, Java alternate preservation, privacy, and package-set rules. |
| `TASKS.md` | Read | Assignment Task 2 scope plus later Task 3-5 boundaries. |
| `sample-transactions.json` | Read and copied | SHA-256 `771DA836CAAAA42921C628D6CD2E42D52E12687917BA60633E594D526BF4BF12`. |
| `mcp.json` | Read | Context7 and protected pipeline-status config observed, not changed. |
| `.codex/config.toml` | Read | Context7 and `agents.max_threads = 8` observed, not changed. |
| `../AGENTS.md` | Read | Branch, changelog, and homework standards guardrails. |
| `../HOMEWORK_STANDARDS.md` | Read | Homework quality and evidence standards. |
| `../README.md` | Read | Repository overview and PR/evidence expectations. |
| `docs/agent-runs/final-selection.md` | Read | Confirmed Python canonical package-set remains protected. |

## Explicit Source Specification

This run intentionally did not use root `specification.md`.

- Source Athena run ID: `20260621-180826-write-spec-java-hera-java-full-set`
- Source path: `docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/agent-1-spec/outputs/specification.md`
- Source SHA-256: `2FC5B597915CFD59D5786271DB0CDE96B06417DEEC1433D056B76D7023234CFC`

## Context7 Sources

| Query | Library ID | Applied to |
|---|---|---|
| Jackson ObjectMapper strict JSON, Java time, and BigDecimal handling | `/fasterxml/jackson-databind` | `JsonCodec`, DTO serialization, safe JSON output. |
| JUnit Jupiter Maven Surefire and `@TempDir` filesystem isolation | `/websites/junit_current` | JUnit tests and Maven Surefire setup. |
| JaCoCo Maven `report` and `check` goal with covered-ratio threshold | `/websites/jacoco_jacoco_trunk_doc` | `pom.xml` JaCoCo 80% check configuration. |

## Scope Boundaries

This run generated Task 2 Java candidate code only. It did not implement Task 3 slash commands/hooks, Task 4 MCP server/config changes, Task 5 reviewer docs/screenshots/PR draft, canonical selection, or root package copying.

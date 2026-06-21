# Phase 01 Test Cases Snapshot

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Short ID: `java-stack-and-hera-orchestration`
Status: Draft Review
Artifact type: Immutable pre-implementation snapshot after approval

## Purpose

This snapshot captures the expected behaviors Phase 01 must protect while making Homework 6 control surfaces Java-ready. It is not a generated test suite and does not authorize Java generation.

## Python Baseline Protection

| Case | Setup | Expected result |
|---|---|---|
| Current pipeline command still works | Run from the Homework 6 root with the selected Python package. | `python integrator.py` completes and reports 8 total sample transactions with no error count. |
| Current Python suite still passes | Run from the Homework 6 root. | `python -m pytest -p no:cacheprovider` passes the selected suite. |
| Current default coverage helper stays compatible | Run the existing command from the Homework 6 root. | `python scripts/check_coverage_gate.py --fail-under 80` still uses Python pytest coverage and passes at or above 80 percent. |
| Explicit Python coverage mode matches default | Run the new explicit Python mode after implementation. | `python scripts/check_coverage_gate.py --stack python --fail-under 80` succeeds with the same selected Python suite behavior. |
| Python command and validation docs remain available | Invoke or inspect `/run-pipeline` and `/validate-transactions` guidance after implementation. | Python examples are still present but guarded as `stack=python` or selected-Python behavior rather than unqualified universal behavior. |

## Java Readiness Cases

| Case | Setup | Expected result |
|---|---|---|
| Java spec generation guidance is concrete | Inspect Athena (Spec Writer) stack profile and quality guidance. | Java instructions name Maven, `pom.xml`, `BigDecimal`, Jackson or equivalent JSON handling, JUnit 5/JUnit Jupiter, JaCoCo, Java source/test paths, and a stack-neutral result JSON contract. |
| Java code-generation guidance is concrete | Inspect Hephaestus (Code Generator) workflow, quality bar, and registry. | Java run IDs, output layouts, validation commands, Context7 research topics, and privacy checks are present and not Python-only. |
| Java test-generation guidance is concrete | Inspect Themis (Test Generator) workflow, quality bar, and registry. | Java test paths, Maven/JUnit validation, JaCoCo evidence, and stack-neutral command/hook validation are present. |
| Java documentation guidance is concrete | Inspect Clio (Documentation Generator) workflow, quality bar, and registry. | Clio can document a Java alternate as a preserved stack set without implying canonical replacement. |
| Java coverage helper fails safely without Java project | Run from the current Python root after implementation. | `python scripts/check_coverage_gate.py --stack java --project-dir . --fail-under 80` fails with a clear missing `pom.xml` or Java-project message, not a traceback. |
| Java coverage helper command family is Maven-native | Inspect helper code and docs. | Java mode uses a Maven/JUnit/JaCoCo command family, planned as `mvn test jacoco:report jacoco:check`, and exposes the 80 percent threshold through documented plugin configuration or property. |

## Selection And Metadata Cases

| Case | Setup | Expected result |
|---|---|---|
| Human selection history remains readable | Inspect `docs/agent-runs/final-selection.md`. | Existing Python selection history is preserved and links to or describes the stack-set metadata. |
| Machine-readable selection metadata exists | Inspect `docs/agent-runs/selection-sets.json` after implementation. | JSON validates and contains the current canonical Python set with stack, status, selected run IDs, inventory paths, and command hints. |
| Java alternate is not selected accidentally | Inspect selection records after implementation. | No Java set is marked canonical or selected until Phase 03/04 and explicit operator selection. |
| Helpers do not parse raw transaction data for stack choice | Inspect helper code and metadata. | Stack resolution uses explicit arguments or selection metadata, not sample transactions or raw result payloads. |

## MCP Compatibility Cases

| Case | Setup | Expected result |
|---|---|---|
| MCP config remains valid | Run JSON validation. | `python -m json.tool mcp.json` succeeds. |
| Python FastMCP server remains the preferred status reader | Inspect `mcp/server.py` and related guidance. | `mcp/server.py` remains Python and reads stack-neutral `shared/results/summary.json` and `TXN*.json` result files. |
| Java guidance targets existing result shape | Inspect Java generation guidance. | Java outputs are expected to emit fields compatible with the existing safe result reader rather than requiring a Java MCP server. |

## Safety And Scope Cases

| Case | Setup | Expected result |
|---|---|---|
| Canonical product files are untouched | Inspect implementation diff. | `specification.md`, generated runtime files, selected tests, canonical reviewer docs, and screenshots are unchanged in Phase 01. |
| Hera is not implemented early | Inspect implementation diff. | Hera references remain limited to approved future-phase planning context; no Hera control package or skill is added in Phase 01. |
| Dirty config is not accidentally staged | Inspect staged files. | Pre-existing `.codex/config.toml` `max_depth = 2` is not included in Phase 01 unless separately authorized. |
| Privacy rules remain explicit | Inspect updated workflows and helper docs. | No raw account IDs, raw descriptions, credentials, tokens, or unfiltered metadata are printed in operation examples or evidence expectations. |

## Deferred Test Cases

The following cases belong to later phases:

- Running Maven tests against an actual generated Java package.
- Running JaCoCo against a generated Java package with a real `pom.xml`.
- Running Hera (Orchestrator) to create child Athena, Hephaestus, Themis, and Clio runs.
- Comparing preserved Python and Java stack sets.

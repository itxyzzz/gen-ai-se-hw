# Homework 6: Canonical Python Transaction Pipeline

> **Author**: Igor Tanatarov
> **Status**: Selected canonical Python package from Hera package-set run `20260621-215717-orchestrate-runs-python-full-set`
> **Canonical set**: `python-canonical-20260622-hera-full-set`
> **Scope**: Educational multi-agent transaction-processing simulation created by the Homework Automation Layer.

## Overview

This package documents the selected deterministic Python transaction-processing pipeline produced from the Hera-dispatched Athena (Spec Writer), Hephaestus (Code Generator), and Themis (Test Generator) runs. It reads the eight synthetic records in `sample-transactions.json`, moves audit-safe JSON messages through the required `shared/` protocol folders, and writes sanitized final results under `shared/results/`.

The pipeline is an educational simulation only. It does not move money, contact payment networks, perform identity checks, run sanctions screening, or make legal, banking, AML, KYC, PCI, settlement, fraud, or payment-network compliance decisions.

## Canonical Traceability

| Layer | Run ID | Evidence |
|---|---|---|
| Hera (Orchestrator) | `20260621-215717-orchestrate-runs-python-full-set` | Parent package-set run and child ledger |
| Athena (Spec Writer) | `20260621-220037-write-spec-python-hera-python-full-set` | Candidate specification SHA-256 `6F2E8CD844884DF06172EEB1CF92A2956BC45425FE514B7A820ABAEA249D2222` |
| Hephaestus (Code Generator) | `20260621-222543-generate-code-python-hera-python-full-set` | Candidate code inventory SHA-256 `CB19F97C1ED372905A764F5735EC6E3D0BDA6F1BEF132B723E258770B99C30DF` |
| Themis (Test Generator) | `20260621-224632-generate-tests-python-hera-python-full-set` | Candidate test inventory SHA-256 `FD3AFE4F20B5DBB697E536535A1706994BD6D1383CBF8C03B20056A5FC7761C4` |
| Clio (Documentation Generator) | `20260621-225923-generate-docs-python-hera-python-full-set` | Selected documentation package |

## Automation Agents

- Athena (Spec Writer): produced the Python transaction-system specification used by this canonical package.
- Hephaestus (Code Generator): generated the Python runtime components, JSON file protocol implementation, baseline tests, and Context7 research notes.
- Themis (Test Generator): added the selected candidate test expansion and validation evidence for runtime behavior, privacy checks, command behavior, and hook behavior.
- Clio (Documentation Generator): generated the reviewer-facing documentation package from the named records.
- Hera (Orchestrator): coordinated the package-set run and selected it after explicit operator authorization.

## Runtime Pipeline Agents

- Integrator: prepares `shared/`, preserves previous output under `archive/`, loads sample records, writes provenance, orchestrates components, and verifies completion.
- Transaction Validator: validates required fields, positive `Decimal` amounts, supported currencies, timestamps, and validation-only dry runs.
- Fraud Detector: applies deterministic educational risk scoring for high-value, unusual-time, channel, type, and destination-pattern signals.
- Settlement Processor: records final simulated outcomes while preserving rejected and review-required statuses.
- Reporting Agent: writes privacy-safe transaction results, `summary.json`, and `pipeline-status.json`.
- Pipeline Status MCP Server: reads existing result files through safe tools and a `pipeline://summary` resource without rerunning the pipeline.

## Architecture At A Glance

```text
sample-transactions.json
        |
        v
integrator.py
        |
        +--> shared/input/TXN*.json
        |
        v
Transaction Validator
        |
        +--> shared/processing/TXN*.json
        |
        v
Fraud Detector
        |
        +--> shared/output/TXN*.json
        |
        v
Settlement Processor
        |
        v
Reporting Agent
        |
        +--> shared/results/TXN*.json
        +--> shared/results/summary.json
        +--> shared/results/pipeline-status.json
        |
        v
mcp/server.py read-only status tools
```

```mermaid
flowchart LR
    Samples["sample-transactions.json"] --> Integrator["Integrator"]
    Integrator --> Input["shared/input"]
    Input --> Validator["Transaction Validator"]
    Validator --> Processing["shared/processing"]
    Processing --> Fraud["Fraud Detector"]
    Fraud --> Output["shared/output"]
    Output --> Settlement["Settlement Processor"]
    Settlement --> Reporting["Reporting Agent"]
    Reporting --> Results["shared/results"]
    Results --> MCP["pipeline-status MCP server"]
```

## Tech Stack

| Area | Choice |
|---|---|
| Runtime language | Python 3.12 |
| Money handling | `decimal.Decimal`, serialized as strings |
| Runtime protocol | JSON files in `shared/input`, `shared/processing`, `shared/output`, `shared/results` |
| Tests | `pytest`, `pytest-cov` |
| Coverage gate | `scripts/check_coverage_gate.py --stack python --fail-under 80` |
| Hook | `.githooks/pre-push` delegates to the coverage helper |
| MCP | `context7` plus custom FastMCP `pipeline-status` server |
| Selected evidence | `docs/agent-runs/20260621-225923-generate-docs-python-hera-python-full-set/agent-4-docs/evidence/` |

## Quick Start

Run these commands from the homework root.

```powershell
python integrator.py
python -m pytest -p no:cacheprovider
python scripts\check_coverage_gate.py --stack python --fail-under 80
```

Expected pipeline summary:

```text
Pipeline complete: total=8 settled=2 rejected=2 review_required=4 error=0
```

Fresh Clio evidence:

- `python integrator.py`: passed with 8 results.
- `python -m pytest -p no:cacheprovider`: passed with 36 tests.
- `python scripts\check_coverage_gate.py --stack python --fail-under 80`: passed with 97.44% total coverage.
- `python scripts\check_coverage_gate.py --stack python --fail-under 99`: failed as expected to demonstrate the blocking path.

Post-selection root validation passed with 52 tests and 95.57% total coverage because the root suite also includes Operator Layer support-surface tests for the coverage helper and MCP server.

## Documentation Map

- [HOWTORUN.md](HOWTORUN.md): setup, run, validation-only, tests, coverage, hook, MCP, screenshots, and cleanup steps.
- [ARCHITECTURE.md](ARCHITECTURE.md): layered architecture, JSON protocol, runtime components, privacy design, and known limitations.
- [TESTING_GUIDE.md](TESTING_GUIDE.md): Themis candidate test strategy, command evidence, coverage gate, and manual checklist.
- [API_REFERENCE.md](API_REFERENCE.md): command interfaces, JSON shapes, validation-only behavior, and MCP tools/resource.
- [docs/pr-description-draft.md](docs/pr-description-draft.md): standalone PR body for this selected package.

## AI Tools And Workflow Summary

The selected package set was generated by a Hera (Orchestrator) run using first-level child agents for Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator). This Clio child did not use nested executor sub-agents.

The work was completed primarily in the Codex app with Codex. As in the previous homework, some operations had to happen in a Codex project opened directly at the `homework-6` folder because Codex tool discovery is sensitive to the relationship between project root, git root, and assignment folder.

The operator workflow used local `dev-doc-harness` planning artifacts from `https://github.com/itxyzzz/dev-doc-harness`. Homework Automation Layer agents were explicitly instructed not to follow the harness, so generated specs, code, tests, docs, and Hera orchestration evidence remain portable assignment artifacts.

Hephaestus documented two Context7 queries in `research-notes.md`: `/python/cpython` for `Decimal`, strict JSON, and filesystem guidance, and `/pytest-dev/pytest` for `tmp_path` and `monkeypatch` isolation patterns. The generated runtime and tests apply those decisions directly.

## Multi-Stack Evidence

The canonical root package is Python, but the Operator Layer pipeline support now understands both Python and Java package sets. `scripts/check_coverage_gate.py` accepts `--stack python` and `--stack java`, and `agent-control/operate-pipeline/commands-and-hooks.md` records stack-specific run, validation, and coverage behavior.

Historical runs are preserved under `docs/agent-runs/`. The Java stack is available as preserved alternate evidence in Hera run `20260621-180512-orchestrate-runs-java-full-set`, with Java Athena, Hephaestus, Themis retry, and Clio child runs retained for review. Java remains evidence, not the canonical root package.

## Review Evidence

| Evidence | Canonical path |
|---|---|
| Pipeline run | `docs/screenshots/pipeline-run.png` |
| Test coverage | `docs/screenshots/test-coverage.png` |
| `/run-pipeline` behavior | `docs/screenshots/skill-run-pipeline.png` |
| Hook trigger | `docs/screenshots/hook-trigger.png` |
| MCP interaction | `docs/screenshots/mcp-interaction.png` |
| Clio evidence | `docs/agent-runs/20260621-225923-generate-docs-python-hera-python-full-set/agent-4-docs/evidence/` |
| Full operator screenshot set | `docs/screenshots/operator-sourced/` |

## Current Limits

- Historical package sets remain preserved under `docs/agent-runs/`; the selected canonical root package is the latest Hera-generated Python set.
- The Java package set is preserved as alternate evidence and is not selected as the canonical root package.
- Direct shell execution of the run-local pre-push hook was blocked by the Windows sandbox; the delegated coverage helper was validated through passing and expected-failure commands.
- The MCP subprocess configuration reads root `shared/results/`; Clio validated candidate compatibility by importing `mcp/server.py` by file path and pointing helper functions at candidate result files.

# Homework 6 Agent Guide

## Context Load Order

Before changing Homework 6 files, load context in this order:

1. System, tool, sandbox, model, plugin, and MCP constraints.
2. Repository `AGENTS.md`, `HOMEWORK_STANDARDS.md`, and root `README.md`.
3. For operator-layer maintenance, `homework-6/TASKS.md` and `homework-6/sample-transactions.json`.
   For Athena (Spec Writer) normal generation, use `homework-6/agent-control/write-spec/transaction-system-brief.md` instead of `TASKS.md`.
   For Hephaestus (Code Generator) normal generation, use selected `homework-6/specification.md`, `TASKS.md` Task 2 checks, Context7 MCP configuration, and `homework-6/agent-control/generate-code/`.
   For Themis (Test Generator) normal generation, use `docs/agent-runs/final-selection.md`, the selected Hephaestus (Code Generator) inventory, `agent-control/generate-tests/`, and `agent-control/operate-pipeline/` only as external support-tool behavior to validate.
   For Clio (Documentation Generator) normal generation, use `docs/agent-runs/final-selection.md`, selected Hephaestus and Themis inventories, `agent-control/generate-docs/`, `agent-control/operate-pipeline/`, Task 4 MCP files, prior Homeworks 1-4 documentation for author/style context, and `docs/screenshots/operator-sourced/`.
   For Hera (Orchestrator) normal orchestration, use `docs/agent-runs/selection-sets.json`, `docs/agent-runs/final-selection.md`, named child inventories, `agent-control/orchestrate-runs/`, and the relevant child-agent control package for each child operation.
4. This `homework-6/agents.md`.
5. Selected or active run artifacts under `homework-6/docs/agent-runs/`.
6. For Athena (Spec Writer), the `write-spec` skill or slash command and its references.
   For Hephaestus (Code Generator), the `generate-code` skill or slash command and its references.
   For Themis (Test Generator), the `generate-tests` skill or slash command and its references.
   For Clio (Documentation Generator), the `generate-docs` skill or slash command and its references.
   For Hera (Orchestrator), the `orchestrate-runs` skill or slash command and its references.
7. Current git status, existing diffs, and relevant tests or validation output.

When instructions conflict, preserve the highest-priority user and repository rules first, then the most specific Homework 6 artifact.

## Layer Glossary

Use this glossary before using Greek identity labels. In technical prompts and instructions, write paired labels such as `Athena (Spec Writer)` until this glossary has been loaded; never rely on `Athena` alone to carry responsibility.

| Layer | Meaning | Naming rule |
|---|---|---|
| Operator Layer | The human/Codex layer that maintains Homework 6 control surfaces, uses repository planning, updates changelogs, and repairs the homework automation setup. | May use harness terms because it is outside the generated agents. |
| Homework Automation Layer | The four assignment deliverable generation agents that create the transaction-processing system artifacts, plus Hera (Orchestrator) as an operator-added control surface for cross-run sequence, comparison, and explicit selection. | Uses Greek identity plus functional role labels listed below. |
| Generated Transaction System Layer | The actual transaction-processing software, tests, docs, MCP server, commands, hooks, and runtime artifacts produced by the homework automation agents. This layer includes deterministic software components and runtime application agents. | Uses product and component names, not Greek identities. |
| Runtime transaction pipeline agents | Stack-native application components inside the generated software, such as Transaction Validator, Fraud Detector, Settlement Processor, Compliance Checker, Reporting Agent, and Integrator. They are implemented as Python modules/classes, Java classes, or equivalent stack-native components with a shared message-processing protocol. | Keep functional component names. Do not name these components Athena, Hephaestus, Themis, Clio, or Hera, and do not implement them as Claude/Codex skills. |
| Executor sub-agents | Optional worker agents used by a tool runtime to decompose a generation or implementation task. They may inspect, draft, test, or review bounded scopes. | Keep functional names such as domain-research sub-agent or final-review sub-agent. Do not confuse them with named Homework Automation Layer agents. |

## Homework Automation Agent Roles

| Agent | Role | Primary outputs |
|---|---|---|
| Athena / Spec Writer / write-spec | Creates the detailed technical specification for the transaction-processing system. | `specification.md`, spec support docs, research notes, and preserved generation runs. |
| Hephaestus / Code Generator | Builds the generated transaction-processing software from the selected specification. | Integrator, at least three cooperating runtime pipeline components, JSON file protocol, and Context7 research notes. Refreshed generation guidance intentionally targets at least four runtime components for stronger coverage. |
| Themis / Test Generator | Creates or extends selected-code test suites and test-quality evidence for the generated transaction-processing software. | Unit tests, integration tests, test fixtures/config, validation evidence, run inventories, and selected test-package records. |
| Clio / Documentation Generator | Produces reviewer-facing documentation and handoff evidence for the generated transaction-processing software. | README, HOWTORUN, architecture and testing docs, screenshots, final test evidence narrative, and final PR support. |
| Hera / Orchestrator / orchestrate-runs | Coordinates preserved stack-specific package sets across the other Homework Automation Layer agents. | Hera run metadata, child-run ledger, comparison notes, selection plans, validation checklists, and orchestration handoff records. |

Athena (Spec Writer) is stack-flexible through the fixed enum in `agent-control/write-spec/stack-profiles.md`. The default generation stack is `python`; `java` is an optional alternate profile. `auto` is not supported. After stack selection, every generated `specification.md` must be concrete for that stack.

Athena's Claude Code surface is intentionally the modern project skill at `.claude/skills/write-spec/SKILL.md`. The assignment's `.claude/commands/write-spec.md` path is treated as an example slash-invoked workflow path; this repository uses the newer project skill because Claude Code exposes project skills as slash invocations and same-named skills supersede legacy command wrappers.

Hephaestus (Code Generator) uses the tool-neutral control package at `agent-control/generate-code/`. Codex entrypoint: `.agents/skills/generate-code/SKILL.md`. Claude Code entrypoint: `.claude/skills/generate-code/SKILL.md`. A Hephaestus run consumes the selected `specification.md`, uses Context7 during code generation, and documents at least two Context7 query records in canonical `research-notes.md`. Hephaestus may use executor sub-agents up to the Homework 6 `agents.max_threads = 8` configuration without additional operator approval; it should use curated context, deliberate model/reasoning selection, and orchestrator-owned final integration.

The assignment minimum is at least three cooperating runtime transaction pipeline components. The current selected package satisfies that minimum with Transaction Validator, Fraud Detector, and Settlement Processor. Refreshed Athena (Spec Writer) and Hephaestus (Code Generator) guidance intentionally targets at least four runtime components, with Reporting Agent as the preferred fourth component, to strengthen future generated candidates without making the current selected package non-compliant.

Themis (Test Generator) uses the tool-neutral control package at `agent-control/generate-tests/`. Codex entrypoint: `.agents/skills/generate-tests/SKILL.md`. Claude Code entrypoint: `.claude/skills/generate-tests/SKILL.md`. Claude Code legacy command wrapper: `.claude/commands/generate-tests.md`. A Themis run targets a named selected Hephaestus software version, records the selected code run ID, inventory path, selection record, source Athena run ID, source and current spec fingerprints, and selected code fingerprints, then writes candidate tests under a preserved run-local `agent-3-tests/outputs/` package before any explicit canonical selection. Themis owns test quality beyond raw coverage: meaningful assertions, unit and integration coverage, dry-run validation, privacy/audit checks, fixture isolation, repeated-run behavior, and validation of command/hook support surfaces.

Pipeline operation support surfaces are maintained separately under `agent-control/operate-pipeline/`. Claude command wrappers live at `.claude/commands/run-pipeline.md`, `.claude/commands/validate-transactions.md`, and `.claude/commands/generate-transactions.md`; modern project skills live under both `.agents/skills/` and `.claude/skills/`; the portable transaction generator is `scripts/generate_transactions.py`; the portable coverage helper is `scripts/check_coverage_gate.py`; the Git hook is `.githooks/pre-push`; and the Claude hook setting is `.claude/settings.json`. These are Operator Layer outer tools. Themis validates them and reports gaps, but it must not treat `/run-pipeline`, `/validate-transactions`, `/generate-transactions`, or the coverage hook as routine Themis per-run outputs.

The `/generate-transactions` surface produces synthetic transaction lists in the canonical input format for use with the validation and pipeline surfaces. It is written against the current `agents.transaction_validator`, `agents.fraud_detector`, and `integrator` rules so it can deliberately emit both valid records (settled and fraud-review) and invalid records (one per validator reason code). It is a test-data generator, not a runtime transaction pipeline component, and is not named after a Greek deity.

Clio (Documentation Generator) consumes selected Themis results for final Task 5 documentation and evidence. Clio reruns the selected test suite, captures reviewer-facing screenshots, and writes the final testing narrative. If Clio finds a test gap, it should request a Themis follow-up or record an explicit final test-hardening delta instead of silently replacing or forking the selected Themis suite.

Clio (Documentation Generator) uses the tool-neutral control package at `agent-control/generate-docs/`. Codex entrypoint: `.agents/skills/generate-docs/SKILL.md`. Claude Code entrypoint: `.claude/skills/generate-docs/SKILL.md`. Claude Code legacy command wrapper: `.claude/commands/generate-docs.md`. A Clio run consumes the selected Athena (Spec Writer), Hephaestus (Code Generator), and Themis (Test Generator) records; prior Homeworks 1-4 documentation as author/style examples; Task 4 MCP files; command/hook support guidance; and operator-sourced screenshots. Clio writes candidate docs under a preserved run-local `agent-4-docs/outputs/` package before canonical selection. Clio must preserve every source screenshot under `docs/screenshots/operator-sourced/`, copy only required safe evidence screenshots to stable reviewer-facing paths, and produce `docs/pr-description-draft.md` without an operator challenges or feedback narrative.

After Clio control-surface repairs, do not directly edit the existing selected Clio documentation output to make it appear regenerated. Run Clio again or explicitly select a refreshed documentation package in a separate step before updating canonical reviewer docs, stable screenshots, `docs/pr-description-draft.md`, selected Clio run records, or final-selection screenshot mappings.

Hera (Orchestrator) uses the tool-neutral control package at `agent-control/orchestrate-runs/`. Codex entrypoint: `.agents/skills/orchestrate-runs/SKILL.md`. Claude Code entrypoint: `.claude/skills/orchestrate-runs/SKILL.md`. Claude Code legacy command wrapper: `.claude/commands/orchestrate-runs.md`. A Hera run consumes `docs/agent-runs/selection-sets.json`, `docs/agent-runs/final-selection.md`, named child inventories, and the relevant child-agent packages before it dispatches, resumes, compares, or selects package sets. Hera must not silently target "latest" files; every child invocation names the parent Hera run ID, mode, stack, source or selected run IDs, inventories, selection records, fingerprints, and package-set ID when one exists. Hera is not a runtime transaction pipeline component, Java class, Python product module, MCP tool, settlement component, reporting component, or generated product dependency.

This `homework-6/agents.md` file is the standing project-level guide required by Task 1. It lives beside `TASKS.md` so every run and downstream Homework Automation Layer agent can load the same stable context. Do not regenerate or overwrite it during individual Athena (Spec Writer) runs; if a run discovers a needed guide change, record the recommendation in that run's handoff and apply it as a separate control-surface update.

`TASKS.md` stays the frozen operator assignment. Athena (Spec Writer) uses `agent-control/write-spec/transaction-system-brief.md` as its direct product input so the generated specification targets the transaction-processing system, not the homework automation harness.

## Shared Data and Run Artifacts

Hera (Orchestrator) run folders use `agent-5-orchestrator/` inside `docs/agent-runs/YYYYMMDD-HHMMSS-orchestrate-runs-<stack>-short-label/`. `child-runs.md` is Hera's mandatory ledger for child Athena, Hephaestus, Themis, and Clio run IDs, modes, stacks, inventories, validation status, blockers, and next actions. `selection-plan.md` is only a proposal until the operator explicitly authorizes inventory-driven selection.

Generated Transaction System Layer components must use the required JSON file protocol:

```text
shared/
  input/
  processing/
  output/
  results/
```

Major Homework Automation Layer run artifacts must be preserved under `docs/agent-runs/` before canonical copy. Use run IDs in this format:

```text
YYYYMMDD-HHMMSS-<agent-or-pipeline>-<stack>-<short-label>
```

Do not overwrite canonical `specification.md`, generated docs, or final results until the operator selects a run or explicitly authorizes the overwrite.

For Hera (Orchestrator), `generate-set`, `resume-set`, and `compare-set` never authorize canonical copy by themselves. `select-set` requires an explicit operator selection, inventory-declared copy targets, updates to `docs/agent-runs/final-selection.md`, and a valid `docs/agent-runs/selection-sets.json`. Python remains canonical until the operator explicitly changes the canonical package set.

## Research and MCP Rules

- Athena (Spec Writer) may perform and document current technical or domain research before producing the spec package.
- Hephaestus (Code Generator) must use Context7 during code generation and document at least two queries in `research-notes.md`.
- Each Context7 note should include search text, returned library ID, access date, and applied insight.
- If Context7 or web research is unavailable, record the limitation and continue from local assignment context.
- `pipeline-status` must be added to `mcp.json` and `.codex/config.toml` only after `mcp/server.py` exists.
- A homework-root Codex project may be needed for reliable discovery of homework-local MCP config.

## Privacy and Audit Rules

- Treat account identifiers, descriptions, transaction metadata, and audit details as sensitive.
- Never log plaintext PII or full account identifiers. Redact examples such as `ACC-****1001`.
- Use precise decimal types for money: `decimal.Decimal` for Python or `BigDecimal` for Java. Never use binary floating point for amounts.
- Validate currency with ISO 4217-style codes and reject unsupported sample values such as `XYZ`.
- Audit records should include timestamp, runtime component name, transaction ID, safe outcome, and reason code when applicable.
- Keep the banking pipeline framed as an educational simulation, not as legal, banking, AML, sanctions, or payment-network compliance.

## Homework Automation Rules Without Harness

Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), Clio (Documentation Generator), and Hera (Orchestrator) must be able to run without `dev-doc-harness` or Superpowers. Those tools belong to the Operator Layer when repository maintainers change the automation surfaces.

1. Read the required context.
2. Define or confirm the bounded task.
3. Preserve major generated outputs under a run folder when regenerating or comparing.
4. Write research notes or fallback limitations.
5. Validate required sections, commands, tests, and privacy rules before reporting completion.
6. Update `CHANGELOG.md` for committed homework increments.
7. Review the diff for unrelated changes, unresolved draft markers, canonical overwrite, and generated noise.

Do not introduce harness freeze gates, planning package requirements, or Superpowers-only flows into prompts for Homework Automation Layer agents. Athena (Spec Writer) must produce a full transaction-system specification in one `specification.md`, not a harness-style split between spec and plan.

## Harness and Superpowers Compatibility

When repository instructions require harness-managed planning, that applies to Operator Layer maintenance work. Use homework-scoped work items under:

```text
homework-6/docs/work-items/<work-id>/
```

Harness and Superpowers may assist Operator Layer planning, execution, verification, and review, but the Homework 6 automation agents and generated transaction-system specs must remain portable. Do not make a Homework Automation Layer agent or Generated Transaction System Layer artifact depend on hidden harness state, a prior chat thread, or unavailable plugins.

## Final Selection Rules

- Preserve every meaningful Athena (Spec Writer) generation attempt under `docs/agent-runs/`.
- Compare runs before selection when more than one viable output exists.
- After the first successful `write-spec generate` run, if `specification.md` does not exist yet, copy the run's selected `specification.md` to the canonical path automatically and record the auto-selection.
- Record the chosen run in `docs/agent-runs/final-selection.md`.
- Record date, run ID, stack, selected files, copied canonical paths, rationale, operator, and post-selection edits.
- Treat `specification.md` as the default selected package. Copy supporting docs only when explicitly selected, and never copy a run-local agent guide over this file.
- Canonical files are submission files; run folders are evidence snapshots. Keep both roles distinct.

## Code And Test Version Traceability

- Preserve every meaningful Hephaestus (Code Generator) candidate under its run folder before canonical copy, with a complete `agent-2-code/outputs/` package and inventory.
- Each selected Hephaestus code package must record the selected Athena (Spec Writer) source run ID, canonical spec path, and `specification.md` SHA-256 fingerprint.
- The first successful Hephaestus code package may be selected by default when no selected code package exists. Later selections require an explicit selection record and clean replacement of the prior inventory-declared canonical targets.
- Runtime output folders such as `shared/` and `archive/` are execution evidence, not selectable code packages.
- Future Themis (Test Generator) runs must name the selected Hephaestus software version they target, including the selected code run ID, inventory path, selection record, and stable file or package fingerprints. Do not silently target "latest" when tests are generated or selected.
- Future Themis runs must copy the selected Hephaestus package into a run-local `workspace/selected-code/`, write candidate tests/config first under `agent-3-tests/outputs/`, overlay those outputs into `workspace/project-under-test/`, and run validation from that workspace. Root `tests/`, root `shared/`, root `.coverage`, and canonical product files stay untouched until explicit inventory-driven test selection.
- Future Clio (Documentation Generator) runs must name the selected Athena, Hephaestus, and Themis versions they document, including inventory paths, selection record, and stable file or package fingerprints. Do not silently target "latest" when docs or screenshots are generated or selected.
- Future Clio runs must write candidate documentation and screenshot outputs first under `agent-4-docs/outputs/`, with an inventory declaring canonical targets. Canonical `README.md`, `HOWTORUN.md`, `ARCHITECTURE.md`, `TESTING_GUIDE.md`, `API_REFERENCE.md`, `docs/pr-description-draft.md`, and stable `docs/screenshots/*.png` targets stay untouched until explicit inventory-driven documentation selection or first-success auto-selection when no final documentation package exists.
- Future Hera (Orchestrator) runs must preserve parent orchestration evidence first under `agent-5-orchestrator/`. Hera may propose selection in `selection-plan.md`, but canonical root files and `canonical_set_id` stay unchanged until the operator explicitly authorizes `select-set` for a named package set or run. If nested child-agent dispatch is unavailable despite `.codex/config.toml` `agents.max_depth = 2`, Hera records the limitation and uses first-level child-agent dispatch when available; child agents continue without their own nested sub-agents only when they can still satisfy their quality bars.

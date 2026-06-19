# Generate Code Workflow

This is the canonical workflow for both Homework 6 Hephaestus (Code Generator) entrypoints:

- Codex Markdown skill: `homework-6/.agents/skills/generate-code/SKILL.md`
- Claude Code project skill, exposed as `/generate-code`: `homework-6/.claude/skills/generate-code/SKILL.md`

The entrypoints must stay thin. Update this package first when the workflow changes, and do not keep independent fallback generation logic in either wrapper.

All paths in this file are repository-root relative unless the active project root is already `homework-6`. When running from a homework-root project, remove the leading `homework-6/` prefix from homework-local paths while keeping root-level references such as `AGENTS.md`, `HOMEWORK_STANDARDS.md`, and `README.md` relative to the repository root when available.

## Required Context

Read product, assignment, MCP, and control context before writing or comparing outputs:

1. `homework-6/specification.md`: selected Athena (Spec Writer) transaction-system specification.
2. `homework-6/sample-transactions.json`: canonical sample input.
3. `homework-6/agents.md`: layer glossary, privacy rules, MCP rules, and Homework Automation Layer responsibilities.
4. `homework-6/TASKS.md`: assignment checks for Task 2 and boundaries against Tasks 3-5.
5. Root `AGENTS.md`, `HOMEWORK_STANDARDS.md`, and `README.md` when available.
6. `homework-6/mcp.json` and `homework-6/.codex/config.toml`: Context7 configuration and `agents.max_threads = 8`.
7. This package's `quality-bar.md` and `run-registry.md`.

If `specification.md` is missing or does not declare a concrete selected stack, stop and ask the operator to select or repair the Athena output before code generation. Do not infer a stack from memory.

Repository `dev-doc-harness` and Superpowers requirements apply to Operator Layer maintenance of this package. They do not apply inside Hephaestus (Code Generator) runs or generated product files. Copy any needed sub-agent/model guidance into this workflow instead of requiring Hephaestus to load harness policy.

## Operating Modes

- `generate`: create or update Task 2 Generated Transaction System Layer files from the selected spec.
- `resume`: continue a bounded Hephaestus run using its preserved handoff and run evidence.
- `compare`: compare two or more preserved Hephaestus runs without changing canonical product files.

Default to `generate` unless the operator asks for comparison or continuation.

## Run Setup

Use run IDs in this format:

```text
YYYYMMDD-HHMMSS-generate-code-python-short-label
```

Create the run folder before drafting or editing product files:

```text
homework-6/docs/agent-runs/RUN_ID/
  run-metadata.md
  inputs/
    source-context.md
  agent-2-code/
    outputs/
      sample-transactions.json
      inventory.md
    handoffs/
      sub-agent-plan.md
    review/
    research-notes.md
    validation-checklist.md
    handoff.md
  comparison.md
```

`comparison.md` is required only for compare mode or when more than one viable run is being evaluated.

`run-metadata.md` must record:

- Run ID, mode, selected stack, start time, and orchestration tool.
- Whether Context7 was reachable.
- Source Athena (Spec Writer) run ID, canonical source spec path, and SHA-256 fingerprint of the selected `specification.md`.
- The planned sub-agent strategy and observed runtime limits.
- The canonical files the run intends to create or modify.
- Any pre-existing product files or dirty git state.

`inputs/source-context.md` must list the exact source artifacts read, including the selected `specification.md`, sample data, assignment task file, agent guide, MCP config, and control package references.

`agent-2-code/outputs/` is the complete candidate product package for the run. Create it before drafting or editing generated product files. It must include generated source files, generated tests when present, canonical `research-notes.md` content, and `inventory.md`. The inventory must list each selectable file, its canonical target, kind, and stable fingerprint; it must also list runtime and tool-output exclusions.

Copy canonical `homework-6/sample-transactions.json` into `agent-2-code/outputs/sample-transactions.json` during run setup. Record the canonical sample source path and SHA-256 fingerprint in `run-metadata.md`, `inputs/source-context.md`, or `inventory.md` so candidate validation is traceable to the selected input fixture.

Candidate validation commands run from `agent-2-code/outputs/` and use the copied local `sample-transactions.json` by default. Local `shared/` is commit-capable last-run evidence for the candidate package, but it is runtime evidence, not selectable code. Local `archive/`, `.coverage*`, `.test-tmp/`, `__pycache__/`, and `.pytest_cache/` are tool/runtime outputs and must stay out of committed evidence and selectable inventory.

## Context7 Requirement

Hephaestus must use Context7 during code generation. Document at least two Context7 queries in canonical `homework-6/research-notes.md` and mirror them in the run-local `agent-2-code/research-notes.md`.

Each entry must include:

- Query number and topic.
- Access date.
- Search text.
- Returned Context7 library ID.
- Key insight or code pattern applied.
- Files or tasks influenced.

Good Python Task 2 query topics include:

- Python `decimal.Decimal` monetary arithmetic and quantization patterns.
- Python `pathlib`, `json`, or file-operation patterns for safe JSON protocol handling.
- `pytest` and temporary-directory test patterns when generating verification tests for the pipeline.
- `argparse` CLI patterns for `python integrator.py`.
- FastMCP only if the generated code reaches read-only result helper design; do not add MCP configuration during Task 2.

If Context7 is unavailable, stop and ask the operator whether to switch to a Context7-enabled project/thread or proceed with a documented limitation. Do not silently mark the assignment requirement satisfied without Context7-backed notes.

## Executor Sub-Agent Strategy

Hephaestus is authorized to spawn executor sub-agents up to the Homework 6 `agents.max_threads = 8` cap without additional operator approval. This approval comes from the frozen Hephaestus planning package and does not need to be repeated in the later start prompt.

Preferred pattern:

- Use one bounded executor sub-agent per selected `specification.md` low-level task or independently testable implementation slice when that improves quality.
- Run sub-agents in waves when there are more slices than available active threads.
- Use fewer sub-agents when a task is tightly coupled, same-file coordination would be brittle, or parallelism would degrade consistency.
- Keep the orchestration thread responsible for final decomposition, file ownership boundaries, integration, conflict resolution, validation, and the final user-facing report.

Context strategy labels:

- `curated prompt`: a narrow task prompt with selected paths, facts, constraints, and expected output.
- `curated artifacts`: specific specs, plans, handoffs, diffs, or run evidence that should ground the work.
- `full-history fork`: conversation history is needed because prior discussion nuance cannot be reconstructed compactly. Use sparingly.
- `no repo context`: the sub-agent only needs supplied text or a narrow external artifact.

Prefer `curated prompt` or `curated artifacts`. Do not use full-history forks as a convenience default.

For each planned sub-agent, record in `agent-2-code/handoffs/sub-agent-plan.md`:

- Purpose.
- Context strategy.
- Input context.
- Output artifact.
- Model policy, using the active Homework 6 policy or `enterprise-default` if no operator override exists.
- Policy-relative model class/profile.
- Reasoning effort.
- Reason for selection.
- Whether the task can run in parallel.
- Blast radius if wrong.

Model and reasoning guidance:

- Use the strongest available model/reasoning profile for architecture-sensitive implementation, privacy/audit decisions, data-shape decisions, final integration review, and failures after one cheaper attempt.
- Use a smaller/faster current profile only for bounded source inventory, summarization, mechanical file inspection, or non-authoritative support.
- Do not use older or cheaper models solely to save cost when correctness or privacy risk is material.
- If model or reasoning controls are unavailable, record the requested policy-relative choice and the observed limitation in the run metadata.

Every sub-agent report must include:

- Assigned scope.
- Files inspected or changed.
- Commands and tests run.
- Assumptions.
- Uncertainty or residual risk.
- Recommended next step.

If the runtime cannot spawn sub-agents, record the limitation and proceed only when the orchestration thread can preserve quality through smaller local increments and focused review. Absence of sub-agents must be justified by quality concerns, runtime unavailability, or tight coupling, not by missing operator reapproval.

## Generation Scope

Hephaestus generates Task 2 Generated Transaction System Layer code only:

- `integrator.py` or equivalent orchestrator.
- At least four cooperating runtime transaction pipeline components when the selected spec follows the refreshed Athena (Spec Writer) quality target. The normal component set is Transaction Validator, Fraud Detector, Settlement Processor, and Reporting Agent.
- JSON file protocol through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- Safe shared utilities for Decimal money, JSON writing, redaction, timestamps, and audit events.
- Result files and summaries that later Task 4 status tooling can read.
- Focused code-level tests when needed to verify the generated pipeline and protect the implementation.
- Canonical `research-notes.md` documenting Context7 usage.

Generated transaction pipelines must be rerunnable with clearly separated runtime evidence. At startup, the integrator must archive an existing configured `shared/` tree beside that configured path under zero-padded incrementing folders such as `archive/shared-001`, `archive/shared-002`, and `archive/shared-003`, then create a fresh `shared/input`, `shared/processing`, `shared/output`, and `shared/results` tree for the current run.

If the selected specification requires runtime provenance, Hephaestus may generate product code that writes a minimal `shared/run-provenance.json` file into the fresh shared tree. That file may record immutable source/version references such as selected Athena run ID, source spec path/fingerprint, selected Hephaestus run ID, and selected output inventory path or package fingerprint. The generated product must not perform run selection, compare preserved run folders, or depend on hidden chat state.

Hephaestus must not implement these later deliverables during a normal Task 2 run:

- Task 3 slash commands, hooks, or enforced coverage gate.
- Task 4 custom `mcp/server.py`, `pipeline-status` MCP config additions, or `.codex/config.toml` expansion.
- Task 5 README, HOWTORUN, final docs, screenshots, or PR packaging.

The selected `specification.md` may define result shapes that make later MCP tooling easy. That is allowed. Adding MCP server/config files is not allowed until the later MCP task or a separate explicit operator instruction.

## Layer Rules

Hephaestus is a Homework Automation Layer agent. Generated runtime product modules must keep functional names such as Transaction Validator, Fraud Detector, Settlement Processor, Reporting Agent, Compliance Checker, or Integrator. Do not name runtime product modules, classes, files, or pipeline components Athena, Hephaestus, Themis, or Clio.

Generated product files must not depend on:

- `dev-doc-harness`.
- Superpowers.
- Planning freeze gates.
- Prior chat state.
- Preserved run folders.
- Canonical-copy or final-selection mechanics.

Runtime provenance is the narrow exception: generated code may record selected run IDs, source paths, and fingerprints when the selected specification requires it, but runtime behavior must not require reading preserved run folders or executing selection workflows.

## Output Selection

Hephaestus writes candidate code to `agent-2-code/outputs/` first. Canonical Homework 6 product paths are updated only through selection:

1. If this is the first successful code-generation run and no selected code package exists, auto-select it by copying the candidate files from `agent-2-code/outputs/` to their inventory-declared canonical targets.
2. For later selections, require explicit operator selection. Remove the previously selected canonical targets declared by the prior selected inventory, then copy the chosen replacement package to the declared canonical targets.
3. Record the selected Hephaestus run ID, source Athena run ID, source spec SHA-256, inventory path, selected files, canonical targets, rationale, operator, and excluded runtime/tool paths in `docs/agent-runs/final-selection.md` or a linked code-selection record.
4. Preserve every meaningful Hephaestus run folder even when it is not selected.

Do not copy runtime evidence such as `shared/` or `archive/` during selection.

## Validation And Handoff

Before reporting a Hephaestus run complete:

1. From `agent-2-code/outputs/`, run the generated pipeline command against the local fixture, normally `python integrator.py --input sample-transactions.json --shared-dir shared`.
2. Verify all sample transactions appear under local `shared/results/`.
3. Inspect local `shared/results/summary.json` for total, settled, rejected, review-required, and error counts.
4. From `agent-2-code/outputs/`, run the pipeline a second time with the same local command and verify the prior local `shared/` output archives to `archive/shared-001` before fresh output is created.
5. Verify the current local `shared/results/summary.json` is fresh after the second run and that local `shared/` is preserved as current last-run evidence when the run package is committed.
6. Run available focused tests from `agent-2-code/outputs/`, normally `python -m pytest --basetemp .test-tmp` or a narrower equivalent if the generated code created tests.
7. Clean local tool artifacts after validation, including `.test-tmp/`, `.coverage*`, `.pytest_cache/`, and `__pycache__/`.
8. Run privacy scans for raw account IDs, raw descriptions, credentials, tokens, secrets, and unfiltered metadata dumps.
9. Verify `research-notes.md` has at least two Context7 entries.
10. Verify `agent-2-code/outputs/` contains a complete inventory, lists `shared/` as runtime evidence rather than selectable code, and excludes `archive/` plus tool-output folders from committed evidence and selectable inventory.
11. Verify `mcp.json` and `.codex/config.toml` were not changed for Task 2.
12. Update `homework-6/CHANGELOG.md` before any commit.
13. Review the diff for unrelated changes, generated noise, unresolved template tokens, and scope creep.

Root-level pipeline smoke runs are optional during ordinary generate mode. Run a root-level smoke only when the operator explicitly authorizes it or during canonical-selection validation, because root `shared/` is reviewer-visible last-run evidence and should not be mutated by unselected candidates.

Write or update `agent-2-code/validation-checklist.md` with commands, expected signals, actual results, blockers, and any known limitations.

Write `agent-2-code/handoff.md` when the run pauses or completes. It must include the run ID, selected stack, files created or modified, Context7 notes status, sub-agent use summary, validation status, known risks, and exact next suggested prompt.

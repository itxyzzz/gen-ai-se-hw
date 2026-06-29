# Hera Run Metadata

- Run ID: `20260621-215717-orchestrate-runs-python-full-set`
- Mode: `generate-set`
- Requested stack: `python`
- Start time: `2026-06-21 21:57:17 Europe/Budapest`
- Orchestration tool: Codex Desktop main thread invoking Hera (Orchestrator)
- Operator instruction: "Invoke Hera orchestrate-runs to orchestrate creation of the full new set of spec/code/tests/docs in Python."
- Selection authorized: no
- Intended canonical-output policy: preserve candidate package-set evidence only. Do not update canonical `specification.md`, runtime product files, selected tests, selected documentation, stable screenshots, `docs/agent-runs/final-selection.md`, or `docs/agent-runs/selection-sets.json` unless the operator later gives an explicit `select-set` instruction naming this set or child runs.
- Current canonical package-set ID: `python-canonical-20260621`
- Source package-set ID: `python-canonical-20260621` as protected comparison/source context only
- Requested package-set ID: not assigned yet; candidate set will be proposed after all child inventories exist

## Agent Configuration

- `.codex/config.toml` declares `agents.max_threads = 8`.
- `.codex/config.toml` declares `agents.max_depth = 2`.
- First-level child-agent dispatch availability: available through `multi_agent_v1.spawn_agent`.
- Expected dispatch mechanism: first-level child agent for Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator).
- Nested child-agent support inside child runs: configured but not assumed. Each child must record whether its own nested execution was available or degraded.

## Child-Agent Plan

1. Dispatch Athena (Spec Writer) in `generate` mode for a fresh Python transaction-system specification.
2. Wait for Athena run folder, validation checklist, inventory/output package, and handoff before constructing Hephaestus input.
3. Dispatch Hephaestus (Code Generator) in `generate` mode against the named Athena candidate output.
4. Wait for Hephaestus inventory, Context7 notes, validation checklist, and handoff before constructing Themis input.
5. Dispatch Themis (Test Generator) in `generate` mode against the named Hephaestus candidate package.
6. Wait for Themis inventory, evidence, validation checklist, and handoff before constructing Clio input.
7. Dispatch Clio (Documentation Generator) in `generate` mode against the named Athena, Hephaestus, and Themis candidate package.
8. Preserve a Hera selection plan only. Do not copy canonical outputs.

## Pre-Existing Dirty State

- `git status --short` emitted a warning: `could not open directory '.pytest_cache/': Permission denied`.
- No short-status file entries were returned with that warning during setup.
- `HOMEWORK_STANDARDS.md` was not present under the active homework root and the parent repository root is outside this sandbox's writable/readable root for this thread.

## Privacy And Layer Safety

- Hera run records should contain run IDs, paths, fingerprints, counts, command status, blockers, and safe reason-code groups only.
- Hera must not include raw account IDs, raw transaction descriptions, credentials, hidden prompts, or full payload dumps.
- Hera must not generate transaction-system source, tests, docs, screenshots, MCP code, or child deliverables directly in this parent thread during `generate-set`.

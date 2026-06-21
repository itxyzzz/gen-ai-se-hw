# Orchestrate Runs Workflow

This is the canonical workflow for Homework 6 Hera (Orchestrator) entrypoints:

- Codex Markdown skill: `homework-6/.agents/skills/orchestrate-runs/SKILL.md`
- Claude Code project skill, exposed as `/orchestrate-runs`: `homework-6/.claude/skills/orchestrate-runs/SKILL.md`
- Claude Code legacy command wrapper: `homework-6/.claude/commands/orchestrate-runs.md`

The entrypoints must stay thin. Update this package first when the workflow changes, and do not keep independent fallback orchestration logic in any wrapper.

All paths are repository-root relative unless the active project root is already `homework-6`. When running from a homework-root project, remove the leading `homework-6/` prefix from homework-local paths.

## Required Context

Read this context before planning, resuming, comparing, or selecting package sets:

1. `homework-6/agents.md`: layer glossary, privacy rules, run preservation, and Homework Automation Layer responsibilities.
2. `homework-6/docs/agent-runs/selection-sets.json`: machine-readable package-set registry and canonical set ID.
3. `homework-6/docs/agent-runs/final-selection.md`: human-readable selection history and rationale.
4. Child-agent inventories named by the selected package set or explicit operator request.
5. The relevant child-agent control package for each child operation:
   - `homework-6/agent-control/write-spec/`
   - `homework-6/agent-control/generate-code/`
   - `homework-6/agent-control/generate-tests/`
   - `homework-6/agent-control/generate-docs/`
6. `homework-6/agent-control/operate-pipeline/commands-and-hooks.md` when comparing runnable evidence or validating selected package-set command hints.
7. `homework-6/.codex/config.toml`: configured `agents.max_threads = 8` and `agents.max_depth = 2`.
8. This package's `quality-bar.md` and `run-registry.md`.

If selection metadata, a named inventory, or a required child-agent package is missing, stop and ask the operator to select or repair the relevant package. Do not infer a target from unqualified tree contents. Silent "latest" targeting is rejected.

Repository `dev-doc-harness` and Superpowers requirements apply to Operator Layer maintenance of this package. They do not apply inside Hera (Orchestrator) runs or child Homework Automation Layer generation runs unless the operator is maintaining repository control surfaces.

## Operating Modes

- `generate-set`: create a preserved stack-specific end-to-end set through Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator).
- `resume-set`: continue a bounded Hera run from preserved Hera metadata, child-run records, validation notes, and handoff evidence.
- `compare-set`: compare preserved package sets or individual child runs without changing canonical root files.
- `select-set`: perform explicit inventory-driven selection updates. Python replacement is prohibited unless the operator explicitly requests replacing the canonical Python set.

Default to `compare-set` only when the operator asks for comparison. Otherwise require an explicit mode so Hera does not accidentally start generation or selection.

Phase 02 control-surface implementation creates this package only. It does not run Hera, spawn child generation agents, generate Java outputs, select Java, or replace the current Python canonical package.

## Run Setup

Use run IDs in this format:

```text
YYYYMMDD-HHMMSS-orchestrate-runs-<stack>-short-label
```

Examples:

```text
20260621-140000-orchestrate-runs-java-alternate
20260621-153000-orchestrate-runs-python-comparison
```

Create the Hera run folder before dispatching child agents or writing comparison records:

```text
homework-6/docs/agent-runs/HERA_RUN_ID/
  run-metadata.md
  inputs/
    source-context.md
    selected-python-set.snapshot.md
    requested-stack-profile.snapshot.md
  agent-5-orchestrator/
    child-runs.md
    comparisons/
    selection-plan.md
    validation-checklist.md
    handoff.md
```

`run-metadata.md` must record:

- Run ID, mode, selected or requested stack, start time, orchestration tool, and operator instruction.
- Source package-set ID when one exists.
- Canonical package-set ID from `selection-sets.json`.
- Child-agent plan and whether nested sub-agent support is expected or degraded.
- Current `.codex/config.toml` agent settings when available.
- Intended canonical-output policy, including whether selection is prohibited, proposed, or explicitly authorized.
- Pre-existing dirty git state relevant to orchestration.

`inputs/source-context.md` must list the exact source artifacts read, including selection records, inventories, child-agent packages, assignment files, agent guide, MCP config, and operation helper guidance.

`inputs/selected-python-set.snapshot.md` is required when the current Python canonical set is being compared, used as source context, or protected from replacement.

`inputs/requested-stack-profile.snapshot.md` is required when the requested set is stack-specific, especially for Java alternate generation. It should quote or summarize only control-surface stack guidance and must not include raw sample transaction payloads.

## Child Invocation Rules

### First-Level Child-Agent Dispatch Contract

For `generate-set`, Hera must dispatch Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator) as first-level child agents when the runtime exposes first-level child-agent dispatch. The Hera parent thread owns setup, sequencing, ledger updates, child prompt construction, integration of child handoffs, comparison, and selection planning; it must not directly produce child deliverables such as stack-specific specifications, generated runtime source packages, test suites, validation inventories, reviewer documentation, or screenshots.

Before each child stage, the parent Hera thread must update `agent-5-orchestrator/child-runs.md` with a pending entry that names the intended dispatch mechanism. After the child returns, the parent updates that same entry with the observed mechanism, child run ID, validation status, blockers, and next action.

If first-level child-agent dispatch is unavailable, fails, or cannot be confirmed during a `generate-set` whose purpose is to test Hera orchestration behavior, Hera must stop the run as blocked and record the limitation in `run-metadata.md`, `child-runs.md`, `validation-checklist.md`, and `handoff.md`. Hera must not silently switch to sequential child deliverable generation in the parent thread. An operator may later start a separately authorized non-Hera manual recovery sequence, but that is not a successful Hera `generate-set` orchestration run.

Evidence is rejected if it shows later child work was performed in the main orchestration thread because the outputs were tightly coupled or because context was running low. Tight coupling is a sequencing concern for Hera to manage through child prompts and handoff integration, not permission for the parent thread to generate child deliverables.

Every child prompt, child handoff, or child run metadata record must include:

- Parent Hera run ID.
- Requested mode and selected or requested stack.
- Source or selected run IDs.
- Inventory paths.
- Selection record paths.
- Source and current spec fingerprints when applicable.
- Package-set ID when one exists.
- The relevant child-agent control package path.
- Explicit statement that silent "latest" targeting is not allowed.
- Expected output path under the child run folder.
- Privacy and layer-boundary reminders from `quality-bar.md`.

Child agents remain responsible for their bounded quality bars:

- Athena (Spec Writer) owns stack-specific transaction-system specs.
- Hephaestus (Code Generator) owns generated runtime code packages and Context7 code-generation notes.
- Themis (Test Generator) owns selected-code test packages and validation evidence.
- Clio (Documentation Generator) owns reviewer-facing documentation packages and screenshot/evidence mapping.

Hera owns sequencing, run preservation, comparison, and explicit selection workflow. Hera does not replace child validation or child inventories.

## Mode Details

### generate-set

Use `generate-set` to produce a preserved stack-specific set. The normal sequence is:

1. Confirm the requested stack is `python` or `java`.
2. Snapshot the current canonical Python package set when it must remain protected.
3. Dispatch Athena (Spec Writer) as a first-level child agent for a stack-specific specification when the requested stack needs a new spec.
4. Wait for Athena's run folder, inventory, validation status, and handoff before constructing the Hephaestus prompt.
5. Dispatch Hephaestus (Code Generator) as a first-level child agent against the named Athena output.
6. Wait for Hephaestus's run folder, inventory, validation status, and handoff before constructing the Themis prompt.
7. Dispatch Themis (Test Generator) as a first-level child agent against the named selected or candidate Hephaestus package.
8. Wait for Themis's run folder, inventory, validation status, and handoff before constructing the Clio prompt.
9. Dispatch Clio (Documentation Generator) as a first-level child agent against the named Athena, Hephaestus, and Themis records.
10. Wait for Clio's run folder, inventory, validation status, and handoff before marking the package set preserved.
11. Record every child run and observed dispatch mechanism in `agent-5-orchestrator/child-runs.md`.
12. Stop with preserved evidence and a handoff unless the operator explicitly authorized selection.

For Java alternate generation, child prompts should carry the Phase 01 Java expectations: Maven, `pom.xml`, `src/main/java/...`, `src/test/java/...`, `BigDecimal`, Jackson or equivalent JSON handling, JUnit Jupiter, JaCoCo, and the stack-neutral `shared/results/` result contract. JUnit guidance should refer to Maven Surefire or Failsafe, and JaCoCo guidance should require a build-blocking `check` goal with a covered-ratio threshold such as `0.80`.

### resume-set

Use `resume-set` only with a named Hera run ID. Before resuming:

1. Read `run-metadata.md`, `agent-5-orchestrator/child-runs.md`, `validation-checklist.md`, and `handoff.md`.
2. Verify child run folders and inventories still exist.
3. Confirm the resume target is a bounded next action, not an implicit request to select or replace canonical files.
4. Record the resume action in `child-runs.md` or `handoff.md`.

If a child run is blocked, resume that child through its own workflow package and record the resulting status in Hera's ledger.

### compare-set

Use `compare-set` to compare preserved package sets or child runs. It may read selected package-set records, child inventories, validation evidence, Context7 notes, and final documentation evidence. It must not change canonical root files.

Comparison records belong under:

```text
agent-5-orchestrator/comparisons/
```

Comparison criteria include assignment completeness, stack specificity, child-run provenance and inventories, validation commands and outcomes, Context7 evidence where required, privacy and audit safety, result-contract compatibility, documentation and screenshot evidence, known blockers, limitations, and selection risk.

### select-set

Use `select-set` only after explicit operator selection. A `selection-plan.md` proposal is not authorization.

Before selection:

1. Confirm the operator instruction names the package set or child run to select.
2. Confirm whether the operator is replacing Python canonical output or only registering a preserved alternate.
3. Read every selected child inventory and verify canonical copy targets.
4. Confirm `selection-sets.json` stays valid and that `canonical_set_id` changes only when explicitly authorized.
5. Update `final-selection.md` as the human-readable audit record.
6. Update `selection-sets.json` only with the selected or registered package-set state.
7. Review the diff for accidental runtime product edits outside inventory-declared targets.

If the operator has not explicitly requested replacing Python, keep `python-canonical-20260621` canonical and record Java as `alternate` or `candidate` in a later phase when such a set exists.

## Nested-Agent Fallback

Homework 6 is configured with:

```toml
[agents]
max_threads = 8
max_depth = 2
```

Runtime support may still be unavailable or constrained. If Hera cannot spawn nested child agents:

1. Record the observed limitation in `run-metadata.md`.
2. Record the limitation and its impact in `agent-5-orchestrator/handoff.md`.
3. Dispatch Athena, Hephaestus, Themis, and Clio as first-level child agents when first-level dispatch is available.
4. Instruct each child agent to continue without its own nested sub-agents only when its quality bar can still be met locally.
5. Require each child to record degraded nested-agent unavailability in its own run metadata or handoff.
6. Do not mark the set complete until child validations, inventories, and handoffs exist or blockers are explicitly recorded.

Fallback does not waive Context7 notes, run inventories, privacy scans, validation evidence, selection records, or handoff requirements.

If first-level child-agent dispatch itself is unavailable during `generate-set`, record the run as blocked instead of performing Athena, Hephaestus, Themis, or Clio deliverable work in the parent Hera thread.

## Validation And Handoff

Before reporting a Hera run complete or a control-surface implementation ready:

1. Verify required Hera package files and wrappers exist.
2. Verify `selection-sets.json` is valid JSON and keeps the intended canonical set.
3. Verify `.codex/config.toml` parses and contains `max_threads = 8` and `max_depth = 2`.
4. Verify child invocation rules reject silent "latest" targeting.
5. Verify Hera is present only in Operator Layer and Homework Automation Layer control surfaces.
6. Verify no raw sample account IDs, raw descriptions, credentials, or hidden prompts were added to Hera control docs.
7. Verify generated product files, selected tests, selected reviewer docs, screenshots, and `mcp/server.py` are unchanged unless a later explicit selection or repair authorizes them.

Write or update `agent-5-orchestrator/validation-checklist.md` with commands, expected signals, actual results, blockers, and known limitations.

Write `agent-5-orchestrator/handoff.md` when a Hera run pauses or completes. It must include the Hera run ID, mode, stack, child run statuses, comparison or selection status, nested-agent behavior, validation status, known risks, and exact next suggested prompt.

For `generate-set`, the handoff must explicitly state for each of Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator) whether the agent ran as a first-level child agent, ran with degraded child-local execution because its own nested sub-agents were unavailable, or was blocked. If Hera did not consult official OpenAI/Codex documentation about runtime dispatch semantics, the handoff must state that no runtime-cause attribution is being made and the repair or disposition is based only on local run evidence.

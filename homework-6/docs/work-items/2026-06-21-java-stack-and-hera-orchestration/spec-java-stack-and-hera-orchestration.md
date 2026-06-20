# Java Stack and Hera Orchestration Large or Phased Work Spec

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Short ID: `java-stack-and-hera-orchestration`
Status: Approved
Harness release: Installed global `dev-doc-harness` loaded on 2026-06-21
Schema: `schema:spec.large-phased`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:lifecycle.large-anchor-spec`, `rule:quality.spec-handoff`, `rule:models.strategy-required`, `rule:freeze.multi-gate-flow`

## Goal

Prepare Homework 6 for a complete Java alternate generation set and add Hera (Orchestrator) as the Homework Automation Layer agent that owns sequence, comparison, and final selection across Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator).

This needs phase planning because the work crosses every Homework 6 control surface: stack profiles, generation workflows, run registries, selection records, command and hook helpers, MCP support, Codex/Claude skill wrappers, local agent config, and future Java run execution. The Java package must be preserved as a parallel alternative by default, not replace the selected Python submission without a later explicit operator selection.

## Planning Handoff Quality Bar

This spec is the central handoff from the initial planning discussion to later phase plans. Phase plans must derive from this file and preserve these decisions:

- Java support is tightened before Java generation starts.
- Existing Python selected output remains the canonical submission unless explicitly replaced later.
- The Java generation set is preserved as a parallel alternative for comparison.
- Hera owns Homework Automation Layer orchestration and selection behavior, not runtime transaction processing.
- Helper pieces such as `mcp/server.py`, hooks, and operation commands should be universal where practical and recreated only when a single universal surface cannot satisfy both stacks.
- Local `.codex/config.toml` must be extended with `[agents] max_depth = 2` before Hera attempts to create the other four automation agents as sub-agents.
- If `max_depth = 2` does not work as expected, Hera must still use first-level sub-agents for Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator). Child agents may record nested-sub-agent unavailability and continue without their own sub-agents because previous runs showed each child scope is small enough to complete with focused local orchestration.

If later planning discovers missing context before this spec is frozen, update this draft directly. If missing context is discovered after freeze, create a plan amendment.

## Scope

Included:

- Tighten Java stack support across the existing Athena, Hephaestus, Themis, Clio, and Operator Layer control packages.
- Introduce a Hera (Orchestrator) control package, Codex skill, Claude skill or wrapper surfaces, run preservation layout, quality bar, and run registry.
- Update the Homework 6 standing guide in `agents.md` to include Hera in the Homework Automation Layer and clarify that Hera is not a runtime pipeline component.
- Update `docs/agent-runs/final-selection.md` or a linked selection record format so it can represent parallel selected package sets, including the current Python set and future Java alternate set.
- Plan stack-aware run IDs, inventories, canonical target declarations, validation commands, cleanup exclusions, and evidence handling for Java runs.
- Update universal helper guidance for `/run-pipeline`, `/validate-transactions`, coverage hooks, and MCP status behavior so helpers select stack-specific runtime commands from selected-run metadata instead of assuming Python.
- Keep `mcp/server.py` as the preferred single Python FastMCP status server that reads stack-neutral JSON result files from `shared/results/`.
- Update `.codex/config.toml` during implementation to include `agents.max_depth = 2` while preserving existing `agents.max_threads = 8`.
- Generate and preserve the Java alternate set after planning and implementation are approved: Java Athena spec, Java Hephaestus code, Java Themis tests, and Java Clio docs/evidence.
- Compare the preserved Java alternate against the current selected Python package without replacing canonical Python output unless the operator later requests that selection.

Expected repository areas:

- `agents.md`
- `agent-control/write-spec/`
- `agent-control/generate-code/`
- `agent-control/generate-tests/`
- `agent-control/generate-docs/`
- `agent-control/operate-pipeline/`
- New `agent-control/orchestrate-runs/` or equivalent Hera package
- `.agents/skills/`
- `.claude/skills/` and `.claude/commands/` where matching legacy wrappers are needed
- `.codex/config.toml`
- `.claude/settings.json`
- `.githooks/pre-push`
- `scripts/check_coverage_gate.py`
- `mcp.json`
- `mcp/server.py`
- `docs/agent-runs/`
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/`
- `CHANGELOG.md` before any planning freeze commit or implementation commit

## Non-Scope

- Do not replace the current selected Python package as the canonical submission during this work item by default.
- Do not rewrite the frozen Homework 6 assignment file `TASKS.md`.
- Do not make generated transaction-system code depend on `dev-doc-harness`, Superpowers, hidden chat state, or Hera.
- Do not implement Hera as a runtime banking pipeline component, Java class, Python module inside the generated product, MCP tool, or settlement/reporting component.
- Do not fork `mcp/server.py` into a Java MCP server unless later validation proves the universal Python reader cannot support the Java result schema.
- Do not recreate `/run-pipeline`, `/validate-transactions`, coverage hooks, or MCP support as per-run Themis outputs.
- Do not add additional implementation stacks beyond the existing fixed enum values `python` and `java`.
- Do not execute Java generation, Java selection, or canonical copy operations until the relevant phase plans are frozen and a fresh operator instruction authorizes implementation.

## Current State

Homework 6 currently has a selected Python package:

- Selected Athena (Spec Writer): `20260619-170102-write-spec-python-fresh`
- Selected Hephaestus (Code Generator): `20260619-175211-generate-code-python-fresh-spec`
- Selected Themis (Test Generator): `20260620-144025-generate-tests-python-fresh-spec`
- Selected Clio (Documentation Generator): `20260620-230201-generate-docs-python-primary`

The stack enum already includes `python` and `java` in `agent-control/write-spec/stack-profiles.md`. The Java profile names Maven, Jackson or equivalent JSON handling, JUnit 5, JaCoCo, Java package/class paths, and a Python FastMCP server that reads Java-produced result files.

Downstream surfaces still contain Python-shaped defaults and examples:

- Run ID examples and layouts under Hephaestus, Themis, and Clio use `generate-*-python-*`.
- Validation commands often assume `python integrator.py`, `pytest`, Python coverage output, Python imports, `.coverage*`, `.pytest_cache/`, and `__pycache__/`.
- `/run-pipeline` and `/validate-transactions` guidance assumes the current Python CLI and importable validator helper.
- The coverage helper currently shells out to `python -m pytest --cov=. --cov-fail-under=...`.
- `.claude/settings.json` and `.githooks/pre-push` invoke the Python coverage helper.
- `mcp/server.py` is already a Python FastMCP server. It reads selected result files and can likely remain stack-neutral if Java output preserves the selected JSON result contract.
- `.codex/config.toml` currently has `[agents] max_threads = 8` but does not yet declare `max_depth = 2`.

Hera is not currently represented as a first-class Homework Automation Layer agent. Orchestration and selection behavior is distributed across the operator thread and the individual agent workflows.

## Proposed Behavior

After this work completes:

- The five Homework Automation Layer agents are Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), Clio (Documentation Generator), and Hera (Orchestrator).
- Hera can instruct the other four agents to generate, resume, compare, and select within their existing boundaries.
- Hera owns final cross-run comparison and selection operations, including inventory-driven copy decisions and final-selection record updates.
- Hera can preserve and compare multiple stack-specific sets, including the current Python set and a Java alternate set.
- The current Python package remains the canonical selected submission unless an explicit later operator instruction selects the Java package for canonical root replacement.
- Java generation workflows use stack-aware commands, inventories, evidence, cleanup, and validation rather than Python defaults.
- Helper surfaces prefer universal behavior:
  - `mcp/server.py` remains Python and reads `shared/results/summary.json` plus per-transaction result files regardless of whether Python or Java produced them.
  - `/run-pipeline` resolves the selected stack and command from selection metadata, inventory, or explicit invocation.
  - `/validate-transactions` resolves either a Java dry-run CLI or the Python validator helper based on selected stack.
  - Coverage hooks call a stack-aware helper that can run pytest for Python or Maven/JUnit/JaCoCo validation for Java.
- `.codex/config.toml` includes both `agents.max_threads = 8` and `agents.max_depth = 2` so Hera can create the other automation agents as sub-agents when the runtime supports that pattern.
- If nested sub-agent depth fails, Hera still dispatches the four named child agents as first-level sub-agents and requires each child to continue in documented degraded mode rather than stop solely because it cannot spawn its own nested sub-agents.
- Final documentation and PR support can describe Python as the selected canonical package and Java as a preserved alternate when both exist.

## Interfaces And Data

Affected control interfaces:

- Skill invocations:
  - Existing: `write-spec`, `generate-code`, `generate-tests`, `generate-docs`, `run-pipeline`, `validate-transactions`
  - New: Hera orchestration skill, likely `orchestrate-runs`
- Hera modes:
  - `generate-set`: produce a stack-specific end-to-end candidate set through Athena, Hephaestus, Themis, and Clio.
  - `resume-set`: resume a bounded Hera run using preserved state and child-run records.
  - `compare-set`: compare preserved stack sets or individual child runs without canonical root changes.
  - `select-set`: perform explicit selection operations from preserved inventories, with Python replacement prohibited unless the operator explicitly asks for it.
- Stack values remain fixed: `python` and `java`.
- Run IDs must include the stack:
  - `YYYYMMDD-HHMMSS-write-spec-java-short-label`
  - `YYYYMMDD-HHMMSS-generate-code-java-short-label`
  - `YYYYMMDD-HHMMSS-generate-tests-java-short-label`
  - `YYYYMMDD-HHMMSS-generate-docs-java-short-label`
  - `YYYYMMDD-HHMMSS-orchestrate-runs-java-short-label` for Hera run folders
- Hera run folder concept:

```text
docs/agent-runs/HERA_RUN_ID/
  run-metadata.md
  inputs/
    source-context.md
    selected-python-set.snapshot.md
  agent-5-orchestrator/
    child-runs.md
    comparisons/
    selection-plan.md
    validation-checklist.md
    handoff.md
```

Selection metadata must support parallel package sets. A Java alternate may be recorded as preserved and comparable without overwriting root `specification.md`, root `integrator.py`, root `tests/`, or root reviewer docs.

Generated Java candidate outputs should use Java-native paths such as:

- `pom.xml`
- `src/main/java/.../Integrator.java`
- `src/main/java/.../agents/TransactionValidator.java`
- `src/main/java/.../agents/FraudDetector.java`
- `src/main/java/.../agents/SettlementProcessor.java`
- `src/main/java/.../agents/ReportingAgent.java`
- `src/main/java/.../model/PipelineMessage.java`
- `src/test/java/.../*Test.java`

Exact package names remain a phase-plan decision, but they must be stack-specific and stable inside the generated Java spec before Java code generation starts.

## State Flow And Control Flow

The intended high-level flow is:

1. Tighten stack-aware control surfaces.
2. Add Hera control surfaces and config support.
3. Run Hera or the approved manual sequence for `stack=java`.
4. Preserve each child run under `docs/agent-runs/`.
5. Validate the Java alternate from run-local outputs first.
6. Record the Java alternate as parallel evidence.
7. Compare Java and Python packages.
8. Stop for operator decision before any canonical replacement.

Hera orchestration must not let child agents silently target "latest." Each child invocation must name the selected stack, parent Hera run ID when applicable, source run IDs, inventories, and fingerprints.

Hera owns final comparison and selection, but final integration remains orchestrator-owned. Child agents still own their bounded artifact generation and quality bars.

## Safety, Security, Privacy, Compliance, Migration, And Rollback

Privacy and safety:

- Preserve all existing Homework 6 privacy rules: no plaintext account identifiers, raw descriptions, credentials, tokens, or unfiltered metadata in logs, results, docs, screenshots, or evidence.
- Keep the banking pipeline framed as an educational simulation, not legal, banking, AML, sanctions, payment-network, PCI, KYC, or regulatory compliance.
- Java code must use `BigDecimal`, never `double` or `float`, for monetary amounts.
- Java tests and evidence must scan for raw account IDs and raw descriptions in generated outputs.

Migration and rollback:

- Because Java is a parallel alternate by default, rollback from Java generation is preservation-only: do not copy Java outputs to canonical root paths unless explicitly selected.
- If a helper is made universal and breaks Python, rollback by reverting the helper change while preserving Java candidate run folders as evidence.
- Planning and implementation commits must stage only scoped files. Do not revert or remove unrelated current Python selected artifacts.

Config safety:

- `.codex/config.toml` must add `agents.max_depth = 2` under the existing `[agents]` table without removing `max_threads = 8` or MCP server configuration.
- If the runtime ignores `max_depth`, Hera run metadata must record that limitation, still dispatch the four primary child agents as first-level sub-agents, and instruct those child agents to continue without nested sub-agents while recording the limitation in their handoffs.

Helper strategy:

- Prefer universal helpers that dispatch on stack metadata.
- Keep `mcp/server.py` Python unless a concrete Java-only blocker appears.
- The MCP reader must treat result JSON as a stack-neutral product contract. If Java result files need small schema alignment, prefer changing the Java generated spec/code to emit the existing result shape rather than forking MCP.
- Hooks should call a universal coverage gate wrapper. The wrapper can choose Python pytest or Java Maven/JaCoCo based on explicit arguments or selected metadata.

## Validation Strategy

Planning validation before freeze:

- Verify the planning artifacts contain no placeholder markers or unresolved required decisions.
- Verify the spec preserves Java-as-parallel-alternate, universal-helper preference, and `agents.max_depth = 2`.
- Verify phase decomposition covers stack tightening, Hera, Java generation, and comparison.
- Verify documentation matrix marks all required artifacts as required, not applicable, or deferred with reason.

Implementation validation expected in later phase plans:

- Static scans confirm run ID examples and validation commands are stack-aware.
- Static scans confirm Hera does not appear as a generated runtime transaction pipeline component.
- TOML validation confirms `.codex/config.toml` has `[agents] max_threads = 8` and `max_depth = 2`.
- JSON validation confirms `mcp.json` remains valid.
- Existing Python checks still pass after helper changes:
  - `python integrator.py`
  - `python -m pytest -p no:cacheprovider`
  - `python scripts/check_coverage_gate.py --fail-under 80`
- Java candidate checks pass from run-local outputs when Java generation is authorized:
  - `mvn test`
  - JaCoCo coverage gate at or above 80 percent
  - Java pipeline command, such as `mvn exec:java` or a packaged `java -jar` command chosen by the generated Java spec
  - Repeated-run archival evidence
  - MCP status helper reads Java-produced `shared/results/`
- Final comparison notes identify whether Java is assignment-complete, where it differs from Python, and whether it is safe to keep as parallel alternate.

## Triage, Debugging, And Operations

Expected failure modes and responses:

- Java spec generation emits Python commands: reject or repair the Java Athena run before Hephaestus uses it.
- Java Hephaestus output produces result JSON that MCP cannot read: first repair Java result shape to the existing stack-neutral contract; fork MCP only if the contract cannot reasonably stay universal.
- Universal coverage helper breaks Python: repair the helper before continuing Java generation.
- Maven or JaCoCo is unavailable locally: record the blocker in Java run metadata and ask the operator whether to install tooling, use an available wrapper, or preserve the run as blocked.
- Hera cannot spawn nested agents despite `max_depth = 2`: record runtime limitation, preserve first-level child-agent dispatch, and have each child continue without nested sub-agents while recording the degraded mode in its run metadata and handoff.
- Canonical-copy drift risk appears: stop before copying and require explicit operator selection.

Diagnostics should be preserved in run-local evidence and handoff files. Final answers should summarize safe counts, commands, paths, and run IDs without exposing sensitive sample data.

## Assumptions

- The current Python package remains accepted as the canonical submission baseline.
- Java is a parallel alternate until a later explicit operator selection says otherwise.
- Maven is the default Java build tool unless an approved phase amendment selects another build plan.
- JUnit 5 and JaCoCo are the default Java testing and coverage tools.
- The Python FastMCP `mcp/server.py` can remain universal if both stacks emit the selected JSON result contract.
- Existing Homework 6 skills stay thin wrappers around `agent-control/` packages.
- The current local branch `homework-6-extension` is the correct branch for this extension planning work.
- The active model and exact reasoning effort may not be externally selectable in this Codex Desktop thread; phase plans should use policy-relative model guidance and record observed limitations.

## Risks

- Python assumptions are scattered enough that a partial tightening could still produce a Java run with Python commands or pytest-only validation.
- Universal helper refactors may accidentally break the already-selected Python package.
- Hera could blur operator-layer selection duties with generated runtime behavior unless layer boundaries are explicit in every control surface.
- Nested sub-agent support may not behave exactly as expected even after `max_depth = 2` is added.
- Java build and coverage tooling may require dependency downloads or environment setup not needed by the Python package.
- Parallel Java outputs could be confused with canonical root outputs unless selection metadata and docs clearly distinguish selected canonical vs preserved alternate.
- Updating final-selection semantics is high-blast-radius because Themis and Clio currently consume it for traceability.

## Known Unknowns

- Whether the local Java/Maven toolchain is already installed and usable in the execution environment.
- Whether Codex Desktop currently honors `[agents] max_depth = 2` for nested Hera-created sub-agents; if not, the accepted fallback is first-level child agents plus child-local degraded mode.
- The exact Java package namespace to use for generated code.
- Whether the current `scripts/check_coverage_gate.py` should become a universal Python wrapper for both stacks or whether a new script name should be added while preserving the old script as a compatibility shim.
- Whether final-selection should stay in one file with stack-specific sections or split into a set registry plus per-stack selection files.
- Whether a Java alternate should copy anything to root canonical paths during selection or stay entirely under preserved run folders until a later replacement decision.

Each known unknown has a planned owner in the phase decomposition below.

## Rejected Alternatives

- Generate Java immediately before tightening controls: rejected because downstream workflows, hooks, commands, and examples still contain Python assumptions.
- Build Hera first before Java stack readiness: rejected because Hera would orchestrate underspecified or Python-biased child workflows.
- Replace Python canonical output with Java automatically if Java passes: rejected because the operator explicitly wants Java preserved as a parallel alternative.
- Create a separate Java MCP server by default: rejected because the existing Python FastMCP server should remain universal if Java writes the same JSON result contract.
- Let Themis recreate hooks and commands for Java: rejected because command and hook surfaces are Operator Layer helpers maintained once and validated by Themis.
- Add more stack enum values while working on Java: rejected to keep the bounded Homework 6 contract stable.

## Acceptance Criteria

- A frozen planning package exists under `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/`.
- The plan records Java as a parallel alternate and prohibits automatic replacement of the selected Python package.
- Stack-aware tightening covers Athena, Hephaestus, Themis, Clio, operation helpers, hooks, MCP, and final-selection traceability.
- Hera is documented as a Homework Automation Layer orchestrator that owns sequence, comparison, and selection, not as a Generated Transaction System Layer runtime component.
- `.codex/config.toml` update to `agents.max_depth = 2` is included in the approved implementation plan, together with the first-level child-agent fallback when nested depth is unavailable.
- Universal-helper preference is preserved, including keeping `mcp/server.py` as a Python stack-neutral result reader unless proven impossible.
- Existing Python verification still passes after helper/control-surface implementation.
- Java alternate generation, when later authorized, produces preserved Athena, Hephaestus, Themis, and Clio run folders with stack-specific inventories, validation evidence, and comparison notes.
- Final comparison can identify the current Python set and preserved Java alternate without ambiguous "latest" targeting.
- No implementation phase starts until the relevant planning freeze gate is complete and the operator gives a fresh explicit start instruction.

## Phase Decomposition

| Phase | Objective | Output |
|---|---|---|
| 01 | Tighten Java stack readiness across existing four-agent and helper surfaces. | `plan-phase-01-java-stack-readiness-java-stack-and-hera-orchestration.md` |
| 02 | Add Hera (Orchestrator) control surfaces, selection ownership, `agents.max_depth = 2` config support, and first-level child-agent fallback behavior. | `plan-phase-02-hera-orchestrator-java-stack-and-hera-orchestration.md` |
| 03 | Run and preserve the Java alternate generation set through Athena, Hephaestus, Themis, and Clio. | `plan-phase-03-java-alternate-generation-java-stack-and-hera-orchestration.md` |
| 04 | Compare Python canonical and Java alternate sets, update handoff records, and stop for operator selection decision. | `plan-phase-04-cross-stack-comparison-java-stack-and-hera-orchestration.md` |

Phase 01 owns the known unknowns around exact helper design and final-selection metadata shape. Phase 02 owns Hera naming, mode definitions, and nested-agent runtime behavior. Phase 03 owns Java package namespace and toolchain validation. Phase 04 owns final comparison reporting and any recommendation about whether Java should remain alternate or be selected later.

## Planning Artifact Freeze Gates

Use `module:freeze-gate`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, and `rule:freeze.multi-gate-flow`.

Required gates:

- Anchor spec draft review: this spec is reviewed before phase plans are written.
- Anchor spec freeze: after operator approval, update `CHANGELOG.md`, stage only this spec and the changelog entry, commit the approved artifact set, and stop.
- Phase-plan freeze: each phase plan, or a bundled set of phase plans approved together, gets its own freeze gate before implementation.
- Amendment freeze: high-impact changes after freeze use plan amendments.

No implementation, Java generation, config update, or canonical-copy operation begins in the same turn as a freeze gate.

## Model And Sub-Agent Strategy

Current orchestration: Codex Desktop coding agent in local mode; exact external model label and reasoning-effort selector not exposed in this thread.
Fit assessment: High complexity and high blast radius because this changes automation lifecycle, selection ownership, cross-stack validation, helper behavior, and future generated package boundaries.
Recommended change: Use the active `enterprise-default` policy. Use latest strongest available model class with high reasoning for phase planning, Hera design, final-selection semantics, and final reviews. Use smaller/faster current model class only for bounded inventory or static scan support.

| Phase | Purpose | Context strategy | Input context | Output artifact | Model policy | Model class/profile | Reasoning effort | Reason | Parallel? | Blast radius if wrong |
|---|---|---|---|---|---|---|---|---|---|---|
| 01 | Inventory Python assumptions and propose stack-aware helper/control-surface updates. | curated artifacts | Existing `agent-control/`, skill wrappers, helper scripts, MCP config, final-selection record, this spec | Discovery notes or phase review memo | `enterprise-default` | smaller/faster for inventory; latest strongest for recommendations | medium for inventory; high for recommendation review | Broad search is bounded, but recommendations affect all downstream runs | Yes, read-only inventory can run with other Phase 01 reviews | Medium: missed assumptions can break Java generation |
| 01 | Review universal MCP, command, hook, and coverage-helper strategy. | curated artifacts | `mcp/server.py`, `scripts/check_coverage_gate.py`, `agent-control/operate-pipeline/`, selected result schemas | Helper strategy review | `enterprise-default` | latest strongest | high | Helper behavior protects both Python and Java verification | Yes | High: wrong helper design can break selected Python evidence or Java validation |
| 02 | Design Hera boundaries, modes, run layout, and selection ownership. | curated artifacts | This spec, existing run registries, final-selection record, `agents.md`, child agent workflows | Hera design review | `enterprise-default` | latest strongest | high | Hera affects orchestration and selection authority | No, integrate centrally | High: unclear ownership can corrupt selection records |
| 03 | Review Java generated package readiness after candidate runs. | curated artifacts | Java candidate outputs, inventories, validation logs, Context7 notes, result files | Java readiness review | `enterprise-default` | latest strongest | high | Determines whether Java alternate is assignment-complete | Yes, can run alongside documentation/evidence review | High: false pass could preserve a broken alternate |
| 04 | Cross-stack comparison and final selection recommendation. | curated artifacts | Python selected records, Java alternate records, validation evidence, docs | Comparison report | `enterprise-default` | latest strongest | high | Final comparison informs operator decision but should not auto-replace canonical files | No | High: ambiguous comparison may cause wrong selection |

Sub-agents are planned as read-only explorers or reviewers unless a frozen phase plan explicitly authorizes write-capable worker agents. More than three concurrent sub-agents requires fresh operator confirmation even though `.codex/config.toml` will allow nested depth for Hera. Hera's four primary child agents are an approved first-level orchestration pattern for the Java alternate generation phase; if nested depth fails, those child agents continue without their own nested sub-agents and record the limitation.

## Documentation Artifact Matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Before each freeze or implementation commit | `CHANGELOG.md` | Newest-first Homework 6 step entry |
| Test cases | Snapshot | Yes | Before implementation phase | `snapshots/test-cases.snapshot.md` | Required before helper/config implementation to capture Python and Java expected behaviors |
| Testing guide delta | Living delta | Yes | During or after implementation | `deltas/testing-guide.delta.md` | Required because coverage and validation commands become stack-aware |
| Operator manual delta | Living delta | Yes | After implementation | `deltas/operator-manual.delta.md` | Required because Hera and parallel Java package operation affect reviewer/operator flow |
| API reference delta | Living delta | No | Not applicable | Not applicable | No public runtime API contract is planned; MCP behavior should remain compatible |
| Architecture snapshot | Snapshot | Yes | Before implementation | `snapshots/architecture.snapshot.md` | Required to capture Automation Layer and selection ownership changes |
| Architecture summary delta | Living delta | Yes | After implementation | `deltas/architecture-summary.delta.md` | Required because Hera changes the Homework Automation Layer architecture |

## Approval

- Status: Approved
- Superseded by: Not superseded

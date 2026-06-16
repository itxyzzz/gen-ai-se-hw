# Homework 6 Spec Agent Large or Phased Work Spec

Work ID: `2026-06-16-homework-6-spec-agent`
Short ID: `homework-6-spec-agent`
Status: Draft
Harness release: Installed global `dev-doc-harness`; no release field is published in `SKILL.md`
Schema: `schema:spec.large-phased`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:lifecycle.large-anchor-spec`, `rule:quality.spec-handoff`, `rule:models.strategy-required`, `rule:freeze.multi-gate-flow`

## Goal

Create the durable plan for Homework 6 Task 1: a portable Agent 1 specification writer that is callable both as a Claude Code slash command and as a Codex Markdown skill, can produce a full high-quality transaction-pipeline specification with documented research, and can preserve, compare, and select multiple generation runs before the final submission package is chosen.

This needs phased planning because Agent 1 becomes the context source for the rest of Homework 6. A weak or single-pass spec would cascade into lower-quality code, tests, MCP evidence, and documentation. The plan must also handle project-root MCP discovery, dual command surfaces, and repeated generation attempts without losing evidence.

## Planning handoff quality bar

This spec is the central handoff for later phase plans. Phase plans must preserve the decisions below rather than replacing them with a generic "write a spec" task. If a later planner finds missing context before freeze, update this draft directly. If the package is frozen and a high-impact change is needed, create a harness plan amendment.

The Agent 1 deliverables must be useful without relying on `dev-doc-harness`, Superpowers, or this planning conversation. When those tools are available, the agent should cooperate with them. When they are absent, the agent must still enforce its own context loading, research, run registry, self-review, and handoff rules.

## Scope

Included in this work item:

- Homework 6 Task 1 planning for the Agent 1 specification writer.
- A dual-surface `write-spec` design:
  - Claude Code slash command at `homework-6/.claude/commands/write-spec.md`.
  - Codex Markdown skill at `homework-6/.agents/skills/write-spec/SKILL.md`.
- A first-class stack selection input for `write-spec`, with a fixed enum instead of an open-ended or automatic chooser.
- A Homework 6 `agents.md` contract that tells future agents how to load context, use run artifacts, avoid unsafe assumptions, and work with or without harness support.
- A run preservation and comparison strategy under `homework-6/docs/agent-runs/`.
- A Context7 and MCP strategy that distinguishes global tool availability, portable homework config, and project-root discovery behavior.
- A research strategy for Agent 1 that can build a Homework 6 equivalent of the Homework 3 domain-rules package from current sources, while documenting fallbacks when web or MCP research is unavailable.
- Phase decomposition for creating the control surfaces and then using them to generate the canonical `specification.md` package.

## Non-scope

Excluded from this planning package:

- Creating the actual `write-spec` command or Codex skill before the planning package is approved.
- Generating or selecting the final Homework 6 `specification.md` in this phase.
- Implementing transaction-processing code, test agents, hooks, screenshots, custom FastMCP server code, or final Homework 6 README documents.
- Making the generated banking rules legally authoritative. The spec writer may research banking, payments, ISO 4217, and audit/logging patterns, but the homework pipeline remains an educational simulation unless a later approved spec states otherwise.
- Depending on hidden prior ChatGPT research from Homework 3. Homework 3 may be used as a format and quality reference, but Homework 6 research must be documented as part of the new run.

## Current state

- Current repository branch is `homework-6-submission`.
- `homework-6/` currently contains only `TASKS.md` and `sample-transactions.json`, and the whole folder is untracked in Git at the start of this planning pass.
- `homework-6/TASKS.md` requires Agent 1 to create `specification.md`, `agents.md`, and a slash command that generates a specification following the template.
- No `homework-6/specification-TEMPLATE-hint.md` file is present in the current working tree. The available reference is the Homework 3 template/example package.
- Homework 3 provides the quality target:
  - `homework-3/specification.md` for detailed objective, constraints, context, verification, and low-level task cards.
  - `homework-3/agents.md` for portable agent context rules.
  - `homework-3/docs/domain-rules.md` and `homework-3/docs/technical-conventions.md` for research-backed domain and technical constraints.
  - `homework-3/docs/process-artifacts/homework-3-pr-description.md` for the lesson that a high-quality spec is unlikely to emerge in one unbounded pass.
- Homework 5 provides the MCP discovery lesson: a portable homework-level `mcp.json` is useful for review, but Codex Desktop only reliably discovers nested homework MCP configs when the selected project root is the homework folder or when a matching project-scoped config exists.
- Current research/tool state in this thread:
  - Context7 MCP tools surfaced after tool discovery as `mcp__context7.resolve_library_id` and `mcp__context7.query_docs`.
  - Context7 resolved `FastMCP` to `/prefecthq/fastmcp` and returned decorator examples for `FastMCP`, `@mcp.tool`, and `@mcp.resource`.
  - `ctx7` and `context7` are not visible on the PowerShell PATH in this shell.
  - `npx` is visible, so `npx -y @upstash/context7-mcp@latest` remains the portable MCP command pattern for homework config.

## Proposed behavior

After this work item is implemented and the write-spec pipeline is run:

- `homework-6/agents.md` gives every later Homework 6 agent a stable, portable operating contract:
  - Context load order.
  - Assignment deliverables and file locations.
  - Research and MCP use expectations.
  - Run preservation and final-selection rules.
  - Privacy and audit constraints for transaction data.
  - Built-in quality gates that work even when harness or Superpowers are unavailable.
- The Codex Markdown skill and Claude Code slash command both generate the same kind of spec package:
  - A full `specification.md` with the five required Task 1 sections.
  - Rich low-level task entries with exact prompts, target files, target functions, details, edge cases, and acceptance criteria.
  - A local research/domain package for the simulated banking pipeline.
  - A generation run folder with inputs, outputs, validation notes, and handoff.
- The `write-spec` surfaces accept a stack input:
  - `stack=python` is the default when omitted.
  - `stack=java` is supported as an optional alternate profile for a second run if time allows.
  - Unsupported stack values make the agent stop and ask for a supported value.
  - `stack=auto` is intentionally not supported; stack choice comes from the fixed enum and the one-off tradeoff analysis captured in the stack-profile reference.
- The generated `specification.md` is stack-specific after the stack input is chosen. Domain objectives can stay broadly portable, but low-level tasks, files, functions, commands, tests, coverage hooks, MCP implementation notes, and prompts must be concrete for the selected stack.
- Multiple write-spec attempts are preserved under `homework-6/docs/agent-runs/` instead of overwriting each other. A later selection step copies the chosen artifacts to canonical homework paths and records why that run was selected.
- Context7 is usable in two ways:
  - In the current repo-root project, use discovered MCP tools when available, or `npx`/web fallback when they are not.
  - In a separate Codex project opened at `homework-6`, use homework-local MCP config so Context7 and the later custom `pipeline-status` server are discoverable.
- The deliverable agents do not require `dev-doc-harness`, but they should detect and respect it when the surrounding repository asks for harness-managed planning.

## Interfaces and data

Planned implementation targets for Phase 01:

| Path | Interface type | Responsibility |
|---|---|---|
| `homework-6/agents.md` | Portable agent contract | Context order, agent roles, research policy, run registry, quality gates, privacy rules, and final-selection rules for Homework 6 agents. |
| `homework-6/.agents/skills/write-spec/SKILL.md` | Codex Markdown skill | Primary Codex-facing workflow for generating, validating, preserving, and selecting spec runs. |
| `homework-6/.agents/skills/write-spec/references/write-spec-quality-bar.md` | Skill reference | Detailed quality bar for the generated specification, low-level task cards, research notes, and handoff artifacts. |
| `homework-6/.agents/skills/write-spec/references/stack-profiles.md` | Skill reference | One-off tradeoff analysis and fixed stack enum for Python and Java generation profiles. |
| `homework-6/.claude/commands/write-spec.md` | Claude Code slash command | Slash-command surface that invokes the same workflow and includes enough fallback instructions to run without Codex skill discovery. |
| `homework-6/docs/agent-runs/README.md` | Run registry guide | Describes run IDs, folder layout, comparison method, and final-selection process. |
| `homework-6/docs/agent-runs/final-selection.md` | Selection ledger | Records the currently selected canonical run, selection criteria, and copy targets. It starts as "no run selected" until Agent 1 is executed. |
| `homework-6/mcp.json` | Portable MCP config | Registers Context7 initially; later Homework 6 phases update the same file to include `pipeline-status` once `mcp/server.py` exists. |
| `homework-6/.codex/config.toml` | Local Codex project config | Makes Context7 discoverable when the Codex project root is exactly `homework-6`; later phases add the custom MCP server after implementation. |
| `homework-6/CHANGELOG.md` | Homework changelog | Required before the Phase 01 implementation commit; newest-first Homework 6 entry. |

Planned run registry layout:

```text
homework-6/docs/agent-runs/
  README.md
  final-selection.md
  <run-id>/
    run-metadata.md
    inputs/
      source-context.md
    agent-1-spec/
      outputs/
        specification.md
        agents.md
        docs/domain-rules.md
        docs/technical-conventions.md
        docs/development-process.md
      research-notes.md
      validation-checklist.md
      handoff.md
    comparison.md
```

Run ID format:

```text
YYYYMMDD-HHMMSS-write-spec-<short-label>
```

Example:

```text
20260616-173000-write-spec-context7-python
```

Canonical selected files for the final homework package:

```text
homework-6/specification.md
homework-6/agents.md
homework-6/docs/domain-rules.md
homework-6/docs/technical-conventions.md
homework-6/docs/development-process.md
```

The canonical files are copied from a selected run and then refined through normal approved changes. The run folder remains the evidence snapshot that explains how those files were produced.

## State flow and control flow

The intended Agent 1 control flow is:

1. Load local context:
   - `homework-6/TASKS.md`
   - `homework-6/sample-transactions.json`
   - `HOMEWORK_STANDARDS.md`
   - root `README.md`
   - Homework 3 reference docs by path when available
   - Current `homework-6/agents.md` if it already exists
2. Establish a new run folder under `homework-6/docs/agent-runs/<run-id>/`.
3. Parse stack input from the operator or command arguments:
   - Use `python` when the stack is omitted.
   - Accept `python` and `java`.
   - Reject any other value with a short message naming the supported enum.
4. Load the selected stack profile from `stack-profiles.md` and record it in run metadata.
5. Inventory required outputs and detect missing template files.
6. Research before writing:
   - Use Context7 for selected technical libraries and frameworks.
   - Use web or other available current sources for domain facts if needed.
   - Record every query, library ID or URL, date accessed, and applied decision.
   - If external research is unavailable, record the limitation and rely only on assignment files and local examples.
7. Produce or refresh domain and technical convention docs for Homework 6.
8. Draft the specification in run output paths, not directly over canonical files.
9. Validate against Task 1 required sections, the selected stack profile, and the Homework 3 quality bar.
10. If the output is too large for one thread, stop with a handoff file that names the next bounded phase and preserves completed context.
11. Compare with previous runs when requested, including cross-stack comparison between Python and Java runs if both exist.
12. Only after an operator selects a run, copy the chosen outputs to canonical homework paths and update `final-selection.md`.

The harness phase flow is separate:

1. Draft planning artifacts under `homework-6/docs/work-items/2026-06-16-homework-6-spec-agent/`.
2. Stage only the draft planning artifacts for review.
3. Wait for operator approval.
4. On approval, update `homework-6/CHANGELOG.md`, stage only approved planning artifacts and changelog, commit, report the hash, and stop.
5. Begin Phase 01 implementation only after a fresh operator instruction after the freeze gate.

## Safety, security, privacy, compliance, migration, and rollback

- Transaction records include account identifiers and descriptions that must be treated as sensitive. Agent instructions must prohibit plaintext PII in logs and generated audit examples unless redacted.
- Monetary handling must use precise decimal types in generated specs and future code prompts; `float` is explicitly disallowed for amounts.
- Currency validation must reference ISO 4217-style codes without pretending the educational sample validates real banking compliance.
- Research-derived banking statements must be framed as design assumptions for a simulated homework pipeline unless backed by cited authoritative sources.
- Context7 and web research notes must avoid credentials, private tokens, personal account details, or unrelated local paths.
- Run preservation must avoid committing bulky transient pipeline directories unless a later approved phase intentionally keeps evidence snapshots.
- Rollback for Phase 01 is file removal of the created docs/config/command surfaces before they are frozen; after freeze, harness amendments are required for high-impact plan changes.

## Validation strategy

Draft planning validation:

- Confirm branch and worktree state with `git status --short --branch`.
- Check that planning artifacts contain no unresolved placeholder markers, using a scan pattern assembled at runtime so the validation command does not create false positives.
- Run `git diff --check -- homework-6/docs/work-items/2026-06-16-homework-6-spec-agent`.
- Stage only the draft planning artifacts for operator review.

Phase 01 implementation validation:

- Validate Markdown skill metadata with `python C:\Users\tanatarov\.codex\skills\.system\skill-creator\scripts\quick_validate.py homework-6\.agents\skills\write-spec`.
- Validate JSON config with `python -m json.tool homework-6\mcp.json`.
- Validate TOML config with Python 3.11+ `tomllib`.
- Check that Claude and Codex command surfaces both reference the same required output paths and run registry rules.
- Check that `agents.md` does not require `dev-doc-harness` or Superpowers to be useful.
- Confirm Context7 via the best available route:
  - Preferred in this thread: `mcp__context7.resolve_library_id`.
  - Portable CLI fallback: `npx -y @upstash/context7-mcp@latest --help`, with network approval when required.

Phase 02 spec-generation validation:

- Validate `specification.md` has all five Task 1 required sections.
- Validate low-level tasks have one entry per meta-agent and include exact prompt, file, function, and details.
- Validate the selected stack is recorded as `python` or `java`; omitted input must be normalized to `python`.
- Validate low-level task cards use stack-specific paths, functions, commands, tests, and coverage tools rather than language-neutral placeholders.
- Validate generated research notes include at least sources or explicit fallback limitations.
- Validate generated domain and technical docs are cited, scoped, and non-legalistic.
- Validate selected canonical files match the chosen run or have documented post-selection edits.

## Triage, debugging, and operations

- If Context7 does not appear in tool discovery, use the portable `npx -y @upstash/context7-mcp@latest` MCP command in the homework config and record the failed discovery route.
- If `ctx7` is unavailable on PATH, do not block the workflow; record that `npx` and MCP discovery are the supported routes.
- If a nested homework-level MCP config is not discovered from the repo-root project, open a separate Codex project rooted at `homework-6` and use `homework-6/.codex/config.toml`.
- If a generated spec exceeds one thread of reliable context, the write-spec agent must stop after a bounded phase and write `handoff.md` with completed decisions, missing sections, and next prompt.
- If two runs both look viable, preserve both and use the comparison checklist rather than overwriting earlier output.

## Assumptions

- The fixed stack enum for Agent 1 is `python` and `java`. Python is the default because Python has built-in `decimal`, available `fastmcp`, and simple file-based JSON workflow support. Java is a supported alternate because `BigDecimal`, JUnit 5, JaCoCo, and Jackson can produce a rigorous implementation if time allows a second run.
- The custom MCP deliverable remains `mcp/server.py` per Homework 6 even when the generated pipeline stack is Java; a Java pipeline run can still produce JSON results read by the Python FastMCP status server.
- Context7 MCP is now available in the current Codex session after tool discovery, but project-local discovery still depends on the active project root.
- The user will run the actual spec-generation pipeline from a Codex project rooted at `homework-6` when local MCP discovery matters.
- Git commits will be made from `gen-ai-se-hw`, the actual repository root, not from a nested homework folder.
- Homework 3 remains a formatting and quality reference, not a hidden source of Homework 6 domain content.
- The final selected spec run can be committed along with canonical files later, provided transient or duplicate run artifacts are intentionally scoped.

## Risks

- A duplicated Claude command and Codex skill can drift. Phase 01 must make one concise workflow canonical and add cross-surface validation checks.
- MCP discovery can differ between Codex, Claude Code, and shell execution. The plan must document usable fallback paths and avoid treating a single UI state as universal.
- A generated spec can become too large and diffuse. The write-spec agent must force bounded phases and handoff files.
- Research can overreach into real banking/legal claims. The generated domain docs must separate cited facts from educational design rules.
- Preserving many runs can create noisy Git diffs. The run registry must distinguish selected evidence from transient experiments.
- Creating `mcp.json` before the custom MCP server exists can confuse local tool startup if it points at missing files. Phase 01 should register Context7 first and add `pipeline-status` only when `mcp/server.py` exists or clearly mark the custom server as a later phase in config notes.

## Known unknowns

- The exact Claude Code slash-command discovery rules in the user's current Claude installation are not verified in this thread. Phase 01 will use the standard `.claude/commands/write-spec.md` layout required by the assignment.
- The exact global Context7 installation location is not visible on this shell PATH. Context7 MCP tool discovery works in this thread, and `npx` is available for portable config.
- The selected stack for the canonical final pipeline is not frozen until a generated run is selected. The allowed generation profiles are fixed as `python` and `java`; `auto` and arbitrary stack names are excluded from this work item unless a later approved amendment expands the enum.
- The student's final author line format is not needed for Task 1, but later documentation must include the student's name.

## Rejected alternatives

- Single-pass direct generation of `specification.md` with no run folder: rejected because Homework 3 showed that one large pass loses detail and makes comparison difficult.
- Relying only on `dev-doc-harness`: rejected because Homework 6 agents must be portable and usable by graders or tools that do not have the harness installed.
- Relying only on Claude Code slash commands: rejected because the user must run the pipeline from Codex and needs a Codex Markdown skill.
- Creating only a Codex skill and asking Claude to read it manually: rejected because the assignment explicitly requires a slash command surface.
- Committing a combined `mcp.json` that points at a missing `mcp/server.py` in Phase 01: rejected because it can break local MCP startup before the custom server exists.
- Using Homework 3 EU banking research as Homework 6 domain source without new attribution: rejected because Agent 1 should either research current sources or explicitly use only local assignment assumptions.
- `stack=auto`: rejected because it would make reproducibility and comparison weaker. The agent should perform or use a one-off tradeoff analysis, then generate from a fixed stack enum.
- Keeping the generated spec fully language-agnostic: rejected because Homework 6 requires exact files, functions, prompts, commands, tests, and MCP details that must be concrete for a chosen stack.

## Acceptance criteria

- Draft planning artifacts exist under `homework-6/docs/work-items/2026-06-16-homework-6-spec-agent/`.
- The draft package includes this large/phased anchor spec and a fresh-thread executable Phase 01 plan.
- The plan preserves the dual-surface requirement for Claude Code and Codex.
- The plan makes stack selection a first-class input with `python` as the default and `java` as a supported alternate profile.
- The plan defines a portable run preservation, comparison, and final-selection workflow.
- The plan records the current Context7/MCP discovery facts and the homework-root project strategy.
- The plan keeps Task 1 implementation files out of scope until after planning approval and freeze.
- The plan records a model and sub-agent strategy.
- Draft validation commands pass or any limitation is reported before asking for review.

## Phase decomposition

| Phase | Objective | Output |
|---|---|---|
| 01 | Create the Agent 1 control surfaces and supporting local strategy docs without generating the final spec yet. | `plan-phase-01-agent-controls-homework-6-spec-agent.md` |
| 02 | Invoke the `write-spec` workflow, preserve one or more spec-generation runs, compare outputs, and select the canonical Task 1 spec package. | `plan-phase-02-generate-and-select-spec-homework-6-spec-agent.md` |
| 03 | Harden the selected Task 1 package for handoff to Agent 2, including final validation and implementation-plan readiness for code generation. | `plan-phase-03-task-1-handoff-homework-6-spec-agent.md` |

Only Phase 01 is planned in detail now. Later phase plans must derive from this anchor spec and the completed Phase 01 outputs.

## Planning artifact freeze gates

Use `module:freeze-gate`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, and `rule:freeze.multi-gate-flow`.

Draft review gate for this pass:

- Draft this spec and the Phase 01 plan.
- Verify no placeholder markers or missing required sections.
- Stage only the draft planning artifacts.
- Ask the operator to approve the staged package or provide feedback.
- Do not commit or implement.

Approval freeze gate after operator approval:

- Update `homework-6/CHANGELOG.md` with a newest-first planning entry.
- Re-run validation.
- Stage only the approved planning artifacts and changelog.
- Commit those paths.
- Report the commit hash and frozen artifact paths.
- Remind the operator they may push and create a draft plan-only PR.
- Ask the operator to confirm model, reasoning-effort, and sub-agent policy choices before implementation.
- Stop before Phase 01 implementation until a fresh operator instruction authorizes it.

## Model and Sub-agent Strategy

Current orchestration: Codex Desktop, GPT-5-based agent per system context; exact reasoning-effort control is not exposed in this thread.
Fit assessment: High planning complexity because this defines the agent contract for the entire capstone, involves cross-tool portability, MCP discovery, research quality, privacy rules, and future run evidence. Current orchestration is suitable for drafting and review.
Recommended change: None for draft planning. For implementation after freeze, continue with a strongest-available reasoning profile if the UI exposes one.

Sub-agents: None for this draft package. The artifact set is small and cross-surface consistency matters more than parallel throughput. A later Phase 02 spec-quality review sub-agent may be useful if the operator explicitly approves it after the Phase 01 freeze.

## Documentation artifact matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Approval freeze and before implementation commit | `homework-6/CHANGELOG.md` | Create if absent; newest-first Homework 6 entry. |
| Test cases | Snapshot | No | Not applicable for draft planning | Not created | Phase 01 validates docs/config/skill metadata rather than application behavior. |
| Testing guide delta | Living delta | Deferred | Later Homework 6 testing phase | `homework-6/docs/work-items/2026-06-16-homework-6-spec-agent/deltas/testing-guide.delta.md` | Needed only if Task 1 changes testing workflow docs before final Homework 6 docs exist. |
| Operator manual delta | Living delta | Deferred | Later run-pipeline and MCP phases | `homework-6/docs/work-items/2026-06-16-homework-6-spec-agent/deltas/operator-manual.delta.md` | Runtime operations will be documented after the pipeline exists. |
| API reference delta | Living delta | No | Not applicable for Task 1 planning | Not created | No public API is introduced by the spec-writing control surfaces. |
| Architecture snapshot | Snapshot | No | Covered by this anchor spec | Not created | The relevant architecture is the agent/run-control design captured here. |
| Architecture summary delta | Living delta | Deferred | Later final docs phase | `homework-6/docs/work-items/2026-06-16-homework-6-spec-agent/deltas/architecture-summary.delta.md` | Final architecture docs are generated after code exists. |
| Run registry guide | Living homework doc | Yes | Phase 01 implementation | `homework-6/docs/agent-runs/README.md` | Defines preservation, comparison, and selection workflow. |
| Skill quality reference | Skill reference | Yes | Phase 01 implementation | `homework-6/.agents/skills/write-spec/references/write-spec-quality-bar.md` | Keeps the Codex skill concise and portable. |
| Stack profile reference | Skill reference | Yes | Phase 01 implementation | `homework-6/.agents/skills/write-spec/references/stack-profiles.md` | Captures the fixed enum, default stack, Python/Java tradeoff analysis, and stack-specific generation rules. |

## Approval

- Status: Draft
- Superseded by: None

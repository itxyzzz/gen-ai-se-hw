# Large or Phased Work Item Phase 01: Java Stack Readiness

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Short ID: `java-stack-and-hera-orchestration`
Status: Draft Review
Harness release: Installed global `dev-doc-harness` loaded on 2026-06-21
Schema: `schema:plan.phase`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:quality.phase-plan-fresh-thread`, `rule:models.strategy-required`, `rule:lifecycle.variance-policy`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, `rule:freeze.stop-before-implementation`

## Objective

Tighten the existing Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), Clio (Documentation Generator), and Operator Layer helper surfaces so a future Java alternate generation set can run without inheriting Python-only commands, run layouts, validation assumptions, selection metadata, or coverage tooling.

This phase does not add Hera (Orchestrator), does not run Java generation, and does not replace the selected Python package. It prepares stack-aware control contracts and helper dispatch while preserving the current Python selected package as the canonical submission.

## Input Context

The implementing agent must read these inputs before editing implementation-target files:

- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/spec-java-stack-and-hera-orchestration.md`, the approved anchor spec.
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/snapshots/test-cases.snapshot.md`, the Phase 01 expected-behavior snapshot.
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/snapshots/architecture.snapshot.md`, the Phase 01 architecture snapshot.
- `AGENTS.md`, `../AGENTS.md`, `../HOMEWORK_STANDARDS.md`, `README.md`, `TASKS.md`, and `sample-transactions.json`.
- `docs/agent-runs/final-selection.md`, especially the selected Python Athena, Hephaestus, Themis, and Clio records.
- `agent-control/write-spec/stack-profiles.md`, `agent-control/write-spec/workflow.md`, `agent-control/write-spec/quality-bar.md`, and `agent-control/write-spec/run-registry.md`.
- `agent-control/generate-code/`, `agent-control/generate-tests/`, `agent-control/generate-docs/`, and `agent-control/operate-pipeline/`.
- Thin skill and command wrappers under `.agents/skills/`, `.claude/skills/`, and `.claude/commands/`.
- `scripts/check_coverage_gate.py`, `.githooks/pre-push`, `.claude/settings.json`, `mcp.json`, `.codex/config.toml`, and `mcp/server.py`.
- Context7 notes from this planning pass:
  - JUnit current docs were resolved as `/websites/junit_current`; Maven Java test guidance should reference JUnit Jupiter with Maven Surefire/Failsafe rather than a custom test runner.
  - JaCoCo docs were resolved as `/websites/jacoco_jacoco_trunk_doc`; Java coverage enforcement should use the JaCoCo Maven plugin `check` goal, where `haltOnFailure` blocks the build and threshold rules can require a `0.80` covered ratio.

Preserve these anchor-spec decisions:

- Java is a parallel alternate by default.
- Python remains canonical unless an explicit later operator selection replaces it.
- Helper surfaces should be universal where practical.
- `mcp/server.py` stays the preferred Python FastMCP status reader and expects stack-neutral JSON result files.
- `.codex/config.toml` `agents.max_depth = 2` belongs to Phase 02, not this phase, unless the operator explicitly amends scope.
- Phase 01 may account for the existing dirty `.codex/config.toml` line but must not stage or commit it as part of the Phase 01 plan or implementation unless separately authorized.

## Likely Files And Areas

Expected implementation files:

- `agent-control/write-spec/stack-profiles.md`
- `agent-control/write-spec/workflow.md`
- `agent-control/write-spec/quality-bar.md`
- `agent-control/write-spec/run-registry.md`
- `agent-control/generate-code/workflow.md`
- `agent-control/generate-code/quality-bar.md`
- `agent-control/generate-code/run-registry.md`
- `agent-control/generate-tests/workflow.md`
- `agent-control/generate-tests/quality-bar.md`
- `agent-control/generate-tests/run-registry.md`
- `agent-control/generate-docs/workflow.md`
- `agent-control/generate-docs/quality-bar.md`
- `agent-control/generate-docs/run-registry.md`
- `agent-control/operate-pipeline/commands-and-hooks.md`
- `.agents/skills/run-pipeline/SKILL.md`
- `.agents/skills/validate-transactions/SKILL.md`
- `.claude/skills/run-pipeline/SKILL.md`
- `.claude/skills/validate-transactions/SKILL.md`
- `.claude/commands/run-pipeline.md`
- `.claude/commands/validate-transactions.md`
- `scripts/check_coverage_gate.py`
- `.githooks/pre-push`
- `.claude/settings.json`
- `docs/agent-runs/final-selection.md`
- New linked selection metadata, planned as `docs/agent-runs/selection-sets.json`
- Possibly `mcp/server.py` tests or documentation only if static review finds it assumes Python-specific result fields beyond the existing stack-neutral contract.
- `CHANGELOG.md` before the implementation commit.
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/deltas/testing-guide.delta.md`
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/deltas/operator-manual.delta.md`
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/deltas/architecture-summary.delta.md`
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/implementation-notes/variance-log.md`

Do not edit canonical generated product files such as `integrator.py`, `agents/*.py`, `tests/*.py`, `README.md`, `HOWTORUN.md`, `ARCHITECTURE.md`, `TESTING_GUIDE.md`, `API_REFERENCE.md`, screenshots, or `specification.md` in this phase.

## Model And Sub-Agent Strategy

Current orchestration: Codex Desktop local coding agent; exact model label and reasoning-effort selector are not exposed in this thread.
Fit assessment: Phase 01 is high-blast-radius control-surface work with moderate implementation complexity. It touches many docs, wrappers, and helper commands but should not require generated product code changes.
Recommended change: Use the active `enterprise-default` policy. Use the latest strongest available model class with high reasoning for final integration and review. Smaller/faster current profiles are acceptable only for read-only inventory scans.

Sub-agents: Optional, read-only only. Phase 01 implementation may use up to two concurrent read-only explorer/reviewer agents without fresh confirmation because the scopes below are independent and the approved strategy stays within the repository policy cap. Write-capable sub-agents require fresh operator confirmation.

| Purpose | Context strategy | Input context | Output artifact | Model policy | Model class/profile | Reasoning effort | Reason | Parallel? | Blast radius if wrong |
|---|---|---|---|---|---|---|---|---|---|
| Inventory remaining Python-only assumptions in agent-control packages and wrappers | curated artifacts | Approved spec, this phase plan, `agent-control/`, `.agents/skills/`, `.claude/skills/`, `.claude/commands/` | Read-only inventory memo | `enterprise-default` | smaller/faster current profile | medium | Broad text scan is bounded and low-risk | Yes | Medium: missed assumptions could block Java generation |
| Review helper dispatch design and selection metadata shape | curated artifacts | This phase plan, `scripts/check_coverage_gate.py`, `.githooks/pre-push`, `.claude/settings.json`, `agent-control/operate-pipeline/`, `docs/agent-runs/final-selection.md`, `mcp/server.py` | Read-only review memo | `enterprise-default` | latest strongest available profile | high | Helper and metadata design affects both Python and Java verification | Yes | High: wrong design can break the selected Python package or Java readiness |

The orchestration thread owns all edits, final integration, validation, staging, commit preparation, and user-facing handoff.

## Tasks

- [ ] Confirm branch is `homework-6-extension` or another explicit Homework 6 working branch, and confirm the worktree has no unrelated staged files before editing.
- [ ] Preserve the existing `.codex/config.toml` dirty `agents.max_depth = 2` line as pre-existing operator work unless the operator explicitly authorizes including it in this phase.
- [ ] Update Athena (Spec Writer) stack guidance so Java runs must produce concrete Maven/Jackson-or-equivalent/JUnit 5/JaCoCo/BigDecimal paths, commands, and result-shape notes, while Python remains the default when stack is omitted.
- [ ] Update Hephaestus (Code Generator) workflow, quality bar, and run registry to use stack-aware run IDs such as `YYYYMMDD-HHMMSS-generate-code-java-short-label`, Java-native output examples, Java validation commands, Java Context7 topics, and Java privacy checks.
- [ ] Update Themis (Test Generator) workflow, quality bar, and run registry to use stack-aware run IDs, Java test output paths such as `src/test/java/...`, Maven/JUnit workspace validation, JaCoCo evidence, and stack-neutral command/hook validation.
- [ ] Update Clio (Documentation Generator) workflow, quality bar, and run registry to document selected package sets by stack, use stack-specific evidence commands, and describe Java alternate evidence without treating it as canonical replacement.
- [ ] Update `agent-control/operate-pipeline/commands-and-hooks.md` so `/run-pipeline`, `/validate-transactions`, and coverage behavior resolve an explicit stack or selected package-set metadata before choosing Python or Java commands.
- [ ] Add a linked selection metadata design to `docs/agent-runs/final-selection.md`, with implementation creating `docs/agent-runs/selection-sets.json` as the machine-readable package-set registry. Keep `final-selection.md` as the human selection history and summary.
- [ ] Seed `docs/agent-runs/selection-sets.json` with the current canonical Python set during implementation. Include schema/version, canonical set ID, stack, status, selected Athena/Hephaestus/Themis/Clio run IDs, inventory paths, expected root or package directory, and stack command hints. Do not record raw transaction data.
- [ ] Update `scripts/check_coverage_gate.py` into a backward-compatible universal coverage helper:
  - Keep `python scripts/check_coverage_gate.py --fail-under 80` working for the current selected Python package.
  - Add `--stack {auto,python,java}` with default `auto`.
  - Add `--project-dir PATH` so Java or run-local candidate packages can be checked without mutating the root.
  - For `python`, preserve the current pytest coverage behavior and temp coverage workspace.
  - For `java`, require `pom.xml` in the project directory and run Maven/JUnit/JaCoCo through the generated package's configured Maven plugins. The planned command family is `mvn test jacoco:report jacoco:check`, with the `--fail-under` percentage exposed to the generated `pom.xml` through a documented coverage-minimum property when supported.
  - Fail clearly when Maven is unavailable, `pom.xml` is missing, or the generated Java package lacks JaCoCo check configuration.
- [ ] Update `.githooks/pre-push` and `.claude/settings.json` only if needed to call the universal helper in a stack-safe way. Preserve the Python pass path.
- [ ] Keep `mcp/server.py` Python and stack-neutral unless a concrete static review finds a result-contract blocker. Prefer updating Java generation guidance to emit the existing `summary.json` and `TXN*.json` safe result fields.
- [ ] Add or update focused tests for the universal coverage helper where practical without requiring Maven downloads. At minimum, cover Python command construction, Java missing-`pom.xml` failure, and stack argument parsing. If existing test style makes direct tests awkward, record that limitation and validate by command-level dry checks.
- [ ] Run validation commands listed below and record exact results in the implementation handoff.
- [ ] Update the required deltas and `CHANGELOG.md` before the implementation commit.
- [ ] Review the diff for unrelated changes, unresolved draft markers, accidental canonical product edits, generated runtime noise, and selection metadata that could imply Java replaced Python.

## Tests And Validation

| Command | Expected result |
|---|---|
| `git branch --show-current` | Reports `homework-6-extension` unless the operator has explicitly switched branches. |
| `git status --short --untracked-files=all` | Shows only scoped Phase 01 changes plus any pre-existing `.codex/config.toml` dirty line kept unstaged. |
| `python -m pytest -p no:cacheprovider` | Existing selected Python suite passes. |
| `python scripts/check_coverage_gate.py --fail-under 80` | Current Python coverage gate still passes at or above 80 percent. If Windows sandbox coverage-file rename fails, request approved unsandboxed rerun and record the blocker. |
| `python scripts/check_coverage_gate.py --stack python --fail-under 80` | Explicit Python mode matches the default pass behavior. |
| `python scripts/check_coverage_gate.py --stack java --project-dir . --fail-under 80` | Fails clearly because the current canonical root is not a Java Maven project; expected signal is a controlled missing-`pom.xml` or Java project error, not a traceback. |
| `python -m json.tool mcp.json` | Existing MCP config remains valid JSON. |
| `python -m json.tool docs/agent-runs/selection-sets.json` | New selection-set metadata is valid JSON after implementation creates it. |
| `python -c "import tomllib; tomllib.load(open('.codex/config.toml','rb'))"` | Codex project config parses; do not require staging the pre-existing `max_depth` change in Phase 01. |
| `git diff --check` | No whitespace errors in the working diff. |
| `git diff -- specification.md README.md HOWTORUN.md ARCHITECTURE.md TESTING_GUIDE.md API_REFERENCE.md integrator.py agents tests docs/screenshots` | Empty for product/runtime/docs canonical targets that Phase 01 must not edit, except `docs/agent-runs/final-selection.md` and `docs/agent-runs/selection-sets.json` selection metadata. |
| `Get-ChildItem -Recurse -File agent-control,.agents,.claude,scripts,.githooks,docs\agent-runs | Select-String -Pattern 'generate-code-python|generate-tests-python|generate-docs-python|python integrator.py|pytest|\.coverage|__pycache__'` | Remaining matches are either Python-specific sections explicitly guarded by `stack=python`, historical selected-run evidence, or intentional compatibility notes. Unqualified Python-only instructions in Java-capable workflows are repaired. |
| `Get-ChildItem -Recurse -File agent-control,.agents,.claude,scripts,.githooks,docs\agent-runs | Select-String -Pattern 'java|mvn|JUnit|JaCoCo|BigDecimal|pom.xml'` | Java-capable workflows and helper guidance include concrete Java stack terms where required. |
| `Get-ChildItem -Recurse -File . | Select-String -Pattern 'Hera'` scoped to edited files | Phase 01 does not introduce Hera implementation surfaces; incidental references should remain in the approved anchor spec or future-phase notes only. |

Do not run Java generation in this phase. Do not install Maven or download Java dependencies merely for Phase 01 unless the operator separately asks for an environment probe.

## Documentation Tasks

- Required now: keep this phase plan and the two snapshots current before draft review.
- During implementation: update `deltas/testing-guide.delta.md` with stack-aware coverage/test evidence changes.
- During implementation: update `deltas/operator-manual.delta.md` with stack-aware `/run-pipeline`, `/validate-transactions`, coverage helper, and selection-set metadata operation.
- During implementation: update `deltas/architecture-summary.delta.md` with the stack-set registry and universal-helper architecture.
- During implementation: create or update `implementation-notes/variance-log.md`; record nontrivial deviations from this plan.
- Before implementation commit: update `CHANGELOG.md` with the Phase 01 implementation entry.
- Not applicable in this phase: `deltas/api-reference.delta.md`, because the runtime JSON and MCP public tool/resource contract should remain compatible.

## Variance Reminder

Use `rule:lifecycle.variance-policy`. Before freeze, edit this draft directly for operator feedback. After freeze, record nontrivial implementation variance in `implementation-notes/variance-log.md`; use a plan amendment for high-impact architecture, API, data, security, privacy, compliance, scope, acceptance-criteria, or feasibility changes.

High-impact variance examples that require an amendment before proceeding:

- Replacing `mcp/server.py` with a Java MCP server.
- Removing Python as the canonical selected package.
- Dropping the linked machine-readable selection-set metadata in favor of brittle markdown-only helper parsing.
- Moving Hera (Orchestrator) implementation into Phase 01.
- Changing Java from Maven/JUnit 5/JaCoCo to a different build/test/coverage stack.

## Planning Artifact Freeze Gate

Draft review checkpoint:

- Status: Draft Review.
- Draft artifacts: this phase plan, `snapshots/test-cases.snapshot.md`, and `snapshots/architecture.snapshot.md`.
- Draft validation: run placeholder, unresolved-decision, and whitespace checks before staging for review.
- Staging rule: stage only Phase 01 draft planning artifacts for operator review; do not commit before approval.

Approval freeze checkpoint after operator approval:

- Update `CHANGELOG.md` with a newest-first entry for Phase 01 planning.
- Verify approved artifacts contain no placeholders, unresolved required decisions, or missing required sections.
- Stage only the approved Phase 01 planning artifacts and `CHANGELOG.md`.
- Commit the approved planning package.
- Stop before implementation and ask for fresh implementation authorization plus model, reasoning-effort, and sub-agent policy confirmation.

## Handoff Output

At Phase 01 implementation completion, the implementing agent must report:

- Assigned scope and confirmation that Phase 02 Hera work, Phase 03 Java generation, and Phase 04 comparison were not started.
- Files changed and files deliberately left unchanged.
- Selection-set metadata shape and how the current Python set is represented.
- Coverage helper behavior by stack, including exact Python and Java command families.
- Commands and tests run with outcomes.
- Any Maven, JaCoCo, or local toolchain limitations encountered.
- Privacy and canonical-output safety review result.
- Variance log status.
- De-facto sub-agent use, including count, roles/scopes, concurrency or waves, context strategy, observed inheritance behavior, and de-facto model/model class/profile when known.
- Recommended next prompt for Phase 02 planning or Phase 01 follow-up repair.

## Completion Criteria

- Phase objective is met without starting Hera or Java generation.
- Existing Python selected package still passes its normal tests and coverage gate.
- Java-capable workflows no longer rely on unqualified Python commands for Java paths.
- Universal helper guidance can dispatch by selected stack or explicit stack.
- `docs/agent-runs/final-selection.md` either links to or describes the new selection-set metadata.
- `docs/agent-runs/selection-sets.json` exists, validates as JSON, and records the current Python canonical set without claiming Java selection.
- `mcp/server.py` remains compatible with the existing stack-neutral result contract or any blocker is documented before Java generation.
- Required deltas and variance log are present or explicitly marked not applicable with reason.
- `CHANGELOG.md` has a newest-first entry before the implementation commit.
- Validation commands have been run and recorded.
- De-facto sub-agent use is reported when applicable.

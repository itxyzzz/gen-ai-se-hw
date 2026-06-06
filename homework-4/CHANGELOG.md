# Homework 4 Changelog

## Homework 4 - Step 25: Open Code Run 010 (minimax-m3-free)

### Added
- Added completed Open Code pipeline evidence under `runs/bug-001/run-010-open-code-minimax-m3-free/`.
- Added `runtimeSubagentAudit` with `collectionMode: adapter-recorded` showing all six stages ran as separate task subagents spawned via the Open Code `task` tool (`subagent_type: general`, `operatorAuthorization.status: pipeline-mandated`).
- Added generated unit tests for multi-line subtotal aggregation, SAVE10 percentage rounding, the full catalog allow-list rejection set, and a happy-path valid catalog load.

### Changed
- Promoted `run-010-open-code-minimax-m3-free` to `app/current`.

### Fixed
- Fixed line totals by multiplying quantity and unit price.
- Fixed `SAVE10` to apply a ten percent discount with currency rounding.
- Fixed catalog loading to reject non-allow-listed names and to enforce resolved-path containment under `data/catalogs`.

### Tests
- `node --test --test-isolation=none homework-4/app/current/tests/*.test.js` passed with 7 tests.
- `node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js` failed as expected with the three seeded baseline defects.

## Homework 4 - Step 24: Codex Chat Run 008

### Added
- Added completed Codex Chat pipeline evidence under `runs/bug-001/run-008-codex-chat-gpt-5.4/`.
- Added `runtimeSubagentAudit` metadata showing all six stages ran through Codex sub-agents.
- Added generated catalog validation tests for valid catalog loading and unsupported catalog-name characters.

### Changed
- Promoted `run-008-codex-chat-gpt-5.4` to `app/current`.

### Fixed
- Fixed line totals by multiplying quantity and unit price.
- Fixed `SAVE10` to apply a ten percent discount with currency rounding.
- Fixed catalog loading to reject unsafe names before reading catalog files.

### Tests
- `node --test --test-isolation=none homework-4/app/current/tests/*.test.js` passed with 5 tests.
- `node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js` failed as expected with the three seeded baseline defects.

## Homework 4 - Step 23: Mandatory Sub-Agent Pipeline Contract

### Added
- Added an operator-directed amendment clarifying that `Run HW4 pipeline` mandates sub-agent stage execution without an extra default confirmation.
- Added `runtimeSubagentAudit.operatorAuthorization` wording for pipeline-mandated spawning and tool-required authorization recovery.

### Changed
- Updated the harness, adapters, and Homework 4 docs so missing authorization is not treated as a fallback excuse; the agent must ask for authorization to spawn sub-agents.
- Limited direct fallback to unavailable sub-agent tooling or a failed authorization path with explicit operator fallback approval.

### Tests
- Verified mandatory sub-agent, authorization recovery, and fallback approval wording across the harness, adapters, and homework docs.
- Verified preserved run and benchmark evidence were not modified.

## Homework 4 - Step 22: Sub-Agent Authorization Gate Planning

### Added
- Added approved planning artifacts under `docs/work-items/2026-06-06-hw4-subagent-authorization-gate/` for the HW4 sub-agent authorization and fallback confirmation contract.

### Changed
- Scoped the planned pipeline contract so missing sub-agent authorization prompts for operator confirmation instead of silently falling back to direct execution.

### Tests
- Planning freeze checks verify the approved artifacts are non-placeholder and staged with the required changelog entry.

## Homework 4 - Step 21: Codex Chat Run 007

### Added
- Added completed Codex Chat pipeline evidence under `runs/bug-001/run-007-codex-chat-gpt-5.4/`.
- Added generated regression coverage for multi-line quote totals, SAVE10 rounding, absolute catalog path rejection, and valid catalog loading.
- Added `runtimeSubagentAudit` metadata for the run, including the current Codex sub-agent spawning limitation.

### Changed
- Promoted `run-007-codex-chat-gpt-5.4` to `app/current`.

### Fixed
- Fixed line totals by multiplying quantity and unit price.
- Fixed `SAVE10` to apply a ten percent discount with currency rounding.
- Fixed catalog loading to reject unsafe names and enforce resolved path containment.

### Tests
- `node --test --test-isolation=none homework-4/app/current/tests/*.test.js` passed with 7 tests.
- `node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js` failed as expected with the three seeded baseline defects.

## Homework 4 - Step 20: Runtime Sub-Agent Audit Contract

### Added
- Added a portable `runtimeSubagentAudit` contract for future pipeline runs.

### Changed
- Updated the pipeline harness, adapters, and homework documentation so de-facto sub-agent use is recorded in `run-metadata.json`.

### Tests
- Verified documentation coverage for `runtimeSubagentAudit` across the harness, adapters, and homework docs.
- Verified preserved source and benchmark evidence folders were not edited.

## Homework 4 - Step 19: Runtime Sub-Agent Audit Planning

### Added
- Added approved lightweight planning artifacts under `docs/work-items/2026-06-06-hw4-runtime-subagent-audit/` for a portable runtime sub-agent audit contract.

### Changed
- Scoped the runtime audit design to future `run-metadata.json` evidence and homework-local documentation updates.

### Tests
- Planning freeze checks verify the approved artifacts are non-placeholder and staged with the required changelog entries.

## Homework 4 - Step 18: Final PR Preparation

### Added
- Added final README reflections on Codex, Google Antigravity, Open Code, adapter portability, benchmark observations, and model cost/behavior differences.
- Added a Homework 4 draft PR description under `docs/process-artifacts/`.
- Added final PNG screenshots for Codex design, scaledown, Antigravity planning/agent work, Open Code adapter/pipeline runs, and Codex benchmark preparation.

### Changed
- Clarified the top-level AI tool summary in `README.md`.
- Removed a duplicated portable launch phrase section from `HOWTORUN.md`.
- Replaced the earlier auto-generated SVG evidence references in the PR draft with the final screenshot sequence.

### Fixed
- Removed the previous auto-generated SVG screenshot placeholders from `docs/screenshots/`.

### Tests
- Final verification commands were rerun during submission preparation and recorded in the closing review notes.

## Homework 4 - Step 17: Snapshot-Safe Benchmark Normalization

### Added
- Added a fresh durable normalization design spec and implementation plan for `bug-001`.
- Added normalized benchmark evidence under `benchmark/bug-001/runs/run-<NNN>-<tool>-<pattern>/` for all six preserved source runs.
- Added benchmark-owned `source-map.json`, `run-metadata.normalized.json`, and `artifact-index.md` files for each normalized run.
- Added a benchmark-owned synthesized `patch.diff` for the Nemotron run because its source snapshot preserved split patch files only.

### Changed
- Updated active pipeline instructions so future runs use the strict `run-<NNN>-<tool>-<pattern>` folder pattern.
- Updated benchmark outputs to compare all six normalized runs using commit-sequence ordering.
- Updated active documentation to distinguish immutable source run snapshots from normalized benchmark artifacts.

### Fixed
- Resolved inconsistent benchmark naming without modifying preserved source run folders or historical plan/spec snapshots.

### Tests
- Verified by snapshot-safety checks: source run folders must remain unchanged, and historical Superpowers specs/plans must only gain the new normalization spec and plan.

## Homework 4 - Step 16: Open Code Pipeline Run with Gemini 3.5 Flash

### Added
- Added a fresh completed pipeline run evidence folder under `runs/bug-001/open-code-gemini-3.5-flash-run-005`.
- Added 6 robust FIRST-compliant regression tests in `tests/generated-regression.test.js` covering line multiplication, SAVE10 discount decimal rounding, invalid discount code rejection, catalog loading, and path traversal defense-in-depth bounds.

### Changed
- Promoted `open-code-gemini-3.5-flash-run-005` to `app/current`.

### Fixed
- Fixed arithmetic bug in `quoteCalculator.js` by changing line total calculation from addition to multiplication.
- Fixed SAVE10 discount bug in `quoteCalculator.js` by applying a 10% discount multiplier wrapped in `roundCurrency`.
- Closed path traversal vulnerability (CWE-22) in `catalogRepository.js` using alphanumeric name validation regex `/^[a-zA-Z0-9_-]+$/` and resolved path boundary containment checks.

### Tests
- `node --test --test-isolation=none homework-4/app/current/tests/*.test.js` passed successfully with 9 tests.
- `node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js` failed as expected with the three seeded baseline defects.

## Homework 4 - Step 15: Open Code Pipeline Run open-code-run-004

### Added
- Added a fresh completed pipeline run evidence folder under `runs/bug-001/open-code-run-004`.
- Added 12 unit tests in `tests/fix.test.js` for changed code, covering edge cases and FIRST criteria.
- Added error messages including the catalog name in `catalogRepository.js` for better diagnostics.

### Changed
- Refined catalog name path validation from `path.relative` + `startsWith("..")` to regex `^[a-zA-Z0-9_-]+$` + `path.resolve` + `startsWith` boundary check.
- Promoted `open-code-run-004` to `app/current`.

### Fixed
- N/A

### Tests
- `node --test --test-isolation=none homework-4/app/current/tests/*.test.js` passed with 17 tests.
- `node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js` failed as expected with the three seeded baseline defects.

## Homework 4 - Step 14: Codex Chat Run gpt-5.2-run-002 Evidence

### Added
- Added a fresh completed pipeline run evidence folder under `runs/bug-001/codex-chat-gpt-5.2-run-002`.

### Changed
- No changes to `app/current`; the new run produces the same fixed output as the canonical completed app.

### Fixed
- N/A

### Tests
- `node --test --test-isolation=none homework-4/app/current/tests/*.test.js` passed with 5 tests.
- `node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js` failed as expected with the three seeded baseline defects.

## Homework 4 - Step 13 (Added by Open Code): Open Code Adapter Mixed-Environment Optimization

### Added
- Added Open Code adapter optimization design spec at `docs/superpowers/specs/2026-05-22-opencode-adapter-optimization-design.md`.
- Added Open Code adapter optimization implementation plan at `docs/superpowers/plans/2026-05-22-opencode-adapter-optimization-plan.md`.

### Changed
- Reworked `adapters/open-code.md` for mixed model environments with ordered candidates across Codex/OpenAI, Claude, and free/open providers.
- Added deterministic fallback and per-stage metadata recording requirements to the Open Code adapter.
- Clarified skill handling split in Open Code adapter: Superpowers skill-tool usage vs local `skills/*.md` stage requirements.
- Normalized Open Code launch semantics to the short canonical phrase `Run HW4 pipeline` and documented prompt-command behavior.
- Updated `HOWTORUN.md` and `API_REFERENCE.md` so adapter prompts use the same canonical launch phrase with context-based adapter selection.

### Fixed
- Removed the long Open Code launch prompt variant that was hard to remember and inconsistent with harness-first launch flow.

### Tests
- `node --test --test-isolation=none homework-4/app/current/tests/*.test.js` passed with 7 tests.
- `node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js` failed as expected with the three seeded baseline defects.
- Verified launch phrase and Open Code wording consistency across Homework 4 markdown docs.

## Homework 4 - Step 12: Submission Evidence Cleanup

### Added
- Documented the intentional portable model-policy decision in `README.md`.

### Changed
- Restored `app/current` to match the completed Codex Chat evidence run.
- Updated Google Antigravity model guidance to use Gemini 3.1 Pro and Gemini 3.5 Flash as the currently available primary mappings, with future Pro/Flash models listed as alternatives.

### Fixed
- Removed the stale required-context reference to the deleted Codex Chat runner skill from `skills/pipeline-harness-wrapper.md`.
- Removed abandoned non-submission run references and artifacts from the submission evidence trail.

### Tests
- `node --test --test-isolation=none homework-4/app/current/tests/*.test.js` passed with 5 tests.
- `node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js` failed as expected with the three seeded baseline defects.
- Verified `homework-4/app/current` matches the completed Codex Chat evidence run.
- Verified no abandoned run identifiers remain.

## Homework 4 - Step 11 (Added by Antigravity)

### Added
- Added optional `## Reusable Agentic Execution Extensions` guidelines inside `skills/pipeline-harness-wrapper.md` to support subagent context isolation, self-correction reflection loops, and static analysis integration generically.
- Added concrete **Model Selection** mappings to `adapters/google-antigravity.md` using Gemini 3.1 Pro and Gemini 3.5 Flash primary models, with future Pro/Flash models allowed as alternatives.
- Added a tool-based programmatic **Orchestration Procedure** in `adapters/google-antigravity.md` specifying subagent delegation (`define_subagent`, `invoke_subagent`), local test running (`run_command`), and reflection loops.
- Added Anthropic Claude model selection mapping to `adapters/claude-code.md` (`claude-3-5-sonnet` and `claude-3-5-haiku`).
- Added open-source model selection mapping to `adapters/open-code.md` (`llama-3.3-70b-instruct` / `Qwen-2.5-Coder-32B-Instruct` and `llama-3.1-8b-instruct` / `Qwen-2.5-Coder-7B-Instruct`).
- Added design spec `homework-4/docs/superpowers/specs/2026-05-22-antigravity-adapter-optimization-design.md` and implementation plan `homework-4/docs/superpowers/plans/2026-05-22-antigravity-adapter-optimization-plan.md` under Superpowers docs.

### Changed
- Refactored pipeline harness by de-duplicating and removing vendor-specific scripts from the `/skills/` folder.
- Decommissioned and safely deleted `skills/codex-chat-pipeline.md`, transferring its runner trigger, procedure steps, artifact contracts, and quality gates directly into `adapters/codex-chat.md`.
- Updated `adapters/claude-code.md` to remove all Codex-specific dependencies and map its workspaces and artifact contracts to the universal harness.
- Updated `adapters/generic-agent.md` to direct capable agents to utilize harness extensions and follow general model-tier guidance.

### Fixed
- Resolved cross-adapter dependencies and alignment gaps between Codex, Claude, Antigravity, Open Code, and Generic adapters.

### Tests
- `node --test --test-isolation=none homework-4/app/current/tests/*.test.js` passed successfully.
- `node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js` failed as expected with the three seeded baseline defects.

## Homework 4 - Step 9

### Added
- Added `homework-4/AGENTS.md` so Homework 4 pipeline intent launches the text wrapper without requiring an exact phrase.
- Added adapter/model naming to the canonical run folder: `codex-chat-gpt-5.4-run-001`.
- Added `adapters/generic-agent.md` as a fallback for capable tools without a dedicated adapter.

### Changed
- Shortened the pipeline launch alias to `Run HW4 pipeline`.
- Moved concrete model names out of portable agent specs and into `adapters/codex-chat.md`.
- Updated portable agents to refer to the current run instead of a hard-coded run folder.
- Updated the pure-agentic plan checklist to reflect completed implementation tasks.
- Split `homework-4/AGENTS.md` into development, pipeline execution, and benchmarking sections.
- Moved the harness wrapper from `docs/` to `skills/pipeline-harness-wrapper.md`.
- Changed adapter selection to happen automatically from the active agentic tool in the happy path.

### Fixed
- Added persistent newest-first changelog guidance to global instructions, repo-level instructions, and homework standards.

### Tests
- `node --test --test-isolation=none homework-4/app/current/tests/*.test.js` passed with 5 tests.
- `node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js` failed as expected with the three seeded baseline defects.
- Verified required canonical run artifacts exist under `runs/bug-001/codex-chat-gpt-5.4-run-001`.
- Verified portable agent specs no longer contain concrete model names or hard-coded run folders.

## Homework 4 - Step 8

### Added
- Added a superseding pure-agentic design spec and implementation plan.
- Added `skills/pipeline-harness-wrapper.md` as the one-phrase Markdown harness skill.
- Added text adapter mappings for Codex Chat, Claude Code, Open Code, Google Antigravity, and a generic capable-agent fallback.
- Added app-local ESM markers so the sample app tests run without a root package or pipeline scripts.

### Changed
- Reframed Homework 4 around the launch phrase `Run HW4 pipeline`.
- Expanded all agent specs with chat-harness instructions, required artifacts, and completion gates.
- Rewrote README, HOWTORUN, API reference, architecture, testing guide, demo notes, benchmark text, and SVG evidence for the text-first workflow.
- Updated the canonical run metadata to identify the `codex-chat` adapter and one-phrase launch command.

### Fixed
- Removed the over-scoped JavaScript harness, script adapters, root npm scripts, executable demo runners, stale blocked SDK runs, and prompt-packet preparation run.
- Marked the earlier JavaScript harness design and plan as superseded historical context.

### Tests
- `node --test --test-isolation=none homework-4/app/current/tests/*.test.js` passed with 5 tests.
- `node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js` failed as expected with the three seeded baseline defects.

> Historical note: Steps 7-0 below describe the earlier JavaScript harness
> implementation. They are retained for process traceability but superseded by
> Step 8 for the submitted architecture.

## Homework 4 - Step 7

### Added
- Added final verification evidence through regenerated benchmark outputs for the intentional run set.

### Changed
- Removed temporary harness-test run folders before final comparison so benchmark outputs focus on intentional submission runs.

### Fixed
- N/A

### Tests
- `npm run verify:baseline` passed and confirmed seeded baseline issues are still present.
- `npm run pipeline:openai -- --scenario bug-001 --run openai-blocked-final --model gpt-5.3-codex --reasoning high` recorded a blocked live run because `OPENAI_API_KEY` is not set.
- `npm test` passed with 11 tests.
- `npm run compare -- --scenario bug-001` passed and generated a comparison for 4 intentional runs.

## Homework 4 - Step 6

### Added
- Added README, HOWTORUN, API reference, architecture, and testing guide documentation.
- Added demo helpers and command notes under `demo/`.
- Added SVG evidence files for pipeline run, Codex chat preparation, tests, security report, and benchmark comparison under `docs/screenshots/`.

### Changed
- Documented the deterministic mock adapter as the local one-command proof and the OpenAI SDK credential blocker honestly.

### Fixed
- Added demo assets that were required by the homework standards but absent from the initial phase plan.

### Tests
- Documentation reflects the latest successful verification commands from Step 5.

## Homework 4 - Step 5

### Added
- Generated the primary deterministic run at `runs/bug-001/run-001`.
- Generated an OpenAI SDK blocked run at `runs/bug-001/openai-blocked-001` because `OPENAI_API_KEY` is not set.
- Generated Codex chat prompt packets at `runs/bug-001/codex-chat-001/codex-chat-prompts`.
- Generated benchmark files under `benchmark/`.
- Promoted `run-001` into `app/current`.

### Changed
- Adjusted promotion to copy changed files without deleting the whole current app folder first, avoiding Windows sandbox file-lock failures.
- Switched Node test commands to `--test-isolation=none` for compatibility with the Windows Codex sandbox.

### Fixed
- Fixed internal harness validation to avoid Node child-process spawning, which is blocked in this sandbox.

### Tests
- `npm run app:baseline:test` failed as expected with the three seeded baseline defects.
- `npm run verify:baseline` passed and confirmed the seeded defects are present.
- `npm run test:harness` passed with 6 tests.
- `npm run app:current:test` passed with 5 tests.
- `npm test` passed with 11 tests.

## Homework 4 - Step 4

### Added
- Added the deterministic mock adapter for local one-command pipeline execution.
- Added the OpenAI SDK adapter shell that records an honest blocked run when credentials or package support are unavailable.
- Added the Codex chat adapter for prompt packet preparation and artifact validation.
- Added adapter documentation including future Claude Code mapping.

### Changed
- Connected `pipeline/run.js` to mock, OpenAI SDK, and Codex chat adapter modes.

### Fixed
- N/A

### Tests
- Added mock adapter coverage through harness tests.

## Homework 4 - Step 3

### Added
- Added the universal harness modules for config loading, agent parsing, workspace isolation, artifact validation, diff summaries, baseline verification, promotion, and comparison.
- Added harness tests for config/spec validation, isolated workspaces, mock adapter runs, promotion, and benchmark comparison.

### Changed
- Moved `verify-baseline.js` into the early harness work so Phase 1 verification can run as written.

### Fixed
- Resolved the Phase 1/Phase 3 sequencing risk where `npm run verify:baseline` needed a script before the full harness phase.

### Tests
- Added `npm run test:harness` coverage for the harness modules.

## Homework 4 - Step 2

### Added
- Added the six portable agent specs for the helper and required pipeline stages.
- Added the research quality measurement, FIRST unit test, and Codex chat pipeline skills.
- Added `pipeline.config.yaml` with stage order, model policies, scenario defaults, required artifacts, and benchmark weights.

### Changed
- Documented explicit model choices in agent frontmatter and model policy configuration.

### Fixed
- N/A

### Tests
- Manual document validation pending harness support.

## Homework 4 - Step 1

### Added
- Added a Node.js package shell with scripts for baseline verification, pipeline execution, promotion, comparison, and tests.
- Added the intentionally buggy quote calculator baseline app with seeded line-total, discount, and catalog traversal defects.
- Added scenario context, initial codebase research, and an implementation plan for `bug-001`.

### Changed
- Established `homework-4/app/baseline` as the immutable seeded input application for future pipeline runs.

### Fixed
- N/A; seeded defects intentionally remain in the baseline app.

### Tests
- Added baseline tests that describe the correct behavior and are expected to fail against `app/baseline`.

## Homework 4 - Step 0

### Added
- Added durable design and implementation planning artifacts for the portable agentic pipeline.
- Captured the chosen architecture for an immutable buggy baseline app, isolated adapter/model runs, a promoted fixed app, and benchmark comparison outputs.

### Changed
- Clarified that Homework 4 implementation will live under `homework-4/` despite the expected structure typo in `TASKS.md`.

### Fixed
- Tightened benchmark scoring so blocked runs do not receive reproducibility points reserved for completed runs.
- Updated benchmark generation so only completed runs appear in the scored comparison table.
- Updated the deterministic adapter to execute every configured stage, load stage skills, and record stage model policy metadata in run metadata.
- Made run artifacts truthful about in-process validation used inside the adapter when the sandbox blocks Node child processes.
- Moved harness test output to ignored `.test-*` folders so `npm test` does not mutate submitted run, current-app, or benchmark artifacts.

### Tests
- Planning-only step; no runtime tests added or run.

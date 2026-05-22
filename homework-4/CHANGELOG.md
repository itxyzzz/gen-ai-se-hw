# Homework 4 Changelog

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

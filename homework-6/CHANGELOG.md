# Homework 6 Changelog

## Homework 6 - Step 39: Review Repair Plan

### Added

- Added the approved planning package for Homework 6 end-to-end review repair under `docs/work-items/2026-06-21-homework-6-review-repair/`.
- Added a test-case snapshot for Clio evidence-generation rules, fourth-component documentation, Claude project-skill documentation, Hephaestus `shared/` evidence consistency, and the Hephaestus terminology leak repair.

### Changed

- Planned the repair as a control-surface-only update for Clio, Athena, Hephaestus, and `agents.md`.
- Recorded that clean Clio documentation-package regeneration and selection is a separate follow-up step, not part of this control-surface repair.

### Fixed

- Not applicable; this is the plan-only checkpoint.

### Tests

- Validated the staged planning package for unresolved draft markers and whitespace issues.

## Homework 6 - Step 38: PR Draft Screenshot Links

### Added

- Added Clio workflow and quality-bar guidance that PR draft screenshot links must resolve relative to `docs/pr-description-draft.md`.

### Changed

- Changed `docs/pr-description-draft.md` screenshot links from `docs/screenshots/*.png` to `screenshots/*.png`.
- Updated the preserved Clio output PR draft copy to match the canonical link repair.

### Fixed

- Fixed broken PR draft screenshot links that rendered as `docs/docs/screenshots/*.png`.

### Tests

- Planned verification covers focused scans for stale `docs/screenshots/*.png` links in PR draft files and existence checks for every linked screenshot target.

## Homework 6 - Step 37: Clio Reviewer-Facing Language Guard

### Added

- Added Clio workflow and quality-bar checks to keep internal Clio instructions out of reviewer-facing generated documentation.

### Changed

- Reworded the HOWTORUN privacy evidence note so it reads as reviewer-facing guidance instead of an internal Clio directive.

### Fixed

- Tightened Clio generation rules for review feedback that flagged internal instruction language in the generated runbook.

### Tests

- Planned verification covers focused scans for the flagged HOWTORUN sentence and internal Clio workflow-control phrases in reviewer-facing docs.

## Homework 6 - Step 36: Clio Documentation Package

### Added

- Added the first selected Clio (Documentation Generator) documentation package for the selected Athena, Hephaestus, and Themis runs.
- Added canonical reviewer docs: `README.md`, `HOWTORUN.md`, `ARCHITECTURE.md`, `TESTING_GUIDE.md`, `API_REFERENCE.md`, and `docs/pr-description-draft.md`.
- Added stable reviewer screenshot targets under `docs/screenshots/` copied from preserved operator-sourced evidence.
- Preserved the Clio run under `docs/agent-runs/20260620-230201-generate-docs-python-primary/` with source snapshots, evidence notes, validation checklist, handoff, and output inventory.

### Changed

- Updated `docs/agent-runs/final-selection.md` to select Clio run `20260620-230201-generate-docs-python-primary`, list copied canonical targets, record screenshot mapping, and summarize validation evidence.

### Fixed

- Documented the local `mcp/server.py` import-name collision with the installed third-party `mcp` package and the file-path import workaround for direct helper checks.
- Documented the Windows sandbox coverage-file rename limitation and the validated unsandboxed coverage rerun.

### Tests

- Ran `python integrator.py`: `total=8 settled=2 rejected=2 review_required=4 error=0`.
- Ran `python -m pytest -p no:cacheprovider`: 50 passed.
- Ran `python scripts/check_coverage_gate.py --fail-under 80` unsandboxed after sandbox coverage-file rename failure: 50 passed, 94.79% total coverage.
- Ran `python scripts/check_coverage_gate.py --fail-under 99` unsandboxed: failed as expected with 94.79% below the demonstration threshold while all tests passed.
- Ran validation-only and MCP status evidence commands with privacy-safe output.

## Homework 6 - Step 35: Clio Review Clarifications

### Added

- Not applicable.

### Changed

- Clarified that Clio documents explicitly selected output runs rather than assuming canonical root files are always the documentation target.
- Reworded Clio selection records to name selected output run IDs and inventory versions instead of agent source versions.

### Fixed

- Fixed ambiguous Clio documentation workflow wording raised during review.

### Tests

- Ran focused markdown scans and whitespace checks for the Clio review update.

## Homework 6 - Step 34: Clio Documentation Generator Controls

### Added

- Added the Clio (Documentation Generator) control package under `agent-control/generate-docs/` with workflow, quality bar, run registry, and README.
- Added Codex and Claude Code `generate-docs` skill surfaces plus the Claude legacy command wrapper.
- Added staged operator-sourced screenshots under `docs/screenshots/operator-sourced/` as preserved source evidence for Clio to select from later.

### Changed

- Updated `agents.md` so future Clio runs load selected Athena, Hephaestus, Themis, MCP, command/hook, prior-homework documentation, and screenshot-source context.
- Clarified that Clio writes candidate documentation and screenshots under run-local `agent-4-docs/outputs/` before canonical selection.

### Fixed

- Not applicable.

### Tests

- Ran baseline `python -m pytest -p no:cacheprovider`: 50 passed.
- Planned validation covers Clio wrapper references, prior-homework author source, no hidden harness dependency in Clio runtime workflow, unresolved draft-marker scans, whitespace checks, and guarded-file diffs.

## Homework 6 - Step 33: Clio Documentation Generator Plan

### Added

- Added the approved planning package for Clio (Documentation Generator) under `docs/work-items/2026-06-20-clio-documentation-generator/`.
- Added a Clio test-case snapshot for shared workflow routing, required documentation scope, author-name sourcing from Homeworks 1-4, screenshot preservation, and privacy-safe evidence handling.

### Changed

- Planned the `generate-docs` control surface so Clio consumes selected Athena, Hephaestus, Themis, MCP, command/hook, screenshot, and prior-homework documentation context before producing final reviewer-facing docs.
- Planned the boundary where Clio reruns and documents selected Themis evidence without silently replacing the selected test suite.

### Fixed

- Not applicable.

### Tests

- Verified prior Homeworks 1-4 README files consistently identify `Igor Tanatarov` as the author source for Clio.
- Validated the draft planning artifacts for unresolved draft markers, whitespace issues, and guarded-file diffs before the planning freeze commit.

## Homework 6 - Step 32: Pipeline Status MCP Server

### Added

- Added the custom FastMCP `pipeline-status` server at `mcp/server.py` with `get_transaction_status`, `list_pipeline_results`, and `pipeline://summary`.
- Added focused MCP tests covering safe transaction status payloads, result listing, summary text, missing results, invalid transaction IDs, and privacy regressions.
- Added a work-item variance log for the local MCP package import bridge and coverage-gate temp directory isolation.

### Changed

- Updated `mcp.json` and `.codex/config.toml` so `pipeline-status` is configured beside the existing `context7` server.
- Changed `scripts/check_coverage_gate.py` to use a PID-scoped temp directory so repeated or tool-driven coverage runs do not collide on stale Windows coverage files.

### Fixed

- Preserved FastMCP access to the installed MCP SDK despite the assignment-required local `mcp/server.py` path.

### Tests

- Confirmed the new MCP tests failed before implementation because the planned helpers did not exist locally.
- Ran `python -m pytest tests/test_mcp_server.py -q`: 9 passed.
- Ran `python -m pytest -p no:cacheprovider`: 50 passed.
- Ran `python scripts/check_coverage_gate.py --fail-under 80` outside the sandbox after sandboxed coverage file-renames were denied: 50 passed with 94.79% total coverage.
- Validated `mcp.json` with `python -m json.tool mcp.json`.
- Validated `.codex/config.toml` with `tomllib`.
- Loaded `mcp/server.py` without starting stdio and confirmed `TXN001` returns `settled` while the serialized output omits raw sample account IDs, raw descriptions, and sensitive field names.
- Verified protected generated files and assignment files have no diff.

## Homework 6 - Step 31: Pipeline Status MCP Plan

### Added

- Added the approved planning package for the Task 4 custom FastMCP `pipeline-status` server under `docs/work-items/2026-06-20-pipeline-status-mcp/`.
- Added a test-case snapshot covering transaction status lookup, pipeline result listing, text summary resource behavior, missing-result handling, invalid transaction IDs, MCP config parsing, and privacy regression checks.

### Changed

- Planned the combined MCP configuration update so `pipeline-status` is added to `mcp.json` and `.codex/config.toml` only after `mcp/server.py` exists.

### Fixed

- Not applicable.

### Tests

- Validated the planning artifacts for placeholder markers.
- Validated the staged planning package with `git diff --cached --check`.
- Verified staged scope contains only the Task 4 planning artifacts and this changelog entry.

## Homework 6 - Step 30: Fast Pipeline Skill Paths

### Changed

- Updated the shared Operator Layer `/run-pipeline` guidance to prefer a single bounded shell invocation that runs the pipeline, verifies result coverage, and extracts safe summary fields.
- Updated the shared `/validate-transactions` guidance to call the current validator dry-run function directly instead of the stale file-path CLI form.
- Synced Codex and Claude Code `run-pipeline` and `validate-transactions` skill wrappers with the fast-path guidance.

### Tests

- Measured the compact pipeline run-and-summary path at about 597 ms.
- Measured the compact validation-only path at about 171 ms and confirmed it reports 8 total, 6 valid, 2 invalid, and safe reason-code groups only.

## Homework 6 - Step 29: Shared Runtime Archive Hardening

### Added

- Added homework-local ignore rules for generated `shared/` runtime evidence while preserving the required protocol directories with `.gitkeep` files.

### Changed

- Changed `shared/` archival to copy the previous runtime tree into `archive/` without moving or deleting the tracked `shared/` directory structure.
- Removed tracked generated `shared/` runtime JSON evidence from the canonical source package.

### Fixed

- Added a stable `SHARED_ARCHIVE_PERMISSION_DENIED` setup reason for Windows/Codex sandbox archive denials.

### Tests

- Added a regression test for archive permission denial without partial archive copy.
- Ran `python -m pytest -p no:cacheprovider`: 41 passed.
- Ran un-escalated `python integrator.py`: `total=8`, `settled=2`, `rejected=2`, `review_required=4`, and `error=0`.

## Homework 6 - Step 28: Fresh Code And Themis Test Selection

### Added

- Added Themis (Test Generator) run `20260620-144025-generate-tests-python-fresh-spec` with run metadata, source context, candidate test inventory, validation checklist, handoff, workspace evidence, and compact command/hook evidence.
- Added `tests/test_themis_quality.py` to the selected canonical test suite for schema, privacy, validation-only, setup-failure, component-failure, and archived-provenance coverage.

### Changed

- Selected Hephaestus (Code Generator) run `20260619-175211-generate-code-python-fresh-spec` as the canonical code package generated from the current Athena (Spec Writer) specification.
- Selected Themis run `20260620-144025-generate-tests-python-fresh-spec` as the canonical test suite for the selected fresh code package.
- Updated `docs/agent-runs/final-selection.md` with the new code and test selection records.

### Fixed

- Repaired the inherited integrator pipeline tests to resolve fixture paths relative to the project root so they work both in the Themis `project-under-test` workspace and after canonical selection.
- Removed stale prior-selected test target `tests/test_pipeline_end_to_end.py` after selecting the fresh package's `tests/test_integrator_pipeline.py`.

### Tests

- Ran the run-local candidate suite with `python -m pytest -p no:cacheprovider`: 40 passed.
- Ran the run-local coverage gate with `python scripts/check_coverage_gate.py --fail-under 80`: 40 passed with 95.10% total coverage.
- Demonstrated the hook blocking path with `python scripts/check_coverage_gate.py --fail-under 99`: expected failure because 95.10% is below 99%, while all 40 tests passed.
- Validated full pipeline support behavior with `total=8`, `settled=2`, `rejected=2`, `review_required=4`, and `error=0`.
- Validated validation-only support behavior with `total=8`, `settled=6`, `rejected=2`, `review_required=0`, and `error=0`.
- Ran post-selection canonical root verification with `python -m pytest -p no:cacheprovider`: 40 passed.
- Ran post-selection canonical root coverage gate with `python scripts/check_coverage_gate.py --fail-under 80`: 40 passed with 95.10% total coverage.

## Homework 6 - Step 27: Themis Test Generator Controls

### Added

- Added the Themis (Test Generator) control package under `agent-control/generate-tests/` with workflow, quality bar, run registry, and README.
- Added a separate `agent-control/operate-pipeline/` package for the one-time `/run-pipeline`, `/validate-transactions`, and coverage gate behavior.
- Added Codex and Claude Code `generate-tests`, `run-pipeline`, and `validate-transactions` skill surfaces plus Claude command wrappers.
- Added `scripts/check_coverage_gate.py`, `.githooks/pre-push`, and `.claude/settings.json` coverage hook surfaces for the 80 percent gate.

### Changed

- Updated `agents.md` so Themis targets named selected Hephaestus versions, uses a run-local workspace, and owns test quality while validating rather than owning outer command/hook tools.
- Clarified that Clio (Documentation Generator) consumes selected Themis outputs for final evidence and documentation instead of silently replacing the selected suite.

### Fixed

- Removed the ambiguity that treated `/run-pipeline`, `/validate-transactions`, and the coverage hook as Themis per-run outputs.

### Tests

- Planned validation covers required file existence, boundary marker scans, unchanged frozen assignment/spec/MCP files, coverage helper pass and demonstration-failure paths, and whitespace checks.

## Homework 6 - Step 26: Themis Test Generator Plan

### Added

- Added the approved planning package for Themis (Test Generator) and pipeline operation support surfaces under `docs/work-items/2026-06-19-themis-test-generator/`.
- Planned a Themis control surface under `agent-control/generate-tests/` for selected-code test generation, run-local execution, candidate preservation, and test-package selection.
- Planned a separate `agent-control/operate-pipeline/` package for the one-time `/run-pipeline`, `/validate-transactions`, and coverage gate support surfaces.

### Changed

- Planned the boundary between Hephaestus baseline tests, Themis selected test suites, and Clio final evidence/documentation ownership.
- Planned Themis run-local workspace handling so generated tests execute under preserved run folders before explicit canonical selection.

### Fixed

- Not applicable.

### Tests

- Validated the finalized planning artifacts for placeholder markers, staged whitespace issues, frozen-file diffs, stale old-support-package references, and staged-file scope before the planning freeze commit.

## Homework 6 - Step 25: Root Pipeline Last-Run Evidence

### Added

- Added root `shared/` last-run evidence so reviewers can inspect the current selected pipeline output now that `shared/` folders are no longer gitignored.

### Changed

- Preserved root runtime evidence separately from the run-local candidate packages and selectable code inventories.

### Fixed

- Not applicable.

### Tests

- Inspected `shared/results/summary.json` for `total_transactions=8`, `settled=2`, `rejected=2`, `review_required=4`, and `error=0`.
- Scanned root `shared/` for raw sample account identifiers, raw sample descriptions, credentials, tokens, and secrets before staging.

## Homework 6 - Step 24: Run-Local Pipeline Generation Controls

### Added

- Added Athena (Spec Writer) quality guidance requiring at least four runtime pipeline components, with Reporting Agent as the default fourth component.
- Added Hephaestus (Code Generator) run-local candidate validation guidance for copied `sample-transactions.json`, local `shared/` evidence, and local `archive/shared-001` repeated-run checks.

### Changed

- Clarified that `transaction-system-brief.md` remains static general-purpose product context while implementation-control details belong in quality and workflow guidance.
- Updated Homework 6 ignore rules so every `archive/` folder and Python/tool cache remains ignored while `shared/` folders are commit-capable last-run evidence.

### Fixed

- Prevented ordinary Hephaestus generate-mode validation from mutating root `shared/` before canonical selection or explicit operator validation.

### Tests

- Planned static validation for unchanged static brief, run-local validation markers, archive-only ignore behavior, unchanged MCP/config/canonical runtime files, and placeholder scans.

## Homework 6 - Step 23: Run-Local Pipeline Generation Plan

### Added

- Added the approved planning package for run-local pipeline generation control updates under `docs/work-items/2026-06-19-run-local-pipeline-generation/`.
- Planned Athena (Spec Writer) quality guidance for at least four runtime pipeline components with Reporting Agent as the default fourth component.
- Planned Hephaestus (Code Generator) run-local validation rules for candidate `sample-transactions.json`, local `shared/` last-run evidence, and local `archive/shared-001` repeated-run checks.

### Changed

- Planned archive-only ignore behavior so `archive/` folders stay gitignored while `shared/` folders remain commit-capable as last-run evidence.
- Preserved `agent-control/write-spec/transaction-system-brief.md` as static general-purpose pipeline context, with implementation details assigned to quality and workflow control files.

### Fixed

- Not applicable.

### Tests

- Validated the draft planning package for placeholder markers.
- Verified `agent-control/write-spec/transaction-system-brief.md` has no diff.
- Updated the plan after review comments clarified that only `archive/` folders, not `shared/` folders, should be gitignored.

## Homework 6 - Step 22: Selected Hephaestus Run Evidence

### Added

- Added run-local `sample-transactions.json` and current `shared/` last-run evidence under selected Hephaestus (Code Generator) run `20260618-223217-generate-code-python-primary`.

### Changed

- Preserved the selected code run's local runtime evidence separately from selectable code inventory and newer candidate-generation evidence.

### Fixed

- Not applicable.

### Tests

- Inspected the run-local `shared/results/summary.json` for the selected code run evidence.

## Homework 6 - Step 21: Fresh Hephaestus Code Candidate

### Added

- Added Hephaestus (Code Generator) run `20260619-175211-generate-code-python-fresh-spec` as a preserved Task 2 candidate generated from the latest canonical Athena (Spec Writer) specification.
- Added a complete candidate package under `agent-2-code/outputs/` with runtime components, integrator, pytest coverage, Context7 notes, and selectable-file inventory.
- Added run metadata, source-context notes, sub-agent strategy, validation checklist, handoff, and comparison evidence for the fresh code candidate.

### Changed

- Reconciled the generated Task 2 candidate with source spec `20260619-170102-write-spec-python-fresh`, including runtime provenance, deterministic protocol filenames, exact sample outcome counts, and no Task 3-5 scope creep.

### Fixed

- Fixed the candidate behavior relative to the prior selected code package by adding `shared/run-provenance.json` support and matching the fresh spec's expected `settled=2`, `rejected=2`, `review_required=4`, and `error=0` sample summary.

### Tests

- Ran candidate `python -m pytest` with 34 passing tests.
- Ran candidate `python -m pytest --cov=. --cov-fail-under=75` with 91.80% total coverage.
- Ran the generated candidate pipeline twice against canonical `sample-transactions.json`, producing `total=8`, `settled=2`, `rejected=2`, `review_required=4`, and `error=0` and archiving repeated runtime output through zero-padded archive folders.
- Verified runtime results, run provenance, and candidate package files do not contain raw sample account IDs or sample descriptions.
- Verified `mcp.json` and `.codex/config.toml` remain unchanged for Task 2 scope.

## Homework 6 - Step 20: Fresh Athena Spec Selection

### Added

- Added a comparison record for Athena (Spec Writer) run `20260619-170102-write-spec-python-fresh` against the prior two generated specs.
- Added supersession notes to the prior Athena comparison records so the last-three selection history is traceable.

### Changed

- Selected `20260619-170102-write-spec-python-fresh` as the canonical Python transaction-processing system specification.
- Copied the selected run's `agent-1-spec/outputs/specification.md` to canonical `specification.md`.
- Updated `docs/agent-runs/final-selection.md` to record the fresh spec selection and the traceability caveat that the existing selected Hephaestus (Code Generator) package was generated from the prior spec.

### Fixed

- Superseded the earlier selected spec with one that includes the current repeated-run archival and `shared/run-provenance.json` product contract.

### Tests

- Compared the last three generated Athena (Spec Writer) specs against completeness, research provenance, Python stack specificity, privacy/audit handling, product-boundary leakage, task-card executability, archival/provenance requirements, and handoff usefulness.
- Verified the fresh spec contains no plaintext sample account IDs, sample transaction descriptions, harness/Superpowers leakage, canonical-copy product tasks, screenshot/PR packaging product tasks, or Homework Automation Layer agent task cards.
- Verified canonical `specification.md` is byte-for-byte content-equivalent to the selected fresh run output after copy.

## Homework 6 - Step 19: Runtime Provenance Spec Contract

### Added

- Added an Athena (Spec Writer) product-spec requirement for `shared/run-provenance.json` so each current or archived runtime run can identify its source Athena specification and selected Hephaestus pipeline version.
- Added Hephaestus (Code Generator) guidance allowing generated product code to write that runtime provenance file when the selected spec requires it, without implementing operator-layer selection workflows.

### Changed

- Clarified that runtime provenance belongs in the generated transaction-system specification, not as an ad hoc direct patch to the current integrator.
- Ignored Homework 6 runtime `shared/` and `archive/` folders at the repository level so transaction-run output remains local evidence unless deliberately preserved in run artifacts.

### Fixed

- Not applicable.

### Tests

- Reverted the interrupted direct-code provenance attempt and verified no `integrator.py` or pipeline test diff remains for that path.
- Planned static validation of Athena and Hephaestus control surfaces for `run-provenance.json` and runtime output ignore rules.

## Homework 6 - Step 18: Code Run Preservation Repair

### Added

- Added the selected Hephaestus (Code Generator) output package under `agent-2-code/outputs/` with an inventory of selectable code, tests, research notes, canonical targets, and SHA-256 fingerprints.
- Added selected-code traceability from Hephaestus run `20260618-223217-generate-code-python-primary` back to Athena run `20260618-003908-write-spec-python-replacement`.
- Added integration coverage for repeated pipeline runs archiving prior `shared/` output to zero-padded `archive/shared-001` style folders.
- Added `pytest.ini` so preserved run-local evidence tests under `docs/agent-runs/` do not collide with canonical tests during default pytest discovery.

### Changed

- Updated Hephaestus control docs so future code-generation runs produce run-local outputs first, then copy to canonical paths only through first-success or explicit selection.
- Updated Athena control docs so future specs require product-level repeated-run archival behavior without leaking operator-layer selection mechanics.
- Added future Themis traceability guidance so generated tests must name the selected Hephaestus software version they target.

### Fixed

- Repaired `integrator.py` so each pipeline run archives an existing `shared/` tree before creating a fresh protocol structure.
- Repaired current run evidence so generated code is preserved in the run folder rather than existing only at canonical paths.

### Tests

- Watched `python -m pytest tests/test_pipeline_end_to_end.py -v` fail for missing archive folders, then pass after the archive implementation.
- Ran `python -m pytest --cov=. --cov-fail-under=75` with 25 passing tests and 92.14% total coverage.
- Ran `python integrator.py` twice with normal filesystem permissions, producing fresh current `shared/` output and zero-padded archived prior runs.

## Homework 6 - Step 17: Code Run Preservation Repair Plan

### Added

- Added the approved planning package for repairing Hephaestus (Code Generator) run-local output preservation, selected-code traceability, and repeatable pipeline run archival.
- Added a test-case snapshot for zero-padded runtime archive folders such as `archive/shared-001` and for code/test generation provenance checks.

### Changed

- Planned a source trace from the selected Athena (Spec Writer) run to each Hephaestus code version using run IDs and stable content fingerprints.
- Planned future Themis (Test Generator) traceability so generated tests name the selected Hephaestus software version they target.

### Fixed

- Planned repair of the current first Hephaestus run folder so the generated code package exists under the run evidence before canonical selection.

### Tests

- Validated the draft planning artifacts for required sections, placeholder markers, staged-file scope, zero-padded archive naming, and source-spec/software-version traceability before the planning freeze commit.

## Homework 6 - Step 16: Hephaestus Code Generation

### Added

- Added the Python transaction-processing pipeline generated from the selected Athena (Spec Writer) specification.
- Added `integrator.py`, runtime pipeline components, shared Decimal/redaction/JSON utilities, and focused pytest coverage.
- Added canonical Context7-backed `research-notes.md` plus preserved Hephaestus run evidence under `docs/agent-runs/20260618-223217-generate-code-python-primary/`.
- Added generated JSON protocol and result evidence under `shared/input/`, `shared/processing/`, `shared/output/`, and `shared/results/`.

### Changed

- Established stable result shapes for later read-only status tooling without adding the Task 4 MCP server or changing MCP configuration.

### Fixed

- Not applicable.

### Tests

- Ran `python -m pytest --cov=. --cov-fail-under=75` with 24 passing tests and 92.03% total coverage.
- Ran `python integrator.py`, producing `total=8`, `settled=3`, `rejected=2`, `review_required=3`, and `error=0`.
- Verified generated shared files do not contain raw sample account IDs or raw transaction descriptions.
- Verified `mcp.json` and `.codex/config.toml` remain unchanged for Task 2 scope.

## Homework 6 - Step 15: Hephaestus Control Surface

### Added

- Added the tool-neutral Hephaestus (Code Generator) control package under `agent-control/generate-code/`.
- Added Codex and Claude Code `generate-code` skill entrypoints for Task 2 code-generation runs.
- Added Hephaestus workflow, quality-bar, and run-registry guidance for Context7 research notes, run preservation, validation, and sub-agent evidence.

### Changed

- Updated `agents.md` so future Hephaestus runs discover the selected `specification.md`, `generate-code` entrypoints, Context7 requirement, and executor sub-agent autonomy up to `agents.max_threads = 8`.

### Fixed

- Not applicable.

### Tests

- Validated branch, expected changed files, untouched `TASKS.md`, untouched `specification.md`, unchanged Context7 MCP config, generate-code package paths, wrapper references, Context7/sub-agent markers, scope rejection markers, unresolved-template scans, and whitespace checks.

## Homework 6 - Step 14: Hephaestus Planning Package

### Added

- Added the approved harness planning package for Hephaestus (Code Generator) under `docs/work-items/2026-06-18-hephaestus-code-generator/`.
- Planned the `generate-code` control surface, tool-neutral workflow package, Codex and Claude Code entrypoints, run preservation, and Context7-backed `research-notes.md` requirements.

### Changed

- Authorized future Hephaestus (Code Generator) executor sub-agent use up to Homework 6's configured `agents.max_threads = 8` cap without additional operator approval.
- Scoped the future Hephaestus run to Task 2 generated transaction-system code only, with Task 3 hooks/commands, Task 4 custom MCP setup, Task 5 docs/screenshots, and PR packaging explicitly out of scope.

### Fixed

- Not applicable.

### Tests

- Validated the planning artifacts for placeholder markers, whitespace issues, staged file scope, frozen-file diffs, Context7 requirements, and sub-agent autonomy language before the planning freeze commit.

## Homework 6 - Step 13: Replacement Athena Spec Selection

### Added

- Added the replacement Athena (Spec Writer) run comparison record under `docs/agent-runs/20260618-003908-write-spec-python-replacement/comparison.md`.

### Changed

- Selected `20260618-003908-write-spec-python-replacement` as the canonical Python transaction-processing system specification.
- Copied the selected run's `agent-1-spec/outputs/specification.md` to canonical `specification.md`.
- Updated `docs/agent-runs/final-selection.md` so downstream Hephaestus (Code Generator) work uses the replacement transaction-system spec instead of the failed/superseded prior selection.

### Fixed

- Replaced the previously failed canonical spec that targeted the homework automation/control layer with a product-only Generated Transaction System Layer specification.

### Tests

- Verified canonical `specification.md` is byte-for-byte content-equivalent to the selected run output.
- Verified required Task 1 sections are present in canonical `specification.md`.
- Checked product-boundary leakage terms; remaining matches are negated non-product scope statements or privacy reminders, not transaction-system implementation tasks.
- Reviewed git status and confirmed the change set is limited to canonical spec selection, final-selection metadata, the preserved replacement run folder, and this changelog entry.

## Homework 6 - Step 12: Athena Control Surface Repair

### Added

- Added `agent-control/write-spec/transaction-system-brief.md` as Athena (Spec Writer)'s direct transaction-processing system input.
- Added layer glossary and Homework Automation Layer identity labels for Athena, Hephaestus, Themis, and Clio.

### Changed

- Updated Athena (Spec Writer) workflow, stack profile, quality bar, and entrypoint wrappers so generated specs target the transaction-processing system instead of the homework automation harness.
- Clarified that Homework Automation Layer agents do not use `dev-doc-harness` or Superpowers freeze gates.
- Clarified that generated transaction-system runtime components include runtime application agents, and set Athena (Spec Writer)'s temporary coverage target to 75% while deferring the >80% gate to Themis (Test Generator).
- Clarified that runtime transaction pipeline agents are stack-native application components, not Claude/Codex skills.

### Fixed

- Marked the first selected Athena (Spec Writer) run as failed/superseded for wrong target while preserving the run folder and canonical failed spec as evidence.
- Replaced the low-level task standard with implementation-ready transaction-system slices and added a meta-layer leakage rejection gate.

### Tests

- Validated the branch, expected dirty files, untouched `TASKS.md`, untouched `specification.md`, required control-surface markers, failed-selection markers, and whitespace with the frozen plan's static checks.

## Homework 6 - Step 11: Athena Spec Layering Plan

### Added

- Added the approved harness planning package for repairing Athena (Spec Writer) so it targets the transaction-processing system specification rather than the homework automation harness.
- Planned the transaction-system brief, layer glossary, Greek identity labels, meta-leakage gates, and failed-spec marking.

### Changed

- Scoped the plan to Athena control-surface repair only; replacement spec generation and selection are deferred to a later clean Homework 6-root thread.

### Fixed

- Planned correction of the low-level task standard so Athena produces implementation-ready transaction-system slices instead of one card per Homework Automation Layer agent.

### Tests

- Validated the planning artifacts for required sections, placeholder markers, out-of-scope replacement-run steps, staged file scope, and whitespace issues before the planning freeze commit.

## Homework 6 - Step 10: Agent 1 Specification Run

### Added

- Preserved the first Python `write-spec` generation run under `docs/agent-runs/20260617-180458-write-spec-python-primary/`.
- Added the selected canonical `specification.md` for Homework 6 Task 1.

### Changed

- Recorded first-run auto-selection in `docs/agent-runs/final-selection.md`.
- Captured domain research, objective design, low-level task decomposition, final review, repair review, validation, and completion handoff for Agent 1.

### Fixed

- Repaired the generated `/validate-transactions` requirement so it validates `sample-transactions.json` through validator dry-run behavior instead of running tests or coverage as its primary command.

### Tests

- Validated the generated specification against the Task 1 section requirements, Python stack profile, privacy/audit rules, JSON file protocol, dry-run validation command requirement, and `write-spec` quality bar.
- Ran placeholder and whitespace scans for the preserved run artifacts.

## Homework 6 - Step 9: Agent 1 Specification Run Plan

### Added

- Added the approved Phase 02 harness plan for the Python `write-spec` generation and first-run auto-selection workflow.

### Changed

- Recorded the required sub-agent waves, validation checks, selection boundary, and canonical-copy rules before running Agent 1.

### Fixed

- Not applicable.

### Tests

- Validated the Phase 02 plan for placeholder markers and whitespace issues before the planning freeze commit.

## Homework 6 - Step 8: Skill Metadata Cleanup

### Changed

- Updated the Codex `write-spec` UI metadata default prompt to explicitly invoke `$write-spec`.

### Removed

- Removed the empty `write-spec/references/` skill directory now that the shared reference package lives under `agent-control/write-spec/`.

### Tests

- Verified the empty references directory is absent, the Codex skill validates, `openai.yaml` parses with a `$write-spec` default prompt, and the working-tree whitespace check is clean.

## Homework 6 - Step 7: Claude Skill Reference Cleanup

### Added

- Documented the official Claude Code project-skill precedence rationale in the shared Agent 1 write-spec package and variance log.

### Changed

- Updated stale planning and validation references from the removed `.claude/commands/write-spec.md` wrapper to `.claude/skills/write-spec/SKILL.md`.

### Removed

- Removed the empty `.claude/commands/` directory to avoid implying that a legacy command wrapper still exists.

### Fixed

- Fixed trailing whitespace in the Homework 6 task file so diff whitespace checks are clean.

### Tests

- Validated Codex skill metadata, MCP JSON, Codex TOML, working-tree whitespace checks against `HEAD` and `main`, placeholder scan, stale active-path scans, and empty command-directory removal.

## Homework 6 - Step 6: Tool-Neutral Write-Spec Package

### Added

- Added `agent-control/write-spec/` as the canonical shared Agent 1 reference package.
- Added a Claude Code native `write-spec` project skill with Claude-oriented frontmatter and slash-command examples.

### Changed

- Updated the Codex `write-spec` skill, Agent 1 workflow references, run registry, and standing agent guide to point at the tool-neutral package.
- Moved run-registry rules into the shared package so `docs/agent-runs/README.md` can stay a thin pointer.

### Removed

- Removed the legacy `.claude/commands/write-spec.md` wrapper with no transition period.

### Tests

- Validated skill metadata, reference paths, active stale path scan, MCP JSON, Codex TOML, placeholder markers, and whitespace checks.

## Homework 6 - Step 5: Post-Review Write-Spec Tightening

### Added

- Added a variance log for the intentional removal of fallback generation behavior from the `write-spec` wrappers.
- Documented the missing original `specification-TEMPLATE-hint.md` caveat in the Agent 1 run registry workflow.

### Changed

- Thinned the Codex skill and Claude Code slash-command wrappers so the shared workflow, stack profiles, and quality bar are mandatory.
- Updated the run registry to reference shared workflow files instead of restating the Agent 1 layout and comparison criteria.
- Replaced standing exact-model labels with policy-relative Codex and Claude Code model guidance.

### Fixed

- Removed the stale Claude command `outputs/agents.md` run-output target.
- Clarified repo-root versus homework-root path resolution for `write-spec` runs.

### Tests

- Validated skill metadata, MCP JSON, Codex TOML, placeholder scan, stack-selection markers, stale run-output/model-name searches, and whitespace checks.

## Homework 6 - Step 4: Write-Spec Selection and Model Controls

### Added

- Added first-run auto-selection rules for successful Agent 1 `write-spec generate` runs when no canonical `specification.md` exists yet.
- Added explicit Codex and Claude Code model-family and reasoning prescriptions for Agent 1 orchestration, sub-agents, and final review.

### Changed

- Clarified that `specification.md` is the default selectable package and supporting docs require explicit selection before canonical copy.
- Clarified that `homework-6/agents.md` is the stable homework-level guide required by Task 1, not a per-run generated output to overwrite.

### Fixed

- Removed stale run-registry copy targets that could cause a generated run to overwrite the standing agent guide.

### Tests

- Validated skill metadata, Codex TOML, first-run selection markers, model-prescription markers, stale `outputs/agents.md` targets, placeholder scan, and whitespace checks.

## Homework 6 - Step 3: Write-Spec Sub-Agent Workflow

### Added

- Added mandatory Agent 1 sub-agent planning, domain research, objectives, low-level task, review, and planned handoff requirements to the shared `write-spec` workflow.
- Added `agents.max_threads = 8` to the Homework 6 Codex project configuration for multi-agent spec-generation runs.

### Changed

- Replaced ad-hoc research and emergency-only handoff guidance with planned sub-agent phases and explicit handoff artifacts.

### Fixed

- Prevented Agent 1 from skipping sub-agents merely because a later operator prompt does not repeat the requirement.

### Tests

- Validated skill metadata, Codex TOML, sub-agent markers, placeholder scan, stack-selection markers, and whitespace checks.

## Homework 6 - Step 2: Canonical Write-Spec Workflow

### Added

- Added `write-spec-workflow.md` as the canonical shared workflow for both the Codex Markdown skill and Claude Code slash command.

### Changed

- Reduced the Codex `write-spec` skill and Claude `/write-spec` command to thin wrappers around the shared workflow, stack profile, and quality-bar references.

### Fixed

- Removed duplicated workflow instructions from the two command surfaces to reduce drift before running Agent 1.

### Tests

- Validated skill metadata, wrapper references, placeholder scan, stack-selection markers, and whitespace checks.

## Homework 6 - Step 1: Agent 1 Control Surfaces

### Added

- Added the Homework 6 agent guide, dual `write-spec` control surfaces, stack profile reference, run registry, and Context7 project configuration.

### Changed

- Established the Task 1 workflow for generating and selecting stack-specific specification runs before downstream implementation.

### Fixed

- Not applicable.

### Tests

- Validated skill metadata, MCP JSON, Codex TOML, stack-selection markers, placeholder scan, and cross-surface command references.

## Homework 6 - Step 0: Agent 1 Planning Package

### Added

- Added the approved harness planning package for the Homework 6 Agent 1 `write-spec` control surfaces.
- Planned dual Claude Code slash-command and Codex Markdown skill support, stack-specific spec generation, run preservation, and Context7/MCP strategy.

### Changed

- Established Python as the default `write-spec` stack input and Java as the fixed alternate profile for optional comparison runs.

### Fixed

- Not applicable.

### Tests

- Validated the approved planning artifacts for placeholder markers and staged whitespace issues before the planning freeze commit.

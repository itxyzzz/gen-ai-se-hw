# Final Selection

Current status: selected Athena (Spec Writer) run `20260619-170102-write-spec-python-fresh` is the canonical Python transaction-processing system specification. Selected Hephaestus (Code Generator) run `20260619-175211-generate-code-python-fresh-spec` is the canonical Task 2 software version generated from that fresh specification. Selected Themis (Test Generator) run `20260620-144025-generate-tests-python-fresh-spec` is the canonical Task 5 test suite for that selected code package. Selected Clio (Documentation Generator) run `20260621-011348-generate-docs-python-review-repair` is the canonical Task 5 reviewer documentation package for the selected spec/code/test set.

Use the current canonical `specification.md` as downstream input for any future Hephaestus (Code Generator) regeneration or repair. The prior selected run `20260617-180458-write-spec-python-primary` remains preserved as failed/superseded evidence only.

## Machine-Readable Selection Sets

`docs/agent-runs/selection-sets.json` is the machine-readable package-set registry for Operator Layer helpers. This markdown file remains the human audit history and rationale record.

Current canonical set ID: `python-canonical-20260621`.

The registry records the current Python set with selected Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator) run IDs, selected inventory paths, package root, and command hints for pipeline execution, validation-only behavior, tests, and coverage. It intentionally does not store raw transaction records, raw account IDs, descriptions, credentials, hidden prompts, or environment dumps.

No Java package set is selected or canonical after Phase 01. Future Java alternates should be added as `alternate` or `candidate` sets until an explicit operator selection changes canonical status.

After Phase 02, future cross-set orchestration, comparison, and explicit selection are owned by Hera (Orchestrator) through `agent-control/orchestrate-runs/`. The current Python canonical set remains unchanged. Hera selection proposals are recorded in preserved run-local `agent-5-orchestrator/selection-plan.md` files until the operator explicitly authorizes inventory-driven selection and the corresponding updates to this file and `selection-sets.json`.

## Selection Criteria

| Criterion | Required evidence |
|---|---|
| Completeness | Required Task 1 files and sections exist. |
| Stack specificity | Selected stack is `python` or `java`, and generated files/functions/commands match it. |
| Research provenance | Context7, web, local-source, or fallback notes are recorded. |
| Privacy and audit | Decimal money, ISO 4217-style currency, audit logging, and no plaintext PII logging are addressed. |
| Downstream readiness | Low-level task cards are executable by Agents 2-4. |
| Handoff continuity | Run metadata, validation checklist, and handoff are sufficient for a fresh thread. |

## Selection History

| Date | Run ID | Stack | Selected files | Rationale | Operator |
|---|---|---|---|---|---|
| 2026-06-19 | `20260619-170102-write-spec-python-fresh` | `python` | `agent-1-spec/outputs/specification.md` copied to `specification.md` | Explicit operator request to compare the last three generated Athena (Spec Writer) specs and select the best. This fresh run passed validation and final-review repair, preserves product-only Python task cards, and adds the current repeated-run archival plus `shared/run-provenance.json` product contract missing from the previous selected spec. | Repository operator in current Codex Desktop thread |
| 2026-06-18 | `20260618-003908-write-spec-python-replacement` | `python` | `agent-1-spec/outputs/specification.md` copied to `specification.md` | Explicit operator selection of replacement Athena (Spec Writer) run. The run passed validation, repaired final-review blockers, targets only the Generated Transaction System Layer, and provides implementation-ready Python task cards for downstream agents. | Repository operator in current Codex Desktop thread |
| 2026-06-17 | `20260617-180458-write-spec-python-primary` | `python` | `agent-1-spec/outputs/specification.md` copied to `specification.md` | First successful Python `write-spec generate` run; canonical `specification.md` was absent; final review blocker was repaired and follow-up review found no remaining blocking issues. | Repository operator in current Codex Desktop thread |

## Hephaestus Code Generation Selection

Current selected code run: `20260619-175211-generate-code-python-fresh-spec`.

Source specification:

- Athena source run ID: `20260619-170102-write-spec-python-fresh`
- Canonical source path: `homework-6/specification.md`
- Source SHA-256: `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B`
- Traceability note: this selected Hephaestus package targets the current canonical Athena specification.

Selected output inventory:

- `docs/agent-runs/20260619-175211-generate-code-python-fresh-spec/agent-2-code/outputs/inventory.md`

Selected canonical paths:

- `integrator.py`
- `pytest.ini`
- `agents/__init__.py`
- `agents/common.py`
- `agents/transaction_validator.py`
- `agents/fraud_detector.py`
- `agents/settlement_processor.py`
- `tests/test_common.py`
- `tests/test_transaction_validator.py`
- `tests/test_fraud_detector.py`
- `tests/test_settlement_processor.py`
- `tests/test_integrator_pipeline.py`
- `research-notes.md`

Selection rationale: explicit operator request on 2026-06-20 to test the latest generated code version, not the current canonical code, and if successful select both that code version and the test suite. The fresh-spec Hephaestus package passed its own validation and the Themis suite below passed with 40 tests and 95.10% coverage.

Excluded from code selection:

- `shared/`
- `archive/`
- `.coverage`
- `__pycache__/`
- `.pytest_cache/`

For later Hephaestus selections, remove the canonical targets declared by the prior selected inventory before copying the replacement package. Do not remove hand-maintained files outside the selected inventory.

## Themis Test Generation Selection

Current selected test run: `20260620-144025-generate-tests-python-fresh-spec`.

Targeted selected code package:

- Hephaestus run ID: `20260619-175211-generate-code-python-fresh-spec`
- Hephaestus inventory: `docs/agent-runs/20260619-175211-generate-code-python-fresh-spec/agent-2-code/outputs/inventory.md`
- Source Athena run ID: `20260619-170102-write-spec-python-fresh`
- Source/current spec SHA-256: `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B`

Selected output inventory:

- `docs/agent-runs/20260620-144025-generate-tests-python-fresh-spec/agent-3-tests/outputs/inventory.md`

Selected canonical paths:

- `pytest.ini`
- `tests/test_common.py`
- `tests/test_transaction_validator.py`
- `tests/test_fraud_detector.py`
- `tests/test_settlement_processor.py`
- `tests/test_integrator_pipeline.py`
- `tests/test_themis_quality.py`

Removed prior selected test target:

- `tests/test_pipeline_end_to_end.py`

Validation:

- `python -m pytest -p no:cacheprovider` from the run-local project-under-test passed with 40 tests.
- `python scripts/check_coverage_gate.py --fail-under 80` from the run-local project-under-test passed with 95.10% total coverage.
- `python scripts/check_coverage_gate.py --fail-under 99` failed as expected with 95.10% below the demonstration threshold while tests still passed.
- Full pipeline support run passed with `total=8 settled=2 rejected=2 review_required=4 error=0`.
- Validation-only support run passed with `total=8 settled=6 rejected=2 review_required=0 error=0`.
- Post-selection canonical root `python -m pytest -p no:cacheprovider` passed with 40 tests.
- Post-selection canonical root `python scripts/check_coverage_gate.py --fail-under 80` passed unsandboxed with 40 tests and 95.10% total coverage.

Selection rationale: explicit operator request to select the test suite after successful generation and validation. The suite adds Themis-owned schema, privacy, validation-only, setup-failure, component-failure, and archived-provenance coverage on top of the fresh Hephaestus baseline tests.

Excluded from test selection:

- `agent-3-tests/workspace/`
- `agent-3-tests/evidence/`
- `.test-tmp/`
- `tmp/`
- `.coverage*`
- `.pytest_cache/`
- `__pycache__/`
- copied validation support inputs such as `sample-transactions.json`, `specification.md`, and `scripts/check_coverage_gate.py`

## Clio Documentation Generation Selection

Current selected documentation run: `20260621-011348-generate-docs-python-review-repair`.

Targeted selected source packages:

- Athena run ID: `20260619-170102-write-spec-python-fresh`
- Hephaestus run ID: `20260619-175211-generate-code-python-fresh-spec`
- Hephaestus inventory: `docs/agent-runs/20260619-175211-generate-code-python-fresh-spec/agent-2-code/outputs/inventory.md`
- Themis run ID: `20260620-144025-generate-tests-python-fresh-spec`
- Themis inventory: `docs/agent-runs/20260620-144025-generate-tests-python-fresh-spec/agent-3-tests/outputs/inventory.md`
- Current canonical spec SHA-256: `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B`

Selected output inventory:

- `docs/agent-runs/20260621-011348-generate-docs-python-review-repair/agent-4-docs/outputs/inventory.md`

Selected canonical paths:

- `README.md`
- `HOWTORUN.md`
- `ARCHITECTURE.md`
- `TESTING_GUIDE.md`
- `API_REFERENCE.md`
- `docs/pr-description-draft.md`
- `docs/screenshots/pipeline-run.png`
- `docs/screenshots/test-coverage.png`
- `docs/screenshots/skill-run-pipeline.png`
- `docs/screenshots/hook-trigger.png`
- `docs/screenshots/mcp-interaction.png`

Screenshot source-to-target mapping:

- Fresh terminal-style evidence -> `docs/screenshots/pipeline-run.png`
- Fresh terminal-style passing 80 percent coverage evidence -> `docs/screenshots/test-coverage.png`
- `docs/screenshots/operator-sourced/080-run-pipeline.png` -> `docs/screenshots/skill-run-pipeline.png`
- `docs/screenshots/operator-sourced/100-pre-push-git-hook-firing.png` -> `docs/screenshots/hook-trigger.png`
- Fresh terminal-style combined Context7 and custom `pipeline-status` evidence -> `docs/screenshots/mcp-interaction.png`

Validation:

- `python integrator.py` passed with `total=8 settled=2 rejected=2 review_required=4 error=0`.
- `python -m pytest -p no:cacheprovider` passed with 50 tests.
- `python scripts/check_coverage_gate.py --fail-under 80` passed unsandboxed with 50 tests and 94.79% total coverage after a Windows sandbox coverage-file rename failure.
- `python scripts/check_coverage_gate.py --fail-under 99` failed as expected with 94.79% below the demonstration threshold while all tests passed.
- Validation-only helper returned 8 total, 6 valid, and 2 rejected records.
- MCP status helper returned safe summary/status evidence using a file-path import of `mcp/server.py`.

Selection rationale: explicit operator request to regenerate documentation after Clio instructions were updated and, if successful, select the package as canonical. This run repairs the prior screenshot mapping by using distinct stable evidence for direct pipeline execution, passing 80 percent coverage, `/run-pipeline`, hook blocking behavior, and combined Context7 plus custom `pipeline-status` MCP evidence.

Operator: Repository operator in current Codex Desktop thread.

Excluded from documentation selection:

- `agent-4-docs/evidence/`
- `agent-4-docs/review/`
- `agent-4-docs/validation-checklist.md`
- `agent-4-docs/handoff.md`
- `inputs/`
- `shared/`
- `archive/`
- `.coverage*`
- `.pytest_cache/`
- `.test-tmp/`
- `tmp/`
- `__pycache__/`

## Failure And Supersession Notes

| Date | Run ID | Status | Reason | Follow-up |
|---|---|---|---|---|
| 2026-06-17 | `20260617-180458-write-spec-python-primary` | Failed / superseded | Wrong target: the selected specification described the homework automation harness and spec-generation workflow in its low-level tasks instead of specifying only the transaction-processing system. | Preserve this run and canonical copy as failed evidence. Replacement generation and selection must happen later in a separate clean Homework 6-root thread after the Athena (Spec Writer) control surface is repaired. |

## Selected Canonical Paths

- `specification.md`

## Post-Selection Edits

- 2026-06-21: Selected refreshed Clio (Documentation Generator) run `20260621-011348-generate-docs-python-review-repair` and copied its inventory-declared README, HOWTORUN, architecture, testing, API, PR draft, and stable screenshot targets to canonical paths. The refreshed mapping replaces the previous duplicate pipeline/skill screenshot use and replaces the prior fail-under-99 image as the passing coverage screenshot.
- 2026-06-20: Repaired `docs/pr-description-draft.md` screenshot links to resolve relative to the draft file (`screenshots/*.png` instead of `docs/screenshots/*.png`) and updated the preserved Clio output inventory fingerprint.
- 2026-06-20: Reworded a `HOWTORUN.md` privacy evidence note from an internal instruction style into reviewer-facing guidance after review feedback. The selected Clio inventory snapshot remains preserved as generated evidence.
- 2026-06-20: Selected Clio (Documentation Generator) run `20260620-230201-generate-docs-python-primary` as the first successful documentation package and copied its inventory-declared README, HOWTORUN, architecture, testing, API, PR draft, and stable screenshot targets to canonical paths.
- 2026-06-20: Selected Hephaestus (Code Generator) run `20260619-175211-generate-code-python-fresh-spec` and copied its inventory-declared runtime code, baseline tests, pytest config, and research notes to canonical targets. Selected Themis (Test Generator) run `20260620-144025-generate-tests-python-fresh-spec` and copied its inventory-declared test suite and pytest config to canonical targets.
- 2026-06-19: Compared the last three generated Athena (Spec Writer) specs, selected `20260619-170102-write-spec-python-fresh`, and copied its `agent-1-spec/outputs/specification.md` to canonical `specification.md`. No support docs were copied because the operator selected the default spec package only.
- 2026-06-18: Copied `docs/agent-runs/20260618-003908-write-spec-python-replacement/agent-1-spec/outputs/specification.md` to canonical `specification.md`. No support docs were copied because the operator selected the default spec package only.
- 2026-06-17: No edits were made to canonical `specification.md`, but the selected output was marked failed/superseded for wrong target in this file. The preserved canonical file remains evidence only until a replacement transaction-processing system spec is selected.

## Future Copy Targets

For the first successful Agent 1 generation run, if `homework-6/specification.md` does not exist, copy:

- `homework-6/specification.md`

For later selections, copy `homework-6/specification.md` only by default. Copy the following support docs only when the operator explicitly selects them:

- `homework-6/docs/domain-rules.md`
- `homework-6/docs/technical-conventions.md`
- `homework-6/docs/development-process.md`
- `homework-6/research-notes.md` when selected as canonical research evidence

Do not copy any run-local agent guide over `homework-6/agents.md`. That file is the stable homework-level agent guide and is updated separately when its standing instructions change.

Record any post-selection edits in this file and in `homework-6/CHANGELOG.md`.

# Run-Local Pipeline Generation Spec

Work ID: `2026-06-19-run-local-pipeline-generation`
Short ID: `run-local-pipeline-generation`
Status: Draft
Harness release: unknown
Schema: `schema:spec.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `rule:lifecycle.documentation-matrix`, `rule:quality.spec-handoff`

## Goal

Future Athena (Spec Writer) and Hephaestus (Code Generator) runs should generate a four-component transaction pipeline and validate code candidates entirely inside their preserved run folder before any canonical/root execution.

## Scope

- Update Athena (Spec Writer) control guidance so generated specifications require at least four cooperating runtime transaction pipeline components, with Reporting Agent as the default fourth component.
- Update Athena quality guidance so generated specifications describe run-local candidate execution expectations without changing `agent-control/write-spec/transaction-system-brief.md`.
- Update Hephaestus (Code Generator) workflow and quality guidance so candidate packages copy `sample-transactions.json` into `agent-2-code/outputs/`, run tests from that folder, run the pipeline from that folder, and validate repeated-run archival using local `shared/` and local `archive/`.
- Update ignore rules so every Homework 6 `archive/` folder remains local historical runtime evidence and is not accidentally committed, while every `shared/` folder remains commit-capable as last-run evidence.
- Preserve existing outer-layer boundaries: no Task 3 commands/hooks, Task 4 custom MCP config, Task 5 docs/screenshots, or canonical package selection behavior in this work.

## Non-scope

- Do not modify `agent-control/write-spec/transaction-system-brief.md`; it remains the stable general-purpose product brief.
- Do not regenerate or select a new Athena `specification.md`.
- Do not regenerate, select, or copy a Hephaestus code package.
- Do not implement Reporting Agent in canonical runtime code during this work item.
- Do not add `mcp/server.py`, `pipeline-status` MCP config, slash commands, hooks, README, HOWTORUN, screenshots, or PR packaging.
- Do not remove existing preserved `shared/` or `archive/` runtime evidence.

## Current state

The latest Hephaestus candidate run `20260619-175211-generate-code-python-fresh-spec` exposed two validation-isolation issues:

- The root smoke command used root-level `shared/` and hit Windows sandbox permissions while moving an existing root runtime file into `archive/shared-007`.
- Pytest and coverage initially used host temp/cache locations, which caused additional Windows permission errors and produced cleanup churn for `.coverage`, `.pytest_cache`, and `__pycache__`.

The current Athena quality bar already requires repeated runtime archival and test seams, but it does not say candidate validation must be fully run-local. The Hephaestus workflow currently says `agent-2-code/outputs/` must exclude runtime folders, but it also instructs validation to run `python integrator.py` from `homework-6`, which encourages root-level `shared/` mutation before candidate selection.

The root `.gitignore` currently ignores:

```text
homework-6/shared/
homework-6/archive/
```

This is too broad for `shared/`: the root `shared/` tree is the most important last-run evidence for reviewers, and run-local `shared/` trees may also be useful preserved evidence. `archive/` should remain ignored because it is historical runtime output that can grow quickly and is superseded by the current `shared/` evidence plus validation notes.

The generated transaction system currently uses three runtime components: Transaction Validator, Fraud Detector, and Settlement Processor. The assignment requires at least three components. The next generated specification should intentionally target at least four components for stronger assignment coverage, with Reporting Agent as the preferred fourth component because it improves summaries, result status files, privacy checks, and future MCP readability without introducing real compliance claims.

## Proposed behavior

After implementation:

- Athena-generated specifications require at least four cooperating runtime transaction pipeline components.
- Reporting Agent is the default fourth component unless the operator explicitly chooses another product component.
- Reporting Agent owns audit-safe aggregate outputs such as `summary.json`, `pipeline-status.json`, optional sanitized report/audit files, final status vocabulary checks, and result completeness checks.
- Athena quality guidance distinguishes general product purpose from implementation-control details: `transaction-system-brief.md` stays static, while `quality-bar.md` carries detailed quality requirements for generated specifications.
- Hephaestus candidate packages are self-contained enough for validation:
  - `agent-2-code/outputs/sample-transactions.json` exists.
  - Tests run from `agent-2-code/outputs/`.
  - Pipeline smoke runs use local `shared/` and local `archive/`.
  - The second local smoke run archives `shared/` to `archive/shared-001`.
  - Local `shared/` is preserved as commit-capable last-run evidence, while remaining excluded from selectable code inventory.
- Validation commands use workspace-local temp/cache controls where practical, such as `--basetemp .test-tmp`, `PYTHONDONTWRITEBYTECODE=1`, and cleanup of `.test-tmp`, `.coverage*`, `.pytest_cache`, and `__pycache__`.
- Root-level smoke runs are reserved for canonical selection or an explicit operator validation step, not for ordinary candidate validation.
- All Homework 6 `archive/` folders are ignored whether they live at the homework root or under a preserved candidate output package.
- All Homework 6 `shared/` folders are not gitignored by default and may be committed as current last-run evidence, especially the root `shared/` tree used for final reviewer-visible results.

## Interfaces and data

Affected control files:

- `agent-control/write-spec/quality-bar.md`
- `agent-control/generate-code/workflow.md`
- `agent-control/generate-code/quality-bar.md`
- Repository root `.gitignore`

Files intentionally unchanged:

- `agent-control/write-spec/transaction-system-brief.md`
- `specification.md`
- `integrator.py`
- `agents/*.py`
- `tests/*.py`
- `mcp.json`
- `.codex/config.toml`

No public runtime API changes are implemented in this work item. The plan changes future generated specs and future code-generation workflow expectations.

## Risks

- Over-specifying Hephaestus candidate execution could accidentally leak operator-layer preservation mechanics into generated product code. Mitigation: keep run-folder and selection mechanics in `generate-code` workflow/quality-bar, not in generated transaction-system modules.
- Requiring a copied `sample-transactions.json` could create stale sample copies. Mitigation: Hephaestus copies the canonical sample at run setup and records the source path and fingerprint in run metadata or inventory.
- Ignoring all nested `archive/` folders could hide intentionally curated historical evidence. Mitigation: archive folders are superseded historical runtime output by default; meaningful evidence belongs in current `shared/`, run metadata, validation checklists, screenshots, or selected JSON snippets outside ignored archive directories.
- Adding Reporting Agent could be confused with real compliance reporting. Mitigation: Reporting Agent is strictly audit-safe educational reporting, not AML, KYC, sanctions, payment-network, legal, or banking compliance.

## Acceptance criteria

- `agent-control/write-spec/transaction-system-brief.md` has no diff.
- Athena quality guidance requires at least four cooperating runtime transaction pipeline components and names Reporting Agent as the default fourth component.
- Athena quality guidance says run-local candidate execution details belong in quality/control guidance, not in the static transaction-system brief.
- Hephaestus workflow requires copying canonical `sample-transactions.json` into `agent-2-code/outputs/` before validation.
- Hephaestus validation guidance runs candidate tests and pipeline smoke commands from `agent-2-code/outputs/` using local `shared/` and `archive/`.
- Hephaestus validation guidance verifies the second local smoke run creates `archive/shared-001`.
- Hephaestus validation guidance keeps `archive/`, `.coverage*`, `.pytest_cache/`, `.test-tmp/`, `__pycache__/`, and `*.pyc` out of selectable output inventory, while treating `shared/` as non-selectable but commit-capable runtime evidence.
- Root `.gitignore` ignores root and nested Homework 6 runtime `archive/` folders but does not ignore Homework 6 `shared/` folders.
- Static validation finds no `pipeline-status` MCP config additions and no Task 3/4/5 deliverables.

## Documentation artifact matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Freeze gate and before implementation commit | `CHANGELOG.md` | Newest-first entry for planning freeze and later implementation |
| Test cases | Snapshot | No | Not applicable | Not applicable | This work changes control docs and static workflow rules; validation commands are recorded in the plan |
| Testing guide delta | Living delta | No | Not applicable | Not applicable | No reviewer-facing test guide exists for these operator controls yet |
| Operator manual delta | Living delta | No | Not applicable | Not applicable | `agent-control/*` files are the operator/control manuals being changed |
| API reference delta | Living delta | No | Not applicable | Not applicable | No public API changes |
| Architecture snapshot | Snapshot | No | Not applicable | Not applicable | Layering decisions are captured in this spec and the plan |
| Architecture summary delta | Living delta | No | Not applicable | Not applicable | No long-lived architecture doc is affected |

## Approval

- Status: Draft
- Superseded by: not superseded

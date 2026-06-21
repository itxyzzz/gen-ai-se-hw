# Themis Workflow Staging Fix Spec

Status: Approved for planning freeze on 2026-06-21.

Work ID: `2026-06-21-themis-workflow-staging-fix`

Short ID: `themis-workflow-staging-fix`

## Goal

Repair Themis (Test Generator) workflow guidance so future Java candidate test-generation runs do not stall during run-local output staging, do not produce malformed selectable paths such as `outputs/src/java/`, and do not claim Java command or hook evidence until the run-local Maven project proves the coverage helper threshold is actually enforced.

## Context

The failed Themis child run `20260621-191017-generate-tests-java-hera-java-full-set` loaded the right source package and copied the selected Java Hephaestus output into `agent-3-tests/workspace/selected-code/`, but it failed before Themis-owned test generation or validation. The immediate failure pattern was run-local output staging:

- Baseline Java tests were copied into the wrong path: `agent-3-tests/outputs/src/java/...`.
- The tests were then copied again into the correct path: `agent-3-tests/outputs/src/test/java/...`.
- Cleanup of the accidental duplicate path hit a Windows `Access to the path is denied` error.
- The run never rebuilt `workspace/project-under-test/`, never ran `mvn test`, never ran JaCoCo, and never generated command/hook/privacy evidence.

The successful retry `20260621-201051-generate-tests-java-hera-java-full-set-retry` avoided the malformed output tree, added a focused `ThemisQualityTest.java`, overlaid a `pom.xml` that honored `-Dcoverage.minimum`, rebuilt `workspace/project-under-test/`, and validated Maven/JUnit/JaCoCo, coverage helper pass/fail, pipeline command, validation-only command, privacy scan, and root containment.

## Scope

Update Themis (Test Generator) control-surface documentation only:

- `agent-control/generate-tests/workflow.md`
- `agent-control/generate-tests/quality-bar.md`
- `agent-control/generate-tests/run-registry.md`

Expected guidance changes:

- Add deterministic Java staging rules for copying or reusing baseline tests under `outputs/src/test/java/`.
- Add a pre-overlay output-tree validation gate that rejects `outputs/src/java/`, `outputs/test/java/`, nested duplicate trees, and other non-canonical Java test roots.
- Add a rule that if staging is malformed early, Themis must mark the run blocked or create a replacement run rather than entering cleanup/retry loops or requesting escalation for routine run-local cleanup.
- Add a rule that `workspace/project-under-test/` is rebuilt only after `outputs/` passes the staging gate.
- Add Java coverage-helper compatibility guidance: when the selected Maven package does not let `-Dcoverage.minimum` affect JaCoCo checks, Themis may add a candidate `pom.xml` test/build configuration overlay under `outputs/`, but must not modify runtime product code.
- Add inventory rules requiring accidental non-selectable paths to be excluded and blocked, not silently ignored in a selectable package.

## Non-Scope

This work will not:

- Edit any preserved Themis run folders.
- Resume or repair `20260621-191017-generate-tests-java-hera-java-full-set`.
- Change canonical Python product files, root tests, root docs, screenshots, MCP files, `final-selection.md`, or `selection-sets.json`.
- Change Java runtime product source under `src/main/java/...`.
- Change the coverage helper implementation.
- Change Hera, Athena, Hephaestus, or Clio workflows except through later separately scoped work.

## Design

The repair should keep the Themis workflow simple and operational:

1. The selected Hephaestus package is copied to `workspace/selected-code/`.
2. Candidate outputs are prepared under `agent-3-tests/outputs/`.
3. Java candidate tests and reused baseline tests must live only under `agent-3-tests/outputs/src/test/java/...`.
4. Themis performs a lightweight output-tree staging check before creating `workspace/project-under-test/`.
5. If staging contains a malformed Java test root such as `outputs/src/java/`, the run is blocked unless the agent can correct it without escalation, without destructive ambiguity, and before any validation evidence is produced.
6. For child-agent runs under Hera, malformed staging should normally become a blocked run or replacement-run decision rather than an approval-dependent cleanup detour.
7. `workspace/project-under-test/` is copied from `workspace/selected-code/` and overlaid with `outputs/` only after the staging check passes.
8. Java validation proceeds from `workspace/project-under-test/` and records evidence for Maven/JUnit, JaCoCo 80%, fail-under 99, pipeline command, validation-only command, privacy, repeated-run archival, provenance, and root containment.

The Themis documentation should frame these rules as workflow gates and quality-bar checks rather than as implementation scripts. The actual Themis run agent can satisfy them with PowerShell, shell, Java, or manual inspection as long as the evidence is clear.

## Acceptance Criteria

- The workflow explicitly requires Java tests to be staged under `agent-3-tests/outputs/src/test/java/...`.
- The workflow explicitly rejects accidental `agent-3-tests/outputs/src/java/...` output as non-selectable malformed staging.
- The workflow says malformed staging must be blocked or restarted before validation rather than repaired through escalation-dependent cleanup loops.
- The workflow requires `workspace/project-under-test/` rebuild only after output staging passes.
- The quality bar includes clean Java output-root validation and Maven coverage-helper threshold compatibility.
- The run registry inventory rules require malformed paths to be excluded and treated as blockers in selectable inventories.
- The guidance allows a Themis-owned `pom.xml` test/build overlay when required to make `--fail-under` behavior real, while still forbidding runtime product source repair without operator authorization.
- Static scans find the expected new guardrail terms and no unresolved draft text.
- The change is documented in `CHANGELOG.md` during the approval freeze.

## Validation Strategy

Planned static validation commands:

```powershell
Select-String -Path agent-control\generate-tests\workflow.md -Pattern 'outputs/src/test/java','outputs/src/java','project-under-test','coverage.minimum'
Select-String -Path agent-control\generate-tests\quality-bar.md -Pattern 'outputs/src/java','coverage.minimum','pom.xml'
Select-String -Path agent-control\generate-tests\run-registry.md -Pattern 'outputs/src/java','malformed','blocked'
$draftTerms = @('T'+'BD','TO'+'DO','PLACE'+'HOLDER')
Select-String -Path docs\work-items\2026-06-21-themis-workflow-staging-fix\*.md -Pattern $draftTerms
```

Expected results:

- First three scans find the new guardrail language in the appropriate files.
- Draft-marker scan returns no matches.

No runtime test suite is required for this control-surface documentation change.

## Model And Sub-Agent Strategy

Current orchestration: Codex Desktop, exact model/reasoning controls unavailable in-thread.

Fit assessment: small/medium documentation and workflow-control repair with low code risk but moderate process risk because child agents will use these instructions as execution authority.

Recommended change: none required; use the current orchestration thread for drafting and implementation.

Sub-agents: None. The affected files are tightly coupled workflow documents, and the successful/failed run evidence is already available locally. Parallel sub-agent review would add coordination overhead without meaningful isolation benefit.

## Documentation Artifact Matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Approval freeze and implementation commit | `CHANGELOG.md` | Add newest-first entry for planning freeze and later implementation. |
| Test cases | Snapshot | No | Not applicable | Not applicable | Static documentation validation is sufficient; no product behavior changes. |
| Testing guide delta | Living delta | No | Not applicable | Not applicable | Long-lived reviewer testing guide is not affected. |
| Operator manual delta | Living delta | No | Not applicable | Not applicable | The Themis workflow docs are the operator surface being updated directly. |
| API reference delta | Living delta | No | Not applicable | Not applicable | No API changes. |
| Architecture snapshot | Snapshot | No | Not applicable | Not applicable | No architecture or package-set structure changes. |
| Architecture summary delta | Living delta | No | Not applicable | Not applicable | No long-lived architecture docs affected. |

## Risks

- Over-prescriptive shell examples could make Themis less portable across Codex, Claude Code, and Windows environments. The implementation should describe gates and path invariants, not mandate one command style.
- Allowing `pom.xml` overlays could be misread as product-code repair. The guidance must limit this to test/build configuration under `outputs/` and inventory it as a candidate test/build configuration file.
- Blocking malformed staging too aggressively could stop a run that could be fixed trivially. The wording should allow simple non-escalated correction before validation, but prefer blocked/replacement behavior for child-agent runs once cleanup becomes approval-dependent.

## Planning Freeze Status

The operator approved this spec and the paired implementation plan for freeze on 2026-06-21. After the freeze gate, implementation must start only after a fresh explicit operator instruction.

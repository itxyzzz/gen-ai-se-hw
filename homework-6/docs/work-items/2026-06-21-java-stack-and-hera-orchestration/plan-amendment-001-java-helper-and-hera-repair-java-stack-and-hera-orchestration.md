# Plan Amendment 001: Java Maven Helper And Hera Orchestration Repair

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Short ID: `java-stack-and-hera-orchestration`
Status: Draft Review
Harness release: Installed global `dev-doc-harness` loaded on 2026-06-21
Schema: `schema:plan.amendment`
Policy references: `module:lifecycle`, `module:freeze-gate`, `rule:lifecycle.variance-policy`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, `rule:freeze.stop-before-implementation`

## Original plan reference

- File: `spec-java-stack-and-hera-orchestration.md`
- Section or task: `Phase Decomposition`
- Original instruction: Phase 04 would compare the current Python canonical package set and the preserved Java alternate after Phase 03 generation.

## Discovered issue

Phase 03 produced useful Java alternate evidence, but post-run review found two blockers that make immediate comparison unsafe:

- The repository Java coverage helper cannot pass the run-local Maven settings override required in this environment, so `python scripts/check_coverage_gate.py --stack java --project-dir ... --fail-under 80` fails before test execution by using an unavailable machine-level Maven mirror.
- Hera (Orchestrator) did not dispatch Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator) as first-level child agents. Instead, only Athena's internal executor work used sub-agents and later child stages ran in the main orchestration thread. This violates the intended fallback rule for a clean Hera orchestration test.

## Proposed change

Insert repair phases before cross-stack comparison:

1. Phase 04: repair the Java coverage helper so Java validation can pass a Maven settings override without changing generated Java code.
2. Phase 05: repair Hera orchestration control surfaces so future `generate-set` runs must dispatch the four Homework Automation Layer agents as child agents when first-level dispatch is available, or stop as blocked when it is not.
3. Move the original cross-stack comparison to the phase after these repairs and, if authorized later, after a fresh clean Hera rerun.

## Reason this change is necessary

The original comparison phase assumes the Java candidate has clean helper validation and trustworthy Hera orchestration provenance. Those assumptions are false for the preserved Phase 03 run. Comparing or selecting from that evidence without repair would blur functional Java package quality with a failed orchestration-process test.

## Impact assessment

| Area | Impact |
|---|---|
| Scope | Adds two repair phases before comparison. Java remains preserved alternate evidence only. |
| Acceptance criteria | Adds helper validation with Maven settings override and Hera child-agent dispatch enforcement before comparison readiness. |
| API/interface | Adds optional Java-only Maven settings arguments to the coverage helper CLI while preserving existing Python and Java defaults. |
| Data model/migration | No data model or migration change. |
| Security/privacy/compliance | Reduces privacy risk by keeping runtime `shared/` copies out of committed run evidence and preserving safe validation output only. |
| Tests | Adds or updates focused helper tests plus direct Java helper validation against a temporary Java project copy. |
| Documentation | Updates operator/helper guidance and Hera orchestration guidance. |
| Rollout/operations | Existing Python canonical package remains unchanged; Java comparison waits for repaired helper and Hera behavior. |

## Approval

- Required: Yes
- Status: Draft Review
- Superseded by: Not superseded

## Planning artifact freeze gate

Use `module:freeze-gate`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, and `rule:freeze.stop-before-implementation`. Implementation remains paused until this amendment and the associated phase plans are approved and frozen.

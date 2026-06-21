# Large or Phased Work Item Phase 04: Java Maven Helper Repair

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Short ID: `java-stack-and-hera-orchestration`
Status: Draft Review
Harness release: Installed global `dev-doc-harness` loaded on 2026-06-21
Schema: `schema:plan.phase`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:quality.phase-plan-fresh-thread`, `rule:models.strategy-required`, `rule:lifecycle.variance-policy`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, `rule:freeze.stop-before-implementation`

## Objective

Repair the Java path in `scripts/check_coverage_gate.py` so the repository coverage helper can validate preserved Java Maven projects in this environment by passing an explicit Maven settings override.

## Input context

Implementation must read:

- `spec-java-stack-and-hera-orchestration.md`
- `plan-amendment-001-java-helper-and-hera-repair-java-stack-and-hera-orchestration.md`
- `scripts/check_coverage_gate.py`
- `tests/test_coverage_gate.py`
- `agent-control/operate-pipeline/commands-and-hooks.md`
- `agent-control/generate-tests/workflow.md`
- `agent-control/generate-docs/workflow.md`
- Java evidence under `docs/agent-runs/20260621-145101-generate-code-java-alternate/` and `docs/agent-runs/20260621-145102-generate-tests-java-alternate/`

Root-cause evidence already gathered:

- Plain helper command fails because Maven attempts an unavailable machine-level Maven mirror before test execution.
- Direct Maven with `-s docs\agent-runs\20260621-145101-generate-code-java-alternate\agent-2-code\validation-settings.xml -gs docs\agent-runs\20260621-145101-generate-code-java-alternate\agent-2-code\validation-settings.xml` passes with 21 tests and JaCoCo checks met when invoked with a project-relative path from the Java validation workspace.

## Likely files and areas

- `scripts/check_coverage_gate.py`
- `tests/test_coverage_gate.py`
- `agent-control/operate-pipeline/commands-and-hooks.md`
- `agent-control/generate-tests/workflow.md`
- `agent-control/generate-docs/workflow.md`
- `CHANGELOG.md`
- `docs/work-items/2026-06-21-java-stack-and-hera-orchestration/implementation-notes/variance-log.md`

## Model and Sub-agent Strategy

Current orchestration: Codex Desktop local coding agent; exact model label and reasoning-effort selector are not exposed in this thread.
Fit assessment: Medium complexity and medium blast radius. The code change is small, but it affects a shared support tool used by hooks and generated-package validation.
Recommended change: None. Use active `enterprise-default` policy with careful main-thread implementation and verification.

Sub-agents: None. The root cause is isolated and the expected change is a narrow helper/test/docs update.

## Tasks

- [ ] Add Java-only optional CLI arguments for Maven settings override, preserving current helper behavior when they are omitted.
- [ ] Pass the override into the Java Maven command as `-s SETTINGS_PATH` and `-gs GLOBAL_SETTINGS_PATH` when provided.
- [ ] Resolve settings paths relative to the caller's current working directory or project directory in a documented, deterministic way.
- [ ] Add focused tests proving Python behavior is unchanged, Java default Maven command is unchanged, and Java settings override arguments are included correctly.
- [ ] Update operator and child-agent guidance so Java helper validation can name the settings override when the environment requires it.
- [ ] Validate the helper against a temporary copy of the Java project so Maven can recreate `target/` outside committed run evidence.
- [ ] Clean temporary Maven output before commit.
- [ ] Update `CHANGELOG.md` and variance log before the implementation commit.

## Tests and validation

| Command | Expected result |
|---|---|
| `python -m pytest tests\test_coverage_gate.py -q -p no:cacheprovider` | Focused coverage-helper tests pass. |
| `python scripts\check_coverage_gate.py --stack python --fail-under 80` | Selected Python package still passes the coverage gate. |
| `python scripts\check_coverage_gate.py --stack java --project-dir tmp\java-helper-validation\project-under-test --maven-settings docs\agent-runs\20260621-145101-generate-code-java-alternate\agent-2-code\validation-settings.xml --maven-global-settings docs\agent-runs\20260621-145101-generate-code-java-alternate\agent-2-code\validation-settings.xml --fail-under 80` | Java helper path passes with Maven/JUnit/JaCoCo in this environment after the implementation copies the Java validation project to `tmp\java-helper-validation\project-under-test`. |
| `git diff --check` | No whitespace errors. |
| Privacy scan over changed docs and helper output | No raw account IDs or raw sample descriptions are introduced outside allowed fixture files. |

## Documentation artifact matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Before implementation commit | `CHANGELOG.md` | Record helper CLI and validation impact. |
| Test cases | Snapshot | No | Not applicable | Not applicable | Focused tests in `tests/test_coverage_gate.py` are sufficient. |
| Testing guide delta | Living delta | No | Not applicable | Not applicable | Operator guidance update lives in `agent-control/operate-pipeline/commands-and-hooks.md`. |
| Operator manual delta | Living delta | No | Not applicable | Not applicable | Same as above. |
| API reference delta | Living delta | No | Not applicable | Not applicable | No runtime API change. |
| Architecture snapshot | Snapshot | No | Not applicable | Not applicable | No architecture change. |
| Architecture summary delta | Living delta | No | Not applicable | Not applicable | No long-lived architecture change. |

## Variance reminder

Use `rule:lifecycle.variance-policy`. Before freeze, edit this draft directly for operator feedback. After freeze, record nontrivial implementation variance in `implementation-notes/variance-log.md`; use a plan amendment for high-impact architecture, API, data, security, privacy, compliance, scope, acceptance-criteria, or feasibility changes.

## Planning artifact freeze gate

Use `module:freeze-gate`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, and `rule:freeze.stop-before-implementation`.

Approval status: Draft review. Implementation remains paused until this plan is approved and frozen.

## Handoff output

Report files changed, exact commands run, whether helper validation passed against the temporary Java project, whether temporary Maven output was cleaned, and any remaining limitation for Hera repair planning.

## Completion criteria

- Java helper can pass an explicit Maven settings override.
- Existing Python helper behavior remains compatible.
- Documentation names the override path only as an optional environment repair, not as a generated Java product requirement.
- `CHANGELOG.md` and variance log are updated before commit.
- No generated `target/`, `shared/`, or `archive/` output is committed.

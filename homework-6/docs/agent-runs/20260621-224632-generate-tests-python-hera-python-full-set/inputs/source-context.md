# Source Context

## Required Control Context Read

- `.agents/skills/generate-tests/SKILL.md`
- `agent-control/generate-tests/workflow.md`
- `agent-control/generate-tests/quality-bar.md`
- `agent-control/generate-tests/run-registry.md`
- `agent-control/operate-pipeline/commands-and-hooks.md`
- `.agents/skills/run-pipeline/SKILL.md`
- `.agents/skills/validate-transactions/SKILL.md`
- `.claude/commands/run-pipeline.md`
- `.claude/commands/validate-transactions.md`
- `.githooks/pre-push`
- `scripts/check_coverage_gate.py`

## Required Homework Context Read

- `agents.md`
- `TASKS.md` Task 3, Task 4, and Task 5 boundaries
- `sample-transactions.json`
- `docs/agent-runs/final-selection.md` as protected source context only
- `docs/agent-runs/selection-sets.json` as protected source context only

## Target Candidate Context Read

- Source Athena specification: `docs/agent-runs/20260621-220037-write-spec-python-hera-python-full-set/agent-1-spec/outputs/specification.md`
- Source Hephaestus metadata: `docs/agent-runs/20260621-222543-generate-code-python-hera-python-full-set/run-metadata.md`
- Source Hephaestus validation: `docs/agent-runs/20260621-222543-generate-code-python-hera-python-full-set/agent-2-code/validation-checklist.md`
- Source Hephaestus handoff: `docs/agent-runs/20260621-222543-generate-code-python-hera-python-full-set/agent-2-code/handoff.md`
- Source Hephaestus inventory: `docs/agent-runs/20260621-222543-generate-code-python-hera-python-full-set/agent-2-code/outputs/inventory.md`
- Source Hephaestus package root: `docs/agent-runs/20260621-222543-generate-code-python-hera-python-full-set/agent-2-code/outputs/`

## Target Package Files Inspected

- `integrator.py`
- `agents/common.py`
- `agents/transaction_validator.py`
- `agents/fraud_detector.py`
- `agents/settlement_processor.py`
- `agents/reporting_agent.py`
- `pytest.ini`
- `tests/conftest.py`
- `tests/test_common.py`
- `tests/test_transaction_validator.py`
- `tests/test_fraud_detector.py`
- `tests/test_settlement_processor.py`
- `tests/test_reporting_agent.py`
- `tests/test_integrator_pipeline.py`

## Traceability Notes

- The Hera request explicitly supplied a named candidate Hephaestus inventory, so this run adapted the selected-code workflow to target that inventory instead of the current canonical selected inventory.
- The current canonical `specification.md` fingerprint differs from the source Athena candidate fingerprint. This run reports that mismatch and validates only the named fresh candidate package.
- Runtime evidence from the Hephaestus package, including `shared/`, `archive/`, and `evidence/`, was excluded from the selected-code workspace copy.


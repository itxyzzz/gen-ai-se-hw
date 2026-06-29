# Validation Checklist

Run ID: `20260617-180458-write-spec-python-primary`
Stack: `python`

## Task 1 Sections

| Check | Status | Evidence |
|---|---|---|
| High-Level Objective present | Pass | `agent-1-spec/outputs/specification.md` |
| Mid-Level Objectives present with 4-5 testable objectives | Pass | Five objectives M1-M5 |
| Implementation Notes present | Pass | Money, currency, JSON protocol, audit, coverage, Context7, MCP |
| Context present | Pass | Beginning and ending state |
| Low-Level Tasks present | Pass | One task card each for Agents 1-4 |

## Stack Specificity

| Check | Status | Evidence |
|---|---|---|
| Selected stack is `python` | Pass | Run metadata and specification |
| Python files are named | Pass | `integrator.py`, `agents/*.py`, `mcp/server.py`, `tests/*.py` |
| Python functions are named | Pass | `main`, `process_message`, `validate_transaction`, `score_fraud_risk`, `settle_transaction`, MCP status functions |
| Python commands are named | Pass | `python integrator.py`, `python -m pytest`, `python -m pytest --cov=. --cov-fail-under=80` |
| Python test and coverage tools are named | Pass | `pytest`, `pytest-cov` |

## Downstream Agent Requirements

| Check | Status | Evidence |
|---|---|---|
| One low-level task card per meta-agent | Pass | Agents 1-4 task cards |
| Agent 2 requires at least three cooperating pipeline agents | Pass | Validator, fraud detector, settlement processor |
| Agent 2 Context7 usage and two-query documentation explicit | Pass | Implementation Notes and Agent 2 task card |
| Agent 3 coverage hook minimum is 80% | Pass | Implementation Notes and Agent 3 task card |
| Final coverage target is at least 90% | Pass | Mid-level objective M4 and Agent 3/4 task cards |
| Agent 4 README student-name requirement explicit | Pass | Agent 4 task card |

## Protocol, Money, Privacy, And MCP

| Check | Status | Evidence |
|---|---|---|
| JSON protocol includes `shared/input` | Pass | Specification |
| JSON protocol includes `shared/processing` | Pass | Specification |
| JSON protocol includes `shared/output` | Pass | Specification |
| JSON protocol includes `shared/results` | Pass | Specification |
| Money uses `decimal.Decimal` | Pass | Implementation Notes and task cards |
| Binary floating point for amounts is prohibited | Pass | Implementation Notes and task cards |
| ISO 4217-style currency validation required | Pass | Implementation Notes and task cards |
| `XYZ` rejection required | Pass | Context and task cards |
| Negative amount rejection required | Pass | Context and task cards |
| Redacted audit/log examples required | Pass | Implementation Notes and task cards |
| Unsupported compliance claims prohibited | Pass | Implementation Notes and task cards |
| `pipeline-status` config deferred until `mcp/server.py` exists | Pass | Implementation Notes and task cards |

## Run Artifacts

| Check | Status | Evidence |
|---|---|---|
| Run metadata present | Pass | `run-metadata.md` |
| Source context present | Pass | `inputs/source-context.md` |
| Sub-agent plan present | Pass | `handoffs/sub-agent-plan.md` |
| Domain research handoff present | Pass | `handoffs/domain-research-handoff.md` |
| Objectives handoff present | Pass | `handoffs/objectives-handoff.md` |
| Low-level tasks handoff present | Pass | `handoffs/low-level-tasks-handoff.md` |
| Research notes present | Pass | `research-notes.md` |
| Support docs present | Pass | `outputs/docs/domain-rules.md`, `outputs/docs/technical-conventions.md`, `outputs/docs/development-process.md` |
| Final review present | Pass | `review/final-review.md`, including repair review appendix |
| Completion handoff present | Pass | `handoff.md` finalized after review |
| Canonical files untouched before selection | Pass | Canonical copy deferred until review passes |

## Manual Risk-Word Review

The generated spec contains terms such as AML, sanctions, KYC, payment-network, and compliance only in negative boundary language that states the educational simulation does not claim those outcomes.

## Final Review Result

Initial review found one blocking issue in `/validate-transactions` command semantics. The issue was repaired in `agent-1-spec/outputs/specification.md` and `agent-1-spec/handoffs/low-level-tasks-handoff.md`. Follow-up repair review found no remaining blocking issues and recommended first-run auto-selection.

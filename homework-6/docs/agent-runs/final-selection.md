# Final Selection

Current status: selected Athena (Spec Writer) run `20260618-003908-write-spec-python-replacement` is the canonical Python transaction-processing system specification. Selected Hephaestus (Code Generator) run `20260618-223217-generate-code-python-primary` is the canonical Task 2 software version.

Use the current canonical `specification.md` as downstream input for Hephaestus (Code Generator). The prior selected run `20260617-180458-write-spec-python-primary` remains preserved as failed/superseded evidence only.

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
| 2026-06-18 | `20260618-003908-write-spec-python-replacement` | `python` | `agent-1-spec/outputs/specification.md` copied to `specification.md` | Explicit operator selection of replacement Athena (Spec Writer) run. The run passed validation, repaired final-review blockers, targets only the Generated Transaction System Layer, and provides implementation-ready Python task cards for downstream agents. | Repository operator in current Codex Desktop thread |
| 2026-06-17 | `20260617-180458-write-spec-python-primary` | `python` | `agent-1-spec/outputs/specification.md` copied to `specification.md` | First successful Python `write-spec generate` run; canonical `specification.md` was absent; final review blocker was repaired and follow-up review found no remaining blocking issues. | Repository operator in current Codex Desktop thread |

## Hephaestus Code Generation Selection

Current selected code run: `20260618-223217-generate-code-python-primary`.

Source specification:

- Athena source run ID: `20260618-003908-write-spec-python-replacement`
- Canonical source path: `homework-6/specification.md`
- Source SHA-256: `B08B8D365070DAD1F6A9BFE36F2D21951802CF0A284615706F17086E961DDD06`

Selected output inventory:

- `docs/agent-runs/20260618-223217-generate-code-python-primary/agent-2-code/outputs/inventory.md`

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
- `tests/test_pipeline_end_to_end.py`
- `research-notes.md`

Selection rationale: first successful Hephaestus Task 2 run, repaired to preserve its full code package under `agent-2-code/outputs/` and selected by default because no earlier selected code package existed.

Excluded from code selection:

- `shared/`
- `archive/`
- `.coverage`
- `__pycache__/`
- `.pytest_cache/`

For later Hephaestus selections, remove the canonical targets declared by the prior selected inventory before copying the replacement package. Do not remove hand-maintained files outside the selected inventory.

## Failure And Supersession Notes

| Date | Run ID | Status | Reason | Follow-up |
|---|---|---|---|---|
| 2026-06-17 | `20260617-180458-write-spec-python-primary` | Failed / superseded | Wrong target: the selected specification described the homework automation harness and spec-generation workflow in its low-level tasks instead of specifying only the transaction-processing system. | Preserve this run and canonical copy as failed evidence. Replacement generation and selection must happen later in a separate clean Homework 6-root thread after the Athena (Spec Writer) control surface is repaired. |

## Selected Canonical Paths

- `specification.md`

## Post-Selection Edits

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

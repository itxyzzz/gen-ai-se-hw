# Sub-Agent Plan

No executor sub-agents were used for this run.

## Rationale

The fresh-spec repair is tightly coupled across a small Python package: the safe message envelope, validator expectations, risk-routing edge cases, final result shape, run provenance, and filesystem tests all depend on the same data contract. Splitting those files among separate workers would raise integration risk more than it would improve quality.

## Planned Local Slices

| Slice | Context strategy | Input context | Output artifact | Model policy | Profile | Reasoning effort | Parallel | Blast radius if wrong |
|---|---|---|---|---|---|---|---|---|
| Common utilities and privacy checks | curated artifacts | selected spec, quality bar, prior code | `agents/common.py` | enterprise-default | strongest available in orchestration thread | high | no | Raw identifier or metadata leaks in every stage |
| Runtime components | curated artifacts | selected spec, sample outcomes | validator, risk scorer, settlement processor | enterprise-default | strongest available in orchestration thread | high | no | Wrong sample outcomes or unsafe final statuses |
| Integrator and provenance | curated artifacts | selected spec, run registry, prior code | `integrator.py` | enterprise-default | strongest available in orchestration thread | high | no | Broken archive behavior or traceability |
| Tests and validation evidence | curated artifacts | quality bar and candidate code | `tests/`, checklist | enterprise-default | strongest available in orchestration thread | medium | no | False confidence or missed privacy regression |

## De-Facto Sub-Agent Use

None. The orchestration thread performed final decomposition, implementation, integration, validation, and review.


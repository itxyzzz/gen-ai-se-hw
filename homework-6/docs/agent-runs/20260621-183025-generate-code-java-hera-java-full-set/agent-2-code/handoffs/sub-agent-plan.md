# Sub-Agent Plan And Observed Use

## Planned Strategy

The selected Java package was tightly coupled across Maven configuration, DTOs, file protocol, runtime components, and integration tests, so the orchestration thread owned implementation. Executor sub-agents were still useful for independent review slices.

| Role | Purpose | Context strategy | Output artifact | Model policy | Reasoning | Parallel | Blast radius if wrong |
|---|---|---|---|---|---|---|---|
| Java implementation quality support | Identify required files, Maven details, and high-risk integration points. | Curated prompt | `java-implementation-quality-handoff.md` | `enterprise-default` | Medium | Yes | Could miss build/runtime risks; main agent reviewed and validated. |
| Privacy/schema support | Identify privacy reject conditions, scans, result schema checks, and validation recommendations. | Curated prompt | `privacy-schema-handoff.md` | `enterprise-default` | Medium | Yes | Could over/under-scan privacy risks; main agent ran focused scans and repaired raw-value test literals. |

## Observed Use

- Both support sub-agents completed.
- Neither sub-agent edited files.
- The orchestration thread implemented and integrated all code and artifacts.
- Accepted recommendations:
  - Configure JaCoCo at 80%.
  - Verify `archive/shared-001` with a clean second run.
  - Keep `shared/` runtime evidence out of selectable inventory.
  - Run privacy scans for raw fixture values, credentials, and production-compliance claims.
  - Inspect result JSON key sets.
- Adjustment from handoff:
  - Source/parser code necessarily references input field names such as `source_account`; result JSON and runtime evidence do not expose those fields.

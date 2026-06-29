# Source Context

## Direct Inputs

- Source Athena candidate specification: `docs/agent-runs/20260621-220037-write-spec-python-hera-python-full-set/agent-1-spec/outputs/specification.md`
- Source Athena candidate SHA-256: `6F2E8CD844884DF06172EEB1CF92A2956BC45425FE514B7A820ABAEA249D2222`
- Canonical sample fixture: `sample-transactions.json`
- Copied candidate fixture: `agent-2-code/outputs/sample-transactions.json`
- Copied sample SHA-256: `771DA836CAAAA42921C628D6CD2E42D52E12687917BA60633E594D526BF4BF12`

## Context Files Read

- `.agents/skills/generate-code/SKILL.md`
- `agent-control/generate-code/workflow.md`
- `agent-control/generate-code/quality-bar.md`
- `agent-control/generate-code/run-registry.md`
- `agents.md`
- `TASKS.md`
- `mcp.json`
- `.codex/config.toml`
- `docs/agent-runs/selection-sets.json` as protected context only
- `docs/agent-runs/final-selection.md` as protected context only
- `docs/agent-runs/20260621-220037-write-spec-python-hera-python-full-set/run-metadata.md`
- `docs/agent-runs/20260621-220037-write-spec-python-hera-python-full-set/inputs/source-context.md`
- `docs/agent-runs/20260621-220037-write-spec-python-hera-python-full-set/agent-1-spec/handoff.md`
- `docs/agent-runs/20260621-220037-write-spec-python-hera-python-full-set/agent-1-spec/validation-checklist.md`
- `docs/agent-runs/20260621-215717-orchestrate-runs-python-full-set/agent-5-orchestrator/child-runs.md`
- `../AGENTS.md`
- `../HOMEWORK_STANDARDS.md`
- `../README.md`

## Assignment Boundary Used

- Generate Task 2 Generated Transaction System Layer code only.
- Do not generate or update Task 3 command wrappers, hooks, or coverage gate config.
- Do not generate or update Task 4 MCP server/config.
- Do not generate Task 5 reviewer docs, screenshots, or PR package files.
- Preserve output under this run folder only; no canonical selection is authorized.

## Product Decisions

- Runtime components: Integrator, Transaction Validator, Fraud Detector, Settlement Processor, Reporting Agent.
- Money: `decimal.Decimal` from strings only; JSON money is serialized as strings.
- JSON: standard-library `json` with `allow_nan=False`.
- Shared protocol: `shared/input`, `shared/processing`, `shared/output`, `shared/results`.
- Rerun archival: existing `shared/` is copied into `archive/shared-001` style evidence before fresh current output is overwritten. Copy-based archival was used because this Windows sandbox denied file/directory moves.
- Runtime provenance: `shared/run-provenance.json` records non-sensitive run IDs, source spec path/fingerprint, generated code run ID, inventory path, stack, and pipeline version.
- Privacy: account IDs and descriptions are not written to results, summaries, provenance, or protocol evidence; the copied fixture is the only preserved raw sample payload.

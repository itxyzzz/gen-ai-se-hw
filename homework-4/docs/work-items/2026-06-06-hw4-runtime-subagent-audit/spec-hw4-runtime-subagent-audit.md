# HW4 Runtime Sub-Agent Audit Spec

Work ID: `2026-06-06-hw4-runtime-subagent-audit`
Short ID: `hw4-runtime-subagent-audit`
Status: Approved

## Goal

Future Homework 4 pipeline runs must document de-facto sub-agent use and model selection in `run-metadata.json` using a portable audit contract that works across Codex, Claude Code, Google Antigravity, Open Code, and generic capable agentic tools.

## Scope

- Add a lightweight runtime sub-agent audit contract to the Homework 4 pipeline harness.
- Require each adapter to populate the same `runtimeSubagentAudit` metadata shape using the strongest native mechanism available to that tool.
- Document native capture mechanisms per adapter:
  - Claude Code: `PostToolUse` on `Agent`, with `SubagentStart` / `SubagentStop` and `Stop` gate when available.
  - Google Antigravity: `PostToolUse` on `invoke_subagent`, with `Stop` gate when available.
  - Open Code: plugin event capture through `tool.execute.before` / `tool.execute.after` or equivalent task-subagent event evidence.
  - Codex: adapter-level recording around delegated stage execution, with native hooks used only when the active Codex surface exposes reliable coverage.
  - Generic tools: required best-effort adapter recording, or an explicit unavailable reason.
- Update Homework 4 documentation that describes the artifact contract, architecture, run instructions, and verification checklist.
- Keep reports and preserved run artifacts compact; do not retroactively rewrite immutable source run snapshots or benchmark-normalized evidence.

## Non-scope

- No new pipeline run is required by this work item.
- No retroactive mutation of `homework-4/runs/bug-001/*` or `homework-4/benchmark/bug-001/runs/*`.
- No attempt to make one universal hook configuration file work across all agent tools.
- No JavaScript pipeline harness revival, SDK adapter, benchmark script, or large report generator.
- No screenshots unless implementation uncovers a concrete review need.

## Current state

Homework 4 already records planned stage model policies and adapter-selected models in `run-metadata.json`. The harness recommends sub-agent context isolation when supported, and some preserved Open Code command logs say that task subagents were used. However, there is no required run-level metadata field that distinguishes planned model policy from runtime sub-agent behavior, and no adapter-wide enforcement that every completed run records whether sub-agents were actually used.

The current Homework 4 submission is intentionally text-first. Its public interface is the prompt contract `Run HW4 pipeline`, adapter Markdown files, and preserved artifacts. That makes `run-metadata.json` the right portable enforcement layer, while hooks/plugins remain tool-specific ingestion mechanisms.

## Proposed behavior

Every future completed or blocked Homework 4 run must include a compact `runtimeSubagentAudit` object in `run-metadata.json`.

Required top-level audit fields:

```json
{
  "runtimeSubagentAudit": {
    "schemaVersion": "1.0.0",
    "collectionMode": "native-hook",
    "collector": "claude-code PostToolUse:Agent",
    "subagentsExpected": true,
    "subagentsUsed": true,
    "unavailableReason": null,
    "events": []
  }
}
```

Allowed `collectionMode` values:

- `native-hook`: native hook or lifecycle event captured runtime sub-agent execution.
- `plugin-event`: plugin event captured runtime sub-agent execution.
- `adapter-recorded`: adapter/orchestrator recorded runtime choices because native hooks were unavailable, incomplete, or non-portable.
- `manual-unavailable`: the tool cannot expose reliable runtime sub-agent evidence; the run must say why and record planned policy-relative intent.

Each event in `events` must be a compact object with these fields when known:

```json
{
  "stageId": "bug-researcher",
  "agentFile": "agents/bug-researcher.agent.md",
  "subagentUsed": true,
  "launchMechanism": "claude.Agent",
  "agentId": "agent-abc123",
  "agentType": "Explore",
  "expectedModelPolicy": "research-high",
  "requestedModel": "sonnet",
  "observedModel": "sonnet",
  "reasoningEffort": "high",
  "contextStrategy": "curated artifacts",
  "status": "completed",
  "evidenceSource": "hook-payload",
  "transcriptPath": ".claude/projects/.../subagents/agent-abc123.jsonl",
  "tokenUsage": {
    "totalTokens": 12450
  },
  "notes": "Exact model alias exposed by platform; concrete model version not exposed."
}
```

Fields that are not exposed by the active tool should be omitted or set to `null`, with `notes` explaining the limitation. The audit should stay factual and small; detailed sub-agent transcripts, long final messages, and raw hook payloads do not belong in the run report unless needed for a blocker investigation.

## Interfaces and data

Affected artifact contract:

- `homework-4/runs/bug-001/run-<NNN>-<tool>-<pattern>/run-metadata.json`
- `homework-4/benchmark/bug-001/runs/run-<NNN>-<tool>-<pattern>/run-metadata.json` only for future normalized copies of future runs

Affected documentation and instructions:

- `homework-4/skills/pipeline-harness-wrapper.md`
- `homework-4/adapters/codex-chat.md`
- `homework-4/adapters/claude-code.md`
- `homework-4/adapters/google-antigravity.md`
- `homework-4/adapters/open-code.md`
- `homework-4/adapters/generic-agent.md`
- `homework-4/API_REFERENCE.md`
- `homework-4/ARCHITECTURE.md`
- `homework-4/HOWTORUN.md`
- `homework-4/TESTING_GUIDE.md`
- `homework-4/README.md`
- `homework-4/CHANGELOG.md`

No source-code API, CLI argument, sample app behavior, or persisted application data changes are planned.

## Risks

- Tool hook surfaces are not fully portable. Mitigation: make `run-metadata.json` the portable contract and document per-adapter collection modes.
- Codex hook behavior may vary by surface and version. Mitigation: require `adapter-recorded` or `manual-unavailable` evidence when native hook coverage is incomplete.
- Reports could become noisy. Mitigation: define compact event fields and explicitly exclude raw hook payloads and long transcripts from normal run metadata.
- Preserved evidence could be accidentally rewritten. Mitigation: future-run-only requirement; immutable existing source and benchmark snapshots remain untouched.
- Adapter instructions could overpromise enforcement for a tool. Mitigation: use factual capability-specific wording and require an unavailable reason when runtime evidence cannot be observed.

## Acceptance criteria

- `skills/pipeline-harness-wrapper.md` requires `runtimeSubagentAudit` in every future run's `run-metadata.json`.
- All dedicated adapters explain how they populate the audit contract and what collection mode they use when native hooks are unavailable.
- `API_REFERENCE.md` documents the new metadata fields and allowed `collectionMode` values.
- `ARCHITECTURE.md` explains that hook/plugin mechanisms are adapter-specific while `run-metadata.json` is the portable enforcement layer.
- `HOWTORUN.md` tells operators to check `runtimeSubagentAudit` after a run.
- `TESTING_GUIDE.md` includes a compact artifact-review checklist for the audit section.
- `README.md` briefly reflects the runtime-audit portability decision without turning it into a long report.
- Existing preserved run and benchmark evidence folders remain unchanged.
- Validation confirms the new term `runtimeSubagentAudit` appears in the harness, all adapters, and the relevant docs.

## Documentation artifact matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Before each commit | `homework-4/CHANGELOG.md` | Newest-first Homework 4 step entry for the implementation commit |
| Test cases | Snapshot | No | Not applicable | N/A | No sample app behavior or executable code changes are planned |
| Testing guide delta | Living delta | No | Not applicable | N/A | Implementation updates `homework-4/TESTING_GUIDE.md` directly |
| Operator manual delta | Living delta | No | Not applicable | N/A | Implementation updates `homework-4/HOWTORUN.md` directly |
| API reference delta | Living delta | No | Not applicable | N/A | Implementation updates `homework-4/API_REFERENCE.md` directly |
| Architecture snapshot | Snapshot | No | Not applicable | N/A | The design is captured in this spec; implementation updates `homework-4/ARCHITECTURE.md` directly |
| Architecture summary delta | Living delta | No | Not applicable | N/A | Implementation updates `homework-4/ARCHITECTURE.md` directly |

## Approval

- Status: Approved
- Superseded by: None

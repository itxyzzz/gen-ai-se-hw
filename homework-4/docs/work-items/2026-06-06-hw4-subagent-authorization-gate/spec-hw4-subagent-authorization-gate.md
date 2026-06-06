# HW4 Sub-Agent Authorization Gate Spec

Work ID: `2026-06-06-hw4-subagent-authorization-gate`
Short ID: `hw4-subagent-authorization-gate`
Status: Draft

## Goal

Homework 4 pipeline runs must not silently fall back from sub-agent execution to
parent-session execution. When sub-agent execution is required but not yet
explicitly authorized by the operator, the orchestrator must pause before stage
execution and ask for confirmation. If sub-agent capability is unavailable, the
orchestrator must ask the operator whether to approve direct fallback or record
the run as blocked.

The operator-visible outcome is a clearer launch and audit contract:

- sub-agent-capable tools ask for missing authorization instead of falling back;
- sub-agent-incapable tools ask before direct fallback;
- every completed or blocked run records the operator authorization decision in
  `runtimeSubagentAudit`.

## Scope

- Update the Homework 4 pipeline harness authorization rules for sub-agent
  execution.
- Update adapter instructions so each active tool class distinguishes these
  states before running any pipeline stage:
  - sub-agent tools available and already authorized;
  - sub-agent tools available but not yet operator-authorized;
  - sub-agent tools unavailable;
  - direct fallback explicitly operator-approved;
  - both sub-agent execution and direct fallback declined.
- Extend the future-run `runtimeSubagentAudit` metadata contract with a compact
  operator authorization record.
- Update Homework 4 run instructions, architecture/API/testing documentation,
  and README wording so operators know how to trigger, confirm, review, and
  audit sub-agent use.
- Preserve the existing text-first pipeline design and current run folder
  naming contract.

## Non-scope

- No JavaScript runner, SDK adapter, slash command, CLI, or executable pipeline
  harness revival.
- No retroactive rewriting of preserved source run evidence under
  `homework-4/runs/bug-001/` or normalized benchmark evidence under
  `homework-4/benchmark/bug-001/`.
- No new pipeline run is required by this work item.
- No change to the sample quote-calculator app behavior.
- No change to portable agent frontmatter model policies.
- No attempt to bypass active platform or tool policy for spawning sub-agents.

## Current state

Homework 4 currently recognizes `Run HW4 pipeline` as a launch phrase and asks
the active assistant to run six stages in order:

1. `bug-researcher`
2. `research-verifier`
3. `bug-planner`
4. `bug-fixer`
5. `security-verifier`
6. `unit-test-generator`

The harness says dedicated subagents are highly recommended when available, and
the runtime audit contract requires future runs to record whether sub-agents
were expected and used. The Codex adapter allows `adapter-recorded` or
`manual-unavailable` audit evidence when native hook coverage is unavailable.

That design records what happened, but it still allows an important failure
mode: if the operator typed only `Run HW4 pipeline`, an agent can run all stages
directly in the parent session and record that no sub-agents were used. This is
not strong enough for the intended pipeline behavior, because the assignment is
about agent execution and the repository now wants the operator to explicitly
confirm any non-sub-agent fallback.

The current Codex multi-agent tool surface also has an external permission rule:
sub-agents may be spawned only when the user explicitly asks for sub-agents,
delegation, or parallel agent work. Repository instructions can define the
desired behavior, but they cannot silently override that tool-level
authorization requirement. The harness therefore needs an explicit confirmation
gate for the recoverable "not authorized yet" case.

## Proposed behavior

Before running `bug-researcher`, the orchestrator must evaluate sub-agent
capability and operator authorization.

### Authorization states

1. **Available and authorized**
   - Condition: the active tool exposes sub-agent spawning, and the operator
     prompt or prior confirmation explicitly authorizes sub-agent execution for
     this run.
   - Required behavior: spawn one sub-agent per pipeline stage, in harness order
     or in an approved wave pattern that preserves dependencies. Do not execute
     stage work directly in the parent session.
   - Audit behavior: record `subagentsExpected: true`,
     `subagentsUsed: true`, and `operatorAuthorization.status:
     "confirmed"` or `"confirmed-after-gate"`.

2. **Available but not yet authorized**
   - Condition: the active tool exposes sub-agent spawning, but the operator has
     not explicitly authorized spawning in the launch prompt or current run
     context.
   - Required behavior: stop before `bug-researcher` and ask the operator for
     explicit confirmation. Do not run report-writing, code-editing, review, or
     test-generation stages directly while waiting.
   - Required confirmation wording:

     ```text
     Please confirm whether I should spawn one sub-agent per HW4 pipeline stage
     and use them for this run. I will not execute the stages directly unless
     you explicitly approve fallback.
     ```

   - If the operator confirms sub-agent use, continue with sub-agents and
     record the confirmation.
   - If the operator declines sub-agent use but explicitly approves fallback,
     run directly and record fallback approval.
   - If the operator declines both or does not answer, record the run as blocked
     only if a run folder has already been opened; otherwise leave no run
     artifact.

3. **Unavailable**
   - Condition: the active environment has no sub-agent spawning capability or
     the tool surface cannot expose a spawn operation to the orchestrator.
   - Required behavior: stop before `bug-researcher` and ask whether the
     operator approves direct fallback or wants the run recorded as blocked.
   - If fallback is approved, run directly and record the approval and
     unavailable reason.
   - If fallback is declined, record a blocked run if a run folder has already
     been opened.

4. **Fallback approved**
   - Condition: sub-agent use is unavailable or declined, and the operator
     explicitly authorizes direct execution.
   - Required behavior: direct execution is allowed for that run only. The
     audit must show `subagentsExpected: true`, `subagentsUsed: false`, and
     `operatorAuthorization.status: "fallback-approved"`.

### Recommended launch phrase

Documentation should promote this launch phrase for the happy path:

```text
Run HW4 pipeline using sub-agents. Spawn one sub-agent per stage. If sub-agent
use is not already authorized or not available, ask me before falling back.
```

The shorter `Run HW4 pipeline` phrase may remain recognized as pipeline intent,
but it should no longer imply permission to perform direct stage execution when
sub-agent execution is required. Instead, it triggers the authorization gate.

### Runtime audit shape

Future `run-metadata.json` files should keep the existing
`runtimeSubagentAudit` fields and add an `operatorAuthorization` object:

```json
{
  "runtimeSubagentAudit": {
    "schemaVersion": "1.0.0",
    "collectionMode": "adapter-recorded",
    "collector": "codex-chat-adapter-record",
    "subagentsExpected": true,
    "subagentsUsed": true,
    "operatorAuthorization": {
      "required": true,
      "status": "confirmed-after-gate",
      "source": "operator confirmation before stage execution",
      "fallbackApproved": false
    },
    "unavailableReason": null,
    "events": []
  }
}
```

Allowed `operatorAuthorization.status` values:

- `confirmed`: the launch prompt explicitly authorized sub-agent execution.
- `confirmed-after-gate`: the orchestrator paused and the operator confirmed
  sub-agent execution before stage work.
- `fallback-approved`: the operator explicitly approved direct execution after
  sub-agent use was unavailable or declined.
- `declined`: the operator declined sub-agent execution and did not approve
  fallback.
- `not-required`: reserved for adapters or future scenarios where sub-agent
  execution is not required. This should not be used for the HW4 pipeline unless
  a future approved spec changes the requirement.

Blocked run metadata should include enough detail for audit without pretending
the run completed. Example:

```json
{
  "status": "blocked",
  "runtimeSubagentAudit": {
    "schemaVersion": "1.0.0",
    "collectionMode": "manual-unavailable",
    "collector": "codex-chat-adapter-record",
    "subagentsExpected": true,
    "subagentsUsed": false,
    "operatorAuthorization": {
      "required": true,
      "status": "declined",
      "source": "operator declined sub-agent use and direct fallback",
      "fallbackApproved": false
    },
    "unavailableReason": "Sub-agent execution was not authorized and direct fallback was not approved.",
    "events": []
  }
}
```

## Interfaces and data

Affected future-run artifact contract:

- `homework-4/runs/bug-001/run-<NNN>-<tool>-<pattern>/run-metadata.json`
- Future normalized benchmark copies of future runs when applicable.

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

No app source API, CLI argument, package dependency, persistence layer, or
sample app data format changes are planned.

## Risks

- **Tool permission mismatch:** a repository instruction may say sub-agents are
  required while the active platform requires explicit user authorization.
  Mitigation: the new gate treats missing authorization as a recoverable
  operator-confirmation step.
- **Silent fallback persists through vague wording:** adapters might still imply
  direct fallback is allowed. Mitigation: require all adapters to use the same
  capability/authorization/fallback decision sequence.
- **Operator annoyance:** the short launch phrase may now ask a follow-up
  question. Mitigation: document a longer happy-path launch phrase that includes
  sub-agent authorization up front.
- **Run artifact ambiguity:** blocked-before-run and blocked-after-run cases
  could be confused. Mitigation: specify that blocked metadata is required once
  a run folder exists; otherwise the orchestrator may ask before creating a run
  folder.
- **Historical evidence churn:** changing the contract could tempt agents to
  backfill old run metadata. Mitigation: explicitly keep the change
  future-run-only and preserve existing source and benchmark snapshots.
- **Overpromising exact runtime telemetry:** some tools may spawn sub-agents
  without exposing exact model or hook payloads. Mitigation: keep
  `collectionMode`, `unavailableReason`, and compact `events.notes` fields.

## Acceptance criteria

- The harness requires a sub-agent authorization gate before `bug-researcher`.
- The harness distinguishes sub-agent capability from operator authorization.
- The harness requires operator confirmation before direct fallback when
  sub-agent use is unavailable, declined, or not yet authorized.
- The Codex adapter documents that `Run HW4 pipeline` alone may trigger a
  confirmation prompt when sub-agent spawning is available but not explicitly
  authorized.
- All dedicated adapters describe how to record `operatorAuthorization` in
  `runtimeSubagentAudit`.
- `API_REFERENCE.md` documents the `operatorAuthorization` object and allowed
  statuses.
- `HOWTORUN.md` includes the longer happy-path launch phrase and explains the
  follow-up confirmation behavior for the short phrase.
- `TESTING_GUIDE.md` includes review checks for sub-agent authorization,
  fallback approval, and blocked-run metadata.
- `ARCHITECTURE.md` explains why authorization is separated from capability.
- `README.md` briefly states that the pipeline is designed for sub-agent stage
  execution and asks before fallback.
- Existing preserved run and benchmark evidence are not rewritten.

## Documentation artifact matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Approval freeze and before implementation commit | `homework-4/CHANGELOG.md` | Newest-first Homework 4 step entry |
| Test cases | Snapshot | No | Not applicable | N/A | This is a Markdown contract change; validation is documentation and artifact-contract review |
| Testing guide delta | Living delta | No | Not applicable | N/A | Implementation should update `homework-4/TESTING_GUIDE.md` directly |
| Operator manual delta | Living delta | No | Not applicable | N/A | Implementation should update `homework-4/HOWTORUN.md` directly |
| API reference delta | Living delta | No | Not applicable | N/A | Implementation should update `homework-4/API_REFERENCE.md` directly |
| Architecture snapshot | Snapshot | No | Not applicable | N/A | This spec captures the work-item-bound design decision |
| Architecture summary delta | Living delta | No | Not applicable | N/A | Implementation should update `homework-4/ARCHITECTURE.md` directly |

## Approval

- Status: Draft
- Superseded by: None

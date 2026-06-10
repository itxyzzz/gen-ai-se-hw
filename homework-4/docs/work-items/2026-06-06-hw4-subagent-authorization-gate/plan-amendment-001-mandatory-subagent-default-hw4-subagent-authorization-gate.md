# Plan Amendment 001: Mandatory Sub-Agent Default

Work ID: `2026-06-06-hw4-subagent-authorization-gate`
Short ID: `hw4-subagent-authorization-gate`
Status: Operator-directed amendment
Date: 2026-06-06

## Reason

The frozen spec and plan incorrectly treated missing operator authorization as
a normal pre-stage confirmation for all pipeline runs. Operator feedback on
2026-06-06 clarified that this is not the intended Homework 4 pipeline design.

## Corrected Contract

The Homework 4 pipeline is designed to run with sub-agents. The launch phrase
`Run HW4 pipeline` is sufficient pipeline intent and must not require extra
prompt keywords or a default confirmation before spawning sub-agents.

The orchestrator must use this decision sequence before `bug-researcher`:

1. If sub-agent spawning tooling is available, spawn one sub-agent per pipeline
   stage without asking for additional confirmation.
2. If the active tool can spawn sub-agents but refuses because it requires
   explicit operator authorization, stop and ask the operator to authorize
   sub-agent spawning. This is an authorization request for sub-agent use, not
   a fallback request.
3. If the operator authorizes spawning after the tool gate, continue with
   sub-agents.
4. If sub-agent tooling is simply unavailable, ask whether the operator
   explicitly approves direct execution fallback or wants the run blocked.
5. If authorization is declined or the tool still will not spawn after the
   authorization path, do not silently run directly. Direct fallback is allowed
   only with explicit operator fallback approval.

Not being authorized is not an excuse to skip sub-agents. The orchestrator must
ask for authorization. The only ordinary excuse for not creating sub-agents is
that the active tooling cannot provide sub-agent spawning at all.

## Impact

- Supersedes frozen wording that made extra sub-agent confirmation part of the
  happy path.
- Keeps the no-silent-fallback requirement.
- Keeps future-run-only metadata changes and does not rewrite preserved run or
  benchmark evidence.
- Updates `runtimeSubagentAudit.operatorAuthorization` to distinguish
  pipeline-mandated sub-agent spawning from explicit authorization requested by
  a restrictive tool.

## Approval Basis

This amendment is based on explicit operator correction in the implementation
thread. It is treated as the approval source for proceeding with the corrected
documentation implementation.

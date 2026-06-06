# Variance Log

Work ID: `2026-06-06-hw4-subagent-authorization-gate`

## Entries

### 2026-06-06 - Mandatory Sub-Agent Default Correction

Operator feedback corrected the frozen plan's authorization model. The pipeline
must spawn sub-agents by default from `Run HW4 pipeline`; extra confirmation is
only a recovery path when a tool refuses to spawn without explicit operator
authorization. Direct fallback remains forbidden unless sub-agent tooling is
unavailable, or the authorization path still cannot spawn sub-agents, and the
operator explicitly approves fallback.

Recorded in
`plan-amendment-001-mandatory-subagent-default-hw4-subagent-authorization-gate.md`.

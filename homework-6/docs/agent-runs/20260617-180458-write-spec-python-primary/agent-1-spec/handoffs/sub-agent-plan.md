# Sub-Agent Plan

Run ID: `20260617-180458-write-spec-python-primary`
Stack: `python`
Policy: `enterprise-default`
Integration owner: orchestration thread

## Runtime Controls

- Sub-agent runtime: available through Codex multi-agent tools.
- Model inheritance: sub-agents inherit the orchestration model unless the runtime applies its own default.
- Requested model profile: strong current Codex profile; latest strongest profile for final review if exposed.
- Requested reasoning: high for research, objectives, task decomposition, and final review. Exact reasoning controls are not exposed to this thread, so results are compensated by explicit handoffs and main-thread validation.
- Maximum concurrent sub-agents: 2; this run uses one required sub-agent per dependent wave to preserve traceability.

## Planned Waves

| Wave | Role | Scope | Context strategy | Required output | Parallel? |
|---|---|---|---|---|---|
| 1 | Domain research sub-agent | Research ISO 4217-style validation, `decimal.Decimal`, audit/log redaction, simulation boundaries, and later FastMCP/Context7 expectations. | Curated prompt with assignment facts, sample data, privacy rules, and source requirements. | `domain-research-handoff.md` plus accepted research-note entries. | No; single required research agent. |
| 2 | Objectives architect sub-agent | Shape one high-level objective and 4-5 testable mid-level objectives. | Curated prompt plus Task 1 requirements and domain-research handoff. | `objectives-handoff.md`. | No; depends on research. |
| 3 | Low-level task decomposition sub-agent | Produce one detailed task card per meta-agent with exact prompts, files, functions, edge cases, acceptance criteria, and verification. | Curated prompt plus stack profile, objectives, quality bar, and assignment deliverables. | `low-level-tasks-handoff.md`. | No; depends on objectives. |
| 4 | Final review sub-agent | Review candidate run outputs for assignment fit, stack specificity, privacy/audit handling, provenance, and task-card executability. | Curated prompt with candidate outputs, validation notes, and selection rules. | `review/final-review.md`. | No; depends on draft package. |

## Integration Rules

- The orchestration thread writes files, integrates accepted findings, validates the package, and applies first-run auto-selection.
- Supporting docs remain run evidence unless the operator explicitly selects them later.
- `agents.md`, pipeline code, tests, hooks, screenshots, `mcp/server.py`, `mcp.json`, and `.codex/config.toml` are outside this run's write scope.

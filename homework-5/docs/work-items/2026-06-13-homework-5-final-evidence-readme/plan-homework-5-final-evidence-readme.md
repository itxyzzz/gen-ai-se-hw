# Homework 5 Final Evidence And README Plan

Work ID: `2026-06-13-homework-5-final-evidence-readme`
Short ID: `homework-5-final-evidence-readme`
Status: Draft
Harness release: `0.3.0`
Schema: `schema:plan.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:models.strategy-required`, `rule:models.context-strategy`, `rule:models.approved-strategy-authorized`, `rule:models.fresh-confirmation`, `rule:lifecycle.variance-policy`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, `rule:freeze.stop-before-implementation`

## Implementation summary

This pass is a bounded final-prep pass for Homework 5. The implementation should first preserve the user's new screenshots in git, then make the README stand on its own as the reviewer-facing map of the submission. The README should not merely list files; it should map the requirements from `TASKS.md` to the actual GitHub, Filesystem, Notion, and custom FastMCP evidence available in the repository.

Two compliance risks should be resolved during implementation if authorized. First, `TASKS.md` requires a custom MCP Tool named `read`, while the current server exposes `read_lorem_ipsum`. Add `read` as the assignment-facing tool, and keep `read_lorem_ipsum` only as a compatibility alias if useful for existing screenshots. Second, `TASKS.md` asks for a `mcp.json` or `.mcp.json` all-server configuration. The current all-server local config is `homework-5/.codex/config.toml`, while `homework-5/mcp.json` only contains the custom server. Align `mcp.json` if practical without committing secrets, or document the distinction explicitly if the portable JSON cannot truthfully represent the verified local setup.

The final pass should update `homework-5/CHANGELOG.md` before the implementation commit, record variance against the prior Task 4 plan if the server tool name or config shape changes, and run focused checks. External MCP interactions that depend on user credentials should be treated as screenshot-backed evidence unless fresh live validation is available in the active environment.

## Files and interfaces

- Add to git: `homework-5/docs/screenshots/listing-mcp-servers.png`
- Add to git: `homework-5/docs/screenshots/task-1-using-github-mcp-1.png`
- Add to git: `homework-5/docs/screenshots/task-1-using-github-mcp-2.png`
- Add to git: `homework-5/docs/screenshots/task-2-using-filesystem-mcp.png`
- Add to git: `homework-5/docs/screenshots/task-3-using-notion-mcp.png`
- Add to git: `homework-5/docs/screenshots/task-4-using-custom-mcp-and-notion.png`
- Modify: `homework-5/README.md`
- Modify: `homework-5/CHANGELOG.md`
- Possibly modify: `homework-5/custom-mcp-server/server.py`
- Possibly modify: `homework-5/custom-mcp-server/test_word_count.py`
- Possibly modify: `homework-5/HOWTORUN.md`
- Possibly modify: `homework-5/mcp.json`
- Possibly modify: `homework-5/docs/work-items/2026-06-13-custom-fastmcp-server/implementation-notes/variance-log.md`

The assignment-facing MCP custom Tool should be `read` if code changes are made. The existing Resource URI should remain `lorem-ipsum://content{?word_count}` and continue to default to 30 words.

## Model and Sub-agent Strategy

Current orchestration: Codex local desktop session, model and reasoning effort not directly user-selected in this turn.
Fit assessment: Small/medium documentation and compliance pass with low implementation blast radius. The only possible code change is a narrow MCP tool alias, but the compliance judgment requires careful evidence review.
Recommended change: Use the active strong local reasoning profile for implementation and final review. No separate model change is required unless the operator wants a different policy after the freeze gate.

Sub-agents: None. The file set is small, evidence and docs are tightly coupled, and the cost of coordinating a separate reviewer would exceed the benefit. The orchestration thread should own final compliance judgment.

## Tasks

- [ ] Review `homework-5/TASKS.md`, `homework-5/README.md`, `homework-5/HOWTORUN.md`, `homework-5/mcp.json`, `homework-5/.codex/config.toml`, custom server files, and screenshot inventory.
- [ ] Inspect the new screenshots for basic readability and absence of obvious credentials before staging them.
- [ ] Stage or commit the six new screenshot files requested by the operator during the implementation pass.
- [ ] Add a newest-first `Homework 5 - Step 3` entry to `homework-5/CHANGELOG.md` describing screenshot evidence, README updates, and any compliance corrections.
- [ ] Update `homework-5/README.md` with author `Igor Tanatarov`, a four-task evidence matrix, configuration file notes, screenshot index, custom Resource and Tool details, verification commands, and final compliance notes.
- [ ] If authorized and still needed, add a FastMCP Tool named `read` that calls the existing shared `read_lorem_ipsum_words` helper.
- [ ] If the tool-name correction is made, add or update focused tests or import checks so the `read` wrapper is verified without requiring a live MCP client.
- [ ] If the tool-name correction is made, update `homework-5/HOWTORUN.md` to make `read` the assignment-facing tool and mention `read_lorem_ipsum` only as a compatibility alias if retained.
- [ ] If needed for deliverable compliance, update `homework-5/mcp.json` to register GitHub, Filesystem, Notion, and custom FastMCP without committing secret values; otherwise document why `.codex/config.toml` is the verified all-server config and `mcp.json` is portable/custom-only.
- [ ] Record variance in `homework-5/docs/work-items/2026-06-13-custom-fastmcp-server/implementation-notes/variance-log.md` for any change from `read_lorem_ipsum` to assignment-facing `read` or for MCP config shape changes that differ from the prior frozen plan.
- [ ] Run validation commands and capture results in the final report.
- [ ] Review `git diff -- homework-5` and `git status --short` for unrelated changes before final reporting or commit work.

## Validation commands

| Command | Expected result |
|---|---|
| `git status --short` | Shows only expected Homework 5 files, plus any unrelated ignored permission warning already known for `.pytest_cache` |
| `python -m pytest homework-5\custom-mcp-server\test_word_count.py -q` | Focused custom server tests pass |
| `python -m json.tool homework-5\mcp.json` | JSON config validates if `mcp.json` is present or changed |
| `python -c "import sys; sys.path.insert(0, r'homework-5\custom-mcp-server'); import server; print(server.read(5) if hasattr(server, 'read') else 'missing read')"` | Prints 5 words after the assignment-facing `read` wrapper is added; omit if no code change is authorized |
| `Select-String -Path homework-5\README.md -Pattern 'Igor Tanatarov','Task 1','Task 2','Task 3','Task 4','listing-mcp-servers.png','task-1-using-github-mcp-1.png','task-2-using-filesystem-mcp.png','task-3-using-notion-mcp.png','task-4-using-custom-mcp-and-notion.png'` | README contains author, task mapping, and screenshot index |
| `git diff -- homework-5` | Diff is limited to planned Homework 5 evidence, docs, small compliance corrections, changelog, and variance notes |

## Plan variance handling

Use `rule:lifecycle.variance-policy`. Before freeze, edit this draft directly for operator feedback. After freeze, record nontrivial implementation variance in the prior Task 4 variance log when the implementation intentionally diverges from the earlier frozen custom-server plan. Create a new plan amendment and stop for approval if review discovers a high-impact scope change, security/privacy issue, or a requirement that cannot be satisfied without significant new implementation.

## Planning artifact freeze gate

Draft review status: Draft package prepared for operator review; approval not yet granted.

Approval freeze status: Not frozen. After operator approval, update `homework-5/CHANGELOG.md` with a plan-only entry, verify no placeholders or unresolved required items, stage only this planning package and changelog, commit the plan-only checkpoint, and stop before implementation.

Implementation authorization status: Not authorized until a fresh operator instruction after the freeze gate explicitly says to begin implementation.

## Completion criteria

- Acceptance criteria in `spec-homework-5-final-evidence-readme.md` are met.
- Required validation commands have been run and recorded, or limitations are reported honestly.
- New screenshots are git-tracked after implementation authorization.
- README gives a clear requirement-by-requirement review of Homework 5.
- `homework-5/CHANGELOG.md` has newest-first entries for the plan freeze and implementation pass before their commits.
- Variance log is current if the prior Task 4 implementation contract changes.
- De-facto sub-agent use is reported. Expected value: none.

## Approval

- Status: Draft
- Superseded by: not applicable

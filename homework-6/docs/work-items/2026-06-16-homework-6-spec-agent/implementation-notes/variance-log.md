# Variance Log

Work ID: `2026-06-16-homework-6-spec-agent`

## 2026-06-17: Official Claude skill precedence verification

Class: Local technical

Variance:

- Kept the Claude Code `write-spec` surface as `.claude/skills/write-spec/SKILL.md` and removed the empty `.claude/commands/` directory.
- Updated stale planning and validation references from `.claude/commands/write-spec.md` to `.claude/skills/write-spec/SKILL.md`.
- Updated stale Codex-skill-local `references/` paths to the shared `agent-control/write-spec/` package.
- Fixed one trailing whitespace issue in the copied Homework 6 task file so repository whitespace checks do not fail on the assignment text.

Reason:

- Official Anthropic Claude Code docs at `https://code.claude.com/docs/en/skills` state that custom commands have been merged into skills, that project skills are directly invocable with `/<skill-name>` based on the skill directory name, and that a same-named skill takes precedence over a `.claude/commands/` file.
- Keeping an empty command directory or stale command references would confuse reviewers and future agents.

Impact:

- No assignment scope, public API, data model, security rule, or acceptance criterion changes.
- The Task 1 command surface remains `/write-spec`, implemented through the native Claude Code project skill.

## 2026-06-17: Post-review control-surface tightening

Class: Local technical

Variance:

- Removed fallback generation behavior from the Claude Code `/write-spec` wrapper and Codex skill wrapper, despite the Phase 01 plan originally asking the Claude command to include a fallback workflow.
- Reduced run-registry duplication by pointing to the shared workflow and quality-bar references instead of restating the Agent 1 run layout and comparison matrix.
- Replaced standing exact-model labels with policy-relative Codex and Claude Code model guidance so the workflow ages better while still requiring strong reasoning for final review and architecture-sensitive decisions.

Reason:

- The operator confirmed that the shared workflow files must be available and that attempting to generate a high-quality spec without them is not useful.
- The duplicated wrapper and registry content had already drifted from the shared workflow, creating a higher risk than a hard stop on missing references.

Impact:

- No assignment scope, public API, data model, security rule, or acceptance criterion changes.
- Agent 1 runs now fail earlier if core workflow references are missing, which is intentional.

## 2026-06-17: Tool-neutral write-spec reference package

Class: Local technical

Variance:

- Moved the canonical Agent 1 workflow, stack profile, quality bar, and run-registry rules from the Codex skill reference folder into `agent-control/write-spec/`.
- Replaced the Claude Code legacy command wrapper with a native project skill at `.claude/skills/write-spec/SKILL.md`.
- Kept both Codex and Claude Code entrypoints as thin wrappers around the tool-neutral package.

Reason:

- The operator confirmed there is no need for a transition period and requested removal of the command file.
- A neutral reference package avoids making Claude Code depend on Codex-owned paths and reduces cross-tool drift.

Impact:

- No assignment scope, public API, data model, security rule, or acceptance criterion changes.
- Claude Code now exposes `/write-spec` through the project skill directory instead of `.claude/commands/write-spec.md`.

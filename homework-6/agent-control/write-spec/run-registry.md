# Agent Run Registry Reference

This reference owns the preservation and selection rules for Homework 6 agent and pipeline runs under `docs/agent-runs/`.

## Sources of Truth

Use the rest of this package for Agent 1 behavior:

- Workflow, modes, run layout, sub-agent phases, and selection rules: `agent-control/write-spec/workflow.md`
- Stack enum and stack-specific defaults: `agent-control/write-spec/stack-profiles.md`
- Output, comparison, and review quality bar: `agent-control/write-spec/quality-bar.md`

If this reference conflicts with those files, update this reference and follow the more specific workflow or quality-bar rule.

The original Homework 6 repository references `specification-TEMPLATE-hint.md`, but that file is not present in this checkout. Agent 1 runs must record that absence in `run-metadata.md` and use the Task 1 section list plus Homework 3 references as the local template source unless the template file is later added.

## Run ID Format

Use:

```text
YYYYMMDD-HHMMSS-<agent-or-pipeline>-<stack>-<short-label>
```

Examples:

```text
20260616-173000-write-spec-python-primary
20260616-191500-write-spec-java-comparison
20260617-101000-pipeline-python-smoke
```

Use `python` or `java` for stack-specific Agent 1 runs. If a later run is not stack-specific, use the stack that produced or consumes the artifacts.

## Preservation Rules

- Draft generated outputs inside the run folder first.
- After the first successful Agent 1 generation run, if `homework-6/specification.md` does not exist yet, copy the run's `agent-1-spec/outputs/specification.md` to that canonical path automatically and record it in `final-selection.md`.
- If a canonical `specification.md` already exists, do not copy or overwrite canonical homework paths until a run is explicitly selected.
- Treat `specification.md` as the default selected package. Supporting docs may be copied only when explicitly selected.
- Do not copy a run-local agent guide over `homework-6/agents.md`; that file is the stable homework-level control surface.
- Keep `run-metadata.md` current with stack, mode, operator prompt, tool availability, context read, and validation commands.
- Keep `handoff.md` current even for complete runs; a complete run may state that no continuation is required.
- Avoid committing bulky transient directories, caches, coverage HTML, or raw screenshots unless a later phase explicitly selects them as evidence.

## Comparison and Selection

Compare runs with the criteria in `agent-control/write-spec/quality-bar.md`.

When comparing Python and Java runs, call out build-system complexity, MCP fit, coverage tooling, file layout, and implementation effort.

Selection process:

1. Review candidate run folders and update each `comparison.md`, except for first-run auto-selection when no canonical spec exists yet.
2. Choose one run for canonical copy, or confirm that first-run auto-selection applies.
3. Update `docs/agent-runs/final-selection.md` with date, run ID, stack, selected files, copied paths, rationale, operator, and post-selection edits.
4. Copy selected files to canonical homework paths. By default this means only `specification.md`.
5. Update `CHANGELOG.md` in the same increment.

## Commit Guidance

Commit selected run evidence when it helps the reviewer understand how the final package was produced. Leave exploratory, incomplete, or bulky troubleshooting runs uncommitted unless the operator asks to preserve them. Canonical homework files and final selection records should be committed once selected.

Context7 is configured in Phase 01. Add `pipeline-status` to both `mcp.json` and `.codex/config.toml` only after `mcp/server.py` exists.

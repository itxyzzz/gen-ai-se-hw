# Homework 5 Final Evidence And README Spec

Work ID: `2026-06-13-homework-5-final-evidence-readme`
Short ID: `homework-5-final-evidence-readme`
Status: Draft
Harness release: `0.3.0`
Schema: `schema:spec.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `rule:lifecycle.documentation-matrix`, `rule:quality.spec-handoff`

## Goal

Prepare Homework 5 for final review by adding the newly supplied MCP screenshots to git, checking the current submission against `homework-5/TASKS.md`, and updating `homework-5/README.md` with the required author, configuration, evidence, and requirement-coverage details.

## Scope

- Stage the new screenshot evidence already present under `homework-5/docs/screenshots/`.
- Review Homework 5 against `homework-5/TASKS.md`, `AGENTS.md`, and `HOMEWORK_STANDARDS.md`.
- Update `homework-5/README.md` so it clearly documents all four MCP tasks, author name, configuration locations, screenshot evidence, custom FastMCP Resource and Tool behavior, verification commands, and remaining reviewer notes.
- Update `homework-5/CHANGELOG.md` before the implementation commit.
- Make small requirement-alignment corrections if the compliance review finds assignment-blocking gaps that cannot be truthfully solved by README text alone.
- Verify the documented custom server checks and JSON/TOML configuration syntax where practical.

## Non-scope

- Creating or committing real GitHub, Notion, Jira, or other service credentials.
- Re-capturing screenshots unless the existing files are unreadable or missing required coverage.
- Publishing a PR, pushing the branch, or merging to `main`.
- Reworking prior frozen Task 4 planning artifacts except through variance notes or a new amendment if high-impact scope changes become necessary.
- Changing unrelated homework folders.

## Current state

The active branch is `homework-5-submission`, satisfying the repository guardrail that homework changes must not be implemented on `main`.

`homework-5/README.md` already contains the author name `Igor Tanatarov`, matching previous homework READMEs, and documents the custom FastMCP server. It does not yet list the newly added screenshots for Tasks 1 through 4, does not present a full assignment compliance checklist, and still frames the custom Tool as `read_lorem_ipsum` even though `homework-5/TASKS.md` requires a Tool named `read`.

`homework-5/mcp.json` currently registers only `custom-lorem-reader`. `homework-5/.codex/config.toml` registers `github`, `filesystem`, `notion`, and `custom-lorem-reader` for the local Codex project. The assignment deliverable asks for `mcp.json` or `.mcp.json` with all four servers registered, so the final README must clearly identify the verified local Codex config and, if implementation is authorized, the portable JSON config should be aligned if needed.

The following screenshot files are untracked and should be added during implementation:

- `homework-5/docs/screenshots/listing-mcp-servers.png`
- `homework-5/docs/screenshots/task-1-using-github-mcp-1.png`
- `homework-5/docs/screenshots/task-1-using-github-mcp-2.png`
- `homework-5/docs/screenshots/task-2-using-filesystem-mcp.png`
- `homework-5/docs/screenshots/task-3-using-notion-mcp.png`
- `homework-5/docs/screenshots/task-4-using-custom-mcp-and-notion.png`

The following screenshot files are already tracked:

- `homework-5/docs/screenshots/custom-mcp-codex-read-lorem-ipsum-result.png`
- `homework-5/docs/screenshots/custom-mcp-codex-validation-result.png`

## Proposed behavior

After implementation, Homework 5 should have a README that a reviewer can use as the entry point for the whole submission. The README should state the author, summarize the assignment, list each configured MCP server, point to the config files, list every screenshot evidence file, and include a concise compliance matrix mapped to Tasks 1 through 4 and the deliverables table.

The custom server should satisfy the assignment wording as closely as practical. If implementation proceeds, add a FastMCP Tool named `read` that uses the existing shared word-count helper. Keeping `read_lorem_ipsum` as a compatibility alias is acceptable if the README and HOWTORUN make `read` the assignment-facing tool and explain any legacy alias. This is a small API-surface correction, so the prior variance log should record the rationale if the frozen Task 4 plan remains in force.

The configuration story should be explicit and honest. If `mcp.json` is updated, it should register GitHub, Filesystem, Notion, and the custom server without committing secret values. Credentials should stay as environment variables or user-local auth. If the local `.codex/config.toml` remains the verified all-server config, the README should state that and explain that `mcp.json` is the portable custom-server config unless updated.

## Interfaces and data

- `homework-5/README.md`
  - Assignment overview and author name.
  - Task-by-task compliance matrix.
  - Server configuration section for GitHub, Filesystem, Notion, and custom FastMCP.
  - Screenshot evidence index with filenames and task mapping.
  - Custom Resource and Tool explanation.
  - Verification command summary.
- `homework-5/CHANGELOG.md`
  - Newest-first entry for this final evidence and README pass.
- `homework-5/custom-mcp-server/server.py`
  - Optional small correction to expose a Tool named `read` if authorized.
- `homework-5/custom-mcp-server/test_word_count.py`
  - Optional focused test coverage for the `read` wrapper if server.py changes.
- `homework-5/HOWTORUN.md`
  - Optional terminology update if the assignment-facing tool changes from `read_lorem_ipsum` to `read`.
- `homework-5/mcp.json`
  - Optional portable all-four-server registration update if required for deliverable compliance.
- `homework-5/docs/screenshots/*.png`
  - New screenshots should become tracked git files.
- `homework-5/docs/work-items/2026-06-13-custom-fastmcp-server/implementation-notes/variance-log.md`
  - Optional variance note for the `read` tool-name correction and any config alignment that differs from the prior frozen plan.

## Risks

- Screenshots may show local paths or service data. Review should ensure no credentials or sensitive private information are exposed before staging.
- Updating `mcp.json` for all four servers may require generic placeholders for machine-specific paths and credentials; the README must distinguish verified local config from portable examples.
- Adding `read` alongside `read_lorem_ipsum` changes the MCP tool surface. This is low blast radius but should be verified through tests or import-level inspection and documented as an assignment-alignment correction.
- The prior Task 4 plan intentionally used `read_lorem_ipsum`; changing the assignment-facing tool name should be recorded as variance rather than silently rewriting frozen planning artifacts.
- Some external MCP validation may not be reproducible from this sandbox because it depends on user credentials and client-specific sessions. The README should cite screenshot evidence and local config checks instead of claiming fresh live validation when not run.

## Acceptance criteria

- The six newly added screenshot files are staged or committed as git-tracked Homework 5 evidence after implementation authorization.
- `homework-5/README.md` includes the author name `Igor Tanatarov`.
- `homework-5/README.md` maps each Task 1 through Task 4 requirement to evidence, config, and implementation files.
- `homework-5/README.md` lists all screenshot evidence files, including the newly added screenshots.
- `homework-5/README.md` explains Resources and Tools in terms required by `TASKS.md`.
- `homework-5/README.md` clearly documents the MCP configuration files and whether each is verified local configuration or portable reviewer configuration.
- If implementation changes the custom server, a Tool named `read` is exposed and verified while preserving existing helper behavior.
- If implementation changes `mcp.json`, the JSON validates with `python -m json.tool homework-5\mcp.json`.
- Focused custom server tests pass with `python -m pytest homework-5\custom-mcp-server\test_word_count.py -q` when Python dependencies are available.
- `homework-5/CHANGELOG.md` has a newest-first entry for this pass before the implementation commit.
- The final status report calls out any remaining requirement that could not be freshly revalidated locally.

## Documentation artifact matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Before implementation commit | `homework-5/CHANGELOG.md` | Homework-specific newest-first entry for the final evidence and README pass |
| Test cases | Snapshot | No | Not applicable | Not applicable | Existing focused custom server tests cover the only likely code correction; validation commands are captured in the plan |
| Testing guide delta | Living delta | No | Not applicable | Not applicable | Homework 5 uses `HOWTORUN.md` rather than a separate testing guide |
| Operator manual delta | Living delta | No | Not applicable | Not applicable | Any runbook terminology update should be made directly in `homework-5/HOWTORUN.md` |
| API reference delta | Living delta | No | Not applicable | Not applicable | MCP Resource and Tool contracts should be documented directly in README/HOWTORUN |
| Architecture snapshot | Snapshot | No | Not applicable | Not applicable | No new architecture is planned; any tool-name variance is recorded in the prior Task 4 variance log |
| Architecture summary delta | Living delta | No | Not applicable | Not applicable | No long-lived architecture summary changes are planned |
| Variance log | Implementation notes | Conditional | During implementation | `homework-5/docs/work-items/2026-06-13-custom-fastmcp-server/implementation-notes/variance-log.md` | Required if the server tool name or config shape changes from the prior frozen plan |

## Approval

- Status: Draft
- Superseded by: not applicable

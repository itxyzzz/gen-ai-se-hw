# Custom FastMCP Server Spec

Work ID: `2026-06-13-custom-fastmcp-server`
Short ID: `custom-fastmcp-server`
Status: Draft
Harness release: `0.3.0`
Schema: `schema:spec.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `rule:lifecycle.documentation-matrix`, `rule:quality.spec-handoff`

## Goal

Implement Homework 5 Task 4: a custom FastMCP server that exposes dynamic word-limited content from `lorem-ipsum.md` through both an MCP Resource URI and a callable `read_lorem_ipsum` Tool, with reproducible setup, configuration, testing, documentation, and screenshot evidence.

## Scope

- Create the custom MCP server under `homework-5/custom-mcp-server/`.
- Add `server.py` using FastMCP.
- Add `lorem-ipsum.md` as the source content file.
- Add a Python dependency file under `custom-mcp-server/` that explicitly includes `fastmcp`.
- Add or update Homework 5 MCP configuration so the custom server is registered for Codex first and also documented for portable use by Claude Code, Antigravity, and other MCP-capable agents.
- Add or update Homework 5 documentation:
  - `README.md` with homework description, author name, and an explanation of Resources versus Tools.
  - `HOWTORUN.md` with install, run, connect, and `read_lorem_ipsum` testing instructions.
  - `CHANGELOG.md` with newest-first implementation entries before commits.
- Add real screenshot evidence under `homework-5/docs/screenshots/` for every local interaction Codex can actually perform, including successful custom MCP `read_lorem_ipsum` call evidence when the MCP client is connected.
- Mark Codex-created screenshots explicitly in README, HOWTORUN, or a screenshot index as `Added by Codex`.
- Add focused tests or executable smoke checks that verify the word-count behavior without requiring a full MCP client.

## Non-scope

- Implementing Tasks 1, 2, or 3 external MCP servers beyond preserving compatibility with a shared `mcp.json` or `.mcp.json` structure when present.
- Creating real GitHub, Filesystem, Jira, or Notion credentials.
- Capturing screenshots for Tasks 1, 2, or 3 unless the operator explicitly expands the scope.
- Publishing a PR or final submission branch merge.
- Changing repository-level harness policy, root workflow rules, or unrelated homework folders.

## Current state

`homework-5/` currently contains only `TASKS.md`. There is no custom MCP server folder, no MCP config, no Homework 5 README, no HOWTORUN, no CHANGELOG, and no screenshot evidence yet. The active branch is `homework-5-submission`, which satisfies the repository requirement to avoid homework implementation on `main`.

## Proposed behavior

The implementation should produce a standalone custom FastMCP server that is usable by Codex in the current development environment first, then documented as portable MCP configuration for Claude Code, Antigravity, Copilot, and other MCP-capable agents. The server should expose:

- A Resource URI pattern `lorem-ipsum://content{?word_count}` that accepts an optional `word_count` query parameter with a default of `30`.
- Documentation that presents the Resource behavior in the reviewer-friendly HTTP-style shape `GET /lorem-ipsum?word-count={count}`, while explaining that the MCP Resource URI uses `word_count` because FastMCP maps query parameters to Python identifiers.
- A Tool named `read_lorem_ipsum` that accepts the same optional `word_count` value and returns exactly that many words from `lorem-ipsum.md`.

The same word-count helper should back both the Resource and Tool so they cannot drift. If `word_count` is omitted, both paths should return exactly 30 words. If `word_count` is a valid non-negative integer within the available source-word range, both paths should return exactly that many words. Invalid query or tool input must fail clearly: blank values use the default, negative numbers, decimal values, non-numeric strings, booleans, and requests larger than the source length raise a validation error with a useful message. The source file should contain more than 60 words so default, custom, and upper-bound cases are meaningful.

## Interfaces and data

- `homework-5/custom-mcp-server/server.py`
  - Defines a FastMCP app, Resource, Tool, and shared content-reading helper.
  - Reads `lorem-ipsum.md` relative to `server.py`, not the caller's working directory.
  - Provides explicit validation through a helper such as `normalize_word_count`.
  - Provides an executable entry point, for example `if __name__ == "__main__": mcp.run()`.
- `homework-5/custom-mcp-server/lorem-ipsum.md`
  - Markdown text source with enough words for at least 50-word testing.
- `homework-5/custom-mcp-server/requirements.txt`
  - Includes `fastmcp`.
- `homework-5/mcp.json` or `homework-5/.mcp.json`
  - Registers the custom server command and arguments using local paths.
  - Should remain compatible with the eventual all-four-server deliverable from `TASKS.md`.
- `homework-5/README.md`
  - Includes description, author name, project structure, and brief Resources versus Tools explanation.
- `homework-5/HOWTORUN.md`
  - Includes installation commands, server startup command, Codex MCP configuration guidance, portable MCP configuration notes for other agents, and `read_lorem_ipsum` tool test steps.
- `homework-5/docs/screenshots/custom-mcp-codex-read-lorem-ipsum-result.png`
  - Real screenshot evidence of a successful MCP call result, marked as added by Codex.
- `homework-5/docs/screenshots/custom-mcp-codex-validation-result.png`
  - Real screenshot evidence of local validation or startup when Codex can capture it, marked as added by Codex.

## Risks

- FastMCP's decorator or Resource URI syntax can vary by installed package version, so implementation should verify against the installed version and update code or docs based on observed behavior.
- MCP client configuration fields differ between Claude Code, Claude Desktop, Copilot, and other clients; documentation should identify which configuration shape was verified locally.
- Codex-specific MCP configuration may differ from other clients; Codex must be the first verified target, and other client snippets must be documented as portable examples unless actually tested.
- The user-facing HTTP-style pattern uses `word-count`, while Python and FastMCP decorators should use `word_count`; documentation must make that mapping explicit.
- A naive word split may mishandle punctuation or Markdown formatting; this homework requires word count rather than semantic tokenization, so a documented whitespace split is acceptable.
- Returning fewer words than requested when the source file is too short would fail the Task 4 criterion; the lorem source should intentionally contain enough words for documented examples.
- Screenshots can accidentally expose local paths or personal workspace details; evidence should avoid credentials and sensitive project data.

## Acceptance criteria

- `homework-5/custom-mcp-server/server.py` defines a working FastMCP server.
- The Resource at `lorem-ipsum://content{?word_count}` reads `lorem-ipsum.md`, accepts the optional `word_count` query parameter, defaults to `30`, and returns exactly that many whitespace-delimited words.
- README or HOWTORUN documents the Resource as equivalent to `GET /lorem-ipsum?word-count={count}` for reviewers.
- The Tool named `read_lorem_ipsum` accepts optional `word_count` and returns the same content as the Resource for the same count.
- Invalid query or tool input is validated and fails clearly for negative numbers, decimal values, non-numeric strings, booleans, and counts larger than the source file length.
- `custom-mcp-server/requirements.txt` explicitly includes `fastmcp`.
- The custom server startup command works from documented instructions.
- Homework MCP configuration exists, is verified first in Codex, and points to the custom server command.
- Portable MCP configuration notes are included for other MCP-capable agents.
- Automated or scriptable verification demonstrates default 30-word output and at least one non-default word count.
- `README.md` explains the homework implementation and includes the required Resources versus Tools distinction.
- `HOWTORUN.md` explains dependency installation, running the server, Codex MCP connection configuration, portable MCP connection notes, and use or test of the `read_lorem_ipsum` tool.
- Real Codex-created screenshots that can be captured locally are present before final submission and explicitly marked as `Added by Codex`.
- `homework-5/CHANGELOG.md` is updated before each implementation commit.

## Documentation artifact matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Before each commit | `homework-5/CHANGELOG.md` | Homework-specific changelog, newest-first, required by repository instructions |
| Test cases | Snapshot | Yes | Before implementation | `homework-5/docs/work-items/2026-06-13-custom-fastmcp-server/snapshots/test-cases.snapshot.md` | Captures expected word-count and MCP behavior |
| Testing guide delta | Living delta | No | Not applicable | Not applicable | Homework 5 does not currently have a long-lived testing guide; HOWTORUN will carry reviewer test commands |
| Operator manual delta | Living delta | No | Not applicable | Not applicable | HOWTORUN is the required operator runbook for this homework |
| API reference delta | Living delta | No | Not applicable | Not applicable | MCP Resource and Tool contracts will be documented in README and HOWTORUN, not a separate API reference |
| Architecture snapshot | Snapshot | Yes | Before implementation | `homework-5/docs/work-items/2026-06-13-custom-fastmcp-server/snapshots/architecture.snapshot.md` | Records server, resource, tool, config, and evidence flow |
| Architecture summary delta | Living delta | No | Not applicable | Not applicable | No long-lived architecture summary exists for Homework 5 yet |
| Variance log | Implementation notes | Yes | During implementation | `homework-5/docs/work-items/2026-06-13-custom-fastmcp-server/implementation-notes/variance-log.md` | Records nontrivial drift after freeze |

## Approval

- Status: Draft
- Superseded by: not applicable

# Variance Log: Custom FastMCP Server

Work ID: `2026-06-13-custom-fastmcp-server`
Short ID: `custom-fastmcp-server`
Status: Active

## Instructions

After the planning package is frozen, append nontrivial implementation variance here. Do not rewrite frozen spec, plan, or snapshot content to hide drift.

Use a plan amendment and request operator approval before proceeding if variance affects architecture, MCP public interface, deliverable scope, security, privacy, acceptance criteria, or plan feasibility.

## Entries

### 2026-06-13: Assignment-facing `read` tool alias

- Variance: the custom server now exposes a FastMCP Tool named `read` in addition to the earlier `read_lorem_ipsum` tool from the frozen Task 4 plan.
- Reason: `homework-5/TASKS.md` explicitly requires a Tool named `read`; keeping `read_lorem_ipsum` preserves compatibility with existing screenshots and prior local evidence.
- Impact: low. Both tools call the same `read_lorem_ipsum_words` helper, so Resource behavior, validation behavior, source file handling, and existing evidence remain unchanged.
- Verification: `python -m pytest homework-5\custom-mcp-server\test_word_count.py -q` passed with coverage for `server.read(5)`.

### 2026-06-13: Portable all-server `mcp.json`

- Variance: `homework-5/mcp.json` now registers GitHub, Filesystem, Notion, and `custom-lorem-reader`, while the frozen Task 4 plan focused on the custom server config.
- Reason: the Homework 5 deliverables require an MCP configuration file with all four required servers registered. Secrets remain outside the repository through environment variables and local MCP session authentication.
- Impact: low. The custom server command remains unchanged; external server entries mirror the local Codex configuration shape and require reviewer-local credentials or paths where applicable.
- Verification: `python -m json.tool homework-5\mcp.json` validates the JSON syntax.

### 2026-06-13: MCP config working directory

- Variance: `homework-5/mcp.json` uses `cwd: "homework-5"` with `args: ["custom-mcp-server/server.py"]` instead of the draft example `cwd: "."`.
- Reason: the Codex workspace and validation commands run from the repository root, so this config is directly usable from the root while keeping the server path portable inside the homework folder.
- Impact: low. The public Resource URI, Tool name, validation behavior, file layout, and acceptance criteria are unchanged.
- Verification: `python -m json.tool homework-5\mcp.json` passed, and a FastMCP `Client(config)` call listed `read_lorem_ipsum` and returned five words from the configured stdio server.

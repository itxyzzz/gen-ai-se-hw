# Variance Log: Custom FastMCP Server

Work ID: `2026-06-13-custom-fastmcp-server`
Short ID: `custom-fastmcp-server`
Status: Active

## Instructions

After the planning package is frozen, append nontrivial implementation variance here. Do not rewrite frozen spec, plan, or snapshot content to hide drift.

Use a plan amendment and request operator approval before proceeding if variance affects architecture, MCP public interface, deliverable scope, security, privacy, acceptance criteria, or plan feasibility.

## Entries

### 2026-06-13: MCP config working directory

- Variance: `homework-5/mcp.json` uses `cwd: "homework-5"` with `args: ["custom-mcp-server/server.py"]` instead of the draft example `cwd: "."`.
- Reason: the Codex workspace and validation commands run from the repository root, so this config is directly usable from the root while keeping the server path portable inside the homework folder.
- Impact: low. The public Resource URI, Tool name, validation behavior, file layout, and acceptance criteria are unchanged.
- Verification: `python -m json.tool homework-5\mcp.json` passed, and a FastMCP `Client(config)` call listed `read_lorem_ipsum` and returned five words from the configured stdio server.

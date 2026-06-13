# Homework 5: MCP Servers

Author: Igor Tanatarov

## Overview

This homework configures MCP servers and implements a custom FastMCP server for Task 4. The custom server reads `custom-mcp-server/lorem-ipsum.md` and exposes word-limited content through both an MCP Resource and an MCP Tool.

```mermaid
flowchart LR
    Client["Codex or MCP client"] --> Config["homework-5/mcp.json"]
    Config --> Server["custom-lorem-reader FastMCP server"]
    Server --> Source["custom-mcp-server/lorem-ipsum.md"]
    Server --> Resource["Resource: lorem-ipsum://content{?word_count}"]
    Server --> Tool["Tool: read_lorem_ipsum"]
```

## Custom FastMCP Server

- Resources are URIs that Claude can read from, such as files, APIs, or generated content.
- Tools are actions Claude can call to perform operations, such as reading a file or running a command.
- The custom Resource is `lorem-ipsum://content{?word_count}`, documented for reviewers as equivalent to `GET /lorem-ipsum?word-count={count}`.
- The custom Tool is `read_lorem_ipsum`.
- The assignment example names the tool `read`; this implementation uses `read_lorem_ipsum` because the approved plan requires an informative matching tool name for portable MCP clients.

Both Resource and Tool paths use the same validation helper. If `word_count` is omitted or blank, the server returns 30 words. If `word_count` is a valid non-negative integer within the source length, it returns exactly that many whitespace-delimited words. Negative numbers, decimal values, non-numeric strings, booleans, and values larger than the source text fail with a clear validation error.

## Evidence

Screenshots marked `Added by Codex` were captured from real local Codex-visible validation or MCP interactions.

- `docs/screenshots/custom-mcp-codex-read-lorem-ipsum-result.png` - Added by Codex, successful `read_lorem_ipsum` MCP tool call evidence.
- `docs/screenshots/custom-mcp-codex-validation-result.png` - Added by Codex, local validation and startup evidence.

## Project Structure

```text
homework-5/
├── README.md
├── HOWTORUN.md
├── CHANGELOG.md
├── TASKS.md
├── mcp.json
├── custom-mcp-server/
│   ├── server.py
│   ├── lorem-ipsum.md
│   ├── requirements.txt
│   └── test_word_count.py
└── docs/
    ├── screenshots/
    │   ├── custom-mcp-codex-read-lorem-ipsum-result.png
    │   └── custom-mcp-codex-validation-result.png
    └── work-items/
        └── 2026-06-13-custom-fastmcp-server/
```

## Verification

Run the focused checks from the repository root:

```powershell
python -m pytest homework-5\custom-mcp-server\test_word_count.py -q
python -m json.tool homework-5\mcp.json
Select-String -Path homework-5\custom-mcp-server\requirements.txt -Pattern '^fastmcp\b'
```

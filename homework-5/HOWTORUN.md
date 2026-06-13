# How to Run Homework 5

## Install Dependencies

From the repository root:

```powershell
cd homework-5\custom-mcp-server
python -m venv .venv
.\.venv\Scripts\Activate.ps1
python -m pip install -r requirements.txt
```

For this Codex implementation pass, dependencies were installed in the active Python environment with:

```powershell
python -m pip install -r homework-5\custom-mcp-server\requirements.txt
```

## Run the Custom Server

From `homework-5\custom-mcp-server`:

```powershell
python server.py
```

The server uses FastMCP's default stdio transport. MCP clients normally start this command themselves from `mcp.json`, so the command may wait for MCP protocol messages when run directly.

## Connect MCP Configuration

The portable assignment configuration lives in `homework-5/mcp.json` and registers all four required servers:

```json
{
  "mcpServers": {
    "github": {
      "url": "https://api.githubcopilot.com/mcp/",
      "bearer_token_env_var": "GITHUB_PERSONAL_ACCESS_TOKEN_FOR_MCP"
    },
    "filesystem": {
      "command": "node",
      "args": [
        "C:\\Work\\Codex\\mcp-servers\\filesystem\\node_modules\\@modelcontextprotocol\\server-filesystem\\dist\\index.js",
        "C:\\Work\\Codex\\folder-mcp"
      ]
    },
    "notion": {
      "url": "https://mcp.notion.com/mcp"
    },
    "custom-lorem-reader": {
      "command": "python",
      "args": [
        "custom-mcp-server/server.py"
      ],
      "cwd": "homework-5"
    }
  }
}
```

When loading the config from the repository root, `cwd` points the client at `homework-5`, and the server script path is then `custom-mcp-server/server.py`. If a client resolves relative paths from the config file location instead, set `cwd` to `.` or replace it with the absolute path to `homework-5`.

The local Codex app configuration used for the captured evidence lives in `homework-5/.codex/config.toml`. It contains the same required server set and keeps credentials in local environment/session state rather than in committed files.

Portable MCP clients such as Claude Code, Claude Desktop, Antigravity, Copilot, or other MCP-capable agents may use a slightly different config location or field set. Keep the same server name, command, and script target where possible:

```json
{
  "mcpServers": {
    "custom-lorem-reader": {
      "command": "python",
      "args": [
        "custom-mcp-server/server.py"
      ],
      "cwd": "C:/Work/Codex/SETU-HW/gen-ai-se-hw/homework-5"
    }
  }
}
```

On another machine, replace the `cwd` value with that machine's absolute path to the `homework-5` folder.

## Test the read Tool

Ask the MCP client to call the assignment-facing `read` tool with no parameter:

```json
{}
```

Expected result: 30 whitespace-delimited words from `lorem-ipsum.md`.

Ask the MCP client to call `read` with a custom count:

```json
{
  "word_count": 12
}
```

Expected result: 12 whitespace-delimited words from `lorem-ipsum.md`.

Ask the MCP client to validate invalid input:

```json
{
  "word_count": "abc"
}
```

Expected result: a clear validation failure containing `word_count must be a non-negative integer`.

The older `read_lorem_ipsum` tool is kept as a compatibility alias for screenshots and prior local evidence, but `read` is the assignment-facing tool name.

The Resource is `lorem-ipsum://content{?word_count}`. For reviewers, this behaves like `GET /lorem-ipsum?word-count={count}`. The implementation uses `word_count` rather than `word-count` because FastMCP maps query parameters to Python function identifiers.

## Local Smoke Checks

Run these commands from the repository root:

```powershell
python -m pytest homework-5\custom-mcp-server\test_word_count.py -q
python -c "import sys; sys.path.insert(0, r'homework-5\custom-mcp-server'); import server; print(len(server.read().split())); print(len(server.read(7).split()))"
python -m json.tool homework-5\mcp.json
Select-String -Path homework-5\custom-mcp-server\requirements.txt -Pattern '^fastmcp\b'
```

Expected results: tests pass, the smoke check prints `30` and `7`, the JSON config formats successfully, and `fastmcp` is found in `requirements.txt`.

## Screenshots

- `docs/screenshots/listing-mcp-servers.png` - MCP server listing evidence.
- `docs/screenshots/task-1-using-github-mcp-1.png` - GitHub MCP interaction evidence.
- `docs/screenshots/task-1-using-github-mcp-2.png` - Additional GitHub MCP interaction evidence.
- `docs/screenshots/task-2-using-filesystem-mcp.png` - Filesystem MCP interaction evidence.
- `docs/screenshots/task-3-using-notion-mcp.png` - Notion MCP availability evidence.
- `docs/screenshots/task-4-using-custom-mcp-and-notion.png` - Custom Resource and Notion page creation evidence.
- `docs/screenshots/custom-mcp-codex-read-lorem-ipsum-result.png` - Added by Codex, captured from a real local MCP client call to `read_lorem_ipsum`.
- `docs/screenshots/custom-mcp-codex-validation-result.png` - Added by Codex, captured from real local validation and startup output.

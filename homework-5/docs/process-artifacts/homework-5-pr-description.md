# Homework 5: MCP Servers

## Summary

This PR completes Homework 5 by configuring the required MCP servers and adding a custom FastMCP server. The submission demonstrates GitHub, Filesystem, Notion, and custom MCP interactions, records screenshot evidence, and documents the reviewer setup and verification flow.

Submitted package surface:

| File or folder | Purpose |
| --- | --- |
| `homework-5/README.md` | Reviewer entry point with author, requirement coverage, server configuration notes, evidence map, Mermaid diagram, challenge notes, and verification commands |
| `homework-5/HOWTORUN.md` | Reproducible setup, run, MCP connection, tool-use, and smoke-check instructions |
| `homework-5/mcp.json` | Portable MCP configuration registering GitHub, Filesystem, Notion, and the custom FastMCP server |
| `homework-5/.codex/config.toml` | Codex project-scoped MCP configuration used for local evidence capture |
| `homework-5/custom-mcp-server/server.py` | Custom FastMCP server with Resource URI and callable Tools |
| `homework-5/custom-mcp-server/lorem-ipsum.md` | Source text read by the custom Resource and Tools |
| `homework-5/custom-mcp-server/requirements.txt` | Python dependency declaration including `fastmcp` |
| `homework-5/custom-mcp-server/test_word_count.py` | Focused tests for word-count validation and the assignment-facing `read` tool |
| `homework-5/docs/screenshots/` | Screenshot evidence for all required MCP servers and interactions |
| `homework-5/docs/work-items/` | Harness planning, snapshots, and variance records for the implementation and final evidence pass |
| `homework-5/CHANGELOG.md` | Homework-specific implementation history |

## Important Implementation Notes

- Configured the required MCP server set: GitHub, Filesystem, Notion, and a custom FastMCP server.
- Kept credentials out of the repository. GitHub uses `GITHUB_PERSONAL_ACCESS_TOKEN_FOR_MCP`; Notion authentication remains in the local MCP session.
- Added `homework-5/mcp.json` as the portable all-server configuration and `.codex/config.toml` as the Codex app project-scoped configuration used locally.
- Implemented `custom-lorem-reader` with Resource URI `lorem-ipsum://content{?word_count}`.
- Implemented the assignment-facing custom Tool named `read`, while preserving `read_lorem_ipsum` as a compatibility alias for earlier evidence.
- Both Resource and Tool paths share the same helper so default, custom, invalid, and over-limit word-count behavior cannot drift.
- Added tests for default 30-word output, custom word counts, string query values, zero words, invalid inputs, source-length limits, and the `read` wrapper.
- Preserved planning and variance records for the tool-name correction and all-server `mcp.json` alignment.

## AI Tools And Workflow

The work was completed primarily in the Codex app with Codex. Codex was used to implement the custom FastMCP server, write focused tests, update documentation, enforce the repository homework standards, and prepare the final PR description.

Because Homework 5 depends heavily on project-scoped MCP discovery, some verification had to happen in a Codex project opened directly at the `homework-5` folder. That project selection allowed the custom server and Notion MCP tools to be discoverable for screenshots. The main repository thread then synced the evidence back into git, updated README references, and verified the local files.

The workflow used local harness planning artifacts for the custom server and final evidence pass. For behavior changes, the `read` Tool correction was tested before implementation: the new test first failed because `server.read` did not exist, then passed after adding the wrapper.

## Challenge And Takeaway

The custom server can be runnable without being registered as a native Codex tool. A stdio MCP server only becomes available to Codex after Codex loads an active MCP configuration and initializes the server as a child process; starting `python server.py` separately or keeping a portable `mcp.json` nearby is not enough. Codex app project selection also matters: project-scoped MCP servers should live in `.codex/config.toml` under the active project path. For this homework, the scoped Codex config lives at `homework-5/.codex/config.toml`, so a thread started with `homework-5` as the selected project can discover the homework-specific server, while a thread started from a broader project root may not load that nested config. Some registered MCP tools may also appear only after tool discovery rather than in the initial visible tool list.

## Challenges And How They Were Addressed

- **Project-scoped MCP discovery**: The custom server was not discoverable from a broader repository project. This was addressed by adding `.codex/config.toml` under `homework-5` and capturing evidence from a Codex project opened directly at that folder.
- **Tool-name mismatch**: The initial custom server exposed `read_lorem_ipsum`, while the assignment required a Tool named `read`. The final server now exposes `read` and keeps `read_lorem_ipsum` as a compatibility alias.
- **All-server configuration requirement**: The first portable `mcp.json` only registered the custom server. It now registers GitHub, Filesystem, Notion, and the custom FastMCP server without committing secrets.
- **Notion evidence gap**: Earlier evidence showed Notion MCP availability but not the exact required issue/page request. The refreshed screenshot now shows the Notion MCP request and five associated Homework 5 issue pages.
- **Credential and privacy handling**: Screenshots and config avoid exposing tokens. Credential-dependent entries use environment variables or local MCP session state.

## Screenshots

MCP server discovery and server actions:

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-5-submission/homework-5/docs/screenshots/listing-mcp-servers.png" alt="MCP server listing showing required servers" width="600">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-5-submission/homework-5/docs/screenshots/listing-mcp-server-actions.png" alt="Notion MCP server action listing" width="600">

GitHub MCP evidence:

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-5-submission/homework-5/docs/screenshots/task-1-using-github-mcp-1.png" alt="GitHub MCP branch and PR interaction part 1" width="600">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-5-submission/homework-5/docs/screenshots/task-1-using-github-mcp-2.png" alt="GitHub MCP branch and PR interaction part 2" width="600">

Filesystem MCP evidence:

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-5-submission/homework-5/docs/screenshots/task-2-using-filesystem-mcp.png" alt="Filesystem MCP write and read interaction" width="600">

Notion MCP evidence:

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-5-submission/homework-5/docs/screenshots/task-3-using-notion-mcp.png" alt="Notion MCP request returning five Homework 5 issue pages" width="600">

Custom MCP evidence:

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-5-submission/homework-5/docs/screenshots/task-4-using-custom-mcp-%28and-notion%29.png" alt="Custom MCP resource result and Notion page creation" width="600">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-5-submission/homework-5/docs/screenshots/custom-mcp-codex-read-lorem-ipsum-result.png" alt="Custom MCP tool call result" width="600">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-5-submission/homework-5/docs/screenshots/custom-mcp-codex-validation-result.png" alt="Custom MCP local validation result" width="600">

Additional screenshot artifacts are available in `homework-5/docs/screenshots/`.

## How To Run And Verify

Review the package in this order:

1. `homework-5/README.md`
2. `homework-5/TASKS.md`
3. `homework-5/HOWTORUN.md`
4. `homework-5/mcp.json`
5. `homework-5/.codex/config.toml`
6. `homework-5/custom-mcp-server/server.py`
7. `homework-5/docs/screenshots/`

Run focused checks from the repository root:

```powershell
python -m pytest homework-5\custom-mcp-server\test_word_count.py -q
python -m json.tool homework-5\mcp.json
python -c "import sys; sys.path.insert(0, r'homework-5\custom-mcp-server'); import server; print(server.read(5))"
Select-String -Path homework-5\custom-mcp-server\requirements.txt -Pattern '^fastmcp\b'
```

Expected results:

- The pytest command reports all focused custom server tests passing.
- `mcp.json` formats successfully as JSON.
- The `server.read(5)` smoke check prints five words from `lorem-ipsum.md`.
- `fastmcp` is found in `requirements.txt`.

More detailed setup and reviewer instructions are in `homework-5/HOWTORUN.md`. The reviewer entry point is `homework-5/README.md`, and the implementation history is in `homework-5/CHANGELOG.md`.

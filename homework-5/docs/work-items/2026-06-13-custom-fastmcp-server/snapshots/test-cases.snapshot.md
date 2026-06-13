# Test Cases Snapshot: Custom FastMCP Server

Work ID: `2026-06-13-custom-fastmcp-server`
Short ID: `custom-fastmcp-server`
Status: Draft

## Purpose

This snapshot records the expected behavior to verify before and during implementation of Homework 5 Task 4.

## Automated helper checks

| Case | Input | Expected result | Verification |
|---|---|---|---|
| Default word count | `read_lorem_ipsum_words()` | Exactly 30 whitespace-delimited words | `python -m pytest homework-5\custom-mcp-server\test_word_count.py -q` |
| Custom word count | `read_lorem_ipsum_words(12)` | Exactly 12 whitespace-delimited words | `python -m pytest homework-5\custom-mcp-server\test_word_count.py -q` |
| String query value | `read_lorem_ipsum_words("7")` | Exactly 7 whitespace-delimited words | `python -m pytest homework-5\custom-mcp-server\test_word_count.py -q` |
| Blank query value | `normalize_word_count("")` | Defaults to `30` | `python -m pytest homework-5\custom-mcp-server\test_word_count.py -q` |
| Zero words | `read_lorem_ipsum_words(0)` | Empty string with zero words | `python -m pytest homework-5\custom-mcp-server\test_word_count.py -q` |
| Negative count | `normalize_word_count(-1)` | Raises `ValueError` with `word_count must be a non-negative integer` | `python -m pytest homework-5\custom-mcp-server\test_word_count.py -q` |
| Non-numeric query value | `normalize_word_count("abc")` | Raises `ValueError` with `word_count must be a non-negative integer` | `python -m pytest homework-5\custom-mcp-server\test_word_count.py -q` |
| Decimal query value | `normalize_word_count("12.5")` | Raises `ValueError` with `word_count must be a non-negative integer` | `python -m pytest homework-5\custom-mcp-server\test_word_count.py -q` |
| Count larger than source | `read_lorem_ipsum_words(1000)` | Raises `ValueError` with `word_count cannot exceed available words` | `python -m pytest homework-5\custom-mcp-server\test_word_count.py -q` |
| Shared helper consistency | Resource and Tool both call `read_lorem_ipsum_words` | No duplicate word-count logic | Code review plus MCP smoke test |

## MCP checks

| Case | Input | Expected result | Evidence |
|---|---|---|---|
| Tool default call | `read_lorem_ipsum` with no parameters | Exactly 30 words from `lorem-ipsum.md` | Codex MCP client response or screenshot |
| Tool custom call | `read_lorem_ipsum` with `{"word_count": 12}` | Exactly 12 words from `lorem-ipsum.md` | `docs/screenshots/custom-mcp-codex-read-lorem-ipsum-result.png` marked `Added by Codex` |
| Tool invalid call | `read_lorem_ipsum` with `{"word_count": "abc"}` | Clear validation error containing `word_count must be a non-negative integer` | Codex MCP client response or screenshot |
| Resource explicit count | `lorem-ipsum://content?word_count=12` | Exactly 12 words from `lorem-ipsum.md` | MCP client response or documented smoke output |
| Reviewer-facing route shape | `GET /lorem-ipsum?word-count=12` in docs | Documents the equivalent URL-like pattern requested by the operator | README or HOWTORUN review |
| Server startup | Documented startup command | Server starts without import, dependency, or path errors | Terminal output or screenshot if useful |
| Config validity | `homework-5/mcp.json` | Valid JSON and command points to `custom-mcp-server/server.py` | `python -m json.tool homework-5\mcp.json` |

## Documentation checks

| Document | Required content | Verification |
|---|---|---|
| `README.md` | Description, author name, Resources explanation, Tools explanation | Manual review and `Select-String` phrase check |
| `HOWTORUN.md` | Install dependencies, run server, connect Codex MCP config, portable config notes, test `read_lorem_ipsum` tool | Manual review and `Select-String` phrase check |
| `CHANGELOG.md` | Newest-first Homework 5 implementation entry before commit | Manual review before commit |

## Evidence privacy checks

- Screenshots must not include credentials, tokens, private issue content, or unrelated local secrets.
- Screenshots created by Codex must be real captured evidence and explicitly marked `Added by Codex` in README, HOWTORUN, or a screenshot index.
- Local filesystem paths are acceptable when needed to show MCP configuration, but avoid exposing unnecessary personal directories.
- The Jira or Notion evidence required by other tasks is outside this Task 4 planning scope.

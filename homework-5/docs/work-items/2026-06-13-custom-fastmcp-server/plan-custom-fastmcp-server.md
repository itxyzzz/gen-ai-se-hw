# Custom FastMCP Server Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use `superpowers:subagent-driven-development` (recommended) or `superpowers:executing-plans` to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build the Homework 5 Task 4 custom FastMCP server and documentation deliverables.

**Architecture:** A small Python FastMCP server will own the MCP Resource and Tool. A shared helper validates the requested word count, reads `lorem-ipsum.md` relative to `server.py`, normalizes whitespace, and returns the requested number of words so Resource and Tool behavior remains identical. Homework documentation and MCP config will describe how Codex uses the server first, then how other MCP-capable agents can connect portably.

**Tech Stack:** Python, FastMCP, JSON MCP configuration, Markdown documentation, PowerShell validation commands.

---

Work ID: `2026-06-13-custom-fastmcp-server`
Short ID: `custom-fastmcp-server`
Status: Draft
Harness release: `0.3.0`
Schema: `schema:plan.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:models.strategy-required`, `rule:models.context-strategy`, `rule:models.approved-strategy-authorized`, `rule:models.fresh-confirmation`, `rule:lifecycle.variance-policy`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, `rule:freeze.stop-before-implementation`

## Implementation summary

Implement the custom MCP server as a contained Homework 5 package at `homework-5/custom-mcp-server/`. The core behavior should be written test-first around validation and content helpers such as `normalize_word_count(word_count: object = 30, available_words: int | None = None) -> int` and `read_lorem_ipsum_words(word_count: object = 30) -> str`, then exposed through FastMCP Resource and Tool decorators.

The implementation should favor simple, reviewable homework deliverables over framework abstraction. The Resource and Tool should share one helper, the lorem source file should be long enough for positive and upper-bound examples, and the MCP config should be verified in Codex first with portable notes for Claude Code, Antigravity, Copilot, and other MCP clients.

Documentation is part of the deliverable, not an afterthought. `README.md` should describe what was built and explain MCP Resources versus Tools. `HOWTORUN.md` should be a practical reviewer runbook with exact dependency, startup, Codex configuration, portable configuration, and smoke-test commands. Real screenshot evidence should be captured for each local interaction Codex can perform and explicitly marked as `Added by Codex`.

## Files and interfaces

- Create `homework-5/CHANGELOG.md`: homework-specific newest-first change log before implementation commits.
- Create `homework-5/README.md`: overview, author name, project structure, Resource versus Tool explanation, and evidence links.
- Create `homework-5/HOWTORUN.md`: install, run, Codex MCP config, portable MCP config notes, and use/test instructions.
- Create `homework-5/mcp.json` or `homework-5/.mcp.json`: custom server MCP registration; preserve room for other Homework 5 servers.
- Create `homework-5/custom-mcp-server/server.py`: FastMCP server, Resource, informative Tool, shared validation helper, shared word-count helper.
- Create `homework-5/custom-mcp-server/lorem-ipsum.md`: source Markdown with at least 60 whitespace-delimited words.
- Create `homework-5/custom-mcp-server/requirements.txt`: includes `fastmcp`.
- Create `homework-5/docs/screenshots/`: evidence folder.
- Add `homework-5/docs/screenshots/custom-mcp-codex-read-lorem-ipsum-result.png`: successful Codex MCP result screenshot before final submission.
- Add `homework-5/docs/screenshots/custom-mcp-codex-validation-result.png`: real Codex-captured local validation or startup screenshot before final submission when available.

The external interface is the MCP Resource URI and the `read_lorem_ipsum` Tool. The planned Resource URI is `lorem-ipsum://content{?word_count}`, using FastMCP query-parameter syntax so `word_count` can be omitted and default to `30`. Documentation should describe the reviewer-facing shape as equivalent to `GET /lorem-ipsum?word-count={count}` and state that the implementation uses `word_count` because FastMCP maps query parameters to Python identifiers.

## Model and Sub-agent Strategy

Current orchestration: Codex local desktop session, GPT-5.5 High.
Fit assessment: Small/medium homework implementation with moderate package/API uncertainty around FastMCP syntax and low blast radius. Main-thread execution is sufficient after plan approval.
Recommended change: Use strong reasoning for implementation and final review if the local environment exposes model controls, because FastMCP behavior and MCP config need careful verification.

Sub-agents: None for planned implementation. The work is compact, file ownership is tightly coupled, and review quality can be handled by the orchestration thread. Add a read-only reviewer only if implementation discovers FastMCP version ambiguity or MCP-client configuration uncertainty that cannot be resolved quickly.

## Tasks

### Task 1: Create baseline homework structure and changelog

**Files:**
- Create: `homework-5/CHANGELOG.md`
- Create: `homework-5/custom-mcp-server/`
- Create: `homework-5/docs/screenshots/`

- [ ] **Step 1: Create the homework changelog**

Use this initial content:

```markdown
# Homework 5 Changelog

## Homework 5 - Step 1: Custom MCP server implementation

### Added

- Added the custom FastMCP server package, local MCP configuration, and reviewer documentation for Task 4.

### Tests

- Verified the custom server helper returns exact word counts before committing the implementation.
```

- [ ] **Step 2: Create required folders**

Create `homework-5/custom-mcp-server/` and `homework-5/docs/screenshots/`.

- [ ] **Step 3: Review git status**

Run:

```powershell
git status --short
```

Expected: only Homework 5 files and the already approved planning package are modified or untracked.

### Task 2: Add source text and dependency file

**Files:**
- Create: `homework-5/custom-mcp-server/lorem-ipsum.md`
- Create: `homework-5/custom-mcp-server/requirements.txt`

- [ ] **Step 1: Add lorem source content**

Create `lorem-ipsum.md` with more than 60 words. Use a simple single-paragraph source so whitespace splitting is predictable:

```markdown
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer vitae lectus at massa facilisis luctus. Donec finibus, neque in pretium posuere, arcu libero varius justo, vitae luctus nibh nunc sed urna. Suspendisse potenti. Curabitur at magna sed velit feugiat porta. Aliquam erat volutpat. Praesent feugiat sem a neque viverra, non commodo sapien gravida. Morbi dignissim, risus at tincidunt placerat, lorem justo luctus turpis, at efficitur augue orci vitae purus.
```

- [ ] **Step 2: Add FastMCP dependency**

Create `requirements.txt`:

```text
fastmcp
```

- [ ] **Step 3: Verify dependency declaration**

Run:

```powershell
Select-String -Path homework-5\custom-mcp-server\requirements.txt -Pattern '^fastmcp\b'
```

Expected: one matching line containing `fastmcp`.

### Task 3: Write test-first word-count verification

**Files:**
- Create: `homework-5/custom-mcp-server/test_word_count.py`
- Later modify: `homework-5/custom-mcp-server/server.py`

- [ ] **Step 1: Write a focused helper test**

Create `test_word_count.py`:

```python
import pytest

from server import normalize_word_count, read_lorem_ipsum_words


def _word_count(text: str) -> int:
    return len(text.split())


def test_read_lorem_ipsum_words_defaults_to_30_words() -> None:
    result = read_lorem_ipsum_words()
    assert _word_count(result) == 30


def test_read_lorem_ipsum_words_accepts_custom_word_count() -> None:
    result = read_lorem_ipsum_words(12)
    assert _word_count(result) == 12


def test_read_lorem_ipsum_words_accepts_string_query_value() -> None:
    result = read_lorem_ipsum_words("7")
    assert _word_count(result) == 7


def test_read_lorem_ipsum_words_accepts_zero_word_count() -> None:
    assert read_lorem_ipsum_words(0) == ""


def test_normalize_word_count_defaults_blank_query_value() -> None:
    assert normalize_word_count("") == 30


@pytest.mark.parametrize("invalid", [-1, "abc", "12.5", True])
def test_normalize_word_count_rejects_invalid_query_values(invalid: object) -> None:
    with pytest.raises(ValueError, match="word_count must be a non-negative integer"):
        normalize_word_count(invalid)


def test_read_lorem_ipsum_words_rejects_count_larger_than_source() -> None:
    with pytest.raises(ValueError, match="word_count cannot exceed available words"):
        read_lorem_ipsum_words(1000)
```

- [ ] **Step 2: Run the test before implementation**

Run:

```powershell
python -m pytest homework-5\custom-mcp-server\test_word_count.py -q
```

Expected: failure because `server.py`, `normalize_word_count`, or `read_lorem_ipsum_words` does not exist yet.

### Task 4: Implement the FastMCP server

**Files:**
- Create: `homework-5/custom-mcp-server/server.py`

- [ ] **Step 1: Implement shared helper, Resource, Tool, and entry point**

Create `server.py` with this baseline:

```python
from pathlib import Path

from fastmcp import FastMCP


mcp = FastMCP("custom-lorem-reader")
LOREM_PATH = Path(__file__).with_name("lorem-ipsum.md")


def normalize_word_count(word_count: object = 30, available_words: int | None = None) -> int:
    if word_count is None or word_count == "":
        count = 30
    elif isinstance(word_count, bool):
        raise ValueError("word_count must be a non-negative integer")
    else:
        text_value = str(word_count).strip()
        if not text_value.isdecimal():
            raise ValueError("word_count must be a non-negative integer")
        count = int(text_value)

    if available_words is not None and count > available_words:
        raise ValueError("word_count cannot exceed available words")

    return count


def read_lorem_ipsum_words(word_count: object = 30) -> str:
    words = LOREM_PATH.read_text(encoding="utf-8").split()
    count = normalize_word_count(word_count, available_words=len(words))
    return " ".join(words[:count])


@mcp.resource("lorem-ipsum://content{?word_count}")
def lorem_resource(word_count: str | int = 30) -> str:
    return read_lorem_ipsum_words(word_count)


@mcp.tool(name="read_lorem_ipsum")
def read_lorem_ipsum(word_count: str | int = 30) -> str:
    return read_lorem_ipsum_words(word_count)


if __name__ == "__main__":
    mcp.run()
```

- [ ] **Step 2: Run focused tests**

Run:

```powershell
python -m pytest homework-5\custom-mcp-server\test_word_count.py -q
```

Expected: all tests pass.

- [ ] **Step 3: Run import smoke check**

Run:

```powershell
python -c "import sys; sys.path.insert(0, r'homework-5\custom-mcp-server'); import server; print(len(server.read_lorem_ipsum_words().split())); print(len(server.read_lorem_ipsum_words(7).split()))"
```

Expected output:

```text
30
7
```

### Task 5: Add MCP configuration

**Files:**
- Create: `homework-5/mcp.json` or `homework-5/.mcp.json`

- [ ] **Step 1: Create custom-server MCP config**

Create a config file using the MCP client shape verified for the Codex environment first. Use the same server name and command in portable examples for Claude Code, Antigravity, Copilot, or other MCP-capable agents when their config shape differs. For a JSON MCP config that starts the server from the homework folder, use:

```json
{
  "mcpServers": {
    "custom-lorem-reader": {
      "command": "python",
      "args": [
        "custom-mcp-server/server.py"
      ],
      "cwd": "."
    }
  }
}
```

If the verified Codex MCP config does not support `cwd`, replace the command or argument with an absolute path in the implementation step and document the reason in `HOWTORUN.md`. If another client requires a different wrapper, add that as a clearly labeled portable example rather than changing the Codex-verified config.

- [ ] **Step 2: Validate JSON syntax**

Run:

```powershell
python -m json.tool homework-5\mcp.json
```

Expected: formatted JSON is printed and the command exits successfully.

### Task 6: Write reviewer documentation

**Files:**
- Create: `homework-5/README.md`
- Create: `homework-5/HOWTORUN.md`

- [ ] **Step 1: Write README**

Include:

```markdown
# Homework 5: MCP Servers

Author: Student name provided by the operator before final submission

## Overview

This homework configures MCP servers and implements a custom FastMCP server for Task 4.

## Custom FastMCP Server

The custom server reads `custom-mcp-server/lorem-ipsum.md` and exposes the content through a Resource and a Tool.

- Resources are URIs that Claude can read from, such as files, APIs, or generated content.
- Tools are actions Claude can call to perform operations, such as reading a file or running a command.
- The custom Resource is `lorem-ipsum://content{?word_count}`, documented for reviewers as equivalent to `GET /lorem-ipsum?word-count={count}`.
- The custom Tool is `read_lorem_ipsum`.

## Evidence

Screenshots marked `Added by Codex` were captured from real local Codex-visible validation or MCP interactions.

## Project Structure

List the final files under `homework-5/`, including `custom-mcp-server/server.py`, `requirements.txt`, `mcp.json`, and `docs/screenshots/`.
```

The author name is a required operator-provided value. If implementation starts without that name in the prompt or existing repository context, stop before finalizing `README.md` and ask the operator for the exact author line.

- [ ] **Step 2: Write HOWTORUN**

Include exact sections and command content:

- `# How to Run Homework 5`
- `## Install Dependencies`
- Dependency commands:

```powershell
cd homework-5\custom-mcp-server
python -m venv .venv
.\.venv\Scripts\Activate.ps1
python -m pip install -r requirements.txt
```

- `## Run the Custom Server`
- Startup command:

```powershell
python server.py
```

- `## Connect MCP Configuration`
- Explain where Codex should load or reference `homework-5/mcp.json`, then provide portable notes for Claude Code, Antigravity, Copilot, or other clients.
- Heading `## Test the read_lorem_ipsum Tool`
- Ask the MCP client to call the `read_lorem_ipsum` tool with no parameter and with `word_count: 12`.
- Expected: the default call returns 30 words and the custom call returns 12 words.

- [ ] **Step 3: Check documentation for required phrases**

Run:

```powershell
Select-String -Path homework-5\README.md -Pattern 'Resources are URIs','Tools are actions'
Select-String -Path homework-5\HOWTORUN.md -Pattern 'Install Dependencies','Run the Custom Server','Connect MCP Configuration','Test the read_lorem_ipsum Tool'
```

Expected: each required phrase appears at least once.

### Task 7: Verify startup and capture evidence

**Files:**
- Add: `homework-5/docs/screenshots/custom-mcp-codex-read-lorem-ipsum-result.png`
- Add when locally available: `homework-5/docs/screenshots/custom-mcp-codex-validation-result.png`
- Modify: `homework-5/HOWTORUN.md` if observed commands differ
- Modify: `homework-5/CHANGELOG.md` if additional verification notes are useful

- [ ] **Step 1: Install dependencies**

Run from `homework-5/custom-mcp-server/`:

```powershell
python -m pip install -r requirements.txt
```

Expected: `fastmcp` is installed or already satisfied.

- [ ] **Step 2: Start the server using the documented command**

Run:

```powershell
python server.py
```

Expected: the server starts without import or configuration errors. Stop it after confirming startup if the command stays attached.

- [ ] **Step 3: Connect through Codex MCP client**

Use the documented Codex MCP client configuration and call the `read_lorem_ipsum` tool with:

```json
{
  "word_count": 12
}
```

Expected: the response contains exactly 12 whitespace-delimited words from `lorem-ipsum.md`.

- [ ] **Step 4: Verify invalid query or tool input**

Call the `read_lorem_ipsum` tool with:

```json
{
  "word_count": "abc"
}
```

Expected: the response fails clearly with a validation message containing `word_count must be a non-negative integer`.

- [ ] **Step 5: Capture successful MCP screenshot evidence**

Save the successful client call screenshot as:

```text
homework-5/docs/screenshots/custom-mcp-codex-read-lorem-ipsum-result.png
```

Expected: the screenshot shows the request or tool call and the returned content. It must be a real screenshot, must not expose credentials, and must be marked `Added by Codex` in README, HOWTORUN, or a screenshot index.

- [ ] **Step 6: Capture local validation screenshot if Codex can capture it**

Save a real screenshot of Codex-visible validation or startup output as:

```text
homework-5/docs/screenshots/custom-mcp-codex-validation-result.png
```

Expected: the screenshot shows real command output from this implementation and is marked `Added by Codex`.

### Task 8: Final review and commit

**Files:**
- Review all Homework 5 files changed for Task 4.

- [ ] **Step 1: Run final validation commands**

Run:

```powershell
python -m pytest homework-5\custom-mcp-server\test_word_count.py -q
python -m json.tool homework-5\mcp.json
Select-String -Path homework-5\custom-mcp-server\requirements.txt -Pattern '^fastmcp\b'
```

Expected: tests pass, JSON validates, and `fastmcp` is found.

- [ ] **Step 2: Review diff**

Run:

```powershell
git diff -- homework-5
git status --short
```

Expected: the diff contains only planned Homework 5 Task 4 changes, documentation, changelog, evidence, and the frozen planning package.

- [ ] **Step 3: Commit implementation after plan approval**

Run:

```powershell
git add homework-5
git commit -m "feat(homework-5): add custom FastMCP server"
```

Expected: commit succeeds using an allowed private or noreply email address.

## Validation commands

| Command | Expected result |
|---|---|
| `python -m pytest homework-5\custom-mcp-server\test_word_count.py -q` | Focused helper tests pass for default, custom, and invalid word counts |
| `python -c "import sys; sys.path.insert(0, r'homework-5\custom-mcp-server'); import server; print(len(server.read_lorem_ipsum_words().split())); print(len(server.read_lorem_ipsum_words(7).split()))"` | Prints `30` and `7` on separate lines |
| `python -m json.tool homework-5\mcp.json` | MCP configuration is valid JSON |
| `Select-String -Path homework-5\custom-mcp-server\requirements.txt -Pattern '^fastmcp\b'` | Finds the explicit FastMCP dependency |
| Manual MCP client call to `read_lorem_ipsum` with `{"word_count": 12}` | Returns exactly 12 words and screenshot evidence is saved |
| Manual MCP client call to `read_lorem_ipsum` with `{"word_count": "abc"}` | Fails clearly with `word_count must be a non-negative integer` |

## Plan variance handling

Use `rule:lifecycle.variance-policy`. Before freeze, edit this draft directly for operator feedback. After freeze, record nontrivial implementation variance in `implementation-notes/variance-log.md`; use a plan amendment for high-impact architecture, API, data, security, privacy, compliance, scope, acceptance-criteria, or feasibility changes.

Expected low-impact variances include an MCP config path shape change required by the verified local client. Record those in the variance log with the observed reason and verification command. Stop for operator approval if the Resource, Tool name, word-count contract, dependency strategy, or deliverable locations need to change.

## Planning artifact freeze gate

Draft review status: Draft package prepared for operator review; approval not yet granted.

Approval freeze status: Not frozen. After operator approval, update `homework-5/CHANGELOG.md`, verify no placeholders or unresolved required items, stage only approved planning artifacts and changelog, commit the plan-only checkpoint, and stop before implementation.

Implementation authorization status: Not authorized until a fresh operator instruction after the freeze gate explicitly says to begin implementation.

## Completion criteria

- Acceptance criteria in `spec-custom-fastmcp-server.md` are met.
- Required validation commands have been run and recorded.
- Required documentation artifacts have been created or updated.
- `homework-5/CHANGELOG.md` has a newest-first entry before each commit.
- Variance log is present and current.
- De-facto sub-agent use is reported when applicable, including count, roles/scopes, concurrency or waves, context strategy, observed inheritance behavior, and de-facto model/model class/profile when known.

## Approval

- Status: Draft
- Superseded by: not applicable

# Pipeline Status MCP Variance Log

Work ID: `2026-06-20-pipeline-status-mcp`
Short ID: `pipeline-status-mcp`

## 2026-06-20 - Local MCP Package Import Bridge

Class: Local technical variance

Plan expectation: create `mcp/__init__.py` as a short package marker and import test helpers from `mcp.server`.

Observed issue: the assignment-required path `mcp/server.py` shares the top-level package name used by the installed MCP SDK. Importing `mcp.server` from the test process resolves through the local homework package first and can shadow SDK imports that FastMCP needs, such as `mcp.server.session` and top-level `LoggingLevel`.

Decision: keep the required `mcp/server.py` script path, but make local tests load that script by file path under a neutral module name. Keep `mcp/__init__.py` as a compatibility bridge that orders the installed MCP SDK package path before the local folder for SDK submodule imports, while leaving the script executable as `python mcp/server.py`.

Impact: no public MCP surface changes. `mcp.json` and `.codex/config.toml` still launch the server with `python mcp/server.py`. The variance prevents local test imports from breaking FastMCP internals and keeps the implementation within the approved Task 4 scope.

## 2026-06-20 - Coverage Gate Temp Directory Isolation

Class: Local technical variance

Plan expectation: run the existing `scripts/check_coverage_gate.py --fail-under 80` helper as verification without changing it.

Observed issue: the first coverage-gate verification was accidentally run in parallel with another pytest process. The helper used a fixed `tmp/coverage-gate` directory, leaving an inaccessible stale coverage file on Windows. Subsequent sequential gate runs failed before test collection when coverage tried to erase the stale file.

Decision: keep the same public helper command and threshold behavior, but make the helper use a PID-scoped directory under `tmp/coverage-gate-<pid>` for each invocation.

Impact: no hook or coverage threshold semantics change. The helper remains the same command surface, still runs pytest coverage with `--cov-fail-under`, and now avoids stale temp-directory collisions during local or tool-driven verification.

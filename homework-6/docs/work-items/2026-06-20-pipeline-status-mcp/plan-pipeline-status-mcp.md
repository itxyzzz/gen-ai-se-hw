# Pipeline Status MCP Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use `superpowers:subagent-driven-development` only if the operator explicitly wants parallel review; otherwise use `superpowers:executing-plans` to implement this plan task-by-task after the harness freeze gate and a fresh explicit operator instruction. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add the custom read-only FastMCP `pipeline-status` server required by Homework 6 Task 4 Part 2 and configure it beside the existing Context7 MCP server.

**Architecture:** Implement ordinary pure helper functions in `mcp/server.py` for reading `shared/results/` and shaping safe payloads, then expose those helpers through FastMCP decorators. Validate helper behavior through focused pytest coverage before updating `mcp.json` and `.codex/config.toml` to start the MCP server with `python mcp/server.py`.

**Tech Stack:** Python, FastMCP, pytest, existing Homework 6 JSON result files, existing `mcp.json`, existing `.codex/config.toml`, existing coverage helper `scripts/check_coverage_gate.py`.

---

Work ID: `2026-06-20-pipeline-status-mcp`
Short ID: `pipeline-status-mcp`
Status: Approved
Harness release: `unknown`
Schema: `schema:plan.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `module:models`, `module:freeze-gate`, `rule:models.strategy-required`, `rule:models.context-strategy`, `rule:models.approved-strategy-authorized`, `rule:models.fresh-confirmation`, `rule:lifecycle.variance-policy`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, `rule:freeze.stop-before-implementation`

## Implementation Summary

This implementation completes the remaining Task 4 gap. Context7 is already configured and documented in `research-notes.md`; the missing work is the custom MCP server and combined MCP configuration.

The server should be read-only. It should not call `integrator.run_pipeline`, mutate `shared/`, archive runtime files, or import runtime pipeline components. It should inspect the latest generated result evidence under `shared/results/`, shape stable safe payloads, and expose them through two tools plus one resource.

The implementation should be test-first where possible:

1. Add tests for pure helper behavior using temporary `shared/results/` directories.
2. Add `mcp/server.py` helpers and FastMCP decorators.
3. Verify tests pass.
4. Update `mcp.json` and `.codex/config.toml` after `mcp/server.py` exists.
5. Run existing selected tests and coverage gate.

If FastMCP is not installed locally, the implementation should still keep tests focused on helper logic and server import shape. If import fails during verification, add a minimal dependency declaration such as `requirements.txt` with `fastmcp`, `pytest`, and `pytest-cov`, then rerun import/tests after installing dependencies if allowed by the operator.

## Files And Interfaces

Create:

- `mcp/__init__.py`: marks the local MCP package.
- `mcp/server.py`: FastMCP server, helper functions, tool decorators, resource decorator, `mcp.run()` entrypoint.
- `tests/test_mcp_server.py`: focused MCP helper and privacy tests.

Modify:

- `mcp.json`: add `pipeline-status` beside existing `context7`.
- `.codex/config.toml`: add `[mcp_servers.pipeline-status]` beside existing Context7 config.
- `CHANGELOG.md`: add newest-first entries during planning freeze and implementation commits.

Conditional:

- `requirements.txt`: create only if local verification shows there is no dependency declaration and `fastmcp` is not available.

Interfaces to preserve:

- Existing `context7` config in `mcp.json`.
- Existing `[mcp_servers.context7]` config and `[agents] max_threads = 8` in `.codex/config.toml`.
- Existing selected transaction result JSON schema under `shared/results/`.
- Existing generated pipeline behavior and tests.

## Model And Sub-agent Strategy

Current orchestration: Codex Desktop thread; concrete model and reasoning effort are not exposed in the shell environment.
Fit assessment: Small/medium implementation with modest privacy and configuration risk. The code is compact, but it affects MCP startup and exposes generated banking-pipeline data, so careful helper tests and privacy checks matter.
Recommended change: Use the strongest available reasoning profile for implementation if model/reasoning controls are exposed. If controls are unavailable, compensate with test-first implementation, scope scans, and explicit diff review.

Sub-agents: None. The implementation is compact and file-coupled; a single orchestration thread should own final integration. A read-only reviewer sub-agent may be requested after implementation only if the operator wants extra review of MCP/privacy behavior before commit.

## Tasks

- [ ] Confirm preflight state.
  Run `git branch --show-current` from `C:\Work\Codex\SETU-HW\gen-ai-se-hw\homework-6` and confirm `homework-6-submission`. Run `git status --short --untracked-files=all`, ignoring only the known `.pytest_cache` permission warning if it appears without file changes. Read `TASKS.md` Task 4, `agents.md` Research and MCP Rules, `docs/agent-runs/final-selection.md`, `mcp.json`, `.codex/config.toml`, `shared/results/summary.json`, and one representative transaction result such as `shared/results/TXN001.json`.

- [ ] Write failing tests in `tests/test_mcp_server.py`.
  Add tests that create temporary result directories rather than relying on root `shared/`. Include helper fixtures for a minimal `summary.json` and transaction result files. Test cases must cover:
  - `get_transaction_status_payload("TXN001", results_dir=tmp_path)` returns `found=True`, status `settled`, and `SETTLEMENT_SIMULATED`.
  - `get_transaction_status_payload("UNKNOWN", results_dir=tmp_path)` returns `found=False` and `TRANSACTION_RESULT_NOT_FOUND`.
  - invalid transaction ID `../summary` returns `found=False` and `INVALID_TRANSACTION_ID`.
  - `list_pipeline_results_payload(results_dir=tmp_path)` returns summary counts and sorted compact transaction views.
  - `build_summary_text(results_dir=tmp_path)` includes counts and the simulation notice.
  - serialized tool/resource payloads do not contain raw sample account IDs or raw descriptions.

  Use this import shape so implementation helpers stay testable:

  ```python
  from mcp.server import (
      build_summary_text,
      get_transaction_status_payload,
      list_pipeline_results_payload,
  )
  ```

- [ ] Run the focused MCP tests and confirm they fail for missing module/functions.
  Run `python -m pytest tests/test_mcp_server.py -q`. Expected result before implementation: import failure for `mcp.server` or missing helper functions. If FastMCP import fails before helper tests can run, continue to implementation and dependency check because the failure is still a useful red signal.

- [ ] Create `mcp/__init__.py`.
  Add a short module docstring such as:

  ```python
  """MCP support surfaces for the educational transaction-processing pipeline."""
  ```

- [ ] Implement `mcp/server.py` helper constants and imports.
  Use `from fastmcp import FastMCP`, `json`, `re`, `Path`, and `Any`. Define `mcp = FastMCP("pipeline-status")`, `PROJECT_ROOT`, `RESULTS_DIR`, `TRANSACTION_ID_RE = re.compile(r"^[A-Za-z0-9_-]+$")`, and the educational simulation notice copied from `integrator.py`.

- [ ] Implement safe JSON and transaction helper functions in `mcp/server.py`.
  Add:

  ```python
  def load_json(path: Path) -> dict[str, Any]:
      data = json.loads(Path(path).read_text(encoding="utf-8"))
      if not isinstance(data, dict):
          return {}
      return data
  ```

  Add a transaction ID validator that rejects empty values and path traversal characters. Add `result_file_for(transaction_id, results_dir)` that returns `Path(results_dir) / f"{transaction_id}.json"` only after validation passes.

- [ ] Implement `safe_transaction_view(result: dict[str, Any]) -> dict[str, Any]`.
  Return only allowlisted fields:

  ```python
  {
      "transaction_id": result.get("transaction_id", ""),
      "status": result.get("status", "unknown"),
      "reason_codes": list(result.get("reason_codes", [])),
      "risk_score": result.get("risk_score", 0),
      "risk_level": result.get("risk_level", "unknown"),
      "amount": result.get("amount", ""),
      "currency": result.get("currency", ""),
      "processed_at": result.get("processed_at", ""),
      "safe_summary": result.get("safe_summary", {}),
      "component_count": len(result.get("component_history", [])),
      "audit_event_count": len(result.get("audit_events", [])),
      "simulation_notice": SIMULATION_NOTICE,
  }
  ```

  Normalize `reason_codes` to an empty list when not a list. Normalize `safe_summary` to an empty dict when not a dict.

- [ ] Implement `get_transaction_status_payload`.
  For invalid IDs, return:

  ```python
  {
      "found": False,
      "transaction_id": transaction_id,
      "reason_code": "INVALID_TRANSACTION_ID",
      "simulation_notice": SIMULATION_NOTICE,
  }
  ```

  For missing files, return `TRANSACTION_RESULT_NOT_FOUND`. For present files, merge `{"found": True}` with `safe_transaction_view(result)`.

- [ ] Implement `list_pipeline_results_payload`.
  Read `summary.json` if present. Iterate sorted `TXN*.json` files and build safe views. Return:

  ```python
  {
      "found": True,
      "summary": {
          "runtime_run_id": summary.get("runtime_run_id", ""),
          "generated_at": summary.get("generated_at", ""),
          "total_transactions": summary.get("total_transactions", len(transactions)),
          "settled": summary.get("settled", 0),
          "rejected": summary.get("rejected", 0),
          "review_required": summary.get("review_required", 0),
          "error": summary.get("error", 0),
      },
      "result_count": len(transactions),
      "transactions": transactions,
      "simulation_notice": SIMULATION_NOTICE,
  }
  ```

  If no summary and no result files exist, return `found=False`, `reason_code="PIPELINE_RESULTS_NOT_FOUND"`, `result_count=0`, `transactions=[]`, and the simulation notice.

- [ ] Implement `build_summary_text`.
  Use `list_pipeline_results_payload` and return human-readable text with one field per line. Include `Pipeline run summary`, `Runtime run ID`, `Generated at`, `Total transactions`, `Settled`, `Rejected`, `Review required`, `Error`, and `Simulation notice`. If no results exist, return text that includes `Pipeline results not found` and the safe reason code.

- [ ] Add FastMCP decorators and entrypoint.
  Add:

  ```python
  @mcp.tool
  def get_transaction_status(transaction_id: str) -> dict[str, Any]:
      """Return the latest safe status for one processed transaction."""
      return get_transaction_status_payload(transaction_id)

  @mcp.tool
  def list_pipeline_results() -> dict[str, Any]:
      """Return a safe summary of all latest processed transaction results."""
      return list_pipeline_results_payload()

  @mcp.resource("pipeline://summary", mime_type="text/plain")
  def pipeline_summary() -> str:
      """Return the latest pipeline run summary as text."""
      return build_summary_text()

  if __name__ == "__main__":
      mcp.run()
  ```

- [ ] Run focused MCP tests.
  Run `python -m pytest tests/test_mcp_server.py -q`. Expected result: all MCP helper tests pass. If `ModuleNotFoundError: No module named 'fastmcp'` occurs, create or update a minimal dependency declaration and rerun after dependency installation is available.

- [ ] Add dependency declaration only if needed.
  If no project dependency file exists and `python -c "import fastmcp"` fails, create `requirements.txt` with `fastmcp`, `pytest`, and `pytest-cov`. Do not pin versions unless local tooling requires a pin. If network access is required to install, request approval for dependency installation instead of bypassing the sandbox.

- [ ] Update `mcp.json`.
  Preserve the existing `context7` object exactly and add:

  ```json
  "pipeline-status": {
    "command": "python",
    "args": ["mcp/server.py"]
  }
  ```

  Validate the JSON parses after editing.

- [ ] Update `.codex/config.toml`.
  Preserve `sandbox_mode`, `[mcp_servers.context7]`, and `[agents] max_threads = 8`. Add:

  ```toml
  [mcp_servers.pipeline-status]
  command = "python"
  args = ["mcp/server.py"]
  ```

  Keep the config readable and avoid moving unrelated settings.

- [ ] Run full selected test suite.
  Run `python -m pytest -p no:cacheprovider`. Expected result: all selected tests plus the new MCP tests pass. Current baseline before this work was 41 passing tests after Step 29 and 40 passing tests in final selection before Step 29; the exact post-change count should increase by the number of new MCP tests.

- [ ] Run coverage gate.
  Run `python scripts/check_coverage_gate.py --fail-under 80`. Expected result: exits 0 with total coverage at or above 80 percent.

- [ ] Run static config and privacy checks.
  Run `python -m json.tool mcp.json` and expect valid formatted JSON output. Run a PowerShell check that imports or reads `mcp/server.py` without starting the server where practical, such as `python -c "import mcp.server; print(mcp.server.mcp.name if hasattr(mcp.server.mcp, 'name') else 'pipeline-status')"`. Run a privacy scan over MCP test output or helper payload serialization to confirm raw sample account IDs and descriptions from `sample-transactions.json` are absent.

- [ ] Review protected-file scope.
  Run `git diff -- TASKS.md specification.md research-notes.md integrator.py agents tests/test_common.py tests/test_fraud_detector.py tests/test_integrator_pipeline.py tests/test_settlement_processor.py tests/test_themis_quality.py tests/test_transaction_validator.py`. Expected result: no diff except the intentionally new `tests/test_mcp_server.py` when using broader test path inspection.

- [ ] Update `CHANGELOG.md` before the implementation commit.
  Add a newest-first entry for the Task 4 custom MCP implementation. Include created server/config/test files and verification commands. Do not claim screenshots or Clio final docs are complete.

- [ ] Final diff review.
  Run `git diff --check` and expect no whitespace errors. Run `git status --short --untracked-files=all` and confirm only expected implementation paths are dirty.

## Validation Commands

| Command | Expected result |
|---|---|
| `git branch --show-current` | Prints `homework-6-submission`. |
| `git status --short --untracked-files=all` | Shows only expected planning or implementation files; known `.pytest_cache` permission warnings are noted separately if present. |
| `python -m pytest tests/test_mcp_server.py -q` before implementation | Fails because `mcp.server` or planned helper functions do not exist yet. |
| `python -m pytest tests/test_mcp_server.py -q` after implementation | Passes all focused MCP helper tests. |
| `python -m pytest -p no:cacheprovider` | Passes the selected suite plus new MCP tests. |
| `python scripts/check_coverage_gate.py --fail-under 80` | Passes with total coverage at or above 80 percent. |
| `python -m json.tool mcp.json` | Parses successfully and includes both `context7` and `pipeline-status`. |
| `python -c "import mcp.server; print('pipeline-status')"` | Imports the server module without launching `mcp.run()` and prints `pipeline-status`. |
| `git diff -- TASKS.md specification.md research-notes.md integrator.py agents` | Prints no diff. |
| `git diff --check` | Exits 0 with no whitespace errors. |

## Plan Variance Handling

Use `rule:lifecycle.variance-policy`. Before freeze, edit this draft directly for operator feedback. After freeze, record nontrivial implementation variance in `implementation-notes/variance-log.md`; use a plan amendment for high-impact architecture, API, data, security, privacy, compliance, scope, acceptance-criteria, or feasibility changes.

Likely local variance: FastMCP may be absent locally and may require a dependency file or installation approval. This is local technical variance if the MCP server still exposes the same tools/resource and tests validate helper behavior. Replacing FastMCP with a different MCP framework is a scope change and requires operator approval because Task 4 explicitly asks for a custom FastMCP server.

## Planning Artifact Freeze Gate

Use `module:freeze-gate`, `rule:freeze.draft-review`, `rule:freeze.approval-freeze`, and `rule:freeze.stop-before-implementation`.

Draft review status: Approved by operator on 2026-06-20.
Approval commit: Created by the planning freeze commit; commit hash is reported in the freeze-gate completion response.
Post-freeze implementation authorization: Not granted.

## Completion Criteria

- Acceptance criteria in `spec-pipeline-status-mcp.md` are met.
- Required validation commands have been run and recorded.
- `CHANGELOG.md` has a newest-first entry before each commit.
- Variance log is present and current if nontrivial variance occurs.
- De-facto sub-agent use is reported when applicable; this plan currently authorizes no sub-agents for implementation.

## Approval

- Status: Approved by operator on 2026-06-20
- Superseded by: None

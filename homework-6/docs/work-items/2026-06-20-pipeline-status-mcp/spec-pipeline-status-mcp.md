# Pipeline Status MCP Spec

Work ID: `2026-06-20-pipeline-status-mcp`
Short ID: `pipeline-status-mcp`
Status: Approved
Harness release: `unknown`
Schema: `schema:spec.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `rule:lifecycle.documentation-matrix`, `rule:quality.spec-handoff`

## Goal

Complete Homework 6 Task 4 Part 2 by adding a read-only custom FastMCP server that makes the selected transaction-processing pipeline's latest results queryable through MCP tools and a summary resource, while preserving the existing Context7 MCP setup from Task 4 Part 1.

## Scope

- Add a Python FastMCP server at `mcp/server.py`.
- Expose tool `get_transaction_status(transaction_id: str)` that reads the latest transaction result from `shared/results/`.
- Expose tool `list_pipeline_results()` that summarizes all processed transaction result files in `shared/results/`.
- Expose resource `pipeline://summary` that returns the latest pipeline run summary as text.
- Add focused tests for result parsing, missing-result behavior, summary text, privacy safety, and FastMCP surface registration where practical.
- Update `mcp.json` to configure both `context7` and `pipeline-status` after `mcp/server.py` exists.
- Update `.codex/config.toml` to configure the local `pipeline-status` MCP server after `mcp/server.py` exists, matching the standing Homework 6 agent guide.
- Update supporting project metadata or dependency notes only if local inspection shows FastMCP is not otherwise installable for reviewers.
- Update `CHANGELOG.md` during the approval freeze and implementation commits, newest-first.

## Non-scope

- Do not regenerate Athena (Spec Writer), Hephaestus (Code Generator), or Themis (Test Generator) artifacts.
- Do not overwrite or reselect canonical generated code, canonical tests, `specification.md`, `research-notes.md`, or `docs/agent-runs/final-selection.md`.
- Do not implement Clio (Documentation Generator), final README/HOWTORUN documentation, final PR narrative, or screenshot capture in this work item.
- Do not modify the banking pipeline's business rules, result schema, or runtime status counts unless a failing test proves the MCP server cannot read the current selected output contract.
- Do not log or expose raw account identifiers, raw descriptions, raw metadata payloads, credentials, tokens, or secrets.
- Do not make the custom MCP server mutate `shared/`, run the pipeline automatically, archive results, or perform payment, banking, legal, AML, sanctions, KYC, PCI, or payment-network compliance decisions.
- Do not change the frozen assignment file `TASKS.md`.

## Current State

The selected Homework Automation Layer artifacts are:

- Athena (Spec Writer): `20260619-170102-write-spec-python-fresh`.
- Hephaestus (Code Generator): `20260619-175211-generate-code-python-fresh-spec`.
- Themis (Test Generator): `20260620-144025-generate-tests-python-fresh-spec`.

`mcp.json` currently configures only `context7`:

```json
{
  "mcpServers": {
    "context7": {
      "command": "npx",
      "args": ["-y", "@upstash/context7-mcp@latest"]
    }
  }
}
```

`.codex/config.toml` currently configures only the matching Context7 server plus `agents.max_threads = 8`.

There is no `mcp/` directory yet. The latest root pipeline run has safe, redacted result files under `shared/results/`, including:

- `summary.json` with `total_transactions=8`, `settled=2`, `rejected=2`, `review_required=4`, `error=0`, `runtime_run_id`, and `simulation_notice`.
- `pipeline-status.json` with compact status counts.
- `TXN001.json` through `TXN008.json` with final result records containing fields such as `transaction_id`, `status`, `reason_codes`, `risk_score`, `risk_level`, `amount`, `currency`, `processed_at`, `safe_summary`, `component_history`, and `audit_events`.

The existing result contract already excludes raw sample account identifiers and raw descriptions. The MCP server should preserve that privacy boundary by reading and returning only these generated result files, never `sample-transactions.json`.

Context7 documentation lookup on 2026-06-20 resolved FastMCP as `/prefecthq/fastmcp`. The current documented minimal pattern is:

```python
from fastmcp import FastMCP

mcp = FastMCP("MyServer")

@mcp.tool
def hello(name: str) -> str:
    return f"Hello, {name}!"

if __name__ == "__main__":
    mcp.run()
```

The same docs show resource decorators such as:

```python
@mcp.resource("config://app", mime_type="application/json")
def app_config() -> str:
    return "{\"debug\": false}"
```

This work item should apply that current decorator/run shape.

## Proposed Behavior

Add a small read-only Python package under `mcp/`:

```text
mcp/
  __init__.py
  server.py
```

`mcp/server.py` should use FastMCP and keep business logic in ordinary helper functions so tests can validate behavior without launching a long-running MCP subprocess:

```python
from __future__ import annotations

from pathlib import Path
from typing import Any

from fastmcp import FastMCP

mcp = FastMCP("pipeline-status")
PROJECT_ROOT = Path(__file__).resolve().parents[1]
RESULTS_DIR = PROJECT_ROOT / "shared" / "results"
SIMULATION_NOTICE = (
    "Educational simulation only; no real payment, banking, legal, AML, sanctions, "
    "KYC, PCI, or payment-network compliance determination is performed."
)
```

The implementation should include these testable helpers:

- `load_json(path: Path) -> dict[str, Any]`
- `result_file_for(transaction_id: str, results_dir: Path = RESULTS_DIR) -> Path`
- `safe_transaction_view(result: dict[str, Any]) -> dict[str, Any]`
- `get_transaction_status_payload(transaction_id: str, results_dir: Path = RESULTS_DIR) -> dict[str, Any]`
- `list_pipeline_results_payload(results_dir: Path = RESULTS_DIR) -> dict[str, Any]`
- `build_summary_text(results_dir: Path = RESULTS_DIR) -> str`

The public tool/resource surface should be:

```python
@mcp.tool
def get_transaction_status(transaction_id: str) -> dict[str, Any]:
    return get_transaction_status_payload(transaction_id)

@mcp.tool
def list_pipeline_results() -> dict[str, Any]:
    return list_pipeline_results_payload()

@mcp.resource("pipeline://summary", mime_type="text/plain")
def pipeline_summary() -> str:
    return build_summary_text()

if __name__ == "__main__":
    mcp.run()
```

`get_transaction_status_payload` should:

- Accept only non-empty string transaction IDs matching a conservative pattern such as `^[A-Za-z0-9_-]+$`.
- Return a safe error payload instead of raising for missing transaction IDs or missing result files.
- Read only `shared/results/<transaction_id>.json`.
- Return `found=True` plus safe fields when present:
  - `transaction_id`
  - `status`
  - `reason_codes`
  - `risk_score`
  - `risk_level`
  - `amount`
  - `currency`
  - `processed_at`
  - `safe_summary`
  - `component_count`
  - `audit_event_count`
  - `simulation_notice`
- Return `found=False`, `transaction_id`, and a safe `reason_code` such as `TRANSACTION_RESULT_NOT_FOUND` when absent.

`list_pipeline_results_payload` should:

- Read `summary.json` when present.
- Iterate only `TXN*.json` result files, sorted by transaction ID.
- Return summary counts and a compact `transactions` list of safe transaction views.
- Return `result_count` matching the number of transaction result files found.
- Return a safe `reason_code` such as `PIPELINE_RESULTS_NOT_FOUND` if no summary or result files exist.

`pipeline://summary` should return stable text suitable for humans and screenshots, for example:

```text
Pipeline run summary
Runtime run ID: 69e78c66-c543-4393-b244-f10e84ca8287
Generated at: 2026-06-20T14:37:33Z
Total transactions: 8
Settled: 2
Rejected: 2
Review required: 4
Error: 0
Simulation notice: Educational simulation only; no real payment, banking, legal, AML, sanctions, KYC, PCI, or payment-network compliance determination is performed.
```

The server should avoid importing from `integrator.py` or runtime agent modules. That keeps MCP status reads independent from pipeline execution and avoids side effects on `shared/` or `archive/`.

Update `mcp.json` after `mcp/server.py` exists:

```json
{
  "mcpServers": {
    "context7": {
      "command": "npx",
      "args": ["-y", "@upstash/context7-mcp@latest"]
    },
    "pipeline-status": {
      "command": "python",
      "args": ["mcp/server.py"]
    }
  }
}
```

Update `.codex/config.toml` after `mcp/server.py` exists:

```toml
sandbox_mode = "workspace-write"

[mcp_servers.context7]
command = "npx"
args = ["-y", "@upstash/context7-mcp@latest"]

[mcp_servers.pipeline-status]
command = "python"
args = ["mcp/server.py"]

[agents]
max_threads = 8
```

If local verification shows FastMCP is missing from the Python environment, add a minimal dependency file such as `requirements.txt` with:

```text
fastmcp
pytest
pytest-cov
```

If a dependency file is added, the implementation must also update run guidance in the later Clio documentation work. This planning package does not create final Clio docs.

## Interfaces And Data

Public MCP interfaces:

| Interface | Type | Input | Output |
|---|---|---|---|
| `get_transaction_status` | Tool | `transaction_id: str` | Safe dict for one transaction result or safe missing/error payload |
| `list_pipeline_results` | Tool | None | Safe dict containing summary counts and compact transaction views |
| `pipeline://summary` | Resource | None | Text summary of the latest pipeline run |

Files expected to be created during implementation:

- `mcp/__init__.py`
- `mcp/server.py`
- `tests/test_mcp_server.py`

Files expected to be modified during implementation:

- `mcp.json`
- `.codex/config.toml`
- `CHANGELOG.md`

Conditional implementation files:

- `requirements.txt`, only if no existing dependency declaration is available and local FastMCP import is unavailable.

Files to read but not modify without approved variance:

- `TASKS.md`
- `agents.md`
- `specification.md`
- `research-notes.md`
- `docs/agent-runs/final-selection.md`
- `integrator.py`
- `agents/*.py`
- Existing `tests/*.py`
- Runtime evidence under `shared/` and `archive/`

The server reads these generated result files only:

- `shared/results/summary.json`
- `shared/results/pipeline-status.json`
- `shared/results/TXN*.json`

The server must not read `sample-transactions.json` for status responses because that source includes sensitive raw account identifiers and descriptions.

## Risks

- FastMCP may not be installed in the local Python environment. Mitigation: first implement importable helpers that tests can inspect, then add a minimal dependency declaration if local verification requires it.
- MCP subprocess validation may be hard inside the sandbox. Mitigation: unit-test helper payloads and decorator-defined callables directly; use import/startup checks for `mcp/server.py`; record any subprocess limitation honestly.
- The tool could leak PII if it reads raw sample input or returns full result payloads blindly. Mitigation: read only `shared/results/`, return allowlisted fields, and add privacy tests scanning outputs for raw sample account IDs and descriptions.
- `shared/results/` may be absent on a fresh checkout before the pipeline runs. Mitigation: tools return safe `found=False` or `PIPELINE_RESULTS_NOT_FOUND` payloads and resource text that tells the operator to run the pipeline first.
- Path traversal through `transaction_id` could read unintended files. Mitigation: accept only conservative transaction ID characters and construct the path from the sanitized ID.
- Updating `pipeline-status` config before `mcp/server.py` exists could break MCP startup. Mitigation: implementation order creates and tests `mcp/server.py` before editing MCP config.
- The custom MCP server could be confused with Homework Automation Layer agents. Mitigation: keep it a Generated Transaction System Layer support surface named `pipeline-status`, with functional tool names only.
- The server could become stale if result schema changes later. Mitigation: helpers tolerate optional fields, return compact counts from observed files, and tests lock the current selected result contract.

## Acceptance Criteria

- `mcp/server.py` exists and defines a `FastMCP("pipeline-status")` server.
- `mcp/server.py` exposes tool `get_transaction_status(transaction_id: str)`.
- `mcp/server.py` exposes tool `list_pipeline_results()`.
- `mcp/server.py` exposes resource `pipeline://summary`.
- `get_transaction_status("TXN001")` returns `found=True`, status `settled`, reason code `SETTLEMENT_SIMULATED`, and no raw sample account identifiers or raw descriptions when current sample results exist.
- `get_transaction_status("TXN006")` returns status `rejected` and includes `UNSUPPORTED_CURRENCY` when current sample results exist.
- `get_transaction_status("UNKNOWN")` returns `found=False` with safe reason code `TRANSACTION_RESULT_NOT_FOUND`.
- Invalid transaction IDs such as `../summary` return a safe validation payload and do not read outside `shared/results/`.
- `list_pipeline_results()` returns `total_transactions=8`, `settled=2`, `rejected=2`, `review_required=4`, `error=0`, and `result_count=8` when current sample results exist.
- `pipeline://summary` returns text containing the runtime run ID, generated timestamp, total count, status counts, and educational simulation notice.
- The MCP payloads and summary text do not contain raw sample `source_account`, `destination_account`, or `description` values.
- `mcp.json` configures both `context7` and `pipeline-status` in a single `mcpServers` object.
- `.codex/config.toml` configures both `context7` and `pipeline-status` only after `mcp/server.py` exists.
- Focused tests for the MCP helpers pass.
- Existing selected tests continue to pass with the new MCP tests included.
- `python scripts/check_coverage_gate.py --fail-under 80` continues to pass if the helper is available in the selected root.
- `TASKS.md`, `specification.md`, selected generated code business logic, selected generated tests other than the new MCP test file, and `research-notes.md` remain unchanged.
- `CHANGELOG.md` records the planning freeze and implementation increments before commits.

## Documentation Artifact Matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Before each commit | `CHANGELOG.md` | Newest-first Homework 6 entries for planning freeze and later Task 4 MCP implementation |
| Test cases | Snapshot | Yes | Before implementation | `docs/work-items/2026-06-20-pipeline-status-mcp/snapshots/test-cases.snapshot.md` | Captures expected custom MCP server behavior before code changes |
| Testing guide delta | Living delta | No | Not applicable | `docs/work-items/2026-06-20-pipeline-status-mcp/deltas/testing-guide.delta.md` | Final reviewer docs belong to later Clio work |
| Operator manual delta | Living delta | No | Not applicable | `docs/work-items/2026-06-20-pipeline-status-mcp/deltas/operator-manual.delta.md` | The MCP config change is captured in this spec and plan; final run docs are later Clio scope |
| API reference delta | Living delta | No | Not applicable | `docs/work-items/2026-06-20-pipeline-status-mcp/deltas/api-reference.delta.md` | No HTTP/API reference exists yet; MCP surface is captured in this spec |
| Architecture snapshot | Snapshot | No | Not applicable | `docs/work-items/2026-06-20-pipeline-status-mcp/snapshots/architecture.snapshot.md` | Architecture impact is small and captured in this spec and plan |
| Architecture summary delta | Living delta | No | Not applicable | `docs/work-items/2026-06-20-pipeline-status-mcp/deltas/architecture-summary.delta.md` | Long-lived architecture docs belong to later Clio work |

## Approval

- Status: Approved by operator on 2026-06-20
- Superseded by: None

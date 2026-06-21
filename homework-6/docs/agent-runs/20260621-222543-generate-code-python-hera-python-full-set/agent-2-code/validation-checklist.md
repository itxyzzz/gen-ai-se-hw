# Validation Checklist

## Run Identity

- Run ID: `20260621-222543-generate-code-python-hera-python-full-set`
- Stack: `python`
- Mode: `generate`
- Source Athena (Spec Writer) run: `20260621-220037-write-spec-python-hera-python-full-set`
- Source specification SHA-256: `6F2E8CD844884DF06172EEB1CF92A2956BC45425FE514B7A820ABAEA249D2222`
- Status: pass with noted sandbox limitations.

## Required Output Presence

| Check | Status | Evidence |
|---|---:|---|
| `run-metadata.md` exists | Pass | Contains Hera parent dispatch, source spec fingerprint, Context7 status, sub-agent choice, and protected canonical context. |
| `inputs/source-context.md` exists | Pass | Lists exact source artifacts read and protected context. |
| `agent-2-code/handoffs/sub-agent-plan.md` exists | Pass | Records explicit no-subagent decision and rationale. |
| `agent-2-code/research-notes.md` exists | Pass | Mirrors Context7 records. |
| `agent-2-code/outputs/` complete candidate package exists | Pass | Contains runtime modules, tests, copied fixture, research notes, inventory, and runtime evidence. |
| `agent-2-code/outputs/inventory.md` exists | Pass | Lists selectable files, canonical targets, hashes, and exclusions. |
| `agent-2-code/handoff.md` exists | Pass | Completion handoff created. |

## Spec Coverage

| Selected spec objective/task | Generated evidence |
|---|---|
| Integrator with repeated-run setup and provenance | `integrator.py`, `shared/run-provenance.json`, `archive/shared-001/` |
| Transaction Validator | `agents/transaction_validator.py`, `tests/test_transaction_validator.py` |
| Fraud Detector | `agents/fraud_detector.py`, `tests/test_fraud_detector.py` |
| Settlement Processor | `agents/settlement_processor.py`, `tests/test_settlement_processor.py` |
| Reporting Agent | `agents/reporting_agent.py`, `tests/test_reporting_agent.py`, `shared/results/summary.json`, `shared/results/pipeline-status.json` |
| JSON file protocol | `shared/input`, `shared/processing`, `shared/output`, `shared/results` in run-local outputs |
| Isolated tests | `tests/`, verified with `python -m pytest -p no:cacheprovider --basetemp test-tmp` unsandboxed |

## Validation Commands

| Command | Status | Actual result |
|---|---:|---|
| `python integrator.py --input sample-transactions.json --shared-dir shared` | Pass | Exit 0; `total=8 settled=2 rejected=2 review_required=4 error=0`. |
| Second `python integrator.py --input sample-transactions.json --shared-dir shared` | Pass | Exit 0; same counts; previous run preserved under `archive/shared-001/`; current `shared/results/summary.json` has a fresh runtime run ID. |
| `python -m pytest --basetemp .test-tmp` sandboxed | Blocked by environment | Windows sandbox denied pytest temp-directory cleanup/access. |
| `python -m pytest -p no:cacheprovider --basetemp test-tmp` sandboxed | Blocked by environment | Windows sandbox denied pytest temp-directory cleanup/access. |
| `python -m pytest -p no:cacheprovider --basetemp test-tmp` unsandboxed | Pass | `18 passed in 3.01s`. |

## Sample Outcome Checks

| Transaction | Expected signal | Observed |
|---|---|---|
| `TXN001` | Low-risk settled | `settled`, `SETTLED` |
| `TXN002` | High-value review | `review_required`, `REVIEW_HIGH_VALUE` |
| `TXN003` | Safe derived destination-pattern review | `review_required`, `REVIEW_DESTINATION_PATTERN` |
| `TXN004` | Odd-hour/channel review | `review_required`, `REVIEW_UNUSUAL_TIME`, `REVIEW_CHANNEL_PATTERN` |
| `TXN005` | Very-high-value review | `review_required`, `REVIEW_HIGH_VALUE` |
| `TXN006` | Unsupported currency rejection | `rejected`, `UNSUPPORTED_CURRENCY` |
| `TXN007` | Non-positive amount rejection | `rejected`, `NON_POSITIVE_AMOUNT` |
| `TXN008` | Low-risk settled | `settled`, `SETTLED` |

## Privacy And Safety Checks

| Check | Status | Evidence |
|---|---:|---|
| Raw account IDs absent outside copied fixture | Pass | `Select-String` for `ACC-[0-9]{4,}` returned no matches outside `sample-transactions.json`. |
| Raw descriptions absent from runtime evidence | Pass | `Select-String` over `shared/` and `archive/` for all sample descriptions returned no matches. |
| Results and summaries are safe | Pass | `privacy_check: passed` in transaction results and `summary.json`. |
| Runtime provenance is safe | Pass | Contains run IDs, paths, fingerprints, stack, and pipeline version only. |
| No real compliance claims or integrations | Pass | Product code is deterministic educational simulation; no network calls or payment movement. |

## Scope Checks

| Check | Status |
|---|---:|
| No Task 3 commands or hooks generated | Pass |
| No Task 4 MCP server/config generated or changed | Pass |
| No Task 5 docs/screenshots/PR package generated | Pass |
| `mcp.json` and `.codex/config.toml` unchanged | Pass |
| Canonical `specification.md`, runtime code, tests, research notes, selection records unchanged | Pass |

## Known Limitations

- Copy-based archival is used instead of destructive move-based archival because this Windows sandbox denied file and directory move/delete operations inside run-local validation folders. The observable archive contract is still satisfied: prior `shared/` evidence is preserved under `archive/shared-001/`, and current `shared/` contains fresh results.
- Sandboxed pytest could not access its temp directories; unsandboxed pytest passed with the same candidate test command and cache disabled.
- Coverage enforcement and hooks remain out of scope for this Task 2 Hephaestus run.

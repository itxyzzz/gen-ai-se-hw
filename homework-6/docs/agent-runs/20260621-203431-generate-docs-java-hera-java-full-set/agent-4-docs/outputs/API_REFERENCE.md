# Java Candidate API Reference

The Java candidate exposes local command interfaces and stack-neutral JSON file contracts. It does not expose an HTTP API.

## Full Pipeline Command

Plain Maven command:

```powershell
mvn exec:java
```

Equivalent explicit main class:

```powershell
mvn exec:java '-Dexec.mainClass=edu.setu.transactionpipeline.Integrator'
```

Evidence command when an empty settings workaround is needed:

```powershell
mvn -o -s ..\maven-empty-settings.xml -gs ..\maven-empty-settings.xml exec:java
```

Supported Integrator arguments from `PipelineOptions`:

| Argument | Meaning | Default |
|---|---|---|
| `--input <path>` | Input transaction JSON file | `sample-transactions.json` |
| `--shared-dir <path>` | Runtime protocol output directory | `shared` |

The archive directory is derived beside the selected shared directory.

## Validation-Only Command

Plain command:

```powershell
mvn exec:java '-Dexec.mainClass=edu.setu.transactionpipeline.cli.ValidateTransactionsCommand' '-Dexec.args=sample-transactions.json'
```

Behavior:

- Loads the sample transaction file.
- Runs validator logic only.
- Writes safe `validation-report.json`.
- Does not run fraud scoring, settlement, or reporting as its primary behavior.

Safe validation report fields:

| Field | Meaning |
|---|---|
| `schema_version` | Report schema version |
| `generated_at` | Report timestamp |
| `total` | Number of input records |
| `valid` | Number passing validation |
| `invalid` | Number failing validation |
| `invalid_transaction_ids` | Synthetic transaction IDs for invalid records |
| `reason_code_counts` | Aggregate validator reason-code counts |

## JSON File Protocol

```text
shared/
  input/
  processing/
  output/
  results/
```

The Integrator writes and moves JSON messages through those directories and writes final results under `shared/results`.

## Transaction Result Shape

Each Java `shared/results/TXN*.json` file contains the safe `TransactionResult` shape:

| Field | Type | Meaning |
|---|---|---|
| `schema_version` | string | Result schema version |
| `transaction_id` | string | Synthetic transaction identifier |
| `status` | string | `settled`, `rejected`, `review_required`, or `error` |
| `reason_codes` | array | Stable reason-code strings |
| `amount` | string | Decimal amount string |
| `currency` | string | Currency code |
| `processed_at` | string | Processing timestamp |
| `risk_tier` | string | Educational risk tier |
| `component_history_count` | integer | Count of component history entries |
| `audit_event_count` | integer | Count of audit events |
| `settlement_reference` | string/null | Simulated settlement reference when applicable |

Result files omit raw account identifiers, raw descriptions, unfiltered metadata, credentials, hidden prompts, and full audit payloads.

## Summary Shape

`shared/results/summary.json` contains:

| Field | Type | Meaning |
|---|---|---|
| `schema_version` | string | Summary schema version |
| `generated_at` | string | Summary timestamp |
| `total` | integer | Total result count |
| `settled` | integer | Settled count |
| `rejected` | integer | Rejected count |
| `review_required` | integer | Review-required count |
| `error` | integer | Error count |
| `complete` | boolean | Whether one terminal result exists per input |
| `reason_code_counts` | object | Aggregate reason-code counts |

## Pipeline Status Shape

`shared/results/pipeline-status.json` contains:

| Field | Type | Meaning |
|---|---|---|
| `schema_version` | string | Status schema version |
| `generated_at` | string | Status timestamp |
| `ready` | boolean | Whether the result tree is ready to inspect |
| `summary_path` | string | Path to `summary.json` |
| `results_path` | string | Path to result files |
| `total` | integer | Total result count |
| `settled` | integer | Settled count |
| `rejected` | integer | Rejected count |
| `review_required` | integer | Review-required count |
| `error` | integer | Error count |
| `complete` | boolean | Completion flag |

## MCP Reader Compatibility

The existing custom MCP server is Python:

```text
mcp/server.py
```

It exposes:

- Tool `get_transaction_status(transaction_id: str)`
- Tool `list_pipeline_results()`
- Resource `pipeline://summary`

Normal MCP operation reads root `shared/results`. If Java is later selected and its pipeline writes there, the server can read Java result files for core safe fields such as transaction ID, status, reason codes, amount, currency, and counts.

For preserved-candidate inspection without copying root files, load the server by file path and pass a candidate result directory to helper functions:

```powershell
python -c "import importlib.util; from pathlib import Path; p=Path('mcp/server.py').resolve(); spec=importlib.util.spec_from_file_location('pipeline_status_server', p); mod=importlib.util.module_from_spec(spec); spec.loader.exec_module(mod); results=Path('docs/agent-runs/20260621-201051-generate-tests-java-hera-java-full-set-retry/agent-3-tests/workspace/project-under-test/shared/results'); print(mod.list_pipeline_results_payload(results))"
```

Python-specific optional fields not present in Java result files default safely in the current reader. Java `summary.json` uses `total`; the reader can still count `TXN*.json` files and read shared status counts.

## Coverage Gate Interface

```powershell
python scripts\check_coverage_gate.py --stack java --project-dir <java-project> --fail-under 80
```

Optional Maven settings flags:

```powershell
--maven-settings <settings.xml> --maven-global-settings <settings.xml>
```

The helper maps `--fail-under 80` to a JaCoCo covered-ratio threshold of `0.80`.

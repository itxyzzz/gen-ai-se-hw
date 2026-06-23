# Pipeline Commands And Hooks

This file defines the shared Operator Layer behavior for Homework 6 pipeline operation commands and coverage gates.

These surfaces are maintained once outside Themis (Test Generator). Future Themis runs validate them and report missing or stale behavior instead of regenerating them as ordinary test outputs.

## Stack Resolution

Resolve the package set before choosing a command:

1. Use an explicit operator argument such as `stack=python`, `stack=java`, or a package-set ID when provided.
2. Otherwise read `docs/agent-runs/selection-sets.json` and use its `canonical_set_id`.
3. If that registry is unavailable, fall back to the current Python canonical package recorded in `docs/agent-runs/final-selection.md`.

Do not infer the stack from raw transaction data or parse full result payloads. The registry contains command hints for pipeline execution, validation-only behavior, and coverage. Python remains canonical until a later explicit operator selection changes the canonical package set.

## `/run-pipeline`

Purpose: run the multi-agent banking pipeline end to end.

Fast path for the canonical Python package:

Use one bounded shell invocation for the happy path so the command does not spend time on broad context loading, repeated schema probes, `git status`, or separate summary passes:

```powershell
$elapsed = Measure-Command {
  if (-not (Test-Path 'sample-transactions.json')) { throw 'sample-transactions.json missing' }
  $pipelineOutput = python integrator.py
  $summary = Get-Content -Raw 'shared\results\summary.json' | ConvertFrom-Json
  $inputCount = (Get-Content -Raw 'sample-transactions.json' | ConvertFrom-Json).Count
  $resultCount = (Get-ChildItem 'shared\results' -Filter 'TXN*.json').Count
  $rejected = Get-ChildItem 'shared\results' -Filter 'TXN*.json' | ForEach-Object {
    $j = Get-Content -Raw $_.FullName | ConvertFrom-Json
    if ($j.status -eq 'rejected') {
      [pscustomobject]@{ transaction_id = $j.transaction_id; reason_codes = ($j.reason_codes -join ',') }
    }
  } | Sort-Object transaction_id
}
[pscustomobject]@{
  elapsed_ms = [Math]::Round($elapsed.TotalMilliseconds, 2)
  total = $summary.total_transactions
  settled = $summary.settled
  review_required = $summary.review_required
  rejected = $summary.rejected
  error = $summary.error
  result_count = $resultCount
  all_present = ($inputCount -eq $resultCount)
  rejected_safe = @($rejected)
} | ConvertTo-Json -Depth 5
```

Required behavior:

1. Confirm `sample-transactions.json` exists.
2. Archive or clear `shared/` according to the selected pipeline behavior. The current Python pipeline archives existing `shared/` output before creating a fresh run.
3. Run the selected pipeline command. For `stack=python`, the normal command is:

   ```bash
   python integrator.py
   ```

   For `stack=java`, use the package-set command hint or selected inventory, such as a generated `mvn exec:java` invocation or a packaged `java -jar target/...jar` command. Do not run Java generation as part of `/run-pipeline`.

4. Confirm all transactions from `sample-transactions.json` appear in `shared/results/`.
5. Summarize `shared/results/summary.json` when present.
6. Report rejected transaction IDs with safe reason codes only.
7. Redact account identifiers, raw descriptions, names, and unfiltered metadata from command summaries.
8. Fall back to targeted file inspection only if the fast path fails or reports `all_present=false`.

Good summary fields include total count, settled count, rejected count, review-required count, error count, and output directory path.

## `/validate-transactions`

Purpose: validate transactions without running the full pipeline.

Fast path for the canonical Python package:

The current Python validator exposes dry-run behavior as an importable function rather than a file-path CLI. Use a single bounded invocation that emits only safe fields:

```powershell
python -c "import json; from collections import Counter; from agents.transaction_validator import validate_transactions_file; r=validate_transactions_file('sample-transactions.json'); safe={'total': r['total'], 'valid': r['valid'], 'invalid': r['rejected'], 'reason_code_groups': dict(Counter(code for item in r['results'] for code in item['reason_codes'])), 'results': [{'transaction_id': item['transaction_id'], 'status': item['status'], 'reason_codes': item['reason_codes']} for item in r['results']]}; print(json.dumps(safe, indent=2))"
```

If the selected code later exposes a real dry-run CLI, prefer that interface and record the exact command. Do not run the full pipeline unless the operator explicitly asks to fall back.

For `stack=java`, use the package-set command hint or selected Java inventory for the validation-only dry-run CLI. A valid Java alternate should report safe totals, valid/invalid counts, and reason-code groups without settlement, using Maven or packaged Java command syntax chosen by the generated spec.

Report:

- Total transaction count.
- Valid count.
- Invalid count.
- Reason-code groups.
- A redacted table with transaction ID, valid/invalid status, and safe reason code.

Avoid reading raw input records or result payloads in the final answer. The validator import already handles the raw file locally and returns a safe transaction-level report.

Do not print raw account IDs, raw descriptions, names, or full audit payloads.

## `/generate-transactions`

Purpose: generate a fresh list of synthetic transactions in the canonical input format, including both valid and invalid records, for use with `/validate-transactions` and `/run-pipeline`.

The generator is written against the current rules in `agents.transaction_validator`, `agents.fraud_detector`, and `integrator`, so it can deliberately emit records in every outcome class. It is the recommended way to exercise the pipeline beyond the committed `sample-transactions.json`.

Fast path for the canonical Python package:

Use one bounded shell invocation. Prefer `--balanced` to cover every branch, and always pass a `--seed` so evidence is reproducible:

```bash
python scripts/generate_transactions.py --balanced --seed 42 --output sample-transactions.generated.json --manifest sample-transactions.manifest.json
```

Required behavior:

1. Resolve the package set first using the Stack Resolution rules above. For `stack=python`, use `scripts/generate_transactions.py`. For `stack=java`, use the package-set command hint or selected Java inventory for an equivalent generator; if none exists, report the gap instead of inventing one.
2. Choose the mix:
   - `--balanced` emits at least one record of every category (best for branch coverage).
   - Otherwise tune `--count`, `--invalid-ratio`, and `--review-ratio`.
   - Always pass `--seed` for reproducibility, and `--start-index` to avoid `transaction_id` collisions when appending to an existing set.
3. Write transactions to an explicit `--output` path. Do not overwrite the committed `sample-transactions.json` unless the operator explicitly asks; prefer a clearly named file such as `sample-transactions.generated.json`.
4. Optionally write `--manifest` to record the redacted expected outcome (validator status and reason codes, plus pipeline status and reason codes) per transaction.
5. Report only safe summary fields: total, valid, invalid, category counts, and expected pipeline counts. Do not print raw account IDs, raw descriptions, names, or full transaction payloads.
6. To confirm the generated set behaves as predicted, hand the output file to `/validate-transactions` (validator layer) or `/run-pipeline` (full pipeline). Remember the validator-vs-pipeline divergence below.

Generator outcome contract:

| Category | Validator | Full pipeline | Reason code |
|---|---|---|---|
| `valid_settled` | validated | settled | `SETTLED` |
| `review_high_value` | validated | review_required | `REVIEW_HIGH_VALUE` |
| `review_unusual_time` | validated | review_required | `REVIEW_UNUSUAL_TIME` |
| `review_channel_pattern` | validated | review_required | `REVIEW_CHANNEL_PATTERN` |
| `review_destination_pattern` | validated | review_required | `REVIEW_DESTINATION_PATTERN` |
| `invalid_missing_field` | rejected | settled | `MISSING_FIELD` (validator) / `SETTLED` (pipeline) |
| `invalid_amount` | rejected | rejected | `INVALID_AMOUNT` |
| `invalid_non_positive_amount` | rejected | rejected | `NON_POSITIVE_AMOUNT` |
| `invalid_unsupported_currency` | rejected | rejected | `UNSUPPORTED_CURRENCY` |
| `invalid_timestamp` | rejected | rejected | `INVALID_TIMESTAMP` |

`invalid_missing_field` is the deliberate divergence: the validator rejects it with `MISSING_FIELD`, but `integrator.seed_input_messages` backfills every required field, so the full pipeline settles it. `MISSING_FIELD` is observable only through `/validate-transactions`.

## Coverage Gate

The default coverage threshold is 80 percent. The portable helper is:

```bash
python scripts/check_coverage_gate.py --fail-under 80
```

The helper defaults to `--stack auto` and remains backward-compatible with the selected Python root. Use explicit stack commands for package-set evidence:

```bash
python scripts/check_coverage_gate.py --stack python --fail-under 80
python scripts/check_coverage_gate.py --stack java --project-dir path/to/java-package --fail-under 80
```

When a local Maven installation inherits an unavailable machine-level mirror or other external settings, Java validation may pass an explicit run-local settings override without changing the generated Java package:

```bash
python scripts/check_coverage_gate.py --stack java --project-dir path/to/java-package --maven-settings path/to/settings.xml --maven-global-settings path/to/settings.xml --fail-under 80
```

Relative Maven settings paths are resolved from the caller's current directory first, then from `--project-dir`. Omit these flags in normal environments; the default Java command remains unchanged.

For Python, the helper runs coverage from the project root using pytest coverage:

```bash
python -m pytest --cov=. --cov-fail-under=80
```

For Java, the helper requires a Maven project with `pom.xml` and JaCoCo `check` configuration, then runs:

```bash
mvn -Dcoverage.minimum=0.80 test jacoco:report jacoco:check
```

With Maven settings overrides, the helper adds `-s SETTINGS_PATH` and `-gs GLOBAL_SETTINGS_PATH` before the coverage threshold property. The Java check relies on the generated `pom.xml` to configure JaCoCo rules and halt the build below the covered-ratio threshold.

For Python, the helper stores coverage data and pytest temporary files in a short-lived ignored `tmp/coverage-gate-<pid>/` workspace folder so the hook does not mutate root `.coverage`, root `.pytest_cache/`, or `shared/` evidence while checking the gate.

Use an explicit override to demonstrate the blocking path without degrading the real suite:

```bash
python scripts/check_coverage_gate.py --fail-under 99
```

The committed Git pre-push hook invokes the helper with `--stack auto` and the default 80 percent threshold. Claude hook settings should invoke the same helper for push-like actions when the local Claude Code hook schema is supported.

The hook passes when coverage meets or exceeds the threshold and blocks or fails the action when coverage is below the threshold.

## Privacy Requirements

All command summaries, hook evidence, and screenshots must be safe for review:

- Redact account identifiers, for example `ACC-****1001`.
- Do not print raw descriptions when they may contain sensitive metadata.
- Prefer reason codes and counts over full transaction payloads.
- Keep the project framed as an educational simulation, not legal, banking, AML, sanctions, or payment-network compliance.

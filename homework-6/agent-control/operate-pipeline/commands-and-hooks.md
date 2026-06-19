# Pipeline Commands And Hooks

This file defines the shared Operator Layer behavior for Homework 6 pipeline operation commands and coverage gates.

These surfaces are maintained once outside Themis (Test Generator). Future Themis runs validate them and report missing or stale behavior instead of regenerating them as ordinary test outputs.

## `/run-pipeline`

Purpose: run the multi-agent banking pipeline end to end.

Steps:

1. Confirm `sample-transactions.json` exists.
2. Archive or clear `shared/` according to the selected pipeline behavior. The current Python pipeline archives existing `shared/` output before creating a fresh run.
3. Run the selected pipeline command, normally:

   ```bash
   python integrator.py
   ```

4. Confirm all transactions from `sample-transactions.json` appear in `shared/results/`.
5. Summarize `shared/results/summary.json` when present.
6. Report rejected transaction IDs with safe reason codes only.
7. Redact account identifiers, raw descriptions, names, and unfiltered metadata from command summaries.

Good summary fields include total count, settled count, rejected count, review-required count, error count, and output directory path.

## `/validate-transactions`

Purpose: validate transactions without running the full pipeline.

Preferred Python command when available:

```bash
python agents/transaction_validator.py --dry-run sample-transactions.json
```

If the selected code exposes a different dry-run interface, use that interface and record the exact command. Do not run the full pipeline unless the operator explicitly asks to fall back.

Report:

- Total transaction count.
- Valid count.
- Invalid count.
- Reason-code groups.
- A redacted table with transaction ID, valid/invalid status, and safe reason code.

Do not print raw account IDs, raw descriptions, names, or full audit payloads.

## Coverage Gate

The default coverage threshold is 80 percent. The portable helper is:

```bash
python scripts/check_coverage_gate.py --fail-under 80
```

The helper runs coverage from the Homework 6 root using the selected stack's pytest coverage command:

```bash
python -m pytest --cov=. --cov-fail-under=80
```

The helper stores coverage data and pytest temporary files in a short-lived ignored `tmp/coverage-gate/` workspace folder so the hook does not mutate root `.coverage`, root `.pytest_cache/`, or `shared/` evidence while checking the gate.

Use an explicit override to demonstrate the blocking path without degrading the real suite:

```bash
python scripts/check_coverage_gate.py --fail-under 99
```

The committed Git pre-push hook invokes the helper with the default 80 percent threshold. Claude hook settings should invoke the same helper for push-like actions when the local Claude Code hook schema is supported.

The hook passes when coverage meets or exceeds the threshold and blocks or fails the action when coverage is below the threshold.

## Privacy Requirements

All command summaries, hook evidence, and screenshots must be safe for review:

- Redact account identifiers, for example `ACC-****1001`.
- Do not print raw descriptions when they may contain sensitive metadata.
- Prefer reason codes and counts over full transaction payloads.
- Keep the project framed as an educational simulation, not legal, banking, AML, sanctions, or payment-network compliance.

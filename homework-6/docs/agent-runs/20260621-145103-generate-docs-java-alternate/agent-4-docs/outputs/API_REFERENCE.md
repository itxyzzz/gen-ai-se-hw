# Java Alternate API Reference

The Java alternate is a CLI and file-protocol package, not an HTTP API.

## Pipeline Command

```powershell
mvn -s ..\validation-settings.xml -gs ..\validation-settings.xml exec:java '-Dexec.args=--input sample-transactions.json --shared-dir shared'
```

Options:

- `--input <path>`: path to JSON transaction fixture.
- `--shared-dir <path>`: shared protocol directory root.
- `--dry-run`: validate records without settlement.

## Dry-Run Command

```powershell
mvn -s ..\validation-settings.xml -gs ..\validation-settings.xml exec:java '-Dexec.args=--input sample-transactions.json --dry-run'
```

Safe response fields:

- `total`
- `valid`
- `invalid`
- per-record `transaction_id`, `status`, and `reason_codes`

## Result Files

- `shared/results/summary.json`
- `shared/results/TXN*.json`

The result files are compatible with the existing Python `pipeline-status` MCP reader because they use stack-neutral safe fields.

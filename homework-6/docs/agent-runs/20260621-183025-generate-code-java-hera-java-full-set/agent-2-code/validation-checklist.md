# Validation Checklist

Run ID: `20260621-183025-generate-code-java-hera-java-full-set`

## Result

Status: pass with environment note.

The Java candidate package generated Task 2 product code only, preserved it under the run folder, and did not overwrite canonical root files or selection records.

## Source Traceability

| Requirement | Status | Evidence |
|---|---|---|
| Explicit Java source spec used | Pass | `docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/agent-1-spec/outputs/specification.md` |
| Source spec SHA-256 verified | Pass | `2FC5B597915CFD59D5786271DB0CDE96B06417DEEC1433D056B76D7023234CFC` |
| Canonical Python package protected | Pass | No canonical root product files, root tests, root docs, MCP files/config, final-selection, selection-sets, or root shared output were changed. |

## Required Files

| File or folder | Status |
|---|---|
| `run-metadata.md` | Pass |
| `inputs/source-context.md` | Pass |
| `agent-2-code/outputs/` | Pass |
| `agent-2-code/outputs/sample-transactions.json` | Pass |
| `agent-2-code/outputs/inventory.md` | Pass |
| `agent-2-code/research-notes.md` | Pass |
| `agent-2-code/outputs/research-notes.md` | Pass |
| `agent-2-code/validation-checklist.md` | Pass |
| `agent-2-code/handoff.md` | Pass |
| `agent-2-code/handoffs/sub-agent-plan.md` | Pass |
| Sub-agent handoffs | Pass |

## Java Package Checks

| Requirement | Status | Evidence |
|---|---|---|
| Maven `pom.xml` | Pass | `agent-2-code/outputs/pom.xml` |
| Java sources under `src/main/java` | Pass | 29 source files. |
| Tests under `src/test/java` | Pass | 10 JUnit Jupiter tests. |
| BigDecimal money handling | Pass | `FraudDetector` thresholds and `TransactionValidator` amount parsing use `BigDecimal`; no binary floating-point money handling was introduced. |
| Jackson JSON handling | Pass | `JsonCodec` centralizes ObjectMapper setup. |
| Functional runtime class names | Pass | `TransactionValidator`, `FraudDetector`, `SettlementProcessor`, `ReportingAgent`, `Integrator`. |
| JUnit Jupiter | Pass | `pom.xml` and tests. |
| JaCoCo 80% threshold-capable rule | Pass | `pom.xml` uses `COVEREDRATIO` minimum `0.80`; direct `jacoco:check` passed. |
| `shared/input`, `processing`, `output`, `results` protocol | Pass | Runtime smoke run created all protocol directories. |
| Repeated-run archive | Pass | Second clean run created `archive/shared-001/results/summary.json`. |
| Runtime provenance | Pass with note | `shared/run-provenance.json` contains safe source IDs/path/fingerprint and candidate run ID; pipeline package fingerprint is a safe placeholder until selection supplies a final version value. |
| MCP-compatible result shape | Pass | `shared/results/summary.json`, `pipeline-status.json`, and 8 `TXN*.json` files use generic JSON keys. |

## Commands Run

The configured Maven mirror failed DNS resolution for `artifactory.cengage.info`, so final Maven validation used a temporary empty Maven settings file and offline mode against cached artifacts:

```powershell
$settings = Join-Path $env:TEMP 'codex-empty-maven-settings.xml'
Set-Content -LiteralPath $settings -Value '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"></settings>' -Encoding UTF8
mvn -o -s $settings -gs $settings test jacoco:report jacoco:check
```

Result: pass. `Tests run: 10, Failures: 0, Errors: 0, Skipped: 0`; JaCoCo reported `All coverage checks have been met.`

```powershell
mvn -o -s $settings -gs $settings exec:java '-Dexec.mainClass=edu.setu.transactionpipeline.Integrator'
```

Result: pass on first clean run.

```powershell
mvn -o -s $settings -gs $settings exec:java '-Dexec.mainClass=edu.setu.transactionpipeline.Integrator'
```

Result: pass on second clean run; `archive/shared-001/results/summary.json` exists.

```powershell
mvn -o -s $settings -gs $settings exec:java '-Dexec.mainClass=edu.setu.transactionpipeline.cli.ValidateTransactionsCommand' '-Dexec.args=sample-transactions.json'
```

Result: pass; `validation-report.json` reports `total=8`, `valid=6`, `invalid=2`, invalid IDs `TXN006` and `TXN007`.

## Runtime Result Evidence

`shared/results/summary.json` after the second clean run:

- `total=8`
- `settled=2`
- `rejected=2`
- `review_required=4`
- `error=0`
- `complete=true`
- Reason counts include `HIGH_VALUE=2`, `VERY_HIGH_VALUE=1`, `ODD_HOUR=1`, `UNSUPPORTED_CURRENCY=1`, and `NON_POSITIVE_AMOUNT=1`.

Expected sample checks:

| Transaction | Expected | Status |
|---|---|---|
| `TXN006` | rejected with `UNSUPPORTED_CURRENCY` | Pass |
| `TXN007` | rejected with `NON_POSITIVE_AMOUNT` | Pass |
| `TXN002` | high-value review signal | Pass |
| `TXN005` | very-high-value review signal | Pass |
| `TXN004` | odd-hour review signal | Pass |
| `TXN001` | low-risk settled with simulated reference | Pass |

## Privacy And Scope Scans

Commands run from `agent-2-code/outputs/`:

```powershell
rg -n --hidden --glob '!archive/**' --glob '!target/**' --glob '!sample-transactions.json' "ACC-[0-9]{4,}|Monthly rent payment|Equipment purchase|Consulting payment|Invoice #4471|Property settlement|Test payment|Refund for order #8821|Salary advance" .
```

Result: no matches.

```powershell
rg -n --hidden --glob '!archive/**' --glob '!target/**' --glob '!sample-transactions.json' "(api[_-]?key|token|secret|password|authorization|bearer|credential|System\.getenv|System\.getProperties)" .
```

Result: no matches.

```powershell
rg -n --hidden --glob '!archive/**' --glob '!target/**' --glob '!sample-transactions.json' "(AML|KYC|sanctions|PCI|legal compliance|regulatory compliance|production banking|payment network|real settlement)" .
```

Result: no matches.

Field-name scan result:

- Matches exist only in `InputTransactionLoader`, `PrivacyGuard`, and negative privacy tests where source input keys or guard behavior are intentionally referenced.
- `shared/results/TXN*.json`, `summary.json`, `pipeline-status.json`, and `shared/run-provenance.json` do not contain raw account identifiers, raw descriptions, raw metadata objects, credentials, or hidden prompt/thread content.

## Inventory And Exclusions

- Output inventory: `agent-2-code/outputs/inventory.md`
- Inventory SHA-256: `3C5B7DB95A30250DEE60A26D17BAD577FA8BFF98A293FA917A1DEA8ECCBCDEB1`
- Selectable package fingerprint: `248ba67b199104cbe06b4b0869cf3143de85ff1dd88da7bf4a13bc85a58ed883`
- Explicitly excluded from selectable code: `shared/`, `archive/`, `target/`, `validation-report.json`, Maven caches, coverage output, temporary files, and tool output.

## Known Limitations

- Maven validation depends on cached artifacts in this local environment because the configured corporate mirror was unavailable by DNS. A normal online Maven environment should run the same commands without the temporary settings workaround.
- Runtime provenance records the source spec fingerprint and candidate run ID. The pipeline package fingerprint field remains a safe placeholder during unselected candidate execution; the authoritative package fingerprint is recorded in this inventory and should be supplied explicitly if a later selector copies or reruns this package.

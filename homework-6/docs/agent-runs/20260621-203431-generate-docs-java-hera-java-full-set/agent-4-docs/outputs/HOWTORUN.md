# How To Run The Java Candidate

This runbook is for the preserved Java candidate package. It does not replace the canonical Python runbook unless Java is explicitly selected later.

## 1. Choose Review Mode

For preserved-candidate review, use the Themis retry workspace:

```powershell
cd docs\agent-runs\20260621-201051-generate-tests-java-hera-java-full-set-retry\agent-3-tests\workspace\project-under-test
```

For a future selected Java package, run the same Maven commands from the selected Java project root after inventory-driven copy.

## 2. Confirm Prerequisites

```powershell
java -version
mvn -version
```

The candidate is a Maven project using Java 17+, Jackson, JUnit Jupiter, Maven Surefire, Maven Exec Plugin, and JaCoCo.

## 3. Maven Settings Workaround When Needed

In this workspace, Maven inherited an unavailable mirror. The Java evidence therefore used a run-local empty settings file and offline cached artifacts:

```powershell
mvn -o -s ..\maven-empty-settings.xml -gs ..\maven-empty-settings.xml test jacoco:report jacoco:check
```

Use this workaround only when normal Maven resolution is blocked by local settings. In a normal environment, use the plain Maven commands shown below.

## 4. Run The Full Pipeline

Plain command:

```powershell
mvn exec:java
```

Evidence command from the preserved Themis retry:

```powershell
mvn -o -s ..\maven-empty-settings.xml -gs ..\maven-empty-settings.xml exec:java
```

Expected safe counts:

| Field | Expected |
|---|---:|
| `total` | 8 |
| `settled` | 2 |
| `rejected` | 2 |
| `review_required` | 4 |
| `error` | 0 |
| `complete` | `true` |
| `result_count` | 8 |

The run writes:

- `shared/input/*.json`
- `shared/processing/*.json`
- `shared/output/*.json`
- `shared/results/TXN*.json`
- `shared/results/summary.json`
- `shared/results/pipeline-status.json`
- `shared/run-provenance.json`

Existing `shared/` output is archived to `archive/shared-###` before a fresh run.

## 5. Run Validation-Only Mode

Plain command:

```powershell
mvn exec:java '-Dexec.mainClass=edu.setu.transactionpipeline.cli.ValidateTransactionsCommand' '-Dexec.args=sample-transactions.json'
```

Evidence command from the preserved Themis retry:

```powershell
mvn -o -s ..\maven-empty-settings.xml -gs ..\maven-empty-settings.xml exec:java '-Dexec.mainClass=edu.setu.transactionpipeline.cli.ValidateTransactionsCommand' '-Dexec.args=sample-transactions.json'
```

Expected safe counts:

- Total: 8
- Valid: 6
- Invalid: 2
- Invalid transaction IDs: `TXN006`, `TXN007`
- Reason-code groups: `UNSUPPORTED_CURRENCY=1`, `NON_POSITIVE_AMOUNT=1`

The validation-only flow writes a safe `validation-report.json` and does not perform fraud scoring or settlement.

## 6. Run Tests And JaCoCo Coverage

Plain command:

```powershell
mvn test jacoco:report jacoco:check
```

Evidence command from the preserved Themis retry:

```powershell
mvn -o -s ..\maven-empty-settings.xml -gs ..\maven-empty-settings.xml test jacoco:report jacoco:check
```

Current retry evidence:

```text
13 tests, 0 failures
87.86% instruction coverage
JaCoCo check met
```

## 7. Run The Portable Coverage Gate

From the project-under-test workspace, with the repository helper path available:

```powershell
python scripts\check_coverage_gate.py --stack java --project-dir . --fail-under 80
```

Evidence command with Maven settings workaround:

```powershell
python scripts\check_coverage_gate.py --stack java --project-dir . --maven-settings ..\maven-empty-settings.xml --maven-global-settings ..\maven-empty-settings.xml --fail-under 80
```

Expected result: pass at the 80% threshold.

## 8. Demonstrate The Blocking Path

```powershell
python scripts\check_coverage_gate.py --stack java --project-dir . --maven-settings ..\maven-empty-settings.xml --maven-global-settings ..\maven-empty-settings.xml --fail-under 99
```

Expected result: tests run, then the gate exits nonzero because 87.86% instruction coverage is below 99%.

## 9. Use Operation Commands

The shared operation behavior lives in:

```text
agent-control/operate-pipeline/commands-and-hooks.md
```

For Java package sets, `/run-pipeline` and `/validate-transactions` use the selected Java inventory or package-set command hints rather than Python `integrator.py`.

Until Java is selected, root operation commands continue to default to the canonical Python package.

## 10. Inspect MCP Status

The root MCP config already includes both required servers:

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

The custom `pipeline-status` server normally reads root `shared/results`. For Java candidate review, either inspect a selected/staged Java result tree there or load `mcp/server.py` by file path and call helper functions with the candidate results directory.

## 11. Review Screenshots

Run-local Java candidate screenshots live under:

- `docs/screenshots/pipeline-run.png`
- `docs/screenshots/test-coverage.png`
- `docs/screenshots/skill-run-pipeline.png`
- `docs/screenshots/hook-trigger.png`
- `docs/screenshots/mcp-interaction.png`

Original operator screenshots remain preserved under root `docs/screenshots/operator-sourced/`.

## 12. Cleanup

For preserved-candidate review, leave the run folders intact. Runtime outputs inside run-local `shared/`, `archive/`, `target/`, and evidence directories are not selectable canonical docs.

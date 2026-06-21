# Validation Checklist

Status: BLOCKED by Hera parent interrupt.

No validation commands were run from `agent-3-tests/workspace/project-under-test/`.

## Required Checks

| Check | Expected command or evidence | Actual status |
|---|---|---|
| Rebuild validation workspace | Copy `workspace/selected-code/` to `workspace/project-under-test/` and overlay `outputs/` | BLOCKED: not performed before interrupt |
| Maven unit/integration tests | `mvn test` from `workspace/project-under-test/` | BLOCKED: not run |
| JaCoCo 80 percent gate | `python scripts/check_coverage_gate.py --stack java --project-dir . --fail-under 80` from workspace, with Maven settings override only if needed | BLOCKED: not run |
| Hook blocking demonstration | `python scripts/check_coverage_gate.py --stack java --project-dir . --fail-under 99` from workspace | BLOCKED: not run |
| Full pipeline command evidence | `mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.Integrator` or selected Java command hint from workspace | BLOCKED: not run |
| Validation-only command evidence | `mvn exec:java -Dexec.mainClass=edu.setu.transactionpipeline.cli.ValidateTransactionsCommand -Dexec.args="sample-transactions.json"` from workspace | BLOCKED: not run |
| Privacy/redaction evidence | Inspect generated results, summaries, validation report, and command evidence for raw account IDs/descriptions | BLOCKED: no runtime outputs generated |
| Strict result schema | JUnit/schema tests plus representative JSON inspection | BLOCKED: not run |
| Repeated-run archive behavior | Pipeline run twice in isolated workspace | BLOCKED: not run |
| Runtime provenance | Inspect `shared/run-provenance.json` in workspace output | BLOCKED: no pipeline run |
| Root containment | Confirm root canonical files, root `tests/`, root `shared/`, root docs/screenshots, MCP config untouched | PARTIAL: no intentional root canonical edits; final diff/status review not run after blocked artifact write |

## Observed Blockers

1. Hera parent interrupt stopped the run before Themis-owned test generation and validation.
2. An accidental duplicate baseline test tree remains at `agent-3-tests/outputs/src/java/`; it must be removed or explicitly ignored before any resumed inventory can become selectable.
3. A cleanup attempt for the duplicate run-local path failed with Windows `Access to the path is denied`; no escalation was approved before interruption.
4. No Maven/JUnit/JaCoCo validation evidence exists for this Themis run.
5. No selected test package fingerprint exists for this Themis run.

## Coverage Status

Coverage percentage: unavailable.

JaCoCo check status: not run.

Coverage gate helper status: not run.

Hook pass/blocking evidence status: not run.

## Command Support Status

`/run-pipeline` behavior for the Java candidate: not validated.

`/validate-transactions` behavior for the Java candidate: not validated.

Operator Layer command/hook support files were read as context only and were not modified.

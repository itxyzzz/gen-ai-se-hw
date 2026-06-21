# Development Process

## Purpose

This document gives downstream Homework Automation Layer agents a portable process for implementing, testing, and documenting the Java transaction-processing system from this preserved Athena (Spec Writer) run. It does not require hidden harness state or canonical selection.

## Source Inputs For Hephaestus

Hephaestus (Code Generator) should consume explicit data from Hera (Orchestrator), not "latest" files:

- Athena run ID: `20260621-180826-write-spec-java-hera-java-full-set`
- Specification path: `docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/agent-1-spec/outputs/specification.md`
- Specification SHA-256 fingerprint from Hera or final handoff.
- Package-set ID under construction: `java-candidate-20260621-180512`
- Selection record path: `docs/agent-runs/final-selection.md`
- Current canonical context: `python-canonical-20260621` remains protected unless the operator explicitly authorizes selection.

## Implementation Order

1. Create Maven project structure and `pom.xml`.
2. Add model classes, JSON mapper, reason codes, redaction helpers, and audit helpers.
3. Implement Integrator run setup, archival, provenance, and protocol movement.
4. Implement Transaction Validator.
5. Implement Fraud Detector.
6. Implement Settlement Processor.
7. Implement Reporting Agent.
8. Add JUnit unit tests and temporary-directory integration tests.
9. Verify `mvn test`, `mvn test jacoco:report jacoco:check`, and the pipeline run command.

## Required Research Discipline

Hephaestus must use Context7 during code generation and record at least two query records in its generated research notes. Recommended targets:

- Jackson Databind for file-based JSON mapping.
- JUnit Jupiter/Surefire and JaCoCo Maven plugin setup.

Each record should include search text, returned library ID, access date, and applied insight.

## Verification Gates

Before a Java code package can be considered complete:

- No root canonical `shared/` output is required during candidate generation; run-local or explicit validation workspaces are preferred.
- `mvn test` passes.
- `mvn test jacoco:report jacoco:check` passes at the Athena temporary 75% threshold.
- Running the pipeline processes all eight sample transactions.
- Repeated runs archive previous shared output.
- Validator dry-run reports valid and invalid records without running the full pipeline.
- Result files contain no raw account identifiers, descriptions, hidden prompts, credentials, or prohibited fields.
- `summary.json`, `pipeline-status.json`, and `TXN*.json` are readable stack-neutral JSON.

## Handoff Expectations

Downstream agents should preserve candidate outputs under their own run folders before any canonical copy. Java candidates are alternate package-set artifacts until explicit operator selection. The current Python canonical set remains protected.

Do not perform selection, update `final-selection.md`, update `selection-sets.json`, or overwrite canonical product files unless Hera (Orchestrator) or the operator gives an explicit selection instruction after comparison.

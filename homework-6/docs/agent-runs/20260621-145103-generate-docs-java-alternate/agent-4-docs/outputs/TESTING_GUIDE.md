# Java Alternate Testing Guide

## Test Strategy

- Unit tests cover Transaction Validator, Fraud Detector, Settlement Processor, Reporting Agent, JSON helper, directory helper, and time helper behavior.
- Integration tests run the full Java pipeline against the copied sample fixture from the preserved output package.
- Privacy tests assert safe validation output does not include raw account identifiers or raw description text.
- Rerun tests verify archival behavior with temporary directories.

## Evidence

The Themis overlay passed:

```text
Tests run: 21
Failures: 0
Errors: 0
Skipped: 0
JaCoCo: All coverage checks have been met.
```

Evidence file:

- `docs/agent-runs/20260621-145102-generate-tests-java-alternate/agent-3-tests/evidence/test-coverage.txt`

## Coverage Gate

The Java package uses JaCoCo Maven plugin with an 80 percent instruction covered-ratio threshold. The baseline Hephaestus package failed at 0.76, proving the gate blocks; Themis added `ThemisQualityTest.java`, after which the gate passed.

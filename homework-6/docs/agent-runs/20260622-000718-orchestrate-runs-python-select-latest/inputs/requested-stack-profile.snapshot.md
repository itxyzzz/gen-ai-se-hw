# Requested Stack Profile Snapshot

- Requested canonical stack: `python`
- Python package remains the selected root runtime.
- Java package set `java-candidate-20260621-180512` is registered as alternate evidence only.
- Stack-aware support surfaces include:
  - `python scripts/check_coverage_gate.py --stack python --fail-under 80`
  - `python scripts/check_coverage_gate.py --stack java --project-dir <java-package> --fail-under 80`

This snapshot records stack decisions only. It intentionally omits raw sample transaction payloads.

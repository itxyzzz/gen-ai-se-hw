# Phase 03 Test Cases Snapshot

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Phase: `03-java-alternate-generation`
Status: Draft Review

## Minimal Expectations

| Case | Expected result |
|---|---|
| Clean Hera invocation | The execution thread sends only the single instruction from the Phase 03 plan and lets Hera load its own skill. |
| Java alternate generation | Hera attempts a `generate-set` run for `stack=java` through Athena, Hephaestus, Themis, and Clio. |
| Preservation-only output | New run evidence is preserved under `docs/agent-runs/`. |
| Python canonical protection | The current Python package remains canonical unless a later explicit selection changes it. |
| Stop before comparison or selection | Hera stops with handoff evidence for a later comparison step. |

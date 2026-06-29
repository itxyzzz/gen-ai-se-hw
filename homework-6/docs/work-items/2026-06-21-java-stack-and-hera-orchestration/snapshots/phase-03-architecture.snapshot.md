# Phase 03 Architecture Snapshot

Work ID: `2026-06-21-java-stack-and-hera-orchestration`
Phase: `03-java-alternate-generation`
Status: Draft Review

## Clean-Thread Boundary

Phase 03 is a clean Hera execution test. The phase plan authorizes the run but must not become Hera's prompt payload.

```text
Clean main thread
  -> Hera (Orchestrator), generate-set, stack=java
      -> Athena (Spec Writer)
      -> Hephaestus (Code Generator)
      -> Themis (Test Generator)
      -> Clio (Documentation Generator)
  -> preserved docs/agent-runs evidence
  -> stop before comparison or selection
```

## Source Of Truth

Detailed orchestration behavior lives in Hera's skill and control package:

- `.agents/skills/orchestrate-runs/SKILL.md`
- `agent-control/orchestrate-runs/workflow.md`
- `agent-control/orchestrate-runs/quality-bar.md`
- `agent-control/orchestrate-runs/run-registry.md`

## Protected Boundary

The current Python package remains canonical. Phase 03 does not replace canonical root outputs, select Java, or run the Phase 04 comparison.

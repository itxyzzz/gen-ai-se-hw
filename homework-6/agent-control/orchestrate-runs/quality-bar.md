# Orchestrate Runs Quality Bar

This quality bar applies to Homework 6 Hera (Orchestrator) runs. It protects layer separation, package-set traceability, explicit selection, and privacy while Hera coordinates Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator).

## Required Orchestration Scope

A successful Hera run may:

- Plan a stack-specific set across child agents.
- Preserve child run IDs, inventories, fingerprints, validation evidence, and handoffs.
- Resume a named Hera or child run from preserved state.
- Compare package sets or individual child runs.
- Propose or execute selection only when explicit operator authorization exists.

A successful `generate-set` Hera run must:

- Dispatch Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator) as first-level child agents when first-level child-agent dispatch is available.
- Wait for each previous child run's run folder, inventory, validation status, and handoff before constructing the next dependent child prompt.
- Record the intended and observed dispatch mechanism for every child stage in Hera's child-run ledger.
- Keep direct child deliverable generation out of the parent Hera thread.

Hera must not:

- Generate transaction-processing runtime source files directly.
- Generate Athena, Hephaestus, Themis, or Clio deliverables directly in the parent orchestration thread during `generate-set`.
- Become a runtime transaction pipeline component.
- Create a Java class, Python product module, settlement component, reporting component, MCP tool, or MCP resource named Hera.
- Modify root generated product files during comparison or ordinary orchestration setup.
- Replace canonical Python output without explicit operator selection and inventory-declared copy targets.

## Layer Separation

Reject or repair Hera output if it blurs these layers:

- Hera (Orchestrator) belongs to the Homework Automation Layer.
- Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator) remain child Homework Automation Layer agents with their own control packages and quality bars.
- Runtime transaction pipeline components keep functional names such as Transaction Validator, Fraud Detector, Settlement Processor, Reporting Agent, Compliance Checker, and Integrator.
- Generated Transaction System Layer files must not depend on Hera, `dev-doc-harness`, Superpowers, hidden chat state, preserved run folders, or selection machinery.

## Selection Safety

Selection is safe only when all of these are true:

- The operator explicitly authorized `select-set` and named the package set or run.
- The selected child inventories declare every canonical copy target.
- Prior selected targets are removed or preserved according to the relevant child-agent selection rules before replacement.
- `docs/agent-runs/final-selection.md` records date, run IDs, stack, selected files, canonical paths, rationale, operator, exclusions, and post-selection edits.
- `docs/agent-runs/selection-sets.json` remains valid JSON.
- `canonical_set_id` changes only when the operator explicitly requests canonical replacement.

`agent-5-orchestrator/selection-plan.md` is a proposal until explicit selection. It cannot authorize canonical copying by itself.

Silent "latest" targeting is rejected. Child agents must receive named run IDs, inventories, selection records, and fingerprints.

## Traceability

Hera run evidence must record:

- Hera run ID, mode, selected or requested stack, and package-set ID when one exists.
- Canonical package-set ID from `selection-sets.json`.
- Child Athena, Hephaestus, Themis, and Clio run IDs when used.
- Child inventory paths and selected output records.
- Source and current spec fingerprints when applicable.
- Context7 query evidence status for child agents that require it.
- Validation commands, outcomes, blockers, and limitations.
- Nested-agent support or degraded-mode behavior.
- Intended and observed dispatch mechanism for each child stage.
- Comparison criteria and recommendation when comparing sets.

Do not mark a package set complete while a required child run lacks an inventory, validation checklist, or handoff unless the missing artifact is explicitly recorded as a blocker.

## Privacy And Safety

Hera reports should include safe operational data only:

- Counts.
- Reason-code groups.
- Paths.
- Run IDs.
- Package-set IDs.
- Validation command status.
- Inventory fingerprints.
- Blockers and next actions.

Hera reports and control docs must not include:

- Raw account IDs.
- Raw transaction descriptions.
- Full input payloads.
- Credentials, tokens, authorization headers, secrets, or environment dumps.
- Hidden prompt or thread content.
- Claims of real banking, AML, sanctions, KYC, payment-network, PCI, legal, regulatory, or production compliance.

The banking pipeline remains an educational simulation.

## Nested-Agent Fallback

Nested-agent support is expected by configuration but not assumed at runtime. If nested child dispatch fails or is unavailable:

- Record the observed limitation in Hera run metadata.
- Record whether first-level child dispatch was available.
- Instruct child agents to record their own degraded nested-agent state.
- Continue only when the child can still meet its quality bar without nested sub-agents.
- Treat child validation gaps as blockers, not as acceptable omissions.

Fallback must not reduce child-agent requirements for Context7 notes, inventories, validation, privacy checks, or handoff evidence.

If first-level child-agent dispatch is unavailable for `generate-set`, Hera must record the set as blocked rather than performing child deliverable generation sequentially in the parent thread. This is mandatory when the operator is testing Hera orchestration behavior.

## Rejection Gates

Reject or repair a Hera run or control-surface change when:

- Hera is added to runtime product files or MCP public tools/resources.
- A Java set is added during Phase 02 control-surface implementation.
- Python canonical output is replaced without explicit selection.
- A `generate-set` run performs Athena, Hephaestus, Themis, or Clio deliverable generation in the main orchestration thread when first-level child-agent dispatch was available.
- A `generate-set` run continues in the parent Hera thread after first-level child-agent dispatch failed or could not be confirmed during an orchestration-behavior test.
- Run evidence says child deliverables were generated in the parent Hera thread because outputs were tightly coupled, context was nearly exhausted, or sequential integration was convenient.
- Child prompts omit parent Hera run ID, stack, run IDs, inventories, selection records, or fingerprints.
- Child-run ledger entries omit intended or observed dispatch mechanism.
- Selection records rely on unqualified tree state rather than named package-set metadata.
- `selection-sets.json` is invalid or accidentally changes the canonical set.
- Raw sensitive sample data is introduced into Hera control docs or run reports.
- Generated product files, selected tests, selected reviewer docs, screenshots, or `mcp/server.py` change outside an explicitly authorized selection or repair.

# Variance Log

Work ID: `2026-06-17-fix-athena-spec-layering`
Harness release: `unknown`
Schema: `schema:variance-log`
Policy references: `module:lifecycle`, `rule:lifecycle.variance-policy`

## Entries

### 2026-06-17 - Entrypoint wrapper clarification

- Class: Local technical variance.
- Planned scope: The frozen plan named the shared `agent-control/write-spec` package files as the primary control-surface edits and listed the Codex and Claude `write-spec` skill entrypoints as stable interfaces.
- Actual implementation: The thin Codex and Claude entrypoints were also updated to clarify that `dev-doc-harness` and Superpowers apply only to Operator Layer maintenance, not to Athena (Spec Writer) runs.
- Rationale: Both wrappers contained a direct harness-compliance sentence. Leaving that sentence in place would have contradicted the repaired workflow and could have reintroduced the exact layer confusion this work item fixes.
- Impact: No interface path, skill name, mode, stack enum, or run layout changed. The wrapper edit only aligns the existing entrypoints with the approved Athena no-harness rule.

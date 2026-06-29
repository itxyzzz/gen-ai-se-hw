# Variance Log: Code Run Preservation Repair

Work ID: `2026-06-18-code-run-preservation-repair`
Status: Active

## 2026-06-19: Runtime Provenance Moved To Athena Spec Contract

- Trigger: The operator requested a minimal `shared/` runtime reference tying each transaction run to its source Athena (Spec Writer) specification and selected Hephaestus (Code Generator) pipeline version, then clarified this must be specified by Athena rather than patched directly into `integrator.py`.
- Decision: Do not directly change current generated pipeline code for runtime provenance in this pass. Instead, update Athena (Spec Writer) control guidance so future generated specifications require `shared/run-provenance.json`, and update Hephaestus (Code Generator) guardrails so future code generation may implement that product requirement without implementing operator-layer selection mechanics.
- Scope impact: Within the approved provenance and rerun-evidence goals. No Task 3 commands/hooks, Task 4 MCP server/config, or Task 5 documentation/screenshots are added.
- Verification expectation: Static review confirms the requirement appears in Athena control surfaces and no direct `integrator.py` provenance implementation remains in the working diff.

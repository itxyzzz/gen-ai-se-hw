# Generate Docs Quality Bar

Clio (Documentation Generator) passes only when the documentation package is reviewer-ready, traceable to selected Homework 6 artifacts, privacy-safe, and clear about evidence.

## Required Gates

- Version traceability names the selected Athena run ID, selected Hephaestus run ID and inventory, selected Themis run ID and inventory, final-selection record, and current canonical `specification.md` fingerprint.
- Prior homework documentation context records author source and style sources. Current author source is `Igor Tanatarov` from Homeworks 1 through 4 README files.
- Candidate docs and screenshots are listed in `agent-4-docs/outputs/inventory.md` with canonical targets and SHA-256 fingerprints.
- Runtime and tool outputs are excluded from selectable inventory: `evidence/`, `review/`, `shared/`, `archive/`, `.coverage*`, `.pytest_cache/`, `.test-tmp/`, `tmp/`, and `__pycache__/`.
- `README.md` includes the student author name, what the system does, Homework Automation Layer responsibilities, runtime pipeline agent responsibilities, ASCII architecture diagram, tech stack table, quick start, documentation map, and AI tools/workflow summary.
- `HOWTORUN.md` provides numbered setup, run, validation, MCP, command/hook, screenshot, troubleshooting, and cleanup steps.
- `ARCHITECTURE.md` describes Operator Layer, Homework Automation Layer, Generated Transaction System Layer, JSON file protocol, runtime components, selection/run preservation, privacy/audit design, and known limitations.
- `TESTING_GUIDE.md` describes the selected Themis suite, test strategy, unit/integration coverage, coverage gate, command/hook validation, privacy checks, fixture isolation, and manual checklist.
- `API_REFERENCE.md` documents command interfaces, JSON file protocol shapes, result and summary shapes, validation-only behavior, and custom MCP tools/resource.
- `docs/pr-description-draft.md` is standalone and includes summary, AI workflow, verification commands/results, screenshot links, reviewer run instructions, known limitations, and omitted operator challenges narrative.
- Required stable screenshots exist or missing-capture notes provide exact manual capture steps:
  - `docs/screenshots/pipeline-run.png`
  - `docs/screenshots/test-coverage.png`
  - `docs/screenshots/skill-run-pipeline.png`
  - `docs/screenshots/hook-trigger.png`
  - `docs/screenshots/mcp-interaction.png`
- Operator-sourced screenshots remain preserved under `docs/screenshots/operator-sourced/`.
- Screenshot inventory records used and unused source screenshots with privacy/safety status and target mapping.
- Pipeline, test, coverage, command/hook, and MCP evidence is fresh when local tooling permits, or blockers are explicitly recorded.
- Evidence and docs avoid raw account IDs, raw descriptions, credentials, tokens, secrets, full audit payloads, and unfiltered metadata dumps.
- Documentation frames the banking pipeline as an educational simulation, not legal, banking, AML, sanctions, or payment-network compliance.
- Clio documents selected Themis evidence but does not silently modify or replace the selected Themis suite.
- Clio does not change runtime product code, MCP server behavior, command/hook support surfaces, or selected run records without explicit operator authorization.

## Scope Rejection

Reject or pause a Clio run when it attempts to:

- Target "latest" instead of named selected Athena, Hephaestus, and Themis versions.
- Modify selected runtime product code without explicit operator repair authorization.
- Generate, fork, or replace selected tests without Themis follow-up or explicit final test-hardening authorization.
- Delete, edit, or prune `docs/screenshots/operator-sourced/`.
- Use unsafe screenshots or evidence containing raw account IDs, raw descriptions, credentials, tokens, or unfiltered metadata.
- Include an operator challenges or feedback narrative in the final docs or PR draft.
- Make generated documentation depend on `dev-doc-harness`, Superpowers, hidden chat state, or unavailable plugins.
- Copy run-local evidence, review notes, caches, runtime output, or tool-output folders to canonical documentation targets.

## Review Questions

- Can a reviewer run the pipeline, tests, coverage gate, validation command, and MCP status checks from the docs alone?
- Can a reviewer identify exactly which Athena, Hephaestus, Themis, and Clio runs produced the submission?
- Does README make the author, purpose, automation agents, runtime components, stack, and architecture obvious within the first screen?
- Do screenshots cover the assignment table and remain safe for review?
- Would a stale selected test suite, missing MCP evidence, or screenshot gap be visible in the validation checklist?
- Does the PR draft stand alone without requiring the reviewer to infer details from chat history?

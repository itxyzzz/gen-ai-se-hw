# Generate Tests Quality Bar

Themis (Test Generator) must treat coverage as necessary but not sufficient. A candidate suite passes only when it demonstrates behavior, isolation, privacy, and support-tool quality for a named selected Hephaestus (Code Generator) package.

## Required Gates

- Version traceability names the selected Hephaestus run ID, inventory path, final-selection record, source Athena run ID, source spec fingerprint, current canonical spec fingerprint, and selected code fingerprints.
- Source spec mismatch is explicitly reported when the selected code source spec differs from current canonical `specification.md`.
- Candidate tests and config are listed in `agent-3-tests/outputs/inventory.md` with canonical targets and SHA-256 fingerprints.
- Runtime and tool outputs are excluded from selectable inventory: `workspace/`, `evidence/`, `shared/`, `archive/`, `.coverage*`, `.pytest_cache/`, `.test-tmp/`, and `__pycache__/`.
- Coverage is at least 80 percent using the selected stack's coverage tool.
- Tests include meaningful assertions over output fields, statuses, reason codes, summary counts, file movements, and redaction behavior, not only "does not crash" checks.
- Unit tests cover each runtime component selected for the pipeline.
- At least one integration test covers the full pipeline.
- Validator dry-run behavior is tested or validated without running the full pipeline.
- `/run-pipeline` behavior is validated through the Operator Layer surface or compact evidence.
- `/validate-transactions` behavior is validated through the Operator Layer surface or compact evidence.
- Coverage hook pass and blocking paths are validated, including a hook-trigger path such as `--fail-under 99`.
- Fixtures are isolated from root `shared/`; tests use temp paths or workspace-local shared directories.
- Repeated-run archival behavior is tested or validated.
- Runtime provenance is tested when present in selected code.
- Decimal money assertions check exact string or `Decimal` behavior and never rely on binary floating point.
- Strict JSON behavior covers invalid JSON, missing fields, unsupported currency such as `XYZ`, and malformed amounts where applicable.
- Privacy scans prove raw account IDs, raw descriptions, credentials, tokens, and unfiltered metadata are not exposed in results, audit files, command output, screenshots, or evidence.
- Workspace containment checks prove root `tests/`, root `shared/`, root `.coverage`, and canonical product files are unchanged during ordinary generation.

## Scope Rejection

Reject or pause a Themis run when it attempts to:

- Target "latest" instead of a named selected Hephaestus version.
- Modify runtime product code without explicit operator repair authorization.
- Implement Task 4 MCP server/config.
- Produce final README, HOWTORUN, screenshot, or PR-description materials owned by Clio (Documentation Generator).
- Regenerate `/run-pipeline`, `/validate-transactions`, the coverage helper, Git hook, or Claude hook settings as routine per-run outputs.
- Copy `workspace/` or runtime evidence wholesale into canonical root targets.

## Review Questions

- Would a changed validator, fraud detector, settlement processor, or reporting component break at least one test?
- Would a privacy leak of a raw account ID or raw description be caught?
- Would stale command or hook wiring be visible in evidence?
- Would rerunning the pipeline twice prove archival behavior rather than overwriting prior evidence?
- Can a reviewer identify exactly which selected Hephaestus package this suite targets?

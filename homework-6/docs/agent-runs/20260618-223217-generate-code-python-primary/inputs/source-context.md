# Source Context

This Hephaestus (Code Generator) run read these source artifacts before implementation:

- `specification.md`: selected Python transaction-processing pipeline specification from Athena (Spec Writer) run `20260618-003908-write-spec-python-replacement`.
- `sample-transactions.json`: canonical eight-record sample input.
- `agents.md`: Homework 6 layer glossary, privacy/audit rules, Context7 rules, and Hephaestus responsibilities.
- `TASKS.md`: Homework 6 assignment Task 2 boundaries and required deliverables.
- `../AGENTS.md`: repository workflow guardrails.
- `../HOMEWORK_STANDARDS.md`: homework branch, changelog, verification, and documentation standards.
- `../README.md`: repository submission context.
- `mcp.json`: Context7 server configuration; left unchanged for Task 2.
- `.codex/config.toml`: Context7 server and `agents.max_threads = 8`; left unchanged for Task 2.
- `.agents/skills/generate-code/SKILL.md`: Codex skill wrapper.
- `agent-control/generate-code/workflow.md`: canonical Hephaestus workflow.
- `agent-control/generate-code/quality-bar.md`: generated code and validation quality bar.
- `agent-control/generate-code/run-registry.md`: run preservation and evidence rules.

Context7 queries performed during code generation:

- `/python/cpython`: Decimal construction from strings, non-finite checks, strict JSON serialization with `allow_nan=False`, and `pathlib` file I/O.
- `/pytest-dev/pytest`: `tmp_path` fixture usage for isolated filesystem tests.

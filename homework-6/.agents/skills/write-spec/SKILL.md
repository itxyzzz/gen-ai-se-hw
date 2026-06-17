---
name: write-spec
description: Generate or refine the Homework 6 multi-agent banking pipeline specification package. Use when asked to create the Homework 6 specification, run Agent 1, compare specification-generation runs, select a generated spec run, or prepare the spec handoff for downstream code, test, and documentation agents.
---

# Write Spec

Use this skill to create, resume, compare, or select preserved Homework 6 Agent 1 specification runs. It is the Codex Markdown entrypoint for the same core workflow used by the Claude Code `/write-spec` slash command.

## Canonical Workflow

Before acting, read these references from this skill folder:

1. `references/write-spec-workflow.md`: canonical workflow for both Codex and Claude Code surfaces.
2. `references/stack-profiles.md`: fixed stack enum, default stack, and stack-specific generation profiles.
3. `references/write-spec-quality-bar.md`: generated spec, run, comparison, and handoff quality bar.

Treat `write-spec-workflow.md` as the source of truth if this wrapper and the slash command differ.

## Invocation Defaults

- Default mode: `generate`.
- Default stack: `stack=python`.
- Supported stacks: `stack=python` and `stack=java`.
- Unsupported: `stack=auto` and every unlisted stack.

Reject unsupported stack values before drafting or selecting output.

## Execution

Follow the canonical workflow exactly:

1. Load assignment and repository context in the workflow's required order.
2. Normalize mode and stack.
3. Preserve generated outputs under `homework-6/docs/agent-runs/<run-id>/`.
4. Research with Context7 and current sources when available, or record fallback limitations.
5. Draft run outputs before canonical copy.
6. Validate with `write-spec-quality-bar.md` and the selected stack profile.
7. Compare and select only when requested.

When repository instructions require `dev-doc-harness` or Superpowers, comply with their planning and freeze gates. The generated Homework 6 agents must remain usable without those tools.

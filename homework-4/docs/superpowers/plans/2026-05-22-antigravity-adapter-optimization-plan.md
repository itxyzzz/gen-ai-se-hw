# Google Antigravity Adapter Optimization & Generic Harness Implementation Plan (COMPLETED)

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Refactor the Homework 4 pipeline harness and align all adapters for total consistency, clean vendor-specific runner procedures out of the `/skills` folder, and update each adapter with appropriate model selections and agency support guidelines.

---

## Task 1: Generic Harness Enhancements

- [x] Modify `homework-4/skills/pipeline-harness-wrapper.md` to:
  - Add an optional "## Reusable Agentic Execution Extensions" section.
  - Detail guidance for subagent isolation, automated reflection loops, and local static analysis.

## Task 2: Codex Chat Adapter Refactoring & De-duplication

- [x] Merge trigger, procedure, required artifacts, and quality gates from `skills/codex-chat-pipeline.md` directly into `adapters/codex-chat.md`.
- [x] Safely delete `homework-4/skills/codex-chat-pipeline.md` from the workspace.

## Task 3: Dedicated Adapters Alignment & Model Mapping

- [x] Modify `homework-4/adapters/google-antigravity.md`:
  - Add the concrete **Model Selection** table for Gemini 3.5 Pro and Gemini 3.5 Flash.
  - Detail the tool-based **Orchestration Procedure** (subagents, reflection loops, local testing, and promotion rules).
  - Streamline validation checklists.
- [x] Modify `homework-4/adapters/claude-code.md`:
  - Remove all Codex-specific dependencies and map workspace/artifacts to the universal harness.
  - Add the **Model Selection** table for Anthropic Claude models (e.g. `claude-3-5-sonnet`, `claude-3-5-haiku`).
  - Direct the Claude Code runner to utilize background subagents where supported.
- [x] Modify `homework-4/adapters/open-code.md`:
  - Add the **Model Selection** table for leading open-source models (e.g. `llama-3.3-70b-instruct`, `llama-3.1-8b-instruct`).
  - Align its execution guidelines with the optional agentic extensions.

## Task 4: Generic Adapter Enhancements

- [x] Modify `homework-4/adapters/generic-agent.md`:
  - Add directions linking capable tools to the harness's optional agentic extensions.
  - Add general model tier guidance (high-reasoning vs fast-coding).

## Task 5: Documentation & Changelog Update

- [x] Create a new entry `## Homework 4 - Step 11` at the very top of `homework-4/CHANGELOG.md` detailing the harness cleanups, adapter alignment, and model mappings.
- [x] Review all modified markdown files for syntax correctness.

## Task 6: Verification

- [x] Run baseline tests to verify they fail as expected:
  `node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js`
- [x] Run current tests to verify the application remains fully functional:
  `node --test --test-isolation=none homework-4/app/current/tests/*.test.js`
- [x] Confirm no vendor-specific files remain under `homework-4/skills/`.
- [x] Perform a Git diff check before finalizing.

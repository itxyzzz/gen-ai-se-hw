# Homework 4 Agent Instructions

## Development And Maintenance

Use this section when editing Homework 4 files, documentation, plans, agents,
skills, adapters, evidence, or benchmark artifacts.

- Keep `CHANGELOG.md` newest-first. Add each new step at the top, directly under
  the title and any standing historical notes.
- Keep portable agent specs model-agnostic. They may declare `model_policy` and
  `reasoning_effort`, but concrete model names belong in adapter instructions
  and run metadata.
- Keep portable agent specs current-run based. Do not hard-code a specific
  `runs/<scenario>/<run-folder>` path inside agent instructions.
- If run evidence is renamed, update the run contract, metadata, benchmark
  artifacts, screenshots, and docs in the same change.

## Pipeline Execution

Use this section when the user asks to run, launch, execute, continue, validate,
or otherwise operate the Homework 4 agentic pipeline. Exact wording is not
required. Short prompts such as `Run HW4 pipeline`, `Start homework 4 pipeline`,
or `Запусти пайплайн HW4` are sufficient.

When pipeline intent is detected:

1. Load `skills/pipeline-harness-wrapper.md`.
2. Choose the adapter automatically from the active agentic tool:
   - Codex uses `adapters/codex-chat.md`.
   - Claude Code uses `adapters/claude-code.md`.
   - Open Code uses `adapters/open-code.md`.
   - Google Antigravity uses `adapters/google-antigravity.md`.
3. If no dedicated adapter exists, use `adapters/generic-agent.md` only when it
   is sufficient for the current tool.
4. Ask the user which adapter to use only when no dedicated adapter exists and
   no generic mapping can be applied safely.
5. Use the run selected by the harness as the current run.

## Benchmarking

Use this section when the user asks to compare, score, benchmark, or review runs.

- Benchmarking is text-based review of preserved run artifacts, not an
  executable benchmark script.
- Score only completed runs with the required artifact contract.
- Use `benchmark/scoring-rubric.md` as the scoring source.
- Do not infer missing test or security results; record gaps plainly.

# Homework 4: Pure Agentic Bug-Fixing Pipeline

## Summary

This PR completes Homework 4 with a portable, text-first multi-agent
bug-fixing pipeline. The submitted workflow is launched by one canonical agentic
phrase:

```text
Run HW4 pipeline
```

The phrase loads the Homework 4 Markdown harness, resolves the active
tool-specific adapter, runs the required stages in order, applies fixes to a
small quote-calculator app, records security review output, and generates unit
tests for the changed code.

Submitted package surface:

| File or folder | Purpose |
| --- | --- |
| `homework-4/agents/` | Required agent specs plus helper stages for research and planning |
| `homework-4/skills/research-quality-measurement.md` | Research quality labels and required verifier output shape |
| `homework-4/skills/unit-tests-FIRST.md` | FIRST criteria for generated unit tests |
| `homework-4/skills/pipeline-harness-wrapper.md` | Text-first orchestration harness used by the launch phrase |
| `homework-4/adapters/` | Tool-specific adapters for Codex Chat, Google Antigravity, Open Code, Claude Code, and a generic fallback |
| `homework-4/scenarios/bug-001/` | Seeded scenario context, research, and implementation plan |
| `homework-4/app/baseline/` | Runnable buggy baseline with two defects and one path traversal issue preserved |
| `homework-4/app/current/` | Fixed quote-calculator app with generated regression tests |
| `homework-4/runs/bug-001/` | Immutable source run evidence from Codex, Open Code, and other model/tool attempts |
| `homework-4/benchmark/` | Normalized benchmark comparison, scoring rubric, and per-run artifacts |
| `homework-4/docs/screenshots/` | PR evidence images for pipeline, tests, security, benchmark, and Codex preparation |
| `homework-4/README.md` and `homework-4/HOWTORUN.md` | Reviewer entry point and runnable instructions |
| `homework-4/API_REFERENCE.md`, `ARCHITECTURE.md`, `TESTING_GUIDE.md`, `CHANGELOG.md` | Supporting docs required by the homework standards |

The original task asked for the four required agents, skills, screenshots,
fixes, and unit tests. This submission intentionally goes further by preserving
multiple tool/model runs and adding a small benchmark comparison.

## Important Implementation Notes

- Implemented the required four agents: Research Verifier, Bug Fixer, Security
  Verifier, and Unit Test Generator.
- Added helper Bug Researcher and Bug Planner stages to match the assignment's
  stated run order before the required four-agent handoff.
- Created the required research-quality and FIRST skills and required their use
  in verifier/test-generator outputs.
- Chose a small Node.js quote-calculator app with two intentional correctness
  bugs and one path traversal issue in the baseline.
- Preserved `app/baseline` as the buggy input and promoted the fixed result into
  `app/current`.
- Interpreted the homework's "single command" requirement as a single agentic
  skill/harness invocation through chat, as discussed during implementation.
- Moved from an initial Node.js orchestration script toward a text-only
  pipeline because that better matched the intended agentic homework exercise.
- Kept portable agent specs model-policy based, with concrete model mappings in
  the adapters and per-run metadata.
- Added normalized benchmark artifacts so Codex, Open Code, and model-specific
  runs can be compared without mutating preserved source snapshots.

## AI Tools And Workflow

Most of the work was done in the Codex app with Codex. The first design included
a Node.js script for literal slash-command-style pipeline execution, but that
was scaled down so the final submission remains a text-first agentic pipeline.

That interpretation question motivated a deliberate extension: making the
pipeline portable across Codex, Google Antigravity, and Open Code. These tools
support different model sets. Open Code was especially useful for comparison
because it can run free models, OpenAI subscription models, and a broad range of
OpenRouter models.

Google Antigravity was used to refine the separation between agents, skills, the
universal harness, and tool-specific adapters. It also improved its own adapter
by documenting more concrete use of Antigravity orchestration features.

Open Code was used for adapter validation and benchmark runs. The Open Code work
showed meaningful behavioral differences between models. Gemini was more
aggressive about spawning sub-agents. Nemotron 3 Super (free) reported that it
was too restricted in the current environment and did not spawn sub-agents. It
also overreached once by trying to continue improving the whole homework after
the pipeline run; that unrelated work was stopped and reverted.

An Antigravity run with Gemini 3.5 Flash did not finish because it hit
free-account limits. Completed per-token paid model runs cost consistently about
`$2` per pipeline run.

All tools used understand skill invocation either by name or by recognized
intent, but none has Claude Code-style `/skill-name` commands. The closest
option observed was `$skill-name` references in Codex CLI.

## Challenges And How They Were Addressed

- **Command versus skill invocation**: The assignment language says "single
  command." The submitted solution treats this as a single chat invocation that
  loads a skill and adapter automatically. The README and HOWTORUN call this out
  directly.
- **Over-scoped initial harness**: Early work included a JavaScript pipeline
  runner. It was removed from the final architecture to keep the focus on a
  portable agentic workflow rather than a custom local orchestrator.
- **Tool portability**: Different tools expose different model selection,
  sub-agent, command-running, and skill semantics. The adapters now isolate those
  differences while preserving the same artifact contract.
- **Benchmark fairness**: Source run folders are immutable, so normalized
  benchmark folders were added instead of renaming or rewriting historical run
  evidence.
- **Model overreach**: One Open Code/Nemotron run began making unrelated
  homework improvements after the requested pipeline run. That work was stopped
  and reverted, and the behavior is documented as part of the comparison notes.

## Screenshots

AI planning, adapter refinement, pipeline execution, and benchmark evidence:

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-4-submission/homework-4/docs/screenshots/2026-05-23%2023_58_06-Codex-initial-design.png" alt="Codex initial Homework 4 pipeline design discussion" width="300">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-4-submission/homework-4/docs/screenshots/2026-05-23%2023_59_28-Codex-scaledown.png" alt="Codex scaling the implementation down to a text-only agentic pipeline" width="300">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-4-submission/homework-4/docs/screenshots/2026-05-23%2000_46_19-Antigravity-planning.png" alt="Google Antigravity planning adapter and harness improvements" width="300">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-4-submission/homework-4/docs/screenshots/2026-05-23%2000_51_35-Antigravity-agents.png" alt="Google Antigravity agent separation and orchestration details" width="300">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-4-submission/homework-4/docs/screenshots/2026-05-23%2000_37_11-OpenCode-adapter.png" alt="Open Code adapter refinement for mixed model environments" width="300">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-4-submission/homework-4/docs/screenshots/2026-05-23%2000_34_17-OpenCode-pipeline1.png" alt="Open Code pipeline run evidence part 1" width="300">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-4-submission/homework-4/docs/screenshots/2026-05-23%2000_33_05-OpenCode-pipeline2.png" alt="Open Code pipeline run evidence part 2" width="300">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-4-submission/homework-4/docs/screenshots/2026-05-23%2000_31_21-OpenCode-pipeline3.png" alt="Open Code pipeline run evidence part 3" width="300">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-4-submission/homework-4/docs/screenshots/2026-05-24%2000_00_29-Codex-benchmark.png" alt="Codex benchmark and comparison preparation" width="300">

Additional screenshot artifacts are available in `homework-4/docs/screenshots/`.

## How To Run And Verify

Review the package in this order:

1. `homework-4/README.md`
2. `homework-4/TASKS.md`
3. `homework-4/HOWTORUN.md`
4. `homework-4/skills/pipeline-harness-wrapper.md`
5. `homework-4/adapters/codex-chat.md`
6. `homework-4/agents/`
7. `homework-4/runs/bug-001/`
8. `homework-4/benchmark/bug-001/`

Canonical pipeline invocation from an agentic chat tool:

```text
Run HW4 pipeline
```

Verify the fixed app from the repository root:

```powershell
node --test --test-isolation=none homework-4/app/current/tests/*.test.js
```

Verify that the baseline still contains the seeded defects:

```powershell
node --test --test-isolation=none homework-4/app/baseline/tests/*.test.js
```

The fixed app test command should pass. The baseline command is expected to fail
because the original intentional defects are still present there.

More detailed review instructions are in `homework-4/HOWTORUN.md`. The reviewer
entry point is `homework-4/README.md`, and the implementation history is in
`homework-4/CHANGELOG.md`.

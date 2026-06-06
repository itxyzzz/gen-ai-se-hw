# Google Antigravity Adapter

This adapter configures the Homework 4 agentic pipeline for advanced, tool-based autonomous execution under the **Google Antigravity** environment, utilizing background subagents and reflection loops.

## Launch Phrase

```text
Run Homework 4 bug-001 through the full agentic pipeline using the Google Antigravity adapter.
```

## Model Selection

Concrete Gemini model policies. The primary mappings use the models currently
available for this homework environment; future stronger models may be used as
drop-in alternatives when they are available in Google Antigravity.

| Model policy | Primary Gemini model | Alternative when available | Reasoning / Effort |
| --- | --- | --- | --- |
| `research-high` | Gemini 3.1 Pro | Gemini 3.5 Pro or newer Pro-tier model | High reasoning for deep codebase context analysis |
| `verification-high` | Gemini 3.1 Pro | Gemini 3.5 Pro or newer Pro-tier model | High reasoning for strict research verification |
| `planning-high` | Gemini 3.1 Pro | Gemini 3.5 Pro or newer Pro-tier model | High reasoning for precise implementation plans |
| `implementation-medium` | Gemini 3.5 Flash | Newer Flash-tier coding model | Fast and efficient for mechanical code changes |
| `security-high` | Gemini 3.1 Pro | Gemini 3.5 Pro or newer Pro-tier model | High reasoning for finding complex security vulnerabilities |
| `test-medium` | Gemini 3.5 Flash | Newer Flash-tier coding model | Fast and reliable for standard unit-test generation |

## Mapping & Subagent Orchestration Procedure

Google Antigravity must execute the stages sequentially using its native background subagent delegation tools to prevent context bloat:

1. **Environment Initialization:**
   - Create the run workspace folder with the normalized pattern, for example
     `homework-4/runs/bug-001/run-001-google-antigravity-gemini-3.1-pro`.
   - Copy `homework-4/app/baseline` to the run workspace. Do not modify `app/baseline`.
   - Initialize `command-log.md` and `run-metadata.json` under the workspace.
2. **Execute Stages sequentially using Subagents:**
   For each of the six stages in harness order:
   - **Stage 1 (Bug Researcher):** Define a specialized researcher subagent using `define_subagent` and invoke it with `agents/bug-researcher.agent.md`. Output: `research/codebase-research.md`.
   - **Stage 2 (Research Verifier):** Define a verification subagent, load `skills/research-quality-measurement.md`, and verify research files. Output: `research/verified-research.md`.
   - **Stage 3 (Bug Planner):** Define a planner subagent, analyze verified research, and write `implementation-plan.md`.
   - **Stage 4 (Bug Fixer with Reflection):** 
     - Define an implementation subagent and apply the edits inside the workspace.
     - **Reflection Loop:** Run `node --test --test-isolation=none homework-4/runs/bug-001/<run-folder>/app/tests/*.test.js`. If tests fail, feed back the failing test and failure message to the implementation subagent for automatic self-correction. Repeat up to 3 times.
     - Output: `fix-summary.md` and modified source files.
   - **Stage 5 (Security Verifier):** Define a security subagent. Perform a read-only audit of changed code. Output: `security-report.md`. If critical/high issues are found, block the run.
   - **Stage 6 (Unit Test Generator with Reflection):**
     - Define a test subagent, load `skills/unit-tests-FIRST.md`, and generate unit tests for the changed code.
     - Run `node --test` to confirm all tests pass. If tests fail, automatically repair (up to 3 times).
     - Output: `test-report.md` and updated test files.
3. **Promotion & Evidence Collection:**
   - Generate `patch.diff` between the baseline app and the run folder's app.
   - Copy the run folder's app to `homework-4/app/current`.
   - Set `"promoted": true` in `run-metadata.json` and log the promotion.

The launch contract already requires sub-agents. Do not ask for an extra
default confirmation before `define_subagent` or `invoke_subagent`. If the
active Antigravity surface refuses to invoke subagents until the operator gives
explicit authorization, stop and ask for authorization to spawn subagents. Do
not fall back to direct execution unless subagent tooling is unavailable, or
still unusable after the authorization path, and the operator explicitly
approves fallback for that run.

## Runtime Sub-Agent Audit

Populate `runtimeSubagentAudit` in `run-metadata.json` with
`collectionMode: "native-hook"` when Google Antigravity hook events are
available. Capture sub-agent invocations with `PostToolUse` matched to
`invoke_subagent`; include `define_subagent` evidence where it identifies the
stage role, model, or subagent profile. Use a `Stop` hook as the final
completeness gate when available.

Map runtime evidence into compact audit events. Include stage id, agent file,
launch mechanism, expected model policy, requested or observed Gemini model,
reasoning effort, status, and evidence source when exposed. If native events are
not available for a run, use `collectionMode: "adapter-recorded"` or
`manual-unavailable` with a clear reason.

Record `operatorAuthorization.status` as `pipeline-mandated`,
`authorized-after-tool-gate`, `fallback-approved`, or `declined`.

## Validation Checklist

- `run-metadata.json` specifies adapter name as `google-antigravity` and concrete Gemini models.
- Spawning dedicated subagents via `define_subagent` and `invoke_subagent` is logged.
- `run-metadata.json` contains `runtimeSubagentAudit`.
- Direct execution fallback is absent unless explicitly approved after
  unavailable or unusable subagent tooling.
- Security report is strictly read-only and does not modify code files.
- Unit test report includes FIRST assessment.
- Verified fixed app promoted to `app/current` passes all tests.

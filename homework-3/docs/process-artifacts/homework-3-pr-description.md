# Homework 3: EU Payment-Account Dispute Intake Specification

## Summary

This PR completes Homework 3 with a documentation-only specification package for EU/EEA payment-account Dispute Intake. The package defines how end users can file disputes against posted transactions and how authorized internal ops/compliance users can review cases, request more information, apply intake outcomes, and preserve audit-safe evidence.

Submitted package surface:

| File | Purpose |
| --- | --- |
| `homework-3/specification.md` | Core layered specification: objective, scope, mid-level objectives, state machine, implementation notes, context, edge cases, verification, performance, and low-level tasks |
| `homework-3/agents.md` | AI and human agent behavior contract for documentation-first work in this domain |
| `homework-3/.github/copilot-instructions.md` | Compact editor-specific AI rules pointing back to the full agent contract |
| `homework-3/README.md` | Reviewer entry point, package map, rationale, AI workflow notes, and best-practice references |
| `homework-3/HOWTORUN.md` | Documentation-only review and verification guide |
| `homework-3/docs/domain-rules.md` | EU/EEA payment-account regulatory and domain rationale, scoped to this homework |
| `homework-3/docs/technical-conventions.md` | Reusable conventions for audit, IDs, money, state, idempotency, errors, pagination, logging, and redaction |
| `homework-3/docs/development-process.md` | Spec-first workflow and verification gates |
| `homework-3/docs/operator-manual.md` | Internal operator queue, review, escalation, audit-note, and manual-check expectations |

The submission intentionally contains no application code, API implementation, UI implementation, database migrations, real evidence files, chargeback integration, provisional credit workflow, or regulator reporting workflow. The graded artifact is the written specification package and its traceability from goals down to implementable low-level tasks.

## Important Implementation Notes

- Selected **Dispute Intake** as the finance feature because it naturally includes end-user and internal ops/compliance stakeholders without requiring real money movement.
- Framed the package around an EU/EEA payment-account context, using PSD2, GDPR, DORA, EBA ICT/security guidance, and complaints-handling guidance as scoped rationale without claiming legal compliance.
- Added a layered `specification.md` with high-level objective, scope boundary, five mid-level objectives, state machine, non-functional/policy expectations, implementation notes, beginning/ending context, edge cases, verification, performance targets, and low-level tasks.
- Reworked low-level tasks into a multi-level hierarchy mapped to `M1` through `M5`, with implementation prompts, core behavior, edge cases, acceptance criteria, and verification notes.
- Added `agents.md` and `.github/copilot-instructions.md` to define AI behavior, context order, sensitive-data rules, verification expectations, and source-of-truth routing.
- Added active supporting docs for domain rationale, technical conventions, development process, and operator behavior.
- Preserved historical process evidence under `docs/process-artifacts/` and AI-assistance plans under `docs/superpowers/plans/`, while keeping active requirements in the main package docs.
- Added screenshots under `docs/screenshots/` for PR evidence.

## AI Tools And Workflow

The initial plan was three-step:

1. **Agentic starting point**
   - Built the initial AI-agent harness around `agents.md`, `.github/copilot-instructions.md`, and supporting docs.
   - This step went well and produced a useful context order, source-of-truth routing, and documentation-first workflow for future AI work.

2. **Banking regulation research**
   - Used ChatGPT Deep Research without specifying the homework context.
   - The result was a large regulatory research document that was too broad for the assignment.
   - Codex then adapted and scaled that research back to a realistic Homework 3 scope, resulting in the EU/EEA payment-account framing and the scoped rationale in `docs/domain-rules.md`.

3. **Specification writing**
   - Used Codex to write and revise the actual Dispute Intake specification.
   - The first attempt tried to force too much into one pass and had to be redone from top to bottom with a tighter feature boundary.
   - The final low-level tasks were rewritten step by step, objective by objective, rather than generated as one large block.

The main process lesson was about task sizing. It turned out to be useful to ask Codex whether the next task was small enough for one pass, should be split into smaller steps, or should be handed off to a fresh thread. That advice became part of the workflow and helped keep the final specification controlled.

## Challenges And How They Were Addressed

- **Research scope was too broad**: ChatGPT Deep Research produced a large banking-regulation document because it was intentionally not constrained by the homework context. Codex narrowed it into a practical EU/EEA payment-account Dispute Intake rationale and moved unsupported or overly specific legal claims out of scope.
- **First spec pass was too large**: The initial attempt tried to generate too much of the specification at once. This was addressed by rebuilding the spec from the top down with a smaller feature boundary and clearer document ownership rules.
- **Low-level tasks needed more decomposition**: The first low-level task section was too generic. It was rewritten incrementally into concrete `M1` through `M5` task groups with checkable acceptance criteria and verification notes.
- **Avoiding overclaiming in a regulated domain**: The package needed to sound realistic without inventing legal deadlines, refund obligations, chargeback rules, regulator reporting duties, retention periods, or ADR outcomes. Those boundaries are now explicit in the spec, README, domain rules, and agent guidance.
- **Keeping process evidence without polluting active docs**: The low-level task rewrite handoff was useful evidence, but did not belong beside normative docs. It was moved to `docs/process-artifacts/`.

## Screenshots

AI planning, research, implementation, and verification evidence:

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-3-submission/homework-3/docs/screenshots/2026-05-08%2019_20_35-Codex-1-harness.png" alt="Codex harness setup for Homework 3" width="300">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-3-submission/homework-3/docs/screenshots/2026_05_08_19_33_30_ChatGPT_deep_research_AI_driven_Fintech_Regulations.png" alt="ChatGPT Deep Research for fintech regulations" width="300">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-3-submission/homework-3/docs/screenshots/2026-05-08%2022_32_37-Codex-2-domain1.png" alt="Codex adapting domain research part 1" width="300">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-3-submission/homework-3/docs/screenshots/2026-05-08%2022_34_49-Codex-3-domain2.png" alt="Codex adapting domain research part 2" width="300">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-3-submission/homework-3/docs/screenshots/2026-05-09%2000_42_56-Codex-4-feature-selection1.png" alt="Codex feature selection discussion part 1" width="300">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-3-submission/homework-3/docs/screenshots/2026-05-09%2000_43_42-Codex-5-feature-selection2.png" alt="Codex feature selection discussion part 2" width="300">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-3-submission/homework-3/docs/screenshots/2026-05-09%2000_44_55-Codex-6-Add-dispute-intake.png" alt="Codex adding Dispute Intake specification" width="300">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-3-submission/homework-3/docs/screenshots/2026-05-09%2015_04_58-Low-level-rewrite.png" alt="Codex low-level task rewrite" width="300">

<img src="https://raw.githubusercontent.com/itxyzzz/gen-ai-se-hw/homework-3-submission/homework-3/docs/screenshots/2026-05-09%2019_10_20-Thread-mngment.png" alt="Codex thread management and handoff discussion" width="300">

Additional screenshots are available in `homework-3/docs/screenshots/`.

## How To Run And Verify

Homework 3 is documentation-only. There is no application server, API, UI, database, test runner, or build command to run.

Review the package in this order:

1. `homework-3/README.md`
2. `homework-3/TASKS.md`
3. `homework-3/specification.md`
4. `homework-3/agents.md`
5. `homework-3/.github/copilot-instructions.md`
6. Active supporting docs under `homework-3/docs/`

Optional local checks from the repository root:

```powershell
git status --short
git diff --check
$markers = 'TO' + 'DO|TB' + 'D|FIX' + 'ME'
rg -n $markers homework-3
```

More detailed review instructions are in `homework-3/HOWTORUN.md`. The main reviewer entry point is `homework-3/README.md`, the graded specification is `homework-3/specification.md`, and the change history is in `homework-3/CHANGELOG.md`.

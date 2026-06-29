# Root README Portfolio Router Spec

Work ID: `2026-06-24-root-readme-portfolio-router`
Short ID: `root-readme-portfolio-router`
Status: Approved
Harness release: `0.4.0`
Schema: `schema:spec.small-medium`
Policy references: `module:lifecycle`, `module:quality`, `rule:lifecycle.documentation-matrix`, `rule:lifecycle.commit-message-format`, `rule:quality.spec-handoff`

## Goal

Rewrite the repository root `README.md` so an external human or agentic reviewer first sees a concise portfolio narrative for the six-homework course sequence, the AI-engineering-adjacent skills demonstrated, and the safest claim boundaries for job-search use. Preserve the existing assignment-template README text at the end of the file under a clear label that identifies it as the original homework/assignment README formulation.

## Scope

- Rewrite only the root `README.md` during implementation.
- Add new reviewer-facing content at the top of `README.md`.
- Preserve the existing root README content at the end of `README.md` under an explicit original-assignment label.
- Present a connected narrative across Homework 1 through Homework 6.
- Highlight skills relevant to AI-engineering-adjacent roles, including requirements analysis, systems decomposition, AI-assisted workflow orchestration, agentic workflow design, MCP integration, testing, documentation, and claim-safety judgment.
- Use the six homework `README.md` files and actual GitHub PR descriptions as primary source material.
- Use the attached review excerpt as guidance for positioning, caveats, and portfolio-readiness concerns.
- Update the root `CHANGELOG.md` only when required by the harness approval and implementation commit gates.
- Keep planning artifacts in this work item package under `docs/work-items/2026-06-24-root-readme-portfolio-router/`.

## Non-scope

- No changes to homework-specific files or documentation.
- No changes to source code, tests, run scripts, screenshots, MCP configuration, or PR bodies.
- No new screenshots, badges, generated assets, diagrams, or hosted pages.
- No claims that the coursework proves production banking, real fraud detection, AML/KYC, PCI, cloud/SRE, ML model development, RAG, or compliance-specialist expertise.
- No use of draft PR descriptions as primary source when actual GitHub PR descriptions are available.
- No duplicate Superpowers spec under `docs/superpowers`; this harness package is the canonical planning source for the work.

## Current state

The root `README.md` is still primarily the course homework template. It explains how to fork, clone, submit homework PRs, and satisfy assignment requirements, but it does not act as a portfolio entry point for the completed repository. It undersells the completed capstone and does not route a reviewer toward the strongest evidence in Homework 3 through Homework 6.

The attached review excerpt identifies the repository as stronger than the older baseline, especially after Homework 6, but calls out that the root README remains weak as a portfolio router. The review recommends adding a root-level portfolio README or project index, a "what to look at first" section, and a concise authorship/AI-assistance note with careful claim boundaries.

## Proposed behavior

The rewritten root `README.md` should open with a concise, dense portfolio overview that makes the repository legible as a completed AI-assisted software engineering course portfolio rather than a blank assignment template.

The new top section should:

- State the portfolio framing and claim boundary: coursework and local prototypes, not production financial infrastructure.
- Provide a short "what to review first" router, likely emphasizing Homework 6, Homework 4, Homework 3, and then the implementation/API foundations in Homeworks 1, 2, and 5.
- Show the progression across all six homeworks:
  - Homework 1: Java/Spring Boot banking transaction API with validation, per-currency balances, account summaries, tests, manual API evidence, and lifecycle scripts.
  - Homework 2: customer-support API with CRUD, imports, deterministic classification, OpenAPI/Swagger, coverage, and multi-tool AI workflow reflection.
  - Homework 3: documentation-only EU/EEA payment-account dispute-intake specification with scoped domain rationale, state/workflow modeling, low-level tasks, and agent guidance.
  - Homework 4: text-first multi-agent bug-fixing pipeline with agents, skills, adapters, seeded app, fixed app, security/test stages, and benchmark evidence.
  - Homework 5: MCP configuration and custom FastMCP server with GitHub, Filesystem, Notion, and custom reader evidence.
  - Homework 6: canonical Python transaction-processing simulation generated and selected through Hera/Athena/Hephaestus/Themis/Clio workflow, with deterministic runtime components, tests, coverage gate, sanitized outputs, and read-only MCP status access.
- Summarize demonstrated skills in a recruiter-readable but technically honest way.
- Include a compact source/evidence map linking to each homework README and the actual GitHub PRs:
  - PR #1 Homework 1: `https://github.com/itxyzzz/gen-ai-se-hw/pull/1`
  - PR #5 Homework 2: `https://github.com/itxyzzz/gen-ai-se-hw/pull/5`
  - PR #6 Homework 3: `https://github.com/itxyzzz/gen-ai-se-hw/pull/6`
  - PR #8 Homework 4: `https://github.com/itxyzzz/gen-ai-se-hw/pull/8`
  - PR #9 Homework 5: `https://github.com/itxyzzz/gen-ai-se-hw/pull/9`
  - PR #10 Homework 6: `https://github.com/itxyzzz/gen-ai-se-hw/pull/10`
- Keep the tone concise and confident, with careful phrasing around AI assistance and course scaffolding.

After the new portfolio content, the preserved old assignment README should remain available under a heading such as `Original Course Assignment README`.

## Interfaces and data

No runtime APIs, data schemas, configuration files, or persistence layers are affected.

Documentation interfaces affected:

- `README.md`: becomes the root reviewer/router entry point while preserving original assignment-template content at the end.
- `CHANGELOG.md`: updated only for harness approval and implementation commit records.

## Risks

- Overclaiming risk: The README could imply production banking, compliance, ML, or autonomous-agent platform experience beyond the evidence. Mitigation: include explicit claim boundaries and use "coursework", "local prototype", "simulation", "deterministic rules", and "AI-assisted" phrasing where needed.
- Source drift risk: Summaries could conflict with homework READMEs or PR descriptions. Mitigation: use the six homework READMEs and actual PR bodies as primary sources and verify links.
- Duplication risk: A long README could duplicate homework docs instead of routing to them. Mitigation: keep the new root text dense and link-oriented.
- Scope risk: Harness changelog requirements may appear to conflict with the user's "only README plus planning package" scope. Mitigation: limit implementation content edits to `README.md`; update root `CHANGELOG.md` only at required commit gates.
- Existing-work risk: The worktree currently contains untracked Homework 6 sample transaction files unrelated to this task. Mitigation: do not stage, edit, or remove them.

## Acceptance criteria

- `README.md` opens with new portfolio/router content before the original template text.
- The original root README text is preserved at the end under a clear label identifying it as the original homework/assignment formulation.
- The new content narrates all six homeworks and the progression across the course.
- The new content highlights relevant skills without unnecessary duplication of homework-level docs.
- The new content links to the six homework READMEs and actual GitHub PRs #1, #5, #6, #8, #9, and #10.
- The new content includes an authorship/AI-assistance note and careful claim-safety caveats aligned with the review excerpt.
- No homework-specific files, source code, tests, scripts, screenshots, MCP config, or PR bodies are changed.
- Root `CHANGELOG.md` is updated only as required for the planning and implementation commits.
- Markdown validation checks pass with no trailing whitespace or obvious malformed links introduced by this work.

## Planned commits

| Stage | Planned subject | Changelog title or snippet | Notes |
|---|---|---|---|
| Planning approval | `root-readme-portfolio-router spec: plan portfolio router README rewrite` | `2026-06-24-root-readme-portfolio-router: plan portfolio router README rewrite` | Approval commit for this spec, plan, and variance log. |
| Implementation | `root-readme-portfolio-router docs: rewrite root README as portfolio router` | `2026-06-24-root-readme-portfolio-router: rewrite root README as portfolio router` | Implementation commit for root `README.md` plus required changelog update. |

## Documentation artifact matrix

| Artifact | Type | Required? | Stage | Output path | Notes |
|---|---|---:|---|---|---|
| Changelog | Living | Yes | Before each commit | `CHANGELOG.md` | Required by repository and harness gates; newest-first entry with title snippets synchronized to planned commits. |
| Test cases | Snapshot | No | Not applicable | `snapshots/test-cases.snapshot.md` | Documentation-only README rewrite; acceptance criteria and validation commands are sufficient. |
| Testing guide delta | Living delta | No | Not applicable | `deltas/testing-guide.delta.md` | No test workflow changes. |
| Operator manual delta | Living delta | No | Not applicable | `deltas/operator-manual.delta.md` | No runtime/operator workflow changes. |
| API reference delta | Living delta | No | Not applicable | `deltas/api-reference.delta.md` | No public API changes. |
| Architecture snapshot | Snapshot | No | Not applicable | `snapshots/architecture.snapshot.md` | No architecture or runtime design changes. |
| Architecture summary delta | Living delta | No | Not applicable | `deltas/architecture-summary.delta.md` | No architecture documentation changes outside root README routing. |
| Variance log | Living notes | Yes | During implementation | `implementation-notes/variance-log.md` | Records scope or source-material variance after freeze if needed. |

## Approval

- Status: Approved
- Superseded by: none

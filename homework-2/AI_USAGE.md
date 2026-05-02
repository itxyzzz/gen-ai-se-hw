# AI Usage and Context-Model-Prompt Log

## Audience

This document is for instructors and reviewers who want to understand how AI assistance was applied to Homework 2 and how the Context-Model-Prompt framework shaped the implementation.

## Context-Model-Prompt Workflow

| Phase | Context supplied | Model role | Prompt intent | Output |
| --- | --- | --- | --- | --- |
| API implementation | `TASKS.md`, Spring Boot project structure, existing test conventions | Coding assistant | Build CRUD, import, validation, and classification behavior with transparent local rules | Source code, tests, run scripts |
| Test hardening | Required test matrix, existing MockMvc helpers, JaCoCo reports | QA/test assistant | Find missing checklist coverage and add focused automated tests | Expanded API, import, integration, performance, and fixture tests |
| Documentation | Implemented endpoints, sample data, architecture decisions, verification outputs | Documentation assistant | Write audience-specific docs with concrete examples and diagrams | README, API reference, architecture guide, testing guide, this AI usage log |
| Verification | Maven output, Surefire reports, JaCoCo report, sample data counts | Review assistant | Confirm evidence before claiming completion | `mvn test`, `mvn verify`, coverage report, screenshot deliverable |

## AI Tools and Model Attribution

The actual work in this local session was performed with Codex in the desktop app, using the current Codex GPT-5 based coding assistant and Superpowers workflow skills for planning, TDD, and verification discipline.

The assignment asks for different AI models for different documentation types. This repository does not claim a model pass that was not actually run. The documentation was structured for different audiences as follows:

| Document | Audience | AI role used in this session |
| --- | --- | --- |
| `README.md` | Developers | Developer onboarding writer |
| `API_REFERENCE.md` | API consumers | API contract writer |
| `ARCHITECTURE.md` | Technical leads | Architecture reviewer |
| `TESTING_GUIDE.md` | QA engineers | QA documentation writer |
| `AI_USAGE.md` | Instructors/reviewers | Process and evidence auditor |

If a submission grader requires literal multi-model provenance, run an additional review pass with separate available models and record the exact model names and changes here. No fabricated multi-model attribution is included.

## Prompting Patterns Used

- Provide the task text first, then inspect the codebase before changing files.
- Ask the model to compare implementation evidence against each homework deliverable.
- Use test-first changes for behavioral gaps and verification-first reporting for completion claims.
- Keep classification deterministic and explainable so tests can assert category, priority, confidence, reasoning, and keywords without external credentials.

## Verification Evidence

- `mvn test` runs the automated suite and generates the JaCoCo report.
- `mvn verify` enforces the configured `0.85` line coverage gate.
- `target/site/jacoco/index.html` is the HTML coverage report.
- `docs/screenshots/test_coverage.png` is the required coverage screenshot deliverable.

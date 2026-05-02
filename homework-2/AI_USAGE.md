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

The actual work for this assignment was completed in a multi-stage flow across different platforms and models:

1. **Initial Planning (Codex Web on Android)**
   - Focused on analyzing processes, conventions, improvements, and instructions from Homework 1 to be elevated to the repository level for all future homeworks.
   - **Challenges**: This high-level planning required significant reasoning, but Codex Web lacked plugin support (e.g., Superpowers), model selection, and intelligence/effort control. It took multiple attempts to accomplish the goals. The git flow was also notably different and more restrictive compared to local environments.

2. **Refinement and Implementation (Codex Local, GPT-5.5 High Intelligence)**
   - Finalized the repository-level instructions, successfully integrating testing and documentation work into the main development flow.
   - Introduced fallback instructions for the web version to mitigate the absence of critical controls (e.g., model selection and reasoning effort).
   - Executed the main body of implementation for Tasks 1 and 2, along with tests and documentation (leveraging Superpowers' TDD approach).

3. **Finalization (Google Antigravity, Gemini 3.1 Pro High)**
   - After hitting usage limits with GPT-5.5, work transitioned to Google Antigravity.
   - Reviewed the current state, committed all pending changes, and finalized documentation.

The assignment asks for different AI models for different documentation types. This repository's multi-stage flow naturally incorporated multiple models (Codex Web, Codex Local GPT-5.5, Google Antigravity) across different phases. The documentation was structured for different audiences as follows:

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

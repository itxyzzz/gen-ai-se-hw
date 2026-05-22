# Pure Agentic Pipeline Scale-Down Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use
> superpowers:executing-plans to implement this plan task-by-task. Steps use
> checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace the scripted Homework 4 harness with a text-first agentic
pipeline launched by one chat phrase.

**Architecture:** The pipeline is a hierarchy of Markdown instructions:
`skills/pipeline-harness-wrapper.md` loads portable adapters, agents, skills, scenario
inputs, and the run artifact contract. The only executable JavaScript left is
the sample app and its tests.

**Tech Stack:** Markdown agent specs, Markdown adapter docs, Node.js app/tests.

---

## Task 1: Planning Refresh

- [x] Add a superseding pure-agentic design spec.
- [x] Mark the earlier JavaScript harness design as superseded.
- [x] Add this implementation plan so the change is durable for future threads.

## Task 2: Text Harness And Adapters

- [x] Create `skills/pipeline-harness-wrapper.md` with the launch intent,
  stage order, artifact contract, workspace rules, promotion rule, and stop
  conditions.
- [x] Replace executable adapter files with Markdown docs for Codex Chat, Claude
  Code, Open Code, Google Antigravity, and generic capable-agent fallback.
- [x] Rewrite `skills/codex-chat-pipeline.md` as the primary chat runner skill.

## Task 3: Agent And Artifact Reframe

- [x] Expand each `agents/*.agent.md` file so it can run without a JavaScript
  harness injecting context.
- [x] Update `runs/bug-001/codex-chat-gpt-5.4-run-001/run-metadata.json` and benchmark files to
  identify the canonical run as a Codex chat text-pipeline run.
- [x] Keep `app/baseline`, `app/current`, run reports, benchmark text, and
  screenshots as evidence.

## Task 4: Script Pruning

- [x] Delete `pipeline/`, JavaScript adapter files, `package.json`,
  `pipeline.config.yaml`, and executable demo runners.
- [x] Keep or rewrite text-only demo notes if useful.
- [x] Remove stale blocked SDK and prompt-preparation run folders so the
  submission has one clear canonical run.

## Task 5: Documentation Rewrite

- [x] Rewrite README, HOWTORUN, ARCHITECTURE, API_REFERENCE, and TESTING_GUIDE
  around one-phrase chat execution and text artifacts.
- [x] Update CHANGELOG with the scale-down and verification results.
- [x] Search for stale script-era references and fix any remaining claims.

## Task 6: Verification

- [x] Run current app tests directly with Node.
- [x] Run baseline tests and record that they fail for the seeded defects.
- [x] Confirm required agents, skills, adapters, and artifacts remain present.
- [x] Review git status for unrelated changes before final response.

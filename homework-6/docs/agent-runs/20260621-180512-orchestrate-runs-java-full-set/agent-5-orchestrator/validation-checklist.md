# Hera Validation Checklist

## Setup

- [x] Hera skill entrypoint read.
- [x] Hera workflow, quality bar, and run registry read.
- [x] Homework 6 `agents.md`, package-set registry, final selection, Java stack profile, and child control packages read.
- [x] Current canonical set identified as `python-canonical-20260621`.
- [x] First-level child-agent dispatch availability checked through `multi_agent_v1.spawn_agent`.
- [x] `.codex/config.toml` checked for `max_threads = 8` and `max_depth = 2`.
- [x] Selection not authorized; canonical output policy recorded as preserve-only.

## Pending Child Evidence

- [x] Athena (Spec Writer) observed dispatch mechanism, run ID, output path, validation status, and handoff recorded.
- [x] Hephaestus (Code Generator) observed dispatch mechanism, run ID, inventory, validation status, Context7 status, and handoff recorded.
- [x] Themis (Test Generator) observed dispatch mechanism, run ID, inventory, validation status, and handoff recorded for usable retry `20260621-201051-generate-tests-java-hera-java-full-set-retry`. First attempt `20260621-191017-generate-tests-java-hera-java-full-set` is recorded as blocked and not selectable.
- [x] Clio (Documentation Generator) observed dispatch mechanism, run ID, inventory, validation status, screenshot status, and handoff recorded.

## Required Checks Before Handoff Completion

- [x] `docs/agent-runs/selection-sets.json` remains valid JSON.
- [x] `.codex/config.toml` remains parseable and keeps `agents.max_threads = 8` and `agents.max_depth = 2`.
- [x] Child ledger rejects silent "latest" targeting.
- [x] No Athena, Hephaestus, Themis, or Clio deliverables were generated in the parent Hera thread.
- [x] Hera appears only in Operator Layer / Homework Automation Layer run evidence, not runtime product files.
- [x] No raw sample account IDs, raw descriptions, credentials, or hidden prompts were added to Hera run reports.
- [x] Canonical generated product files, selected tests, selected reviewer docs, screenshots, and `mcp/server.py` remain unchanged; only preserved run folders were added.

## Commands

- `git branch --show-current`: `homework-6-extension`
- `git status --short`: emitted a `.pytest_cache/` permission warning; no visible path changes before Hera setup
- `python -m json.tool docs/agent-runs/selection-sets.json`: passed.
- `python -c "import tomllib, pathlib; ... .codex/config.toml ..."`: returned `8 2`.
- Parent Hera privacy scan for `ACC-` and known raw description markers: passed with no matches.
- `git diff --name-only`: no tracked canonical diffs.
- `git ls-files --others --exclude-standard docs/agent-runs/20260621-180512-orchestrate-runs-java-full-set docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set docs/agent-runs/20260621-183025-generate-code-java-hera-java-full-set docs/agent-runs/20260621-191017-generate-tests-java-hera-java-full-set docs/agent-runs/20260621-201051-generate-tests-java-hera-java-full-set-retry docs/agent-runs/20260621-203431-generate-docs-java-hera-java-full-set`: listed only preserved Java candidate run evidence.

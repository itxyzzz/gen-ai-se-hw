# Bug 001 Normalized Benchmark Design

## Goal

Create a snapshot-safe normalized benchmark view for Homework 4 `bug-001` runs without modifying preserved run evidence or historical planning artifacts.

## Boundaries

- Immutable evidence snapshots: `homework-4/runs/bug-001/**`.
- Immutable historical planning snapshots: existing files under `homework-4/docs/superpowers/specs/` and `homework-4/docs/superpowers/plans/`.
- Mutable normalized benchmark layer: `homework-4/benchmark/bug-001/**`.
- Mutable current guidance: active Homework 4 docs, adapters, skills, and `homework-4/AGENTS.md`.

## Normalized Run Mapping

Commit order from `git log --reverse -- homework-4/runs/bug-001`, reconciled with `homework-4/CHANGELOG.md`, defines this canonical sequence:

| Original snapshot folder | Normalized benchmark id |
| --- | --- |
| `codex-chat-gpt-5.4-run-001` | `run-001-codex-chat-gpt-5.4` |
| `codex-chat-gpt-5.2-run-002` | `run-002-codex-chat-gpt-5.2` |
| `open-code-claude-sonnet-4.5-run-003` | `run-003-open-code-claude-sonnet-4.5` |
| `open-code-run-004` | `run-004-open-code-big-pickle` |
| `open-code-nemotron-3-super-free-1779485697` | `run-005-open-code-nemotron-3-super-free-1779485697` |
| `open-code-gemini-3.5-flash-run-005` | `run-006-open-code-gemini-3.5-flash` |

## Benchmark Evidence Shape

Each normalized run lives under `homework-4/benchmark/bug-001/runs/<normalized-run-id>/` and contains benchmark-owned files:

- `source-map.json`: source snapshot path, normalized id, sequence basis, and immutability policy.
- `run-metadata.normalized.json`: comparable metadata and benchmark metrics.
- `artifact-index.md`: copied and derived artifact inventory.
- Copied source reports needed for repeatable review.
- `patch.diff`: copied when present; synthesized only for the Nemotron run because the source snapshot has split patch files.

## Future Run Naming Rule

New pipeline runs must write directly to `homework-4/runs/<scenario>/run-<NNN>-<tool>-<pattern>`.

- `NNN` is a zero-padded three-digit number.
- The next number is max existing normalized run number for the scenario plus one.
- `<tool>` is the adapter/tool label, such as `codex-chat`, `open-code`, `claude-code`, or `google-antigravity`.
- `<pattern>` is a lower-case filesystem-safe model or run pattern, such as `gpt-5.5`, `claude-sonnet-4.5`, or `gemini-3.5-flash`.
- `run-metadata.json` must set both `runId` and `runFolderName` to the normalized folder name.

## Model And Sub-agent Strategy

GPT-5.5 owns planning, normalization design, integration edits, and final consistency checks. Three read-only explorer sub-agents analyze the preserved source runs in parallel: Codex runs 001-002, Open Code runs 003-004, and Open Code runs 005-006. Sub-agents do not mutate files and report inspected files, metrics, scoring, notes, and uncertainty.

## Validation

- `git diff -- homework-4/runs/bug-001` must show no source snapshot edits.
- `git diff -- homework-4/docs/superpowers/specs homework-4/docs/superpowers/plans` must show only this new spec and its paired plan.
- `homework-4/benchmark/bug-001-results.json` must contain exactly six normalized runs.
- Active instructions must contain the strict `run-<NNN>-<tool>-<pattern>` rule and no contradictory future-run naming rule.

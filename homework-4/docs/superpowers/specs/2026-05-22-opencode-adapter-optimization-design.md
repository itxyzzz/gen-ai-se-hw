# Open Code Adapter Optimization Design Spec

## 1. Goal

Optimize the Homework 4 Open Code adapter so it is:

1. Truly compatible with mixed OpenCode model availability (Codex/OpenAI, Claude, and free/open models).
2. Deterministic and benchmark-friendly through explicit fallback rules and metadata recording.
3. Aligned with current text-harness architecture (`Run HW4 pipeline`) instead of shell-command assumptions.
4. Clear about skill semantics: platform Superpowers skills vs local homework skill documents.

---

## 2. Context and Problem

`homework-4/TASKS.md` was originally framed around one shell command (for example `npm run pipeline`).
The current Homework 4 architecture in this repository is a text-first harness where a launch phrase triggers pipeline orchestration.

Current mismatch areas:

- `adapters/open-code.md` is currently biased toward open-source model families only and does not cover Codex/Claude usage patterns.
- Prompt phrase in Open Code adapter is long and hard to remember.
- Adapter does not clearly define model fallback behavior when preferred models are unavailable.
- Skill terminology is ambiguous:
  - Superpowers platform skills are invoked by the `skill` tool.
  - Homework-local stage "skills" are markdown files under `homework-4/skills/` that must be read and followed.
- OpenCode-native tooling behavior is not explicitly documented in the adapter.

---

## 3. Design Decisions

### 3.1 Keep one short canonical launch phrase

Use:

```text
Run HW4 pipeline
```

Rationale:

- Already canonical in `skills/pipeline-harness-wrapper.md`.
- Already mapped by `homework-4/AGENTS.md` to auto-select the correct adapter by active tool.
- Minimizes user friction and typo risk.

### 3.2 Clarify command semantics in OpenCode

Interpret "single-command execution" as a single launch command phrase in chat for this architecture, not a required shell script.

Rationale:

- Codex/OpenCode workflows here are prompt-orchestrated.
- This keeps assignment intent (single launch, no manual per-agent prompts) while matching the actual tool.

### 3.3 Replace single-family model mapping with multi-family ordered candidates

For each `model_policy`, define ordered candidates spanning:

- Codex/OpenAI models
- Claude models
- Free/open model families

Selection must be deterministic:

1. Pick first available candidate.
2. If unavailable, fall through to the next candidate.
3. If all listed candidates are unavailable, pick strongest available model matching required reasoning tier.
4. Record fallback details in `run-metadata.json`.

### 3.4 Explicitly split skill handling

- Superpowers skills (process skills): invoked via `skill` tool.
- Homework local stage skills (content specs): read as files and applied as report/test quality constraints.

### 3.5 Add OpenCode-native tooling guidance

Document preferred tool usage for reproducibility:

- `glob` for file discovery
- `grep` for text search
- `read` for file reads
- `apply_patch` for focused edits
- `bash` for tests/commands
- optional `task` subagents where available

### 3.6 Keep harness contract untouched

No changes to universal stage order, required artifacts, baseline immutability, stop conditions, or promotion model.

---

## 4. Files and Planned Changes

### 4.1 `homework-4/adapters/open-code.md` (primary)

Planned updates:

- Replace long launch phrase with canonical short phrase.
- Replace current model table with multi-family ordered candidate matrix.
- Add deterministic fallback and metadata recording rules.
- Add explicit skill-semantics split.
- Add OpenCode-native tooling guidance.
- Keep validation checklist but expand it with fallback metadata checks.

### 4.2 `homework-4/HOWTORUN.md`

Planned updates:

- Keep canonical phrase as-is.
- Normalize Open Code portable phrase to short canonical trigger and explain adapter auto-selection by active tool context.

### 4.3 `homework-4/API_REFERENCE.md`

Planned updates:

- Adapter prompt table: normalize Open Code row to short canonical phrase.
- Keep adapter differentiation documented as context-selected behavior, not phrase length.

### 4.4 `homework-4/CHANGELOG.md`

Planned updates:

- Add newest-first step entry describing:
  - Open Code adapter optimization
  - multi-family model candidate mapping
  - fallback/metadata policy
  - launch phrase simplification and prompt-contract normalization

---

## 5. Proposed Open Code Adapter Shape

The updated adapter should include these sections:

1. Title and scope statement for OpenCode mixed-model environments.
2. Short launch phrase (`Run HW4 pipeline`).
3. Model selection matrix with ordered candidates per `model_policy`.
4. Fallback rules and metadata recording schema.
5. Mapping to harness + agents + local skill docs.
6. OpenCode tooling guidance.
7. Execution rules (ordered stages, workspace boundaries, reflection loop limits).
8. Validation checklist (including fallback metadata completeness).

---

## 6. Non-Goals

- Reintroducing JavaScript harness scripts (`npm run pipeline`) as required proof path.
- Changing portable agent specs in `agents/*.agent.md`.
- Changing scenario contents, run evidence contract, or benchmark scoring rubric.
- Renaming canonical run folders or baseline/current app layout.

---

## 7. Acceptance Criteria

1. `adapters/open-code.md` supports Codex, Claude, and free/open model options without ambiguity.
2. Launch phrase in Open Code adapter is short and memorable: `Run HW4 pipeline`.
3. Fallback behavior is deterministic and explicitly documented.
4. Metadata requirements clearly capture actual per-stage model decisions and fallback reasons.
5. Skill semantics are unambiguous between platform `skill` invocations and local markdown skill files.
6. `HOWTORUN.md` and `API_REFERENCE.md` remain consistent with the same launch contract.
7. `CHANGELOG.md` includes a newest-first entry for this update.

---

## 8. Risks and Mitigations

- **Risk:** Model names differ by provider aliases.
  **Mitigation:** Adapter explicitly allows closest equivalent aliases and requires logging actual model IDs.

- **Risk:** Users expect shell-command pipeline execution.
  **Mitigation:** Adapter and docs explicitly define command-phrase execution semantics for OpenCode/Codex context.

- **Risk:** Ambiguous skill usage causes non-compliant artifacts.
  **Mitigation:** Explicit two-class skill handling (platform skills vs local stage skill docs).

---

## 9. Reviewer Checklist

- [ ] Open Code adapter launch phrase is short and canonical.
- [ ] Model matrix spans Codex, Claude, and free/open families.
- [ ] Fallback and metadata rules are explicit and testable.
- [ ] Skill handling split is explicit.
- [ ] Prompt contract in HOWTORUN/API docs is consistent.
- [ ] Changelog entry is newest-first and complete.

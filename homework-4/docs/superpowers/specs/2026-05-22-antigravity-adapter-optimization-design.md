# Universal Pipeline Adapter Alignment & Optimization Design Spec

## 1. Goal

The objective is to review, align, and optimize **all Homework 4 adapters** (including dedicated ones for Codex Chat, Google Antigravity, Claude Code, Open Code, and the Generic Agent fallback) to ensure total consistency with the universal harness updates and strict platform-agnostic separation:
1. **Decommission Agent-Specific Skills under `/skills`**: Move and integrate `skills/codex-chat-pipeline.md` directly into the Codex Chat adapter (`adapters/codex-chat.md`). `/skills` is preserved strictly for platform-agnostic, reusable rules.
2. **Enhance the Generic Harness (`skills/pipeline-harness-wrapper.md`)**: Add a universal, platform-agnostic section called `## Reusable Agentic Execution Extensions` that defines guidelines for subagent delegation, reflection/self-correction loops, and static analysis that any capable agentic tool can leverage.
3. **Harmonize Model Mappings Across All Adapters**: Update all dedicated adapters to include a concrete **Model Selection** table mapping the abstract policies (`*-high`, `*-medium`) to their respective vendor's specific models.
4. **Remove Cross-Adapter Dependencies**: Clean up references in other adapters (like `claude-code.md`) that refer to the "Codex Chat adapter" for workspaces or contracts, pointing them instead to the universal harness.

---

## 2. Component Architecture & Alignment Map

```mermaid
flowchart TD
    H["Generic Harness (skills/pipeline-harness-wrapper.md)"]
    H -->|Reference| A1["Codex Adapter (adapters/codex-chat.md)"]
    H -->|Reference| A2["Antigravity Adapter (adapters/google-antigravity.md)"]
    H -->|Reference| A3["Claude Code Adapter (adapters/claude-code.md)"]
    H -->|Reference| A4["Open Code Adapter (adapters/open-code.md)"]
    H -->|Reference| A5["Generic Adapter (adapters/generic-agent.md)"]
    
    subgraph /skills (Strictly Generic Rules)
        H
        S1["skills/research-quality-measurement.md"]
        S2["skills/unit-tests-FIRST.md"]
    end

    subgraph /adapters (Model mappings + execution runner procedures)
        A1
        A2
        A3
        A4
        A5
    end
```

---

## 3. Comprehensive Adapter Review & Planned Changes

### 3.1 Codex Chat Adapter (`adapters/codex-chat.md`)
* **Planned Changes:** Merge the entire runner trigger, procedure, required artifact contract, and quality gates from `skills/codex-chat-pipeline.md` directly into this file. 
* **Model Mapping:** Maintains existing `gpt-5.4` (for high reasoning) and `gpt-5.3-codex` (for mechanical coding) policies.
* **Consistency:** Eliminates the `skills/codex-chat-pipeline.md` file completely.

### 3.2 Google Antigravity Adapter (`adapters/google-antigravity.md`)
* **Planned Changes:** 
  - Add concrete model mapping table using currently available Gemini models (`Gemini 3.1 Pro` and `Gemini 3.5 Flash`), with future Pro/Flash models allowed as alternatives.
  - Add an advanced, tool-based **Orchestration Procedure** to run the pipeline sequentially using `define_subagent`, `invoke_subagent`, `run_command` (local test runner), and automated **Reflection Loops** with up to 3 repair iterations.
  - Streamline the validation checklist to remove duplicate references.

### 3.3 Claude Code Adapter (`adapters/claude-code.md`)
* **Critique & Inconsistency:** 
  - It references Codex Chat under Mapping: *"Use the same run workspace and artifact contract as Codex Chat."* This is a cross-adapter dependency violation.
  - It has no model mapping section, leaving the agent to guess what models are used.
* **Planned Changes:**
  - Remove all cross-adapter references. Under Mapping, point to the universal harness: *"Use the run workspace and artifact contract defined in skills/pipeline-harness-wrapper.md."*
  - Add a **Model Selection** table mapping Anthropic Claude models:
    - `research-high`, `verification-high`, `planning-high`, `security-high` $\rightarrow$ `claude-3-5-sonnet` (or equivalent high reasoning).
    - `implementation-medium`, `test-medium` $\rightarrow$ `claude-3-5-haiku` (or high-throughput coding models).
  - Explicitly direct the Claude Code runner to utilize its native background subagents for verification stages if supported, aligned with the generic harness extensions.

### 3.4 Open Code Adapter (`adapters/open-code.md`)
* **Critique & Inconsistency:** It lacks concrete model selections for open-source ecosystems.
* **Planned Changes:**
  - Add a **Model Selection** table mapping abstract policies to leading open-source models:
    - `research-high`, `verification-high`, `planning-high`, `security-high` $\rightarrow$ `llama-3.3-70b-instruct` / `Qwen-2.5-Coder-32B-Instruct`
    - `implementation-medium`, `test-medium` $\rightarrow$ `llama-3.1-8b-instruct` / `Qwen-2.5-Coder-7B-Instruct`
  - Align its execution rules with the harness's optional agentic extensions.

### 3.5 Generic Agentic Adapter (`adapters/generic-agent.md`)
* **Critique & Inconsistency:** Does not direct capable agents to utilize the new optional harness extensions.
* **Planned Changes:**
  - Add a section under Mapping directing capable tools to inspect and implement the `## Reusable Agentic Execution Extensions` defined in the generic harness wrapper (subagents, reflection loops, and local static tools) if their environment supports them.
  - Add general model guidance: Use high reasoning effort for `*-high` policies, and fast coding models for `*-medium` policies.

---

## 4. Modified Directory Map
```text
homework-4/
├── adapters/
│   ├── README.md
│   ├── claude-code.md         # [MODIFY] Added Claude mappings, fixed cross-dependencies
│   ├── codex-chat.md          # [MODIFY] Merged runner skill directly here
│   ├── generic-agent.md       # [MODIFY] Linked generic agentic extensions & model advice
│   ├── google-antigravity.md  # [MODIFY] Added Gemini mapping & subagent tool-orchestrator
│   └── open-code.md           # [MODIFY] Added open-source model mappings
└── skills/
    ├── pipeline-harness-wrapper.md # [MODIFY] Added universal agentic extensions
    ├── research-quality-measurement.md
    ├── unit-tests-FIRST.md
    └── codex-chat-pipeline.md      # [DELETE] Safely removed
```

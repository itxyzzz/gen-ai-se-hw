# Hera Run Metadata

- Run ID: `20260621-180512-orchestrate-runs-java-full-set`
- Mode: `generate-set`
- Requested stack: `java`
- Start time: `2026-06-21 18:05:12 Europe/Budapest`
- Orchestration tool: Codex Desktop, Hera (Orchestrator) via `.agents/skills/orchestrate-runs/SKILL.md`
- Operator instruction: "Invoke Hera ... to orchestrate creation of the full new set of spec/code/tests/docs in Java stack."
- Selection authorized: no. This run may preserve a Java candidate set and propose selection later, but it must not replace canonical Python output.
- Current canonical package-set ID: `python-canonical-20260621`
- Source package-set ID protected from replacement: `python-canonical-20260621`
- Intended package-set ID if all child runs succeed: `java-candidate-20260621-180512`
- Child-agent plan: dispatch Athena (Spec Writer), Hephaestus (Code Generator), Themis (Test Generator), and Clio (Documentation Generator) as first-level child agents in sequence.
- Expected first-level child dispatch mechanism: `multi_agent_v1.spawn_agent` worker agents.
- Nested sub-agent support expected by config: `.codex/config.toml` declares `agents.max_threads = 8` and `agents.max_depth = 2`.
- Observed first-level child dispatch availability: available through `multi_agent_v1.spawn_agent`.
- Child-local nested support: not yet observed; each child must record any degraded nested-agent behavior in its own run metadata or handoff.
- Agent config values: `max_threads = 8`; `max_depth = 2`.
- Canonical-output policy: preserve-only. Do not edit canonical `specification.md`, root Java/Python product files, selected tests, selected reviewer docs, screenshots, `mcp/server.py`, `selection-sets.json`, or `final-selection.md` unless a later explicit `select-set` instruction names the Java package set.
- Pre-existing dirty git state: `git status --short` emitted a permission warning for `.pytest_cache/`; no listed path changes were visible in that command output before Hera setup.
- Privacy notes: run evidence should store run IDs, paths, counts, safe reason codes, command statuses, fingerprints, and blockers only. Do not store raw account IDs, raw transaction descriptions, credentials, hidden prompts, or full sample payloads.

## Java Stack Expectations

- Build: Maven with `pom.xml`.
- Source layout: `src/main/java/...`.
- Test layout: `src/test/java/...`.
- Money: Java `BigDecimal`; no `double` or `float` for amounts, thresholds, summaries, or tests.
- JSON: Jackson or equivalent strict JSON handling.
- Tests: JUnit 5/JUnit Jupiter through Maven Surefire or Failsafe.
- Coverage: JaCoCo `report` and `check` goals with a build-blocking covered-ratio threshold such as `0.80`.
- Pipeline results: stack-neutral `shared/results/summary.json` plus `shared/results/TXN*.json` safe fields consumable by the existing Python `pipeline-status` MCP reader.


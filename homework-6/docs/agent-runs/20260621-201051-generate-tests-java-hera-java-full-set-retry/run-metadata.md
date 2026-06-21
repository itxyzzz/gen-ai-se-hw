# Themis Run Metadata

Run ID: `20260621-201051-generate-tests-java-hera-java-full-set-retry`

Mode: `generate`

Stack: `java`

Parent Hera (Orchestrator) run ID: `20260621-180512-orchestrate-runs-java-full-set`

Package-set ID under construction: `java-candidate-20260621-180512`

Selection authorized: no. This run preserves outputs only and does not copy to canonical root files.

Start time: 2026-06-21 20:10:51 Europe/Budapest

Orchestration tool: Codex Desktop, dispatched as replacement Themis (Test Generator) child agent.

Prior blocked Themis run excluded: `20260621-191017-generate-tests-java-hera-java-full-set`.

## Targeted Code Package

- Source Athena (Spec Writer) run ID: `20260621-180826-write-spec-java-hera-java-full-set`
- Source spec path: `docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/agent-1-spec/outputs/specification.md`
- Source spec SHA-256: `2FC5B597915CFD59D5786271DB0CDE96B06417DEEC1433D056B76D7023234CFC`
- Hephaestus (Code Generator) run ID: `20260621-183025-generate-code-java-hera-java-full-set`
- Hephaestus inventory path: `docs/agent-runs/20260621-183025-generate-code-java-hera-java-full-set/agent-2-code/outputs/inventory.md`
- Hephaestus inventory SHA-256: `3C5B7DB95A30250DEE60A26D17BAD577FA8BFF98A293FA917A1DEA8ECCBCDEB1`
- Hephaestus package fingerprint: `248ba67b199104cbe06b4b0869cf3143de85ff1dd88da7bf4a13bc85a58ed883`
- Protected canonical package-set: `python-canonical-20260621`

## Canonical Spec Mismatch

Current canonical `specification.md` SHA-256: `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B`

Mismatch status: expected mismatch. This Themis run targets the preserved Java candidate spec above, while the canonical root spec remains the protected Python package-set spec.

## Candidate Outputs

Selectable outputs root: `agent-3-tests/outputs/`

Intended canonical targets if later selected: `pom.xml` and `src/test/java/...` only, as declared in `agent-3-tests/outputs/inventory.md`.

Selected test package fingerprint: `d75aee1b9e76733d78860348fe26608fef17667a88c25e3c7b416e9150b54922`

## Pre-Existing Dirty State

Initial `git status --short` showed preserved Java/Hera run folders as untracked:

- `docs/agent-runs/20260621-180512-orchestrate-runs-java-full-set/`
- `docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/`
- `docs/agent-runs/20260621-183025-generate-code-java-hera-java-full-set/`
- `docs/agent-runs/20260621-191017-generate-tests-java-hera-java-full-set/`

The status command also warned that `.pytest_cache/` could not be opened because of local permissions. This retry added only `docs/agent-runs/20260621-201051-generate-tests-java-hera-java-full-set-retry/`.

## Sub-Agent Use

No executor sub-agents were used. The retry scope was tightly coupled and small: preserve the selected Java package, add focused test/config overlays, run validation, and write handoff evidence.

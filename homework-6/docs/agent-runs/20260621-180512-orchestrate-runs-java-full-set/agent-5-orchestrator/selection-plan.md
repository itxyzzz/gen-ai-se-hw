# Selection Plan

Status: proposal placeholder only. This file is not authorization to copy canonical files.

This Hera run was requested as `generate-set` for a new Java stack package. No `select-set` authorization was provided.

If all Java child runs succeed, a future operator may decide whether to:

- Keep the Java package as a preserved alternate/candidate only.
- Register it in `docs/agent-runs/selection-sets.json` as an alternate without changing `canonical_set_id`.
- Explicitly select Java as canonical in a separate `select-set` instruction that names the package set and inventory-declared copy targets.

Until that separate instruction, Python package set `python-canonical-20260621` remains canonical and root generated product, test, documentation, screenshot, MCP, and selection files stay unchanged.

## Preserved Candidate Evidence

The Java candidate package set now has preserved child evidence:

- Athena (Spec Writer): `20260621-180826-write-spec-java-hera-java-full-set`
- Hephaestus (Code Generator): `20260621-183025-generate-code-java-hera-java-full-set`
- Themis (Test Generator): `20260621-201051-generate-tests-java-hera-java-full-set-retry`
- Blocked Themis attempt, not selectable: `20260621-191017-generate-tests-java-hera-java-full-set`
- Clio (Documentation Generator): `20260621-203431-generate-docs-java-hera-java-full-set`

If the operator later asks to register or select this candidate, the next `select-set` prompt should name `java-candidate-20260621-180512` and these inventories:

- Code inventory: `docs/agent-runs/20260621-183025-generate-code-java-hera-java-full-set/agent-2-code/outputs/inventory.md`
- Test inventory: `docs/agent-runs/20260621-201051-generate-tests-java-hera-java-full-set-retry/agent-3-tests/outputs/inventory.md`
- Documentation inventory: `docs/agent-runs/20260621-203431-generate-docs-java-hera-java-full-set/agent-4-docs/outputs/inventory.md`

Registration as an alternate may update `selection-sets.json` without changing `canonical_set_id`. Replacing the canonical Python package requires an explicit operator instruction that says Java should become canonical.

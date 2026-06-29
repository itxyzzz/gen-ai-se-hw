# Selection Plan

This is a proposal only. It does not authorize copying canonical files or updating selection records.

## Recommended Default

Keep `python-canonical-20260621` as the canonical package set.

Rationale:

- It is already selected in `docs/agent-runs/selection-sets.json`.
- It is already documented in `docs/agent-runs/final-selection.md`.
- It has complete Clio validation evidence and stable root reviewer files.
- It has the lowest selection and submission risk.

## Optional Upgrade Path

If the operator wants a fresher Python canonical package, run a separate explicit Hera `select-set` operation for the latest Python candidate:

```text
Select the preserved Python candidate from Hera run 20260621-215717-orchestrate-runs-python-full-set as the new canonical package set. Use Athena 20260621-220037-write-spec-python-hera-python-full-set, Hephaestus 20260621-222543-generate-code-python-hera-python-full-set, Themis 20260621-224632-generate-tests-python-hera-python-full-set, and Clio 20260621-225923-generate-docs-python-hera-python-full-set. Replace the current Python canonical set through inventory-declared copy targets and update final-selection.md plus selection-sets.json.
```

Selection would need to verify:

- All candidate inventories exist and declare intended canonical targets.
- Prior selected canonical targets are replaced or removed according to inventory rules.
- `docs/agent-runs/final-selection.md` records the new selected run IDs, fingerprints, copied paths, rationale, and exclusions.
- `docs/agent-runs/selection-sets.json` keeps valid JSON and updates `canonical_set_id` only because the operator explicitly authorized replacement.
- Root docs, screenshots, product files, tests, and MCP behavior match the selected candidate evidence after copy.

## Optional Java Alternate Registration

If the operator wants to preserve Java as an alternate without replacing Python, run a separate explicit Hera `select-set` or alternate-registration operation:

```text
Register java-candidate-20260621-180512 as a preserved alternate package set. Keep canonical_set_id as python-canonical-20260621. Use Athena 20260621-180826-write-spec-java-hera-java-full-set, Hephaestus 20260621-183025-generate-code-java-hera-java-full-set, Themis retry 20260621-201051-generate-tests-java-hera-java-full-set-retry, and Clio 20260621-203431-generate-docs-java-hera-java-full-set.
```

Registration would need to verify:

- The blocked Themis run `20260621-191017-generate-tests-java-hera-java-full-set` remains non-selectable.
- The usable Themis retry inventory and Clio inventory are the only Java test/doc sources for the alternate.
- Java remains candidate or alternate status unless the operator separately authorizes canonical replacement.
- Maven settings limitations and MCP staging/helper limitations remain documented.

## Explicit Non-Authorization

This comparison does not authorize:

- Replacing Python canonical output.
- Making Java canonical.
- Copying candidate docs, screenshots, source, tests, or MCP files into root canonical paths.
- Updating `docs/agent-runs/final-selection.md`.
- Updating `docs/agent-runs/selection-sets.json`.


# Clio Candidate Output Inventory

- Run ID: `20260621-011348-generate-docs-python-review-repair`
- Source Athena run ID: `20260619-170102-write-spec-python-fresh`
- Source Hephaestus run ID: `20260619-175211-generate-code-python-fresh-spec`
- Source Themis run ID: `20260620-144025-generate-tests-python-fresh-spec`
- Final-selection record: `docs/agent-runs/final-selection.md`
- Current canonical `specification.md` SHA-256: `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B`

## Selectable Files

| Candidate path under `outputs/` | Canonical target | Kind | SHA-256 | Action | Screenshot source |
|---|---|---|---|---|---|
| `README.md` | `README.md` | README | `DCA463774D92338150564D02B0240D1E392830A26AE2B3D37E975855CCFB3488` | replaces | n/a |
| `HOWTORUN.md` | `HOWTORUN.md` | runbook | `CABD2D9370985D8824276B0EE0E0D8733E56B88E2F427DC16DC4B76E6B1241C0` | replaces | n/a |
| `ARCHITECTURE.md` | `ARCHITECTURE.md` | architecture doc | `F1ED948D763E4C3D3E61959E1D3389D7E41A952E38553E4A7AFF4FEE1E0C9C9D` | replaces | n/a |
| `TESTING_GUIDE.md` | `TESTING_GUIDE.md` | testing doc | `A2B802CF823B74CAFD120FF0EB1F4D442B3854A6D272A19AEEFD874BD22DBC85` | replaces | n/a |
| `API_REFERENCE.md` | `API_REFERENCE.md` | API reference | `CF2295429188E7FEFD6424FFD5E98D6D9ECBEF69AF448D9C0533397D266DFAAE` | replaces | n/a |
| `docs/pr-description-draft.md` | `docs/pr-description-draft.md` | PR draft | `B6E180248AB7374F7CDBC3AA942A22A30682A2FF31C701787D4592DA3404F689` | replaces | n/a |
| `docs/screenshots/pipeline-run.png` | `docs/screenshots/pipeline-run.png` | screenshot | `61107B945C21897F05B6447068DE5637EF9CE22A9CD739ABA42CB251F77E8377` | replaces | fresh terminal-style evidence |
| `docs/screenshots/test-coverage.png` | `docs/screenshots/test-coverage.png` | screenshot | `2B5FAAF9A4B2FD867D2287FBE3A1D32D26D58F249202738BAE8EF31748CA2C9F` | replaces | fresh terminal-style evidence |
| `docs/screenshots/skill-run-pipeline.png` | `docs/screenshots/skill-run-pipeline.png` | screenshot | `E984E004DB935C85CA16F3D437AA4DE01BCB9965F9DB829B717364167EFD5794` | replaces | `docs/screenshots/operator-sourced/080-run-pipeline.png` |
| `docs/screenshots/hook-trigger.png` | `docs/screenshots/hook-trigger.png` | screenshot | `C18A4359F7E4143FC2812C1BAC68BE9836343C795E421EB5A1E19C60E59E9D7A` | replaces | `docs/screenshots/operator-sourced/100-pre-push-git-hook-firing.png` |
| `docs/screenshots/mcp-interaction.png` | `docs/screenshots/mcp-interaction.png` | screenshot | `2AF2B4A99A74551F44293B565060C661F50054DCA540B596B91B8CC8BD83B1B6` | replaces | fresh terminal-style combined MCP evidence |

## Excluded Runtime And Tool Outputs

The following are not selectable documentation outputs and must not be copied to canonical targets through this inventory:

- `agent-4-docs/evidence/`
- `agent-4-docs/review/`
- `agent-4-docs/validation-checklist.md`
- `agent-4-docs/handoff.md`
- `inputs/`
- `shared/`
- `archive/`
- `.coverage*`
- `.pytest_cache/`
- `.test-tmp/`
- `tmp/`
- `__pycache__/`

## Scope Notes

- The inventory declares only reviewer-facing docs, PR draft, and stable screenshot targets.
- Fresh terminal-style PNGs are used for pipeline, passing coverage, and combined MCP evidence to satisfy the updated screenshot semantics without overwriting operator-sourced images.
- Clio did not modify selected runtime code or selected tests.

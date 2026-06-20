# Clio Candidate Output Inventory

- Run ID: `20260620-230201-generate-docs-python-primary`
- Source Athena run ID: `20260619-170102-write-spec-python-fresh`
- Source Hephaestus run ID: `20260619-175211-generate-code-python-fresh-spec`
- Source Themis run ID: `20260620-144025-generate-tests-python-fresh-spec`
- Final-selection record: `docs/agent-runs/final-selection.md`
- Current canonical specification SHA-256: `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B`

## Selectable Files

| Candidate path under `outputs/` | Canonical target | Kind | SHA-256 | Source screenshot | Action |
|---|---|---|---|---|---|
| `README.md` | `README.md` | README | `34578BE0309F18AF05D507D3AEEEE8384909164432A7B6F41F559411364EC244` | n/a | creates |
| `HOWTORUN.md` | `HOWTORUN.md` | runbook | `7F172769A23CD44B6B086933EF9EFFC517234933698FEF51B4B53868A4598B34` | n/a | creates |
| `ARCHITECTURE.md` | `ARCHITECTURE.md` | architecture doc | `F95F8D06211FE58B3DF4536496C5A5611105BF82D1EEDBCE2798B7F56A1D7525` | n/a | creates |
| `TESTING_GUIDE.md` | `TESTING_GUIDE.md` | testing doc | `A2B802CF823B74CAFD120FF0EB1F4D442B3854A6D272A19AEEFD874BD22DBC85` | n/a | creates |
| `API_REFERENCE.md` | `API_REFERENCE.md` | API reference | `CF2295429188E7FEFD6424FFD5E98D6D9ECBEF69AF448D9C0533397D266DFAAE` | n/a | creates |
| `docs/pr-description-draft.md` | `docs/pr-description-draft.md` | PR draft | `499D4E9D96975A243D265BDF9861EACB688ACA2A3DECE4D3FFD31AE55D8F1CB1` | n/a | creates |
| `docs/screenshots/pipeline-run.png` | `docs/screenshots/pipeline-run.png` | screenshot | `E984E004DB935C85CA16F3D437AA4DE01BCB9965F9DB829B717364167EFD5794` | `docs/screenshots/operator-sourced/080-run-pipeline.png` | creates |
| `docs/screenshots/test-coverage.png` | `docs/screenshots/test-coverage.png` | screenshot | `22F84D5CD0FAFA10693F8D27D17679DD7F1669CEAEA9DEB5F054BBB426D9DD86` | `docs/screenshots/operator-sourced/095-coverage-fail-under-99-fail.png` | creates |
| `docs/screenshots/skill-run-pipeline.png` | `docs/screenshots/skill-run-pipeline.png` | screenshot | `E984E004DB935C85CA16F3D437AA4DE01BCB9965F9DB829B717364167EFD5794` | `docs/screenshots/operator-sourced/080-run-pipeline.png` | creates |
| `docs/screenshots/hook-trigger.png` | `docs/screenshots/hook-trigger.png` | screenshot | `C18A4359F7E4143FC2812C1BAC68BE9836343C795E421EB5A1E19C60E59E9D7A` | `docs/screenshots/operator-sourced/100-pre-push-git-hook-firing.png` | creates |
| `docs/screenshots/mcp-interaction.png` | `docs/screenshots/mcp-interaction.png` | screenshot | `2EEF60CA2B53A5F43115691785BC7C28EA543E33B50830A30EA5C01BD13CDC37` | `docs/screenshots/operator-sourced/110-custom-mcp-server.png` | creates |

## Explicit Exclusions

The following are not selectable outputs from this inventory:

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

- This is the first Clio (Documentation Generator) candidate for the selected Homework 6 package.
- Stable screenshots were copied from preserved operator-sourced screenshots. Fresh text evidence records the current command results.
- The `pipeline-run.png` and `skill-run-pipeline.png` targets intentionally share the same source screenshot because the available source image shows the `/run-pipeline` skill producing the pipeline run summary.

# Clio Candidate Documentation Output Inventory

- Run ID: `20260621-225923-generate-docs-python-hera-python-full-set`
- Stack: `python`
- Mode: `generate`
- Package-set status: preserved fresh Python candidate, not selected
- Parent Hera run ID: `20260621-215717-orchestrate-runs-python-full-set`
- Source Athena (Spec Writer) run ID: `20260621-220037-write-spec-python-hera-python-full-set`
- Source Hephaestus (Code Generator) run ID: `20260621-222543-generate-code-python-hera-python-full-set`
- Source Themis (Test Generator) run ID: `20260621-224632-generate-tests-python-hera-python-full-set`

## Selectable Files

| Candidate file under `agent-4-docs/outputs/` | Intended canonical target | Kind | SHA-256 | Selection action |
|---|---|---|---|---|
| `README.md` | `README.md` | Reviewer README | `A5D48024D451907B7543000DDA5FEF24113BAA65A5E69CBE7DB76F3B77774929` | Replace if this candidate is selected |
| `HOWTORUN.md` | `HOWTORUN.md` | Reviewer runbook | `D6B518B4B0800E7A364DEB16FEEF355B78582E71906185BAAD089844F1AB1311` | Replace if this candidate is selected |
| `ARCHITECTURE.md` | `ARCHITECTURE.md` | Architecture guide | `0B0BA5F3E0316C3AB2FBA854C380C897773DF4C0D331A2B79F41C4CD9B0381A9` | Replace if this candidate is selected |
| `TESTING_GUIDE.md` | `TESTING_GUIDE.md` | Testing guide | `F674BF84393438BDC6AEB8A204B0B349BAD6D78C09A5F513D086BAC759C9B744` | Replace if this candidate is selected |
| `API_REFERENCE.md` | `API_REFERENCE.md` | API/reference guide | `BA293229FB81B8BEA7B34911E578C0FF4592E5D96918EE2F7C3ED12DEC66D243` | Replace if this candidate is selected |
| `docs/pr-description-draft.md` | `docs/pr-description-draft.md` | PR draft | `ED78FD3D62BE0D1CAD89902482AEDDF79A2553D8153BDEFC8C5A397AD4FB8799` | Replace if this candidate is selected |
| `docs/screenshots/pipeline-run.png` | `docs/screenshots/pipeline-run.png` | Screenshot: direct pipeline run | `1FD1CD6ED495152F841BA1B1805A98270BFFE24B6E53019435D8FCFCD0A9D0DF` | Replace if this candidate is selected |
| `docs/screenshots/test-coverage.png` | `docs/screenshots/test-coverage.png` | Screenshot: passing coverage gate | `28F04395D91C3BAA0BB5500E40FE6548454A1085E64046F8C1CC3F2DB4E4E957` | Replace if this candidate is selected |
| `docs/screenshots/skill-run-pipeline.png` | `docs/screenshots/skill-run-pipeline.png` | Screenshot: `/run-pipeline` behavior | `8F018EE19E932B7E8BC9FC29DA4E508FB346309E3CD9564E88CE2957A2D4370A` | Replace if this candidate is selected |
| `docs/screenshots/hook-trigger.png` | `docs/screenshots/hook-trigger.png` | Screenshot: hook/coverage blocking behavior | `1D2AD0450FFEEF8D453EEA5531052C2A1741B8ABDDE6FCF143BBEE7FA57A528C` | Replace if this candidate is selected |
| `docs/screenshots/mcp-interaction.png` | `docs/screenshots/mcp-interaction.png` | Screenshot: Context7 plus custom MCP evidence | `499C19BD4628D1405B8EC5BDE8CA43C3E6404A318249F67568CBF0CD4E76F18C` | Replace if this candidate is selected |

## Screenshot Sources

| Candidate screenshot | Source evidence |
|---|---|
| `docs/screenshots/pipeline-run.png` | Fresh Clio `python integrator.py` evidence |
| `docs/screenshots/test-coverage.png` | Fresh Clio coverage gate at 80% evidence |
| `docs/screenshots/skill-run-pipeline.png` | Fresh Clio documented `/run-pipeline` fast-path evidence |
| `docs/screenshots/hook-trigger.png` | Fresh Clio 99% coverage-threshold blocking evidence and hook limitation |
| `docs/screenshots/mcp-interaction.png` | Hephaestus Context7 research notes plus custom `pipeline-status` helper evidence |

## Source Run Fingerprints

- Athena spec SHA-256: `6F2E8CD844884DF06172EEB1CF92A2956BC45425FE514B7A820ABAEA249D2222`
- Hephaestus inventory SHA-256: `CB19F97C1ED372905A764F5735EC6E3D0BDA6F1BEF132B723E258770B99C30DF`
- Themis inventory SHA-256: `FD3AFE4F20B5DBB697E536535A1706994BD6D1383CBF8C03B20056A5FC7761C4`
- Current canonical spec comparison SHA-256: `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B`

## Exclusions

Do not copy the following during selection:

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
- root operator-sourced screenshots

## Selection Notes

This is not a select run. Later selection must copy only inventory-declared files and update `docs/agent-runs/final-selection.md` plus `docs/agent-runs/selection-sets.json` through an explicit Hera selection step.

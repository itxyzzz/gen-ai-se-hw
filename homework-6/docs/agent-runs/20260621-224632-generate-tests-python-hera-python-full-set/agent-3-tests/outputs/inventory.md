# Themis Candidate Test Output Inventory

- Run ID: `20260621-224632-generate-tests-python-hera-python-full-set`
- Stack: `python`
- Mode: `generate`
- Parent Hera run ID: `20260621-215717-orchestrate-runs-python-full-set`
- Target Hephaestus (Code Generator) run ID: `20260621-222543-generate-code-python-hera-python-full-set`
- Target Hephaestus inventory path: `docs/agent-runs/20260621-222543-generate-code-python-hera-python-full-set/agent-2-code/outputs/inventory.md`
- Target Hephaestus inventory SHA-256: `CB19F97C1ED372905A764F5735EC6E3D0BDA6F1BEF132B723E258770B99C30DF`
- Source Athena (Spec Writer) run ID: `20260621-220037-write-spec-python-hera-python-full-set`
- Source specification path: `docs/agent-runs/20260621-220037-write-spec-python-hera-python-full-set/agent-1-spec/outputs/specification.md`
- Source specification SHA-256: `6F2E8CD844884DF06172EEB1CF92A2956BC45425FE514B7A820ABAEA249D2222`
- Current canonical spec SHA-256, comparison only: `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B`
- Source spec mismatch status: yes, by explicit Hera candidate request.

## Selectable Files

| Candidate file under `agent-3-tests/outputs/` | Intended canonical target | Kind | SHA-256 | Selection action |
|---|---|---|---|---|
| `tests/test_themis_quality.py` | `tests/test_themis_quality.py` | Themis unit, integration, privacy, dry-run, archival, and support-behavior test expansion | `55B0EA6A2D516DACC626AB6B0CE381F7090B18234930D2B326D98D744C8FB9A9` | Create or replace |

## Validation Support Files Not Selectable

The following files were copied into `agent-3-tests/workspace/project-under-test/` only to validate the candidate package:

- `scripts/check_coverage_gate.py`
- `.githooks/pre-push`

Do not copy these from this Themis output package during selection. Their canonical Operator Layer versions are maintained separately.

## Runtime And Tool Exclusions

Do not select or copy these paths from this run:

- `agent-3-tests/workspace/`
- `agent-3-tests/evidence/`
- `agent-3-tests/review/`
- `shared/`
- `archive/`
- `.coverage`
- `.coverage*`
- `.pytest_cache/`
- `.test-tmp/`
- `tmp/`
- `__pycache__/`
- `*.pyc`
- copied validation support inputs such as `sample-transactions.json`, `research-notes.md`, `scripts/check_coverage_gate.py`, and `.githooks/pre-push`

## Selection Notes

- This is a preserved candidate only. It is not a canonical replacement.
- Later selection must copy only the inventory-declared file above.
- The baseline tests from the targeted Hephaestus package remain in `workspace/selected-code/`; this Themis package extends that suite with additional quality coverage.


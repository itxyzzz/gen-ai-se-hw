# Themis Candidate Output Inventory

- Run ID: `20260620-144025-generate-tests-python-fresh-spec`
- Targeted Hephaestus run ID: `20260619-175211-generate-code-python-fresh-spec`
- Targeted Hephaestus inventory: `docs/agent-runs/20260619-175211-generate-code-python-fresh-spec/agent-2-code/outputs/inventory.md`
- Source Athena run ID: `20260619-170102-write-spec-python-fresh`
- Source/current spec SHA-256: `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B`

## Selectable Files

| Candidate path | Canonical target | Kind | SHA-256 | Action |
|---|---|---|---|---|
| `pytest.ini` | `pytest.ini` | pytest config | `EEB1F377120C43692B604DD429BCF53997729FA91BB97BDC243E214B439734C6` | replaces |
| `tests/test_common.py` | `tests/test_common.py` | unit test | `EDCF359EF5F2AB2C62C4AD6128FA1260F9F7C926746F132FBE342F3393295F70` | replaces |
| `tests/test_transaction_validator.py` | `tests/test_transaction_validator.py` | unit test | `0D4EEB9F5E21786DFB62342E11C7F05EABF27091B1A9ECFDA3A8393075240B73` | replaces |
| `tests/test_fraud_detector.py` | `tests/test_fraud_detector.py` | unit test | `E8B42C30A9B451190BBBD522CE487D899D429526FDCC95779C6AAF2CE3D29B17` | replaces |
| `tests/test_settlement_processor.py` | `tests/test_settlement_processor.py` | unit test | `736839F01918CA5175025B15FE2A88035BD344C2FA167404987DE16596B7F352` | replaces |
| `tests/test_integrator_pipeline.py` | `tests/test_integrator_pipeline.py` | integration test | `B3A55C623C82D0B42122FF3376A4AF21B6A27EF9EA936B3AD73B4B4DAB5AFF2A` | replaces |
| `tests/test_themis_quality.py` | `tests/test_themis_quality.py` | Themis quality test | `4D4DE1EC932BDA024C6E52EFE802FE9CBDFA07DA7494E8654836FACF9851C6D9` | creates |

## Excluded Runtime And Tool Outputs

The following are not selectable and must not be copied to canonical targets through this inventory:

- `workspace/`
- `evidence/`
- `shared/`
- `archive/`
- `.test-tmp/`
- `tmp/`
- `.coverage*`
- `.pytest_cache/`
- `__pycache__/`
- `*.pyc`
- copied support inputs such as `sample-transactions.json`, `specification.md`, and `scripts/check_coverage_gate.py`

## Coverage And Scope Notes

- Candidate suite: 40 tests.
- Coverage gate: 95.10% total coverage with `--fail-under=80`.
- Blocking-path evidence: `--fail-under=99` fails as expected while all tests still pass.
- The suite targets the named fresh Hephaestus package and does not modify runtime product code.

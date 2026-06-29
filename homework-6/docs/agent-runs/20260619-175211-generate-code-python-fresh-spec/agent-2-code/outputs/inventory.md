# Hephaestus Candidate Output Inventory

- Run ID: `20260619-175211-generate-code-python-fresh-spec`
- Source Athena run ID: `20260619-170102-write-spec-python-fresh`
- Canonical source spec path: `homework-6/specification.md`
- Source spec SHA-256: `44FD7EF6AC4FEE8070A23820DD784AA6215795D58AE3D7EB8225EFEFB8AB9E3B`

## Selectable Files

| Candidate path | Canonical target | Kind | SHA-256 |
|---|---|---|---|
| `integrator.py` | `integrator.py` | runtime orchestrator | `7FECF35942C0D81D36E44A31CA24401CA3A0604184A1509657F941BA76674EC6` |
| `pytest.ini` | `pytest.ini` | test config | `EEB1F377120C43692B604DD429BCF53997729FA91BB97BDC243E214B439734C6` |
| `agents/__init__.py` | `agents/__init__.py` | runtime package init | `D68783D6D0A6476A13A9C9F284EF7ACB40B9C0FECD0648FE8BE40E9E08E2A9FF` |
| `agents/common.py` | `agents/common.py` | runtime utilities | `193950FA1712238DB2BFC9F162FD4B8ACA79F87AED546DBE63B97F9082CE4F6E` |
| `agents/transaction_validator.py` | `agents/transaction_validator.py` | runtime component | `619DC6691E71A3815306C70835B4E45BF22637AF48FDAA880B4DB2C8BA673713` |
| `agents/fraud_detector.py` | `agents/fraud_detector.py` | runtime component | `65EAFADB9BCD3117C5A6F0579B9EF52C9AC0689881585CF5215FB1D9C35123F1` |
| `agents/settlement_processor.py` | `agents/settlement_processor.py` | runtime component | `13E4BE3EB58956600B6EFC063327A10562E3766BBBCFCADB221459AE7ECB353C` |
| `tests/test_common.py` | `tests/test_common.py` | focused tests | `EDCF359EF5F2AB2C62C4AD6128FA1260F9F7C926746F132FBE342F3393295F70` |
| `tests/test_transaction_validator.py` | `tests/test_transaction_validator.py` | focused tests | `0D4EEB9F5E21786DFB62342E11C7F05EABF27091B1A9ECFDA3A8393075240B73` |
| `tests/test_fraud_detector.py` | `tests/test_fraud_detector.py` | focused tests | `E8B42C30A9B451190BBBD522CE487D899D429526FDCC95779C6AAF2CE3D29B17` |
| `tests/test_settlement_processor.py` | `tests/test_settlement_processor.py` | focused tests | `736839F01918CA5175025B15FE2A88035BD344C2FA167404987DE16596B7F352` |
| `tests/test_integrator_pipeline.py` | `tests/test_integrator_pipeline.py` | integration tests | `212AB3F9751549D33527D274E37AB6981D98339C84EB193F9B15E224EBA419C1` |
| `research-notes.md` | `research-notes.md` | Context7 notes | `12C8E0C589A5B6ACD885C333422FA40A9152A68A1D3351FF8FFFF2FF5A55EA33` |

## Excluded Runtime And Tool Outputs

These paths are not selectable code outputs:

- `shared/`
- `archive/`
- `.coverage`
- `.pytest_cache/`
- `__pycache__/`
- `*.pyc`

## Scope Notes

- No Task 3 commands or hooks are included.
- No Task 4 `mcp/server.py` or MCP config changes are included.
- No Task 5 README, HOWTORUN, screenshots, or PR support files are included.

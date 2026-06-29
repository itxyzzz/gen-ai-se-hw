# Hephaestus Output Inventory

- Run ID: `20260618-223217-generate-code-python-primary`
- Selection status: selected by default as the first successful Hephaestus (Code Generator) output package.
- Source Athena run ID: `20260618-003908-write-spec-python-replacement`
- Source specification: `homework-6/specification.md`
- Source specification SHA-256: `B08B8D365070DAD1F6A9BFE36F2D21951802CF0A284615706F17086E961DDD06`
- Output package path: `homework-6/docs/agent-runs/20260618-223217-generate-code-python-primary/agent-2-code/outputs/`

## Selectable Files

| Output package file | Canonical target | Kind | SHA-256 |
|---|---|---|---|
| `integrator.py` | `homework-6/integrator.py` | source | `6B93D4942CFE1265F51AE94675450899B8E9E495B70253BC8091590C5888B815` |
| `pytest.ini` | `homework-6/pytest.ini` | test configuration | `F72BBD3343AEEDC44A09F0B1BB01855D9A3377931E06944A7BB979CFF1F3198D` |
| `agents/__init__.py` | `homework-6/agents/__init__.py` | source | `2761B882DE3D7B9441FD8B07EF0B9B38E191A2738F735044A3E42D093B7201DB` |
| `agents/common.py` | `homework-6/agents/common.py` | source | `56BB256989F76B41705A67392932B0F45028B82B4E33750D76E57D7F266775F9` |
| `agents/transaction_validator.py` | `homework-6/agents/transaction_validator.py` | source | `7ECCE54A20D1710758412D9453C83DE2FD0A0394FE1C6656EE19145A97C9E5FA` |
| `agents/fraud_detector.py` | `homework-6/agents/fraud_detector.py` | source | `2B0825E33618CB34CE5426717D77D76FEFDA162D9A19CD64E4E5FB4B94C1718E` |
| `agents/settlement_processor.py` | `homework-6/agents/settlement_processor.py` | source | `6523812978B66A6F6F8902ABB322F5BB93D77535B43C269E29A3DFF4E8D09C3A` |
| `tests/test_common.py` | `homework-6/tests/test_common.py` | test | `C73A971C4D66DD8770542D09F311783F734262CA6D8C0E6DF9A53A1B96697234` |
| `tests/test_transaction_validator.py` | `homework-6/tests/test_transaction_validator.py` | test | `C8FA095946A36413AAB0FE79F5EBE26E5D9898A84775B6603C843713A5CB59A3` |
| `tests/test_fraud_detector.py` | `homework-6/tests/test_fraud_detector.py` | test | `3A4305A28F86363B9C2FAF630777B51F696E2EFBFA086163613E24E6E562D68F` |
| `tests/test_settlement_processor.py` | `homework-6/tests/test_settlement_processor.py` | test | `C2366AE884831269179D09AEAFA5C942249FB252B77FC3E0FC78D85283264C31` |
| `tests/test_pipeline_end_to_end.py` | `homework-6/tests/test_pipeline_end_to_end.py` | test | `04D743DD8C86D28C10184D5F1373010FDD02A8217B1FFF6DE6BAE69CAC28A54B` |
| `research-notes.md` | `homework-6/research-notes.md` | research | `20D25E28D94258E98BFA7A752DCCABA99ADAA2F92D82E52FCD95522E2A1DB363` |

## Excluded Paths

The selected package intentionally excludes runtime and tool output:

- `homework-6/shared/`
- `homework-6/archive/`
- `.coverage`
- `__pycache__/`
- `.pytest_cache/`

## Selection And Replacement Procedure

For the first successful Hephaestus run, this inventory is selected by default because no prior selected code package existed. For later selections, remove the canonical targets listed in this inventory, then copy the selected replacement package files to their declared canonical targets. Do not remove or copy paths outside the inventory unless an operator explicitly approves an expanded ownership boundary.

Runtime outputs are never copied during code selection. A selected code package may create new runtime output only when the pipeline is executed after selection.

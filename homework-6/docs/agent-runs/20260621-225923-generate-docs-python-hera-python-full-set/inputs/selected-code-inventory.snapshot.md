# Hephaestus Candidate Output Inventory

- Run ID: `20260621-222543-generate-code-python-hera-python-full-set`
- Stack: `python`
- Mode: `generate`
- Source Athena (Spec Writer) run ID: `20260621-220037-write-spec-python-hera-python-full-set`
- Source specification path: `docs/agent-runs/20260621-220037-write-spec-python-hera-python-full-set/agent-1-spec/outputs/specification.md`
- Source specification SHA-256: `6F2E8CD844884DF06172EEB1CF92A2956BC45425FE514B7A820ABAEA249D2222`
- Candidate package root: `docs/agent-runs/20260621-222543-generate-code-python-hera-python-full-set/agent-2-code/outputs/`

## Selectable Files

| Candidate file | Canonical target | Kind | SHA-256 |
|---|---|---|---|
| `integrator.py` | `integrator.py` | Python runtime integrator | `CB47718A8DE857FE70455105E93FBD4F6C3FDC955EF3A7AFED5638338D3E621B` |
| `agents/__init__.py` | `agents/__init__.py` | Python package marker | `CF62B23F81F2097761B3B13713810A2322439AEA728D4EEE1FC277015D969CE1` |
| `agents/common.py` | `agents/common.py` | Shared runtime utilities | `517B7DA1AE83D6E3812E22A2826649DBC2652BFADC7B829700347B77646AAC62` |
| `agents/transaction_validator.py` | `agents/transaction_validator.py` | Runtime component | `C1BC663F855661A1D0F69DDB0E13A6C772403F896563ACA4FABEBCBC7A255FAD` |
| `agents/fraud_detector.py` | `agents/fraud_detector.py` | Runtime component | `DDF070CF3209B9554B077DDF60CE33DC4F9CD456C3A30F8669D2F8BE5F2A882B` |
| `agents/settlement_processor.py` | `agents/settlement_processor.py` | Runtime component | `DBA6E31BA9A7A6251B3DC4569BA24ED9F7A2FA2A489ACE839FEC4D3CD821BB95` |
| `agents/reporting_agent.py` | `agents/reporting_agent.py` | Runtime component | `5E9D43C7F76E4A80548B7E26473A2803050F6C8AECA09A57B8E048F4ED1F4E99` |
| `pytest.ini` | `pytest.ini` | Test configuration | `C993DD7BD048D7F9178A8409FEDB903C828CC2CE7DCADB5987A79ED2F4BF3C98` |
| `tests/conftest.py` | `tests/conftest.py` | Baseline pytest fixture | `F41990A7A762FB2A82371729E818EE95B096F149DEB572B8CFA35473F79A249D` |
| `tests/test_common.py` | `tests/test_common.py` | Baseline test | `165D286DB00FC0C0E3A5830980837FF5BD5E883543C1DD783ED330B97C43FC8D` |
| `tests/test_transaction_validator.py` | `tests/test_transaction_validator.py` | Baseline test | `BB51B95E97C7E49B6C3274F272842A003469D94B3B9C971AB4E7DC80ABA981B4` |
| `tests/test_fraud_detector.py` | `tests/test_fraud_detector.py` | Baseline test | `D9B88CAACE7472D61C1A8376E51E8AAE46B9DCA527D23A2C52FB8712D11B3484` |
| `tests/test_settlement_processor.py` | `tests/test_settlement_processor.py` | Baseline test | `055DDF93338A81985EB5CA9F573BC31066478695A352CDFED94D731A2D49C15A` |
| `tests/test_reporting_agent.py` | `tests/test_reporting_agent.py` | Baseline test | `24D66A3524D132F419121B4835C6696CA86EE182915CDF8E73E62A13316DBCD3` |
| `tests/test_integrator_pipeline.py` | `tests/test_integrator_pipeline.py` | Baseline test | `2DBF6DE0DA6A3530DDFF77D65AC023C066B179010C5B4E8CFE53A3D6C6FFAFE0` |
| `research-notes.md` | `research-notes.md` | Context7 research notes | `5F6D1013D66F039866B4623F41061117B5460ECF674E78A28FE178997E204E0B` |

## Candidate Fixture

| Candidate file | Canonical target | Kind | SHA-256 |
|---|---|---|---|
| `sample-transactions.json` | `sample-transactions.json` | Copied input fixture, already canonical | `771DA836CAAAA42921C628D6CD2E42D52E12687917BA60633E594D526BF4BF12` |

The fixture is included for candidate validation traceability. Selection should not overwrite the canonical fixture unless an explicit operator selection says so.

## Runtime Evidence, Not Selectable Code

| Path | Purpose | Selection treatment |
|---|---|---|
| `shared/` | Current last-run candidate evidence after the second successful run. Contains sanitized protocol/result files and `run-provenance.json`. | Exclude from selectable code. Do not copy during code selection unless an operator explicitly wants root runtime evidence refreshed. |
| `archive/shared-001/` | Archived prior candidate `shared/` evidence from the first successful validation run. | Exclude from selectable code and normal committed evidence. |
| `evidence/validation-summary.json` | Compact validation evidence summary. SHA-256 `ADE091437F6BD4D9D51A335CBFE3CD3170CEE3F2B79A6504A1ACEF3EE81275FF`. | Evidence only; not a canonical product target. |

## Tool And Runtime Exclusions

Do not select or copy these paths:

- `shared/`
- `archive/`
- `evidence/`
- `.coverage`
- `.coverage*`
- `.pytest_cache/`
- `.test-tmp/`
- `test-tmp/`
- `tmp/`
- `__pycache__/`
- `*.pyc`

## Selection Notes

- This is a preserved candidate only. It is not a canonical replacement.
- Later selection must remove prior selected canonical targets declared by the selected inventory before copying this package.
- This inventory intentionally does not list `mcp/`, `mcp.json`, `.codex/config.toml`, Task 3 commands, hooks, screenshots, or reviewer documentation.

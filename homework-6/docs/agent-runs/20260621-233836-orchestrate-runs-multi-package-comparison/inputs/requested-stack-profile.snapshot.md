# Compared Stack Profile Snapshot

## Python Profile

- Runtime language: Python 3.12
- Money handling: `decimal.Decimal`
- Tests: `pytest` and `pytest-cov`
- Coverage gate: `python scripts/check_coverage_gate.py --stack python --fail-under 80`
- Runtime protocol: JSON files under `shared/input`, `shared/processing`, `shared/output`, and `shared/results`
- MCP: existing FastMCP `pipeline-status` reader plus Context7 research evidence

## Java Profile

- Runtime language: Java 17 or newer
- Build: Maven
- Source layout: `src/main/java/...`
- Test layout: `src/test/java/...`
- Money handling: `BigDecimal`
- JSON: Jackson or equivalent strict JSON handling
- Tests: JUnit 5/JUnit Jupiter through Maven Surefire or Failsafe
- Coverage: JaCoCo `report` and `check`, compatible with the shared helper through `--stack java`
- Runtime protocol: same stack-neutral `shared/results/summary.json` and `shared/results/TXN*.json` contract

## Selection Boundary

Both stack profiles can satisfy the assignment in preserved run evidence. Python remains canonical until an explicit Hera `select-set` instruction updates `docs/agent-runs/final-selection.md` and `docs/agent-runs/selection-sets.json`.


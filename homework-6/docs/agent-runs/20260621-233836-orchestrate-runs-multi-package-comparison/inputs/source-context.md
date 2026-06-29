# Source Context

## Instructions And Control Packages

- Root repository instructions: `../AGENTS.md`
- Root homework standards: `../HOMEWORK_STANDARDS.md`
- Root README: `../README.md`
- Homework assignment: `TASKS.md`
- Homework agent guide: `agents.md`
- Hera skill: `.agents/skills/orchestrate-runs/SKILL.md`
- Hera workflow: `agent-control/orchestrate-runs/workflow.md`
- Hera quality bar: `agent-control/orchestrate-runs/quality-bar.md`
- Hera run registry: `agent-control/orchestrate-runs/run-registry.md`
- Clio workflow context: `agent-control/generate-docs/workflow.md`
- Operation guidance: `agent-control/operate-pipeline/commands-and-hooks.md`
- Agent config: `.codex/config.toml`

## Selection And Registry Records

- Package registry: `docs/agent-runs/selection-sets.json`
- Human selection history: `docs/agent-runs/final-selection.md`
- Current canonical package-set ID: `python-canonical-20260621`

## Compared Package Evidence

### Canonical Selected Package

- Clio run: `docs/agent-runs/20260621-011348-generate-docs-python-review-repair/`
- Clio metadata: `docs/agent-runs/20260621-011348-generate-docs-python-review-repair/run-metadata.md`
- Clio inventory: `docs/agent-runs/20260621-011348-generate-docs-python-review-repair/agent-4-docs/outputs/inventory.md`
- Clio validation: `docs/agent-runs/20260621-011348-generate-docs-python-review-repair/agent-4-docs/validation-checklist.md`
- Clio handoff: `docs/agent-runs/20260621-011348-generate-docs-python-review-repair/agent-4-docs/handoff.md`
- Reviewer docs inspected: `README.md` and `TESTING_GUIDE.md` under the Clio output folder

### Latest Python Package

- Parent Hera run: `docs/agent-runs/20260621-215717-orchestrate-runs-python-full-set/`
- Parent Hera child ledger: `docs/agent-runs/20260621-215717-orchestrate-runs-python-full-set/agent-5-orchestrator/child-runs.md`
- Parent Hera handoff: `docs/agent-runs/20260621-215717-orchestrate-runs-python-full-set/agent-5-orchestrator/handoff.md`
- Clio run: `docs/agent-runs/20260621-225923-generate-docs-python-hera-python-full-set/`
- Clio metadata: `docs/agent-runs/20260621-225923-generate-docs-python-hera-python-full-set/run-metadata.md`
- Clio inventory: `docs/agent-runs/20260621-225923-generate-docs-python-hera-python-full-set/agent-4-docs/outputs/inventory.md`
- Clio validation: `docs/agent-runs/20260621-225923-generate-docs-python-hera-python-full-set/agent-4-docs/validation-checklist.md`
- Clio handoff: `docs/agent-runs/20260621-225923-generate-docs-python-hera-python-full-set/agent-4-docs/handoff.md`
- Reviewer docs inspected: `README.md` and `TESTING_GUIDE.md` under the Clio output folder

### Latest Java Package

- Parent Hera run: `docs/agent-runs/20260621-180512-orchestrate-runs-java-full-set/`
- Parent Hera child ledger: `docs/agent-runs/20260621-180512-orchestrate-runs-java-full-set/agent-5-orchestrator/child-runs.md`
- Parent Hera handoff: `docs/agent-runs/20260621-180512-orchestrate-runs-java-full-set/agent-5-orchestrator/handoff.md`
- Clio run: `docs/agent-runs/20260621-203431-generate-docs-java-hera-java-full-set/`
- Clio metadata: `docs/agent-runs/20260621-203431-generate-docs-java-hera-java-full-set/run-metadata.md`
- Clio inventory: `docs/agent-runs/20260621-203431-generate-docs-java-hera-java-full-set/agent-4-docs/outputs/inventory.md`
- Clio validation: `docs/agent-runs/20260621-203431-generate-docs-java-hera-java-full-set/agent-4-docs/validation-checklist.md`
- Clio handoff: `docs/agent-runs/20260621-203431-generate-docs-java-hera-java-full-set/agent-4-docs/handoff.md`
- Reviewer docs inspected: `README.md` and `TESTING_GUIDE.md` under the Clio output folder

## Notes

- The comparison intentionally did not read raw `sample-transactions.json` payloads because Clio outputs already provide safe aggregate evidence.
- The comparison did not rerun Python or Java tests. It reviewed the preserved validation evidence requested by the operator.


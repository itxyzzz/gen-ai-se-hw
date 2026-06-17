# Agent Run Registry

This folder preserves Homework 6 agent and pipeline runs so repeated generation, troubleshooting, comparison, and final selection do not overwrite useful evidence.

## Sources of Truth

To avoid drift, this registry guide does not restate the full Agent 1 workflow or registry rules. Use these files as the authoritative instructions:

- Canonical write-spec package: `../../agent-control/write-spec/README.md`
- Registry preservation and selection rules: `../../agent-control/write-spec/run-registry.md`
- Workflow, modes, run layout, and sub-agent phases: `../../agent-control/write-spec/workflow.md`
- Stack enum and stack-specific defaults: `../../agent-control/write-spec/stack-profiles.md`
- Output, comparison, and review quality bar: `../../agent-control/write-spec/quality-bar.md`

If this README conflicts with the shared package, update this README and follow the package.

The original Homework 6 repository references `specification-TEMPLATE-hint.md`, but that file is not present in this checkout. Agent 1 runs must record that absence in `run-metadata.md` and use the Task 1 section list plus Homework 3 references as the local template source unless the template file is later added.

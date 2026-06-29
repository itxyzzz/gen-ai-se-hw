# Development Process

1. Generate the Java specification package under this Athena run folder.
2. Generate Java code under the named Hephaestus run folder without copying to canonical root targets.
3. Validate Maven compilation, JUnit tests, pipeline execution, repeated-run archival, Context7 notes, and privacy boundaries.
4. Generate Themis tests under a separate run-local workspace that targets the named Hephaestus inventory.
5. Generate Clio documentation under a preserved Java alternate documentation run.
6. Stop at Hera handoff. Do not update `selection-sets.json`, `final-selection.md`, or canonical Python files until a later explicit `compare-set` or `select-set`.

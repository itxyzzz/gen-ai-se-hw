# Screenshot Capture Notes

This Java candidate run produced fresh run-local terminal-style PNGs from the selected Java Themis retry evidence instead of copying Python-era operator screenshots.

Produced run-local screenshots:

- `agent-4-docs/outputs/docs/screenshots/pipeline-run.png`
- `agent-4-docs/outputs/docs/screenshots/test-coverage.png`
- `agent-4-docs/outputs/docs/screenshots/skill-run-pipeline.png`
- `agent-4-docs/outputs/docs/screenshots/hook-trigger.png`
- `agent-4-docs/outputs/docs/screenshots/mcp-interaction.png`

Manual capture steps if a future selector wants live terminal screenshots:

1. Copy or select the Java candidate package into a workspace containing its `pom.xml`, Java sources, tests, `sample-transactions.json`, and `scripts/check_coverage_gate.py`.
2. If the local Maven settings mirror is unavailable, create an empty settings file outside selectable outputs and run Maven with `-s` and `-gs` pointing to that file.
3. Capture `mvn exec:java` showing `total=8 settled=2 rejected=2 review_required=4 error=0`.
4. Capture `mvn test jacoco:report jacoco:check` or the coverage helper showing the 80% gate passing.
5. Capture the coverage helper with `--fail-under 99` showing the deliberate blocking path.
6. Capture Context7 query evidence and a custom `pipeline-status` reader call against Java result files, keeping raw account identifiers and descriptions out of view.

No source screenshot in `docs/screenshots/operator-sourced/` was edited, renamed, compressed, or copied to root stable targets.

# Screenshot Capture Notes

Automated in-app terminal screenshots were not required for this preserved candidate package because Clio generated five run-local terminal-style PNGs from fresh candidate evidence:

- `agent-4-docs/outputs/docs/screenshots/pipeline-run.png`
- `agent-4-docs/outputs/docs/screenshots/test-coverage.png`
- `agent-4-docs/outputs/docs/screenshots/skill-run-pipeline.png`
- `agent-4-docs/outputs/docs/screenshots/hook-trigger.png`
- `agent-4-docs/outputs/docs/screenshots/mcp-interaction.png`

No operator-sourced screenshots were edited or copied. The candidate screenshots are semantically distinct and map to the assignment-named evidence categories.

If the operator wants literal terminal captures instead of generated terminal-style evidence PNGs, use these manual capture steps from the Clio validation workspace or a selected copy of the candidate package:

1. Capture `python integrator.py` after it prints `Pipeline complete: total=8 settled=2 rejected=2 review_required=4 error=0`.
2. Capture `python scripts\check_coverage_gate.py --stack python --fail-under 80` showing 36 tests and 97.44% total coverage.
3. Capture the documented `/run-pipeline` fast-path summary with `result_count=8` and `all_present=true`.
4. Capture `python scripts\check_coverage_gate.py --stack python --fail-under 99` showing expected coverage-threshold failure.
5. Capture Context7 evidence from Hephaestus research notes beside a custom `pipeline-status` result such as `list_pipeline_results` or `get_transaction_status("TXN006")`.

All captures should avoid raw account IDs, raw descriptions, credentials, tokens, and full audit payloads.

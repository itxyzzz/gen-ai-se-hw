# Athena Research Notes

## Local Sources

- `agent-control/write-spec/stack-profiles.md`: Java profile requires Maven, BigDecimal, Jackson or equivalent JSON handling, JUnit Jupiter, JaCoCo, and stack-neutral results.
- `agents.md`: Java remains alternate; Python remains canonical until explicit operator selection.
- `TASKS.md`: assignment requires at least three runtime pipeline components, file-based JSON protocol, Context7 use in code generation, tests, docs, and screenshots.

## Applied Decisions

- Use four runtime components: Transaction Validator, Fraud Detector, Settlement Processor, and Reporting Agent.
- Keep the Java alternate under preserved run folders only.
- Keep result JSON compatible with existing Python MCP status tooling.
- Require Themis to raise Java coverage over the 80 percent gate instead of weakening JaCoCo rules.

## Limitations

- No raw sample payload is copied here to preserve privacy boundaries.
- This is a preserved alternate specification, not a canonical selection.

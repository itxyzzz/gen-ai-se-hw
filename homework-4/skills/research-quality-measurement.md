# Research Quality Measurement Skill

Use this skill when producing `verified-research.md`.

## Required Output Sections

1. Verification Summary
2. Verified Claims
3. Discrepancies Found
4. Research Quality Assessment
5. References

## Quality Levels

| Level | Label | Criteria |
| --- | --- | --- |
| 4 | Verified | Every file:line reference exists, snippets match source, and no material discrepancy is found. |
| 3 | Mostly Verified | References exist and core claims are correct, but minor line drift or wording differences are present. |
| 2 | Partially Verified | Some core claims are correct, but one or more important references or snippets are wrong. |
| 1 | Unverified | Research is too incomplete, vague, or inaccurate for a planner to rely on. |

## Assessment Rules

- Cite each checked claim with a file path and line number.
- Explain any line drift, missing file, or snippet mismatch.
- State whether a bug planner can use the research safely.
- Prefer the lowest level that matches the observed evidence.

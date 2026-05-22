# Unit Tests FIRST Skill

Use this skill when generating or reviewing unit tests.

## FIRST Criteria

- Fast: tests run quickly and avoid slow external services.
- Independent: each test sets up its own data and does not depend on test order.
- Repeatable: tests produce the same result on every machine without hidden state.
- Self-validating: tests contain clear assertions and do not require manual inspection.
- Timely: tests are written for changed behavior while the change is made.

## Required Test Report Sections

1. Generated Tests
2. FIRST Assessment
3. Commands Run
4. Results
5. Remaining Gaps

## Scope Rule

Generate tests only for changed or newly exposed behavior. Do not rewrite unrelated test suites.

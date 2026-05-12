# Development Process

## Purpose

This document defines the required workflow for Homework 3 Dispute Intake specification work without relying on Superpowers, GitHub Spec Kit, or any other addon. If such tools are used, they may assist the process but do not replace these gates.

## Operating Mode

Homework 3 is documentation-only. Selected-feature increments should refine the Dispute Intake specification package, not build the described application.

Use this process for every meaningful change:

1. Read repository instructions, `homework-3/TASKS.md`, `README.md`, `agents.md`, and the files directly affected by the change.
2. Confirm the work is on `homework-3-submission` or another user-approved homework branch.
3. Write or update the relevant SPEC markdown section before adding supporting process or convention text.
4. Keep each requirement traceable to a clear objective, rule, or reviewer need.
5. Update `CHANGELOG.md` in the same increment.
6. Verify document presence, internal links, unfinished marker text, and scope boundaries before reporting completion.

## SPEC Markdown Usage

Use `specification.md` as the central SPEC file. It must eventually contain:

- High-level objective.
- Mid-level objectives.
- Non-functional and policy expectations.
- Implementation notes.
- Beginning context.
- Ending context.
- Low-level tasks.
- Edge cases and failure modes.
- Verification plan.
- Expected performance targets.

Supporting SPEC-style files may be added under `docs/` only when they keep the core spec readable. A supporting file must be linked from `specification.md` or `README.md`, and its responsibility must not overlap confusingly with another document.

## Documentation-First Gate

Before adding or changing selected-feature requirements:

- State what user, operator, compliance, or reviewer outcome the change supports.
- Identify the document that owns the change.
- Keep assumptions visible.
- Avoid final domain claims until `docs/domain-rules.md` has researched support.

## Verification-First Gate

For documentation-only changes, define the review check before editing. Examples:

- Required files exist.
- Links resolve within the package.
- No unfinished marker text outside intentionally archived or quoted examples.
- The spec keeps required Homework 3 layers.
- Agent rules do not contradict repository instructions.

For selected-feature spec changes, define how each mid-level objective would be verified by a builder or reviewer. Include acceptance criteria, test categories, manual review steps, audit checks, and performance checks as appropriate for Dispute Intake.

## TDD-Style Specification Practice

Because Homework 3 does not implement code, use TDD as a thinking pattern:

1. Write the expected observable behavior or review check first.
2. Define the failure or edge case that would prove the spec is incomplete.
3. Add the task or acceptance criterion that closes the gap.
4. Re-read the objective to confirm the requirement is traceable.

## Change Review Checklist

Before finishing an increment, check:

- The change stays within Homework 3 documentation scope.
- `CHANGELOG.md` has a useful entry.
- Links in changed files are correct.
- Feature changes stay aligned with EU/EEA payment-account Dispute Intake unless the homework scope is intentionally changed and documented.
- Regulatory or compliance statements are either researched or clearly marked for later research.
- Required deliverables remain easy for a reviewer to find.
- The package does not require optional tools to be understood or followed.
- Historical plan artifacts under `docs/superpowers/plans/` are treated as evidence and are excluded from active unfinished-marker scans unless the review intentionally targets the archive.

## Optional Tooling

Agents may use available local tools, AI assistants, or workflow plugins, but the written process must remain executable without them. If an increment uses optional tooling, document the tool's role and any missing quality controls in the relevant changelog or AI-assistance notes.

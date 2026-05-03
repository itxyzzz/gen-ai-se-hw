# Homework 2 Task 2: Rule-Based Auto-Classification Plan

## Summary

Implement Task 2 on top of the existing Spring Boot Task 1 API and Swagger setup. The classifier will be deterministic and rule-based, with no external LLM or agentic workflow runtime. It will support explicit classification through `POST /tickets/{id}/auto-classify`, automatic classification during `POST /tickets`, and automatic classification during `POST /tickets/import`.

## Key Changes

- Add a focused classification subsystem under `com.setu.support.ticket`.
- Extend ticket responses with stored classifier evidence.
- Support explicit, create-time, and import-time classification.
- Use default-on Spring properties for create/import auto-classification and create/import manual override behavior.
- Keep category and priority as top-level final ticket fields while storing classifier suggestions separately when manual input wins.

## API And Flow Updates

- Add `POST /tickets/{id}/auto-classify`.
- Allow missing `category` and `priority` during create/import when auto-classification is enabled.
- Preserve Task 1 validation behavior when auto-classification is disabled.
- Keep `PUT /tickets/{id}` as the manual override path after creation.
- Update OpenAPI, Postman, sample requests, and homework documentation.

## Tests And Evidence

- Add focused categorization tests for categories, priorities, confidence, reasoning, keywords, defaults, and precedence.
- Expand API/integration tests for explicit classification, auto-classification during create/import, manual override evidence, disabled auto-classification, decision logs, and OpenAPI coverage.
- Run targeted Maven test commands and `mvn clean verify`.
- Update coverage evidence at `docs/screenshots/test_coverage.png`.

## Assumptions

- The classifier remains deterministic and local.
- Feature flags are Spring application properties.
- Existing Task 1 sample files remain valid.
- Ticket state and decision logs remain in memory.
- This implements Task 2 only while keeping room for later Task 3-5 work.

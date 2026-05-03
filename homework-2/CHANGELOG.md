# Homework 2 Changelog

## Homework 2 - Step 4

### Added
- Added `AI_USAGE.md` to document the AI tool and model attribution for the implementation session.
- Added extensive test fixtures for valid, invalid, and malformed data in `src/test/resources/fixtures/`.
- Added screenshots of the AI Web mode interactions, Codex Local implementation, and Antigravity finalization in `docs/screenshots/`.

### Changed
- Updated `AI_USAGE.md` and `README.md` to detail the actual multi-stage, multi-model implementation flow using Codex Web, Codex Local, and Google Antigravity.
- Updated `README.md` and `TESTING_GUIDE.md` to finalize the Task 4 and Task 5 documentation, ensuring Mermaid diagram requirements and testing instructions are fully met.
- Enhanced `TicketIntegrationTest` to verify concurrent operations (20+ simultaneous requests) using an ExecutorService.
- Enhanced `TicketPerformanceTest` to validate timeouts and load handling for large bulk imports.

### Fixed
- Addressed any missing sample data by providing comprehensive payload variations for testing.

### Tests
- Finalized integration and performance coverage verifying concurrent operations.
- Verified `mvn clean verify` with 71 passing tests, confirming all functional behavior and that the JaCoCo coverage gate (>85%) is met.

## Homework 2 - Step 3

### Added
- Added deterministic rule-based ticket auto-classification for category and priority.
- Added `POST /tickets/{id}/auto-classify`.
- Added classification confidence, reasoning, keywords, suggestions, timestamp, and manual override evidence to ticket responses.
- Added default-on create/import classification flags and independent manual override flags.
- Added in-memory classification decision logging with application log output.
- Added classification-focused demo data and Postman/manual request coverage.

### Changed
- Create and import flows can now omit category and priority when auto-classification is enabled.
- Import validation now respects import classification flags while preserving partial failure summaries.
- Update flow marks category/priority edits as manual override evidence.
- README, API reference, architecture, runbook, testing guide, and Postman assets now cover Task 2.

### Fixed
- Removed the Task 1 documentation limitation that reserved classification for later work.

### Tests
- Added focused classifier, classification API, disabled-configuration, decision-log, and OpenAPI path tests.
- Verified targeted Task 2 classifier/API/OpenAPI slices during implementation.
- Verified `mvn clean verify` with 53 passing tests and 93.55% JaCoCo line coverage.

## Homework 2 - Step 2

### Added
- Added Springdoc OpenAPI generation and Swagger UI for the Homework 2 API.
- Added a reviewer-facing Swagger UI entrypoint at `/api-docs`.
- Added generated OpenAPI JSON coverage for ticket endpoints at `/v3/api-docs`.
- Added focused MockMvc tests for the API documentation routes.

### Changed
- Updated README, HOWTORUN, and API reference documentation with the OpenAPI documentation paths.

### Fixed
- Not applicable for this documentation feature increment.

### Tests
- Verified `mvn -Dtest=ApiDocumentationTest test` with 2 passing tests.
- Verified `mvn clean verify` with 32 passing tests and the JaCoCo coverage gate met.

## Homework 2 - Step 1

### Added
- Planned and implemented the Task 1 support ticket CRUD and multi-format import API.
- Added sample data, Postman collection, demo scripts, and homework-standard documentation.
- Added JaCoCo coverage evidence at `docs/screenshots/test_coverage.png`.

### Changed
- Replaced the placeholder Homework 2 README with complete implementation documentation.
- Updated managed PowerShell demo scripts to run and stop the packaged Spring Boot jar by stable Java process ID.

### Fixed
- Not applicable for the initial Homework 2 implementation step.

### Tests
- Added automated API, validation, import, integration, and performance tests with JaCoCo coverage reporting.
- Verified `mvn clean verify` with 30 passing tests and the JaCoCo coverage gate met.

# Homework 2 Changelog

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

# Changelog

All notable changes to this project will be documented in this file.

## Unreleased

### Added

- Added the Homework 1 project scaffold with README, run instructions, task notes, source and screenshot directories.
- Implemented a Spring Boot banking transactions REST API with endpoints to create, list, filter, and fetch transactions.
- Added account-level endpoints for balances and summaries:
  - `GET /accounts/{accountId}/balance`
  - `GET /accounts/{accountId}/summary`
- Added in-memory transaction storage, transaction domain models, structured validation errors, not-found handling, and centralized exception handling.
- Added validation for required fields, positive amounts, two-decimal money precision, supported currencies, transaction types, account ID format, filter dates, and invalid filter ranges.
- Added demo assets, including sample data, sample HTTP requests, and a Postman collection for manual API testing.
- Added Windows app lifecycle scripts for managed local runs:
  - `demo/start.ps1`
  - `demo/stop.ps1`
  - `demo/restart.ps1`
  - `demo/AppLifecycle.ps1`
- Added shell and batch launch helpers in `demo/run.sh` and `demo/run.bat`.
- Added automated integration coverage for account APIs, transaction commands, transaction queries, and lifecycle script behavior.
- Added planning and design notes under `docs/superpowers/`.
- Added homework evidence screenshots under `docs/screenshots/`.

### Changed

- Changed `GET /accounts/{accountId}/balance` to return balances grouped by currency rather than a single mixed-currency scalar.
- Changed `GET /accounts/{accountId}/summary` to report completed-transaction totals per currency, including incoming and outgoing transfer totals.
- Enforced type-specific account payload rules for transaction creation:
  - `deposit` now accepts only `toAccount`
  - `withdrawal` now accepts only `fromAccount`
  - forbidden account fields now return explicit validation messages
- Split the original transaction controller integration tests into focused command, query, and account API suites.
- Updated `demo/run.bat` to delegate to the managed Windows start script.
- Updated README, HOWTORUN, sample requests, sample data, and Postman examples to match the revised API contract.
- Documented architecture decisions, intentional overdraft simplification, UTC-only date filtering, and local lifecycle workflow.

### Fixed

- Rejected same-account transfer requests with an explicit validation error instead of allowing them to alter balances.

### Tests

- Added and updated automated tests for transaction creation, validation failures, filtering, account balances, account summaries, same-account transfer rejection, and deposit and withdrawal payload rules.

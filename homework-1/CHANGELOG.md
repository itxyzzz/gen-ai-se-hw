# Changelog

All notable changes to this project will be documented in this file.

## Unreleased

### Changed

- Rejected same-account transfer requests with an explicit validation error instead of allowing them to alter balances.
- Changed `GET /accounts/{accountId}/balance` to return balances grouped by currency rather than a single mixed-currency scalar.
- Changed `GET /accounts/{accountId}/summary` to report completed-transaction totals per currency, including incoming and outgoing transfer totals.
- Documented the intentional overdraft simplification and the UTC-only date filtering decision in the README.
- Enforced type-specific account payload rules for transaction creation:
  - `deposit` now accepts only `toAccount`
  - `withdrawal` now accepts only `fromAccount`
  - forbidden account fields now return explicit validation messages
- Updated automated tests to cover the new deposit and withdrawal payload rules.
- Updated README, run instructions, sample requests, sample data, and Postman examples to match the revised API contract.

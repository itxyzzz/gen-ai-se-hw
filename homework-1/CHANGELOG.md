# Changelog

All notable changes to this project will be documented in this file.

## Unreleased

### Changed

- Enforced type-specific account payload rules for transaction creation:
  - `deposit` now accepts only `toAccount`
  - `withdrawal` now accepts only `fromAccount`
  - forbidden account fields now return explicit validation messages
- Updated automated tests to cover the new deposit and withdrawal payload rules.
- Updated README, run instructions, sample requests, sample data, and Postman examples to match the revised API contract.

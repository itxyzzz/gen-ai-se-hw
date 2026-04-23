# Banking Transactions API

> **Student Name**: [Your Name]
> **Date Submitted**: [Date]
> **AI Tools Used**: Codex

## Project Overview

This project is a Java Spring Boot REST API for Homework 1. It supports creating banking transactions, listing transaction history, fetching transactions by ID, checking account balances, filtering transaction history, and viewing account summaries.

## Features Implemented

- Task 1: Core transaction API
- Task 2: Transaction validation
- Task 3: Transaction history filtering
- Task 4 Option A: Account transaction summary

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/transactions` | Create a new transaction |
| `GET` | `/transactions` | List transactions, with optional filters |
| `GET` | `/transactions/{id}` | Get a transaction by ID |
| `GET` | `/accounts/{accountId}/balance` | Get account balance |
| `GET` | `/accounts/{accountId}/summary` | Get account transaction summary |

## Filtering

`GET /transactions` supports these optional query parameters:

- `accountId=ACC-12345`
- `type=deposit`, `withdrawal`, or `transfer`
- `from=2024-01-01`
- `to=2024-01-31`

Filters can be combined.

## Architecture Decisions

- Spring Boot keeps the REST API small and easy to run.
- In-memory storage is used because the homework does not require a database.
- Money values use `BigDecimal`.
- Validation is isolated in `TransactionValidator`.
- Business logic is handled by `TransactionService`.
- Error handling is centralized in `GlobalExceptionHandler`.
- Constrained fields use allow-list validation to reject malformed values.

## Error Handling and Robustness

The API returns structured JSON errors for validation failures, malformed JSON, unsupported content types, and missing resources. It avoids returning stack traces, Java exception class names, or raw framework error messages to clients.

## AI Assistance Notes

This project was planned and implemented with AI agent assistance. Screenshots of prompts, generated code, and API test runs should be added under `docs/screenshots/`.

<div align="center">

*This project was completed as part of the AI-Assisted Development course.*

</div>

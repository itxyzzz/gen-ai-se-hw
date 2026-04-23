# Banking Transactions API

> **Student Name**: [Your Name]
> **Date Submitted**: [Date]
> **AI Tools Used**: Codex

## Project Overview

This project is a Java Spring Boot REST API for Homework 1. It supports creating banking transactions, listing transaction history, fetching transactions by ID, checking account balances grouped by currency, filtering transaction history, and viewing account summaries with per-currency totals.

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
| `GET` | `/accounts/{accountId}/balance` | Get account balances grouped by currency |
| `GET` | `/accounts/{accountId}/summary` | Get account transaction summary grouped by currency |

## Filtering

`GET /transactions` supports these optional query parameters:

- `accountId=ACC-12345`
- `type=deposit`, `withdrawal`, or `transfer`
- `from=2024-01-01`
- `to=2024-01-31`

Filters can be combined.

Date filters are interpreted strictly in UTC.

## Transaction Payload Rules

`POST /transactions` validates account fields based on `type`:

- `transfer` requires both `fromAccount` and `toAccount`
- `transfer` rejects same-account requests with `Transfer accounts must be different`
- `deposit` requires `toAccount` and rejects `fromAccount` with `fromAccount is not allowed for deposit transactions`
- `withdrawal` requires `fromAccount` and rejects `toAccount` with `toAccount is not allowed for withdrawal transactions`

## Account View Responses

`GET /accounts/{accountId}/balance` returns a per-currency ledger for the account:

```json
{
  "accountId": "ACC-12345",
  "balances": {
    "EUR": 100.00,
    "USD": -25.50
  }
}
```

`GET /accounts/{accountId}/summary` returns completed-transaction totals by currency:

```json
{
  "accountId": "ACC-12345",
  "totalDeposits": {
    "EUR": 100.00
  },
  "totalWithdrawals": {
    "USD": 25.50
  },
  "totalIncomingTransfers": {
    "USD": 15.00
  },
  "totalOutgoingTransfers": {
    "EUR": 10.00
  },
  "transactionCount": 4,
  "mostRecentTransactionDate": "2026-04-23T10:15:30Z"
}
```

## Architecture Decisions

- Spring Boot keeps the REST API small and easy to run.
- In-memory storage is used because the homework does not require a database.
- Money values use `BigDecimal`.
- Validation is isolated in `TransactionValidator`.
- Business logic is handled by `TransactionService`.
- Error handling is centralized in `GlobalExceptionHandler`.
- Constrained fields use allow-list validation to reject malformed values.
- Transaction account directions are enforced by type so deposits only credit a target account and withdrawals only debit a source account.
- Same-account transfers are rejected because they do not represent a meaningful movement of funds.
- Account balances and summary totals are derived per currency from transactions; the API does not convert or mix currencies.
- Overdrafts are intentionally allowed as a homework simplification; the API does not model sufficient-funds checks or credit limits.
- Account balance and summary views only count `completed` transactions so aggregate endpoints stay internally consistent.
- Timestamp storage and date filtering are handled in UTC by design; timezone-aware account statements are out of scope for this homework.
- Windows lifecycle scripts in `demo/` manage a background app process using compiled classes plus copied runtime dependencies so local restarts do not depend on the locked Spring Boot fat-JAR path.

## Error Handling and Robustness

The API returns structured JSON errors for validation failures, malformed JSON, unsupported content types, and missing resources. It avoids returning stack traces, Java exception class names, or raw framework error messages to clients.

## AI Assistance Notes

This project was planned and implemented with AI agent assistance. Screenshots of prompts, generated code, and API test runs should be added under `docs/screenshots/`.
*This project was completed as part of the AI-Assisted Development course.*

## Local Run Scripts

For Windows, prefer:

```powershell
.\demo\start.ps1
.\demo\stop.ps1
.\demo\restart.ps1
```

`demo\run.bat` now delegates to `start.ps1`.

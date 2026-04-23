# Currency Ledger And Transfer Rules Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Reject same-account transfers, represent account balances and summaries per currency, and make account aggregates consistently count only completed transactions.

**Architecture:** Keep the existing transaction record as the single source of truth and derive account views from it. Replace scalar balance/summary totals with per-currency maps at the API boundary, while preserving the existing controller/service/repository structure and adding only the validation and response types needed for clearer domain behavior.

**Tech Stack:** Java 17, Spring Boot 3, JUnit 5, MockMvc, Maven

---

### Task 1: Guard Against Same-Account Transfers

**Files:**
- Modify: `homework-1/src/test/java/com/setu/banking/transaction/TransactionCommandApiTest.java`
- Modify: `homework-1/src/main/java/com/setu/banking/transaction/TransactionValidator.java`
- Test: `homework-1/src/test/java/com/setu/banking/transaction/TransactionCommandApiTest.java`

- [ ] **Step 1: Write the failing test**

```java
@Test
void rejectsTransferWhenFromAndToAccountsMatch() throws Exception {
    mockMvc.perform(post("/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "fromAccount": "ACC-12345",
                  "toAccount": "ACC-12345",
                  "amount": 10.00,
                  "currency": "USD",
                  "type": "transfer"
                }
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Validation failed"))
        .andExpect(jsonPath("$.details[0].field").value("toAccount"))
        .andExpect(jsonPath("$.details[0].message").value("Transfer accounts must be different"));
}
```

- [ ] **Step 2: Run the test to verify it fails**

Run: `mvn test -Dtest=TransactionCommandApiTest#rejectsTransferWhenFromAndToAccountsMatch`
Expected: FAIL because the API currently creates the transaction instead of rejecting it.

- [ ] **Step 3: Write the minimal implementation**

```java
case TRANSFER -> {
    require(errors, "fromAccount", request.fromAccount());
    require(errors, "toAccount", request.toAccount());

    validateAccountIfPresent(errors, "fromAccount", request.fromAccount());
    validateAccountIfPresent(errors, "toAccount", request.toAccount());

    if (request.fromAccount() != null
        && request.toAccount() != null
        && request.fromAccount().equals(request.toAccount())) {
        errors.add(new ValidationErrorResponse.FieldErrorDetail(
            "toAccount",
            "Transfer accounts must be different"
        ));
    }
}
```

- [ ] **Step 4: Run the test to verify it passes**

Run: `mvn test -Dtest=TransactionCommandApiTest#rejectsTransferWhenFromAndToAccountsMatch`
Expected: PASS

- [ ] **Step 5: Run the focused transaction command suite**

Run: `mvn test -Dtest=TransactionCommandApiTest`
Expected: PASS

- [ ] **Step 6: Commit**

```bash
git add homework-1/src/test/java/com/setu/banking/transaction/TransactionCommandApiTest.java homework-1/src/main/java/com/setu/banking/transaction/TransactionValidator.java
git commit -m "fix: reject same-account transfers"
```

### Task 2: Represent Account Balances As Currency-Isolated Ledgers

**Files:**
- Create: `homework-1/src/main/java/com/setu/banking/transaction/AccountBalanceResponse.java`
- Modify: `homework-1/src/main/java/com/setu/banking/transaction/TransactionController.java`
- Modify: `homework-1/src/main/java/com/setu/banking/transaction/TransactionService.java`
- Modify: `homework-1/src/test/java/com/setu/banking/transaction/AccountApiTest.java`
- Modify: `homework-1/src/test/java/com/setu/banking/transaction/TransactionCommandApiTest.java`
- Test: `homework-1/src/test/java/com/setu/banking/transaction/AccountApiTest.java`
- Test: `homework-1/src/test/java/com/setu/banking/transaction/TransactionCommandApiTest.java`

- [ ] **Step 1: Write failing balance tests**

```java
@Test
void newAccountHasNoBalancesBeforeAnyTransaction() throws Exception {
    mockMvc.perform(get("/accounts/{accountId}/balance", "ACC-12345"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accountId").value("ACC-12345"))
        .andExpect(jsonPath("$.balances").isMap())
        .andExpect(jsonPath("$.balances").isEmpty());
}

@Test
void balanceSeparatesCurrenciesForTheSameAccount() throws Exception {
    mockMvc.perform(post("/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"toAccount":"ACC-22222","amount":100.00,"currency":"USD","type":"deposit"}
                """))
        .andExpect(status().isCreated());

    mockMvc.perform(post("/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"toAccount":"ACC-22222","amount":50.00,"currency":"EUR","type":"deposit"}
                """))
        .andExpect(status().isCreated());

    mockMvc.perform(post("/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"fromAccount":"ACC-22222","amount":10.00,"currency":"USD","type":"withdrawal"}
                """))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/accounts/{accountId}/balance", "ACC-22222"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.balances.USD").value(90.00))
        .andExpect(jsonPath("$.balances.EUR").value(50.00));
}
```

- [ ] **Step 2: Run the targeted tests to verify they fail**

Run: `mvn test -Dtest=AccountApiTest#newAccountHasNoBalancesBeforeAnyTransaction,AccountApiTest#balanceSeparatesCurrenciesForTheSameAccount`
Expected: FAIL because the current endpoint returns `balance` plus hard-coded `currency`.

- [ ] **Step 3: Add a response type and minimal service/controller changes**

```java
public record AccountBalanceResponse(
    String accountId,
    Map<String, BigDecimal> balances
) {
}
```

```java
public AccountBalanceResponse balanceFor(String accountId) {
    validator.validateAccountId("accountId", accountId);

    Map<String, BigDecimal> balances = repository.findAll().stream()
        .filter(transaction -> transaction.status() == TransactionStatus.COMPLETED)
        .collect(Collectors.groupingBy(
            Transaction::currency,
            TreeMap::new,
            Collectors.reducing(BigDecimal.ZERO, transaction -> balanceImpact(transaction, accountId), BigDecimal::add)
        ));

    balances.entrySet().removeIf(entry -> entry.getValue().compareTo(BigDecimal.ZERO) == 0);
    return new AccountBalanceResponse(accountId, balances);
}
```

- [ ] **Step 4: Run the targeted tests to verify they pass**

Run: `mvn test -Dtest=AccountApiTest#newAccountHasNoBalancesBeforeAnyTransaction,AccountApiTest#balanceSeparatesCurrenciesForTheSameAccount`
Expected: PASS

### Task 3: Make Account Summary Currency-Aware And Completed-Only

**Files:**
- Modify: `homework-1/src/main/java/com/setu/banking/transaction/AccountSummaryResponse.java`
- Modify: `homework-1/src/main/java/com/setu/banking/transaction/TransactionService.java`
- Modify: `homework-1/src/test/java/com/setu/banking/transaction/AccountApiTest.java`
- Test: `homework-1/src/test/java/com/setu/banking/transaction/AccountApiTest.java`

- [ ] **Step 1: Write failing summary tests**

```java
@Test
void summarySeparatesDepositAndWithdrawalTotalsByCurrency() throws Exception {
    mockMvc.perform(post("/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"toAccount":"ACC-77777","amount":100.00,"currency":"USD","type":"deposit"}
                """))
        .andExpect(status().isCreated());

    mockMvc.perform(post("/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"toAccount":"ACC-77777","amount":25.00,"currency":"EUR","type":"deposit"}
                """))
        .andExpect(status().isCreated());

    mockMvc.perform(post("/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"fromAccount":"ACC-77777","amount":40.00,"currency":"USD","type":"withdrawal"}
                """))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/accounts/{accountId}/summary", "ACC-77777"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalDeposits.USD").value(100.00))
        .andExpect(jsonPath("$.totalDeposits.EUR").value(25.00))
        .andExpect(jsonPath("$.totalWithdrawals.USD").value(40.00));
}
```

```java
void summaryCountsOnlyCompletedTransactions() throws Exception {
    repository.save(new Transaction(
        "txn-completed",
        null,
        "ACC-77777",
        new BigDecimal("20.00"),
        "USD",
        TransactionType.DEPOSIT,
        Instant.parse("2026-04-23T10:15:30Z"),
        TransactionStatus.COMPLETED
    ));

    repository.save(new Transaction(
        "txn-failed",
        null,
        "ACC-77777",
        new BigDecimal("999.00"),
        "USD",
        TransactionType.DEPOSIT,
        Instant.parse("2026-04-23T10:16:30Z"),
        TransactionStatus.FAILED
    ));

    mockMvc.perform(get("/accounts/{accountId}/summary", "ACC-77777"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.transactionCount").value(1))
        .andExpect(jsonPath("$.totalDeposits.USD").value(20.00));
}
```

- [ ] **Step 2: Run the targeted tests to verify they fail**

Run: `mvn test -Dtest=AccountApiTest#summarySeparatesDepositAndWithdrawalTotalsByCurrency`
Expected: FAIL because the summary currently returns scalar totals.

- [ ] **Step 3: Update the response type and service logic**

```java
public record AccountSummaryResponse(
    String accountId,
    Map<String, BigDecimal> totalDeposits,
    Map<String, BigDecimal> totalWithdrawals,
    long transactionCount,
    Instant mostRecentTransactionDate
) {
}
```

```java
List<Transaction> accountTransactions = repository.findAll().stream()
    .filter(transaction -> transaction.status() == TransactionStatus.COMPLETED)
    .filter(transaction -> accountId.equals(transaction.fromAccount()) || accountId.equals(transaction.toAccount()))
    .toList();
```

- [ ] **Step 4: Run the targeted tests to verify they pass**

Run: `mvn test -Dtest=AccountApiTest#summarySeparatesDepositAndWithdrawalTotalsByCurrency`
Expected: PASS

- [ ] **Step 5: Run the full account-focused suite**

Run: `mvn test -Dtest=AccountApiTest,TransactionCommandApiTest,TransactionQueryApiTest`
Expected: PASS

- [ ] **Step 6: Commit**

```bash
git add homework-1/src/main/java/com/setu/banking/transaction/AccountBalanceResponse.java homework-1/src/main/java/com/setu/banking/transaction/AccountSummaryResponse.java homework-1/src/main/java/com/setu/banking/transaction/TransactionController.java homework-1/src/main/java/com/setu/banking/transaction/TransactionService.java homework-1/src/test/java/com/setu/banking/transaction/AccountApiTest.java homework-1/src/test/java/com/setu/banking/transaction/TransactionCommandApiTest.java
git commit -m "feat: isolate account balances by currency"
```

### Task 4: Document The Accepted Simplifications And UTC Decision

**Files:**
- Modify: `homework-1/README.md`
- Test: `homework-1/README.md`

- [ ] **Step 1: Add architecture notes covering the business decisions**

```markdown
- Transfers between the same account are rejected because they do not represent a meaningful movement of funds.
- Account balances and summary totals are derived per currency; amounts are never converted or mixed across currencies.
- Overdrafts are intentionally allowed as a homework simplification; the API does not model credit limits or sufficient-funds checks.
- Date filtering and timestamps are interpreted in UTC as an explicit scope decision; timezone-aware account statements are out of scope.
```

- [ ] **Step 2: Update endpoint descriptions to match the new response shape**

```markdown
| `GET` | `/accounts/{accountId}/balance` | Get account balances grouped by currency |
```

- [ ] **Step 3: Commit documentation together with the currency-ledger changes if not already committed**

```bash
git add homework-1/README.md
git commit -m "docs: describe balance and summary business rules"
```

### Task 5: Final Verification

**Files:**
- Review: `homework-1/src/main/java/com/setu/banking/transaction/TransactionValidator.java`
- Review: `homework-1/src/main/java/com/setu/banking/transaction/TransactionService.java`
- Review: `homework-1/README.md`

- [ ] **Step 1: Run the full homework-1 test suite**

Run: `mvn test`
Expected: PASS with zero failures and zero errors

- [ ] **Step 2: Review the diff and commit history**

Run: `git status --short`
Expected: clean working tree or only intentional uncommitted plan-doc changes

Run: `git log --oneline -2`
Expected: one commit for same-account rejection and one commit for currency-ledger changes

- [ ] **Step 3: Summarize results with evidence**

Report:
- Which tests were run
- Which files changed
- The exact commit boundaries that were requested

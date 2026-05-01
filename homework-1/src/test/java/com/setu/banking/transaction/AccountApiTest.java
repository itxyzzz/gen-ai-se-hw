package com.setu.banking.transaction;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountApiTest extends ApiIntegrationTestSupport {
    @Test
    void newAccountHasZeroBalanceBeforeAnyTransaction() throws Exception {
        mockMvc.perform(get("/accounts/{accountId}/balance", "ACC-12345"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accountId").value("ACC-12345"))
            .andExpect(jsonPath("$.balances").isMap())
            .andExpect(jsonPath("$.balances").isEmpty());
    }

    @Test
    void rejectsInvalidBalanceAccountId() throws Exception {
        mockMvc.perform(get("/accounts/{accountId}/balance", "BADID"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation failed"))
            .andExpect(jsonPath("$.details[0].field").value("accountId"))
            .andExpect(jsonPath("$.details[0].message").value("Account must match format ACC-XXXXX"));
    }

    @Test
    void balanceSeparatesCurrenciesForTheSameAccount() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"toAccount":"ACC-77777","amount":100.00,"currency":"USD","type":"deposit"}
                    """))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"toAccount":"ACC-77777","amount":50.00,"currency":"EUR","type":"deposit"}
                    """))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"fromAccount":"ACC-77777","amount":10.00,"currency":"USD","type":"withdrawal"}
                    """))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/accounts/{accountId}/balance", "ACC-77777"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accountId").value("ACC-77777"))
            .andExpect(jsonPath("$.balances.USD").value(90.00))
            .andExpect(jsonPath("$.balances.EUR").value(50.00));
    }

    @Test
    void returnsAccountTransactionSummaryByCurrency() throws Exception {
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

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"fromAccount":"ACC-77777","toAccount":"ACC-88888","amount":10.00,"currency":"EUR","type":"transfer"}
                    """))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"fromAccount":"ACC-99999","toAccount":"ACC-77777","amount":15.00,"currency":"USD","type":"transfer"}
                    """))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/accounts/{accountId}/summary", "ACC-77777"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accountId").value("ACC-77777"))
            .andExpect(jsonPath("$.totalDeposits.USD").value(100.00))
            .andExpect(jsonPath("$.totalDeposits.EUR").value(25.00))
            .andExpect(jsonPath("$.totalWithdrawals.USD").value(40.00))
            .andExpect(jsonPath("$.totalIncomingTransfers.USD").value(15.00))
            .andExpect(jsonPath("$.totalOutgoingTransfers.EUR").value(10.00))
            .andExpect(jsonPath("$.transactionCount").value(5))
            .andExpect(jsonPath("$.mostRecentTransactionDate").exists());
    }

    @Test
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
            .andExpect(jsonPath("$.totalDeposits.USD").value(20.00))
            .andExpect(jsonPath("$.mostRecentTransactionDate").value("2026-04-23T10:15:30Z"));
    }

    @Test
    void rejectsInvalidSummaryAccountId() throws Exception {
        mockMvc.perform(get("/accounts/{accountId}/summary", "BADID"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation failed"))
            .andExpect(jsonPath("$.details[0].field").value("accountId"))
            .andExpect(jsonPath("$.details[0].message").value("Account must match format ACC-XXXXX"));
    }
}

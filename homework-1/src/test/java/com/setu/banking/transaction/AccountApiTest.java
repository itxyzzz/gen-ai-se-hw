package com.setu.banking.transaction;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

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
            .andExpect(jsonPath("$.balance").value(0))
            .andExpect(jsonPath("$.currency").value("USD"));
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
    void returnsAccountTransactionSummary() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"toAccount":"ACC-77777","amount":100.00,"currency":"USD","type":"deposit"}
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
            .andExpect(jsonPath("$.accountId").value("ACC-77777"))
            .andExpect(jsonPath("$.totalDeposits").value(100.00))
            .andExpect(jsonPath("$.totalWithdrawals").value(40.00))
            .andExpect(jsonPath("$.transactionCount").value(2))
            .andExpect(jsonPath("$.mostRecentTransactionDate").exists());
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

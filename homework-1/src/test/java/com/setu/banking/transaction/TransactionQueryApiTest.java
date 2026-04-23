package com.setu.banking.transaction;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransactionQueryApiTest extends ApiIntegrationTestSupport {
    @Test
    void returnsNotFoundForMissingTransaction() throws Exception {
        mockMvc.perform(get("/transactions/{id}", "missing-id"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Not found"))
            .andExpect(jsonPath("$.message").value("Transaction not found"));
    }

    @Test
    void filtersTransactionsByAccountTypeAndDateRange() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"fromAccount":"ACC-11111","toAccount":"ACC-22222","amount":50.00,"currency":"USD","type":"transfer"}
                    """))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"toAccount":"ACC-22222","amount":25.00,"currency":"USD","type":"deposit"}
                    """))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/transactions")
                .param("accountId", "ACC-11111")
                .param("type", "transfer")
                .param("from", "2024-01-01")
                .param("to", "2099-12-31"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].fromAccount").value("ACC-11111"))
            .andExpect(jsonPath("$[0].type").value("transfer"));
    }

    @Test
    void rejectsInvalidFilterAccountId() throws Exception {
        mockMvc.perform(get("/transactions").param("accountId", "BAD"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation failed"))
            .andExpect(jsonPath("$.details[0].field").value("accountId"))
            .andExpect(jsonPath("$.details[0].message").value("Account must match format ACC-XXXXX"));
    }

    @Test
    void rejectsInvalidFilterType() throws Exception {
        mockMvc.perform(get("/transactions").param("type", "refund"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation failed"))
            .andExpect(jsonPath("$.details[0].field").value("type"))
            .andExpect(jsonPath("$.details[0].message").value("Type must be deposit, withdrawal, or transfer"));
    }

    @Test
    void rejectsInvalidFilterDateFormat() throws Exception {
        mockMvc.perform(get("/transactions").param("from", "01-31-2024"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation failed"))
            .andExpect(jsonPath("$.details[0].field").value("from"))
            .andExpect(jsonPath("$.details[0].message").value("Date must use ISO format yyyy-MM-dd"));
    }

    @Test
    void rejectsFilterDateRangeWhereFromIsAfterTo() throws Exception {
        mockMvc.perform(get("/transactions")
                .param("from", "2024-02-01")
                .param("to", "2024-01-01"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation failed"))
            .andExpect(jsonPath("$.details[0].field").value("from"))
            .andExpect(jsonPath("$.details[0].message").value("From date must be on or before to date"));
    }
}

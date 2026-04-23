package com.setu.banking.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository repository;

    @BeforeEach
    void clearRepository() {
        repository.clear();
    }

    @Test
    void newAccountHasZeroBalanceBeforeAnyTransaction() throws Exception {
        mockMvc.perform(get("/accounts/{accountId}/balance", "ACC-12345"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accountId").value("ACC-12345"))
            .andExpect(jsonPath("$.balance").value(0))
            .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    void transferUpdatesBalancesForBothAccounts() throws Exception {
        String body = """
            {
              "fromAccount": "ACC-12345",
              "toAccount": "ACC-67890",
              "amount": 100.50,
              "currency": "USD",
              "type": "transfer"
            }
            """;

        String response = mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", not(blankOrNullString())))
            .andExpect(jsonPath("$.fromAccount").value("ACC-12345"))
            .andExpect(jsonPath("$.toAccount").value("ACC-67890"))
            .andExpect(jsonPath("$.amount").value(100.50))
            .andExpect(jsonPath("$.currency").value("USD"))
            .andExpect(jsonPath("$.type").value("transfer"))
            .andExpect(jsonPath("$.status").value("completed"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        String id = com.jayway.jsonpath.JsonPath.read(response, "$.id");

        mockMvc.perform(get("/transactions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(get("/transactions/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id));

        mockMvc.perform(get("/accounts/{accountId}/balance", "ACC-12345"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accountId").value("ACC-12345"))
            .andExpect(jsonPath("$.balance").value(-100.50))
            .andExpect(jsonPath("$.currency").value("USD"));

        mockMvc.perform(get("/accounts/{accountId}/balance", "ACC-67890"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accountId").value("ACC-67890"))
            .andExpect(jsonPath("$.balance").value(100.50))
            .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    void depositAndWithdrawalApplyExpectedBalanceChanges() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "fromAccount": "ACC-00000",
                      "toAccount": "ACC-22222",
                      "amount": 250.00,
                      "currency": "USD",
                      "type": "deposit"
                    }
                    """))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/accounts/{accountId}/balance", "ACC-22222"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.balance").value(250.00));

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "fromAccount": "ACC-22222",
                      "toAccount": "ACC-00000",
                      "amount": 40.25,
                      "currency": "USD",
                      "type": "withdrawal"
                    }
                    """))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/accounts/{accountId}/balance", "ACC-22222"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.balance").value(209.75));
    }

    @Test
    void returnsNotFoundForMissingTransaction() throws Exception {
        mockMvc.perform(get("/transactions/{id}", "missing-id"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Not found"))
            .andExpect(jsonPath("$.message").value("Transaction not found"));
    }

    @ParameterizedTest
    @MethodSource("missingRequiredFieldRequests")
    void rejectsRequestsWithMissingRequiredFields(String missingField, String body) throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation failed"))
            .andExpect(jsonPath("$.details", hasSize(1)))
            .andExpect(jsonPath("$.details[0].field").value(missingField))
            .andExpect(jsonPath("$.details[0].message").value("Field is required"));
    }

    static Stream<Arguments> missingRequiredFieldRequests() {
        return Stream.of(
            Arguments.of("fromAccount", """
                {"toAccount":"ACC-67890","amount":100.50,"currency":"USD","type":"transfer"}
                """),
            Arguments.of("toAccount", """
                {"fromAccount":"ACC-12345","amount":100.50,"currency":"USD","type":"transfer"}
                """),
            Arguments.of("amount", """
                {"fromAccount":"ACC-12345","toAccount":"ACC-67890","currency":"USD","type":"transfer"}
                """),
            Arguments.of("currency", """
                {"fromAccount":"ACC-12345","toAccount":"ACC-67890","amount":100.50,"type":"transfer"}
                """),
            Arguments.of("type", """
                {"fromAccount":"ACC-12345","toAccount":"ACC-67890","amount":100.50,"currency":"USD"}
                """)
        );
    }

    @Test
    void rejectsMalformedJsonWithoutLeakingFrameworkDetails() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"fromAccount":"ACC-12345";
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Malformed request"))
            .andExpect(jsonPath("$.message").value("Request body must be valid JSON"));
    }

    @Test
    void rejectsMissingRequestBodyWithoutLeakingFrameworkDetails() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Malformed request"))
            .andExpect(jsonPath("$.message").value("Request body must be valid JSON"));
    }

    @Test
    void rejectsWrongJsonValueTypeWithoutReturningFiveHundred() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "fromAccount": "ACC-12345",
                      "toAccount": "ACC-67890",
                      "amount": {"value": 100.50},
                      "currency": "USD",
                      "type": "transfer"
                    }
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Malformed request"))
            .andExpect(jsonPath("$.message").value("Request body has an invalid field type"));
    }

    @Test
    void rejectsUnsupportedContentTypeWithSanitizedResponse() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.TEXT_PLAIN)
                .content("fromAccount=ACC-12345"))
            .andExpect(status().isUnsupportedMediaType())
            .andExpect(jsonPath("$.error").value("Unsupported media type"))
            .andExpect(jsonPath("$.message").value("Use application/json"));
    }

    @Test
    void rejectsInvalidTransactionRequestWithDetails() throws Exception {
        String body = """
            {
              "fromAccount": "BAD",
              "toAccount": "ACC-67890",
              "amount": -10,
              "currency": "DOGE",
              "type": "transfer"
            }
            """;

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation failed"))
            .andExpect(jsonPath("$.details", hasSize(3)))
            .andExpect(jsonPath("$.details[0].field").exists())
            .andExpect(jsonPath("$.details[0].message").exists());
    }

    @Test
    void rejectsAmountWithMoreThanTwoDecimalPlaces() throws Exception {
        String body = """
            {
              "fromAccount": "ACC-12345",
              "toAccount": "ACC-67890",
              "amount": 10.999,
              "currency": "EUR",
              "type": "deposit"
            }
            """;

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.details[0].field").value("amount"));
    }

    @Test
    void rejectsInvalidTransactionType() throws Exception {
        String body = """
            {
              "fromAccount": "ACC-12345",
              "toAccount": "ACC-67890",
              "amount": 10.00,
              "currency": "EUR",
              "type": "refund"
            }
            """;

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.details[0].field").value("type"))
            .andExpect(jsonPath("$.details[0].message").value("Type must be deposit, withdrawal, or transfer"));
    }

    @Test
    void rejectsUnexpectedCharactersInFieldValuesWithoutReturningFiveHundred() throws Exception {
        String body = """
            {
              "fromAccount": "ACC-12345;",
              "toAccount": "ACC-67890",
              "amount": 10.00,
              "currency": "USD;",
              "type": "transfer;"
            }
            """;

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation failed"))
            .andExpect(jsonPath("$.details", hasSize(3)));
    }

    @Test
    void rejectsOverlyLongStringValues() throws Exception {
        String body = """
            {
              "fromAccount": "ACC-12345678901234567890",
              "toAccount": "ACC-67890",
              "amount": 10.00,
              "currency": "USDDDD",
              "type": "transfertransfertransfer"
            }
            """;

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation failed"))
            .andExpect(jsonPath("$.details", hasSize(3)));
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
    void rejectedTransactionDoesNotChangeTransactionHistoryOrBalances() throws Exception {
        String body = """
            {
              "fromAccount": "ACC-12345",
              "toAccount": "ACC-67890",
              "amount": 0,
              "currency": "USD",
              "type": "transfer"
            }
            """;

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());

        mockMvc.perform(get("/transactions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        mockMvc.perform(get("/accounts/{accountId}/balance", "ACC-12345"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.balance").value(0));

        mockMvc.perform(get("/accounts/{accountId}/balance", "ACC-67890"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.balance").value(0));
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
                    {"fromAccount":"ACC-33333","toAccount":"ACC-22222","amount":25.00,"currency":"USD","type":"deposit"}
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

    @Test
    void returnsAccountTransactionSummary() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"fromAccount":"ACC-90000","toAccount":"ACC-77777","amount":100.00,"currency":"USD","type":"deposit"}
                    """))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"fromAccount":"ACC-77777","toAccount":"ACC-90000","amount":40.00,"currency":"USD","type":"withdrawal"}
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

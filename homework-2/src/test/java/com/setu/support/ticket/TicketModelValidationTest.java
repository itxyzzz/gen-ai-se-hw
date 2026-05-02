package com.setu.support.ticket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketModelValidationTest extends ApiIntegrationTestSupport {
    @ParameterizedTest
    @MethodSource("missingRequiredFieldRequests")
    void rejectsMissingRequiredFields(String field, String body) throws Exception {
        mockMvc.perform(post("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation failed"))
            .andExpect(jsonPath("$.details[*].field", hasItem(field)));
    }

    @Test
    void rejectsInvalidEmailLengthEnumsAndMetadata() throws Exception {
        String body = """
            {
              "customer_id": "CUST-1001",
              "customer_email": "bad-email",
              "customer_name": "Ada Lovelace",
              "subject": "",
              "description": "short",
              "category": "login_problem",
              "priority": "immediate",
              "status": "started",
              "metadata": {
                "source": "fax",
                "device_type": "watch"
              }
            }
            """;

        mockMvc.perform(post("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.details[*].field", hasItem("customer_email")))
            .andExpect(jsonPath("$.details[*].field", hasItem("subject")))
            .andExpect(jsonPath("$.details[*].field", hasItem("description")))
            .andExpect(jsonPath("$.details[*].field", hasItem("category")))
            .andExpect(jsonPath("$.details[*].field", hasItem("priority")))
            .andExpect(jsonPath("$.details[*].field", hasItem("status")))
            .andExpect(jsonPath("$.details[*].field", hasItem("metadata.source")))
            .andExpect(jsonPath("$.details[*].field", hasItem("metadata.device_type")));
    }

    @Test
    void rejectsResolvedAtForNonTerminalStatusWithoutMutatingRepository() throws Exception {
        mockMvc.perform(post("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validTicketJson().replace("\"metadata\":", "\"resolved_at\": \"2026-05-02T10:00:00Z\", \"metadata\":")))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.details[*].field", hasItem("resolved_at")));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/tickets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void rejectsMissingRequestBody() throws Exception {
        mockMvc.perform(post("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Malformed request"));
    }

    static Stream<Arguments> missingRequiredFieldRequests() {
        return Stream.of(
            Arguments.of("customer_id", """
                {"customer_email":"ada@example.com","customer_name":"Ada","subject":"Subject","description":"Long enough description","category":"other","priority":"medium","status":"new","metadata":{"source":"api"}}
                """),
            Arguments.of("customer_email", """
                {"customer_id":"CUST-1","customer_name":"Ada","subject":"Subject","description":"Long enough description","category":"other","priority":"medium","status":"new","metadata":{"source":"api"}}
                """),
            Arguments.of("customer_name", """
                {"customer_id":"CUST-1","customer_email":"ada@example.com","subject":"Subject","description":"Long enough description","category":"other","priority":"medium","status":"new","metadata":{"source":"api"}}
                """),
            Arguments.of("subject", """
                {"customer_id":"CUST-1","customer_email":"ada@example.com","customer_name":"Ada","description":"Long enough description","category":"other","priority":"medium","status":"new","metadata":{"source":"api"}}
                """),
            Arguments.of("description", """
                {"customer_id":"CUST-1","customer_email":"ada@example.com","customer_name":"Ada","subject":"Subject","category":"other","priority":"medium","status":"new","metadata":{"source":"api"}}
                """),
            Arguments.of("metadata.source", """
                {"customer_id":"CUST-1","customer_email":"ada@example.com","customer_name":"Ada","subject":"Subject","description":"Long enough description","category":"other","priority":"medium","status":"new","metadata":{}}
                """)
        );
    }
}

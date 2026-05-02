package com.setu.support.ticket;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketImportJsonTest extends ApiIntegrationTestSupport {
    @Test
    void importsValidJsonArray() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("tickets.json", MediaType.APPLICATION_JSON_VALUE, validJsonArray())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_records").value(2))
            .andExpect(jsonPath("$.successful").value(2));
    }

    @Test
    void returnsJsonPartialFailures() throws Exception {
        String json = validJsonArray().replace("\"medium\"", "\"impossible\"");
        mockMvc.perform(multipart("/tickets/import")
                .file(file("tickets.json", MediaType.APPLICATION_JSON_VALUE, json)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.successful").value(1))
            .andExpect(jsonPath("$.failed").value(1))
            .andExpect(jsonPath("$.errors[0].field").value("priority"));
    }

    @Test
    void rejectsMalformedJsonImport() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("tickets.json", MediaType.APPLICATION_JSON_VALUE, "[{\"customer_id\":\"CUST-1\";]")))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("JSON import must be a valid array of ticket objects"));
    }

    @Test
    void importsEmptyJsonArray() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("tickets.json", MediaType.APPLICATION_JSON_VALUE, "[]")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_records").value(0))
            .andExpect(jsonPath("$.successful").value(0));
    }

    static String validJsonArray() {
        return """
            [
              {
                "customer_id": "CUST-10",
                "customer_email": "alan@example.com",
                "customer_name": "Alan Turing",
                "subject": "API error during checkout",
                "description": "The API returns an error when I try to complete checkout.",
                "category": "technical_issue",
                "priority": "urgent",
                "status": "new",
                "tags": ["api", "checkout"],
                "metadata": {"source": "api", "browser": "Chrome", "device_type": "desktop"},
                "unexpected_field": "ignored"
              },
              {
                "customer_id": "CUST-11",
                "customer_email": "katherine@example.com",
                "customer_name": "Katherine Johnson",
                "subject": "Feature idea",
                "description": "Please add better export options to the dashboard.",
                "category": "feature_request",
                "priority": "medium",
                "status": "new",
                "tags": ["export"],
                "metadata": {"source": "chat", "device_type": "mobile"}
              }
            ]
            """;
    }
}

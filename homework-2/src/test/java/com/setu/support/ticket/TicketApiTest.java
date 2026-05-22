package com.setu.support.ticket;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketApiTest extends ApiIntegrationTestSupport {
    @Test
    void createsTicketWithServerManagedFields() throws Exception {
        mockMvc.perform(post("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validTicketJson()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", not(blankOrNullString())))
            .andExpect(jsonPath("$.customer_id").value("CUST-1001"))
            .andExpect(jsonPath("$.category").value("account_access"))
            .andExpect(jsonPath("$.priority").value("high"))
            .andExpect(jsonPath("$.status").value("new"))
            .andExpect(jsonPath("$.created_at", not(blankOrNullString())))
            .andExpect(jsonPath("$.updated_at", not(blankOrNullString())))
            .andExpect(jsonPath("$.resolved_at").doesNotExist())
            .andExpect(jsonPath("$.metadata.source").value("web_form"))
            .andExpect(jsonPath("$.metadata.device_type").value("desktop"));
    }

    @Test
    void listsAndFiltersTickets() throws Exception {
        createTicket();
        mockMvc.perform(post("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validTicketJson()
                    .replace("CUST-1001", "CUST-2002")
                    .replace("ada@example.com", "grace@example.com")
                    .replace("account_access", "billing_question")
                    .replace("high", "low")
                    .replace("web_form", "email")
                    .replace("[\"login\", \"password\"]", "[\"billing\"]")))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/tickets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get("/tickets")
                .param("category", "billing_question")
                .param("priority", "low")
                .param("source", "email"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].customer_id").value("CUST-2002"));

        mockMvc.perform(get("/tickets").param("tag", "password"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].customer_id").value("CUST-1001"));
    }

    @Test
    void getsUpdatesAndDeletesTicket() throws Exception {
        String id = createTicket();

        mockMvc.perform(get("/tickets/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id));

        mockMvc.perform(put("/tickets/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validTicketJson()
                    .replace("\"status\": \"new\"", "\"status\": \"resolved\"")
                    .replace("\"assigned_to\": \"support-agent-1\"", "\"assigned_to\": \"support-agent-2\"")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.status").value("resolved"))
            .andExpect(jsonPath("$.resolved_at", not(blankOrNullString())))
            .andExpect(jsonPath("$.assigned_to").value("support-agent-2"));

        mockMvc.perform(delete("/tickets/{id}", id))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/tickets/{id}", id))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Not found"));
    }

    @Test
    void rejectsMalformedJsonAndUnsupportedMediaType() throws Exception {
        mockMvc.perform(post("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"customer_id\":\"CUST-1\";"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Malformed request"));

        mockMvc.perform(post("/tickets")
                .contentType(MediaType.TEXT_PLAIN)
                .content("customer_id=CUST-1"))
            .andExpect(status().isUnsupportedMediaType())
            .andExpect(jsonPath("$.error").value("Unsupported media type"));
    }

    @Test
    void deleteUnknownTicketReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/tickets/{id}", "11111111-1111-1111-1111-111111111111"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Ticket not found"));
    }

    @Test
    void listTicketsStartsEmpty() throws Exception {
        mockMvc.perform(get("/tickets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getUnknownTicketReturnsNotFound() throws Exception {
        mockMvc.perform(get("/tickets/{id}", "11111111-1111-1111-1111-111111111111"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Not found"))
            .andExpect(jsonPath("$.message").value("Ticket not found"));
    }

    @Test
    void updateUnknownTicketReturnsNotFound() throws Exception {
        mockMvc.perform(put("/tickets/{id}", "11111111-1111-1111-1111-111111111111")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validTicketJson()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Ticket not found"));
    }

    @Test
    void rejectsValidationErrorsOnCreate() throws Exception {
        mockMvc.perform(post("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validTicketJson()
                    .replace("ada@example.com", "not-an-email")
                    .replace("\"subject\": \"Cannot access my account\"", "\"subject\": \"\"")))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation failed"))
            .andExpect(jsonPath("$.details[*].field", hasItem("customer_email")))
            .andExpect(jsonPath("$.details[*].field", hasItem("subject")));
    }

    @Test
    void filtersTicketsByStatusCustomerEmailAndAssignee() throws Exception {
        createTicket();
        mockMvc.perform(post("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validTicketJson()
                    .replace("CUST-1001", "CUST-3003")
                    .replace("ada@example.com", "hopper@example.com")
                    .replace("\"status\": \"new\"", "\"status\": \"waiting_customer\"")
                    .replace("support-agent-1", "support-agent-3")))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/tickets")
                .param("status", "waiting_customer")
                .param("customer_email", "hopper@example.com")
                .param("assigned_to", "support-agent-3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].customer_id").value("CUST-3003"));
    }

    @Test
    void rejectsMissingImportFile() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart("/tickets/import"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Malformed import"))
            .andExpect(jsonPath("$.message").value("Import file is required"));
    }
}

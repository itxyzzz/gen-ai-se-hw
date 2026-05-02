package com.setu.support.ticket;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketIntegrationTest extends ApiIntegrationTestSupport {
    @Test
    void completeTicketLifecycleWorkflow() throws Exception {
        String id = createTicket();

        mockMvc.perform(get("/tickets").param("customer_id", "CUST-1001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(delete("/tickets/{id}", id))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/tickets").param("customer_id", "CUST-1001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void importThenFilterWorkflow() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("tickets.csv", "text/csv", TicketImportCsvTest.validCsv())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.successful").value(2));

        mockMvc.perform(get("/tickets")
                .param("category", "billing_question")
                .param("priority", "medium")
                .param("assigned_to", "agent-2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].customer_email").value("grace@example.com"));
    }
}

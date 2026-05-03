package com.setu.support.ticket;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketIntegrationTest extends ApiIntegrationTestSupport {
    @Test
    void completeTicketLifecycleWorkflow() throws Exception {
        String id = createTicket();

        mockMvc.perform(get("/tickets/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.status").value("new"));

        mockMvc.perform(put("/tickets/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validTicketJson()
                    .replace("\"status\": \"new\"", "\"status\": \"resolved\"")
                    .replace("\"assigned_to\": \"support-agent-1\"", "\"assigned_to\": \"support-agent-9\"")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.status").value("resolved"))
            .andExpect(jsonPath("$.resolved_at", not(blankOrNullString())))
            .andExpect(jsonPath("$.assigned_to").value("support-agent-9"));

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

    @Test
    void bulkImportWithAutoClassificationStoresDecisionEvidence() throws Exception {
        String csv = """
            customer_id,customer_email,customer_name,subject,description,category,priority,status,resolved_at,assigned_to,tags,metadata_source,metadata_browser,metadata_device_type
            CUST-AUTO-10,auto10@example.com,Auto Ten,Production down,Production down security issue is critical and blocking checkout,,,new,,agent-1,security,api,,desktop
            CUST-AUTO-11,auto11@example.com,Auto Eleven,Refund needed,I need a refund for an invoice charge,,,new,,agent-2,billing,api,,desktop
            """;

        mockMvc.perform(multipart("/tickets/import")
                .file(file("auto-classified.csv", "text/csv", csv)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.successful").value(2));

        mockMvc.perform(get("/tickets").param("priority", "urgent"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].category").value("other"))
            .andExpect(jsonPath("$[0].classification_confidence").isNumber())
            .andExpect(jsonPath("$[0].classification_reasoning", not(blankOrNullString())))
            .andExpect(jsonPath("$[0].classification_keywords", hasSize(4)))
            .andExpect(jsonPath("$[0].manual_override_applied").value(false));
    }

    @Test
    void handlesTwentyConcurrentCreateRequests() throws Exception {
        var executor = Executors.newFixedThreadPool(6);
        try {
            List<Callable<String>> tasks = new ArrayList<>();
            for (int index = 1; index <= 20; index++) {
                int ticketNumber = index;
                tasks.add(() -> {
                    String body = validTicketJson()
                        .replace("CUST-1001", "CUST-CONCURRENT-%02d".formatted(ticketNumber))
                        .replace("ada@example.com", "concurrent%02d@example.com".formatted(ticketNumber));
                    return mockMvc.perform(post("/tickets")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
                });
            }

            var futures = executor.invokeAll(tasks, 10, TimeUnit.SECONDS);
            for (var future : futures) {
                String response = future.get(1, TimeUnit.SECONDS);
                String id = JsonPath.read(response, "$.id");
                org.assertj.core.api.Assertions.assertThat(id).isNotBlank();
            }
        } finally {
            executor.shutdownNow();
        }

        mockMvc.perform(get("/tickets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(20)))
            .andExpect(jsonPath("$[*].category", everyItem(org.hamcrest.Matchers.is("account_access"))));
    }

    @Test
    void partialImportFailurePersistsOnlyValidRecords() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("partial.csv", "text/csv", TicketImportCsvTest.validCsv()
                    + "CUST-BAD,bad-email,Bad Customer,Bad import row,This description is long enough,other,medium,new,,agent-3,bad,api,,desktop\n")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_records").value(3))
            .andExpect(jsonPath("$.successful").value(2))
            .andExpect(jsonPath("$.failed").value(1));

        mockMvc.perform(get("/tickets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get("/tickets").param("customer_id", "CUST-BAD"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }
}

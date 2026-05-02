package com.setu.support.ticket;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketSampleDataTest extends ApiIntegrationTestSupport {
    @Test
    void demoSampleFilesImportWithExpectedRecordCounts() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("sample_tickets.csv", "text/csv", readFixture("sample_tickets.csv"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_records").value(50))
            .andExpect(jsonPath("$.successful").value(50));

        repository.clear();

        mockMvc.perform(multipart("/tickets/import")
                .file(file("sample_tickets.json", "application/json", readFixture("sample_tickets.json"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_records").value(20))
            .andExpect(jsonPath("$.successful").value(20));

        repository.clear();

        mockMvc.perform(multipart("/tickets/import")
                .file(file("sample_tickets.xml", "application/xml", readFixture("sample_tickets.xml"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_records").value(30))
            .andExpect(jsonPath("$.successful").value(30));
    }

    @Test
    void demoInvalidFilesReturnPartialFailureSummaries() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("invalid_tickets.csv", "text/csv", readFixture("invalid_tickets.csv"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.failed").value(2));

        mockMvc.perform(multipart("/tickets/import")
                .file(file("invalid_tickets.json", "application/json", readFixture("invalid_tickets.json"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.failed").value(2));

        mockMvc.perform(multipart("/tickets/import")
                .file(file("invalid_tickets.xml", "application/xml", readFixture("invalid_tickets.xml"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.failed").value(2));
    }

    @Test
    void demoClassificationFileImportsAndAutoClassifiesRecords() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("classification_tickets.csv", "text/csv", readFixture("classification_tickets.csv"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_records").value(5))
            .andExpect(jsonPath("$.successful").value(5));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/tickets")
                .param("category", "bug_report"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].priority").value("medium"));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/tickets")
                .param("priority", "urgent"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].classification_keywords").isArray());
    }
}

package com.setu.support.ticket;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketSampleDataTest extends ApiIntegrationTestSupport {
    @Test
    void demoSampleFilesImportWithExpectedRecordCounts() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("sample_tickets.csv", "text/csv", readDemo("sample_tickets.csv"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_records").value(50))
            .andExpect(jsonPath("$.successful").value(50));

        repository.clear();

        mockMvc.perform(multipart("/tickets/import")
                .file(file("sample_tickets.json", "application/json", readDemo("sample_tickets.json"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_records").value(20))
            .andExpect(jsonPath("$.successful").value(20));

        repository.clear();

        mockMvc.perform(multipart("/tickets/import")
                .file(file("sample_tickets.xml", "application/xml", readDemo("sample_tickets.xml"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_records").value(30))
            .andExpect(jsonPath("$.successful").value(30));
    }

    @Test
    void demoInvalidFilesReturnPartialFailureSummaries() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("invalid_tickets.csv", "text/csv", readDemo("invalid_tickets.csv"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.failed").value(2));

        mockMvc.perform(multipart("/tickets/import")
                .file(file("invalid_tickets.json", "application/json", readDemo("invalid_tickets.json"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.failed").value(2));

        mockMvc.perform(multipart("/tickets/import")
                .file(file("invalid_tickets.xml", "application/xml", readDemo("invalid_tickets.xml"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.failed").value(2));
    }

    private String readDemo(String name) throws Exception {
        return Files.readString(Path.of("demo", name));
    }
}

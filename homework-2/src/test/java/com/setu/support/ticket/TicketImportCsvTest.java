package com.setu.support.ticket;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketImportCsvTest extends ApiIntegrationTestSupport {
    @Test
    void importsValidCsvFile() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("tickets.csv", "text/csv", validCsv())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_records").value(2))
            .andExpect(jsonPath("$.successful").value(2))
            .andExpect(jsonPath("$.failed").value(0))
            .andExpect(jsonPath("$.created_ticket_ids", hasSize(2)));
    }

    @Test
    void returnsPartialCsvFailuresWithRecordDetails() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("tickets.csv", "text/csv", validCsv() + "CUST-3,bad-email,Linus,Invalid email,This description is long enough,other,medium,new,,agent-3,invalid,api,,desktop\n")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_records").value(3))
            .andExpect(jsonPath("$.successful").value(2))
            .andExpect(jsonPath("$.failed").value(1))
            .andExpect(jsonPath("$.errors[0].record").value(3))
            .andExpect(jsonPath("$.errors[0].field").value("customer_email"));
    }

    @Test
    void rejectsMalformedCsvFile() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("broken.csv", "text/csv", "customer_id,customer_email\n\"unterminated")))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Malformed import"));
    }

    @Test
    void supportsExplicitCsvFormatParameter() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("tickets.data", MediaType.TEXT_PLAIN_VALUE, validCsv()))
                .param("format", "csv"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.successful").value(2));
    }

    static String validCsv() {
        return """
            customer_id,customer_email,customer_name,subject,description,category,priority,status,resolved_at,assigned_to,tags,metadata_source,metadata_browser,metadata_device_type
            CUST-1,ada@example.com,Ada Lovelace,Cannot access account,I cannot access my account after a reset,account_access,high,new,,agent-1,login;password,web_form,Firefox,desktop
            CUST-2,grace@example.com,Grace Hopper,Invoice question,I need a copy of my invoice for April,billing_question,medium,waiting_customer,,agent-2,billing,email,Outlook,desktop
            """;
    }
}

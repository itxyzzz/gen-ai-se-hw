package com.setu.support.ticket;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketClassificationApiTest extends ApiIntegrationTestSupport {
    @Test
    void explicitAutoClassifyUpdatesTicketAndReturnsDecisionEvidence() throws Exception {
        String id = createTicket();

        mockMvc.perform(post("/tickets/{id}/auto-classify", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.category").value("account_access"))
            .andExpect(jsonPath("$.priority").value("urgent"))
            .andExpect(jsonPath("$.confidence_score").isNumber())
            .andExpect(jsonPath("$.reasoning", not(blankOrNullString())))
            .andExpect(jsonPath("$.keywords_found", hasItem("can't access")))
            .andExpect(jsonPath("$.suggested_category").value("account_access"))
            .andExpect(jsonPath("$.suggested_priority").value("urgent"))
            .andExpect(jsonPath("$.manual_override_applied").value(false));

        mockMvc.perform(get("/tickets/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.priority").value("urgent"))
            .andExpect(jsonPath("$.classification_confidence").isNumber())
            .andExpect(jsonPath("$.classification_reasoning", not(blankOrNullString())))
            .andExpect(jsonPath("$.classification_keywords", hasItem("can't access")))
            .andExpect(jsonPath("$.classified_at", not(blankOrNullString())))
            .andExpect(jsonPath("$.manual_override_applied").value(false));

        assertThat(decisionLog.findAll()).hasSize(2);
        assertThat(decisionLog.findAll().get(1).trigger()).isEqualTo("explicit_endpoint");
    }

    @Test
    void explicitAutoClassifyUnknownTicketReturnsNotFound() throws Exception {
        mockMvc.perform(post("/tickets/{id}/auto-classify", "11111111-1111-1111-1111-111111111111"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Ticket not found"));
    }

    @Test
    void createWithoutCategoryOrPriorityAutoClassifiesByDefault() throws Exception {
        mockMvc.perform(post("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validTicketJson()
                    .replace("  \"category\": \"account_access\",\n", "")
                    .replace("  \"priority\": \"high\",\n", "")))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.category").value("account_access"))
            .andExpect(jsonPath("$.priority").value("urgent"))
            .andExpect(jsonPath("$.suggested_category").value("account_access"))
            .andExpect(jsonPath("$.suggested_priority").value("urgent"))
            .andExpect(jsonPath("$.manual_override_applied").value(false));
    }

    @Test
    void createManualInputWinsAndStoresClassifierSuggestion() throws Exception {
        mockMvc.perform(post("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validTicketJson()
                    .replace("\"category\": \"account_access\"", "\"category\": \"billing_question\"")
                    .replace("\"priority\": \"high\"", "\"priority\": \"low\"")))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.category").value("billing_question"))
            .andExpect(jsonPath("$.priority").value("low"))
            .andExpect(jsonPath("$.suggested_category").value("account_access"))
            .andExpect(jsonPath("$.suggested_priority").value("urgent"))
            .andExpect(jsonPath("$.manual_override_applied").value(true));

        assertThat(decisionLog.findAll()).hasSize(1);
        assertThat(decisionLog.findAll().get(0).trigger()).isEqualTo("create");
        assertThat(decisionLog.findAll().get(0).manualOverrideApplied()).isTrue();
    }

    @Test
    void importWithoutCategoryOrPriorityAutoClassifiesEachRecordByDefault() throws Exception {
        String csv = """
            customer_id,customer_email,customer_name,subject,description,category,priority,status,resolved_at,assigned_to,tags,metadata_source,metadata_browser,metadata_device_type
            CUST-AUTO-1,auto1@example.com,Auto One,Invoice refund,I need a refund for my invoice charge,,,new,,agent-1,billing,api,,desktop
            CUST-AUTO-2,auto2@example.com,Auto Two,Feature idea,Minor cosmetic suggestion to improve settings,,,new,,agent-2,suggestion,api,,desktop
            """;

        mockMvc.perform(multipart("/tickets/import")
                .file(file("auto.csv", "text/csv", csv)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.successful").value(2));

        mockMvc.perform(get("/tickets").param("category", "billing_question"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].priority").value("medium"));

        mockMvc.perform(get("/tickets").param("category", "feature_request"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].priority").value("low"));
    }

    @Test
    void importManualInputWinsAndStoresClassifierSuggestion() throws Exception {
        String csv = """
            customer_id,customer_email,customer_name,subject,description,category,priority,status,resolved_at,assigned_to,tags,metadata_source,metadata_browser,metadata_device_type
            CUST-OVERRIDE-1,override@example.com,Override Customer,Cannot access account,I can't access my account because password reset failed,billing_question,low,new,,agent-1,login,api,,desktop
            """;

        mockMvc.perform(multipart("/tickets/import")
                .file(file("override.csv", "text/csv", csv)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.successful").value(1));

        mockMvc.perform(get("/tickets").param("customer_id", "CUST-OVERRIDE-1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].category").value("billing_question"))
            .andExpect(jsonPath("$[0].priority").value("low"))
            .andExpect(jsonPath("$[0].suggested_category").value("account_access"))
            .andExpect(jsonPath("$[0].suggested_priority").value("urgent"))
            .andExpect(jsonPath("$[0].manual_override_applied").value(true));
    }

    @Test
    void updateCategoryOrPriorityMarksManualOverrideEvidence() throws Exception {
        String id = createTicket();

        mockMvc.perform(put("/tickets/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validTicketJson()
                    .replace("\"category\": \"account_access\"", "\"category\": \"billing_question\"")
                    .replace("\"priority\": \"high\"", "\"priority\": \"low\"")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.category").value("billing_question"))
            .andExpect(jsonPath("$.priority").value("low"))
            .andExpect(jsonPath("$.manual_override_applied").value(true));
    }
}

package com.setu.support.ticket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
    "tickets.classification.auto-classify-on-create=false",
    "tickets.classification.auto-classify-on-import=false"
})
@AutoConfigureMockMvc
class TicketClassificationDisabledTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TicketRepository repository;

    @Autowired
    TicketClassificationDecisionLog decisionLog;

    @BeforeEach
    void clearState() {
        repository.clear();
        decisionLog.clear();
    }

    @Test
    void createRequiresCategoryAndPriorityWhenCreateAutoClassificationIsDisabled() throws Exception {
        mockMvc.perform(post("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "customer_id": "CUST-DISABLED",
                      "customer_email": "disabled@example.com",
                      "customer_name": "Disabled Customer",
                      "subject": "Cannot access account",
                      "description": "I cannot access my account after password reset.",
                      "status": "new",
                      "metadata": {"source": "api", "device_type": "desktop"}
                    }
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.details[*].field", hasItem("category")))
            .andExpect(jsonPath("$.details[*].field", hasItem("priority")));
    }
}

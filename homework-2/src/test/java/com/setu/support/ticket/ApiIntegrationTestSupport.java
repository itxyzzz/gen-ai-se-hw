package com.setu.support.ticket;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
abstract class ApiIntegrationTestSupport {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TicketRepository repository;

    @BeforeEach
    void clearTickets() {
        repository.clear();
    }

    String validTicketJson() {
        return """
            {
              "customer_id": "CUST-1001",
              "customer_email": "ada@example.com",
              "customer_name": "Ada Lovelace",
              "subject": "Cannot access my account",
              "description": "I cannot access my account after resetting my password.",
              "category": "account_access",
              "priority": "high",
              "status": "new",
              "assigned_to": "support-agent-1",
              "tags": ["login", "password"],
              "metadata": {
                "source": "web_form",
                "browser": "Firefox",
                "device_type": "desktop"
              }
            }
            """;
    }

    String createTicket() throws Exception {
        String response = mockMvc.perform(post("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validTicketJson()))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();
        return JsonPath.read(response, "$.id");
    }

    MockMultipartFile file(String name, String contentType, String content) {
        return new MockMultipartFile("file", name, contentType, content.getBytes());
    }
}

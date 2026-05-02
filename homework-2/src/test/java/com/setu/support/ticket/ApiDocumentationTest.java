package com.setu.support.ticket;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApiDocumentationTest extends ApiIntegrationTestSupport {
    @Test
    void swaggerUiIsAvailableAtApiDocs() throws Exception {
        mockMvc.perform(get("/api-docs"))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", containsString("/swagger-ui/index.html")));
    }

    @Test
    void openApiJsonDescribesTicketEndpoints() throws Exception {
        mockMvc.perform(get("/v3/api-docs").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.openapi").value("3.0.1"))
            .andExpect(jsonPath("$.info.title").value("Homework 2 Support Ticket API"))
            .andExpect(jsonPath("$.paths['/tickets']").exists())
            .andExpect(jsonPath("$.paths['/tickets/import']").exists())
            .andExpect(jsonPath("$.paths['/tickets/{id}']").exists());
    }
}

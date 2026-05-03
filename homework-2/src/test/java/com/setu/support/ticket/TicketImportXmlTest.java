package com.setu.support.ticket;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketImportXmlTest extends ApiIntegrationTestSupport {
    @Test
    void importsValidXmlDocument() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("tickets.xml", "application/xml", validXml())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_records").value(2))
            .andExpect(jsonPath("$.successful").value(2));
    }

    @Test
    void returnsXmlPartialFailures() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("tickets.xml", "application/xml", validXml().replace("bug_report", "defect"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.successful").value(1))
            .andExpect(jsonPath("$.failed").value(1))
            .andExpect(jsonPath("$.errors[0].field").value("category"));
    }

    @Test
    void rejectsMalformedXmlImport() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("tickets.xml", "application/xml", "<tickets><ticket></tickets>")))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("XML import must be a valid tickets document"));
    }

    @Test
    void reportsMissingXmlRequiredField() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("tickets.xml", "application/xml", validXml().replace("<customer_email>marie@example.com</customer_email>", ""))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.failed").value(1))
            .andExpect(jsonPath("$.errors[0].field").value("customer_email"));
    }

    @Test
    void reportsValidationErrorsForXmlWithoutTicketElements() throws Exception {
        mockMvc.perform(multipart("/tickets/import")
                .file(file("tickets.xml", "application/xml", "<tickets></tickets>")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_records").value(1))
            .andExpect(jsonPath("$.successful").value(0))
            .andExpect(jsonPath("$.failed").value(1))
            .andExpect(jsonPath("$.errors[*].field", hasSize(7)));
    }

    static String validXml() {
        return """
            <tickets>
              <ticket>
                <customer_id>CUST-20</customer_id>
                <customer_email>marie@example.com</customer_email>
                <customer_name>Marie Curie</customer_name>
                <subject>Bug report with dashboard</subject>
                <description>The dashboard chart fails to load after refreshing the page.</description>
                <category>bug_report</category>
                <priority>high</priority>
                <status>new</status>
                <assigned_to>agent-3</assigned_to>
                <tags><tag>dashboard</tag><tag>chart</tag></tags>
                <metadata>
                  <source>email</source>
                  <browser>Safari</browser>
                  <device_type>tablet</device_type>
                </metadata>
              </ticket>
              <ticket>
                <customer_id>CUST-21</customer_id>
                <customer_email>rosalind@example.com</customer_email>
                <customer_name>Rosalind Franklin</customer_name>
                <subject>Small cosmetic issue</subject>
                <description>The logo alignment is slightly off on mobile screens.</description>
                <category>technical_issue</category>
                <priority>low</priority>
                <status>new</status>
                <tags><tag>mobile</tag></tags>
                <metadata>
                  <source>web_form</source>
                  <browser>Chrome</browser>
                  <device_type>mobile</device_type>
                </metadata>
              </ticket>
            </tickets>
            """;
    }
}

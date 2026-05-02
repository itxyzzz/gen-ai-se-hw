package com.setu.support.ticket;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketPerformanceTest extends ApiIntegrationTestSupport {
    @Test
    void importsLargeCsvJsonAndXmlFixturesWithinThreshold() {
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            mockMvc.perform(multipart("/tickets/import")
                    .file(file("bulk.csv", "text/csv", bulkCsv(50))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful").value(50));

            repository.clear();

            mockMvc.perform(multipart("/tickets/import")
                    .file(file("bulk.json", "application/json", bulkJson(20))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful").value(20));

            repository.clear();

            mockMvc.perform(multipart("/tickets/import")
                    .file(file("bulk.xml", "application/xml", bulkXml(30))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful").value(30));
        });
    }

    static String bulkCsv(int count) {
        StringBuilder builder = new StringBuilder(TicketImportCsvTest.validCsv().lines().findFirst().orElseThrow()).append('\n');
        for (int index = 1; index <= count; index++) {
            builder.append("CUST-C").append(index)
                .append(",csv").append(index).append("@example.com,CSV Customer ")
                .append(index)
                .append(",CSV subject ")
                .append(index)
                .append(",This CSV generated description is long enough,other,medium,new,,agent-c,bulk,api,Chrome,desktop\n");
        }
        return builder.toString();
    }

    static String bulkJson(int count) {
        StringBuilder builder = new StringBuilder("[");
        for (int index = 1; index <= count; index++) {
            if (index > 1) {
                builder.append(',');
            }
            builder.append("""
                {
                  "customer_id": "CUST-J%s",
                  "customer_email": "json%s@example.com",
                  "customer_name": "JSON Customer %s",
                  "subject": "JSON subject %s",
                  "description": "This JSON generated description is long enough",
                  "category": "other",
                  "priority": "medium",
                  "status": "new",
                  "metadata": {"source": "api", "device_type": "desktop"}
                }
                """.formatted(index, index, index, index));
        }
        return builder.append(']').toString();
    }

    static String bulkXml(int count) {
        StringBuilder builder = new StringBuilder("<tickets>");
        for (int index = 1; index <= count; index++) {
            builder.append("""
                <ticket>
                  <customer_id>CUST-X%s</customer_id>
                  <customer_email>xml%s@example.com</customer_email>
                  <customer_name>XML Customer %s</customer_name>
                  <subject>XML subject %s</subject>
                  <description>This XML generated description is long enough</description>
                  <category>other</category>
                  <priority>medium</priority>
                  <status>new</status>
                  <metadata><source>api</source><device_type>desktop</device_type></metadata>
                </ticket>
                """.formatted(index, index, index, index));
        }
        return builder.append("</tickets>").toString();
    }
}

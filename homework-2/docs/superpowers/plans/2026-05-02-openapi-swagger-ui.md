# OpenAPI Swagger UI Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add Swagger UI to the Homework 2 API at `/api-docs` with generated OpenAPI JSON available for the UI and tests.

**Architecture:** Use Springdoc's WebMVC UI starter so the existing Spring MVC controller mappings produce an OpenAPI 3 document automatically. Keep the ticket API unchanged and add only configuration, one metadata bean, focused tests, and reviewer documentation updates.

**Tech Stack:** Java 17, Spring Boot 3.3.5, Spring MVC, Springdoc OpenAPI, JUnit 5, MockMvc, Maven.

---

## File Structure

- Create `homework-2/src/main/java/com/setu/support/OpenApiConfiguration.java`: provides API title, version, and description metadata.
- Create `homework-2/src/test/java/com/setu/support/ticket/ApiDocumentationTest.java`: MockMvc tests for `/api-docs` and `/v3/api-docs`.
- Modify `homework-2/pom.xml`: adds Springdoc starter dependency.
- Modify `homework-2/src/main/resources/application.properties`: maps Swagger UI to `/api-docs`.
- Modify `homework-2/README.md`, `homework-2/HOWTORUN.md`, and `homework-2/API_REFERENCE.md`: documents the new reviewer path.
- Modify `homework-2/CHANGELOG.md`: records the increment and verification.

### Task 1: Red Test For API Docs

**Files:**
- Create: `homework-2/src/test/java/com/setu/support/ticket/ApiDocumentationTest.java`

- [ ] **Step 1: Write the failing test**

```java
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
```

- [ ] **Step 2: Run the test and verify RED**

Run: `mvn -Dtest=ApiDocumentationTest test`

Expected: the test run fails because `/api-docs` and `/v3/api-docs` are not mapped before Springdoc is added.

### Task 2: Add Springdoc And Metadata

**Files:**
- Modify: `homework-2/pom.xml`
- Modify: `homework-2/src/main/resources/application.properties`
- Create: `homework-2/src/main/java/com/setu/support/OpenApiConfiguration.java`

- [ ] **Step 1: Add dependency**

Add this dependency inside `<dependencies>`:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

- [ ] **Step 2: Configure Swagger UI path**

Append this property:

```properties
springdoc.swagger-ui.path=/api-docs
```

- [ ] **Step 3: Add OpenAPI metadata bean**

```java
package com.setu.support;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    OpenAPI supportTicketOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title("Homework 2 Support Ticket API")
                .version("0.0.1-SNAPSHOT")
                .description("CRUD, filtering, and multi-format import API for support tickets."));
    }
}
```

- [ ] **Step 4: Run the focused test and verify GREEN**

Run: `mvn -Dtest=ApiDocumentationTest test`

Expected: `Tests run: 2, Failures: 0, Errors: 0`.

### Task 3: Update Reviewer Documentation

**Files:**
- Modify: `homework-2/README.md`
- Modify: `homework-2/HOWTORUN.md`
- Modify: `homework-2/API_REFERENCE.md`
- Modify: `homework-2/CHANGELOG.md`

- [ ] **Step 1: README quick start**

Add Swagger UI to the Quick Start section:

```markdown
Swagger UI is available at `http://localhost:8080/api-docs` while the API is running.
```

- [ ] **Step 2: HOWTORUN smoke checks**

Add this smoke check:

```bash
curl -I http://localhost:8080/api-docs
curl http://localhost:8080/v3/api-docs
```

- [ ] **Step 3: API reference documentation section**

Add this section near the top:

```markdown
## Interactive OpenAPI Documentation

Swagger UI is available at `http://localhost:8080/api-docs` when the application is running. The generated OpenAPI JSON document is available at `http://localhost:8080/v3/api-docs`.
```

- [ ] **Step 4: Changelog entry**

Add a new `Homework 2 - Step 2` entry describing the Swagger UI path, generated OpenAPI JSON, documentation updates, and verification commands.

### Task 4: Full Verification

**Files:**
- Verify the whole `homework-2` project.

- [ ] **Step 1: Run full Maven gate**

Run: `mvn clean verify`

Expected: Maven exits with `BUILD SUCCESS`, all tests pass, and the JaCoCo coverage gate remains above 85%.

- [ ] **Step 2: Review diff**

Run: `git diff -- homework-2`

Expected: diff is limited to OpenAPI/Swagger UI integration, docs, changelog, and Superpowers artifacts.

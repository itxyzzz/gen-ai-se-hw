# Support Ticket Import API Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use `superpowers:subagent-driven-development` or `superpowers:executing-plans` to implement this plan task-by-task.

**Goal:** Implement Homework 2 Task 1 as a Java 17 Spring Boot REST API for ticket CRUD and CSV/JSON/XML import.

**Architecture:** Standalone Spring Boot app in `homework-2`, using an in-memory repository, service-layer validation, format-specific import parsers, MockMvc tests, and homework-standard documentation.

**Tech Stack:** Java 17, Spring Boot 3.3.5, Maven, Jackson XML, Apache Commons CSV, JUnit 5, MockMvc, JaCoCo.

## Persistence Requirement

Save this full plan as:

`homework-2/docs/superpowers/plans/2026-05-02-support-ticket-import-api.md`

Also add a `homework-2/CHANGELOG.md` entry for Homework 2 Step 1 before committing implementation work.

## Public API

Implement:

- `POST /tickets`
- `POST /tickets/import`
- `GET /tickets`
- `GET /tickets/{id}`
- `PUT /tickets/{id}`
- `DELETE /tickets/{id}`

Use snake_case JSON names matching `TASKS.md`.

`POST /tickets/import` accepts `multipart/form-data` with:

- `file`: required CSV, JSON, or XML file
- `format`: optional `csv | json | xml`; infer from filename/content type when omitted

Filtering for `GET /tickets`:

- `category`
- `priority`
- `status`
- `customer_id`
- `customer_email`
- `assigned_to`
- `source`
- `tag`

## Implementation Tasks

### Task 1: Scaffold Spring Boot App

Create `homework-2/pom.xml` with Spring Boot 3.3.5, Java 17, web, validation, XML, Commons CSV, test, and JaCoCo dependencies.

Create:

- `src/main/java/com/setu/support/SupportTicketApplication.java`
- `src/test/java/com/setu/support/ticket/ApiIntegrationTestSupport.java`

Run:

`mvn test`

Expected: project compiles with empty or smoke tests.

### Task 2: Domain Model And DTOs

Create ticket domain types under `com.setu.support.ticket`:

- `Ticket`
- `TicketMetadata`
- `CreateTicketRequest`
- `UpdateTicketRequest`
- `TicketImportSummary`
- `TicketImportError`
- enums: `TicketCategory`, `TicketPriority`, `TicketStatus`, `TicketSource`, `DeviceType`

Required validation rules:

- `customer_id`, `customer_email`, `customer_name`, `subject`, `description`, `category`, `priority`, `status`, and `metadata.source` are required.
- Email must be valid.
- `subject`: 1-200 chars.
- `description`: 10-2000 chars.
- Enums must match assignment values.
- `resolved_at` is nullable; set when status becomes `resolved` or `closed` if absent.
- `created_at` and `updated_at` are server-managed.
- `assigned_to` nullable.
- `tags` defaults to empty list.
- `metadata.browser` nullable.
- `metadata.device_type` nullable but validated when present.

### Task 3: Repository, Service, And Errors

Create:

- `TicketRepository` backed by `ConcurrentHashMap<UUID, Ticket>`
- `TicketService`
- `TicketValidator`
- `ValidationException`
- `NotFoundException`
- `ApiErrorResponse`
- `ValidationErrorResponse`
- `GlobalExceptionHandler`

Error responses:

- Validation failures: `400` with field-level details.
- Malformed JSON/import files: `400` with meaningful sanitized message.
- Missing ticket: `404`.
- Unsupported media type: `415`.
- Unexpected server error: `500` without framework leakage.

### Task 4: CRUD Controller

Create `TicketController`.

Behavior:

- `POST /tickets`: validate and create, return `201`.
- `GET /tickets`: return all tickets or filtered list.
- `GET /tickets/{id}`: return ticket or `404`.
- `PUT /tickets/{id}`: replace editable fields, preserve `id` and `created_at`, update `updated_at`.
- `DELETE /tickets/{id}`: remove ticket, return `204`; unknown id returns `404`.

### Task 5: Import Service

Create:

- `TicketImportService`
- `CsvTicketParser`
- `JsonTicketParser`
- `XmlTicketParser`

Import behavior:

- CSV uses header row with snake_case field names.
- JSON accepts an array of ticket objects.
- XML accepts a root containing repeated ticket elements.
- Each record is validated independently.
- Successful records are created.
- Failed records appear in summary with record index, field when known, and message.
- Fully malformed files return `400`.
- Partial failures return `200` with failed count and errors.

Summary shape:

```json
{
  "total_records": 3,
  "successful": 2,
  "failed": 1,
  "created_ticket_ids": ["uuid"],
  "errors": [
    {
      "record": 2,
      "field": "customer_email",
      "message": "Email must be valid"
    }
  ]
}
```

### Task 6: Automated Tests

Create Task 3-inspired test suites scoped to Task 1:

- `TicketApiTest`: CRUD, filtering, status codes, malformed JSON.
- `TicketModelValidationTest`: required fields, email, lengths, enums, metadata, timestamps.
- `TicketImportCsvTest`: valid import, partial failure, malformed file, missing fields, invalid enum/email.
- `TicketImportJsonTest`: valid array, malformed JSON, partial failure, empty array, unknown fields.
- `TicketImportXmlTest`: valid XML, malformed XML, partial failure, missing required field, invalid enum.
- `TicketIntegrationTest`: create-update-filter-delete lifecycle and import-then-filter workflow.
- `TicketPerformanceTest`: import 50 CSV, 20 JSON, and 30 XML records within documented thresholds.

Coverage gate:

`mvn test jacoco:report`

Expected: tests pass and line coverage is greater than 85%.

### Task 7: Sample Data And Reviewer Assets

Create:

- `demo/sample_tickets.csv` with 50 records.
- `demo/sample_tickets.json` with 20 records.
- `demo/sample_tickets.xml` with 30 records.
- Invalid CSV/JSON/XML fixtures for negative tests.
- `demo/sample-requests.http`.
- `docs/support-ticket-api.postman_collection.json`.

Postman collection must cover:

- Create ticket.
- List tickets.
- Filter tickets.
- Get ticket by id.
- Update ticket.
- Delete ticket.
- Import CSV.
- Import JSON.
- Import XML.
- Partial import failure.
- Malformed import failure.
- Validation failure.

### Task 8: Demo Scripts

Create or update:

- `demo/run.sh`
- `demo/run.bat`
- `demo/start.ps1`
- `demo/stop.ps1`
- `demo/restart.ps1`

Scripts should run from `homework-2`, start the Spring Boot app on port `8080`, and write runtime logs under `target/`.

### Task 9: Documentation

Generate homework-standard docs:

- `README.md`: overview, features, setup, project structure, Mermaid architecture diagram, AI workflow notes.
- `HOWTORUN.md`: prerequisites, exact run/test commands, expected results, troubleshooting.
- `API_REFERENCE.md`: endpoint catalog, schemas, errors, cURL examples.
- `ARCHITECTURE.md`: component diagram, sequence/data flow diagrams, design decisions, security/performance notes.
- `TESTING_GUIDE.md`: automated test matrix, manual checklist, sample data guide, Postman instructions, coverage notes.
- `CHANGELOG.md`: Homework 2 Step 1 changes.

Include at least three Mermaid diagrams across README, ARCHITECTURE, and TESTING_GUIDE.

## Manual Test Instructions

Document a reviewer flow in `TESTING_GUIDE.md`:

1. Start the API with `demo/start.ps1` or `mvn spring-boot:run`.
2. Import the Postman collection.
3. Create a valid ticket.
4. List and filter tickets.
5. Get the created ticket by id.
6. Update status and assignment.
7. Delete the ticket.
8. Import CSV, JSON, and XML sample files.
9. Import invalid fixtures and confirm meaningful errors.
10. Run `mvn test jacoco:report` and verify coverage exceeds 85%.

## Verification

Final verification commands:

```bash
mvn test jacoco:report
mvn spring-boot:run
```

Manual verification:

- Run Postman collection against `http://localhost:8080`.
- Capture coverage evidence as `docs/screenshots/test_coverage.png`.
- Confirm documentation examples match actual responses.

## Assumptions

- Storage is in-memory only for part 1.
- Auto-classification is explicitly out of scope until Task 2.
- The app remains API-only; no frontend.
- Work stays on `homework-2-submission`.
- Each implementation increment updates `homework-2/CHANGELOG.md`.

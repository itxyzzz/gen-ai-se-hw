# Testing Guide

## Test Strategy

```mermaid
flowchart TB
    Unit[Validation parser and classifier checks] --> API[MockMvc endpoint tests]
    API --> Integration[Lifecycle import classify and filter workflows]
    Integration --> Manual[Postman and curl manual checks]
    Integration --> Coverage[JaCoCo coverage report]
```

## Automated Tests

Run:

```bash
cd homework-2
mvn test jacoco:report
```

Current automated suites:

| Test file | Coverage area |
| --- | --- |
| `TicketApiTest` | CRUD, filtering, status codes, malformed JSON |
| `TicketModelValidationTest` | required fields, email, lengths, enums, metadata, timestamp consistency |
| `TicketImportCsvTest` | CSV success, partial failure, malformed CSV, explicit format |
| `TicketImportJsonTest` | JSON success, partial failure, malformed JSON, empty array |
| `TicketImportXmlTest` | XML success, partial failure, malformed XML, missing required fields |
| `TicketCategorizationTest` | rule-based category, priority, confidence, reasoning, keyword evidence |
| `TicketClassificationApiTest` | explicit classify endpoint, create/import auto-classification, manual override evidence, decision logs |
| `TicketClassificationDisabledTest` | disabled auto-classification requires manual category and priority |
| `TicketIntegrationTest` | lifecycle and import-then-filter workflows |
| `TicketPerformanceTest` | 50 CSV, 20 JSON, and 30 XML imports within threshold |
| `ApiDocumentationTest` | Swagger UI and OpenAPI path coverage |

Coverage report:

`target/site/jacoco/index.html`

Capture the final coverage screenshot as:

`docs/screenshots/test_coverage.png`

## Test Data

| File | Purpose |
| --- | --- |
| `demo/sample_tickets.csv` | 50 valid CSV tickets |
| `demo/sample_tickets.json` | 20 valid JSON tickets |
| `demo/sample_tickets.xml` | 30 valid XML tickets |
| `demo/classification_tickets.csv` | classification-focused records covering categories and priorities |
| `demo/invalid_tickets.csv` | invalid rows for partial failure checks |
| `demo/invalid_tickets.json` | invalid records for partial failure checks |
| `demo/invalid_tickets.xml` | invalid records for partial failure checks |
| `demo/malformed_tickets.json` | malformed file for hard import failure checks |

## Manual Test Checklist

1. Start the API with `./demo/start.ps1` or `mvn spring-boot:run`.
2. Import `docs/support-ticket-api.postman_collection.json` into Postman.
3. Run `Create ticket`; confirm `201` and capture the returned `id`.
4. Run `Create ticket with auto-classification`; confirm category/priority are assigned and classifier evidence fields are present.
5. Run `Create ticket manual override wins`; confirm final category/priority use request values while `suggested_category`, `suggested_priority`, and `manual_override_applied` document the classifier suggestion.
6. Run `Explicit auto-classify existing ticket`; confirm the existing ticket is reclassified and receives confidence, reasoning, keywords, and `classified_at`.
7. Run `List tickets`; confirm created tickets appear.
8. Run `Filter tickets`; confirm filtered results only include matching category, priority, and source.
9. Run `Get ticket by id`; confirm it returns the stored ticket.
10. Run `Update ticket manual override`; confirm status changes, `resolved_at` is set for `resolved`, and manual override evidence is retained.
11. Run `Delete ticket`; confirm `204`, then run get-by-id and confirm `404`.
12. Run CSV, JSON, and XML imports with sample files; confirm successful counts match 50, 20, and 30.
13. Run `Import classification sample`; confirm records without category/priority are auto-classified.
14. Run invalid fixture imports; confirm `200` partial failure summaries with field-level errors.
15. Run malformed import failure; confirm `400` with a meaningful `Malformed import` message.
16. Run validation failure; confirm `400` with field-level validation details.
17. Run `mvn test jacoco:report`; confirm line coverage exceeds 85%. The latest clean verification run reported 93.55% line coverage.

## Performance Benchmark

| Scenario | Data size | Expected result |
| --- | ---: | --- |
| CSV import | 50 records | completes inside 5 seconds |
| JSON import | 20 records | completes inside 5 seconds |
| XML import | 30 records | completes inside 5 seconds |

The automated benchmark is intentionally small and stable for local reviewer machines.

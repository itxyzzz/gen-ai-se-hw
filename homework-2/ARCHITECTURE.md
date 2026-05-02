# Architecture

## Design Summary

Homework 2 Task 1 is a single Spring Boot API. It keeps data in memory to focus on API behavior, validation, import parsing, tests, and documentation. The design mirrors Homework 1's simple controller-service-repository style while adding import-specific parser components.

## Components

```mermaid
flowchart TB
    Controller[TicketController] --> TicketService[TicketService]
    Controller --> ImportService[TicketImportService]
    TicketService --> Validator[TicketValidator]
    TicketService --> Repository[TicketRepository: ConcurrentHashMap]
    ImportService --> Csv[CsvTicketParser]
    ImportService --> Json[JsonTicketParser]
    ImportService --> Xml[XmlTicketParser]
    ImportService --> Validator
    ImportService --> TicketService
    Advice[GlobalExceptionHandler] -. sanitized errors .-> Controller
```

## Create Ticket Flow

```mermaid
sequenceDiagram
    participant Client
    participant Controller as TicketController
    participant Service as TicketService
    participant Validator as TicketValidator
    participant Repo as TicketRepository
    Client->>Controller: POST /tickets
    Controller->>Service: create(request)
    Service->>Validator: validate(request)
    Validator-->>Service: ok or field errors
    Service->>Repo: save(ticket)
    Repo-->>Service: ticket
    Service-->>Controller: ticket
    Controller-->>Client: 201 Created
```

## Import Flow

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant ImportService
    participant Parser
    participant Validator
    participant TicketService
    Client->>Controller: POST /tickets/import multipart file
    Controller->>ImportService: importFile(file, format)
    ImportService->>Parser: parse CSV/JSON/XML
    Parser-->>ImportService: ticket requests
    loop each record
        ImportService->>Validator: validateFields(record)
        alt valid
            ImportService->>TicketService: create(record)
        else invalid
            ImportService-->>ImportService: add record error
        end
    end
    ImportService-->>Client: import summary
```

## Design Decisions

- **In-memory storage:** Keeps Task 1 focused and avoids database setup before the assignment asks for persistence.
- **Manual validation service:** Produces consistent field-level error responses for JSON requests and imported records.
- **Multipart imports:** Matches real file-upload usage and keeps sample data reusable in Postman.
- **Partial import success:** Valid records are saved even when other records fail validation.
- **Server-managed timestamps:** Clients cannot choose `created_at` or `updated_at`; terminal statuses get `resolved_at` automatically when omitted.

## Security Considerations

- Error responses are sanitized and do not expose stack traces.
- Upload size is capped at 5 MB in `application.properties`.
- Input is validated before storage.
- No authentication is implemented because it is outside Task 1 scope.

## Performance Considerations

- `ConcurrentHashMap` supports safe local concurrent access for this API-only implementation.
- Import parsing is in-memory and suitable for homework sample sizes.
- Performance test covers 50 CSV, 20 JSON, and 30 XML record imports within a five-second threshold.

## Known Limitations

- Data is lost when the process stops.
- Auto-classification, confidence scores, and decision logging are reserved for Task 2.
- No pagination is implemented because Task 1 only requires listing with filtering.

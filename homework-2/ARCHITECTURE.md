# Architecture

## Design Summary

Homework 2 is a single Spring Boot API with in-memory ticket storage. Task 1 provides CRUD, filtering, validation, CSV/JSON/XML import, and Swagger UI. Task 2 adds deterministic rule-based classification for category and priority, plus stored evidence and an in-memory decision log.

## Components

```mermaid
flowchart TB
    Controller[TicketController] --> TicketService[TicketService]
    Controller --> ImportService[TicketImportService]
    Controller --> OpenApi[Springdoc OpenAPI]
    TicketService --> Validator[TicketValidator]
    TicketService --> Classifier[TicketClassificationService]
    TicketService --> Properties[TicketClassificationProperties]
    TicketService --> DecisionLog[TicketClassificationDecisionLog]
    TicketService --> Repository[TicketRepository: ConcurrentHashMap]
    ImportService --> Csv[CsvTicketParser]
    ImportService --> Json[JsonTicketParser]
    ImportService --> Xml[XmlTicketParser]
    ImportService --> Validator
    ImportService --> TicketService
    Advice[GlobalExceptionHandler] -. sanitized errors .-> Controller
```

## Create And Classify Flow

```mermaid
sequenceDiagram
    participant Client
    participant Controller as TicketController
    participant Service as TicketService
    participant Validator as TicketValidator
    participant Classifier as TicketClassificationService
    participant Log as DecisionLog
    participant Repo as TicketRepository
    Client->>Controller: POST /tickets
    Controller->>Service: create(request)
    Service->>Validator: validate(request, classification-aware required fields)
    alt auto-classification enabled
        Service->>Classifier: classify(subject, description, tags)
        Classifier-->>Service: suggested category, priority, confidence, reasoning
        Service->>Log: record create decision
    end
    Service->>Repo: save(ticket with evidence)
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
        ImportService->>Validator: validateFields(record, import flags)
        alt valid
            ImportService->>TicketService: createFromImport(record)
        else invalid
            ImportService-->>ImportService: add record error
        end
    end
    ImportService-->>Client: import summary
```

## Explicit Classification Flow

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant TicketService
    participant Classifier
    participant Log
    participant Repo
    Client->>Controller: POST /tickets/{id}/auto-classify
    Controller->>TicketService: autoClassify(id)
    TicketService->>Repo: findById(id)
    TicketService->>Classifier: classify(existing ticket)
    TicketService->>Repo: save(ticket with classifier-applied category/priority)
    TicketService->>Log: record explicit_endpoint decision
    TicketService-->>Client: classification response
```

## Design Decisions

- **Rule-based classification:** Keeps Task 2 deterministic, transparent, and easy to test without external model credentials or nondeterministic responses.
- **Manual override with stored suggestion:** Existing Task 1 clients can keep sending category/priority; the API records the classifier suggestion for auditability.
- **Feature flags:** Create and import classification can be disabled independently, and manual override policy is separated by flow.
- **In-memory decision log:** Satisfies Task 2 decision logging for local homework scope without adding persistence.
- **Server-managed evidence:** Classification confidence, reasoning, keywords, suggestions, and timestamps are written by the server.

## Security Considerations

- Error responses are sanitized and do not expose stack traces.
- Upload size is capped at 5 MB in `application.properties`.
- Input is validated before storage.
- Classification is local string matching; ticket content is not sent to external services.
- No authentication is implemented because it is outside the current homework scope.

## Performance Considerations

- `ConcurrentHashMap` supports safe local concurrent access for this API-only implementation.
- Classification is linear text matching over a small keyword set and adds negligible overhead for homework sample sizes.
- Import parsing and classification are in-memory and suitable for the provided 50/20/30-record samples plus classification demo data.

## Known Limitations

- Data and classification logs are lost when the process stops.
- No database, authentication, pagination, or external model integration is included.
- Rule-based matching is explainable but less flexible than an LLM or hybrid classifier for ambiguous real-world tickets.

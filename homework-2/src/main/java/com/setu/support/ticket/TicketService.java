package com.setu.support.ticket;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketService {
    private final TicketRepository repository;
    private final TicketValidator validator;
    private final TicketClassificationService classificationService;
    private final TicketClassificationProperties classificationProperties;
    private final TicketClassificationDecisionLog decisionLog;
    private final Clock clock;

    public TicketService(
        TicketRepository repository,
        TicketValidator validator,
        TicketClassificationService classificationService,
        TicketClassificationProperties classificationProperties,
        TicketClassificationDecisionLog decisionLog
    ) {
        this.repository = repository;
        this.validator = validator;
        this.classificationService = classificationService;
        this.classificationProperties = classificationProperties;
        this.decisionLog = decisionLog;
        this.clock = Clock.systemUTC();
    }

    public Ticket create(CreateTicketRequest request) {
        return create(request, "create", classificationProperties.isAutoClassifyOnCreate(), classificationProperties.isAllowCreateManualOverride());
    }

    public Ticket createFromImport(CreateTicketRequest request) {
        return create(request, "import", classificationProperties.isAutoClassifyOnImport(), classificationProperties.isAllowImportManualOverride());
    }

    private Ticket create(CreateTicketRequest request, String trigger, boolean autoClassify, boolean allowManualOverride) {
        validator.validate(request, !autoClassify, !autoClassify);
        Instant now = Instant.now(clock);
        ClassificationSuggestion suggestion = autoClassify ? classificationService.classify(request) : null;
        String category = appliedValue(request.category(), suggestion == null ? null : suggestion.category(), allowManualOverride);
        String priority = appliedValue(request.priority(), suggestion == null ? null : suggestion.priority(), allowManualOverride);
        boolean manualOverrideApplied = autoClassify && allowManualOverride
            && (present(request.category()) || present(request.priority()));

        Ticket ticket = toTicket(
            UUID.randomUUID(),
            request,
            now,
            now,
            category,
            priority,
            suggestion,
            now,
            manualOverrideApplied
        );
        Ticket saved = repository.save(ticket);
        if (suggestion != null) {
            decisionLog.record(decision(saved, trigger, suggestion, manualOverrideApplied, now));
        }
        return saved;
    }

    public Ticket update(UUID id, UpdateTicketRequest request) {
        Ticket existing = findById(id);
        CreateTicketRequest createRequest = request == null ? null : request.asCreateRequest();
        validator.validate(createRequest);
        Instant now = Instant.now(clock);
        boolean manualOverrideApplied = existing.manualOverrideApplied()
            || !Objects.equals(existing.category(), createRequest.category())
            || !Objects.equals(existing.priority(), createRequest.priority());
        Ticket replacement = toTicket(
            existing.id(),
            createRequest,
            existing.createdAt(),
            now,
            createRequest.category(),
            createRequest.priority(),
            existing.classificationConfidence(),
            existing.classificationReasoning(),
            existing.classificationKeywords(),
            existing.suggestedCategory(),
            existing.suggestedPriority(),
            existing.classifiedAt(),
            manualOverrideApplied
        );
        return repository.save(replacement);
    }

    public ClassificationResponse autoClassify(UUID id) {
        Ticket existing = findById(id);
        Instant now = Instant.now(clock);
        ClassificationSuggestion suggestion = classificationService.classify(existing);
        Ticket replacement = toTicket(
            existing.id(),
            toCreateRequest(existing),
            existing.createdAt(),
            now,
            suggestion.category(),
            suggestion.priority(),
            suggestion,
            now,
            false
        );
        Ticket saved = repository.save(replacement);
        decisionLog.record(decision(saved, "explicit_endpoint", suggestion, false, now));
        return new ClassificationResponse(
            saved.category(),
            saved.priority(),
            suggestion.confidenceScore(),
            suggestion.reasoning(),
            suggestion.keywordsFound(),
            suggestion.category(),
            suggestion.priority(),
            false
        );
    }

    public Ticket findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Ticket not found"));
    }

    public List<Ticket> findFiltered(
        String category,
        String priority,
        String status,
        String customerId,
        String customerEmail,
        String assignedTo,
        String source,
        String tag
    ) {
        return repository.findAll().stream()
            .filter(ticket -> matches(ticket.category(), category))
            .filter(ticket -> matches(ticket.priority(), priority))
            .filter(ticket -> matches(ticket.status(), status))
            .filter(ticket -> matches(ticket.customerId(), customerId))
            .filter(ticket -> matches(ticket.customerEmail(), customerEmail))
            .filter(ticket -> matches(ticket.assignedTo(), assignedTo))
            .filter(ticket -> matches(ticket.metadata().source(), source))
            .filter(ticket -> tag == null || ticket.tags().contains(tag))
            .sorted((left, right) -> left.createdAt().compareTo(right.createdAt()))
            .collect(Collectors.toList());
    }

    public void delete(UUID id) {
        if (!repository.delete(id)) {
            throw new NotFoundException("Ticket not found");
        }
    }

    private Ticket toTicket(
        UUID id,
        CreateTicketRequest request,
        Instant createdAt,
        Instant updatedAt,
        String category,
        String priority,
        ClassificationSuggestion suggestion,
        Instant classifiedAt,
        boolean manualOverrideApplied
    ) {
        return toTicket(
            id,
            request,
            createdAt,
            updatedAt,
            category,
            priority,
            suggestion == null ? null : suggestion.confidenceScore(),
            suggestion == null ? null : suggestion.reasoning(),
            suggestion == null ? List.of() : suggestion.keywordsFound(),
            suggestion == null ? null : suggestion.category(),
            suggestion == null ? null : suggestion.priority(),
            suggestion == null ? null : classifiedAt,
            manualOverrideApplied
        );
    }

    private Ticket toTicket(
        UUID id,
        CreateTicketRequest request,
        Instant createdAt,
        Instant updatedAt,
        String category,
        String priority,
        Double classificationConfidence,
        String classificationReasoning,
        List<String> classificationKeywords,
        String suggestedCategory,
        String suggestedPriority,
        Instant classifiedAt,
        boolean manualOverrideApplied
    ) {
        Instant resolvedAt = request.resolvedAt();
        if (TicketStatus.isTerminal(request.status()) && resolvedAt == null) {
            resolvedAt = updatedAt;
        }
        return new Ticket(
            id,
            trimmed(request.customerId()),
            trimmed(request.customerEmail()),
            trimmed(request.customerName()),
            trimmed(request.subject()),
            trimmed(request.description()),
            trimmed(category),
            trimmed(priority),
            request.status(),
            createdAt,
            updatedAt,
            resolvedAt,
            blankToNull(request.assignedTo()),
            request.tags() == null ? List.of() : request.tags().stream().filter(Objects::nonNull).map(String::trim).filter(tag -> !tag.isBlank()).toList(),
            new TicketMetadata(
                request.metadata().source(),
                blankToNull(request.metadata().browser()),
                blankToNull(request.metadata().deviceType())
            ),
            classificationConfidence,
            classificationReasoning,
            classificationKeywords == null ? List.of() : classificationKeywords,
            suggestedCategory,
            suggestedPriority,
            classifiedAt,
            manualOverrideApplied
        );
    }

    private ClassificationDecision decision(
        Ticket ticket,
        String trigger,
        ClassificationSuggestion suggestion,
        boolean manualOverrideApplied,
        Instant decidedAt
    ) {
        return new ClassificationDecision(
            ticket.id(),
            trigger,
            suggestion.category(),
            suggestion.priority(),
            ticket.category(),
            ticket.priority(),
            suggestion.confidenceScore(),
            suggestion.reasoning(),
            suggestion.keywordsFound(),
            manualOverrideApplied,
            decidedAt
        );
    }

    private CreateTicketRequest toCreateRequest(Ticket ticket) {
        return new CreateTicketRequest(
            ticket.customerId(),
            ticket.customerEmail(),
            ticket.customerName(),
            ticket.subject(),
            ticket.description(),
            ticket.category(),
            ticket.priority(),
            ticket.status(),
            ticket.resolvedAt(),
            ticket.assignedTo(),
            ticket.tags(),
            ticket.metadata()
        );
    }

    private String appliedValue(String providedValue, String suggestedValue, boolean allowManualOverride) {
        if (allowManualOverride && present(providedValue)) {
            return trimmed(providedValue);
        }
        return suggestedValue == null ? trimmed(providedValue) : suggestedValue;
    }

    private boolean matches(String actual, String expected) {
        return expected == null || expected.equals(actual);
    }

    private String trimmed(String value) {
        return value == null ? null : value.trim();
    }

    private boolean present(String value) {
        return value != null && !value.isBlank();
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}

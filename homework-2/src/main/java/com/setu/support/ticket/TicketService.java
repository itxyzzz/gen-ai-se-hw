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
    private final Clock clock;

    public TicketService(TicketRepository repository, TicketValidator validator) {
        this.repository = repository;
        this.validator = validator;
        this.clock = Clock.systemUTC();
    }

    public Ticket create(CreateTicketRequest request) {
        validator.validate(request);
        Instant now = Instant.now(clock);
        Ticket ticket = toTicket(UUID.randomUUID(), request, now, now);
        return repository.save(ticket);
    }

    public Ticket update(UUID id, UpdateTicketRequest request) {
        Ticket existing = findById(id);
        CreateTicketRequest createRequest = request == null ? null : request.asCreateRequest();
        validator.validate(createRequest);
        Ticket replacement = toTicket(existing.id(), createRequest, existing.createdAt(), Instant.now(clock));
        return repository.save(replacement);
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

    private Ticket toTicket(UUID id, CreateTicketRequest request, Instant createdAt, Instant updatedAt) {
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
            request.category(),
            request.priority(),
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
            )
        );
    }

    private boolean matches(String actual, String expected) {
        return expected == null || expected.equals(actual);
    }

    private String trimmed(String value) {
        return value == null ? null : value.trim();
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}

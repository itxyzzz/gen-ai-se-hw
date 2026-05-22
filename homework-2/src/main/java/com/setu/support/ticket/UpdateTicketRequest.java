package com.setu.support.ticket;

import java.time.Instant;
import java.util.List;

public record UpdateTicketRequest(
    String customerId,
    String customerEmail,
    String customerName,
    String subject,
    String description,
    String category,
    String priority,
    String status,
    Instant resolvedAt,
    String assignedTo,
    List<String> tags,
    TicketMetadata metadata
) {
    CreateTicketRequest asCreateRequest() {
        return new CreateTicketRequest(
            customerId,
            customerEmail,
            customerName,
            subject,
            description,
            category,
            priority,
            status,
            resolvedAt,
            assignedTo,
            tags,
            metadata
        );
    }
}

package com.setu.support.ticket;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Ticket(
    UUID id,
    String customerId,
    String customerEmail,
    String customerName,
    String subject,
    String description,
    String category,
    String priority,
    String status,
    Instant createdAt,
    Instant updatedAt,
    Instant resolvedAt,
    String assignedTo,
    List<String> tags,
    TicketMetadata metadata
) {
}

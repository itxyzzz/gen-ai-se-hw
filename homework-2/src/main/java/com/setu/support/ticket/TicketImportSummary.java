package com.setu.support.ticket;

import java.util.List;
import java.util.UUID;

public record TicketImportSummary(
    int totalRecords,
    int successful,
    int failed,
    List<UUID> createdTicketIds,
    List<TicketImportError> errors
) {
}

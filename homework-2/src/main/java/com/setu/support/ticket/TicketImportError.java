package com.setu.support.ticket;

public record TicketImportError(int record, String field, String message) {
}

package com.setu.support.ticket;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ClassificationDecision(
    UUID ticketId,
    String trigger,
    String suggestedCategory,
    String suggestedPriority,
    String appliedCategory,
    String appliedPriority,
    double confidenceScore,
    String reasoning,
    List<String> keywordsFound,
    boolean manualOverrideApplied,
    Instant decidedAt
) {
}

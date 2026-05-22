package com.setu.support.ticket;

import java.util.List;

public record ClassificationResponse(
    String category,
    String priority,
    double confidenceScore,
    String reasoning,
    List<String> keywordsFound,
    String suggestedCategory,
    String suggestedPriority,
    boolean manualOverrideApplied
) {
}

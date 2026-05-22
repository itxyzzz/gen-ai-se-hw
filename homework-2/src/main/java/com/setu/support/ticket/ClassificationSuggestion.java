package com.setu.support.ticket;

import java.util.List;

public record ClassificationSuggestion(
    String category,
    String priority,
    double confidenceScore,
    String reasoning,
    List<String> keywordsFound
) {
}

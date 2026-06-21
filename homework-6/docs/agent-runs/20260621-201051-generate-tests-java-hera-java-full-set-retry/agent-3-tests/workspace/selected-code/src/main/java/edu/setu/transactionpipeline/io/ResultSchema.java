package edu.setu.transactionpipeline.io;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Set;

public final class ResultSchema {
    private static final Set<String> RESULT_KEYS = Set.of(
            "schema_version", "transaction_id", "status", "reason_codes", "amount", "currency",
            "processed_at", "risk_tier", "component_history_count", "audit_event_count", "settlement_reference");
    private static final Set<String> SUMMARY_KEYS = Set.of(
            "schema_version", "generated_at", "total", "settled", "rejected", "review_required",
            "error", "complete", "reason_code_counts");

    public void validateTransactionResult(JsonNode node) {
        requireKeys(node, RESULT_KEYS);
        if (!node.path("amount").isTextual()) {
            throw new IllegalArgumentException("Result amount must be a string.");
        }
        String status = node.path("status").asText();
        if (!Set.of("settled", "rejected", "review_required", "error").contains(status)) {
            throw new IllegalArgumentException("Unexpected result status.");
        }
    }

    public void validateSummary(JsonNode node) {
        requireKeys(node, SUMMARY_KEYS);
    }

    private static void requireKeys(JsonNode node, Set<String> requiredKeys) {
        for (String key : requiredKeys) {
            if (!node.has(key) && !"settlement_reference".equals(key)) {
                throw new IllegalArgumentException("Missing required result key.");
            }
        }
    }
}

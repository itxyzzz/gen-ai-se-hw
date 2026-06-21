package edu.setu.transactionpipeline.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ValidationReport(
        @JsonProperty("schema_version") String schemaVersion,
        @JsonProperty("generated_at") Instant generatedAt,
        @JsonProperty("total") int total,
        @JsonProperty("valid") int valid,
        @JsonProperty("invalid") int invalid,
        @JsonProperty("invalid_transaction_ids") List<String> invalidTransactionIds,
        @JsonProperty("reason_code_counts") Map<String, Integer> reasonCodeCounts) {
}

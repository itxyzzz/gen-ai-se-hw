package edu.setu.transactionpipeline.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Map;

public record PipelineSummary(
        @JsonProperty("schema_version") String schemaVersion,
        @JsonProperty("generated_at") Instant generatedAt,
        @JsonProperty("total") int total,
        @JsonProperty("settled") int settled,
        @JsonProperty("rejected") int rejected,
        @JsonProperty("review_required") int reviewRequired,
        @JsonProperty("error") int error,
        @JsonProperty("complete") boolean complete,
        @JsonProperty("reason_code_counts") Map<String, Integer> reasonCodeCounts) {
}

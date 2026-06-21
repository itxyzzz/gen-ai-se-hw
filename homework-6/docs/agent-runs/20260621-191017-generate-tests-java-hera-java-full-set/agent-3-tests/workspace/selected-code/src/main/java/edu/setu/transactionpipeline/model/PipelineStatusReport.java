package edu.setu.transactionpipeline.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record PipelineStatusReport(
        @JsonProperty("schema_version") String schemaVersion,
        @JsonProperty("generated_at") Instant generatedAt,
        @JsonProperty("ready") boolean ready,
        @JsonProperty("summary_path") String summaryPath,
        @JsonProperty("results_path") String resultsPath,
        @JsonProperty("total") int total,
        @JsonProperty("settled") int settled,
        @JsonProperty("rejected") int rejected,
        @JsonProperty("review_required") int reviewRequired,
        @JsonProperty("error") int error,
        @JsonProperty("complete") boolean complete) {
}

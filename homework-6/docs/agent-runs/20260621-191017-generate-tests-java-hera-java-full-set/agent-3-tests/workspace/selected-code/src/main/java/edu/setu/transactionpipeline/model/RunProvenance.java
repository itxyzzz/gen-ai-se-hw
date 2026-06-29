package edu.setu.transactionpipeline.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record RunProvenance(
        @JsonProperty("schema_version") String schemaVersion,
        @JsonProperty("runtime_run_id") String runtimeRunId,
        @JsonProperty("generated_at") Instant generatedAt,
        @JsonProperty("source_spec_run_id") String sourceSpecRunId,
        @JsonProperty("source_spec_path") String sourceSpecPath,
        @JsonProperty("source_spec_fingerprint") String sourceSpecFingerprint,
        @JsonProperty("pipeline_version_id") String pipelineVersionId,
        @JsonProperty("pipeline_inventory_path") String pipelineInventoryPath,
        @JsonProperty("pipeline_package_fingerprint") String pipelinePackageFingerprint) {
}

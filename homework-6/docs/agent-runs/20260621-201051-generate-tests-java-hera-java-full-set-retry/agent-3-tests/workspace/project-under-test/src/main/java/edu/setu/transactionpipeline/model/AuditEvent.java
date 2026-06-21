package edu.setu.transactionpipeline.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record AuditEvent(
        @JsonProperty("timestamp") Instant timestamp,
        @JsonProperty("component") String component,
        @JsonProperty("transaction_id") String transactionId,
        @JsonProperty("outcome") String outcome,
        @JsonProperty("reason_code") String reasonCode) {
}

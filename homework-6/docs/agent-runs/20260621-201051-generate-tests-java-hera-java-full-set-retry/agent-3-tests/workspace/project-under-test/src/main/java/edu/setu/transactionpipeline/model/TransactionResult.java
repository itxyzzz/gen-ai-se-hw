package edu.setu.transactionpipeline.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record TransactionResult(
        @JsonProperty("schema_version") String schemaVersion,
        @JsonProperty("transaction_id") String transactionId,
        @JsonProperty("status") ProcessingStatus status,
        @JsonProperty("reason_codes") List<String> reasonCodes,
        @JsonProperty("amount") String amount,
        @JsonProperty("currency") String currency,
        @JsonProperty("processed_at") Instant processedAt,
        @JsonProperty("risk_tier") RiskTier riskTier,
        @JsonProperty("component_history_count") int componentHistoryCount,
        @JsonProperty("audit_event_count") int auditEventCount,
        @JsonProperty("settlement_reference") String settlementReference) {
}

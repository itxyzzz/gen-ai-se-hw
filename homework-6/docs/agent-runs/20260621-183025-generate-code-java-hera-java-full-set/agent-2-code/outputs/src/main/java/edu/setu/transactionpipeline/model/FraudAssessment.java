package edu.setu.transactionpipeline.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FraudAssessment(
        @JsonProperty("risk_score") int riskScore,
        @JsonProperty("risk_tier") RiskTier riskTier,
        @JsonProperty("reason_codes") List<String> reasonCodes) {
}

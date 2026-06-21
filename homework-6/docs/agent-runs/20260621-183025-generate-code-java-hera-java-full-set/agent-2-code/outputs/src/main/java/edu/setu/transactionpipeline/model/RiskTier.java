package edu.setu.transactionpipeline.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RiskTier {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high"),
    VERY_HIGH("very_high");

    private final String jsonValue;

    RiskTier(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    @JsonValue
    public String jsonValue() {
        return jsonValue;
    }
}

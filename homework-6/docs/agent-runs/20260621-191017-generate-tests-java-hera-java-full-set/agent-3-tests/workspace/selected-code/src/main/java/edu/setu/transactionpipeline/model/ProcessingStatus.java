package edu.setu.transactionpipeline.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProcessingStatus {
    SETTLED("settled"),
    REJECTED("rejected"),
    REVIEW_REQUIRED("review_required"),
    ERROR("error"),
    VALIDATED("validated"),
    RISK_SCORED("risk_scored");

    private final String jsonValue;

    ProcessingStatus(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    @JsonValue
    public String jsonValue() {
        return jsonValue;
    }

    @JsonCreator
    public static ProcessingStatus fromJson(String value) {
        for (ProcessingStatus status : values()) {
            if (status.jsonValue.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unsupported processing status");
    }

    public boolean isTerminal() {
        return this == SETTLED || this == REJECTED || this == REVIEW_REQUIRED || this == ERROR;
    }
}

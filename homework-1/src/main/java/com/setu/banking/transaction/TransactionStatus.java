package com.setu.banking.transaction;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionStatus {
    PENDING("pending"),
    COMPLETED("completed"),
    FAILED("failed");

    private final String jsonValue;

    TransactionStatus(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    @JsonValue
    public String jsonValue() {
        return jsonValue;
    }
}

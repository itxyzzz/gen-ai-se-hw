package com.setu.banking.transaction;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum TransactionType {
    DEPOSIT("deposit"),
    WITHDRAWAL("withdrawal"),
    TRANSFER("transfer");

    private final String jsonValue;

    TransactionType(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    public static TransactionType fromValue(String value) {
        return Arrays.stream(values())
            .filter(type -> type.jsonValue.equalsIgnoreCase(value))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid transaction type"));
    }

    @JsonValue
    public String jsonValue() {
        return jsonValue;
    }
}

package edu.setu.transactionpipeline.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class TransactionRecord {
    private final String transactionId;
    private final String timestamp;
    private final String amountText;
    private final String currency;
    private final String transactionType;
    private final String channel;
    private final String country;
    private final boolean sourcePresent;
    private final boolean destinationPresent;
    private BigDecimal parsedAmount;

    public TransactionRecord(
            String transactionId,
            String timestamp,
            String amountText,
            String currency,
            String transactionType,
            String channel,
            String country,
            boolean sourcePresent,
            boolean destinationPresent) {
        this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.amountText = amountText;
        this.currency = currency;
        this.transactionType = transactionType;
        this.channel = channel;
        this.country = country;
        this.sourcePresent = sourcePresent;
        this.destinationPresent = destinationPresent;
    }

    @JsonProperty("transaction_id")
    public String transactionId() {
        return transactionId;
    }

    @JsonProperty("timestamp")
    public String timestamp() {
        return timestamp;
    }

    @JsonProperty("amount")
    public String amountText() {
        return amountText;
    }

    @JsonProperty("currency")
    public String currency() {
        return currency;
    }

    @JsonProperty("transaction_type")
    public String transactionType() {
        return transactionType;
    }

    @JsonProperty("channel")
    public String channel() {
        return channel;
    }

    @JsonProperty("country")
    public String country() {
        return country;
    }

    @JsonIgnore
    public boolean sourcePresent() {
        return sourcePresent;
    }

    @JsonIgnore
    public boolean destinationPresent() {
        return destinationPresent;
    }

    @JsonIgnore
    public BigDecimal parsedAmount() {
        return parsedAmount;
    }

    public void setParsedAmount(BigDecimal parsedAmount) {
        this.parsedAmount = parsedAmount;
    }
}

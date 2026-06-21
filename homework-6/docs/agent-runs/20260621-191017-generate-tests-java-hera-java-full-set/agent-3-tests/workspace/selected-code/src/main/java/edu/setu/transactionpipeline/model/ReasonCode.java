package edu.setu.transactionpipeline.model;

public final class ReasonCode {
    public static final String MISSING_REQUIRED_FIELD = "MISSING_REQUIRED_FIELD";
    public static final String INVALID_TIMESTAMP = "INVALID_TIMESTAMP";
    public static final String INVALID_AMOUNT = "INVALID_AMOUNT";
    public static final String NON_POSITIVE_AMOUNT = "NON_POSITIVE_AMOUNT";
    public static final String UNSUPPORTED_CURRENCY = "UNSUPPORTED_CURRENCY";
    public static final String HIGH_VALUE = "HIGH_VALUE";
    public static final String VERY_HIGH_VALUE = "VERY_HIGH_VALUE";
    public static final String ODD_HOUR = "ODD_HOUR";
    public static final String WIRE_TRANSFER_TYPE = "WIRE_TRANSFER_TYPE";
    public static final String CHANNEL_RISK = "CHANNEL_RISK";
    public static final String COUNTRY_RISK = "COUNTRY_RISK";
    public static final String PROCESSING_ERROR = "PROCESSING_ERROR";

    private ReasonCode() {
    }
}

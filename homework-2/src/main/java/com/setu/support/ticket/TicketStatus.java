package com.setu.support.ticket;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum TicketStatus {
    NEW("new"),
    IN_PROGRESS("in_progress"),
    WAITING_CUSTOMER("waiting_customer"),
    RESOLVED("resolved"),
    CLOSED("closed");

    private final String value;

    TicketStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static boolean contains(String value) {
        return valuesSet().contains(value);
    }

    public static boolean isTerminal(String value) {
        return RESOLVED.value.equals(value) || CLOSED.value.equals(value);
    }

    public static String allowedValues() {
        return String.join(", ", valuesSet());
    }

    private static Set<String> valuesSet() {
        return Arrays.stream(values()).map(TicketStatus::value).collect(Collectors.toSet());
    }
}

package com.setu.support.ticket;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum TicketPriority {
    URGENT("urgent"),
    HIGH("high"),
    MEDIUM("medium"),
    LOW("low");

    private final String value;

    TicketPriority(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static boolean contains(String value) {
        return valuesSet().contains(value);
    }

    public static String allowedValues() {
        return String.join(", ", valuesSet());
    }

    private static Set<String> valuesSet() {
        return Arrays.stream(values()).map(TicketPriority::value).collect(Collectors.toSet());
    }
}

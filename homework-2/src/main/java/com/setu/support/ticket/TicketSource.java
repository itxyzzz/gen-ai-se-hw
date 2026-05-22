package com.setu.support.ticket;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum TicketSource {
    WEB_FORM("web_form"),
    EMAIL("email"),
    API("api"),
    CHAT("chat"),
    PHONE("phone");

    private final String value;

    TicketSource(String value) {
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
        return Arrays.stream(values()).map(TicketSource::value).collect(Collectors.toSet());
    }
}

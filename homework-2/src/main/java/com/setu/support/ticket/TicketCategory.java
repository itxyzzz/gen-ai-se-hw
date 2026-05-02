package com.setu.support.ticket;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum TicketCategory {
    ACCOUNT_ACCESS("account_access"),
    TECHNICAL_ISSUE("technical_issue"),
    BILLING_QUESTION("billing_question"),
    FEATURE_REQUEST("feature_request"),
    BUG_REPORT("bug_report"),
    OTHER("other");

    private final String value;

    TicketCategory(String value) {
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
        return Arrays.stream(values()).map(TicketCategory::value).collect(Collectors.toSet());
    }
}

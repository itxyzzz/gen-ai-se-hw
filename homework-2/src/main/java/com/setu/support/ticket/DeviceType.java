package com.setu.support.ticket;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum DeviceType {
    DESKTOP("desktop"),
    MOBILE("mobile"),
    TABLET("tablet");

    private final String value;

    DeviceType(String value) {
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
        return Arrays.stream(values()).map(DeviceType::value).collect(Collectors.toSet());
    }
}

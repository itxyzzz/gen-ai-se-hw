package com.setu.banking.transaction;

import java.util.List;

public record ValidationErrorResponse(
    String error,
    List<FieldErrorDetail> details
) {
    public record FieldErrorDetail(String field, String message) {
    }
}

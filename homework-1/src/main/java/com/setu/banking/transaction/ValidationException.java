package com.setu.banking.transaction;

import java.util.List;

public class ValidationException extends RuntimeException {
    private final List<ValidationErrorResponse.FieldErrorDetail> details;

    public ValidationException(List<ValidationErrorResponse.FieldErrorDetail> details) {
        super("Validation failed");
        this.details = details;
    }

    public List<ValidationErrorResponse.FieldErrorDetail> details() {
        return details;
    }
}

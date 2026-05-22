package com.setu.support.ticket;

import java.util.List;

public class ValidationException extends RuntimeException {
    private final List<ValidationErrorResponse.FieldError> details;

    public ValidationException(List<ValidationErrorResponse.FieldError> details) {
        super("Validation failed");
        this.details = details;
    }

    public List<ValidationErrorResponse.FieldError> details() {
        return details;
    }
}

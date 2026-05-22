package com.setu.support.ticket;

import java.util.List;

public record ValidationErrorResponse(String error, List<FieldError> details) {
    public record FieldError(String field, String message) {
    }
}

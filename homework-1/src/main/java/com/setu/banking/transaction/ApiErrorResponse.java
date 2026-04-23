package com.setu.banking.transaction;

public record ApiErrorResponse(
    String error,
    String message
) {
}

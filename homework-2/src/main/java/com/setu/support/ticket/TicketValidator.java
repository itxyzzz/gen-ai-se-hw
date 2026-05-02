package com.setu.support.ticket;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class TicketValidator {
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public void validate(CreateTicketRequest request) {
        List<ValidationErrorResponse.FieldError> errors = validateFields(request);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public List<ValidationErrorResponse.FieldError> validateFields(CreateTicketRequest request) {
        List<ValidationErrorResponse.FieldError> errors = new ArrayList<>();
        if (request == null) {
            errors.add(new ValidationErrorResponse.FieldError("body", "Request body is required"));
            return errors;
        }

        required(errors, "customer_id", request.customerId());
        required(errors, "customer_email", request.customerEmail());
        required(errors, "customer_name", request.customerName());
        required(errors, "subject", request.subject());
        required(errors, "description", request.description());
        required(errors, "category", request.category());
        required(errors, "priority", request.priority());
        required(errors, "status", request.status());

        if (present(request.customerEmail()) && !EMAIL.matcher(request.customerEmail()).matches()) {
            errors.add(new ValidationErrorResponse.FieldError("customer_email", "Email must be valid"));
        }
        length(errors, "subject", request.subject(), 1, 200);
        length(errors, "description", request.description(), 10, 2000);
        enumValue(errors, "category", request.category(), TicketCategory.contains(request.category()), TicketCategory.allowedValues());
        enumValue(errors, "priority", request.priority(), TicketPriority.contains(request.priority()), TicketPriority.allowedValues());
        enumValue(errors, "status", request.status(), TicketStatus.contains(request.status()), TicketStatus.allowedValues());

        if (request.metadata() == null) {
            errors.add(new ValidationErrorResponse.FieldError("metadata.source", "Field is required"));
        } else {
            required(errors, "metadata.source", request.metadata().source());
            enumValue(errors, "metadata.source", request.metadata().source(), TicketSource.contains(request.metadata().source()), TicketSource.allowedValues());
            if (present(request.metadata().deviceType())) {
                enumValue(errors, "metadata.device_type", request.metadata().deviceType(), DeviceType.contains(request.metadata().deviceType()), DeviceType.allowedValues());
            }
        }

        if (present(request.status()) && TicketStatus.contains(request.status())
            && !TicketStatus.isTerminal(request.status()) && request.resolvedAt() != null) {
            errors.add(new ValidationErrorResponse.FieldError("resolved_at", "resolved_at is only allowed for resolved or closed tickets"));
        }

        return errors;
    }

    private void required(List<ValidationErrorResponse.FieldError> errors, String field, String value) {
        if (!present(value)) {
            errors.add(new ValidationErrorResponse.FieldError(field, "Field is required"));
        }
    }

    private void length(List<ValidationErrorResponse.FieldError> errors, String field, String value, int min, int max) {
        if (value == null) {
            return;
        }
        int length = value.length();
        if (length < min || length > max) {
            errors.add(new ValidationErrorResponse.FieldError(field, "Length must be between " + min + " and " + max + " characters"));
        }
    }

    private void enumValue(List<ValidationErrorResponse.FieldError> errors, String field, String value, boolean valid, String allowed) {
        if (present(value) && !valid) {
            errors.add(new ValidationErrorResponse.FieldError(field, "Value must be one of: " + allowed));
        }
    }

    private boolean present(String value) {
        return value != null && !value.isBlank();
    }
}

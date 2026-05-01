package com.setu.banking.transaction;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class TransactionValidator {
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("^ACC-[A-Za-z0-9]{5}$");
    private static final int MAX_ACCOUNT_LENGTH = 9;
    private static final int MAX_CURRENCY_LENGTH = 3;
    private static final int MAX_TYPE_LENGTH = 10;
    private static final Set<String> VALID_CURRENCIES = Set.of(
        "USD", "EUR", "GBP", "JPY", "CHF", "CAD", "AUD", "NZD",
        "SEK", "NOK", "DKK", "PLN", "HUF", "CZK"
    );

    public List<ValidationErrorResponse.FieldErrorDetail> validate(CreateTransactionRequest request) {
        List<ValidationErrorResponse.FieldErrorDetail> errors = new ArrayList<>();

        require(errors, "amount", request.amount());
        require(errors, "currency", request.currency());
        require(errors, "type", request.type());

        if (!errors.isEmpty()) {
            return errors;
        }

        validateAmount(errors, request.amount());
        validateCurrency(errors, request.currency());
        TransactionType transactionType = validateType(errors, request.type());

        if (transactionType == null) {
            validateAccountIfPresent(errors, "fromAccount", request.fromAccount());
            validateAccountIfPresent(errors, "toAccount", request.toAccount());
            return errors;
        }

        validateAccountsForType(errors, request, transactionType);

        return errors;
    }

    public void validateAccountId(String field, String accountId) {
        List<ValidationErrorResponse.FieldErrorDetail> errors = validateAccountIdDetails(field, accountId);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public List<ValidationErrorResponse.FieldErrorDetail> validateAccountIdDetails(String field, String accountId) {
        List<ValidationErrorResponse.FieldErrorDetail> errors = new ArrayList<>();
        require(errors, field, accountId);
        if (errors.isEmpty()) {
            validateAccount(errors, field, accountId);
        }
        return errors;
    }

    private void require(List<ValidationErrorResponse.FieldErrorDetail> errors, String field, Object value) {
        if (value == null || value instanceof String text && text.isBlank()) {
            errors.add(new ValidationErrorResponse.FieldErrorDetail(field, "Field is required"));
        }
    }

    private void validateAccount(List<ValidationErrorResponse.FieldErrorDetail> errors, String field, String account) {
        if (account.length() > MAX_ACCOUNT_LENGTH) {
            errors.add(new ValidationErrorResponse.FieldErrorDetail(field, "Account must match format ACC-XXXXX"));
            return;
        }
        if (!ACCOUNT_PATTERN.matcher(account).matches()) {
            errors.add(new ValidationErrorResponse.FieldErrorDetail(field, "Account must match format ACC-XXXXX"));
        }
    }

    private void validateAmount(List<ValidationErrorResponse.FieldErrorDetail> errors, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(new ValidationErrorResponse.FieldErrorDetail("amount", "Amount must be a positive number"));
            return;
        }
        if (amount.scale() > 2) {
            errors.add(new ValidationErrorResponse.FieldErrorDetail("amount", "Amount must have maximum 2 decimal places"));
        }
    }

    private void validateCurrency(List<ValidationErrorResponse.FieldErrorDetail> errors, String currency) {
        if (currency.length() > MAX_CURRENCY_LENGTH) {
            errors.add(new ValidationErrorResponse.FieldErrorDetail("currency", "Invalid currency code"));
            return;
        }
        if (!VALID_CURRENCIES.contains(currency.toUpperCase(Locale.ROOT))) {
            errors.add(new ValidationErrorResponse.FieldErrorDetail("currency", "Invalid currency code"));
        }
    }

    private TransactionType validateType(List<ValidationErrorResponse.FieldErrorDetail> errors, String type) {
        if (type.length() > MAX_TYPE_LENGTH) {
            errors.add(new ValidationErrorResponse.FieldErrorDetail("type", "Type must be deposit, withdrawal, or transfer"));
            return null;
        }
        try {
            return TransactionType.fromValue(type);
        } catch (IllegalArgumentException exception) {
            errors.add(new ValidationErrorResponse.FieldErrorDetail("type", "Type must be deposit, withdrawal, or transfer"));
            return null;
        }
    }

    private void validateAccountsForType(
        List<ValidationErrorResponse.FieldErrorDetail> errors,
        CreateTransactionRequest request,
        TransactionType transactionType
    ) {
        switch (transactionType) {
            case TRANSFER -> {
                require(errors, "fromAccount", request.fromAccount());
                require(errors, "toAccount", request.toAccount());

                validateAccountIfPresent(errors, "fromAccount", request.fromAccount());
                validateAccountIfPresent(errors, "toAccount", request.toAccount());

                if (request.fromAccount() != null
                    && request.toAccount() != null
                    && request.fromAccount().equals(request.toAccount())) {
                    errors.add(new ValidationErrorResponse.FieldErrorDetail("toAccount", "Transfer accounts must be different"));
                }
            }
            case DEPOSIT -> {
                require(errors, "toAccount", request.toAccount());
                rejectIfPresent(errors, "fromAccount", request.fromAccount(), "fromAccount is not allowed for deposit transactions");

                if (request.toAccount() != null && !request.toAccount().isBlank()) {
                    validateAccount(errors, "toAccount", request.toAccount());
                }
            }
            case WITHDRAWAL -> {
                require(errors, "fromAccount", request.fromAccount());
                rejectIfPresent(errors, "toAccount", request.toAccount(), "toAccount is not allowed for withdrawal transactions");

                if (request.fromAccount() != null && !request.fromAccount().isBlank()) {
                    validateAccount(errors, "fromAccount", request.fromAccount());
                }
            }
        }
    }

    private void rejectIfPresent(
        List<ValidationErrorResponse.FieldErrorDetail> errors,
        String field,
        String value,
        String message
    ) {
        if (value != null) {
            errors.add(new ValidationErrorResponse.FieldErrorDetail(field, message));
        }
    }

    private void validateAccountIfPresent(
        List<ValidationErrorResponse.FieldErrorDetail> errors,
        String field,
        String account
    ) {
        if (account != null && !account.isBlank()) {
            validateAccount(errors, field, account);
        }
    }
}

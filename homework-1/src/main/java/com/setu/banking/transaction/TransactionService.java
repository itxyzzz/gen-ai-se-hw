package com.setu.banking.transaction;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class TransactionService {
    private final TransactionRepository repository;
    private final TransactionValidator validator;

    public TransactionService(TransactionRepository repository, TransactionValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public Transaction create(CreateTransactionRequest request) {
        var errors = validator.validate(request);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        Transaction transaction = new Transaction(
            UUID.randomUUID().toString(),
            request.fromAccount(),
            request.toAccount(),
            request.amount(),
            request.currency().toUpperCase(Locale.ROOT),
            TransactionType.fromValue(request.type()),
            Instant.now(),
            TransactionStatus.COMPLETED
        );
        return repository.save(transaction);
    }

    public List<Transaction> findAll() {
        return repository.findAll();
    }

    public List<Transaction> findFiltered(String accountId, String type, String from, String to) {
        LocalDate fromDate = parseDateFilter("from", from);
        LocalDate toDate = parseDateFilter("to", to);
        TransactionType filterType = validateFilterParameters(accountId, type, fromDate, toDate);

        return repository.findAll().stream()
            .filter(transaction -> accountId == null
                || accountId.equals(transaction.fromAccount())
                || accountId.equals(transaction.toAccount()))
            .filter(transaction -> filterType == null || transaction.type() == filterType)
            .filter(transaction -> fromDate == null
                || !transaction.timestamp().atZone(ZoneOffset.UTC).toLocalDate().isBefore(fromDate))
            .filter(transaction -> toDate == null
                || !transaction.timestamp().atZone(ZoneOffset.UTC).toLocalDate().isAfter(toDate))
            .toList();
    }

    public Transaction findById(String id) {
        if (id.length() > 100) {
            throw new NotFoundException("Transaction not found");
        }
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Transaction not found"));
    }

    public BigDecimal balanceFor(String accountId) {
        validator.validateAccountId("accountId", accountId);

        return repository.findAll().stream()
            .filter(transaction -> transaction.status() == TransactionStatus.COMPLETED)
            .map(transaction -> balanceImpact(transaction, accountId))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public AccountSummaryResponse summaryFor(String accountId) {
        validator.validateAccountId("accountId", accountId);

        List<Transaction> accountTransactions = repository.findAll().stream()
            .filter(transaction -> accountId.equals(transaction.fromAccount()) || accountId.equals(transaction.toAccount()))
            .toList();

        BigDecimal deposits = accountTransactions.stream()
            .filter(transaction -> transaction.type() == TransactionType.DEPOSIT && accountId.equals(transaction.toAccount()))
            .map(Transaction::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal withdrawals = accountTransactions.stream()
            .filter(transaction -> transaction.type() == TransactionType.WITHDRAWAL && accountId.equals(transaction.fromAccount()))
            .map(Transaction::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Instant mostRecent = accountTransactions.stream()
            .map(Transaction::timestamp)
            .max(Instant::compareTo)
            .orElse(null);

        return new AccountSummaryResponse(
            accountId,
            deposits,
            withdrawals,
            accountTransactions.size(),
            mostRecent
        );
    }

    private BigDecimal balanceImpact(Transaction transaction, String accountId) {
        return switch (transaction.type()) {
            case DEPOSIT -> accountId.equals(transaction.toAccount()) ? transaction.amount() : BigDecimal.ZERO;
            case WITHDRAWAL -> accountId.equals(transaction.fromAccount()) ? transaction.amount().negate() : BigDecimal.ZERO;
            case TRANSFER -> {
                if (accountId.equals(transaction.fromAccount())) {
                    yield transaction.amount().negate();
                }
                if (accountId.equals(transaction.toAccount())) {
                    yield transaction.amount();
                }
                yield BigDecimal.ZERO;
            }
        };
    }

    private TransactionType validateFilterParameters(String accountId, String type, LocalDate from, LocalDate to) {
        List<ValidationErrorResponse.FieldErrorDetail> errors = new ArrayList<>();

        if (accountId != null) {
            errors.addAll(validator.validateAccountIdDetails("accountId", accountId));
        }

        TransactionType filterType = null;
        if (type != null) {
            try {
                filterType = TransactionType.fromValue(type);
            } catch (IllegalArgumentException exception) {
                errors.add(new ValidationErrorResponse.FieldErrorDetail("type", "Type must be deposit, withdrawal, or transfer"));
            }
        }

        if (from != null && to != null && from.isAfter(to)) {
            errors.add(new ValidationErrorResponse.FieldErrorDetail("from", "From date must be on or before to date"));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        return filterType;
    }

    private LocalDate parseDateFilter(String field, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException exception) {
            throw new ValidationException(List.of(
                new ValidationErrorResponse.FieldErrorDetail(field, "Date must use ISO format yyyy-MM-dd")
            ));
        }
    }
}

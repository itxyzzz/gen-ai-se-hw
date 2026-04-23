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
import java.util.Map;
import java.util.TreeMap;
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

    public AccountBalanceResponse balanceFor(String accountId) {
        validator.validateAccountId("accountId", accountId);

        Map<String, BigDecimal> balances = new TreeMap<>();

        repository.findAll().stream()
            .filter(transaction -> transaction.status() == TransactionStatus.COMPLETED)
            .forEach(transaction -> applyBalanceImpact(balances, transaction, accountId));

        balances.entrySet().removeIf(entry -> entry.getValue().compareTo(BigDecimal.ZERO) == 0);

        return new AccountBalanceResponse(accountId, balances);
    }

    public AccountSummaryResponse summaryFor(String accountId) {
        validator.validateAccountId("accountId", accountId);

        List<Transaction> accountTransactions = repository.findAll().stream()
            .filter(transaction -> transaction.status() == TransactionStatus.COMPLETED)
            .filter(transaction -> accountId.equals(transaction.fromAccount()) || accountId.equals(transaction.toAccount()))
            .toList();

        Map<String, BigDecimal> deposits = new TreeMap<>();
        Map<String, BigDecimal> withdrawals = new TreeMap<>();
        Map<String, BigDecimal> incomingTransfers = new TreeMap<>();
        Map<String, BigDecimal> outgoingTransfers = new TreeMap<>();

        accountTransactions.forEach(transaction -> accumulateSummaryTotals(
            transaction,
            accountId,
            deposits,
            withdrawals,
            incomingTransfers,
            outgoingTransfers
        ));

        Instant mostRecent = accountTransactions.stream()
            .map(Transaction::timestamp)
            .max(Instant::compareTo)
            .orElse(null);

        return new AccountSummaryResponse(
            accountId,
            deposits,
            withdrawals,
            incomingTransfers,
            outgoingTransfers,
            accountTransactions.size(),
            mostRecent
        );
    }

    private void applyBalanceImpact(Map<String, BigDecimal> balances, Transaction transaction, String accountId) {
        BigDecimal impact = switch (transaction.type()) {
            case DEPOSIT -> accountId.equals(transaction.toAccount()) ? transaction.amount() : BigDecimal.ZERO;
            case WITHDRAWAL -> accountId.equals(transaction.fromAccount()) ? transaction.amount().negate() : BigDecimal.ZERO;
            case TRANSFER -> {
                BigDecimal transferImpact = BigDecimal.ZERO;
                if (accountId.equals(transaction.fromAccount())) {
                    transferImpact = transferImpact.subtract(transaction.amount());
                }
                if (accountId.equals(transaction.toAccount())) {
                    transferImpact = transferImpact.add(transaction.amount());
                }
                yield transferImpact;
            }
        };

        if (impact.compareTo(BigDecimal.ZERO) != 0) {
            balances.merge(transaction.currency(), impact, BigDecimal::add);
        }
    }

    private void accumulateSummaryTotals(
        Transaction transaction,
        String accountId,
        Map<String, BigDecimal> deposits,
        Map<String, BigDecimal> withdrawals,
        Map<String, BigDecimal> incomingTransfers,
        Map<String, BigDecimal> outgoingTransfers
    ) {
        switch (transaction.type()) {
            case DEPOSIT -> {
                if (accountId.equals(transaction.toAccount())) {
                    addAmount(deposits, transaction.currency(), transaction.amount());
                }
            }
            case WITHDRAWAL -> {
                if (accountId.equals(transaction.fromAccount())) {
                    addAmount(withdrawals, transaction.currency(), transaction.amount());
                }
            }
            case TRANSFER -> {
                if (accountId.equals(transaction.fromAccount())) {
                    addAmount(outgoingTransfers, transaction.currency(), transaction.amount());
                }
                if (accountId.equals(transaction.toAccount())) {
                    addAmount(incomingTransfers, transaction.currency(), transaction.amount());
                }
            }
        }
    }

    private void addAmount(Map<String, BigDecimal> totals, String currency, BigDecimal amount) {
        totals.merge(currency, amount, BigDecimal::add);
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

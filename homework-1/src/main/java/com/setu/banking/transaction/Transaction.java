package com.setu.banking.transaction;

import java.math.BigDecimal;
import java.time.Instant;

public record Transaction(
    String id,
    String fromAccount,
    String toAccount,
    BigDecimal amount,
    String currency,
    TransactionType type,
    Instant timestamp,
    TransactionStatus status
) {
}

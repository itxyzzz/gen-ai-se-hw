package com.setu.banking.transaction;

import java.math.BigDecimal;

public record CreateTransactionRequest(
    String fromAccount,
    String toAccount,
    BigDecimal amount,
    String currency,
    String type
) {
}

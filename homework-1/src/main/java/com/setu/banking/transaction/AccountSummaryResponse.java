package com.setu.banking.transaction;

import java.math.BigDecimal;
import java.time.Instant;

public record AccountSummaryResponse(
    String accountId,
    BigDecimal totalDeposits,
    BigDecimal totalWithdrawals,
    long transactionCount,
    Instant mostRecentTransactionDate
) {
}
